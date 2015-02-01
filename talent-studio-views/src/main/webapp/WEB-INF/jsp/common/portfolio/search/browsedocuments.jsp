<%@ page import="com.zynap.talentstudio.web.common.ParameterConstants"%>

<c:set var="item" value="${command.itemWrapper.item}"/>

<zynap:saveUrl/>

<fmt:message key="date.format" var="datePattern"/>

<c:choose>
    <c:when test="${item == null}">
        <fmt:message key="portfolio.items" var="noresultsmsg"/>
        <zynap:infobox title="${noresultsmsg}" id="docitem">
            <div class="infomessage" id="no_portfolio_item"><fmt:message key="portfolioitem.removed"/></div>
        </zynap:infobox>
    </c:when>
    <c:otherwise>

        <c:set var="BrowseType" value="Portfolio Items" />
        <%@include file="../../nav/browsebuttons.jsp"%>

        <fmt:message key="portfolioitem.title" var="msg"/>
        <zynap:infobox title="${msg}" id="docitem">

            <%-- Form that contains hidden fields used by browse buttons --%>
            <zynap:form  method="post" name="navigation" >
                <input id="hidden_node_id" type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${artefact.id}"/>"/>
                <input id="nodeTargetId" type="hidden" name="nodeTarget" value=""/>
            </zynap:form>

            <table class="infotable">

                <%-- determine node type --%>
                <c:set var="node" value="${item.node}"/>
                <c:set var="isPosition" value="${node.nodeType == 'P'}"/>

                <%-- set artefact type label and urls based on node type --%>
                <fmt:message key="subject" var="artefactTypeLabel"/>
                <c:set var="url" value="viewsubject.htm"/>
                <c:set var="fileUrl" value="viewsubjectportfoliofile.htm"/>
                <c:if test="${isPosition}">
                    <fmt:message key="position" var="artefactTypeLabel"/>
                    <c:set var="url" value="viewposition.htm"/>
                    <c:set var="fileUrl" value="viewpositionportfoliofile.htm"/>
                </c:if>

                <%-- build up artefact url --%>
                <zynap:historyLink var="artefactUrl" url="${url}">
                    <zynap:param name="command.node.id" value="${node.id}"/>
                    <zynap:param name="_disableDelete" value="_saveCommand_"/>
                </zynap:historyLink>

                <%-- build up portfolio item download url --%>
                <zynap:historyLink var="downloadUrl" url="${fileUrl}">
                    <zynap:param name="i_id" value="${item.id}"/>
                    <zynap:param name="command.node.id" value="${item.node.id}"/>
                    <zynap:param name="_disableDelete" value="_saveCommand_"/>
                    <zynap:param name="doc_type" value="${item.scope}"/>
                </zynap:historyLink>

                <tr>
                    <td class="infolabel"><c:out value="${artefactTypeLabel}"/>&nbsp;:&nbsp;</td>
                    <td class="infodata">
                        <c:choose>
                            <c:when test="${node.hasAccess}">
                                <a href="<c:out value="${artefactUrl}"/>">
                                    <c:out value="${node.label}"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${node.label}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>

                <tr>
                    <td class="infolabel"><fmt:message key="item.title"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><c:out value="${item.label}"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="content.type"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><c:out value="${item.contentType.label}"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="content.subtype"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><fmt:message key="content.subtype.${item.contentSubType}"/></td>                    
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="item.lastupdated"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><fmt:formatDate value="${item.lastModified}" pattern="${datePattern} HH:mm:ss"/></td>
                </tr>
                <tr>
                    <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
                    <td class="infodata"><zynap:desc><c:out value="${item.comments}"/></zynap:desc></td>
                </tr>
                <%-- Content types --%>
                <c:if test="${item.upload}">
                    <tr>
                        <td class="infolabel"><fmt:message key="uploaded.file.name"/>&nbsp;:&nbsp;</td>
                        <td class="infodata">
                            <a href="<c:out value="${downloadUrl}"/>" target="_blank"><c:out value="${item.origFileName}"/></a>
                        </td>
                    </tr>
                </c:if>
                <c:if test="${item.text}">
                    <tr>
                        <td valign="top" class="infolabel"><fmt:message key="textual.info"/>&nbsp;:&nbsp;</td>
                        <td class="infodata"><zynap:desc><c:out value="${command.itemWrapper.textContent}"/></zynap:desc></td>
                    </tr>
                </c:if>
                <c:if test="${item.URL}">
                    <tr class="infodata">
                        <td class="infolabel"><fmt:message key="url.value"/>&nbsp;:&nbsp;</td>
                        <td><a href="<c:out value="${command.itemWrapper.url}"/>" target="_blank"><c:out value="${command.itemWrapper.url}"/></a></td>
                    </tr>
                </c:if>
            </table>
        </zynap:infobox>
    </c:otherwise>
</c:choose>

