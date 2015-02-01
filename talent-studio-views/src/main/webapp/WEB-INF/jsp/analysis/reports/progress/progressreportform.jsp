<%@ include file="../../../includes/include.jsp" %>


<fmt:message key="progress.report.wizard.1" var="msg"/>

<zynap:infobox title="${msg}">
    <zynap:form name="assigncolumns" method="post" encType="multipart/form-data">
        <%-- hidden field to set the target pages --%>
        <input type="hidden" id="tgtId" name="" value=""/>
        <input type="hidden" id="delIndx" name="selectedColumnIndex" value="-1"/>
        <input type="hidden" id="delIndx2" name="selectedWfIndex" value="-1"/>
        <spring:bind path="command">
            <%@include file="../../../includes/error_messages.jsp" %>
        </spring:bind>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.definition"/>:&nbsp;*&nbsp;</td>
                <td class="infodata"><c:out value="${command.questionnaireDefinition.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/>:&nbsp;*&nbsp;</td>
                <td class="infodata">
                    <form:input path="command.label"/>
                    <form:errors path="command.label" cssClass="error"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.description"/></td>
                <td class="infodata">
                    <form:input path="command.description"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="display.last.item"/></td>
                <td class="infodata">
                    <form:checkbox path="command.lastLineItem"/>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.population"/>:&nbsp;*&nbsp;</td>
                <td class="infodata">
                    <fmt:message key="please.select" var="nb"/>
                    <form:select path="command.populationId">
                        <form:option value="" label="${nb}"/>
                        <form:options items="${populations}" itemValue="id" itemLabel="label"/>
                    </form:select>
                    <form:errors path="command.populationId" cssClass="error"/>
                </td>
            </tr>
        </table>

        <table class="infotable" cellspacing="0">
            <%-- headers for the columns --%>
            <thead>
                <tr>
                    <th class="small"><fmt:message key="sort.order"/></th>
                    <th><fmt:message key="report.attribute"/>&nbsp;*</th>
                    <th><fmt:message key="column.label"/>&nbsp;*</th>
                    <th>&nbsp;</th>
                </tr>
            </thead>
            <%-- todo the column for the workflow --%>
            <tr>
                <td class="infodata small">&nbsp;</td>
                <td class="infodata"><fmt:message key="workflow.form.column"/></td>
                <td class="infodata">
                    <form:input path="command.workflowColumn.label" cssClass="input_text"/>
                    <form:errors path="command.workflowColumn.label" cssClass="error"/>
                </td>
            </tr>
            <c:forEach var="col" items="${command.columns}" varStatus="colIndex">
            <tr>
                <td class="infodata small">
                    <form:input path="command.columns[${colIndex.index}].position" maxlength="5"/>
                    <form:errors path="command.columns[${colIndex.index}].position" cssClass="error"/>
                </td>
                <td class="infodata">
                    <spring:bind path="command.columns[${colIndex.index}].attributeName">

                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${col.label != null && col.label != ''}">
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
                        <%@include file="../../../includes/error_messages.jsp" %>

                    </spring:bind>
                </td>
                <td class="infodata">
                    <form:input path="command.columns[${colIndex.index}].label" id="colLbl${colIndex.index}" cssClass="input_text"/>
                    <form:errors path="command.columns[${colIndex.index}].label" cssClass="error"/>                    
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
        </table>

        <table class="infotable" cellspacing="0">
            <thead>
                <tr>
                    <th class="small"><fmt:message key="sort.order"/></th>
                    <th><fmt:message key="report.workflow"/>&nbsp;*</th>
                    <th><fmt:message key="column.label"/>&nbsp;*</th>
                    <th>&nbsp;</th>
                </tr>
            </thead>
            <c:forEach var="wf" items="${command.workflows}" varStatus="wfIndex">
            <tr>
                <td class="infodata small">
                    <form:input path="command.workflows[${wfIndex.index}].position" maxlength="5"/>
                    <form:errors path="command.workflows[${wfIndex.index}].position" cssClass="error"/>
                </td>
                <td class="infodata">
                    <fmt:message key="please.select" var="ps"/>

                    <form:select path="command.workflows[${wfIndex.index}].questionnaireWorkflowId" cssClass="input_select" onchange="copyText(this, 'wfLbl${wfIndex.index}');">
                        <form:option value="" label="${ps}"/>
                        <form:options items="${command.questionnaireWorkflows}" itemLabel="label" itemValue="id"/>
                    </form:select>
                    <form:errors path="command.workflows[${wfIndex.index}].questionnaireWorkflowId" cssClass="error"/>
                </td>
                <td class="infodata">
                    <form:input path="command.workflows[${wfIndex.index}].label" id="wfLbl${wfIndex.index}" cssClass="input_text"/>
                    <form:errors path="command.workflows[${wfIndex.index}].label" cssClass="error"/>
                </td>
                <td class="infodata">
                    <c:if test="${wfIndex.index > 0}"><span><input type="button" value="<fmt:message key="delete"/>" onclick="setValue('delIndx2', '<c:out value="${wfIndex.index}"/>'); setNameValueAndSubmit('assigncolumns', 'tgtId', '_target5', '5')"/></span></c:if>
                </td>
            </tr>
            </c:forEach>
            <tr>
                <td class="infobutton" colspan="3">
                    <input type="button" name="addWfBtn" value="<fmt:message key="add"/>" onclick="setNameValueAndSubmit('assigncolumns', 'tgtId', '_target4', '4')"/>&nbsp;
                </td>
            </tr>
        </table>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infobutton" colspan="3">
                    <c:if test="${add}">
                        <input class="inlinebutton" type="button" name="_target0" value="<fmt:message key="wizard.back"/>" onclick="setNameValueAndSubmit('assigncolumns', 'tgtId', '_target0', '0')"/>
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

<zynap:window elementId="questionTree">
    <zynap:serverTree trees="${command.tree}" branchIcon="ClosedFolder.gif" emptyBranchIcon="item.gif" leafIcon="item.gif" branchSelectable="false"/>
</zynap:window>
