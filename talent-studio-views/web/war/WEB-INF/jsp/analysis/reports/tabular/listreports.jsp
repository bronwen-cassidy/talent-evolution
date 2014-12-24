<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_add" action="/analysis/addstandardreport.htm">
            <input class="actionbutton" id="add" name="_add" type="button" value="<fmt:message key="add"/>" onclick="javascript:document.forms._add.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

   <zynap:link var="pageUrl" url="listreports.htm">
        <zynap:param name="<%= ParameterConstants.HAS_RESULTS%>" value="true"/>
    </zynap:link>
   <zynap:link var="runUrl" url="runviewstandardreport.htm"/>

   <%@include file="../../displayreports.jsp" %>

</zynap:infobox>
