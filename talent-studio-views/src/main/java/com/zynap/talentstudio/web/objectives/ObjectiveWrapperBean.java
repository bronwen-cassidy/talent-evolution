/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveAssessment;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 24-May-2006 08:50:54
 */
public final class ObjectiveWrapperBean implements Serializable {

    public ObjectiveWrapperBean() {
    }

    public ObjectiveWrapperBean(Objective objective) {
        this.objective = objective;
        initObjective(objective);
    }

    public boolean isModifiable() {
        return !objective.isApproved();
    }

    public boolean isApproved() {
        return objective.isApproved();
    }

    public boolean isApprovedOrComplete() {
        return objective.isApprovedOrComplete();
    }

    public Long getObjectiveId() {
        return objective != null ? objective.getId() : null;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
        initObjective(objective);
    }

    private void initObjective(Objective objective) {
        Objective parent = objective.getParent();
        if(parent != null) {
            this.selectedParentId = parent.getId();
            this.parentDesc = parent.getDescription();
        }        
    }

    public Objective getObjective() {
        return objective;
    }

    public Objective getModifiedObjective() {
        if (!wrappedDynamicAttributes.isEmpty()) {
            DynamicAttributesHelper.assignAttributeValuesToNode(wrappedDynamicAttributes, objective);
        }
        return objective;
    }

    public void setWrappedDynamicAttributes(List<FormAttribute> wrappedDynamicAttributes) {
        this.wrappedDynamicAttributes = wrappedDynamicAttributes;
    }

    public List getWrappedDynamicAttributes() {
        return wrappedDynamicAttributes;
    }

    public Long getSelectedParentId() {
        return selectedParentId;
    }

    public void setSelectedParentId(Long selectedParentId) {
        this.selectedParentId = selectedParentId;
    }

    public String getParentDesc() {
        return parentDesc;
    }

    public void setParentDesc(String parentDesc) {
        this.parentDesc = parentDesc;
    }

    public String getLabel() {
        return objective.getLabel();
    }

    public String getDescription() {
        return objective.getDescription();
    }

    public void setLabel(String label) {
        if(StringUtils.hasText(label)) label = label.trim();
        objective.setLabel(label);
    }

    public void setDescription(String description) {
        objective.setDescription(description);
    }

    public String getManagerComments() {
        return objective.getComments();
    }

    public void setManagerComments(String value) {
        objective.setComments(value);
    }

    public Objective getParent() {
        return objective.getParent();
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public AssessmentFormBean getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentFormBean assessment) {
        this.assessment = assessment;
    }

    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public boolean isSendEmail() {
        return sendEmail;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public String getStatus() {
        return objective.getStatus();
    }

    public Long getId() {
        return objective.getId();
    }

    public List<AssessmentFormBean> getAssessments() {
        return assessments;
    }

    public List<User> getAssignedUsers() {
        return new ArrayList<User>(objective.getAssessors());    
    }

    public void setUsers(Long[] userIds) {
        this.userIds = userIds;
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public boolean isHasOtherAssessments() {
        return !assessments.isEmpty();
    }
    
    public Objective getModifiedObjectiveAssessment() {
        ObjectiveAssessment objectiveAssessment = assessment.getModifiedAssessment();
        // remove the assessment and then add it
        objective.removeAssessment(objectiveAssessment);        
        objective.addAssessment(objectiveAssessment);        
        return objective;
    }

    public void add(AssessmentFormBean assessmentFormBean) {
        if(assessmentFormBean.isManagerAssessment()) {
            assessment = assessmentFormBean;
        } else assessments.add(assessmentFormBean);
    }

    public void setInvalid(boolean invalid) {
        this.invalid = invalid;
    }

    public boolean isInvalid() {
        return invalid && getParentDesc() == null;
    }

    private Objective objective;
    private AssessmentFormBean assessment;
    // all assessments used in the manager/staff/manager appraisal views only 
    private List<AssessmentFormBean> assessments = new ArrayList<AssessmentFormBean>();
    private List<FormAttribute> wrappedDynamicAttributes = new ArrayList<FormAttribute>();
    private Long selectedParentId;
    private String parentDesc;
    private NodeInfo nodeInfo;
    private Long userId;
    private boolean sendEmail;
    private String activeTab = "objectives";
    private Long[] userIds;
    private boolean invalid;
}
