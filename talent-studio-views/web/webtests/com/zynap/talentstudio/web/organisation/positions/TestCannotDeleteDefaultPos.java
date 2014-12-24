package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.talentstudio.web.ZynapWebGenTestCase;

public class TestCannotDeleteDefaultPos extends ZynapWebGenTestCase {

    public TestCannotDeleteDefaultPos() {
        super();
    }


    public void testCannotDeleteDefaultPos() throws Exception {
        String value;
        String xpath;


        value = "http://localhost:7001/talent-studio/talentarena/home.htm?a=MYZYNAPMODULE&a_tab=MYZYNAPMODULE";
        beginAt(value);

        value = "Talent Studio - Home";

        assertTitleEquals(value);

        value = "";
        xpath = "A[@ID=\"a_ORGANISATIONMODULE\"]";

        assertNotNull(xpath);
        clickLink(xpath, value);


        value = "Talent Studio - Organisation Home";
        assertTitleEquals(value);


        value = "http://localhost:7001/talent-studio/orgbuilder/listposition.htm";
        beginAt(value);


        value = "Talent Studio - Position Search";
        assertTitleEquals(value);


        value = "_target3";
        xpath = "*/FORM[@ID=\"position\"]/*/INPUT[@NAME=\"_target3\" and @VALUE=\"Search\"]";
        assertNotNull(xpath);
        submitForm(xpath, value);


        value = "Talent Studio - Position Search";
        assertTitleEquals(value);


        value = "Default Position";
        assertTextPresent(value);


        value = "";
        xpath = "TABLE[@ID=\"positiontable\"]/TBODY[1]/TR[1]/TD[1]/A[@CDATA=\"Default Position\"]";
        assertNotNull(xpath);
        clickLink(xpath, value);


        value = "Talent Studio - Position Search";
        assertTitleEquals(value);
        assertButtonNotPresent("btn_deletepos");

        value = "http://localhost:7001/talent-studio/orgbuilder/browseposition.htm";
        beginAt(value);

        value = "Talent Studio - Browse Positions";
        assertTitleEquals(value);

        assertButtonNotPresent("btn_deletepos");


        value = "Current Holder";
        assertTextPresent(value);

        logger.debug("Test completed");
    }
}
