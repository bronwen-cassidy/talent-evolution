package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.web.utils.tree.TreeWrapperBean;
import com.zynap.talentstudio.web.utils.tree.Branch;

import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 06-Feb-2006
 * Time: 18:46:35
 */
public final class AnalysisAttributeTreeWrapperBean extends TreeWrapperBean {

    private String viewType;

    private String type;

    public AnalysisAttributeTreeWrapperBean(List<Branch> tree, String popupId, String leafIcon, String artefactType, String viewType) {
        super(tree, null, popupId, leafIcon);
        this.type = artefactType;
        this.viewType = viewType;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}