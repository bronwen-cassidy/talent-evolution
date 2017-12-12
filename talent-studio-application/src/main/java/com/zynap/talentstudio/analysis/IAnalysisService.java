/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.analysis;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationDto;
import com.zynap.talentstudio.common.IZynapService;
import com.zynap.talentstudio.common.groups.Group;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author jsueiras
 * @version $Revision: $
 *          $Id: $
 */
public interface IAnalysisService extends IZynapService {

    Population create(Population population, Long userId) throws TalentStudioException;

    void update(Population population) throws TalentStudioException;

    void delete(Population population) throws TalentStudioException;

    Collection<PopulationDto> findAll(String type, Long userId, String scope) throws TalentStudioException;

    boolean populationInPublicReport(Long populationId);

    Collection<PopulationDto> findAllByGroup(String nodeType, Long userId, Group userGroup);
}
