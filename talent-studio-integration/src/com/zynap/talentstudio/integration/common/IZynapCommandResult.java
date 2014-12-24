package com.zynap.talentstudio.integration.common;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 14:52:19
 * To change this template use File | Settings | File Templates.
 */
public interface IZynapCommandResult extends Serializable {

    String getAction();

    IZynapDataTransferObject getResult();

}
