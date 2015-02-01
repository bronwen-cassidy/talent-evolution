<%@ page import="com.zynap.domain.IDomainObject"%>
<!--secondary associations -->
<tr>
    <td class="infolabel">
        <fmt:message key="select.position.secondary.association"/>&nbsp;:&nbsp;
        <br/>
        <input id="addSSPAssoc" class="inlinebutton" type="submit" name="_target4" value="<fmt:message key="add"/>"/>
    </td>
    <td class="infodata">
        <c:if test="${not empty command.subjectSecondaryAssociations}">
            <table>
                <c:forEach var="assoc" items="${command.subjectSecondaryAssociations}" varStatus="count">
                    <tr>
                        <td valign="top">
                            <spring:bind path="command.subjectSecondaryAssociations[${count.index}].qualifierId">
                                <select name="<c:out value="${status.expression}"/>">
                                    <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!assoc.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                                    <c:forEach items="${secondarySubjectTypes}" var="stype">
                                        <option value="<c:out value="${stype.id}"/>" <c:if test="${assoc.qualifierId == stype.id}">selected</c:if>>
                                            <c:out value="${stype.label}"/>
                                        </option>
                                    </c:forEach>
                                </select>
                                <%@include file="../../includes/error_message.jsp" %>
                            </spring:bind>
                        </td>
                        <td valign="top">
                            <img alt="" src="../images/reportsTo.gif"/>
                        </td>
                        <td valign="top">
                            <spring:bind path="command.subjectSecondaryAssociations[${count.index}].sourceId">
                                    <zynap:message code="select.subject" var="perMsg" javaScriptEscape="true"/>
                                    <c:set var="btnAction">javascript:popupShowServerTree('<c:out value="${perMsg}"/>', this, 'subjectTree', 'nav_ss_disp<c:out value="${count.index}"/>', 'nav_ss_id<c:out value="${count.index}"/>')</c:set>
                                    <span style="white-space: nowrap;"><input id="nav_ss_disp<c:out value="${count.index}"/>" type="text" class="input_text"
                                        value="<c:out value="${command.subjectSecondaryAssociations[count.index].sourceLabel}"/>"
                                            name="subjectSecondaryAssociations[<c:out value="${count.index}"/>].sourceLabel"
                                            readonly="true"
                                    /><input type="button"
                                            class="partnerbutton"
                                            value="..." id="navOUPopup"
                                            onclick="<c:out value="${btnAction}"/>"/></span>
                                    <input id="nav_ss_id<c:out value="${count.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.subjectSecondaryAssociations[count.index].sourceId}"/>" />
                                    <%@include file="../../includes/error_message.jsp" %>
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
                            <input class="inlinebutton" type="submit" id="delSubAssoc<c:out value="${count.index}"/>" name="_target6" value="<fmt:message key="delete"/>" onclick="javascript:setHiddenField('_deleteAssoc:<c:out value="${count.index}"/>', '<c:out value="${count.index}"/>')"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </td>
</tr>