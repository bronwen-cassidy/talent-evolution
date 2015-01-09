package com.zynap.talentstudio.search;

import java.io.Serializable;

/**
 *
 */
public class DataTerm implements Serializable {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private Long id;
    private String label;

}
