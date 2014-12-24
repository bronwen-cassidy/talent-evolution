package com.zynap.talentstudio.web.questionnaires.definition;

import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.questionnaires.QuestionGroupWrapper;
import com.zynap.talentstudio.web.questionnaires.QuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.LineItemWrapper;
import com.zynap.talentstudio.web.questionnaires.NarrativeAttributeWrapperBean;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: amark
 * Date: 15-Aug-2005
 * Time: 13:30:24
 */
public class QuestionnaireDefinitionValidator implements Validator {

    public boolean supports(Class clazz) {
        return QuestionnaireDefinitionWrapper.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field");

        List<String> groupNames = new ArrayList<String>();
        QuestionnaireDefinitionWrapper questionnaireDefinitionWrapper = (QuestionnaireDefinitionWrapper) obj;
        String queDefLabel = questionnaireDefinitionWrapper.getLabel();
        
        if(StringUtils.hasText(queDefLabel) && queDefLabel.length() > MAX_LABEL_LENGTH) {
            errors.rejectValue("label", "error.max.length.exceeded." + MAX_LABEL_LENGTH);
        }
        List groups = questionnaireDefinitionWrapper.getQuestionnaireGroups();

        for (int i = 0; i < groups.size(); i++) {

            QuestionGroupWrapper questionGroupWrapper = (QuestionGroupWrapper) groups.get(i);
            final String groupLabel = questionGroupWrapper.getLabel();
            final String maxLengthErrorMessage = "error.max.length.exceeded.";

            if(!StringUtils.hasText(groupLabel)) {
                errors.rejectValue("questionnaireGroups[" + i + "].label", "error.label.required", "Label is a required field");
            }

            if (groupLabel != null && groupLabel.length() > MAX_GROUP_LABEL_LENGTH) {
                errors.rejectValue("questionnaireGroups[" + i + "].label", maxLengthErrorMessage + MAX_GROUP_LABEL_LENGTH);
            }

            if (groupNames.contains(groupLabel)) {
                errors.rejectValue("questionnaireGroups[" + i + "].label", "error.unique.questionnaire.group", "group names must be unique you have given a previous group this name");
            }
            groupNames.add(groupLabel);

            List attributes = questionGroupWrapper.getWrappedDynamicAttributes();

            for (int j = 0; j < attributes.size(); j++) {

                FormAttribute formAttribute = (FormAttribute) attributes.get(j);
                String label = formAttribute.getLabel();

                final String prefix = "questionnaireGroups[" + i + "].wrappedDynamicAttributes[" + j + "]";
                final String field = prefix + ".label";

                if (formAttribute instanceof QuestionAttributeWrapperBean) {
                    validateQuestionLabel(errors, field, label, maxLengthErrorMessage);

                } else if (formAttribute instanceof LineItemWrapper) {
                    if (label != null && label.length() > MAX_GROUP_LABEL_LENGTH) {
                        errors.rejectValue(field, maxLengthErrorMessage + MAX_GROUP_LABEL_LENGTH);
                    }
                    // now validate the line item
                    QuestionAttributeWrapperBean[][] questionAttributeWrapperBeans = ((LineItemWrapper) formAttribute).getGrid();
                    for (int k = 0; k < questionAttributeWrapperBeans.length; k++) {
                        QuestionAttributeWrapperBean questionAttributeWrapperBean = questionAttributeWrapperBeans[k][0];
                        String lineItemLabel = questionAttributeWrapperBean.getLineItemLabel();
                        if (lineItemLabel != null && lineItemLabel.length() > MAX_GROUP_LABEL_LENGTH) {
                            errors.rejectValue(prefix + ".grid[" + k + "][0].lineItemLabel", maxLengthErrorMessage + MAX_GROUP_LABEL_LENGTH);
                        }
                    }
                    
                    for (int l = 0; l < questionAttributeWrapperBeans[0].length; l++) {
                        validateQuestionLabel(errors, prefix + ".grid[0][" + l + "].label", label, maxLengthErrorMessage);
                    }

                } else if (formAttribute instanceof NarrativeAttributeWrapperBean) {
                    ValidationUtils.rejectIfEmpty(errors, field, "error.required.field");
                    if (label != null && label.length() > MAX_GROUP_LABEL_LENGTH) {
                        errors.rejectValue(field, maxLengthErrorMessage + MAX_GROUP_LABEL_LENGTH);
                    }
                }
            }
        }
    }

    private void validateQuestionLabel(Errors errors, String field, String label, String maxLengthErrorMessage) {
        ValidationUtils.rejectIfEmpty(errors, field, "error.empty.label");
        if (label != null && label.length() > MAX_LABEL_FIELD_LENGTH) {
            errors.rejectValue(field, maxLengthErrorMessage + MAX_LABEL_FIELD_LENGTH);
        }
    }

    private static final int MAX_LABEL_FIELD_LENGTH = 500;
    private static final int MAX_GROUP_LABEL_LENGTH = 2000;
    private static final int MAX_LABEL_LENGTH = 1000;
}
