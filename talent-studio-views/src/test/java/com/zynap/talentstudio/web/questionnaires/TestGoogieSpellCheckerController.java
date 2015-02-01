/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.questionnaires;
/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @since 14-Jan-2009 13:20:28
 * @version 0.1
 */

import junit.framework.TestCase;

import com.zynap.exception.TalentStudioException;
import com.zynap.talentstudio.questionnaires.GoogleSpellCheckService;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestGoogieSpellCheckerController extends TestCase {


    public void testGoogieSpellChecker() throws TalentStudioException {
        StringBuffer query = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><spellrequest textalreadyclipped=\"0\" ignoredups=\"1\" ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
        query.append("where an i breat");
        query.append("</text></spellrequest>");
        GoogleSpellCheckService service = new GoogleSpellCheckService();

        String result = service.doSpellCheckXML(query.toString());
        assertNotNull(result);
        System.out.println("result " + result);
    }

    public void testGoogieSpellCheckerInvalidChars() throws TalentStudioException {
        StringBuffer query = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><spellrequest textalreadyclipped=\"0\" ignoredups=\"1\" ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
        query.append("<![CDATA[this is t<c> mt and bee]]>");
        query.append("</text></spellrequest>");
        GoogleSpellCheckService service = new GoogleSpellCheckService();

        String result = service.doSpellCheckXML(query.toString());
        assertNotNull(result);
        System.out.println("result " + result);
    }

    public void testGoogleUrl() throws IOException {

        StringBuffer query = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><spellrequest textalreadyclipped=\"0\" ignoredups=\"1\" ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
        query.append("where an i breat");
        query.append("</text></spellrequest>");
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
            System.out.println("***** Can't init the SSLContent !\n" + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("***** Can;t find SSL !\n" + e);
        }

        // For some unknown reason I have to create a URL instance before the above takes affect!
        URL url = null;
        try {
            url = new URL("https://www.google.com/tbproxy/spell?lang=en&hl=en");
        } catch (MalformedURLException e) {
            // Should never get here !
        }


        assert url != null;
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(query.toString());
        out.close();

        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            System.out.println("output " + inputLine);
        }
        in.close();
    }
}
