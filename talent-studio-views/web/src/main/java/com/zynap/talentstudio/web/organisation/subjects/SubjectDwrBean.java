/*
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.exception.TalentStudioException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 08-May-2007 16:14:28
 */
public class SubjectDwrBean {

    private Subject findSubject(Long id) {
        try {
            return subjectService.findById(id);
        }
        catch (TalentStudioException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return null;
    }

    public String getFullName(Long id) {

        Subject subject = findSubject(id);
        if(subject == null) return "";
        return subject.getLabel();
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private ISubjectService subjectService;
    private static final Log logger = LogFactory.getLog(SubjectDwrBean.class);
}