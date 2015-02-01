/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.analysis.picker;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.zynap.common.util.XmlUtils;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Jan-2008 14:07:55
 */
public class XMLParser {

    XMLParser() {
    }

    Map<String, List<AnalysisAttributeBranch>> buildTree(Document document, AttributeSet qualifierAttributeSet, List<AttributeSet> coreAttributeSets) {

        processTreeElements(document);
        processAttributeElements(document, coreAttributeSets);
        processExtendedAttributeElement(document);
        processDerivedAttributeElement(document);
        processDynamicLineItemsElement(document);
        processQualifierAttributeElements(document, qualifierAttributeSet);
        return trees;
    }

    public boolean isUseSearchableExtendedAttributesOnly() {
        return useSearchableExtendedAttributesOnly;
    }

    public boolean isIncludeDerivedAttributes() {
        return includeDerivedAttributes;
    }

    public boolean isIncludeDynamicLineItemAttributes() {
        return includeDynamicLineItemAttributes;
    }

    public String[] getExtendedAttributeTypes() {
        return extendedAttributeTypes;
    }

    private void processDynamicLineItemsElement(Document document) {
        final Element element = XmlUtils.getFirstElement(document, DYNAMIC_LINEITEMS_TAG);
        if (element != null) {
            includeDynamicLineItemAttributes = XmlUtils.getBooleanValue(element, INCLUDE_ATTR);
        }
    }

    private void processQualifierAttributeElements(Document document, AttributeSet qualifierAttributeSet) {
        final NodeList nodeList = XmlUtils.getNodes(document, QUALIFIER_ATTRIBUTES_TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {

            final Element element = (Element) nodeList.item(i);
            final NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {

                final Element attributeElement = (Element) childNodes.item(j);
                qualifierAttributeSet.addAttribute(buildAttributeWrapperBean(attributeElement));
            }
        }
    }

    private void processExtendedAttributeElement(Document document) {
        final Element element = XmlUtils.getFirstElement(document, EXTENDED_ATTRIBUTES_TAG);
        if (element != null) {
            useSearchableExtendedAttributesOnly = XmlUtils.getBooleanValue(element, SEARCHABLE_ATTR);
            extendedAttributeTypes = StringUtils.commaDelimitedListToStringArray(element.getAttribute(TYPE_ATTR));
        }
    }

    private void processDerivedAttributeElement(Document document) {
        final Element element = XmlUtils.getFirstElement(document, DERIVED_ATTRIBUTES_TAG);
        if (element != null) {
            includeDerivedAttributes = XmlUtils.getBooleanValue(element, INCLUDE_ATTR);
        }
    }

    private void processAttributeElements(Document document, List<AttributeSet> coreAttributeSets) {
        final NodeList nodeList = XmlUtils.getNodes(document, ATTRIBUTES_TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {

            final Element element = (Element) nodeList.item(i);
            final String type = element.getAttribute(TYPE_ATTR);
            final String viewType = element.getAttribute(VIEW_TYPE_ATTR);
            final String includeQuestionnairesAttribute = XmlUtils.getAttributeValue(element, INCLUDE_QUESTIONNAIRES_ATTR);
            final String includeDynamicAttribute = XmlUtils.getAttributeValue(element, INCLUDE_DYNAMIC_ATTR);
            final String includeCalculatedFields = XmlUtils.getAttributeValue(element, INCLUDE_CALCULATED_ATTRS);

            boolean includeQuestionnaires = (includeQuestionnairesAttribute == null || Boolean.valueOf(includeQuestionnairesAttribute).booleanValue());
            final boolean includeCalcFields = (includeCalculatedFields == null || Boolean.valueOf(includeCalculatedFields).booleanValue());
            final boolean includeDaFields = (includeDynamicAttribute == null || Boolean.valueOf(includeDynamicAttribute).booleanValue());

            final AttributeSet attributeSet = new AttributeSet(type, viewType, includeQuestionnaires, includeCalcFields, includeDaFields);

            // add entry for each set of attributes to map
            coreAttributeSets.add(attributeSet);

            final NodeList attributeElements = element.getChildNodes();
            for (int j = 0; j < attributeElements.getLength(); j++) {

                final Element attributeElement = (Element) attributeElements.item(j);
                attributeSet.addAttribute(buildAttributeWrapperBean(attributeElement));
            }
        }
    }

    private AttributeWrapperBean buildAttributeWrapperBean(Element attributeElement) {
        final String name = attributeElement.getAttribute(LABEL_ATTR);
        final String id = attributeElement.getAttribute(ID_ATTR);
        final String type = attributeElement.getAttribute(TYPE_ATTR);

        DynamicAttribute dynamicAttribute = new DynamicAttribute(name, type);
        return new AttributeWrapperBean(name, id, dynamicAttribute);
    }

    private void processTreeElements(final Document document) {
        final NodeList nodeList = XmlUtils.getNodes(document, TREE_TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {

            final Element treeElement = (Element) nodeList.item(i);
            final String type = treeElement.getAttribute(TYPE_ATTR);

            // add entry for each tree to map
            final List<AnalysisAttributeBranch> results = new ArrayList<AnalysisAttributeBranch>();
            trees.put(type, results);

            // get top-level branches for each tree
            final NodeList childList = treeElement.getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {

                final Element branchElement = (Element) childList.item(j);

                // add branch to results and to Map
                final AnalysisAttributeBranch branch = buildBranch(branchElement);
                results.add(branch);

                // recurse
                addChildren(branchElement, branch);
            }
        }
    }

    private void addChildren(Element currentElement, AnalysisAttributeBranch currentBranch) {

        final NodeList childNodes = currentElement.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {

            final Element childElement = (Element) childNodes.item(i);

            // add branch to results and to Map
            AnalysisAttributeBranch childBranch = buildBranch(childElement);
            currentBranch.addChild(childBranch);

            // recurse
            addChildren(childElement, childBranch);
        }
    }

    private AnalysisAttributeBranch buildBranch(final Element childElement) {
        final String label = childElement.getAttribute(LABEL_ATTR);
        final String id = childElement.getAttribute(ID_ATTR);
        final String type = childElement.getAttribute(TYPE_ATTR);

        final boolean root = XmlUtils.getBooleanValue(childElement, ROOT_ATTR);
        final boolean includeQualifierAttribute = XmlUtils.getBooleanValue(childElement, INCLUDE_QUALIFIER_ATTR);
        return new AnalysisAttributeBranch(id, label, type, root, includeQualifierAttribute, id);
    }

    /**
     * Holds branches.
     */
    private final Map<String, List<AnalysisAttributeBranch>> trees = new HashMap<String, List<AnalysisAttributeBranch>>();

    private static final String TREE_TAG = "tree";

    private static final String ATTRIBUTES_TAG = "attributes";

    private static final String DYNAMIC_LINEITEMS_TAG = "dynamiclineitems";
    private static final String DERIVED_ATTRIBUTES_TAG = "derivedattributes";
    private static final String EXTENDED_ATTRIBUTES_TAG = "extendedattributes";
    private static final String QUALIFIER_ATTRIBUTES_TAG = "qualifierattributes";

    private static final String ID_ATTR = "id";
    private static final String TYPE_ATTR = "type";
    private static final String ROOT_ATTR = "root";
    private static final String LABEL_ATTR = "label";
    private static final String INCLUDE_ATTR = "include";
    private static final String VIEW_TYPE_ATTR = "viewType";
    private static final String SEARCHABLE_ATTR = "searchableOnly";
    private static final String INCLUDE_QUALIFIER_ATTR = "includeQualifierAttributes";
    private static final String INCLUDE_QUESTIONNAIRES_ATTR = "includeQuestionnaires";
    private static final String INCLUDE_CALCULATED_ATTRS = "includeCalculatedFields";
    private static final String INCLUDE_DYNAMIC_ATTR = "includeDynamicAttributes";

    boolean useSearchableExtendedAttributesOnly = false;
    boolean includeDerivedAttributes = false;
    boolean includeDynamicLineItemAttributes = false;
    String[] extendedAttributeTypes;
}
