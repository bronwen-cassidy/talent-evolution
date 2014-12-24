/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.organisation.Node;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPortfolioService extends IZynapService {

    Collection getContentTypes(Node node, String action);

    Collection getContentTypes(String nodeType, String action);

    Collection getContentTypes(String nodeType);

    /**
     * Find portfolio item and also check the artefact access -meant for user when viewing items returned by portfolio document searches.
     *
     * @param portfolioItemId The item id
     * @return PortfolioItem
     */
    IDomainObject findAndCheckArtefactAccess(Long portfolioItemId) throws TalentStudioException;

    ContentType findContentType(String contentTypeId);

    void delete(Long id) throws TalentStudioException;

    /**
     * Get all content types for both the position and the subject nodes
     *
     * @param viewAction
     * @return sorted list ordered by label
     */
    Collection getAllContentTypes(String viewAction);

    Collection getContentTypes(String action, Collection contentTypePermits);

    /**
     * Get all content types for both the position and the subject nodes
     * @return Collection of content types ordered by label
     */
    Collection getAllContentTypes();

    PortfolioItemFile findItemFile(Long portfolioItemId);

    void create(PortfolioItem item, PortfolioItemFile file) throws TalentStudioException;

    void update(PortfolioItem item, PortfolioItemFile file) throws TalentStudioException;
}
