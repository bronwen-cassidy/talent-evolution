<%@ include file="../includes/include.jsp"%>

<fmt:message key="${command.displayConfig.nodeType}" var="nodeType"/>
<fmt:message key="artefact.view.items" var="msg">
    <fmt:param value="${nodeType}"/>
</fmt:message>

<zynap:infobox id="config_items" title="${msg}">

    <table class="infotable" id="artefactviews">
        <thead>
            <tr>
                <th class="infolabel"><fmt:message key="view.label"/></th>
                <th class="infolabel"><fmt:message key="visible"/></th>
            </tr>
        </thead>
        <c:forEach var="item" items="${command.displayConfigItems}" varStatus="index" >
            <tr>
                <td class="pager">
                    <form method="post" action="" name="editI<c:out value="${index.count}"/>">
                        <a id="disI<c:out value="${index.count}"/>" href="#" onclick="javascript:document.forms.editI<c:out value="${index.count}"/>.submit();"><c:out value="${item.label}"/></a>
                        <input type="hidden" id="hiddI<c:out value="${index.count}"/>" name="ic" value="<c:out value="${item.id}"/>"/>
                        <input type="hidden" id="tardI<c:out value="${index.count}"/>" name="_target2" value="2"/>
                    </form>
                </td>
            <td class="infodata">
                    <c:choose>
                        <c:when test="${item.active}">
                            <img alt="checked" src="../images/checked.gif"/>
                        </c:when>
                        <c:otherwise>
                            <img alt="unchecked" src="../images/false.gif"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>

</zynap:infobox>
