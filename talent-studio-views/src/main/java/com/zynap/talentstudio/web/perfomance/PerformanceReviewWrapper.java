package com.zynap.talentstudio.web.perfomance;

import com.zynap.talentstudio.performance.PerformanceReview;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * User: amark
 * Date: 23-Nov-2005
 * Time: 13:06:21
 */
public final class PerformanceReviewWrapper implements Serializable {

    private String label;

    public PerformanceReviewWrapper(Collection populations, Collection<DefinitionDTO> definitions) {
        this.populations = populations;
        this.definitions = definitions;
    }

    public String getLabel() {
        return label;
    }

    public boolean isNotifiable() {
        return notifiable;
    }

    public void setNotifiable(boolean notifiable) {
        this.notifiable = notifiable;
    }

    public boolean isUserManagedReview() {
        return userManagedReview;
    }

    public void setUserManagedReview(boolean userManagedReview) {
        this.userManagedReview = userManagedReview;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PerformanceReview getModifiedPerformanceReview() {
        PerformanceReview performanceReview = new PerformanceReview(null, label);
        performanceReview.setNotifiable(notifiable);
        performanceReview.setUserManaged(userManagedReview);
        return performanceReview;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Long getManagerQuestionnaireDefinitionId() {
        return managerQuestionnaireDefinitionId;
    }

    public void setManagerQuestionnaireDefinitionId(Long managerQuestionnaireDefinitionId) {
        this.managerQuestionnaireDefinitionId = managerQuestionnaireDefinitionId;
    }

    public Long getGeneralQuestionnaireDefinitionId() {
        return generalQuestionnaireDefinitionId;
    }

    public void setGeneralQuestionnaireDefinitionId(Long generalQuestionnaireDefinitionId) {
        this.generalQuestionnaireDefinitionId = generalQuestionnaireDefinitionId;
    }

    public Long getHrUserId() {
        return hrUserId;
    }

    public void setHrUserId(Long hrUserId) {
        this.hrUserId = hrUserId;
    }

    public String getHrUserLabel() {
        return hrUserLabel;
    }

    public void setHrUserLabel(String hrUserLabel) {
        this.hrUserLabel = hrUserLabel;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Collection getPopulations() {
        return populations;
    }

    public Collection<DefinitionDTO> getDefinitions() {
        return definitions;
    }

    private Long populationId;
    private Long managerQuestionnaireDefinitionId;
    private Long generalQuestionnaireDefinitionId;
    private Long hrUserId;
    private String hrUserLabel;
    private Date expiryDate;

    private final Collection populations;
    private final Collection<DefinitionDTO> definitions;
    private boolean notifiable;
    private boolean userManagedReview;
}
