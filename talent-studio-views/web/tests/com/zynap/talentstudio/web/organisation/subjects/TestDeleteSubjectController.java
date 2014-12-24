/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 31-Mar-2009 10:47:26
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.exception.DomainObjectNotFoundException;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestDeleteSubjectController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        deleteSubjectController = (DeleteSubjectController) getBean("deleteSubjectController");
        subjectService = (ISubjectService) getBean("subjectService");

        Subject subject = subjectService.findByUserId(USER_ID);
        final UserPrincipal userPrincipal = getUserPrincipal(subject);
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        userSession.setNavigator(new ZynapNavigator());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        deleteSubjectController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "subject-data.xml";
    }

    public void testDeleteNode() throws Exception {

        Subject subject = subjectService.findById(SUBJECT_ID);
        assertNotNull(subject);
        deleteSubjectController.deleteNode(subject);
      
        try{
            subjectService.findById(SUBJECT_ID);
            fail("not allowed operation");
        }catch (DomainObjectNotFoundException e){}
        
    }

  

    private DeleteSubjectController deleteSubjectController;
    private ISubjectService subjectService;
    private final Long USER_ID = new Long(-132);
    private final Long SUBJECT_ID = new Long(-34);
}