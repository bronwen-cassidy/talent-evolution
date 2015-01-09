/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain;



/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IDomainObject extends IBasicDomainObject {

    Long getId();

    String getLabel();

    void initLazy();

    /**
     * Constant that indicates that the object has no id and is therefore to be ignored. 
     */
    public static final Long UNASSIGNED_VALUE = Long.MIN_VALUE;
    public static final Long ROOT_USER_ID = new Long(0);
}
