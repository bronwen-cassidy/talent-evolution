<%@ include file="../../includes/include.jsp" %>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form method="get" name="back" action="/admin/listhomepages.htm">
            <input class="actionbutton" id="add" name="listHPages" type="button" value="<fmt:message key="back"/>" onclick="javascript:document.forms.back.submit();"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message var="msg" key="home.pages">
    <fmt:param value="${group.label}"/>
</fmt:message>

<zynap:infobox title="${msg}">

    <table class="infotable" cellpadding="0" cellspacing="0">
        <c:forEach items="${homePages}" var="page">
            <tr>
                <td class="infolabel"><c:out value="${page.arenaLabel}"/></td>
                <td class="infodata">
                    <c:choose>
                        <c:when test="${page.hasUpload}">
                            <fmt:message key="uploaded.file.name"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${page.url}"/>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</zynap:infobox>