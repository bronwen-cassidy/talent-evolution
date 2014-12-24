package com.zynap.talentstudio.organisation;

import com.zynap.talentstudio.organisation.positions.PositionAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectCommonAssociation;
import com.zynap.talentstudio.organisation.subjects.SubjectPrimaryAssociation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 31-Aug-2005
 * Time: 16:37:40
 * To change this template use File | Settings | File Templates.
 */
public final class ArtefactAssociationHelper {

    private ArtefactAssociationHelper() {
    }

    public static Collection<ArtefactAssociation> getPrimaryAssociations(Collection<? extends ArtefactAssociation> input) {
        return CollectionUtils.select(input, new PrimaryArtefactAssociationPredicate());
    }

    public static PositionAssociation getPrimaryAssociation(Collection input) {
        return (PositionAssociation) CollectionUtils.find(input, new PrimaryArtefactAssociationPredicate());
    }

    public static Collection<ArtefactAssociation> getSecondaryAssociations(Collection input) {
        return CollectionUtils.selectRejected(input, new PrimaryArtefactAssociationPredicate());
    }

    public static SubjectCommonAssociation getSubjectPrimaryAssociation(Collection primaryAssociations) {
        SubjectCommonAssociation primaryAssociation;
        if (primaryAssociations == null || primaryAssociations.isEmpty()) return null;
        if (primaryAssociations.size() == 1) primaryAssociation = (SubjectCommonAssociation) primaryAssociations.iterator().next();
        else {
            primaryAssociation = (SubjectPrimaryAssociation) CollectionUtils.find(primaryAssociations, new Predicate() {
                public boolean evaluate(Object object) {
                    SubjectPrimaryAssociation temp = (SubjectPrimaryAssociation) object;
                    return PERMANENT_ASSOCIATION_VALUE_ID.equals(temp.getQualifier().getValueId());
                }
            });
        }
        if(primaryAssociation == null) primaryAssociation = (SubjectCommonAssociation) primaryAssociations.iterator().next();
        return primaryAssociation;
    }

    private static final String PERMANENT_ASSOCIATION_VALUE_ID = "PERMANENT";
}
