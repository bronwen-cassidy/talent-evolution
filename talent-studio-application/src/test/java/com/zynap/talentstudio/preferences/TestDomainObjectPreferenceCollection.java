package com.zynap.talentstudio.preferences;

/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 14:05:51
 */

import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.preferences.format.FormattingAttribute;
import com.zynap.talentstudio.preferences.format.FormattingInfo;
import com.zynap.talentstudio.preferences.properties.AttributePreference;
import com.zynap.talentstudio.preferences.properties.AttributeView;

public class TestDomainObjectPreferenceCollection extends BasePreferenceTestCase {

    DomainObjectPreferenceCollection domainObjectPreferenceCollection;

    protected void setUp() throws Exception {

        super.setUp();
        domainObjectPreferenceCollection = (DomainObjectPreferenceCollection) applicationContext.getBean("reportingChartPreferences");
    }

    public void testGetAttributeValue() throws Exception {

        String label = "RTSe";

        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setLabel(label);

        AttributeView attributeView = domainObjectPreferenceCollection.getPropertyValue(organisationUnit, LABEL_PREF_NAME);
        assertNotNull(attributeView);
        assertEquals(label, attributeView.getExpectedValue());
        FormattingInfo formattingInfo = attributeView.getFormattingInfo();
        assertNotNull(formattingInfo);
        assertEquals(2, formattingInfo.getFormattingAttributes().size());
        FormattingAttribute formattingAttribute = formattingInfo.get(COLOR_ATTR_NAME);
        assertNotNull(formattingAttribute);
        assertEquals("white", formattingAttribute.getValue());

        DomainObjectPreference positionPref = domainObjectPreferenceCollection.get(Position.class.getName());
        AttributePreference posAttrPref = positionPref.get(POSITION_PRIMARY_ASSOCIATION_PREF_NAME);
        assertNotNull(posAttrPref);
        assertNotNull(posAttrPref.getFormattingInfo().get(LINE_HEIGHT_ATTR_NAME));
    }

    public void testConditionalAttributeValue() throws Exception {

        final String title = "position1";
        Position position = new Position();
        position.setTitle(title);

        // get preference for position title
        AttributeView attributeView = domainObjectPreferenceCollection.getPropertyValue(position, TITLE_PREF_NAME);
        assertNotNull(attributeView);

        // set to be false and try again
        domainObjectPreferenceCollection.get(Position.class.getName()).addOrUpdate(TITLE_PREF_NAME, title, false);
        attributeView = domainObjectPreferenceCollection.getPropertyValue(position, TITLE_PREF_NAME);
        assertNull(attributeView);
    }
}