package com.zynap.talentstudio.performance;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

public class PerformanceReview extends ZynapDomainObject {

    public PerformanceReview(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public PerformanceReview() {
    }

    public Set<QuestionnaireWorkflow> getQueWorkflows() {
        return queWorkflows;
    }

    public void setQueWorkflows(Set<QuestionnaireWorkflow> queWorkflows) {
        this.queWorkflows = queWorkflows;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNotifiable() {
        return notifiable;
    }

    public void setUserManaged(boolean userManaged) {
        this.userManaged = userManaged;
    }

    public boolean isUserManaged() {
        return userManaged;
    }

    public void setNotifiable(boolean notifiable) {
        this.notifiable = notifiable;
    }

    public void addQuestionnaireWorkflow(QuestionnaireWorkflow managerQuestionnaire) {
      queWorkflows.add(managerQuestionnaire);
      managerQuestionnaire.setPerformanceReview(this);
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .append("label", getLabel())
            .append("status", getStatus())
            .toString();
    }

    public QuestionnaireWorkflow getEvaluatorWorkflow() {
        return (QuestionnaireWorkflow) CollectionUtils.find(queWorkflows, new WorkflowPredicate(QuestionnaireWorkflow.TYPE_EVALUATOR_APPRAISAL));
    }

    public QuestionnaireWorkflow getManagerWorkflow() {
        return (QuestionnaireWorkflow) CollectionUtils.find(queWorkflows, new WorkflowPredicate(QuestionnaireWorkflow.TYPE_MANAGER_APPRAISAL));
    }

    private class WorkflowPredicate implements Predicate {

        private String workflowType;

        public WorkflowPredicate(String workflowType) {
            this.workflowType = workflowType;
        }

        public boolean evaluate(Object object) {
            QuestionnaireWorkflow workflow = (QuestionnaireWorkflow) object;

            return this.workflowType.equals(workflow.getWorkflowType());
        }
    }

    private boolean notifiable;
    private boolean userManaged;
    private Set<QuestionnaireWorkflow> queWorkflows = new HashSet<QuestionnaireWorkflow>();
    private String status;
    private String appraisalType = PERFORMANCE_TYPE;

    public static final String COMPETENCY_TYPE = "COMPETENCY";
    public static final String PERFORMANCE_TYPE = "PERFORMANCE";
}
