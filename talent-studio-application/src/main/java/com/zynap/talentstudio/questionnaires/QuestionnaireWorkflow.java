package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.performance.PerformanceReview;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.Set;


/**
 * @author Hibernate CodeGenerator
 */
public class QuestionnaireWorkflow extends ZynapDomainObject {

    private static final long serialVersionUID = 1160746921824169553L;

    /**
     * default constructor.
     */
    public QuestionnaireWorkflow() {
    }

    public QuestionnaireWorkflow(Long id) {
        super(id);
    }

    public QuestionnaireWorkflow(String label, String workflowType, QuestionnaireDefinition questionnaireDefinition, Long userId) {
        super(null, true, label);
        this.workflowType = workflowType;
        this.label = label;
        this.questionnaireDefinition = questionnaireDefinition;
        this.userId = userId;
    }

    public String getWorkflowType() {
        return this.workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return this.expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return this.questionnaireDefinition;
    }

    public void setQuestionnaireDefinition(QuestionnaireDefinition questionnaireDefinition) {
        this.questionnaireDefinition = questionnaireDefinition;
    }

    public Set<Questionnaire> getQuestionnaires() {
        return this.questionnaires;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public PerformanceReview getPerformanceReview() {
        return performanceReview;
    }

    public void setPerformanceReview(PerformanceReview performanceReview) {
        this.performanceReview = performanceReview;
    }

    public Population getPopulation() {
        return population;
    }

    public void setPopulation(Population population) {
        this.population = population;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public boolean isManagerWrite() {
        return managerWrite;
    }

    public void setManagerWrite(boolean managerWrite) {
        this.managerWrite = managerWrite;
    }

    public boolean isManagerRead() {
        return managerRead;
    }

    public void setManagerRead(boolean managerRead) {
        this.managerRead = managerRead;
    }

    public boolean isIndividualWrite() {
        return individualWrite;
    }

    public void setIndividualWrite(boolean individualWrite) {
        this.individualWrite = individualWrite;
    }

    public boolean isIndividualRead() {
        return individualRead;
    }

    public void setIndividualRead(boolean individualRead) {
        this.individualRead = individualRead;
    }

    /**
     * Will archive or unarchive this workflow.
     * Archiving a questionnaire workflow removes all the associated questionnaires from the respective portfolios. A workflow can only be archived
     * once it has been completed. Unarchiving the workflow resets it's status to completed.
     */
    public void toggleArchived() {
        if(STATUS_ARCHIVED.equals(status)) {
            setStatus(STATUS_COMPLETED);
        } else if (STATUS_COMPLETED.equals(status)) {
            setStatus(STATUS_ARCHIVED);
        }
    }

    /**
     * Is this the manager evaluation.
     *
     * @return true or false
     */
    public boolean isQuestionnaireManager() {
        return performanceReview != null && TYPE_MANAGER_APPRAISAL.equals(this.workflowType);
    }

    public boolean isPerformanceQuestionnaire() {
        return performanceReview != null;
    }

    /**
     * Return true if status equals {@link #STATUS_PUBLISHED} and workflow type equals {@link #TYPE_QUESTIONNAIRE}.
     *
     * @return true or false
     */
    public boolean hasProcess() {
        return isNotificationBased() && STATUS_PUBLISHED.equals(status);
    }

    public boolean isNotificationBased() {
        return isNotificationBased(workflowType);
    }

    public boolean isInfoForm() {
        //TS-2253: returns true if any of the permissions have been set to true.
        return (isManagerRead() || isManagerWrite() || isIndividualRead() || isIndividualWrite());
    }

    public boolean isCompleted() {
        return STATUS_COMPLETED.equals(status);
    }

    /**
     * Return true if workflow type not equals {@link #TYPE_INFO_FORM}.
     *
     * @param workflowType used to check if the workflow is an info form
     * @return true or false
     */
    public static boolean isNotificationBased(String workflowType) {
        return !TYPE_INFO_FORM.equals(workflowType);
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("workflowType", getWorkflowType())
                .append("label", getLabel())
                .append("startDate", getStartDate())
                .append("expiryDate", getExpiryDate())
                .append("status", getStatus())
                .toString();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getLastRepublishedDate() {
        return lastRepublishedDate;
    }

    public void setLastRepublishedDate(Date lastRepublishedDate) {
        this.lastRepublishedDate = lastRepublishedDate;
    }

    /**
     * The type - currently can be {@link #TYPE_INFO_FORM} or {@link #TYPE_QUESTIONNAIRE}.
     */
    private String workflowType;

    /**
     * The start date - currently not used.
     */
    private Date startDate;

    /**
     * The expiry date - optional.
     */
    private Date expiryDate;

    /**
     * The date closed - optional.
     */
    private Date closedDate;

    /**
     * The status of the workflow - pending, published, cancelled, etc.
     */
    private String status;

    /**
     * The description.
     */
    private String description;

    private boolean managerWrite;

    private boolean managerRead;

    private boolean individualWrite;

    private boolean individualRead;

    private Date lastRepublishedDate;

    /**
     * The definition that the workflow belongs to.
     */
    private QuestionnaireDefinition questionnaireDefinition;

    private Long userId;

    /**
     * The questionnnaires belonging to the workflow.
     */
    private Set<Questionnaire> questionnaires;

    /**
     * The performance review.
     */
    private PerformanceReview performanceReview;

    /**
     * The population.
     */
    private Population population;

    /**
     * The group this workflow belongs to can be null in which case it belongs to the default group
     */
    private Group group;

    /**
     * Constants for status.
     */
    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_PUBLISHED = "PUBLISHED";
    public static final String STATUS_OPEN = "OPEN";
    public static final String STATUS_ARCHIVED = "ARCHIVED";

    /**
     * Constant for type to indicate that workflow is info form.
     */
    public static final String TYPE_INFO_FORM = "INFO_FORM";

    /**
     * Constant for type to indicate that workflow is questionnaire.
     */
    public static final String TYPE_QUESTIONNAIRE = "QUESTIONNARE_GENERAL";

    /**
     * Constant for type to indicate that workflow is performance review evaluator appraisal.
     */
    public static final String TYPE_EVALUATOR_APPRAISAL = "PERFORMANCE_REVIEW_EVALUATOR";

    /**
     * Constant for type to indicate that workflow is performance review manager appraisal.
     */
    public static final String TYPE_MANAGER_APPRAISAL = "PERFORMANCE_REVIEW_MANAGER";

}