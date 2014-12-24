package com.zynap.talentstudio.web.organisation.subjects;

import org.springframework.validation.Errors;

/**
 * User: amark
 * Date: 30-Mar-2006
 * Time: 14:24:16
 * Separate validator for adding / editing a user on an existing subject.
 */
public final class SubjectUserValidator extends SubjectValidator {

    public void validate(Object obj, Errors errors) {

        final SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) obj;

        // always validate user name
        validateUserName(subjectWrapperBean, errors);

        // only validate password for new user
        if (subjectWrapperBean.isNewUser()) {
            validatePassword(subjectWrapperBean, errors);
        }
    }
}
