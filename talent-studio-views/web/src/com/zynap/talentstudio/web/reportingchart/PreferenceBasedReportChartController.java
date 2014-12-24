/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.reportingchart;

import com.zynap.talentstudio.preferences.IPreferencesManager;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.web.reportingchart.settings.ChartSettingsMultiController;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller used for displaying advanced reporting structure that uses report chart preferences.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PreferenceBasedReportChartController extends ReportChartController {

    protected Preference getDomainObjectPreferenceCollection(HttpServletRequest request) throws Exception {
        final Long preferenceId = ChartSettingsMultiController.getPreferenceId(request);
        return getPreferencesManager().getPreference(preferenceId);
    }

    public IPreferencesManager getPreferencesManager() {
        return preferencesManager;
    }

    public void setPreferencesManager(IPreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }

    private IPreferencesManager preferencesManager;
}
