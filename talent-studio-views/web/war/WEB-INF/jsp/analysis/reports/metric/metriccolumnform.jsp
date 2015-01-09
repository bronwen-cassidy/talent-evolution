<%@ page import="IPopulationEngine,
                 com.zynap.talentstudio.web.analysis.reports.BaseReportsWizardController"%>
<%@ include file="../../../includes/include.jsp" %>

<script type="text/javascript">

   function fillField(toElementA, fromElement) {
      var target = getElemById(toElementA);
      target.value = fromElement.options[fromElement.selectedIndex].text;
   }

   function addMetricAttribute() {
      var frm = document.forms['reports'];
      var targetField = getElemById('pgTarget');
      var indexField = getElemById('<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>');
      indexField.value = 0;
      targetField.name='_target1';
      targetField.value=1;
      frm.submit();
   }

   function addMetricsFunction() {
         var frm = document.forms['reports'];
         var targetField = getElemById('pgTarget');
         targetField.name='_target3';
         targetField.value=3;
         frm.submit();
    }


   function removeMetricAttribute(index) {
     var frm = document.forms['reports'];
     var indexField = getElemById('<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>');
     indexField.value= index;
     var targetField = getElemById('pgTarget');
     targetField.name='_target2';
     targetField.value=2;
     frm.submit();
   }


</script>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="reports" method="post" encType="multipart/form-data">
       <%-- hidden field set by JavaScript that holds the target page variable for the wizard controller --%>
    <input id="pgTarget" type="hidden" name="" value="-1"/>       
    <input id="backId" type="hidden" name="" value="-1"/>

   <input id="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" type="hidden" name="<%= BaseReportsWizardController.SELECTED_COLUMN_INDEX %>" value="-1"/>

    <table class="infotable">
        <tr>
            <td class="infoheading" colspan="3"><fmt:message key="report.columns"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="metric"/>&nbsp;:&nbsp;*</td>
            <td class="infolabel"><fmt:message key="column.label"/>&nbsp;:&nbsp;*</td>
            <td class="infolabel">&nbsp;</td>
        </tr>
        <c:forEach var="col" items="${command.columns}" varStatus="indexer" >
            <tr>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${col.formula}">
                            <label><c:out value="${col.functionWrapperBean.formulaDisplay}"/></label>
                        </c:when>
                        <c:otherwise>
                            <spring:bind path="command.columns[${indexer.index}].metricId">
                                <select name="<c:out value="${status.expression}"/>" onchange="fillField('idText<c:out value="${indexer.index}"/>', this);">
                                    <option value="-1" <c:if test="${col.metricId == -1 || col.metricId == null}">selected</c:if>><fmt:message key="scalar.operator.count"/></option>
                                    <c:forEach var="metric" items="${command.metrics}">
                                        <option value="<c:out value="${metric.id}"/>" <c:if test="${col.metricId == metric.id}">selected</c:if>><c:out value="${metric.label}"/></option>
                                    </c:forEach>
                                </select>
                                <%@include file="../../../includes/error_message.jsp" %>
                            </spring:bind>
                        </c:otherwise>
                   </c:choose>
                </td>
                <td class="infodata">
                    <spring:bind path="command.columns[${indexer.index}].label">
                        <input type="text" maxlength="240" class="input_text" id="idText<c:out value="${indexer.index}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                        <%@include file="../../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
                <td class="infodata">
                    <input type="button" class="inlinebutton" value="<fmt:message key="delete"/>" onclick="removeMetricAttribute('<c:out value="${indexer.index}"/>');"/>
                    <c:if test="${col.formula}">
                        <input type="button" id="<c:out value="${indexer.index}"/>" class="inlinebutton" value="<fmt:message key="edit"/>" onclick="submitFormToTarget('reports', '_target4', '4', 'pgTarget','selectedColumnIndex','<c:out value="${indexer.index}"/>');"/>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <spring:bind path="command">
            <%@include file="../../../includes/error_message.jsp" %>
        </spring:bind>
        <tr>
            <td class="infodata" colspan="3">
                <input class="inlinebutton" type="button" name="_target1" value="<fmt:message key="report.add.metric"/>" onclick="addMetricAttribute();"/>
                <input class="inlinebutton" type="button" name="addFunction" value="<fmt:message key="report.add.function"/>" onclick="addMetricsFunction();"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="report.choose.group.column"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <spring:bind path="command.groupingColumn.attribute">

                    <%-- label field name --%>
                    <c:set var="fieldName" value="groupingColumn.attributeLabel"/>

                    <c:set var="btnAction">javascript:showColumnTree('<zynap:message code="please.select" javaScriptEscape="true"/>', this, 'columnTree', '<c:out value="${fieldName}"/>', '<c:out value="${status.expression}"/>', 'groupingColumn.label', true)</c:set>

                    <%-- determine the correct label --%>
                    <c:set var="label" value=""/>
                    <c:if test="${command.groupingColumn.attributeSet}">
                        <c:set var="label" value="${command.groupingColumn.attributeLabel}"/>
                    </c:if>

                    <span style="white-space: nowrap;"><input id="<c:out value="${fieldName}"/>" type="text" class="input_text"
                        value="<c:out value="${label}"/>"
                            name="<c:out value="${fieldName}"/>"
                            readonly="true"
                    /><input type="button"
                            class="partnerbutton"
                            value="..." id="navOUPopup"
                            onclick="<c:out value="${btnAction}"/>"/></span>
                    <input id="<c:out value="${status.expression}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />

                    <%@include file="../../../includes/error_messages.jsp" %>
                </spring:bind>
            </td>
            <td class="infodata">
                <spring:bind path="command.groupingColumn.label">
                    <input type="text" maxlength="240" class="input_text" id="<c:out value="${status.expression}"/>" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>"/>
                    <%@include file="../../../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>

        <tr>
            <td class="infolabel"><fmt:message key="preferred.drilldown.report"/>&nbsp;:&nbsp;</td>
            <td class="infodata" colspan="2">
                <spring:bind path="command.drillDownReportId">
                    <select name="<c:out value="${status.expression}"/>">
                        <option value="" <c:if test="${command.drillDownReportId == null}">selected</c:if>/>
                        <c:forEach var="report" items="${command.drillDownReports}">
                            <option value="<c:out value="${report.id}"/>" <c:if test="${command.drillDownReportId == report.id}">selected</c:if>><c:out value="${report.label}"/></option>
                        </c:forEach>
                    </select>
                    <%@include file="../../../includes/error_message.jsp" %>
                </spring:bind>
            </td>
        </tr>
       
        <tr>
            <td class="infobutton">&nbsp;</td>
            <td class="infobutton" colspan="2">
                <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                <input class="inlinebutton" name="_back" type="button" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('reports', 'pgTarget', '0', 'backId');"/>
                <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
            </td>
        </tr>
    </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<zynap:form method="post" name="_back">
    <input type="hidden" name="_back" value="_back"/>
    <input type="hidden" name="_target0" value="_target0"/>
</zynap:form>

<%-- the picker is the crosstab picker as the only picked field is the group by and this must be an enumeration --%>
<c:url value="/picker/crosstabreportpicker.htm" var="pickerUrl">
    <c:param name="populationType" value="${command.type}"/>
</c:url>

<zynap:window elementId="columnTree" src="${pickerUrl}"/>

