/* 
 * Copyright (c) Zynap Ltd. 2006
 * All rights reserved.
 */
package com.zynap.talentstudio.web.organisation;

import com.zynap.talentstudio.web.organisation.attributes.AttributeWrapperBean;

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
		return xAxis != null ? xAxis.getDisplayValue() : null;
	}

	public String getYValue() {
		return yAxis != null ? yAxis.getDisplayValue() : null;
	}

	public AttributeWrapperBean getxAxis() {
		return xAxis;
	}

	public AttributeWrapperBean getyAxis() {
		return yAxis;
	}

	private final AttributeWrapperBean xAxis;
	private final AttributeWrapperBean yAxis;
}
