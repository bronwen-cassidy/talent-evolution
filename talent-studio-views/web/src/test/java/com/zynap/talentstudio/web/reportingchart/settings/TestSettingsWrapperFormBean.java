package com.zynap.talentstudio.web.reportingchart.settings;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.analysis.reports.IReportService;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.PreferenceConstants;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.web.preferences.AttributePreferenceAccessor;
import com.zynap.talentstudio.web.preferences.AttributePreferenceModifier;

import java.util.Collection;

public class TestSettingsWrapperFormBean extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        _preferenceCollection = (DomainObjectPreferenceCollection) getBean("reportingChartPreferences");

        Preference preference = new Preference(null, PreferenceConstants.USER_PREFERENCE_TYPE, _preferenceCollection, ADMINISTRATOR_USER_ID);

        IReportService reportService = (IReportService) getBean("reportService");

        final Collection menuSections = reportService.getMenuSections();
        final Collection homeMenuSections = reportService.getHomePageReportMenuSections();

        String menuItemURL = "runchart.htm";
        _settingsWrapperFormBean = new SettingsWrapperFormBean(preference, menuSections, homeMenuSections, menuItemURL);
    }

    public void testSetOuShow() throws Exception {
        String labelKey = ViewSettingsWrapperBean.LABEL_KEY;
        Class domainObjectClass = OrganisationUnit.class;
        boolean current = AttributePreferenceAccessor.get(_preferenceCollection, domainObjectClass, labelKey).isDisplayable();
        boolean actual = !current;

        _settingsWrapperFormBean.setOuShow(actual);
        assertEquals(actual, _settingsWrapperFormBean.isOuShow());
        AttributePreferenceModifier.modifyAttributePreference(_preferenceCollection, domainObjectClass.getName(), labelKey, labelKey, actual);
        assertEquals(actual, AttributePreferenceAccessor.get(_preferenceCollection, domainObjectClass, labelKey).isDisplayable());
    }

    public void testSetOuColor() throws Exception {
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, OrganisationUnit.class, ViewSettingsWrapperBean.LABEL_KEY).getFormattingInfo();
        String current = formattingInfo.get(ViewSettingsWrapperBean.FG_COLOR_FORMAT_ATTR_KEY) + ":" + formattingInfo.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY);
        String actual = "#ff9900:black";
        _settingsWrapperFormBean.setOuColor(actual);
        String expected = _settingsWrapperFormBean.getOuColor();
        assertNotSame(current, expected);
        assertEquals(actual, expected);
    }

    public void testSetOuColorViaModifier() {
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, OrganisationUnit.class, ViewSettingsWrapperBean.LABEL_KEY).getFormattingInfo();
        String current = formattingInfo.get(ViewSettingsWrapperBean.FG_COLOR_FORMAT_ATTR_KEY) + ":" + formattingInfo.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY);
        String actual = "#ff9900:black";
        assertNotSame(current, actual);
        AttributePreferenceModifier.modifyAttributePreference(_preferenceCollection, OrganisationUnit.class.getName(),
                ViewSettingsWrapperBean.LABEL_KEY,
                new String[]{ViewSettingsWrapperBean.FG_COLOR_FORMAT_ATTR_KEY, ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY}, new String[]{"#ff9900", "black"});
        FormattingInfo formattingInfo1 = AttributePreferenceAccessor.get(_preferenceCollection, OrganisationUnit.class, ViewSettingsWrapperBean.LABEL_KEY).getFormattingInfo();
        String expected = formattingInfo.get(ViewSettingsWrapperBean.FG_COLOR_FORMAT_ATTR_KEY) + ":" + formattingInfo1.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY);
        assertEquals(actual, expected);
    }

    public void testSetPositionPrimaryAssociationShow() throws Exception {
        String labelKey = ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY;
        Class domainObjectClass = Position.class;
        boolean current = AttributePreferenceAccessor.get(_preferenceCollection, domainObjectClass, labelKey).isDisplayable();
        boolean actual = !current;

        _settingsWrapperFormBean.setPositionPrimaryAssociationShow(actual);
        assertEquals(actual, _settingsWrapperFormBean.isPositionPrimaryAssociationShow());
        AttributePreferenceModifier.modifyAttributePreference(_preferenceCollection, domainObjectClass.getName(), labelKey, null, actual);
        assertEquals(actual, AttributePreferenceAccessor.get(_preferenceCollection, domainObjectClass, labelKey).isDisplayable());
    }

    public void testSetPositionPrimaryAssociationColor() throws Exception {
        String actual = "#66ff33:black";
        String bgNewValue = "#66ff33";
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY).getFormattingInfo();
        String bgCurrent = formattingInfo.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY).getValue();
        assertNotSame(bgCurrent, bgNewValue);
        _settingsWrapperFormBean.setPositionPrimaryAssociationColor(actual);

        FormattingInfo formattingInfo1 = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY).getFormattingInfo();
        String expected = formattingInfo1.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY).getValue();
        assertEquals(bgNewValue, expected);
    }

    public void testSetPositionSecondaryAssociationColor() throws Exception {
        String actual = "#66ffff:black";
        String bgNewValue = "#66ffff";
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.SECONDARY_ASSOCIATION_KEY).getFormattingInfo();
        String bgCurrent = formattingInfo.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY).getValue();
        assertNotSame(bgCurrent, bgNewValue);
        _settingsWrapperFormBean.setPositionSecondaryAssociationColor(actual);

        FormattingInfo formattingInfo1 = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.SECONDARY_ASSOCIATION_KEY).getFormattingInfo();
        String expected = formattingInfo1.get(ViewSettingsWrapperBean.BG_COLOR_FORMAT_ATTR_KEY).getValue();
        assertEquals(bgNewValue, expected);
    }

    public void testSetPositionPrimaryAssociationLine() throws Exception {
        String actual = "4px";
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY).getFormattingInfo();
        String lineHeight = formattingInfo.get(ViewSettingsWrapperBean.LINE_HEIGHT).getValue();

        assertNotSame(lineHeight, actual);
        _settingsWrapperFormBean.setPositionPrimaryAssociationLine(actual);
        FormattingInfo formattingInfo1 = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.POSITION_PRIMARY_ASSOCIATION_KEY).getFormattingInfo();
        String expected = formattingInfo1.get(ViewSettingsWrapperBean.LINE_HEIGHT).getValue();
        assertEquals(actual, expected);
    }

    public void testSetPositionSecondaryAssociationLine() throws Exception {
        String actual = "8px";
        FormattingInfo formattingInfo = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.SECONDARY_ASSOCIATION_KEY).getFormattingInfo();
        String lineHeight = formattingInfo.get(ViewSettingsWrapperBean.LINE_HEIGHT).getValue();

        assertNotSame(lineHeight, actual);
        _settingsWrapperFormBean.setPositionSecondaryAssociationLine(actual);
        FormattingInfo formattingInfo1 = AttributePreferenceAccessor.get(_preferenceCollection, Position.class, ViewSettingsWrapperBean.SECONDARY_ASSOCIATION_KEY).getFormattingInfo();
        String expected = formattingInfo1.get(ViewSettingsWrapperBean.LINE_HEIGHT).getValue();
        assertEquals(actual, expected);
    }

    public void testGetDisplayPositionAttributes() throws Exception {

        IDynamicAttributeService attributeService = (IDynamicAttributeService) applicationContext.getBean("dynamicAttrService");

        Collection actual = attributeService.getAllActiveAttributes(Node.POSITION_UNIT_TYPE_, true);
        _settingsWrapperFormBean.setPositionExtendedAttributes(actual);
        Collection expected = _settingsWrapperFormBean.getDisplayPositionAttributes();
        
        // the three removed are primary, secondary, and the title which can never be deselected
        int allDisplayAttributeCountOnly = (expected.size() - 3);
        assertEquals(actual.size(), allDisplayAttributeCountOnly);
    }

    private SettingsWrapperFormBean _settingsWrapperFormBean;
    private DomainObjectPreferenceCollection _preferenceCollection;
}