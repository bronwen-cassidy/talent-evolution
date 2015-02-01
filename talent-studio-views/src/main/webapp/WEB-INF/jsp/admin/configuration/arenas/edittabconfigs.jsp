<%@ include file="../../../includes/include.jsp" %>

<c:url var="url" value="/admin/edittabsettings.htm"/>

<zynap:tab styleSheetClass="tab" url="${url}">


    <zynap:infobox title="${title}">

        <table class="infotable" cellspacing="0">
            <zynap:form method="post" action="${url}">
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

                                <select name="tabItems[<c:out value='${row.index}'/>].selectTabIds[<c:out value='${cell.index}'/>]">
                                    <c:forEach var="configItem" items="${viewConfig.displayConfigItems}">
                                        <c:set var="isSelectedOption" value=""/>
                                        <c:forEach var="configItemSelected" items="${tab.configItems}">
                                            <c:if test="${configItemSelected.displayConfigItem.id==configItem.id}">
                                                <c:set var="isSelectedOption" value="selected"/>
                                            </c:if>
                                        </c:forEach>
                                        <option value="<c:out value="${configItem.id}"/>" <c:out value="${isSelectedOption}"/>>
                                            <c:out value="${configItem.label}"/></option>
                                    </c:forEach>

                                </select>

                            </td>

                        </c:forEach>
                        <c:if test="${not tab.homeArena}">
                            <td class="infolabel"></td>
                        </c:if>
                    </tr>
                </c:forEach>
                <tr>
                    <td class="infolabel" colspan="4">
                        <input type="submit" name="_cancel" class="inlinebutton" value="<fmt:message key="cancel"/>"/>&nbsp;&nbsp;
                        <input type="submit" name="_save" class="inlinebutton" value="<fmt:message key="save"/>"/>&nbsp;&nbsp;

                    </td>
                </tr>
            </zynap:form>
        </table>

    </zynap:infobox>
</zynap:tab>
