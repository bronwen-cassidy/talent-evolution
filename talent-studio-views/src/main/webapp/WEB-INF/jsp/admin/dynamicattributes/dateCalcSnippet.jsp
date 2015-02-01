<%@ page import="com.zynap.talentstudio.calculations.DateCalculation" %>
<tr>
    <td class="infoarea" colspan="2"><span id="calcMessageId">&nbsp;</span></td>
</tr>
<%-- table here where one can select an attribute (of type date or function) or enter a number --%>
<tr>
    <td class="infobox" colspan="2">
        <input id="expCountId" type="hidden" name="numExpressions" value="<c:out value="${command.numExpressions}"/>"/>
        <table id="calTableId" class="infotable" cellpadding="0" cellspacing="0">
            <%-- todo add x button to delete a row --%>
            <c:forEach var="expression" items="${command.expressions}" varStatus="indexer">
                <%-- contains 2 radio buttons a select field a text area and format value as --%>
                <tr>
                    <%-- contains the radio and the select box --%>
                    <td class="infodata">
                        <input id="selChoice<c:out value="${indexer.index}"/>" type="checkbox" class="input_radio" name="attrOption<c:out value="${indexer.index}"/>"
                            <c:if test="${expression.value == null}">checked="true"</c:if>
                            onclick="toggleEnabled('attrSelId', 'valSelId', 'formatSelId', 'txtChoice');"
                        />&nbsp;&nbsp;
                        <spring:bind path="command.expressions[${indexer.index}].attribute">
                            <select id="attrSelId<c:out value="${indexer.index}"/>" name="<c:out value="${status.expression}"/>" <c:if test="${expression.value != null}">disabled="yes"</c:if> onchange="updateCalculation('calcMessageId', 'calTableId');">
                                <option value="" <c:if test="${status.value == null}">selected="yes"</c:if>>&nbsp;</option>
                                <c:forEach var="attr" items="${command.attributes}">
                                    <option value="<c:out value="${attr.id}"/>" <c:if test="${expression.attribute == attr.id}">selected="true"</c:if>><c:out value="${attr.label}"/></option>
                                </c:forEach>
                            </select>
                            <%@include file="../../includes/error_message.jsp" %>
                        </spring:bind>

                        </br>

                        <input id="txtChoice<c:out value="${indexer.index}"/>" type="checkbox" class="input_radio" name="selOption<c:out value="${indexer.index}"/>" <c:if test="${expression.value != null}">checked="true"</c:if>
                            onclick="toggleEnabled('attrSelId', 'valSelId', 'formatSelId', 'selChoice');"
                        />&nbsp;&nbsp;
                        <spring:bind path="command.expressions[${indexer.index}].value">
                            <input id="valSelId<c:out value="${indexer.index}"/>" type="text" class="input_text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${expression.value == null}">disabled="yes"</c:if> onblur="updateCalculation('calcMessageId', 'calTableId');"/>
                            <%@include file="../../includes/error_message.jsp" %>
                        </spring:bind>
                        &nbsp;
                        <spring:bind path="command.expressions[${indexer.index}].format">
                            <select id="formatSelId<c:out value="${indexer.index}"/>"  name="<c:out value="${status.expression}"/>" <c:if test="${expression.value == null}">disabled="yes"</c:if> onchange="updateCalculation('calcMessageId', 'calTableId');">
                                <option value="0" <c:if test="${expression.format == '0'}">selected="true"</c:if>><fmt:message key="please.select"/></option>
                                <option value="<%= DateCalculation.YEARS %>" <c:if test="${expression.format == '1'}">selected="true"</c:if>><fmt:message key="format.years"/></option>
                                <option value="<%= DateCalculation.MONTHS %>" <c:if test="${expression.format == '2'}">selected="true"</c:if>><fmt:message key="format.months"/></option>
                            </select>
                            <%@include file="../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                    <td class="infodata">
                        <spring:bind path="command.expressions[${indexer.index}].operator">
                            <select id="operatorSelId<c:out value="${indexer.index}"/>" name="<c:out value="${status.expression}"/>" onchange="updateCalculation('calcMessageId', 'calTableId');">
                                <option value="" <c:if test="${expression.operator == null}">selected="true"</c:if>><fmt:message key="please.select"/></option>
                                <option value="+" <c:if test="${expression.operator == '+'}">selected="true"</c:if>>+</option>
                                <option value="-" <c:if test="${expression.operator == '-'}">selected="true"</c:if>>-</option>
                            </select>
                            <%@include file="../../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </td>
</tr>
<tr>
    <td class="infobutton" colspan="2">
        <input type="button" name="addExpBtn" value="<fmt:message key="add"/>" onclick="addRow('calTableId', 'expCountId')"/>&nbsp;
        <input type="button" name="rmExpBtn" value="<fmt:message key="remove.last.row"/>" onclick="removeRow('calTableId', 'expCountId');"/>
    </td>
</tr>