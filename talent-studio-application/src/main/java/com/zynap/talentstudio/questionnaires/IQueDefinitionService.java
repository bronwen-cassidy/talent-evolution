package com.zynap.talentstudio.questionnaires;

import com.zynap.talentstudio.common.IZynapService;
import com.zynap.exception.TalentStudioException;

import java.util.List;

/**
 * Class or Interface description.
 *
 * @author syeoh
 * @version 0.1
 * @since 29-Jun-2007 17:44:39
 */
public interface IQueDefinitionService extends IZynapService {

    List<DefinitionDTO> listDefinitions();

    QuestionnaireDefinition findDefinition(Long queDefinitionId);

    List<QuestionnaireDefinition> findReportableDefinitions(String[] questionTypes) throws TalentStudioException;

    void createXml(QuestionnaireXmlData data);

    QuestionnaireXmlData findQuestionnaireDefinitionXml(Long questionnaireDefinitionId);
}
