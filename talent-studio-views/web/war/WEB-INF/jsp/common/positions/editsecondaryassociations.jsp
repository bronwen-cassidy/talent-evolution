<%@ page import="IDomainObject"%>
<!--secondary associations -->
<tr>
    <td class="infolabel">
        <fmt:message key="select.secondary.association"/>&nbsp;:&nbsp;
        <br/>
        <input id="addPPAssoc" class="inlinebutton" type="submit" name="_target1" value="<fmt:message key="add"/>"/>
    </td>
    <td class="infodata">
        <c:if test="${not empty command.secondaryAssociations}">
            <table>
                <c:forEach var="assoc" items="${command.secondaryAssociations}" varStatus="count">
                    <tr>
                        <td valign="top">
                            <spring:bind path="command.secondaryAssociations[${count.index}].qualifierId">
                                <select name="<c:out value="${status.expression}"/>">
                                    <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!assoc.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <c:forEach items="${secondary}" var="stype">
                                        <option value="<c:out value="${stype.id}"/>" <c:if test="${assoc.qualifierId == stype.id}">selected</c:if>>
                                            <c:out value="${stype.label}"/>
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
                            <spring:bind path="command.secondaryAssociations[${count.index}].targetId">
                                    <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>
                                    <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${ouMsg}"/>', this, 'positionTree', 'nav_ou_disp<c:out value="${count.index}"/>', 'nav_ou_id<c:out value="${count.index}"/>')</c:set>
                                    <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${count.index}"/>" type="text" class="input_text"
                                        value="<c:out value="${command.secondaryAssociations[count.index].targetLabel}"/>"
                                            name="secondaryAssociations[<c:out value="${count.index}"/>].targetLabel"
                                            readonly="true"
                                    /><input type="button"
                                            class="partnerbutton"
                                            value="..." id="navOUPopup"
                                            onclick="<c:out value="${btnAction}"/>"/></span>
                                    <input id="nav_ou_id<c:out value="${count.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
                                    <%@include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                        <td>
                            <input type="hidden" id="_deleteAssoc:<c:out value="${count.index}"/>" name="_deleteAssoc:<c:out value="${count.index}"/>" value="-1"/>
                            <input class="inlinebutton" type="submit" id="delSubAssoc<c:out value="${count.index}"/>" name="_target2" value="<fmt:message key="delete"/>" onclick="javascript:setHiddenField('_deleteAssoc:<c:out value="${count.index}"/>', '<c:out value="${count.index}"/>')"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </td>
</tr>