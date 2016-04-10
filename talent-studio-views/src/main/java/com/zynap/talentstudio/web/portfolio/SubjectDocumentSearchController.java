/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.DocumentSearchQuery;
import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SubjectDocumentSearchController extends DocumentSearchController {

    /**
     * Gets the position node and filters its portfolio items according to security.
     * 
     * @param request
     * @return DocumentSearchWrapper containing a {@link com.zynap.talentstudio.organisation.positions.Position} node.
     * @throws Exception
     */
    protected DocumentSearchWrapper getNewFormBackingObject(HttpServletRequest request) throws Exception {

        DocumentSearchWrapper documentSearchWrapper = new DocumentSearchWrapper(new DocumentSearchQuery());
        documentSearchWrapper.setSelectedSources((String) dataSources.get(Node.SUBJECT_UNIT_TYPE_));
        documentSearchWrapper.setContentTypes(portfolioService.getContentTypes(Node.SUBJECT_UNIT_TYPE_, SecurityConstants.VIEW_ACTION));

        Long nodeId = RequestUtils.getRequiredLongParameter(request, ParameterConstants.NODE_ID_PARAM);
        Node node = positionService.findById(nodeId);
        documentSearchWrapper.setNode(node);

        // set flag to true - otherwise restricted portfolio item security checks will not work correctly
        node.setHasAccess(true);
        documentSearchWrapper.setPortfolioItems(PortfolioItemHelper.filterSearchablePortfolioItems(node, ZynapWebUtils.getUserSession(request)));
        return documentSearchWrapper;
    }
}
