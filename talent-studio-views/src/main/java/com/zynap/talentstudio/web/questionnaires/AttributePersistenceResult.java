/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Class or Interface description.
 *
 * @author taulant.bajraktari
 * @since 08-Jul-2008 11:12:22
 */
public class AttributePersistenceResult implements Serializable {

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public boolean isNotNull() {
        return StringUtils.hasText(errorMessage) || StringUtils.hasText(attributeId);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setRootDaId(Long rootDaId) {
        this.rootDaId = rootDaId;
    }

    public Long getRootDaId() {
        return rootDaId;
    }

    public void addAnswer(String answer, Long id) {
        answers.add(new Answer(answer, id));
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setLastModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setModifiedBy(String displayName) {

        this.modifiedBy = displayName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    private String errorMessage;
    private String attributeId;
    private String answer = "";
    private Long rootDaId;
    private List<Answer> answers = new ArrayList<Answer>();
    private String dateModified;
    private String modifiedBy;
}
