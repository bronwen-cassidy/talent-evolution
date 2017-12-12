package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.common.lookups.LookupValue;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO class that represents the Questionnaire object - used when fetching list of questionnaires for portfolio.
 * <p/>
 * User: amark
 * Date: 02-Oct-2006
 * Time: 15:45:44
 */
public class QuestionnaireDTO implements Serializable, Comparable {

    /**
     * Constructor used when loading questionnaire workflows.
     *
     * @param workflowId     the id of the workflow
     * @param label          the label of the workflow
     * @param workflowType   the type of workflow i.e. INFO_FORM
     * @param status         the current status of the workflow i.e. {@link com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow#STATUS_COMPLETED }
     * @param completeByDate the date the questionnaire is to be completed by.
     * @param closedDate     the date the workflow was closed.
     */
    public QuestionnaireDTO(Long workflowId, String label, String workflowType, String status, Date completeByDate, Date closedDate, boolean individualWrite, boolean managerWrite) {

        this.managerWrite = managerWrite;
        this.individualWrite = individualWrite;
        this.workflowId = workflowId;
        this.label = label;
        this.workflowType = workflowType;
        this.completeByDate = completeByDate;
        this.closedDate = closedDate;
        this.status = status;
    }

    public QuestionnaireDTO(Long workflowId, String label, String workflowType, String status, Date completeByDate, Date closedDate, boolean individualWrite, boolean managerWrite, String groupLabel) {

        this(workflowId, label, workflowType, status, completeByDate, closedDate, individualWrite, managerWrite);
        this.groupLabel = groupLabel;
    }

    /**
     * Constructor used to load questionnaires or appraisals.
     *
     * @param id             the questionnaire id
     * @param workflowId     the worklfow id
     * @param label          the workflow/questionnaire's label
     * @param workflowType   the workflow type i.e. INFO_FORM
     * @param status          completed, in progress open, not started
     * @param completeByDate the date the workflow is to be completed (may be null i.e. never completes)
     * @param completedDate  the date the questionnaire was completed (may be null i.e. not yet completed)
     * @param role           the role, if this is an appraisal.
     */
    public QuestionnaireDTO(Long id, Long workflowId, String label, String workflowType, String status, Date completeByDate, boolean individualWrite, boolean managerWrite, Date completedDate, LookupValue role) {

        this.id = id;
        this.workflowId = workflowId;
        this.label = label;
        this.workflowType = workflowType;
        this.status = status;
        this.individualWrite = individualWrite;
        this.managerWrite = managerWrite;
        this.completedDate = completedDate;
        this.completeByDate = completeByDate;
        this.role = role;
    }

    public QuestionnaireDTO(boolean queStatus, Long id, Long workflowId, String label, String workflowType, String status, Date completeByDate, boolean individualWrite, boolean managerWrite, Date completedDate, LookupValue role, String groupLabel) {

        this(id, workflowId, label, workflowType, status, completeByDate, individualWrite, managerWrite, completedDate, role);
        this.groupLabel = groupLabel;
        this.isCompleted = queStatus;
    }

    public Long getId() {
        return id;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public String getLabel() {
        return label;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public boolean isManagerAppraisal() {
        return QuestionnaireWorkflow.TYPE_MANAGER_APPRAISAL.equals(workflowType);
    }

    public Date getCompleteByDate() {
        return completeByDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public LookupValue getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public String getProgress() {
        if (isHasAnswers()) {
            if (isOpen()) {
                return IN_PROGRESS;
            } else {
                return COMPLETED;
            }
        } else {
            if (isOpen()) {
                return NOT_STARTED;
            } else if (isCompleted()) {
                return NO_ANSWERS;
            } else {
                return NOT_COMPLETED;
            }
        }
    }

    public boolean isCompleted() {
        return QuestionnaireWorkflow.STATUS_COMPLETED.equals(status);
    }

    public boolean isNotificationBased() {
        return QuestionnaireWorkflow.isNotificationBased(workflowType);
    }

    public boolean isHasAnswers() {
        return getId() != null;
    }

    public boolean isViewable() {
        return !QuestionnaireWorkflow.TYPE_INFO_FORM.equals(workflowType) || isHasAnswers() || isOpen();
    }

    public boolean isEditable() {
        return isOpen() && !QuestionnaireWorkflow.isNotificationBased(workflowType);
    }

    public boolean isIndividualWrite() {
        return individualWrite;
    }

    public boolean isManagerWrite() {
        return managerWrite;
    }

    private boolean isOpen() {
        if(QuestionnaireWorkflow.TYPE_QUESTIONNAIRE.equals(workflowType) && isCompleted != null) return !isCompleted;
        return status.equals(QuestionnaireWorkflow.STATUS_OPEN) || status.equals(QuestionnaireWorkflow.STATUS_PENDING) || status.equals(QuestionnaireWorkflow.STATUS_PUBLISHED);
    }

    public int compareTo(Object o) {

        final QuestionnaireDTO other = (QuestionnaireDTO) o;

        int comparison = this.workflowId.compareTo(other.getWorkflowId());

        if (isAnyAppraisal()) {

            // only check roles for same workflow
            if (comparison == 0) {
                final LookupValue otherRole = other.getRole();
                if (otherRole == null) {
                    comparison = this.role != null ? 1 : 0;
                } else if (this.role == null) {
                    comparison = -1;
                } else {
                    comparison = role.compareBySortOrder(otherRole);
                }
            }
        } else {
            String tempLabel = label == null ? "" : label;
            comparison += tempLabel.compareTo(other.label);
        }

        return comparison;
    }


    public boolean equals(Object command) {
        if (this == command) return true;
        if (!(command instanceof QuestionnaireDTO)) return false;

        final QuestionnaireDTO other = (QuestionnaireDTO) command;
        if (QuestionnaireWorkflow.TYPE_INFO_FORM.equals(workflowType)) {
	        return workflowId.equals(other.workflowId);
        } else {
            // this is a questionnaire or appraisal, always have ids by the time they get into portfolios
	        return id != null ? id.equals(other.id) : other.id == null;
        }
    }

    public int hashCode() {
        int result;
        if (QuestionnaireWorkflow.TYPE_INFO_FORM.equals(workflowType)) {
            result = workflowId.hashCode();
        } else {
            // this is a questionnaire or appraisal, always have ids by the time they get into portfolios
            result = id != null ? id.hashCode() : 31;
        }
        return result;
    }

    public boolean isPerformanceReview() {
        return role != null;
    }

    public boolean isAnyAppraisal() {
        return isPerformanceReview() || isManagerAppraisal();
    }

    public void setId(Long questionnaireId) {
        this.id = questionnaireId;
    }

    public boolean isArchived() {
        return Questionnaire.STATUS_ARCHIVED.equals(status);
    }

    private Long id;
    private final Long workflowId;
    private String label;
    private final String workflowType;
    private Boolean isCompleted = null;
    private String groupLabel;
    private boolean individualWrite;
    private boolean managerWrite;
    private Date completedDate;
    private LookupValue role;
    private String status;
    private Date completeByDate;
    private Date closedDate;

    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final String COMPLETED = "COMPLETED";
    private static final String NOT_STARTED = "NOT_STARTED";
    private static final String NOT_COMPLETED = "NOT_COMPLETED";
    private static final String NO_ANSWERS = "NO_ANSWERS";
}
