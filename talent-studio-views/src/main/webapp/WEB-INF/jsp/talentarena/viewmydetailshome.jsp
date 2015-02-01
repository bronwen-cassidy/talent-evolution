<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:choose>
    <c:when test="${command.errorKey != null}">
        <div class="error" align="center">
            <fmt:message key="${command.errorKey}"/>
        </div>
    </c:when>

    <c:otherwise>
        <c:set var="artefact" value="${command.nodeWrapper}" scope="request"/>
        <c:set var="displayConfigView" value="${command.contentView}" scope="request"/>
        <c:set var="imageUrl" value="/image/mydetailsimageview.htm" scope="request"/>
        <c:set var="viewType" value="personnal" scope="request"/>

        <c:url var="questionnaireUrl" value="viewmyquestionnaire.htm" scope="request"/>

        <table class="artefact">
            <tr>
                <th class="artefact">
                    <%@ include file="../common/subjectpictureview.jsp" %>
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
                            <div id="<c:out value="${tabContent.key}_span"/>"
                                 style="display:<c:choose><c:when test="${command.activeTab == tabContent.key}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                                <c:choose>

                                    <c:when test="${tabContent.association}">
                                        <c:set var="tbId" value="${tabContent.key}" scope="request"/>
                                        <c:set var="invert" value="false" scope="request"/>
                                        <c:forEach var="reportItem" items="${tabContent.reportItems}" varStatus="loopStatus">
                                            <c:set var="displayed" value="false" scope="request"/>

                                            <c:if test="${reportItem.subjectPrimaryAssociation}">
                                                <c:set var="associations" value="${artefact.subjectPrimaryAssociations}" scope="request"/>
                                                <c:set var="linkParam" value="primary" scope="request"/>
                                                <c:set var="tbId" value="${tabContent.key}_pp" scope="request"/>
                                                <c:set var="displayed" value="true" scope="request"/>
                                            </c:if>
                                            <%@ include file="../common/associations/associations.jsp" %>

                                            <c:if test="${not loopStatus.last && displayed}">
                                                <br/>
                                            </c:if>
                                        </c:forEach>
                                    </c:when>

                                    <c:when test="${tabContent.progressReports}">
                                        <input type="hidden" id="prpts" name="prrplnme" value="<c:out value="${tabContent.key}"/>"/>
                                        <script type="text/javascript">
                                            $(function() {
                                                var elemId = $('#artefct_id').val();
                                                var acttb = $('#prpts').val();
                                                $.get('viewmybrowseprogress.htm', {'command.node.id': elemId, activeTab: acttb}, function(data) {
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
                                                $.get('viewmybrowseportfolio.htm', {'command.node.id': elemId, activeTab: acttb}, function(data) {
                                                        $('#my_portfolio').html(data);
                                                });
                                            });
                                        </script>

                                        <div id="my_portfolio">
                                            <span class="loading">
                                                <img align="middle" src="<c:url value="/images/icons/ajax-loader.gif"/>" alt="loading"/>
                                            </span>
                                        </div>
                                    </c:when>

                                    <c:when test="${tabContent.personReports}">
                                        <c:import url="../common/subjects/viewappraisalreports.jsp"/>
                                    </c:when>

                                    <c:when test="${tabContent.dashboard}">
                                        <c:set var="dashboardHeading" value="${tabContent.label}" scope="request"/>
                                        <c:import url="../common/subjects/viewsubjectdashboard.jsp"/>
                                    </c:when>

                                    <c:when test="${tabContent.objectives}">
                                        <c:set var="objectives" value="${artefact.currentObjectives}" scope="request"/>
                                        <fmt:message key="objectives" var="boxtitle" scope="request"/>
                                        <%@ include file="myobjectives.jsp" %>
                                    </c:when>

                                    <c:otherwise>
                                        <c:if test="${tabContent.hasItemReports}">
                                            <c:set var="editsurl" value="editmydetails.htm" scope="request"/>
                                            <c:forEach var="reportItem" items="${tabContent.reportItems}">
                                                <%@ include file="../common/details.jsp" %>
                                            </c:forEach>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>

                    </zynap:tab>

                </td>
            </tr>
        </table>
    </c:otherwise>
</c:choose>