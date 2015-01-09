/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.messages;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;
import net.sf.hibernate.transform.DistinctRootEntityResultTransformer;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 13-Sep-2007 12:20:03
 */
public class HibernateMessageDao extends HibernateCrudAdaptor implements IMessageDao {

    public Class getDomainObjectClass() {
        return MessageItem.class;
    }

    public List<MessageItem> findAll(Long userId) {
        Session session = getSession();
        try {
            //noinspection unchecked
            return session.createCriteria(getDomainObjectClass())
                    .add(Expression.eq("toUserId", userId))
                    .addOrder(Order.asc("type"))
                    .addOrder(Order.asc("dateReceived"))
                    .addOrder(Order.asc("status"))
                    .setResultTransformer(new DistinctRootEntityResultTransformer())
                    .list();

        } catch (HibernateException e) {
            logger.error(e.getMessage(), e);
            throw new DataAccessResourceFailureException(e.getMessage(), e);
        }
    }

    public void delete(String[] messageItemIds) {
        getHibernateTemplate().delete("from MessageItem item where item.id in (" + StringUtils.arrayToCommaDelimitedString(messageItemIds) + ")");
    }

    public Integer countUnreadMessages(Long userId) throws TalentStudioException {
        final StringBuffer query = new StringBuffer("select count(*) from MessageItem item where item.toUserId = ");
        query.append(userId).append(" and item.status = 'UNREAD'");
        Integer numRecords = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Query queryObject = session.createQuery(query.toString());
                return queryObject.uniqueResult();
            }
        }, true);
        return numRecords;
    }
}
