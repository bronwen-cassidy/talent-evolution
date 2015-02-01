<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController" %>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
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
                    <th><fmt:message key="group"/></th>
                    <th><fmt:message key="column.colour"/></th>
                </tr>
            </thead>
            <c:forEach var="col" items="${command.columns}" varStatus="indexer">
                <tr>
                    <td class="infodata">
                        <form:input path="command.columns[${indexer.index}].label"/>
                        <form:errors path="command.columns[${indexer.index}].label" cssClass="error"/>
                    </td>
                    <%-- colours --%>
                    <td class="infodata">
                        <div>
                            <spring:bind path="command.columns[${indexer.index}].displayColour">
                                <c:set var="attrId"><zynap:id><c:out value="ab${indexer.index}"/></zynap:id></c:set>
                                <span style="white-space:nowrap"><input id="posAttr<c:out value="${indexer.index}"/>" style="background-color:<c:out value="${col.displayColour}"/>;" value="" name="<c:out value="${status.expression}"/>" readonly="true" type="text" class="input_text" /><input class="partnerbutton" value="..." onclick="popupShow('<zynap:message code="select.colour" javaScriptEscape="true"/>', this, 'colourPicker', 'posAttr<c:out value="${indexer.index}"/>', '<c:out value="${attrId}"/>');" type="button"/></span>
                                <input id="<c:out value="${attrId}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${col.displayColour}"/>"/>
                            </spring:bind>
                        </div>
                    </td>
                    <td class="infodata" width="100%" colspan="2">
                        <input type="button" id="delColInd<c:out value="${indexer.index}"/>" name="_target1" class="inlinebutton"
                               value="<fmt:message key="delete"/>" onclick="deleteChartReportColumn('<c:out value="${indexer.index}"/>');"/>
                    </td>
                </tr>
            </c:forEach>            
            <tr>
                <td colspan="2" class="infodata">
                    <input class="inlinebutton" type="button" name="_target1" value="<fmt:message key="report.add.column"/>" onclick="addTabularReportColumn();"/>
                    <input id="selectedColumnIndex" type="hidden" name="selectedColumnIndex" value="-1"/>
                    <input id="deletedColumnIndex" type="hidden" name="deletedColumnIndex" value="-1"/>
                </td>
            </tr>
            <spring:bind path="command">
                <%@include file="../../../includes/error_messages.jsp" %>
            </spring:bind>
            <tr>
                <td class="infobutton" colspan="2">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('reports', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target2" value="<fmt:message key="wizard.next"/>"/>
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