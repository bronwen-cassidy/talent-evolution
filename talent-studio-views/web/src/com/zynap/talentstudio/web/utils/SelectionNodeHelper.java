/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.utils;

import com.zynap.domain.IDomainObject;
import com.zynap.talentstudio.common.SelectionNode;
import com.zynap.talentstudio.organisation.portfolio.search.IField;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import java.util.Collection;
import java.util.Arrays;
import java.util.List;

/**
 * Class or Interface description.
 * todo return the enabled object collection always
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public final class SelectionNodeHelper {

	public static boolean hasSelectedItems(Collection collection) {
		return collection != null && CollectionUtils.exists(collection, new SelectionNodePredicate());
	}

    /**
     * takes a collection of {@link SelectionNode } objects and sets all selections to false.
     *
     * @param selection
     */
    public static void disableSelections(Collection selection) {
        CollectionUtils.transform(selection, new SelectionNodeDisabler());
    }

    /**
     * Enables the selection that has a value that matches the given object.
     *
     * @param toEnable the collection that is searched for items to enable
     * @param valueToMatch the value to match
     */
    public static void enableSelections(Collection toEnable, Object valueToMatch) {
        disableSelections(toEnable);
        CollectionUtils.transform(toEnable, new SelectionNodeIdTransformer(valueToMatch, null));
    }

    /**
     * Enables the selection node with the input values used as the criteria.
     *
     * @param toEnable
     * @param valuesToMatch
     */
    public static void enableSelections(Collection toEnable, Object[] valuesToMatch) {
        disableSelections(toEnable);
        for (int i = 0; i < valuesToMatch.length; i++) {
            CollectionUtils.transform(toEnable, new SelectionNodeIdTransformer(valuesToMatch[i], null));          
        }
    }

    /**
     * Enables items in the toEnable collection that match the valueToMatch, populating the toPopulate collection with the enabled results.
     *
     * @param toEnable the collection which needs items enabled
     * @param valuesToMatch the value to check which items are to be enabled
     * @param toPopulate the collection which will be populated with the enabled items
     */
    public static void enableSelections(Collection toEnable, Object[] valuesToMatch, Collection<IField> toPopulate) {
        disableSelections(toEnable);
        for (int i = 0; i < valuesToMatch.length; i++) {
            CollectionUtils.transform(toEnable, new SelectionNodeIdTransformer(valuesToMatch[i], toPopulate));
        }
    }

    public static void enableDomainObjectSelections(Collection toEnable, Long[] valuesToMatch) {
        disableSelections(toEnable);
        CollectionUtils.transform(toEnable, new SelectionNodeEnabler(valuesToMatch));
    }

    /**
     * Enables items in the toEnable collection that match the valueToMatch, populating the toPopulate collection with the enabled results.
     *
     * NOTE: The enabled results contain the underlying, wrapped object not the selectionNode.
     *
     * @param toEnable the collection which needs items enabled
     * @param valuesToMatch the value to check which items are to be enabled
     * @param toPopulate the collection which will be populated with the enabled items which are the value objects of the selection node.
     */
    public static void enableDomainObjectSelections(Collection toEnable, Object[] valuesToMatch, Collection toPopulate) {
        disableSelections(toEnable);
        CollectionUtils.transform(toEnable, new MultiSelectionNodeTransformer(valuesToMatch, toPopulate));
    }

    /**
     * Wraps any object with a selectionNode, the selectionNodes value holds the requid object
     *
     * @param toWrap  the collection containing the objects to wrap
     * @return a collection of transformed objects. Objects of type {@link SelectionNode} which have the
     *         given objects in the collection as their values
     */
    public static Collection createDomainObjectSelections(Collection toWrap) {
        CollectionUtils.transform(toWrap, new SelectionNodeWrapper());
        return toWrap;
    }

    /**
     * Takes a collection of {@link IDomainObject} objects extracts the id and label attribute values.
     *
     * @param domainObjects a collection of IDomainObjects
     * @return Collection of selectionNodes whose value is the id of the domainObject and whose name is the label
     *         of the domain object
     */
    public static Collection createSelections(Collection domainObjects) {
        CollectionUtils.transform(domainObjects, new SelectionNodeIdCreator());
        return domainObjects;
    }

    /**
     * Wraps the domain object with a selection node.
     * Uses the existingSelectedDomainObjects to use as the criteria to enable those that match.
     *
     * @param domainObjects
     * @param existingSelectedDomainObjects
     * @return Collection of selectionNode objects wrapping the domainObject in the domainObjects parameter with selcted to true for
     *                    those that match the input existingSelectedDomainObjects. If this is null the collection returned contains
     *                    all deselected wrapped domainObjects.
     */
    public static Collection createDomainObjectSelections(Collection domainObjects, Collection existingSelectedDomainObjects) {
        if(existingSelectedDomainObjects == null || existingSelectedDomainObjects.isEmpty()) {
            return createDomainObjectSelections(domainObjects);
        }
        CollectionUtils.transform(domainObjects, new SelectionNodeFilter(existingSelectedDomainObjects));
        return domainObjects;
    }

    private static class SelectionNodeDisabler implements Transformer {

        public Object transform(Object input) {
            SelectionNode selectionNode = (SelectionNode) input;
            selectionNode.setSelected(false);

            return selectionNode;
        }
    }

    private static class SelectionNodeEnabler implements Transformer {

        public SelectionNodeEnabler(Long[] idsToMatch) {
            this.idsToMatch = Arrays.asList(idsToMatch);
        }

        public Object transform(Object input) {
            SelectionNode selectionNode = (SelectionNode) input;
            if (idsToMatch != null && !idsToMatch.isEmpty()) {
                IDomainObject object = (IDomainObject) selectionNode.getValue();
                if(idsToMatch.contains(object.getId())) selectionNode.setSelected(true);
            }
            return selectionNode;
        }

        private List<Long> idsToMatch;
    }

    private static class SelectionNodeIdCreator implements Transformer {

        public SelectionNodeIdCreator() {
        }

        public Object transform(Object input) {
            IDomainObject domainObject = (IDomainObject) input;
            return new SelectionNode(domainObject.getId(), domainObject.getLabel());
        }

    }

    private static class SelectionNodeIdTransformer implements Transformer {

        public SelectionNodeIdTransformer(Object valueToMatch, Collection<IField> matchedContent) {
            this.valueToMatch = valueToMatch;
            this.matchedContent = matchedContent;
        }

        public Object transform(Object input) {
            SelectionNode selectionNode = (SelectionNode) input;
            if (selectionNode.getValue().equals(valueToMatch)) {
                selectionNode.setSelected(true);
                if (matchedContent != null) {
                    matchedContent.add(selectionNode);
                }
            }
            return selectionNode;
        }

        protected Object valueToMatch;
        protected Collection<IField> matchedContent;
    }

    private static class SelectionNodeWrapper implements Transformer {

        public SelectionNodeWrapper() {
        }

        public Object transform(Object input) {
            if (input instanceof IDomainObject) {
                IDomainObject domainObject = (IDomainObject) input;
                return new SelectionNode(domainObject, domainObject.getLabel());
            }
            return new SelectionNode(input);
        }

    }

    private static class MultiSelectionNodeTransformer implements Transformer {

        public MultiSelectionNodeTransformer(Object[] matchValues, Collection toComplete) {
            this.matchValues = matchValues;
            this.toComplete = toComplete;
        }

        public Object transform(Object input) {
            SelectionNode selectionNode = (SelectionNode) input;
            IDomainObject value = (IDomainObject) selectionNode.getValue();
            for (int i = 0; i < matchValues.length; i++) {
                Object matchValue = matchValues[i];
                if(value.getId().equals(matchValue)) {
                    selectionNode.setSelected(true);
                    toComplete.add(value);
                }
            }
            return selectionNode;
        }

        private Object[] matchValues;
        private Collection toComplete;
    }

    private static class SelectionNodeFilter implements Transformer {

        public SelectionNodeFilter(Collection existing) {
            this.existingObjects = existing;
        }

        public Object transform(Object input) {
            IDomainObject domainObject = (IDomainObject) input;
            SelectionNode node = new SelectionNode(domainObject, domainObject.getLabel());
            if(existingObjects.contains(input)) {
                node.setSelected(true);
            }
            return node;
        }

        private Collection existingObjects;
    }

	private static class SelectionNodePredicate implements Predicate {

		public boolean evaluate(Object object) {
			SelectionNode selectionNode = (SelectionNode) object;
			return selectionNode.isSelected();
		}
	}
}
