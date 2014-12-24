<%-- used as include when publishing reports to arenas --%>
<tr>

    <spring:bind path="command">
        <%@include file="../../../includes/error_messages.jsp" %>
    </spring:bind>

    <td class="infolabel">
        <fmt:message key="assigned.arenas"/>&nbsp;:&nbsp;
    </td>
    <td class="infodata">
        <input class="inlinebutton" type="button" name="SetAll" value="<fmt:message key="setall"/>" onClick="checkAll(document.reports.activeMenuItems);checkAll(document.reports.homePageMenuItems)"/>
        <input class="inlinebutton" type="button" name="ClearAll" value="<fmt:message key="clearall"/>" onClick="uncheckAll(document.reports.activeMenuItems);uncheckAll(document.reports.homePageMenuItems)"/>

        <table class="infotable" id="publisharenas" cellspacing="0">
            <tr>
                <th class="infolabel"><fmt:message key="generic.arena"/></th>
                <th class="infolabel"><fmt:message key="report.addtoarena"/></th>
                <th class="infolabel"><fmt:message key="report.addtofavourites"/></th>
            </tr>

            <c:forEach var="menuItemWrapper" items="${command.menuItemWrappers}" varStatus="loopStatus">
                <tr>
                    <spring:bind path="command.activeMenuItems">
                        <td class="infolabel">
                            <c:out value="${menuItemWrapper.label}"/>
                        </td>
                        <td class="infodata">
                            <input type="checkbox" class="input_checkbox"
                                id="ms_<c:out value="${loopStatus.index}"/>"
                                name="<c:out value="${status.expression}"/>"
                                value="<c:out value="${loopStatus.index}"/>"
                                <c:if test="${menuItemWrapper.selected}">checked</c:if> />
                        </td>
                    </spring:bind>

                    <c:if test="${menuItemWrapper.supportsHomePage}">
                        <spring:bind path="command.homePageMenuItems">
                            <td class="infodata">
                                <input type="checkbox" class="input_checkbox"
                                    id="fav_<c:out value="${loopStatus.index}"/>"
                                    name="<c:out value="${status.expression}"/>"
                                    value="<c:out value="${loopStatus.index}"/>"
                                    <c:if test="${menuItemWrapper.homePage}">checked</c:if> />
                            </td>
                        </spring:bind>
                    </c:if>
                </tr>
            </c:forEach>
            <%-- include a multiselect of groups to assign this report to --%>
            <c:if test="${!empty command.groups}">
                <tr>
                    <td class="infomessage" colspan="2"><fmt:message key="report.groups.info"/></td>
                </tr>
                <tr>
                    <td class="infolabel">
                        <fmt:message key="report.groups"/>&nbsp;:&nbsp;
                    </td>
                    <td class="infodata">
                        <c:set var="selectSize" value="6"/>
                        <c:if test="${command.groupsSize < 6}">
                            <c:set var="selectSize" value="${command.groupsSize}"/>
                        </c:if>
                        <spring:bind path="command.groupIds">
                            <select multiple="true" name="<c:out value="${status.expression}"/>" size="<c:out value="${selectSize}"/>">
                                <c:forEach var="group" items="${command.groups}">
                                    <option value="<c:out value="${group.value.id}"/>" <c:if test="${group.selected}">selected</c:if> ><c:out value="${group.value.label}"/></option>
                                </c:forEach>
                            </select>
                        </spring:bind>
                    </td>
                </tr>
            </c:if>
        </table>
    </td>
</tr>
