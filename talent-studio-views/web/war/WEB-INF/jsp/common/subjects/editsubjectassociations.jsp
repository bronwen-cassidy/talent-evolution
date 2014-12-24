<%@ include file="../../includes/include.jsp" %>

<fmt:message key="generic.for" var="for"/>
<c:set var="assocInfo" value="" scope="request"/>
<c:if test="${command.assocInfo != null || command.assocInfo != ''}">
    <c:set var="assocInfo" value="( ${command.assocInfo} )" scope="request"/>
</c:if>
<c:set var="msg" value="${command.displayConfigItem.label} ${for} ${command.label} ${assocInfo}"/>

<zynap:infobox title="${msg}">

    <spring:bind path="command">
        <%@ include file="../../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form name="addSubPAssoc" method="post">
        
        <c:forEach var="itemReport" items="${command.displayConfigItem.reportItems}">

            <c:if test="${itemReport.subjectPrimaryAssociation}">
                <c:if test="${empty command.subjectPrimaryAssociations}">
                    <div class="infomessage"><fmt:message key="no.associations"/></div>
                </c:if>

                <c:set var="associations" value="${command.subjectPrimaryAssociations}"/>
                <%@ include file="editprimaryassociations.jsp" %>
            </c:if>
            
            <c:if test="${itemReport.subjectSecondaryAssociation}">
                <c:if test="${empty command.subjectSecondaryAssociations}">
                    <div class="infomessage"><fmt:message key="no.associations"/></div>
                </c:if>
                <c:set var="associations" value="${command.subjectSecondaryAssociations}"/>
                <%@ include file="editsecondaryassociations.jsp" %>
            </c:if>

        </c:forEach>
    </zynap:form>
</zynap:infobox>

<c:if test="${not empty command.subjectPrimaryAssociations}">
    <zynap:window elementId="securePositionTree" src="../picker/positionparentpicker.htm"/>
</c:if>

<c:if test="${not empty command.subjectSecondaryAssociations}">
    <zynap:window elementId="positionTree" src="../picker/positionparentpicker.htm"/>
</c:if>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>



