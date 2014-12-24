<%@ include file="../../includes/include.jsp" %>
<%-- include the jquery js file and dwr bean --%>

<script type="text/javascript">
    $(function() {
        var dragOpts = {
            containment: "#targetbuilder",
            helper: "clone",
            snap: "#targetbuilder",
            snapMode: "inner",
            stop: buildForm
        };

        //swap image back to original
        function buildForm(event, ui) {

            // ui.helper[0] is my element
            var elemId = this.id;

            // find out the array of existing item positions
            var indexArray = $("#rrrrIndex").val().split(',');
            var max = 0;
            for (var i = 0; i < indexArray.length; i++) {
                if(indexArray[i] * 1 > (max * 1)) {
                    max = indexArray[i] * 1;
                }
            }
            max = max * 1 + 1;
            indexArray[indexArray.length] = max;
            // rewrite out the itemArray
            $("#rrrrIndex").val(indexArray + "");

            $.get('viewitemdashboard.htm', {id: elemId, index: max }, function(data) {
                    $('#itemselectable').append(data);
                });
        }

        //make the specified li element draggable
        $("li.ui-selectable").draggable(dragOpts);
    });
    
    $(function() {
        $('img.control_buttons').click(function() {

            // this items index / sort order will be removed from the array
            var index = $('#rrm' + this.id).val();
            $('#a' + this.id).remove();

            var indexArray = $("#rrrrIndex").val().split(',');
            var newVal= new Array();
            var newArrayIndex = 0;
            
            for(var i = 0; i < indexArray.length; i++) {
                if(indexArray[i] != index) {
                    newVal[newArrayIndex] = indexArray[i];
                    newArrayIndex++;
                }
            }

            // rewrite out the itemArray
            $("#rrrrIndex").val(newVal + "");

        });
    });
</script>


<zynap:infobox title="${title}">

    <zynap:form method="post" name="addinfo" encType="multipart/form-data">

        <input type="hidden" id="rrrrIndex" name="rr_index" value="<c:out value="${command.numItems}"/>"/>

        <spring:bind path="command">
            <%@ include file="../../includes/error_messages.jsp" %>
        </spring:bind>

        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infolabel"><fmt:message key="report.label"/>&nbsp;:&nbsp;*</td>
                <td class="infodata">
                    <form:input cssClass="input_text" path="command.label"/>
                    <form:errors path="command.label" cssClass="error"/>
                </td>
            </tr>

            <%@include file="../../analysis/reports/reportcoreformcommon.jsp"%> 

            <c:if test="${command.personType}">
                <tr>
                    <td class="infoheading"><fmt:message key="access.role"/></td>
                    <td class="infoheading"><fmt:message key="groups"/></td>
                </tr>
                <tr>
                    <td class="infodata">
                        <c:import url="assignrolesform.jsp"/>
                    </td>
                    <td class="infodata">
                        <c:import url="assigngroupsform.jsp"/>
                    </td>
                </tr>
            </c:if>
        </table>
        <div class="infomessage"><fmt:message key="drag.from.source.to.target"/></div>
        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infodata" width="250px">
                    <div id="sourcebuilder">
                        <ul>
                            <li><fmt:message key="standard.reports"/>
                                <ul id="repselectable" class="ui-selectable">
                                    <c:forEach items="${tabreports}" var="report" varStatus="repIndexer">
                                        <li id="x_<c:out value="${report.id}"/>" class="ui-selectable"><div id="ax_<c:out value="${report.id}"/>"><c:out value="${report.label}"/></div></li>
                                    </c:forEach>
                                </ul>
                            </li>
                            <li><fmt:message key="progress.reports"/>
                                <ul id="progressselectable" class="ui-selectable">
                                    <c:forEach items="${progressreports}" var="rep" varStatus="prepIndexer">
                                        <li id="x_<c:out value="${rep.id}"/>" class="ui-selectable"><div id="ax_<c:out value="${rep.id}"/>"><c:out value="${rep.label}"/></div></li>
                                    </c:forEach>
                                </ul>
                            </li>
                            <li><fmt:message key="chart.reports"/>
                                <ul id="chartselectable" class="ui-selectable">
                                    <c:forEach items="${chartreports}" var="rep" varStatus="crepIndexer">
                                        <li id="x_<c:out value="${rep.id}"/>" class="ui-selectable"><div id="ax_<c:out value="${rep.id}"/>"><c:out value="${rep.label}"/></div></li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </td>

                <td id="targetbuilder" class="infodata" valign="top" height="100%">
                    <ul>
                        <li><fmt:message key="dashboard.display"/>
                            <ul id="itemselectable" class="ui-selected">
                                <c:forEach items="${command.dashboardItems}" var="item" varStatus="droppedIndexer">
                                    <spring:bind path="command.dashboardItems[${droppedIndexer.index}].*">
                                        <c:set var="report" value="${item.report}"/>
                                        <%@include file="dashboarditemsnippet.jsp"%>
                                        <%@ include file="../../includes/error_messages.jsp" %>
                                    </spring:bind>
                                </c:forEach>
                            </ul>
                        </li>
                    </ul>
                </td>
            </tr>
        </table>

        <table class="infotable" cellpadding="0" cellspacing="0">
            <tr>
                <td class="infobutton">&nbsp;</td>
                <td class="infobutton">
                    <input class="inlinebutton" type="button" name="_cancel" value="<fmt:message key="cancel"/>" onclick="document.forms._cancel.submit();"/>
                    <input class="inlinebutton" type="submit" name="addinfo" value="<fmt:message key="save"/>"/>
                </td>
            </tr>
        </table>

    </zynap:form>

    <zynap:form name="_cancel" method="post">
        <input type="hidden" name="<%=ParameterConstants.CANCEL_PARAMETER%>" value="true"/>
    </zynap:form>

</zynap:infobox>