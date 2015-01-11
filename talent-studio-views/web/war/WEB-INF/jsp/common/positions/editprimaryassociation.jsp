<%@ page import="com.zynap.domain.IDomainObject"%>
<!-- primaryAssociation -->
<tr>
    <td class="infolabel"><fmt:message key="select.position.primary.association"/>&nbsp;:&nbsp;*</td>
    <td class="infodata">
        <table>
            <tr>
                <td valign="top">
                    <c:set var="primaryAssociationQualifierId" value="${command.primaryAssociation.qualifierId}"/>
                    <spring:bind path="command.primaryAssociation.qualifierId">
                        <select name="<c:out value="${status.expression}"/>">
                            <c:if test="${!command.primaryAssociation.qualifierSet}">
                                <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" selected><fmt:message key="please.select"/></option>
                            </c:if>
                            <c:forEach items="${primary}" var="type">
                                <option value="<c:out value="${type.id}"/>" <c:if test="${primaryAssociationQualifierId == type.id}">selected</c:if>>
                                    <c:out value="${type.label}"/>
                                </option>
                            </c:forEach>
                        </select>
                        <%@include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
                <td>
                    <img alt="" src="../images/reportsTo.gif"/>
                </td>
                <td valign="top">
                    <c:set var="primaryAssociationTargetId" value="${command.primaryAssociation.targetId}"/>
                    <spring:bind path="command.primaryAssociation.targetId">
                        <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>
                        <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'parentTree', 'nav_ou_disp', 'nav_ou_id')</c:set>

                        <span style="white-space: nowrap;"><input id="nav_ou_disp" type="text" class="input_text"
                            value="<c:out value="${command.primaryAssociation.targetLabel}"/>"
                                name="<c:out value="primaryAssociation.targetLabel"/>"
                                readonly="true"
                        /><input type="button"
                                class="partnerbutton"
                                value="..." id="navOUPopup"
                                onclick="<c:out value="${btnAction}"/>"/></span>
                        <input id="nav_ou_id" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${primaryAssociationTargetId}"/>" />
                        <%@ include file="../../includes/error_message.jsp" %>
                    </spring:bind>
                </td>
            </tr>
        </table>
    </td>
</tr>