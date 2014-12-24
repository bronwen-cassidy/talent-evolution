package com.zynap.talentstudio.web.organisation;

import com.zynap.domain.SearchAdaptor;
import com.zynap.talentstudio.analysis.populations.Population;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeDTO;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 11-Feb-2005
 * Time: 11:00:37
 * To change this template use File | Settings | File Templates.
 */

public abstract class NodeSearchQueryWrapper implements Serializable {

    public NodeSearchQueryWrapper(Long organisationId, SearchAdaptor searchAdaptor) {
        this(searchAdaptor);
        setOrgUnitId(organisationId);                 
    }

    protected NodeSearchQueryWrapper(SearchAdaptor searchAdaptor) {
        wrappedDynamicAttributes = new HashSet();
        this.searchAdaptor = searchAdaptor;
    }

    public Long getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(Long orgUnitId) {
        this.orgUnitId = orgUnitId;
        if (searchAdaptor != null)
            searchAdaptor.setOrgUnitId(orgUnitId);
    }

    public String getOuLabel() {
        return ouLabel;
    }

    public void setOuLabel(String ouLabel) {
        this.ouLabel = ouLabel;
    }

    public String getSearchMethod() {
        return searchMethod;
    }

    public void setSearchMethod(String searchMethod) {
        this.searchMethod = searchMethod;
    }

    public Long getPopulationId() {
        return searchAdaptor.getPopulationId();
    }

    public void setPopulationId(Long populationId) {
        searchAdaptor.setPopulationId(populationId);
    }

    public Collection<DynamicAttributeDTO> getWrappedDynamicAttributes() {
        return wrappedDynamicAttributes;
    }

    public void setWrappedDynamicAttributes(Collection<DynamicAttributeDTO> attributeValues) {
        wrappedDynamicAttributes = attributeValues;
    }

    public String getActive() {
        return searchAdaptor.getActive();
    }

    public void setActive(String active) {
        searchAdaptor.setActive(active);
    }

    protected SearchAdaptor getNodeSearchQuery() {
        searchAdaptor.setAttributeValues(getAttributeValues());
        return searchAdaptor;
    }

    private Collection<AttributeValue> getAttributeValues() {
        final Set<AttributeValue> selectedAttributeValues = new HashSet<AttributeValue>();

        for (DynamicAttributeDTO attribute : wrappedDynamicAttributes) {
            String value = attribute.getValue();
            if (StringUtils.hasText(value)) {
                final DynamicAttribute da = new DynamicAttribute(attribute.getId(), attribute.getLabel(), attribute.getType());
                if (attribute.isDateTime()) {
                    if (value.endsWith(DynamicAttribute.TIME_DELIMITER)) value = value.substring(0, value.length() - 1);
                }
                AttributeValue val = AttributeValue.create(value, da);
                selectedAttributeValues.add(val);
            }
        }
        return selectedAttributeValues;
    }

    public void clearAdvancedCriteriaIfNeeded() {
        if ("N".equals(getSearchMethod())) {
            for (DynamicAttributeDTO wrappedDynamicAttribute : wrappedDynamicAttributes) {
                wrappedDynamicAttribute.setValue("");
            }            
        }
    }


    public Long getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(Long questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    private Long questionnaireId;

    private Long orgUnitId;

    private String ouLabel;

    private Collection<DynamicAttributeDTO> wrappedDynamicAttributes;

    protected SearchAdaptor searchAdaptor;

    protected Population population;

    private String searchMethod;
}
