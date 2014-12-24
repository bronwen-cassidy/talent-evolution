<%@ include file="../../../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants" %>
<script type="text/javascript">	
	$(function() 
    {     	
        $("#portfolioitemtable").tablesorter({widthFixed: true, widgets: ['zebra']})
        .tablesorterPager({container: $("#portfolioitemtablepg"), positionFixed: false, size: 5 });                 
    });    
</script>

<zynap:artefactLink var="viewItemUrl" url="${itemUrl}" tabName="activeTab" activeTab="${portfolioActiveTab}" >
    <zynap:param name="_parameter_save_command_.activeSearchTab" value="browse"/>
    <zynap:param name="command.node.id" value="${artefact.id}"/>
</zynap:artefactLink>

<fmt:message key="item.title" var="headerlabel" />
<fmt:message key="generic.comments" var="headercomments" />
<fmt:message key="content.type" var="headercontenttype" />
<fmt:message key="content.subtype" var="headersubtype" />
<fmt:message key="item.lastupdated" var="headerlastupdated" />
<fmt:message key="portfolio.items" var="headerdocuments" />

<zynap:infobox title="${headerdocuments}" id="portfolio">

    <zynap:actionbox id="actions_portfolio">
        <zynap:actionEntry>
            <fmt:message key="add.item" var="addButtonLabel"/>
            <zynap:artefactForm name="additem" method="get" action="${additemaction}" tabName="activeTab" buttonMessage="${addButtonLabel}" artefactId="${artefact.id}" />
        </zynap:actionEntry>
        <zynap:actionEntry>
            <fmt:message key="document.search" var="searchButtonLabel"/>
            <zynap:artefactForm name="d_search" method="get" action="${searchitemsaction}" tabName="activeTab" buttonMessage="${searchButtonLabel}" artefactId="${artefact.id}" />
        </zynap:actionEntry>
    </zynap:actionbox>    
    <c:if test="${portfolioItems != null && !empty portfolioItems}">
		<span id="portfolioitemtablepg" class="pagelinks">
            <form>
                <img src="../images/icons/first.png" class="first"/>
                <img src="../images/icons/prev.png" class="prev"/>
                <select class="pagesize">
                    <option value="2">2&nbsp;<fmt:message key="num.items"/></option>
                    <option value="5" selected="selected">5&nbsp;<fmt:message key="num.items"/></option>
                    <option value="10">10&nbsp;<fmt:message key="num.items"/></option>
                    <option value="15">15&nbsp;<fmt:message key="num.items"/></option>
                </select>
                <img src="../images/icons/next.png" class="next"/>
                <img src="../images/icons/last.png" class="last"/>
            </form>
            <span><fmt:message key="num.portfolio.items"><fmt:param value="${artefact.numPortfolioItems}"/></fmt:message></span>
		</span>
	</c:if>
    <display:table name="${portfolioItems}" htmlId="portfolioitemtable" id="portfolioitemtable" sort="list" class="pager" excludedParams="*">

        <display:column property="label" title="${headerlabel}" href="${viewItemUrl}"
            paramId="<%=ParameterConstants.ITEM_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager">
        </display:column>

        <display:column property="contentType.label" title="${headercontenttype}" sortable="true" headerClass="sortable" class="pager"/>

        <display:column title="${headersubtype}" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="content.subtype.${portfolioitemtable.contentSubType}"/>
        </display:column>

        <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateColumnDecorator"
            property="lastModified" title="${headerlastupdated}" sortable="true" headerClass="sortable" class="pager"/>

        <display:column property="comments" title="${headercomments}" headerClass="sorted" class="pager" maxWords="5"/>
    </display:table>


</zynap:infobox>

