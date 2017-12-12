/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.analysis.reports.Column;
import com.zynap.talentstudio.organisation.attributes.AttributeValue;
import com.zynap.talentstudio.organisation.attributes.NodeExtendedAttribute;
import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class or Interface description.
 *
 * @author bcassidy
 * @version 0.1
 * @since 29-Apr-2010 14:13:46
 */
public class ChartPoint {
	
	public ChartPoint(AttributeWrapperBean xAxis, AttributeWrapperBean yAxis) {
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
	
	public String getXValue() {
		return xAxis != null ? xAxis.getDisplayValue() : "0";
	}

	public String getYValue() {
		return yAxis != null ? yAxis.getDisplayValue() : "0";
	}

	private final AttributeWrapperBean xAxis;
	private final AttributeWrapperBean yAxis;
}
