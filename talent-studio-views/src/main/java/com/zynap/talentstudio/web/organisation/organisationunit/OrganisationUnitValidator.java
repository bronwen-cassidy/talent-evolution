package com.zynap.talentstudio.web.organisation.organisationunit;


import com.zynap.talentstudio.web.organisation.OrganisationUnitWrapperBean;
import com.zynap.talentstudio.web.common.validation.NodeValidator;
import com.zynap.talentstudio.web.utils.ZynapValidationUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for organsaition unit controllers.
 *
 * User: aandersson
 * Date: 09-Feb-2004
 * Time: 10:56:06
 */
public class OrganisationUnitValidator extends NodeValidator implements Validator {

    public boolean supports(Class aClass) {
        return OrganisationUnitWrapperBean.class.isAssignableFrom(aClass);
    }

    /**
     * Validate that the label has been provided and that the length of the comments and the label is ok.
     * <br> Also check that the org unit has a parent if it is not the default (root) org unit.
     *
     * @param o
     * @param errors - the error object that gets populated for any null values
     */
    public void validate(Object o, Errors errors) {
        OrganisationUnitWrapperBean ou = (OrganisationUnitWrapperBean) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "label", "error.organisation.label.required", "Name is a required field");
        ZynapValidationUtils.rejectGreater4000(errors, "comments", "error.max.length.exceeded.4000", "Comments has too many characters");
        ZynapValidationUtils.rejectGreater1000(errors, "label", "error.max.length.exceeded.1000", "Name has too many characters");
        if (!ou.isDefault()) {
            if (ou.getParentId() == null) {
                errors.rejectValue("parentId", "error.parent.orgunit.required", "You must select a parent organisation unit.");
            }
        }
        validateDynamicAttributes(ou, errors);
    }
}