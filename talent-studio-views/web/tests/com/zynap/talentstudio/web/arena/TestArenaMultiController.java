package com.zynap.talentstudio.web.arena;

/**
 * User: amark
 * Date: 14-Nov-2005
 * Time: 11:54:28
 */

import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.ZynapMockControllerTest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public class TestArenaMultiController extends ZynapMockControllerTest {

    protected void setUp() throws Exception {
        super.setUp();

        arenaMultiController = (ArenaMultiController) getBean("arenaMultiController");
    }

    public void testListAllArenas() throws Exception {

        final ModelAndView modelAndView = arenaMultiController.listAllArenas(mockRequest, mockResponse);
        assertEquals("listarenas", modelAndView.getViewName());
        final Map model = getModel(modelAndView);

        final List list = (List) model.get(ControllerConstants.ARENAS);
        assertFalse(list.isEmpty());
    }

    ArenaMultiController arenaMultiController;
}