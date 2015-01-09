package com.zynap.talentstudio.preferences.properties;


/**
 * User: amark
 * Date: 08-Dec-2004
 * Time: 11:30:20
 */
public class NestedAttributePreference extends AttributePreference {

	private static final long serialVersionUID = -4536761865271736667L;
    private AttributePreference nestedAttributePreference;

    public NestedAttributePreference() {
        super();
    }

    public NestedAttributePreference(AttributePreference nestedAttributePreference) {
        super();
        this.nestedAttributePreference = nestedAttributePreference;
    }

    public NestedAttributePreference(String attributeName, AttributePreference nestedAttributePreference) {
        super(attributeName);
        this.nestedAttributePreference = nestedAttributePreference;
    }

    public NestedAttributePreference(String attributeName, boolean displayable, AttributePreference nestedAttributePreference) {
        super(attributeName, displayable);
        this.nestedAttributePreference = nestedAttributePreference;
    }

    public AttributePreference getNestedAttributePreference() {
        return nestedAttributePreference;
    }

    public void setNestedAttributePreference(AttributePreference nestedAttributePreference) {
        this.nestedAttributePreference = nestedAttributePreference;
    }

    public AttributeView apply(Object domainObject) throws Exception {
        if (displayable) {
            if (nestedAttributePreference != null) {
                AttributeView attributeView = nestedAttributePreference.apply(domainObject);
                AttributeView value = invoke(domainObject);

                if (value != null) {
                    return new AttributeView(value.getExpectedValue(), value.getDisplayValue(), attributeView.getFormattingInfo());
                }
            }
        }

        return null;
    }
}
