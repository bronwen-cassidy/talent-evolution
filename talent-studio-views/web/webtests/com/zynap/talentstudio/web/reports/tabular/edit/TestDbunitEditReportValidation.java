package com.zynap.talentstudio.web.reports.tabular.edit;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

import com.zynap.talentstudio.web.ZynapDbUnitWebGenTestCase;

public class TestDbunitEditReportValidation extends ZynapDbUnitWebGenTestCase {

    public TestDbunitEditReportValidation() {
        super();
    }

    protected String getDataSetFileName() {
        return "dbunitEditReportValidation-test-data.xml";
    }

    public void testDbunitEditReportValidation() throws Exception {
        HttpUnitOptions.setScriptingEnabled(false);
        String value;
        String xpath;


        value = "http://localhost:7001/talent-studio/analysis/listreports.htm?navigator.notSubmit=true";
        beginAt(value);

        value = "Talent Studio - Tabular Reports";
        assertTitleEquals(value);

        value = "All Positions Key Colored";
        assertTextPresent(value);

        value = "";
        xpath = "TABLE[@ID=\"reports\"]/TBODY[1]/TR[1]/TD[1]/A[@CDATA=\"All Positions Key Colored\"]";
        assertNotNull(xpath);
        clickLink(xpath, value);

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);

        value = "amr_83";
        assertTextPresent(value);

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);

        value = "is coloured cell";
        assertTextPresent(value);

        value = "";
        xpath = "A[@ID=\"a_runoptions\"]";
        clickLink(xpath, value);

        value = "_target0";
        xpath = "*/FORM[@ID=\"runreport\"]/*/INPUT[@NAME=\"_target0\" and @VALUE=\"Run\"]";
        submitForm(xpath, value);

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);

        value = "Company Manager 1";
        assertTextPresent(value);

        value = "";
        xpath = "A[@ID=\"a_runoptions\"]";
        clickLink(xpath, value);

        WebForm form = getForm("_edit");
        form.submit();
        value = "Talent Studio - Edit Tabular Report";
        assertTitleEquals(value);

        value = "";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@NAME=\"label\"]";
        setInputParameter(xpath, value);

        value = "_target1";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"target1\"]";
        submitForm(xpath, value);

        value = "Talent Studio - Edit Tabular Report";
        assertTitleEquals(value);

        value = "Label is a required field.";
        assertTextPresent(value);

        value = "Ben's Private Population";
        assertTextNotPresent(value);

        value = "_cancel";
        form = getForm(value);
        form.submit();

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);


        value = "Organisation Unit";
        assertTextPresent(value);

        value = "_back";
        form = getForm(value);
        form.submit();

        value = "Talent Studio - Tabular Reports";
        assertTitleEquals(value);

        value = "Tabular Reports";
        assertTextPresent(value);

        value = "";
        xpath = "TABLE[@ID=\"reports\"]/TBODY[1]/TR[1]/TD[1]/A[@CDATA=\"All Positions Key Colored\"]";
        clickLink(xpath, value);

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);

        value = "_edit";
        form = getForm(value);
        form.submit();

        value = "Talent Studio - Edit Tabular Report";
        assertTitleEquals(value);

        value = "_target1";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"target1\"]";
        submitForm(xpath, value);

        value = "Talent Studio - Edit Tabular Report";
        assertTitleEquals(value);

        value="";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"columns[0].label\"]";
        setInputParameter(xpath, value);

        value = "_finish";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@NAME=\"_finish\" and @VALUE=\"Save\"]";
        submitForm(xpath, value);

        value = "Label is a required field.";
        assertTextPresent(value);

        form = getForm("_cancel");
        form.submit();

        value = "Talent Studio - Tabular Report";
        assertTitleEquals(value);

        value = "";
        xpath = "A[@ID=\"a_runoptions\"]";
        clickLink(xpath, value);

        form = getForm("_csvexport");
        setHiddenField(form, "report_id", "1");
        setHiddenField(form, "id", "-1");
        setHiddenField(form, "sort_order", "1");
        setHiddenField(form, "order_by", "");
        form.submit();

        // get the csv export form
        form = getForm("export");
        form.submit();

        // gets the info from the response for testing a download file
        WebResponse frameContents = getDownloadFileResponse("_top");
        String contentType = frameContents.getContentType();
        assertEquals("text/csv", contentType);
        assertTrue(frameContents.getContentLength() > 0);

        // returns the contents of the download file
        String text = frameContents.getText();
        assertTrue(text.indexOf(TARGET_POSITION_TITLE) > 0);

        logger.debug("Test completed");
    }

    private static final String TARGET_POSITION_TITLE = "Company Manager 2";
}
