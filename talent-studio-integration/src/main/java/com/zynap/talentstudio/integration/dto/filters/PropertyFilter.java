/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.dto.filters;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zynap.common.util.XmlUtils;
import com.zynap.exception.TalentStudioException;

import org.apache.xpath.XPathAPI;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class PropertyFilter implements IPropertyFilter {

    public PropertyDescriptor[] filter(PropertyDescriptor[] propertyDescriptors, String artefactName) throws TalentStudioException {
        List propertyDescriptorList = new ArrayList();
        try {
            Document document = XmlUtils.createDocument(DEFAULT_INTEGRATION_SCHEMA_PATH);
            Element firstElement = (Element) document.getFirstChild();
            final NodeList nodeList = XPathAPI.selectNodeList(firstElement, "./element[@name=\"" + artefactName + "\"]");
            if (nodeList.getLength() > 0) {
                Element artefactElement = (Element) nodeList.item(0);

                for (int i = 0; i < propertyDescriptors.length; i++) {
                    PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                    String propertyName = propertyDescriptor.getName();

                    if(retainedElements.contains(propertyName)) {
                        propertyDescriptorList.add(propertyDescriptor);
                        continue;
                    }

                    int numElements = XPathAPI.selectNodeList(artefactElement, "*//element[@name=\"" + propertyName + "\" or @ref=\"" + propertyName + "\" ]").getLength();
                    if (numElements > 0) {
                        propertyDescriptorList.add(propertyDescriptor);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new TalentStudioException(e);
        } catch (SAXException e) {
            throw new TalentStudioException(e);
        } catch (IOException e) {
            throw new TalentStudioException(e);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return (PropertyDescriptor[]) propertyDescriptorList.toArray(new PropertyDescriptor[propertyDescriptorList.size()]);
    }

    public void setRetainedElements(List retainedElements) {
        this.retainedElements = retainedElements;
    }

    private List retainedElements;
}
