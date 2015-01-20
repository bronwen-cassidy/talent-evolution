/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web.organisation.subjects;

import com.zynap.common.util.UploadedFile;
import com.zynap.domain.admin.LoginInfo;
import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserType;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.objectives.Objective;
import com.zynap.talentstudio.objectives.ObjectiveConstants;
import com.zynap.talentstudio.objectives.ObjectiveSetDto;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectCommonAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectPicture;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.security.roles.Role;
import com.zynap.talentstudio.web.organisation.GroupMapKey;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.security.admin.UserWrapperBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 *          Wrapper bean for Subject.
 */
public final class SubjectWrapperBean extends NodeWrapperBean {

    /**
     * Constructor.
     *
     * @param subject The subject
     */
    public SubjectWrapperBean(Subject subject) {
        this(subject, subject.getAllAssignedRoles());
    }

    /**
     * Constructor.
     *
     * @param subject  The subject
     * @param allRoles roles
     */
    public SubjectWrapperBean(Subject subject, Collection<Role> allRoles) {

        super(subject);

        if (subject.getSubjectAssociations() != null) {
            Collection<SubjectCommonAssociation> associations = new ArrayList<SubjectCommonAssociation>(subject.getSubjectAssociations());
            for (SubjectCommonAssociation subjectAssociation : associations) {
                addSubjectAssociation(subjectAssociation);
            }
        }
        this.subject = subject;
        userWrapper = new UserWrapperBean(this.subject.getUser(), allRoles);
    }

    /**
     * Resets subject id, core detail id, login info id and (if there is a user) the user id.
     * <br/> Resets associations ids.
     */
    protected void resetIdsInternal() {

        getCoreDetail().setId(null);

        for (Iterator iterator = subjectPrimaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            artefactAssociationWrapperBean.resetId();
        }

        for (Iterator iterator = subjectSecondaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            artefactAssociationWrapperBean.resetId();
        }

        resetUserIds();
    }

    public boolean isLoginChange() {
        return getLoginInfo().isLoginChange();
    }

    public void resetUserIds() {
        if (userWrapper != null) userWrapper.resetIds();
    }

    public List<ArtefactAssociationWrapperBean> getSubjectPrimaryAssociations() {
        return subjectPrimaryAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getSubjectSecondaryAssociations() {
        return subjectSecondaryAssociations;
    }

    public Collection getSubjectAssociations() {
        return subject.getSubjectAssociations();
    }

    public void addSubjectAssociation(SubjectCommonAssociation association) {
        final ArtefactAssociationWrapperBean associationWrapperBean = new ArtefactAssociationWrapperBean(association);
        if (association.isSecondary()) {
            subjectSecondaryAssociations.add(associationWrapperBean);
        } else {
            subjectPrimaryAssociations.add(associationWrapperBean);
        }
    }

    public boolean isCanLogIn() {
        return getLoginInfo().logsIn();
    }

    public Collection getUserRoles() {
        return userWrapper != null ? userWrapper.getUserRoles() : null;
    }

    public String getComments() {
        return subject.getComments();
    }

    public void setComments(String comments) {
        subject.setComments(comments);
    }

    public String getTitle() {
        return getCoreDetail().getTitle();
    }

    public void setTitle(String title) {
        getCoreDetail().setTitle(title);
    }

    public String getFirstName() {
        return getCoreDetail().getFirstName();
    }

    public void setFirstName(String name) {
        getCoreDetail().setFirstName(name);
    }

    public String getSecondName() {
        return getCoreDetail().getSecondName();
    }

    public void setSecondName(String secondName) {
        getCoreDetail().setSecondName(secondName);
    }

    public String getPrefGivenName() {
        return getCoreDetail().getPrefGivenName();
    }

    public void setPrefGivenName(String name) {
        getCoreDetail().setPrefGivenName(name);
    }

    public Date getDateOfBirth() {
        return subject.getDateOfBirth();
    }

    public void setDateOfBirth(Date dateOfBirth) {
        subject.setDateOfBirth(dateOfBirth);
    }

    public String getAssocInfo() {
        return subject.getCurrentJobInfo();
    }

    public Long getUserId() {
        return userWrapper.getId();
    }

    public Long getParentId() {
        return null;
    }

    public Collection getAccessRoles() {
        return userWrapper.getAccessRoles();
    }

    public void setAccessRoleIds(Long[] ids) {
        userWrapper.setAccessRoleIds(ids);
    }

    public Long[] getAccessRoleIds() {
        return userWrapper.getAccessRoleIds();
    }

    public UserWrapperBean getUser() {
        return userWrapper;
    }

    public void addNewPrimaryAssociation() {
        final LookupValue qualifier = new LookupValue(null, ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC, "Primary", "");
        final SubjectAssociation subjectAssociation = new SubjectAssociation(qualifier, this.subject, new Position());
        addSubjectAssociation(subjectAssociation);
    }

    public void addNewSecondaryAssociation() {
        final LookupValue qualifier = new LookupValue(null, ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC, "Secondary", "");
        final SubjectAssociation subjectAssociation = new SubjectAssociation(qualifier, this.subject, new Position());
        addSubjectAssociation(subjectAssociation);
    }

    public boolean isHasPicture() {
        return subject.isHasPicture();
    }

    public Subject getModifiedSubject(User userlogged) {

        DynamicAttributesHelper.assignAttributeValuesToNode(getWrappedDynamicAttributes(), subject);

        subject.assignNewSubjectAssociations(getModifiedAssociations());
        if (userWrapper != null) {
            final LoginInfo loginInfo = userWrapper.getLoginInfo();
            if (loginInfo != null && loginInfo.logsIn()) {
                User user = userWrapper.getModifiedUser();
                subject.setUser(user);
            }
        }
        setNodeAudit(userlogged);
        return subject;
    }

    public SubjectPicture getModifiedSubjectPicture() {
        final SubjectPicture temp = new SubjectPicture();
        if (pictureFile != null && !isDirtyFile()) {
            temp.setPicture(pictureFile.getBlobValue());
        }
        return temp;
    }

    public Node getNode() {
        return this.subject;
    }

    public String getLabel() {
        return subject.getLabel();
    }

    public String getFullName() {
        return getCoreDetail().getName();
    }

    public void setLabel(String label) {
        subject.setLabel(label);
    }

    public String getContactTelephone() {
        return getCoreDetail().getContactTelephone();
    }

    public void setContactTelephone(String value) {
        getCoreDetail().setContactTelephone(value);
    }

    public String getContactEmail() {
        return getCoreDetail().getContactEmail();
    }

    public void setContactEmail(String value) {
        getCoreDetail().setContactEmail(value);
    }

    public Subject getOriginalSubject() {
        return subject;
    }

    public UserWrapperBean getUserWrapper() {
        return userWrapper;
    }

    public LoginInfo getLoginInfo() {
        return userWrapper.getLoginInfo();
    }

    public CoreDetail getCoreDetail() {
        return subject.getCoreDetail();
    }

    public String getType() {
        return subject == null ? UserType.USER.toString() : UserType.SUBJECT.toString();
    }

    public UploadedFile getFile() {
        return pictureFile;
    }

    public void setFile(UploadedFile pictureFile) {
        if (pictureFile.isValid()) {
            this.pictureFile = pictureFile;
        }
    }

    public boolean isDirtyFile() {
        return pictureFile != null && !pictureFile.isValid();
    }

    public boolean isNewUser() {
        return (getUserId() == null);
    }

    private Collection<ArtefactAssociation> getModifiedAssociations() {

        final Set<ArtefactAssociation> assignedAssociations = new HashSet<ArtefactAssociation>();

        // add primary associations
        assignModifiedAssociations(assignedAssociations, subjectPrimaryAssociations);

        // add secondary associations
        assignModifiedAssociations(assignedAssociations, subjectSecondaryAssociations);

        return assignedAssociations;
    }

    /**
     * Gets the subject's current completed objective set, general people browsing the organisation cannot view objectives until they are completed.
     * The exception is an administrator user who gets to see objectives in any state.
     *
     * @return List of Objectives is there are any completed ones (both the objectives and assessments are completed).
     */
    public Collection<Objective> getCompletedObjectives() {
        return (currentObjectiveSet != null && (isAdministratorLoggedIn() || currentObjectiveSet.isComplete())) ? currentObjectives : new ArrayList<Objective>();
    }

    public Collection<Objective> getCurrentObjectives() {
        return currentObjectives;
    }

    public boolean isShouldDisplayAdminView() {
        return administratorLoggedIn && isHasObjectives();
    }

    /**
     * @return true if there is at least one archived objective set.
     */
    public boolean isHasArchivedObjectives() {
        return hasArchivedObjectives;
    }

    public void setHasArchivedObjectives(boolean hasArchivedObjectives) {
        this.hasArchivedObjectives = hasArchivedObjectives;
    }

    /**
     * Checks if the subject has objectives that can be edited.
     * <p/>
     * <ol>
     * <li>false: if the subjects current objective set is null</li>
     * <li>false: if the objective set status is pending, this is the personal view and the action group is waiting on the manager's review/response
     * </li>
     * <li>false: if the objective set status is pending this is not the personal view
     * and the action group we are waiting on is individual's response/review
     * </li>
     * <li>true: for the inverse of the above</li>
     * </ol>
     *
     * @return true / false
     * @see com.zynap.talentstudio.objectives.ObjectiveSet#getActionGroup()
     * @see com.zynap.talentstudio.objectives.ObjectiveConstants#ACTION_GROUP_MANAGER
     */
    public boolean isHasApprovableObjectives() {
        // returns only the open or pending objective set, null if non found
        if (currentObjectiveSet == null) return false;
        else if (currentObjectiveSet.isOpen() && getId().equals(currentObjectiveSet.getSubjectId())) return true;
        else if (isPersonalView() && currentObjectiveSet.isIndividualPending()) return true;
        else if (!isPersonalView() && currentObjectiveSet.isManagerPending()) return true;
        return false;
    }

    public boolean isPending() {
        return currentObjectiveSet != null && currentObjectiveSet.isPending();
    }

    public ObjectiveSetDto getCurrentObjectiveSet() {
        return currentObjectiveSet;
    }

    public void setCurrentObjectiveSet(ObjectiveSetDto currentObjectiveSet) {
        this.currentObjectiveSet = currentObjectiveSet;
    }

    public boolean isIndividualPending() {
        return currentObjectiveSet != null && currentObjectiveSet.isIndividualPending();
    }

    public boolean isManagerPending() {
        return currentObjectiveSet != null && currentObjectiveSet.isManagerPending();
    }

    public boolean isApproved() {
        return currentObjectiveSet != null && currentObjectiveSet.isApproved();
    }

    public boolean isStatusApproved() {
        return currentObjectiveSet != null && ObjectiveConstants.STATUS_APPROVED.equals(currentObjectiveSet.getStatus());
    }

    public boolean isOpen() {
        return currentObjectiveSet != null && currentObjectiveSet.isOpen();
    }

    public boolean isComplete() {
        return currentObjectiveSet != null && currentObjectiveSet.isComplete();
    }

    public Date getLastModifiedDate() {
        return currentObjectiveSet == null ? null : currentObjectiveSet.getLastModifiedDate();
    }

    public void resetPasswords() {
        userWrapper.clearPasswordFields();
    }

    public void setHasPublishedObjectives(boolean hasPublishedObjectives) {
        this.hasPublishedObjectives = hasPublishedObjectives;
    }

    public boolean isHasPublishedObjectives() {
        return hasPublishedObjectives;
    }

    public boolean isHasApprovableAssessments() {
        return hasApprovableAssessments;
    }

    public void setHasApprovableAssessments(boolean value) {
        hasApprovableAssessments = value;
    }

    public void setPersonalView(boolean personalView) {
        this.personalView = personalView;
    }

    public boolean isPersonalView() {
        return personalView;
    }

    public boolean isAdministratorLoggedIn() {
        return administratorLoggedIn;
    }

    public void setAdministratorLoggedIn(boolean administratorLoggedIn) {
        this.administratorLoggedIn = administratorLoggedIn;
    }

    public void setAppraisals(List<QuestionnaireDTO> appraisals) {
        this.appraisals = appraisals;
    }

    public List<QuestionnaireDTO> getAppraisals() {
        return appraisals;
    }

    public void setInfoForms(Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms) {
        this.groupedInfoForms = groupedInfoForms;
    }

    public Map<GroupMapKey, List<QuestionnaireDTO>> getGroupedInfoForms() {
        return groupedInfoForms;
    }

    public boolean isHasObjectives() {
        return currentObjectives != null && !currentObjectives.isEmpty();
    }

    public void setCurrentObjectives(List<Objective> currentObjectives) {
        this.currentObjectives = currentObjectives;
    }

    public void setSecurityDomains(List<SecurityDomain> securityDomains) {

        this.securityDomains = securityDomains;
    }

    public List<SecurityDomain> getSecurityDomains() {
        return securityDomains;
    }

    public int getNumAppraisals() {
        return appraisals.size();
    }

    public void setProgressReports(List<SubjectProgressReportWrapper> progressReports) {
        this.progressReports = progressReports;
    }

    public List<SubjectProgressReportWrapper> getProgressReports() {
        return progressReports;
    }

    /**
     * are we on my details or being browsed
     */
    private boolean personalView;
    private final Subject subject;
    private final UserWrapperBean userWrapper;

    private final List<ArtefactAssociationWrapperBean> subjectPrimaryAssociations = new ArrayList<ArtefactAssociationWrapperBean>();
    private final List<ArtefactAssociationWrapperBean> subjectSecondaryAssociations = new ArrayList<ArtefactAssociationWrapperBean>();
    private UploadedFile pictureFile;
    private boolean hasPublishedObjectives;
    private boolean hasArchivedObjectives;
    private ObjectiveSetDto currentObjectiveSet;
    private List<Objective> currentObjectives;
    private boolean administratorLoggedIn;
    private List<QuestionnaireDTO> appraisals = new ArrayList<QuestionnaireDTO>();
    private Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms;
    private boolean hasApprovableAssessments;
    private List<SecurityDomain> securityDomains;
    private List<SubjectProgressReportWrapper> progressReports;
}
