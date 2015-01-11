<%@ page import="com.zynap.talentstudio.analysis.populations.IPopulationEngine"%>
<%@ include file="../includes/include.jsp" %>

<fmt:message key="analysis.population.add.0" var="msg"/>

<zynap:form method="post" name="_cancel">
    <input type="hidden" name="_cancel" value="_cancel"/>
</zynap:form>

<zynap:infobox title="${msg}">
    <zynap:form name="population" method="post" encType="multipart/form-data">
       <table class="infotable" cellspacing="0">
            <c:set var="firstPage" value="true"/>
            <%@ include file="populationheaderinputs.jsp" %>
            <tr>
                <td class="infobutton"/>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onClick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" id="addcriteria" type="submit" name="_target1" value="<fmt:message key="wizard.next"/>" />
                 </td>
             </tr>            
       </table>

    </zynap:form>
</zynap:infobox>

