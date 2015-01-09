<%@ page import="IPopulationEngine"%>
<%@ include file="../../includes/include.jsp" %>

<zynap:infobox title="${title}">
    <zynap:form method="post" name="metric" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="metric.label"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.label">
                    <td class="infodata">
                        <input type="text" maxlength="240" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="analysis.scope"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.access">
                    <td class="infodata">
                    <c:choose>
                      <c:when test="${command.scopeChangeable}">
                        <%@include file="../report_scope_select.jsp" %>
                      </c:when>
                     <c:otherwise>
                        <fmt:message key="scope.Public"/>
                        <input type="hidden" name="<c:out value="${status.expression}"/>" value="<%=AccessType.PUBLIC_ACCESS%>" />
                     </c:otherwise>
                </c:choose>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="type"/>&nbsp;:&nbsp;*</td>
                <spring:bind path="command.type">
                    <td class="infodata">
                        <select name="<c:out value="${status.expression}"/>" onchange="submitFormToTarget('metric', '_target1', '1', 'pgTarget');">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <option value="<%= IPopulationEngine.P_POS_TYPE_ %>" <c:if test="${status.value == 'P'}">selected</c:if>><fmt:message key="P"/></option>
                            <option value="<%= IPopulationEngine.P_SUB_TYPE_ %>" <c:if test="${status.value == 'S'}">selected</c:if>><fmt:message key="S"/></option>
                        </select>
                        <%@include file="../../includes/error_message.jsp" %>
                    </td>
                </spring:bind>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="scalar.operator"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <spring:bind path="command.operator">
                        <select name="<c:out value="${status.expression}"/>" onchange="submitFormToTarget('metric', '_target1', '1', 'pgTarget');">
                            <option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>
                            <option value="<%= IPopulationEngine.AVG %>" <c:if test="${status.value == 'avg'}">selected</c:if>><fmt:message key="scalar.operator.avg"/></option>
                            <option value="<%= IPopulationEngine.SUM %>" <c:if test="${status.value == 'sum'}">selected</c:if>><fmt:message key="scalar.operator.sum"/></option>
                            <option value="<%= IPopulationEngine.COUNT %>" <c:if test="${status.value == 'count'}">selected</c:if>><fmt:message key="scalar.operator.count"/></option>
                        </select>
                        <%@include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
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
                <c:choose>
                    <c:when test="${command.countOperator}">
                        <td colspan="2">
                            <table class="infotable" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td class="infolabel"><fmt:message key="metric.attribute"/></td>
                                    <td class="infolabel"><fmt:message key="analysis.population.criteria.operator"/></td>
                                    <td class="infolabel"><fmt:message key="analysis.population.criteria.value"/></td>
                                </tr>
                                <tr>
                                    <td class="infodata" width="40%">
                                        <spring:bind path="command.attribute">
                                            <c:set var="btnAction">javascript:showCountMetricCriteriaTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'criteriaCountTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>')</c:set>
                                            <%@include file="metricpickersnippet.jsp"%>
                                        </spring:bind>
                                    </td>
                                    <td class="infodata" width="20%">&nbsp;<c:out value="="/>&nbsp;</td>
                                    <td class="infodata" width="40%">
                                        <c:choose>
                                            <c:when test="${command.derivedAttribute}">
                                                <spring:bind path="command.value">
                                                    <input type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                                                    <%@include file="../../includes/error_message.jsp" %>
                                                </spring:bind>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="prefix" value="command" scope="request"/>
                                                <c:set var="criteria" scope="request" value="${command}"/>
                                                <%@ include file="../populations/editcriteriasnippet.jsp" %>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:when>
                    <c:otherwise>
                        <td class="infolabel"><fmt:message key="metric.attribute"/>&nbsp;:&nbsp;*</td>
                        <td class="infodata">
                            <spring:bind path="command.attribute">
                                <c:set var="btnAction">javascript:showMetricCriteriaTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'criteriaTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>')</c:set>
                                <%@include file="metricpickersnippet.jsp"%>
                            </spring:bind>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" id="finish" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                    <input type="hidden" id="pgTarget" name="" value=""/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>


<c:url value="/picker/metricpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/>
</c:url>
<c:url value="/picker/metriccountpicker.htm" var="pickerCountUrl">
    <c:param name="populationType" value="${command.type}"/>
</c:url>

<zynap:window elementId="criteriaTree" src="${pickerUrl}"/>
<zynap:window elementId="criteriaCountTree" src="${pickerCountUrl}"/>


