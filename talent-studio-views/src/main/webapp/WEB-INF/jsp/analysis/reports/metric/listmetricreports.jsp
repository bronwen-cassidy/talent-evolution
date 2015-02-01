<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="document.forms._add.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

   <zynap:link var="pageUrl" url="listmetricreports.htm">
        <zynap:param name="<%= ParameterConstants.HAS_RESULTS%>" value="true"/>
    </zynap:link>
   <zynap:link var="runUrl" url="runviewmetricreport.htm"/>

   <%@include file="../../displayreports.jsp" %>

</zynap:infobox>

<zynap:form method="get" name="_add" action="/analysis/addmetricreport.htm"/>