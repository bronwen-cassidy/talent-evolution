/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 10-Jul-2008 15:56:00
 */
public class SubjectPicture {

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isHasValue() {
        return picture != null && picture.length > 0;
    }

    private Long subjectId;
    private byte[] picture;
}
