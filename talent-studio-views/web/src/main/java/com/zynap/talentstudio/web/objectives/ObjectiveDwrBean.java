/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 26-Mar-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.objectives.IObjectiveService;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.security.users.IUserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Mar-2007 13:20:45
 */
public class ObjectiveDwrBean implements Serializable {

    public boolean approveObjective(Long objectiveId, Long userId) {

        try {
            Objective objective = objectiveService.findObjective(objectiveId);
            User user = (User) userService.findById(userId);
            objectiveService.approveObjective(objective, user);

        } catch (TalentStudioException e) {
            return false;
        }
        return true;
    }

    public void saveAssessors(Long objectiveId, Long[] userIds) {

        try {

            Objective objective = objectiveService.findObjective(objectiveId);
            objective.getAssessors().clear();

            if (userIds.length > 0) {
                List<User> users = userService.find(userIds);
                for (User user : users) {
                    objective.addAssessor(user);
                }
            }

            objectiveService.createOrUpdateAssessors(objective);

        } catch (TalentStudioException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> findAssessors(Long objectiveId) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        try {
            Objective objective = objectiveService.findObjective(objectiveId);
            List<User> users = new ArrayList<User>(objective.getAssessors());
            if (users.size() > 1) {
                Collections.sort(users, new Comparator<User>() {
                    public int compare(User o1, User o2) {
                        return o1.getCoreDetail().getSecondName().compareTo(o2.getCoreDetail().getSecondName());
                    }
                });
            }
            for(User user : users) {
                result.put(user.getId() + ":" + objectiveId, user.getDisplayName());
            }
        } catch (TalentStudioException e) {
            return result;
        }
        return result;
    }

    public void setObjectiveService(IObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IObjectiveService objectiveService;
    private IUserService userService;
}
