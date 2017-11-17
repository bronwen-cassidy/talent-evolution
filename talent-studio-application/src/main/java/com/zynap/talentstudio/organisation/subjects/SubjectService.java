/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.organisation.subjects;

import com.zynap.domain.IDomainObject;
import com.zynap.domain.admin.User;
import com.zynap.domain.orgbuilder.SubjectSearchQuery;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.IAnalysisService;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.common.DefaultService;
import com.zynap.talentstudio.common.IFinder;
import com.zynap.talentstudio.common.IModifiable;
import com.zynap.talentstudio.common.SecurityConstants;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.IQuestionnaireDao;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.security.permits.IPermitManagerDao;
import com.zynap.talentstudio.security.users.IUserDao;
import com.zynap.talentstudio.security.users.LoginInfoValidator;
import com.zynap.talentstudio.workflow.IWorkflowAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;


/**
 * Implementation of ISubjectService.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class SubjectService extends DefaultService implements ISubjectService {

    /**
     * Check access to the subject and its associated positions.
     *
     * @param subject The subject
     */
    public void checkAccess(Subject subject) {
        final Long id = UserSessionFactory.getUserSession().getId();
        subjectDao.checkAccess(subject, id, getViewSubjectPermitId(), getViewPositionPermitId());
    }


    public boolean checkNodeAccess(Node node, Long userId) {
        return subjectDao.checkNodeAccess(node, userId, getViewSubjectPermitId());
    }

    public boolean checkNodeViewAccess(Node node, Long userId) {
        return subjectDao.checkNodeViewAccess(node, userId, getViewSubjectPermitId());
    }

    public List search(Long principalId, SubjectSearchQuery query) throws TalentStudioException {
        Population population = buildPopulation(query);
        return populationEngine.find(population, principalId);
    }

    public List<SubjectDTO> searchSubjects(Long userId, SubjectSearchQuery searchQuery) throws TalentStudioException {
        Population population = buildPopulation(searchQuery);
        return populationEngine.findSubjects(population, userId);
    }

    public Subject findByUserId(Long userId) throws TalentStudioException {
        return subjectDao.findByUserId(userId);
    }

    public Subject findById(Long id) throws TalentStudioException {
        return (Subject) super.findById(id);
    }

    /**
     * Find the team of the subject for the given user.
     *
     * @param userId the logged in user
     * @return all the people that report to this person's position. An empty collection is returned if there are none.
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public Collection<SubjectDTO> findTeam(Long userId) throws TalentStudioException {

        List<SubjectDTO> team = subjectDao.findTeam(userId);
        Collections.sort(team, new Comparator<SubjectDTO>() {
            public int compare(SubjectDTO o1, SubjectDTO o2) {
                return (o1.getSecondName().toUpperCase().compareTo(o2.getSecondName().toUpperCase()));
            }
        });
        return team;
    }

    public Collection<SubjectDTO> findAllSubjectDTOs() {
        return subjectDao.findAllSubjectDTOs();
    }

    public SubjectPicture findPicture(Long id) {
        return subjectDao.findPicture(id);
    }

    /**
     * Creates a new Subject and any photos associated with the subject.
     *
     * @param newSubject the subject to create
     * @param picture    the picture to create if it has data.
     * @throws TalentStudioException
     */
    public void create(Subject newSubject, SubjectPicture picture) throws TalentStudioException {
        boolean create = picture.isHasValue();
        newSubject.setHasPicture(create);
        create(newSubject);
        if (create) {
            picture.setSubjectId(newSubject.getId());
            subjectDao.createPicture(picture);
        }
    }

    /**
     * Updates the subject and updates or creates the subjects picture.
     * Note pictures cannot be deleted unless the subject is deleted therefore this method will never set the
     * subject.setHasPicture to false;
     *
     * @param subject the subject to update
     * @param picture the subject's picture to create or update
     * @throws TalentStudioException
     */
    public void update(Subject subject, SubjectPicture picture) throws TalentStudioException {
        boolean update = subject.isHasPicture() && picture.isHasValue();
        boolean create = !subject.isHasPicture() && picture.isHasValue();
        if(create) subject.setHasPicture(true);
        update(subject);
        picture.setSubjectId(subject.getId());
        if (create) {
            subjectDao.createPicture(picture);
        } else if (update) {
            subjectDao.updatePicture(picture);
        }
    }

    public String getMyTeamViewAttributeLabel(Long userId) {
        return subjectDao.getMyTeamViewAttributeLabel(userId);
    }

    public NodeExtendedAttribute getTeamViewAttribute(Long managerUserId, Long subjectId) {
        return subjectDao.getTeamViewAttribute(managerUserId, subjectId);
    }

    public void updateCurrentJobInfo(Long subjectId) {
        subjectDao.updateCurrentJobInfo(subjectId);
    }

    public void updateStateInfo(IDomainObject domainObject) {
        updateCurrentJobInfo(domainObject.getId());
    }

    /**
     * Creates a new Subject, checks the user information, and adds them to any questionnaire lists that have
     * a population of all people against them.
     *
     * @param domainObject
     * @throws TalentStudioException
     */
    public void create(IDomainObject domainObject) throws TalentStudioException {
        Subject subject = (Subject) domainObject;
        checkUser(subject);
        subjectDao.create(subject);

        // now add the subject to the participants table for all people populations and questionnaire type of info_form or questionnaire
        Collection questionnaireWorkflows = questionnaireDao.findWorkflowsForPopulation(IPopulationEngine.ALL_PEOPLE_POPULATION_ID);
        for (Iterator iterator = questionnaireWorkflows.iterator(); iterator.hasNext();) {
            QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) iterator.next();
            if (!questionnaireWorkflow.isNotificationBased()) {
                questionnaireDao.addParticipant(questionnaireWorkflow, subject);
            } else if (subject.isCanLogIn()) {
                // only add the subject if they can log in, as this is the only case they can answer a questionnaire
                questionnaireDao.addParticipant(questionnaireWorkflow, subject);
            }
        }
    }

    public void update(IDomainObject domainObject) throws TalentStudioException {
        Subject subject = (Subject) domainObject;
        checkUser(subject);
        subjectDao.update(subject);
    }

    public void disable(IDomainObject domainObject) throws TalentStudioException {
        Subject subject = (Subject) subjectDao.findById(domainObject.getId());
        subject.setActive(false);
        subjectDao.update(subject);
    }


    public void delete(IDomainObject domainObject) throws TalentStudioException {
        subjectDao.delete(domainObject);
        // remove any pending notifications
        workflowAdapter.removeNotifications(domainObject.getId());

        // find the picture
        SubjectPicture picture = null;
        try {
            picture = subjectDao.findPicture(domainObject.getId());
        } catch (Throwable e) {
            // error thrown if subject does not have a picture
            logger.debug("Exception thrown trying to find subject's picture with id of " + domainObject.getId());
        }
        if (picture != null) {
            subjectDao.deletePicture(picture);
        }
    }

    private Population buildPopulation(SubjectSearchQuery query) throws TalentStudioException {
        Population population;
        if (query.getPopulationId() == null)
            population = populationEngine.getAllSubjectsPopulation();
        else
            population = (Population) analysisService.findById(query.getPopulationId());

        population.setForSearching(true);
        population.setOrderColumns(new String[]{SubjectSearchQuery.SECOND_NAME, SubjectSearchQuery.FIRST_NAME});
        query.getPopulationForSearch(population);
        return population;
    }

    private Long getViewPositionPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.POSITION_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    private Long getViewSubjectPermitId() {
        return permitManagerDao.getPermit(SecurityConstants.SUBJECT_CONTENT, SecurityConstants.VIEW_ACTION).getId();
    }

    /**
     * Validate the user's login info (username + password) if present.
     *
     * @param subject The Subject
     * @throws TalentStudioException
     */
    private void checkUser(Subject subject) throws TalentStudioException {
        if (subject.isCanLogIn()) {
            final User user = subject.getUser();

            // is a create if the subject has a user with no id
            LoginInfoValidator.validateLoginInfo(user, userDao, (user.getId() == null));
        }
    }


    public Subject findNodeById(Long id) throws TalentStudioException {
        return subjectDao.findById(Node.class, id);
    }

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setPermitManagerDao(IPermitManagerDao permitManagerDao) {
        this.permitManagerDao = permitManagerDao;
    }

    public void setSubjectDao(ISubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    public void setPopulationEngine(IPopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    public void setAnalysisService(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public void setWorkflowAdapter(IWorkflowAdapter workflowAdapter) {
        this.workflowAdapter = workflowAdapter;
    }

    public void setQuestionnaireDao(IQuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    protected IFinder getFinderDao() {
        return subjectDao;
    }

    protected IModifiable getModifierDao() {
        return subjectDao;
    }

    private IPermitManagerDao permitManagerDao;
    private ISubjectDao subjectDao;
    private IUserDao userDao;
    private IQuestionnaireDao questionnaireDao;
    private IPopulationEngine populationEngine;
    private IAnalysisService analysisService;
    private IWorkflowAdapter workflowAdapter;
}
