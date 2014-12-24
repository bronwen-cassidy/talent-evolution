package com.zynap.talentstudio.help;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.DomainObjectNotFoundException;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

/**
 * User: amark
 * Date: 07-Aug-2006
 * Time: 17:09:14
 */
public final class HelpTextService extends DefaultService implements IHelpTextService {

    protected IFinder getFinderDao() {
        return helpTextDao;
    }

    protected IModifiable getModifierDao() {
        return helpTextDao;
    }

    public void setHelpTextDao(IHelpTextDao helpTextDao) {
        this.helpTextDao = helpTextDao;
    }

    public HelpTextItem findByID(Long id) throws TalentStudioException {
        try {
            return (HelpTextItem) helpTextDao.findByID(id);
        } catch (DomainObjectNotFoundException e) {
            return null;
        }
    }


    public void update(IDomainObject domainObject) throws TalentStudioException {
        helpTextDao.saveOrUpdate((HelpTextItem) domainObject);
    }

    private IHelpTextDao helpTextDao;
}
