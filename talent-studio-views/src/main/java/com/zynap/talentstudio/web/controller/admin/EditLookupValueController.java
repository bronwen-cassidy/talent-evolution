package com.zynap.talentstudio.web.controller.admin;

import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: ssoong
 * Date: 07-Apr-2004
 * Time: 14:31:43
 */
public class EditLookupValueController extends BaseLookupValueController {

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        LookupValue lookupValue = (LookupValue) command;
        final Long principalId = ZynapWebUtils.getUserId(request);

        try {
            getLookupManager().updateLookupValue(principalId, lookupValue);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.label", "A value with the specified display name already exists for this selection type.");
            return showForm(request, response, errors);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.LOOKUP_ID, lookupValue.getId());
        return new ModelAndView(view);
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return getLookupManager().findLookupValue(RequestUtils.getRequiredLongParameter(request, ParameterConstants.LOOKUP_ID));
    }
}
