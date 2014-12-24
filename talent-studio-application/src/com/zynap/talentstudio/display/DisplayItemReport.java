/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.display;

import com.zynap.talentstudio.analysis.reports.Report;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DisplayItemReport implements Serializable {

    public DisplayItemReport() {
    }

    public DisplayItemReport(DisplayConfigItem displayConfigItem, Report report, String contentType) {
        this.displayConfigItem = displayConfigItem;
        this.report = report;
        this.contentType = contentType;
    }

    public DisplayItemReport(DisplayConfigItem displayConfigItem, Report report, String contentType, Long id) {
        this(displayConfigItem, report, contentType);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DisplayConfigItem getDisplayConfigItem() {
        return displayConfigItem;
    }

    public void setDisplayConfigItem(DisplayConfigItem displayConfigItem) {
        this.displayConfigItem = displayConfigItem;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isSubjectPrimaryAssociation() {
        return SUBJECT_PRIMARY_ASSOCIATION_TYPE.equals(getContentType());
    }

    public boolean isSubjectSecondaryAssociation() {
        return SUBJECT_SECONDARY_ASSOCIATION_TYPE.equals(getContentType());
    }

    public boolean isPositionPrimarySourceAssociation() {
        return PRIMARY_SOURCE_ASSOCIATION.equals(getContentType());
    }

    public boolean isPositionPrimaryTargetAssociation() {
        return PRIMARY_TARGET_ASSOCIATION.equals(getContentType());
    }

    public boolean isPositionSecondarySourceAssociation() {
        return SECONDARY_SOURCE_ASSOCIATION.equals(getContentType());
    }

    public boolean isPositionSecondaryTargetAssociation() {
        return SECONDARY_TARGET_ASSOCIATION.equals(getContentType());
    }

    public boolean isSubjectPositionPrimaryAssociation() {
        return SUBJECT_POSITION_PRIMARY_ASSOCIATION.equals(getContentType());
    }

    public boolean isSubjectPositionSecondaryAssociation() {
        return SUBJECT_POSITION_SECONDARY_ASSOCIATION.equals(getContentType());
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof DisplayItemReport)) return false;
        DisplayItemReport castOther = (DisplayItemReport) other;
        return new EqualsBuilder()
                .append(getId(), castOther.getId())
                .append(getContentType(), castOther.getContentType())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .append(getContentType())
                .toHashCode();
    }

    private DisplayConfigItem displayConfigItem;
    private Report report;
    private String contentType;
    private Long id;

    public static final String SUBJECT_PRIMARY_ASSOCIATION_TYPE = "SUBJECT_PRIMARY";
    public static final String SUBJECT_SECONDARY_ASSOCIATION_TYPE = "SUBJECT_SECONDARY";

    public static final String PRIMARY_SOURCE_ASSOCIATION = "PRIMARY_SOURCE";
    public static final String PRIMARY_TARGET_ASSOCIATION = "PRIMARY_TARGET";
    public static final String SECONDARY_SOURCE_ASSOCIATION = "SECONDARY_SOURCE";
    public static final String SECONDARY_TARGET_ASSOCIATION = "SECONDARY_TARGET";
    public static final String SUBJECT_POSITION_PRIMARY_ASSOCIATION = "SUBJECT_POS_PRIMARY";
    public static final String SUBJECT_POSITION_SECONDARY_ASSOCIATION = "SUBJECT_POS_SECONDARY";
}
