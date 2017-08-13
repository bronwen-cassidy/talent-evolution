/*
 * Copyright (c) 2004 Zynap Limited. All rights reserved.
 */

package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.calculations.Expression;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.calculations.Calculation;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that adds dynamic attributes.
 *
 * @author amark
 */
public class AddDynamicAttributeController extends DefaultWizardFormController {

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        DynamicAttributeWrapper dynamicAttributeWrapper = new DynamicAttributeWrapper(new DynamicAttribute());
        String artefactType = request.getParameter(ParameterConstants.ARTEFACT_TYPE).toUpperCase();
        dynamicAttributeWrapper.setArtefactType(artefactType);
        return dynamicAttributeWrapper;
    }

    protected void onBindInternal(HttpServletRequest request, Object command, Errors errors) {

        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;
        int page = getCurrentPage(request);
        switch (page) {
            case SELECT_TYPE_PAGE:
                attribute.setCalculated(RequestUtils.getBooleanParameter(request, CALCULATABLE, false));
                break;
            case ENTER_DATA_PAGE:
                // set attributes mapped to check box fields to false if they were not in the request - ie: they were deselected
                attribute.setActive(RequestUtils.getBooleanParameter(request, ParameterConstants.ACTIVE, false));
                attribute.setMandatory(RequestUtils.getBooleanParameter(request, ParameterConstants.MANDATORY, false));
                attribute.setSearchable(RequestUtils.getBooleanParameter(request, ParameterConstants.SEARCHABLE, false));
                break;
        }
    }

    /**
     * Callback for custom post-processing in terms of binding and validation.
     * <br> Currently sets attribute in wrapper based on selected type
     * and set attributes mapped to check box fields to false if they were not in the request (active, mandatory, searchable, etc)
     *
     * @param request current HTTP request
     * @param command bound command
     * @param errors  Errors instance for additional custom validation
     * @param page    current wizard page
     * @throws Exception
     */
    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;
        if (getTargetPage(request, page) == ENTER_DATA_PAGE && !isFinishRequest(request)) {
            // the allowable attributes to calculate this field against
            if (attribute.isCalculated()) {
                String[] artefactTypes = new String[]{attribute.getArtefactType(), DynamicAttribute.NODE_TYPE_FUNCTION};
                Collection<DynamicAttributeDTO> attributes = dynamicAttributeService.listActiveAttributes(artefactTypes, false, getPossibleTypes(attribute));
                if (Node.SUBJECT_UNIT_TYPE_.equals(attribute.getArtefactType())) {
                    attributes.add(DynamicAttribute.DATE_OF_BITH_ATTR);
                }
                attribute.setAttributes(attributes);

                List<ExpressionWrapper> expressions = LazyList.decorate(new ArrayList<ExpressionWrapper>(), FactoryUtils.instantiateFactory(ExpressionWrapper.class));

                expressions.add(new ExpressionWrapper(new Expression()));
                expressions.add(new ExpressionWrapper(new Expression()));
                attribute.setExpressions(expressions);
            }
        }
        if (page == ENTER_DATA_PAGE) {
            DynamicAttributeValidator validator = (DynamicAttributeValidator) getValidator();
            validator.validate(attribute, errors);
        }
    }

    protected Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;

        Map<String, Object> refData = new HashMap<String, Object>();
        switch (page) {
            case SELECT_TYPE_PAGE:
                List types = getLookupManager().findActiveLookupValues(ILookupManager.LOOKUP_TYPE_DA);
                refData.put("types", types);
                refData.put("artefactType", attribute.getArtefactType());
                break;
            case ENTER_DATA_PAGE:
                // If the user has selected to create a lookup type based dynamic attribute, get the list of available lookup types
                if (attribute.isSelectionType()) {
                    refData.put(ControllerConstants.LOOKUPS, getLookupManager().findActiveLookupTypes());
                }
                
                break;
        }
        refData.put(ControllerConstants.TITLE, MESSAGE_KEY + page);
        return refData;

    }

    protected ModelAndView processFinish(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        DynamicAttributeWrapper da = (DynamicAttributeWrapper) command;
        DynamicAttribute real = da.getModifiedAttributeDefinition();
        if(da.isCurrencyType())  {
            da.setRefersTo(ILookupManager.LOOKUP_TYPE_CURRENCY);
        }
        try {
            dynamicAttributeService.create(real);
        } catch (DataIntegrityViolationException e) {
            logger.error(e.getMessage(), e);
            real.setId(null);
            if (real.isCalculated()) {
                // clear out the ids that have been set
                Calculation calculation = real.getCalculation();
                calculation.setId(null);
                List<Expression> expressions = calculation.getExpressions();
                for (Expression expression : expressions) {
                    expression.setId(null);
                }
            }
            errors.rejectValue("label", "error.duplicate.label", e.getMessage());
            return showPage(request, errors, ENTER_DATA_PAGE);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.ATTR_ID, real.getId());
        view.addStaticAttribute(ParameterConstants.ARTEFACT_TYPE, real.getArtefactType());
        return new ModelAndView(view);
    }

    protected ModelAndView processCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        DynamicAttributeWrapper attribute = (DynamicAttributeWrapper) command;
        String cancelView;
        if (Node.SUBJECT_UNIT_TYPE_.equals(attribute.getArtefactType())) {
            cancelView = SUBJECT_CANCEL_VIEW;
        } else {
            cancelView = POSITION_CANCEL_VIEW;
        }

        RedirectView view = new ZynapRedirectView(cancelView, ParameterConstants.ARTEFACT_TYPE, attribute.getArtefactType());
        return new ModelAndView(view);
    }

    private String[] getPossibleTypes(DynamicAttributeWrapper attribute) {
        String[] values = null;
        if (DynamicAttribute.DA_TYPE_DATE.equals(attribute.getType())) {
            values = new String[]{DynamicAttribute.DA_TYPE_DATE};
        }
        return values;
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

    private static final int SELECT_TYPE_PAGE = 0;
    private static final int ENTER_DATA_PAGE = 1;
    private static final String SUBJECT_CANCEL_VIEW = "listsubjectDA.htm";
    private static final String POSITION_CANCEL_VIEW = "listpositionDA.htm";
    private static final String MESSAGE_KEY = "add.attribute.wizard.page.";
    private static final String CALCULATABLE = "calculated";
}
