package com.zynap.talentstudio.integration.dto.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import com.zynap.talentstudio.integration.common.IZynapDataTransferObject;
import com.zynap.talentstudio.integration.common.IntegrationConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 10-Oct-2005
 * Time: 15:59:34
 * To change this template use File | Settings | File Templates.
 */
public class ZynapDataTransferObject implements IZynapDataTransferObject {

    public ZynapDataTransferObject(Element element) {
        xmlElement = element;
    }

    public IZynapDataTransferObject getProperty(String name) {
        return null;
    }

    public void setProperty(String name, IZynapDataTransferObject value) {
    }

    public Collection getProperties() {
        return null;
    }


    public String getName() {
        return xmlElement.getNodeName();
    }

    public Collection getPropertiesByName(String s) {
        NodeList list = xmlElement.getElementsByTagName(s);
        final Collection idNodes = new ArrayList();
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            idNodes.add(new ZynapDataTransferObject(element));
        }

        return idNodes;
    }

    public void setValue(Serializable value) {
        xmlElement.getFirstChild().setNodeValue(value.toString());
    }

    public Serializable getValue() {
        return xmlElement.getFirstChild().getNodeValue();
    }

    public String getExternalId() {
        return getAttributeByName(IntegrationConstants.ID_ATTRIBUTE);
    }

    public String getUpdatedExternalId() {
        return getAttributeByName(IntegrationConstants.UPDATED_ID_ATTRIBUTE);
    }

    public Element getElement() {
        return xmlElement;
    }

    private String getAttributeByName(String name) {
        NodeList children = xmlElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Element element = (Element) children.item(i);
            if (name.equalsIgnoreCase(element.getNodeName())) {
                return element.getFirstChild().getNodeValue();
            }
        }

        return null;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("ZynapDataTransferObject[");
        stringBuffer.append("\r\n xmlElement=").append(xmlElement);
        stringBuffer.append("\r\n external id=").append(getExternalId());

        final NodeList childNodes = xmlElement.getChildNodes();
        printChildren(stringBuffer, childNodes);

        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    private void printChildren(StringBuffer stringBuffer, NodeList nodes) {
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                stringBuffer.append("\r\n node=").append(node.getNodeName()).append(" : ").append(node.getNodeValue());
                final NodeList childNodes = node.getChildNodes();
                printChildren(stringBuffer, childNodes);
            }
        }
    }


    private final Element xmlElement;
}
