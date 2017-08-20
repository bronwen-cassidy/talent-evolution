/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.domain.admin.User;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Aug-2006 14:27:38
 */
public class QuestionCalculator {

    public static String calculate(DynamicAttribute root, IQuestionnaireService service, Long questionnaireId, User modifiedBy) throws TalentStudioException {

        String displayValue = null;
        String answer = null;
        if (root.isSumType()) {
            displayValue = calculateSum(root, service, questionnaireId);
            answer = displayValue;
        } else if (root.isMappingType()) {
            LookupValue lookupValue = determineMapping(root.getFirstLevelChildren(), null, service, questionnaireId);
            if (lookupValue != null) {
                displayValue = lookupValue.getLabel();
                answer = lookupValue.getId().toString();
            }
        }
        // check for an existing answer
        final List<NodeExtendedAttribute> attributeList = service.findAttributes(root.getId(), questionnaireId);
        if (attributeList.isEmpty()) {
            service.saveUpdateDeleteQuestionAttribute(questionnaireId, root.getId(), null, null, answer, null, modifiedBy);
        } else {
            for (NodeExtendedAttribute attribute : attributeList) {
                service.saveUpdateDeleteQuestionAttribute(questionnaireId, root.getId(), attribute.getId(), null, answer, null, modifiedBy);
            }
        }
        return displayValue;
    }

    private static String calculateSum(DynamicAttribute dynamicAttribute, IQuestionnaireService questionnaireService, Long queId) throws TalentStudioException {
        Integer total = null;
        boolean isBlank = true;

        List children = dynamicAttribute.getChildren();
        for (Object aChildren : children) {
            DynamicAttributeReference dynamicAttributeReference = (DynamicAttributeReference) aChildren;
            // the target is what will be coming from dwr
            DynamicAttribute target = dynamicAttributeReference.getReferenceDa();
            List<NodeExtendedAttribute> attributeList = questionnaireService.findAttributes(target.getId(), queId);

            // only do the summing if we have an answer
            if (attributeList != null && !attributeList.isEmpty()) {
                isBlank = false;
                String numberValue = getCalculatedField(target, attributeList);                
                if (NumberUtils.isNumber(numberValue)) {
                    if(total == null) total = new Integer(0);
                    total += Integer.parseInt(numberValue);
                }
            }
        }

        return isBlank ? null : total == null ? null : String.valueOf(total);
    }

    private static String getCalculatedField(DynamicAttribute dynamicAttribute, List<NodeExtendedAttribute> attributeList) {
        String numberValue;
        LookupType type = dynamicAttribute.getRefersToType();
        if (dynamicAttribute.isSelectionType()) {
            numberValue = getValue(type, attributeList.get(0));
        } else if (dynamicAttribute.isMultiSelectionType()) {
            double runningTotal = 0;
            for (NodeExtendedAttribute attribute : attributeList) {
                final String value = getValue(type, attribute);
                if( NumberUtils.isNumber(value)) runningTotal += Double.parseDouble(value);
            }
            numberValue = String.valueOf(runningTotal);
        } else {
            numberValue = attributeList.get(0).getValue();
        }
        return numberValue;
    }

    private static String getValue(LookupType type, NodeExtendedAttribute attribute) {
        String numberValue = " ";
        String value = attribute.getValue();
        if (StringUtils.hasText(value)) {
            LookupValue lookupValue = type.getLookupValue(new Long(value));
            numberValue = lookupValue.getValueId();
        }
        return numberValue;
    }

    private static LookupValue determineMapping(List children, LookupValue value, IQuestionnaireService questionnaireService, Long queId) {

        Integer result = null;
        for (int i = 0; i < children.size(); i++) {
            DynamicAttributeReference dynamicAttributeReference = (DynamicAttributeReference) children.get(i);
            if (i == 0) {
                // find the reference value
                DynamicAttribute target = dynamicAttributeReference.getReferenceDa();
                List<NodeExtendedAttribute> attributeList = questionnaireService.findAttributes(target.getId(), queId);

                if (attributeList != null && !attributeList.isEmpty()) {
                    String numberValue = getCalculatedField(target, attributeList);
                    if(NumberUtils.isNumber(numberValue)) result = Integer.parseInt(numberValue);
                }
            }
            Integer lowerBounds = dynamicAttributeReference.getLowerBound() != null ? dynamicAttributeReference.getLowerBound() : Integer.MIN_VALUE;
            Integer upperBounds = dynamicAttributeReference.getUpperBound() != null ? dynamicAttributeReference.getUpperBound() : Integer.MAX_VALUE;

            // result must be within both if it is recurse with the children
            if (result != null) {
                if (result >= lowerBounds && result <= upperBounds) {
                    // if there are no children we have found our value
                    final LookupValue lookupValue = dynamicAttributeReference.getLookupValue();
                    return determineMapping(dynamicAttributeReference.getChildren(), lookupValue, questionnaireService, queId);
                }
            }
        }
        return value;
    }
}
