package com.zynap.web.controller.admin;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ZynapMultiActionController;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for MultiActionController implementations.
 * User: amark
 * Date: 20-Apr-2005
 * Time: 14:42:35
 */
public class LookupMultiController extends ZynapMultiActionController {

    private ILookupManager lookupManager;

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    /**
     * Get list of editable lookup types.
     * <br> Includes inactive ones.
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return ModelAndView
     * @throws ServletException
     */
    public ModelAndView listLookupTypesHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Map model = new HashMap();
        model.put(ControllerConstants.LOOKUPS, getLookupManager().findEditableLookupTypes());
        return new ModelAndView("listlookuptypes", ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Get lookup type - the lookup type id is in the request.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView viewLookupTypeHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String typeId = getLookupTypeId(request);
        LookupType lookupType = getLookupManager().findLookupType(typeId);

        Map model = new HashMap();
        model.put("lookupType", lookupType);
        return new ModelAndView("viewlookuptype", ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Get lookup value.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView viewLookupValueHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = getLookupValueId(request);
        LookupValue lookupValue = getLookupManager().findLookupValue(id);
        LookupType lookupType = lookupValue.getLookupType();

        Map model = new HashMap();
        model.put("lookupValue", lookupValue);
        model.put(ParameterConstants.LOOKUP_TYPE_ID, lookupType.getTypeId());
        model.put(ControllerConstants.LABEL, lookupType.getLabel());
        return new ModelAndView("viewlookupvalue", ControllerConstants.MODEL_NAME, model);
    }

    /**
     * Delete lookup value.
     * @param request
     * @param response
     * @return ModelAndView
     * @throws Exception
     */
    public ModelAndView deleteLookupValueHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = getLookupValueId(request);
        if (ZynapWebUtils.isConfirmed(request)) {
            try {
                getLookupManager().deleteLookupValue(id);
            } catch (TalentStudioException e) {
            }

            return viewLookupTypeHandler(request, response);
        }

        Map model = new HashMap();
        model.put(ParameterConstants.LOOKUP_ID, id);
        return new ModelAndView("confirmdeletelookupvalue", ControllerConstants.MODEL_NAME, model);
    }

    private Long getLookupValueId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.LOOKUP_ID);
    }

    private String getLookupTypeId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredStringParameter(request, ParameterConstants.LOOKUP_TYPE_ID);
    }
}
