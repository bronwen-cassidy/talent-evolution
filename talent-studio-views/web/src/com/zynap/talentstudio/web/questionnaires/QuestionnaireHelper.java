package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.AnalysisAttributeHelper;
import com.zynap.talentstudio.analysis.AnalysisParameter;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.AttributeValuesCollection;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.questionnaires.AbstractQuestion;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.LineItem;
import com.zynap.talentstudio.questionnaires.MultiQuestionItem;
import com.zynap.talentstudio.questionnaires.QuestionAttribute;
import com.zynap.talentstudio.questionnaires.QuestionGroup;
import com.zynap.talentstudio.questionnaires.Questionnaire;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinition;
import com.zynap.talentstudio.questionnaires.QuestionnaireDefinitionModel;
import com.zynap.talentstudio.questionnaires.QuestionnaireWorkflow;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.web.common.exceptions.InvalidSubmitException;
import com.zynap.talentstudio.web.organisation.BrowseSubjectController;
import com.zynap.talentstudio.web.organisation.GroupMapKey;
import com.zynap.talentstudio.web.organisation.attributes.FormAttribute;
import com.zynap.util.ArrayUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: amark
 * Date: 31-Aug-2005
 * Time: 12:13:28
 */
public class QuestionnaireHelper {

    /**
     * Private constructor to prevent instantiation.
     */
    public QuestionnaireHelper() {
    }

    /**
     * Create wrappers for questionnaire groups and narratives and questions.
     * <br/> Then sets the questionnaire and the QuestionGroupWrappers and the QuestionAttributeWrapperBeans on the QuestionnaireWrapper.
     *
     * @param questionnaireWrapper    the questionnaire delegate
     * @param questionnaireDefinition the original questionnaire definition
     */
    public void setQuestionnaireState(QuestionnaireWrapper questionnaireWrapper, QuestionnaireDefinition questionnaireDefinition) {
        setQuestionnaireState(questionnaireWrapper, questionnaireDefinition, null);
    }

    /**
     * Create wrappers for questionnaire groups and narratives and questions.
     * <br/> If the questionnaire is not null, will read the value from the questionnaire and set it on the matching QuestionAttributeWrapperBean.
     * <br/> Then sets the questionnaire and the QuestionGroupWrappers and the QuestionAttributeWrapperBeans on the QuestionnaireWrapper.
     *
     * @param questionnaireWrapper    the questionnaire delegate
     * @param questionnaireDefinition the questionnaire definition
     * @param questionnaire           can be null
     */
    public void setQuestionnaireState(QuestionnaireWrapper questionnaireWrapper, QuestionnaireDefinition questionnaireDefinition, Questionnaire questionnaire) {

        questionnaireWrapper.clearQuestionnaireState();

        final List<FormAttribute> allAttributes = new ArrayList<FormAttribute>();
        final List<QuestionGroupWrapper> questionGroupWrappers = new ArrayList<QuestionGroupWrapper>();

        final List<QuestionGroup> groups = questionnaireDefinition.getQuestionnaireDefinitionModel().getQuestionGroups();
        for (QuestionGroup questionGroup : groups) {
            // get all formattributes
            final List<FormAttribute> formAttributes = createFormAttributes(questionnaireWrapper, questionnaire, questionGroup.getAbstractQuestions());

            // iterate and add to all attributes list
            for (int i = 0; i < formAttributes.size(); i++) {
                final FormAttribute formAttribute = formAttributes.get(i);

                // if dynamic line item wrapper
                if (formAttribute instanceof LineItemWrapper) {
                    allAttributes.addAll(((LineItemWrapper) formAttribute).getQuestionWrappers());
                } else {
                    allAttributes.add(formAttribute);
                }
            }

            // create group wrapper and set form attributes on wrapper
            final QuestionGroupWrapper wrapper = new QuestionGroupWrapper(questionGroup);
            wrapper.setWrappedDynamicAttributes(formAttributes);

            // add group wrapper to collection
            questionGroupWrappers.add(wrapper);
        }

        questionnaireWrapper.setQuestionnaireState(questionnaire, questionGroupWrappers, allAttributes, questionnaireDefinition.getId());
    }

    /**
     * Loop through a series of QuestionnaireDefinitions
     * and find the one with the questionnaire that matches the questionnaire workflow id in the Attribute.
     * <br/> Then finds the question that matches the question id in the Attribute in the collection of questions belonging to the definition.
     *
     * @param questionnaireDefinitions all questionnaire definitions
     * @param criteriaAttribute        the chosen criteria
     * @return Question or null
     */
    public QuestionAttribute findQuestion(List questionnaireDefinitions, AnalysisParameter criteriaAttribute) {

        QuestionAttribute question = null;

        final Long questionnaireWorkflowId = criteriaAttribute.getQuestionnaireWorkflowId();
        final Long questionId = AnalysisAttributeHelper.getQuestionId(criteriaAttribute);

        question = findAnswer(questionnaireDefinitions, questionnaireWorkflowId, question, questionId);

        return question;
    }

    public QuestionAttribute findQuestion(List questionnaireDefinitions, String attributeName, Long questionnaireWorkflowId) {

        QuestionAttribute question = null;
        final Long questionId = AnalysisAttributeHelper.getQuestionId(attributeName);

        question = findAnswer(questionnaireDefinitions, questionnaireWorkflowId, question, questionId);

        return question;
    }

    private QuestionAttribute findAnswer(List questionnaireDefinitions, Long questionnaireWorkflowId, QuestionAttribute question, Long questionId) {
        
        for (Iterator iterator = questionnaireDefinitions.iterator(); iterator.hasNext();) {
            QuestionnaireDefinition questionnaireDefinition = (QuestionnaireDefinition) iterator.next();

            final Set questionnaireWorkflows = questionnaireDefinition.getQuestionnaireWorkflows();
            for (Iterator workflowInterator = questionnaireWorkflows.iterator(); workflowInterator.hasNext();) {
                QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) workflowInterator.next();

                if (questionnaireWorkflow.getId().equals(questionnaireWorkflowId)) {
                    // go and refresh the definition from the database to avois lazy initialisations
                    final QuestionnaireDefinition definition = questionnaireDefinitionService.findDefinition(questionnaireDefinition.getId());
                    final QuestionnaireDefinitionModel digesterQuestionnaireDefinition = definition.getQuestionnaireDefinitionModel();
                    final List questions = digesterQuestionnaireDefinition.getQuestions();
                    question = findQuestion(questions, questionId);
                    // stop looping - will now return null
                    break;
                }
            }
        }
        return question;
    }

    /**
     * Removes all questions from the definitions that are not of the correct type.
     *
     * @param questionnaireDefinitions list of all definitions
     * @param questionTypes            all question types
     * @param includeDynamicLineItems  whether to include dynamic line items or not
     * @return List                    list of questionnaire definitions
     */
    public List<QuestionnaireDefinition> filterQuestionnaireDefinitions(Collection<QuestionnaireDefinition> questionnaireDefinitions, String[] questionTypes, boolean includeDynamicLineItems) {

        List<QuestionnaireDefinition> definitions = new ArrayList<QuestionnaireDefinition>();

        for (QuestionnaireDefinition questionnaireDefinition : questionnaireDefinitions) {
            final QuestionnaireDefinitionModel filteredModel = filterQuestionnaireDefinition(questionTypes, includeDynamicLineItems, questionnaireDefinition);
            if (!filteredModel.getQuestionGroups().isEmpty()) {
                questionnaireDefinition.setQuestionnaireDefinitionModel(filteredModel);
                definitions.add(questionnaireDefinition);
            }
        }
        return definitions;
    }

    public QuestionnaireDefinitionModel filterQuestionnaireDefinition(String[] questionTypes, boolean includeDynamicLineItems, QuestionnaireDefinition questionnaireDefinition) {
        // get definition
        final QuestionnaireDefinitionModel definitionModel = questionnaireDefinition.getQuestionnaireDefinitionModel();
        final QuestionnaireDefinitionModel filteredModel = new QuestionnaireDefinitionModel(questionnaireDefinition);

        final List<QuestionGroup> questionnaireGroups = definitionModel.getQuestionGroups();
        for (QuestionGroup questionnaireGroup : questionnaireGroups) {

            QuestionGroup filteredGroup = new QuestionGroup(questionnaireGroup.getId(), questionnaireGroup.getLabel());
            final List questions = questionnaireGroup.getAbstractQuestions();
            filterQuestions(questions, questionTypes, includeDynamicLineItems, filteredGroup, null);

            // remove group if there are no questions left
            if (!filteredGroup.getAbstractQuestions().isEmpty()) {
                filteredModel.addQuestionGroup(filteredGroup);
            }
        }
        return filteredModel;
    }

    /**
     * Find the questions that belong to the specified group in the definition.
     *
     * @param questionnaireDefinition the definition
     * @param groupName               the name of the group we wish to identify
     * @return List of {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} objects
     */
    public List<QuestionAttribute> getQuestions(QuestionnaireDefinitionModel questionnaireDefinition, String groupName) {

        final QuestionGroup group = questionnaireDefinition.getQuestionGroup(groupName);
        final List<QuestionAttribute> questions = group.collectAllQuestions();
        return getAnswerableQuestions(questions);
    }


    private List<QuestionAttribute> getAnswerableQuestions(List<QuestionAttribute> questions) {

        final List<QuestionAttribute> answerableQuestions = new ArrayList<QuestionAttribute>();
        for (QuestionAttribute baseQuestion : questions) {
            if (!baseQuestion.isNarrativeType()) answerableQuestions.add(baseQuestion);
        }

        return answerableQuestions;
    }

    /**
     * Find the question.
     *
     * @param questions  The questions
     * @param questionId The question id
     * @return Question or null
     */
    public QuestionAttribute findQuestion(List questions, Long questionId) {

        QuestionAttribute question = null;

        for (Iterator questionIterator = questions.iterator(); questionIterator.hasNext();) {
            AbstractQuestion baseQuestion = (AbstractQuestion) questionIterator.next();

            if (baseQuestion.isMultiQuestion()) {
                MultiQuestionItem multiQuestion = (MultiQuestionItem) baseQuestion;
                question = findQuestion(multiQuestion.getQuestions(), questionId);
            } else {
                QuestionAttribute temp = (QuestionAttribute) baseQuestion;
                if (!temp.isNarrativeType()) {
                    if (questionId.equals(temp.getDynamicAttribute().getId())) {
                        question = temp;
                    }
                }
            }

            // if question found exit loop
            if (question != null) {
                break;
            }
        }

        return question;
    }

    /**
     * Find QuestionAttributeWrapperBeans that match specified question.
     * <br/> For multiquestion will return all questions.
     *
     * @param wrappedDynamicAttributes the dynamic attributes which are wrapped
     * @param question                 the question for which we wish to find the wrapper for
     * @return List of QuestionAttributeWrapperBeans (never null)
     */
    public List<FormAttribute> findQuestionWrapperBeans(List wrappedDynamicAttributes, QuestionAttribute question) {

        final List<FormAttribute> questionAttributeWrapperBeans = new ArrayList<FormAttribute>();
        final List relatedQuestions = question.getLineItem().getQuestions();
        for (int i = 0; i < relatedQuestions.size(); i++) {
            final QuestionAttribute relatedQuestion = (QuestionAttribute) relatedQuestions.get(i);
            final QuestionAttributeWrapperBean found = findQuestionWrapperBean(wrappedDynamicAttributes, relatedQuestion);
            if (found != null) {
                questionAttributeWrapperBeans.add(found);
            }
        }

        return questionAttributeWrapperBeans;
    }

    /**
     * Find QuestionAttributeWrapperBeans that match specified question.
     * <br/> Will not work with multi questions.
     *
     * @param wrappedDynamicAttributes the wrapped dynamic attributes
     * @param question                 the question for which we wish to find the wrapper for
     * @return QuestionAttributeWrapperBean or null
     */
    public QuestionAttributeWrapperBean findQuestionWrapperBean(List wrappedDynamicAttributes, QuestionAttribute question) {

        QuestionAttributeWrapperBean questionAttributeWrapperBean = null;

        if (wrappedDynamicAttributes != null && !wrappedDynamicAttributes.isEmpty()) {
            questionAttributeWrapperBean = (QuestionAttributeWrapperBean) CollectionUtils.find(wrappedDynamicAttributes, new QuestionAttributeWrapperBeanPredicate(question));
        }

        return questionAttributeWrapperBean;
    }

    /**
     * Attempts to find questionnaire by questionnaireId.
     * <br/> If not found finds workflow using workflowId and then creates a new unsaved Questionnaire object
     * on which it sets the questionnaireWorkflow, the subject id and the user.
     * <br/> Finally it creates a QuestionnaireWrapper using the questionnaire and uses setQuestionnaireState(...) to initialise the wrapper
     * so it can be used to display the questionnaire form.
     *
     * @param questionnaireId the id of the questionnaire to find
     * @param workflowId      the workflow id if the questionnaire has not yet had an id assigned
     * @param user            the user the workflow belongs to
     * @param subjectId       the target subject's id
     * @param forUpdate       are we viewing or updating the questionnaire
     * @return QuestionnaireWrapper   the wrapper of the questionnaire
     * @throws TalentStudioException any requests via the services throw the exception.
     *                               An InvalidSubmitException if both the workflowId and questionnaireId are null
     */
    public synchronized QuestionnaireWrapper getQuestionnaireWrapper(final Long questionnaireId, final Long workflowId, final User user, final Long subjectId, boolean forUpdate) throws TalentStudioException {

        if (questionnaireId == null && workflowId == null) throw new InvalidSubmitException(null, null, null, true, this.getClass().getName());

        Questionnaire questionnaire;
        if (questionnaireId != null) {
            if (forUpdate) {
                questionnaire = questionnaireService.loadQuestionnaire(questionnaireId, user.getId());
            } else {
                questionnaire = (Questionnaire) questionnaireService.findById(questionnaireId);
            }
        } else {
            final QuestionnaireWorkflow questionnaireWorkflow = (QuestionnaireWorkflow) questionnaireWorkflowService.findById(workflowId);
            if (forUpdate) {
                // this method looks for a questionnaire given the workflow, if it does not exist it creates it and locks it down
                questionnaire = questionnaireService.findOrCreateQuestionnaire(workflowId, user.getId(), subjectId);
            } else {
                questionnaire = questionnaireService.findQuestionnaireByWorkflow(workflowId, null, subjectId, null);
                if (questionnaire == null) {
                    questionnaire = new Questionnaire(null, questionnaireWorkflow, user);
                    questionnaire.setSubjectId(subjectId);
                }
            }
        }

        final QuestionnaireWrapper wrapper = new QuestionnaireWrapper();
        final QuestionnaireDefinition questionnaireDefinition = questionnaire.getQuestionnaireWorkflow().getQuestionnaireDefinition();
        setQuestionnaireState(wrapper, questionnaireDefinition, questionnaire);

        return wrapper;
    }

    /**
     * Get label.
     *
     * @param attributeValue the value of the node
     * @return label or empty string
     */
    private String getDomainObjectLabel(AttributeValue attributeValue) {
        return dynamicAttributeService.getDomainObjectLabel(attributeValue);
    }

    /**
     * Build wrappers for questions.
     * <br/> If the questionnaire is null the question wrappers will be returned with no answers set.
     *
     * @param questionnaire The questionnaire with the answers (can be null)
     * @param questions     the list of questions
     * @return List of {@link com.zynap.talentstudio.web.questionnaires.QuestionGroupWrapper } objects
     */
    private List<FormAttribute> createFormAttributes(QuestionnaireWrapper questionnaireWrapper, Questionnaire questionnaire, List<AbstractQuestion> questions) {

        final boolean hasNode = (questionnaire != null);
        final List<FormAttribute> wrappers = new ArrayList<FormAttribute>();

        AttributeValuesCollection dynamicAttributeValues = null;
        if (hasNode) {
            dynamicAttributeValues = questionnaire.getDynamicAttributeValues();
        }

        for (AbstractQuestion abstractQuestion : questions) {

            if (abstractQuestion.isNarrativeType()) {

                final NarrativeAttributeWrapperBean narrativeAttributeWrapperBean = new NarrativeAttributeWrapperBean((QuestionAttribute) abstractQuestion);
                wrappers.add(narrativeAttributeWrapperBean);

            } else if (abstractQuestion.isMultiQuestion()) {

                final MultiQuestionItem multiQuestion = (MultiQuestionItem) abstractQuestion;
                final LineItemWrapper lineItemWrapper = createLineItemWrapper(questionnaireWrapper, dynamicAttributeValues, multiQuestion);
                wrappers.add(lineItemWrapper);

            } else {

                final QuestionAttribute question = (QuestionAttribute) abstractQuestion;
                final QuestionAttributeWrapperBean questionAttributeWrapperBean = createQuestionAttributeWrapper(questionnaireWrapper, question, dynamicAttributeValues);
                wrappers.add(questionAttributeWrapperBean);

            }
        }

        return wrappers;
    }

    /**
     * Create wrapper for line item.
     *
     * @param dynamicAttributeValues the collection of answers
     * @param multiQuestion          the multi question item which contains line items and questions
     * @return LineItemWrapper or DynamicLineItemWrapper as appropriate
     */
    private LineItemWrapper createLineItemWrapper(QuestionnaireWrapper questionnaireWrapper, AttributeValuesCollection dynamicAttributeValues, MultiQuestionItem multiQuestion) {

        final List questions = multiQuestion.getQuestions();
        final boolean isDynamic = ((QuestionAttribute) questions.get(0)).isDynamic();

        final LineItemWrapper dynamicLineItemWrapper;
        if (isDynamic) {
            dynamicLineItemWrapper = createDynamicLineItemWrapper(questions, dynamicAttributeValues, multiQuestion);
        } else {
            dynamicLineItemWrapper = createLineItemWrapper(questionnaireWrapper, questions, dynamicAttributeValues, multiQuestion);
        }

        return dynamicLineItemWrapper;
    }

    /**
     * Create wrapper for Line Item.
     *
     * @param questions              the questions within the line items of the multi question item
     * @param dynamicAttributeValues the collection of answers
     * @param multiQuestion          the multi question item containing the line items
     * @return LineItemWrapper wrapping the LineItem object
     */
    private LineItemWrapper createLineItemWrapper(QuestionnaireWrapper questionnaireWrapper, List questions, AttributeValuesCollection dynamicAttributeValues, MultiQuestionItem multiQuestion) {
        final LineItemWrapper lineItemWrapper = new LineItemWrapper(multiQuestion);

        // build wrappers - if questionnaire supplied iterate questions and find values (if any) for each question
        for (Iterator iterator = questions.iterator(); iterator.hasNext();) {
            QuestionAttribute question = (QuestionAttribute) iterator.next();
            QuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = createQuestionAttributeWrapper(questionnaireWrapper, question, dynamicAttributeValues);
            lineItemWrapper.addLineItemQuestion(dynamicQuestionAttributeWrapperBean);
        }

        lineItemWrapper.initialiseState();

        return lineItemWrapper;
    }

    /**
     * Create wrapper for Dynamic Line Item.
     *
     * @param questions              the line items of a multi question
     * @param dynamicAttributeValues the ansers to the questions
     * @param multiQuestion          the multiQuestionItem
     * @return DynamicLineItemWrapper
     */
    private DynamicLineItemWrapper createDynamicLineItemWrapper(final List questions, AttributeValuesCollection dynamicAttributeValues, MultiQuestionItem multiQuestion) {

        // determine max number of line items
        int maxNumberOfLineItems = 0;

        if (dynamicAttributeValues != null) {

            for (Object question1 : questions) {
                QuestionAttribute question = (QuestionAttribute) question1;
                // get associated value
                // if found get last node extended attribute and save dynamic position if greater than current maximum
                final DynamicAttribute childDynamicAttribute = question.getDynamicAttribute();
                AttributeValue childAttributeValue = dynamicAttributeValues.get(childDynamicAttribute);
                if (childAttributeValue != null) {
                    NodeExtendedAttribute nodeExtendedAttribute = childAttributeValue.getLastNodeExtendedAttribute();
                    final int dynamicPosition = nodeExtendedAttribute.getDynamicPosition();
                    if (dynamicPosition > maxNumberOfLineItems) maxNumberOfLineItems = dynamicPosition;
                }
            }
        }

        final DynamicLineItemWrapper dynamicLineItemWrapper = new DynamicLineItemWrapper(multiQuestion);
        // build wrappers - if questionnaire supplied iterate questions and find values (if any) for each question
        for (Object question1 : questions) {
            QuestionAttribute question = (QuestionAttribute) question1;
            final DynamicAttribute childDynamicAttribute = question.getDynamicAttribute();

            final AttributeValue childAttributeValue = getAttributeValue(childDynamicAttribute, dynamicAttributeValues);
            final DynamicQuestionAttributeWrapperBean dynamicQuestionAttributeWrapperBean = new DynamicQuestionAttributeWrapperBean(question, childAttributeValue, maxNumberOfLineItems);
            //dynamicQuestionAttributeWrapperBean.setLineItemWrapper(dynamicLineItemWrapper);
            dynamicLineItemWrapper.addLineItemQuestion(dynamicQuestionAttributeWrapperBean);
        }

        dynamicLineItemWrapper.initialiseState();
        return dynamicLineItemWrapper;
    }

    /**
     * Create QuestionAttributeWrapperBean.
     * <br/> Makes sure that attributevalue is set and that label is set for node or last updated by questions.
     *
     * @param question               the question attribute
     * @param dynamicAttributeValues the list of answers
     * @return QuestionAttributeWrapperBean
     */
    private QuestionAttributeWrapperBean createQuestionAttributeWrapper(QuestionnaireWrapper questionnaireWrapper, final QuestionAttribute question, AttributeValuesCollection dynamicAttributeValues) {


        final DynamicAttribute dynamicAttribute = question.getDynamicAttribute();
        dynamicAttribute.initLazy();

        if (dynamicAttribute.isPositionType()) {
            questionnaireWrapper.setPositionPickerList(true);
        } else if (dynamicAttribute.isSubjectType()) {
            questionnaireWrapper.setSubjectPickerList(true);
        } else if (dynamicAttribute.isOrganisationUnitType()) {
            questionnaireWrapper.setOrganisationPickerList(true);
        }

        AttributeValue attributeValue = getAttributeValue(dynamicAttribute, dynamicAttributeValues);

        // make sure that label is set for node type questions or user name for the last updated by question if has value
        String label = null;
        if (attributeValue.hasValue() && dynamicAttributeService != null && (dynamicAttribute.isNodeType() || dynamicAttribute.isLastUpdatedByType())) {
            label = getDomainObjectLabel(attributeValue);
        }

        QuestionAttributeWrapperBean questionAttributeWrapperBean;
        if (dynamicAttribute.isBlogComment()) {
            questionAttributeWrapperBean = new BlogCommentQuestionWrapper(question, attributeValue, UserSessionFactory.getUserSession().getUser());
        } else {
            questionAttributeWrapperBean = new QuestionAttributeWrapperBean(question, attributeValue);
        }

        questionAttributeWrapperBean.setNodeLabel(label);
        return questionAttributeWrapperBean;
    }

    /**
     * Get attribute value.
     * <br/> Attempts to get attribute value from AttributeValuesCollection.
     * <br/> If not found or the AttributeValuesCollection is null will return new AttributeValue.
     *
     * @param dynamicAttribute       the dynamic attribute
     * @param dynamicAttributeValues The AttributeValuesCollection (can be null)
     * @return AttributeValue
     */
    private AttributeValue getAttributeValue(final DynamicAttribute dynamicAttribute, AttributeValuesCollection dynamicAttributeValues) {

        AttributeValue attributeValue = null;
        if (dynamicAttributeValues != null) {
            attributeValue = dynamicAttributeValues.get(dynamicAttribute);
        }

        // ensure that null is never returned
        if (attributeValue == null) {
            attributeValue = AttributeValue.create(dynamicAttribute);
        }

        return attributeValue;
    }

    /**
     * Remove questions that are not of the specified type from the list of questions.
     * <br/> If the question is a multiQuestion will remove them from the list of nested questions.
     *
     * @param questions               the list of {@link com.zynap.talentstudio.questionnaires.QuestionAttribute} objects
     * @param questionTypes           the array of question types
     * @param includeDynamicLineItems whether to include dynamic line items or not
     * @param filteredGroup
     * @param lineItem
     */
    private void filterQuestions(List questions, String[] questionTypes, boolean includeDynamicLineItems, QuestionGroup filteredGroup, LineItem lineItem) {

        for (Iterator iterator = questions.iterator(); iterator.hasNext();) {
            AbstractQuestion question = (AbstractQuestion) iterator.next();

            if (question.isMultiQuestion()) {
                final List questionItems = ((MultiQuestionItem) question).getLineItems();

                MultiQuestionItem filteredMultiQuestion = new MultiQuestionItem(question.getId(), question.getLabel());
                for (Iterator lineItemIterator = questionItems.iterator(); lineItemIterator.hasNext();) {

                    LineItem questionLineItem = (LineItem) lineItemIterator.next();
                    LineItem filteredLineItem = new LineItem(questionLineItem.getId(), questionLineItem.getLabel(), questionLineItem.isDynamic());

                    final List nestedQuestions = questionLineItem.getQuestions();
                    filterQuestions(nestedQuestions, questionTypes, includeDynamicLineItems, filteredGroup, filteredLineItem);

                    if (!filteredLineItem.getQuestions().isEmpty()) {
                        filteredMultiQuestion.addLineItem(filteredLineItem);
                    }
                }

                if (!filteredMultiQuestion.getLineItems().isEmpty()) {
                    filteredGroup.addMultiQuestion(filteredMultiQuestion);
                }
            } else {
                // remove questions that are not of the right type or if includeDynamicLineItems is false remove dynamic line item questions
                final DynamicAttribute dynamicAttribute = ((QuestionAttribute) question).getDynamicAttribute();

                if (shouldInclude(dynamicAttribute, questionTypes, includeDynamicLineItems)) {
                    if (lineItem != null) lineItem.addQuestion((QuestionAttribute) question);
                    else filteredGroup.addQuestion((QuestionAttribute) question);
                }
            }
        }
    }

    private boolean shouldInclude(DynamicAttribute dynamicAttribute, String[] questionTypes, boolean includeDynamicLineItems) {
        // null dynamic attribute = a narrative, not included for pickers
        if (dynamicAttribute == null) return false;
        else if (!includeDynamicLineItems && dynamicAttribute.isDynamic()) return false;
        return ArrayUtils.contains(questionTypes, dynamicAttribute.getType());
    }

    public static List sortResults(Collection<QuestionnaireDTO> questionnaires) {
        Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms = new LinkedHashMap<GroupMapKey, List<QuestionnaireDTO>>();
        int index = 0;
        for (QuestionnaireDTO questionnaire : questionnaires) {
            if (!questionnaire.isAnyAppraisal()) {
                String groupName = questionnaire.getGroupLabel();
                // no group has been assigned to it, it will thus go in a list of it's own the field being defined in the messages.properties
                if (groupName == null) groupName = BrowseSubjectController.DEFAULT_QUESTIONNAIRE_GROUP;
                GroupMapKey key = new GroupMapKey(groupName, String.valueOf(index));
                List<QuestionnaireDTO> questionnaireDTOs = groupedInfoForms.get(key);
                if (questionnaireDTOs == null) {
                    questionnaireDTOs = new ArrayList<QuestionnaireDTO>();
                    groupedInfoForms.put(key, questionnaireDTOs);
                    index++;
                }
                questionnaireDTOs.add(questionnaire);
                Collections.sort(questionnaireDTOs);
            }
        }
        List<QuestionnaireDTO> results = new ArrayList<QuestionnaireDTO>();
        for (List<QuestionnaireDTO> questionnaireDTOs : groupedInfoForms.values()) {
            results.addAll(questionnaireDTOs);
        }
        return results;
    }

    public static QuestionnaireDTO findCurrent(Long workflowId, List<QuestionnaireDTO> results) {
        for (QuestionnaireDTO dto : results) {
            if (dto.getWorkflowId().equals(workflowId)) {
                return dto;
            }
        }
        return null;
    }

    /**
     * Predicate for finding QuestionAttributeWrapperBean based on question.
     */
    private final class QuestionAttributeWrapperBeanPredicate implements Predicate {

        private final QuestionAttribute questionToFind;

        public QuestionAttributeWrapperBeanPredicate(QuestionAttribute question) {
            super();
            this.questionToFind = question;
        }

        public boolean evaluate(Object object) {
            if (object instanceof QuestionAttributeWrapperBean) {
                QuestionAttributeWrapperBean attributeWrapperBean = (QuestionAttributeWrapperBean) object;
                return attributeWrapperBean.getQuestion() == questionToFind;
            }
            return false;
        }
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public void setQuestionnaireDefinitionService(IQueDefinitionService questionnaireDefinitionService) {
        this.questionnaireDefinitionService = questionnaireDefinitionService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private IQuestionnaireService questionnaireService;
    private IQueWorkflowService questionnaireWorkflowService;
    private IQueDefinitionService questionnaireDefinitionService;
    private IDynamicAttributeService dynamicAttributeService;

}
