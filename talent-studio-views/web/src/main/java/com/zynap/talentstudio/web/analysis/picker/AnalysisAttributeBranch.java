package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.web.utils.tree.Branch;
import com.zynap.talentstudio.web.utils.tree.ITreeContainer;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;

import java.util.Iterator;
import java.util.List;

/**
 * User: amark
 * Date: 01-Feb-2006
 * Time: 10:04:57
 */
public final class AnalysisAttributeBranch extends Branch implements Cloneable {

    public AnalysisAttributeBranch(String id, String label, String prefix) {
        super(id, label);
        this.prefix = prefix;
    }

    public AnalysisAttributeBranch(String id, String label, String type, boolean root, boolean includeQualifierAttribute, String prefix) {
        this(id, label, prefix);
        this.type = type;
        this.root = root;
        this.includeQualifierAttributes = includeQualifierAttribute;
    }

    /**
     * Get the prefix to add to attributes.
     *
     * @return the id or empty string if this is a root branch.
     */
    public String getPrefix() {
        return root ? "" : prefix;
    }

    public boolean isRoot() {
        return root;
    }

    public String getType() {
        return type;
    }

    public boolean includeQualifierAttributes() {
        return includeQualifierAttributes;
    }

    public boolean isSubject() {
        return Node.SUBJECT_UNIT_TYPE_.equals(type);
    }

    public boolean isPosition() {
        return Node.POSITION_UNIT_TYPE_.equals(type);
    }

    protected Object clone() {
        AnalysisAttributeBranch newBranch = new AnalysisAttributeBranch(getId(), getLabel(), getType(), root, includeQualifierAttributes, prefix);

        final List children = getChildren();
        for (Iterator iterator = children.iterator(); iterator.hasNext();) {
            AnalysisAttributeBranch childBranch = (AnalysisAttributeBranch) iterator.next();
            newBranch.addChild(((AnalysisAttributeBranch) childBranch.clone()));
        }

        return newBranch;
    }

    public void removeChild(String branchId) {
        for (Iterator iterator = getChildren().iterator(); iterator.hasNext();) {
            Branch branch = (Branch) iterator.next();
            if (branch.getId().equals(branchId)) iterator.remove();
        }
    }

    public boolean isQuestionnaireWorkflow() {
        return Node.QUESTIONNAIRE_TYPE.equals(type);
    }

    public boolean isSubjectAssociation() {
        return IPopulationEngine.S_P_ASSOC_TYPE_.equals(type);
    }

    public boolean hasBranch(String branchId) {
        final List<ITreeContainer> branches = getChildren();
        for (ITreeContainer branch : branches) {
            if (branchId.equals(branch.getId())) return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisAttributeBranch)) return false;

        AnalysisAttributeBranch leaf = (AnalysisAttributeBranch) o;

        if (getId() != null ? !getId().equals(leaf.getId()) : leaf.getId() != null) return false;

        return true;
    }

    public boolean isDocumentBranch() {
        return IPopulationEngine.P_ITEM_TYPE_.equals(type);
    }

    public boolean isOrganisationUnitBranch() {
        return IPopulationEngine.O_UNIT_TYPE_.equals(type);
    }

    public AnalysisAttributeBranch findBranch(String branchId, String leafId) {
        String tempPrefix = getPrefix();

//        if (AnalysisAttributeHelper.isQualifierAttribute(leafId)) {
//            // remove last clause from prefix eg: convert "subjectPrimaryAssociations.position" to "subjectPrimaryAssociations"
//            final int pos = tempPrefix.lastIndexOf(AnalysisAttributeHelper.DELIMITER);
//            if (pos > 0) {
//                tempPrefix = tempPrefix.substring(0, pos);
//            }
//        }
        
        if (tempPrefix.equals(branchId)) return this;

        for (ITreeContainer container : getChildren()) {
            AnalysisAttributeBranch b = (AnalysisAttributeBranch) container;
            AnalysisAttributeBranch branch = b.findBranch(branchId, leafId);
            if (branch != null) return branch;
        }
        
        return null;
    }

    private boolean includeQualifierAttributes = false;
    private boolean root = false;
    private String type;
    // a very important field that is used to prefix the dyanmicAttribute id in the ref_value column of the report_columns table
    // it is an indicator of the path to the questionnaires, either top level persons questionnaire or the positions, current holders questionnaire etc.
    private String prefix;
}
