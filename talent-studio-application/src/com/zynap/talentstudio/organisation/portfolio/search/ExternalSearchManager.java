/*
 * Copyright (c) 2004 Zynap Ltd. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio.search;

import com.zynap.domain.UserSession;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of facade class.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ExternalSearchManager implements ISearchManager {

    public ISearchResult search(ISearchQuery query, IField[] fields) throws ExternalSearchException {
        final ISearchResult searchResult = getGateway().executeSearch(query, fields);
        filterSearchResults(searchResult);
        return searchResult;
    }

    public ISearchResult search(int docId, IField[] fields, String datasources) throws ExternalSearchException {
        final ISearchResult searchResult = getGateway().executeSearch(docId, datasources, fields);
        filterSearchResults(searchResult);
        return searchResult;
    }

    public boolean checkVersion() throws ExternalSearchException {
        return gateway.checkVersion();
    }

    public IGateway getGateway() {
        return gateway;
    }

    public void setGateway(IGateway gateway) {
        this.gateway = gateway;
    }

    public IPermitManagerDao getPermitManagerDao() {
        return permitManagerDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    /**
     * Filter search results by checking user access to nodes the portfolio items belong to.
     *
     * @param searchResult takes in search result to use for filtering
     */
    private void filterSearchResults(final ISearchResult searchResult) {

        // only filter for non-admin users
        final UserSession userSession = UserSessionFactory.getUserSession();
        if (searchResult != null && !userSession.isAdministrator()) {
            final List hits = searchResult.getHits();

            // get user id and subject id (may be null) and ids of nodes that user has access to and filter results
            final Long userId = userSession.getId();
            final Long subjectId = userSession.getSubjectId();
            final String[] nodeTypes = new String[]{Node.POSITION_UNIT_TYPE_, Node.SUBJECT_UNIT_TYPE_};
            Collection accessibleNodes = getPermitManagerDao().getNodeIds(userId, nodeTypes, SecurityConstants.VIEW_ACTION);
            CollectionUtils.filter(hits, new ISearchResultPredicate(accessibleNodes, userId, subjectId));
        }
    }

    private IGateway gateway;

    private IPermitManagerDao permitManagerDao;

    /**
     * Predicate that filters ISearchResults by scope and against artefact / node id.
     * <br> Rules are as follows:
     * <ul>
     * <li>public documents and documents created by the current user are always included in the search results returned.</li>
     * <li>protected documents are included in the search results if the current user has access to the artefact the item belongs to.</li>
     * <li>private documents are included in the search results if the current user created the document.</li>
     * </ul>
     */
    private class ISearchResultPredicate implements Predicate {

        private Collection nodeIds;

        private Long userId;

        private Long subjectId;

        public ISearchResultPredicate(Collection nodeIds, Long userId, Long subjectId) {
            this.nodeIds = nodeIds;
            this.userId = userId;
            this.subjectId = subjectId;
        }

        public boolean evaluate(Object object) {

            boolean accessAllowed;

            final ISearchResult searchResult = (ISearchResult) object;
            final String scope = searchResult.getScope();
            if (PortfolioItem.PUBLIC_SCOPE.equalsIgnoreCase(scope) || searchResult.createdBy(userId)) {
                accessAllowed = true;
            } else if (PortfolioItem.PRIVATE_SCOPE.equalsIgnoreCase(scope)) {
                accessAllowed = searchResult.createdBy(userId);
            } else {
                // otherwise is restricted so first check if ids match to ensure that restricted items are seen
                final Long artefactId = searchResult.getArtefactId();
                // checking to see if the logged in user owns the item
                if(subjectId == null) {
                    // only check nodeIds.contains((artefactId)) as this is only a user
                    accessAllowed = nodeIds != null && nodeIds.contains((artefactId));
                } else {
                    accessAllowed = subjectId.equals(artefactId) || nodeIds != null && nodeIds.contains((artefactId));
                }
            }
            return accessAllowed;
        }
    }

}
