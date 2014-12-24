/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;

import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.questionnaires.Questionnaire;

import java.util.Date;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 13:28:27
 */
public class MessageService extends DefaultService implements IMessageService {

    public List<MessageItem> findAll(long userId) {
        return messageDao.findAll(userId);
    }

    /**
     * Creates messages for the given questionnaire
     * @param modifiedQuestionnaire
     * @param managerView
     * @param fromUser
     * @param toUser
     * @throws TalentStudioException
     */
    public void create(Questionnaire modifiedQuestionnaire, boolean managerView, User fromUser, User toUser) throws TalentStudioException {

        MessageItem inboxItem = new MessageItem();
        if(managerView) {
            // we are going to the subordinate who is the subject of the questionnaire
            inboxItem.setViewType(MessageItem.INDIVIDUAL_VIEW);
        } else {
            // we are sending to our managers we need to ask the subject of the questionnaire who the maanger is.
            inboxItem.setViewType(MessageItem.MANAGER_VIEW);
        }
        inboxItem.setToUserId(toUser.getId());
        inboxItem.setDateReceived(new Date());
        inboxItem.setLabel(modifiedQuestionnaire.getLabel());
        inboxItem.setFromUser(fromUser);
        inboxItem.setQuestionnaireId(modifiedQuestionnaire.getId());
        inboxItem.setStatus(MessageItem.STATUS_UNREAD);
        inboxItem.setType(MessageItem.TYPE_QUESTIONNAIRE);
        messageDao.create(inboxItem);
    }

    public void create(Questionnaire modifiedQuestionnaire, boolean managerView, User user, List<User> participants) throws TalentStudioException {
        for(User participant : participants) {
            create(modifiedQuestionnaire, managerView, user, participant);
        }
    }

    public void delete(String[] messageItemIds) {
        messageDao.delete(messageItemIds);    
    }

    public Integer countUnreadMessages(Long userId) throws TalentStudioException {
        return messageDao.countUnreadMessages(userId);
    }

    public void markAsRead(Long messageItemId) throws TalentStudioException {
        MessageItem messageItem = (MessageItem) findById(messageItemId);
        messageItem.setStatus(MessageItem.STATUS_READ);
        messageDao.update(messageItem);
    }

    public void delete(Long messageItemId) throws TalentStudioException {
        MessageItem messageItem = (MessageItem) findById(messageItemId);
        messageDao.delete(messageItem);
    }

    protected IFinder getFinderDao() {
        return messageDao;
    }

    protected IModifiable getModifierDao() {
        return messageDao;
    }

    public void setMessageDao(IMessageDao messageDao) {
        this.messageDao = messageDao;
    }

    private IMessageDao messageDao;
}
