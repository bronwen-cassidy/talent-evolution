/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.web.tag.support.tree;

import com.zynap.talentstudio.web.utils.tree.ITreeContainer;
import com.zynap.talentstudio.web.utils.tree.IVisitor;


/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class ChildVisitor implements IVisitor {

    public ChildVisitor(String selectedOrganisationUnitId) {
        _selectedOrganisationUnitId = selectedOrganisationUnitId;
    }

    public void visit(ITreeContainer container) {
        if (container.getId().equals(_selectedOrganisationUnitId)) {
            _isChild = true;
        }
    }

    public boolean isChild() {
        return _isChild;
    }

    public boolean visitEnter(ITreeContainer container) {

        // if the id does not match do not enter
        return !(container.getId().equals(_selectedOrganisationUnitId));
    }

    public void visitLeave(ITreeContainer container) {
    }

    private boolean _isChild = false;
    private String _selectedOrganisationUnitId;
}
