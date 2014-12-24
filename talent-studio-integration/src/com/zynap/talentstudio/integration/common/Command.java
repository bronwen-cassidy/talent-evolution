/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.common;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.zynap.talentstudio.integration.adapter.InvalidDataException;
import com.zynap.talentstudio.integration.adapter.NoDataForCommandException;
import com.zynap.talentstudio.integration.dto.dom.ZynapDataTransferObject;

import org.springframework.util.StringUtils;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Command implements IZynapCommand {


    public Command(Element commandNode, byte[][] attachments) throws InvalidDataException {

        final String actionAttribute = commandNode.getAttribute(IntegrationConstants.ACTION_ATTRIBUTE);
        if (!StringUtils.hasText(actionAttribute)) {
            throw new InvalidDataException("Command requires an action attribute");
        }

        this.action = actionAttribute;

        final NodeList childNodes = commandNode.getChildNodes();
        final int length = childNodes.getLength();

        if (length == 0) throw new NoDataForCommandException();
        else if (length > 1) throw new InvalidDataException("Command requires exactly one input element");

        final Node childNode = childNodes.item(0);
        this.input = new ZynapDataTransferObject((Element) childNode);

        this.attachments = attachments;
    }

    public String getAction() {
        return action;
    }

    public IZynapDataTransferObject getInput() {
        return input;
    }

    public byte[][] getAttachments() {
        return attachments;
    }

    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Command[");
        stringBuffer.append("\r\n action=" + action);
        stringBuffer.append("\r\n input=" + input);
        stringBuffer.append("\r\n attachments=" + (attachments == null ? null : "length:" + attachments.length));
        stringBuffer.append("]");

        return stringBuffer.toString();
    }


    private final String action;
    private final IZynapDataTransferObject input;
    private final byte[][] attachments;

}
