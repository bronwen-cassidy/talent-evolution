package com.zynap.talentstudio.analysis;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.IPopulationDao;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.AccessType;
import com.zynap.talentstudio.common.groups.Group;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 25-Feb-2005
 * Time: 10:47:38
 */
public class AnalysisService implements IAnalysisService {

    public Collection<PopulationDto> findAll(String type, Long userId, String scope) throws TalentStudioException {
        return populationDao.findPopulations(type, userId, AccessType.PUBLIC_ACCESS.toString().equals(scope));
    }

    public Collection<PopulationDto> findAllByGroup(String nodeType, Long userId, Group userGroup) {
        if(userGroup == null) {
            return populationDao.findPopulations(nodeType, userId, false);
        }
        return populationDao.findPopulations(nodeType, userId, userGroup.getId());
    }

    public Population create(Population population, Long userId) throws TalentStudioException {
        population.setUserId(userId);
        return (Population) populationDao.create(population);
    }

    public void update(Population population) throws TalentStudioException {
        populationDao.update(population);
    }

    public void delete(Population population) throws TalentStudioException {
        populationDao.delete(population);
    }

    public IDomainObject findById(Serializable id) throws TalentStudioException {
        return populationDao.findById(id);
    }

    public boolean populationInPublicReport(Long populationId) {
        return populationDao.populationInPublicReport(populationId);
    }

    public void setPopulationDao(IPopulationDao populationDao) {
        this.populationDao = populationDao;
    }

    IPopulationDao populationDao;
}
