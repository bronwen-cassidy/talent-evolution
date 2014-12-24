<%@ include file="../includes/include.jsp"%>

<zynap:actionbox>
  <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton" method="get"/>
  </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="corporate.goals.msg" var="msg"><fmt:param value="${command.label}"/></fmt:message>
<zynap:infobox title="${msg}">
   
    <table class="infotable">
        <c:forEach var="objective" items="${command.objectives}" varStatus="indexer">
            <tr>
                <td class="infolabel"><fmt:message key="generic.label"/>&nbsp;:&nbsp;</td>
                <td class=""infodata><c:out value="${objective.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><div class="blogContent"><c:out value="${objective.description}"/></div></td>
            </tr>
            <tr><td colspan="2" class="infoheading">&nbsp;</td></tr>
        </c:forEach>
    </table>
</zynap:infobox>