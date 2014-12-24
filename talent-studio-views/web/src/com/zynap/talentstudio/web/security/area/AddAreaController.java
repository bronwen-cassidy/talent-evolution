package com.zynap.talentstudio.web.security.area;

import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaUtils;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 13:19:56
 */
public class AddAreaController extends BaseAreaController {

    /**
     * Constructor.
     */
    public AddAreaController() {
    }

    /**
     * Form backing object consists of a new AreaWrapperBean.
     *
     * @param request
     * @return AreaWrapperBean
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        return new AreaWrapperBean(new Area(), new HashSet<AreaElement>());
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        AreaWrapperBean areaWrapperBean = getWrapper(command);
        Area newArea = areaWrapperBean.getModifiedArea();
        // add in the subject elements
        final Set<AreaElement> elements = AreaUtils.assignPopulationElements(newArea, populationEngine, analysisService);
        for (AreaElement element : elements) {
            newArea.addAreaElement(element);
        }
        
        try {
            securityManager.createArea(newArea);
        } catch (DataIntegrityViolationException e) {

            areaWrapperBean.resetIds();

            logger.error("Failed to create area because of: ", e);
            errors.rejectValue("label", "error.duplicate.label", e.getMessage());
            return showPage(request, errors, CORE_VIEW_IDX);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.AREA_ID, newArea.getId());
        return new ModelAndView(view);
    }

}