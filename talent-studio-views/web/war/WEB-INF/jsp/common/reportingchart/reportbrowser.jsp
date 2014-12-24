<%@ include file="../../includes/include.jsp" %>

<%@include file="common.jsp" %>

<c:url value="/image/viewsubjectimage.htm" var="picUrl" scope="request"/>

<c:if test="${hasPosition}">
    <zynap:infobox title="${title}">

        <!--reporting chart start -->
        <zynap:reportingChartTag subjectUrl="${subUrl}" orgUrl="${ouUrl}"
                                          positionUrl="${posUrl}" viewUrl="${url}"
                                          target="${command.position}" imageUrl="${picUrl}"/>        
        <!-- report chart end -->
        
    </zynap:infobox>
</c:if>
