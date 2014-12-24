package com.zynap.talentstudio.web.history;

/**
 * User: bcassidy
 * Date: 19-Apr-2006
 * Time: 10:00:20
 */

import com.zynap.talentstudio.ZynapTestCase;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.HashMap;

public class TestSavedURL extends ZynapTestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetCompleteURLNoParameters() throws Exception {

        String url = "test.html";

        Map paramMap = new HashMap();

        savedURL = new SavedURL(url, paramMap);
        final String completeURL = savedURL.getCompleteURL();
        assertEquals(url, completeURL);
    }

    public void testGetCompleteURL() throws Exception {

        String url = "test.html";
        final String paramName = "param1";
        final String[] values = new String[]{"1", "2", "3"};

        Map paramMap = new HashMap();
        paramMap.put(paramName, values);

        savedURL = new SavedURL(url, paramMap);
        final String completeURL = savedURL.getCompleteURL();
        assertEquals(ZynapWebUtils.buildURL(url, paramName, StringUtils.arrayToCommaDelimitedString(values)), completeURL);
    }

    private SavedURL savedURL;
}