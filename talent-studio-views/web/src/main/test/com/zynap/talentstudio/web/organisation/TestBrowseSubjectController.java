/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 30-Mar-2009 16:40:25
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.talentstudio.navigation.ZynapNavigator;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.organisation.subjects.SubjectSearchQueryWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestBrowseSubjectController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        browseSubjectController = (BrowseSubjectController) getBean("browseSubjectController");
        subjectService = (ISubjectService) getBean("subjectService");

        Subject subject = subjectService.findByUserId(USER_ID);

        final UserPrincipal userPrincipal = getUserPrincipal(subject);
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        userSession.setNavigator(new ZynapNavigator());
        setUserSession(userSession, mockRequest);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        browseSubjectController = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "test-data-position-app.xml";
    }

    public void testFormBackingObject() throws Exception {

        BrowseNodeWrapper browseNodeWrapper = (BrowseNodeWrapper) browseSubjectController.formBackingObject(mockRequest);
        assertNotNull(browseNodeWrapper);
    }

    public void testDoSearch() throws Exception {
        Subject subject = subjectService.findByUserId(USER_ID);
        assertNotNull(subject.getCoreDetail());
        assertNotNull(subject.getUser());
        BrowseNodeWrapper browseNodeWrapper = (BrowseNodeWrapper) browseSubjectController.formBackingObject(mockRequest);

        //search by position title
        SubjectSearchQuery subjectSearchQuery = new SubjectSearchQuery();

        subjectSearchQuery.setPositionTitle("IT");

        SubjectSearchQueryWrapper queryWrapper = new SubjectSearchQueryWrapper(subjectSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);

        browseSubjectController.doSearch(browseNodeWrapper, mockRequest);

        List result = browseNodeWrapper.getCurrentNodes();
        assertEquals(result.size(), 1);
        Subject subj = (Subject) result.get(0);

        String name = subj.getFirstName() + " " + subj.getSecondName();
        assertEquals(name, "Agatha Christie");

        //search by first name
        subjectSearchQuery = new SubjectSearchQuery();

        subjectSearchQuery.setFirstName("Daniel");

        queryWrapper = new SubjectSearchQueryWrapper(subjectSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);

        browseSubjectController.doSearch(browseNodeWrapper, mockRequest);

        result = browseNodeWrapper.getCurrentNodes();
        subj = (Subject) result.get(0);
        name = subj.getFirstName() + " " + subj.getSecondName();
        assertEquals(result.size(), 1);
        assertEquals(name, "Daniel Deronda");

        //search for wierd stuff
        subjectSearchQuery = new SubjectSearchQuery();

        subjectSearchQuery.setSecondName("e"); //all the test subjects

        queryWrapper = new SubjectSearchQueryWrapper(subjectSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);

        browseSubjectController.doSearch(browseNodeWrapper, mockRequest);

        result = browseNodeWrapper.getCurrentNodes();
        assertEquals(result.size(), 3);

        //search for wierd stuff
        subjectSearchQuery = new SubjectSearchQuery();

        subjectSearchQuery.setFirstName("*");

        queryWrapper = new SubjectSearchQueryWrapper(subjectSearchQuery);
        browseNodeWrapper.setFilter(queryWrapper);

        browseSubjectController.doSearch(browseNodeWrapper, mockRequest);

        result = browseNodeWrapper.getCurrentNodes();
        assertTrue(result.isEmpty());

    }

    public void testCreateBrowseNodeWrapper() throws Exception {

        BrowseNodeWrapper browseNodeWrapper = browseSubjectController.createBrowseNodeWrapper(UserSessionFactory.getUserSession(), mockRequest);
        assertSame(browseNodeWrapper.getFilter().getClass(), SubjectSearchQueryWrapper.class);
        assertSame(browseNodeWrapper.getNodeWrapper().getClass(), SubjectWrapperBean.class);

    }

    private BrowseSubjectController browseSubjectController;
    private ISubjectService subjectService;
    private final Long USER_ID = new Long(-132);
}