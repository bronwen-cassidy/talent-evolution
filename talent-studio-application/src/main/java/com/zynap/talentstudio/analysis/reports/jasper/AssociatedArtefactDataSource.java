package com.zynap.talentstudio.analysis.reports.jasper;

import net.sf.jasperreports.engine.JRField;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.QuestionAttributeValuesCollection;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.analysis.reports.GroupingAttribute;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.organisation.ArtefactAssociation;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.subjects.SubjectCommonAssociation;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: amark
 * Date: 20-Sep-2006
 * Time: 14:04:00
 *
 * Datasource for use in tabular reports for with associated arefacts.
 */
public class AssociatedArtefactDataSource extends JRCollectionDataSource {

    public AssociatedArtefactDataSource(Report report, Collection<Object> artefactAssociations, JasperDataSourceFactory jasperDataSourceFactory, User user) {
        super(report, artefactAssociations, null, jasperDataSourceFactory, null, null, 0, user);
    }

	@Override
	void sortResults(List<GroupingAttribute> groupedAttributeNames, String orderAttributeName, int orderDirection) {
		super.sortResults(groupedAttributeNames, orderAttributeName, orderDirection);
		
		Collections.sort(this.records, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				ArtefactAssociation first = (ArtefactAssociation) o1;
				ArtefactAssociation second = (ArtefactAssociation) o2;
				return first.getQualifier().compareBySortOrder(second.getQualifier());
			}
		});
	}

	public Object getFieldValue(JRField field) {

        Object value = null;

        if (currentRecord instanceof ArtefactAssociation) {
            ArtefactAssociation currentAssociation = (ArtefactAssociation) currentRecord;
            value = getValueFromAssociatedNode(field, currentAssociation);
        }

        return value;
    }

    private Object getValueFromAssociatedNode(JRField field, ArtefactAssociation currentAssociation) {

        Object value = null;

        String fieldName = field.getName();
        int index = fieldName.indexOf(AnalysisAttributeHelper.DELIMITER);
        String prefix = null;
        if (index > 0) prefix = fieldName.substring(0, index);

        if (prefix == null || AnalysisAttributeHelper.isQualifierAttribute(fieldName)) {
            value = getAssociationValue(fieldName, currentAssociation);
        } else {

            // otherwise get value from associated artefacts
            String suffix = fieldName.substring(index + 1);
            try {
                Node node = (Node) PropertyUtils.getProperty(currentAssociation, prefix);
                AnalysisParameter analysisParameter = AnalysisAttributeHelper.getAttributeFromName(suffix);

                // if is questionnaire attribute and questionanswers not loaded already load them
                if (analysisParameter.isQuestionnaireAttribute() && questionnaireAnswers == null) {
                    loadQuestionnaireAnswers(field, currentAssociation);
                }

                // sets current question collection and max dynamic position and current dynamic position
                if (currentQuestionCollection == null) getCurrentAnswers(node);

                // get value regardless
                value = getFieldValue(field, analysisParameter, node);

            } catch (Exception e) {
                logger.error("Failed to get value from associated node for field: " + field.getName() + ", " + field.getValueClassName(), e);
            }
        }

        return value;
    }

    /**
     * Get value from association itself using reflection.
     * @param fieldName
     * @param currentAssociation
     * @return String or null
     */
    private Object getAssociationValue(String fieldName, ArtefactAssociation currentAssociation) {

        Object value = null;
        if (currentDynamicPosition == 0) {
            value = AnalysisAttributeHelper.evaluateProperty(fieldName, currentAssociation);
        }

        return value;
    }

    /**
     * Load questionnaire answers.
     * <br/> The field name will always be in the format "subject.122_21" where 122 is the dynamic attribute id and 21 is the workflow id.
     * <br/> the "subject" bit is used to get the right Node from the associations.
     *
     * @param field
     * @param currentAssociation
     */
    private void loadQuestionnaireAnswers(JRField field, ArtefactAssociation currentAssociation) throws Exception {

        final String fieldName = field.getName();
        final int index = fieldName.indexOf(AnalysisAttributeHelper.DELIMITER);
        final String prefix = fieldName.substring(0, index);

        final List<Long> nodeIds = new ArrayList<Long>();
        for (int i = 0; i < records.size(); i++) {
            final ArtefactAssociation artefactAssociation = (ArtefactAssociation) records.get(i);
            final Node node = (Node) PropertyUtils.getProperty(artefactAssociation, prefix);
            nodeIds.add(node.getId());
        }

        if (!nodeIds.isEmpty()) {

            final List<AnalysisParameter> analysisParameters = new ArrayList<AnalysisParameter>();

            final String collectionPrefix = getAttributePrefix(currentAssociation);
            final List<AnalysisParameter> questionnaireAttributes = report.getQuestionnaireAttributes();
            for (AnalysisParameter parameter : questionnaireAttributes) {                
                final String[] strings = AnalysisAttributeHelper.splitQuestionnaireAttributeName(parameter);

                // split will return something like "subjectPrimaryAssociations, subject, 12"
                // or "subjectSecondaryAssociations, position, subjectPrimaryAssociations, subject, 12"
                // Therefore we work backwards to match the questionnaireAttributes.
                // If the second to last element in the array matches the prefix
                // and the third to last element matches the collection prefix we have a match.
                // If so we pass in only the last element (the dynamic attribute id) in a cloned AnalysisParameter
                final int length = strings.length;
                if (length >= 3) {
                    if (strings[length - 3].equals(collectionPrefix) && strings[length - 2].equals(prefix)) {
                        final AnalysisParameter clone = new AnalysisParameter(strings[length - 1], parameter.getQuestionnaireWorkflowId(), parameter.getRole());
                        analysisParameters.add(clone);
                    }
                }
            }

            questionnaireAnswers = factory.getPopulationEngine().findQuestionnaireAnswers(analysisParameters, nodeIds, currentUser.getId());

        } else {

            // IMPORTANT: ensures that there are no further attempts to load questionnaire answers as we load if this collection is null
            questionnaireAnswers = new HashMap<Long, QuestionAttributeValuesCollection>(0);
        }
    }

    private String getAttributePrefix(ArtefactAssociation artefactAssociation) {

        String collectionPrefix = null;
        if (artefactAssociation instanceof SubjectCommonAssociation) {
            SubjectCommonAssociation subjectCommonAssociation = (SubjectCommonAssociation) artefactAssociation;
            collectionPrefix = subjectCommonAssociation.isPrimary() ? IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR : IPopulationEngine.SUBJECT_SECONDARY_ASSOCIATIONS_ATTR;
        }

        return collectionPrefix;
    }
}
