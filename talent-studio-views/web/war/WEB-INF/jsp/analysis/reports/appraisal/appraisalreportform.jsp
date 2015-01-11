<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine" %>
<%@ include file="../../../includes/include.jsp" %>

<zynap:window elementId="questionTree">
    <zynap:serverTree trees="${command.tree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="item.gif" leafIcon="item.gif" branchSelectable="false"/>
</zynap:window>

<fmt:message key="app.report.wizard.1" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="assigncolumns" method="post" encType="multipart/form-data">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/>:&nbsp;*&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.label">
                        <input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.description"/></td>
                <td class="infodata">
                    <spring:bind path="command.description">
                        <textarea name="<c:out value="${status.expression}"/>" rows="5" cols="10"><c:out value="${status.value}"/></textarea>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
            </tr>
            <%--<tr>--%>
                <%--<td class="infolabel"><fmt:message key="scalar.operator"/>&nbsp;:&nbsp;*</td>--%>
                <%--<td class="infodata">--%>
                    <%--<spring:bind path="command.operator">--%>
                        <%--<select name="<c:out value="${status.expression}"/>">--%>
                            <%--<option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>--%>
                            <%--<option value="<%= IPopulationEngine.AVG %>" <c:if test="${status.value == 'avg'}">selected</c:if>><fmt:message key="scalar.operator.avg"/></option>--%>
                            <%--<option value="<%= IPopulationEngine.SUM %>" <c:if test="${status.value == 'sum'}">selected</c:if>><fmt:message key="scalar.operator.sum"/></option>--%>
                        <%--</select>--%>
                        <%--<%@include file="../../../includes/error_messages.jsp" %>--%>
                    <%--</spring:bind>--%>
                <%--</td>--%>
            <%--</tr>--%>
        </table>

        <table class="infotable" cellspacing="0">                               
            <%-- hidden field to set the target pages --%>
            <input type="hidden" id="tgtId" name="" value=""/>
            <input type="hidden" id="delIndx" name="selectedColumnIndex" value="-1"/>

            <spring:bind path="command">
                <%@include file="../../../includes/error_messages.jsp" %>    
            </spring:bind>
                
            <%-- headers for the columns --%>
            <thead>
                <tr>
                    <th><fmt:message key="report.weighting"/></th>
                    <th><fmt:message key="report.attribute"/>&nbsp;*</th>
                    <th><fmt:message key="column.label"/>&nbsp;*</th>
                    <th>&nbsp;</th>
                </tr>
            </thead>

            <c:forEach var="col" items="${command.columns}" varStatus="colIndex">
            <tr>
                <td class="infodata">
                    <spring:bind path="command.columns[${colIndex.index}].weighting">
                        <input type="text" maxlength="4" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <spring:bind path="command.columns[${colIndex.index}].attributeName">

                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${col.label != null}">
                            <c:set var="label" value="${col.label}"/>
                        </c:if>

                        <span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                            value="<c:out value="${label}"/>"
                                name="<c:out value="${status.expression}_label"/>"
                                readonly="true"
                        /><input type="button"
                                class="partnerbutton"
                                value="..." id="navOUPopup"
                                onclick="popupShowTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'questionTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>', 'appraisalReportColumnSelected(\'<c:out value="${status.expression}"/>_label\', \'colLbl<c:out value="${colIndex.index}"/>\')', false);"/></span>
                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                        <%--<select name="<c:out value="${status.expression}"/>" onchange="clearAndSetField('colLbl<c:out value="${colIndex.index}"/>', this);">--%>
                            <%--<option value="" <c:if test="${status.value == null}">selected</c:if>><fmt:message key="please.select"/></option>--%>
                            <%--<c:forEach var="attribute" items="${command.attributes}">--%>
                                <%--<option value="<c:out value="${attribute.id}"/>" <c:if test="${attribute.id == col.attributeName}">selected</c:if>><c:out value="${attribute.label}"/></option>--%>
                            <%--</c:forEach>--%>
                        <%--</select>--%>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <spring:bind path="command.columns[${colIndex.index}].label">
                        <input id="colLbl<c:out value="${colIndex.index}"/>" type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <c:if test="${colIndex.index > 0}"><span><input type="button" value="<fmt:message key="delete"/>" onclick="setValue('delIndx', '<c:out value="${colIndex.index}"/>'); setNameValueAndSubmit('assigncolumns', 'tgtId', '_target3', '3')"/></span></c:if>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td class="infobutton" colspan="3">
                    <input type="button" name="addExpBtn" value="<fmt:message key="add"/>" onclick="setNameValueAndSubmit('assigncolumns', 'tgtId', '_target2', '2')"/>&nbsp;
                </td>
            </tr>
            <tr>
                <td class="infobutton" colspan="3">
                    <c:if test="${!edit}">
                        <input class="inlinebutton" type="button" name="_target1" value="<fmt:message key="wizard.back"/>" onclick="setNameValueAndSubmit('assigncolumns', 'tgtId', '_target0', '0')"/>
                    </c:if>
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms.cancelled.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="cancelled">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>