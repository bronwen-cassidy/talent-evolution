package com.zynap.talentstudio.web.analysis.picker;


import org.w3c.dom.Document;

import com.zynap.common.util.XmlUtils;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.analysis.populations.IPopulationEngine;
import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.common.lookups.LookupType;
import com.zynap.talentstudio.common.lookups.LookupValue;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.performance.IPerformanceReviewService;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.web.analysis.AnalysisAttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.DynamicAttributesHelper;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.talentstudio.web.questionnaires.QuestionnaireHelper;
import com.zynap.talentstudio.web.utils.tree.Branch;
import com.zynap.talentstudio.web.utils.tree.ITreeContainer;
import com.zynap.talentstudio.web.utils.tree.Leaf;

import org.apache.commons.collections.CollectionUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: amark
 * Date: 01-Feb-2006
 * Time: 09:45:55
 * <p/>
 * Component that builds collection of attributes available for populations and reports.
 */
public final class PopulationCriteriaBuilder implements InitializingBean {

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public void setPerformanceReviewService(IPerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    /**
     * Initialisation callback provided for Spring.
     * <br/> Loads XML from source file location and parses it.
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {

        final Document document = XmlUtils.createFilteredDocument(sourceFile);
        XMLParser parser = new XMLParser();
        trees = parser.buildTree(document, qualifierAttributeSet, coreAttributeSets);
        useSearchableExtendedAttributesOnly = parser.isUseSearchableExtendedAttributesOnly();
        includeDerivedAttributes = parser.isIncludeDerivedAttributes();
        includeDynamicLineItemAttributes = parser.isIncludeDynamicLineItemAttributes();
        extendedAttributeTypes = parser.getExtendedAttributeTypes();
    }

    /**
     * Find branch (recurses through branches passed in.)
     *
     * @param branches main branches of the tree picker
     * @param branchId the id of the branch
     * @return AnalysisAttributeBranch or null
     */
    public AnalysisAttributeBranch findBranch(List branches, String branchId) {

        AnalysisAttributeBranch found = null;

        for (Object branche : branches) {
            AnalysisAttributeBranch branch = (AnalysisAttributeBranch) branche;
            found = (AnalysisAttributeBranch) branch.find(branchId);
            if (found != null) return found;
        }

        return found;
    }

    /**
     * Find branch by leaf id (recurses through branches passed in.)
     * <br/>
     *
     * @param branches the list of branches
     * @param leafId   the id of the leaf
     * @return AnalysisAttributeBranch or null
     * @throws com.zynap.exception.TalentStudioException
     *          under any condition
     */
    public AnalysisAttributeBranch findOrgBranchByLeafId(List branches, String leafId) throws TalentStudioException {

        String branchId = AnalysisAttributeHelper.getOrganisationUnitPrefix(leafId);
        return findBranch(branches, leafId, branchId, null);
    }

    public AnalysisAttributeBranch findBranchByLeafId(List branches, String leafId, String viewType) throws TalentStudioException {

        final AnalysisParameter parameter = AnalysisAttributeHelper.getAttributeFromName(leafId);

        // we want the prefix
        if (parameter.isQuestionnaireAttribute()) {

            // produces a branchId of _ap_2_q_1
            final QuestionnaireWorkflow queWorkflow = questionnaireWorkflowService.findWorkflowById(parameter.getQuestionnaireWorkflowId());

            String branchId = queWorkflow.isPerformanceQuestionnaire()
                    ? APPRAISALS_BRANCH_ID + QuestionnaireTreeBuilder.APPRAISAL_WORKFLOW_PREFIX + queWorkflow.getPerformanceReview().getId()
                    : QUESTIONNAIRE_BRANCH_ID;
            branchId += QuestionnaireTreeBuilder.QUESTIONNAIRE_PREFIX + queWorkflow.getId();

            String pathWithoutId = parameter.getNestedPathWithoutId();
            if (StringUtils.hasText(pathWithoutId)) {
                branchId = pathWithoutId + "." + branchId;
            }

            AnalysisAttributeBranch workflowBranch = findBranch(branches, branchId);

            if (workflowBranch == null) {

                // find the correct parent branch as in position.subjectPrimaryAssociations.appraisals
                AnalysisAttributeBranch routeBranch;
                if (StringUtils.hasText(pathWithoutId)) {
                    routeBranch = findBranch(branches, pathWithoutId);
                } else {
                    // top level branch no path associated
                    routeBranch = (AnalysisAttributeBranch) branches.get(0);
                }

                // always rebuild these branches
                routeBranch.removeChild(APPRAISALS_BRANCH_ID);
                routeBranch.removeChild(QUESTIONNAIRE_BRANCH_ID);

                final Collection<QuestionnaireDefinition> questionnaireDefinitions = getQuestionnaireDefinitions();
                new QuestionnaireTreeBuilder().addQuestionnaires(routeBranch, questionnaireDefinitions);
                List<Branch> subBranches = new ArrayList<Branch>();
                subBranches.add(routeBranch);
                workflowBranch = findBranch(subBranches, branchId);
            }

            if (!workflowBranch.isHasChildren()) {
                updateQuestionnaireModel(workflowBranch, queWorkflow);
            }

            return workflowBranch;
        }
        // else return branch

        return findCoreBranch(branches, leafId, viewType);
    }


    private AnalysisAttributeBranch findCoreBranch(List branches, String leafId, String viewType) throws TalentStudioException {

        String branchId = AnalysisAttributeHelper.getPrefix(leafId);

        if (AnalysisAttributeHelper.isDocumentAttribute(leafId)) {
            branchId = "portfolioItems";
        }

        return findBranch(branches, leafId, branchId, viewType);
    }

    private AnalysisAttributeBranch findBranch(List branches, String leafId, String branchId, String viewType) {
        AnalysisAttributeBranch found = null;

        for (Iterator iterator = branches.iterator(); iterator.hasNext();) {
            AnalysisAttributeBranch branch = (AnalysisAttributeBranch) iterator.next();
            found = branch.findBranch(branchId, leafId);
            if (found != null) break;
        }
        if (found != null && found.getLeaves().isEmpty()) {
            addLeaves(found, viewType);

        }

        return found;
    }

    private void addLeaves(AnalysisAttributeBranch found, String viewType) {
        // assign the leaves
        if (found.isDocumentBranch()) {
            addCoreLeaves(found, viewType);
        } else {
            addNodeLeaves(found, viewType);
        }
    }

    /**
     * Build up labels to include branch labels to give full "path".
     *
     * @param artefactType                  type of artefact position, subject or questionnaire
     * @param analysisAttributeWrapperBeans list of analysis attribute deletages
     * @param viewType
     * @throws TalentStudioException if any errors
     */
    public void setAttributeLabels(String artefactType, List<AnalysisAttributeWrapperBean> analysisAttributeWrapperBeans, String viewType) throws TalentStudioException {

        if (analysisAttributeWrapperBeans != null) {
            for (Iterator iterator = analysisAttributeWrapperBeans.iterator(); iterator.hasNext();) {
                AnalysisAttributeWrapperBean analysisAttributeWrapperBean = (AnalysisAttributeWrapperBean) iterator.next();
                setAttributeLabel(viewType, artefactType, analysisAttributeWrapperBean);
            }
        }
    }

    /**
     * Build up label to include branch labels to give full "path".
     *
     * @param viewType                     - the view needed by attribute views
     * @param type
     * @param analysisAttributeWrapperBean the wrapper  @throws TalentStudioException any errors
     * @throws com.zynap.exception.TalentStudioException
     *
     */
    public void setAttributeLabel(String viewType, String type, AnalysisAttributeWrapperBean analysisAttributeWrapperBean) throws TalentStudioException {

        String label = getLabel(type, analysisAttributeWrapperBean, viewType);
        analysisAttributeWrapperBean.setAttributeLabel(label);
    }

    /**
     * Get all attributes available for artefact type (will use default view.)
     *
     * @return AnalysisAttributeCollection
     * @throws TalentStudioException on error
     */
    public AnalysisAttributeCollection buildCollection() throws TalentStudioException {

        List<FormAttribute> results = new ArrayList<FormAttribute>();

        final String subjectType = IPopulationEngine.P_SUB_TYPE_;
        AttributeSet subjectAttributeSet = buildSubjectAttributes(results, subjectType);

        // returns just the questionnaireDefinitions
        List<QuestionnaireDefinition> questionnaireDefinitions = buildQuestionnairesAttributeSet(subjectType, subjectAttributeSet);
        // get map of attributes for qualifiers
        final Map<String, AttributeWrapperBean> qualifierAttributeWrappers = getAssociationQualifiers();

        // get all attribute definitions for position
        final String positionType = IPopulationEngine.P_POS_TYPE_;
        if (trees.get(positionType) != null) {
            buildPositionAttributes(results);
        }

        buildOrganisationAttributes(results);
        return new AnalysisAttributeCollection(results, questionnaireDefinitions, qualifierAttributeWrappers, questionnaireHelper);
    }

    /**
     * Used to build the executive summary only.
     * The executive summary for subjects is special as it requires position attributes regardless of whether the position tree exists.
     *
     * @return AnaysisAttributeCollection of a subject tree with position attribute branches.
     * @throws TalentStudioException
     */
    public AnalysisAttributeCollection buildExecSummaryCollection() throws TalentStudioException {
        List<FormAttribute> results = new ArrayList<FormAttribute>();

        final String subjectType = IPopulationEngine.P_SUB_TYPE_;
        AttributeSet subjectAttributeSet = buildSubjectAttributes(results, subjectType);

        // returns just the questionnaireDefinitions
        List<QuestionnaireDefinition> questionnaireDefinitions = buildQuestionnairesAttributeSet(subjectType, subjectAttributeSet);
        // get map of attributes for qualifiers
        final Map<String, AttributeWrapperBean> qualifierAttributeWrappers = getAssociationQualifiers();
        buildPositionAttributes(results);

        return new AnalysisAttributeCollection(results, questionnaireDefinitions, qualifierAttributeWrappers, questionnaireHelper);
    }

    private List<QuestionnaireDefinition> buildQuestionnairesAttributeSet(String subjectType, AttributeSet subjectAttributeSet) throws TalentStudioException {
        List<QuestionnaireDefinition> questionnaireDefinitions = includeQuestionnaires(subjectAttributeSet)
                ? getQuestionnaireDefinitions()
                : new ArrayList<QuestionnaireDefinition>();

        // make sure we clear out the questionnaires at the beginning
        if (includeQuestionnaires(subjectAttributeSet)) {
            final List<AnalysisAttributeBranch> branches = trees.get(subjectType);
            AnalysisAttributeBranch branch = branches.get(0);
            branch.removeChild(APPRAISALS_BRANCH_ID);
            branch.removeChild(QUESTIONNAIRE_BRANCH_ID);

            // and add them
            new QuestionnaireTreeBuilder().addQuestionnaires(branch, questionnaireDefinitions);
        }
        return questionnaireDefinitions;
    }

    private AttributeSet buildSubjectAttributes(List<FormAttribute> results, String subjectType) {
        AttributeSet subjectAttributeSet = getAttributeSet(subjectType, null);
        results.addAll(copyAttributeWrapperBeans(subjectAttributeSet));

        if (subjectAttributeSet == null || subjectAttributeSet.isIncludeDynamicAttributes()) {
            results.addAll(getExtendedAttributes(subjectType, subjectAttributeSet));
            results.addAll(getDerivedAttributes(subjectType));

            // add in the document information (need to test without)
            results.addAll(copyAttributeWrapperBeans(getAttributeSet("PI", null)));
        }
        return subjectAttributeSet;
    }

    private void buildOrganisationAttributes(List<FormAttribute> results) {
        final String organisationType = IPopulationEngine.O_UNIT_TYPE_;
        AttributeSet organisationAttributeSet = getAttributeSet(organisationType, null);
        if (organisationAttributeSet == null) return;

        results.addAll(copyAttributeWrapperBeans(organisationAttributeSet));
        if (organisationAttributeSet.isIncludeDynamicAttributes()) {
            List<FormAttribute> formAttributes = getExtendedAttributes(organisationType, organisationAttributeSet);
            for (FormAttribute attribute : formAttributes) {
                AttributeWrapperBean attr = (AttributeWrapperBean) attribute;
                attr.setIsOrgunitBranch(true);
            }
            results.addAll(formAttributes);
        }
    }

    private void buildPositionAttributes(List<FormAttribute> results) {
        final String positionType = IPopulationEngine.P_POS_TYPE_;
        AttributeSet positionAttributeSet = getAttributeSet(positionType, null);
        results.addAll(copyAttributeWrapperBeans(positionAttributeSet));
        results.addAll(getExtendedAttributes(positionType, positionAttributeSet));
        results.addAll(getDerivedAttributes(positionType));
    }

    /**
     * Get all attributes available for artefact view.
     *
     * @param artefactType type of artefact position {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_} ,
     *                     subject({@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_} ) or questionnaire ({@link com.zynap.talentstudio.organisation.Node#QUESTIONNAIRE_TYPE })
     * @param viewType     which view
     * @return AnalysisAttributeCollection
     * @throws TalentStudioException any errors
     */
    public AnalysisAttributeCollection buildCollection(String artefactType, String viewType) throws TalentStudioException {

        List<FormAttribute> results = new ArrayList<FormAttribute>();

        AttributeSet found = getAttributeSet(artefactType, viewType);
        results.addAll(copyAttributeWrapperBeans(found));
        results.addAll(getExtendedAttributes(artefactType, found));
        results.addAll(getDerivedAttributes(artefactType));

        List<QuestionnaireDefinition> questionnaireDefinitions = buildQuestionnairesAttributeSet(artefactType, found);
        // get map of attributes for qualifiers
        final Map<String, AttributeWrapperBean> qualifierAttributeWrappers = getAssociationQualifiers();

        return new AnalysisAttributeCollection(results, questionnaireDefinitions, qualifierAttributeWrappers, questionnaireHelper);
    }

    /**
     * Set attributes as leaves on branch.
     *
     * @param branch   the branch
     * @param viewType the view type
     * @throws TalentStudioException any errors
     */
    public void updateBranch(AnalysisAttributeBranch branch, String viewType) throws TalentStudioException {

        if (branch != null && !branch.hasLeaves()) {

            // include qualifier attributes
            if (branch.includeQualifierAttributes()) {
                addAssociationLeaves(branch);
            }
            if (branch.isSubject()) {
                addSubjectLeaves(branch, viewType);
            } else if (branch.isQuestionnaireWorkflow()) {
                addQuestionnaireLeaves(branch);
            } else if (branch.isPosition()) {
                addNodeLeaves(branch, viewType);
            } else if (branch.isSubjectAssociation()) {
                final AttributeSet attributeSet = getAttributeSet(branch.getType(), viewType);
                final List<FormAttribute> coreAttributes = copyAttributeWrapperBeans(attributeSet);
                addLeaves(branch, coreAttributes, false);
            } else if (branch.isDocumentBranch() && branch.getLeaves().isEmpty()) {
                final AttributeSet attributeSet = getAttributeSet(branch.getType(), viewType);
                final List<FormAttribute> coreAttributes = copyAttributeWrapperBeans(attributeSet);
                addLeaves(branch, coreAttributes, true);
            } else if (branch.isOrganisationUnitBranch()) {
                // clear the leaves and add them
                branch.getLeaves().clear();
                addNodeLeaves(branch, viewType);
            }
        }
    }

    private void addAssociationLeaves(AnalysisAttributeBranch branch) {
        // remove last part from branch prefix
        final String prefix = AnalysisAttributeHelper.getPrefix(branch.getPrefix());

        final List<FormAttribute> attributeWrapperBeans = qualifierAttributeSet.getAttributeWrapperBeans();
        for (Iterator<FormAttribute> iterator = attributeWrapperBeans.iterator(); iterator.hasNext();) {
            FormAttribute qualifierAttributeWrapperBean = iterator.next();
            final String id = buildId(prefix, qualifierAttributeWrapperBean.getId());
            branch.addLeaf(new Leaf(id, qualifierAttributeWrapperBean.getLabel()));
        }
    }

    /**
     * Add leaves for position core, extended and derived attributes to branch.
     *
     * @param branch   the branch
     * @param viewType the view (subject view, position view)
     */
    private void addNodeLeaves(AnalysisAttributeBranch branch, String viewType) {

        final AttributeSet attributeSet = addCoreLeaves(branch, viewType);
        addExtendedAttributeLeaves(branch, attributeSet);
        addDerivedAttributeLeaves(branch);
    }

    /**
     * Add leaves for subject core, extended and derived attributes to branch, plus add branches for questionnaires and appraisals.
     *
     * @param branch   the branch
     * @param viewType the view (subject view, position view)
     * @throws com.zynap.exception.TalentStudioException
     *          any errors
     */
    private void addSubjectLeaves(AnalysisAttributeBranch branch, String viewType) throws TalentStudioException {

        final AttributeSet attributeSet = addCoreLeaves(branch, viewType);

        if (attributeSet == null || attributeSet.isIncludeDynamicAttributes()) {
            addExtendedAttributeLeaves(branch, attributeSet);
            addDerivedAttributeLeaves(branch);
        }
        branch.removeChild(QUESTIONNAIRE_BRANCH_ID);
        branch.removeChild(APPRAISALS_BRANCH_ID);

        if (includeQuestionnaires(attributeSet)) {
            final Collection<QuestionnaireDefinition> questionnaireDefinitions = getQuestionnaireDefinitions();
            new QuestionnaireTreeBuilder().addQuestionnaires(branch, questionnaireDefinitions);
        }
    }

    private AttributeSet addCoreLeaves(AnalysisAttributeBranch branch, String viewType) {

        final AttributeSet attributeSet = getAttributeSet(branch.getType(), viewType);
        final List<FormAttribute> coreAttributes = copyAttributeWrapperBeans(attributeSet);
        addLeaves(branch, coreAttributes, true);
        return attributeSet;
    }

    /**
     * Adds the leaves to the questionnaire that has been selected, the leaves include the groups and questions.
     *
     * @param workflowBranch - the questionnaire or appraisal workflow workflowBranch.
     * @throws com.zynap.exception.TalentStudioException
     *          thrown by db queries
     */
    private void addQuestionnaireLeaves(AnalysisAttributeBranch workflowBranch) throws TalentStudioException {

        final String[] parameters = StringUtils.delimitedListToStringArray(workflowBranch.getId(), AnalysisAttributeHelper.QUESTION_CRITERIA_DELIMITER);
        final String workflowId = parameters[parameters.length - 1];
        // this occurs only when we have the root questionnaire or appraisal branches
        if (!org.apache.commons.lang.StringUtils.isNumeric(workflowId)) return;
        Long queWorkflowId = new Long(workflowId);
        final QuestionnaireWorkflow workflow = questionnaireWorkflowService.findWorkflowById(queWorkflowId);
        updateQuestionnaireModel(workflowBranch, workflow);
    }

    private void updateQuestionnaireModel(AnalysisAttributeBranch workflowBranch, QuestionnaireWorkflow workflow) throws TalentStudioException {

        final QuestionnaireDefinition definition = workflow.getQuestionnaireDefinition();
        QuestionnaireDefinitionModel definitionModel = definition.getQuestionnaireDefinitionModel();

        if (extendedAttributeTypes != null && extendedAttributeTypes.length > 0) {
            definitionModel = questionnaireHelper.filterQuestionnaireDefinition(extendedAttributeTypes, includeDynamicLineItemAttributes, definition);
        }

        new QuestionnaireTreeBuilder().updateQuestionnaires(definitionModel, getAppraisalRoles(), workflow, workflowBranch);
    }

    /**
     * Copy attributes wrapper beans from AttributeSet to new List.
     * <br/> The AttributeSet can be null in which case an empty List is returned.
     *
     * @param attributeSet the attributeSet a set delegate of attributes
     * @return List (never null)
     */
    List<FormAttribute> copyAttributeWrapperBeans(AttributeSet attributeSet) {
        final List<FormAttribute> attributes = new ArrayList<FormAttribute>();
        if (attributeSet != null) {
            attributes.addAll(attributeSet.getAttributeWrapperBeans());
        }

        return attributes;
    }

    /**
     * Determine whether or not questionnaires should be included.
     * <br/> Does this by checking attributeSet.isIncludeQuestionnaires()
     * <br/> However if the attributeset is null (as is the case with metrics) returns true so questionnaires are included.
     *
     * @param attributeSet the attributeSet a set delegate of attributes
     * @return true or false
     */
    private boolean includeQuestionnaires(AttributeSet attributeSet) {
        return attributeSet == null || attributeSet.isIncludeQuestionnaires();
    }

    private void addExtendedAttributeLeaves(AnalysisAttributeBranch branch, AttributeSet attributeSet) {
        final String type = branch.getType();
        final List<FormAttribute> extendedAttributes = getExtendedAttributes(type, attributeSet);
        addLeaves(branch, extendedAttributes, true);
    }

    private void addDerivedAttributeLeaves(AnalysisAttributeBranch branch) {
        final String type = branch.getType();
        final List<FormAttribute> derivedAttributes = getDerivedAttributes(type);
        addLeaves(branch, derivedAttributes, false);
    }

    /**
     * Add leaves.
     * <br/> For each AttributeWrapperBean in the List, add a leaf to the branch.
     *
     * @param branch           the branch
     * @param attributes       List of {@link com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean} objects.
     * @param endWithSeparator add a separator at the end
     */
    private void addLeaves(AnalysisAttributeBranch branch, List<FormAttribute> attributes, boolean endWithSeparator) {
        if (attributes != null) {
            for (Iterator<FormAttribute> iterator = attributes.iterator(); iterator.hasNext();) {
                FormAttribute attributeWrapperBean = iterator.next();
                String id = buildId(branch, attributeWrapperBean.getId());
                if (shouldAddAttribute(branch, attributeWrapperBean, includeActiveFlag)) {
                    branch.addLeaf(new Leaf(id, attributeWrapperBean.getLabel()));
                }
            }

            if (endWithSeparator && !attributes.isEmpty()) {
                final Leaf separatorLeaf = new Leaf();
                separatorLeaf.setSeparator(true);
                branch.addLeaf(separatorLeaf);
            }
        }
    }

    private boolean shouldAddAttribute(AnalysisAttributeBranch branch, FormAttribute attributeWrapperBean, boolean includeActiveFlag) {

        final String branchId = branch.getId();
        if (includeActiveFlag && AnalysisAttributeHelper.ACTIVE_ATTR.equals(attributeWrapperBean.getId())) {
            if (PERSON_BRANCH_ID.equals(branchId) || POSITION_BRANCH_ID.equals(branch.getId())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Build id to include branch prefix.
     * <br/> If prefix is null or empty will return id; otherwise will return "prefix.id".
     *
     * @param branch the branch
     * @param id     the branch id
     * @return String
     */
    public static String buildId(AnalysisAttributeBranch branch, String id) {
        return buildId(branch.getPrefix(), id);
    }

    /**
     * Build id to include branch prefix.
     * <br/> If prefix is null or empty will return id; otherwise will return "prefix.id".
     *
     * @param prefix the prefix for the tree
     * @param id     part of the prefix
     * @return String
     */
    public static String buildId(String prefix, String id) {

        StringBuffer stringBuffer = new StringBuffer();
        if (id == null) id = "";
        // add current branch prefix
        if (StringUtils.hasText(prefix)) {
            if (!id.startsWith(prefix)) {
                stringBuffer.append(prefix).append(AnalysisAttributeHelper.DELIMITER);
            }
        }
        // add id
        stringBuffer.append(id);

        return stringBuffer.toString();
    }

    /**
     * Get label : will find branch attribute belongs to. Note this method does not support finding the labels for the attributes in formula's
     *
     * @param artefactType         type of artefact position {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_} ,
     *                             subject({@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_} ) or questionnaire
     *                             ({@link com.zynap.talentstudio.organisation.Node#QUESTIONNAIRE_TYPE })
     * @param attributeWrapperBean the attribute delegate
     * @return Label
     * @throws com.zynap.exception.TalentStudioException
     *          any errors
     */
    private String getLabel(String artefactType, AnalysisAttributeWrapperBean attributeWrapperBean, String viewType) throws TalentStudioException {

        StringBuffer buffer = new StringBuffer();

        if (attributeWrapperBean.isFormula()) {
            // get the expressions and then send through the branch for each attribute the best solution, we will for now just return /
            buffer.append(LABEL_SEPARATOR);
            return buffer.toString();

        } else if (attributeWrapperBean.isAttributeSet()) {

            String attributeName = attributeWrapperBean.getAttributeName();
            List<AnalysisAttributeBranch> branches = getTree(artefactType);
            // find the correct branch
            AnalysisAttributeBranch b;
            if (attributeWrapperBean.isOrgunitBranch()) {
                b = findOrgBranchByLeafId(branches, attributeName);
            } else {
                b = findBranchByLeafId(branches, attributeWrapperBean.getAttribute(), viewType);
            }

            if (b != null) {
                b.buildLabel(buffer, LABEL_SEPARATOR);
                ITreeContainer treeContainer = findLeaf(attributeWrapperBean, b, viewType);
                if (treeContainer != null) {
                    buffer.append(treeContainer.getLabel());
                } else {
                    buffer.append(attributeWrapperBean.getAttributeLabel());
                }
            } else {
                // happens in the artefact views as we do not have branches for them
                final AnalysisParameter analysisParameter = attributeWrapperBean.getAnalysisParameter();
                final String name = analysisParameter.getName();
                if (AnalysisAttributeHelper.isCoreAttribute(name)) {
                    buffer.append(analysisParameter.getUnqualifiedName());
                } else {
                    buffer.append(attributeWrapperBean.getAttributeLabel());
                }
            }
        }
        return buffer.toString();
    }

    private ITreeContainer findLeaf(AnalysisAttributeWrapperBean attributeWrapperBean, AnalysisAttributeBranch b, String viewType) {
        ITreeContainer treeContainer = b.findLeaf(attributeWrapperBean.getAttribute());
        // if not found lets try to reload the leaves
        if (treeContainer == null) {
            // add leaves
            b.getLeaves().clear();
            addLeaves(b, viewType);
        }
        // refind the leaf
        treeContainer = b.findLeaf(attributeWrapperBean.getAttribute());
        return treeContainer;
    }

    private List<LookupValue> getAppraisalRoles() {
        return performanceReviewService.getRoles();
    }

    /**
     * Get extended attributes.
     *
     * @param nodeType     the type of node
     * @param attributeSet a grouped set of attributes
     * @return List of {@link AttributeWrapperBean} objects.
     */
    List<FormAttribute> getExtendedAttributes(String nodeType, AttributeSet attributeSet) {
        final boolean includeCalculatedFields = attributeSet != null && attributeSet.isIncludeCalculatedFields();
        Collection attributes = dynamicAttributeService.getActiveAttributes(nodeType, this.useSearchableExtendedAttributesOnly, this.extendedAttributeTypes, includeCalculatedFields);
        return DynamicAttributesHelper.createAttributeWrappers(attributes, true);
    }

    /**
     * Get derived attributes for specified node type.
     *
     * @param nodeType the type of node
     * @return List of {@link AttributeWrapperBean} objects.
     */
    List<FormAttribute> getDerivedAttributes(String nodeType) {

        List<FormAttribute> derivedAttributes = new ArrayList<FormAttribute>();

        if (includeDerivedAttributes) {
            if (nodeType.equals(IPopulationEngine.P_POS_TYPE_)) {

                // build derived attributes for position to position associations (superior and subordinate)
                Collection<DynamicAttribute> listPtP = getPositionToPositionAssociationDynamicAttributes();
                for (Iterator<DynamicAttribute> iterator = listPtP.iterator(); iterator.hasNext();) {
                    DynamicAttribute dynamicAttribute = iterator.next();
                    buildDerivedAttributeWrappers(dynamicAttribute.getLabel(), dynamicAttribute.getActiveLookupValues(), derivedAttributes, IDynamicAttributeService.PP_SUB_DERIVED_ATT_DEFINITION);
                    buildDerivedAttributeWrappers(dynamicAttribute.getLabel(), dynamicAttribute.getActiveLookupValues(), derivedAttributes, IDynamicAttributeService.PP_SUP_DERIVED_ATT_DEFINITION);
                }

                // build derived attributes for position to subject associations
                Collection<DynamicAttribute> listStP = getSubjectToPositionAssociationDynamicAttributes();
                for (Iterator<DynamicAttribute> iterator = listStP.iterator(); iterator.hasNext();) {
                    DynamicAttribute dynamicAttribute = iterator.next();
                    buildDerivedAttributeWrappers(dynamicAttribute.getLabel(), dynamicAttribute.getActiveLookupValues(), derivedAttributes, IDynamicAttributeService.SP_SUB_DERIVED_ATT_DEFINITION);
                }
            }

            if (nodeType.equals(IPopulationEngine.P_SUB_TYPE_)) {

                // build derived attributes for subject to position associations
                Collection<DynamicAttribute> listStP = getSubjectToPositionAssociationDynamicAttributes();
                for (Iterator<DynamicAttribute> iterator = listStP.iterator(); iterator.hasNext();) {
                    DynamicAttribute dynamicAttribute = iterator.next();
                    buildDerivedAttributeWrappers(dynamicAttribute.getLabel(), dynamicAttribute.getActiveLookupValues(), derivedAttributes, IDynamicAttributeService.SP_SUP_DERIVED_ATT_DEFINITION);
                }
            }
        }

        return derivedAttributes;

    }

    private List<DynamicAttribute> getPositionToPositionAssociationDynamicAttributes() {
        return (List<DynamicAttribute>) dynamicAttributeService.getSearchableAttributes(IPopulationEngine.P_P_ASSOC_TYPE_);
    }

    private List<DynamicAttribute> getSubjectToPositionAssociationDynamicAttributes() {
        return (List<DynamicAttribute>) dynamicAttributeService.getSearchableAttributes(IPopulationEngine.S_P_ASSOC_TYPE_);
    }

    /**
     * Builds an AttributeWrapperBeans for each lookup value
     *
     * @param prefix           The label of the association
     * @param lookupValues     The lookup values.
     * @param results          The collection to add the AttributeWrapperBeans to
     * @param dynamicAttribute The dynamic attribute to add to the new AttributeWrapperBeans
     */
    private void buildDerivedAttributeWrappers(String prefix, Collection lookupValues, Collection<FormAttribute> results, DynamicAttribute dynamicAttribute) {

        prefix = prefix.substring(0, prefix.indexOf(' '));
        for (Iterator it = lookupValues.iterator(); it.hasNext();) {
            LookupValue lookupValue = (LookupValue) it.next();
            String name = prefix + " " + lookupValue.getLabel() + " " + dynamicAttribute.getLabel() + " Count";
            String id = dynamicAttribute.getType() + "[" + lookupValue.getId() + "]";
            AttributeWrapperBean attributeWrapperBean = new AttributeWrapperBean(name, id, dynamicAttribute);
            results.add(attributeWrapperBean);
        }
    }

    /**
     * Get questionnaire definitions.
     * <br/> Will filter the definitions to remove the questions that are of the incorrect type if extendedAttributeTypes has been set.
     *
     * @return List of {@link com.zynap.talentstudio.questionnaires.QuestionnaireDefinition} objects.
     * @throws com.zynap.exception.TalentStudioException
     *          any error
     */
    private List<QuestionnaireDefinition> getQuestionnaireDefinitions() throws TalentStudioException {

        final List<QuestionnaireDefinition> definitions = questionnaireDefinitionService.findReportableDefinitions(extendedAttributeTypes);
        // load the workflows
        for (QuestionnaireDefinition definition : definitions) {
            final Set<QuestionnaireWorkflow> questionnaireWorkflowSet = definition.getQuestionnaireWorkflows();
            if (questionnaireWorkflowSet != null) questionnaireWorkflowSet.size();
        }
        return definitions;
    }

    /**
     * Get attribute wrappers for associations qualifiers.
     * <br/> Returns map with attributewrappers as values keyed by association prefix.
     *
     * @return Map
     */
    private Map<String, AttributeWrapperBean> getAssociationQualifiers() {

        Map<String, AttributeWrapperBean> attributeWrappers = new HashMap<String, AttributeWrapperBean>();

        final List<FormAttribute> attributeWrapperBeans = qualifierAttributeSet.getAttributeWrapperBeans();

        if (!attributeWrapperBeans.isEmpty()) {
            final FormAttribute qualifierAttributeWrapper = attributeWrapperBeans.get(0);
            final String id = qualifierAttributeWrapper.getId();
            final String name = qualifierAttributeWrapper.getLabel();

            // build one attribute wrapper for position to position associations with lookupValues from both
            // association types so that the population picker
            // when picking the association type attribute will have all values both primary and secondary to choose from
            LookupType lookupType = null;
            DynamicAttribute positionAttribute = null;

            List<DynamicAttribute> listPtP = getPositionToPositionAssociationDynamicAttributes();

            for (int i = 0; i < listPtP.size(); i++) {
                DynamicAttribute temp = listPtP.get(i);
                final LookupType currentLookupType = temp.getRefersToType();

                if (i == 0) {
                    positionAttribute = new DynamicAttribute(temp.getId(), QUALIFIER_TYPE_LABEL, temp.getType());
                    lookupType = new LookupType(currentLookupType);
                } else {
                    lookupType.getLookupValues().addAll(currentLookupType.getLookupValues());
                }
            }

            if (positionAttribute != null) {
                positionAttribute.setRefersToType(lookupType);
                AttributeWrapperBean positionQualifiers = new AttributeWrapperBean(name, id, positionAttribute);
                attributeWrappers.put(IPopulationEngine.TARGET_ASSOCIATIONS_ATTR, positionQualifiers);
                attributeWrappers.put(IPopulationEngine.SOURCE_ASSOCIATIONS_ATTR, positionQualifiers);
            }

            List<DynamicAttribute> listStP = getSubjectToPositionAssociationDynamicAttributes();

            for (DynamicAttribute temp : listStP) {

                DynamicAttribute subjectAttribute = new DynamicAttribute(temp.getId(), temp.getLabel(), temp.getType());
                final LookupType refersToType = temp.getRefersToType();
                subjectAttribute.setRefersToType(refersToType);

                AttributeWrapperBean qualifierAttribute = new AttributeWrapperBean(name, id, subjectAttribute);
                String key;
                if (ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC.equals(refersToType.getTypeId())) {
                    key = IPopulationEngine.SUBJECT_PRIMARY_ASSOCIATIONS_ATTR;
                } else {
                    key = IPopulationEngine.SUBJECT_SECONDARY_ASSOCIATIONS_ATTR;
                }
                attributeWrappers.put(key, qualifierAttribute);
            }
        }

        return attributeWrappers;
    }

    /**
     * Always returns a new List to avoid pass-by-ref problems.
     *
     * @param artefactType type of artefact position {@link com.zynap.talentstudio.organisation.Node#POSITION_UNIT_TYPE_} ,
     *                     subject({@link com.zynap.talentstudio.organisation.Node#SUBJECT_UNIT_TYPE_} ) or questionnaire ({@link com.zynap.talentstudio.organisation.Node#QUESTIONNAIRE_TYPE })
     * @return List (returns empty list if not found)
     */
    final List<AnalysisAttributeBranch> getTree(String artefactType) {
        List<AnalysisAttributeBranch> temp = trees.get(artefactType);
        if (temp == null) temp = new ArrayList<AnalysisAttributeBranch>();
        // do we need to get the questionnaires
        return temp;
    }

    AttributeSet getAttributeSet(String artefactType, String viewType) {
        return (AttributeSet) CollectionUtils.find(coreAttributeSets, new AttributeSetPredicate(artefactType, viewType));
    }

    /**
     * Get qualifier attributes.
     *
     * @return List (always returns empty list not null)
     */
    final List<FormAttribute> getQualifierAttributes() {
        return qualifierAttributeSet.getAttributeWrapperBeans();
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setIncludeActiveFlag(boolean includeActiveFlag) {
        this.includeActiveFlag = includeActiveFlag;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public AnalysisAttributeBranch findBranchByLeafId(String artefactType, String leafId, String viewType) throws TalentStudioException {
        final List<AnalysisAttributeBranch> tree = getTree(artefactType);
        return findBranchByLeafId(tree, leafId, viewType);
    }


    private IDynamicAttributeService dynamicAttributeService;
    private IQueWorkflowService questionnaireWorkflowService;
    private IQueDefinitionService questionnaireDefinitionService;
    private IPerformanceReviewService performanceReviewService;
    private QuestionnaireHelper questionnaireHelper;

    boolean useSearchableExtendedAttributesOnly = false;
    boolean includeDerivedAttributes = false;
    boolean includeDynamicLineItemAttributes = false;
    boolean includeActiveFlag = false;
    String[] extendedAttributeTypes;

    /**
     * Holds branches.
     */
    private Map<String, List<AnalysisAttributeBranch>> trees;

    /**
     * Holds AttributeSets for each type and view type.
     */
    private final List<AttributeSet> coreAttributeSets = new ArrayList<AttributeSet>();

    /**
     * Holds qualifier attributes.
     */
    private final AttributeSet qualifierAttributeSet = new AttributeSet(null, null, true, true, true);
    private String sourceFile;
    static final String LABEL_SEPARATOR = " / ";
    static final String ROLE_SEPARATOR = " - ";
    public static final String QUESTIONNAIRE_BRANCH_ID = "questionnaires";
    public static final String APPRAISALS_BRANCH_ID = "appraisals";
    private static final String QUALIFIER_TYPE_LABEL = "Type";
    private static final String PERSON_BRANCH_ID = "person";
    private static final String POSITION_BRANCH_ID = "position";
    private String viewType;
}
