/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.permits.IPermit;

import java.util.Collection;
import java.util.HashSet;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PortfolioService extends DefaultService implements IPortfolioService {

    /**
     * Gets the supported content types for the artefact type.
     *
     * @param node The subject or position this new portfolio item belongs to
     * @param action The action (read or create) being used to determine whether the node is allowed
     * @return Collection of {@link ContentType} objects
     */
    public Collection getContentTypes(Node node, String action) {

        final String nodeType = node.getNodeType();
        return getContentTypes(nodeType, action);
    }

    public Collection getContentTypes(final String nodeType, String action) {
        Collection contentTypePermits = getContentTypePermits(action);
        return portfolioDao.getContentTypes(nodeType, contentTypePermits);
    }

    /**
     * Get all content types for both the position and the subject nodes
     *
     * @param action
     * @return sorted list ordered by label
     */
    public Collection getAllContentTypes(String action) {
        Collection contentTypePermits = getContentTypePermits(action);
        return portfolioDao.getAllContentTypes(action, contentTypePermits);
    }

    public Collection getContentTypes(String action, Collection contentTypePermits) {
        return portfolioDao.getContentTypes(action, contentTypePermits);
    }

    public Collection getAllContentTypes() {
        return portfolioDao.getAllContentTypes();
    }

    public PortfolioItemFile findItemFile(Long portfolioItemId) {
        return portfolioDao.findItemFile(portfolioItemId);
    }

    public void create(PortfolioItem item, PortfolioItemFile file) throws TalentStudioException {
        create(item);
        file.setId(item.getId());
        portfolioDao.createFile(file);
    }

    /**
     * Updates a portfolio item and it's file.     
     * @param item
     * @param file
     * @throws TalentStudioException
     */
    public void update(PortfolioItem item, PortfolioItemFile file) throws TalentStudioException {
        update(item);
        portfolioDao.updateFile(file);
    }

    public Collection getContentTypes(String nodeType) {
        return portfolioDao.getContentTypes(nodeType);
    }

    /**
     * Find portfolio item and also check the artefact access - meant for user when viewing items returned by portfolio document searches.
     *
     * @param portfolioItemId The item id
     * @return PortfolioItem
     */
    public IDomainObject findAndCheckArtefactAccess(Long portfolioItemId) throws TalentStudioException {
        return portfolioDao.findAndCheckArtefactAccess(portfolioItemId, getViewSubjectPermitId(), getViewPositionPermitId());
    }

    public ContentType findContentType(String contentTypeId) {
        return portfolioDao.getContentType(contentTypeId);
    }

    public void delete(Long id) throws TalentStudioException {
        PortfolioItem portfolioItem = (PortfolioItem) findById(id);
        Node node = portfolioItem.getNode();
        node.removePortfolioItem(portfolioItem);
        portfolioDao.delete(portfolioItem);
        PortfolioItemFile portfolioItemFile = null;
        try {
            portfolioItemFile = findItemFile(id);
        } catch (Throwable e) {
            // ignored nothing to delete
        }
        if (portfolioItemFile != null) {
            portfolioDao.deleteFile(portfolioItemFile);
        }
    }

    public void setPortfolioDao(IPortfolioDao portfolioDao) {
        this.portfolioDao = portfolioDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    protected IFinder getFinderDao() {
        return portfolioDao;
    }

    protected IModifiable getModifierDao() {
        return portfolioDao;
    }

    private Collection getContentTypePermits(String action) {
        if (contentPermits == null) {
            contentPermits = permitManagerDao.getContentTypePermits(getUserId(), "c_type", action);
        }

        return new HashSet<IPermit>(contentPermits);
    }

    private Long getViewPositionPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private Long getViewSubjectPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private static Collection<IPermit> contentPermits;
    private IPermitManagerDao permitManagerDao;
    private IPortfolioDao portfolioDao;
}
