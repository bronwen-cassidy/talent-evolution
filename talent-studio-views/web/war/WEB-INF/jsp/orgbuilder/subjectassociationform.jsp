<%@ page import="com.zynap.domain.IDomainObject"%>
<%@ include file="../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>

<zynap:infobox title="${msg}">

    <c:if test="${empty command.subjectPrimaryAssociations}">
        <div class="infomessage"><fmt:message key="no.associations"/></div>
    </c:if>

    <spring:bind path="command">
        <%@ include file="../includes/error_messages.jsp" %>
    </spring:bind>

    <zynap:form name="_addAssoc" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel">
                    <fmt:message key="select.primary.association"/>&nbsp;:&nbsp;
                    <br/>
                    <input class="inlinebutton" type="submit" name="_addAssociation" value="<fmt:message key="add"/>"/>
                </td>
                <td class="infodata">
                    <c:if test="${not empty command.subjectPrimaryAssociations}">
                        <table>
                           <c:forEach var="primaryAssociation" items="${command.subjectPrimaryAssociations}" varStatus="count" >
                               <tr>
                                    <td valign="top">
                                       <spring:bind path="command.subjectPrimaryAssociations[${count.index}].qualifierId">
                                           <select name="<c:out value="${status.expression}"/>">
                                               <option value="<%=IDomainObject.UNASSIGNED_VALUE%>" <c:if test="${!primaryAssociation.qualifierSet}">selected</c:if>><fmt:message key="please.select"/></option>
                                               <c:forEach var="associationType" items="${associationTypes}">
                                                   <option name="<c:out value="${associationType.valueId}"/>" value="<c:out value="${associationType.id}"/>" <c:if test="${primaryAssociation.qualifierId == associationType.id}">selected</c:if>><c:out value="${associationType.label}"/></option>
                                               </c:forEach>
                                           </select>
                                           <%@ include file="../includes/error_message.jsp" %>
                                       </spring:bind>
                                    </td>
                                    <td>
                                        <img alt="" src="../images/reportsTo.gif"/>
                                    </td>
                                    <td valign="top">
                                       <spring:bind path="command.subjectPrimaryAssociations[${count.index}].targetId">
                                           <zynap:message code="select.position" var="ouMsg" javaScriptEscape="true"/>
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
                                           <%@ include file="../includes/error_message.jsp" %>
                                       </spring:bind>
                                    </td>
                                    <td>
                                        <input class="inlinebutton" type="submit" name="_deleteAssoc:<c:out value="${count.index}"/>" value="<fmt:message key="delete"/>"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:if>
                </td>
            </tr>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="handleWizardBack('_addAssoc', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<c:if test="${not empty command.subjectPrimaryAssociations}">
    <zynap:window elementId="positionTree" src="../picker/positionparentpicker.htm"/>
</c:if>    