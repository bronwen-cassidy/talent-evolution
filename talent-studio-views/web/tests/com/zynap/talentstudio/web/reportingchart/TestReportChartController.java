package com.zynap.talentstudio.web.reportingchart;

/**
 * User: amark
 * Date: 21-Apr-2005
 * Time: 10:58:41
 */

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class TestReportChartController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();
        UserSession userSession = new UserSession(getAdminUserPrincipal(), getArenaMenuHandler());
        setUserSession(userSession, mockRequest);

        reportChartController = (ReportChartController) applicationContext.getBean("reportChartController");
    }

    public void testFormBackingObject() throws Exception {
        final ReportChartWrapper command = (ReportChartWrapper) reportChartController.formBackingObject(mockRequest);
        assertNotNull(command);
    }

    public void testOnSubmitInternal() throws Exception {

        final ReportChartWrapper command = (ReportChartWrapper) reportChartController.formBackingObject(mockRequest);

        ModelAndView modelAndView = reportChartController.onSubmitInternal(mockRequest, mockResponse, command, getErrors(command));
        assertEquals(reportChartController.getFormView(), modelAndView.getViewName());
    }

    public void testReferenceData() throws Exception {

        final ReportChartWrapper command = (ReportChartWrapper) reportChartController.formBackingObject(mockRequest);
        final Long defaultPositionId = DEFAULT_POSITION_ID;
        mockRequest.addParameter(ParameterConstants.ARTEFACT_ID, defaultPositionId.toString());
        Map model = reportChartController.referenceData(mockRequest, command, getErrors(command));

        // should have empty but not null model
        assertTrue(model.isEmpty());

        final Position defaultPosition = command.getPosition();
        assertEquals(defaultPositionId, defaultPosition.getId());
        assertTrue(defaultPosition.isHasAccess());
    }

    ReportChartController reportChartController;
}