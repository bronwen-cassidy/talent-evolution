/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.common;

import com.zynap.common.util.StringUtil;
import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.security.UserSessionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class DefaultService implements IZynapService {

    protected Long getUserId() {
        return UserSessionFactory.getUserSession().getUserPrincipal().getUserId();
    }

    /**
     * Finds a given domainObject.
     *
     * @param id id of object to be found
     * @return the specified domainObject.
     */
    public <T> T findById(Long id) throws TalentStudioException {
        return getFinderDao().findById(id);
    }

    public void create(IDomainObject domainObject) throws TalentStudioException {
        getModifierDao().create(domainObject);
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {
        getModifierDao().update(domainObject);
    }

    public void delete(IDomainObject domainObject) throws TalentStudioException {
        getModifierDao().delete(domainObject);        
    }

    public void disable(IDomainObject domainObject) throws TalentStudioException {
        throw new UnsupportedOperationException("Disable not supported");
    }

    public <T> List<T> findAll() throws TalentStudioException {
        return getFinderDao().findAll();
    }    

    public void updateStateInfo(IDomainObject domainObject) {
    }

    protected abstract IFinder getFinderDao();

    protected abstract IModifiable getModifierDao();

    /**
     * Build list of participants by running associated population.
     * <br/> There are 3 types of questionnaires - performance reviews, questionnaires and info forms.
     * <br/> Questionnaires are meant only for people in the selected population who can login.
     * <br/> Performance reviews are meant for managers - therefore the population gives you the list of evaulatees and it does not matter if they can log in or not.
     * <br/> Info forms display the same behaviour as performance reviews
     * - it doesn't matter if the subject can login - if they can he will see the info form in their portfolio, and their superior(s) will always see the infoform in the subject portfolio.
     *
     * @param activeUsersOnly
     * @param populationId
     * @param userId
     * @return list of subjects
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public List determineParticipants(Long userId, boolean activeUsersOnly, final Long populationId, IPopulationEngine populationEngine, IAnalysisService analysisService) throws TalentStudioException {

        List results = null;
        if (populationId != null) {

            // IMPORTANT: find population to avoid lazy initialisation problems and then mockPopulation population to avoid accidentally modifying the population
            final Population newPopulation = (Population) analysisService.findById(populationId);
            final List<PopulationCriteria> populationCriterias = newPopulation.getPopulationCriterias();

            final Population mockPopulation = new Population();
            mockPopulation.setType(IPopulationEngine.P_SUB_TYPE_);
            for (PopulationCriteria populationCriteria : populationCriterias) {
                PopulationCriteria criteria = (PopulationCriteria) populationCriteria.clone();
                criteria.setId(null);
                mockPopulation.addPopulationCriteria(criteria);
            }
            mockPopulation.setForSearching(true);

            if (activeUsersOnly) {

                // add additional criteria to only get subjects with active users
                final PopulationCriteria activeUserCriteria = new PopulationCriteria();

                // check if "and" operator is required
                String operator = (populationCriterias.isEmpty()) ? null : IPopulationEngine.AND;
                activeUserCriteria.setOperator(operator);

                activeUserCriteria.setType(IPopulationEngine.OP_TYPE_);
                activeUserCriteria.setAttributeName("user.active");
                activeUserCriteria.setComparator(IPopulationEngine.EQ);
                activeUserCriteria.setRefValue(StringUtil.TRUE);
                mockPopulation.addPopulationCriteria(activeUserCriteria);
            }

            Set<Node> simpleResults = new HashSet<Node>(populationEngine.find(mockPopulation, userId));
            results = new ArrayList<Node>(simpleResults);
        }
        return results;
    }

    protected final Log logger = LogFactory.getLog(getClass());
}
