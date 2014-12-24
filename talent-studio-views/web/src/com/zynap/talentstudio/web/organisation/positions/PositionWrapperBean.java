package com.zynap.talentstudio.web.organisation.positions;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.ArtefactAssociationHelper;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.organisation.subjects.SubjectAssociation;
import com.zynap.talentstudio.web.organisation.NodeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.associations.ArtefactAssociationWrapperBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
* Wraps a position and provides methods for the spring forms
*
* @author jsuiras
* @since 07-Feb-2005 14:11:34
* @version 0.1
*/
public class PositionWrapperBean extends NodeWrapperBean implements Serializable {

    /**
     * Constructor.
     *
     * @param p The Position
     */
    public PositionWrapperBean(Position p) {

        super(p);

        this.position = p;

        Set<PositionAssociation> sourceAssociations = position.getSourceAssociations();
        Collection<PositionAssociation> targetAssociations = position.getTargetAssociations();

        final PositionAssociation primaryArtefactAssociation = ArtefactAssociationHelper.getPrimaryAssociation(sourceAssociations);
        if (primaryArtefactAssociation != null) primaryAssociation = new ArtefactAssociationWrapperBean(primaryArtefactAssociation);

        primaryTargetAssociations = buildWrappedAssociations(ArtefactAssociationHelper.getPrimaryAssociations(targetAssociations));

        secondaryTargetAssociations = buildWrappedAssociations(ArtefactAssociationHelper.getSecondaryAssociations(targetAssociations));

        // build collection of wrapped secondary pos-to-pos associations
        secondaryAssociations = buildWrappedAssociations(ArtefactAssociationHelper.getSecondaryAssociations(sourceAssociations));

        // build collection of wrapped pos-to-subject associations
        subjectPrimaryAssociations = buildWrappedAssociations(ArtefactAssociationHelper.getPrimaryAssociations(position.getSubjectAssociations()));
        subjectSecondaryAssociations = buildWrappedAssociations(ArtefactAssociationHelper.getSecondaryAssociations(position.getSubjectAssociations()));
    }

    public Position getModifiedPosition(User user) {

        DynamicAttributesHelper.assignAttributeValuesToNode(getWrappedDynamicAttributes(), position);

        // only set position associations for non-default positions
        if (!position.isDefault()) {

            final Set<ArtefactAssociation> assignedAssociations = new HashSet<ArtefactAssociation>();
            assignModifiedAssociations(assignedAssociations, secondaryAssociations);

            if (primaryAssociation != null) {

                // add modified primary association to list of associations and set position parent
                ArtefactAssociation modifiedPrimaryAssociation = primaryAssociation.getModifiedAssociation();
                assignedAssociations.add(modifiedPrimaryAssociation);
                position.setParentId(modifiedPrimaryAssociation.getTargetId());
            }

            position.assignNewSourceAssociations(assignedAssociations);
        }
        // assign subject associations for any position including the default
        final Set<ArtefactAssociation> assignedSubjectAssociations = new HashSet<ArtefactAssociation>();
        assignModifiedAssociations(assignedSubjectAssociations, subjectPrimaryAssociations);
        assignModifiedAssociations(assignedSubjectAssociations, subjectSecondaryAssociations);
        position.assignNewSubjectAssociations(assignedSubjectAssociations);

        setNodeAudit(user);
        return position;
    }
    
    /**
     * Resets position id and associations ids.
     */
    protected void resetIdsInternal() {
        if (primaryAssociation != null) primaryAssociation.resetId();
        for (Iterator iterator = secondaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            artefactAssociationWrapperBean.resetId();
        }

        for (Iterator iterator = subjectPrimaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            artefactAssociationWrapperBean.resetId();
        }

        for (Iterator iterator = subjectSecondaryAssociations.iterator(); iterator.hasNext();) {
            ArtefactAssociationWrapperBean artefactAssociationWrapperBean = (ArtefactAssociationWrapperBean) iterator.next();
            artefactAssociationWrapperBean.resetId();
        }
    }

    public ArtefactAssociationWrapperBean getPrimaryAssociation() {
        return primaryAssociation;
    }

    public List<ArtefactAssociationWrapperBean> getSecondaryAssociations() {
        return secondaryAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getSecondarySourceAssociations() {
        return secondaryAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getPrimaryTargetAssociations() {
        return primaryTargetAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getSecondaryTargetAssociations() {
        return secondaryTargetAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getPrimarySourceAssociations() {
        List<ArtefactAssociationWrapperBean> result = new ArrayList<ArtefactAssociationWrapperBean>();
        if (primaryAssociation != null) {
            result.add(primaryAssociation);
        }
        return result;
    }

    public void setPrimaryAssociation(PositionAssociation pa) {
        pa.setSource(this.position);
        this.primaryAssociation = new ArtefactAssociationWrapperBean(pa);
    }

    public void addNewPrimaryAssociation() {
        setPrimaryAssociation(new PositionAssociation(new LookupValue(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC), new Position()));
    }

    public void addNewSecondaryAssociation() {
        final LookupValue qualifier = new LookupValue(null, ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC, "Secondary", "");
        final PositionAssociation positionAssociation = new PositionAssociation(qualifier, new Position());
        positionAssociation.setSource(this.position);
        secondaryAssociations.add(new ArtefactAssociationWrapperBean(positionAssociation));
    }

    public void addNewSubjectPrimaryAssociation() {
        final LookupValue qualifier = new LookupValue(null, ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC, "Person Primary Association", "");
        final SubjectAssociation subjectAssociation = new SubjectAssociation(qualifier, new Subject(), position);
        subjectPrimaryAssociations.add(new ArtefactAssociationWrapperBean(subjectAssociation));
    }

    public void addNewSubjectSecondaryAssociation() {
        final LookupValue qualifier = new LookupValue(null, ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC, "Person Secondary Association", "");
        final SubjectAssociation subjectAssociation = new SubjectAssociation(qualifier, new Subject(), position);
        subjectSecondaryAssociations.add(new ArtefactAssociationWrapperBean(subjectAssociation));
    }

    public List<ArtefactAssociationWrapperBean> getSubjectPrimaryAssociations() {
        return subjectPrimaryAssociations;
    }

    public List<ArtefactAssociationWrapperBean> getSubjectSecondaryAssociations() {
        return subjectSecondaryAssociations;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Is the wrapped position the default position.
     *
     * @return True if this is the default position.
     */
    public boolean isDefault() {
        return getPosition().isDefault();
    }

    public String getTitle() {
        return position.getTitle();
    }

    public Node getNode() {
        return this.position;
    }

    public String getLabel() {
        return position.getTitle();
    }

    public void setLabel(String label) {
        position.setTitle(label);
    }

    public Long getParentId() {
        return position.getParent().getId();
    }

    private List<ArtefactAssociationWrapperBean> buildWrappedAssociations(final Collection input) {

        List<ArtefactAssociationWrapperBean> output = new ArrayList<ArtefactAssociationWrapperBean>();
        if (input != null) {
            for (Iterator iterator = input.iterator(); iterator.hasNext();) {
                ArtefactAssociation association = (ArtefactAssociation) iterator.next();
                output.add(new ArtefactAssociationWrapperBean(association));
            }
        }

        return output;
    }

    public List<ArtefactAssociationWrapperBean> getPrimaryAssociations() {
        List<ArtefactAssociationWrapperBean> primaryAssociations = new ArrayList<ArtefactAssociationWrapperBean>();
        primaryAssociations.add(primaryAssociation);
        return primaryAssociations;
    }

    public String getAssocInfo() {
        return position.getCurrentHolderInfo();
    }

    public void setDecendantIds(List<Long> decendantIds) {
        this.decendantIds = decendantIds;
    }

    public List<Long> getDecendantIds() {
        return decendantIds;
    }

    private final Position position;
    private ArtefactAssociationWrapperBean primaryAssociation;
    private List<ArtefactAssociationWrapperBean> secondaryAssociations;

    // collections that hold the position to subject associations - not currently modified
    private List<ArtefactAssociationWrapperBean> subjectPrimaryAssociations;
    private List<ArtefactAssociationWrapperBean> subjectSecondaryAssociations;
    private List<ArtefactAssociationWrapperBean> primaryTargetAssociations;
    private List<ArtefactAssociationWrapperBean> secondaryTargetAssociations;
    private List<Long> decendantIds;
}
