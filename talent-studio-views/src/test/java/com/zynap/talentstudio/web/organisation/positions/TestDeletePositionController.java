/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 31-Mar-2009 11:24:56
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestDeletePositionController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        deletePositionController = (DeletePositionController) getBean("deletePositionController");
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");
        positionService = (IPositionService) getBean("positionService");

        Subject subject = subjectService.findByUserId(USER_ID);

        final UserPrincipal userPrincipal = getUserPrincipal(subject);
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        userSession.setNavigator(new ZynapNavigator());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        deletePositionController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "test-data.xml";
    }

    public void testDeleteNode() throws Exception {
        Position position = positionService.findById(POSITION_ID);
        assertNotNull(position);
        try {
            deletePositionController.deleteNode(position);
            //fail("not allowed operation");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DeletePositionController deletePositionController;
    private IPositionService positionService;
    private final Long USER_ID = new Long(-132);
    private final Long POSITION_ID = new Long(-8);
}