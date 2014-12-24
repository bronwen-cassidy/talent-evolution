<%@ page import="com.zynap.talentstudio.common.AccessType" %>
<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>

<zynap:infobox title="${msg}">
    <zynap:form name="changeAccess" method="post">
        <input type="hidden" name="changeAccess" value="yes"/>
        <input type="hidden" name="command.access" value="yes"/>
    </zynap:form>

    <zynap:form name="reports" method="post" encType="multipart/form-data">
        <table class="infotable" id="reportform" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata">
                        <input type="text" maxlength="240" class="input_text" name="<c:out value="${status.expression}"/>"
                               value="<c:out value="${status.value}" escapeXml="true"/>"/>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.description">
                        <textarea id="desc_id_" name="<c:out value="${status.expression}"/>" rows="4"><c:out value="${status.value}"/></textArea>
                        <%@include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.access">
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${command.scopeEditable}">
                                <select name="<c:out value="${status.expression}"/>" onchange="javascript:document.forms.reports.submit();">
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
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>

            <c:if test="${not command.metricReport}">
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
            </c:if>

                <%-- Include form to publish report to arenas --%>
            <%@ include file="common/publishform.jsp" %>

            <%@ include file="reportcoreformcommon.jsp" %>
            <c:if test="${command.metricReport}">
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
            </c:if>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>"
                           onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" id="target1" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
