package com.zynap.talentstudio.web.reports.tabular.add;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebRequest;

import com.zynap.talentstudio.web.ZynapDbUnitWebGenTestCase;

public class TestDbunitAddReportValidation extends ZynapDbUnitWebGenTestCase {

    public TestDbunitAddReportValidation() {
        super();
    }

    protected String getDataSetFileName() {
        return "dbunitAddReportValidation-test-data.xml";
    }

    public void testDbunitAddReportValidation() throws Exception {
        HttpUnitOptions.setScriptingEnabled( false );
        String value;
        String xpath;

        value = "http://localhost:7001/talent-studio/analysis/listreports.htm";
        beginAt(value);

        value = "Talent Studio - Tabular Reports";
        assertTitleEquals(value);

        WebForm form = getForm("_add");
        form.submit();


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "Ben's Private Population";
        assertTextNotPresent(value);


        value = "-1";
        xpath = "*/FORM[@ID=\"reports\"]/*/SELECT[@NAME=\"populationId\"]";
        assertNotNull(xpath);
        setInputParameter(xpath, value);


        value = "_target1";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"target1\"]";
        assertNotNull(xpath);
        submitForm(xpath, value);

        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "Label is a required field.";
        assertTextPresent(value);


        value = "Scope is a required field.";
        assertTextPresent(value);

        form = getForm("_cancel");
        form.submit();

        value = "Talent Studio - Tabular Reports";
        assertTitleEquals(value);

        WebForm addform2 = getForm("_add");
        addform2.submit();


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "Test Report Errors";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@NAME=\"label\"]";
        assertNotNull(xpath);
        setInputParameter(xpath, value);


        value = "-2";
        xpath = "*/FORM[@ID=\"reports\"]/*/SELECT[@NAME=\"populationId\"]";
        assertNotNull(xpath);
        setInputParameter(xpath, value);


        value = "_target1";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"target1\"]";
        assertNotNull(xpath);
        submitForm(xpath, value);


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "Scope is a required field.";
        assertTextPresent(value);


        value = "Public";
        xpath = "*/FORM[@ID=\"reports\"]/*/SELECT[@NAME=\"access\"]";
        assertNotNull(xpath);
        setInputParameter(xpath, value);


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "_target1";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@ID=\"target1\"]";
        assertNotNull(xpath);
        submitForm(xpath, value);


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);

        WebForm reportForm = getForm("reports");
        WebRequest webRequest = reportForm.newUnvalidatedRequest();
        webRequest.setParameter("_target0", "_target0");
        webRequest.setParameter("_back", "_back");
        Button button = getButtonByIdOrName(reportForm, "_back");
        button.click();


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);

        // this not valid as the arena has this name need a different assertion to validate on the correct page
        value = "Performance Management";
        assertTextPresent(value);

        reportForm = getForm("reports");
        reportForm.submit();


        value = "Talent Studio - Add Tabular Report";
        assertTitleEquals(value);


        value = "Columns";
        assertTextPresent(value);


        value = "_finish";
        xpath = "*/FORM[@ID=\"reports\"]/*/INPUT[@NAME=\"_finish\" and @VALUE=\"Save\"]";
        assertNotNull(xpath);
        submitForm(xpath, value);


        value = "Talent Studio - Add Tabular Report";
        xpath = "*";

        assertNotNull(xpath);


        assertTitleEquals(value);


        value = "At least one column must be defined before saving.";
        assertTextPresent(value);


        WebForm cancelForm = getForm("_cancel");
        cancelForm.submit();

        value = "Talent Studio - Tabular Reports";
        assertTitleEquals(value);


        value = "Tabular Reports";
        assertTextPresent(value);


        logger.debug("Test completed");
    }
}
