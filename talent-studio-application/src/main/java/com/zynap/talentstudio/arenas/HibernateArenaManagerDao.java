/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.arenas;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.SecurityConstants;

import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Hibernate implementation of IArenaManagerDao
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
@SuppressWarnings({"unchecked"})
public class HibernateArenaManagerDao extends HibernateDaoSupport implements IArenaManagerDao {

    public Class getDomainObjectClass() {
        return Arena.class;
    }

    public Arena findById(Serializable id) throws TalentStudioException {
        try {
            return (Arena) getHibernateTemplate().load(Arena.class, id);
        } catch (HibernateObjectRetrievalFailureException e) {
            throw new DomainObjectNotFoundException(Arena.class, id, e);
        }
    }

    public Collection<Arena> getActiveArenas() {
        return getHibernateTemplate().find("from com.zynap.talentstudio.arenas.Arena arena where arena.active = 'T' order by arena.sortOrder");
    }

    public Collection<Arena> getArenas() {
        return getHibernateTemplate().find("from com.zynap.talentstudio.arenas.Arena arena order by upper(arena.label)");
    }

    public Collection<Arena> getSortedArenas() {
        return getHibernateTemplate().find("from com.zynap.talentstudio.arenas.Arena arena order by arena.sortOrder");
    }

    public Collection<Arena> getHideableArenas() {
        return getHibernateTemplate().find("from com.zynap.talentstudio.arenas.Arena arena where arena.hideable = 'T' order by upper(arena.label)");
    }

    public void update(Arena arena) {
        getHibernateTemplate().update(arena);
    }

    public Collection<MenuSection> getReportMenuSections() {
        String query = "from MenuSection menuSection where menuSection.primaryKey.id = '" + SecurityConstants.REPORTS_CONTENT + "' and menuSection.arena.id != '" + IArenaManager.ANALYSIS_MODULE + "' order by menuSection.arena.sortOrder";
        return getHibernateTemplate().find(query);
    }

    public Collection<MenuSection> getHomePageReportMenuSections() {
        StringBuffer query = new StringBuffer("from MenuSection menuSection where menuSection.arena.id = '");
        query.append(IArenaManager.MYZYNAP_MODULE).append("'");
        query.append(" and menuSection.primaryKey.id in (");

        Collection hideableArenas = getHideableArenas();
        int count = 0;
        for (Iterator iterator = hideableArenas.iterator(); iterator.hasNext(); count++) {
            Arena hideableArena = (Arena) iterator.next();
            query.append("'").append(hideableArena.getArenaId()).append("'");
            if (count < hideableArenas.size() - 1) query.append(",");
        }

        query.append(") order by menuSection.arena.sortOrder, menuSection.sortOrder ");

        return getHibernateTemplate().find(query.toString());
    }

    public Collection<MenuSection> getMenuSections(String arenaId) {
        return getHibernateTemplate().find("from com.zynap.talentstudio.arenas.MenuSection section where section.primaryKey.arenaId=? order by upper(section.label)", arenaId);
    }

    public MenuSection getMenuSection(String arenaId, String sectionId) {
        final List list = getHibernateTemplate().find("from com.zynap.talentstudio.arenas.MenuSection menuSection where menuSection.primaryKey.arenaId=? and menuSection.primaryKey.id=?", new Object[]{arenaId, sectionId});
        return list.isEmpty() ? null : (MenuSection) list.get(0);
    }

    public void update(MenuSection menuSection) {
        getHibernateTemplate().update(menuSection);
    }

    public List<MenuItem> getMenuItems(Long userId, String userType) throws TalentStudioException {
        try {
            StringBuffer x = new StringBuffer("select {mi.*} from  menu_items mi, permits p, permits_roles pr, roles r, app_roles_users au, MODULES m");
            x.append(" where mi.permit_id=p.id")
                    .append(" and (mi.user_type='").append(userType).append("' OR MI.USER_TYPE IS NULL)")
                    .append(" and pr.permit_id=p.id")
                    .append(" and pr.role_id=r.id")
                    .append(" and au.role_id=r.id")
                    .append(" and au.user_id=").append(userId)
                    .append(" and mi.module_id=m.id")
                    .append(" and m.is_active='T' ");

            Session session = getSession(false);
            final Query query1 = session.createSQLQuery(x.toString(), "mi", MenuItem.class);
            return query1.list();
        } catch (HibernateException e) {
            throw new TalentStudioException(e);
        }
    }
}