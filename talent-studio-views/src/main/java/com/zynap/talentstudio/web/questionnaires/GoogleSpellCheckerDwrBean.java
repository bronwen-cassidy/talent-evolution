/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.talentstudio.questionnaires.support.RepublishResults;
import com.zynap.talentstudio.questionnaires.IGoogleSpellCheckService;
import com.zynap.talentstudio.questionnaires.GoogleSpellCheckService;
import com.zynap.exception.TalentStudioException;

import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 14-Jan-2009 14:15:33
 */
public class GoogleSpellCheckerDwrBean {

    public String getSpelledWords(String text) throws TalentStudioException, IOException {

        return spellCheckService.doSpellCheck(text);
    }

    public String getSpelledWordsXML(String xml) throws TalentStudioException {
        return spellCheckService.doSpellCheckXML(xml);
    }

    public void setSpellCheckService(GoogleSpellCheckService spellCheckService) {
        this.spellCheckService = spellCheckService;
    }

    private GoogleSpellCheckService spellCheckService;


}
