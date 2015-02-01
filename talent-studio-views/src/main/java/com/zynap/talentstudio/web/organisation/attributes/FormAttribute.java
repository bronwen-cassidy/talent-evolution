/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.attributes;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface FormAttribute extends Serializable {

    String getValue();

    String getLabel();

    String getId();

    boolean isEditable();

    boolean isLineItem();

    boolean isHidden();
}
