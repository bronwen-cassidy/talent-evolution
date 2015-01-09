package com.zynap.talentstudio.analysis.populations;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.util.Formatter;
import com.zynap.domain.orgbuilder.ISearchConstants;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Class or Interface description.
 *
 * @author jsuiras
 * @version 0.1
 * @since 25-Jul-2005 13:49:08
 */
public class HibernateCriteriaResolver {

    public HibernateCriteriaResolver() {
        extendedAttCriteriaCompiler = new ExtendedAttCriteriaCompiler();
        derivedAttCriteriaCompiler = new DerivedAttCriteriaCompiler();
        coreAttCriteriaCompiler = new CoreCriteriaCompiler();
        coreDetailAttCriteriaCompiler = new CoreDetailsAttCriteriaCompiler();
        questionnaireAttCriteriaCompiler = new QuestionnaireAttCriteriaCompiler();
        assocArtefactAttCriteriaCompiler = new AssocArtefactAttCriteriaCompiler();
        userDetailAttCriteriaCompiler = new UserDetailsAttCriteriaCompiler();
        questionnaireWorkflowAttCriteriaCompiler = new QuestionnaireWorkflowAttCriteriaCompiler();
    }

    private DefaultCriteriaCompiler getCriteriaCompiler(AnalysisParameter attr) {
        if (attr == null)
            return coreAttCriteriaCompiler;
        else if (attr.isAssociatedArtefactAttribute())
            return assocArtefactAttCriteriaCompiler;
        else if (attr.isQuestionnaireAttribute())
            return questionnaireAttCriteriaCompiler;
        else if (attr.isDynamicAttribute())
            return extendedAttCriteriaCompiler;
        else if (attr.isDerivedAttribute())
            return derivedAttCriteriaCompiler;
        else if (attr.isCoreDetail())
            return coreDetailAttCriteriaCompiler;
        else if (attr.isRelatedUser())
            return userDetailAttCriteriaCompiler;
        else if (attr.isQuestionnaire())
            return questionnaireWorkflowAttCriteriaCompiler;
        else
            return coreAttCriteriaCompiler;

    }

    public static String getQueAlias(String attAlias) {
        return "q" + attAlias;
    }

    public static String getWorkflowAlias(String attAlias) {
        return "qw" + attAlias;
    }

    public void compile(StringBuffer sql, List criterias, String nodeAlias) throws TalentStudioException {

        if (criterias != null && criterias.size() > 0) {
            List<List<PopulationCriteria>> groupCriterias = groupCriterias(criterias);

            if (!groupCriterias.isEmpty()) {
                sql.append(" AND ( ");
                for (Iterator it = groupCriterias.iterator(); it.hasNext();) {
                    String nodeAliasMod = nodeAlias;
                    List subCriterias = (List) it.next();
                    final PopulationCriteria populationCriteria = (PopulationCriteria) subCriterias.get(0);
                    if (populationCriteria.getModifiedAlias() != null) nodeAliasMod = populationCriteria.getModifiedAlias();

                    getCriteriaCompiler(populationCriteria.getAnalysisParameter()).process(sql, subCriterias, nodeAliasMod, populationCriteria.getPopulation().getType());
                }
                sql.append(" ) ");
            }
        }

    }

    public void addCriteriaValueForMetricAttribute(AnalysisParameter attr, String attrAlias, String value, StringBuffer sql, String nodeAlias) {
        StringBuffer s = getCriteriaCompiler(attr).addMetricValue(attr, nodeAlias, attrAlias, value);
        if (s != null) {
            sql.append(s);
        }
    }

    public void addCriteriaForMetricAttribute(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attPrefix) {
        StringBuffer s = getCriteriaCompiler(attr).getCriteriaForAttribute(attr, nodeAlias, attPrefix);
        if (s != null) sql.append(AND).append(s);       
    }

    public String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attrAlias) {
        return getCriteriaCompiler(attr).getAttrForSelectClause(attr, nodeAlias, attrAlias);
    }

    public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
        getCriteriaCompiler(attr).addMetricAttributeToFromClause(attr, sql, nodeAlias, attrAlias);
    }

    abstract class DefaultCriteriaCompiler {

        abstract void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias);

        abstract StringBuffer getCriteriaForAttribute(AnalysisParameter metric, String nodeAlias, String attPrefix);

        String getQuotes(PopulationCriteria criteria) {
            //if ((NumberUtils.isNumber(criteria.getRefValue()) && (criteria.getComparator().equals(IPopulationEngine.GT) || criteria.getComparator().equals(IPopulationEngine.LT))) || criteria.getAnalysisParameter().isDateOfBirth())
            final String value = criteria.getRefValue();
            final String comparator = criteria.getComparator();
            final boolean lessThanOrGreaterThan = comparator.equals(IPopulationEngine.GT) || comparator.equals(IPopulationEngine.LT);

            if ((NumberUtils.isNumber(value) && lessThanOrGreaterThan) || criteria.getAnalysisParameter().isDateOfBirth()) {
                return "";
            } else {
                return "'";
            }
        }

        void compareExpression(StringBuffer sql, String attribute, PopulationCriteria criteria) {

            String value = criteria.getRefValue();
            final String comparator = criteria.getComparator();

            // escape single quotes
            value = StringUtils.replace(value, "'", "''");

            // if date of birth format correctly
            if (criteria.getAnalysisParameter().isDateOfBirth()) {
                value = "to_date('" + value + "','" + Formatter.STORED_DATE_PATTERN + "')";
            } else if (IPopulationEngine.LIKE.equals(comparator)) {
                // if like put wildcard characters and make upper case
                value = QUERY_WILDCARD_CHAR + value.toUpperCase() + QUERY_WILDCARD_CHAR;
                attribute = "upper(" + attribute + ")";
            } else if (criteria.getAnalysisParameter().isActiveAttribute()) {
                if (ISearchConstants.INACTIVE.equals(value)) {
                    value = "F";
                }
            }

            // enclose value in quotes if necessary
            String quotes = getQuotes(criteria);
            value = quotes + value + quotes;

            // append attribute and comparator
            sql.append(attribute).append(comparator);

            // only append value if comparator is not null
            if (!criteriaHasNullComparator(criteria)) sql.append(value);
        }

        protected void process(StringBuffer sql, List criterias, String nodeAlias, String nodeClass) throws TalentStudioException {
            PopulationCriteria criteria = (PopulationCriteria) criterias.get(0);
            if (criteria.getType().equals(IPopulationEngine.BRCKT_TYPE_)) {
                sql.append(IPopulationEngine.RIGHT_BRCKT_);
                return;
            }
            if (criteria.getOperator() != null) sql.append(criteria.getOperator());
            sql.append("(");
            getCriteriaCompiler(criteria.getAnalysisParameter()).process(sql, criteria, nodeAlias);
            sql.append(")");
        }

        protected boolean criteriaHasNullComparator(PopulationCriteria criteria) {
            return IPopulationEngine.ISNULL.equals(criteria.getComparator());
        }

        abstract String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix);

        abstract void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias);

        abstract StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value);

        protected static final String QUERY_WILDCARD_CHAR = "%";
    }

    class CoreCriteriaCompiler extends DefaultCriteriaCompiler {

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {
            final String attribute = nodeAlias + "." + criteria.getAttributeName();
            if (!criteriaHasNullComparator(criteria)) {
                compareExpression(sql, attribute, criteria);
            } else {
                sql.append(attribute);
                sql.append(" is null ");
            }
        }

        StringBuffer getCriteriaForAttribute(AnalysisParameter metric, String nodeAlias, String attPrefix) {
            return null;
        }

        StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            return null;
        }

        String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return nodeAlias + "." + attr.getName();
        }

        void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
        }
    }

    class CoreDetailsAttCriteriaCompiler extends DefaultCriteriaCompiler {

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {
            sql.append(" exists (");
            addSelectClauseWhereExists(sql, "coreDet");
            sql.append(" where ");
            addCriteriaForAttribute(sql, nodeAlias, "coreDet");
            addCompareExpresion(criteria, sql, "coreDet");
            sql.append(")");
        }

        protected void addCriteriaForAttribute(StringBuffer sql, String nodeAlias, String attrAlias) {
            sql.append(" ").append(attrAlias).append(".id = ").append(nodeAlias).append(".coreDetail.id ");
        }

        StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            StringBuffer sql = new StringBuffer();
            sql.append(AND).append(nodeAlias).append(".").append(attr.getName()).append(EQ).append("'").append(value).append("'");
            return sql;
        }

        protected void addSelectClauseWhereExists(StringBuffer sql, String attrAlias) {
            sql.append(" select ").append(attrAlias).append(".id from CoreDetail ").append(attrAlias);
        }

        private void addCompareExpresion(PopulationCriteria criteria, StringBuffer sql, String prefix) {
            final String attribute = prefix + "." + criteria.getAttributeName().substring(criteria.getAttributeName().indexOf('.') + 1);
            sql.append(AND);
            if (!criteriaHasNullComparator(criteria)) {
                compareExpression(sql, attribute, criteria);
            } else {
                sql.append(attribute);
                sql.append(" is null ");
            }

        }

        StringBuffer getCriteriaForAttribute(AnalysisParameter metric, String nodeAlias, String attPrefix) {
            return null;
        }

        String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return nodeAlias + "." + attr.getName();
        }

        public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
        }
    }

    class DerivedAttCriteriaCompiler extends DefaultCriteriaCompiler {

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {

            final AnalysisParameter attribute = criteria.getAnalysisParameter();
            final String comparator = criteria.getComparator();

            // add main part of query
            sql.append(" exists (select da.nodeId from ").append(attribute.getClassDerivedAttribute().getName());
            sql.append(" da where ");
            sql.append(getCriteriaForAttribute(attribute, nodeAlias, "da"));
            sql.append(" AND da.associationNumber").append(comparator);

            // get ref value - add special characters if like
            String value = criteria.getRefValue();
            if (IPopulationEngine.LIKE.equals(comparator)) value = QUERY_WILDCARD_CHAR + value + QUERY_WILDCARD_CHAR;

            // only add to query if not the "is null" comparator
            if (!criteriaHasNullComparator(criteria)) sql.append(value);

            sql.append(")");
        }

        StringBuffer getCriteriaForAttribute(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return new StringBuffer(nodeAlias + ".id = " + attPrefix + ".nodeId AND " + attPrefix + ".qualifierId =" + attr.getIndexDerivedAttribute());
        }

        StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            StringBuffer criteriaForAttribute = getCriteriaForAttribute(attr, nodeAlias, attrAlias);
            String attrForSelectClause = getAttrForSelectClause(attr, nodeAlias, attrAlias);

            StringBuffer sql = new StringBuffer();
            sql.append(AND).append(criteriaForAttribute);
            sql.append(AND).append(attrForSelectClause).append(EQ).append(value);
            return sql;
        }

        String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return attPrefix + ".associationNumber";
        }

        public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
            sql.append(", ").append(attr.getClassDerivedAttribute().getName()).append(" ").append(attrAlias).append(" ");
        }
    }

    class ExtendedAttCriteriaCompiler extends DefaultCriteriaCompiler {

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {
            if (criteriaHasNullComparator(criteria)) sql.append(" not");
            sql.append(" exists (");
            addSelectClauseWhereExists(sql, criteria, "att");
            sql.append(" where ");
            addCriteriaForAttribute(sql, nodeAlias, criteria, "att");
            addCompareExpression(criteria, sql, "att");
            sql.append(")");
        }

        protected void addCriteriaForAttribute(StringBuffer sql, String nodeAlias, PopulationCriteria criteria, String attrAlias) {
            sql.append(" ").append(attrAlias).append(".node.id = ").append(nodeAlias).append(".id ");
            sql.append(AND).append(getCriteriaForAttribute(criteria.getAnalysisParameter(), nodeAlias, attrAlias));
        }

        StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            StringBuffer sql = new StringBuffer();
            sql.append(AND).append(attrAlias).append(".value").append(EQ).append("'").append(value).append("'");
            // and the dynamic_attribute id
            StringBuffer criteriaForAttribute = getCriteriaForAttribute(attr, nodeAlias, attrAlias);
            sql.append(AND).append(criteriaForAttribute);
            return sql;
        }

        protected void addSelectClauseWhereExists(StringBuffer sql, PopulationCriteria criteria, String attrAlias) {
            sql.append(" select ").append(attrAlias).append(".id from NodeExtendedAttribute ").append(attrAlias);
        }

        private void addCompareExpression(PopulationCriteria criteria, StringBuffer sql, String prefix) {
            if (!criteriaHasNullComparator(criteria)) {
                sql.append(AND);
                compareExpression(sql, prefix + ".value", criteria);
            }
        }

        StringBuffer getCriteriaForAttribute(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return new StringBuffer(" " + attPrefix + ".dynamicAttribute.id = " + attr.getName());
        }

        String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return attPrefix + ".value";
        }

        public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
            sql.append(" join ").append(nodeAlias).append(".extendedAttributes ").append(attrAlias).append(" ");
        }
    }

    class QuestionnaireAttCriteriaCompiler extends ExtendedAttCriteriaCompiler {

        protected void addSelectClauseWhereExists(StringBuffer sql, PopulationCriteria criteria, String attrAlias) {
            sql.append(" select ").append(attrAlias).append(".id from Questionnaire ").append(getQueAlias(attrAlias)).append(", NodeExtendedAttribute ").append(attrAlias).append(", QuestionnaireWorkflow ").append(getWorkflowAlias(attrAlias)).append(" ");
        }

        protected void addCriteriaForAttribute(StringBuffer sql, String nodeAlias, PopulationCriteria criteria, String attrAlias) {
            sql.append(getCriteriaForAttribute(criteria.getAnalysisParameter(), nodeAlias, attrAlias));
        }

        /*StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            StringBuffer sql = super.addMetricValue(attr, nodeAlias, attrAlias, value);
            final String qAlias = getQueAlias(attrAlias);
            final  String qwAlias = getWorkflowAlias(attrAlias);
            sql.append(AND).append(qAlias).append(".questionnaireWorkflow.id").append(EQ).append(qwAlias).append(".id");
            sql.append(AND).append(qwAlias).append(".id").append(EQ).append(attr.getQuestionnaireWorkflowId());
            sql.append(AND).append(attrAlias).append(".node.id").append(EQ).append(qAlias).append(".id");
            sql.append(AND).append(qAlias).append(".subjectId").append(EQ).append(nodeAlias).append(".id ");
            return sql;
        }*/

        StringBuffer getCriteriaForAttribute(AnalysisParameter attr, String nodeAlias, String attrAlias) {
            StringBuffer sql = new StringBuffer();
            final String qAlias = getQueAlias(attrAlias);
            final String workflowAlias = getWorkflowAlias(attrAlias);

            sql.append(qAlias).append(".questionnaireWorkflowId = ").append(workflowAlias).append(".id")
                    .append(AND).append(workflowAlias).append(".id = ").append(attr.getQuestionnaireWorkflowId())
                    .append(AND).append(" ( ").append(qAlias).append(".completed = 'T' or ").append(workflowAlias).append(".workflowType = 'INFO_FORM')")
                    .append(AND).append(attrAlias).append(".node.id = ").append(qAlias).append(".id").append(AND).append(qAlias).append(".subjectId = ")
                    .append(nodeAlias).append(".id ")
                    .append(AND).append(attrAlias).append(".dynamicAttribute.id = ").append(attr.getName());
            if (StringUtils.hasText(attr.getRole())) {
                sql.append(AND).append(qAlias).append(".role.id = '").append(attr.getRole()).append("'");
            }

            return sql;
        }

        public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
            sql.append(", NodeExtendedAttribute ").append(attrAlias).append(", QuestionnaireWorkflow ").append(getWorkflowAlias(attrAlias))
                    .append(", Questionnaire ").append(getQueAlias(attrAlias)).append(" ");
        }
    }

    class QuestionnaireWorkflowAttCriteriaCompiler extends ExtendedAttCriteriaCompiler {

        protected void process(StringBuffer sql, List criterias, String nodeAlias, String nodeClass) throws TalentStudioException {
            PopulationCriteria questionCriteria = (PopulationCriteria) criterias.get(0);
            if (sql.toString().endsWith(")")) sql.append(" and");
            sql.append(" exists (select participant.primaryKey.subjectId from WorkflowParticipant participant");
            sql.append(" where participant.primaryKey.subjectId = n.id  ");
            sql.append(" and ").append(questionCriteria.getAttributeName()).append("=").append(questionCriteria.getRefValue()).append(")");
        }
    }


    class AssocArtefactAttCriteriaCompiler extends DefaultCriteriaCompiler {

        private String getClassNameFromCriteria(String prefix) {
            if (prefix.endsWith(IPopulationEngine.SUBJECT_ASSOCIATION_ATTR))
                return SUBJECT_ASSOCIATION_CLASSNAME;
            else if (prefix.endsWith(IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR))
                return SUBJECT_PRIMARY_ASSOCIATION_CLASSNAME;
            else if (prefix.endsWith(IPopulationEngine.SUBJECT_SECONDARY_ASSOCIATIONS_ATTR))
                return SUBJECT_SECONDARY_ASSOCIATION_CLASSNAME;
            else if (prefix.endsWith(IPopulationEngine.SOURCE_ASSOCIATIONS_ATTR) || prefix.endsWith(IPopulationEngine.TARGET_ASSOCIATIONS_ATTR))
                return POSITION_ASSOCIATION_CLASSNAME;
            else if (prefix.endsWith(IPopulationEngine.CHILDREN_ATTR))
                return POSITION_CLASSNAME;
            else
                return null;

        }

        private String getAssociationLink(String prefix, String sufix, String nodeClass) {
            if (prefix.endsWith(IPopulationEngine.SUBJECT_ASSOCIATION_ATTR) || (prefix.endsWith(IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR)) || (prefix.endsWith(IPopulationEngine.SUBJECT_SECONDARY_ASSOCIATIONS_ATTR)))
                if (sufix.startsWith(IPopulationEngine.POSITION_ATTR))
                    return IPopulationEngine.SUBJECT_ATTR;
                else if (sufix.startsWith(IPopulationEngine.SUBJECT_ATTR))
                    return IPopulationEngine.POSITION_ATTR;
                else if (nodeClass.equals(IPopulationEngine.P_POS_TYPE_))
                    return IPopulationEngine.POSITION_ATTR;
                else
                    return IPopulationEngine.SUBJECT_ATTR;
            else if (prefix.endsWith(IPopulationEngine.SOURCE_ASSOCIATIONS_ATTR))
                return IPopulationEngine.SOURCE_ATTR;
            else if (prefix.endsWith(IPopulationEngine.TARGET_ASSOCIATIONS_ATTR))
                return IPopulationEngine.TARGET_ATTR;
            else if (prefix.endsWith(IPopulationEngine.CHILDREN_ATTR))
                return IPopulationEngine.PARENT_ATTR;
            else
                return null;

        }

        protected void process(StringBuffer sql, List criterias, String nodeAlias, String nodeClass) throws TalentStudioException {
            final PopulationCriteria firstCriteria = (PopulationCriteria) criterias.get(0);
            String relatedNodeAlias = nodeAlias + "_assoc_";

            String[] splitName = firstCriteria.getAnalysisParameter().splitAttributeName();
            String prefix = splitName[0];
            String sufix = splitName[1];
            String parentPrefix = prefix.startsWith(IPopulationEngine.PARENT_ATTR) ? AnalysisAttributeHelper.DELIMITER + IPopulationEngine.PARENT_ATTR : "";
            if (firstCriteria.getOperator() != null) sql.append(firstCriteria.getOperator());
            sql.append("(");

            if (criteriaHasNullComparator(firstCriteria)) sql.append(" not");
            sql.append(" exists (select ").append(relatedNodeAlias).append(".id from ").append(getClassNameFromCriteria(prefix));
            sql.append(" ").append(relatedNodeAlias).append(" where ").append(nodeAlias).append(parentPrefix).append(".id = ").append(relatedNodeAlias).append(".").append(getAssociationLink(prefix, sufix, nodeClass)).append(".id ");
            if (!hasOrgUnitNullCriteria(criterias)) {
                List<PopulationCriteria> transformCriterias = new ArrayList<PopulationCriteria>(criterias.size());
                for (int i = 0; i < criterias.size(); i++) {
                    transformCriterias.add(changeAttributeName((PopulationCriteria) criterias.get(i), relatedNodeAlias));
                }
                transformCriterias.get(0).setOperator(null);
                compile(sql, transformCriterias, relatedNodeAlias);
            }
            sql.append("))");
        }

        private boolean hasOrgUnitNullCriteria(List criterias) {
            for (int i = 0; i < criterias.size(); i++) {
                final PopulationCriteria populationCriteria = (PopulationCriteria) criterias.get(i);
                if (populationCriteria.getAttributeName().indexOf("position.organisationUnit") > 0 && criteriaHasNullComparator(populationCriteria))
                    return true;
            }

            return false;
        }

        private PopulationCriteria changeAttributeName(PopulationCriteria criteria, String nodeAlias) {
            final PopulationCriteria cloned = (PopulationCriteria) criteria.clone();

            final String[] splitName = criteria.getAnalysisParameter().splitAttributeName();
            final String sufix = splitName[1];
            cloned.setAttributeName(sufix);
            if (isFromNodeinAssociation(sufix)) {
                cloned.setAttributeName(sufix.substring(sufix.indexOf('.') + 1));
                cloned.setModifiedAlias(nodeAlias + "." + sufix.substring(0, sufix.indexOf('.')));
            } else {
                cloned.setAttributeName(sufix);
            }

            return cloned;
        }

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {
        }

        StringBuffer getCriteriaForAttribute(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return null;
        }

        StringBuffer addMetricValue(AnalysisParameter attr, String nodeAlias, String attrAlias, String value) {
            return null;
        }

        String getAttrForSelectClause(AnalysisParameter attr, String nodeAlias, String attPrefix) {
            return nodeAlias + "." + getAssociatedArtefactAttribute(attr.getName());
        }

        public void addMetricAttributeToFromClause(AnalysisParameter attr, StringBuffer sql, String nodeAlias, String attrAlias) {
            sql.append(", ").append(SUBJECT_PRIMARY_ASSOCIATION_CLASSNAME).append(" ").append(attrAlias).append(" ");
        }

        private boolean isFromNodeinAssociation(String name) {
            return name.startsWith(IPopulationEngine.SUBJECT_ATTR) || name.startsWith(IPopulationEngine.POSITION_ATTR) || name.startsWith(IPopulationEngine.TARGET_ATTR) || name.startsWith(IPopulationEngine.SOURCE_ATTR);
        }

        private String getAssociatedArtefactAttribute(String name) {
            final int i = name.indexOf(".");
            final String prefix = name.substring(0, i);
            return StringUtils.replace(name, prefix, prefix + ".elements");
        }

        private static final String SUBJECT_ASSOCIATION_CLASSNAME = "SubjectAssociation";
        private static final String SUBJECT_PRIMARY_ASSOCIATION_CLASSNAME = "SubjectPrimaryAssociation";
        private static final String SUBJECT_SECONDARY_ASSOCIATION_CLASSNAME = "SubjectSecondaryAssociation";
        private static final String POSITION_ASSOCIATION_CLASSNAME = "PositionAssociation";
        private static final String POSITION_CLASSNAME = "Position";
    }

    private List<List<PopulationCriteria>> groupCriterias(List criteria) {
        PopulationCriteria previousPopulationCriteria = null;
        List<PopulationCriteria> currentList = null;
        List<List<PopulationCriteria>> results = new ArrayList<List<PopulationCriteria>>();
        for (Iterator iterator = criteria.iterator(); iterator.hasNext();) {
            PopulationCriteria populationCriteria = (PopulationCriteria) iterator.next();

            if (populationCriteria.getAnalysisParameter() != null && populationCriteria.getAnalysisParameter().isAssociatedArtefactAttribute()) {
                if (previousPopulationCriteria != null) {
                    if (previousPopulationCriteria.getAnalysisParameter().splitAttributeName()[0].equals(populationCriteria.getAnalysisParameter().splitAttributeName()[0])) {
                        currentList.add(populationCriteria);
                    } else {
                        previousPopulationCriteria = populationCriteria;
                        currentList = new ArrayList<PopulationCriteria>();
                        currentList.add(populationCriteria);
                        results.add(currentList);
                    }
                } else {
                    previousPopulationCriteria = populationCriteria;
                    currentList = new ArrayList<PopulationCriteria>();
                    currentList.add(populationCriteria);
                    results.add(currentList);
                }
            } else {
                currentList = new ArrayList<PopulationCriteria>();
                currentList.add(populationCriteria);
                results.add(currentList);
                currentList = null;
                previousPopulationCriteria = null;
            }
        }
        return results;
    }

    class UserDetailsAttCriteriaCompiler extends CoreCriteriaCompiler {

        void process(StringBuffer sql, PopulationCriteria criteria, String nodeAlias) {
            if (!criteriaHasNullComparator(criteria)) {
                super.process(sql, criteria, nodeAlias);
            } else {
                sql.append(" not exists ");
                sql.append(" ( select u.id from User u where u.id = ").append(nodeAlias).append(".user.id )");
            }
        }
    }

    private ExtendedAttCriteriaCompiler extendedAttCriteriaCompiler;
    private CoreCriteriaCompiler coreAttCriteriaCompiler;
    private DerivedAttCriteriaCompiler derivedAttCriteriaCompiler;
    private QuestionnaireAttCriteriaCompiler questionnaireAttCriteriaCompiler;
    private QuestionnaireWorkflowAttCriteriaCompiler questionnaireWorkflowAttCriteriaCompiler;
    private AssocArtefactAttCriteriaCompiler assocArtefactAttCriteriaCompiler;
    private CoreDetailsAttCriteriaCompiler coreDetailAttCriteriaCompiler;
    private UserDetailsAttCriteriaCompiler userDetailAttCriteriaCompiler;

    private static final String AND = IPopulationEngine.AND;
    private static final String EQ = "=";
}
