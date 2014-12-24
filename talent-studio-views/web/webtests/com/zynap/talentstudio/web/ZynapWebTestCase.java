/*
 * Copyright (c) 2002 Zynap Limited. All rights reserved.
 */
package com.zynap.talentstudio.web;

import com.meterware.httpunit.Button;
import com.meterware.httpunit.HTMLElementPredicate;
import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.TableCell;
import com.meterware.httpunit.WebClient;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebLink;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebWindow;
import junit.framework.AssertionFailedError;
import net.sourceforge.jwebunit.TestContext;
import net.sourceforge.jwebunit.WebTestCase;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.zynap.talentstudio.web.common.ParameterConstants;
import com.zynap.talentstudio.web.utils.ZynapWebUtils;
import com.zynap.talentstudio.UnitTestConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version $Revision: $
 *          $Id: $
 */
public abstract class ZynapWebTestCase extends WebTestCase {

    public ZynapWebTestCase() {

        HttpUnitOptions.setExceptionsThrownOnScriptError(false);

        loadBaseUrl();
        acceptAllCertificates();
        TestContext testContext = getTestContext();
        testContext.setBaseUrl(baseUrl);

        // Set to not load iframes - we currently use an iframe
        // for the org unit pop-up and the path to the src is wrong so we must ignore this.
        testContext.getWebClient().getClientProperties().setIframeSupported(false);
    }

    public void beginAt(String url) {
        beginAt(url, DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    protected final void beginAt(String url, String userName, String password) {

        goToURL(url);
        checkForErrors();

        if (getResponseURL().indexOf(getLoginPage()) != -1) {
            startAndLogin(userName, password);
            acceptPolicyStatement();
        }
    }

    protected final void goToURL(String url) {

        // remove absolute path from URL
        if (url.startsWith("http")) {
            final String[] strings = url.split("/");
            final int length = strings.length;

            url = strings[strings.length - 1];
            if (length == 6) {
                url = strings[strings.length - 2] + "/" + url;
            }
        }

        super.beginAt(url);
    }

    /**
     * Check for back button form and button in page.
     */
    protected final void assertBackButtonPresent() {
        assertFormPresent(BACK_FORM_NAME);
        assertFormElementPresent("history_back");
        assertButtonPresent(BACK_BUTTON_ID);
    }

    protected final void assertCancelButtonPresent() {
        assertButtonPresent(CANCEL_BUTTON_ID);
    }

    protected final void assertSaveButtonPresent() {
        assertButtonPresent(SAVE_BUTTON_ID);
    }

    protected final WebForm getBackForm() {
        return getForm(BACK_FORM_NAME);
    }

    protected final WebForm getCancelForm() {
        return getForm(CANCEL_FORM_NAME);
    }

    /**
     * Return the base Url where all testing to start at
     */
    protected final String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Return the starting page URL, relative to the base URL
     */
    protected final String getLoginPage() {
        return "login.htm";
    }

    /**
     * Check title - appends "Talent Studio - " to the title parameter.
     *
     * @param title
     */
    protected final void assertAppTitleEquals(String title) {
        assertTitleEquals("Talent Studio - " + title);
    }

    protected final void assertTabIsActive(String tabId, String expectedLabel) throws Exception {
        final TableCell tdElement = (TableCell) getWebResponse().getElementWithID(tabId);
        assertEquals(tdElement.getClassName(), "tab_tab tab_active_tab");
        assertEquals(expectedLabel, tdElement.getDOM().getFirstChild().getNodeValue());
    }

    protected final void assertTabIsNotActive(String tabId, String expectedLabel) throws Exception {
        final TableCell tdElement = (TableCell) getWebResponse().getElementWithID(tabId);
        assertEquals(tdElement.getClassName(), "tab_tab tab_inactive_tab");
        assertEquals(expectedLabel, tdElement.getDOM().getFirstChild().getNodeValue());
    }

    /**
     * Get a link based on the text.
     *
     * @param linkText
     * @return
     * @throws SAXException
     */
    protected final WebLink getLink(String linkText) throws SAXException {
        return getWebResponse().getLinkWith(linkText);
    }

    protected final WebResponse getWebResponse() {
        return getDialog().getResponse();
    }

    protected final Button getButtonByIdOrName(WebForm form, String idOrName) {

        Button button = getButtonById(form, idOrName);
        if (button == null) {
            button = getButtonByName(form, idOrName);
        }

        return button;
    }

    protected final Button getButtonByName(WebForm form, String buttonName) {

        final Button[] buttons = form.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            Button button1 = buttons[i];
            if (buttonName.equals(button1.getName())) {
                return button1;
            }
        }

        return null;
    }

    protected final Button getButtonById(WebForm form, String id) {

        final Button[] buttons = form.getButtons();
        for (int i = 0; i < buttons.length; i++) {
            Button button1 = buttons[i];
            if (id.equals(button1.getID())) {
                return button1;
            }
        }

        return null;
    }

    /**
     * Overriden to dump response.
     *
     * @param text
     */
    public void assertTextPresent(String text) {
        try {
            super.assertTextPresent(text);
        } catch (AssertionFailedError e) {
            logResponse();
            throw e;
        }
    }

    /**
     * Overriden to dump response.
     *
     * @param text
     */
    public void assertTextNotPresent(String text) {
        try {
            super.assertTextNotPresent(text);
        } catch (AssertionFailedError e) {
            logResponse();
            throw e;
        }
    }

    /**
     * Get a link based on the id.
     *
     * @param linkId
     * @return
     * @throws SAXException
     */
    protected final WebLink getLinkWithID(String linkId) throws SAXException {
        return getWebResponse().getLinkWithID(linkId);
    }

    /**
     * Get a link using the supplied predicate.
     *
     * @param predicate
     * @return
     * @throws SAXException
     */
    protected final WebLink getLink(HTMLElementPredicate predicate, String text) throws SAXException {
        return getWebResponse().getFirstMatchingLink(predicate, text);
    }

    /**
     * Start application and login as requested user.
     */
    protected final void startAndLogin(String user, String password) {

        super.beginAt(getLoginPage());

        setFormElement("username", user);
        setFormElement("password", password);
        submit();
    }

    /**
     * Should be on the policy page - accept the policy.
     */
    protected final void acceptPolicyStatement() {
        checkCheckbox("acceptedPolicy");
        submit();
    }

    /**
     * Check response URL.
     *
     * @param expected
     */
    protected final void assertResponseURLEquals(String expected) {

        final String responseURL = ZynapWebUtils.removeQueryString(getResponseURL());
        expected = ZynapWebUtils.removeQueryString(expected);

        assertTrue(responseURL != null && responseURL.indexOf(expected) != -1);
    }

    protected final String getResponseURL() {
        return getWebResponse().getURL().toString();
    }

    protected final void checkElementStyle(String spanName, String expectedStyle) {

        final Element detailsSpan = getDialog().getElement(spanName);
        final String style = detailsSpan.getAttribute("style");
        assertEquals(expectedStyle, style);
    }

    protected final void checkElementClass(String spanName, String expectedClass) {

        final Element detailsSpan = getDialog().getElement(spanName);
        final String cssClass = detailsSpan.getAttribute("class");
        assertEquals(expectedClass, cssClass);
    }

    protected final void checkForErrors() {
        assertElementNotPresent("error");
    }

    protected final void setHiddenField(WebForm form, String fieldName, String value) {
        form.getScriptableObject().setParameterValue(fieldName, value);
    }

    protected final WebForm getForm(String formName) {
        setWorkingForm(formName);
        return getDialog().getForm();
    }

    protected WebResponse getDownloadFileResponse(String frameName) {
        WebClient webClient = getDialog().getWebClient();
        WebWindow[] openWindows = webClient.getOpenWindows();
        for (int i = 0; i < openWindows.length; i++) {
            WebWindow openWindow = openWindows[i];
            WebResponse frameContents = openWindow.getFrameContents(frameName);
            if (frameContents != null && !TEXT_HTML_CONTENT_TYPE.equals(frameContents.getContentType())) {
                return frameContents;
            }
        }
        return null;
    }

    protected final void assertErrorPresent(String errorMessage) {
        assertTextPresent(errorMessage);
    }

    /**
     * Log response using logger.
     */
    private void logResponse() {
        logger.error(getDialog().getResponseText());
    }

    /**
     * Load the base URL for accessing the application. This comes from the
     * test.base.url users environment variable.
     */
    private void loadBaseUrl() {
        baseUrl = System.getProperty("test.base.url");

        if (baseUrl == null) {
            throw new IllegalArgumentException("test.base.url environment variable must be set");
        }

        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
    }

    /**
     * Override the default TrustManager and HostnameVerifier so that all SSL
     * certificates are automatically accepted.
     */
    private void acceptAllCertificates() {

        TrustManager[] openTrustManager = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        HostnameVerifier openHostnameVerifier = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, openTrustManager, null);

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(openHostnameVerifier);

        } catch (java.security.KeyManagementException e) {
            logger.error("***** Can't init the SSLContent !\n" + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            logger.error("***** Can;t find SSL !\n" + e);
        }

        // For some unknown reason I have to create a URL instance before the above takes affect!

        try {
            new URL("https://www.zynap.com");
        } catch (MalformedURLException e) {
            // Should never get here !
        }
    }

    public class OrgUnitLinkPredicate implements HTMLElementPredicate {

        public boolean matchesCriteria(Object o, Object o1) {
            WebLink link = (WebLink) o;
            return link.getURLString().indexOf(ZynapWebUtils.PARAMETER_SEPARATOR + ParameterConstants.ORG_UNIT_ID_PARAM) != -1;
        }
    }


    /**
     * Find link html element based on url.
     */
    public class LinkHTMLElementPredicate implements HTMLElementPredicate {

        public boolean matchesCriteria(Object htmlElement, Object criteria) {
            if (htmlElement instanceof WebLink) {
                final WebLink link = (WebLink) htmlElement;
                final String url = link.getURLString();

                String expectedUrl = (String) criteria;
                return url != null && url.indexOf(expectedUrl) >= 0;
            }

            return false;
        }
    }

    protected static final String DEFAULT_ORG_UNIT_ID = "0";
    protected static final String DEFAULT_POSITION_ID = "1";
    protected static final String DEFAULT_POSITION_LABEL = "Default Position";

    protected static final String ADD_FORM_NAME = "_add";
    protected static final String BACK_FORM_NAME = "_back";
    protected static final String CANCEL_FORM_NAME = "_cancel";
    protected static final String VIEW_FORM_NAME = "_view";

    protected static final String LABEL_FORM_ELEMENT_NAME = "label";
    protected static final String ACTIVE_FORM_ELEMENT_NAME = "active";

    protected static final String BACK_BUTTON_ID = "back";
    protected static final String SAVE_BUTTON_ID = "_save";
    protected static final String CANCEL_BUTTON_ID = "_cancel";

    protected static final String MENU_URL = "menu.htm";

    private String baseUrl = null;

    /**
     * Logger that is available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Constants for default username and password.
     */
    protected static final String DEFAULT_USERNAME = UnitTestConstants.ROOT_USERNAME;
    protected static final String DEFAULT_PASSWORD = UnitTestConstants.ROOT_PASSWORD;
    private static final String TEXT_HTML_CONTENT_TYPE = "text/html";
}
