package com.zynap.talentstudio.web.security.domain;

import com.zynap.talentstudio.security.SecurityDomain;
import com.zynap.talentstudio.web.utils.ZynapValidationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: amark
 * Date: 16-Mar-2005
 * Time: 16:13:03
 */
public class SecurityDomainValidator implements Validator {

    /**
     * Return whether or not this object can validate objects
     * of the given class.
     */
    public boolean supports(Class clazz) {
        return SecurityDomainWrapperBean.class.isAssignableFrom(clazz);
    }

    /**
     * Validate an object, which must be of a class for which
     * the supports() method returned true.
     *
     * @param obj Populated object to validate
     * @param errors Errors object we're building. May contain
     * errors for this field relating to types.
     */
    public void validate(Object obj, Errors errors) {
        validateCoreValues(obj, errors);
    }

    /**
     * Validate the core values of the object (currently only label is required.)
     *
     * @param obj Populated object to validate
     * @param errors Errors object we're building. May contain
     * errors for this field relating to types.
     */
    public void validateCoreValues(Object obj, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.securitydomain.label.required", "'Name' is a required field.");
    }

    /**
     * Check that an area has been set on the security domain.
     *
     * @param obj Populated object to validate
     * @param errors Errors object we're building. May contain
     * errors for this field relating to types.
     */
    public void validateArea(Object obj, Errors errors) {
        ZynapValidationUtils.rejectIfNull(errors, "areaId", "error.securitydomain.area.required", "Please select an area", obj);
    }

    public void validateSecurityDomainLabel(SecurityDomainWrapperBean securityDomainWrapper, Errors errors, Collection allDomains) {
        
        for (Iterator it = allDomains.iterator(); it.hasNext();) {
            SecurityDomain domain = (SecurityDomain) it.next();
            // do not validate against self
            if(domain.getId().equals(securityDomainWrapper.getId())) continue;
            if (domain.getLabel().equalsIgnoreCase(securityDomainWrapper.getLabel())) {
                errors.rejectValue("label", "error.duplicate.label");
                break;
            }
        }
    }    
}
