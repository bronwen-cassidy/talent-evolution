package com.zynap.talentstudio.security.areas;

import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.OrganisationUnit;
import com.zynap.talentstudio.organisation.positions.Position;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Hibernate CodeGenerator
 */
public class Area extends Node implements Serializable {

    /**
     * default constructor.
     */
    public Area() {
    }

    public Area(Long id) {
        super(id);
    }

    public void addAreaElement(AreaElement ae) {
        ae.setArea(this);
        if (!containsNode(ae.getNode().getId())) {
            getAreaElements().add(ae);
        }
    }

    protected boolean containsNode(Long nodeId) {
        if(nodeId == null) return false;
        for (AreaElement areaElement : areaElements) {
            if(nodeId.equals(areaElement.getNode().getId())) return true;
        }
        return false;
    }

    public void removeAreaElement(AreaElement ae) {
        getAreaElements().remove(ae);
        ae.setArea(null);
    }

    public void assignAreaElements(Set<AreaElement> areaElementSet) {

        this.areaElements.clear();
        for (AreaElement areaElement : areaElementSet) {
            addAreaElement(areaElement);
        }        
    }

    /**
     * Get all position area elements.
     *
     * @return Collection of AreaElements
     */
    public Collection getAssignedPositions() {
        List<AreaElement> elements = new ArrayList<AreaElement>(CollectionUtils.select(getAreaElements(), new NodeTypeAreaElementPredicate(Position.class)));
        sortByLabel(elements);
        return elements;
    }

    /**
     * Get all subject area elements sorted by node label.
     *
     * @return Collection of AreaElements
     */
    public Collection getAssignedSubjects() {
        List<AreaElement> elements = new ArrayList<AreaElement>(CollectionUtils.select(getAreaElements(), new NodeTypeAreaElementPredicate(Subject.class)));
        sortByLabel(elements);
        return elements;
    }

    /**
     * Get all org unit area elements.
     *
     * @return Collection of AreaElements
     */
    public Collection getAssignedOrganisationUnits() {
        List<AreaElement> elements = new ArrayList<AreaElement>(CollectionUtils.select(getAreaElements(), new NodeTypeAreaElementPredicate(OrganisationUnit.class)));
        sortByLabel(elements);
        return elements;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("nodeId", getId())
                .append("label", getLabel())
                .toString();
    }

    public Set<AreaElement> getAreaElements() {
        if (areaElements == null) areaElements = new HashSet<AreaElement>();
        return this.areaElements;
    }

    public void setAreaElements(Set<AreaElement> areaElements) {
        this.areaElements = areaElements;
    }
                                                                
    private void sortByLabel(List<AreaElement> elements) {
        Collections.sort(elements, new Comparator<AreaElement>() {
            public int compare(AreaElement o1, AreaElement o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
    }

    public void setPositionPopulationId(Long positionPopulationId) {
        this.positionPopulationId = positionPopulationId;
    }

    public Long getPositionPopulationId() {
        return positionPopulationId;
    }

    public void setSubjectPopulationId(Long subjectPopulationId) {
        this.subjectPopulationId = subjectPopulationId;
    }

    public Long getSubjectPopulationId() {
        return subjectPopulationId;
    }

    public Set<AreaElement> getPopulationAreaElements() {
        Set<AreaElement> result = new HashSet<AreaElement>();
        for (AreaElement areaElement : areaElements) {
            if(areaElement.isFromPopulation()) result.add(areaElement);
        }
        return result;
    }

    /**
     * persistent field.
     */
    private Set<AreaElement> areaElements = new HashSet<AreaElement>();
    private Long positionPopulationId;
    private Long subjectPopulationId;
}