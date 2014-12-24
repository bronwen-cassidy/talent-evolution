/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesMap;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 06-Jul-2009 14:04:15
 */
public class MockJRField implements JRField {

    public MockJRField(String name, String description, Class valueClass) {
        this.name = name;
        this.description = description;
        this.valueClass = valueClass;
    }

    public MockJRField(String name) {
        this.name = name;
        this.description = name;
        this.valueClass = String.class;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class getValueClass() {
        return valueClass;
    }

    public String getValueClassName() {
        return valueClass != null ? valueClass.getName() : null;
    }

    public JRPropertiesMap getPropertiesMap() {
        return null;
    }

    private String name;
    private String description;
    private Class valueClass;
}
