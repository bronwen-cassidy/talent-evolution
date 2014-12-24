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
public abstract class ZynapDomainObject implements IDomainObject {

    private static final long serialVersionUID = 8024149901561258691L;

    /**
     * Constructor.
     */
    public ZynapDomainObject() {
    }

    /**
     * Constructor.
     * @param id unique identifier for the domain object
     */
    protected ZynapDomainObject(Long id) {
        this.id = id;
    }

    /**
     * Constructor.
     * @param id unique identifier for the domain object
     * @param label label for the domain object
     */
    protected ZynapDomainObject(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    /**
     * Constructor.
     * @param id unique identifier for the domain object
     * @param active boolean to indicate whether or not the domain object is active or not
     * @param label label for the domain object
     */
    protected ZynapDomainObject(Long id, boolean active, String label) {
        this.id = id;
        this.active = active;
        this.label = label;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getLockId() {
        return lockId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final boolean equals(Object object) {
        if(object == null) return false;
        if (!(object.getClass().isAssignableFrom(this.getClass()) || this.getClass().isAssignableFrom(object.getClass())))
            return false;
        else {
            ZynapDomainObject zobject = (ZynapDomainObject) object;

            if (this.getId() == null || zobject.getId() == null) return (this == object);
            return (this.getId().longValue() == zobject.getId().longValue());
        }
    }

    public final int hashCode() {
        return id == null ? super.hashCode(): id.hashCode();
    }


    public void initLazy() {}

    protected Long id;

    protected boolean active = true;
    protected Integer lockId = -1;
    protected String label;
}
