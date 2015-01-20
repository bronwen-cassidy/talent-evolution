/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.util.IFormatter;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Validates the questionnaire state before persistence. Is only used when adding a new questionnaire, as none of
 * these options are available for editing as the questionnaire workflow has by this time already been published and
 * changing the complete by date, label or population is no longer an option.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class QuestionnaireWorkflowValidator implements Validator {

    public boolean supports(Class clazz) {
        return QuestionnaireWorkflowWrapperBean.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        
        QuestionnaireWorkflowWrapperBean questionnaireWrapper = (QuestionnaireWorkflowWrapperBean) obj;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.label.required", "Label is a required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "populationId", "error.required.field", "This is is a required field");
        String desc = questionnaireWrapper.getDescription();
        if(StringUtils.hasText(desc)) {
            if(desc.length() >= 2500) {
                errors.rejectValue("description", "error.max.length.exceeded.2500");
            }
        }
        if(questionnaireWrapper.getQuestionnaireDefinition().getId() == null) {
            errors.rejectValue("questionnaireDefinition.id", "error.required.field");
        }
        final Date expiryDate = questionnaireWrapper.getExpiryDate();
        if (questionnaireWrapper.getId() == null) {
            if (expiryDate != null) {

                // compare expiry date to current date - use formatter to remove time so we only compare dates
                SimpleDateFormat formatter = new SimpleDateFormat(IFormatter.STORED_DATE_PATTERN);
                String dateValue = formatter.format(new Date());
                Date today = null;
                try {
                    today = formatter.parse(dateValue);
                } catch (ParseException e) {
                    // ignored will never happen
                }

                if (expiryDate.before(today))
                    errors.rejectValue("expiryDate", "error.complete.date.before.today", "Cannot set the complete by date before today");
            }
        }

        if(questionnaireWrapper.getGroupId() == null && StringUtils.hasText(questionnaireWrapper.getGroupLabel())) {
            String groupLabel = questionnaireWrapper.getGroupLabel();
            final List<Group> groupList = questionnaireWrapper.getGroups();
            for(Group grp : groupList) {
                if(grp.getLabel().equals(groupLabel)) {
                    errors.rejectValue("groupLabel", "error.duplicate.group.label", "Duplicate Group Label");
                    questionnaireWrapper.setGroupLabel(null);
                }
            }
        }

        // 	TS-2253: logic to ensure that if an infoform is configured for read, when a write permissions for manager or individual is also specified.
        if ((questionnaireWrapper.isIndividualRead() || questionnaireWrapper.isManagerRead()) && (!questionnaireWrapper.isIndividualWrite() && !questionnaireWrapper.isManagerWrite())) {
            errors.rejectValue("individualWrite", "error.field.writeRequired", "A write permission must be assigned if any read permissions have been specified.");
        }

    }
}
