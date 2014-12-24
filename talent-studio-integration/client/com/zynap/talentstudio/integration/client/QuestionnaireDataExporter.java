/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.integration.client;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.DefinitionDTO;
import com.zynap.talentstudio.questionnaires.IQueDefinitionService;
import com.zynap.talentstudio.questionnaires.QuestionnaireXmlData;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * sunserver properties
 * -Dtest.db.url=jdbc:oracle:thin:@sunserver:1521:oradb -Dtest.db.username=tsdev -Dtest.db.password=tsdev -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.host=zynap89 -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy
 *
 * live server properties
 * -Dtest.db.url=jdbc:oracle:thin:@10.83.110.118:1521:ZYNTS01 -Dtest.db.username=TS3 -Dtest.db.password=TS3 -Dtest.base.url=http://localhost:7001/talent-studio -Dautonomy.host=zynap89 -Dautonomy.indexport=9001 -Dautonomy.aciport=9002   -Dautonomy.position.database=positiondata -Dautonomy.subject.database=subjectdata -Dautonomy.retries=3 -Dautonomy.connection.timeout=5000 -Dautonomy.searcher=mockAutonomySearcher -Dsender.email=bcassidy@zynap.com -Dtest.webservice.username=webserviceuser -Dtest.webservice.password=webserviceuser -Dsearch.engine=autonomy
 *
 * EXPECTED ANT BUILD FILE CALL:
 * ant run-question-calc -Ddb.username=tsdev -Dhost=localhost -Dsid=Oradb -Dqwf.id=101
 * @author bcassidy
 * @version 0.1
 * @since 20-Nov-2008 13:14:20
 */
public class QuestionnaireDataExporter extends IntegrationMain {

    public static void main(String[] args) {
        //String x = processArguments(args);
        System.out.println("********************************************************************************************************************");
        // extract the workflowId
        try {
            loadApp();
            execute();
            shutdown();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void execute() throws TalentStudioException {

        IQueDefinitionService questionnaireService = (IQueDefinitionService) getBean("questionnaireDefinitionService");
        List<DefinitionDTO> dtos = questionnaireService.listDefinitions();
        for (DefinitionDTO dto : dtos) {
            QuestionnaireXmlData questionnaireDefinitionXml = questionnaireService.findQuestionnaireDefinitionXml(dto.getId());
            String label = StringUtils.replace(dto.getLabel(), "/", "_") + ".xml";
            File f = new File(label);
            try {
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(questionnaireDefinitionXml.getXmlDefinition());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
