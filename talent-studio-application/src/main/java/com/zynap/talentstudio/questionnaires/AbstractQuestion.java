/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.questionnaires.support.ITreeElement;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Jan-2007 09:22:16
 */
public abstract class AbstractQuestion extends ZynapDomainObject implements ITreeElement {

    private static final long serialVersionUID = 2394793755228098705L;

    public abstract boolean isMultiQuestion();

    public abstract boolean isNarrativeType();

    public abstract void refresh();
}
