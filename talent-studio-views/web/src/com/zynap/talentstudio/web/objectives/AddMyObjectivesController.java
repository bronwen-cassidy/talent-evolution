/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
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
public final class AddMyObjectivesController extends AddObjectivesController {

    /**
     * Get the subject the objective will be added to.
     * @param request the servlet http request
     * @return Subject of the user logged in
     * @throws com.zynap.exception.TalentStudioException
     */
    protected Subject getTargetSubject(HttpServletRequest request) throws TalentStudioException {
        return subjectService.findByUserId(ZynapWebUtils.getUserId(request));
    }

    /**
     * The managers of person logged in.
     * @param request the servlet http request
     * @param objectiveWrapperBean the form bean wrapper
     * @return the manager of the person logged in or null
     */
    protected User[] getRecipients(HttpServletRequest request, ObjectiveSetFormBean objectiveWrapperBean) {
        // staff member subject, need the boss
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
