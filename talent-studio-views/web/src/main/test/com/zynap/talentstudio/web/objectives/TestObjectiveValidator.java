/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @since 25-May-2006 09:41:03
 * @version 0.1
 */
package com.zynap.talentstudio.web.objectives;

import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.web.AbstractValidatorTestCase;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */

public class TestObjectiveValidator extends AbstractValidatorTestCase {

    protected void setUp() throws Exception {
        objectiveValidator = new ObjectiveValidator();
    }

    protected void tearDown() throws Exception {
        objectiveValidator = null;
    }

    public void testSupports() throws Exception {
        assertTrue(objectiveValidator.supports(ObjectiveSetFormBean.class));
    }

    public void testValidateLabelFails() throws Exception {
        ObjectiveSetFormBean set = new ObjectiveSetFormBean();
        ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean(new Objective());
        wrapperBean.setLabel(" ");
        wrapperBean.setParentDesc("The parent Objective Description");
        wrapperBean.setDescription("This is a description");
        wrapperBean.setSelectedParentId(new Long(6));
        set.addObjective(wrapperBean);

        Errors errors = new DataBinder(set, "command").getBindingResult();
        objectiveValidator.validateObjectiveSet(set, errors);
        assertErrorCount(1, errors);
        assertErrorCode(errors, "error.required.field", "objectives[0].label");
    }

    public void testValidationRejectsDuplicateLabels() throws Exception {

        final String label = "objective 1";
        final String description = "description";

        ObjectiveSetFormBean set = new ObjectiveSetFormBean();

        final Objective newObjective1 = new Objective();
        
        ObjectiveWrapperBean wrapperBean1 = new ObjectiveWrapperBean(newObjective1);
        wrapperBean1.setLabel(label);
        wrapperBean1.setParentDesc(label);
        wrapperBean1.setSelectedParentId(new Long(6));
        wrapperBean1.setDescription(description);

        set.addObjective(wrapperBean1);
        // use upper case to make sure
        final Objective newObjective2 = new Objective();

        ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean(newObjective2);
        wrapperBean.setParentDesc(label);
        wrapperBean.setSelectedParentId(new Long(6));
        wrapperBean.setDescription(description + "2");
        wrapperBean.setLabel(label);
        set.addObjective(wrapperBean);

        Errors errors = new DataBinder(set, "command").getBindingResult();
        objectiveValidator.validateObjectiveSet(set, errors);
        assertErrorCode(errors, "error.duplicate.label", "objectives[1].label");
    }

    public void testValidate() throws Exception {
        ObjectiveSetFormBean bean = new ObjectiveSetFormBean();
        ObjectiveSet set = new ObjectiveSet();
        bean.setObjectiveSet(set);
        Objective objective = new Objective();
        ObjectiveWrapperBean wrapperBean = new ObjectiveWrapperBean(objective);
        wrapperBean.setSelectedParentId(new Long(2));
        wrapperBean.setParentDesc("desc");
        bean.addObjective(wrapperBean);

        Errors errors = new DataBinder(bean, "command").getBindingResult();
        objectiveValidator.validateObjectiveSet(bean, errors);
        assertErrorCount(2, errors);
        assertErrorCode(errors, "error.required.field", "objectives[0].label");
        assertErrorCode(errors, "error.required.field", "objectives[0].description");
    }

    private ObjectiveValidator objectiveValidator;
}