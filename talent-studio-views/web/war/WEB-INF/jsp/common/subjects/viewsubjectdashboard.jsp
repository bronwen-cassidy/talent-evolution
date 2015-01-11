<%@ include file="../../includes/include.jsp" %>
<%@ page import="com.zynap.talentstudio.analysis.reports.ReportConstants" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.BarChartProducer" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.cewolf.producers.PieChartProducer" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>

<c:choose>
    <c:when test="${dashboards == null}">
        <zynap:infobox title="${dashboardHeading}" id="nodshbrd">
            <div class="noinfo">
                <fmt:message key="no.dashboards"/>
            </div>
        </zynap:infobox>
    </c:when>
    <c:otherwise>

        <zynap:artefactLink var="viewSubjectUrl" url="${subjectUrl}" tabName="activeTab" activeTab="${tabContent.key}"/>
        <zynap:artefactLink var="viewPositionUrl" url="${positionUrl}" tabName="activeTab" activeTab="${tabContent.key}"/>
        <zynap:artefactLink var="viewQuestionnaireUrl" url="${questionnaireUrl}" tabName="activeTab" activeTab="${tabContent.key}"/>

        <c:forEach var="dashboard" items="${dashboards}" varStatus="dIndexer">

            <c:set var="dashboardItem" value="${dashboard.dashboardItem}"/>
            <c:set var="filledReport" value="${dashboard.filledReport}"/>
            <c:set var="report" value="${dashboardItem.report}"/>
            <c:set var="chartIdNum" value="${dIndexer.index}" scope="page"/>

            <c:choose>
                <c:when test="${filledReport.spiderChart}">
                    <%@include file="viewspiderchartsnippet.jsp" %>        
                </c:when>
                <c:when test="${dashboardItem.report.chartReport}">
                    <fieldset>
                        <legend><c:out value="${dashboardItem.label}"/></legend>
                        <c:if test="${dashboardItem.description != null && dashboardItem != ''}">
                            <div class="infomessage"><c:out value="${dashboardItem.description}"/></div>
                        </c:if>
                    <span class="left">
                        <div class="infomessage"><fmt:message key="actual"/></div>
                        <c:set var="filledReportProducer" value="${filledReport.producer}"/>
                        <c:set var="chartView" value="${filledReport.producer}"/>
                        <c:set var="colourProcessor" value="${filledReport.producer}"/>

                        <c:set var="baseUrl" value="runtabularreport.htm"/>
                        <zynap:artefactLink var="ddUrl" url="${baseUrl}" tabName="activeTab" activeTab="${tabContent.key}">
                            <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${report.drillDownReport.id}"/>
                            <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${report.defaultPopulation.id}"/>
                            <zynap:param name="<%= ParameterConstants.POPULATION_PERSON_ID %>" value="${artefact.id}"/>
                        </zynap:artefactLink>

                        <c:if test="${viewType == 'personnal'}">
                            <zynap:artefactLink var="ddUrl" url="${baseUrl}" tabName="activeTab" activeTab="${tabContent.key}">
                                <zynap:param name="<%= ParameterConstants.REPORT_ID %>" value="${report.drillDownReport.id}"/>
                                <zynap:param name="<%= ReportConstants.ORIGINAL_POPULATION_ID %>" value="${report.defaultPopulation.id}"/>
                                <zynap:param name="<%= ParameterConstants.POPULATION_PERSON_ID %>" value="${artefact.id}"/>
                                <zynap:param name="<%= ParameterConstants.MOCK_USER_ID_PARAM %>" value="0"/>
                            </zynap:artefactLink>
                        </c:if>
                        
                        <c:choose>
                            <c:when test="${filledReport.pieChart}">
                                <%@include file="viewpiechartsnippet.jsp" %>
                            </c:when>
                            <c:otherwise>
                                <%@include file="viewbarchartsnippet.jsp" %>
                            </c:otherwise>
                        </c:choose>

                    </span>
                    <c:if test="${dashboard.expectedProducer != null}">
                        <span class="left">
                            <div class="infomessage"><fmt:message key="expected"/></div>
                            <c:set var="echartView" value="${dashboard.expectedProducer}"/>
                            <c:set var="ecolourProcessor" value="${dashboard.expectedProducer}"/>

                            <%
                                Object num2 = pageContext.getAttribute("chartIdNum");
                                int idNum2 = Integer.parseInt(String.valueOf(num2));
                                String expectedChartId = "epieChart" + idNum2;
                            %>
                            <c:choose>
                                <c:when test="${filledReport.pieChart}">
                                    <cewolf:chart id="<%=expectedChartId%>" type="pie3D" showlegend="false">                                        
                                        <cewolf:data>
                                            <cewolf:producer id="echartView"/>
                                        </cewolf:data>
                                        <cewolf:colorpaint color="#fafafa"/>
                                        <cewolf:chartpostprocessor id="ecolourProcessor"/>
                                    </cewolf:chart>
                                    <cewolf:img chartid="<%=expectedChartId%>" renderer="/cewolf" width="400" height="250" alt="Pie chart">
                                        <cewolf:map linkgeneratorid="echartView" tooltipgeneratorid="echartView"/>
                                    </cewolf:img>
                                </c:when>
                                <c:otherwise>
                                    <cewolf:chart id="<%=expectedChartId%>" type="verticalBar3D" showlegend="false" xaxislabel="" yaxislabel="" antialias="true">
                                        <cewolf:data>
                                            <cewolf:producer id="echartView"/>
                                        </cewolf:data>
                                        <cewolf:colorpaint color="#fafafa"/>
                                        <cewolf:chartpostprocessor id="ecolourProcessor"/>
                                    </cewolf:chart>
                                    <cewolf:img chartid="<%=expectedChartId%>" renderer="/cewolf" width="400" height="250" alt="Pie chart">
                                        <cewolf:map linkgeneratorid="echartView" tooltipgeneratorid="echartView"/>
                                    </cewolf:img>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </c:if>
                    </fieldset>
                </c:when>
                <c:otherwise>
                    <fieldset>
                        <legend><c:out value="${dashboardItem.label}"/></legend>
                        <c:if test="${dashboardItem.description != null && dashboardItem != ''}">
                            <div class="infomessage"><c:out value="${dashboardItem.description}"/></div>
                        </c:if>
                        <zynap:jasperTabularReportTag jasperPrint="${filledReport.jasperPrint}" viewSubjectUrl="${viewSubjectUrl}"
                                                      viewPositionUrl="${viewPositionUrl}" viewQuestionnaireUrl="${viewQuestionnaireUrl}"
                                                      report="${dashboardItem.report}"/>
                    </fieldset>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:otherwise>
</c:choose>