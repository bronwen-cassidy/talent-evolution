<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../../../includes/include.jsp" %>

<script type="text/javascript">
    function deleteColumnAttributeRow(index, targetNum) {
        var indexField = getElemById('deletedColumnIndex');
        indexField.value= index;
        submitFormToTarget('reports', '_target' + targetNum, targetNum, 'pgTarget');
    }
</script>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <div class="infomessage"><fmt:message key="excludes.leave.blank"/></div>

    <zynap:form name="reports" method="post" encType="multipart/form-data">

    <%-- hidden field set by JavaScript that holds the target page variable for the wizard controller --%>
    <input id="pgTarget" type="hidden" name="" value="-1"/>
    <input id="backId" type="hidden" name="" value="-1"/>
    <spring:bind path="command">
        <%@include file="../../../includes/error_messages.jsp" %>
    </spring:bind>
    <table id="dtable1" class="infotable" cellspacing="0">
        
        <thead>
            <tr>
                <th><fmt:message key="attribute"/></th>
                <th><fmt:message key="category"/></th>
                <th><fmt:message key="group"/></th>
                <th><fmt:message key="column.expected.value"/></th>
                <th>&nbsp;</th>
            </tr>
        </thead>
        <c:forEach var="column" items="${command.columnAttributes}" varStatus="indexer">
            <tr>
                <td class="infodata">
                    <spring:bind path="command.columnAttributes[${indexer.index}].attribute">

                        <c:set var="btnAction">
                            javascript:showChartColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${status.expression}_label"/>',
                            '<c:out value="${status.expression}"/>', '<c:out value="columnAttributes[${indexer.index}].label"/>')
                        </c:set>

                        <%-- determine the correct label --%>
                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${column.attributeSet}">
                            <c:set var="label" value="${column.attributeLabel}"/>
                        </c:if>

                        <span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                                                              value="<c:out value="${label}"/>"
                                                              name="<c:out value="${status.expression}_label"/>"
                                                              readonly="true"
                            /><input type="button"
                                     class="partnerbutton"
                                     value="..." id="navOUPopup"
                                     onclick="<c:out value="${btnAction}"/>"/></span>
                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>"
                               value="<c:out value="${status.value}"/>"/>

                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <form:input path="command.columnAttributes[${indexer.index}].label" id="columnAttributes[${indexer.index}].label" cssClass="input_text"/>
                    <form:errors path="command.columnAttributes[${indexer.index}].label" cssClass="error"/>
                </td>
                <td class="infodata">
                    <fmt:message key="please.select" var="pleaseSelect"/>
                    <form:select path="command.columnAttributes[${indexer.index}].columnLabel" cssClass="input_select">
                        <form:option value="" label="${pleaseSelect}"/>
                        <form:options items="${command.chartGroups}"/>                        
                    </form:select>
                    <form:errors path="command.columnAttributes[${indexer.index}].columnLabel" cssClass="error"/>
                </td>
                <td class="infodata">
                    <form:input path="command.columnAttributes[${indexer.index}].expectedValue" id="columnAttributes[${indexer.index}].label" cssClass="input_text"/>
                    <form:errors path="command.columnAttributes[${indexer.index}].expectedValue" cssClass="error"/>
                </td>
                <td class="infodata">
                    <input type="button" id="delColInd<c:out value="${indexer.index}"/>" name="_target2" class="inlinebutton"
                           value="<fmt:message key="delete"/>" onclick="deleteColumnAttributeRow('<c:out value="${indexer.index}"/>', '2');"/>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td colspan="5" class="infodata">
                <input class="inlinebutton" type="button" name="_target2" value="<fmt:message key="report.add.column"/>" onclick="submitTabularReportForm('2', '2');"/>
                <input id="selectedColumnIndex" type="hidden" name="selectedColumnIndex" value="-1"/>
                <input id="deletedColumnIndex" type="hidden" name="deletedColumnIndex" value="-1"/>
            </td>
        </tr>
        <tr>
            <td class="infobutton" colspan="4">
                <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('reports', 'pgTarget', '1', 'backId');"/>
                <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
            </td>
        </tr>

    </table>

    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<c:url value="/picker/metricpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="S"/>
</c:url>

<zynap:window elementId="columnTree" src="${pickerUrl}"/>