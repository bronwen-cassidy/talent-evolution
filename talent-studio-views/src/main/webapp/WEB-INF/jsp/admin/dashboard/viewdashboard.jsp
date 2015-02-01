<%@ include file="../../includes/include.jsp" %>

<c:set var="dashboard" value="${model.dashboard}"/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_back" action="listdashboards.htm">
            <input class="actionbutton" name="_back" type="button" value="<fmt:message key="back"/>" onclick="document.forms._back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <c:set var ="frmActionVar" value="editdashboard.htm"/>
        <zynap:form method="get" name="_edit" action="${frmActionVar}">
            <input type="hidden" name="dashboardId" value="<c:out value="${dashboard.id}"/>"/>
            <input class="actionbutton" name="_edit" type="button" value="<fmt:message key="edit"/>" onclick="document.forms._edit.submit();"/>
        </zynap:form>
    </zynap:actionEntry>

    <zynap:actionEntry>
        <zynap:form method="get" name="_delete" action="deletedashboard.htm">
            <fmt:message key="confirm.delete.dashboard" var="confirmDeleteMsg"><fmt:param value="${model.dashboard.label}"/></fmt:message>
            <input type="hidden" name="dashboardId" value="<c:out value="${dashboard.id}"/>"/>
            <input class="actionbutton" name="_delete" type="submit" value="<fmt:message key="delete"/>" onclick="confirmActionAndPost('_delete', '<c:out value="${confirmDeleteMsg}"/>')"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <table class="infotable" cellspacing="0" cellpadding="0">
        <tr>
            <td class="infolabel"><fmt:message key="report.label"/></td>
            <td class="infodata"><c:out value="${dashboard.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="population"/></td>
            <td class="infodata"><c:out value="${dashboard.population.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="dashboard.type"/></td>
            <td class="infodata"><fmt:message key="${dashboard.type}"/></td>
        </tr>
        <c:if test="${dashboard.person}">
            <tr>
                <td class="infoheading"><fmt:message key="access.role"/></td>
                <td class="infoheading"><fmt:message key="groups"/></td>
            </tr>
            <tr>
                <td class="infodata">
                    <c:set var="rolesEmpty" value="${empty dashboard.roles}"/>
                    <c:set var="userRoles" value="${dashboard.roles}"/>
                    <%@include file="../../common/viewroles.jsp"%>
                </td>
                <td class="infodata">
                    <c:set var="groupsEmpty" value="${empty dashboard.groups}"/>
                    <c:set var="groups" value="${dashboard.groups}"/>
                    <%@include file="../../common/viewgroups.jsp"%>
                </td>
            </tr>
        </c:if>
    </table>

    <div class="infomessage">
        <fmt:message key="dashboard.items"/>
    </div>
    <%--the dashboard item information --%>
   <table class="infotable" cellspacing="0" cellpadding="0">
        <tr>
            <td class="infoheading"><fmt:message key="report.label"/></td>
            <td class="infoheading"><fmt:message key="report.description"/></td>
            <td class="infoheading"><fmt:message key="expected.values"/></td>
        </tr>
        <c:forEach var="item" items="${dashboard.dashboardItems}" varStatus="indexer">
            <tr>
                <td class="infodata"><c:out value="${item.label}"/></td>
                <td class="infodata"><c:out value="${item.description}"/></td>
                <td class="infodata">
                    <c:if test="${item.report.chartReport}">
                        <c:forEach var="val" items="${item.dashboardChartValues}">
                            <c:out value="${val.column.label}"/>&nbsp:&nbsp;<c:out value="${val.expectedValue}"/><br/>
                        </c:forEach>
                    </c:if>
                    <c:if test="${not item.report.chartReport}">
                        <fmt:message key="dash"/>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</zynap:infobox>