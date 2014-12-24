/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.web.utils.ResponseUtils;
import com.zynap.talentstudio.web.utils.RequestUtils;
import com.zynap.talentstudio.questionnaires.IGoogleSpellCheckService;

import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;


/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 14-Jan-2009 11:45:05
 */
public class GoogieSpellCheckerController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setContentType("text/xml");
        final String key = "q";
        String text=RequestUtils.getStringParameter(request, key,"");
        String result=spellCheckService.doSpellCheckXML(text);
        if(StringUtils.hasText(result)){
            ResponseUtils.writeToResponse(response, request, "", result.getBytes(), true);
        }
        return null;
    }


    public void setSpellCheckService(IGoogleSpellCheckService spellCheckService) {
        this.spellCheckService = spellCheckService;
    }

    private IGoogleSpellCheckService spellCheckService;
}
