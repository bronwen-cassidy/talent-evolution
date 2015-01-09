/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.security.areas;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionDto;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 07-Dec-2009 17:24:49
 */
public class AreaUtils {

    public static Set<AreaElement> assignPopulationElements(Area newArea, IPopulationEngine populationEngine, IAnalysisService analysisService) throws TalentStudioException {

        Set<AreaElement> newAreaElements = new HashSet<AreaElement>();
        List<Long> nodeIds = new ArrayList<Long>();
        if (newArea.getSubjectPopulationId() != null) {
            final List<SubjectDTO> subjects = populationEngine.findSubjects((Population) analysisService.findById(newArea.getSubjectPopulationId()), IDomainObject.ROOT_USER_ID);
            for (SubjectDTO subject : subjects) {
                final Long nodeId = subject.getId();
                if (!newArea.containsNode(nodeId) && !nodeIds.contains(nodeId)) {
                    newAreaElements.add(new AreaElement(new Subject(nodeId, null), false, true));
                    nodeIds.add(nodeId);
                }
            }
        }

        // add in the position elements
        if (newArea.getPositionPopulationId() != null) {
            final List<PositionDto> positions = populationEngine.findPositions((Population) analysisService.findById(newArea.getPositionPopulationId()), IDomainObject.ROOT_USER_ID);
            for (PositionDto positionDto : positions) {
                final Long nodeId = positionDto.getId();
                if (!newArea.containsNode(nodeId) && !nodeIds.contains(nodeId)) {
                    newAreaElements.add(new AreaElement(new Position(nodeId, positionDto.getTitle()), false, true));
                    nodeIds.add(nodeId);
                }
            }
        }

        return newAreaElements;
    }
}
