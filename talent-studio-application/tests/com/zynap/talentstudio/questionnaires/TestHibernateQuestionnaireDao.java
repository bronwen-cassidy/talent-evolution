/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.AbstractHibernateTestCase;
import com.zynap.talentstudio.organisation.attributes.DynamicAttribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public class TestHibernateQuestionnaireDao extends AbstractHibernateTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        hibernateQuestionnaireDao = (HibernateQuestionnaireDao) applicationContext.getBean("questionnaireDao");
    }

    public void testGetDomainObjectClass() throws Exception {
        assertEquals(QuestionnaireWorkflow.class, hibernateQuestionnaireDao.getDomainObjectClass());
    }

    public void testCreateDefinition() throws Exception {
        QuestionnaireDefinition expected = createDefinition();

        QuestionnaireDefinition actual = hibernateQuestionnaireDao.findDefinition(expected.getId());
        assertEquals(expected, actual);
    }

    public void testGetPortfolioQuestionnaires() throws Exception {
        Collection<QuestionnaireDTO> dtos = hibernateQuestionnaireDao.getPortfolioQuestionnaires(new Long(2978));
        Map<GroupMapKey, List<QuestionnaireDTO>> groupedInfoForms = new LinkedHashMap<GroupMapKey, List<QuestionnaireDTO>>();
        int index = 0;
        for (QuestionnaireDTO questionnaire : dtos) {
            if (!questionnaire.isAnyAppraisal()) {

                String groupName = questionnaire.getGroupLabel();
                // no group has been assigned to it, it will thus go in a list of it's own the field being defined in the messages.properties
                if (groupName == null) groupName = "none";
                GroupMapKey key = new GroupMapKey(groupName, String.valueOf(index));
                List<QuestionnaireDTO> questionnaireDTOs = groupedInfoForms.get(key);
                if (questionnaireDTOs == null) {
                    questionnaireDTOs = new ArrayList<QuestionnaireDTO>();
                    groupedInfoForms.put(key, questionnaireDTOs);
                    index++;
                }
                questionnaireDTOs.add(questionnaire);
            }
        }

        System.out.println("groupedInfoForms = " + groupedInfoForms);
        assertNotNull(dtos);
    }

    public void testFindDefinition() throws Exception {
        QuestionnaireDefinition expected = createDefinition();
        QuestionnaireDefinition actual = findDefinition(expected.getId());
        assertEquals(expected, actual);
    }

    public void testFindDefinitionXmlDefNotNull() throws Exception {
        QuestionnaireDefinition expected = createDefinition();
        QuestionnaireDefinition actual = findDefinition(expected.getId());
        assertNotNull(actual);
    }

    public void testFindPublishedQuestionnaireDefinitions() throws Exception {
        final Collection publishedQuestionnaireDefinitions = hibernateQuestionnaireDao.findPublishedQuestionnaireDefinitions(null);
        assertNotNull(publishedQuestionnaireDefinitions);
    }

    public void testFindPublishedQuestionnaireDefinitions_NumSum() throws Exception {
        final Collection publishedQuestionnaireDefinitions = hibernateQuestionnaireDao.findPublishedQuestionnaireDefinitions(
                new String[]{DynamicAttribute.DA_TYPE_NUMBER, DynamicAttribute.DA_TYPE_SUM});
        assertNotNull(publishedQuestionnaireDefinitions);
    }

    public void testFindPublishedQuestionnaireDefinitions_StructEnum() throws Exception {
        final Collection publishedQuestionnaireDefinitions = hibernateQuestionnaireDao.findPublishedQuestionnaireDefinitions(
                new String[]{DynamicAttribute.DA_TYPE_STRUCT, DynamicAttribute.DA_TYPE_ENUM_MAPPING});
        assertNotNull(publishedQuestionnaireDefinitions);
    }

    public void testFindQuestionnaireDefinitions() throws Exception {
        final String[] questionTypes = {DynamicAttribute.DA_TYPE_STRUCT, DynamicAttribute.DA_TYPE_OU};
        final Collection questionnaireDefinitions = hibernateQuestionnaireDao.findPublishedQuestionnaireDefinitions(questionTypes);
        assertNotNull(questionnaireDefinitions);
    }

    public void testFindExpiredWorkflows() throws Exception {
        final Collection expiredWorkflows = hibernateQuestionnaireDao.findExpiredWorkflows();
        assertNotNull(expiredWorkflows);
    }

    private QuestionnaireDefinition createDefinition() throws TalentStudioException {
        final String xmlQueDefinition = createXmlQueDefinition();
        return createDefinition(xmlQueDefinition);
    }

    private QuestionnaireDefinition createDefinition(final String xmlQueDefinition) throws TalentStudioException {
        QuestionnaireDefinition questionnaireDefinition = new QuestionnaireDefinition();
        questionnaireDefinition.setLabel("Test Questionnaire Definition");

        hibernateQuestionnaireDao.createDefinition(questionnaireDefinition);
        return questionnaireDefinition;
    }

    private QuestionnaireDefinition findDefinition(Long definitionId) throws Exception {
        return hibernateQuestionnaireDao.findDefinition(definitionId);
    }


    private String createXmlQueDefinition() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        xml.append("<xml>\n");
        xml.append("\t<questionnaire name=\"Assessment of Potential\" title=\"Assessment of Potential\">\n");
        xml.append("\t\t<access name=\"Administrator\" />\n");
        xml.append("\t\t<access name=\"Super User\" />\n");
        xml.append("\t\t<access name=\"User\" />\n");
        xml.append("\t\t<group name=\" \">\n");
        xml.append("\t\t\t<narrative>An assessment of performance over the last three years.</narrative>\n");
        xml.append("\t\t</group>\t\t\t\t\n");
        xml.append("\t\t<group name=\"Next career move - position and timescale\">\n");
        xml.append("\t\t\t<question type=\"Select\" name=\"Function\" title=\" \">\n");
        xml.append("\t\t\t\t<answer value=\"Sales and Marketing\" text=\"Sales and Marketing\" />\n");
        xml.append("\t\t\t\t<answer value=\"Medical and Regulatory\" text=\"Medical and Regulatory\" />\n");
        xml.append("\t\t\t\t<answer value=\"IS\" text=\"IS\" />\n");
        xml.append("\t\t\t\t<answer value=\"HR\" text=\"HR\" />\n");
        xml.append("\t\t\t\t<answer value=\"Finance\" text=\"Finance\" />\n");
        xml.append("\t\t\t\t<answer value=\"PS and L\" text=\"PS and L\" />\n");
        xml.append("\t\t\t\t<answer value=\"Operations\" text=\"Operations\" />\n");
        xml.append("\t\t\t\t<answer value=\"R and D\" text=\"R and D\" />\n");
        xml.append("\t\t\t\t<answer value=\"US\" text=\"US\" />\n");
        xml.append("\t\t\t\t<answer value=\"General Management\" text=\"General Management\" />\n");
        xml.append("\t\t\t\t<answer value=\"Other\" text=\"Other\" />\n");
        xml.append("\t\t\t</question>\t\t\t\n");
        xml.append("\t\t\t<question type=\"MultiSelect\" name=\"Mobility Region\" title=\" \">\n");
        xml.append("\t\t\t\t<answer value=\"Any Region\" text=\"Any Region\" />\n");
        xml.append("\t\t\t\t<answer value=\"Europe Main\" text=\"Europe Main\" />\n");
        xml.append("\t\t\t\t<answer value=\"Europe Area I\" text=\"Europe Area I\" />\n");
        xml.append("\t\t\t\t<answer value=\"Europe Area II\" text=\"Europe Area II\" />\n");
        xml.append("\t\t\t\t<answer value=\"LAMEA\" text=\"LAMEA\" />\n");
        xml.append("\t\t\t\t<answer value=\"Asia Pacific\" text=\"Asia Pacific\" />\n");
        xml.append("\t\t\t\t<answer value=\"Japan\" text=\"Japan\" />\n");
        xml.append("\t\t\t\t<answer value=\"United States\" text=\"United States\" />\n");
        xml.append("\t\t\t\t<answer value=\"ISMO Central Functions\" text=\"ISMO Central Functions\" />\n");
        xml.append("\t\t\t</question>\n");
        xml.append("\t\t\t<question type=\"TextBox\" name=\"Comments\" title=\" \"></question>\n");
        xml.append("\t\t</group>\n");
        xml.append("\t</questionnaire>\n");
        xml.append("</xml>");

        return xml.toString();
    }

    public void testFindAllQuestionnaireWorkflowDTOs() throws Exception {
        Collection<QuestionnaireWorkflowDTO> workflowDTOs = hibernateQuestionnaireDao.findAllQuestionnaireWorkflowDTOs();
        assertNotNull(workflowDTOs);
    }

    class GroupMapKey implements Serializable {

        public GroupMapKey(String groupName, String ref) {
            this.groupName = groupName;
            this.ref = ref;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getRef() {
            return ref;
        }

        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GroupMapKey that = (GroupMapKey) o;

            if (!groupName.equals(that.groupName)) return false;

            return true;
        }

        public int hashCode() {
            return groupName.hashCode();
        }

        public String toString() {
            return groupName;
        }

        private String groupName;
        private boolean hidden;
        private String ref;
    }

    private HibernateQuestionnaireDao hibernateQuestionnaireDao;
}