/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 27-May-2008 11:36:27
 * @version 0.1
 */

import com.zynap.talentstudio.ZynapDatabaseTestCase;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.domain.admin.User;

import java.util.List;


public class TestMessageService extends ZynapDatabaseTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        messageService = (IMessageService) applicationContext.getBean("messageService");
        userService = (IUserService) getBean("userService");
        questionnaireService = (IQuestionnaireService) getBean("questionnaireService");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected String getDataSetFileName() {
        return "test-message-data.xml";
    }

    public void testFindAll() throws Exception {
        // are none
        final List<MessageItem> messageItemList = messageService.findAll(ADMINISTRATOR_USER_ID);
        assertTrue(messageItemList.isEmpty());
    }

    public void testCreateManagerView() throws Exception {

        Questionnaire questionnaire = (Questionnaire) questionnaireService.findById(new Long(-26));
        final User fromUser = (User) userService.findById(new Long(-34));
        final User toUser = (User) userService.findById(new Long(-44));
        messageService.create(questionnaire, true, fromUser, toUser);

        // lets us find the message
        final List<MessageItem> messageItemList = messageService.findAll(new Long(-44));
        assertEquals(1, messageItemList.size());
    }

    public void testCreateManagerViewIndividualView() throws Exception {

        Questionnaire questionnaire = (Questionnaire) questionnaireService.findById(new Long(-26));
        // manager
        final User fromUser1 = (User) userService.findById(new Long(-34));
        final User fromUser2 = (User) userService.findById(new Long(-321));
        // to employee
        final User toUser = (User) userService.findById(new Long(-34));
        // create first
        messageService.create(questionnaire, true, fromUser1, toUser);

        // create the second
        messageService.create(questionnaire, true, fromUser2, toUser);

        // lets us find the message
        final List<MessageItem> messageItemList = messageService.findAll(new Long(-34));
        assertEquals(2, messageItemList.size());
    }

    public void testDelete() throws Exception {
        Questionnaire questionnaire = (Questionnaire) questionnaireService.findById(new Long(-26));
        final User fromUser = (User) userService.findById(new Long(-34));
        final User toUser = (User) userService.findById(new Long(-44));
        messageService.create(questionnaire, true, fromUser, toUser);

        // lets us find the message
        List<MessageItem> messageItemList = messageService.findAll(new Long(-44));
        assertEquals(1, messageItemList.size());

        messageService.delete(messageItemList.get(0).getId());
        messageItemList = messageService.findAll(new Long(-44));
        assertEquals(0, messageItemList.size());
    }

    public void testCountUnreadMessages() throws Exception {
        Questionnaire questionnaire = (Questionnaire) questionnaireService.findById(new Long(-26));
        final User fromUser = (User) userService.findById(new Long(-34));
        final User toUser = (User) userService.findById(new Long(-44));
        messageService.create(questionnaire, true, fromUser, toUser);

        Integer numUnread = messageService.countUnreadMessages(new Long(-44));
        assertEquals(new Integer(1), numUnread);
    }


    private IMessageService messageService;
    private IUserService userService;
    private IQuestionnaireService questionnaireService;
}