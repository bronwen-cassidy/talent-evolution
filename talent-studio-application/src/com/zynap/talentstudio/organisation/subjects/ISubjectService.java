/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.common.IZynapService;
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
public interface ISubjectService extends IZynapService {

    /**
     * Check access to the subject and its associated positions.
     * @param subject The subject
     */
    void checkAccess(Subject subject);

    /**
     * Search for subjects.
     *
     * @param principalId The id of the user searching for subjects
     * @param query The SubjectSearchQuery containing all the parameters
     * @return Collection of Subjects
     * @throws TalentStudioException
     */
    List search(Long principalId, SubjectSearchQuery query) throws TalentStudioException;

    List<SubjectDTO> searchSubjects(Long userId, SubjectSearchQuery searchQuery) throws TalentStudioException;

    /**
     * Find subject by user id.
     * <br> Works for both active and inactive subjects.
     *
     * @param userId The user id
     * @return Subject
     * @throws TalentStudioException
     */
    Subject findByUserId(Long userId) throws TalentStudioException;

    /**
     * Find subject by id.
     * <br> Works for both active and inactive subjects.
     *
     * @param id The subject id
     * @return Subject
     * @throws TalentStudioException
     */
    Subject findById(Long id) throws TalentStudioException;

    /**
     * Find the team of the subject for the given user.
     *
     * @param userId
     * @return all the people that report to this person's position. An empty collection is returned if there are none.
     * @throws TalentStudioException
     */
    Collection<SubjectDTO> findTeam(Long userId) throws TalentStudioException;

    Node findNodeById(Long id) throws TalentStudioException;

    boolean checkNodeAccess(Node node, Long userId);

    boolean checkNodeViewAccess(Node node, Long userId);

    Collection<SubjectDTO> findAllSubjectDTOs();

    SubjectPicture findPicture(Long id);

    void create(Subject newSubject, SubjectPicture picture) throws TalentStudioException;

    void update(Subject subject, SubjectPicture modifiedSubjectPicture) throws TalentStudioException;

    String getMyTeamViewAttributeLabel(Long userId);

    NodeExtendedAttribute getTeamViewAttribute(Long managerUserId, Long subjectId);

    void updateCurrentJobInfo(Long subjectId);
}
