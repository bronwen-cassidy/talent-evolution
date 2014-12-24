package com.zynap.talentstudio.integration.adapter;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.Node;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bronwen.
 * Date: 21/03/12
 * Time: 11:26
 */
public interface IAdapter extends Serializable {

    /**
     * Creates all the people within this population to Talent Analytics system
     * @param population
     * @return AdaptorResults - this records what was added and what was updated.
     * @throws TalentStudioException
     */
    AdaptorResults exportPopulation(Population population) throws TalentStudioException;

    /**
     * Updates the domain object that may or may not be associated with the population
     * @param obj - the subject to create
     * @param population - may be null
     * @throws TalentStudioException
     */
    void create(IDomainObject obj, Population population) throws TalentStudioException;

    void update (IDomainObject obj) throws TalentStudioException;
    void update (IDomainObject obj, Population population) throws TalentStudioException;
    void update (IDomainObject obj, String repondentId) throws TalentStudioException;
    void update (IDomainObject obj, String repondentId, Population population) throws TalentStudioException;
    AdaptorResults importPopulation(Population population) throws TalentStudioException;

    /**
     * Updates the talentstudio subject according to chnges made from the third party systm.
     * <p>This method will lookup the external id of the subject, load it then perform an update to the Subject in the talentstudio system./p>
     * @param nodeId - the id of the subject
     * @throws TalentStudioException
     */
    void update (Long nodeId) throws TalentStudioException;
    void delete (IDomainObject obj);



    List<Node> synchroniseSystems() throws TalentStudioException;

}
