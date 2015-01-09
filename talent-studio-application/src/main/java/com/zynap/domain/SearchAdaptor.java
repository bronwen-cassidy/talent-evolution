/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.domain;

import com.zynap.domain.orgbuilder.ISearchConstants;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.analysis.populations.PopulationCriteria;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class SearchAdaptor implements ISearchConstants, Serializable {

    public Map<String, QueryParameter> getMappedResults() {
        return results;
    }

    public String getActive() {
        if (results.containsKey(ACTIVE)) {
            String active = (String) ((QueryParameter) results.get(ACTIVE)).getValue();
            return "F".equals(active) ? INACTIVE : ACTIVE;
        }
        return allActive;
    }

    public void setActive(String active) {
        if (StringUtils.hasText(active) && !ALL.equals(active)) {
            final String value = ACTIVE.equals(active) ? "T" : "F";
            results.put(ACTIVE, new QueryParameter(value, QueryParameter.STRING));
        } else {
            // remove parameter if all is selected or if nothing was selected
            results.remove(ACTIVE);
            if (ALL.equals(active)) {
                allActive = active;
            }
        }
    }

    public Collection<AttributeValue> getAttributesValues() {
        return attributeValues;
    }

    public void setAttributeValues(Collection<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    protected String getStringQueryParamValue(String paramName) {
        return (String) getQueryParamValue(paramName);

    }

    protected Object getQueryParamValue(String paramName) {
        if (results.containsKey(paramName)) {
            return results.get(paramName).getValue();
        }

        return null;
    }

    protected void setStringQueryParam(String paramName, String value) {
        if (!StringUtils.hasText(value)) {
            results.remove(paramName);
        } else {
            results.put(paramName, new QueryParameter(value, QueryParameter.STRING, true, true));
        }
    }

    public Long getLongQueryParamValue(String paramName) {
        return (Long) getQueryParamValue(paramName);
    }

    protected void setIntegerQueryParam(String paramName, Integer i) {
        if (i == null) {
            results.remove(paramName);
        } else {
            results.put(paramName, new QueryParameter(i, QueryParameter.NUMBER));
        }
    }

    public void setLongQueryParam(String paramName, Long i) {
        if (i == null) {
            results.remove(paramName);
        } else {
            results.put(paramName, new QueryParameter(i, QueryParameter.NUMBER));
        }
    }

    public void reset() {
        results.clear();
        attributeValues.clear();
    }

    public Long getOrgUnitId() {
        return getLongQueryParamValue(ORG_ID);
    }

    public void setOrgUnitId(Long orgUnitId) {
        setLongQueryParam(ORG_ID, orgUnitId);
    }

    public Long getPopulationId() {
        return populationId;
    }

    public void setPopulationId(Long populationId) {
        this.populationId = populationId;
    }

    public Population getPopulationForSearch(Population population) {

        List<PopulationCriteria> populationCriterias = population.getPopulationCriterias();
        boolean hasCriterias = (populationCriterias != null && populationCriterias.size() > 0);
        // the population will always be set to search for all, this will make sure that no active criteria is added to the search, then if the user has selected
        // an active option in the search fields this will reflect through, note the active is highlighted by default, so this will never be unset
        population.setActiveCriteria(Population.ALL_ACTIVE);
        Collection<AttributeValue> das = this.getAttributesValues();
        Map<String, QueryParameter> mappedValues = this.getMappedResults();

        for (Map.Entry<String, QueryParameter> entry : mappedValues.entrySet()) {
            String key = entry.getKey();
            QueryParameter qp = entry.getValue();
            population.addPopulationCriteria(qp.buildCriteria(key));
        }

        if (das != null) {
            Iterator it = das.iterator();
            while (it.hasNext()) {
                AttributeValue da = (AttributeValue) it.next();
                QueryParameter qp;
                DynamicAttribute attribute = da.getDynamicAttribute();
                if (attribute.isTextAttribute()) {
                    qp = new QueryParameter(da, true, true);
                } else {
                    qp = new QueryParameter(da);
                }
                population.addPopulationCriteria(qp.buildCriteria(attribute.getId().toString()));
            }
        }

        // get new list of population criterias
        populationCriterias = population.getPopulationCriterias();
        if (populationCriterias != null && populationCriterias.size() > 0 && !hasCriterias) {            
            populationCriterias.get(0).setOperator(null);
        }
        return population;
    }

    private Collection<AttributeValue> attributeValues = new HashSet<AttributeValue>();
    protected Map<String, QueryParameter> results = new HashMap<String, QueryParameter>();
    private Long populationId;
    private String allActive;
}
