package com.zynap.web.controller.orgbuilder;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.SubmitButton;

import com.zynap.talentstudio.web.ZynapWebTestCase;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

/**
 * Test class that tests editing, listing and viewing of org unit.
 * todo currently have no test to add an org unit as we cannot delete the newly added org unit!
 *
 * User: amark
 * Date: 01-Jun-2005
 * Time: 17:09:45
 */
public class TestOrganisationUnitAdministration extends ZynapWebTestCase {

    public void testBackViewOrganisation() throws Exception {

        beginAt(LIST_ORGUNITS_URL);
        final WebLink link = getLinkWithID("item_0");
        link.click();

        assertAppTitleEquals(VIEW_PAGE_TITLE);
        assertBackButtonPresent();

        final WebForm backForm = getBackForm();
        backForm.submit();
        assertResponseURLEquals(LIST_ORGUNITS_URL);
    }

    public void testCancelEditOrganisation() throws Exception {

        String url = ZynapWebUtils.buildURL(VIEW_ORGUNIT_URL, ParameterConstants.ORG_UNIT_ID_PARAM, DEFAULT_ORG_UNIT_ID);
        beginAt(url);
        assertAppTitleEquals(VIEW_PAGE_TITLE);

        // go to edit page
        final WebForm editLinkForm = getForm("_edit");
        editLinkForm.submit();

        // check title and form elements
        assertAppTitleEquals(EDIT_PAGE_TITLE);
        assertFormElementEquals(LABEL_FORM_ELEMENT_NAME, DEF_ORGUNIT_LABEL);

        // get cancel form and submit - should return to view url
        final WebForm cancelForm = getForm("_add");
        SubmitButton cancelButton = cancelForm.getSubmitButton("_cancel");
        cancelButton.click();
        assertResponseURLEquals(VIEW_ORGUNIT_URL);
    }

    public void testEditOrganisation() throws Exception {

        String url = ZynapWebUtils.buildURL(VIEW_ORGUNIT_URL, ParameterConstants.ORG_UNIT_ID_PARAM, DEFAULT_ORG_UNIT_ID);
        beginAt(url);
        assertAppTitleEquals(VIEW_PAGE_TITLE);

        // go to edit
        assertAppTitleEquals(VIEW_PAGE_TITLE);
        final WebForm editLinkForm = getForm("_edit");
        editLinkForm.submit();

        // set label to null and submit
        WebForm editForm = getForm("_add");
        setFormElement(LABEL_FORM_ELEMENT_NAME, "");
        SubmitButton editButton = editForm.getSubmitButton("_add");
        editButton.click();
        assertAppTitleEquals(EDIT_PAGE_TITLE);

        // change def org unit name
        final String newOrgUnitLabel = "Test Org Unit";
        setFormElement(LABEL_FORM_ELEMENT_NAME, newOrgUnitLabel);

        getForm("_add").getSubmitButton("_add").click();

        // go back to edit form and check label has changed
        editLinkForm.submit();
        assertFormElementEquals(LABEL_FORM_ELEMENT_NAME, newOrgUnitLabel);

        // reset to original value
        //editForm = getForm("_add");
        setFormElement(LABEL_FORM_ELEMENT_NAME, DEF_ORGUNIT_LABEL);
        //editForm.submit();
        getForm("_add").getSubmitButton("_add").click();
    }

    public void testAddOrganisationValidation() throws Exception {

        beginAt(ADD_ORGUNIT_URL);

        assertAppTitleEquals(ADD_PAGE_TITLE);
        assertFormElementEmpty(LABEL_FORM_ELEMENT_NAME);
        assertFormElementEmpty(COMMENTS_FORM_ELEMENT_NAME);

        // submit form - should stay on same page due to validation failures
        WebForm editForm = getForm("_add");
        SubmitButton addButton = editForm.getSubmitButton("_add");
        addButton.click();
        //editForm.submit();

        assertAppTitleEquals(ADD_PAGE_TITLE);
    }

    public void testCancelAddOrganisation() throws Exception {

        beginAt(ADD_ORGUNIT_URL);

        assertAppTitleEquals(ADD_PAGE_TITLE);
        assertFormElementEmpty(LABEL_FORM_ELEMENT_NAME);
        assertFormElementEmpty(COMMENTS_FORM_ELEMENT_NAME);

        // cancel and check you are returned to the add menu section
        SubmitButton cancelButton = getForm("_add").getSubmitButton("_cancel");
        cancelButton.click();
        assertResponseURLEquals(MENU_URL);
    }

    static final String LIST_ORGUNITS_URL = "orgbuilder/listorganisations.htm";
    static final String VIEW_ORGUNIT_URL = "orgbuilder/vieworganisation.htm";
    static final String ADD_ORGUNIT_URL = "orgbuilder/addorganisation.htm";

    static final String DEF_ORGUNIT_LABEL = "Default Org Unit";

    static final String COMMENTS_FORM_ELEMENT_NAME = "comments";

    static final String EDIT_PAGE_TITLE = "Edit Organisation Unit";
    static final String VIEW_PAGE_TITLE = "Organisation Unit";
    static final String ADD_PAGE_TITLE = "Add Organisation Unit";
}
