<%@ page import="com.zynap.domain.IDomainObject"%>
<%@ include file="../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_addAssoc" method="post">
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>

        <table class="infotable" cellspacing="0">
            <%@include file="../common/positions/editprimaryassociation.jsp"%>

            <!--secondary associations -->
            <tr>
                <td class="infolabel">
                    <fmt:message key="select.secondary.association"/>&nbsp;:&nbsp;
                    <br/>
                    <input class="inlinebutton" type="submit" name="_addAssociation" value="<fmt:message key="add"/>"/>
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
                                            <%@include file="../includes/error_message.jsp" %>
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
                                                <input id="nav_ou_id<c:out value="${count.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.secondaryAssociations[count.index].targetId}"/>" />
                                                <%@include file="../includes/error_message.jsp" %>
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
                <td class="infobutton"></td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_addAssoc', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
	<input type="hidden" name="_cancel" value="_cancel"/>
	<input type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${command.position.id}"/>"/>
</zynap:form>

<c:url var="iframeSrc" value="../picker/positionparentpicker.htm"/>
<zynap:window elementId="parentTree" src="${iframeSrc}" initialLeaf="${primaryAssociationTargetId}"/>

<c:if test="${not empty command.secondaryAssociations}">
    <zynap:window elementId="positionTree" src="../picker/positionpicker.htm"/>
</c:if>

