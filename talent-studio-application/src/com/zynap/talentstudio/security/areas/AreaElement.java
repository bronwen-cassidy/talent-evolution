package com.zynap.talentstudio.security.areas;

import com.zynap.domain.ZynapDomainObject;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.Subject;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AreaElement extends ZynapDomainObject {

    /**
     * default constructor.
     */
    public AreaElement() {
    }

    /**
     * Convenient constructor.
     * <br> Sets id and area to null.
     *
     * @param node The Node
     * @param cascading Whether or not children are included
     */
    public AreaElement(Node node, boolean cascading) {
        this(null, node, null, cascading);
    }

    /**
     * minimal constructor.
     * <br> Sets cascading to false.
     *
     * @param id The id
     * @param node The Node
     * @param area The Area this element belongs to
     */
    public AreaElement(Long id, Node node, Area area) {
        this(id, node, area, false);
    }

    /**
     * full constructor.
     *
     * @param id The id
     * @param node The Node
     * @param area The Area this element belongs to
     * @param cascading Whether or not children are included
     */
    public AreaElement(Long id, Node node, Area area, boolean cascading) {
        super(id);
        this.cascading = cascading;
        this.node = node;
        this.area = area;
    }

    public AreaElement(Node node, boolean cascading, boolean fromPopulation) {
        this(node, cascading);
        this.fromPopulation = fromPopulation;
    }

    public boolean isCascading() {
        return this.cascading;
    }

    public void setCascading(boolean cascading) {
        this.cascading = cascading;
    }

    public boolean isExcluded() {
        return excluded;
    }

    public void setExcluded(boolean excluded) {
        this.excluded = excluded;
    }

    public boolean isFromPopulation() {
        return fromPopulation;
    }

    public void setFromPopulation(boolean fromPopulation) {
        this.fromPopulation = fromPopulation;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Area getArea() {
        return this.area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getLabel() {
        return node != null ? node.getLabel() : super.getLabel();
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("cascading", isCascading())
                .toString();
    }

    /**
     * nullable persistent field.
     */
    private boolean cascading;

    private boolean fromPopulation;

    /**
     * persistent field.
     */
    private Node node;

    /**
     * persistent field.
     */
    private Area area;

    private boolean excluded;

}
