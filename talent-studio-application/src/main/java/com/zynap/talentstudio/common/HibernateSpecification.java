package com.zynap.talentstudio.common;

import net.sf.hibernate.expression.Criterion;

/**
 *
 */
public interface HibernateSpecification {

	Criterion toCriteria();
}
