<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>

<fmt:message key="admin.add.user.lastname" var="headerlastname" />
<fmt:message key="admin.add.user.firstname" var="headerfirstname" />
<fmt:message key="admin.add.user.givenname" var="headerprefgivenname" />
<fmt:message key="admin.add.user.username" var="headerusername" />
<fmt:message key="group" var="headergroup" />
<fmt:message key="generic.active" var="headeractive" />
<fmt:message key="has.subject" var="headersubject"/>

<display:table name="${model.users}" id="userList" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" defaultsort="1" export="true">
    <display:column property="loginInfo.username" title="${headerusername}" href="${viewUrl}" paramId="<%=ParameterConstants.USER_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
    <display:column property="coreDetail.prefGivenName" title="${headerprefgivenname}" sortable="true" headerClass="sortable" class="pager"/>
    <display:column property="coreDetail.firstName" title="${headerfirstname}" sortable="true" headerClass="sortable" class="pager"/>
    <display:column property="coreDetail.secondName" title="${headerlastname}" sortable="true" headerClass="sortable" class="pager"/>
    <display:column property="group.label" title="${headergroup}" sortable="true" headerClass="sortable" class="pager"/>
    <display:column title="${headersubject}" sortable="true" headerClass="sortable" class="pager">
        <c:choose>
            <c:when test="${userList.hasSubject}">
                <fmt:message key="generic.yes"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="generic.no"/>
            </c:otherwise>
        </c:choose>
    </display:column>
    <display:column title="${headeractive}" sortable="true" headerClass="sortable" class="pager">
        <fmt:message key="${userList.active}"/>
    </display:column>
</display:table>