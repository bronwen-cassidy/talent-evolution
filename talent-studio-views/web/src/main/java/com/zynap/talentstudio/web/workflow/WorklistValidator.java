/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.workflow;

import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationUtils;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.questionnaires.DynamicQuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper;
import com.zynap.talentstudio.performance.PerformanceEvaluator;

import org.springframework.validation.Errors;
import org.springframework.validation.BindException;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class WorklistValidator extends NodeValidator {

    public boolean supports(Class clazz) {
        return QuestionnaireWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        // ignore this method
    }

    public void validateAnswersOnSave(QuestionnaireWrapper wrapper, Errors errors) {
        validateAnswers(wrapper, errors, false);
    }

    public void validateAnswersOnComplete(QuestionnaireWrapper wrapper, Errors errors) {
        validateAnswers(wrapper, errors, true);
    }

    private void validateAnswers(QuestionnaireWrapper wrapper, Errors errors, boolean checkMandatory) {

        final List wrappedDynamicAttributes = wrapper.getWrappedDynamicAttributes();
        for (int i = 0; i < wrappedDynamicAttributes.size(); i++) {

            final FormAttribute formAttribute = (FormAttribute) wrappedDynamicAttributes.get(i);

            // if dynamic line item question
            if (formAttribute instanceof DynamicQuestionAttributeWrapperBean) {

                // validate children
                final DynamicQuestionAttributeWrapperBean questionAttributeWrapperBean = (DynamicQuestionAttributeWrapperBean) formAttribute;
                final List questionWrappers = questionAttributeWrapperBean.getQuestionWrappers();
                for (int j = 0; j < questionWrappers.size(); j++) {
                    final QuestionAttributeWrapperBean attributeWrapperBean = (QuestionAttributeWrapperBean) questionWrappers.get(j);
                    final String prefix = NodeValidator.ATTRIBUTE_WRAPPERS_PREFIX + "[" + i + "].questionWrappers";
                    validateAnswer(attributeWrapperBean, errors, prefix, j, checkMandatory);
                }

            } else if (formAttribute instanceof QuestionAttributeWrapperBean) {

                // validate question
                final QuestionAttributeWrapperBean questionAttributeWrapperBean = (QuestionAttributeWrapperBean) formAttribute;
                validateAnswer(questionAttributeWrapperBean, errors, NodeValidator.ATTRIBUTE_WRAPPERS_PREFIX, i, checkMandatory);
            }
        }
    }

    private void validateAnswer(QuestionAttributeWrapperBean attributeWrapperBean, Errors errors, String prefix, int index, boolean checkMandatory) {
        AttributeValueValidationUtils.validateAttribute(factory, attributeWrapperBean, errors, prefix, index, null, checkMandatory, null);
    }

    public void validatePerformanceRoles(WorklistWrapper wrapper, List<PerformanceEvaluator> performanceEvaluators, Errors errors) {
        if(wrapper.getSelectedManagerId() == null && noPerformanceEvaluators(performanceEvaluators)) {
            errors.reject("error.evaluator.required", "At least one evaluator must be selected");
        }
        int index = 0;
        for (PerformanceEvaluator performanceEvaluator : performanceEvaluators) {
            if(performanceEvaluator.isSelfEvaluator()) continue;
            if(performanceEvaluator.getRole().getId() == null) errors.rejectValue("performanceRoles[" + index + "].performanceRoleId", "error.evaluator.role.required", "The evaluator must have a role");
            if(performanceEvaluator.getUser() == null) errors.rejectValue("performanceRoles[" + index + "].performerId", "error.evaluator.required", "An evaluator is required");
            index++;
        }
    }

    private boolean noPerformanceEvaluators(List<PerformanceEvaluator> performanceEvaluators) {
        int numUsersSelected = 0;
        for (PerformanceEvaluator performanceEvaluator : performanceEvaluators) {
            if(performanceEvaluator.getUser() != null) {
                numUsersSelected++;
            }
        }
        return numUsersSelected == 0;
    }
}