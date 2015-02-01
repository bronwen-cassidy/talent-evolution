<%@ page import="com.zynap.talentstudio.web.security.area.BaseAreaController"%>
<%@ include file="../../includes/include.jsp" %>

<fmt:message key="${pagetitle}" var="msg"/>
<zynap:infobox title="${msg}">
    <zynap:form name="_add" method="post">
        <input id="searchS" type="hidden" name="<%=BaseAreaController.SEARCH_STARTED%>" value=""/>
        <input id="backId" type="hidden" name="" value="-1"/>
        <input id="pgTarget" type="hidden" name="" value="-1"/>

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="area.assignedsubjects"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:forEach var="subjAreaElement" items="${command.assignedSubjects}"  varStatus="list">
                        <c:set var="subject" value="${subjAreaElement.node}"/>
                        <spring:bind path="command.subjectIds">
                            <div>
                                <c:out value="${subject.firstName}"/>&nbsp;<c:out value="${subject.secondName}"/>
                                &nbsp;
                                <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${subject.id}"/>" checked/>
                            </div>
                        </spring:bind>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.population"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <spring:bind path="command.subjectPopulationId">
                        <select name="<c:out value="${status.expression}"/>">
                            <option value="" <c:if test="${command.subjectPopulationId == null}">selected</c:if>>&nbsp;</option>
                            <c:forEach var="subPop" items="${populations}">
                                <option value="<c:out value="${subPop.id}"/>" <c:if test="${subPop.id == command.subjectPopulationId}">selected</c:if>><c:out value="${subPop.label}"/></option>
                            </c:forEach>
                        </select>
                    </spring:bind>
                </td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="area.assign.subjects"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <table class="infotable" cellspacing="0">
                        <tr>
                            <td class="infoheading" colspan="2"><fmt:message key="search.parameters"/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="subject.search.firstname"/>&nbsp;:&nbsp;</td>
                            <td class="infodata"><input type="text" class="input_text" name="<%=BaseAreaController.SUBJECT_FIRST_NAME%>" value=""/></td>
                        </tr>
                        <tr>
                            <td class="infolabel"><fmt:message key="subject.search.secondname"/>&nbsp;:&nbsp;</td>
                            <td class="infodata"><input type="text" class="input_text" name="<%=BaseAreaController.SUBJECT_LAST_NAME%>" value=""/></td>
                        </tr>
                        <tr>
                            <td class="infobutton"></td>
                            <td class="infobutton">
                                <input class="inlinebutton" name="<%=BaseAreaController.SUBJECT_SEARCH%>" type="submit" value="<fmt:message key="search"/>" onclick="javascript:setSearchStarted('_add');"/>
                            </td>
                        </tr>
                    </table>

                    <c:set var="subjects" value="${command.subjects}"/>
                    <%-- Only include header if there has been a search --%>
                    <%
                        String val = request.getParameter(BaseAreaController.SUBJECT_SEARCH);
                        if (val != null) {
                    %>
                        <%-- Only include search results if there are any --%>
                        <c:choose>
                            <c:when test="${!empty subjects}">
                                <c:forEach var="subjAreaElement" items="${subjects}"  varStatus="list">
                                    <c:set var="subject" value="${subjAreaElement.node}"/>
                                    <spring:bind path="command.subjectIds">
                                        <div>
                                            <input type="checkbox" class="input_checkbox" name="<c:out value="${status.expression}"/>" value="<c:out value="${subject.id}"/>"/>
                                            &nbsp;
                                            <c:out value="${subject.firstName}"/>&nbsp;<c:out value="${subject.secondName}"/>
                                        </div>
                                    </spring:bind>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <span><fmt:message key="area.nosubjectsfound"/></span>
                            </c:otherwise>
                        </c:choose>
                    <% } %>
                </td>
            </tr>
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="javascript:document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="button" name="_back" value="<fmt:message key="wizard.back"/>" onclick="javascript:handleWizardBack('_add', 'pgTarget', '2', 'backId');"/>
                    <input class="inlinebutton" type="submit" name="_finish" value="<fmt:message key="save"/>"/>
                </td>
            </tr>

        </table>
    </zynap:form>
</zynap:infobox>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>
