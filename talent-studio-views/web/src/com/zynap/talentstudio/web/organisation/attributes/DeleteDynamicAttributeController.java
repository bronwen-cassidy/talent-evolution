package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.Node;

import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataAccessException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;

/**
 * Handler to delete the dynamic attribute
 *
 * User: bcassidy
 * Date: 08-Jun-2005
 * Time: 18:11:09
 */
public class DeleteDynamicAttributeController extends ZynapDefaultFormController {

    /**
     * Retrieves the metric for the given id in the request.
     *
     * @param request the http servlet request
     * @return Object an instance of an {@link com.zynap.talentstudio.analysis.metrics.Metric} object
     * @throws Exception
     */
    public Object formBackingObject(HttpServletRequest request) throws Exception {        
        Long attributeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.ATTR_ID);
        String artefactType = RequestUtils.getRequiredStringParameter(request, ParameterConstants.ARTEFACT_TYPE);
        setSuccessView(artefactType.equals(Node.SUBJECT_UNIT_TYPE_) ? LIST_SUBJECT_DA_VIEW : LIST_POSITION_DA_VIEW);
        return dynamicAttributeService.findById(attributeId);
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        DynamicAttribute dynamicAttribute = (DynamicAttribute) command;
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(USED_BY_NODE, new Boolean(dynamicAttributeService.usedByNode(dynamicAttribute.getId())));
        return refData;
    }

    /**
     * Delete the metric.
     *
     * @param request the http servlet request
     * @param response the http servlet response
     * @param command a dynamicAttribute
     * @param errors the object containing errors if any
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        DynamicAttribute dynamicAttribute = (DynamicAttribute) command;
        try {
            dynamicAttributeService.delete(dynamicAttribute);
            return new ModelAndView(new ZynapRedirectView(getSuccessView()));
        } catch (DataAccessException e) {
            errors.reject("error.attribute.in.use", "This attribute is currently in use and connot be deleted.");            
            return showFormAndModel(request, errors, getFormView());
        }
    }

    /**
     * Handle the cancel.
     *
     * @param request the http servlet request
     * @param response the http servlet response
     * @param command a dynamicAttribute
     * @return ModelAndView the cancel view
     */
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) {
        DynamicAttribute dynamicAttribute = (DynamicAttribute) command;
        return new ModelAndView(new ZynapRedirectView(getCancelView(), ParameterConstants.ATTR_ID, dynamicAttribute.getId()));
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private IDynamicAttributeService dynamicAttributeService;
    static final String USED_BY_NODE = "usedByNode";
    static final String LIST_POSITION_DA_VIEW = "listpositionDA.htm";
    static final String LIST_SUBJECT_DA_VIEW = "listsubjectDA.htm";
}