<%@ include file="../../../includes/include.jsp" %>

<c:set var="node" value="${model.item.node}"/>

<fmt:message key="portfolioitem.label" var="title">
    <fmt:param value="${node.label}"/>
</fmt:message>
<c:choose>
    <c:when test="${model.item.writePermission}">
        <%-- Only include edit and delete links if controller says to do so --%>
        <zynap:actionbox>
            <zynap:actionEntry>
                <fmt:message key="back" var="buttonLabel" />
                <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
            </zynap:actionEntry>

  
         <zynap:actionEntry>
            <zynap:form action="${model.editView}" method="get" name="_edit">
                <input type="hidden" name="<%= ParameterConstants.ITEM_ID %>" value="<c:out value="${model.item.id}"/>"/>
                <input type="hidden" name="<%= ParameterConstants.NODE_ID_PARAM %>" value="<c:out value="${node.id}"/>"/>
                <input type="hidden" name="<%= ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>
                <input class="actionbutton" type="button" name="edit" value="<fmt:message key="edit"/>" onclick="document.forms._edit.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
        <zynap:actionEntry>
            <zynap:form action="${model.deleteView}" method="get" name="_delete">
                <input type="hidden" name="<%= ParameterConstants.ITEM_ID %>" value="<c:out value="${model.item.id}"/>"/>
                <input type="hidden" name="<%= ParameterConstants.NODE_ID_PARAM %>" value="<c:out value="${node.id}"/>"/>
                <input type="hidden" name="<%=ParameterConstants.DISABLE_COMMAND_DELETION%>" value="<%=ParameterConstants.SAVE_COMMAND%>"/>
                <input class="actionbutton" type="button" name="_delete" value="<fmt:message key="delete"/>" onclick="document.forms._delete.submit();"/>
            </zynap:form>
        </zynap:actionEntry>
    </zynap:actionbox>
 </c:when>
    <c:otherwise>
        <zynap:evalBack>
            <zynap:actionbox>
                <zynap:actionEntry>
                    <fmt:message key="back" var="buttonLabel" />
                    <zynap:backButton label="${buttonLabel}" cssClass="actionbutton"/>
                </zynap:actionEntry>
            </zynap:actionbox>
        </zynap:evalBack>
    </c:otherwise>
</c:choose>

<zynap:infobox title="${title}">
    <fmt:message key="date.format" var="datePattern"/>
    <table height="100%" class="infotable" cellspacing="0">
        <tr>
            <td class="infolabel"><fmt:message key="item.title"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:out value="${model.item.label}"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="content.type"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <c:out value="${model.item.contentType.label}"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="content.subtype"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <fmt:message key="content.subtype.${model.item.contentSubType}"/>
            </td>
        </tr>     
        <tr>
            <td class="infolabel"><fmt:message key="item.lastupdated"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <fmt:formatDate value="${model.item.lastModified}" pattern="${datePattern} HH:mm"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.comments"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <zynap:desc><c:out value="${model.item.comments}"/></zynap:desc>
            </td>
        </tr>
            <%-- Content types --%>
        <c:if test="${model.item.upload}">
            <tr>
                <td class="infolabel"><fmt:message key="uploaded.file.name"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${node.nodeType == 'P'}">
                            <a href="<c:url value="viewpositionportfoliofile.htm"><c:param name="i_id" value="${model.item.id}"/><c:param name="command.node.id" value="${node.id}"/></c:url>" target="_blank">
                                <c:out value="${model.item.origFileName}"/>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value="viewsubjectportfoliofile.htm"><c:param name="i_id" value="${model.item.id}"/><c:param name="command.node.id" value="${node.id}"/></c:url>" target="_blank">
                                <c:out value="${model.item.origFileName}"/>
                            </a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:if>
        <c:if test="${model.item.text}">
            <tr>
                <td valign="top" class="infolabel"><fmt:message key="textual.info"/>&nbsp;:&nbsp;</td>
                <td class="infodata">
                    <zynap:desc><c:out value="${model.item.textContent}"/></zynap:desc>
                </td>
            </tr>
        </c:if>
        <c:if test="${model.item.URL}">
            <tr class="infodata">
                <td class="infolabel"><fmt:message key="url.value"/>&nbsp;:&nbsp;</td>
                <td>
                    <a href="<c:out value="${model.item.url}"/>" target="_blank"><c:out value="${model.item.url}"/></a>                    
                </td>
            </tr>
        </c:if>          
    </table>
</zynap:infobox>
