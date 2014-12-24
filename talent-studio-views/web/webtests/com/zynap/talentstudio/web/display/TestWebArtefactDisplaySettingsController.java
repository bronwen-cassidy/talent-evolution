/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.meterware.httpunit.WebForm;

import com.zynap.talentstudio.web.ZynapWebTestCase;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestWebArtefactDisplaySettingsController extends ZynapWebTestCase {

    public void testArtefactListVisible() throws Exception {
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        assertLinkPresent(PERSON_ARTEFACT_LINK_ID);
        assertLinkPresent(POSITION_ARTEFACT_LINK_ID);
    }

    public void testConfigTabPresent() throws Exception {
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        clickLink(PERSON_ARTEFACT_LINK_ID);
        assertTextPresent(PERSON_LABEL);
    }

    public void testConfigItemTabPresent() throws Exception {
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        clickLink(PERSON_ARTEFACT_LINK_ID);
        // The first item for the person configuration
        clickLink("disI1");
        // the tab value should be the items value
        assertTextPresent("Executive Summary");
    }

    public void testEditButtonPresent() throws Exception {
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        clickLink(PERSON_ARTEFACT_LINK_ID);
        clickLink("disI1");
        assertTextPresent("Edit");
        assertFormPresent("editDCI");
    }

    public void testEditFieldEmptyErrorMessage() throws Exception {
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        beginAt(ARTEFACT_VIEW_SETTINGS_URL);
        clickLink(PERSON_ARTEFACT_LINK_ID);
        clickLink("disI1");

        assertFormPresent("editDCI");

        WebForm form = getForm("editDCI");
        form.submit();

        assertFormPresent("cancelRes");
        assertFormPresent("reports");

        WebForm reportForm = getForm("reports");

        reportForm.setParameter("displayConfigItem.label", " ");
        reportForm.submit();

        assertErrorPresent("Label is a required field.");
        WebForm cancelForm = getForm("cancelRes");
        cancelForm.submit();
        assertFormNotPresent("reports");
    }

    private static final String ARTEFACT_VIEW_SETTINGS_URL = "/admin/displayconfigsettings.htm";
    public static final String PERSON_ARTEFACT_LINK_ID = "dis1";
    public static final String POSITION_ARTEFACT_LINK_ID = "dis2";
    public static final String PERSON_LABEL = "Person";
}