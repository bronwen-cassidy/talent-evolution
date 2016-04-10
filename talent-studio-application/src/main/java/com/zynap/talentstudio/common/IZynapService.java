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

    <T> T findById(Long id) throws TalentStudioException;

    List findAll() throws TalentStudioException;

    void create(IDomainObject domainObject) throws TalentStudioException;

    void update(IDomainObject domainObject) throws TalentStudioException;

    void delete(IDomainObject domainObject) throws TalentStudioException;

    void disable(IDomainObject domainObject) throws TalentStudioException;

    void updateStateInfo(IDomainObject domainObject);

    Long ROOT_USER_ID = (long) 0;

}
