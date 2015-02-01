<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms._add.submit();"/>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

   <zynap:link var="pageUrl" url="listcrosstabreports.htm">
        <zynap:param name="<%= ParameterConstants.HAS_RESULTS%>" value="true"/>
    </zynap:link>
   <zynap:link var="runUrl" url="runviewcrosstabreport.htm"/>

   <%@include file="../../displayreports.jsp" %>

</zynap:infobox>

<zynap:form method="get" name="_add" action="/analysis/addcrosstabreport.htm"/>