/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.portfolio;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.analysis.reports.Report;
import com.zynap.talentstudio.display.DisplayConfig;
import com.zynap.talentstudio.display.DisplayConfigItem;
import com.zynap.talentstudio.display.IDisplayConfigService;
import com.zynap.talentstudio.questionnaires.QuestionnaireDTO;
import com.zynap.talentstudio.web.organisation.BrowseNodeWrapper;
import com.zynap.talentstudio.web.organisation.DisplayContentWrapper;
import com.zynap.talentstudio.web.organisation.GroupMapKey;
import com.zynap.talentstudio.web.organisation.subjects.SubjectWrapperBean;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 26-Nov-2008 16:06:10
 */
public class MyPortfolioHelper {

    public static void setDisplayInfo(IDisplayConfigService displayConfigService,BrowseNodeWrapper browseNodeWrapper, HttpServletRequest request) throws TalentStudioException {

        DisplayContentWrapper displayContentWrapper = getPersonalExecSummaryViews(displayConfigService, request, browseNodeWrapper.getNodeType());
        browseNodeWrapper.setContentView(displayContentWrapper);
    }

    public static DisplayContentWrapper getPersonalExecSummaryViews(IDisplayConfigService displayConfigService, HttpServletRequest request, String nodeType) throws TalentStudioException {

        List<DisplayConfigItem> viewConfigs = displayConfigService.findUserDisplayItems(nodeType, DisplayConfig.MY_DETAILS_TYPE, ZynapWebUtils.getUserId(request));
        Report execSummaryReport = displayConfigService.findDisplayConfigReport(nodeType, DisplayConfig.MY_EXEC_SUMMARY_TYPE);

        String currentArena = ZynapWebUtils.getUserSession(request).getCurrentArenaId();
        DisplayContentWrapper displayContentWrapper = new DisplayContentWrapper();
        displayContentWrapper.setExecutiveSummaryReport(execSummaryReport);
        displayContentWrapper.setViewDisplayConfigItems(viewConfigs, currentArena);
        return displayContentWrapper;
    }

    public static void assignQuestionnaires(SubjectWrapperBean subjectWrapper, Collection<QuestionnaireDTO> questionnaires) {

        Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms = new LinkedHashMap<GroupMapKey, List<QuestionnaireDTO>>();
        List<QuestionnaireDTO> appraisals = new ArrayList<QuestionnaireDTO>();
        int index = 0;
        for (QuestionnaireDTO questionnaire : questionnaires) {
            if (questionnaire.isAnyAppraisal()) {
                appraisals.add(questionnaire);
            } else {
                if (questionnaire.isArchived()) continue;
                String groupName = questionnaire.getGroupLabel();
                // no group has been assigned to it, it will thus go in a list of it's own the field being defined in the messages.properties
                if (groupName == null) groupName = DEFAULT_QUESTIONNAIRE_GROUP;
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
        subjectWrapper.setAppraisals(appraisals);
        updateInfoForms(groupedInfoForms);
        subjectWrapper.setInfoForms(groupedInfoForms);
    }

    private static void updateInfoForms(Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms) {
        for (Map.Entry<GroupMapKey, List<QuestionnaireDTO>> entry : groupedInfoForms.entrySet()) {
            GroupMapKey key = entry.getKey();
            key.setNumValues(entry.getValue().size());
        }
    }

    public static final String DEFAULT_QUESTIONNAIRE_GROUP = "default.questionnaire.group";
}
