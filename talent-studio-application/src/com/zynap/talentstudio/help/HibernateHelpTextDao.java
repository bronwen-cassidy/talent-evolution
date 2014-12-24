package com.zynap.talentstudio.help;

import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.HibernateCrudAdaptor;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

/**
 * User: amark
 * Date: 07-Aug-2006
 * Time: 17:07:57
 */
public final class HibernateHelpTextDao extends HibernateCrudAdaptor implements IHelpTextDao {

    public Class getDomainObjectClass() {
        return HelpTextItem.class;
    }

    public void saveOrUpdate(HelpTextItem helpTextItem) {
        getHibernateTemplate().saveOrUpdate(helpTextItem);
        // update dynamic attribute
        DynamicAttribute dynamicAttribute = (DynamicAttribute) getHibernateTemplate().get(DynamicAttribute.class, helpTextItem.getId());
        dynamicAttribute.setHasHelpText(true);
        getHibernateTemplate().update(dynamicAttribute);
        // todo determine why we are calling a refresh here?
        //getHibernateTemplate().refresh(dynamicAttribute);
    }

    public void delete(Long id) throws TalentStudioException {

        try {
            final HelpTextItem helpTextItem = (HelpTextItem) findByID(id);
            getHibernateTemplate().delete(helpTextItem);
            DynamicAttribute dynamicAttribute = (DynamicAttribute) getHibernateTemplate().get(DynamicAttribute.class, id);
            dynamicAttribute.setHasHelpText(false);
            getHibernateTemplate().update(dynamicAttribute);
            getHibernateTemplate().refresh(dynamicAttribute);
        } catch (DomainObjectNotFoundException ignored) {
            logger.info("help text item with id: " + id + " not found", ignored);
        }
    }
}