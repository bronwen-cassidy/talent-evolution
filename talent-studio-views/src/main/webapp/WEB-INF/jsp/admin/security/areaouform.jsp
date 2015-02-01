<%@ page import="com.zynap.talentstudio.web.security.area.BaseAreaController"%>
<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input type="hidden" name="<%=BaseAreaController.SEARCH_STARTED%>" value=""/>
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>

        <table class="infotable" cellspacing="0">
            <c:if test="${!empty command.assignedOrganisationUnits}">
                <thead>
                    <tr>
                        <th><fmt:message key="area.assignedorgunits"/></th>
                        <th><fmt:message key="generic.cascading"/> </th>
                    </tr>
                </thead>
            </c:if>
            <c:forEach var="orgUnitAreaElement" items="${command.assignedOrganisationUnits}" varStatus="listIndex">
                <tr>
                    <td class="infodata">
                        <spring:bind path="command.orgUnitIds">
                            <c:set var="orgUnit" value="${orgUnitAreaElement.node}"/>
                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${orgUnit.id}"/>" checked/>
                            &nbsp;
                            <c:out value="${orgUnit.label}"/>
                        </spring:bind>
                    </td>
                    <td class="infodata">
                        <spring:bind path="command.assignedOrganisationUnits[${listIndex.index}].cascading">
                            <c:set var="orgUnit" value="${orgUnitAreaElement.node}"/>
                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${orgUnitAreaElement.cascading}">checked</c:if>/>
                        </spring:bind>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td class="infolabel"><fmt:message key="area.assign.ous"/>&nbsp;:&nbsp;</td>
                <td class="infodata">

                    <table class="infotable" cellspacing="0">
                        <tr>
                            <td class="infoheading" colspan="2"><fmt:message key="search.parameters"/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="orgunit.search.label"/>&nbsp;:&nbsp;</td>
                            <td class="infodata"><input type="text" class="input_text" name="<%=BaseAreaController.ORG_UNIT_LABEL%>" value=""/></td>
                        </tr>
                        <tr>
                            <td class="infobutton"/>
                            <td class="infobutton">
                                <input class="inlinebutton" name="<%=BaseAreaController.ORG_UNIT_SEARCH%>" type="submit" value="<fmt:message key="search"/>" onclick="javascript:setSearchStarted('_add');"/>
                            </td>
                        </tr>
                    </table>

                    <c:set var="orgUnits" value="${command.organisationUnits}"/>
                    <%
                        String val = request.getParameter(BaseAreaController.ORG_UNIT_SEARCH);
                        if (val != null) {
                    %>
                        <%-- Only include search results if there are any --%>
                        <c:choose>
                            <c:when test="${!empty orgUnits}">
                                <table class="infotable" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th><fmt:message key="organisation.unit"/></th>
                                            <th><fmt:message key="generic.cascading"/> </th>
                                        </tr>
                                    </thead>
                                    <c:forEach var="orgUnitAreaElement" items="${orgUnits}"  varStatus="list">
                                        <c:set var="orgUnit" value="${orgUnitAreaElement.node}"/>
                                        <tr>
                                            <td>
                                                <spring:bind path="command.orgUnitIds">
                                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${orgUnit.id}"/>"/>
                                                    &nbsp;
                                                    <c:out value="${orgUnit.label}"/>
                                                </spring:bind>
                                            </td>
                                            <td>
                                                <spring:bind path="command.organisationUnits[${list.index}].cascading">
                                                    <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" <c:if test="${orgUnitAreaElement.cascading}">checked</c:if>/>
                                                </spring:bind>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </c:when>
                            <c:otherwise>
                                <span><fmt:message key="area.noorgunitsfound"/></span>
                            </c:otherwise>
                        </c:choose>
                    <% } %>
                </td>
            </tr>

            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '0', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_target2" value="<fmt:message key="wizard.next"/>"/>
                </td>
            </tr>
        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
