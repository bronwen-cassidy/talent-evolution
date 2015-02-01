<%@ page import="com.zynap.talentstudio.analysis.reports.ChartReport" %>
<%@ page import="com.zynap.talentstudio.common.AccessType" %>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>

<zynap:infobox title="${msg}">

    <zynap:form name="changeAccess" method="post">
        <input type="hidden" name="changeAccess" value="yes"/>
        <input type="hidden" name="command.access" value="yes"/>
    </zynap:form>

    <zynap:form name="reports" method="post" encType="multipart/form-data">
        <table class="infotable" id="creportform" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata">
                        <input type="text" maxlength="240" class="input_text" name="<c:out value="${status.expression}"/>"
                               value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.description">
                        <textarea id="desc_id_" name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textArea>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
             <tr>
                <td class="infolabel"><fmt:message key="report.chart.type"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.chartType">
                    <td class="infodata">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <option value="<%=ChartReport.PIE_CHART%>" <c:if test="${status.value == 'PIE'}"> selected</c:if>><fmt:message key="chart.PIE"/></option>
                            <option value="<%=ChartReport.BAR_CHART%>" <c:if test="${status.value == 'BAR'}"> selected</c:if>><fmt:message key="chart.BAR"/></option>
                        </select>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.access">
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${command.scopeEditable}">
                                <select name="<c:out value="${status.expression}"/>" onchange="document.forms.reports.submit();">
                                    <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <option value="<%=AccessType.PUBLIC_ACCESS%>" <c:if test="${status.value == 'Public'}"> selected</c:if>>
                                        <fmt:message key="scope.Public"/></option>
                                    <option value="<%=AccessType.PRIVATE_ACCESS%>" <c:if test="${status.value == 'Private'}"> selected</c:if>>
                                        <fmt:message key="scope.Private"/></option>
                                </select>
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="scope.Public"/>
                                <input type="hidden" name="<c:out value="${status.expression}"/>" value="<%=AccessType.PUBLIC_ACCESS%>"/>
                            </c:otherwise>
                        </c:choose>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="display.last.item"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.lastLineItem">
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.lastLineItem}">checked="yes"</c:if>/>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="supports.personal"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.personal">
                        <input type="checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${command.personal}">checked="yes"</c:if>/>
                    </spring:bind>
                </td>
            </tr>

            <%-- Include form to publish report to arenas --%>
            <%@ include file="../common/publishform.jsp" %>
            <%@ include file="../reportcoreformcommon.jsp" %>

            <tr>
                <td class="infolabel"><fmt:message key="preferred.drilldown.report"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.drillDownReportId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${command.drillDownReportId == null}">selected</c:if>></option>
                            <c:forEach var="report" items="${command.drillDownReports}">
                                <option value="<c:out value="${report.id}"/>" <c:if test="${command.drillDownReportId == report.id}">selected</c:if>>
                                    <c:out value="${report.label}"/>
                                </option>
                            </c:forEach>
                        </select>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>

            <c:if test="${not empty command.reports}">
                <tr>
                    <td class="infolabel"><fmt:message key="base.report.on"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <spring:bind path="command.reportId">
                            <select name="<c:out value="${status.expression}"/>">
                                <option value="" <c:if test="${command.reportId == null}">selected</c:if>></option>
                                <c:forEach var="report" items="${command.reports}">
                                    <option value="<c:out value="${report.id}"/>" <c:if test="${command.reportId == report.id}">selected</c:if>>
                                        <c:out value="${report.label}"/>
                                    </option>
                                </c:forEach>
                            </select>
                            <%@include file="../../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                </tr>
            </c:if>
            
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" id="target1" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
