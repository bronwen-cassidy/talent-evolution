package com.zynap.talentstudio.web.security.area;

import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.areas.AreaUtils;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 13:20:06
 */
public class EditAreaController extends BaseAreaController {

    /**
     * Constructor.
     */
    public EditAreaController() {
    }

    /**
     * Form backing object consists of a AreaWrapperBean containing the Area being edited.
     *
     * @param request
     * @return AreaWrapperBean
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {

        final Long areaId = AreaMultiController.getAreaId(request);
        Area area = securityManager.findArea(areaId);
         // remove population elements
        final Set<AreaElement> areaElementSet = area.getAreaElements();
        areaElementSet.size();
        final Set<AreaElement> areaElements = new HashSet(areaElementSet);

        for (Iterator<AreaElement> iterator = areaElements.iterator(); iterator.hasNext();) {
            AreaElement areaElement = iterator.next();
            if(areaElement.isFromPopulation()) iterator.remove();
        }
        
        return new AreaWrapperBean(area, areaElements);
    }

    /**
     * Process cancel request.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param command form object with the current wizard state
     * @param errors Errors instance containing errors
     * @return the cancellation view
     * @throws Exception
     */
    protected ModelAndView processCancel(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        AreaWrapperBean areaWrapperBean = getWrapper(command);

        ModelAndView modelAndView = super.processCancel(request, response, command, errors);
        RedirectView view = (RedirectView) modelAndView.getView();
        view.addStaticAttribute(ParameterConstants.AREA_ID, areaWrapperBean.getId());
        return modelAndView;
    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        AreaWrapperBean areaWrapperBean = getWrapper(command);
        Area updatedArea = areaWrapperBean.getModifiedArea();
        Set<AreaElement> popElements = AreaUtils.assignPopulationElements(updatedArea, populationEngine, analysisService);
        
        try {
            securityManager.updateArea(updatedArea);
            if (!popElements.isEmpty()) {
                securityManager.createAreaElements(updatedArea, popElements);
            }
        } catch (DataIntegrityViolationException e) {

            logger.error("Failed to update area because of: ", e);
            errors.rejectValue("label", "error.duplicate.label", e.getMessage());
            return showPage(request, errors, CORE_VIEW_IDX);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.AREA_ID, updatedArea.getId());
        return new ModelAndView(view);
    }
}
