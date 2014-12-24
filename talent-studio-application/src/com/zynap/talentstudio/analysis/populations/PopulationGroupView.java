/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.populations;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 08-Aug-2009 22:28:39
 */
public class PopulationGroupView {

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getActiveCriteria() {
        return activeCriteria;
    }

    public void setActiveCriteria(Long activeCriteria) {
        this.activeCriteria = activeCriteria;
    }

    private Long populationId;
    private Long groupId;
    private Long userId;    
    private String type;
    private String label;
    private String scope;
    private String description;
    private Long activeCriteria;
    
}
