/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.common.util.UploadedFile;
import com.zynap.domain.UserSession;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.talentstudio.web.utils.mvc.ZynapRedirectView;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**                                                                                                                                                                                              r
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class BaseAddPortfolioItemController extends CancellableFormController {

    protected BaseAddPortfolioItemController() {
        setCommandName(ControllerConstants.COMMAND_NAME);
    }

    protected final Object formBackingObject(HttpServletRequest request) throws Exception {

        setCancelView(HistoryHelper.getBackURL(request));
        setSuccessView(HistoryHelper.getBackURL(request));

        final Node node = getNode(RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM));

        PortfolioItem item = new PortfolioItem();
        SecurityAttribute securityAttr = new SecurityAttribute(false, false, true, false, false);

        item.setSecurityAttribute(securityAttr);
        item.setNode(node);
        item.setScope(PortfolioItem.RESTRICTED_SCOPE);
        item.setContentSubType(PortfolioItem.UPLOAD_SUBTYPE);

        PortfolioItemFile portfolioItemFile = new PortfolioItemFile();
        PortfolioItemWrapper portItemWrapper = new PortfolioItemWrapper(item, portfolioItemFile);
        portItemWrapper.setMyPortfolio(myPortfolio);
        portItemWrapper.setUserSession(ZynapWebUtils.getUserSession(request));

        return portItemWrapper;
    }

    protected abstract Node getNode(Long nodeId) throws TalentStudioException;

    protected final void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
    }

    /**
     * Only validate if is not a back request.
     *
     * @param request
     * @param command
     * @param errors
     * @throws Exception
     */
    protected final void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {

        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        PortfolioItemValidator validator = (PortfolioItemValidator) getValidator();
        validator.validateRequiredFileItemValues(errors);
        validator.validateSubContentValues(errors);
        validator.validateContentValues(wrapper, errors);
    }


    /**
     * Reference data required by the view is collected in this method.
     * <p/>
     * The reference data method needs to check a few things with regards to resource security:
     * <ul>
     * <li>Is the logged in user associated to this subject portfolio?</li>
     * <li>If yes, which content types does this user have permission to read and write</li>
     * <li>No write permissions means do not display the content type</li>
     * <li>No read permissions means do not include that content type in the displayed items</li>
     * </ul>
     *
     * @param request
     * @param command
     * @param errors
     * @return Map        a map of key value pairs used by the view to display the page
     * @throws Exception
     */
    protected final Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        Map refData = new HashMap();
        Collection contentTypes = getContentTypes(wrapper);
        refData.put("contenttypes", contentTypes);
        refData.put("contentsubtypes", getAllSubContentTypes());
        return refData;
    }

    protected final Collection getContentTypes(PortfolioItemWrapper itemWrapper) {
        return portfolioService.getContentTypes(itemWrapper.getNodeType());
    }

    protected final ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        UserSession userSession = ZynapWebUtils.getUserSession(request);

        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        final PortfolioItem item = wrapper.getModifiedItem();
        PortfolioItemFile file = wrapper.getModifiedFileItem();
        final Long creatorId = userSession.getId();
        item.setCreatedById(creatorId);
        item.setLastModifiedById(creatorId);
        PortfolioItemHelper.enforceSecurityLogic(item, myPortfolio);

        try {
            portfolioService.create(item, file);
        } catch (DataIntegrityViolationException e) {

            // clear ids on new portfolio item
            wrapper.resetId();

            errors.rejectValue("label", "label.already.exists", "The label given already exists for another item");
            return showForm(request, response, errors);
        }

        // redirect to portfolio page not to portfolio item view page
        RedirectView view = new ZynapRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, item.getNode().getId());
        return new ModelAndView(view);

    }

    protected final ModelAndView onCancel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object command) throws Exception {

        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        RedirectView view = new ZynapRedirectView(getCancelView());
        view.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, wrapper.getNode().getId());
        return new ModelAndView(view);
    }

    public final void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public boolean isMyPortfolio() {
        return myPortfolio;
    }

    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }

    public Collection getAllSubContentTypes() {
        Set subtypesTable = new HashSet();
        final Collection<ContentType> allContentTypes = portfolioService.getAllContentTypes();
        for (ContentType allContentType : allContentTypes) {
           String[] subtypes = allContentType.getContentSubTypes();
           subtypesTable.addAll(Arrays.asList(subtypes));
        }
        List<String> subtypes= new LinkedList(subtypesTable);
        Collections.sort(subtypes);
        return subtypes;
    }


    protected IPortfolioService portfolioService;

    private boolean myPortfolio;
}
