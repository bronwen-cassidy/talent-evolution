/*
 * $Header: ${}
 * $Revision: ${}
 * $Date: 21-Nov-2007
 *
 * Copyright (c) 1999-2006 Bronwen Cassidy.  All rights reserved.
 */
package com.zynap.talentstudio.common.groups;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Query;
import net.sf.hibernate.expression.EqExpression;
import net.sf.hibernate.expression.Order;
import net.sf.hibernate.transform.DistinctRootEntityResultTransformer;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.security.homepages.HomePage;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 21-Nov-2007 08:54:37
 */
public class HibernateGroupDao extends HibernateCrudAdaptor implements IGroupDao {

    public Class getDomainObjectClass() {
        return Group.class;
    }

    public List<Group> find(String groupType) throws TalentStudioException {
        try {
            //noinspection unchecked
            return getSession().createCriteria(getDomainObjectClass())
                    .add(new EqExpression("type", groupType, false))
                    .addOrder(Order.asc("label"))
                    .setResultTransformer(new DistinctRootEntityResultTransformer())
                    .list();
        } catch (HibernateException e) {
            logger.error(e);
            throw new TalentStudioException(e.getMessage(), e);
        }
    }

    public List<HomePage> findHomePages(Long groupId) throws TalentStudioException {
        try {
            //noinspection unchecked
            return getSession().createCriteria(HomePage.class)
                    .add(new EqExpression("group.id", groupId, false))
                    .setResultTransformer(new DistinctRootEntityResultTransformer())
                    .list();
        } catch (HibernateException e) {
            logger.error(e);
            throw new TalentStudioException(e.getMessage(), e);
        }
    }    
}
