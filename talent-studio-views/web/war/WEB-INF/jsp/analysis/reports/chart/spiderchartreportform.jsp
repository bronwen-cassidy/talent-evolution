<%@ page import="IPopulationEngine" %>
<%@ page import="AccessType" %>
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
                <td class="infodata">
                    <form:input path="command.label" cssClass="input_text"/>
                    <form:errors path="command.label" cssClass="error"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.description">
                        <form:textarea path="command.description" cols="8" rows="4"/>
                        <form:errors path="command.description" cssClass="error"/>
                    </spring:bind>
                </td>
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
                <td class="infolabel"><fmt:message key="supports.personal"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <form:checkbox path="command.personal" cssClass="input_checkbox"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="scalar.operator"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.operator">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <option value="<%= IPopulationEngine.AVG %>" <c:if test="${status.value == 'avg'}">selected</c:if>><fmt:message key="scalar.operator.avg"/></option>
                            <option value="<%= IPopulationEngine.SUM %>" <c:if test="${status.value == 'sum'}">selected</c:if>><fmt:message key="scalar.operator.sum"/></option>
                        </select>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <%-- Include form to publish report to arenas --%>
            <%@ include file="../common/publishform.jsp" %>
            <%@ include file="../reportcoreformcommon.jsp" %>
            
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
