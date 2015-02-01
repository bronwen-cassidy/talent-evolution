<%@ page import="com.zynap.talentstudio.web.reportingchart.ReportChartController,
                 com.zynap.talentstudio.web.common.ControllerConstants"%>

<zynap:actionbox>

    <c:set var="position" value="${command.position}"/>
    <c:set var="positions" value="${command.subjectPositions}"/>
    <c:set var="hasPosition" value="${position != null}"/>

    <fmt:message var="positionLabel" key="please.select"/>
    <c:if test="${hasPosition}">
        <c:set var="positionLabel" value="${position.label}"/>
        <c:set var="positionId" value="${position.id}"/>
    </c:if>

    <zynap:form name="_view" method="post" action="reportingchart.htm">
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infodata">
                    <zynap:message code="report.popup.message" var="ouMsg" javaScriptEscape="true"/>
                    <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'chartTree', 'nav_ou_disp', 'nav_ou_id' , 'goToPreviousSelected()')</c:set>

                    <c:out value="${ouMsg}"/>&nbsp;:&nbsp;

                    <span style="white-space: nowrap;"><input id="nav_ou_disp" type="text" class="input_text" style="width:220px;"
                            value="<c:out value="${positionLabel}"/>"
                            name="positionLabel"
                            readonly="true"/><input type="button"
                            class="partnerbutton"
                            value="..." id="navOUPopup"
                            onclick="<c:out value="${btnAction}"/>"/></span>
                    <input id="nav_ou_id" type="hidden" name="node_id" value="<c:out value="${positionId}"/>" />
                </td>
                <td class="infodata">
                    <c:forEach var="pos" items="${positions}">
                        <a href="javascript:setValueAndSubmit('_view', 'nav_ou_id', '<c:out value="${pos.id}"/>')"><c:out value="${pos.label}"/><c:out value=" ( "/><c:out value="${pos.organisationUnit.label}"/><c:out value=" )"/></a><br/>
                    </c:forEach>
                </td>
            </tr>
        </table>
    </zynap:form>

    <c:choose>
        <c:when test="${command.secure}">
            <zynap:window elementId="chartTree" src="../picker/positionparentpicker.htm" initialLeaf="${positionId}"/>
        </c:when>
        <c:otherwise>
            <zynap:window elementId="chartTree" src="../picker/positionparentpicker.htm" initialLeaf="${positionId}"/>
        </c:otherwise>
    </c:choose>


</zynap:actionbox>

<%-- links --%>

<%-- arrow link (up/down) --%>
<zynap:artefactLink activeTab="" tabName="" var="url" url="reportingchart.htm" commandAction="leaveCommand">
    <zynap:param name="<%=ReportChartController.SUBMIT_PARAM%>" value="true"/>
</zynap:artefactLink>

<zynap:artefactLink activeTab="" tabName="" var="ouUrl" url="vieworganisation.htm" commandAction="_saveCommand_"/>

<zynap:artefactLink activeTab="" tabName="" var="subUrl" url="viewsubject.htm" commandAction="_saveCommand_">
    <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${command.displayConfigKey}"/>
</zynap:artefactLink>

<zynap:artefactLink activeTab="" tabName="" var="posUrl" url="viewposition.htm" commandAction="_saveCommand_">
    <zynap:param name="<%=ControllerConstants.DISPLAY_CONFIG_KEY%>" value="${command.displayConfigKey}"/>
</zynap:artefactLink>