/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.display;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.common.groups.Group;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.DisplayItemReport;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.security.roles.IRoleManager;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestDisplaySettingsValidator extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        displaySettingsValidator = (DisplaySettingsValidator) applicationContext.getBean("displaySettingsValidator");
        displayConfigService = (IDisplayConfigService) applicationContext.getBean("displayConfigService");
        IRoleManager roleManager = (IRoleManager) getBean("roleManager");
        final List<DisplayConfig> configs = displayConfigService.findAll();
        wrapper = new DisplayConfigWrapper(configs, roleManager.getActiveAccessRoles(), new ArrayList<Group>());
        binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
    }

    public void testSupports() throws Exception {
        assertTrue(displaySettingsValidator.supports(DisplayConfigWrapper.class));
    }

    public void testValidateNoReportsOK() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = findItem(displayConfig, PORTFOLIO_ITEM_CONTENT_TYPE);
        wrapper.setDisplayConfigItem(item);
        assertTrue(item.getReportItems().isEmpty());
        item.setLabel("New Label Test");
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateNoLabel() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = findItem(displayConfig, PORTFOLIO_ITEM_CONTENT_TYPE);
        wrapper.setDisplayConfigItem(item);
        assertTrue(item.getReportItems().isEmpty());
        item.setLabel(" ");
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertEquals(1, errors.getErrorCount());
        assertTrue(errors.getFieldError("displayConfigItem.label") != null);
    }

    public void testValidateOneReportOK() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        wrapper.setDisplayConfig(displayConfig);

        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();
        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());
        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        Column column = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        wrapper.setDisplayConfigItem(item);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateOneReportMissingReportLabel() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();
        wrapper.setDisplayConfigItem(item);

        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());
        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        Column column = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);
        report.setLabel(" ");
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertEquals(1, errors.getErrorCount());
        assertTrue(errors.getFieldError("reports[0].label") != null);
    }

    public void testValidateOneReportMissingColumnLabel() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();

        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());
        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        Column column = new Column(" ", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        wrapper.setDisplayConfigItem(item);

        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertEquals(1, errors.getErrorCount());
        assertTrue(errors.getFieldError("reports[0].columns[" + (report.getColumns().size() - 1) + "].label") != null);
    }

    public void testValidateManyReportsOK() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(3);
        final List reportItems = item.getReportItems();
        assertEquals(2, reportItems.size());

        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        Column column = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        DisplayItemReport itemReport1 = (DisplayItemReport) reportItems.get(1);
        Report report1 = itemReport1.getReport();
        Column column1 = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report1.addColumn(column1);

        wrapper.setDisplayConfigItem(item);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertFalse(errors.hasErrors());
    }

    public void testValidateManyReportsNoLabels() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(3);
        final List reportItems = item.getReportItems();
        assertEquals(2, reportItems.size());

        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        report.setLabel(" ");
        Column column = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        DisplayItemReport itemReport1 = (DisplayItemReport) reportItems.get(1);
        Report report1 = itemReport1.getReport();
        report1.setLabel(" ");
        Column column1 = new Column("Test Label", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report1.addColumn(column1);

        wrapper.setDisplayConfigItem(item);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertTrue(errors.hasErrors());
        assertEquals(2, errors.getErrorCount());
        assertNotNull(errors.getFieldError("reports[0].label"));
        assertNotNull(errors.getFieldError("reports[1].label"));
    }

    public void testValidateManyReportsNoColumnLabels() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.VIEW_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getDisplayConfigItems().get(3);
        final List reportItems = item.getReportItems();
        assertEquals(2, reportItems.size());

        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        report.setLabel(" ");
        Column column = new Column(" ", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        DisplayItemReport itemReport1 = (DisplayItemReport) reportItems.get(1);
        Report report1 = itemReport1.getReport();
        report1.setLabel(" ");
        Column column1 = new Column(" ", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report1.addColumn(column1);

        wrapper.setDisplayConfigItem(item);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertTrue(errors.hasErrors());
        assertEquals(4, errors.getErrorCount());
        assertNotNull(errors.getFieldError("reports[0].label"));
        assertNotNull(errors.getFieldError("reports[0].columns[" + (report.getColumns().size() - 1) + "].label"));
        assertNotNull(errors.getFieldError("reports[1].label"));
        assertNotNull(errors.getFieldError("reports[1].columns[" + (report1.getColumns().size() - 1) + "].label"));
    }

    public void testDuplicateColumns() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.EXECUTIVE_SUMMARY_TYPE);
        wrapper.setDisplayConfig(displayConfig);

        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();
        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());
        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        Column column = new Column("column1", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column);

        Column column2 = new Column("column2", "some.ref.value", DynamicAttribute.DA_TYPE_TEXTFIELD);
        report.addColumn(column2);

        wrapper.setDisplayConfigItem(item);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertTrue(errors.hasErrors());

        final ObjectError duplicateColumnError = errors.getGlobalError();
        assertEquals("error.duplicate.columns.defined", duplicateColumnError.getCode());
    }

    public void testValidateRequiredAttributesPresent() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.POSITION_UNIT_TYPE_, DisplayConfig.ADD_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();
        wrapper.setDisplayConfigItem(item);

        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());

        // remove all columns and check for validation errors
        wrapper.removeColumn(0, 0);
        wrapper.removeColumn(0, 0);
        wrapper.removeColumn(0, 0);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        ObjectError error = errors.getGlobalError();
        assertEquals("error.no.columns.defined", error.getCode());
        assertNotNull(error.getDefaultMessage());

        // add one of the required columns and check for validation errors
        Column titleColumn = new Column("position title", AnalysisAttributeHelper.POSITION_TITLE_ATTR, DynamicAttribute.DA_TYPE_TEXTFIELD);
        wrapper.addColumn(titleColumn, 0);
        binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
        errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        error = errors.getGlobalError();
        assertEquals("error.position.required.attributes.missing", error.getCode());

        // add other required column and check for no validation errors
        Column orgUnitColumn = new Column("position title", AnalysisAttributeHelper.ORG_UNIT_LABEL_ATTR, DynamicAttribute.DA_TYPE_TEXTFIELD);
        wrapper.addColumn(orgUnitColumn, 0);
        binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
        errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertEquals(0, errors.getGlobalErrorCount());
    }

    public void testValidateRequiredSubjectAttributesPresent() throws Exception {

        DisplayConfig displayConfig = displayConfigService.find(Node.SUBJECT_UNIT_TYPE_, DisplayConfig.ADD_TYPE);
        wrapper.setDisplayConfig(displayConfig);
        DisplayConfigItem item = displayConfig.getFirstDisplayConfigItem();
        final List reportItems = item.getReportItems();
        assertEquals(1, reportItems.size());

        // remove all columns
        DisplayItemReport itemReport = (DisplayItemReport) reportItems.get(0);
        Report report = itemReport.getReport();
        report.getColumns().clear();
        wrapper.setDisplayConfigItem(item);

        // add one of the required columns and check for validation errors
        Column firstNameColumn = new Column("firstname", AnalysisAttributeHelper.FIRST_NAME_ATTR, DynamicAttribute.DA_TYPE_TEXTFIELD);
        wrapper.addColumn(firstNameColumn, 0);
        binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
        Errors errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        ObjectError error = errors.getGlobalError();
        assertEquals("error.subject.required.attributes.missing", error.getCode());

        // add other required column and check for no validation errors
        Column secondNameColumn = new Column("secondname", AnalysisAttributeHelper.SECOND_NAME_ATTR, DynamicAttribute.DA_TYPE_TEXTFIELD);
        wrapper.addColumn(secondNameColumn, 0);
        binder = new DataBinder(wrapper, ControllerConstants.COMMAND_NAME);
        errors = binder.getBindingResult();
        displaySettingsValidator.validate(wrapper, errors);
        assertEquals(0, errors.getGlobalErrorCount());
    }

    private DisplayConfigItem findItem(DisplayConfig displayConfig, String itemContentType) {
        List items = displayConfig.getDisplayConfigItems();
        for (int i = 0; i < items.size(); i++) {
            DisplayConfigItem displayConfigItem = (DisplayConfigItem) items.get(i);
            if(itemContentType.equals(displayConfigItem.getContentType())) return displayConfigItem;
        }
        return null;
    }

    private DataBinder binder;
    private DisplaySettingsValidator displaySettingsValidator;
    private DisplayConfigWrapper wrapper;
    private IDisplayConfigService displayConfigService;

    private static final String PORTFOLIO_ITEM_CONTENT_TYPE = "PORTFOLIO";
}