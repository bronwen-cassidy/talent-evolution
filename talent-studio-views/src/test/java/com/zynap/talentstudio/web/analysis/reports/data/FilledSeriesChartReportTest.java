package com.zynap.talentstudio.web.analysis.reports.data;

import org.junit.Test;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class FilledSeriesChartReportTest {
	
	@Test
	public void testSubtractCollations() {
		List<String> a = new ArrayList<>();
		a.add("achived");
		a.add("achived");
		a.add("achived");
		List<String> b = new ArrayList<>();
		b.add("achived");
		b.add("achived +");
		b.add("in progess");
		
		Collection<String> test = CollectionUtils.subtract(b, a);
		System.out.println(test);
		
	}
}