<%@ page import="com.zynap.talentstudio.web.reportingchart.settings.ChartSettingsMultiController"%>
<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_add" method="get" action="addchartsettings.htm">
            <input type="hidden" name="<%=ChartSettingsMultiController.TYPE_KEY%>" value="<c:out value="${model.preferenceType}"/>"/>
            <input type="submit" name="_add" value="<fmt:message key="add"/>" class="actionbutton"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="generic.name" var="headerlabel" />

<zynap:infobox title="${title}">

    <zynap:historyLink var="viewUrl" url="viewchartsettings.htm"/>
    <c:url var="pageUrl" value="listchartsettings.htm"/>

    <display:table name="${model.preferences}" id="preference" sort="list" requestURI="${pageUrl}" pagesize="25" class="pager" excludedParams="*" defaultsort="1">
        <display:column property="viewName" title="${headerlabel}" href="${viewUrl}" paramId="<%=ChartSettingsMultiController.PREFERENCE_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
    </display:table>
</zynap:infobox>