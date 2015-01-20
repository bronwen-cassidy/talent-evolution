package com.zynap.talentstudio.web.analysis.picker;

import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.BasicAnalysisAttribute;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionAttributeWrapperBean;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User: amark
 * Date: 01-Feb-2006
 * Time: 09:46:23
 */
public final class AnalysisAttributeCollection implements Serializable {


    public AnalysisAttributeCollection(List attributes, List questionnaireDefinitions, Map qualifierAttributes, QuestionnaireHelper questionnaireHelper) {
        this.attributes = attributes;
        this.questionnaireDefinitions = questionnaireDefinitions;
        this.qualifierAttributes = qualifierAttributes;
        this.questionnaireHelper = questionnaireHelper;
    }

    public List getAttributes() {
        return attributes;
    }

    public List getQuestionnaireDefinitions() {
        return questionnaireDefinitions;
    }

    public Map getQualifierAttributes() {
        return qualifierAttributes;
    }

    /**
     * Set the AttributeWrapperBean on the AnalysisAttributeWrapperBean - clones the AttributeWrapperBean to avoid pass-by-ref problems.
     *
     * @param analysisAttributeWrapperBean wrapper been for the analysis package
     */
    public final void setAttributeDefinition(AnalysisAttributeWrapperBean analysisAttributeWrapperBean) {

        AttributeWrapperBean found = null;

        final BasicAnalysisAttribute analysisAttribute = analysisAttributeWrapperBean.getAnalysisAttribute();

        if (analysisAttribute != null) {

            final String attributeName = analysisAttributeWrapperBean.getAttribute();
            if (attributeName == null) {
                analysisAttributeWrapperBean.setAttributeDefinition(new AttributeWrapperBean(IDynamicAttributeService.INVALID_ATT));
                return;
            }
            // try and find it directly
            found = findAttributeWrapper(attributeName);

            if (found == null) {

                // if not found check explicitly for questionnaire or qualifier attribute 
                if (analysisAttributeWrapperBean.isQuestion()) {
                    found = findQuestion(analysisAttributeWrapperBean.getQuestionAttribute());
                } else if (AnalysisAttributeHelper.isQualifierAttribute(attributeName)) {
                    found = findQualifierAttribute(attributeName);
                }
            }

            if (found == null) found = new AttributeWrapperBean(IDynamicAttributeService.INVALID_ATT);

            try {
                found = (AttributeWrapperBean) found.clone();
            } catch (CloneNotSupportedException e) {
                throw new TalentStudioRuntimeException("Unable to clone an instance of " + AttributeWrapperBean.class.getName(), e);
            }
        }

        analysisAttributeWrapperBean.setAttributeDefinition(found);
    }

    public final AttributeWrapperBean findAttributeDefinition(String attributeName) {

        final AnalysisParameter parameter = AnalysisAttributeHelper.getAttributeFromName(attributeName);
        final Long questionnaireWorkflowId = parameter.getQuestionnaireWorkflowId();

        AttributeWrapperBean found;

        if (questionnaireWorkflowId != null) {
            found = findQuestion(attributeName, questionnaireWorkflowId);
        } else if (AnalysisAttributeHelper.isQualifierAttribute(attributeName)) {
            found = findQualifierAttribute(attributeName);
        } else {
            found = findAttributeWrapper(attributeName);
        }

        if (found == null) found = new AttributeWrapperBean(IDynamicAttributeService.INVALID_ATT);

        try {
            found = (AttributeWrapperBean) found.clone();
        } catch (CloneNotSupportedException e) {
            throw new TalentStudioRuntimeException("Unable to clone an instance of " + AttributeWrapperBean.class.getName(), e);
        }

        return found;
    }

    private AttributeWrapperBean findQualifierAttribute(String attributeName) {

        final int endPos = attributeName.indexOf(AnalysisAttributeHelper.QUALIFIER_PREFIX) - 1;
        String prefix = attributeName.substring(0, endPos);

        final int startPos = prefix.lastIndexOf(AnalysisAttributeHelper.DELIMITER) + 1;
        prefix = prefix.substring(startPos);

        return (AttributeWrapperBean) qualifierAttributes.get(prefix);
    }

    /**
     * Find the attribute wrapper for the specified id.
     *
     * @param attributeId the id of attribute to find
     * @return the AttributeWrapperBean or null if not found
     */
    private AttributeWrapperBean findAttributeWrapper(String attributeId) {

        AttributeWrapperBean attributeWrapperBean = null;

        if (attributeId != null) {

            // try direct match first
            attributeWrapperBean = (AttributeWrapperBean) CollectionUtils.find(attributes, new AttributeWrapperPredicate(attributeId));

            // if none found remove prefix and try again
            if (attributeWrapperBean == null) {
                attributeId = AnalysisAttributeHelper.removePrefix(attributeId);
                attributeWrapperBean = (AttributeWrapperBean) CollectionUtils.find(attributes, new AttributeWrapperPredicate(attributeId));
            }

            if (attributeWrapperBean == null && AnalysisAttributeHelper.isOrganisationAttribute(attributeId)) {
                attributeId = AnalysisAttributeHelper.removeOrganisationPrefix(attributeId);
                attributeWrapperBean = (AttributeWrapperBean) CollectionUtils.find(attributes, new AttributeWrapperPredicate(attributeId));
            }
        }

        return attributeWrapperBean;
    }

    /**
     * Find the question for the specified criteria attribute.
     *
     * @param criteriaAttribute the criteria attribute for the analysis functionality
     * @return the QuestionAttributeWrapperBean or null if not found
     */
    private QuestionAttributeWrapperBean findQuestion(AnalysisParameter criteriaAttribute) {
        final QuestionAttribute question = questionnaireHelper.findQuestion(questionnaireDefinitions, criteriaAttribute);
        return question != null ? new QuestionAttributeWrapperBean(question) : null;
    }

    private QuestionAttributeWrapperBean findQuestion(String attributeName, Long questionnaireWorkflow) {
        final QuestionAttribute question = questionnaireHelper.findQuestion(questionnaireDefinitions, attributeName, questionnaireWorkflow);
        return question != null ? new QuestionAttributeWrapperBean(question) : null;
    }

    private final class AttributeWrapperPredicate implements Predicate {

        private String attributeId;

        public AttributeWrapperPredicate(String attributeId) {
            this.attributeId = attributeId;
        }

        public boolean evaluate(Object object) {
            AttributeWrapperBean attributeWrapperBean = (AttributeWrapperBean) object;
            return attributeId.equals(attributeWrapperBean.getId().toString());
        }
    }

    private final List attributes;
    private final List questionnaireDefinitions;
    private final Map qualifierAttributes;
    private QuestionnaireHelper questionnaireHelper;
}
