package com.zynap.talentstudio.web.organisation;

import com.meterware.httpunit.WebForm;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.zynap.common.util.XmlUtils;

import com.zynap.talentstudio.web.ZynapDbUnitWebTestCase;

/**
 * User: amark
 * Date: 09-May-2005
 * Time: 16:04:07
 */
public class TestBrowsePosition extends ZynapDbUnitWebTestCase {

    protected String getDataSetFileName() {
        return "test-data.xml";
    }

    public void testOrgBuilderArena() throws Exception {

        final String browsePositionUrl = "orgbuilder/browseposition.htm";

        // start at browse position - will display default position
        beginAt(browsePositionUrl);

        // check that default org unit is present in org unit picker
        assertFormElementPresent(BrowseArtefactConstants.OU_LABEL_FIELD_NAME);
        assertFormElementEquals(BrowseArtefactConstants.OU_ID_FIELD_NAME, DEFAULT_ORG_UNIT_ID);

        // details span should be the selected item, other spans should be blank
        checkElementStyle(BrowseArtefactConstants.DETAILS_SPAN_NAME, "display:inline");

        // check javascript on details tab
        final Element element = getDialog().getElement("details");
        final Node link =  XmlUtils.getFirstElement(element, "a");
        final NamedNodeMap attributes = link.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            final Node node = attributes.item(i);
            if (node.getNodeName().equalsIgnoreCase("href")) {
                assertEquals("javascript:switchTab('activeTab','details');", node.getNodeValue());
            }
        }        

        // check that portfolio tab is not present
        assertElementPresent(BrowseArtefactConstants.PORTFOLIO_SPAN_NAME);

        // check that default position is being displayed
        assertTextInTable(BrowseArtefactConstants.DETAILS_TABLE, DEFAULT_POSITION_LABEL);

        // check that delete button is not present as default position cannot be deleted
        assertButtonNotPresent(DELETE_BUTTON_ID);

        // check for form with hidden elements for navigation
        assertFormPresent("navigation");

        // check url for edit and submit
        WebForm form = getForm("details_frm");
        assertEquals("editposition.htm", form.getAction());
        form.submit();

        // check title
        assertAppTitleEquals("Edit Position");

        // cancel out and check we are back on the browse page
        form = getCancelForm();
        form.submit();

        assertAppTitleEquals("Browse Positions");
    }

    public void testSuccessionBuilderArena() throws Exception {

        final String browsePositionUrl = "succession/browseposition.htm";

        // start at browse position - will display default position
        beginAt(browsePositionUrl);

        // check document search form not present
        assertFormPresent(BrowseArtefactConstants.SEARCH_FORM_NAME);

        // check that portfolio tab is present
        assertElementPresent(BrowseArtefactConstants.PORTFOLIO_SPAN_NAME);
    }

    public void testTalentIdentifierArena() throws Exception {

        final String browsePositionUrl = "talentidentifier/browseposition.htm";

        // start at browse position - will display default position
        beginAt(browsePositionUrl);

        // check document search form present
        assertFormPresent(BrowseArtefactConstants.SEARCH_FORM_NAME);

        // check that portfolio tab is present
        assertElementPresent(BrowseArtefactConstants.PORTFOLIO_SPAN_NAME);
    }

    public void testInactivePositions() throws Exception {

        final String browsePositionUrl = "orgbuilder/browseposition.htm";

        // start at browse position - will display default position
        beginAt(browsePositionUrl);

        // go to IT OU that has no active positions
        WebForm form = getForm("navigationOrgUnitSection");
        setHiddenField(form, BrowseArtefactConstants.OU_ID_FIELD_NAME, IT_OU_ID);
        form.submit();

        // check that the correct message is displayed
        assertTextInTable("ib_posresults", "No positions available.");
    }

    private static final String IT_OU_ID = "4";
    private static final String DELETE_BUTTON_ID = "btn_deletepos";    
}
