/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;

import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.exception.TalentStudioException;
import com.zynap.domain.admin.User;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 13:27:27
 */
public interface IMessageService {

    List<MessageItem> findAll (long userId);

    void create(Questionnaire modifiedQuestionnaire, boolean managerView, User fromUser, User toUser) throws TalentStudioException;

    void markAsRead(Long messageItemId) throws TalentStudioException;

    void delete(Long messageItemId) throws TalentStudioException;

    void create(Questionnaire modifiedQuestionnaire, boolean managerView, User user, List<User> participants) throws TalentStudioException;

    void delete(String[] messageItemIds);

    Integer countUnreadMessages(Long userId) throws TalentStudioException;
}
