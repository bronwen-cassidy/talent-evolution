<%@ include file="../../includes/include.jsp" %>

<tr valign="top" class="noprint">
    <td>
        <div class="arenatabs">
            <c:if test="${nav != null}">
                <span id="showHideId" class="nav_manipulation">
                    <a class="nav" href="#" onclick="showHideNav();"><fmt:message key="show.hide.nav"/></a>
                </span>
            </c:if>
            
            <c:if test="${userPrincipal != null}">
                <span class="arena_nav_span">
                    <zynap:tab id="arenatabs" styleSheetClass="arena" defaultTab="${currentArenaId}" url="${arena.url}" includeBody="false" tabParamName="a_tab">
                        <c:forEach var="arena" items="${arenaz}">
                            <c:url var="url" value="${arena.url}"><c:param name="a" value="${arena.arenaId}"/></c:url>
                            <zynap:tabName name="${arena.arenaId}" value="${arena.label}" specificURL="${url}"/>
                        </c:forEach>
                    </zynap:tab>
                </span>
                
            </c:if>
        </div>
    </td>
</tr>
