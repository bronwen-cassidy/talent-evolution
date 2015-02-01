/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Branch extends Leaf implements ITreeContainer {

    public Branch() {
        super();
        children = new ArrayList<ITreeContainer>();
        leaves = new ArrayList<ITreeContainer>();
    }

    public Branch(String id, String label) {
        super(id, label);
        children = new ArrayList<ITreeContainer>();
        leaves = new ArrayList<ITreeContainer>();
    }


    public void display(TreeDisplayVisitor displayer, StringBuffer buffer) {
        displayer.display(this, buffer);
    }

    public void buildLabel(StringBuffer buffer, String separator) {
        ITreeContainer parent = getParent();
        if (parent != null) {
            String bufferLabel = getLabel() + separator;
            buffer.insert(0, bufferLabel);
            parent.buildLabel(buffer, separator);
        } else {
            buffer.insert(0, separator);
        }
    }

    /**
     * Accepts a visitor in order to walk it's hierarchical tree.
     *
     * @param visitor
     */
    public boolean build(ITreeBuilder visitor) {
        if (visitor.build(this)) return true;
        for (Iterator iterator = getChildren().iterator(); iterator.hasNext();) {
            Branch childContainer = (Branch) iterator.next();
            if (childContainer.build(visitor)) return true;
        }
        return false;
    }

    public void accept(IVisitor visitor) {
        visitor.visit(this);
        if (visitor.visitEnter(this)) {
            for (ITreeContainer childContainer : getChildren()) {
                childContainer.accept(visitor);
            }
        }
        visitor.visitLeave(this);
    }


    public List<ITreeContainer> getChildren() {
        return children;
    }

    public void setChildren(List<ITreeContainer> children) {
        this.children = children;
    }

    public List<ITreeContainer> getLeaves() {
        return leaves;
    }

    public void setLeaves(List<ITreeContainer> leaves) {
        this.leaves = leaves;
    }

    /**
     * Does branch have contents (other branches or leaves.)
     *
     * @return true if branch has at least one child branch or one child leaf.
     */
    public boolean isHasChildren() {
        return hasLeaves() || (children != null && children.size() > 0);
    }

    /**
     * Does branch have leaves.
     *
     * @return true or false
     */
    public boolean hasLeaves() {
        return (leaves != null && leaves.size() > 0);
    }

    public void addChild(Branch node) {
        node.setParent(this);
        children.remove(node);
        children.add(node);
    }

    public void addLeaf(Leaf nodeLeaf) {
        nodeLeaf.setParent(this);
        leaves.add(nodeLeaf);
    }

    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Branch)) return false;

        Branch leaf = (Branch) o;

        if (getId() != null ? !getId().equals(leaf.getId()) : leaf.getId() != null) return false;

        return true;
    }

    public ITreeContainer find(String branchId) {
        if(getId().equals(branchId)) return this;
        for (ITreeContainer child : children) {
            Branch b = (Branch)child;
            ITreeContainer treeContainer = b.find(branchId);
            if(treeContainer != null) return treeContainer;
        }
        return null;
    }

    public ITreeContainer findLeaf(String id) {
        if (hasLeaves()) {
            for (ITreeContainer leaf : leaves) {
                if (id.equals(leaf.getId())) {
                    return leaf;
                }
            }
        }
        // not found in my leaves going to try the children
        ITreeContainer result = null;
        for (ITreeContainer container : children) {
            Branch branch = (Branch) container;
            result = branch.findLeaf(id);
            if (result != null) break;
        }
        return result;
    }

    private List<ITreeContainer> children;
    private List<ITreeContainer> leaves;
}