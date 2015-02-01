<%@ page import="com.zynap.talentstudio.web.perfomance.PerformanceReviewMultiController"%>
<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<zynap:saveUrl/>

<zynap:actionbox>
    <zynap:actionEntry>
        <zynap:form name="_add" method="get" action="addperformancereview.htm">
            <input type="submit" name="_add" value="<fmt:message key="add"/>" class="actionbutton"/>
        </zynap:form>
    </zynap:actionEntry>
</zynap:actionbox>

<fmt:message key="generic.name" var="headerlabel" />
<fmt:message key="questionnaire.status" var="headerstatus" />
<fmt:message key="questionnaire.end.date" var="headerenddate" />

<zynap:infobox title="${title}">

    <c:url var="pageUrl" value="listperformancereviews.htm"/>

    <zynap:link var="viewUrl" url="viewperformancereview.htm"/>

    <display:table name="${model.reviews}" id="review" sort="list" requestURI="${pageUrl}" pagesize="25" class="pager" excludedParams="*" defaultsort="1">

        <display:column property="label" title="${headerlabel}" href="${viewUrl}"
            paramId="<%=PerformanceReviewMultiController.REVIEW_ID%>" paramProperty="id" sortable="true" headerClass="sortable" class="pager"/>

        <display:column decorator="com.zynap.talentstudio.web.utils.displaytag.DateDecorator" property="managerWorkflow.expiryDate" title="${headerenddate}" sortable="true" headerClass="sortable" class="pager"/>

        <display:column title="${headerstatus}" sortProperty="status" sortable="true" headerClass="sortable" class="pager">
            <fmt:message key="${review.status}"/>
        </display:column>

    </display:table>
</zynap:infobox>