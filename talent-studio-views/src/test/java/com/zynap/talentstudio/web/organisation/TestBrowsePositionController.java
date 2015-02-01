/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 30-Mar-2009 15:27:05
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.orgbuilder.PositionSearchQuery;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.positions.PositionSearchQueryWrapper;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestBrowsePositionController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        browsePositionController = (BrowsePositionController) getBean("browsePositionController");
        ISubjectService subjectService = (ISubjectService) getBean("subjectService");

        Subject subject = subjectService.findByUserId(USER_ID);

        final UserPrincipal userPrincipal = getUserPrincipal(subject);
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        userSession.setNavigator(new ZynapNavigator());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        browsePositionController = null;        
    }

    protected String getDataSetFileName() throws Exception {
        return "test-data-position-app.xml";
    }

    public void testFormBackingObject() throws Exception {
        
        BrowseNodeWrapper browseNodeWrapper = (BrowseNodeWrapper) browsePositionController.formBackingObject(mockRequest);
        //todo maybe expand
        assertNotNull(browseNodeWrapper);

    }

    public void testDoSearch() throws Exception {
        BrowseNodeWrapper browseNodeWrapper = (BrowseNodeWrapper) browsePositionController.formBackingObject(mockRequest);
        /**
         * 1) load some subjects
         * 2) override the search pattern
         * 3) check for results
         * 4) check for 0 results
         */
        PositionSearchQuery positionSearchQuery = new PositionSearchQuery();
        final String HEAD_OF_IT = "Head Of IT";
        positionSearchQuery.setTitle(HEAD_OF_IT);

        PositionSearchQueryWrapper queryWrapper = new PositionSearchQueryWrapper(positionSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);
        browsePositionController.doSearch(browseNodeWrapper, mockRequest);

        List result = browseNodeWrapper.getCurrentNodes();

        assertEquals(result.size(), 1);
        for (Object o : result) {

            Position p = (Position) o;
            assertEquals(p.getLabel(), HEAD_OF_IT);
        }

        positionSearchQuery = new PositionSearchQuery();
        String HEAD_OF = "Head Of";
        positionSearchQuery.setTitle(HEAD_OF);

        queryWrapper = new PositionSearchQueryWrapper(positionSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);
        browsePositionController.doSearch(browseNodeWrapper, mockRequest);
        result = browseNodeWrapper.getCurrentNodes();
        assertEquals(result.size(), 4);

        for (Object o : result) {

            Position p = (Position) o;
            assertTrue(p.getLabel().startsWith(HEAD_OF));
        }

        positionSearchQuery = new PositionSearchQuery();

        positionSearchQuery.setTitle("findnothing");

        queryWrapper = new PositionSearchQueryWrapper(positionSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);
        browsePositionController.doSearch(browseNodeWrapper, mockRequest);
        result = browseNodeWrapper.getCurrentNodes();
        assertTrue(result.isEmpty());


    }

    private BrowsePositionController browsePositionController;
    private final Long USER_ID = new Long(-132);
}