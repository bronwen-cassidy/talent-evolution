package com.zynap.talentstudio.search;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.MatchMode;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HibernateSearchTermDaoImpl extends HibernateDaoSupport implements SearchTermDao {

    public List<SearchTermResult> search(Long userId, String searchTerm, Long permitId) {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(SearchTermResult.class);
            criteria.add(Expression.like("label", searchTerm, MatchMode.ANYWHERE))
                    .add(Expression.eq("userId", userId))
                    .add(Expression.or(Expression.eq("managerRead", Boolean.TRUE), Expression.eq("individualRead", Boolean.TRUE)));
            return criteria.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<SearchTermResult>();
        }
    }

    public List<DataTerm> search(String searchTerm) {
        try {
            Session session = getSession(false);
            Criteria criteria = session.createCriteria(SearchTermResult.class);
            criteria.add(Expression.like("label", searchTerm, MatchMode.START));
            return criteria.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            return new ArrayList<DataTerm>();
        }
    }
}
