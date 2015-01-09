<%@ page import="AccessType" %>
<%@ page import="com.zynap.talentstudio.web.analysis.reports.views.RunCrossTabReportWrapper" %>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
<zynap:form name="reports" method="post" encType="multipart/form-data">
<input id="pgTarget" type="hidden" name="" value="-1"/>
<input id="backId" type="hidden" name="" value="-1"/>
<table class="infotable" id="reportform" cellspacing="0">
<tr>
    <td class="infolabel"><fmt:message key="report.label"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.label">
        <td class="infodata">
            <input type="text" maxlength="240" class="input_text" id="reportLabel" name="<c:out value="${status.expression}"/>"
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
    <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.access">
        <td class="infodata">
            <c:choose>
                <c:when test="${command.scopeEditable}">
                    <select name="<c:out value="${status.expression}"/>"
                            onchange="javascript:submitFormToTarget('reports','_target0','0', 'pgTarget');">
                        <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                        <option value="<%=AccessType.PUBLIC_ACCESS%>" <c:if test="${status.value == 'Public'}"> selected</c:if>><fmt:message
                                key="scope.Public"/></option>
                        <option value="<%=AccessType.PRIVATE_ACCESS%>" <c:if test="${status.value == 'Private'}"> selected</c:if>><fmt:message
                                key="scope.Private"/></option>
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
    <td class="infolabel"><fmt:message key="number.decimal.places"/>&nbsp;:&nbsp;</td>
    <td class="infodata">
        <spring:bind path="command.decimalPlaces">
            <select name="<c:out value="${status.expression}"/>">
                <c:forEach var="index" begin="0" end="10">
                    <option value="<c:out value="${index}"/>" <c:if test="${status.value == index}">selected</c:if>><c:out
                            value="${index}"/></option>
                </c:forEach>
            </select>
        </spring:bind>
    </td>
</tr>

    <%-- Include form to publish report to arenas --%>
<%@ include file="../common/publishform.jsp" %>

<tr>
    <td class="infolabel"><fmt:message key="preferred.population"/>&nbsp;:&nbsp;*</td>
    <spring:bind path="command.populationId">
        <td class="infodata">
            <select name="<c:out value="${status.expression}"/>" onchange="javascript:submitFormToTarget('reports','_target1','1', 'pgTarget');">
                <option value="" <c:if test="${command.populationId == null}">selected</c:if>><fmt:message key="please.select"/></option>
                <c:forEach var="prefPop" items="${command.populations}">
                    <option value="<c:out value="${prefPop.id}"/>" <c:if test="${command.populationId == prefPop.id}">selected</c:if>><c:out
                            value="${prefPop.label}"/></option>
                </c:forEach>
            </select>
            <%@include file="../../../includes/error_message.jsp" %>
        </td>
    </spring:bind>
</tr>
<c:if test="${command.showPreferredMetric}">
    <tr>
        <td class="infolabel"><fmt:message key="report.display.as"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.resultFormat">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="<%= RunCrossTabReportWrapper.DISCREET_VALUE %>" <c:if test="${command.resultFormat == 0}">selected</c:if>><fmt:message
                            key="crosstab.format.discreet"/></option>
                    <option value="<%= RunCrossTabReportWrapper.PERCENTAGE_VALUE %>" <c:if test="${command.resultFormat == 1}">selected</c:if>>
                        <fmt:message
                                key="crosstab.format.percentage"/></option>

                    <option value="<%= RunCrossTabReportWrapper.ARTEFACTS_VALUE %>" <c:if test="${command.resultFormat == 2}">selected</c:if>><fmt:message
                            key="crosstab.format.artefacts"/></option>
                </select>
                <%@include file="../../../includes/error_message.jsp" %>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="preferred.metric"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.preferredMetric">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="-1" <c:if test="${command.preferredMetric == null}">selected</c:if>><fmt:message
                            key="scalar.operator.count"/></option>
                    <c:forEach var="metric" items="${command.metrics}">
                        <option value="<c:out value="${metric.id}"/>" <c:if test="${command.preferredMetric == metric.id}">selected</c:if>><c:out
                                value="${metric.label}"/></option>
                    </c:forEach>
                </select>
                <%@include file="../../../includes/error_message.jsp" %>
            </spring:bind>
        </td>
    </tr>
    <tr>
        <td class="infolabel"><fmt:message key="preferred.drilldown.report"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.drillDownReportId">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="" <c:if test="${command.drillDownReportId == null}">selected</c:if>></option>
                    <c:forEach var="report" items="${command.drillDownReports}">
                        <option value="<c:out value="${report.id}"/>" <c:if test="${command.drillDownReportId == report.id}">selected</c:if>><c:out
                                value="${report.label}"/></option>
                    </c:forEach>
                </select>
                <%@include file="../../../includes/error_message.jsp" %>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td class="infolabel"><fmt:message key="preferred.display.report"/>&nbsp;:&nbsp;</td>
        <td class="infodata">
            <spring:bind path="command.displayReportId">
                <select name="<c:out value="${status.expression}"/>">
                    <option value="" <c:if test="${command.displayReportId == null}">selected</c:if>></option>
                    <c:forEach var="displayReport" items="${command.displayReports}">
                        <option value="<c:out value="${displayReport.id}"/>"
                                <c:if test="${command.displayReportId == displayReport.id}">selected</c:if>><c:out
                                value="${displayReport.label}"/></option>
                    </c:forEach>
                </select>
                <%@include file="../../../includes/error_message.jsp" %>
            </spring:bind>
        </td>
    </tr>

    <tr>
        <td class="infolabel"><fmt:message key="preferred.display.limit"/>&nbsp;:&nbsp;</td>
        <spring:bind path="command.displayLimit">
            <td class="infodata">
                <input type="text" maxlength="5" class="input_text" id="displayLimit" name="<c:out value="${status.expression}"/>"
                       value="<c:out value="${status.value}"/>"/>
                <%@include file="../../../includes/error_message.jsp" %>
            </td>
        </spring:bind>
    </tr>

</c:if>

<tr>
    <td class="infobutton"/>
    <td class="infobutton">
        <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
               onclick="javascript:document.forms._cancel.submit();"/>
        <input class="inlinebutton" id="target2" type="submit" name="_target2" value="<fmt:message key="wizard.next"/>"/>
    </td>
</tr>
</table>
</zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
