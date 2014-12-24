<%@ include file="../../../includes/include.jsp" %>

<c:url var="url" value="/admin/tablistsettings.htm"/>

<zynap:tab styleSheetClass="tab" url="${url}">


    <zynap:form name="edit" method="get" action="/admin/edittabsettings.htm">

        <zynap:actionbox>
            <zynap:actionEntry>
                <input class="actionbutton" type="submit" name="edit" value="<fmt:message key="edit"/>"/>
            </zynap:actionEntry>
        </zynap:actionbox>

    </zynap:form>
    <zynap:infobox title="${title}">
        <table class="infotable" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="arena.tabs.title"/>&nbsp;:&nbsp;</td>
                <td class="infolabel"><fmt:message key="arena.tabs.position.title"/>&nbsp;:&nbsp;</td>
                <td class="infolabel"><fmt:message key="arena.tabs.subject.title"/>&nbsp;:&nbsp;</td>
                <td class="infolabel"><fmt:message key="arena.tabs.subject.home.title"/>&nbsp;:&nbsp;</td>
            </tr>
            <c:forEach var="tab" items="${command.tabItems}" varStatus="row">
                <tr>
                    <td class="infodata"><c:out value="${tab.label}"/></td>


                    <c:forEach var="viewConfig" items="${tab.displayConfigs}" varStatus="cell">

                        <td class="infolabel">

                            <c:forEach var="configItem" items="${viewConfig.displayConfigItems}">
                                <c:set var="isSelectedOption" value="false"/>
                                <c:forEach var="configItemSelected" items="${tab.configItems}">
                                    <c:if test="${configItemSelected.displayConfigItem.id==configItem.id}">
                                        <c:set var="isSelectedOption" value="true"/>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${isSelectedOption}">
                                    <c:out value="${configItem.label}"/>
                                </c:if>
                            </c:forEach>
                        </td>

                    </c:forEach>
                    <c:if test="${not tab.homeArena}">
                        <td class="infolabel"/>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
    </zynap:infobox>
</zynap:tab>
