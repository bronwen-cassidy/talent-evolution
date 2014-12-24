/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 28-Aug-2008 14:40:38
 * @version 0.1
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */

public class TestAddCorporateObjectiveController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        addCorporateObjectiveController = (AddCorporateObjectiveController) getBean("addCorporateObjectiveController");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFormBackingObject() throws Exception {
        // log in an administrator
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        ObjectiveSet objectiveSet = bean.getObjectiveSet();
        assertNotNull(objectiveSet);
        // set should not have any objectives yet!
        assertTrue(objectiveSet.getObjectives().isEmpty());
        // objective wrappers should be on the bean
        assertTrue(bean.getObjectives().size() == 1);
        ObjectiveWrapperBean objective = bean.getObjectives().get(0);
        // no info on this item until we set it
        assertNull(objective.getId());
        assertNull(objective.getLabel());
    }

    public void testOnBindAndValidateInternalAddObjective() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        // add some info to the set
        addObjective(bean);
        assertEquals(2, bean.getObjectives().size());
    }

    public void testOnBindAndValidateInternalFinishError3() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        // finish this should get errors
        mockRequest.addParameter("_finish", "finish");
        final Errors errors = getErrors(bean);
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        assertTrue(errors.hasErrors());

        // errors should be
        // 1) set needs a label
        // 2) objective needs a label
        // 3) objective needs a description
        assertEquals(3, errors.getErrorCount());

    }

    public void testOnBindAndValidateInternalFinishError2() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        bean.getObjectiveSet().setLabel("Set Label # 1");
        // finish this should get errors
        mockRequest.addParameter("_finish", "finish");
        final Errors errors = getErrors(bean);
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        assertTrue(errors.hasErrors());
        // errors should be
        // 1) objective needs a label
        // 2) objective needs a description
        assertEquals(2, errors.getErrorCount());
    }

    public void testOnBindAndValidateInternalNoErrors() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        bean.getObjectiveSet().setLabel("Set Label # 1");
        final List<ObjectiveWrapperBean> objectives = bean.getObjectives();
        int index = 1;
        populateObjectives(objectives, index);
        // finish this should not get errors
        mockRequest.addParameter("_finish", "finish");
        final Errors errors = getErrors(bean);
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        assertFalse(errors.hasErrors());
        assertEquals(0, errors.getErrorCount());
    }

    public void testOnBindAndValidateInternalRemoveObjective() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        // add some info to the set
        addObjective(bean);
        mockRequest.removeParameter("_target2");
        removeObjective(bean, 1);
        assertEquals(1, bean.getObjectives().size());
    }

    public void testProcessFinish() throws Exception {
        setUserSession(ADMINISTRATOR_USER_ID, mockRequest);
        CorporateObjectivesFormBean bean = (CorporateObjectivesFormBean) addCorporateObjectiveController.formBackingObject(mockRequest);
        bean.getObjectiveSet().setLabel("Set Label # 1");
        final List<ObjectiveWrapperBean> objectives = bean.getObjectives();
        populateObjectives(objectives, 1);
        // finish this should not get errors
        Errors errors = saveObjectiveSet(bean);
        assertEquals(0, errors.getErrorCount());

        // see if we can find it
        final Long id = bean.getObjectiveSet().getId();
        assertNotNull(id);

        ObjectiveSet corporate = findObjectiveSet(id);
        assertTrue(corporate.isOpen());
    }

    private ObjectiveSet findObjectiveSet(Long id) throws Exception {
        ViewCorporateObjectiveController viewController = (ViewCorporateObjectiveController) getBean("viewCorporateObjectives");
        mockRequest.clearAttributes();
        mockRequest.removeAllParameters();
        mockRequest.addParameter("id", String.valueOf(id));
        final ModelAndView modelAndView = viewController.handleRequest(mockRequest, mockResponse);
        final Map map = modelAndView.getModel();
        return (ObjectiveSet) map.get("command");
    }

    private Errors saveObjectiveSet(CorporateObjectivesFormBean bean) throws Exception {
        mockRequest.addParameter("_finish", "finish");
        final Errors errors = getErrors(bean);
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, errors, 0);
        addCorporateObjectiveController.processFinishInternal(mockRequest, mockResponse, bean, errors);
        return errors;
    }

    private void addObjective(CorporateObjectivesFormBean bean) throws Exception {
        mockRequest.addParameter("_target2", "2");
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, getErrors(bean), 0);
    }

    private void removeObjective(CorporateObjectivesFormBean bean, int index) throws Exception {
        mockRequest.addParameter("_target3", "3");
        mockRequest.addParameter("deleteIndex", String.valueOf(index));
        addCorporateObjectiveController.onBindAndValidateInternal(mockRequest, bean, getErrors(bean), 0);
    }

    private void populateObjectives(List<ObjectiveWrapperBean> objectives, int index) {
        for (ObjectiveWrapperBean objective : objectives) {
            objective.setLabel("Label # " + index);
            objective.setDescription("Objective Description # " + index);
            index++;
        }
    }

    private AddCorporateObjectiveController addCorporateObjectiveController;
}