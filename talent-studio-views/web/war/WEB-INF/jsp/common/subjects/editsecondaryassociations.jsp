<%@ page import="com.zynap.domain.IDomainObject"%>
<table class="infotable" cellspacing="0">
    <tr>
        <td class="infolabel">
            <fmt:message key="select.secondary.association"/>&nbsp;:&nbsp;
            <br/>
            <input id="addAssoc" class="inlinebutton" type="submit" name="_target2" value="<fmt:message key="add"/>"/>
        </td>
        <td class="infodata">
            <table>
                <c:forEach var="primaryAssociation" items="${associations}" varStatus="count" >
                   <tr>
                        <td valign="top">
                            <spring:bind path="command.subjectSecondaryAssociations[${count.index}].qualifierId">
                                <select name="<c:out value="${status.expression}"/>">
                                    <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!primaryAssociation.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <c:forEach var="associationType" items="${secondaryTypes}">
                                        <option value="<c:out value="${associationType.id}"/>" <c:if test="${primaryAssociation.qualifierId == associationType.id}">selected</c:if>><c:out value="${associationType.label}"/></option>
                                    </c:forEach>
                                </select>
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                        <td valign="top">
                            <img alt="" src="../images/reportsTo.gif"/>
                        </td>
                        <td valign="top">
                            <spring:bind path="command.subjectSecondaryAssociations[${count.index}].targetId">
                                <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>
                                <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'positionTree', 'nav_ou_disp<c:out value="${count.index}"/>', 'nav_ou_id<c:out value="${count.index}"/>')</c:set>
                                <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${count.index}"/>" type="text" class="input_text"
                                    value="<c:out value="${command.subjectSecondaryAssociations[count.index].targetLabel}"/>"
                                        name="subjectSecondaryAssociations[<c:out value="${count.index}"/>].targetLabel"
                                        readonly="true"
                                /><input type="button"
                                        class="partnerbutton"
                                        value="..." id="navOUPopup"
                                        onclick="<c:out value="${btnAction}"/>"/></span>
                                <input id="nav_ou_id<c:out value="${count.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.subjectSecondaryAssociations[count.index].targetId}"/>" />
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                       <td valign="top">
                            <spring:bind path="command.subjectSecondaryAssociations[${count.index}].comments">
                                <textarea name="<c:out value="${status.expression}"/>" rows="3" cols="30" class="input_textarea"><c:out value="${status.value}"/></textarea>
                                <%@include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                        <td>
                            <input type="hidden" id="_deleteAssoc:<c:out value="${count.index}"/>" name="_deleteAssoc:<c:out value="${count.index}"/>" value="-1"/>
                            <input class="inlinebutton" type="submit" id="delSubAssoc<c:out value="${count.index}"/>" name="_target4" value="<fmt:message key="delete"/>" onclick="javascript:setHiddenField('_deleteAssoc:<c:out value="${count.index}"/>', '<c:out value="${count.index}"/>')"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>
    <tr>
        <td class="infobutton"></td>
        <td class="infobutton">
            <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
            <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
        </td>
    </tr>
</table>