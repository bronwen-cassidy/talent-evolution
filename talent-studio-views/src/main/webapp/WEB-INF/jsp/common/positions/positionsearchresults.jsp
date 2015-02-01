<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants, com.zynap.talentstudio.web.history.HistoryHelper"%>

<fmt:message key="admin.position.title" var="headerlabel" />
<fmt:message key="admin.position.active" var="headeractive" />
<fmt:message key="search.orgunit" var="headerorgunit" />
<fmt:message key="current.holder" var="headercurrentholder" />

<fmt:message key="searchresults.position" var="msg"/>
<zynap:infobox title="${msg}" id="results">
    <c:if test="${positions != null}">

        <zynap:artefactLink var="viewPageUrl" url="listposition.htm" tabName="activeTab" activeTab="${command.activeTab}" commandAction="<%=ParameterConstants.UPDATE_COMMAND%>">
            <zynap:param name="_parameter_save_command_.activeSearchTab" value="results"/>

            <%-- parameter that indicates that the user has changed page --%>
            <zynap:param name="pageChange" value="true"/>

            <%-- page number parameter required to maintain correct page for display tag --%>
            <c:set var="pageNumberParameter" value="${command.pageNumberParameter}"/>
            <c:if test="${pageNumberParameter != null}">
                <zynap:param name="${pageNumberParameter.key}" value="${pageNumberParameter.value}"/>
            </c:if>
        </zynap:artefactLink>

        <display:table name="${positions}" id="positiontable" sort="list" pagesize="25" requestURI="${viewPageUrl}" class="pager" excludedParams="*" defaultsort="1">

            <display:column title="${headerlabel}" sortable="true" headerClass="sortable" class="pager" sortProperty="title">
                <a href="#" onclick="javascript:setHiddenFromList(<c:out value="${positiontable.id}"/>, 'hidden_node_id_results', 'navigationResults');"><c:out value="${positiontable.title}"/></a>
            </display:column>

            <display:column property="organisationUnitLabel" title="${headerorgunit}" sortable="true" headerClass="sortable" class="pager"/>

            <display:column property="currentHolderInfo" title="${headercurrentholder}" sortable="true" headerClass="sortable" class="pager"/>

            <display:column title="${headeractive}" sortable="true" headerClass="sortable" class="pager">
                <fmt:message key="${positiontable.active}"/>                
            </display:column>
        </display:table>

        <zynap:form  method="post" name="navigationResults" >
            <input id="hidden_node_id_results" type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>"/>
            <input id="nodeTarget_results" type="hidden" name="nodeTarget" value=""/>

            <%-- page number parameter required to maintain correct page for display tag --%>
            <c:set var="pageNumberParameter" value="${command.pageNumberParameter}"/>
            <c:if test="${pageNumberParameter != null}">
                <input id="pageNumber_results" type="hidden" name="<c:out value="${pageNumberParameter.key}"/>" value="<c:out value="${pageNumberParameter.value}"/>"/>
            </c:if>
        </zynap:form>
    </c:if>
</zynap:infobox>
