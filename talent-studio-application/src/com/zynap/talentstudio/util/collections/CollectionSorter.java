package com.zynap.talentstudio.util.collections;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * User: amark
 * Date: 20-Jan-2005
 * Time: 17:20:21
 */
public class CollectionSorter {

    private CollectionSorter() {
    }

    public static final PrimaryAttrHolder sort(String primaryAttribute, String secondaryAttribute, Collection collection) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        PrimaryAttrHolder primaryAttrHolder = new PrimaryAttrHolder();

        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            String primaryAttributeValue = BeanUtils.getNestedProperty(object, primaryAttribute);
            String secondaryAttributeValue = BeanUtils.getNestedProperty(object, secondaryAttribute);
            primaryAttrHolder.add(primaryAttributeValue, secondaryAttributeValue, object);
        }

        return primaryAttrHolder;
    }

    public static class PrimaryAttrHolder {
        private Map<String, SecondaryAttrHolder> entries = new LinkedHashMap<String, SecondaryAttrHolder>();

        private Set<String> uniqueSecondaryAttributes = new LinkedHashSet<String>();

        public void add(String primaryAttributeValue, String secondaryAttributeValue, Object object) {
            SecondaryAttrHolder entry = entries.get(primaryAttributeValue);
            if (entry == null) {
                entry = new SecondaryAttrHolder(primaryAttributeValue);
                entries.put(primaryAttributeValue, entry);
            }

            entry.add(secondaryAttributeValue, object);

            uniqueSecondaryAttributes.add(secondaryAttributeValue);
        }

        public Map getEntries() {
            return entries;
        }

        public Collection getUniqueSecondaryAttributes() {
            return uniqueSecondaryAttributes;
        }

        public Collection getUniquePrimaryAttributes() {
            return entries.keySet();
        }

        public int numberOfUniquePrimaryAttributeValues() {
            return entries.size();
        }

        public int numberOfUniqueSecondaryAttributeValues() {
            return uniqueSecondaryAttributes.size();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PrimaryAttrHolder)) return false;

            final PrimaryAttrHolder primaryAttrHolder = (PrimaryAttrHolder) o;

            if (!entries.equals(primaryAttrHolder.entries)) return false;
            if (!uniqueSecondaryAttributes.equals(primaryAttrHolder.uniqueSecondaryAttributes)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = entries.hashCode();
            result = 29 * result + uniqueSecondaryAttributes.hashCode();
            return result;
        }
    }

    public static class SecondaryAttrHolder {
        private String primaryAttributeValue;

        private Map<String, Object> entries = new LinkedHashMap<String, Object>();

        public void add(String secondaryAttributeValue, Object object) {
            entries.put(secondaryAttributeValue, object);
        }

        public SecondaryAttrHolder(String primaryAttributeValue) {
            this.primaryAttributeValue = primaryAttributeValue;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SecondaryAttrHolder)) return false;

            final SecondaryAttrHolder secondaryAttrHolder = (SecondaryAttrHolder) o;

            if (!entries.equals(secondaryAttrHolder.entries)) return false;
            if (!primaryAttributeValue.equals(secondaryAttrHolder.primaryAttributeValue)) return false;

            return true;
        }

        public int hashCode() {
            int result;
            result = primaryAttributeValue.hashCode();
            result = 29 * result + entries.hashCode();
            return result;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SecondaryAttrHolder[");
            stringBuffer.append("\r\n primaryAttributeValue=").append(primaryAttributeValue);
            stringBuffer.append("]");

            return stringBuffer.toString();
        }

        public Object get(String secondaryAttribute) {
            return entries.get(secondaryAttribute);
        }

        public Map getEntries() {
            return entries;
        }
    }
}
