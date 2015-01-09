package com.zynap.talentstudio.analysis.populations;

import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 23-Feb-2005
 * Time: 13:58:01
 */
public interface IPopulationDao extends IFinder,IModifiable {

    /**
     * Find all public populations of specified type, plus any private ones of specified type created by the user if publicOnly is false.
     * @param type Must be either {@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_} or {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_}
     * @param userId
     * @param publicOnly
     * @return Collection
     */
    Collection<PopulationDto> findPopulations(String type, Long userId, boolean publicOnly);

    /**
     * Check if population is in use in a public report. 
     * @param populationId
     * @return true or false
     */
    boolean populationInPublicReport(Long populationId);

    Collection<PopulationDto> findPopulations(String nodeType, Long userId, Long id);
}
