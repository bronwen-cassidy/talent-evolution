<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<fmt:message key="${model.artefactType}.arena.attributes.msg" var="title">
    <fmt:param value="${model.defTab}"/>
</fmt:message>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_create" action="addDA.htm">
            <input id="hidden1" type="hidden" name="<%=ParameterConstants.ARTEFACT_TYPE%>" value="<c:out value="${model.artefactType}"/>"/>
            <input id="add1" class="actionbutton" type="button" name="_create" value="<fmt:message key="add"/>" onclick="javascript:document.forms._create.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <c:choose>
        <c:when test="${empty model.das}">
            <div class="infomessage"><fmt:message key="no.attributes"/></div>
        </c:when>
        <c:otherwise>

                <zynap:historyLink var="viewUrl" url="viewDA.htm"/>        

                <fmt:message key="da.general.label" var="headerlabel" />
                <fmt:message key="da.general.type" var="headertype" />
                <fmt:message key="da.general.active" var="headeractive" />
                <fmt:message key="da.general.mandatory" var="headermandatory" />
                <fmt:message key="da.general.searchable" var="headersearchable" />
                <fmt:message key="calculated" var="headercalculated" />

                <display:table name="${model.das}" id="attribute" sort="list" pagesize="25" requestURI="${pageUrl}" class="pager" excludedParams="*" defaultsort="1">

                    <display:column property="label" title="${headerlabel}" href="${viewUrl}"
                        paramId="<%=ParameterConstants.ATTR_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>

                    <display:column title="${headertype}" sortable="true" headerClass="sortable" class="pager">
                        <fmt:message key="${attribute.type}"/>
                    </display:column>

                    <display:column title="${headeractive}" sortable="true" headerClass="sortable" class="pager">
                        <fmt:message key="${attribute.active}"/>
                    </display:column>

                    <display:column title="${headermandatory}" sortable="true" headerClass="sortable" class="pager">
                        <fmt:message key="${attribute.mandatory}"/>
                    </display:column>

                    <display:column title="${headersearchable}" sortable="true" headerClass="sortable" class="pager">
                        <fmt:message key="${attribute.searchable}"/>
                    </display:column>

                    <display:column title="${headercalculated}" sortable="true" headerClass="sortable" class="pager">
                        <fmt:message key="${attribute.calculated}"/>
                    </display:column>
                </display:table>
        </c:otherwise>
    </c:choose>
</zynap:infobox>


