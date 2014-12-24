package com.zynap.talentstudio.analysis;

import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.organisation.Node;

import org.springframework.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: jsueiras
 * Date: 26-Jul-2005
 * Time: 11:51:13
 * Class that contains parameters used by population engine and other reporting components.
 * <br/> Contains several fields such as attribute name, workflow id and role.
 * <br/> Attribute name is currently the only required parameter.
 */
public class AnalysisParameter implements Cloneable {

    /**
     *
     * @param name - represents the attribute name or ref_value, this field is qualified. The field may be:
     * <ul>
     *      <li>123 this is a dynamicAttribute that may belong to a questionnaire if questionnaireWorkflowId is not null</li>
     *      <li>subjectPrimaryAssociations.position.123 this is a dynamicAttribute that belongs to the subjects current job</li>
     *      <li>coreDetail.name this is the name of the person</li>
     *      <li>sourceDerivedAttributes[300] this is what is known as a qualifier attribute</li>  
     * </ul>
     *
     * @param questionnaireWorkflowId - this will be null unless the dynamicAttribute (or name field) is a questionnaire attribute
     * @param role - this will be null unless the questionnaireWrokflow is an appraisal
     */
    public AnalysisParameter(String name, Long questionnaireWorkflowId, String role) {
        this.name = name;
        this.questionnaireWorkflowId = questionnaireWorkflowId;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Long getQuestionnaireWorkflowId() {
        return questionnaireWorkflowId;
    }

    public boolean isDerivedAttribute() {
        return StringUtils.hasText(name) && (name.indexOf(IPopulationEngine.SOURCE_DA) >= 0 || name.indexOf(IPopulationEngine.TARGET_DA) >= 0);
    }

    public boolean isDynamicAttribute() {
        // For dynamic attributes the attribute is the dynamic attribute id
        // for regular attributes is the label.
        String unqualifiedName = getDynamicAttributeId();
        try {
            Long.parseLong(unqualifiedName);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public String getDynamicAttributeId() {
        return name != null ? StringUtils.unqualify(name) : name;
    }

    public String getUnqualifiedName() {
        return name != null ? StringUtils.unqualify(name) : name;
    }

    public boolean isAssociatedArtefactAttribute() {
        for (int i = 0; i < IPopulationEngine.ASSOCIATED_ARTEFACT_DA.length; i++) {
            if (name.indexOf(IPopulationEngine.ASSOCIATED_ARTEFACT_DA[i]) >= 0) return true;
        }
        return false;
    }

    public String[] splitAttributeName() {
        for (int i = 0; i < IPopulationEngine.ASSOCIATED_ARTEFACT_DA.length; i++) {
            int index = name.indexOf(IPopulationEngine.ASSOCIATED_ARTEFACT_DA[i]);
            if (index >= 0) {
                String prefix = name.substring(0, index + IPopulationEngine.ASSOCIATED_ARTEFACT_DA[i].length());
                String sufix = name.substring(index + IPopulationEngine.ASSOCIATED_ARTEFACT_DA[i].length() + 1);
                return new String[]{prefix, sufix};
            }
        }
        return null;
    }

    public boolean isNestedAssociatedArtefactAttribute() {
        return (isAssociatedArtefactAttribute() && name.startsWith(IPopulationEngine.PARENT_ATTR));
    }

    public boolean isQuestionnaireAttribute() {
        return (questionnaireWorkflowId != null);
    }

    public boolean isQualifierAttribute() {
        return AnalysisAttributeHelper.isQualifierAttribute(name);
    }

    public boolean isDateOfBirth() {
        return AnalysisAttributeHelper.isDate(name);
    }

    public boolean isQuestionnaire() {
        return AnalysisAttributeHelper.isQuestionnaire(name);
    }

    public boolean isCoreDetail() {
        return AnalysisAttributeHelper.isCoreDetailAttribute(name);
    }

    public boolean isRelatedUser() {
        return AnalysisAttributeHelper.isRelatedUserAttribute(name);
    }

    public boolean isCoreAttribute() {
        return (!isDynamicAttribute() && !isAssociatedArtefactAttribute() && !isQuestionnaireAttribute() && !isDerivedAttribute());
    }

    public boolean isPositionCoreMandatoryAttribute() {
        return AnalysisAttributeHelper.isCoreAttribute(name) && (AnalysisAttributeHelper.isPositionMandatoryCore(name));
    }

    public boolean isPersonLinkAttribute() {
        return AnalysisAttributeHelper.isCoreAttribute(name) && AnalysisAttributeHelper.isPersonLinkAttribute(name);
    }

    public boolean isPositionLinkAttribute() {
        return AnalysisAttributeHelper.isCoreAttribute(name) && AnalysisAttributeHelper.isPositionLinkAttribute(name);
    }

    public boolean isActiveAttribute() {
        return AnalysisAttributeHelper.isCoreAttribute(name) && AnalysisAttributeHelper.isActiveAttribute(name);
    }

    public boolean isSystemAttribute(String type) {
        if (Node.POSITION_UNIT_TYPE_.equals(type))
            return isPositionCoreMandatoryAttribute();
        return AnalysisAttributeHelper.isPersonMandatoryCore(name);
    }

    public Class getClassDerivedAttribute() {
        if (name.indexOf(IPopulationEngine.SOURCE_DA) == 0) return SourceDerivedAttribute.class;
        if (name.indexOf(IPopulationEngine.TARGET_DA) == 0) return TargetDerivedAttribute.class;
        return null;
    }

    public String getIndexDerivedAttribute() {
        if (!isDerivedAttribute()) return null;
        return name.substring(name.indexOf("[") + 1, name.indexOf("]"));
    }

    /**
     * Get the collection prefix from the attribute name.
     *
     * @return String or null
     */
    public String getCollectionPrefix() {

        if (name.indexOf(AnalysisAttributeHelper.DELIMITER) < 0) return null;
        String[] tokens = StringUtils.tokenizeToStringArray(name, AnalysisAttributeHelper.DELIMITER);
        int limit = IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? 3 : 2;
        if (tokens.length < limit) return null;
        String attName = IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? tokens[1] : tokens[0];
        if (IPopulationEngine.ASSOCIATION_PROPERTIES_NAMES_LIST.contains(attName)) {
            return IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? tokens[0] + AnalysisAttributeHelper.DELIMITER + tokens[1] : tokens[0];
        }

        return null;
    }

    public boolean equals(Object command) {
        if (this == command) return true;
        if (!(command instanceof AnalysisParameter)) return false;

        final AnalysisParameter analysisParameter = (AnalysisParameter) command;

        if (name != null ? !name.equals(analysisParameter.name) : analysisParameter.name != null) return false;
        if (questionnaireWorkflowId != null ? !questionnaireWorkflowId.equals(analysisParameter.questionnaireWorkflowId) : analysisParameter.questionnaireWorkflowId != null)
            return false;
        if (role != null ? !role.equals(analysisParameter.role) : analysisParameter.role != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (questionnaireWorkflowId != null ? questionnaireWorkflowId.hashCode() : 0);
        result = 29 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Attribute[");
        stringBuffer.append("\r\n name=").append(name);
        stringBuffer.append("\r\n questionnaireWorkflowId=").append(questionnaireWorkflowId);
        stringBuffer.append("\r\n role=").append(role);
        stringBuffer.append("]");

        return stringBuffer.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getNestedPathWithoutId() {
        if (name != null && name.indexOf(AnalysisAttributeHelper.DELIMITER) != -1) {
            return name.substring(0, name.lastIndexOf("."));
        }
        return "";
    }

    public boolean isDocument() {
        return AnalysisAttributeHelper.isDocumentAttribute(name);
    }

    public boolean isOrganisationUnitAttribute() {
        return AnalysisAttributeHelper.isOrganisationAttribute(name);

    }

    public boolean isOrganisationUnitLabelAttribute() {
        return AnalysisAttributeHelper.isOrganisationLabelAttribute(name);
    }

    public boolean isIdField() {
        return name != null && name.equals(ID_PARAM);
    }

    private String name;

    private Long questionnaireWorkflowId;

    private String role;
    public static final String QUESTIONNAIRE_DELIMITER = AnalysisAttributeHelper.QUESTION_CRITERIA_DELIMITER;
    private static final String ID_PARAM = "id";
}
