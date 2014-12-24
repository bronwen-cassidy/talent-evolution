<%@ include file="../includes/include.jsp"%>

<table id="assessorTblId" class="infotable" cellspacing="0" cellpadding="0">
    <tr>
        <td class="navigation_no_hover" nowrap>
            <fmt:message key="assign.users.to.all"/>:<br/>
            <fmt:message key="remove.users.from.all"/>:
        </td>
        <td class="infoarea">
            <img src="../images/reportsTo.gif" class="clickable" alt="assign for all" title="assign users to all" width="30" height="20" onclick="assignSelectedToAll('personSelectorId', 'assResultsTbls');"/><br/>
            <img src="../images/fromarrow.gif" class="clickable" alt="remove all" title="remove users from all" width="30" height="20" onclick="removeAllSelections('assResultsTbls', 'personSelectorId');"/>
        </td>
    </tr>
    <tr>
        <td>
            <span><input type="button" class="inlinebutton" name="clrAll1" value="<fmt:message key="clearall"/>" onclick="clearCheckedAssessors('personSelectorId');"/></span>
        </td>
    </tr>
    <tr>
        <!--The td holding the user tree -->
        <td class="navigation" nowrap id="usrselectortd">
            <!-- building the picker tree -->
            <div class="verticalScroll" id="userSelectorDiv">

                <select id="personSelectorId" name="personSelector" multiple="true" size="20">
                    <c:forEach var="person" items="${assessors}" varStatus="personIndex">
                        <option value="<c:out value="${person.id}"/>"><c:out value="${person.displayLabel}"/></option>
                    </c:forEach>
                </select>

            </div>
        </td>
        <td class="infodata">
            <table cellpadding="0" cellspacing="0" class="infotable" id="assResultsTbls">
                <thead>
                    <tr>
                        <th class="sorted">&nbsp;</th>
                        <th class="sorted"><fmt:message key="assigned.assessors"/></th>
                        <th class="sorted"><fmt:message key="objective.description"/></th>
                    </tr>
                </thead>
                <c:forEach var="objective" items="${objectives}" varStatus="indexer">
                    <tr>
                        <td class="infodata">
                            <img src="../images/reportsTo.gif" width="25" class="clickable" alt="add selected items" title="assign selected users" onclick="addSelected('selectedusers<c:out value="${indexer.index}"/>', 'personSelectorId');"/><br/>
                            <img src="../images/fromarrow.gif" width="25" class="clickable" alt="remove selected items" title="remove selected users" onclick="removeSelected('selectedusers<c:out value="${indexer.index}"/>', 'personSelectorId');"/><br/>
                        </td>
                        <td class="infodata">
                            <select name="selectedasscs_<c:out value="${objective.id}"/>" id="selectedusers<c:out value="${indexer.index}"/>" multiple="true">
                                <c:forEach var="assessor" items="${objective.assessors}" varStatus="counter">
                                    <option value="<c:out value="${assessor.id}"/>"><c:out value="${assessor.displayName}"/></option>                                    
                                </c:forEach>
                            </select>
                        </td>
                        <td class="infodata">                            
                            <c:out value="${objective.description}"/>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td class="actionButton">
                        <input type="hidden" id="currArtefactIdVar" name="currArteIdNme" value="<c:out value="${artefact.currentObjectiveSet.id}"/>"/>
                        <input class="inlinebutton" type="button" name="aname" id="saveasessbtn" value="<fmt:message key="save"/>" onclick="saveAssessors('assResultsTbls'); popupHide();"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>