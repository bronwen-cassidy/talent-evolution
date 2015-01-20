/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.UserSession;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class PortfolioItemHelper {

    private PortfolioItemHelper() {
        super();
    }

    /**
     * Filter the portfolio items in the personal portfolio based on security.
     * <br> The difference between this and the portfolios of other artefacts is that access to restricted items
     * in a personal portfolio is restricted according to content type.
     *
     * @param nodeWrapperBean
     */
    public static void filterPersonalPortfolioItems(NodeWrapperBean nodeWrapperBean) {
        final Node node = nodeWrapperBean.getNode();
        Collection<PortfolioItem> filteredItems = filterPersonalPortfolioItems(node);
        nodeWrapperBean.setPortfolioItems(filteredItems);
    }

    public static Collection<PortfolioItem> filterPersonalPortfolioItems(Node node) {
        final Collection<PortfolioItem> portfolioItems = node.getPortfolioItems();
        final Collection<PortfolioItem> filteredItems = new LinkedHashSet<PortfolioItem>();
        for (Iterator iterator = portfolioItems.iterator(); iterator.hasNext();) {
            final PortfolioItem portfolioItem = (PortfolioItem) iterator.next();

            if (portfolioItem.getSecurityAttribute().isIndividualRead()) {
                filteredItems.add(portfolioItem);
            }
        }
        return filteredItems;
    }

    /**
     * Filter the portfolio items in the personal portfolio based on security.
     * <br> The difference between this and the portfolios of other artefacts is that access to restricted items
     * in a personal portfolio is restricted according to content type.
     *
     * @param node             The Node
     * @param userSession      The user session
     * @return Collection of PortfolioItems
     */
    public static Collection<IDomainObject> filterPersonalSearchPortfolioItems(Node node, UserSession userSession) {

        final Collection<IDomainObject> filteredItems = new LinkedHashSet<IDomainObject>();
        final Collection<PortfolioItem> portfolioItems = node.getPortfolioItems();

        for (Iterator iterator = portfolioItems.iterator(); iterator.hasNext();) {
            final PortfolioItem portfolioItem = (PortfolioItem) iterator.next();
            if (portfolioItem.hasAccess(userSession.getId())) {
                filteredItems.add(portfolioItem);
            }
        }

        return filteredItems;
    }

    /**
     * Filter the portfolio items of the wrapped artefact based on security.
     * <br> The difference between this and a subjectuser's personal portfolios is that access to restricted items
     * in a personal portfolio is restricted according to whether or not a user has access to the artefact.
     *
     * @param nodeWrapperBean The bean that contains the artefact
     * @param userSession     The user session
     */
    public static void filterPortfolioItems(NodeWrapperBean nodeWrapperBean, UserSession userSession) {

        final Node node = nodeWrapperBean.getNode();
        final Long userId = userSession.getId();
        final Collection<PortfolioItem> filteredItems = new LinkedHashSet<PortfolioItem>(node.getPortfolioItems());

        List<PortfolioItem> sortedItems = filterPortfolioItems(userSession, userId, filteredItems);

        nodeWrapperBean.setPortfolioItems(sortedItems);
    }

    public static List<PortfolioItem> filterPortfolioItems(UserSession userSession, Long userId, Collection<PortfolioItem> filteredItems) {

        if (!userSession.isAdministrator()) {

            for (Iterator iterator = filteredItems.iterator(); iterator.hasNext();) {
                PortfolioItem portfolioItem = (PortfolioItem) iterator.next();

                if (!portfolioItem.getSecurityAttribute().isManagerRead() && !portfolioItem.isCreatedByUser(userId)) {
                    iterator.remove();
                }
            }
        }
        // now sort the items by date
        List<PortfolioItem> sortedItems = new ArrayList<PortfolioItem>(filteredItems);
        Collections.sort(sortedItems, new Comparator<PortfolioItem>() {
            public int compare(PortfolioItem o1, PortfolioItem o2) {
                return -(o1.getLastModified().compareTo(o2.getLastModified()));
            }
        });
        
        return sortedItems;
    }

    /**
     * Filter the portfolio items of the wrapped artefact based on security.
     * <br> The difference between this and a subjectuser's personal portfolios is that access to restricted items
     * in a personal portfolio is restricted according to whether or not a user has access to the artefact.
     *
     * @param node        The Node
     * @param userSession The user session
     * @return Collection of PortfolioItems
     */
    public static Collection<PortfolioItem> filterSearchablePortfolioItems(Node node, UserSession userSession) {

        final Collection<PortfolioItem> filteredItems = new LinkedHashSet<PortfolioItem>(node.getPortfolioItems());

        if (!userSession.isAdministrator()) {

            for (Iterator iterator = filteredItems.iterator(); iterator.hasNext();) {
                PortfolioItem portfolioItem = (PortfolioItem) iterator.next();
                if (!checkItemAccess(portfolioItem, userSession)) {
                    iterator.remove();
                }
            }
        }

        return filteredItems;
    }

    /**
     * Check access to a portfolio item.
     * <br/> If the user is a root user return true always.
     * <br/> Otherwise, if the item is public, or the item is private and created by the user,
     * or the item is restricted and the user has access to the node the item belongs to then will return true.
     *
     * @param portfolioItem
     * @param userSession
     * @return true or false.
     */
    public static boolean checkItemAccess(PortfolioItem portfolioItem, UserSession userSession) {

        boolean hasAccess = userSession.isAdministrator();

        if (!hasAccess) {

            if (portfolioItem.hasAccess(userSession.getId())) {
                hasAccess = true;

            } else {

                final Node node = portfolioItem.getNode();
                if (node.isHasAccess() && portfolioItem.isManagerRead()) {
                    hasAccess = true;
                }
            }
        }

        return hasAccess;
    }

    /**
     * Copies portfolio items returned by portfolio search into a Map ordered by artefact id.
     *
     * @param searchResults - a list containing {@link com.zynap.talentstudio.organisation.portfolio.search.ISearchResult} objects
     * @param itemIds       List to add the selected item ids to
     * @return Map keyed on artefact id (the value is an ArrayList of {@link com.zynap.talentstudio.organisation.portfolio.search.ISearchResult} objects)
     */
    public static Map mapResults(List searchResults, List<Long> itemIds) {
        final Map<Long, List<SearchResult>> filteredResults = new LinkedHashMap<Long, List<SearchResult>>();

        for (Iterator iterator = searchResults.iterator(); iterator.hasNext();) {
            final SearchResult searchResult = (SearchResult) iterator.next();
            final Long artefactId = searchResult.getArtefactId();
            final Long itemId = searchResult.getItemId();

            List<SearchResult> hits = filteredResults.get(artefactId);
            if (hits == null) {
                hits = new ArrayList<SearchResult>();
                filteredResults.put(artefactId, hits);
            }

            hits.add(searchResult);
            itemIds.add(itemId);
        }

        return filteredResults;
    }

    public static void enforceSecurityLogic(PortfolioItem item, boolean myPortfolio) {

        SecurityAttribute securityAttr = item.getSecurityAttribute();

        // if the user is editing/adding a portfolio item in their own portfolio then they must have (and keep) read/write perms.
        if (myPortfolio) securityAttr.setIndividualWrite(true);

        // Enforcing permissions logic (to write you MUST have read permissions also).
        if (securityAttr.isManagerWrite()) securityAttr.setManagerRead(true);
        if (securityAttr.isIndividualWrite()) securityAttr.setIndividualRead(true);

        // Enforcing permissions logic (to have public read all other read permissions must be read).
        if (securityAttr.isPublicRead()) {
            securityAttr.setIndividualRead(true);
            securityAttr.setManagerRead(true);
            item.setScope(PortfolioItem.PUBLIC_SCOPE);
        } else if (securityAttr.isManagerRead()) {
            item.setScope(PortfolioItem.RESTRICTED_SCOPE);            
        } else {
            item.setScope(PortfolioItem.PRIVATE_SCOPE);
        }
        item.setSecurityAttribute(securityAttr);
    }
}
