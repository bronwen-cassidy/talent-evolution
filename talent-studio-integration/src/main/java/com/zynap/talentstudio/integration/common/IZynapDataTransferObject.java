package com.zynap.talentstudio.integration.common;

import java.util.Collection;
import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 14:52:53
 * To change this template use File | Settings | File Templates.
 */
public interface IZynapDataTransferObject {

    IZynapDataTransferObject getProperty(String name);

    void setProperty(String name, IZynapDataTransferObject value);

    Collection getProperties();

    String getName();

    Collection getPropertiesByName(String s);

    void setValue(Serializable value);

    Serializable getValue();

    String getExternalId();

    String getUpdatedExternalId();
}
