package com.zynap.talentstudio.preferences;



/**
 * User: amark
 * Date: 02-Dec-2004
 * Time: 11:18:32
 */
public class DomainObjectTestHelper extends Object {

    private String name;

    private String desc;

    private boolean active;

    public DomainObjectTestHelper() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
