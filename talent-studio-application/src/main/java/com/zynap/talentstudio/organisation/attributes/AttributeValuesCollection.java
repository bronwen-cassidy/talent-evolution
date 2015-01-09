package com.zynap.talentstudio.organisation.attributes;

import com.zynap.talentstudio.organisation.Node;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: jsueiras
 * Date: 25-Nov-2005
 * Time: 17:03:33
 *
 * Collection that holds AttributeValues.
 */
public final class AttributeValuesCollection {

    /**
     * Constructor.
     * <br/> Takes extended attributes from Node and builds up a Map of AttributeValues keyed on DynamicAttribute.
     *
     * @param node
     */
    public AttributeValuesCollection(Node node) {
        this.values = new HashMap<DynamicAttribute, AttributeValue>();

        final Set<NodeExtendedAttribute> extendedAttributes = node.getExtendedAttributes();
        for (NodeExtendedAttribute nodeExtendedAttribute : extendedAttributes) {
            final DynamicAttribute dynamicAttribute = nodeExtendedAttribute.getDynamicAttribute();
            AttributeValue attValue = get(dynamicAttribute);
            if (attValue != null)
                attValue.addValue(nodeExtendedAttribute, true);
            else {
                attValue = AttributeValue.create(nodeExtendedAttribute);
                values.put(dynamicAttribute, attValue);
            }
        }
    }

    public AttributeValue get(DynamicAttribute dynamicAttribute) {
        return values.get(dynamicAttribute);
    }

    public Collection<AttributeValue> getValues() {
        return values.values();
    }

    public int size() {
        return values.size();
    }

    private final Map<DynamicAttribute, AttributeValue> values;
}
