package com.zynap.talentstudio.integration.common;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 14:52:36
 * To change this template use File | Settings | File Templates.
 */
public interface IZynapCommand {

    String getAction();

    String CREATE_ACTION = "create";
    String UPDATE_ACTION = "update";
    String DELETE_ACTION = "delete";
    String FIND_ACTION = "find";
    String EXISTS_ACTION = "exists";
    String FIND_ALL_ACTION = "findAll";
    String SET_EXTERNAL_ID_ACTION = "setExternalId" ;

    IZynapDataTransferObject getInput();

    byte[][] getAttachments();



}
