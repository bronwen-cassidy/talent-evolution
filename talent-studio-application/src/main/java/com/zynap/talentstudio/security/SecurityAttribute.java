/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 23-Jan-2007 15:08:31
 */
public class SecurityAttribute implements Serializable {

    public SecurityAttribute() {
    }


    public SecurityAttribute(boolean individualRead, boolean individualWrite, boolean managerRead, boolean managerWrite, boolean publicRead) {
        isIndividualRead = individualRead;
        isIndividualWrite = individualWrite;
        isManagerRead = managerRead;
        isManagerWrite = managerWrite;
        isPublicRead = publicRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIndividualRead() {
        return isIndividualRead;
    }

    public void setIndividualRead(boolean individualRead) {
        isIndividualRead = individualRead;
    }

    public boolean isIndividualWrite() {
        return isIndividualWrite;
    }

    public void setIndividualWrite(boolean individualWrite) {
        isIndividualWrite = individualWrite;
    }

    public boolean isManagerRead() {
        return isManagerRead;
    }

    public void setManagerRead(boolean managerRead) {
        isManagerRead = managerRead;
    }

    public boolean isManagerWrite() {
        return isManagerWrite;
    }

    public void setManagerWrite(boolean managerWrite) {
        isManagerWrite = managerWrite;
    }


    public boolean isPublicRead() {
        return isPublicRead;
    }

    public void setPublicRead(boolean publicRead) {
        isPublicRead = publicRead;
    }

    private Long id;

    private boolean isIndividualRead;
    private boolean isIndividualWrite;

    private boolean isManagerRead;
    private boolean isManagerWrite;

    private boolean isPublicRead;

}
