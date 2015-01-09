package com.zynap.talentstudio.organisation.portfolio;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import com.zynap.domain.IDomainObject;
import com.zynap.domain.UserPrincipal;
import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class TestPortfolioService extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        portfolioService = (IPortfolioService) applicationContext.getBean("portfolioService");
    }

    public void testGetContentTypes() throws Exception {
        Node n = new Subject();
        n.setId(new Long(0));
        Collection contentTypes = portfolioService.getContentTypes(n, SecurityConstants.VIEW_ACTION);
        assertNotNull("the content types should not have been null", contentTypes);
        assertFalse("content types should have been found", contentTypes.isEmpty());
    }

    public void testGetContentTypesNonSecure() throws Exception {
        Node n = new Position();
        n.setId(DEFAULT_POSITION_ID);
        Collection contentTypes = portfolioService.getContentTypes(n.getNodeType());
        assertNotNull("the content types should not have been null", contentTypes);
        assertFalse("content types should have been found", contentTypes.isEmpty());
        assertEquals(3, contentTypes.size());
    }

    public void testGetAllContentTypes() throws Exception {
        final Collection allContentTypes = portfolioService.getAllContentTypes();
        assertNotNull(allContentTypes);
    }

    public void testGetAllContentTypesByAction() throws Exception {
        final Collection allContentTypes = portfolioService.getAllContentTypes(SecurityConstants.VIEW_ACTION);
        assertNotNull(allContentTypes);
    }

    public void testDelete() throws Exception {

        IPositionService positionService = (IPositionService) getBean("positionService");
        Position position = positionService.findByID(DEFAULT_POSITION_ID);

        final PortfolioItem portfolioItem = new PortfolioItem("label", "comments", "status", "scope", new Date(), "subtype");
        PortfolioItemFile file = new PortfolioItemFile();
        file.setBlobValue("This is a test".getBytes());
        
        position.addPortfolioItem(portfolioItem);
        portfolioService.create(portfolioItem, file);
        final Long id = portfolioItem.getId();
        portfolioService.delete(id);

        try {
            portfolioService.findById(id);
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testDeleteInvalidId() throws Exception {
        try {
            portfolioService.delete(new Long(-11));
            fail("Should have thrown an Exception");
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testFindContentType() throws Exception {
        final ContentType contentType = portfolioService.findContentType("CV");
        assertNotNull(contentType);
    }

    public void testFindByInvalidId() throws Exception {
        try {
            portfolioService.findById(new Long(-10));
        } catch (DomainObjectNotFoundException expected) {
        }
    }

    public void testCreate() throws Exception {

        final PortfolioItem portfolioItem = new PortfolioItem("label", "comments", "status", "scope", new Date(), "subtype");
        portfolioService.create(portfolioItem);

        final Long id = portfolioItem.getId();
        assertNotNull(id);

        final IDomainObject found = portfolioService.findById(id);
        assertEquals(portfolioItem.getLabel(), found.getLabel());
    }

    public void testUpdate() throws Exception {

        final PortfolioItem portfolioItem = new PortfolioItem("label", "comments", "status", "scope", new Date(), "subtype");
        portfolioService.create(portfolioItem);

        final Long id = portfolioItem.getId();
        final PortfolioItem found = (PortfolioItem) portfolioService.findById(id);

        found.setLabel("newlabel");
        portfolioService.update(found);
    }

    public void testFindAndCheckArtefactAccess() throws Exception {

        final PortfolioItem portfolioItem = new PortfolioItem("label", "comments", "status", "scope", new Date(), "subtype");
        portfolioItem.setNode(DEFAULT_POSITION);
        DEFAULT_POSITION.setHasAccess(false);

        final ContentType contentType = (ContentType) portfolioService.getContentTypes(Node.POSITION_UNIT_TYPE_).iterator().next();
        portfolioItem.setContentType(contentType);
        portfolioService.create(portfolioItem);

        final Long id = portfolioItem.getId();
        final PortfolioItem found = (PortfolioItem) portfolioService.findAndCheckArtefactAccess(id);
        assertEquals(found, portfolioItem);
        assertTrue(found.getNode().isHasAccess());
    }

    public void testFindAndCheckArtefactAccessDenied() throws Exception {

        final PortfolioItem portfolioItem = new PortfolioItem("label", "comments", "status", "scope", new Date(), "subtype");
        portfolioItem.setNode(DEFAULT_POSITION);
        DEFAULT_POSITION.setHasAccess(false);

        final ContentType contentType = (ContentType) portfolioService.getContentTypes(Node.POSITION_UNIT_TYPE_).iterator().next();
        portfolioItem.setContentType(contentType);
        portfolioService.create(portfolioItem);

        final UserPrincipal userPrincipal = new UserPrincipal(new User(new Long(-999), "test", "test", "test"), new ArrayList<IPermit>());
        final UserSession newSession = new UserSession(userPrincipal, getArenaMenuHandler());
        UserSessionFactory.setUserSession(newSession);

        final Long id = portfolioItem.getId();
        final PortfolioItem found = (PortfolioItem) portfolioService.findAndCheckArtefactAccess(id);
        assertEquals(found, portfolioItem);
        assertFalse(found.getNode().isHasAccess());
    }

    private IPortfolioService portfolioService;
}