package com.zynap.talentstudio.web.arena;

import com.zynap.talentstudio.arenas.IArenaManager;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * MultiAction Controller that is responsible for system config data.
 *
 * @author Andreas Andersson
 * @since 20/10/2004
 */
public class ArenaMultiController extends ZynapMultiActionController {

    public IArenaManager getArenaManager() {
        return arenaManager;
    }

    public void setArenaManager(IArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public ModelAndView listAllArenas(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Map myModel = new HashMap();
        myModel.put(ControllerConstants.ARENAS, arenaManager.getArenas());
        return new ModelAndView("listarenas", ControllerConstants.MODEL_NAME, myModel);
    }

    private IArenaManager arenaManager;
}
