<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>
<%@ page import="com.zynap.talentstudio.web.workflow.WorkflowConstants"%>

<zynap:actionbox id="rolesActions">
    <zynap:actionEntry>
        <zynap:form method="post" name="closerfrm">
            <input type="hidden" name="_target4" value="4"/>
            <input class="actionbutton" type="button" value="<fmt:message key="close"/>" onclick="document.forms.closerfrm.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>


<c:set var="performanceRoles" value="${command.performanceRoles}"/>

<fmt:message key="appraisal.roles" var="roleTabLabel"/>

<zynap:infobox title="${roleTabLabel}" id="appraisalRoles">
    <c:if test="${message != null}">
        <div class="infomessage"><fmt:message key="${message}"/></div>
    </c:if>

    <spring:bind path="command">
       <%@ include file="../includes/error_messages.jsp" %>
    </spring:bind>
    <zynap:form method="post" name="rolesForm">
        <table class="infotable" id="assignRoles">
            <tr>
                <td class="infolabel"><fmt:message key="appraisal"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><c:out value="${command.appraisalReview.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="appraisee"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:out value="${command.evaluateeRole.performanceEvaluator.subject.label}"/>
                    <%-- index to know which row is to be deleted --%>
                    <input id="delIndx" type="hidden" name="delIndx" value="-1"/>
                    <%-- form target --%>
                    <input id="roleFrmFldId" type="hidden" name="" value=""/>
                </td>
            </tr>
            <%--the existing roles for edit --%>
            <c:forEach var="performanceRole" items="${performanceRoles}" varStatus="indexer">
                <tr id="markerRow<c:out value="${indexer.index}"/>">
                    <td class="infodata">
                        <spring:bind path="command.performanceRoles[${indexer.index}].performanceRoleId">
                            <select name="<c:out value="${status.expression}"/>">
                                <option value="" <c:if test="${performanceRole.performanceRoleId == null}">selected</c:if> ><fmt:message key="please.select"/></option>
                                <c:forEach var="roleValue" items="${command.roles}" varStatus="roleIndex">
                                    <option value="<c:out value="${roleValue.id}"/>" <c:if test="${performanceRole.performanceRoleId == roleValue.id}">selected</c:if> ><c:out value="${roleValue.label}"/></option>
                                </c:forEach>
                            </select>
                            <%@ include file="../includes/error_message.jsp" %>
                        </spring:bind>
                    </td>
                    <td class="infodata">
                        <spring:bind path="command.performanceRoles[${indexer.index}].performerLabel">
                            <span style="white-space: nowrap;"><input id="nav_ou_disp<c:out value="${indexer.index}"/>" type="text" class="input_text"
                                    value="<c:out value="${status.value}"/>"
                                    name="<c:out value="${status.expression}"/>"
                                    readonly="true"/><input type="button"
                                    class="partnerbutton"
                                    value="..." id="navOUPopup"
                                        <c:out value="${titleAttr}" escapeXml="false"/>
                                    onclick="popupShowServerTree('<zynap:message code="select.subject" javaScriptEscape="true"/>', this, 'subjectUserTree', 'nav_ou_disp<c:out value="${indexer.index}"/>', 'nav_ou_id<c:out value="${indexer.index}"/>',null, true)"/>
                            </span>
                        </spring:bind>
                        <spring:bind path="command.performanceRoles[${indexer.index}].performerId">
                            <input id="nav_ou_id<c:out value="${indexer.index}"/>" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.performanceRoles[indexer.index].performerId}"/>" />
                            <%@ include file="../includes/error_message.jsp" %>
                        </spring:bind>
                        <%--delete button div--%>
                        <span><input type="button" value="<fmt:message key="delete"/>" onclick="setValue('delIndx', '<c:out value="${indexer.index}"/>'); setNameValueAndSubmit('rolesForm', 'roleFrmFldId', '_target15', '15')"/></span>                 
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="infobutton" colspan="2">
                    <input type="button" name="addExpBtn" value="<fmt:message key="add"/>" onclick="setNameValueAndSubmit('rolesForm', 'roleFrmFldId', '_target14', '14')"/>&nbsp;
                </td>
            </tr>
            <c:if test="${command.evaluateeHasUser}">
                <tr>
                    <td class="infolabel"><fmt:message key="appraisal.self"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <input type="checkbox" class="input_checkbox" name="<%=WorkflowConstants.SELF_EVALUATION_PARAM %>"
                            <c:if test="${command.evaluateeRole.hasUser}">checked="checked"</c:if>/>
                    </td>
                </tr>
            </c:if>
            <c:if test="${not empty command.managers && command.notification.userManaged}">
                <tr>
                    <td class="infolabel"><fmt:message key="manager.select"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <table cellpadding="0" cellspacing="0">
                            <tr>
                                <td colspan="2" align="center" class="infodata">
                                    <input class="inlinebutton" type="button" name="UnCheckAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.rolesForm.selectedManagerId)"/>
                                </td>
                            </tr>

                            <c:forEach var="manager" items="${command.managers}">
                                <tr>
                                    <td class="infodata" width="50%"><c:out value="${manager.label}"/><c:out value="${manager.jobTitles}"/>&nbsp;:</td>
                                    <td class="infodata">
                                        <spring:bind path="command.selectedManagerId">
                                            <c:choose>
                                                <c:when test="${command.managerCount == 1}">
                                                    <input type="checkbox" class="input_checkbox" value="<c:out value="${manager.userId}"/>" name="<c:out value="${status.expression}"/>" <c:if test="${command.selectedManagerId == manager.userId}">checked="true"</c:if>/><br/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" class="input_radio" value="<c:out value="${manager.userId}"/>" name="<c:out value="${status.expression}"/>" <c:if test="${command.selectedManagerId == manager.userId}">checked="true"</c:if>/><br/>
                                                </c:otherwise>
                                            </c:choose>
                                        </spring:bind>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </c:if>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input type="submit" class="inlinebutton" name="_target10" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

