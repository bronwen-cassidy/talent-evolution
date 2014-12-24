package com.zynap.talentstudio.organisation;

import org.apache.commons.collections.Predicate;

/**
 * User: amark
 * Date: 17-Jun-2005
 * Time: 12:50:46
 * Predicate that selects primary artefact associations.
 */
public class PrimaryArtefactAssociationPredicate implements Predicate {

    /**
     * Checks if association is primary.
     * @param object An ArtefactAssociation
     * @return true if the ArtefactAssociation is primary
     */
    public boolean evaluate(Object object) {
        ArtefactAssociation association = (ArtefactAssociation) object;
        return association.isPrimary();
    }
}
