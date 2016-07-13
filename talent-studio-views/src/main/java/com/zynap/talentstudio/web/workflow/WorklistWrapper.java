package com.zynap.talentstudio.web.workflow;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.performance.PerformanceEvaluator;
import com.zynap.talentstudio.performance.PerformanceManager;
import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.NodeInfo;
import com.zynap.talentstudio.web.objectives.ObjectiveWrapperBean;
import com.zynap.talentstudio.web.organisation.DisplayContentWrapper;
import com.zynap.talentstudio.web.organisation.NodeWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.perfomance.PerformanceEvaluatorWrapper;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireWrapper;
import com.zynap.talentstudio.web.workflow.performance.PerformanceReviewQuestion;
import com.zynap.talentstudio.workflow.Notification;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 20-Jul-2005 09:02:32
 */
public class WorklistWrapper extends QuestionnaireWrapper {

    public WorklistWrapper(List<Notification> notificationList, String workflowType, String notificationStatus, boolean performanceReview) {
        this.notificationList = notificationList;
        this.workflowType = workflowType;
        this.notificationStatus = notificationStatus;
        this.performanceReview = performanceReview;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(final Long notificationId) {
        this.notificationId = notificationId;
        this.notification = (Notification) CollectionUtils.find(notificationList, new Predicate() {
            public boolean evaluate(Object object) {
                return ((IDomainObject) object).getId().equals(notificationId);
            }
        });
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
        if (notification != null) notification.setAction(action);
    }

    public String getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(String activeTab) {
        this.activeTab = activeTab;
    }

    public boolean isManagerEvaluation() {
        return questionnaire != null && questionnaire.isManager();
    }

    public String getQuestionnaireLabel() {

        String label = super.getQuestionnaireLabel();
        if (performanceReview) {
            label = label + " - " + subjectName;
        }

        return label;

    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public List<PerformanceEvaluatorWrapper> getPerformanceRoles() {
        return performanceRoles;
    }

    public void setPerformanceRoles(List<PerformanceEvaluator> performanceRoles, boolean evaluateeHasUser) {
        this.performanceRoles = new ArrayList<PerformanceEvaluatorWrapper>();

        for (PerformanceEvaluator performanceEvaluator: performanceRoles) {
            addEvaluator(performanceEvaluator);
        }

        this.evaluateeHasUser = evaluateeHasUser;
    }

    public void addEvaluator(PerformanceEvaluator performanceEvaluator) {
        if (performanceEvaluator.isSelfEvaluator()) {
            this.evaluateeRole = new PerformanceEvaluatorWrapper(performanceEvaluator);
        } else {
            this.performanceRoles.add(new PerformanceEvaluatorWrapper(performanceEvaluator));
        }
    }

    public void clearPerformanceRoles() {

        this.evaluateeHasUser = false;
        this.performanceRoles = null;
        this.evaluateeRole = null;
    }

    public void clearWorkflowParameters() {

        this.role = null;
        this.subjectId = null;
        this.workflowId = null;
        this.notificationId = null;
        this.notification = null;
    }

    public void clearEvaluatorQuestions() {

        this.selectedGroup = null;
        this.evaluatorQuestions = null;
    }

    public void clearObjectives() {

        this.objective = null;
        this.objectives = null;
    }

    public PerformanceEvaluatorWrapper getEvaluateeRole() {
        return evaluateeRole;
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

    public boolean isPerformanceReview() {
        return performanceReview;
    }

    public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    /**
     * Method for spring.
     *
     * @return Collection of {@link com.zynap.talentstudio.web.workflow.performance.PerformanceReviewQuestion} objects
     */
    public Collection getEvaluatorQuestions() {
        return evaluatorQuestions;
    }

    public void setEvaluatorQuestions(Collection<PerformanceReviewQuestion> evaluatorQuestions) {
        this.evaluatorQuestions = evaluatorQuestions;
    }

    /**
     * Method for spring.
     *
     * @return questionnaireDefinitionModel {@link QuestionnaireDefinitionModel}
     */
    public QuestionnaireDefinitionModel getEvaluatorDefinition() {
        final QuestionnaireWorkflow evaluatorWorkflow = getEvaluatorWorkflow();
        return evaluatorWorkflow != null ? evaluatorWorkflow.getQuestionnaireDefinition().getQuestionnaireDefinitionModel() : null;
    }

    /**
     * Method for spring.
     *
     * @return Long
     */
    public Long getEvaluatorWorkflowId() {
        final QuestionnaireWorkflow evaluatorWorkflow = getEvaluatorWorkflow();
        return evaluatorWorkflow != null ? evaluatorWorkflow.getId() : null;
    }

    /**
     * Method for spring.
     *
     * @return String
     */
    public String getSelectedGroup() {
        return selectedGroup;
    }

    /**
     * Method for spring binding.
     *
     * @param selectedGroup the name of the chosen group
     */
    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    private QuestionnaireWorkflow getEvaluatorWorkflow() {

        QuestionnaireWorkflow evaluatorWorkflow = null;

        if (isManagerEvaluation()) {
            try {
                evaluatorWorkflow = questionnaire.getQuestionnaireWorkflow().getPerformanceReview().getEvaluatorWorkflow();
            } catch (NullPointerException ignored) {
            }
        }

        return evaluatorWorkflow;
    }

    public void setSubjectName(String s) {
        subjectName = s;
    }

    public boolean isEvaluateeHasUser() {
        return evaluateeHasUser;
    }

    public void setArtefact(SubjectWrapperBean subjectWrapperBean) {
        this.artefact = subjectWrapperBean;
    }

    public SubjectWrapperBean getArtefact() {
        return artefact;
    }

    public void setDisplayConfigView(DisplayContentWrapper displayContentWrapper) {
        this.displayConfigView = displayContentWrapper;
    }

    public DisplayContentWrapper getDisplayConfigView() {
        return displayConfigView;
    }

    public void setObjectives(Collection objectives) {
        this.objectives = objectives;
    }

    public Collection getObjectives() {
        return objectives;
    }

    public ObjectiveWrapperBean getObjective() {
        return objective;
    }

    public void setObjective(ObjectiveWrapperBean objective) {
        this.objective = objective;
    }

    public void setNodeInfo(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    public boolean isApproved() {
        return objective != null && objective.isApprovedOrComplete();
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }

    public Long getPerformanceId() {
        return performanceId;
    }

    public Long getSelectedManagerId() {
        return selectedManagerId;
    }

    public void setSelectedManagerId(Long selectedManagerId) {
        this.selectedManagerId = selectedManagerId;
    }

    public void setManagers(List<NodeWrapper> managers) {
        this.managers = managers;
    }

    public int getManagerCount() {
        return managers.size();
    }

    public List<NodeWrapper> getManagers() {
        return managers;
    }

    public void setPerformanceManager(PerformanceManager performanceManager) {
        this.performanceManager = performanceManager;
        if (this.performanceManager != null) setSelectedManagerId(performanceManager.getManagerId());
    }

    public PerformanceManager getModifiedPerformanceManager() {
        if (performanceManager == null) {
            performanceManager = new PerformanceManager(subjectId, performanceId, selectedManagerId);
        }
        performanceManager.setManagerId(selectedManagerId);
        return performanceManager;
    }

    public void setShowQuestionnairePdf(boolean showQuestionnairePdf) {

        this.showQuestionnairePdf = showQuestionnairePdf;
    }

    public boolean isShowQuestionnairePdf() {
        return showQuestionnairePdf;
    }

    public void setRoles(List<LookupValue> roles) {
        this.roles = roles;
    }

    public List<LookupValue> getRoles() {
        return roles;
    }

    public void setAppraisalReview(PerformanceReview appraisalReview) {
        this.appraisalReview = appraisalReview;
    }

    public PerformanceReview getAppraisalReview() {
        return appraisalReview;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }

    public void removeEvaluator(int index) {
        performanceRoles.remove(index);
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setManagersManagerView(boolean managersManagerView) {
        this.managersManagerView = managersManagerView;
    }

    public boolean isManagersManagerView() {
        return managersManagerView;
    }

    public void setHrView(boolean hrView) {
        this.hrView = hrView;
    }

    public boolean isHrView() {
        return hrView;
    }

    public boolean isApproveView() {
        return hrView || managersManagerView;
    }

    private List<Notification> notificationList;
    private Long notificationId;
    private String action;
    private String activeTab = WorklistController.WORKLIST_TAB;
    private Notification notification;
    private String workflowType;
    private Long role;
    private Long workflowId;
    private Long subjectId;
    private String notificationStatus;
    private List<PerformanceEvaluatorWrapper> performanceRoles;
    private PerformanceEvaluatorWrapper evaluateeRole;
    private String subjectName;

    private Collection<PerformanceReviewQuestion> evaluatorQuestions;
    private String selectedGroup;
    private boolean evaluateeHasUser;
    private SubjectWrapperBean artefact;
    private DisplayContentWrapper displayConfigView;
    private Collection objectives;
    private ObjectiveWrapperBean objective;
    private NodeInfo nodeInfo;
    private boolean saved;
    private Long performanceId;
    private Long selectedManagerId;
    private List<NodeWrapper> managers;
    private PerformanceManager performanceManager;
    private boolean showQuestionnairePdf=false;
    private List<LookupValue> roles;
    private PerformanceReview appraisalReview;
    private Subject subject;
    private Long currentUserId;

    private boolean managersManagerView;
    private boolean hrView;
}
