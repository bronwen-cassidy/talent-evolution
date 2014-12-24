package com.zynap.talentstudio.util.collections;

import com.zynap.talentstudio.organisation.Node;

import org.apache.commons.collections.Transformer;

import java.util.*;

/**
 * User: amark
 * Date: 17-Aug-2005
 * Time: 17:36:06
 */
public final class CollectionUtils {

    public static List removeDuplicates(final Collection<Object> collection) {
        Set<Object> set = new LinkedHashSet<Object>(collection);
        return new ArrayList<Object>(set);
    }

    public static boolean isLastElement(int i, Collection list) {
        return i == list.size() - 1;
    }

    public static boolean hasElements(Collection list) {
        return list != null && !list.isEmpty();
    }

    public static void transformFromObjectArray(List list) {
        org.apache.commons.collections.CollectionUtils.transform(list, new Transformer() {
            public Object transform(Object o) {
                Object[] row = (Object[]) o;
                return row[0];
            }
        });
    }

    public static void sortByLevel(List<Object[]> list) {
        Collections.sort(list, new Comparator<Object[]>() {
            public int compare(Object[] row1, Object[] row2) {                
                return ((Integer) row1[1]).compareTo((Integer) row2[1]);
            }
        });
    }

    public static void hasAccessTransformer(List<Object[]> list, List<Object[]> list2) {
        org.apache.commons.collections.CollectionUtils.transform(list, new Transformer() {
            public Object transform(Object o) {
                Object[] row = (Object[]) o;
                Node p = (Node) row[0];
                p.setHasAccess(true);
                return row;
            }
        });
        list.addAll(list2);
    }
}
