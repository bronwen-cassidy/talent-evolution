/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.security.permits;

import com.zynap.talentstudio.security.ISecureResource;

import java.util.Collection;

/**
 * Class or Interface description.
 * 
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IPermit {

    Long getId();

    String getUrl();

    String getIdParam();

    String getContentParam();

    String getContent();

    String getObjectName();

    void setObjectName(String objectName);

    String getLabel();

    void setLabel(String label);

    String getDescription();

    void setDescription(String description);

    String getAction();

    void setAction(String action);

    boolean isActive();

    void setActive(boolean active);

    boolean isSelected();

    void setSelected(boolean selected);

    void addSecureResource(ISecureResource secureResource);

    Collection<ISecureResource> getSecureResources();

    /**
     * Is this an access permit or a domain permit.
     *
      * @return true if this is an access permit, otherwise false.
     */
    boolean isAccess();
}
