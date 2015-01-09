/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import java.io.Serializable;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 12-Aug-2008 12:21:34
 */
public class ReportDto implements Serializable, Comparable<ReportDto> {

    public ReportDto(String label, String description, String populationLabel, String type, String accessType, Long id, Long populationId, String populationType) {
        this.label = label;
        this.description = description;
        this.populationLabel = populationLabel;
        this.type = type;
        this.accessType = accessType;
        this.id = id;
        this.populationId = populationId;
        this.populationType = populationType;
    }

    public ReportDto(String label, String description, String populationLabel, String type, String accessType, Long id, Long populationId, String populationType, String chartType) {
        this.label = label;
        this.description = description;
        this.populationLabel = populationLabel;
        this.type = type;
        this.accessType = accessType;
        this.id = id;
        this.populationId = populationId;
        this.populationType = populationType;
        this.chartType = chartType;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getPopulationLabel() {
        return populationLabel;
    }

    public String getType() {
        return type;
    }

    public String getAccessType() {
        return accessType;
    }

    public Long getId() {
        return id;
    }

    public Long getPopulationId() {
        return populationId;
    }

    public String getPopulationType() {
        return populationType;
    }

    public String getChartType() {
        return chartType;
    }

    public int compareTo(ReportDto other) {
        return label.compareTo(other.label);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportDto)) return false;

        ReportDto reportDto = (ReportDto) o;

        if (id != null ? !id.equals(reportDto.id) : reportDto.id != null) return false;

        return true;
    }

    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    public boolean isTabularReport() {
        return Report.TABULAR_REPORT.equals(type);
    }

    public boolean isChartReport() {
        return Report.CHART_REPORT.equals(type);
    }

    private String label;
    private String description;
    private String populationLabel;
    private String type;
    private String accessType;
    private Long id;
    private Long populationId;
    private String populationType;
    private String chartType;
}
