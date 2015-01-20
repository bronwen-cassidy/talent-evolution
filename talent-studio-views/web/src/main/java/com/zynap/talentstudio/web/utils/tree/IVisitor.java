/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils.tree;




/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface IVisitor {

    void visit(ITreeContainer container);

    boolean visitEnter(ITreeContainer container);

    void visitLeave(ITreeContainer container);
}