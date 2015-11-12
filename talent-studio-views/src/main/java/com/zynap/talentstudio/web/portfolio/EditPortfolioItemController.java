/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.common.util.UploadedFile;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.history.HistoryHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.utils.beans.UploadedFilePropertyEditor;
import com.zynap.talentstudio.web.controller.ZynapDefaultFormController;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author cassidy
 * @version $Revision: $
 *          $Id: $
 */
public class EditPortfolioItemController extends ZynapDefaultFormController {

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {

        super.initBinder(request, binder);
        binder.registerCustomEditor(UploadedFile.class, new UploadedFilePropertyEditor());
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        UserSession userSession = ZynapWebUtils.getUserSession(request);
        Long portfolioItemId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.ITEM_ID);
        PortfolioItem portfolioItem = (PortfolioItem) portfolioService.findById(portfolioItemId);
        PortfolioItemFile itemFile = portfolioService.findItemFile(portfolioItemId);
        PortfolioItemWrapper itemWrapper = new PortfolioItemWrapper(portfolioItem, itemFile);
        itemWrapper.setUserSession(userSession);
        itemWrapper.setMyPortfolio(myPortfolio);
        return itemWrapper;
    }

    /**
     * Create a reference data map for the given request and command,
     * consisting of bean name/bean instance pairs as expected by ModelAndView.
     * <p>The default implementation delegates to {@link #referenceData(javax.servlet.http.HttpServletRequest)}.
     * Subclasses can override this to set reference data used in the view.
     *
     * @param request current HTTP request
     * @param command form object with request parameters bound onto it
     * @param errors  validation errors holder
     * @return a Map with reference data entries, or <code>null</code> if none
     * @throws Exception in case of invalid state or arguments
     * @see org.springframework.web.servlet.ModelAndView
     */
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
        Map<String, Object> refData = new HashMap<String, Object>();
        refData.put(ControllerConstants.CANCEL_VIEW, getCancelView());
        return refData;
    }

    public void onBindAndValidateInternal(HttpServletRequest request, Object command, Errors errors) throws Exception {

        // validate content of file item - not done by default validation so must be done here
        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        final PortfolioItemValidator validator = (PortfolioItemValidator) getValidator();
        validator.validateContentValues(wrapper, errors);
    }

    public ModelAndView onSubmitInternal(HttpServletRequest request, HttpServletResponse response, Object command, Errors errors) throws Exception {

        PortfolioItemWrapper wrapper = (PortfolioItemWrapper) command;
        PortfolioItem item = wrapper.getModifiedItem();
        PortfolioItemFile file = wrapper.getModifiedFileItem();

        PortfolioItemHelper.enforceSecurityLogic(item, myPortfolio);

        try {
            item.setLastModifiedById(ZynapWebUtils.getUserId(request));            
            portfolioService.update(item, file);
        } catch (DataIntegrityViolationException e) {
            errors.rejectValue("label", "label.already.exists", "The label given already exists for another item");
            return showForm(request, response, errors);
        }

        final RedirectView view = HistoryHelper.buildRedirectView(getSuccessView());
        view.addStaticAttribute(ParameterConstants.ITEM_ID, item.getId());
        view.addStaticAttribute(ParameterConstants.NODE_ID_PARAM, item.getNode().getId());
        return new ModelAndView(view);
    }

    public void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public void setMyPortfolio(boolean myPortfolio) {
        this.myPortfolio = myPortfolio;
    }

    private IPortfolioService portfolioService;

    private boolean myPortfolio;
}
