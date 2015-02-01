<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="_back" action="/admin/listlookupvalues.htm">
            <input class="actionbutton" type="button" name="_back" value="<fmt:message key="back"/>" onclick="javascript:document.forms._back.submit();"/>
            <input type="hidden" name="typeId" value="<c:out value="${model.typeId}"/>"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" name="_edit" action="/admin/editlookupvalue.htm">
            <input class="actionbutton" type="submit" name="_edit" value="<fmt:message key="edit"/>"/>        
            <input type="hidden" name="<%=ParameterConstants.LOOKUP_ID%>" value="<c:out value="${model.lookupValue.id}"/>"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="lookupvalue" var="msg"/>
<zynap:infobox title="${msg}" >
    <div class="infomessage">
        <fmt:message key="lookupvalue.for">
            <fmt:param value="${model.lookupValue.label}"/>
            <fmt:param value="${model.label}"/>
        </fmt:message>
    </div>

    <table class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="lookupvalue.label"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.lookupValue.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${model.lookupValue.description}"/></zynap:desc></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="sort.order"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.lookupValue.sortOrder}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${model.lookupValue.active}"/></td>
        </tr>
    </table>
</zynap:infobox>