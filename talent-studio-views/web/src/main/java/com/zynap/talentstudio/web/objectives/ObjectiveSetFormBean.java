/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveDefinition;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.organisation.OrganisationUnit;

import java.io.Serializable;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Mar-2007 09:23:50
 */

public class ObjectiveSetFormBean implements Serializable {

    public void setObjectiveSet(ObjectiveSet objectiveSet) {
        this.objectiveSet = objectiveSet;
    }

    public ObjectiveSet getModifiedObjectiveSet() {

        objectiveSet.getObjectives().clear();

        for (ObjectiveWrapperBean wrapperBean : objectives) {
            Objective newObjective = wrapperBean.getModifiedObjective();
            newObjective.setAppearInTodo(isAppearInTodo());

            Long parentId = wrapperBean.getSelectedParentId();
            if (parentId != null) {
                Objective parent = getParent(parentId);
                newObjective.setParent(parent);
                // todo verify if the parent can ever be null
                //if (parent != null) parent.addChild(newObjective);

            } /*else {
                // get the previous parent and remove the child
                Objective parent = newObjective.getParent();
                if (parent != null) parent.removeObjective(newObjective);
            }*/
            if (assessmentsApproved) {
                newObjective.setStatus(ObjectiveConstants.STATUS_COMPLETE);
            } else {
                newObjective.setStatus(approveObjectives ? ObjectiveConstants.STATUS_APPROVED : ObjectiveConstants.STATUS_OPEN);
            }
            objectiveSet.addObjective(newObjective);
        }

        // the objectives process has a number of states and actions(state transitions) this method assigns the phase we are at.
        assignProcessProgress();

        objectiveSet.setApproved(approveObjectives);
        objectiveSet.setLastModifiedDate(new Date());
        return objectiveSet;
    }

    private void assignProcessProgress() {

        String status;
        String group = null;
        String action = null;
        if (assessmentsApproved) {
            // no group and no action as the process has completed
            status = ObjectiveConstants.STATUS_COMPLETE;

        } else if (pendingManagerAction()) {
            status = ObjectiveConstants.STATUS_PENDING;
            group = ObjectiveConstants.ACTION_GROUP_MANAGER;
            action = ObjectiveConstants.ACTION_REQUIRED_APPROVE;

        } else if (pendingIndividualAction()) {
            status = ObjectiveConstants.STATUS_PENDING;
            group = ObjectiveConstants.ACTION_GROUP_INDIVIDUAL;
            action = ObjectiveConstants.ACTION_REQUIRED_REVIEW;

        } else {
            status = approveObjectives ? ObjectiveConstants.STATUS_APPROVED : ObjectiveConstants.STATUS_PENDING;
            group = personnalObjectives ? ObjectiveConstants.ACTION_GROUP_INDIVIDUAL : ObjectiveConstants.ACTION_GROUP_MANAGER;
            action = personnalObjectives ? ObjectiveConstants.ACTION_REQUIRED_REVIEW : ObjectiveConstants.ACTION_REQUIRED_APPROVE;
        }
        objectiveSet.setStatus(status);
        objectiveSet.setActionGroup(group);
        objectiveSet.setActionRequired(action);
    }

    private boolean pendingIndividualAction() {
        return !personnalObjectives && sendReview;
    }

    private boolean pendingManagerAction() {
        return personnalObjectives && sendReview;
    }

    /**
     * Gets the selected parent object fro the given id
     *
     * @param parentId the parent objectvie
     * @return the parent objective if found, null otherwise
     */
    private Objective getParent(Long parentId) {
        // the publishedCorporateObjectiveSet will be null when we are adding subject objectives
        // as they only use the organisation objectives not corporate
        if (publishedCorporateObjectiveSet != null) {
            List<Objective> corpObjectives = publishedCorporateObjectiveSet.getObjectives();
            for (Objective objective : corpObjectives) {
                if (parentId.equals(objective.getId())) return objective;
            }
        }
        for (ObjectiveSet orgUnitObjective : organisationObjectiveSets) {
            List<Objective> objectiveList = orgUnitObjective.getObjectives();
            for (Objective objective : objectiveList) {
                if (parentId.equals(objective.getId())) return objective;
            }
        }
        return null;
    }

    public Long getSubjectId() {
        return objectiveSet != null ? objectiveSet.getSubject() != null ? objectiveSet.getSubject().getId() : null : null;
    }

    public void addObjective(ObjectiveWrapperBean wrapperBean) {
        objectives.add(wrapperBean);
    }

    /**
     * Set the current set of wrapped objectives that are being worked on
     *
     * @param objectives
     */
    public void setObjectives(List<ObjectiveWrapperBean> objectives) {
        this.objectives = objectives;
    }

    public List<ObjectiveWrapperBean> getObjectives() {
        return objectives;
    }

    public ObjectiveDefinition getObjectiveDefinition() {
        return objectiveSet.getObjectiveDefinition();
    }

    public void removeObjective(int deleteIndex) {
        objectives.remove(deleteIndex);
    }

    public boolean isHasOtherAssessments() {
        for (ObjectiveWrapperBean wrapper : objectives) {
            if (wrapper.isHasOtherAssessments()) return true;
        }
        return false;
    }

    public boolean isPersonnalObjectives() {
        return personnalObjectives;
    }

    public void setPersonnalObjectives(boolean personnalObjectives) {
        this.personnalObjectives = personnalObjectives;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public boolean isApproveObjectives() {
        return approveObjectives;
    }

    public void setApproveObjectives(boolean approveObjectives) {
        this.approveObjectives = approveObjectives;
    }

    public boolean isAssessmentsApproved() {
        return assessmentsApproved || objectiveSet.isComplete();
    }

    public void setAssessmentsApproved(boolean assessmentsApproved) {
        this.assessmentsApproved = assessmentsApproved;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public boolean isApproved() {
        return objectives.iterator().next().isApproved();
    }

    public boolean isApprovedOrComplete() {
        return objectiveSet.isApprovedOrComplete();
    }

    public void setOrganisationUnitId(Long organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public Long getOrganisationUnitId() {
        return organisationUnit != null ? organisationUnit.getId() : organisationUnitId;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void deleteObjective(int deleteIndex) {
        objectives.remove(deleteIndex);
    }

    public ObjectiveSet getPublishedCorporateObjectiveSet() {
        return publishedCorporateObjectiveSet;
    }

    public void setPublishedCorporateObjectiveSet(ObjectiveSet publishedCorporateObjectiveSet) {
        this.publishedCorporateObjectiveSet = publishedCorporateObjectiveSet;
    }

    public void setArchivedObjectiveSets(List archivedSets) {
        archivedObjectiveSets = archivedSets;
    }

    public List getArchivedObjectiveSets() {
        return archivedObjectiveSets;
    }

    public ObjectiveSet getObjectiveSet() {
        return objectiveSet;
    }

    public void setObjectiveSets(Collection<ObjectiveSet> objectiveSets) {
        archivedObjectiveSets = new ArrayList<ObjectiveSet>(objectiveSets);
    }

    public List getObjectiveSets() {
        return archivedObjectiveSets;
    }

    public void setHasCurrentObjectives(boolean hasCurrentObjectives) {
        this.hasCurrentObjectives = hasCurrentObjectives;
    }

    public boolean isHasCurrentObjectives() {
        return hasCurrentObjectives;
    }


    public boolean isAppearInTodo() {
        return this.objectiveSet.isAppearInTodo();
    }

    public void setAppearInTodo(boolean appearInTodo) {
        this.objectiveSet.setAppearInTodo(appearInTodo);
    }

    public boolean isSendReview() {
        return sendReview;
    }

    public List<AssessmentFormBean> getAssessments() {
        List<AssessmentFormBean> assessments = new ArrayList<AssessmentFormBean>();
        for (ObjectiveWrapperBean wrapper : objectives) {
            assessments.addAll(wrapper.getAssessments());
        }
        return assessments;
    }

    public void setSendReview(boolean sendReview) {
        this.sendReview = sendReview;
    }

    public void addOrganisationObjectiveSet(ObjectiveSet set) {
        this.organisationObjectiveSets.add(set);
    }

    public List<ObjectiveSet> getOrganisationObjectiveSets() {
        return organisationObjectiveSets;
    }

    private ObjectiveSet publishedCorporateObjectiveSet;
    private ObjectiveSet objectiveSet;
    private List<ObjectiveWrapperBean> objectives = new ArrayList<ObjectiveWrapperBean>();
    private boolean personnalObjectives = false;
    private NodeInfo nodeInfo;
    private Long userId;
    private boolean sendEmail;
    private boolean approveObjectives;
    private boolean assessmentsApproved;
    private String activeTab = "objectives";
    private Long organisationUnitId;
    private OrganisationUnit organisationUnit;
    private List archivedObjectiveSets;
    private boolean hasCurrentObjectives;
    private boolean sendReview = false;
    private List<ObjectiveSet> organisationObjectiveSets = new ArrayList<ObjectiveSet>();
}
