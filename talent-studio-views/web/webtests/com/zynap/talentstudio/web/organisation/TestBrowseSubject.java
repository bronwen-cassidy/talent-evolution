package com.zynap.talentstudio.web.organisation;

import com.meterware.httpunit.WebForm;

import com.zynap.talentstudio.web.ZynapDbUnitWebTestCase;

/**
 * User: amark
 * Date: 11-May-2005
 * Time: 12:32:06
 */
public class TestBrowseSubject extends ZynapDbUnitWebTestCase {

    protected String getDataSetFileName() {
        return "test-subject-data.xml";
    }

    public void testOrgBuilderArena() throws Exception {

        final String browseSubjectUrl = "orgbuilder/browsesubject.htm";

        // start at browse subject - will have holder of default position
        beginAt(browseSubjectUrl);

        // check tabs are present
        assertElementPresent(BrowseArtefactConstants.DETAILS_SPAN_NAME);

        // check delete button present as we are in org builder
        assertButtonPresent(DELETE_BUTTON_ID);
    }

    public void testOrgUnitNoSubjects() throws Exception {

        final String browseSubjectUrl = "orgbuilder/browsesubject.htm";

        // start at browse subject - will have no subjects
        beginAt(browseSubjectUrl);

        // go to OU that has no active subjects
        WebForm form = getForm("navigationOrgUnitSection");
        setHiddenField(form, BrowseArtefactConstants.OU_ID_FIELD_NAME, MARKETING_OU_ID);
        form.submit();

        // check that the correct message is displayed
        assertTextInTable("ib_results", "No people available.");

        // check no tabs are present as we have no subjects
        assertElementNotPresent(BrowseArtefactConstants.DETAILS_SPAN_NAME);

        // check delete button not present
        assertButtonNotPresent(DELETE_BUTTON_ID);
    }

    private static final String MARKETING_OU_ID = "2";
    private static final String DELETE_BUTTON_ID = "btn_deletesubject";
}
