package com.zynap.talentstudio.web.reportingchart;

import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import org.xml.sax.SAXException;

import com.zynap.talentstudio.web.ZynapWebTestCase;
import com.zynap.talentstudio.web.common.ParameterConstants;

import java.io.IOException;

/**
 * User: amark
 * Date: 10-May-2005
 * Time: 12:06:57
 *
 * Test the reporting chart in the org builder arena.
 */
public class TestReportBrowser extends ZynapWebTestCase {

    public void testOrgBuilderArena() throws Exception {
        beginAt("/orgbuilder/reportingchart.htm");
        navigateReportChart();
    }

    private void navigateReportChart() throws IOException, SAXException {

        WebForm form = getForm(VIEW_FORM_NAME);

        // select default position        
        assertTrue(form.hasParameterNamed(ParameterConstants.ARTEFACT_ID));
        setHiddenField(form, ParameterConstants.ARTEFACT_ID, DEFAULT_POSITION_ID);
        form.submit();

        // get link to position and click on it
        final WebLink posLink = getLink(new LinkHTMLElementPredicate(), "viewposition.htm");
        posLink.click();

        assertAppTitleEquals("Position");

        final WebForm backForm = getBackForm();
        backForm.submit();

        assertAppTitleEquals("Reporting Structure");

        // check for link to default org unit and click on it
        final WebLink orgUnitLink = getWebResponse().getMatchingLinks(new OrgUnitLinkPredicate(), DEFAULT_ORG_UNIT_ID)[0];
        assertTrue(orgUnitLink.getURLString().startsWith("vieworganisation.htm"));

        orgUnitLink.click();

        assertAppTitleEquals("Organisation Unit");
    }
}
