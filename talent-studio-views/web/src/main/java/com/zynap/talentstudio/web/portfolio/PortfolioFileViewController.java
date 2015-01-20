/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItemFile;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PortfolioFileViewController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = RequestUtils.getRequiredLongParameter(request, ParameterConstants.ITEM_ID);
        PortfolioItem item = (PortfolioItem) portfolioService.findById(id);
        PortfolioItemFile file = portfolioService.findItemFile(id);

        if (file != null && file.getBlobValue() != null) {
            ResponseUtils.writeToResponse(response, request, item.getOrigFileName(), file.getBlobValue(), !ZynapWebUtils.isInternetExplorer(request));
        }          
        return null;
    }

    public void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    private IPortfolioService portfolioService;
}
