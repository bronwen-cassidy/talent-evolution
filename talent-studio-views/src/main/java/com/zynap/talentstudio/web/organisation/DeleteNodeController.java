package com.zynap.talentstudio.web.organisation;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.history.SavedURL;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.controller.ViewConfig;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;
import com.zynap.web.controller.ZynapDefaultFormController;

import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * User: amark
 * Date: 25-May-2005
 * Time: 09:02:34
 */
public abstract class DeleteNodeController extends ZynapDefaultFormController {

    public final static Long getNodeId(HttpServletRequest request) throws ServletRequestBindingException {
        return RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
    }

    /**
     * Get the node and set the urls required for the cancel and the confirmation.
     *
     * @param request
     * @return Object An instance of an {@link com.zynap.talentstudio.organisation.Node}
     * @throws Exception
     */
    public final Object formBackingObject(HttpServletRequest request) throws Exception {
        Long nodeId = getNodeId(request);

        // parameters for success and cancel views
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ParameterConstants.NODE_ID_PARAM, nodeId);

        // set cancel view
        final String cancelView = ZynapWebUtils.buildURL(HistoryHelper.getBackURL(request), parameters, false);
        setCancelView(cancelView);

        /**
         * Check for ControllerConstants.NEW_NODE in request. Indicates that node has been recently added - cancel behaves differently if this is the case.
         * Is only ever set on the request by AddPositionWizardFormController/AddSubjectWixardFormController
         * and then passed on by ViewPositionController / ViewSubjectController.
         * If present, use the default success view. Otherwise use the previous URL.
         */
        String successView = defaultSuccessViewConfig.getView();

        final boolean newNode = RequestUtils.getBooleanParameter(request, ControllerConstants.NEW_NODE, false);
        if (!newNode) {

            SavedURL lastURL = getLastURL(request);

            // if view page and are doing delete must take the previous saved url not the last one
            if (isViewPage(lastURL)) {
                lastURL = HistoryHelper.getCommandURL(1, request);
            }

            if (lastURL != null) {

                // append context path and add back parameter
                final String url = ZynapWebUtils.addContextPath(request, lastURL.getCompleteURL());
                HistoryHelper.appendHistoryBackParam(parameters);

                successView = ZynapWebUtils.buildURL(url, parameters, false);
            }
        }

        setSuccessView(successView);

        return getNode(nodeId);
    }

    /**
     * Call deleteNode(...) and redirect to success view.
     *
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return ModelAndView
     * @throws Exception
     */
    public final ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        try {
            deleteNode(command);
        } catch (DataIntegrityViolationException e) {
            errors.reject("error.cannot.delete." + ((Node) command).getNodeType());
            return showForm(request, response, errors);
        }

        // IMPORTANT: remove last url from history if view - otherwise formbacking object cannot be recovered
        if (isViewPage(getLastURL(request))) {
            HistoryHelper.removeLastURL(request);
        }

        // if using default success view, get redirect correctly
        ZynapRedirectView redirectView = new ZynapRedirectView(getSuccessView(), defaultSuccessViewConfig.getAdditionalParameters());

        // add parameter to indicate which Node was deleted - will be picked by BrowseNodeController - see BrowseNodeController.formBackingObject(...)
        redirectView.addStaticAttribute(ControllerConstants.DELETED_NODE_ID, ((Node) command).getId());
        return new ModelAndView(redirectView);
    }

    public final void setDefaultSuccessViewConfig(ViewConfig defaultSuccessViewConfig) {
        this.defaultSuccessViewConfig = defaultSuccessViewConfig;
    }

    public final void setViewPageURL(String viewPageURL) {
        this.viewPageURL = viewPageURL;
    }

    /**
     * Get node.
     *
     * @param nodeId The node id
     * @return A Node
     * @throws TalentStudioException
     */
    protected abstract Node getNode(Long nodeId) throws TalentStudioException;

    /**
     * Delete node.
     *
     * @param command The command object
     * @throws TalentStudioException
     */
    protected abstract void deleteNode(Object command) throws TalentStudioException;

    /**
     * Check if url matches view page url.
     *
     * @param lastURL
     * @return true or false
     */
    private boolean isViewPage(SavedURL lastURL) {
        return lastURL != null && lastURL.getURL().indexOf(viewPageURL) > 0;
    }

    private ViewConfig defaultSuccessViewConfig;

    private String viewPageURL;
}
