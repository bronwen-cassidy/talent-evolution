/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.web.utils.tree.IVisitor;
import com.zynap.talentstudio.web.utils.tree.ITreeContainer;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Jul-2009 10:04:59
 */
public class TreeLabelVisitor {


    public TreeLabelVisitor(StringBuffer result, List<AnalysisAttributeBranch> analysisAttributeBranches, String attributeName) {
        this.label = result;
        this.analysisAttributeBranches = analysisAttributeBranches;
        this.attributeName = attributeName;
    }

    public void visit(AnalysisAttributeBranch container) {
        // from the root
        label.append(container.getLabel());
    }

    public boolean visitEnter(AnalysisAttributeBranch container) {        
        return true;
    }

    public boolean visitLeave(AnalysisAttributeBranch container) {
        if(container.isRoot()) return false;
        return true;
    }

    public String getLabel() {
        return label.toString();
    }

    private StringBuffer label;
    private List<AnalysisAttributeBranch> analysisAttributeBranches;
    private String attributeName;
}
