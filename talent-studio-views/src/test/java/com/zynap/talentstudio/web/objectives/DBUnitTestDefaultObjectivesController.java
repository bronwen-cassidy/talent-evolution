/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.objectives;
/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 07-May-2008 11:12:22
 * @version 0.1
 */

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.objectives.ObjectiveSetDto;
import com.zynap.talentstudio.organisation.IOrganisationUnitService;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.users.IUserService;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.organisation.BrowseMyTeamController;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapDbUnitMockControllerTestCase;

import java.util.Collection;
import java.util.List;

public class DBUnitTestDefaultObjectivesController extends ZynapDbUnitMockControllerTestCase {

    protected String getDataSetFileName() {
        return "objectives-test-data.xml";
    }

    protected void setUp() throws Exception {
        super.setUp();
        editMyObjectivesController = (EditMyObjectivesController) getBean("editMyObjectivesController");
        editObjectivesController = (DefaultObjectivesController) getBean("editObjectivesController");
        browseMyTeamController = (BrowseMyTeamController) getBean("browseMyTeamController");
        userService = (IUserService) getBean("userService");
        positionService = (IPositionService) getBean("positionService");
        organisationService = (IOrganisationUnitService) getBean("organisationUnitService");
    }

    /**
     * testing the scenario where a manager logs in goes to my team and assigns new objectives to a subordinate
     * @throws Exception
     */
    public void testFormBackingObject_Manager() throws Exception {

        User user = (User) userService.findById(PETER_ID);
        setUserSession(user, mockRequest);
        BrowseNodeWrapper nodeWrapperBean = (BrowseNodeWrapper) browseMyTeamController.formBackingObject(mockRequest);
        // get the subjectWrapper we already know we have the people as this is my team
        SubjectWrapperBean bean = (SubjectWrapperBean) nodeWrapperBean.getNodeWrapper();
        String fullName = bean.getFullName();
        // should be daniel deronda
        assertEquals(AGATHA_NAME, fullName);

        // we should have an edit on her objectives
        ObjectiveSetDto objectiveSet = bean.getCurrentObjectiveSet();
        assertNotNull(objectiveSet);

        Collection<Objective> currentObjectives = bean.getCurrentObjectives();
        assertEquals(1, currentObjectives.size());
        // condition used to test whether the edit button is displayed on the front end
        assertTrue(bean.isHasPublishedObjectives());
        assertTrue(bean.isHasApprovableObjectives());
        // click on the adit button as manager
        mockRequest.addParameter(ParameterConstants.NODE_ID_PARAM, AGATHA_SUBJECT_ID.toString());
        ObjectiveSetFormBean objectiveSetWrapper = (ObjectiveSetFormBean) editObjectivesController.formBackingObject(mockRequest);
        // assert organisationUnit info
        List<ObjectiveWrapperBean> objectives = objectiveSetWrapper.getObjectives();
        assertEquals(1, objectives.size());
        List<ObjectiveSet> objectiveSetList = objectiveSetWrapper.getOrganisationObjectiveSets();
        assertNotNull(objectiveSetList);
        assertEquals(1, objectiveSetList.size());
        ObjectiveSet ouObjectiveSet = objectiveSetList.get(0);
        assertEquals(1, ouObjectiveSet.getObjectives().size());
        // assert validity none should be invalid
        ObjectiveWrapperBean objectiveWrapper = objectives.get(0);
        assertFalse(objectiveWrapper.isInvalid());
        // move agatha to a new OU
        Position position = positionService.findById(HEAD_OF_IT_ID);
        OrganisationUnit marketing = organisationService.findById(MARKETING_OU_ID);
        position.setOrganisationUnit(marketing);
        positionService.update(position);
        // assertions on status of objectives
        objectiveSetWrapper = (ObjectiveSetFormBean) editObjectivesController.formBackingObject(mockRequest);
        // assert invalid objectives
        objectiveWrapper = objectiveSetWrapper.getObjectives().get(0);
        assertTrue(objectiveWrapper.isInvalid());
    }

    public void testFormBackingObject_Subject() throws Exception {

        User user = (User) userService.findById(AGATHA_ID);
        setUserSession(user, mockRequest);

        // click on the adit button as manager
        mockRequest.addParameter(ParameterConstants.NODE_ID_PARAM, AGATHA_SUBJECT_ID.toString());
        ObjectiveSetFormBean objectiveSetWrapper = (ObjectiveSetFormBean) editMyObjectivesController.formBackingObject(mockRequest);

        // assert organisationUnit info
        List<ObjectiveWrapperBean> objectives = objectiveSetWrapper.getObjectives();
        assertEquals(1, objectives.size());
        List<ObjectiveSet> objectiveSetList = objectiveSetWrapper.getOrganisationObjectiveSets();
        assertNotNull(objectiveSetList);
        assertEquals(1, objectiveSetList.size());
        ObjectiveSet ouObjectiveSet = objectiveSetList.get(0);
        assertEquals(1, ouObjectiveSet.getObjectives().size());
        // assert validity none should be invalid
        ObjectiveWrapperBean objectiveWrapper = objectives.get(0);
        assertFalse(objectiveWrapper.isInvalid());
        // move agatha to a new OU
        Position position = positionService.findById(HEAD_OF_IT_ID);
        OrganisationUnit marketing = organisationService.findById(MARKETING_OU_ID);
        position.setOrganisationUnit(marketing);
        positionService.update(position);
        // assertions on status of objectives
        objectiveSetWrapper = (ObjectiveSetFormBean) editObjectivesController.formBackingObject(mockRequest);
        // assert invalid objectives
        objectiveWrapper = objectiveSetWrapper.getObjectives().get(0);
        assertTrue(objectiveWrapper.isInvalid());
    }

    private final Long PETER_ID = new Long(-132);
    private final Long AGATHA_ID = new Long(-135);
    private final Long AGATHA_SUBJECT_ID = new Long(-35);
    private final Long MARKETING_OU_ID = new Long(-22);
    private final Long HEAD_OF_IT_ID = new Long(-8);

    private final String AGATHA_NAME = "Agatha Christie";

    private EditMyObjectivesController editMyObjectivesController;
    private DefaultObjectivesController editObjectivesController;
    private BrowseMyTeamController browseMyTeamController;
    private IUserService userService;
    private IPositionService positionService;
    private IOrganisationUnitService organisationService;
}