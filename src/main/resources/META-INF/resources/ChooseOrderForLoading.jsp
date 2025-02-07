<style>
    .date_field { position: relative; z-index: 1000; }
    .ui-datepicker { position: relative; z-index: 1000!important; }
    .center-checkbox { text-align: center; } /* CSS to center align checkboxes */
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstPlanningRegister" value='${requestScope["outputObject"].get("lstPlanningRegister")}' />
<c:set var="loadingDetails" value='${requestScope["outputObject"].get("loadingDetails")}' />


<br>

<div class="card">
    <div class="card-body table-responsive p-0" style="height: 600px;">
        <table id="example1" class="table table-head-fixed table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
            
            <tbody>
                <c:forEach items="${lstPlanningRegister}" var="pendData">
                    <tr>
                        <td class="center-checkbox"> <!-- Apply center alignment here -->
                            <input type="checkbox" name="namecheckboxes" class="row-checkbox" value="" id="${pendData.invoice_id}">
                        </td>
                        <td>
                            <a href="?a=showLoadingScreen&loading_id=${param.loading_id}&order_id=${pendData.invoice_id}&line_no=1">${pendData.city} (${pendData.totalQty}) </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>



<script>
    // JavaScript function to toggle all checkboxes based on select all checkbox
    function toggleAllCheckboxes(selectAllCheckbox) {
        var checkboxes = document.getElementsByName('namecheckboxes');
        for (var i = 0; i < checkboxes.length; i++) {
            checkboxes[i].checked = selectAllCheckbox.checked;
        }
    }

    $(function () {
        $('#example1').DataTable({
            "paging": true,
            "lengthChange": false,
            "searching": false,
            "info": true,
            "autoWidth": false,
            "responsive": true,
            "pageLength": 100,
            "order": [[2, 'desc']]
        });
    });

    function moveToPending() {
        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked == true) {
                requiredInvoiceIds += checkboxes[k].id + "~";
            }
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                alert(xhttp.responseText);
                window.location = "?a=showHomePage";
            }
        };
        xhttp.open("GET", "?a=moveToPending&invoiceIds=" + requiredInvoiceIds, true);
        xhttp.send();
    }

    function moveToDone() {
        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked == true) {
                requiredInvoiceIds += checkboxes[k].id + "~";
            }
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                alert(xhttp.responseText);
                window.location = "?a=showHomePage";
            }
        };
        xhttp.open("GET", "?a=movetoDone&invoiceIds=" + requiredInvoiceIds, true);
        xhttp.send();
    }

    function fryPlanning() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText);
            }
        };
        xhttp.open("GET", "?a=generatefryPlanning", false);
        xhttp.send();
    }

    function generateReadingReport() {


        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked == true) {
                requiredInvoiceIds += checkboxes[k].id + "~";
            }
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText);
            }
        };
        xhttp.open("GET", "?a=generateReadingReport&requiredInvoiceIds="+requiredInvoiceIds+"&chkconsiderstock="+chkconsiderstock.checked, false);
        xhttp.send();
    }

    function makaiPlanning() {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText);
            }
        };
        xhttp.open("GET", "?a=generatemakaiPlanning", false);
        xhttp.send();
    }

    function generateOrderForms() {

        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked == true) {
                requiredInvoiceIds += checkboxes[k].id + "~";
            }
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText);
            }
        };
        xhttp.open("GET", "?a=generateOrderForms&requiredInvoiceIds=" + requiredInvoiceIds, false);
        xhttp.send();
    }

	document.getElementById("divTitle").innerHTML = "Choose Order For Loading ${loadingDetails.vehicle_name} ${loadingDetails.vehicle_number}";
    document.title += " Add Vehicle ";
</script>
