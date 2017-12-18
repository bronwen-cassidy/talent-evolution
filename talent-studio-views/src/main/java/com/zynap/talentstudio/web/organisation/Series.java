/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.common.util.StringUtil;
import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 14:13:46
 */
public class Series {

	public Series(Column yAxisAttribute) {
		this.yAxisAttribute = yAxisAttribute;
		this.answers = new ArrayList<>();
	}

	public void add(AttributeWrapperBean yAnswer, AttributeWrapperBean xAnswer) {
		answers.add(new ChartPoint(xAnswer, yAnswer));
	}

	public List<ChartPoint> getAnswers() {
		return answers;
	}

	public String getSeriesName() {
		return yAxisAttribute.getLabel();
	}
	
	public String getXAnswers() {
		StringBuilder result = new StringBuilder();
		int i = 1;
		for (ChartPoint answer : answers) {
			AttributeWrapperBean attributeWrapperBean = answer.getxAxis();
			appendString(result, attributeWrapperBean);
			if(i++ < answers.size()) result.append(",");
		}
		return result.toString();
	}

	public String getYAnswers() {
		StringBuilder result = new StringBuilder();
		int i = 1;
		for (ChartPoint answer : answers) {
			final AttributeWrapperBean attributeWrapperBean = answer.getyAxis();
			appendString(result, attributeWrapperBean);
			if(i++ < answers.size()) result.append(",");
		}
		return result.toString();
	}

	private void appendString(StringBuilder result, AttributeWrapperBean attributeWrapperBean) {
		if(attributeWrapperBean == null) {
			result.append("'No Data'");	
		} else {
			result.append("'").append(attributeWrapperBean.getDisplayValue()).append("'");
		}
	}

	private final Column yAxisAttribute;
	private List<ChartPoint> answers; 
}
