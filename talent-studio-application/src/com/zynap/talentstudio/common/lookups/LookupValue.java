package com.zynap.talentstudio.common.lookups;

import com.zynap.domain.ZynapDomainObject;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * @author Hibernate CodeGenerator
 */
public class LookupValue extends ZynapDomainObject {


    private static final long serialVersionUID = 3817110678636858943L;

    /**
     * default constructor.
     */
    public LookupValue() {
    }

    public LookupValue(Long id) {
        super(id);
    }

    public LookupValue(String typeId) {
        this.typeId = typeId;
    }

    /**
     * Convenience constructor.
     * Uses the values of the LookUpType to set the other fields.
     *
     * @param lookupType The lookup type
     */
    public LookupValue(LookupType lookupType) {
        this(null, lookupType.getLabel(), lookupType.getDescription(), false, false, 0, lookupType);
    }

    public LookupValue(String valueId, String typeId, String label, String description) {
        this.valueId = valueId;
        this.typeId = typeId;
        this.label = label;
        this.description = description;
    }

    public LookupValue(String valueId, String label, String description, LookupType lookupType) {
        this(valueId, label, description, true, false, 0, lookupType);
    }

    /**
     * full constructor.
     */
    public LookupValue(String valueId, String label, String description, boolean active, boolean system, int sortOrder, LookupType lookupType) {
        this.valueId = valueId;
        this.label = label;
        this.description = description;
        this.active = active;
        this.system = system;
        this.sortOrder = sortOrder;
        this.lookupType = lookupType;
        if (lookupType != null) this.typeId = lookupType.getTypeId();
    }

    public String getValueId() {
        return this.valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId;
    }

    public String getTypeId() {
        return this.lookupType != null ? lookupType.getTypeId() != null ? lookupType.getTypeId() : this.typeId : this.typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystem() {
        return this.system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    public int getSortOrder() {
        return this.sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LookupType getLookupType() {
        return this.lookupType;
    }

    public void setLookupType(LookupType lookupType) {
        this.lookupType = lookupType;
        this.typeId = lookupType.getTypeId();
    }

    public boolean isBlank() {
        return blank;
    }

    public void setBlank(boolean blank) {
        this.blank = blank;
    }

    /**
     * Sort by short description.
     *
     * @param o1
     * @param o2
     * @return int
     */
    public int compare(Object o1, Object o2) {
        if (o1 instanceof LookupValue && o2 instanceof LookupValue) {
            // now i can do a comparison
            LookupValue obj1 = (LookupValue) o1;
            LookupValue obj2 = (LookupValue) o2;
            return (obj1.label.compareTo(obj2.label));
        }
        return -1;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("valueId", getValueId())
                .append("typeId", getTypeId())
                .append("label", getLabel())
                .append("description", getDescription())
                .append("active", isActive())
                .append("system", isSystem())
                .append("sortOrder", getSortOrder())
                .append("lookupType", getLookupType())
                .toString();
    }

    public int compareBySortOrder(LookupValue lookupValue) {
        return new Integer(this.sortOrder).compareTo(new Integer(lookupValue.getSortOrder()));
    }

    public LookupValue createAuditable() {
        LookupValue qualif = new LookupValue(valueId, typeId, label, "");
        qualif.setLookupType(new LookupType(lookupType.getTypeId(), lookupType.getLabel(), ""));
        qualif.setId(id);
        return qualif;
    }

    /**
     * persistent field
     */
    private String valueId;

    /**
     * persistent field
     */
    private String typeId;

    /**
     * persistent field
     */
    private String description;

    /**
     * persistent field
     */
    private boolean system = false;

    /**
     * nullable persistent field
     */
    private int sortOrder;
    private boolean blank;

    /**
     * nullable persistent field
     */
    private LookupType lookupType = new LookupType();
}
