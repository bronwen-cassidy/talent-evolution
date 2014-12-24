/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.crosstab;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Heading implements Serializable, Cloneable, Comparable {

    public Heading() {
    }

    public Heading(String id, String label) {
        this.id = id;
        this.label = label;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int compareTo(Object o) {
        Heading heading = (Heading) o;
        String compareMe = getId() + getLabel();
        String compareYou = heading.getId() + heading.getLabel();
        return compareMe.compareTo(compareYou);
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Heading heading = (Heading) o;

        if (id != null ? !id.equals(heading.id) : heading.id != null) return false;
        if (label != null ? !label.equals(heading.label) : heading.label != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 29 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    public String toString() {
        return label;
    }

    private String id;
    private String label;
}
