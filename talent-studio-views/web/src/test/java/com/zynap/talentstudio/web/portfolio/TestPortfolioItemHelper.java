package com.zynap.talentstudio.web.portfolio;

/**
 * User: amark
 * Date: 24-May-2005
 * Time: 13:14:43
 */

import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.ContentType;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.portfolio.PortfolioItem;
import com.zynap.talentstudio.organisation.portfolio.search.SearchResult;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.SecurityAttribute;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.positions.PositionWrapperBean;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Date;

public class TestPortfolioItemHelper extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        portfolioService = (IPortfolioService) applicationContext.getBean("portfolioService");
    }

    public void testMapResults() throws Exception {

        // add 1 item belonging to one artefact
        final List searchResults = new ArrayList();
        final SearchResult searchResult1 = new SearchResult();
        final Long firstArtefactId = new Long(1);
        searchResult1.setArtefactId(firstArtefactId.toString());
        searchResult1.setItemId(new Long(1));
        searchResults.add(searchResult1);

        // add 2 items belonging to second artefact
        final SearchResult searchResult2 = new SearchResult();
        final Long secondArtefactId = new Long(2);
        searchResult2.setArtefactId(secondArtefactId.toString());
        searchResult2.setItemId(new Long(2));
        searchResults.add(searchResult2);
        final SearchResult searchResult3 = new SearchResult();
        searchResult3.setArtefactId(secondArtefactId.toString());
        searchResult3.setItemId(new Long(3));
        searchResults.add(searchResult3);

        // sorted map should contain 2 entries - one per artefact
        final List itemIds = new ArrayList();
        final Map mappedSearchResults = PortfolioItemHelper.mapResults(searchResults, itemIds);
        assertEquals(2, mappedSearchResults.size());

        // itemIds should contain same number of elements as Map and in the same order
        assertEquals(searchResults.size(), itemIds.size());
        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = (Long) itemIds.get(i);
            assertEquals(itemId, ((SearchResult) searchResults.get(i)).getItemId());
        }

        // check that list for second artefact contains 2 items and that 1st item is item 1 (ie: check that order has not been lost)
        final List secondArtefactItems = (List) mappedSearchResults.get(secondArtefactId);
        assertEquals(2, secondArtefactItems.size());
        assertEquals(new Long(1), ((SearchResult) searchResults.get(0)).getItemId());
    }

    public void testFilterPublicItems() throws Exception {

        final Long newUserId = new Long(-1);
        final UserPrincipal userPrincipal = new UserPrincipal(new User(newUserId, "username", "fname", "lname"));

        // create test position with some public portfolio items
        Set portfolioItems = new HashSet();
        final PortfolioItem fileItem1 = new PortfolioItem();
        fileItem1.setId(new Long(1));
        fileItem1.setScope(PortfolioItem.PUBLIC_SCOPE);
        fileItem1.setCreatedById(new Long(-99));
        fileItem1.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        fileItem1.setLastModified(new Date());
        portfolioItems.add(fileItem1);

        final PortfolioItem fileItem2 = new PortfolioItem();
        fileItem2.setId(new Long(2));
        fileItem2.setScope(PortfolioItem.PUBLIC_SCOPE);
        fileItem2.setCreatedById(new Long(-100));
        fileItem2.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        fileItem2.setLastModified(new Date());
        portfolioItems.add(fileItem2);

        Position position = DEFAULT_POSITION;
        position.setPortfolioItems(portfolioItems);
        NodeWrapperBean nodeWrapperBean = new PositionWrapperBean(position);

        UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        PortfolioItemHelper.filterPortfolioItems(nodeWrapperBean, userSession);

        final Collection filteredPortfolioItems = nodeWrapperBean.getPortfolioItems();
        assertEquals(2, filteredPortfolioItems.size());
    }

    public void testFilterPrivateItems() throws Exception {

        final Long newUserId = new Long(-1);
        final Long newUserId2 = new Long(-2);

        final UserPrincipal userPrincipal = new UserPrincipal(new User(newUserId, "username", "fname", "lname"));

        // create test position with some private portfolio items - some of which have been created by the current user
        Set portfolioItems = new HashSet();
        final PortfolioItem fileItem1 = new PortfolioItem();
        fileItem1.setId(new Long(1));
        fileItem1.setLabel("item1");
        fileItem1.setCreatedById(newUserId);
        fileItem1.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem1.setLastModified(new Date());
        portfolioItems.add(fileItem1);

        // this item has not been created by the current user
        final PortfolioItem fileItem2 = new PortfolioItem();
        fileItem2.setId(new Long(2));
        fileItem2.setLabel("item2");
        fileItem2.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem2.setCreatedById(newUserId2);
        fileItem2.setLastModified(new Date());
        portfolioItems.add(fileItem2);

        final PortfolioItem fileItem3 = new PortfolioItem();
        fileItem3.setId(new Long(3));
        fileItem3.setLabel("item3");
        fileItem3.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem3.setCreatedById(newUserId);
        fileItem3.setLastModified(new Date());
        portfolioItems.add(fileItem3);

        Position position = DEFAULT_POSITION;
        position.setPortfolioItems(portfolioItems);
        NodeWrapperBean nodeWrapperBean = new PositionWrapperBean(position);

        UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());
        PortfolioItemHelper.filterPortfolioItems(nodeWrapperBean, userSession);

        // there should only be 2 items - 1 and 3 - as private items are only displayed if the user viewing the portfolio created the items
        final Collection filteredPortfolioItems = nodeWrapperBean.getPortfolioItems();
        assertEquals(2, filteredPortfolioItems.size());
        assertTrue(filteredPortfolioItems.contains(fileItem1));
        assertFalse(filteredPortfolioItems.contains(fileItem2));
        assertTrue(filteredPortfolioItems.contains(fileItem3));
    }

    public void testFilterPrivateItemsRootUser() throws Exception {

        final Long userId = new Long(-1);
        final UserPrincipal userPrincipal = new UserPrincipal(new User(userId, "username", "fname", "lname"));
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());

        Set portfolioItems = new HashSet();
        final PortfolioItem fileItem1 = new PortfolioItem();
        fileItem1.setId(new Long(1));
        fileItem1.setLabel("item1");
        fileItem1.setCreatedById(userId);
        fileItem1.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem1.setLastModified(new Date());
        portfolioItems.add(fileItem1);

        // this item has not been created by the current user
        final PortfolioItem fileItem2 = new PortfolioItem();
        fileItem2.setId(new Long(2));
        fileItem2.setLabel("item2");
        fileItem2.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem2.setCreatedById(userId);
        fileItem2.setLastModified(new Date());
        portfolioItems.add(fileItem2);

        final PortfolioItem fileItem3 = new PortfolioItem();
        fileItem3.setId(new Long(3));
        fileItem3.setLabel("item3");
        fileItem3.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        fileItem3.setCreatedById(userId);
        fileItem3.setLastModified(new Date());
        portfolioItems.add(fileItem3);

        Position position = DEFAULT_POSITION;
        position.setPortfolioItems(portfolioItems);
        NodeWrapperBean nodeWrapperBean = new PositionWrapperBean(position);

        PortfolioItemHelper.filterPortfolioItems(nodeWrapperBean, userSession);

        // all items should be displayed as root user can see everything
        final Collection filteredPortfolioItems = nodeWrapperBean.getPortfolioItems();
        assertEquals(3, filteredPortfolioItems.size());
        assertTrue(filteredPortfolioItems.contains(fileItem1));
        assertTrue(filteredPortfolioItems.contains(fileItem2));
        assertTrue(filteredPortfolioItems.contains(fileItem3));
    }

    public void testFilterPersonalPortfolioRestrictedItems() throws Exception {

        // test restricted items in own portfolio
        final Long userId = new Long(-1);
        Subject subject = new Subject(userId, null);

        final Collection contentTypes = portfolioService.getContentTypes(subject, SecurityConstants.VIEW_ACTION);
        final ContentType contentType = (ContentType) contentTypes.iterator().next();
        final String contentSubType = contentType.getContentSubTypes()[0];

        Set portfolioItems = new HashSet();

        // this file item has content type so it is will be left in by the filtering
        final PortfolioItem fileItem2 = new PortfolioItem();
        fileItem2.setId(new Long(2));
        fileItem2.setSecurityAttribute(new SecurityAttribute(true, true, true, true, false));
        fileItem2.setContentType(contentType);
        portfolioItems.add(fileItem2);

        // add a public item
        final PortfolioItem publicItem = new PortfolioItem();
        publicItem.setId(new Long(3));
        publicItem.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        publicItem.setContentType(contentType);
        publicItem.setContentSubType(contentSubType);
        portfolioItems.add(publicItem);

        // add a private item
        final PortfolioItem privateItem = new PortfolioItem();
        privateItem.setId(new Long(4));
        privateItem.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        privateItem.setContentType(contentType);
        privateItem.setContentSubType(contentSubType);
        privateItem.setCreatedById(userId);
        portfolioItems.add(privateItem);

        subject.setPortfolioItems(portfolioItems);
        NodeWrapperBean nodeWrapperBean = new SubjectWrapperBean(subject);

        PortfolioItemHelper.filterPersonalPortfolioItems(nodeWrapperBean);

        // filtering should have left public and private item in list
        final Collection filteredPortfolioItems = nodeWrapperBean.getPortfolioItems();
        assertEquals(3, filteredPortfolioItems.size());
        assertTrue(filteredPortfolioItems.contains(fileItem2));
        assertTrue(filteredPortfolioItems.contains(publicItem));
        assertTrue(filteredPortfolioItems.contains(privateItem));
    }

    public void testFilterPersonalRestrictedItems() throws Exception {

        final Long userId = new Long(-12);
        final User user = new User(userId, "username", "fname", "lname");

        // test restricted items in portfolio of artefact other than one's own
        // first add a new subject
        Subject subject = new Subject(new Long(-13), new CoreDetail("Angus", "Mark", "amark", "Mr", "angus@one.com", "0889776876"));
        subject.setUser(user);

        final String contentSubType = "CV";

        Set portfolioItems = new HashSet();

        // add restricted item only managers can see
        final PortfolioItem fileItem1 = new PortfolioItem();
        fileItem1.setCreatedById(new Long(0));
        fileItem1.setLabel("restricted create by admin 1");
        fileItem1.setSecurityAttribute(new SecurityAttribute(false, false, true, true, false));
        fileItem1.setContentSubType(contentSubType);
        fileItem1.setNode(subject);
        portfolioItems.add(fileItem1);

        // add restricted item only managers can see
        final PortfolioItem fileItem2 = new PortfolioItem();
        fileItem2.setCreatedById(new Long(0));
        fileItem2.setLabel("restricted create by admin 2");
        fileItem2.setSecurityAttribute(new SecurityAttribute(false, false, true, true, false));
        fileItem2.setContentSubType(contentSubType);
        fileItem2.setNode(subject);
        portfolioItems.add(fileItem2);

        // add a public item
        final PortfolioItem publicItem = new PortfolioItem();
        publicItem.setCreatedById(new Long(0));
        publicItem.setLabel("public create by admin");
        publicItem.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        publicItem.setContentSubType(contentSubType);
        publicItem.setNode(subject);
        portfolioItems.add(publicItem);

        // add a private item
        final PortfolioItem privateItem = new PortfolioItem();
        privateItem.setLabel("private created by out user");
        privateItem.setSecurityAttribute(new SecurityAttribute(true, true, false, false, false));
        privateItem.setContentSubType(contentSubType);
        privateItem.setCreatedById(userId);
        privateItem.setNode(subject);
        portfolioItems.add(privateItem);

        subject.setPortfolioItems(portfolioItems);

        // set access to subject to false - should remove all restricted portfolio items
        subject.setHasAccess(false);
        NodeWrapperBean nodeWrapperBean = new SubjectWrapperBean(subject);

        PortfolioItemHelper.filterPersonalPortfolioItems(nodeWrapperBean);

        // should only contain the public and private items
        final Collection filteredPortfolioItems = nodeWrapperBean.getPortfolioItems();
        assertEquals(2, filteredPortfolioItems.size());
        assertFalse(filteredPortfolioItems.contains(fileItem1));
        assertFalse(filteredPortfolioItems.contains(fileItem2));
        assertTrue(filteredPortfolioItems.contains(publicItem));
        assertTrue(filteredPortfolioItems.contains(privateItem));
    }

    public void testCheckItemAccess() throws Exception {

        final Long userId = new Long(-1);
        final UserPrincipal userPrincipal = new UserPrincipal(new User(userId, "username", "fname", "lname"), new ArrayList());
        final UserSession userSession = new UserSession(userPrincipal, getArenaMenuHandler());

        final Long userId2 = new Long(-2);
        final User root = new User(userId2, "username", "fname", "lname");
        root.setRoot(true);
        final UserPrincipal rootUserPrincipal = new UserPrincipal(root, new ArrayList());
        final UserSession rootUserSession = new UserSession(rootUserPrincipal, getArenaMenuHandler());

        final PortfolioItem portfolioItem = new PortfolioItem();
        portfolioItem.setId(new Long(2));


        // make public and check
        portfolioItem.setCreatedById(ROOT_USER_ID);
        portfolioItem.setSecurityAttribute(new SecurityAttribute(true, true, true, true, true));
        portfolioItem.setScope(PortfolioItem.PUBLIC_SCOPE);
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, userSession));
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, rootUserSession));

        final Node node = new Subject();
        node.setHasAccess(false);
        // make private - no access as user did not create it
        portfolioItem.setSecurityAttribute(new SecurityAttribute(false, false, false, false, false));
        portfolioItem.setScope(PortfolioItem.PRIVATE_SCOPE);
        portfolioItem.setNode(node);
        assertFalse(PortfolioItemHelper.checkItemAccess(portfolioItem, userSession));
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, rootUserSession));

        // set created by - acces no allowed as user created it
        portfolioItem.setCreatedById(userId);
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, userSession));
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, rootUserSession));

        // make restricted and set access to Node false
        portfolioItem.setNode(node);
        portfolioItem.setSecurityAttribute(new SecurityAttribute(false, false, true, true, false));
        portfolioItem.setCreatedById(ROOT_USER_ID);
        assertFalse(PortfolioItemHelper.checkItemAccess(portfolioItem, userSession));
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, rootUserSession));

        // still restricted but has access
        node.setHasAccess(true);
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, userSession));
        assertTrue(PortfolioItemHelper.checkItemAccess(portfolioItem, rootUserSession));
    }

    public void testEnforceSecurityLogic() throws Exception {

        PortfolioItem item = new PortfolioItem();
        SecurityAttribute securityAttr;
        SecurityAttribute modifiedAttr;

        // check that when individual write is set that individual read is set
        securityAttr = new SecurityAttribute(false, true, false, false, false);
        item.setSecurityAttribute(securityAttr);

        PortfolioItemHelper.enforceSecurityLogic(item, true);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), true);
        assertEquals(modifiedAttr.isManagerRead(), false);
        assertEquals(modifiedAttr.isManagerWrite(), false);
        assertEquals(modifiedAttr.isPublicRead(), false);

        securityAttr = new SecurityAttribute(false, true, false, false, false);
        item.setSecurityAttribute(securityAttr);
        PortfolioItemHelper.enforceSecurityLogic(item, false);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), true);
        assertEquals(modifiedAttr.isManagerRead(), false);
        assertEquals(modifiedAttr.isManagerWrite(), false);
        assertEquals(modifiedAttr.isPublicRead(), false);

        // check that when manager write is set that manager read is set
        securityAttr = new SecurityAttribute(false, false, false, true, false);
        item.setSecurityAttribute(securityAttr);

        PortfolioItemHelper.enforceSecurityLogic(item, true);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), true);
        assertEquals(modifiedAttr.isManagerRead(), true);
        assertEquals(modifiedAttr.isManagerWrite(), true);
        assertEquals(modifiedAttr.isPublicRead(), false);

        securityAttr = new SecurityAttribute(false, false, false, true, false);
        item.setSecurityAttribute(securityAttr);
        PortfolioItemHelper.enforceSecurityLogic(item, false);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), false);
        assertEquals(modifiedAttr.isIndividualWrite(), false);
        assertEquals(modifiedAttr.isManagerRead(), true);
        assertEquals(modifiedAttr.isManagerWrite(), true);
        assertEquals(modifiedAttr.isPublicRead(), false);

        // check that when public read is set that individual and manager read are also set
        securityAttr = new SecurityAttribute(false, false, false, false, true);
        item.setSecurityAttribute(securityAttr);

        PortfolioItemHelper.enforceSecurityLogic(item, true);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), true);
        assertEquals(modifiedAttr.isManagerRead(), true);
        assertEquals(modifiedAttr.isManagerWrite(), false);
        assertEquals(modifiedAttr.isPublicRead(), true);

        securityAttr = new SecurityAttribute(false, false, false, false, true);
        item.setSecurityAttribute(securityAttr);
        PortfolioItemHelper.enforceSecurityLogic(item, false);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), false);
        assertEquals(modifiedAttr.isManagerRead(), true);
        assertEquals(modifiedAttr.isManagerWrite(), false);
        assertEquals(modifiedAttr.isPublicRead(), true);

        // check that it's my portfolio then individual read and write are set
        securityAttr = new SecurityAttribute(false, false, false, false, false);
        item.setSecurityAttribute(securityAttr);

        PortfolioItemHelper.enforceSecurityLogic(item, true);
        modifiedAttr = item.getSecurityAttribute();
        assertEquals(modifiedAttr.isIndividualRead(), true);
        assertEquals(modifiedAttr.isIndividualWrite(), true);
        assertEquals(modifiedAttr.isManagerRead(), false);
        assertEquals(modifiedAttr.isManagerWrite(), false);
        assertEquals(modifiedAttr.isPublicRead(), false);
    }
    
    private IPortfolioService portfolioService;
}
