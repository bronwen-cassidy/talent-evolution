package com.zynap.talentstudio.preferences;

import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreference;

/**
 * User: amark
 * Date: 08-Apr-2005
 * Time: 08:36:37
 */
public abstract class BasePreferenceTestCase extends AbstractHibernateTestCase {

    protected DomainObjectPreference ouDomainObjectPreference;

    protected static final String COLOR_ATTR_NAME = "color";
    protected static final String TITLE_PREF_NAME = "title";
    protected static final String POSITION_PRIMARY_ASSOCIATION_PREF_NAME = "primaryAssociation";

    protected static final String LINE_HEIGHT_ATTR_NAME = "line-height";
    protected static final String LABEL_PREF_NAME = "label";
}
