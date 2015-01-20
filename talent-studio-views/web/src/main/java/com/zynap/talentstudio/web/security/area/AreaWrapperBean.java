package com.zynap.talentstudio.web.security.area;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.security.areas.Area;
import com.zynap.talentstudio.security.areas.AreaElement;
import com.zynap.talentstudio.security.areas.NodeTypeAreaElementPredicate;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * User: amark
 * Date: 20-Mar-2005
 * Time: 11:28:56
 */
public class AreaWrapperBean implements Serializable {


    /**
     * Constructor.
     *
     * @param area
     */
    public AreaWrapperBean(Area area, Set<AreaElement> areaElements) {
        this.area = area;
        this.assignedAreaElements = new ArrayList(areaElements);
        this.positionPopulationId = area.getPositionPopulationId();
        this.subjectPopulationId = area.getSubjectPopulationId();
    }

    /**
     * Get modified area.
     *
     * @return Area
     */
    public Area getModifiedArea() {
        area.assignAreaElements(new HashSet(assignedAreaElements));
        area.setPositionPopulationId(positionPopulationId);
        area.setSubjectPopulationId(subjectPopulationId);
        return area;
    }

    /**
     * Resets area id and area element ids to null.
     * <br> Required if there is a problem adding an area to ensure that hibernate
     * realises that the domain and the node are both new objects to be saved.
     */
    public void resetIds() {
        area.setId(null);
        for (Iterator iterator = area.getAreaElements().iterator(); iterator.hasNext();) {
            AreaElement areaElement = (AreaElement) iterator.next();
            areaElement.setId(null);
        }
    }

    public Long getId() {
        return area.getId();
    }

    public String getComments() {
        return area.getComments();
    }

    public void setComments(String comments) {
        area.setComments(comments);
    }

    public String getLabel() {
        return area.getLabel();
    }

    public void setLabel(String label) {
        area.setLabel(label);
    }

    public void setActive(boolean active) {
        area.setActive(active);
    }

    public boolean isActive() {
        return area.isActive();
    }

    public Long[] getOrgUnitIds() {
        return new Long[0];
    }

    /**
     * Method used by spring when org units are selected.
     * <br> Adds the selected org unit area elements to the assignedAreaElements Collection.
     *
     * @param orgUnitIds The ids of the selected org units (the org units will be in the newOrgUnitAreaElements Collection).
     */
    public void setOrgUnitIds(Long[] orgUnitIds) {
        assignAreaElements(orgUnitIds, OrganisationUnit.class, newOrgUnitAreaElements);
    }

    /**
     * Add org unit to newOrgUnitAreaElements Collection - used for search results.
     *
     * @param organisationUnit
     */
    public void addOrganisationUnit(OrganisationUnit organisationUnit) {
        addAreaElement(organisationUnit, newOrgUnitAreaElements);
    }

    /**
     * Get the org unit search results.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getOrganisationUnits() {
        return newOrgUnitAreaElements;
    }

    /**
     * Clear the org unit search results.
     */
    public void clearOrganisationUnits() {
        newOrgUnitAreaElements.clear();
    }

    /**
     * Clear all org units from assignedAreaElements.
     */
    public void removeOrgUnits() {
        removeAreaElements(OrganisationUnit.class);
    }

    /**
     * Get all organisation unit area elements from assignedAreaElements.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getAssignedOrganisationUnits() {
        return getAreaElements(OrganisationUnit.class, assignedAreaElements);
    }

    public Long[] getPositionIds() {
        return new Long[0];
    }

    public Long getPositionPopulationId() {
        return positionPopulationId;
    }

    public void setPositionPopulationId(Long positionPopulationId) {
        this.positionPopulationId = positionPopulationId;
    }

    public Long getSubjectPopulationId() {
        return subjectPopulationId;
    }

    public void setSubjectPopulationId(Long subjectPopulationId) {
        this.subjectPopulationId = subjectPopulationId;
    }

    /**
     * Method used by spring when position are selected.
     * <br> Adds the selected position area elements to the assignedAreaElements Collection.
     *
     * @param positionIds The ids of the selected positions (the position will be in the newPositionAreaElements Collection).
     */
    public void setPositionIds(Long[] positionIds) {
        assignAreaElements(positionIds, Position.class, newPositionAreaElements);
    }

    /**
     * Add position to newPositionAreaElements Collection - used for search results.
     *
     * @param position
     */
    public void addPosition(Position position) {
        addAreaElement(position, newPositionAreaElements);
    }

    /**
     * Get the position search results.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getPositions() {
        return newPositionAreaElements;
    }

    /**
     * Clear the position search results.
     */
    public void clearPositions() {
        newPositionAreaElements.clear();
    }

    /**
     * Clear all positions from assignedAreaElements.
     */
    public void removePositions() {
        removeAreaElements(Position.class);
    }

    /**
     * Get all position area elements from assignedAreaElements.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getAssignedPositions() {
        return getAreaElements(Position.class, assignedAreaElements);
    }

    public Long[] getSubjectIds() {
        return new Long[0];
    }

    /**
     * Method used by spring when subject are selected.
     * <br> Adds the selected subject area elements to the assignedAreaElements Collection.
     *
     * @param subjectIds The ids of the selected subject (the position will be in the newSubjectAreaElements Collection).
     */
    public void setSubjectIds(Long[] subjectIds) {
        assignAreaElements(subjectIds, Subject.class, newSubjectAreaElements);
    }

    /**
     * Add subject to newSubjectAreaElements Collection - used for search results.
     *
     * @param subject
     */
    public void addSubject(Subject subject) {
        addAreaElement(subject, newSubjectAreaElements);
    }

    /**
     * Get the subject search results.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getSubjects() {
        return newSubjectAreaElements;
    }

    /**
     * Clear the subject search results.
     */
    public void clearSubjects() {
        newSubjectAreaElements.clear();
    }

    /**
     * Clear all subjects from assignedAreaElements.
     */
    public void removeSubjects() {
        removeAreaElements(Subject.class);
    }

    /**
     * Get all subject area elements from assignedAreaElements.
     *
     * @return Collection of AreaElements
     */
    public Collection<AreaElement> getAssignedSubjects() {
        return getAreaElements(Subject.class, assignedAreaElements);
    }

    Area getArea() {
        return area;
    }

    private void removeAreaElements(Class clazz) {
        assignedAreaElements = CollectionUtils.subtract(assignedAreaElements, getAreaElements(clazz, assignedAreaElements));
    }

    private void addAreaElement(Node node, Collection<AreaElement> areaElements) {
        if (node != null) {
            if (CollectionUtils.find(areaElements, new NodePredicate(node)) == null) {
                AreaElement areaElement = new AreaElement(node, false);
                areaElements.add(areaElement);
            }
        }
    }

    private void assignAreaElements(Long[] ids, Class clazz, Collection<AreaElement> areaElements) {
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                Long id = ids[i];

                // get AreaElement from collection that contains search results and add to assigned area elements
                AreaElement areaElement = (AreaElement) CollectionUtils.find(areaElements, new NodeIdAreaElementPredicate(clazz, id));
                if (areaElement != null) {
                    assignAreaElement(areaElement);
                }
            }
        }

        // remove area elements of the specified class whose ids are not in the list from the assigned area elements
        Collection selectedIds = Arrays.asList(ids);
        for (Iterator iterator = assignedAreaElements.iterator(); iterator.hasNext();) {
            AreaElement areaElement = (AreaElement) iterator.next();
            final Node node = areaElement.getNode();
            if (node != null) {
                final boolean classMatch = node.getClass() == clazz;
                if (classMatch && !selectedIds.contains(node.getId())) {
                    iterator.remove();
                }
            }
        }
    }

    private void assignAreaElement(AreaElement areaElement) {
        if (CollectionUtils.find(assignedAreaElements, new NodePredicate(areaElement.getNode())) == null) {
            assignedAreaElements.add(areaElement);
        }
    }

    private Collection<AreaElement> getAreaElements(Class clazz, Collection<AreaElement> areaElements) {
        return CollectionUtils.select(areaElements, new NodeTypeAreaElementPredicate(clazz));
    }

    /**
     * Predicate used to find an areaelement node with the specified id and of the specified class.
     */
    private class NodeIdAreaElementPredicate extends NodeTypeAreaElementPredicate {

        private Long id;

        public NodeIdAreaElementPredicate(Class clazz, Long id) {
            super(clazz);
            this.id = id;
        }

        /**
         * Check if the object matches our criteria.
         *
         * @param object
         * @return true if the object class type is the same and the id matches.
         */
        public boolean evaluate(Object object) {

            final boolean matches = super.evaluate(object);
            final Node node = getNode(object);
            return matches && node != null && node.getId().equals(id);
        }
    }

    /**
     * Predicate used to find a matching areaelement node.
     */
    private class NodePredicate implements Predicate {

        private Node nodeToFind;

        public NodePredicate(Node nodeToFind) {
            this.nodeToFind = nodeToFind;
        }

        /**
         * Check if the object matches our criteria.
         *
         * @param object
         * @return true if object.equals(nodeToFind)
         */
        public boolean evaluate(Object object) {
            AreaElement areaElement = (AreaElement) object;
            final Node node = areaElement.getNode();
            return node != null && node.equals(nodeToFind);
        }
    }

    private Area area;

    private Long positionPopulationId;
    private Long subjectPopulationId;

    /**
     * The list of AreaElements that an Area being updated originally had.
     */
    private Collection<AreaElement> assignedAreaElements = new ArrayList<AreaElement>();

    /**
     * The list of AreaElements containing org units returned by the search.
     */
    private Collection<AreaElement> newOrgUnitAreaElements = new ArrayList<AreaElement>();

    /**
     * The list of AreaElements containing positions returned by the search.
     */
    private Collection<AreaElement> newPositionAreaElements = new ArrayList<AreaElement>();

    /**
     * The list of AreaElements containing subjects returned by the search.
     */
    private Collection<AreaElement> newSubjectAreaElements = new ArrayList<AreaElement>();
}