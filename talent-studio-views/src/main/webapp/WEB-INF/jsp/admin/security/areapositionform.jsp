<%@ page import="com.zynap.talentstudio.web.security.area.BaseAreaController"%>
<%@ include file="../../includes/include.jsp" %>

<%@include file="../../includes/orgunitpicker.jsp"%>

<c:set var="hasOuTree" value="${outree != null && !empty outree}"/>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input id="searchS" type="hidden" name="<%=BaseAreaController.SEARCH_STARTED%>" value=""/>
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>

        <table class="infotable" cellspacing="0">
            <c:if test="${!empty command.assignedPositions}">
                <thead>
                    <tr>
                        <th><fmt:message key="area.assignedpositions"/></th>
                        <th><fmt:message key="area.positions.excluded"/></th>
                    </tr>
                </thead>
            </c:if>
            <c:forEach var="posAreaElement" items="${command.assignedPositions}"  varStatus="listIndex">
                <c:set var="position" value="${posAreaElement.node}"/>
                <tr>
                    <td class="infodata">
                        <spring:bind path="command.positionIds">
                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${position.id}"/>" checked/>
                            &nbsp;
                            <c:out value="${position.label}"/>
                        </spring:bind>
                    </td>
                    <td class="infodata">
                        <spring:bind path="command.assignedPositions[${listIndex.index}].excluded">
                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${posAreaElement.excluded}">checked</c:if>/>
                        </spring:bind>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="infolabel"><fmt:message key="report.population"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.positionPopulationId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${command.positionPopulationId == null}">selected</c:if>>&nbsp;</option>
                            <c:forEach var="posPop" items="${populations}">
                                <option value="<c:out value="${posPop.id}"/>" <c:if test="${posPop.id == command.positionPopulationId}">selected</c:if>><c:out value="${posPop.label}"/></option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="area.assign.positions"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <table class="infotable" cellspacing="0">
                        <tr>
                            <td class="infoheading" colspan="2"><fmt:message key="search.parameters"/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="admin.position.title"/>&nbsp;:&nbsp;</td>
                            <td class="infodata"><input type="text" class="input_text" name="<%=BaseAreaController.POSITION_TITLE%>" value=""/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="search.orgunit"/>&nbsp;:&nbsp;</td>
                            <td class="infodata">
                               <zynap:message code="search.orgunit" var="popMsg" javaScriptEscape="true"/>
                               <span style="white-space: nowrap;"><input id="oufld4" type="text" class="input_text" value="" name="ouLabel" readonly="true"
									/><input type="button" id="pick_ou" <c:if test="${!hasOuTree}">disabled</c:if> class="partnerbutton" value="..." onclick="popupShowTree('<c:out value="${popMsg}"/>', this, 'ouPicker', 'oufld4', 'oufld5');"/></span>
                               <input id="oufld5" type="hidden" name="<%=BaseAreaController.POSITION_ORG_UNIT_ID%>" value=""/>
                           </td>
                        </tr>

                        <tr>
                            <td class="infobutton"/>
                            <td class="infobutton">
                                <input class="inlinebutton" name="<%=BaseAreaController.POSITION_SEARCH%>" type="submit" value="<fmt:message key="search"/>" onclick="setSearchStarted('_add');"/>
                            </td>
                        </tr>
                    </table>

                    <c:set var="positions" value="${command.positions}"/>
                    <%-- Only include header if there has been a search --%>
                    <%
                        String val = request.getParameter(BaseAreaController.POSITION_SEARCH);
                        if (val != null) {
                    %>
                        <%-- Only include search results if there are any --%>
                        <c:choose>

                            <c:when test="${!empty positions}">
                                <table class="infotable" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th><fmt:message key="positions"/></th>
                                            <th><fmt:message key="area.positions.excluded"/></th>
                                        </tr>
                                    </thead>
                                    <c:forEach var="posAreaElement" items="${positions}"  varStatus="list">
                                        <c:set var="position" value="${posAreaElement.node}"/>
                                        <tr>
                                            <td>
                                                <spring:bind path="command.positionIds">
                                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${position.id}"/>"/>
                                                    &nbsp;
                                                    <c:out value="${position.label}"/>
                                                </spring:bind>
                                            </td>
                                            <td>
                                                <spring:bind path="command.positions[${list.index}].excluded">
                                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${posAreaElement.excluded}">checked</c:if>/>
                                                </spring:bind>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>

                            <c:otherwise>
                                <span><fmt:message key="area.nopositionsfound"/></span>
                            </c:otherwise>

                        </c:choose>
                    <% } %>
                </td>
            </tr>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '1', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target3" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

