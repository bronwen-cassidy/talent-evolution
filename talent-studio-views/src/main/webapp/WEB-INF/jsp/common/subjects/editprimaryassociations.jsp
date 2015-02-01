<%@ page import="com.zynap.domain.IDomainObject"%>
<table class="infotable" cellspacing="0">
    <tr>
        <td class="infolabel">
            <fmt:message key="select.primary.association"/>&nbsp;:&nbsp;
            <br/>
            <input id="addAssoc" class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="add"/>"/>
        </td>
        <td class="infodata">
            <table>
                <c:forEach var="primaryAssociation" items="${associations}" varStatus="count" >
                   <tr>
                        <td valign="top">
                            <spring:bind path="command.subjectPrimaryAssociations[${count.index}].qualifierId">
                                <select name="<c:out value="${status.expression}"/>">
                                    <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!primaryAssociation.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <c:forEach var="associationType" items="${primaryTypes}">
                                        <option value="<c:out value="${associationType.id}"/>" <c:if test="${primaryAssociation.qualifierId == associationType.id}">selected</c:if>><c:out value="${associationType.label}"/></option>
                                    </c:forEach>
                                </select>
                                <%@ include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                        <td>
                            <img alt="" src="../images/reportsTo.gif"/>
                        </td>
                        <td valign="top">
                            <spring:bind path="command.subjectPrimaryAssociations[${count.index}].targetId">
                                <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>
                                <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'securePositionTree', 'nav_ou_disp<c:out value="${count.index}"/>', 'nav_ou_id<c:out value="${count.index}"/>')</c:set>
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
                        </td>
                        <td>
                            <input type="hidden" id="_deleteAssoc:<c:out value="${count.index}"/>" name="_deleteAssoc:<c:out value="${count.index}"/>" value="-1"/>
                            <input class="inlinebutton" type="submit" id="delSubAssoc<c:out value="${count.index}"/>" name="_target3" value="<fmt:message key="delete"/>" onclick="javascript:setHiddenField('_deleteAssoc:<c:out value="${count.index}"/>', '<c:out value="${count.index}"/>')"/>
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