<%@ page import="com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="reports" method="post" encType="multipart/form-data">

    <%-- hidden field set by JavaScript that holds the target page variable for the wizard controller --%>
    <input id="pgTarget" type="hidden" name="" value="-1"/>
    <input id="backId" type="hidden" name="" value="-1"/>

    <table id="dtable1" class="infotable" cellspacing="0">
        <c:set var="ncol" value="6"/>
        <tr>
            <td class="infoheading" colspan="<c:out value="${ncol}"/>"><fmt:message key="report.columns"/></td>
        </tr>
        <tr>
            <td class="small infolabel"><fmt:message key="order"/>&nbsp;:&nbsp;*</td>
            <td class="infolabel"><fmt:message key="column"/>&nbsp;:&nbsp;*</td>
            <td class="infolabel"><fmt:message key="column.label"/>&nbsp;:&nbsp;*</td>
            <td class="small infolabel"><fmt:message key="column.group"/>&nbsp;:&nbsp;</td>
            <td class="infolabel" colspan="2">&nbsp;</td>
        </tr>
        <c:forEach var="col" items="${command.columns}" varStatus="indexer">
        <tr>
            <td class="small infodata">
                <spring:bind path="command.columns[${indexer.index}].columnPosition">
                    <select id="colPosIndex<c:out value="${indexer.index}"/>" name="<c:out value="${status.expression}"/>">
                        <c:forEach var="colPosx" begin="0" end="${command.numColumns}">
                            <option value="<c:out value="${colPosx}"/>" <c:if test="${status.value == colPosx}">selected</c:if>><c:out value="${colPosx}"/></option>
                        </c:forEach>
                    </select>
                    <%@include file="../../../includes/error_messages.jsp" %>
                </spring:bind>
            </td>
            <td class="infodata">
                <c:choose>
                 <c:when test="${col.formula}">
                    <label><c:out value="${col.functionWrapperBean.formulaDisplay}"/></label>
                 </c:when>
                 <c:otherwise>
                    <spring:bind path="command.columns[${indexer.index}].attribute">

                        <c:set var="btnAction">javascript:showTabularColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${status.expression}_label"/>', '<c:out value="${status.expression}"/>', '<c:out value="columns[${indexer.index}].label"/>')</c:set>

                        <%-- determine the correct label --%>
                        <fmt:message key="please.select" var="label"/>
                        <c:if test="${col.attributeSet}">
                            <c:set var="label" value="${col.attributeLabel}"/>
                        </c:if>

                        <span style="white-space: nowrap;"><input id="<c:out value="${status.expression}"/>_label" type="text" class="input_text"
                            value="<c:out value="${label}"/>"
                                name="<c:out value="${status.expression}_label"/>"
                                readonly="true"
                        /><input type="button"
                                class="partnerbutton"
                                value="..." id="navOUPopup"
                                onclick="<c:out value="${btnAction}"/>"/></span>
                        <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                  </c:otherwise>
                </c:choose>
            </td>

            <td class="infodata">
                <spring:bind path="command.columns[${indexer.index}].label">
                    <input type="text" maxlength="240" class="input_text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <%@include file="../../../includes/error_messages.jsp" %>
                </spring:bind>
            </td>

            <td class="small infodata">
                <c:if test="${col.supportsGroups}">
                    <spring:bind path="command.columns[${indexer.index}].grouped">
                        <input type="checkbox" class="input_checkbox" id="colgrp<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" <c:if test="${col.grouped}">checked</c:if>/>
                        <%@include file="../../../includes/error_messages.jsp" %>
                    </spring:bind>
                </c:if>
            </td>

            <td class="infodata" width="100%" colspan="2">
                <input type="button" id="delColInd<c:out value="${indexer.index}"/>" name="_target4" class="inlinebutton" value="<fmt:message key="delete"/>" onclick="deleteTabularReportColumn('<c:out value="${indexer.index}"/>');"/>
                <c:if test="${col.formula}">
                    <input type="button" id="<c:out value="${indexer.index}"/>" class="inlinebutton" value="<fmt:message key="edit"/>" onclick="javascript:editFormula('<c:out value="${indexer.index}"/>');"/>
                </c:if>
                 <c:if test="${col.supportColours}">
                    <input type="button" id="<c:out value="${indexer.index}"/>" class="inlinebutton" value="<fmt:message key="select.colours"/>" onclick="javascript:selectTabularReportColumnColour('<c:out value="${indexer.index}"/>');"/>
                 </c:if>
            </td>

        </tr>
        </c:forEach>
        <tr>
            <td colspan="<c:out value="${ncol}"/>" class="infodata">
                <input class="inlinebutton" type="button" name="_target1" value="<fmt:message key="report.add.column"/>" onclick="javascript:addTabularReportColumn();"/>
                <input id="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" type="hidden" name="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" value="-1"/>
                <input class="inlinebutton" type="button" name="_target6" value="<fmt:message key="report.add.function"/>" onclick="javascript:addTabularReportFormula();"/>
            </td>
        </tr>
        <spring:bind path="command">
            <%@include file="../../../includes/error_messages.jsp" %>
        </spring:bind>
        <tr>
            <td class="infobutton" colspan="<c:out value="${ncol}"/>">
                <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('reports', 'pgTarget', '0', 'backId');"/>
                <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
            </td>
        </tr>
    </table>

    <%-- include must be inside form --%>
    <c:set var="selectedColumnWrapper" value="${command.selectedColumnWrapper}"/>
    <c:if test="${selectedColumnWrapper != null && showPopup}">
        <%@include file="../common/colourpopup.jsp" %>
    </c:if>

    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<c:url value="/picker/tabularreportpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/>
    <c:param name="organisationType" value="true"/>
</c:url>

<zynap:window elementId="columnTree" src="${pickerUrl}"/>