package com.zynap.talentstudio.web.organisation.organisationunit;

/**
 * Validator for organsaition unit controllers.
 *
 * User: aandersson
 * Date: 09-Feb-2004
 * Time: 10:56:06
 */
import junit.framework.TestCase;

import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class TestOrganisationUnitValidator extends TestCase {

    protected void setUp() throws Exception {
        organisationUnitValidator = new OrganisationUnitValidator();
    }

    protected void tearDown() throws Exception {
        organisationUnitValidator = null;
    }



    public void testSupports() throws Exception {
        assertTrue(organisationUnitValidator.supports(OrganisationUnitWrapperBean.class));
    }

    public void testValidate_LabelEmpty() throws Exception {
        OrganisationUnitWrapperBean ou = new OrganisationUnitWrapperBean(new OrganisationUnit(new Long(9)));
        ou.setComments("some comments");
        ou.setParentId(new Long(0));

        DataBinder binder = new DataBinder(ou, "organisation");
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("label", ""));
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        organisationUnitValidator.validate(ou, errors);
        try {
			binder.close();
			fail("Should have thrown BindException");
		}
		catch (BindException ex) {
            FieldError labelError = ex.getFieldError("label");
            assertEquals("label", labelError.getField());
            assertEquals("", labelError.getRejectedValue());
            assertEquals("error.organisation.label.required", labelError.getCode());            
        }
    }

    public void testValidate_ParentInvalid() throws Exception {
        OrganisationUnit organisationUnit = new OrganisationUnit(new Long(9), "Happy Days");
        organisationUnit.setParent(new OrganisationUnit(new Long(0)));

        OrganisationUnitWrapperBean ou = new OrganisationUnitWrapperBean(organisationUnit);
        ou.setComments("some comments");
        ou.setParentId(null);

        DataBinder binder = new DataBinder(ou, "organisation");
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.addPropertyValue(new PropertyValue("parentId",null));
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        organisationUnitValidator.validate(ou, errors);
        try {
			binder.close();
			fail("Should have thrown BindException");
		}
		catch (BindException ex) {
            FieldError labelError = ex.getFieldError("parentId");
            assertEquals(null, labelError.getRejectedValue());
            assertEquals("error.parent.orgunit.required", labelError.getCode());
        }
    }

    public void testValidate_Top() throws Exception {
        OrganisationUnitWrapperBean ou = new OrganisationUnitWrapperBean(new OrganisationUnit(new Long(0), "Happy Days"));
        ou.setComments("some comments");
        ou.setParentId(new Long(-1));

        DataBinder binder = new DataBinder(ou, "organisation");
        MutablePropertyValues pvs = new MutablePropertyValues();
        binder.bind(pvs);
        Errors errors = binder.getBindingResult();
        organisationUnitValidator.validate(ou, errors);
        assertEquals(0, errors.getErrorCount());
    }

    private OrganisationUnitValidator organisationUnitValidator;
}