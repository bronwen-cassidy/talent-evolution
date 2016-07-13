package com.zynap.talentstudio.workflow;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author Hibernate CodeGenerator
 */
public class Notification extends ZynapDomainObject {

    /**
     * default constructor
     */
    public Notification() {
    }

    public String getLabel() {
        return getWorkflowName();
    }

    public boolean isActionable() {
        return actionable;
    }

    public void setActionable(boolean actionable) {
        this.actionable = actionable;
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public int getActionsSize() {
        return 1;
    }

   
    public String getFirstAction() {
        return action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("beginDate", getBeginDate())
                .append("dueDate", getDueDate())
                .append("endDate", getEndDate())
                .append("status", getStatus())
                .toString();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getLauncherId() {
        return launcherId;
    }

    public void setLauncherId(Long launcherId) {
        this.launcherId = launcherId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getManagerInstanceId() {
        return managerInstanceId;
    }

    public void setManagerInstanceId(Long managerInstanceId) {
        this.managerInstanceId = managerInstanceId;
    }

    public Long getEvaluatorInstanceId() {
        return evaluatorInstanceId;
    }

    public void setEvaluatorInstanceId(Long evaluatorInstanceId) {
        this.evaluatorInstanceId = evaluatorInstanceId;
    }

    public Long getPerformanceReviewId() {
        return performanceReviewId;
    }

    public void setPerformanceReviewId(Long performanceReviewId) {
        this.performanceReviewId = performanceReviewId;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public LookupValue getRole() {
        return role;
    }

    public void setRole(LookupValue role) {
        this.role = role;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Integer getTarget() {
        // the target is determined by the current state and the next state of appraisals or questionnaires
        if(APPRAISAL_TYPE.equals(type)) {
            if(APPROVE.equals(action) || VERIFY.equals(action)) {
                return 6;
            }
            else if(Arrays.asList(appraisalActions).indexOf(action) < 2)
                return 5;
            else
                return 2;
        } else {
            return 2;
        }
    }

    /**
     * works through the list of potential actions and finds the next valid one due.
     *
     * @param currentAction the action step that is currently active
     * @param subType - whether this is the manager or evaluatee assement
     *
     * @return the next action in this workflow
     * @see Notification#appraisalActions
     */
    public static String getNextAction(String currentAction, String subType) {

        if (!CLOSE_ACTION.equals(currentAction)) {
            List<String> actions;
            if(MANAGER_ASSESSMENT_SUBTYPE.equals(subType) || EVALUATOR_ASSESSMENT_SUBTYPE.equals(subType)) {
                actions = Arrays.asList(appraisalActions);
            } else {
                actions = Arrays.asList(questionnaireActions);
            }
            return actions.get((actions.indexOf(currentAction) + 1));
        }
        return CLOSE_ACTION;
    }

    public Long getHrId() {
        return hrId;
    }

    public void setHrId(Long hrId) {
        this.hrId = hrId;
    }

    public Long getManagersManagerId() {
        return managersManagerId;
    }

    public void setManagersManagerId(Long managersManagerId) {
        this.managersManagerId = managersManagerId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isUserManaged() {
        return userManaged;
    }

    public void setUserManaged(boolean userManaged) {
        this.userManaged = userManaged;
    }

    public boolean isPerformanceReviewType() {
        return MANAGER_ASSESSMENT_SUBTYPE.equals(subType) || EVALUATOR_ASSESSMENT_SUBTYPE.equals(subType);
    }


    private Long managerId;
    private Long hrId;
    private Long managersManagerId;
    private Long managerInstanceId;
    private Long evaluatorInstanceId;
    private Long performanceReviewId;
    private String type;
    private String subType;

    private boolean approved;
    private boolean verified;

    /**
     * Status defines the current state of this notification object. The status will range through the following states
     * <ol><li>OPEN</li><li>COMPLETED</li></ol>
     */
    private String status;
    private Long subjectId;
    private String subjectName;
    private Long workflowId;
    private String roleId;
    private String roleName;
    private String workflowName;
    private Date beginDate;
    private Date dueDate;
    private Date endDate;

    /**
     * action defines the action this notification need to perform next. The action will range through states
     * determined by the appraisalActions or questionnaireActions
     */
    private String action;
    private boolean actionable;
    private Long launcherId;
    private Long recipientId;
    private Long rootId;
    private User recipient;
    private LookupValue role;
    private Subject subject;

    public static final String APPRAISAL_TYPE = "APPRAISAL";
    public static final String MANAGER_ASSESSMENT_SUBTYPE = "MANAGERASSESMENT";
    public static final String EVALUATOR_ASSESSMENT_SUBTYPE = "EVALUATOR_ASSESMENT";    
    public static final String QUESTIONNAIRE_TYPE = "QUESTIONNAIRE";

    public static final String ANSWER = "ANSWER";
    public static final String CLOSE_ACTION = "CLOSE";
    public static final String VERIFY = "VERIFY";
    public static final String VERIFIED = "VERIFIED";
    public static final String APPROVE = "APPROVE";
    public static final String APPROVED = "APPROVED";
    public static final String AWAITING_APPROVAL = "AWAITING_APPROVAL";

    public static final String[] appraisalActions = new String[] {"ASSIGN_ROLES", "START", ANSWER, AWAITING_APPROVAL, "COMPLETE", CLOSE_ACTION};
    public static final String[] questionnaireActions = new String[] {ANSWER, "COMPLETE", CLOSE_ACTION};

    private boolean userManaged;
}
