/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;

import java.util.List;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Leaf implements ITreeContainer {

    public Leaf() {
    }

    public Leaf(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ITreeContainer getParent() {
        return parent;
    }

    public void display(TreeDisplayVisitor displayer, StringBuffer buffer) {
    }

    public void accept(IVisitor tempVisitor) {
    }

    public List<ITreeContainer> getChildren() {
        return null;
    }

    public void setParent(ITreeContainer parent) {
        this.parent = parent;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isSeparator() {
        return separator;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leaf)) return false;

        Leaf leaf = (Leaf) o;

        if (id != null ? !id.equals(leaf.id) : leaf.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public String toString() {
        return id;
    }

    public void buildLabel(StringBuffer buffer, String separator) {
        
    }

    private String id;
    private String parentId;
    private boolean active;
    private ITreeContainer parent;
    private boolean hasAccess;
    private String label;
    private boolean separator;
}