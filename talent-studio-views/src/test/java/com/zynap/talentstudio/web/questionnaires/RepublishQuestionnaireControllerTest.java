package com.zynap.talentstudio.web.questionnaires;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 *
 */
public class RepublishQuestionnaireControllerTest {

	@Test
	public void republishQuestionnaire() throws Exception {

		final DateFormat dateInstance = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, Locale.CANADA);
		final String format = dateInstance.format(new Date());
		System.out.println("format = " + format);
	}

}