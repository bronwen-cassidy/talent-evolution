/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.Collection;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPortfolioDao extends IFinder, IModifiable {

    /**
     * Find all portfolio items for specified node type.
     * <br/> If node type is null, will return all portfolio items.
     * <br/> If content types are null or empty, will return all portfolio items.
     *
     * @param nodeType Should be {@link com.zynap.talentstudio.organisation.Node.SUBJECT_UNIT_TYPE_}
     * or {@link com.zynap.talentstudio.organisation.Node.POSITION_UNIT_TYPE_}
     * @param contentTypes
     * @return Collection of PortfolioItems.
     */
    Collection findAllByNodeType(String nodeType, String[] contentTypes);

    /**
     * Gets the supported content types for the artefact type.
     *
     * @param nodeType Should be {@link com.zynap.talentstudio.organisation.Node.SUBJECT_UNIT_TYPE_}
     * or {@link com.zynap.talentstudio.organisation.Node.POSITION_UNIT_TYPE_}
     * @return Collection of {@link ContentType} objects
     */
    Collection getContentTypes(String nodeType);

    /**
     * Gets the allowed content types.
     *
     * @param nodeType
     * @param contentTypePermits
     * @return Collection of filtered content types
     */
    Collection getContentTypes(String nodeType, Collection contentTypePermits);

    /**
     * Get all content types for both the position and the subject nodes.
     *
     * @return Collection of content types ordered by label
     */
    Collection getAllContentTypes();

    /**
     * Gets all the content types the user is allowed to view for both types of node (subject and position).
     *
     * @param action
     * @param contentTypePermits
     * @return sorted list of content types
     */
    Collection getAllContentTypes(String action, Collection contentTypePermits);

    /**
     * Finds the given content type.
     *
     * @param contentTypeId
     * @return contentType
     */
    ContentType getContentType(String contentTypeId);

    /**
     * Find portfolio item and also check the artefact access -meant for user when viewing items returned by portfolio document searches.
     *
     * @param portfolioItemId The item id
     * @param viewSubjectPermitId
     * @param viewPositionPermitId
     * @return PortfolioItem
     */
    IDomainObject findAndCheckArtefactAccess(Long portfolioItemId, Long viewSubjectPermitId, Long viewPositionPermitId) throws TalentStudioException;

    PortfolioItemFile findItemFile(Long portfolioItemId);

    void createFile(PortfolioItemFile file);

    void updateFile(PortfolioItemFile file);

    void deleteFile(PortfolioItemFile itemFile);
}
