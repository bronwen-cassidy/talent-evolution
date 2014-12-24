/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis;

import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 27-Feb-2008 09:33:00
 */
public class Page implements Serializable {

    public Integer getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(Integer numRecords) {
        this.numRecords = numRecords;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public List<? extends Node> getResults() {
        return results;
    }

    public void setResults(List<? extends Node> results) {
        this.results = results;
    }

    private Integer numRecords;
    private int start;
    private int next;
    private List<? extends Node> results = new ArrayList<Node>();
}
