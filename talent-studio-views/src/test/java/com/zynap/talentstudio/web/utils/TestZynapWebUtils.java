package com.zynap.talentstudio.web.utils;

/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.common.ControllerConstants;

import javax.servlet.http.HttpSession;

public class TestZynapWebUtils extends ZynapMockControllerTest {

    public void testNullURL() throws Exception {

        String newURL = ZynapWebUtils.buildURL(null, new HashMap(1));
        assertNotNull(newURL);
        assertEquals(0, newURL.length());
    }

    public void testBuildURLAllNullParameters() throws Exception {

        // add 1 parameter with a null key
        Map parameters = new HashMap();
        final String nullParamValue = "null1";
        parameters.put(null, nullParamValue);

        String newURL = ZynapWebUtils.buildURL("url", parameters);

        assertEquals(0, StringUtils.countOccurrencesOf(newURL, ZynapWebUtils.PARAMETER_SEPARATOR));
        assertEquals(0, StringUtils.countOccurrencesOf(newURL, ZynapWebUtils.QUERY_STRING_SEPARATOR));
    }

    public void testBuildURLNullParameters() throws Exception {

        Map parameters = new HashMap();
        final String param1 = "param1";
        parameters.put(param1, "value");

        // add parameter with null parameter value
        final String param2 = "param2";
        parameters.put(param2, null);

        // add parameter with null key
        final String nullParamValue = "null1";
        parameters.put(null, nullParamValue);

        String newURL = ZynapWebUtils.buildURL("url?", parameters);

        // all the parameters other than the one with the null key should be present
        assertEquals(1, StringUtils.countOccurrencesOf(newURL, ZynapWebUtils.QUERY_STRING_SEPARATOR));
        assertEquals(1, StringUtils.countOccurrencesOf(newURL, param1));
        assertEquals(1, StringUtils.countOccurrencesOf(newURL, "param2"));
        assertEquals(0, StringUtils.countOccurrencesOf(newURL, nullParamValue));
    }

    public void testBuildURL() throws Exception {

        Map parameters = new HashMap();
        parameters.put("test", "value");
        parameters.put("test2", "value");
        parameters.put("test3", "");

        String url1 = ZynapWebUtils.buildURL("url?", parameters);
        assertEquals(1, StringUtils.countOccurrencesOf(url1, ZynapWebUtils.QUERY_STRING_SEPARATOR));

        String url2 = ZynapWebUtils.buildURL("url", parameters);
        assertEquals(1, StringUtils.countOccurrencesOf(url2, ZynapWebUtils.QUERY_STRING_SEPARATOR));
        assertEquals(parameters.size() - 1, StringUtils.countOccurrencesOf(url2, ZynapWebUtils.PARAMETER_SEPARATOR));

        parameters.clear();
        String url3 = ZynapWebUtils.buildURL("url", parameters);
        assertEquals(0, StringUtils.countOccurrencesOf(url3, ZynapWebUtils.QUERY_STRING_SEPARATOR));
        assertEquals(0, StringUtils.countOccurrencesOf(url3, ZynapWebUtils.PARAMETER_SEPARATOR));

        parameters.put("test", "angus");
        String url4 = ZynapWebUtils.buildURL("url?test=fred", parameters);
        assertEquals(1, StringUtils.countOccurrencesOf(url4, ZynapWebUtils.QUERY_STRING_SEPARATOR));
        assertEquals(1, StringUtils.countOccurrencesOf(url4, "test"));
    }

    public void testParseQueryString() throws Exception {
        Map params = ZynapWebUtils.parseQueryString(null);
        assertEquals(0, params.size());

        params = ZynapWebUtils.parseQueryString("");
        assertEquals(0, params.size());

        params = ZynapWebUtils.parseQueryString("name=angus&tab=fred&tab=");
        assertEquals(2, params.size());
        assertNotNull(params.get("name"));
        assertNotNull(params.get("tab"));
        assertEquals("", params.get("tab"));
    }

    public void testGetReferrerWithoutContextPath() throws Exception {

        final String contextPath = "talentstudio";
        final String url = ZynapWebUtils.PATH_SEPARATOR + "test.htm";

        mockRequest.setContextPath(contextPath);
        mockRequest.addHeader("referer", ZynapWebUtils.PATH_SEPARATOR + contextPath + url);

        String urlWithoutContextPath = ZynapWebUtils.getReferrerWithoutContextPath(mockRequest);
        assertEquals(url, urlWithoutContextPath);

        mockRequest = new MockHttpServletRequest();
        mockRequest.setContextPath(contextPath);
        urlWithoutContextPath = ZynapWebUtils.getReferrerWithoutContextPath(mockRequest);
        assertNull(urlWithoutContextPath);
    }

    public void testAddContextPath() throws Exception {

        mockRequest.setContextPath("talentstudio");
        String fullUrl = ZynapWebUtils.addContextPath(mockRequest, "/login.htm");
        assertEquals(0, StringUtils.countOccurrencesOf(fullUrl, "//"));
    }

    public void testGetValue() throws Exception {

        assertNull(ZynapWebUtils.getValue(mockRequest, "name"));

        mockRequest.setAttribute("name", "value");
        assertNotNull(ZynapWebUtils.getValue(mockRequest, "name"));
        assertEquals("value", ZynapWebUtils.getValue(mockRequest, "name"));

        mockRequest.removeAttribute("name");
        WebUtils.setSessionAttribute(mockRequest, "name", "sessionValue");
        assertNotNull(ZynapWebUtils.getValue(mockRequest, "name"));
        assertEquals("sessionValue", ZynapWebUtils.getValue(mockRequest, "name"));
    }

    public void testIsGetRequest() throws Exception {

        mockRequest.setMethod("HEAD");
        assertFalse(ZynapWebUtils.isGetRequest(mockRequest));

        mockRequest.setMethod("GET");
        assertTrue(ZynapWebUtils.isGetRequest(mockRequest));

        mockRequest.setMethod("get");
        assertFalse(ZynapWebUtils.isGetRequest(mockRequest));
    }

    public void testIsPostRequest() throws Exception {

        mockRequest.setMethod("get");
        assertFalse(ZynapWebUtils.isPostRequest(mockRequest));

        mockRequest.setMethod("post");
        assertFalse(ZynapWebUtils.isPostRequest(mockRequest));

        mockRequest.setMethod("POST");
        assertTrue(ZynapWebUtils.isPostRequest(mockRequest));
    }

    public void testCheckForArena() throws Exception {

        // try with null list - should return false
        List arenaNames = null;
        assertFalse(ZynapWebUtils.checkForArena(arenaNames, mockRequest).booleanValue());

        // try with list containing admin arena - should return false
        mockRequest.setRequestURI("/orgbuilder/test.htm");
        arenaNames = new ArrayList();
        arenaNames.add("admin");
        assertFalse(ZynapWebUtils.checkForArena(arenaNames, mockRequest).booleanValue());

        // try with list containing orgbuilder arena - should return true                
        arenaNames.add("orgbuilder");
        assertTrue(ZynapWebUtils.checkForArena(arenaNames, mockRequest).booleanValue());
    }

    public void testGetArenaName() throws Exception {
        assertEquals("orgbuilder", ZynapWebUtils.getArenaName("/orgbuilder/test.htm"));
    }

    public void testRemoveQueryString() throws Exception {

        // try with null
        assertNull(ZynapWebUtils.removeQueryString(null));

        // test w'out query string
        assertEquals("test.htm", ZynapWebUtils.removeQueryString("test.htm"));

        // test w'out query string
        assertEquals("test.htm", ZynapWebUtils.removeQueryString("test.htm"));

        // test w query string
        assertEquals("test.htm", ZynapWebUtils.removeQueryString("test.htm?id=1"));
    }

    public void testSaveCurrentURI() throws Exception {

        final String url = "/viewuser.htm";
        final String currentURI = BASE_URL + url;

        mockRequest.setRequestURI(currentURI);
        ZynapWebUtils.saveCurrentURI(mockRequest);

        assertEquals(url, ZynapWebUtils.getCurrentURI(mockRequest));

    }

    public void testSavePreviousURI() throws Exception {

        final String previousURI = BASE_URL + "/viewuser.htm";

        mockRequest.setRequestURI(previousURI);
        ZynapWebUtils.savePreviousURI(mockRequest);

        assertEquals(previousURI, ZynapWebUtils.getPreviousURI(mockRequest));
    }

    public void testCheckURLChange() throws Exception {

        final String currentURI = BASE_URL + "/viewuser.htm";

        mockRequest.setRequestURI(currentURI);
        ZynapWebUtils.savePreviousURI(mockRequest);

        assertFalse(ZynapWebUtils.checkURLChange(mockRequest));

        // change request uri - this should now indicate that the URL has changed
        String newURI = "viewposition.htm";
        mockRequest.setRequestURI(newURI);
        assertTrue(ZynapWebUtils.checkURLChange(mockRequest));
    }

    public void testAppendQueryString() throws Exception {

        final String parameterToExclude = "sessionid";

        String uri = "viewuser.htm";
        mockRequest.setRequestURI(uri);
        mockRequest.setQueryString("id=1&sessionid=2&arena=4");

        final String newURI = ZynapWebUtils.appendQueryString(uri, mockRequest, parameterToExclude);
        assertNotNull(newURI);
        assertTrue(newURI.indexOf(parameterToExclude) == -1);
        assertTrue(newURI.indexOf(ZynapWebUtils.ENCODED_PARAMETER_SEPARATOR) == -1);
    }

    public void testIsCancelled() throws Exception {
        mockRequest.setParameter(ParameterConstants.CANCEL_PARAMETER, "CANCEL");
        assertTrue(ZynapWebUtils.isCancelled(mockRequest));
        mockRequest.removeParameter(ParameterConstants.CANCEL_PARAMETER);
        assertFalse(ZynapWebUtils.isCancelled(mockRequest));
    }

    public void testRemoveCommandFromSession() throws Exception {
        
        final String attrName = "test." + ControllerConstants.COMMAND_NAME;
        HttpSession mockSession = mockRequest.getSession();
        mockSession.setAttribute(attrName, "blah");
        mockRequest.setSession(mockSession);

        // check that the command has been added correctly before removing it
        assertNotNull(mockRequest.getSession().getAttribute(attrName));
        ZynapWebUtils.removeCommandFromSession(mockRequest);

        // check the command has been removed correctly.
        assertNull(mockRequest.getSession().getAttribute(attrName));
    }
}