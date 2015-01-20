/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.security.users.UserDTO;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.objectives.ObjectiveSetDto;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Sep-2006 09:20:51
 */
public class BrowseMyTeamController extends BrowseSubjectController {

    protected BrowseNodeWrapper createBrowseNodeWrapper(UserSession userSession, HttpServletRequest request) throws TalentStudioException {

        final Long userId = userSession.getId();
        final List<Node> results = new ArrayList<Node>(subjectService.findTeam(userId));
        BrowseNodeWrapper browseNodeWrapper = new SubjectBrowseNodeWrapper(null, results, null);
        final String attributeLabel = subjectService.getMyTeamViewAttributeLabel(userId);
        browseNodeWrapper.setCustomColumnLabel(attributeLabel);
        browseNodeWrapper.setHasCustomColumn(attributeLabel != null);
        browseNodeWrapper.setUserId(userId);
        browseNodeWrapper.setSearching(true);
        browseNodeWrapper.setObjectivesModifiable(true);

        browseNodeWrapper.setNodeType(Node.SUBJECT_UNIT_TYPE_);
        setDisplayInfo(browseNodeWrapper, request);

        return browseNodeWrapper;
    }

    /**
     * Overridden as the initial page for this controller will always be zero
     *
     * @param request the requet
     * @return 0
     */
    protected int getInitialPage(HttpServletRequest request) {
        return 0;
    }

    protected void updateNodeInfo(Node currentNode, BrowseNodeWrapper wrapper, UserSession userSession) throws TalentStudioException {
        super.updateNodeInfo(currentNode, wrapper, userSession);
        final Long subjectId = currentNode.getId();

        boolean objectivesViewable = wrapper.getContentView().isHasObjectivesView();
        if (objectivesViewable) {
            // assign the objectives, always visible to a manager
            SubjectWrapperBean subjectWrapper = (SubjectWrapperBean) wrapper.getNodeWrapper();
            subjectWrapper.setHasArchivedObjectives(objectiveService.hasArchivedObjectiveSets(subjectId));
            final ObjectiveSetDto currentObjectiveSet = subjectWrapper.getCurrentObjectiveSet();
            if(currentObjectiveSet != null) {

                subjectWrapper.setCurrentObjectives(objectiveService.findCurrentObjectives(subjectId));
                checkObjectiveStatus(wrapper);
                subjectWrapper.setHasApprovableAssessments(objectiveService.isHasApprovableAssessments(subjectId));
            } else {
                subjectWrapper.setHasApprovableAssessments(false);
            }
        }
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        Map<String, Object> refData = super.referenceData(request, command, errors, page);

        BrowseNodeWrapper nodeWrapper = (BrowseNodeWrapper) command;
        SubjectWrapperBean wrapper = (SubjectWrapperBean) nodeWrapper.getNodeWrapper();
        if (wrapper == null) return refData;
        Subject node = (Subject) nodeWrapper.getNode();
        if (node == null) return refData;

        boolean objectivesViewable = nodeWrapper.getContentView().isHasObjectivesView() && wrapper.isApproved() && !wrapper.isComplete();
        if (objectivesViewable) {
            List<UserDTO> allActiveUsers = userService.listAppUsers();
            User loggedInUser = ZynapWebUtils.getUser(request);
            allActiveUsers.remove(new UserDTO(loggedInUser.getId()));
            allActiveUsers.remove(new UserDTO(new Long(1)));
            if (node.getUser() != null) allActiveUsers.remove(new UserDTO(node.getUser().getId()));

            Collections.sort(allActiveUsers, new Comparator<UserDTO>() {
                public int compare(UserDTO o1, UserDTO o2) {
                    String secondName1 = o1.getSecondName().toUpperCase();
                    String secondName2 = o2.getSecondName().toUpperCase();
                    return secondName1.compareTo(secondName2);
                }
            });
            refData.put("assessors", allActiveUsers);
        }

        return refData;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    private IUserService userService;
}
