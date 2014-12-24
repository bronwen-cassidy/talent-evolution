<%@ include file="../../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="backlr" method="get" action="listprogressreports.htm">
           <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="document.forms.backlr.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="editar" method="get" action="editprogressreport.htm">
            <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${model.report.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="edit"/>" onclick="document.forms.editar.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form name="deletepr" method="get" action="deleteprogressreport.htm">
            <input type="hidden" name="<%= ParameterConstants.REPORT_ID %>" value="<c:out value="${model.report.id}"/>"/>
            <input class="actionbutton" type="button" value="<fmt:message key="delete"/>" onclick="document.forms.deletepr.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">

        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.definition"/></td>
                <td class="infodata"><c:out value="${model.report.questionnaireDefinition.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/></td>
                <td class="infodata"><c:out value="${model.report.label}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.description"/></td>
                <td class="infodata"><c:out value="${model.report.description}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="display.last.item"/>&nbsp;:&nbsp;</td>
                <td class="infodata"><fmt:message key="${model.report.lastLineItem}"/></td>
            </tr>
            <tr>
                <td class="infolabel"><fmt:message key="report.population"/></td>
                <td class="infodata"><c:out value="${model.report.defaultPopulation.label}"/></td>
            </tr>
        </table>

        <table class="infotable" cellspacing="0">
            <tr>
                <td>
                    <table class="infotable" cellspacing="0" cellpadding="0">
                        <%-- headers for the columns --%>
                        <tr>
                            <td class="infomessage"><fmt:message key="report.columns.x.axis"/></td>
                        </tr>
                        <c:forEach var="col" items="${model.report.columns}" varStatus="colIndex">
                            <tr>
                                <td class="infodata"><c:out value="${col.label}"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                </td>
                <td valign="top">
                    <table class="infotable" cellspacing="0">
                        <tr>
                            <td class="infomessage"><fmt:message key="report.workflows.y.axis"/></td>
                        </tr>
                        <c:forEach var="wf" items="${model.report.workflows}" varStatus="wfIndex">
                        <tr>                            
                            <td class="infodata"><c:out value="${wf.label}"/></td>
                        </tr>
                        </c:forEach>
                    </table>
                </td>
            </tr>
        </table>


</zynap:infobox>
