<%@ include file="../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <fmt:message key="back" var="buttonLabel" />
        <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" action="/admin/addlookupvalue.htm" name="addLookupValue">
            <input type="hidden" name="typeId" value="<c:out value="${model.lookupType.typeId}"/>"/>
            <input type="hidden" name="type_label" value="<c:out value="${model.lookupType.label}"/>"/>
            <input class="actionbutton" type="button" name="addLookupValue" value="<fmt:message key="add.value"/>" onclick="javascript:document.forms.addLookupValue.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
    <zynap:actionEntry>
        <zynap:form method="get" action="/admin/editlookuptype.htm" name="editLookup">
            <input type="hidden" name="typeId" value="<c:out value="${model.lookupType.typeId}"/>"/>
            <input class="actionbutton" type="button" name="editLookup" value="<fmt:message key="edit"/>" onclick="javascript:document.forms.editLookup.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<zynap:infobox title="${title}">
    <table class="infotable" cellspacing="0" width="100%">
        <tr>
            <td class="infolabel"><fmt:message key="generic.name"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><c:out value="${model.lookupType.label}"/></td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><zynap:desc><c:out value="${model.lookupType.description}"/></zynap:desc></td>
        </tr>        
        <tr>
            <td class="infolabel"><fmt:message key="generic.active"/>&nbsp;:&nbsp;</td>
            <td class="infodata"><fmt:message key="${model.lookupType.active}"/></td>
        </tr>
    </table>

    <hr class="searchresults"/>

    <fmt:message key="lookupvalue.label" var="headerlabel" />
    <fmt:message key="generic.active" var="headeractive" />
    <fmt:message key="sort.order" var="headersortorder" />
    <fmt:message key="generic.description" var="headerdescription" />    

    <c:url var="url" value="listlookupvalues.htm"/>
    <zynap:historyLink var="pageUrl" url="viewlookupvalue.htm"/>

    <display:table name="${model.lookupType.lookupValues}" id="lookupValue" sort="list" pagesize="25" requestURI="${url}" class="pager" defaultsort="1">
        <display:column property="label" title="${headerlabel}" href="${pageUrl}" paramId="<%=ParameterConstants.LOOKUP_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="description" title="${headerdescription}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column property="sortOrder" title="${headersortorder}" sortable="true" headerClass="sortable" class="pager"/>
        <display:column title="${headeractive}" headerClass="sorted" class="pager">
            <fmt:message key="${lookupValue.active}"/>
        </display:column>
    </display:table>
</zynap:infobox>
