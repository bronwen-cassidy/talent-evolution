package com.zynap.talentstudio.web.utils.tree;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 11-Aug-2005
 * Time: 09:38:08
 * To change this template use File | Settings | File Templates.
 */
public interface ITreeContainer {


    String getLabel();

    boolean isHasAccess();

    boolean isActive();

    String getId();

    ITreeContainer getParent();

    void display(TreeDisplayVisitor displayer,StringBuffer buffer);

    void buildLabel(StringBuffer buffer, String separator);

    void accept(IVisitor tempVisitor);

    List<ITreeContainer> getChildren();
}
