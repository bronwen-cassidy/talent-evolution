package com.zynap.talentstudio.common;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;

import java.io.Serializable;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author jsueiras
 * @version 0.1
 * @since 12-Oct-2005 15:19:39
 */
public interface IZynapService {

    public IDomainObject findById(Serializable id) throws TalentStudioException;

    public List findAll() throws TalentStudioException;

    public void create(IDomainObject domainObject) throws TalentStudioException;

    public void update(IDomainObject domainObject) throws TalentStudioException;

    public void delete(IDomainObject domainObject) throws TalentStudioException;

    public void disable(IDomainObject domainObject) throws TalentStudioException;

    void updateStateInfo(IDomainObject domainObject);

    final Long ROOT_USER_ID = (long) 0;

}
