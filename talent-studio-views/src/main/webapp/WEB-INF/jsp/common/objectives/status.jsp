<div class="infomessage">
    <c:choose>
        <c:when test="${artefact.pending}">
            <c:if test="${artefact.personalView}">
                <c:choose>
                    <c:when test="${artefact.individualPending}">
                        <img src="<c:url value="/images/objectives/green.gif"/>" alt="open..."/>
                        <fmt:message key="objectives.individualview.requesting.review"/>
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value="/images/objectives/amber.gif"/>" alt="pending..."/>
                        <fmt:message key="objectives.individualview.awaiting.approval"/>
                    </c:otherwise>
                </c:choose>
            </c:if>
            <c:if test="${!artefact.personalView}">
                <c:choose>
                    <c:when test="${artefact.managerPending}">
                        <img src="<c:url value="/images/objectives/green.gif"/>" alt="open..."/>
                        <fmt:message key="objectives.managerview.requesting.approval"/>
                    </c:when>
                    <c:otherwise>
                        <img src="<c:url value="/images/objectives/amber.gif"/>" alt="pending..."/>
                        <fmt:message key="objectives.managerview.awaiting.review"/>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:when>
        <c:when test="${artefact.open}">
            <img src="<c:url value="/images/objectives/nocolor.gif"/>" alt="open..."/>
            <fmt:message key="objectives.globalview.open"/>
        </c:when>
        <c:when test="${artefact.approved && !artefact.complete}">
            <img src="<c:url value="/images/objectives/red.gif"/>" alt="approved..."/>
            <fmt:message key="objectives.globalview.approved"/>            
        </c:when>
        <c:when test="${artefact.complete}">
            <img src="<c:url value="/images/objectives/red.gif"/>" alt="approved..."/>
            <fmt:message key="objectives.globalview.complete"/>
         </c:when>
        <c:otherwise>
            <c:if test="${artefact.personalView}">
                <img src="<c:url value="/images/objectives/nocolor.gif"/>" alt="pending..."/>
                <fmt:message key="objectives.individualview.no.currentobjectives"/>
            </c:if>
            <c:if test="${!artefact.personalView}">
                <img src="<c:url value="/images/objectives/nocolor.gif"/>" alt="pending..."/>
                <fmt:message key="objectives.managerview.no.currentobjectives"/>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>