/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.common.lookups.LookupValue;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ColumnDisplayImage extends ZynapDomainObject implements Comparable<ColumnDisplayImage> {

    public ColumnDisplayImage() {
    }

    public ColumnDisplayImage(LookupValue lookupValue, String displayImage) {
        this(null, lookupValue, displayImage);
    }

    public ColumnDisplayImage(Long id, LookupValue lookupValue, String displayImage) {
        this.id = id;
        this.lookupValue = lookupValue;
        this.displayImage = displayImage;
    }

    public LookupValue getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(LookupValue lookupValue) {
        this.lookupValue = lookupValue;
    }

    public String getLabel() {
        return displayImage;
    }

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public boolean matches(LookupValue lookupValueToFind) {
        return lookupValue != null && lookupValue.equals(lookupValueToFind);
    }

    public int compareTo(ColumnDisplayImage o) {
        return lookupValue.compareBySortOrder(o.lookupValue);
    }

    private LookupValue lookupValue;
    private String displayImage;
    private Column column;
}
