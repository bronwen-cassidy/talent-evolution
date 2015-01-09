package com.zynap.talentstudio.analysis;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.common.exceptions.TalentStudioRuntimeException;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.util.FormatterFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * User: amark
 * Date: 19-Aug-2005
 * Time: 10:16:30
 */
public final class AnalysisAttributeHelper {

    public static boolean isPersonLinkAttribute(String attributeName) {
        return contains(attributeName, NAME_ATTR) || contains(attributeName, SECOND_NAME_ATTR);
    }

    public static boolean isPositionLinkAttribute(String attributeName) {
        return contains(attributeName, POSITION_TITLE_ATTR);
    }

    public static boolean isQualifierAttribute(String attributeName) {
        return contains(attributeName, QUALIFIER_PREFIX);
    }

    public static boolean isDocumentAttribute(String attributeName) {
        return contains(attributeName, DOCUMENT_ATTR);
    }

    public static boolean isOrgUnitAttribute(String attributeName) {
        return contains(attributeName, ORGUNIT_PREFIX);
    }

    public static boolean isCoreDetailAttribute(String attributeName) {
        return contains(attributeName, CORE_DETAIL_PREFIX);
    }

    public static boolean isRelatedUserAttribute(String attributeName) {
        return contains(attributeName, USER_PREFIX);
    }

    public static boolean isActiveAttribute(String name) {
        return ACTIVE_ATTR.equals(name);
    }

    public static boolean isDate(String attributeName) {
        return DOB_ATTR.equals(attributeName);
    }

    public static boolean isQuestionnaire(String attributeName) {
        return contains(attributeName, PARTICIPANTS_PREFIX);
    }

    public static boolean isCoreAttribute(String attributeName) {
        return (!isDynamicAttribute(attributeName) && !isAssociatedArtefactAttribute(attributeName) && !isDerivedAttribute(attributeName) && !isDocumentAttribute(attributeName));
    }

    public static boolean supportsLink(String attributeName) {

        if (attributeName != null) {
            if (isCoreDetailAttribute(attributeName)) {
                return isPersonLinkAttribute(attributeName);
            } else {
                return isPositionLinkAttribute(attributeName);
            }
        }

        return false;
    }

    public static String getPrefix(final String attributeName) {
        return splitAttributeAndPrefix(attributeName, true);
    }

    public static String removePrefix(final String attributeName) {
        return splitAttributeAndPrefix(attributeName, false);
    }

    public static String removeOrganisationPrefix(String attributeName) {
        return attributeName != null ? splitAttributeAndPrefix(attributeName, false, new String[]{CORE_DETAIL_PREFIX, USER_PREFIX, QUALIFIER_PREFIX}) : "";
    }

    /**
     * Split id - format is question id, workflow id, role.
     *
     * @param id the id of the ref_value stored in report columns from the pickers.
     * For a questionnaire the name path i.e. the path stored in the ref_value of the report column database will be
     * <ul>
     * <li>1234 (the id of the dynamicAttribute if the questionnaire comes off the top level person in the pickers</li>
     * <li>subjectPrimaryAssociations_1234 (the dynamicAttribute id of the subjectPrimaryAssociations of a position</li>
     * </ul>
     * <ul>
     * <li>The first part (prefix) of subjectPrimaryAssociations is the name attribute</li> 
     * </ul>
     * @return Attribute or null
     */
    public static AnalysisParameter splitQuestionCriteriaId(String id) {

        AnalysisParameter analysisParameter = null;

        if (StringUtils.isNotEmpty(id)) {

            final String[] strings = StringUtils.split(id, QUESTION_CRITERIA_DELIMITER);
            if (strings != null && strings.length > 1) {
                String role = strings.length == 3 ? strings[2] : null;
                analysisParameter = new AnalysisParameter(strings[0], Long.valueOf(strings[1].trim()), role);
            }
        }

        return analysisParameter;
    }

    /**
     *
     * @param id
     * @return
     */
    public static AnalysisParameter getAttributeFromName(final String id) {
        AnalysisParameter analysisParameter = splitQuestionCriteriaId(id);
        if (analysisParameter == null)
            analysisParameter = new AnalysisParameter(id, null, null);
        return analysisParameter;
    }

    public static AttributeValue findAttributeValue(AnalysisParameter analysisParameter, Node node) {
        return findAttributeValue(node, new Long(analysisParameter.getUnqualifiedName()));
    }

    public static AttributeValue findAttributeValue(Node node, final Long dynamicAttributeId) {
        Object result = CollectionUtils.find(node.getDynamicAttributeValues().getValues(), new Predicate() {
            public boolean evaluate(Object object) {
                AttributeValue av = (AttributeValue) object;
                return av.getDynamicAttribute().getId().equals(dynamicAttributeId);
            }
        });
        return result != null ? (AttributeValue) result : null;
    }

    public static Integer getDerivedAttributeValue(String attributeName, Node node) {
        Integer value = null;
        int begin = attributeName.indexOf("[");
        int end = attributeName.indexOf("]");
        Long key = new Long(attributeName.substring(begin + 1, end));
        try {
            Method method = node.getClass().getMethod("get" + StringUtils.capitalize(attributeName.substring(0, begin)));
            Map map = (Map) method.invoke(node);
            Object o = map.get(key);
            if (o != null) value = new Integer(o.toString());
        } catch (Exception e) {
            throw new TalentStudioRuntimeException("Unable to get value for derived attribute: " + attributeName, e);
        }
        return value;
    }

    public static String getCoreAttributeValue(String attributeName, Node node) {

        String value = evaluateProperty(attributeName, node);
        if (StringUtils.isNotEmpty(value) && isDate(attributeName)) {
            value = FormatterFactory.getDateFormatter().formatDateAsString(value);
        }
        return value;
    }

    public static String buildQuestionCriteriaId(AnalysisParameter analysisParameter) {
        return analysisParameter != null ? buildQuestionCriteriaId(analysisParameter.getName(), analysisParameter.getQuestionnaireWorkflowId(), analysisParameter.getRole()) : null;
    }

    public static String buildQuestionCriteriaId(final String questionId, final Long workflowId, Long roleId) {
        final String role = roleId != null ? roleId.toString() : null;
        return buildQuestionCriteriaId(questionId, workflowId, role);
    }

    public static String buildQuestionCriteriaId(final DynamicAttribute dynamicAttribute, final Questionnaire questionnaire) {
        final String questionId = dynamicAttribute.getId().toString();
        final Long workflowId = questionnaire.getQuestionnaireWorkflowId();
        final LookupValue role = questionnaire.getRole();
        final String roleId = role != null ? role.getId().toString() : null;

        return buildQuestionCriteriaId(questionId, workflowId, roleId);
    }

    /**
     * Build question id.
     * <br/> Returns string in format "questionId_workflowId_roleId" or "questionId_workflowId" if roleId not supplied.
     *
     * @param questionId the id of the question
     * @param workflowId the workflow id
     * @param roleId     the role id (i.e. the id of the role mentor) used in appraisals
     * @return String or null
     */
    public static String buildQuestionCriteriaId(final String questionId, final Long workflowId, String roleId) {
        if (roleId == null)
            return workflowId != null && questionId != null ? StringUtils.join(new Object[]{questionId, workflowId}, QUESTION_CRITERIA_DELIMITER) : null;
        else
            return workflowId != null && questionId != null ? StringUtils.join(new Object[]{questionId, workflowId, roleId}, QUESTION_CRITERIA_DELIMITER) : null;
    }

    public static String getName(AnalysisParameter analysisParameter) {
        String name = buildQuestionCriteriaId(analysisParameter);
        if (analysisParameter != null && name == null) {
            name = analysisParameter.getName();
        }

        return name;
    }

    public static String getCollectionPrefix(String name) {

        if (name.indexOf(AnalysisAttributeHelper.DELIMITER) < 0) return null;
        String[] tokens = org.springframework.util.StringUtils.tokenizeToStringArray(name, DELIMITER);
        if(AnalysisAttributeHelper.isDocumentAttribute(name)) return tokens[0];
        int limit = IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? 3 : 2;
        if (tokens.length < limit) return null;
        String attName = IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? tokens[1] : tokens[0];
        if (IPopulationEngine.ASSOCIATION_PROPERTIES_NAMES_LIST.contains(attName)) {
            return IPopulationEngine.RELATED_ARTEFACT_PROPERTIES_NAMES_LIST.contains(tokens[0]) ? tokens[0] + DELIMITER + tokens[1] : tokens[0];
        }

        return null;
    }

    public static boolean isPersonMandatoryCore(String name) {
        return (FIRST_NAME_ATTR.equals(name) || SECOND_NAME_ATTR.equals(name));
    }

    public static boolean isPositionMandatoryCore(String name) {
        return (POSITION_TITLE_ATTR.equals(name) || ORG_UNIT_ID_ATTR.equals(name) || ORG_UNIT_LABEL_ATTR.equals(name));
    }

    public static String getOrganisationUnitPrefix(String attributeName) {
        if (attributeName != null) {

            String[] prefixes = new String[]{CORE_DETAIL_PREFIX, USER_PREFIX, QUALIFIER_PREFIX};
            return splitAttributeAndPrefix(attributeName, true, prefixes);
        }

        return "";
    }

    private static String splitAttributeAndPrefix(String attributeName, boolean returnPrefix) {

        if (attributeName != null) {

            String[] prefixes = new String[]{CORE_DETAIL_PREFIX, ORGUNIT_PREFIX, USER_PREFIX, QUALIFIER_PREFIX};
            return splitAttributeAndPrefix(attributeName, returnPrefix, prefixes);
        }

        return "";
    }

    private static String splitAttributeAndPrefix(String attributeName, boolean returnPrefix, String[] prefixes) {
        int pos = -1;
        // check if any of the prefixes are in the id
        for (int i = 0; i < prefixes.length && pos < 0; i++) {
            String prefix = prefixes[i];
            pos = attributeName.lastIndexOf(prefix);
        }

        // check for delimiter as last attempt
        if (pos < 0) {
            pos = attributeName.lastIndexOf(DELIMITER);

            // increment pos by 1 so the delimiter is removed as well
            if (pos >= 0 && pos < attributeName.length()) pos++;
        }

        if (returnPrefix) {
            return (pos > 0) ? attributeName.substring(0, pos - 1) : "";
        } else {
            return (pos > 0) ? attributeName.substring(pos) : attributeName;
        }
    }

    /**
     * Find AttributeValue.
     *
     * @param answers           Map of QuestionAttributeValuesCollection keyed on node id.
     * @param subjectId         the id of the subject
     * @param analysisParameter carries the attributes of the question
     * @return AttributeValue or null
     */
    public static AttributeValue findAnswer(Map answers, Long subjectId, AnalysisParameter analysisParameter) {
        final QuestionAttributeValuesCollection collection = findAnswers(answers, subjectId);
        return findAnswer(analysisParameter, collection);
    }

    /**
     * Get QuestionAttributeValuesCollection from map keyed on subject id.
     *
     * @param answers   answers to th questions
     * @param subjectId the person for whom the questionnaire belongs to
     * @return QuestionAttributeValuesCollection or null
     */
    public static QuestionAttributeValuesCollection findAnswers(Map answers, Long subjectId) {

        QuestionAttributeValuesCollection collection = null;
        if (answers != null && !answers.isEmpty()) {
            collection = (QuestionAttributeValuesCollection) answers.get(subjectId);
        }

        return collection;
    }

    /**
     * Find AttributeValue keyed on analysis parameter key.
     *
     * @param analysisParameter the details of the question attributes, id, workflow id, role id etc
     * @param collection        collection of question attributes
     * @return AttributeValue or null
     */
    public static AttributeValue findAnswer(AnalysisParameter analysisParameter, final QuestionAttributeValuesCollection collection) {

        AttributeValue answer = null;

        if (collection != null) {
            final String key = AnalysisAttributeHelper.buildQuestionCriteriaId(analysisParameter);
            answer = collection.getValue(key);
        }

        return answer;
    }

    /**
     * Get question id.
     * <br/> If question id is in form "subjectPrimaryAssociations.subject.1" return "1".
     *
     * @param analysisParameter the details of the question attributes, id, workflow id, role id etc
     * @return Long
     */
    public static Long getQuestionId(AnalysisParameter analysisParameter) {
        return Long.valueOf(analysisParameter.getDynamicAttributeId());
    }

    public static Long getQuestionId(String name) {
        return Long.valueOf(getUnqualifiedAttributeName(name));
    }

    /**
     * Takes an attribute name of expected format coreDetails.name and removes the last word after the last '.'
     *
     * @param name the name of the attribute
     * @return     the last word of the attribute after the last '.'
     */
    public static String getUnqualifiedAttributeName(String name) {
        if(StringUtils.isBlank(name)) return name;
        AnalysisParameter analysisParameter = getAttributeFromName(name);
        return org.springframework.util.StringUtils.unqualify(analysisParameter.getName());
    }

    public static List<Node> getAssociatedNodes(Node targetNode, String[] values) {
        List<Node> associatedNodes = new ArrayList<Node>();

        try {
            Method method = targetNode.getClass().getMethod(ACCESSOR_PREFIX + StringUtils.capitalize(values[0]));
            Collection associations = (Collection) method.invoke(targetNode);
            for (Iterator iterator = associations.iterator(); iterator.hasNext();) {
                ArtefactAssociation artefactAssociation = (ArtefactAssociation) iterator.next();
                Method nodeMethod = artefactAssociation.getClass().getMethod(ACCESSOR_PREFIX + StringUtils.capitalize(values[1]));
                Node associatedNode = (Node) nodeMethod.invoke(artefactAssociation);
                if (associatedNode.isActive()) {
                    associatedNodes.add(associatedNode);
                }
            }
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder("Node: ");
            builder.append(targetNode.getNodeType()).append(" with ID: ").append(targetNode.getId())
                    .append("\nthrew and exception: ").append(e.getClass()).append(" for reasons of ").append(e.getMessage());            
            logger.debug(builder.toString());
        }

        return associatedNodes;
    }

    /**
     * Split questionnaire attribute name by delimiter.
     *
     * @param analysisParameter the details of the question attributes, id, workflow id, role id etc
     * @return String[] (will be empty if the analysisParameter is not a questionnaire one)
     */
    public static String[] splitQuestionnaireAttributeName(AnalysisParameter analysisParameter) {

        if (analysisParameter.isQuestionnaireAttribute()) {
            return org.springframework.util.StringUtils.delimitedListToStringArray(analysisParameter.getName(), DELIMITER);
        }

        return new String[0];
    }

    /**
     * Splits attribute name and make sures that it joins the last two elements together if the size of the split array is 4
     * as this means the attribute name is of the form "subjectPrimaryAssociations.position.organisationUnit.label" or
     * "subjectPrimaryAssociations.subject.user.label".
     * <br/> NOTE: will not work with nested associated artefact attributes
     * - eg: "subjectPrimaryAssociations.subject.subjectPrimaryAssociations.position.organisationUnit.label"
     *
     * @param attributeName the name of the attribute is a dynamicAttribute it will be an id
     * @return always returns a String[] of size 3
     */
    public static String[] splitAttributeNames(String attributeName) {

        String[] values = org.springframework.util.StringUtils.delimitedListToStringArray(attributeName, DELIMITER);

        if (values.length == 4) {
            String[] newValues = new String[3];
            newValues[0] = values[0];
            newValues[1] = values[1];

            // join the last two together so we get "organisationunit.id" or "coredetail.name"
            newValues[2] = values[2] + DELIMITER + values[3];
            values = newValues;
        }

        return values;
    }

    /**
     * Utility method that gets property from object using reflection.
     *
     * @param name   the method name to invoke
     * @param object the object to invoke it on
     * @return String or null
     */
    public static final String evaluateProperty(final String name, Object object) {

        String value = null;
        try {
            value = BeanUtils.getNestedProperty(object, name);
        } catch (Exception e) {
        }

        return value;
    }

    /**
     * Find sub columns based on prefix.
     * <br/> If the prfix matches and column is not grouped, return a clone of the column with the prefix name removed from the attribute name.
     *
     * @param columns
     * @param collectionPrefix
     * @return List of Columns
     */
    public static List findSubColumns(List columns, final String collectionPrefix) {

        // get non-grouped columns with same prefix
        List subColumns = (List) CollectionUtils.select(columns, new Predicate() {
            public boolean evaluate(Object o) {
                Column column = (Column) o;
                final String attributeName = column.getAttributeName();
                if (!column.isGrouped() && attributeName != null && attributeName.startsWith(collectionPrefix)) {
                    return true;
                }
                return false;
            }
        });

        // remove prefix from column
        CollectionUtils.transform(subColumns, new Transformer() {
            public Object transform(Object o) {
                Column clone;
                clone = (Column) ((Column) o).clone();
                clone.setAttributeName(clone.getAttributeName().substring(collectionPrefix.length() + 1));
                return clone;
            }
        });

        return subColumns;
    }

    private static boolean contains(String attributeName, String prefix) {
        return attributeName != null && attributeName.indexOf(prefix) != -1;
    }

    public static DynamicAttribute findDynamicAttribute(AnalysisParameter analysisParameter, Collection<DynamicAttribute> attributes) {        
        final Long dynamicAttributeId = new Long(analysisParameter.getUnqualifiedName());
        return (DynamicAttribute) CollectionUtils.find(attributes, new Predicate() {
            public boolean evaluate(Object object) {
                DynamicAttribute dynamicAttribute = (DynamicAttribute) object;
                return dynamicAttribute.getId().equals(dynamicAttributeId);
            }
        });
    }

    public static boolean isBracket(String element) {
        return IPopulationEngine.LEFT_BRCKT_.equals(element) || IPopulationEngine.RIGHT_BRCKT_.equals(element);
    }

    public static boolean isMathSymbol(String element) {
        return StringUtil.equalsAny(element, IPopulationEngine.operators);
    }

    public static boolean isRightBracket(String element) {
        return IPopulationEngine.RIGHT_BRCKT_.equals(element);
    }

    public static boolean isLeftBracket(String element) {
        return IPopulationEngine.LEFT_BRCKT_.equals(element);
    }

    /**
     *
     * @param attribute - the attributeName of the BasicAnalysisAttribute 
     * @return true if the unqualified version of the attribute is a number, false otherwise
     */
    public static boolean isDynamicAttribute(String attribute) {
        String name = getUnqualifiedAttributeName(attribute);
        try {
            Long.parseLong(name);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean isDerivedAttribute(String attributeName) {
        return StringUtils.isNotBlank(attributeName) && (attributeName.indexOf(IPopulationEngine.SOURCE_DA) >= 0 || attributeName.indexOf(IPopulationEngine.TARGET_DA) >= 0);
    }

    public static boolean isAssociatedArtefactAttribute(String attributeName) {
        if(StringUtils.isBlank(attributeName)) return false;
        for (int i = 0; i < IPopulationEngine.ASSOCIATED_ARTEFACT_DA.length; i++) {
            if (attributeName.indexOf(IPopulationEngine.ASSOCIATED_ARTEFACT_DA[i]) >= 0) return true;
        }
        return false;
    }

    public static boolean isOrganisationAttribute(String name) {
        return name != null && name.startsWith(ORGUNIT_PREFIX);
    }

    public static boolean isOrganisationLabelAttribute(String name) {
        return name != null && name.indexOf(ORG_UNIT_LABEL_ATTR) != -1;
    }

    public static int getLevel(String attributeName) {
        return StringUtils.countMatches(attributeName, ".");
    }


    public static final String CORE_DETAIL_PREFIX = "coreDetail.";
    public static final String ORGUNIT_PREFIX = "organisationUnit.";
    public static final String USER_PREFIX = "user.";
    public static final String QUALIFIER_PREFIX = "qualifier.";
    public static final String DOCUMENT_PREFIX = "portfolioItems.";

    public static final String DOCUMENT_ATTR = "portfolioItems";
    public static final String PERSON_TITLE_ATTR = CORE_DETAIL_PREFIX + "title";
    public static final String PERSON_FULL_NAME_ATTR = CORE_DETAIL_PREFIX + "name";
    public static final String FIRST_NAME_ATTR = CORE_DETAIL_PREFIX + "firstName";
    public static final String SECOND_NAME_ATTR = CORE_DETAIL_PREFIX + "secondName";
    public static final String PREF_NAME_ATTR = CORE_DETAIL_PREFIX + "prefGivenName";
    public static final String EMAIL_ATTR = CORE_DETAIL_PREFIX + "contactEmail";
    public static final String TELEPHONE_ATTR = CORE_DETAIL_PREFIX + "contactTelephone";
    public static final String NAME_ATTR = CORE_DETAIL_PREFIX + "name";
    public static final String DOB_ATTR = "dateOfBirth";
    public static final String ACTIVE_ATTR = "active";

    public static final String POSITION_TITLE_ATTR = "title";

    public static final String ORG_UNIT_ID_ATTR = ORGUNIT_PREFIX + "id";
    public static final String ORG_UNIT_LABEL_ATTR = ORGUNIT_PREFIX + "label";

    public static final String QUALIFIER_LABEL_ATTR = QUALIFIER_PREFIX + "label";

    public static final String ACCESSOR_PREFIX = "get";
    public static final String DELIMITER = ".";
    public static final String QUESTION_CRITERIA_DELIMITER = "_";

    public static final String PARTICIPANTS_PREFIX = "participant";
    public static final String QUE_WORKFLOW_ID = PARTICIPANTS_PREFIX + ".primaryKey.workflowId";

    private static final Log logger = LogFactory.getLog(AnalysisAttributeHelper.class);
}
