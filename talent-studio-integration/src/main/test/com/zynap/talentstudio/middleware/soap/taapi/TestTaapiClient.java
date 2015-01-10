package com.zynap.talentstudio.middleware.soap.taapi;

import junit.framework.TestCase;

import com.zynap.talentstudio.middleware.soap.taapi.client.RespondentInfoType;
import com.zynap.talentstudio.organisation.subjects.Subject;

import java.util.List;

/**
 * Created by bronwen.
 * Date: 07/05/12
 * Time: 12:26
 */
public class TestTaapiClient extends TestCase {

    public void testFindAllRespondents() throws Exception {

        TaapiClient client = new TaapiClient();
        client.setClient("demo");
        client.setPassword("4321TS1234");
        client.setUsername("bronwen.cassidy@talentscope.com");

//        RespondentInfoType respondent = client.findRespondent("JBBQVC2974");

//        List<RespondentContentType> info = client.getContentInfo(new String[]{respondent.getRespondentID()}, "tips_for_communicating", "text");
//        if(info != null && info.size() > 0) {
//            RespondentContentType contentType = info.get(0);
//            String text = contentType.getText();
//            System.out.println("text = " + text);
//        }
        RespondentInfoType[] details = client.findResponentInfo(new String[] {"JBBQVC2974"});
        assertNotNull(details);
        RespondentInfoType respondentDetails = details[0];
        //List<RespondentInfoType> allRespondents = client.findAllRespondents();
        assertNotNull(respondentDetails);
        //System.out.println("allRespondents = " + allRespondents);
    }

    public void testFindAllRespondentsPopulation() throws Exception {
        TaapiClient client = new TaapiClient();
        client.setClient("demo");
        client.setPassword("4321TS1234");
        client.setUsername("bronwen.cassidy@talentscope.com");

        List<RespondentInfoType> allRespondents = client.findAllRespondents(new Long(101));
        assertNotNull(allRespondents);
        //System.out.println("allRespondents = " + allRespondents);
    }

    public void testCreateRespondent() throws Exception {
        TaapiClient client = new TaapiClient();
        client.setClient("demo");
        client.setPassword("4321TS1234");
        client.setUsername("bronwen.cassidy@talentscope.com");

        Subject s = new Subject();
        s.setEmail("test@123.com");
        s.setFirstName("Testing");
        s.setSecondName("Populations");
        s.setTelephone("123456789");
        s.setId(new Long(-123));

        client.createRespondent(s, new Long(101));

        List<RespondentInfoType> allRespondents = client.findAllRespondents(new Long(101));
        assertFalse(allRespondents.isEmpty());
    }

    public void testClearInfo() throws Exception {
        TaapiClient client = new TaapiClient();
        client.setClient("demo");
        client.setPassword("4321TS1234");
        client.setUsername("bronwen.cassidy@talentscope.com");
        client.setCallbackUrl("https://ynshosting.com/demo/editsubject.htm");

        //List<RespondentInfoType> allRespondents = client.findAllRespondents();
        RespondentInfoType respondent = client.findRespondent("WC44313551");
         // greyne.adekunde@gmail.co.uk
        // extID = 1249
        // pop
//        if (allRespondents.size() > 0) {
//            String[] respondentIds = new String[allRespondents.size()];
//            for (int i = 0; i < allRespondents.size(0.0); i++) {
//                 respondentIds[i] = allRespondents.get(i).getRespondentID();
//            }
//
//            client.clearInfo(respondentIds);
//
//            allRespondents = client.findAllRespondents();
//        }
        //System.out.println(allRespondents);
        System.out.println(respondent);
    }

}
