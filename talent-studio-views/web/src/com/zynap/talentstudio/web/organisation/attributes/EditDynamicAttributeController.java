/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditDynamicAttributeController extends ZynapDefaultFormController {

    protected void onBindInternal(HttpServletRequest request, Object command) throws Exception {
        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;
        attribute.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
        attribute.setMandatory(RequestUtils.getBooleanParameter(request, ParameterConstants.MANDATORY, false));
        attribute.setSearchable(RequestUtils.getBooleanParameter(request, ParameterConstants.SEARCHABLE, false));
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {

        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(ControllerConstants.CANCEL_URL, getCancelView());

        // If the user has selected to create a lookup type based dynamic attribute, get the list of available lookup types
        if (attribute != null && attribute.isSelectionType()) {
            refData.put(ControllerConstants.LOOKUPS, getLookupManager().findActiveLookupTypes());
        }
        return refData;
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final DynamicAttribute da = dynamicAttributeService.findById(RequestUtils.getRequiredLongParameter(request, ParameterConstants.ATTR_ID));
        DynamicAttributeWrapper wrapper = new DynamicAttributeWrapper(da);

        String[] artefactTypes = new String[]{wrapper.getArtefactType(), DynamicAttribute.NODE_TYPE_FUNCTION};
        Collection<DynamicAttributeDTO> attributes = dynamicAttributeService.listActiveAttributes(artefactTypes, false, new String[]{wrapper.getType()});
        if (Node.SUBJECT_UNIT_TYPE_.equals(da.getArtefactType())) {
            attributes.add(DynamicAttribute.DATE_OF_BITH_ATTR);
        }
        attributes.remove(new DynamicAttributeDTO(da.getType(), da.getLabel(), da.getId(), da.getArtefactType(), da.getDescription(), da.getExternalRefLabel(), da.getRefersToType()));
        wrapper.setAttributes(attributes);

        List<ExpressionWrapper> temp = wrapExpressions(da);
        List<ExpressionWrapper> expressions = LazyList.decorate(temp, FactoryUtils.instantiateFactory(ExpressionWrapper.class));
        wrapper.setExpressions(expressions);
        return wrapper;
    }

    /**
     * Wraps expressions in expressionWrappers.
     *
     * @param dynamicAttribute the dynamic attribute which contains the calculation with the expressions.
     * @return a List of wrapped expressions
     */
    public static List<ExpressionWrapper> wrapExpressions(DynamicAttribute dynamicAttribute) {
        List<ExpressionWrapper> temp = new ArrayList<ExpressionWrapper>();
        if (dynamicAttribute.isCalculated()) {
            List<Expression> expressions = dynamicAttribute.getCalculation().getExpressions();
            for (int i = 0; i < expressions.size(); i++) {
                Expression expression = expressions.get(i);
                ExpressionWrapper expressionWrapper = new ExpressionWrapper(expression);
                temp.add(expressionWrapper);
            }
        }
        return temp;
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors)
            throws Exception {

        DynamicAttributeWrapper wrapper = (DynamicAttributeWrapper) command;
        DynamicAttribute modifiedAttributeDefinition = wrapper.getModifiedAttributeDefinition();
        try {
            dynamicAttributeService.update(modifiedAttributeDefinition);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.label", "An attribute with the specified label already exists.");
            return showForm(request, response, errors);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.ATTR_ID, modifiedAttributeDefinition.getId());
        return new ModelAndView(view);
    }

    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {

        DynamicAttributeWrapper wrapper = (DynamicAttributeWrapper) command;

        RedirectView view = new ZynapRedirectView(getCancelView(), ParameterConstants.ATTR_ID, wrapper.getId());
        return new ModelAndView(view);
    }

    public IDynamicAttributeService getDynamicAttributeService() {
        return dynamicAttributeService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public ILookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(ILookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    private IDynamicAttributeService dynamicAttributeService;
    private ILookupManager lookupManager;
}
