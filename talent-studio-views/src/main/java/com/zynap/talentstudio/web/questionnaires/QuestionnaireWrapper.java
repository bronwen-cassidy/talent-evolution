package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* Class or Interface description.
*
* @author bcassidy
* @since 20-Jul-2005 09:02:32
* @version 0.1
 */
public class QuestionnaireWrapper implements Serializable {

    private static final long serialVersionUID = 8140178606331559713L;
    private boolean sendFail;
    private String sendErrorMessage;

    public QuestionnaireWrapper() {
    }

    public final void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public final void setQuestionnaireState(Questionnaire questionnaire, List<QuestionGroupWrapper> questionnaireGroups, List<FormAttribute> wrappedDynamicAttributes, Long definitionId) {
        this.questionnaireGroups = questionnaireGroups;
        this.questionnaire = questionnaire;
        this.definitionId = definitionId;
        if (questionnaire != null) {
            QuestionnaireWorkflow workflow = questionnaire.getQuestionnaireWorkflow();
            if(workflow != null && workflow.getPerformanceReview() != null) {
                performanceReview = true;
            }            
        }
        this.wrappedDynamicAttributes = wrappedDynamicAttributes;
    }

    public final void clearQuestionnaireState() {
        this.questionnaire = null;
        this.questionnaireGroups = new ArrayList<QuestionGroupWrapper>();
        this.wrappedDynamicAttributes = new ArrayList<FormAttribute>();
    }

    public final List<FormAttribute> getWrappedDynamicAttributes() {
        return wrappedDynamicAttributes;
    }

    public final List<QuestionGroupWrapper> getQuestionnaireGroups() {
        return questionnaireGroups;
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaire != null ? getWorkflow().getQuestionnaireDefinition() : null;
    }

    public final Questionnaire getQuestionnaire() {
        return this.questionnaire;
    }

    public final Node getNode() {
        return questionnaire;
    }

    public String getQuestionnaireLabel() {
        return questionnaire != null ? questionnaire.getLabel() : "";
    }

    public final void setFatalErrors(boolean fatalErrors) {
        this.fatalErrors = fatalErrors;
    }

    public final boolean isFatalErrors() {
        return fatalErrors;
    }

    /**
     * Sets the wrapper as the target for adding a new comment blog.
     * 
     * @param selectAttributeWrapperIndex the index of the question selected to add a comment to
     */
    public final void assignSelectedAttribute(int selectAttributeWrapperIndex) {
        this.selectedIndex = selectAttributeWrapperIndex;
        this.attributeSelected = true;
    }

    public final int getSelectedIndex() {
        return selectedIndex;
    }

    public final void setAttributeSelected(boolean attributeSelected) {
        this.attributeSelected = attributeSelected;
    }

    public final boolean isAttributeSelected() {
        return attributeSelected;
    }

    public final Long getQuestionnaireId() {
        return questionnaire != null ? questionnaire.getId() : null;
    }

    public final Questionnaire getModifiedQuestionnaire() {
        DynamicAttributesHelper.assignAttributeValuesToNode(getWrappedDynamicAttributes(), questionnaire);
        return questionnaire;
    }

    public final boolean isNotificationBased() {
        return getWorkflow().isNotificationBased();
    }

    public boolean isPerformanceReview() {
        return performanceReview;
    }

    public final boolean isCompleted() {
        return questionnaire.isCompleted();
    }

    public Long getWorkflowId() {
        return questionnaire.getQuestionnaireWorkflowId();
    }

    public Long getSubjectId() {
        return questionnaire.getSubjectId();
    }

    public QuestionnaireWorkflow getWorkflow() {
        return questionnaire.getQuestionnaireWorkflow();
    }

    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }


    public boolean isMyPortfolio() {
        return myPortfolio;
    }

    public boolean isWritePermission() {
        if (myPortfolio) {
            return getWorkflow().isIndividualWrite();
        } else {
            return getWorkflow().isManagerWrite();
        }
    }

    public void setManagerView(boolean managerView) {
        this.managerView = managerView;
    }

    public boolean isManagerView() {
        return managerView;
    }

    /**
     * Only to be shown under edit conditions.
     *
     * @return true if manager view and the subject of the questionnaire is a user and can login (no point otherwise)
     *  or is an individual view and the manager can write
     */
    public boolean isShowInboxInfo() {
        if(managerView && getWorkflow().isIndividualWrite() && subjectUser != null) return true;
        else if(!managerView && getWorkflow().isManagerWrite()) return true;
        return false;
    }

    public void setSubjectUser(User ownerUser) {
        this.subjectUser = ownerUser;
    }

    public User getSubjectUser() {
        return subjectUser;
    }

    public void setErrorKey(String key) {
        errorKey = key;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(Long definitionId) {
        this.definitionId = definitionId;
    }

    public boolean isPositionPickerList() {
        return positionPickerList;
    }

    public void setPositionPickerList(boolean positionPickerList) {
        this.positionPickerList = positionPickerList;
    }

    public boolean isSubjectPickerList() {
        return subjectPickerList;
    }

    public void setSubjectPickerList(boolean subjectPickerList) {
        this.subjectPickerList = subjectPickerList;
    }

    public boolean isOrganisationPickerList() {
         return organisationPickerList;
    }

    public void setOrganisationPickerList(boolean organisationPickerList) {
        this.organisationPickerList = organisationPickerList;
    }

    public void setUserManagers(List<User> subjectManagers) {
        this.userManagers = subjectManagers;
    }

    public List<User> getUserManagers() {
        return userManagers;
    }

    public int getUserManagersCount(){
        return userManagers.size();
    }

    public Long[] getSelectedManagerIds() {
        return selectedManagerIds;
    }

    public void setSelectedManagerIds(Long[] selectedManagerIdToInbox) {
        this.selectedManagerIds = selectedManagerIdToInbox;
    }

    public Long getId() {
        return questionnaire.getId();
    }

    public void setSendSuccess(boolean sendSuccess) {
        this.sendSuccess = sendSuccess;
    }

    public boolean isSendSuccess() {
        return sendSuccess;
    }

    public boolean containsManagerSelection(Long userId) {
        if(selectedManagerIds == null) return false;
        for (Long managerId : selectedManagerIds) {
            if(userId.equals(managerId)) return true;    
        }
        return false;
    }

    private List<QuestionGroupWrapper> questionnaireGroups;
    protected Questionnaire questionnaire;
    private Long definitionId;
    private List<FormAttribute> wrappedDynamicAttributes;
    private boolean fatalErrors;
    private boolean attributeSelected = false;
    private int selectedIndex;
    private boolean myPortfolio;
    protected boolean performanceReview;
    private boolean managerView;
    private String errorKey;
    private Long userId;
    private Long[] selectedManagerIds;
    
    private boolean positionPickerList=false;
    private boolean subjectPickerList=false;
    private boolean organisationPickerList=false;

    private List<User> userManagers;
    private boolean sendSuccess;
    private User subjectUser;

    public void setSendFail(boolean sendFile) {
        this.sendFail = sendFile;
    }

    public boolean isSendFail() {
        return sendFail;
    }

    public void setSendErrorMessage(String sendErrorMessage) {
        this.sendErrorMessage = sendErrorMessage;
    }

    public String getSendErrorMessage() {
        return sendErrorMessage;
    }
}
