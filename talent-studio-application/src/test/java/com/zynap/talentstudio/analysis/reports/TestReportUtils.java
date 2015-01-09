/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.analysis.reports;

import junit.framework.TestCase;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 18-Aug-2009 16:36:28
 */
public class TestReportUtils extends TestCase {

    public void testFormatCSV() {
        StringBuffer test = new StringBuffer();
        test.append("Full Name^Status^Date from^Date to^No. of Days^Type of activity^Comments^Authorised^Authorised On^Authorised By~Georgie Stadward^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Lisa Barber^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Victoria Burgess^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Laura Gaze^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Sarah Cunningham^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Natalie Pope^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Philip Purdy^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Sophia Hami^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~James Pearce^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Daniel O'Brien^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Edward Hobbs^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Tony Boness^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Samir Motala^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Chris Mahony^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Mary Taylor^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Isabel Bovaird^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Jennie Pittam^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Hasan Ali^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Aongus McGrane^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Stacey Thompson^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Jo Shepherd^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Christina Banerji^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Jean Annan^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Laura Graham^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Gemma Self^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Emma Hopkins^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Caroline Brindley^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Angela Emerson^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Emma Hay^Current Trainee^ -^ -^ -^ -^ -^ -^ -^ -~Curtis Abbott^Current Trainee^18 Aug 2008^29 Aug 2008^9^Annual Leave^ -^Yes^13 Aug 2008^Margaret Wilson~Helen Adewole^Current Trainee^18 Aug 2008^18 Aug 2008^2 hours^Training provided by placement^ESR Finance Workshop^Yes^29 Aug 2008^J Davis~  ^  ^22 Aug 2008^22 Aug 2008^1^Education programme^CIMA P1, MAPE^Yes^29 Aug 2008^J Davis~  ^  ^29 Aug 2008^29 Aug 2008^1^Education programme^CIMA P5, IM^Yes^29 Aug 2008^J Davis~Rizwan Ahmed^Current Trainee^04 Aug 2008^04 Aug 2008^1^Mentor meeting^\"Patrick .G Cheif exec Runwell hospital\"^Yes^ -^ -~  ^  ^15 Aug 2008^15 Aug 2008^1^Education programme^CIMA P1 MAPE^Yes^  ^  ");

        final String expected = "Full Name,Status,Date from,Date to,No. of Days,Type of activity,Comments,Authorised,Authorised On,Authorised By\r\n" +
                "Georgie Stadward,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Lisa Barber,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Victoria Burgess,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Laura Gaze,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Sarah Cunningham,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Natalie Pope,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Philip Purdy,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Sophia Hami,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "James Pearce,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Daniel O'Brien,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Edward Hobbs,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Tony Boness,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Samir Motala,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Chris Mahony,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Mary Taylor,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Isabel Bovaird,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Jennie Pittam,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Hasan Ali,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Aongus McGrane,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Stacey Thompson,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Jo Shepherd,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Christina Banerji,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Jean Annan,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Laura Graham,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Gemma Self,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Emma Hopkins,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Caroline Brindley,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Angela Emerson,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Emma Hay,Current Trainee, -, -, -, -, -, -, -, -\r\n" +
                "Curtis Abbott,Current Trainee,18 Aug 2008,29 Aug 2008,9,Annual Leave, -,Yes,13 Aug 2008,Margaret Wilson\r\n" +
                "Helen Adewole,Current Trainee,18 Aug 2008,18 Aug 2008,2 hours,Training provided by placement,ESR Finance Workshop,Yes,29 Aug 2008,J Davis\r\n" +
                "Helen Adewole,Current Trainee,22 Aug 2008,22 Aug 2008,1,Education programme,\"CIMA P1, MAPE\",Yes,29 Aug 2008,J Davis\r\n" +
                "Helen Adewole,Current Trainee,29 Aug 2008,29 Aug 2008,1,Education programme,\"CIMA P5, IM\",Yes,29 Aug 2008,J Davis\r\n" +
                "Rizwan Ahmed,Current Trainee,04 Aug 2008,04 Aug 2008,1,Mentor meeting,\"Patrick .G Cheif exec Runwell hospital\",Yes, -, -\r\n" +
                "Rizwan Ahmed,Current Trainee,15 Aug 2008,15 Aug 2008,1,Education programme,CIMA P1 MAPE,Yes, -, -";
        final String result = ReportUtils.formatCSV(test, ",");

        // there should be no blanks
        assertEquals(expected, result);
    }

    public void testFormatCsv() throws Exception {
        String input = "Full Name^Status^Date from^Date to^No. of Days^Type of activity^Comments^Authorised^Authorised On^Authorised By~" +
                "Helen Adewole^Current Trainee^18 Aug 2008^18 Aug 2008^2 hours^Training, provided, by placement^ESR Finance Workshop^Yes^29 Aug 2008^J Davis";

        String expected = "Full Name,Status,Date from,Date to,No. of Days,Type of activity,Comments,Authorised,Authorised On,Authorised By\r\n" +
                "Helen Adewole,Current Trainee,18 Aug 2008,18 Aug 2008,2 hours,\"Training, provided, by placement\",ESR Finance Workshop,Yes,29 Aug 2008,J Davis";

        final String actual = ReportUtils.formatCSV(new StringBuffer(input), ",");

        assertEquals(expected, actual);

    }
}
