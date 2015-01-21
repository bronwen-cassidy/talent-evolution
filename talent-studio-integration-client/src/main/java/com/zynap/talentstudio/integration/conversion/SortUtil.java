/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.conversion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 11-Jul-2007 14:45:25
 */

public class SortUtil {

    public static List<ISortNode> sortByTreeLevel(List<ISortNode> unsorted) {
        List<ISortNode> topLevelNodes = findOrphans(unsorted);
        return sortAccordingToParents(new ArrayList<ISortNode>(), unsorted, topLevelNodes);
    }

    private static List<ISortNode> sortAccordingToParents(List<ISortNode> sorted, List<ISortNode> unsorted, List<ISortNode> parents) {
        if (parents.isEmpty()) {
            return sorted;
        } else {
            List<ISortNode> newParents = findChildren(unsorted, parents);
            unsorted = (List<ISortNode>) CollectionUtils.subtract(unsorted, parents);
            sorted.addAll(parents);
            return sortAccordingToParents(sorted, unsorted, newParents);
        }
    }

    private static List<ISortNode> findChildren(List<ISortNode> unsorted, List<ISortNode> parents) {
        List<ISortNode> newParents = new ArrayList<ISortNode>();
        for (ISortNode node : unsorted) {
            final String parentId = node.getParentId();
            if (parentExists(parents, parentId)) newParents.add(node);
        }
        return newParents;
    }

    private static List<ISortNode> findOrphans(List<ISortNode> unsorted) {
        List<ISortNode> orphans = new ArrayList<ISortNode>();
        for (ISortNode node : unsorted) {
            final String parentId = node.getParentId();
            if (parentId==null||!parentExists(unsorted, parentId)) orphans.add(node);
        }
        return orphans;
    }

    private static boolean parentExists(List<ISortNode> unsorted, final String parentId) {
        ISortNode parent = (ISortNode) CollectionUtils.find(unsorted, new Predicate() {
            public boolean evaluate(Object object) {
                if(parentId!=null)
                    return parentId.equals(((ISortNode) object).getId());
                return false;
            }
        });
        return parent != null;
    }

}