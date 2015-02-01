<%-- include for common run options for metrics --%>
    <%@ include file="../../reportpopulationselect.jsp"%>
    <%@include file="selectmetric.jsp"%>
    <%@include file="selectformat.jsp"%>
    <%@ include file="../decimalselect.jsp"%>
    <tr>
        <td class="infobutton"/>
        <td class="infobutton">
            <input type="hidden" name="_redisplay" value="_redisplay"/>
            <input class="inlinebutton" type="submit" name="_target0" value="<fmt:message key="run"/>" onclick = "tabLoading('runoptions_span', 'loading');"/>
        </td>
    </tr>
