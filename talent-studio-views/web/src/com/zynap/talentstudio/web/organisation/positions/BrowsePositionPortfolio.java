/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.DefaultWizardFormController;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.portfolio.PortfolioItemHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller responsible to returning and filtering the portfolio documents of the position currently being viewed.
 *
 * @author bcassidy
 * @version 0.1
 * @since 14-Jun-2010 10:16:13
 */
public class BrowsePositionPortfolio extends DefaultWizardFormController {

    /**
     * Creates the form backing object for an ajax get request the name of the command in this instance is 'artefact'
     *
     * @param request the request carrying the subject id param
     * @return the subjectWrapperBean a delegate for the Subject object
     * @throws Exception
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        final UserSession userSession = UserSessionFactory.getUserSession();
        Long positionId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);

        Position position = (Position) positionService.findById(positionId);
        PositionWrapperBean artefact = new PositionWrapperBean(position);
        Collection<PortfolioItem> sortedItems = PortfolioItemHelper.filterPortfolioItems(userSession, userSession.getId(), position.getPortfolioItems());
        artefact.setPortfolioItems(sortedItems);
        return artefact;
    }

    public Map referenceData(HttpServletRequest request, Object command, Errors errors, int page) throws Exception {
        String activeTab = RequestUtils.getStringParameter(request, "activeTab", "portfolio");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("portfolioActiveTab", activeTab);
        return model;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    private IPositionService positionService;
}