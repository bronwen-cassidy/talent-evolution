package com.zynap.talentstudio.organisation.attributes;

import com.zynap.talentstudio.organisation.attributes.validators.IDaSpecification;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 29-Jun-2005
 * Time: 15:59:39
 * To change this template use File | Settings | File Templates.
 */
public class DaDefinitionValidationFactory {

    public IDaSpecification getValidator(String dynamicAttributeType) {
        final Object o = specificationMappings.get(dynamicAttributeType);
        return o != null ? (IDaSpecification) o : null;
    }

    public Map getSpecificationMappings() {
        return specificationMappings;
    }

    public void setSpecificationMappings(Map specificationMappings) {
        this.specificationMappings = specificationMappings;
    }    

    private Map specificationMappings;
}
