<%@ include file="../includes/include.jsp" %>
<zynap:window elementId="objectiveSelect${objectiveIndex}">

    <div class="listspan">
        <c:set var="def" value="${command.publishedCorporateObjectiveSet}"/>
        <span class="listspan"><c:out value="${def.label}"/> </span>
            <ul class="selectable">
                <c:forEach var="corp" items="${def.objectives}" >
                    <li class="hideable">
                        <hr/>
                        <c:out value="${corp.label}"/>
                        <ul class="selectable">
                            <li class="selectable" id="ab<c:out value="${corp.id}"/>" onmouseover="swapLiStyle(this, 'inset');" onmouseout="swapLiStyle(this, 'outset');" onclick="setParentObjectiveInfo('<c:out value="${corp.id}"/>', 'bcpartnerval<c:out value="${objectiveIndex}"/>', 'btnBlogX<c:out value="${objectiveIndex}"/>', '<c:out value="${corp.id}"/>'); popupHide();"><c:out value="${corp.description}"/></li>
                        </ul>
                    </li>
                </c:forEach>
            </ul>

        <c:forEach var="ouSet" items="${command.organisationObjectiveSets}">
            <fieldset>
                <legend><fmt:message key="organisation.goals"/>&nbsp;:&nbsp;<strong><c:out value="${ouSet.organisationUnit.label}"/></strong></legend>
            <!--<span class="listspan"></span>-->
            <ul class="selectable">
                <c:forEach var="buObjectives" items="${ouSet.objectives}" >
                    <li class="hideable">
                        <!--<hr/>-->
                        &nbsp;&nbsp;<c:out value="${buObjectives.label}"/>
                        <ul class="selectable">
                            <li class="selectable" id="ab<c:out value="${buObjectives.id}"/>" onmouseover="swapLiStyle(this, 'inset');" onmouseout="swapLiStyle(this, 'outset');" onclick="setParentObjectiveInfo('<c:out value="${buObjectives.id}"/>', 'bcpartnerval<c:out value="${objectiveIndex}"/>', 'btnBlogX<c:out value="${objectiveIndex}"/>', '<c:out value="${buObjectives.id}"/>'); popupHide();"><c:out value="${buObjectives.description}"/></li>
                        </ul>
                    </li>
                </c:forEach>
            </ul>
            </fieldset>
        </c:forEach>
    </div>
</zynap:window>
