/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.questionnaires;

import com.zynap.exception.TalentStudioException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Class or Interface description.
 *
 * @author taulant bajraktari
 * @version 0.1
 * @since 14-Jan-2009 14:17:47
 */
public class GoogleSpellCheckService implements IGoogleSpellCheckService {


    public String doSpellCheck(String text) throws TalentStudioException, IOException {
        StringBuffer result = new StringBuffer();
        StringBuffer query = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><spellrequest textalreadyclipped=\"0\" ignoredups=\"1\" ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
        query.append(text);
        query.append("</text></spellrequest>");
        getGoogleResult(result, query);
        return result.toString();
    }

    public String doSpellCheckXML(String text) throws TalentStudioException {

        StringBuffer result = new StringBuffer();
        try {
            StringBuffer requestXML = new StringBuffer();
            requestXML.append(text);

            getGoogleResult(result, requestXML);
        }
        catch (IOException e) {
            // log this exception it is out of our control and depending on the status            
        } catch (Exception e) {
            throw new TalentStudioException(e);
        }
        return result.toString();
    }

    private void getGoogleResult(StringBuffer result, StringBuffer requestXML) throws IOException, TalentStudioException {
        acceptCertificateExceptions();
        URL url = new URL(googleUrl);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        out.write(requestXML.toString());
        out.close();

        InputStream in = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
    }

    private void acceptCertificateExceptions() throws TalentStudioException {

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
            throw new TalentStudioException("***** Can't init the SSLContent !\n" + e);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new TalentStudioException("***** Can;t find SSL !\n" + e);
        }
    }

    private static final String googleUrl = "https://www.google.com/tbproxy/spell?lang=en&hl=en";
//    private static final String googleUrl = "https://209.85.171.100/tbproxy/spell?lang=en&hl=en";

}
