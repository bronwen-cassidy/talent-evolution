/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import com.zynap.exception.TalentStudioException;
import com.zynap.domain.admin.User;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * Controller that adds objectives to self.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 13:14:20
 */
public final class EditMyObjectivesController extends DefaultObjectivesController {

    /**
     * Get the subject the objective will be added to.
     *
     * @param request
     * @return Subject
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    Subject getTargetSubject(HttpServletRequest request) throws TalentStudioException {
        return subjectService.findByUserId(ZynapWebUtils.getUserId(request));
    }

    /**
     *
     * @param request
     * @param objectiveWrapperBean
     * @return the managers for the logged in user.
     */
    protected User[] getRecipients(HttpServletRequest request, ObjectiveSetFormBean objectiveWrapperBean) {
        Subject subject;
        try {
            subject = getTargetSubject(request);
        } catch (TalentStudioException e) {
            return null;
        }
        final List<User> managers = subject.getManagers();
        return managers.toArray(new User[managers.size()]);
    }
}
