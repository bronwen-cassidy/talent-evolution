package com.zynap.talentstudio.web.controller;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ControllerUtils;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.talentstudio.web.history.SavedURL;
import com.zynap.talentstudio.web.history.HistoryHelper;

import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * JavaBean abstract base class for menumanager-aware form controllers.
 * Provides convenience methods for subclasses.
 *
 * @author Andreas Andersson
 */
public abstract class ZynapDefaultFormController extends CancellableFormController {

    public void onBind(HttpServletRequest request, Object command) throws Exception {
        // validate only if not a cancel request
        setValidateOnBinding(!ZynapWebUtils.isCancelled(request));
        onBindInternal(request, command);
    }

    /**
     * Set up custom property editors.
     *
     * @throws Exception
     */
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

        //Tell the security interceptor which is the current Controller to be able to check the command
        request.getSession().setAttribute(ParameterConstants.CONTROLLER_NAME, this.getClass().getName());

        // register common custom binders
        ControllerUtils.registerCommonEditors(binder);

        // register binder for String arrays
        binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor());

        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor (in this case the
        // ByteArrayMultipartEditor - There is another one called StringMultipartEditor
        // capable of converting files to Strings (using a user-defined character set)
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    protected void onBindInternal(HttpServletRequest request, Object command) throws Exception {
        // default does nothing
    }

    public void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
        if (ZynapWebUtils.isCancelled(request))
            return;
        else
            onBindAndValidateInternal(request, command, errors);
    }

    protected void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {
        // default implementation does nothing
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (ZynapWebUtils.isCancelled(request)) {
            return onCancelInternal(request, response, command);
        } else {
            return onSubmitInternal(request, response, command, errors);
        }
    }

    protected ModelAndView onCancel(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return onCancelInternal(request, response, command);
    }

    /**
     * Handles the situation where a simple form has been cancelled.
     *
     * @param request
     * @param response
     * @param command
     * @return ModelAndView the cancel view
     * @throws Exception
     */
    protected ModelAndView onCancelInternal(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(new ZynapRedirectView(getCancelView()));
    }

    /**
     * Handles the situation where a simple form has been submitted but has no requirements to persist data.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView the success view
     * @throws Exception
     */
    protected ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {
        return null;
    }


    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, Errors errors) throws Exception {
        return super.showForm(request, response, (BindException) errors);
    }

    protected ModelAndView showForm(HttpServletRequest request, Errors errors, String viewName) throws Exception {
        return super.showForm(request, (BindException) errors, viewName);
    }

    protected ModelAndView showForm(HttpServletRequest request, Errors errors, String viewName, Map model) throws Exception {
        return super.showForm(request, (BindException) errors, viewName, model);
    }

    protected ModelAndView showFormAndModel(HttpServletRequest request, Errors errors, String viewName) throws Exception {
        return super.showForm(request, (BindException) errors, viewName, ((BindException) errors).getModel());
    }

    protected ModelAndView showFormAndModel(HttpServletRequest request, HttpServletResponse response, Errors errors) throws Exception {
        return super.showForm(request, response, (BindException) errors, ((BindException) errors).getModel());
    }

    protected Map getModel(Errors errors) {
        return ((BindException) errors).getModel();
    }

    protected SavedURL getLastURL(HttpServletRequest request) {

        // check to see if there are any objects in the command history first
        // if none found try the token-based history
        SavedURL lastURL = HistoryHelper.getLastURL(request);
        if (lastURL == null) {
            lastURL = HistoryHelper.getCurrentURL(request);
        }

        return lastURL;
    }
}
