package com.zynap.talentstudio.questionnaires.support;

import java.io.Serializable;
import java.util.Collection;

/**
* Class or Interface description.
*
* @author bcassidy
* @since 30-Sep-2005 11:09:36
* @version 0.1
*/
public interface ITreeElement<T> extends Serializable {

       String getLabel();

       Collection<T> getChildren();

       String getElementId();

}
