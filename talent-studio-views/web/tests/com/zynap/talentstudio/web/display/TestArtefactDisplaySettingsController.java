/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestArtefactDisplaySettingsController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        artefactDisplaySettingsController = (ArtefactDisplaySettingsController) getBean("artefactDisplaySettingsController");
        displayConfigWrapper = buildFormBackingObject();
    }

    public void testFormBackingObject() throws Exception {
        DisplayConfigWrapper wrapper = buildFormBackingObject();
        assertNotNull(wrapper.getDisplayConfigs());
    }

    public void testReferenceDataPage1Person() throws Exception {

        executePage(PERSON_CONFIG_ID, 1, ArtefactDisplaySettingsController.DISPLAY_CONFIG_PARAM);

        assertEquals(Node.SUBJECT_UNIT_TYPE_, displayConfigWrapper.getDisplayConfig().getNodeType());
        assertNull(displayConfigWrapper.getDisplayConfigItem());
    }

    public void testReferenceDataPage1Position() throws Exception {

        executePage(POSITION_CONFIG_ID, 1, ArtefactDisplaySettingsController.DISPLAY_CONFIG_PARAM);
        assertEquals(Node.POSITION_UNIT_TYPE_, displayConfigWrapper.getDisplayConfig().getNodeType());
        assertNull(displayConfigWrapper.getDisplayConfigItem());
    }

    public void testReferenceDataPage2Person() throws Exception {
        setUserSession(new Long(ROOT_USER_ID), mockRequest);
        executePage(PERSON_CONFIG_ID, 1, ArtefactDisplaySettingsController.DISPLAY_CONFIG_PARAM);

        DisplayConfig displayConfig = displayConfigWrapper.getDisplayConfig();
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(0);

        executePage(item.getId().toString(), 2, ArtefactDisplaySettingsController.DISPLAY_CONFIG_ITEM_PARAM);

        assertEquals(Node.SUBJECT_UNIT_TYPE_, displayConfigWrapper.getDisplayConfig().getNodeType());
        assertNotNull(displayConfigWrapper.getDisplayConfigItem());
        assertEquals(item, displayConfigWrapper.getDisplayConfigItem());
        assertEquals(false, displayConfigWrapper.isEditing());
        assertNotNull(displayConfigWrapper.getReports());
    }

    public void testReferenceDataEditConfigItemPickers() throws Exception {
        setUserSession(new Long(ROOT_USER_ID), mockRequest);
        // chose an view so we can then choose an item
        executePage(PERSON_CONFIG_ID, 1, ArtefactDisplaySettingsController.DISPLAY_CONFIG_PARAM);


        DisplayConfig displayConfig = displayConfigWrapper.getDisplayConfig();
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(0);
        // choose the item we can then edit
        executePage(item.getId().toString(), 2, ArtefactDisplaySettingsController.DISPLAY_CONFIG_ITEM_PARAM);

        // now hit the edit
    }

    public void testReferenceDataEditConfigItemNoPickers() throws Exception {
        setUserSession(new Long(ROOT_USER_ID), mockRequest);
        // chose an view so we can then choose an item
        executePage(PERSON_CONFIG_ID, 1, ArtefactDisplaySettingsController.DISPLAY_CONFIG_PARAM);


        DisplayConfig displayConfig = displayConfigWrapper.getDisplayConfig();
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(0);
        // choose the item we can then edit
        executePage(item.getId().toString(), 2, ArtefactDisplaySettingsController.DISPLAY_CONFIG_ITEM_PARAM);

        // now hit the edit
    }

    private void executePage(String id, int pageNum, String displayConfigParam) throws Exception {

        Errors errors = getErrors(displayConfigWrapper);
        mockRequest.addParameter(displayConfigParam, id);
        artefactDisplaySettingsController.referenceData(mockRequest, displayConfigWrapper, errors, pageNum);
    }

    private DisplayConfigWrapper buildFormBackingObject() throws Exception {
        return (DisplayConfigWrapper) artefactDisplaySettingsController.formBackingObject(null);
    }


    private ArtefactDisplaySettingsController artefactDisplaySettingsController;
    public static final String PERSON_CONFIG_ID = "-2";
    public static final String POSITION_CONFIG_ID = "-1";
    private DisplayConfigWrapper displayConfigWrapper;
}