/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.tools;

import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeTypePredicate;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.exception.TalentStudioException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jan-2010 17:01:40
 */
public class QuestionnaireCalculator implements IQuestionnaireCalculator {

    public void calculateQuestions(Long questionnaireWorkflowId) throws TalentStudioException {

        final QuestionnaireWorkflow questionnaireWorkflow = workflowService.findWorkflowById(questionnaireWorkflowId);
        List<Questionnaire> questionnaires = questionnaireService.findQuestionnairesByWorkflow(questionnaireWorkflowId);

        QuestionnaireDefinition questionnaireDefinition = questionnaireWorkflow.getQuestionnaireDefinition();
        List<DynamicAttribute> dynamicAttributes = questionnaireDefinition.getDynamicAttributes();

        for (Questionnaire questionnaire : questionnaires) {
            try {
                calculate(questionnaire, dynamicAttributes);
                questionnaireService.update(questionnaire);
            } catch (TalentStudioException e) {
                e.printStackTrace();
            }
        }
    }

    public static void calculate(Questionnaire questionnaire, List<DynamicAttribute> dynamicAttributes) throws TalentStudioException {

        Collection sumAttributes = CollectionUtils.select(dynamicAttributes, new DynamicAttributeTypePredicate(new String[]{DynamicAttribute.DA_TYPE_SUM}));
        Collection mappingAttributes = CollectionUtils.select(dynamicAttributes, new DynamicAttributeTypePredicate(new String[]{DynamicAttribute.DA_TYPE_ENUM_MAPPING}));

        for (Iterator iterator = sumAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            String value = calculateSum(dynamicAttribute, questionnaire);
            addOrUpdateAttributeValue(questionnaire, dynamicAttribute, value);
        }

        for (Iterator iterator = mappingAttributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            List children = dynamicAttribute.getFirstLevelChildren();
            String value = determineMapping(children, null, questionnaire);
            addOrUpdateAttributeValue(questionnaire, dynamicAttribute, value);
        }
    }

    private static String calculateSum(DynamicAttribute dynamicAttribute, Questionnaire questionnaire) throws TalentStudioException {
        Integer total = null;
        boolean isBlank = true;

        List children = dynamicAttribute.getChildren();
        for (int j = 0; j < children.size(); j++) {
            DynamicAttributeReference dynamicAttributeReference = (DynamicAttributeReference) children.get(j);
            DynamicAttribute target = dynamicAttributeReference.getReferenceDa();
            AttributeValue answer = findTargetAnswer(target, questionnaire);
            // only do the summing if we have an answer
            if (answer != null) {
                String numberValue = answer.getCalculatableField();
                if(NumberUtils.isNumber(numberValue)) {
                    isBlank = false;
                    if(total == null) total = new Integer(0);
                    total += Integer.parseInt(numberValue);
                }
            }
        }

        return isBlank ? null : String.valueOf(total);
    }

    private static void addOrUpdateAttributeValue(Questionnaire questionnaire, DynamicAttribute dynamicAttribute, String value) {
        // do we already have an attribute value that needs updating
        AttributeValue existingValue = questionnaire.getDynamicAttributeValues().get(dynamicAttribute);
        if (StringUtils.hasText(value)) {
            if (existingValue == null) {
                existingValue = AttributeValue.create(value, dynamicAttribute);
                questionnaire.addAttributeValue(existingValue);
            } else {
                existingValue.initialiseNodeExtendedAttributes(value);
            }
        } else {
            if (existingValue != null) questionnaire.removeAttributeValue(existingValue);
        }
    }

    private static AttributeValue findTargetAnswer(DynamicAttribute target, Questionnaire questionnaire) {
        AttributeValue answer = null;
        Collection values = questionnaire.getDynamicAttributeValues().getValues();
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            AttributeValue attributeValue = (AttributeValue) iterator.next();
            if (attributeValue.getDynamicAttribute().equals(target)) {
                answer = attributeValue;
                break;
            }
        }
        return answer;
    }

    /**
     * Follows the mapping branches recursively testing for values in range and travelling down that branch until we have reached the leaves.
     *
     * @param children
     * @param value
     * @param questionnaire
     * @return
     */
    private static String determineMapping(List children, String value, Questionnaire questionnaire) {

        Integer result = null;
        for (int i = 0; i < children.size(); i++) {
            DynamicAttributeReference dynamicAttributeReference = (DynamicAttributeReference) children.get(i);
            if (i == 0) {
                // find the reference value
                DynamicAttribute target = dynamicAttributeReference.getReferenceDa();
                AttributeValue answer = findTargetAnswer(target, questionnaire);

                if (answer != null) {
                    String numberValue = answer.getCalculatableField();
                    result = new Integer(Integer.parseInt(numberValue));
                }
            }
            Integer lowerBounds = dynamicAttributeReference.getLowerBound() != null ? dynamicAttributeReference.getLowerBound() : new Integer(Integer.MIN_VALUE);
            Integer upperBounds = dynamicAttributeReference.getUpperBound() != null ? dynamicAttributeReference.getUpperBound() : new Integer(Integer.MAX_VALUE);

            // result must be within both if it is recurse with the children
            if (result != null) {
                if (result.intValue() >= lowerBounds.intValue() && result.intValue() <= upperBounds.intValue()) {
                    // if there are no children we have found our value
                    final LookupValue lookupValue = dynamicAttributeReference.getLookupValue();
                    String currentValue = lookupValue != null ? lookupValue.getId().toString() : null;
                    return determineMapping(dynamicAttributeReference.getChildren(), currentValue, questionnaire);
                }
            }
        }
        return value;
    }

    public void setWorkflowService(IQueWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    private IQueWorkflowService workflowService;
    private IQuestionnaireService questionnaireService;
}
