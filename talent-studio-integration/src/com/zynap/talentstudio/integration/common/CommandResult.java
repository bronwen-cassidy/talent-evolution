package com.zynap.talentstudio.integration.common;

import com.zynap.talentstudio.integration.dto.dom.ZynapDataTransferObject;


/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 21-Oct-2005
 * Time: 13:27:44
 * To change this template use File | Settings | File Templates.
 */
public class CommandResult implements IZynapCommandResult {

    private final IZynapCommand zynapCommand;

    private final IZynapDataTransferObject zynapDataTransferObject;

    public CommandResult(IZynapCommand zynapCommand, IZynapDataTransferObject dto) {
        this.zynapCommand = zynapCommand;
        this.zynapDataTransferObject = dto;
        ((ZynapDataTransferObject) dto).getElement().setAttribute(IntegrationConstants.ACTION_ATTRIBUTE, zynapCommand.getAction());
    }

    public String getAction() {
        return zynapCommand.getAction();
    }

    public IZynapDataTransferObject getResult() {
        return zynapDataTransferObject;
    }
}
