<%@ include file="../../includes/include.jsp" %>

<script type="text/javascript">

    $(function() {
        $('img.control_buttons').click(function() {

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


<c:set var="item" value="${model.item}"/>
<c:set var="report" value="${model.report}"/>
<c:set var="elemid" value="${report.id}"/>

<li id="ac<c:out value="${elemid}"/><c:out value="${item.reference}"/>" class="ui-selected">
    <div id="abc<c:out value="${elemid}"/><c:out value="${item.reference}"/>" class="contained">
        <input type="hidden" id="rrmc<c:out value="${elemid}"/><c:out value="${item.reference}"/>" name="ll<c:out value="${elemid}"/>" value="<c:out value="${item.index}"/>"/>
        <div class="top_buttons">
        	<img id="c<c:out value="${elemid}"/><c:out value="${item.reference}"/>" class="control_buttons" src="../images/popupClose.gif" alt="Remove"/>
        </div>
        <div><c:out value="${report.label}"/></div>
        <div class="infomessage">            
            <fmt:message key="report.columns"/>&nbsp;:&nbsp;
            <c:out value="${item.columnString}"/>
        </div>
        <input type="hidden" name="dashboardItems[<c:out value="${item.index}"/>].reportId" value="<c:out value="${report.id}"/>"/>
        <input type="hidden" name="dashboardItems[<c:out value="${item.index}"/>].columnString" value="<c:out value="${item.columnString}"/>"/>
        <input type="hidden" name="dashboardItems[<c:out value="${item.index}"/>].reportType" value="<c:out value="${report.reportType}"/>"/>

        <table class="contained" cellspacing="0" cellpadding="0">
            <tr>
                <td class="infoheading small"><fmt:message key="order"/></td>
                <td class="infoheading"><fmt:message key="report.label"/></td>
                <td class="infoheading"><fmt:message key="report.description"/></td>
            </tr>
            <tr>
                <td class="infodata small"><input type="text" name="dashboardItems[<c:out value="${item.index}"/>].position"
                                            value="<c:out value="${item.index}"/>" maxlength="5"/></td>
                <td class="infodata"><input type="text" name="dashboardItems[<c:out value="${item.index}"/>].label"
                                            value="<c:out value="${item.label}"/>"/></td>                
                <td class="infodata"><textarea cols="32" rows="2" name="dashboardItems[<c:out value="${item.index}"/>].description"><c:out
                        value="${item.description}"/></textarea></td>
            </tr>
        </table>

		<c:if test="${report.chartReport}">
			<div class="infomessage"><fmt:message key="enter.expected.values"/></div>
        	<table class="contained" cellspacing="0" cellpadding="0">
                <tr>
                    <c:forEach var="val" items="${item.dashboardChartValues}" varStatus="indexer">
                        <td nowrap class="infodata">
                        	<c:out value="${val.column.label}"/>&nbsp:&nbsp;
                            <input type="hidden"
                                   name="dashboardItems[<c:out value="${item.index}"/>].dashboardChartValues[<c:out value="${indexer.index}"/>].columnId"
                                   value="<c:out value="${val.column.id}"/>"/>
                            <input type="hidden"
                                   name="dashboardItems[<c:out value="${item.index}"/>].dashboardChartValues[<c:out value="${indexer.index}"/>].columnLabel"
                                   value="<c:out value="${val.column.label}"/>"/>
                            <input type="text"
                                   name="dashboardItems[<c:out value="${item.index}"/>].dashboardChartValues[<c:out value="${indexer.index}"/>].expectedValue"
                                   value="<c:out value="${val.expectedValue}"/>"/>
                        </td>
                    </c:forEach>
                </tr>
            </table>
        </c:if>
    </div>
</li>
