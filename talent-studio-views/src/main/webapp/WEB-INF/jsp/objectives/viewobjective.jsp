<%@ include file="../includes/include.jsp"%>

<zynap:infobox id="objectivesBoxId" title="${objectivesMsg}">
    
    <table class="infotable" cellspacing="0" cellpadding="0">
        <tr>
            <td class="infolabel">
                <fmt:message key="${objective.parent.objectiveSet.type}"/>&nbsp;:&nbsp;
            </td>
            <td class="infodata">                
                <c:out value="${objective.parent.label}"/>
            </td>
        </tr>
        <!-- The objective label and description -->
        <tr>
            <td class="infolabel"><fmt:message key="title"/>&nbsp;:&nbsp;*</td>
            <td class="infodata">
                <c:out value="${objective.label}"/>
            </td>
        </tr>
        <tr>
            <td class="infolabel"><fmt:message key="generic.description"/>&nbsp;:&nbsp;</td>
            <td class="infodata">
                <div class="blogContent" id="descX<c:out value="${objectiveIndex}"/>">
                    <c:out value="${objective.description}"/>
                </div>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table class="infotable" cellspacing="0" cellpadding="0">
                    <tr>
                        <c:forEach var="heading" items="${objective.wrappedDynamicAttributes}" varStatus="headingIndex">
                            <td class="infolabel"><c:out value="${heading.label}"/></td>
                        </c:forEach>
                    </tr>
                    <tr>
                        <c:forEach var="attr" items="${objective.wrappedDynamicAttributes}" varStatus="attrIndex">
                            <td class="infodata">
                                <c:set var="attrType" value="${attr.attributeDefinition.type}"/>
                                <c:set var="criteria" value="${attr}"/>
                                <c:set var="nodeId" value="${objective.id}"/>
                                <c:set var="attrValueId" value="${attr.attributeValue.id}"/>
                                <%@include file="../common/attributes/viewattributesnippet.jsp"%>
                            </td>
                        </c:forEach>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="infoheading" colspan="2"><fmt:message key="staff.manager.comments"/></td>
        </tr>
        <tr>
            <td class="infolabel">&nbsp;</td>
            <td class="infodata">
                <div class="blogContent" id="btnMgrBlogX<c:out value="${objectiveIndex}"/>">
                    <c:out value="${objective.managerComments}"/>
                </div>
            </td>
        </tr>
    </table>

</zynap:infobox>