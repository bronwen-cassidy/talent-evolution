/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Sep-2008 10:28:27
 */
public class MyTeamView implements Serializable {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDynamicAttributeId() {
        return dynamicAttributeId;
    }

    public void setDynamicAttributeId(Long dynamicAttributeId) {
        this.dynamicAttributeId = dynamicAttributeId;
    }

    private Long id;
    private Long userId;
    private Long dynamicAttributeId;
}
