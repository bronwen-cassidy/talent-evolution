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
     *
     * @param modifiedQuestionnaire - the modified questionaire
     * @param viewType           - is this a managers view
     * @param fromUser              - which user is sending this notification
     * @param toUser                - to whom is it going to
     * @throws TalentStudioException - db save errors
     */
    public void create(Questionnaire modifiedQuestionnaire, String viewType, User fromUser, User toUser) throws TalentStudioException {

        create(modifiedQuestionnaire.getLabel(), modifiedQuestionnaire.getId(), viewType, fromUser, toUser);
    }

    @Override
    public void create(String messageLabel, Long itemId, String viewType, User fromUser, User toUser) throws TalentStudioException {
        MessageItem inboxItem = new MessageItem();
        inboxItem.setViewType(viewType);
        inboxItem.setToUserId(toUser.getId());
        inboxItem.setDateReceived(new Date());
        inboxItem.setLabel(messageLabel);
        inboxItem.setFromUser(fromUser);
        inboxItem.setQuestionnaireId(itemId);
        inboxItem.setStatus(MessageItem.STATUS_UNREAD);
        inboxItem.setType(MessageItem.TYPE_QUESTIONNAIRE);
        messageDao.create(inboxItem);
    }

    public void create(Questionnaire modifiedQuestionnaire, String viewType, User user, List<User> participants) throws TalentStudioException {
        create(modifiedQuestionnaire.getLabel(), modifiedQuestionnaire.getId(), viewType, user, participants);
    }

    public void create(String queLabel, Long itemId, String viewType, User user, List<User> participants) throws TalentStudioException {
        for (User participant : participants) {
            create(queLabel, itemId, viewType, user, participant);
        }
    }

    public void delete(String[] messageItemIds) {
        messageDao.delete(messageItemIds);
    }

    public Integer countUnreadMessages(Long userId) throws TalentStudioException {
        return messageDao.countUnreadMessages(userId);
    }

    public void markAsRead(Long messageItemId) throws TalentStudioException {
        MessageItem messageItem = findById(messageItemId);
        messageItem.setStatus(MessageItem.STATUS_READ);
        messageDao.update(messageItem);
    }

    public void delete(Long messageItemId) throws TalentStudioException {
        MessageItem messageItem = findById(messageItemId);
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
