package com.zynap.talentstudio.web.controller.admin;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: ssoong
 * Date: 09-Feb-2004
 * Time: 16:43:54
 */
public class AddLookupValueController extends BaseLookupValueController {

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        LookupValue lookupValue = (LookupValue) command;
        final Long principalId = ZynapWebUtils.getUserId(request);

        try {
            getLookupManager().createLookupValue(principalId, lookupValue);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.label", "A value with the specified display name already exists for this selection type.");
            return showForm(request, response, errors);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.LOOKUP_TYPE_ID, lookupValue.getTypeId());
        return new ModelAndView(view);
    }

    protected Object formBackingObject(HttpServletRequest request) throws ServletException {
        LookupValue lookupValue = new LookupValue();
        lookupValue.setSystem(false);

        lookupValue.setTypeId(request.getParameter(ParameterConstants.LOOKUP_TYPE_ID));
        lookupValue.getLookupType().setLabel(request.getParameter(LOOKUP_TYPE_LABEL));
        return lookupValue;
    }

    static final String LOOKUP_TYPE_LABEL = "type_label";
}