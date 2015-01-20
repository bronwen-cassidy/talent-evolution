package com.zynap.talentstudio.web.analysis;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * User: amark
 * Date: 17-Aug-2005
 * Time: 18:17:46
 */
public abstract class AnalysisAttributeWrapperBean implements Serializable {

    public AnalysisAttributeWrapperBean() {
    }

    public final Long getId() {
        return getAnalysisAttribute().getId();
    }

    public final void setId(Long id) {
        getAnalysisAttribute().setId(id);
    }

    /**
     * Get the question attribute.
     *
     * @return an instance of Attribute, or null if this is not a question-based criteria.
     */
    public final AnalysisParameter getQuestionAttribute() {
        final BasicAnalysisAttribute analysisAttribute = getAnalysisAttribute();
        final AnalysisParameter attribute = analysisAttribute.getAnalysisParameter();
        return attribute == null ? null : attribute.isQuestionnaireAttribute() ? attribute : null;
    }

    public AnalysisParameter getAnalysisParameter() {
        final BasicAnalysisAttribute analysisAttribute = getAnalysisAttribute();
        return analysisAttribute.getAnalysisParameter();
    }

    /**
     * Spring binding method.
     * <br/> Returns concatenated string with attribute name, workflow id and role (role and workflow id are optional.)
     *
     * @return String representing the attribute.
     */
    public String getAttribute() {
        return AnalysisAttributeHelper.getName(getAnalysisParameter());
    }

    /**
     *
     * @return just the attribute part no concatenations
     */
    public String getAttributeName() {
        return getAnalysisAttribute().getAttributeName();
    }

    /**
     * Spring binding method.
     * Set attribute - expects concatenated string with attribute name, workflow id and role (role and workflow id are optional.)
     *
     * @param value
     */
    public void setAttribute(String value) {
        final AnalysisParameter attribute = AnalysisAttributeHelper.getAttributeFromName(value);
        if (attribute != null) {
            final BasicAnalysisAttribute analysisAttribute = getAnalysisAttribute();
            analysisAttribute.setAnalysisParameter(attribute);            
        }
    }

    /**
     * Method used to determine criteria has an attribute.
     * <br/> Only used in JSPs.
     *
     * @return true if criteria has an attribute.
     */
    public boolean isAttributeSet() {
        return StringUtils.hasText(getAttribute());
    }

    /**
     * Check that criteria has had the name of the attribute set.
     *
     * @return true if the underlying criteria object has a value for its "attribute name" property.
     */
    public boolean hasAttributeName() {
        return getAnalysisAttribute() != null && StringUtils.hasText(getAnalysisAttribute().getAttributeName());
    }

    public abstract BasicAnalysisAttribute getAnalysisAttribute();

    public final String getAttributeLabel() {
        if (label != null) return label;
        return getAttributeDefinitionLabel();
    }

    public final String getAttributeDefinitionLabel() {
        return attributeDefinition != null ? attributeDefinition.getLabel() : "";    
    }

    public final void setAttributeLabel(String label) {
        this.label = label;
    }

    public final AttributeWrapperBean getAttributeDefinition() {
        return attributeDefinition;
    }

    public void setAttributeDefinition(AttributeWrapperBean attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
    }

    public final boolean isQuestion() {
        final BasicAnalysisAttribute analysisAttribute = getAnalysisAttribute();
        return analysisAttribute != null && analysisAttribute.getAnalysisParameter() != null && analysisAttribute.getAnalysisParameter().isQuestionnaireAttribute();
    }

    public final boolean isOrganisationUnit() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isOrganisationUnitType();
    }

    public final boolean isPosition() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isPositionType();
    }

    public final boolean isActiveAttribute() {
        return attributeDefinition != null && AnalysisAttributeHelper.ACTIVE_ATTR.equals(attributeDefinition.getId());
    }

    public final boolean isSubject() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isSubjectType();
    }

    public final boolean isNode() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isNodeType();
    }


    public boolean isLastUpdatedBy() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isLastUpdatedByType();
    }

    public boolean isDerivedAttribute() {
        return attributeDefinition != null && attributeDefinition.isDerivedAttribute();
    }

    public final boolean isText() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isTextAttribute();
    }

    public final boolean isNumber() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isNumericType();
    }

    public final boolean isImage() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isImageType();
    }

    public final boolean isDate() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isDateAttribute();
    }

    public final boolean isBlogComment() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isBlogComment();
    }

    public final boolean isSum() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isSumType();
    }

    public final boolean isLastUpdated() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinition().isLastUpdatedType();
    }

    /**
     * Is this an invalid attribute - only returns true if the dynamic attribute definition equals {@link IDynamicAttributeService#INVALID_ATT}.
     * <br/> Invalid attributes are attributes that no longer have corresponding attribute definitions and must be removed,
     * <b>NOT attributes whose attribute defintion has not been set<b/>.
     * <br/> If the dynamic attribute definition is null (which is true for new columns or criterias) this will return false.
     *
     * @return true or false.
     */
    public final boolean isInvalid() {
        return getAttributeDefinition() != null && getAttributeDefinition().getAttributeDefinition() == IDynamicAttributeService.INVALID_ATT;
    }

    /**
     * Determine that the criteria attribute is set and that it is set correctly - ie: the id of the
     * attributeDefinition property matches the criteria's name.
     *
     * @return true or false
     */
    public final boolean hasIncorrectAttributeDefinition() {

        final boolean definitionNotSet = (attributeDefinition == null);

        String id = definitionNotSet ? null : attributeDefinition.getId();
        final String attributeName = getAnalysisAttribute().getAttributeName();

        // IMPORTANT use endsWith(..) because attribute name can be "subjectPrimaryAssociations.position.title" but the id will be "title"
        return definitionNotSet || !(attributeName != null && id != null && attributeName.endsWith(id));
    }

    /**
     * Get id for dynamic attribute.
     *
     * @return Long or null if has none.
     */
    public final Long getDynamicAttributeId() {
        String attributeName = getAttributeName();
        if(StringUtils.hasText(attributeName)) {
            String name = AnalysisAttributeHelper.getUnqualifiedAttributeName(attributeName);
            if(org.apache.commons.lang.math.NumberUtils.isNumber(name)) {
                return new Long(name);
            }
        }
        return verifyAttributeDefinitionState() ? attributeDefinition.getAttributeDefinition().getId() : null;
    }

    private boolean verifyAttributeDefinitionState() {
        return attributeDefinition != null && attributeDefinition.getAttributeDefinitionId() != null;
    }

    public boolean isFormula() {
        return false;
    }

    public String toString() {
        return getAttributeName();
    }

    public boolean isOrgunitBranch() {
        return attributeDefinition != null && attributeDefinition.isOrgunitBranch();
    }

    /**
     * The wrapped dynamic attribute.
     */
    protected AttributeWrapperBean attributeDefinition;

    protected String label;
}
