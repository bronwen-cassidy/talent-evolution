/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.exception.TalentStudioException;

import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 14-Jan-2009 14:17:10
 */
public interface IGoogleSpellCheckService {

    public String doSpellCheck(String text) throws TalentStudioException, IOException;

    String doSpellCheckXML(String text) throws TalentStudioException;
}
