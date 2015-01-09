/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.domain.IDomainObject;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;

import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public interface ISubjectDao extends IFinder, IModifiable {

    /**
     * Find subject by user id.
     * <br> Works for both active and inactive subjects.
     *
     * @param userId
     * @return IDomainObject
     * @throws TalentStudioException
     */
    Subject findByUserId(Long userId) throws TalentStudioException;

    /**
     * Check access to the subject and the positions associated to it.
     * 
     * @param subject The subject
     * @param userId The user id
     * @param viewSubjectPermitId The permit the user must have in order to have access to the subject
     * @param viewPositionPermitId The permit the user must have in order to have access to the positions
     */
    void checkAccess(Subject subject, Long userId, Long viewSubjectPermitId, Long viewPositionPermitId);

    /**
     * Finds the people who report to the given user's position.
     *
     * @param userId
     * @return all of the people reporting to the given user.
     * @throws TalentStudioException
     */
    List<SubjectDTO> findTeam(Long userId) throws TalentStudioException;

    boolean checkNodeAccess(Node node, Long userId, Long viewSubjectPermitId);

    boolean checkNodeViewAccess(Node node, Long userId, Long viewSubjectPermitId);

    Collection<SubjectDTO> findAllSubjectDTOs();

    SubjectPicture findPicture(Long id);

    void createPicture(SubjectPicture picture);

    void updatePicture(SubjectPicture picture);

    void deletePicture(SubjectPicture picture);

    String getMyTeamViewAttributeLabel(Long userId);

    NodeExtendedAttribute getTeamViewAttribute(Long managerUserId, Long subjectId);

    void updateCurrentJobInfo(Long subjectId);
}
