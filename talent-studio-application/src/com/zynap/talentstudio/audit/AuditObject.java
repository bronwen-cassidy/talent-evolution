/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.audit;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Jan-2009 12:09:16
 */
public class AuditObject implements Serializable {

    public AuditObject() {
    }

    public AuditObject(Serializable serializedObject) {
        this.serializedObject = serializedObject;
    }

    public void setSerializedObject(Serializable serializable) {
        this.serializedObject = serializable;
    }

    public Serializable getSerializedObject() {
        return serializedObject;
    }

    private Serializable serializedObject;
    private static final long serialVersionUID = -595001461087691811L;
}
