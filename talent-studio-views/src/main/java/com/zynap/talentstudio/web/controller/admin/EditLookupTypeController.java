package com.zynap.talentstudio.web.controller.admin;

import com.zynap.talentstudio.common.lookups.LookupType;
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
 * Date: 09-Feb-2004
 * Time: 16:43:54
 */
public class EditLookupTypeController extends BaseLookupTypeController {

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        LookupType lookupType = (LookupType) command;
        final Long principalId = ZynapWebUtils.getUserId(request);

        try {
            getLookupManager().updateLookupType(principalId, lookupType);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "error.duplicate.label", "A selection type with the specified label already exists.");
            return showForm(request, response, errors);
        }

        RedirectView view = new ZynapRedirectView(getSuccessView(), ParameterConstants.LOOKUP_TYPE_ID, lookupType.getTypeId());
        return new ModelAndView(view);
    }

    protected Object formBackingObject(HttpServletRequest httpServletRequest) throws Exception {
        String typeId = RequestUtils.getRequiredStringParameter(httpServletRequest, ParameterConstants.LOOKUP_TYPE_ID);
        return getLookupManager().findLookupType(typeId);
    }
}
