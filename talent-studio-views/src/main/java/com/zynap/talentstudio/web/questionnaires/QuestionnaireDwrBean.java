/* 
 * Copyright (C)  Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;


import com.zynap.domain.UserSession;
import com.zynap.domain.admin.User;
import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;
import com.zynap.talentstudio.organisation.attributes.DynamicAttributeReference;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.questionnaires.IQuestionnaireService;
import com.zynap.talentstudio.questionnaires.QuestionCalculator;
import com.zynap.talentstudio.questionnaires.support.RepublishResults;
import com.zynap.talentstudio.security.UserSessionFactory;
import com.zynap.talentstudio.util.FormatterFactory;
import com.zynap.talentstudio.web.organisation.attributes.AttributeValueValidationFactory;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;
import com.zynap.talentstudio.web.organisation.attributes.validators.ErrorMessageHandler;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @author taulant bajraktari
 * @version 0.1
 * @since 21-Jun-2007 14:53:46
 */

public class QuestionnaireDwrBean {

    public RepublishResults getRepublishResults(Long queId, Long userId) {

        RepublishResults results = null;
        try {
            results = queWorkflowService.republishWorkflow(queId, userId);
            return results;
        } catch (TalentStudioException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return results;
    }

    public AttributePersistenceResult saveUpdateDeleteQuestionnaireBlogComments(Long queId, Long daId, Long attributeId, Long userId, String value, Long queDefId, HttpServletRequest request) {
        assignUserSession(request);
        AttributePersistenceResult result = new AttributePersistenceResult();
        saveUpdateLastModifiedQuestionnaireAction(result,queId, queDefId, request); //shifted up so that when error occurs the last updated date would show
        //revalidate below fields again
        if (attributeId == null && !StringUtils.hasText(value)) {
            return result;
        }

        boolean validating = !((attributeId != null && !StringUtils.hasText(value)));
        if (validating) {
            if (!validateDynamicAttribute(daId, attributeId, value, null, request, result)) return result;
        }

        try {
            Long attrId = questionnaireService.saveUpdateDeleteQuestionAttribute(queId, daId, attributeId, value, new User(userId));
            result.setAttributeId(attrId != null ? attrId.toString() : "");

        } catch (TalentStudioException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            clearUserUsession(request);
        }
        return result;
    }

    private void clearUserUsession(HttpServletRequest request) {
        // get the current arena to obtain the timeout info
        UserSession userSession = UserSessionFactory.getUserSession();
        int sessionTimeout = userSession.getMaxTimeout();
        request.getSession(false).setMaxInactiveInterval(sessionTimeout * 60);
        UserSessionFactory.setUserSession(null);
    }

    private void assignUserSession(HttpServletRequest request) {
        UserSession userSession = ZynapWebUtils.getUserSession(request);
        UserSessionFactory.setUserSession(userSession);
    }

    /* all fields with list subject and date - use standard save, reason each being a method is in case change in validation procedure */
    public AttributePersistenceResult saveUpdateDeleteQuestionnaireList(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, Long queDefId, HttpServletRequest request) {
        return saveUpdateDeleteQuestionnaireGeneric(queId, daId, attributeId, dynamicPosition, value, null, queDefId, request);
    }

    public AttributePersistenceResult saveUpdateDeleteQuestionnaireSubject(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, Long queDefId, HttpServletRequest request) {
        return saveUpdateDeleteQuestionnaireGeneric(queId, daId, attributeId, dynamicPosition, value, null, queDefId, request);
    }

    public AttributePersistenceResult saveUpdateDeleteQuestionnaireDate(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String dateValue, Long queDefId, HttpServletRequest request) {
        return saveUpdateDeleteQuestionnaireGeneric(queId, daId, attributeId, dynamicPosition, dateValue, null, queDefId, request);
    }
    
    public AttributePersistenceResult saveDeleteQuestionnaireCurrency(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, String currency, Long queDefId, HttpServletRequest request) {
        if(currency != null) currency = currency.trim();	
    	if(!StringUtils.hasText(currency) && StringUtils.hasText(value)) {
		    AttributePersistenceResult result = new AttributePersistenceResult();
		    result.setAttributeId(attributeId.toString());
		    result.setErrorMessage(messageSource.getMessage("error.currency.required", null, getLocale(request)));
	    }
    	return saveUpdateDeleteQuestionnaireGeneric(queId, daId, attributeId, dynamicPosition, value, currency, queDefId, request);
    }

    public AttributePersistenceResult saveDeleteQuestionnaireCheckBox(Long queId, Long daId, Long attributeId, String value, String action, Long queDefId, Integer dynamicPosition, HttpServletRequest request) {
        assignUserSession(request);

        AttributePersistenceResult result = new AttributePersistenceResult();
        saveUpdateLastModifiedQuestionnaireAction(result,queId, queDefId, request);

        //revalidate below fields again
        if (attributeId == null && !StringUtils.hasText(value)) {
            return result;
        }

        boolean validating = !((attributeId != null && !StringUtils.hasText(value)));
        if (validating) {
            if (!validateDynamicAttribute(daId, attributeId, value, null, request, result)) return result;
        }

        try {
            final User user = ZynapWebUtils.getUser(request);
            Long attrId = questionnaireService.saveDeleteQuestionnaireCheckBox(queId, daId, attributeId, value, action, dynamicPosition, user);
            result.setAttributeId(attrId != null ? attrId.toString() : "");
            dynamicCalculations(daId, queId, result, user);

        } catch (TalentStudioException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            clearUserUsession(request);
        }

        return result;
    }

    public void deleteQuestionnaireAttribute(Long attributeId, HttpServletRequest request) throws TalentStudioException {
        assignUserSession(request);
        questionnaireService.deleteQuestionnaireAttribute(attributeId);
        clearUserUsession(request);
    }

    public AttributePersistenceResult saveUpdateQuestionnaireAttributeDisabled(boolean disabled, Long queId, Long lineItemId, Integer dynamicPosition, Long queDefId, HttpServletRequest request) {
        assignUserSession(request);
        AttributePersistenceResult  result=new AttributePersistenceResult();
        try {
            User user = ZynapWebUtils.getUser(request);
            questionnaireService.saveUpdateQuestionnaireAttributeDisabled(disabled, queId, lineItemId, dynamicPosition, user);
            saveUpdateLastModifiedQuestionnaireAction(result,queId, queDefId, request);
        } catch (TalentStudioException e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            clearUserUsession(request);
        }
        return  result;
    }

    public AttributePersistenceResult saveUpdateDeleteQuestionnairePosition(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, Long queDefId, HttpServletRequest request) {
        return saveUpdateDeleteQuestionnaireGeneric(queId, daId, attributeId, dynamicPosition, value, null, queDefId, request);
    }

    public AttributePersistenceResult saveUpdateDeleteQuestionnaireMultiSelect(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String[] attributeIdValues, Long queDefId, HttpServletRequest request) {
        assignUserSession(request);
        final User user = ZynapWebUtils.getUser(request);
        AttributePersistenceResult result = new AttributePersistenceResult();
        try {
            //delete all
            questionnaireService.deleteQuestionnaireAttributes(queId, daId);
            //insert these
            questionnaireService.insertQuestionnaireAttribute(queId, daId, attributeId, dynamicPosition, attributeIdValues, user);
            dynamicCalculations(daId, queId, result, user);
            saveUpdateLastModifiedQuestionnaireAction(result,queId, queDefId, request);
        } catch (TalentStudioException e) {
            // todo handle error
            logger.error(e);

        } finally {
            clearUserUsession(request);
        }
        return result;
    }

    public void saveUpdateLastModifiedQuestionnaireAction(AttributePersistenceResult result, Long queId, Long queDefId, HttpServletRequest request) {

        //do the update based on these conditions
        //1, get Userid
        //2, get queDefId
        //3, store dynamic attribute
        User user = ZynapWebUtils.getUser(request);

        Date lastModified = new Date();
        //problems with existing getDisplay date and time
        String dateModified = FormatterFactory.getDateFormatter(request.getLocale()).getDisplayDateTime(request.getLocale(),lastModified);
        try {
            questionnaireService.saveUpdateLastModifiedQuestionnaireAction(queId, queDefId, lastModified, user);
            result.setLastModified(dateModified);
            result.setModifiedBy(user.getDisplayName());

        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(e.toString());
        } 
    }
    
    public AttributePersistenceResult saveUpdateDeleteQuestionnaireGeneric(Long queId, Long daId, Long attributeId, Integer dynamicPosition, String value, String currency, Long queDefId, HttpServletRequest request) {
        assignUserSession(request);

        AttributePersistenceResult result = new AttributePersistenceResult();
        // nothing to do return immediately
        saveUpdateLastModifiedQuestionnaireAction(result,queId, queDefId, request);


        if (attributeId == null && !StringUtils.hasText(value)) {
            return result;
        }

        boolean validating = !((attributeId != null && !StringUtils.hasText(value)));
        if (validating) {
            if (!validateDynamicAttribute(daId, attributeId, value, currency, request, result)) return result;
        }
        try {
            User user = ZynapWebUtils.getUser(request);
            Long attrId = questionnaireService.saveUpdateDeleteQuestionAttribute(queId, daId, attributeId, dynamicPosition, value, currency, user);
            result.setAttributeId(attrId != null ? attrId.toString() : "");
            dynamicCalculations(daId, queId, result, user);

        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            clearUserUsession(request);
        }
        return result;
    }

    public AttributePersistenceResult deleteDynamicLineItemRow(Long queId,Long queDefId, Integer dynamicPosition, Long lineItemId, HttpServletRequest request) {
        assignUserSession(request);
        AttributePersistenceResult result = new AttributePersistenceResult();
        saveUpdateLastModifiedQuestionnaireAction(result,queId,queDefId,request);
        try {
            questionnaireService.deleteLineItemAnswers(queId, dynamicPosition, lineItemId);
        } catch (Throwable e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            clearUserUsession(request);
        }
        return result;
    }

    private void dynamicCalculations(Long daId, Long queId, AttributePersistenceResult result, User user) throws TalentStudioException {

        DynamicAttribute dynamicAttribute = dynamicAttributeService.findById(daId);
        testResult(queId, result, dynamicAttribute, user);
    }

    private void testResult(Long queId, AttributePersistenceResult result, DynamicAttribute dynamicAttribute, User user) throws TalentStudioException {

        try {
            if (dynamicAttribute.getReferences().size() > 0) {

                Set<DynamicAttribute> uniqueReferences = getUniqueParentRefs(dynamicAttribute.getReferences());
                //final DynamicAttributeReference dynamicAttributeReference = dynamicAttribute.getReferences().get(0);

                for (DynamicAttribute root : uniqueReferences) {
                    String answer = QuestionCalculator.calculate(root, questionnaireService, queId, user);

                    if (answer == null) answer = ""; //default to empty - nothing set
                    result.addAnswer(answer, root.getId());

                    testResult(queId, result, root, user);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private Set<DynamicAttribute> getUniqueParentRefs(List<DynamicAttributeReference> references) {
        Set<DynamicAttribute> result = new HashSet<DynamicAttribute>();
        for (DynamicAttributeReference reference : references) {
            result.add(reference.getParentDa());
        }
        return result;
    }

    private ErrorMessageHandler validateDynamicAttribute(String value, String currency, DynamicAttribute dynamicAttribute) {
        AttributeWrapperBean answer = new AttributeWrapperBean(dynamicAttribute);
        answer.setValue(value);
        answer.setCurrency(currency);
        return validationFactory.getAttributeValidator(dynamicAttribute.getType()).validate(answer, null);
    }

    private boolean validationErrors(HttpServletRequest request, AttributePersistenceResult result, ErrorMessageHandler errorMessageHandler) {
        if (errorMessageHandler != null) {
            result.setErrorMessage(messageSource.getMessage(errorMessageHandler.getErrorKey(), errorMessageHandler.getErrorValues(), getLocale(request)));  //look resources - key
            return true;
        }
        return false;
    }

    private boolean validateDynamicAttribute(Long daId, Long attributeId, String value, String currency, HttpServletRequest request, AttributePersistenceResult result) {
        DynamicAttribute dynamicAttribute;
        try {
            dynamicAttribute = dynamicAttributeService.findById(daId);
        } catch (TalentStudioException e) {
            logger.error(e);
            return false;
        }

        final ErrorMessageHandler errorMessageHandler = validateDynamicAttribute(value, currency, dynamicAttribute);
        String sAttributeId = "";
        if (attributeId != null) sAttributeId = String.valueOf(attributeId);
        result.setAttributeId(sAttributeId);
        return !validationErrors(request, result, errorMessageHandler);
    }

    private Locale getLocale(HttpServletRequest request) {
        Locale locale = null;
        try {
            locale = request.getLocale();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return locale;
    }

    public void setQuestionnaireService(IQuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    public void setQueWorkflowService(IQueWorkflowService queWorkflowService) {
        this.queWorkflowService = queWorkflowService;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setValidationFactory(AttributeValueValidationFactory validationFactory) {
        this.validationFactory = validationFactory;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    private IQueWorkflowService queWorkflowService;
    private IQuestionnaireService questionnaireService;
    private static final Log logger = LogFactory.getLog(QuestionnaireDwrBean.class);

    private MessageSource messageSource;
    private AttributeValueValidationFactory validationFactory;
    private IDynamicAttributeService dynamicAttributeService;
}
