package com.zynap.talentstudio.security.areas;

import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.collections.Predicate;

/**
 * Predicate used to find areaelements nodes with the specified class.
 */
public class NodeTypeAreaElementPredicate implements Predicate {

    /**
     * The class.
     */
    private Class clazz;

    /**
     * Constructor.
     * @param clazz
     */
    public NodeTypeAreaElementPredicate(Class clazz) {
        this.clazz = clazz;
    }

    /**
     * Check if the object matches our criteria.
     *
     * @param object
     * @return true if the object class type is the same.
     */
    public boolean evaluate(Object object) {
        AreaElement areaElement = (AreaElement) object;
        final Node node = getNode(areaElement);
        return clazz == node.getClass();
    }

    protected Node getNode(Object areaElement) {
        return ((AreaElement) areaElement).getNode();
    }
}
