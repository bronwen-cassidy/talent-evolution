package com.zynap.talentstudio.utils.build.translate.elements;

public class Action {

    public String type;
    public String xpath;
    public String value;
    public String refresh;
    public String step;

    public void dump() {
        System.out.println("action= " + type);
        System.out.println("xpath= " + xpath);
        System.out.println("value= " + value);
        System.out.println("refresh= " + refresh);
        System.out.println("type= " + type);
    }

    /**
     * Get xpath.
     *
     * @return xpath as String.
     */
    public String getXpath() {
        return xpath;
    }

    /**
     * Set xpath.
     *
     * @param xpath the value to set.
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * Get value.
     *
     * @return value as String.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set value.
     *
     * @param value the value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get refresh.
     *
     * @return refresh as String.
     */
    public String getRefresh() {
        return refresh;
    }

    /**
     * Set refresh.
     *
     * @param refresh the value to set.
     */
    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    /**
     * Get type.
     *
     * @return type as String.
     */
    public String getType() {
        return type;
    }

    /**
     * Set type.
     *
     * @param type the value to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    public void setStep(String sequence) {
        this.step = sequence;
    }

    public String getStep() {
        return this.step;
    }
}
