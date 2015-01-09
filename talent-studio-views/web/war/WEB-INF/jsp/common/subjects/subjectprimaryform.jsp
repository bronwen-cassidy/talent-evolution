<%@ page import="IDomainObject"%>
<c:forEach var="primaryAssociation" items="${command.subjectPrimaryAssociations}" varStatus="count" >
    <div style="white-space: nowrap;">
        <spring:bind path="command.subjectPrimaryAssociations[${count.index}].qualifierId">
            <select name="<c:out value="${status.expression}"/>">
                <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!primaryAssociation.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                <c:forEach var="associationType" items="${associationTypes}">
                    <option name="<c:out value="${associationType.valueId}"/>" value="<c:out value="${associationType.id}"/>" <c:if test="${primaryAssociation.qualifierId == associationType.id}">selected</c:if>><c:out value="${associationType.label}"/></option>
                </c:forEach>
            </select>
            <%@ include file="../../includes/error_message.jsp" %>
        </spring:bind>
        <img alt="" src="../images/reportsTo.gif"/>
        <spring:bind path="command.subjectPrimaryAssociations[${count.index}].targetId">
            <zynap:message code="select.position" var="ouMsg"  javaScriptEscape="true"/>
            <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'positionTree', 'nav_ou_disp<c:out value="${count.index}"/>', 'nav_ou_id<c:out value="${count.index}"/>')</c:set>
            <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${count.index}"/>" type="text" class="input_text"
                value="<c:out value="${command.subjectPrimaryAssociations[count.index].targetLabel}"/>"
                    name="subjectPrimaryAssociations[<c:out value="${count.index}"/>].targetLabel"
                    readonly="true"
            /><input type="button"
                    class="partnerbutton"
                    value="..." id="navOUPopup"
                    onclick="<c:out value="${btnAction}"/>"/></span>
            <input id="nav_ou_id<c:out value="${count.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.subjectPrimaryAssociations[count.index].targetId}"/>" />
            <%@ include file="../../includes/error_message.jsp" %>
        </spring:bind>
        <input class="inlinebutton" type="submit" name="_deleteAssoc:<c:out value="${count.index}"/>" value="<fmt:message key="delete"/>"/>
    </div>
</c:forEach>
