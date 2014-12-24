/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 01-May-2007 11:37:56
 */
public class DefinitionDTO {


    public DefinitionDTO(String label, Long id, String description, String title, byte[] xmlDefinition) {
        this.label = label;
        this.id = id;
        this.description = description;
        this.title = title;
        this.xmlDefinition = xmlDefinition;
    }

    public DefinitionDTO(String label, Long id, String description, String title) {
        this.label = label;
        this.id = id;
        this.description = description;
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getXmlDefinition() {
        return this.xmlDefinition;
    }

    private String label;
    private Long id;
    private String description;
    private String title;
    private byte[] xmlDefinition;
}
