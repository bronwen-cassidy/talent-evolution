/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.portfolio;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 22-Aug-2008 14:07:03
 */
public class PortfolioItemFile implements Serializable {

    public PortfolioItemFile() {
    }

    public byte[] getBlobValue() {
        return blobValue;
    }

    public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private byte[] blobValue;
    private Long id;
}
