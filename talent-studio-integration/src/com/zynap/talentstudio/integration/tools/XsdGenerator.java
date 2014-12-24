/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.integration.tools;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.zynap.exception.TalentStudioException;

import com.zynap.talentstudio.common.lookups.ILookupManager;
import com.zynap.talentstudio.organisation.Node;
import com.zynap.talentstudio.organisation.portfolio.IPortfolioService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.questionnaires.IQueWorkflowService;
import com.zynap.talentstudio.util.velocity.VelocityUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class XsdGenerator extends AbstractGenerator implements IXsdGenerator {

    public XsdGenerator() throws Exception {
        VelocityEngine velocityEngine = VelocityUtils.getEngine();
        template = velocityEngine.getTemplate(INTEGRATION_SCHEMA_TEMPLATE);
    }

    public void setPortfolioService(IPortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setQuestionnaireWorkflowService(IQueWorkflowService questionnaireWorkflowService) {
        this.questionnaireWorkflowService = questionnaireWorkflowService;
    }

    public String generateSchema() throws TalentStudioException {

        final StringWriter stringWriter = new StringWriter();
        final BufferedWriter writer = new BufferedWriter(stringWriter);

        try {

            generateSchema(writer);
            writer.flush();

        } catch (Exception e) {
            throw new TalentStudioException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException ignored) {
            }
        }

        return stringWriter.toString();
    }

    public void generateSchema(Writer writer) throws TalentStudioException {

        try {

            VelocityContext context = new VelocityContext();

            Collection positionAttributes = dynamicAttributeService.getAllActiveAttributes(Node.POSITION_UNIT_TYPE_, false);

            Collection subjectAttributes = dynamicAttributeService.getAllActiveAttributes(Node.SUBJECT_UNIT_TYPE_, false);
            Collection questionnaireAttributes = dynamicAttributeService.getAllActiveAttributes(Node.QUESTIONNAIRE_TYPE, false);

            List subjectAssociationValues = new ArrayList();
            subjectAssociationValues.addAll(lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_SUBJECT_ASSOC));
            subjectAssociationValues.addAll(lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_SUBJECT_ASSOC));

            List positionAssociationValues = new ArrayList();
            positionAssociationValues.addAll(lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_PRIMARY_POSITION_ASSOC));
            positionAssociationValues.addAll(lookupManager.findActiveLookupValues(ILookupManager.LOOKUP_TYPE_SECONDARY_POSITION_ASSOC));

            List subjectValues = new ArrayList();
            subjectValues.addAll(subjectService.findAllSubjectDTOs());

            List workFlows = new ArrayList();
            workFlows.addAll(questionnaireWorkflowService.findAllQuestionnaireWorkflowDTOs());

            context.put(POSITION_ATTRIBUTE_KEY, positionAttributes);
            context.put(SUBJECT_ATTRIBUTE_KEY, subjectAttributes);
            context.put(QUESTIONS_KEY, questionnaireAttributes);
            context.put(SUBJECT_ASSOCIATIONS_VALUE_KEY, subjectAssociationValues);
            context.put(POSITION_ASSOCIATIONS_VALUE_KEY, positionAssociationValues);
            context.put(CONTENT_TYPES_KEY, portfolioService.getAllContentTypes());
            context.put(SUBJECTS_KEY, subjectValues);
            context.put(WORKFLOWS_KEY, workFlows);

            if (template != null)
                template.merge(context, writer);

        } catch (ResourceNotFoundException e) {
            throw new TalentStudioException("Cannot find template " + INTEGRATION_SCHEMA_TEMPLATE, e);
        } catch (ParseErrorException e) {
            throw new TalentStudioException("Cannot parse template " + INTEGRATION_SCHEMA_TEMPLATE, e);
        } catch (Exception e) {
            throw new TalentStudioException(e);
        }
    }

    private Template template;
    private IPortfolioService portfolioService;
    private ISubjectService subjectService;
    private IQueWorkflowService questionnaireWorkflowService;

    private static final String INTEGRATION_SCHEMA_TEMPLATE = "schema/integrationTemplate.vm";
    private static final String POSITION_ATTRIBUTE_KEY = "positionAttributes";
    private static final String SUBJECT_ATTRIBUTE_KEY = "subjectAttributes";
    private static final String SUBJECT_ASSOCIATIONS_VALUE_KEY = "subjectAssociationValues";
    private static final String POSITION_ASSOCIATIONS_VALUE_KEY = "positionsAssociationValues";
    private static final String CONTENT_TYPES_KEY = "contentTypes";
    private static final String WORKFLOWS_KEY = "workflows";
    private static final String SUBJECTS_KEY = "subjects";
    private static final String QUESTIONS_KEY = "questions";

}