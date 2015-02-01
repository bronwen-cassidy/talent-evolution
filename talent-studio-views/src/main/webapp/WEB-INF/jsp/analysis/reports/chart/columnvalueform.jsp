<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <div class="infomessage"><fmt:message key="excludes.leave.blank"/></div>

    <zynap:form name="reports" method="post" encType="multipart/form-data">

    <%-- hidden field set by JavaScript that holds the target page variable for the wizard controller --%>
    <input id="pgTarget" type="hidden" name="" value="-1"/>
    <input id="backId" type="hidden" name="" value="-1"/>
    
    <table id="dtable1" class="infotable" cellspacing="0">
        
        <thead>
            <tr>
                <th><fmt:message key="column.label"/></th>
                <th><fmt:message key="column.value"/></th>
                <%--<th><fmt:message key="column.expected.value"/></th>--%>
                <th><fmt:message key="column.colour"/></th>
            </tr>
        </thead>
        <c:forEach var="column" items="${command.reportColumns}" varStatus="indexer">
            <tr>
                <td class="infodata">
                    <form:input path="command.reportColumns[${indexer.index}].label" autocomplete="true" cssClass="input_text" id="frmlbl${indexer.index}"/>
                    <div class="error"><form:errors path="command.reportColumns[${indexer.index}].label"/></div>
                </td>
                
                <td class="infodata">
                    <fmt:message key="chart.null.answer" var="noVal"/>
                    <form:select path="command.reportColumns[${indexer.index}].value" cssClass="input_select" onchange="copyText(this, 'frmlbl${indexer.index}');">
                        <form:option value="" label=""/>
                        <form:option value="_NULL_" label="${noVal}" cssClass="text_external"/>
                        <form:options items="${command.answers}"/>
                    </form:select>
                    <div class="error"><form:errors path="command.reportColumns[${indexer.index}].value"/></div>
                </td>                
                <td class="infodata">
                    <div>
                        <spring:bind path="command.reportColumns[${indexer.index}].displayColour">
                            <c:set var="attrId"><zynap:id><c:out value="ab${indexer.index}"/></zynap:id></c:set>
                            <span style="white-space:nowrap"><input id="posAttr<c:out value="${indexer.index}"/>" style="background-color:<c:out value="${column.displayColour}"/>;" value="" name="<c:out value="${status.expression}"/>" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'posAttr<c:out value="${indexer.index}"/>', '<c:out value="${attrId}"/>');" type="button"/></span>
                            <input id="<c:out value="${attrId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${column.displayColour}"/>"/>
                        </spring:bind>
                    </div>
                </td>
            </tr>
        </c:forEach>
        
        <spring:bind path="command">
            <%@include file="../../../includes/error_messages.jsp" %>
        </spring:bind>

        <tr>
            <td class="infobutton" colspan="<c:out value="${ncol}"/>">
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

<zynap:window elementId="colourPicker">
    <c:import url="/statics/colourPicker.html"/>
</zynap:window>