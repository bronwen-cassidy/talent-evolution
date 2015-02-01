package com.zynap.talentstudio.web.reportingchart.settings;

/**
 * User: amark
 * Date: 22-Nov-2005
 * Time: 17:24:58
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.preferences.Preference;
import com.zynap.talentstudio.preferences.PreferenceConstants;
import com.zynap.talentstudio.preferences.domain.DomainObjectPreferenceCollection;
import com.zynap.talentstudio.web.utils.MissingRequestParameterException;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;

import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class TestChartSettingsMultiController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        chartSettingsMultiController = (ChartSettingsMultiController) getBean("chartSettingsMultiController");
    }

    public void testViewChartSettingsHandlerNoId() throws Exception {
        try {
            chartSettingsMultiController.viewChartSettingsHandler(mockRequest, mockResponse);
            fail("Must fail as preference id was not supplied");
        } catch (MissingRequestParameterException e) {
        }
    }

    public void testViewChartSettingsHandler() throws Exception {

        final String expectedViewName = PreferenceConstants.MASTER_REPORTING_CHART_VIEW;
        final Long id = chartSettingsMultiController.getPreferencesManager().getPreference(expectedViewName).getId();

        mockRequest.addParameter(ChartSettingsMultiController.PREFERENCE_ID, id.toString());
        final ModelAndView modelAndView = chartSettingsMultiController.viewChartSettingsHandler(mockRequest, mockResponse);

        assertEquals(ChartSettingsMultiController.DISPLAY_VIEW, modelAndView.getViewName());
        final Map model = getModel(modelAndView);
        ViewSettingsWrapperBean bean = (ViewSettingsWrapperBean) model.get(ChartSettingsMultiController.BEAN_KEY);
        assertNotNull(bean);
        assertEquals(expectedViewName, bean.getViewName());
        assertEquals(id, bean.getId());
    }

    public void testListSystemChartSettingsHandler() throws Exception {

        final String type = PreferenceConstants.SYSTEM_PREFERENCE_TYPE;

        final ModelAndView modelAndView = chartSettingsMultiController.listSystemChartSettingsHandler(mockRequest, mockResponse);
        checkPreferenceList(modelAndView, type);
    }

    public void testListUserChartSettingsHandler() throws Exception {

        // add a user preference as a test - use the root user to check that the master settings are not included
        final Long userId = ROOT_USER_ID;
        final String viewName = "testPref";
        final String type = PreferenceConstants.USER_PREFERENCE_TYPE;

        final Preference preference = new Preference(viewName, type, new DomainObjectPreferenceCollection(), userId);

        setUserSession(new UserSession(getSysUserPrincipal(), getArenaMenuHandler()), mockRequest);
        chartSettingsMultiController.getPreferencesManager().createPreference(preference);

        final ModelAndView modelAndView = chartSettingsMultiController.listUserChartSettingsHandler(mockRequest, mockResponse);
        checkPreferenceList(modelAndView, type);
    }

    private void checkPreferenceList(ModelAndView modelAndView, final String type) throws Exception {

        assertEquals(ChartSettingsMultiController.LIST_VIEW, modelAndView.getViewName());
        final Map model = getModel(modelAndView);

        assertEquals(type, model.get(ChartSettingsMultiController.TYPE_KEY));
        final Collection preferences = (Collection) model.get(ChartSettingsMultiController.PREFERENCES_KEY);
        assertNotNull(preferences);
        assertFalse(preferences.isEmpty());
        for (Iterator iterator = preferences.iterator(); iterator.hasNext();) {
            Preference pref = (Preference) iterator.next();
            assertEquals(type, pref.getType());
        }
    }

    private ChartSettingsMultiController chartSettingsMultiController;
}