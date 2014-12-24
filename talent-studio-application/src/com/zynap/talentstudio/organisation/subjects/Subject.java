package com.zynap.talentstudio.organisation.subjects;

import com.zynap.domain.admin.User;
import com.zynap.domain.admin.UserType;
import com.zynap.domain.Auditable;
import com.zynap.talentstudio.CoreDetail;
import com.zynap.talentstudio.objectives.ObjectiveSet;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.organisation.positions.Position;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that represents a subject in the system.
 *
 * @author amark
 */
public class Subject extends Node implements Auditable {

    /**
     * default constructor.
     */
    public Subject() {
    }

    /**
     * Convenient constructor.
     *
     * @param coreDetail
     */
    public Subject(CoreDetail coreDetail) {
        this(null, coreDetail, null);
    }

    /**
     * Minimal constructor.
     *
     * @param nodeId
     * @param coreDetail
     */
    public Subject(Long nodeId, CoreDetail coreDetail) {
        this(nodeId, coreDetail, null);
    }

    /**
     * Full constructor.
     *
     * @param nodeId
     * @param coreDetail
     * @param dateOfBirth
     */
    public Subject(Long nodeId, CoreDetail coreDetail, Date dateOfBirth) {
        setId(nodeId);
        this.coreDetail = coreDetail;
        this.dateOfBirth = dateOfBirth;
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

    public void setFirstName(String firstName) {
        getCoreDetail().setFirstName(firstName);
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

    public void setPrefGivenName(String prefGivenName) {
        getCoreDetail().setPrefGivenName(prefGivenName);
    }

    public void setEmail(String email) {
        getCoreDetail().setContactEmail(email);
    }

    public String getEmail() {
        return getCoreDetail().getContactEmail();
    }

    public void setTelephone(String telephone) {
        getCoreDetail().setContactTelephone(telephone);
    }

    public String getTelephone() {
        return getCoreDetail().getContactTelephone();
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public User getUser() {
        if (user != null)
            user.setUserType(UserType.SUBJECT);

        return user;
    }

    public void setUser(User user) {
        if (user != null) {
            user.setUserType(UserType.SUBJECT.toString());
            user.setCoreDetail(getCoreDetail());
        }

        this.user = user;
    }

    public CoreDetail getCoreDetail() {
        if (coreDetail == null) coreDetail = new CoreDetail();
        return coreDetail;
    }

    public void setCoreDetail(CoreDetail coreDetail) {
        this.coreDetail = coreDetail;
    }

    /**
     * Clear the list of current subject associations and add the ones in the collection passed in to the underlying collection.
     *
     * @param newSubjectAssociations Collection of {@link SubjectAssociation}s
     */
    public void assignNewSubjectAssociations(Collection<ArtefactAssociation> newSubjectAssociations) {
        getSubjectAssociations().clear();
        if (newSubjectAssociations != null) {
            for (Iterator iterator = newSubjectAssociations.iterator(); iterator.hasNext();) {
                SubjectAssociation subjectAssociation = (SubjectAssociation) iterator.next();
                addSubjectAssociation(subjectAssociation);
            }
        }
    }

    /**
     * Get the subject associations.
     *
     * @return Collection of {@link SubjectAssociation}s
     */
    public Set<SubjectCommonAssociation> getSubjectAssociations() {
        return this.subjectAssociations;
    }

    /**
     * Set the subject associations.
     *
     * @param subjectAssociations Collection of {@link SubjectAssociation}s
     */
    public void setSubjectAssociations(Set<SubjectCommonAssociation> subjectAssociations) {
        this.subjectAssociations = subjectAssociations;
    }

    /**
     * Add subject to subject associations.
     * <br> Sets subject of association to be the instance of the Subject the association is added to.
     *
     * @param subjectAssociation The SubjectAssociation
     */
    public void addSubjectAssociation(SubjectAssociation subjectAssociation) {
        subjectAssociation.setSource(this);
        this.subjectAssociations.add(subjectAssociation);
    }

    /**
     * Can the subject login.
     *
     * @return true if the subject has an associated user with login info.
     */
    public boolean isCanLogIn() {
        return (user != null && user.getLoginInfo().logsIn());
    }

    /**
     * Has the login details of the user associated with the subject changed.
     *
     * @return true if the login details of the user associated with the subject changed.
     */
    public boolean isLoginChange() {
        return (user != null && user.getLoginInfo() != null && user.getLoginInfo().isLoginChange());
    }

    public Collection getAllAssignedRoles() {

        if (user != null && user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            return user.getUserRoles();
        }

        return null;
    }

    public Collection getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(Collection questionnaires) {
        this.questionnaires = questionnaires;
    }

    public Collection<SubjectPrimaryAssociation> getSubjectPrimaryAssociations() {
        return subjectPrimaryAssociations;
    }

    public void setSubjectPrimaryAssociations(Collection<SubjectPrimaryAssociation> subjectPrimaryAssociations) {
        this.subjectPrimaryAssociations = subjectPrimaryAssociations;
    }

    public Collection getSubjectSecondaryAssociations() {
        return subjectSecondaryAssociations;
    }

    public void setSubjectSecondaryAssociations(Collection<SubjectSecondaryAssociation> subjectSecondaryAssociations) {
        this.subjectSecondaryAssociations = subjectSecondaryAssociations;
    }

    public boolean isHasPicture() {
        return hasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        this.hasPicture = hasPicture;
    }

    public String getPicture() {
        return isHasPicture() ? "true" : null;
    }

    public Collection<ObjectiveSet> getObjectiveSets() {
        return objectiveSets;
    }

    public void setObjectiveSets(Collection<ObjectiveSet> objectiveSets) {
        this.objectiveSets = objectiveSets;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("active", isActive())
                .append("coreDetails", getCoreDetail())
                .append("dateOfBirth", getDateOfBirth())
                .append("currentJobInfo", getCurrentJobInfo())
                .toString();
    }

    public String getLabel() {
        return coreDetail != null ? coreDetail.getName() : null;
    }

    public List<OrganisationUnit> getPrimaryOrganisationUnits() {
        List<OrganisationUnit> organisationUnits = new ArrayList<OrganisationUnit>();
        List<Position> positions = getPrimaryPositions();
        for (Position position : positions) {
            organisationUnits.add(position.getOrganisationUnit());
        }
        return organisationUnits;
    }

    public List<Position> getPrimaryPositions() {
        List<Position> positions = new ArrayList<Position>();
        Collection<SubjectPrimaryAssociation> primaryAssociations = getSubjectPrimaryAssociations();
        for (SubjectPrimaryAssociation primaryAssociation : primaryAssociations) {
            positions.add(primaryAssociation.getPosition());
        }
        return positions;
    }

    public String getPrimaryPositionDisplay() {
        StringBuffer sp = new StringBuffer();
        List<Position> listPosition = getPrimaryPositions();
        int i = 0;
        for (Position p : listPosition) {
            sp.append(p.getLabel());
            if (i < listPosition.size()-1) {
                sp.append(" , ");
            }
            i++;
        }

        return sp.toString();
    }

    public String getCurrentJobInfo() {
        return currentJobInfo;
    }

    public void setCurrentJobInfo(String currentJobInfo) {
        this.currentJobInfo = currentJobInfo;
    }

    public Subject createAuditable() {
        Subject s = new Subject(id, this.coreDetail, dateOfBirth);
        s.setActive(this.active);
        if(user != null) s.setUser(user.createAuditable());
        for (SubjectCommonAssociation subjectAssociation : subjectAssociations) {
            s.addSubjectAssociation(subjectAssociation.createAuditable());    
        }

        Set<NodeExtendedAttribute> auditAttrs = new HashSet<NodeExtendedAttribute>();
        for (NodeExtendedAttribute attr : getExtendedAttributes()) {
            auditAttrs.add(attr.createAuditable());        
        }
        s.setExtendedAttributes(auditAttrs);
        return s;
    }

    public List<Subject> getSubjectManagers() {
        List<Subject> managers = new ArrayList<Subject>();
        Collection<SubjectPrimaryAssociation> primaryAssociations = getSubjectPrimaryAssociations();
        for (SubjectCommonAssociation association : primaryAssociations) {
            final Position position = association.getPosition();
            Position parent = position.getParent();
            if (parent != null) {
                final Collection<SubjectPrimaryAssociation> managerPrimaryAssociations = parent.getSubjectPrimaryAssociations();
                for (SubjectPrimaryAssociation primaryAssociation : managerPrimaryAssociations) {
                    managers.add(primaryAssociation.getSubject());
                }
            }
        }
        return managers;
    }

    public List<User> getManagers() {
        List<User> managers = new ArrayList<User>();
        Collection<SubjectPrimaryAssociation> primaryAssociations = getSubjectPrimaryAssociations();
        for (SubjectCommonAssociation association : primaryAssociations) {
            final Position position = association.getPosition();
            Position parent = position.getParent();
            if (parent != null) {
                final Collection<SubjectPrimaryAssociation> managerPrimaryAssociations = parent.getSubjectPrimaryAssociations();
                for (SubjectPrimaryAssociation primaryAssociation : managerPrimaryAssociations) {
                    final Subject subject = primaryAssociation.getSubject();
                    if (subject != null && subject.getUser() != null) {
                        managers.add(subject.getUser());
                    }
                }
            }
        }
        return managers;
    }

    /**
     * nullable persistent field.
     */
    private Date dateOfBirth;

    private String currentJobInfo;

    /**
     * persistent field.
     */
    private User user;

    /**
     * persistent field.
     */
    private Set<SubjectCommonAssociation> subjectAssociations = new HashSet<SubjectCommonAssociation>();

    /**
     * persistent field.
     */
    private Collection<SubjectPrimaryAssociation> subjectPrimaryAssociations = new LinkedHashSet<SubjectPrimaryAssociation>();

    /**
     * persistent field.
     */
    private Collection<SubjectSecondaryAssociation> subjectSecondaryAssociations = new HashSet<SubjectSecondaryAssociation>();

    /**
     * persistent field.
     */
    private CoreDetail coreDetail;

    /**
     * persistent field.
     */
    private Collection questionnaires;

    private boolean hasPicture;

    /**
     * persistent field.
     */
    private Collection<ObjectiveSet> objectiveSets = new LinkedHashSet<ObjectiveSet>();
}
