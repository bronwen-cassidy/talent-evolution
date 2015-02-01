/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author taulant.bajraktari
 * @since 18-Jul-2008 09:48:28
 */
public class Answer implements Serializable {

    public Answer() {
    }

    public Answer(String value, Long id) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String value;
    private Long id;
}
