<%@ include file="../includes/include.jsp"%>

<!-- todo: currently not in use -->
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
            <tr>
                <td colspan="2">
                    <c:set var="attrs" value="${objective.dynamicAttributeValues.values}"/>
                    <table class="infotable">
                        <tr>
                            <c:forEach var="attr1" items="${attrs}" varStatus="headingIndex">
                                <td class="infolabel"><c:out value="${attr1.dynamicAttribute.label}"/></td>
                            </c:forEach>
                        </tr>
                        <tr>
                            <c:forEach var="attr2" items="${attrs}" varStatus="headingIndex">
                                <td class="infodata"><c:out value="${attr2.displayValue}"/></td>
                            </c:forEach>
                        </tr>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </table>
</zynap:infobox>