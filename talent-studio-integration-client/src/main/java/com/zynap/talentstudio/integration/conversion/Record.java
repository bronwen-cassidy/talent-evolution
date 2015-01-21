package com.zynap.talentstudio.integration.conversion;

import org.w3c.dom.*;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class Record implements ISortNode {

    public Record(CSV doc, ArrayList columnHeaders, String objectType, Document document, String action) throws Exception {

        this.objectType = objectType;
        element = document.createElement("command");
        element.setAttribute("action", action);
        object = document.createElement(objectType);
        element.appendChild(object);
        cells = new ArrayList();

        for (int i = 0; i < columnHeaders.size(); i++) {
            String value = doc.get(i);

            try {
                if (StringUtils.hasText(value)) {
                    Cell cell = createCell((String) columnHeaders.get(i), value);
                    String colReference = (String) columnHeaders.get(i);
                    if (colReference.equals("id")) id = cell.getValue();
                    else if (colReference.equals(POSITION_PARENT_ID)||colReference.equals(ORGUNIT_PARENT_ID)) parentId = cell.getValue();
                    addCell(colReference, cell);
                }
            }
            catch (Exception e) {
                System.err.println("Exception in Record. Cell " + i + "(" + columnHeaders.get(i) + ") = " + value);
                throw e;
            }
        }
    }

    private void addCell(String key, Cell cell) {

        String[] tags = StringUtils.delimitedListToStringArray(key, ".");
        Element parent = object;

        final Document ownerDocument = parent.getOwnerDocument();
        for (int i = 0; i < tags.length - 1; i++) {
            String tag = tags[i];
            Element temp = getElementByTagName(parent, tag);

            if (temp == null) {
                temp = ownerDocument.createElement(tag);
                parent.appendChild(temp);
            }
            parent = temp;
        }

        String lastTag = tags[tags.length - 1];
        Element lastElement;
        final String value = cell.getValue();

        if (lastTag.indexOf('@') == -1) {
            lastElement = getElementByTagName(parent, lastTag);

            if (lastElement == null) {
                lastElement = ownerDocument.createElement(lastTag);
                parent.appendChild(lastElement);
            }
            // todo create cdata tags for all text nodes
            CDATASection cdataElement = ownerDocument.createCDATASection(value);
            lastElement.appendChild(cdataElement);

            //lastElement.appendChild(ownerDocument.createTextNode(value));

        } else {
            String[] splitAttribute = StringUtils.delimitedListToStringArray(lastTag, "@");
            lastElement = getElementByTagName(parent, splitAttribute[0]);

            if (lastElement == null) {
                lastElement = ownerDocument.createElement(splitAttribute[0]);
                parent.appendChild(lastElement);
            }
            lastElement.setAttribute(splitAttribute[1], value);
        }
    }

    private Element getElementByTagName(Element parent, String tag) {
        NodeList list = parent.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element && node.getNodeName().equals(tag))
                return (Element) node;
        }
        return null;
    }

    public List getCells() {
        return cells;
    }

    public String getObjectType() {
        return objectType;
    }

    private Cell createCell(String key, String value) {
        if (key.indexOf('.') == -1) {
            return new Cell(key, value);
        } else {
            String[] keys = StringUtils.delimitedListToStringArray(key, ".");
            //String suf = keys[keys.length - 1];
            String suf = keys[0];
            return new Cell(suf, value);
        }
    }

    public Element getElement() {
        return element;
    }


    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    private List cells;
    private String objectType;
    private Element element;
    private Element object;
    private String id = null;
    private String parentId = null;

    private final String POSITION_PARENT_ID = "sourceAssociations.ptpAssociation.target.id";
    private final String ORGUNIT_PARENT_ID = "parent.id";

}
