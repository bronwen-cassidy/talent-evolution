/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-Dec-2008 09:10:29
 */
public class BrowseQuestionnaireWrapper implements Serializable {

    public BrowseQuestionnaireWrapper(List<QuestionnaireDTO> results, QuestionnaireWrapper questionnaireWrapper, QuestionnaireDTO current, boolean myPortfolio) {

        this.results = results;
        this.questionnaireWrapper = questionnaireWrapper;
        this.current = current;        
        this.myPortfolio = myPortfolio;
        this.questionnaireWrapper.setMyPortfolio(myPortfolio);
    }

    public QuestionnaireWrapper getQuestionnaireWrapper() {
        return questionnaireWrapper;
    }

    public void updateState(QuestionnaireWrapper wrapper, QuestionnaireDTO current) {
        this.questionnaireWrapper = wrapper;
        this.current = current;
        questionnaireWrapper.setSubjectUser(subjectUser);
        questionnaireWrapper.setMyPortfolio(myPortfolio);
    }

    public Questionnaire getQuestionnaire() {
        return questionnaireWrapper.getQuestionnaire();
    }

    public QuestionnaireWorkflow getWorkflow() {
        return questionnaireWrapper.getWorkflow();
    }

    public QuestionnaireDTO getCurrent() {
        return current;
    }

    public void setCurrent(QuestionnaireDTO current) {
        this.current = current;
    }

    public Long getFirst() {
        Long firstId = getId(0);
        if (firstId == null) return null;
        return (questionnaireWrapper == null || !firstId.equals(questionnaireWrapper.getWorkflowId())) ? firstId : null;
    }

    public Long getLast() {
        Long lastId = getId(results.size() - 1);
        if (lastId == null) return null;
        return (questionnaireWrapper == null || !lastId.equals(questionnaireWrapper.getWorkflowId())) ? lastId : null;
    }

    public Long getId(int pos) {
        if (results != null && !results.isEmpty())
            try {
                return results.get(pos).getWorkflowId();
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        return null;
    }

    public int getCurrentItemIndex() {
        if (questionnaireWrapper != null && (results != null && !results.isEmpty())) {
            return getIndex();
        }
        return -1;
    }

    public int getIndex() {
        return results.indexOf(current);
    }

    public Long getPrevious() {
        int index = getIndex();
        if(index <= 0) return null;
        else return getId(index - 1);
    }

    public Long getNext() {
        int index = getIndex();
        if(index >= results.size() - 1) return null;
        else return getId(index + 1);
    }

    public Long getCurrentIndex() {
        if (questionnaireWrapper != null && results != null) {
            int index = getIndex();
            return new Long(index + 1);
        }
        return null;
    }

    public int getNumResults() {
        return results != null ? results.size() : -1; 
    }

    public String getQuestionnaireLabel() {
        return questionnaireWrapper.getQuestionnaireLabel();
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public boolean isNotificationBased() {
        return questionnaireWrapper.isNotificationBased();
    }

    public final boolean isCompleted() {
        return questionnaireWrapper.isCompleted();
    }

    public boolean isWritePermission() {
        return questionnaireWrapper.isWritePermission();
    }

    public boolean isMyPortfolio() {
        return myPortfolio;
    }

    public Long getQuestionnaireId() {
        return questionnaireWrapper.getQuestionnaireId();
    }

    public Long getWorkflowId() {
        return questionnaireWrapper.getWorkflowId();
    }

    public boolean isPerformanceReview() {
        return questionnaireWrapper.isPerformanceReview();
    }

    public List<QuestionGroupWrapper> getQuestionnaireGroups() {
        return questionnaireWrapper.getQuestionnaireGroups();
    }

    public QuestionnaireDefinition getQuestionnaireDefinition() {
        return questionnaireWrapper.getQuestionnaireDefinition();
    }

    public List<QuestionnaireDTO> getResults() {
        return results;
    }

    public QuestionnaireDTO findQuestionnaireDTO(Long workflowId) {
        for (QuestionnaireDTO result : results) {
            if(result.getWorkflowId().equals(workflowId)) return result;
        }
        return null;
    }

    public void setSelectedManagerIds(Long[] managerIds) {
        questionnaireWrapper.setSelectedManagerIds(managerIds);
    }

    public Long[] getSelectedManagerIds() {
        return questionnaireWrapper.getSelectedManagerIds();
    }

    public boolean isManagerView() {
        return !myPortfolio;
    }

    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }

    public String getErrorKey() {
        return questionnaireWrapper.getErrorKey();
    }

    public void setErrorKey(String value) {
        questionnaireWrapper.setErrorKey(value);        
    }

    public boolean isPositionPickerList() {
        return questionnaireWrapper.isPositionPickerList();
    }

    public boolean isSubjectPickerList() {
        return questionnaireWrapper.isSubjectPickerList();
    }

    public boolean isOrganisationPickerList() {
         return questionnaireWrapper.isOrganisationPickerList();
    }

    public List<FormAttribute> getWrappedDynamicAttributes() {
        return questionnaireWrapper.getWrappedDynamicAttributes();
    }

    public Long getDefinitionId() {
        return questionnaireWrapper.getDefinitionId();
    }

    public boolean isShowInboxInfo() {
        questionnaireWrapper.setManagerView(!myPortfolio);
        return questionnaireWrapper.isShowInboxInfo();
    }

    public void setUserManagers(List<User> userManagers) {
        this.userManagers = userManagers;
    }

    public List<User> getUserManagers() {
        return userManagers;
    }

    public int getUserManagersCount(){
        return userManagers.size();
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public void setSendSuccess(boolean sendSuccess) {
        questionnaireWrapper.setSendSuccess(sendSuccess);
    }

    public boolean isSendSuccess() {
        return questionnaireWrapper.isSendSuccess();
    }

    public void setSubjectUser(User ownerUser) {
        this.subjectUser = ownerUser;
        questionnaireWrapper.setSubjectUser(ownerUser);
    }

    public Long getUserId() {
        return questionnaireWrapper.getUserId();
    }

    public User getSubjectUser() {
        return subjectUser;
    }

    private List<QuestionnaireDTO> results;
    private QuestionnaireWrapper questionnaireWrapper;
    private QuestionnaireDTO current;
    private boolean myPortfolio;
    private List<User> userManagers;
    private Long subjectId;
    private User subjectUser;
}
