/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.populations;

import com.zynap.domain.IDomainObject;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Aug-2008 09:44:15
 */
public class PopulationDto implements Serializable, IDomainObject {

    public PopulationDto(Long id, String label, String type, String scope, String description) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.scope = scope;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void initLazy() {}

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public String getDescription() {
        return description;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PopulationDto)) return false;

        PopulationDto that = (PopulationDto) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }

    private Long id;
    private String label;
    private String type;
    private String scope;
    private String description;
}
