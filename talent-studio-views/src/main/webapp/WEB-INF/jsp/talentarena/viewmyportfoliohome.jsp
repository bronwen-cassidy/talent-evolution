<%@ include file="../includes/include.jsp" %>
<%@ taglib uri="http://displaytag.sf.net/el" prefix="display" %>

<c:set var="artefact" value="${command.nodeWrapper}" scope="request"/>
<c:set var="displayConfigView" value="${command.contentView}" scope="request"/>
<c:set var="imageUrl" value="/image/mydetailsimageview.htm" scope="request"/>
<c:set var="viewType" value="personnal" scope="request"/>
<c:set var="tabContent" value="${displayConfigView.portfolioView}" scope="request"/>
<c:url var="questionnaireUrl" value="viewmyquestionnaire.htm" scope="request"/>

<table class="artefact">
    <tr>
        <th class="artefact">
            <%@ include file="../common/subjectpictureview.jsp" %>
        </th>
    </tr>
    <tr>
        <td class="artefact">
            <c:set var="portfolioActiveTab" value="${tabContent.key}" scope="request"/>
            <c:import url="myportfolio.jsp"/>
        </td>
    </tr>
</table>

<%----%>