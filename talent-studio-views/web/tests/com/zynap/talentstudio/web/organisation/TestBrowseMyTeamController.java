/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 30-Mar-2009 10:18:14
 * @version 0.1
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.List;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
public class TestBrowseMyTeamController extends ZynapDbUnitMockControllerTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        browseMyTeamController = (BrowseMyTeamController) getBean("browseMyTeamController");
        subjectService = (ISubjectService) getBean("subjectService");
        //objectiveService = (IObjectiveService) getBean("objectiveService");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        browseMyTeamController = null;
        subjectService = null;
    }

    protected String getDataSetFileName() throws Exception {
        return "subjects/subject-data.xml";
    }

    public void testCreateBrowseNodeWrapperWithSubjects() throws Exception {

        /**
         * 1) a subject that has position as leader
         * 2) members that report to him
         * 3) test with subject that don't have members
         */


        final Subject subject = subjectService.findById(SUBJECT_TEAM_LEADER);
        assertNotNull(subject.getCoreDetail());
        //peter is the manager for agatha, jane, daniel
        assertEquals(subject.getCoreDetail().getName(), "Peter Sellers");
        final UserSession userSession = new UserSession(getUserPrincipal(subject));
        setUserSession(userSession, mockRequest);

        BrowseNodeWrapper browseNodeWrapper = browseMyTeamController.createBrowseNodeWrapper(userSession, mockRequest);
        List<Subject> teamMembers = browseNodeWrapper.getCurrentNodes();
        assertFalse(teamMembers.isEmpty());
        assertEquals(teamMembers.size(), 3);

        String viewAttLabel = subjectService.getMyTeamViewAttributeLabel(subject.getId());
        assertEquals(viewAttLabel, browseNodeWrapper.getAttributeLabel());

    }

    public void testUpdateNodeInfoWithoutSubjects() throws Exception {
        final Subject subject = subjectService.findById(SUBJECT_NOT_TEAM_LEADER);
        assertNotNull(subject.getCoreDetail());
        //does not have team members
        assertEquals(subject.getCoreDetail().getName(), "Agatha Christie");
        final UserSession userSession = new UserSession(getUserPrincipal(subject));
        setUserSession(userSession, mockRequest);
        BrowseNodeWrapper browseNodeWrapper = browseMyTeamController.createBrowseNodeWrapper(userSession, mockRequest);
        List<Subject> teamMembers = browseNodeWrapper.getCurrentNodes();
        assertTrue(teamMembers.isEmpty());
        assertEquals(teamMembers.size(), 0);
        String viewAttLabel = subjectService.getMyTeamViewAttributeLabel(subject.getId());
        assertEquals(viewAttLabel, browseNodeWrapper.getAttributeLabel());
    }

    public void testUpdateNodeInfoNoObjectives() throws Exception {

        //get a subject
        final Subject subject = subjectService.findById(SUBJECT_TO_UPDATE_NO_OBJECTIVES);
        assertNotNull(subject.getCoreDetail());
        assertEquals(subject.getCoreDetail().getName(), "Daniel Deronda");
        final UserSession userSession = new UserSession(getUserPrincipal(subject));
        setUserSession(userSession, mockRequest);

        BrowseNodeWrapper browseNodeWrapper = browseMyTeamController.createBrowseNodeWrapper(userSession, mockRequest);

        browseMyTeamController.updateNodeInfo(subject, browseNodeWrapper, userSession);

        SubjectWrapperBean subjectWrapperBean = (SubjectWrapperBean) browseNodeWrapper.getNodeWrapper();
        assertNotNull(subjectWrapperBean);
        assertNull(subjectWrapperBean.getCurrentObjectiveSet());
        assertFalse(subjectWrapperBean.isHasApprovableAssessments());
        //todo to test for has objectives 
     
    }

   

    private BrowseMyTeamController browseMyTeamController;
    private ISubjectService subjectService;
    //private IObjectiveService objectiveService;
    private final Long SUBJECT_TEAM_LEADER = new Long(-32);
    private final Long SUBJECT_NOT_TEAM_LEADER = new Long(-35);
    private final Long SUBJECT_TO_UPDATE_NO_OBJECTIVES = new Long(-33);
    //private final Long SUBJECT_TO_UPDATE_HAS_OBJECTIVES = new Long(-34);
}