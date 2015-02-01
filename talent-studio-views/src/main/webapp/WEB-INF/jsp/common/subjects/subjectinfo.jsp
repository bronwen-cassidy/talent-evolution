<%@ include file="../../includes/include.jsp" %>

<c:set var="BrowseType" value="Person"/>
<%@ include file="../nav/browsebuttons.jsp" %>

<c:choose>
<c:when test="${artefact == null}">
    <fmt:message key="subject.information" var="msg"/>
    <zynap:infobox title="${msg}" id="results">
        <div class="infomessage">
            <fmt:message key="no.subjects"/>
        </div>
    </zynap:infobox>
</c:when>
<c:otherwise>
<c:set var="displayConfigView" value="${command.contentView}"/>
<table class="artefact">
<tr>
    <th class="artefact">
        <c:set var="imageUrl" value="/image/viewsubjectimage.htm"/>
        <%@ include file="../subjectpictureview.jsp" %>
    </th>
</tr>

<tr>
<td class="artefact">
<input type="hidden" id="artefct_id" value="<c:out value="${artefact.id}"/>"/>
<zynap:tab defaultTab="${command.activeTab}" id="subj_info" url="javascript" tabParamName="activeTab">

<c:forEach var="tabViews" items="${displayConfigView.viewDisplayConfigItems}">
    <zynap:tabName value="${tabViews.label}" name="${tabViews.key}"/>
</c:forEach>

<c:forEach var="tabContent" items="${displayConfigView.viewDisplayConfigItems}">
    <c:set value="${tabContent}" var="tabContent" scope="request"/>
    <div id="<c:out value="${tabContent.key}_span"/>" style="display:<c:choose><c:when test="${command.activeTab == tabContent.key}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">

        <c:choose>

            <c:when test="${tabContent.association}">
                <c:set var="editassocurl" value="editsubjectassociations.htm" scope="request"/>
                <c:set var="invert" value="false" scope="request"/>
                <c:set var="tbId" value="${tabContent.key}" scope="request"/>

                <%@ include file="../associations/associationheader.jsp" %>

                <c:forEach var="reportItem" items="${tabContent.reportItems}" varStatus="loopStatus">

                    <c:if test="${reportItem.subjectPrimaryAssociation}">
                        <c:set var="associations" value="${artefact.subjectPrimaryAssociations}" scope="request"/>
                        <c:set var="linkParam" value="primary" scope="request"/>
                        <c:set var="tbId" value="${tabContent.key}_pp" scope="request"/>
                    </c:if>

                    <c:if test="${reportItem.subjectSecondaryAssociation}">
                        <c:set var="associations" value="${artefact.subjectSecondaryAssociations}" scope="request"/>
                        <c:set var="linkParam" value="secondary" scope="request"/>
                        <c:set var="tbId" value="${tabContent.key}_ps" scope="request"/>
                    </c:if>
                    <c:set var="imageUrl" value="/image/viewpositionimage.htm" scope="request"/>
                    <%@ include file="../associations/associations.jsp" %>

                    <c:if test="${not loopStatus.last}">
                        <br/>
                    </c:if>
                </c:forEach>
            </c:when>

            <c:when test="${tabContent.user}">
                <c:choose>
                    <c:when test="${artefact.canLogIn}">
                        <c:set var="user" value="${artefact.userWrapper}" scope="request"/>
                        <c:set var="userId" value="${user.id}" scope="request"/>
                        <c:set var="userHeading" value="${tabContent.label}" scope="request"/>
                        <c:url var="cancelUrl" value="browsesubject.htm" scope="request"/>
                        <%@ include file="../users/browseuserbuttons.jsp" %>
                      
                        <c:set var="securityDomains" value="${artefact.securityDomains}"/>
                        <%@ include file="../users/userdetails.jsp" %>
                    </c:when>
                    <c:otherwise>
                        <%@ include file="../users/adduserbuttons.jsp" %>
                        <%@ include file="../users/nouserdetails.jsp" %>
                    </c:otherwise>
                </c:choose>
            </c:when>

            <c:when test="${tabContent.progressReports}">
                <input type="hidden" id="prpts" name="prrplnme" value="<c:out value="${tabContent.key}"/>"/>
                <script type="text/javascript">
                    $(function() {
                        var elemId = $('#artefct_id').val();
                        var acttb = $('#prpts').val();
                        $.get('browseviewsubjectprogress.htm', {'command.node.id': elemId, activeTab: acttb}, function(data) {
                                $('#sbj_progress_reps').html(data);
                        });
                    });
                </script>
                
                <div id="sbj_progress_reps">
                    <span class="loading">                        
                        <img align="middle" src="<c:url value="/images/icons/ajax-loader.gif"/>" alt="loading"/>
                    </span>
                </div>
            </c:when>

            <c:when test="${tabContent.portfolio}">
                <input type="hidden" id="prtfol" name="prtfolnme" value="<c:out value="${tabContent.key}"/>"/>
                <script type="text/javascript">
                    $(function() {
                        var elemId = $('#artefct_id').val();
                        var acttb = $('#prtfol').val();
                        $.get('browseviewsubjectportfolio.htm', {'command.node.id': elemId, activeTab: acttb}, function(data) {
                                $('#sbj_portfolio').html(data);
                        });
                    });
                </script>

                <%-- todo we will put this into a div with an id then we will load this using ajax --%>
                <div id="sbj_portfolio">
                    <span class="loading">
                        <img align="middle" src="<c:url value="/images/icons/ajax-loader.gif"/>" alt="loading"/>
                    </span>
                </div>
            </c:when>

            <c:when test="${tabContent.personReports}">
                <c:import url="subjects/viewappraisalreports.jsp"/>
            </c:when>

            <c:when test="${tabContent.dashboard}">
                <c:set var="dashboardHeading" value="${tabContent.label}" scope="request"/>
                <c:url var="questionnaireUrl" value="viewsubjectquestionnaire.htm" scope="request"/>
                <c:set var="positionUrl" value="viewposition.htm" scope="request"/>
                <c:set var="subjectUrl" value="" scope="request"/>
                <c:import url="subjects/viewsubjectdashboard.jsp"/>
            </c:when>

            <c:when test="${tabContent.objectives}">
                <c:url var="baseViewUrl" value="viewsubjectobjective.htm"/>
                <c:set var="activeTab" value="${tabContent.key}" scope="request"/>
                <c:set var="objectives" value="${artefact.completedObjectives}" scope="request"/>

                <fmt:message key="objectives" var="boxtitle" scope="request"/>

                <c:choose>
                    <c:when test="${command.objectivesModifiable}">
                        <%-- only modifiable in the my team view and only open in progress etc available in myteam my details view --%>
                        <c:set var="objectives" value="${artefact.currentObjectives}" scope="request"/>
                        <c:url var="baseViewUrl" value="worklistviewobjective.htm" scope="request"/>
                        <%@include file="../objectives/modifybuttons.jsp" %>
                        <c:if test="${artefact.hasPublishedObjectives}">
                            <%@include file="../objectives/status.jsp" %>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <%-- the objectives are not modifiable only in the search, browse subject views --%>
                        <%-- include only the view buttons conditions would be always for archived, and for current completed sets --%>
                        <%@include file="../objectives/viewobjectivesbutton.jsp" %>
                    </c:otherwise>
                </c:choose>

                <%@ include file="../objectives/viewobjectives.jsp" %>
            </c:when>

            <c:otherwise>
                <c:if test="${tabContent.hasItemReports}">
                    <c:forEach var="reportItem" items="${tabContent.reportItems}">
                        <c:set var="editsurl" value="editsubject.htm" scope="request"/>
                        <c:set var="imageUrl" value="/image/viewsubjectimage.htm" scope="request"/>
                        <%@ include file="../details.jsp" %>
                    </c:forEach>
                </c:if>
            </c:otherwise>

        </c:choose>

    </div>
</c:forEach>

</zynap:tab>

    <%-- Form that contains hidden fields used by browse buttons --%>
<zynap:form method="post" name="navigation">
    <input id="hidden_node_id" type="hidden" name="<%=ParameterConstants.NODE_ID_PARAM%>" value="<c:out value="${artefact.id}"/>"/>
    <input id="nodeTargetId" type="hidden" name="nodeTarget" value=""/>

    <%-- page number parameter required to maintain correct page for display tag --%>
    <c:set var="pageNumberParameter" value="${command.pageNumberParameter}" scope="request"/>
    <c:if test="${pageNumberParameter != null}">
        <input id="pageNumber_results" type="hidden" name="<c:out value="${pageNumberParameter.key}"/>"
               value="<c:out value="${pageNumberParameter.value}"/>"/>
    </c:if>
</zynap:form>
</td>
</tr>
</table>
</c:otherwise>
</c:choose>
