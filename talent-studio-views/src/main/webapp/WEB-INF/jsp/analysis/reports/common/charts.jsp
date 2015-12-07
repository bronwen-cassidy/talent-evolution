<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.ChartConstants" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractChartProducer" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractBarChartProducer" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.processors.AbstractBarChartPercentLabelGenerator" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.AbstractOverlaidChartProducer" %>
<%@ page import="de.laures.cewolf.links.XYItemLinkGenerator" %>
<%@ page import="org.jfree.data.xy.XYDataset" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.awt.*" %>
<%@ page import="de.laures.cewolf.tooltips.XYToolTipGenerator" %>
<%@ page import="org.jfree.chart.plot.XYPlot" %>
<%@ page import="org.jfree.chart.JFreeChart" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.jfree.chart.renderer.xy.XYItemRenderer" %>
<%@ page import="de.laures.cewolf.DatasetProducer" %>
<%@ page import="org.jfree.data.time.Month" %>
<%@ page import="de.laures.cewolf.DatasetProduceException" %>
<%@ page import="org.jfree.data.time.TimeSeriesCollection" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.jfree.data.time.MovingAverage" %>
<%@ page import="org.jfree.data.time.TimeSeries" %>
<%@ page import="de.laures.cewolf.ChartPostProcessor" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>




<div class="chart" id="chart">
    <c:choose>
        <c:when test="${producer.hasValues}">
            <c:set var="labelProcessor" value="${labelProcessor}"/>
            <c:set var="chartView" value="${producer}"/>
            <%
                AbstractChartProducer chartProducer = (AbstractChartProducer) request.getAttribute(ChartConstants.PRODUCER_PARAM);
                final int width = chartProducer.getPreferredWidth();
                final int height = chartProducer.getPreferredHeight();
                final int legendHeight = chartProducer.getLegendHeight();
            %>
            <c:choose>

                <c:when test="${producer.overlaidChart}">



                    <%
                        final AbstractOverlaidChartProducer abstractOverlaidChartProducer = ((AbstractOverlaidChartProducer) chartProducer);
                        final String xAxisOverlaidLabel = abstractOverlaidChartProducer.getXAxisLabel();
                        final String yAxisOverlaidLabel = abstractOverlaidChartProducer.getYAxisLabel();
                        final String overlaidChartType = abstractOverlaidChartProducer.getChartOrientation();
                        final AbstractOverlaidChartProducer.OverlaidBarChartProducer overlaidBarChartProducer=abstractOverlaidChartProducer.getOverlaidBarChartProducer();
                        request.setAttribute("overlaidBarChartProducer",overlaidBarChartProducer);

                    %>

                    <c:set var="barChartView" value="${overlaidBarChartProducer}"/>
                    <cewolf:overlaidchart
                            id="overlaid"
                            type="<%=overlaidChartType%>"
                            showlegend="false"
                            xaxistype="number"
                            yaxistype="number"
                            xaxislabel="<%= xAxisOverlaidLabel %>"
                            yaxislabel="<%=yAxisOverlaidLabel%>"
                            antialias="true">


                        <cewolf:plot type="xyline">
                            <cewolf:data>
                                <cewolf:producer id="chartView"/>
                            </cewolf:data>
                        </cewolf:plot>

                        <cewolf:plot type="xyverticalbar">
                            <cewolf:data>
                                <cewolf:producer id="barChartView"/>
                            </cewolf:data>
                        </cewolf:plot>

                    </cewolf:overlaidchart>

                    <cewolf:img chartid="overlaid" renderer="/cewolf" width="<%=width%>" height="<%=height%>" alt="Overlaid chart">
                        <cewolf:map linkgeneratorid="barChartView" tooltipgeneratorid="barChartView"/>
                    </cewolf:img>


                    <div class="legend">
                        <cewolf:legend id="overlaid" renderer="/cewolf" width="<%=width%>" height="<%=legendHeight%>"/>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:choose>

                        <c:when test="${producer.barChart}">
                            <%
                                final AbstractBarChartProducer abstractBarChartProducer = (AbstractBarChartProducer) chartProducer;
                                final String xAxisLabel = abstractBarChartProducer.getXAxisLabel();
                                final String yAxisLabel = abstractBarChartProducer.getYAxisLabel();
                                final String chartType = abstractBarChartProducer.getChartOrientation();
                                AbstractBarChartPercentLabelGenerator percentProcessor = (AbstractBarChartPercentLabelGenerator) request.getAttribute(ChartConstants.PERCENT_ITEM_PROCESSOR);
                            %>


                            <!-- add the bar chart result here passing in rows and horizontal headings and vertical headings here -->
                            <cewolf:chart id="barChart" type="<%=chartType%>" showlegend="false" xaxislabel="<%= xAxisLabel %>"
                                          yaxislabel="<%=yAxisLabel%>" antialias="true">
                                <cewolf:data>
                                    <cewolf:producer id="chartView"/>
                                </cewolf:data>
                                <cewolf:colorpaint color="#fafafa"/>                                
                                <cewolf:chartpostprocessor id="labelProcessor">
                                    <cewolf:param name="<%=ChartConstants.PERCENT_ITEM_PROCESSOR%>" value="<%=percentProcessor%>"/>
                                </cewolf:chartpostprocessor>
                            </cewolf:chart>

                            <cewolf:img chartid="barChart" renderer="/cewolf" width="<%=width%>" height="<%=height%>" alt="Bar chart">
                                <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
                            </cewolf:img>

                            <div class="legend">
                                <cewolf:legend id="barChart" renderer="/cewolf" width="<%=width%>" height="<%=legendHeight%>"/>
                            </div>


                        </c:when>
                        <c:otherwise>

                            <h2>
                                <fmt:message key="pie.chart.label">
                                    <c:forEach var="item" items="${producer.columnLabelItems}">
                                        <fmt:param value="${item}"/>
                                    </c:forEach>
                                </fmt:message>
                            </h2>

                            <cewolf:chart id="pieChart" type="pie" showlegend="false">
                                <cewolf:data>
                                    <cewolf:producer id="chartView"/>
                                </cewolf:data>
                                <cewolf:colorpaint color="#fafafa00"/>
                                <cewolf:chartpostprocessor id="labelProcessor"/>
                            </cewolf:chart>

                            <cewolf:img chartid="pieChart" renderer="/cewolf" width="800" height="800" alt="Pie chart">
                                <cewolf:map linkgeneratorid="chartView" tooltipgeneratorid="chartView"/>
                            </cewolf:img>

                            <div class="legend">
                                <cewolf:legend id="pieChart" renderer="/cewolf" width="<%=width%>" height="<%=legendHeight%>"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <div class="infomessage"><fmt:message key="chart.no.results"/></div>
        </c:otherwise>
    </c:choose>
</div>