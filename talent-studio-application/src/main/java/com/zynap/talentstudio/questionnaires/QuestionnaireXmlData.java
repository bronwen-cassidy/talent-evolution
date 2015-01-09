/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Jul-2008 16:16:57
 */
public class QuestionnaireXmlData implements Serializable {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getXmlDefinition() {
        return this.xmlDefinition;
    }

    public void setXmlDefinition(byte[] xmlDefinition) {
        this.xmlDefinition = xmlDefinition;
    }

    private byte[] xmlDefinition;
    private Long id;
}
