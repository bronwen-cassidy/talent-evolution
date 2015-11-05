/* 
 * Copyright (c) TalentScope Ltd. 2008
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.domain.admin.User;
import com.zynap.talentstudio.analysis.populations.HibernatePopulationEngine;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.organisation.attributes.IDynamicAttributeService;
import com.zynap.talentstudio.organisation.positions.IPositionService;
import com.zynap.talentstudio.organisation.subjects.ISubjectService;
import com.zynap.talentstudio.organisation.subjects.Subject;
import com.zynap.talentstudio.web.display.support.ArtefactViewQuestionnaireHelper;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.web.workflow.WorkflowConstants;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class or Interface description.
 *
 * @author taulant.bajraktari
 * @since 08-Sep-2008 12:42:15
 */
public class QuestionnairePdfExporter implements Controller {

    public ModelAndView handleRequest(final HttpServletRequest request, HttpServletResponse response) throws Exception {

        final Long workflowId = RequestUtils.getLongParameter(request, WorkflowConstants.WORKFLOW_ID_PARAM_PREFIX);
        String x = RequestUtils.getStringParameter(request, "subjectsIds[]", null);
        final String[] subjectIds = StringUtils.commaDelimitedListToStringArray(x);
        final User user = ZynapWebUtils.getUser(request);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final ZipOutputStream zipfile = new ZipOutputStream(bos);
        String zipLabel = "questionnaires.zip";

        //int numThreads = subjectIds.length;
        //final BlockingQueue<QuestionnaireWrapper> queue = new ArrayBlockingQueue<QuestionnaireWrapper>(numThreads, false);
        //List<QuestionnaireWrapper> tempList = new ArrayList<QuestionnaireWrapper>();
        //final ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (String subjectId : subjectIds) {
            QuestionnaireWrapper questionnaireWrapper = questionnaireHelper.getQuestionnaireWrapper(null, workflowId, user, new Long(subjectId), false);
            questionnaireWrapper.setMyPortfolio(false);
            String pdfLabel = questionnaireWrapper.getQuestionnaireLabel();
            ArtefactViewQuestionnaireHelper displayHelper = getDisplayHelper(user);

            byte[] pdf = questionnairePdfMakerHelper.createPdfDocument(user, questionnaireWrapper, messageSource, request, subjectService,
                    displayConfigService, displayHelper, mySummary);

            Subject subject = questionnairePdfMakerHelper.getSubject();

            if (subject != null) {
                final String label = subject.getLabel();
                final String fileName = pdfLabel + " - " + label + ".pdf";


                if (pdf.length > 1) {
                    ZipEntry zipentry = new ZipEntry(fileName);
                    zipfile.putNextEntry(zipentry);
                    zipfile.write(pdf);
                }
            }
            //tempList.add(questionnaireWrapper);
        }

//        queue.addAll(tempList);
//        
//        for (int i = 0; i < numThreads; i++) {
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        QuestionnaireWrapper questionnaireWrapper;
//
//                        while ((questionnaireWrapper = queue.poll(1000, TimeUnit.MILLISECONDS)) != null) {
//
//                            String pdfLabel = questionnaireWrapper.getQuestionnaireLabel();
//                            ArtefactViewQuestionnaireHelper displayHelper = getDisplayHelper(user);
//
//                            byte[] pdf = questionnairePdfMakerHelper.createPdfDocument(user, questionnaireWrapper, messageSource, request, subjectService,
//                                    displayConfigService, displayHelper, mySummary);
//
//                            final String fileName = pdfLabel + " - " + questionnaireWrapper.getSubjectUser().getDisplayName() + ".pdf";
//
//
//                            if (pdf.length > 1) {
//                                ZipEntry zipentry = new ZipEntry(fileName);
//                                zipfile.putNextEntry(zipentry);
//                                zipfile.write(pdf);
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//        executor.shutdown();
//        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        zipfile.close();
        ResponseUtils.writeToResponse(response, request, zipLabel, bos.toByteArray(), true);

        return null;
    }

    public ArtefactViewQuestionnaireHelper getDisplayHelper(User user) {
        ArtefactViewQuestionnaireHelper artefactViewQuestionnaireHelper = new ArtefactViewQuestionnaireHelper(populationEngine);
        artefactViewQuestionnaireHelper.setPositionService(positionService);
        artefactViewQuestionnaireHelper.setSubjectService(subjectService);
        artefactViewQuestionnaireHelper.setUserId(user.getId());
        artefactViewQuestionnaireHelper.setDynamicAttributeService(dynamicAttributeService);
        return artefactViewQuestionnaireHelper;
    }

    public void setQuestionnaireHelper(QuestionnaireHelper questionnaireHelper) {
        this.questionnaireHelper = questionnaireHelper;
    }

    public void setQuestionnairePdfMakerHelper(QuestionnairePdfMakerHelper questionnairePdfMakerHelper) {
        this.questionnairePdfMakerHelper = questionnairePdfMakerHelper;
    }

    public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setSubjectService(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    public void setDisplayConfigService(IDisplayConfigService displayConfigService) {
        this.displayConfigService = displayConfigService;
    }

    public void setDynamicAttributeService(IDynamicAttributeService dynamicAttributeService) {
        this.dynamicAttributeService = dynamicAttributeService;
    }

    public void setPositionService(IPositionService positionService) {
        this.positionService = positionService;
    }

    public void setPopulationEngine(HibernatePopulationEngine populationEngine) {
        this.populationEngine = populationEngine;
    }

    protected QuestionnaireHelper questionnaireHelper;
    protected QuestionnairePdfMakerHelper questionnairePdfMakerHelper;
    protected ReloadableResourceBundleMessageSource messageSource;
    protected ISubjectService subjectService;
    protected IDisplayConfigService displayConfigService;
    protected IDynamicAttributeService dynamicAttributeService;
    protected IPositionService positionService;
    protected HibernatePopulationEngine populationEngine;
    protected boolean mySummary;
}
