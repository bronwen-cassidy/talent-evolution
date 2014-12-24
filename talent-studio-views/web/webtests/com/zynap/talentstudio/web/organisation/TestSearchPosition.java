package com.zynap.talentstudio.web.organisation;

import com.meterware.httpunit.WebForm;
import com.zynap.talentstudio.web.ZynapDbUnitWebTestCase;

/**
 * User: amark
 * Date: 19-Sep-2005
 * Time: 13:07:01
 */
public class TestSearchPosition extends ZynapDbUnitWebTestCase {

    public TestSearchPosition() {
        super();
    }

    public void testNoResults() throws Exception {

        beginAt(POSITION_SEARCH_URL);
        assertAppTitleEquals(PAGE_TITLE);

        // engineering OU has no positions
        WebForm form = getForm(SEARCH_FORM_NAME);
        setHiddenField(form, BrowseArtefactConstants.ORGUNIT_SEARCH_FIELD_NAME, ENGINEERING_OU_ID);
        form.submit();

        checkForErrors();
        assertAppTitleEquals(PAGE_TITLE);

        assertTextInTable(BrowseArtefactConstants.NO_RESULTS_TABLE, "Nothing found to display.");
    }

    public void testBasicSearch() throws Exception {

        beginAt(POSITION_SEARCH_URL);
        assertAppTitleEquals(PAGE_TITLE);

        WebForm form = getForm(SEARCH_FORM_NAME);
        setHiddenField(form, BrowseArtefactConstants.ORGUNIT_SEARCH_FIELD_NAME, DEFAULT_ORG_UNIT_ID);
        form.submit();

        checkForErrors();
        assertAppTitleEquals(PAGE_TITLE);

        assertTextInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, DEFAULT_POSITION_LABEL);
        assertTextNotInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, HEAD_OF_MARKETING_LABEL);

        form = getForm(SEARCH_FORM_NAME);
        setHiddenField(form, BrowseArtefactConstants.ORGUNIT_SEARCH_FIELD_NAME, MARKETING_OU_ID);
        form.submit();
        checkForErrors();

        assertTextNotInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, DEFAULT_POSITION_LABEL);
        assertTextInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, HEAD_OF_MARKETING_LABEL);
    }

    public void testSearchForInactivePositions() throws Exception {

        beginAt(POSITION_SEARCH_URL);
        assertAppTitleEquals(PAGE_TITLE);

        WebForm form = getForm(SEARCH_FORM_NAME);
        form.setParameter("filter.active", "inactive");
        form.submit();

        checkForErrors();
        assertAppTitleEquals(PAGE_TITLE);

        // head of IT is marked as inactive in test data - others are all active
        assertTextNotInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, DEFAULT_POSITION_LABEL);
        assertTextNotInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, HEAD_OF_MARKETING_LABEL);
        assertTextInTable(BrowseArtefactConstants.POSITION_SEARCH_RESULTS_TABLE, HEAD_OF_IT_LABEL);
    }

    public void testNavigation() throws Exception {

        beginAt(POSITION_SEARCH_URL);

        WebForm form = getForm(SEARCH_FORM_NAME);
        setHiddenField(form, BrowseArtefactConstants.ORGUNIT_SEARCH_FIELD_NAME, MARKETING_OU_ID);
        form.submit();
        checkForErrors();

        // check that head of marketing has link to head of accounts
        assertTextInTable(BrowseArtefactConstants.POSITION_PROVISIONAL_REPORTING_TABLE, HEAD_OF_ACCOUNTS_LABEL);

        // click on link to head of accounts
        clickLinkWithText(HEAD_OF_ACCOUNTS_LABEL);
        checkForErrors();

        // check now viewing head of accounts
        assertTextInTable(BrowseArtefactConstants.DETAILS_TABLE, HEAD_OF_ACCOUNTS_LABEL);

        // check that head of accounts has link to default position
        assertTextInTable(BrowseArtefactConstants.POSITION_DIRECT_REPORTING_TABLE, DEFAULT_POSITION_LABEL);

        // click on link to default position
        clickLinkWithText(DEFAULT_POSITION_LABEL);
        checkForErrors();

        // check now viewing default position
        assertTextInTable(BrowseArtefactConstants.DETAILS_TABLE, DEFAULT_POSITION_LABEL);

        // now click back button - should now be back on head of accounts page
        clickButton(BACK_BUTTON_ID);
        checkForErrors();
        assertTextInTable(BrowseArtefactConstants.DETAILS_TABLE, HEAD_OF_ACCOUNTS_LABEL);

        // now click back button - should now be back on head of marketing page
        clickButton(BACK_BUTTON_ID);
        checkForErrors();
        assertTextInTable(BrowseArtefactConstants.DETAILS_TABLE, HEAD_OF_MARKETING_LABEL);
    }

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    private static final String PAGE_TITLE = "Position Search";
    private static final String POSITION_SEARCH_URL = "orgbuilder/listposition.htm";

    private static final String SEARCH_FORM_NAME = "position";

    protected static final String HEAD_OF_MARKETING_LABEL = "Head Of Marketing";
    protected static final String HEAD_OF_ACCOUNTS_LABEL = "Head Of Accounts";
    protected static final String HEAD_OF_IT_LABEL = "Head of IT";

    protected static final String MARKETING_OU_ID = "2";
    protected static final String ENGINEERING_OU_ID = "5";
}
