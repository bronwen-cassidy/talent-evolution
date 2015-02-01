package com.zynap.talentstudio.web.organisation.attributes;

import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.common.ControllerConstants;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* Class or Interface description.
*
* @author bcassidy
* @since 07-Feb-2005 15:45:10
* @version 0.1
*/
public final class DynamicAttributesHelper {

    /**
     * Get the dynamic attributes for the specified arena and node.
     * <br> Will return an empty list if there are no dynamic attributes available for the arena.
     *
     * @param nodeType The node type
     * @param dynamicAttributeService The IDynamicAttributeService to use to get the dynamic attributes
     * @return ArrayList of {@link AttributeWrapperBean}s
     */
    public static Collection getSearchableExtendedAttributes(String nodeType, IDynamicAttributeService dynamicAttributeService) {
        return dynamicAttributeService.getSearchableAttributeDtos(nodeType);
    }

    /**
     * Add the edited / added AttributeWrapperBeans to the node.
     * <br> Also remove the cleared (ie: with no value) attribute values from the Node.
     *
     * @param attributeWrapperBeans collection of wrapped attributes
     * @param node The node
     */
    public static void assignAttributeValuesToNode(Collection<FormAttribute> attributeWrapperBeans, Node node) {

        if (!attributeWrapperBeans.isEmpty() && node != null) {
            for (FormAttribute formAttribute : attributeWrapperBeans) {
                if (formAttribute.isEditable()) {
                    final AttributeWrapperBean attributeWrapperBean = (AttributeWrapperBean) formAttribute;
                    final AttributeValue newAttributeValue = attributeWrapperBean.getModifiedAttributeValue();
                    node.addOrUpdateAttributeValue(newAttributeValue);
                }
            }
        }
    }

    /**
     * Check if request is a request to clear an image extended attribute.
     *
     * @param request The request
     * @param wrapperBean The NodeWrapperBean
     * @return true if the request contains parameters starting with {@link ControllerConstants#DELETE_IMAGE_INDEX} and the wrapperBean delete image index is set.
     */
    public static boolean isClearAttributeValueRequest(HttpServletRequest request, NodeWrapperBean wrapperBean) {
        return !WebUtils.getParametersStartingWith(request, ControllerConstants.DELETE_IMAGE_INDEX).isEmpty() && wrapperBean.getDeleteImageIndex() != null;
    }

    /**
     * Clear the value of the AttributeWrapperBean in the list of wrappeddynamicattributes specified by the nodeWrapperBean delete image index.
     *
     * @param nodeWrapperBean The NodeWrapperBean
     */
    public static void clearAttributeValue(final NodeWrapperBean nodeWrapperBean) {
        final List wrappedDynamicAttributes = nodeWrapperBean.getWrappedDynamicAttributes();
        final Long index = nodeWrapperBean.getDeleteImageIndex();
        clearAttributeValue(index, wrappedDynamicAttributes);
    }

    /**
     * Clear the value of the AttributeWrapperBean in the list at the specified index.
     *
     * @param index of the attribute within the collection
     * @param wrappedDynamicAttributes The List of AttributeWrapperBeans
     */
    public static void clearAttributeValue(final Long index, final List wrappedDynamicAttributes) {
        if (index != null && index != -1) {
            AttributeWrapperBean attributeWrapperBean = (AttributeWrapperBean) wrappedDynamicAttributes.get(index.intValue());
            attributeWrapperBean.clearValue();
        }
    }

    /**
     * Create a list of {@link AttributeWrapperBean} objects - one per dynamic attribute in the collection passed in.
     * <br/> if removeEmptyStruct is true will remove the struct dynamic attributes that have no active lookup values.
     *
     * @param attributes A collection of DynamicAttribute objects.
     * @param removeEmptyStruct
     * @return List
     */
    public static List<FormAttribute> createAttributeWrappers(Collection attributes, boolean removeEmptyStruct) {

        List<FormAttribute> attributeWrappers = new ArrayList<FormAttribute>();

        for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
            DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
            if (removeEmptyStruct && dynamicAttribute.isSelectionType() && dynamicAttribute.getActiveLookupValues().isEmpty()) iterator.remove();
            AttributeWrapperBean wrapper = new AttributeWrapperBean(dynamicAttribute);
            attributeWrappers.add(wrapper);
        }

        return attributeWrappers;
    }

    /**
     * Assign AttributeWrapperBeans for displayconfig attributes to NodewrapperBean.
     *
     * @param nodeWrapperBean
     * @param configItem
     * @param node
     * @param dynamicAttributeService The IDynamicAttributeService
     */
    public static void assignDisplayConfigAttributes(NodeWrapperBean nodeWrapperBean, DisplayConfigItem configItem, Node node, IDynamicAttributeService dynamicAttributeService) {

        nodeWrapperBean.setDisplayConfigItem(configItem);

        final List<Column> reportColumns = configItem.getReportColumns();
        final List<Column> coreAttributes = new ArrayList<Column>();
        final Map<DynamicAttribute, Column> extendedAttributes = new LinkedHashMap<DynamicAttribute, Column>();

        splitCoreExtendedAttributes(reportColumns, coreAttributes, extendedAttributes, node, dynamicAttributeService);
        nodeWrapperBean.setCoreValues(coreAttributes);

        final Map<Object, FormAttribute> result = new LinkedHashMap<Object, FormAttribute>();

        if (!extendedAttributes.isEmpty()) {
            final Iterator it = extendedAttributes.entrySet().iterator();
            while (it.hasNext()) {

                final Map.Entry entry = (Map.Entry) it.next();
                final DynamicAttribute attributeDefinition = (DynamicAttribute) entry.getKey();
                final Column column = (Column) entry.getValue();

                final FormAttribute formAttribute = new AttributeWrapperBean(column.getLabel(), null, attributeDefinition);
                final Object key = attributeDefinition.getId();
                result.put(key, formAttribute);
            }

            mapExistingAttributeValues(node, result, dynamicAttributeService);
        }

        final List<FormAttribute> wrappedAttributes = new ArrayList<FormAttribute>(result.values());
        nodeWrapperBean.setWrappedDynamicAttributes(wrappedAttributes);
    }

    /**
     * Maps the attribute values of the node.
     * If an attributeValue for the given node exists it sets the attribute value on the relevant wrapper.
     *
     * @param node
     * @param result
     * @param daService The IDynamicAttributeService
     */
    public static void mapExistingAttributeValues(Node node, Map result, IDynamicAttributeService daService) {
        Iterator it = node.getDynamicAttributeValues().getValues().iterator();
        while (it.hasNext()) {
            AttributeValue attributeValue = (AttributeValue) it.next();
            final DynamicAttribute dynamicAttribute = attributeValue.getDynamicAttribute();
            AttributeWrapperBean attributeWrapperBean = (AttributeWrapperBean) result.get(dynamicAttribute.getId());
            if (attributeWrapperBean != null) {
                attributeWrapperBean.setAttributeValue(attributeValue);
                attributeWrapperBean.setNodeLabel(daService.getDomainObjectLabel(attributeValue));
            }
        }
    }

    /**
     * Splits the core and extended attributes.
     * NOTE: questionnaire attributes are not editable, therefore if they are in this list of attributes they are ignored.
     *
     * @param columns all attributes including core, dynamic, questionnaire and derived
     * @param coreAttributes is populated with all the core attributes found in the columns param
     * @param extendedAttributes is populated with extended attributes and only extended attributes found in the columns param
     * @param node The Node
     * @param daService The IDynamicAttributeService
     */
    private static void splitCoreExtendedAttributes(List<Column> columns, List<Column> coreAttributes, Map<DynamicAttribute, Column> extendedAttributes, Node node, IDynamicAttributeService daService) {
        Collection<DynamicAttribute> dynamicAttributes = daService.getAllActiveAttributes(node.getNodeType(), true);
        for (int i = 0; i < columns.size(); i++) {
            Column column = columns.get(i);
            AnalysisParameter attribute = column.getAnalysisParameter();
            if (AnalysisAttributeHelper.isCoreAttribute(attribute.getName())) {
                coreAttributes.add(column);
            }
            if (attribute.isDynamicAttribute() && !attribute.isQuestionnaireAttribute()) {
                for (Iterator iterator = dynamicAttributes.iterator(); iterator.hasNext();) {
                    DynamicAttribute dynamicAttribute = (DynamicAttribute) iterator.next();
                    if (!dynamicAttribute.isCalculated() && dynamicAttribute.getId().equals(new Long(column.getAttributeName()))) {
                        extendedAttributes.put(dynamicAttribute, column);
                    }
                }
            }
        }
    }

    public static List<FormAttribute> getAttributeWrapperBeans(Collection<DynamicAttribute> dynamicAttributes, Node node, IDynamicAttributeService dynamicAttributeService) {

        final Map<Long, FormAttribute> result = new LinkedHashMap<Long, FormAttribute>();

        if (!dynamicAttributes.isEmpty()) {
            final Iterator it = dynamicAttributes.iterator();
            while (it.hasNext()) {
                final DynamicAttribute attributeDefinition = (DynamicAttribute) it.next();
                final AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean(node, attributeDefinition);
                final Long key = attributeDefinition.getId();

                result.put(key, attributeWrapperBean);
            }

            mapExistingAttributeValues(node, result, dynamicAttributeService);
        }

        return new ArrayList<FormAttribute>(result.values());
    }

    public static List<FormAttribute> getDisplayableAttributeWrapperBeans(Node node, IDynamicAttributeService dynamicAttributeService) {
        Collection<DynamicAttribute> dynamicAttributes = dynamicAttributeService.getAllActiveAttributes(node.getNodeType(), true);
        return getAttributeWrapperBeans(dynamicAttributes, node, dynamicAttributeService);
    }

    public static List<FormAttribute> getEditableAttributeWrapperBeans(Node node, IDynamicAttributeService dynamicAttributeService) {
        Collection<DynamicAttribute> dynamicAttributes = dynamicAttributeService.getAllActiveAttributes(node.getNodeType(), false);
        return getAttributeWrapperBeans(dynamicAttributes, node, dynamicAttributeService);
    }

    public static List<FormAttribute> wrapExtendedAttributes(Node node) {
        List<FormAttribute> result = new ArrayList<FormAttribute>();
        AttributeValuesCollection valuesCollection = node.getDynamicAttributeValues();
        Collection values = valuesCollection.getValues();
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            AttributeValue attributeValue = (AttributeValue) iterator.next();
            // lazy init
            attributeValue.getDynamicAttribute().getActiveLookupValues().size();
            result.add(new AttributeWrapperBean(attributeValue));
        }
        return result;
    }
}
