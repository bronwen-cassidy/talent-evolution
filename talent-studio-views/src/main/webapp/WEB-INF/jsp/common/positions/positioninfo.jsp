<%@ page import="static com.zynap.talentstudio.web.common.ParameterConstants.*" %>
<c:set var="BrowseType" value="Position" />
<%@include file="../nav/browsebuttons.jsp"%>

<c:choose>
    <c:when test="${artefact == null}">
        <fmt:message key="position.information" var="msg"/>
        <zynap:infobox title="${msg}" id="posresults">
            <div class="infomessage">
                <fmt:message key="no.positions"/>
            </div>
        </zynap:infobox>
    </c:when>
    <c:otherwise>
        <c:set var="displayConfigView" value="${command.contentView}"/>

        <table class="artefact">
            <tr>
                <th class="artefact">
                    <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                    <%@ include file="../executivesummary.jsp"%>
                </th>
            </tr>

            <tr>
                <td class="artefact">
                    <input type="hidden" id="artefct_id" value="<c:out value="${artefact.id}"/>"/>
                    <zynap:tab defaultTab="${command.activeTab}" id="pos_info" url="javascript" tabParamName="activeTab" >

                    <c:forEach var="tabViews" items="${displayConfigView.viewDisplayConfigItems}">
                        <zynap:tabName value="${tabViews.label}" name="${tabViews.key}"/>
                    </c:forEach>

                    <c:forEach var="tabContent" items="${displayConfigView.viewDisplayConfigItems}" varStatus="arrayIndex" >
                        <c:set value="${tabContent}" var="tabContent" scope="request"/>
                        <div id="<c:out value="${tabContent.key}_span"/>" style="display:<c:choose><c:when test="${command.activeTab == tabContent.key}">inline</c:when><c:otherwise>none</c:otherwise></c:choose>">
                            <c:choose>
                                <c:when test="${tabContent.association}">
                                    <c:set var="editassocurl" value="editpositionassociations.htm"/>
                                    <c:set var="invert" value="false"/>
                                    <c:set var="tbId" value="${tabContent.key}"/>
                                    <!--include the association header only for editing the subjects-->
                                    <c:if test="${!artefact.default}">
                                        <%@ include file="../associations/associationheader.jsp" %>
                                    </c:if>

                                    <c:forEach var="reportItem" items="${tabContent.reportItems}" varStatus="loopStatus">

                                        <c:set var="displayed" value="false"/>

                                        <c:if test="${!artefact.default}">
                                            <c:if test="${reportItem.positionPrimarySourceAssociation}">
                                                <c:set var="associations" value="${artefact.primarySourceAssociations}"/>
                                                <c:set var="linkParam" value="primary"/>
                                                <c:set var="tbId" value="${tabContent.key}_pp"/>
                                                <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                                                <%@ include file="../associations/associations.jsp" %>
                                                <c:set var="displayed" value="true"/>
                                            </c:if>

                                            <c:if test="${reportItem.positionSecondarySourceAssociation}">
                                                <c:set var="associations" value="${artefact.secondarySourceAssociations}"/>
                                                <c:set var="linkParam" value="secondary"/>
                                                <c:set var="tbId" value="${tabContent.key}_ps"/>
                                                <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                                                <%@ include file="../associations/associations.jsp" %>
                                                <c:set var="displayed" value="true"/>
                                            </c:if>
                                        </c:if>

                                        <c:if test="${reportItem.positionPrimaryTargetAssociation}">
                                            <c:set var="associations" value="${artefact.primaryTargetAssociations}"/>
                                            <c:set var="linkParam" value="primary"/>
                                            <c:set var="invert" value="true"/>
                                            <c:set var="tbId" value="${tabContent.key}_pt"/>
                                            <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                                            <%@ include file="../associations/associations.jsp" %>
                                            <c:set var="displayed" value="true"/>
                                        </c:if>

                                        <c:if test="${reportItem.positionSecondaryTargetAssociation}">
                                            <c:set var="associations" value="${artefact.secondaryTargetAssociations}"/>
                                            <c:set var="linkParam" value="secondary"/>
                                            <c:set var="invert" value="true"/>
                                            <c:set var="tbId" value="${tabContent.key}_pst"/>
                                            <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                                            <%@ include file="../associations/associations.jsp" %>
                                            <c:set var="displayed" value="true"/>
                                        </c:if>

                                        <c:if test="${reportItem.subjectPositionPrimaryAssociation}">
                                            <c:if test="${artefact.default}">
                                                <%@ include file="../associations/associationheader.jsp" %>
                                            </c:if>
                                            <c:set var="associations" value="${artefact.subjectPrimaryAssociations}"/>
                                            <c:set var="linkParam" value="primary"/>
                                            <c:set var="tbId" value="${tabContent.key}_spp"/>
                                            <c:set var="imageUrl" value="/image/viewsubjectimage.htm"/>
                                            <%@ include file="../associations/associations.jsp" %>
                                            <c:set var="displayed" value="true"/>
                                        </c:if>

                                        <c:if test="${reportItem.subjectPositionSecondaryAssociation}">
                                            <c:if test="${artefact.default}">
                                                <%@ include file="../associations/associationheader.jsp" %>
                                            </c:if>
                                            <c:set var="associations" value="${artefact.subjectSecondaryAssociations}"/>
                                            <c:set var="linkParam" value="secondary"/>
                                            <c:set var="tbId" value="${tabContent.key}_sps"/>
                                            <c:set var="imageUrl" value="/image/viewsubjectimage.htm"/>
                                            <%@ include file="../associations/associations.jsp" %>
                                            <c:set var="displayed" value="true"/>
                                        </c:if>

                                        <c:if test="${not loopStatus.last && displayed}">
                                            <br/>
                                        </c:if>
                                    </c:forEach>

                                </c:when>

                                <c:when test="${tabContent.portfolio}">
                                    <input type="hidden" id="prtfol" name="prtfolnme" value="<c:out value="${tabContent.key}"/>"/>
                                    <script type="text/javascript">
                                        $(function() {
                                            var elemId = $('#artefct_id').val();
                                            var acttb = $('#prtfol').val();
                                            $.get('browseviewpositionportfolio.htm', {'command.node.id': elemId, activeTab: acttb}, function(data) {
                                                    $('#psn_portfolio').html(data);
                                            });
                                        });
                                    </script>

                                    <div id="psn_portfolio">
                                        <span class="loading">
                                            <img align="middle" src="<c:url value="/images/icons/ajax-loader.gif"/>" alt="loading"/>
                                        </span>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <c:if test="${tabContent.hasItemReports}">
                                        <c:forEach var="reportItem" items="${tabContent.reportItems}" >

                                            <c:set var="editsurl" value="editposition.htm"/>
                                            <c:set var="imageUrl" value="/image/viewpositionimage.htm"/>
                                            <%@ include file="../details.jsp" %>

                                        </c:forEach>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </c:forEach>

                    </zynap:tab>

                    <%-- Form that contains hidden fields used by browse buttons --%>            
                    <zynap:form method="post" name="navigation" >
                        <input id="hidden_node_id" type="hidden" name="<%=NODE_ID_PARAM%>" value="<c:out value="${artefact.id}"/>"/>
                        <input id="nodeTargetId" type="hidden" name="nodeTarget" value=""/>
                            
                        <%-- page number parameter required to maintain correct page for display tag --%>
                        <c:set var="pageNumberParameter" value="${command.pageNumberParameter}"/>
                        <c:if test="${pageNumberParameter != null}">
                            <input id="pageNumber_results" type="hidden" name="<c:out value="${pageNumberParameter.key}"/>" value="<c:out value="${pageNumberParameter.value}"/>"/>
                        </c:if>
                    </zynap:form>
                </td>
            </tr>
        </table>
    </c:otherwise>
</c:choose>
