/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.permits;

import com.zynap.exception.TalentStudioException;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPermitManagerDao {

    /**
     * Get all the active permissions relating to access roles.
     *
     * @return List of active permissions.
     */
    List<IPermit> getActiveAccessPermits();

    /**
     * Get all the active permissions relating to resource roles.
     *
     * @return List of active permissions.
     */
    List<IPermit> getActiveDomainObjectPermits();

    /**
     * Get all active permits (DomainObjectPermits or AccessPermits.)
     *
     * @return The List of active permits.
     */
    List<IPermit> getActivePermits();

    /**
     * Get the permit for the specified content type and action eg: "Positions" "edit".
     *
     * @param content The content
     * @param action The action
     * @return The IPermit
     */
    IPermit getPermit(String content, String action);

    /**
     * Get the permits assigned to the user.
     *
     * @param userId The user id
     * @return A Collection of {@link AccessPermit}s
     */
    Collection<IPermit> getAccessPermits(Long userId);

    /**
     * Get the content type permits assigned to the user.
     *
     * @param principalId
     * @param content
     * @param action
     * @return Collection of {@link AccessPermit}
     */
    Collection<IPermit> getContentTypePermits(Long principalId, String content, String action);

    /**
     * Get ids of nodes of the specified type(s) the user has access to
     * @param userId The user id
     * @param nodeTypes The node types
     * @param action The permit action
     * @return Collection of Longs
     */
    Collection getNodeIds(Long userId, String[] nodeTypes, String action);


	/**
	 * Get all permits for the specified action and content.
	 * @param action
	 * @param content
	 * @return Collection (never null)
	 * @throws com.zynap.exception.TalentStudioException
	 */
	Collection getPermits(String action, String content) throws TalentStudioException;

    Long getViewOrgUnitPermitId();
}