/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.util.collections;

import com.zynap.domain.IDomainObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class DomainObjectCollectionHelper {

    public static IDomainObject findById(Collection domainObjectCollection, Long id) {
        Assert.isTrue(id != null, "The item cannot be found if evaluator id is null");
        return (IDomainObject) CollectionUtils.find(domainObjectCollection, new IdPredicate(id));
    }

    public static IDomainObject findByLabel(Collection domainObjectCollection, String label) {
        Assert.hasText(label, "We cannot find an item if the label given is empty or null");
        return (IDomainObject) CollectionUtils.find(domainObjectCollection, new LabelPredicate(label));
    }

    public static IDomainObject findByField(Collection domainObjectCollection, String field, String value) {
        Assert.hasText(value, "We cannot find an item if the value given is empty or null");
        Assert.hasText(field, "We cannot find an item if the value given is empty or null");
        return (IDomainObject) CollectionUtils.find(domainObjectCollection, new FieldPredicate(field, value));
    }

    private static class IdPredicate implements Predicate {

        public IdPredicate(Long evaluator) {
            this.evaluator = evaluator;
        }

        public boolean evaluate(Object object) {
            return evaluator.equals(((IDomainObject) object).getId());
        }

        private Long evaluator;
    }

    private static class LabelPredicate implements Predicate {

        public LabelPredicate(String evaluator) {
            this.evaluator = evaluator;
        }

        public boolean evaluate(Object object) {
            return evaluator.equals(((IDomainObject) object).getLabel());
        }

        private String evaluator;
    }

    private static class FieldPredicate implements Predicate {

        public FieldPredicate(String field, Object value) {
            this.field = field;
            this.value = value;
        }

        public boolean evaluate(Object object) {
            try {
                return value.equals(BeanUtils.getProperty(object, field));
            } catch (Exception e) {
                return false;
            }
        }

        private Object value;
        private String field;
    }
}
