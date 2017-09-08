<%@ include file="../includes/include.jsp" %>

<div class="row full-width arena-nav-row">
   <div class="medium-2 column">
   	    <c:if test="${nav != null}">
            <span id="showHideId" class="nav_manipulation" onclick="showHideNav()">
                <a class="nav" href="javascript:void(0)"><fmt:message key="show.hide.nav"/></a>
            </span>
        </c:if>
   </div>
   <div class="medium-10 column" style="background-color: #589AFF;">     
        <c:if test="${userPrincipal != null}">  	
            <ul id="arenatabs" class="tabs arena_tabbar" data-tabs>
                <c:forEach var="arena" items="${arenaz}">
                    <c:url var="url" value="${arena.url}"><c:param name="a" value="${arena.arenaId}"/><c:param name="a_tab" value="${arena.arenaId}"/></c:url>
                    <li id="<c:out value="arena.arenaId"/>" 
                        class="arena_tab tabs-title no_hover 
                        <c:choose><c:when test="${currentArenaId == arena.arenaId}">arena_active_tab tab_active_tab</c:when><c:otherwise>arena_inactive_tab tab_inactive_tab</c:otherwise></c:choose>" 
                        onmouseover="swapStyle(this, 'no_hover', 'hover');" 
                        onmouseout="swapStyle(this, 'hover', 'no_hover');">
                        <a class="tab" id="a_<c:out value="arena.arenaId"/>" href="<c:out value="${url}"/>" <c:if test="${currentArenaId == arena.arenaId}">aria-selected="true"</c:if>><c:out value="${arena.label}"/></a>
                    </li>
                </c:forEach>
                          
             </ul>    
        </c:if>
   </div>
</div> 
