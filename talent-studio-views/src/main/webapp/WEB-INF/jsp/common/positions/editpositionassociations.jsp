<%@ include file="../../includes/include.jsp" %>

<fmt:message key="generic.for" var="for"/>
<c:set var="assocInfo" value="" scope="request"/>
<c:if test="${command.assocInfo != null || command.assocInfo != ''}">
    <c:set var="assocInfo" value="( ${command.assocInfo} )" scope="request"/>
</c:if>

<c:set var="msg" value="${command.displayConfigItem.label} ${for} ${command.label} ${assocInfo}"/>

<c:set var="positionPrimarySourceAssociation" value="x" scope="request"/>
<c:set var="positionSecondarySourceAssociation" value="x" scope="request"/>
<c:set var="subjectPositionPrimaryAssociation" value="x" scope="request"/>
<c:set var="subjectPositionSecondaryAssociation" value="x" scope="request"/>

<zynap:infobox title="${msg}">
    <zynap:form name="addEditPosAssocs" method="post">
        <table class="infotable" cellspacing="0" cellpadding="0">

            <c:forEach var="itemReport" items="${command.displayConfigItem.reportItems}">
                <!-- exclude if the default position -->
                <c:if test="${!command.default}">
                    <c:if test="${itemReport.positionPrimarySourceAssociation}">
                        <c:set var="positionPrimarySourceAssociation" value="y" scope="request"/>
                        <%@include file="editprimaryassociation.jsp" %>
                    </c:if>

                    <c:if test="${itemReport.positionSecondarySourceAssociation}">

                        <c:set var="positionSecondarySourceAssociation" value="y" scope="request"/>
                        <%@include file="editsecondaryassociations.jsp"%>
                    </c:if>
                </c:if>

                <c:if test="${itemReport.subjectPositionPrimaryAssociation}">
                    <c:set var="subjectPositionPrimaryAssociation" value="y" scope="request"/>
                    <%@include file="editsubjectprimaryassociations.jsp"%>
                </c:if>
                <c:if test="${itemReport.subjectPositionSecondaryAssociation}">
                    <c:set var="subjectPositionSecondaryAssociation" value="y" scope="request"/>
                    <%@include file="editsubjectsecondaryassociations.jsp"%>
                </c:if>
                
            </c:forEach>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<%-- pickers --%>
<c:if test="${positionPrimarySourceAssociation == 'y'}">
    <c:url var="iframeSrc" value="../picker/positionparentpicker.htm">
       <c:param name="sourcePositionId" value="${command.position.id}"/>
    </c:url>
    <zynap:window elementId="parentTree" src="${iframeSrc}" initialLeaf="${primaryAssociationTargetId}"/>
</c:if>

<c:if test="${positionSecondarySourceAssociation == y}">
    <zynap:window elementId="positionTree" src="../picker/positionpicker.htm"/>
</c:if>

<c:if test="${subjectPositionPrimaryAssociation == 'y' || subjectPositionSecondaryAssociation == 'y'}">
    <zynap:window elementId="subjectTree" src="../picker/subjectpicker.htm"/>
</c:if>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>