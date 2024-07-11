<style>
    .date_field { position: relative; z-index: 1000; }
    .ui-datepicker { position: relative; z-index: 1000!important; }
    .center-checkbox { text-align: center; } /* CSS to center align checkboxes */
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstPlanningRegister" value='${requestScope["outputObject"].get("lstPlanningRegister")}' />

<br>

<div class="card">
    <div class="card-body table-responsive p-0" style="height: 600px;">
        <table id="example1" class="table table-head-fixed table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
            <thead>
                <tr align="center">
                    <th><input type="checkbox" id="selectAllCheckbox" onclick="toggleAllCheckboxes(this)"></th>
                    <th><b>Todays Planning</b></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${lstPlanningRegister}" var="pendData">
                    <tr>
                        <td class="center-checkbox"> <!-- Apply center alignment here -->
                            <input type="checkbox" name="namecheckboxes" class="row-checkbox" value="" id="${pendData.invoice_id}">
                        </td>
                        <td>
                            <a href="?a=showGenerateInvoice&invoice_id=${pendData.invoice_id}&editInvoice=N">${pendData.city} (${pendData.totalQty}) </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="col-sm-12">
    <div class="form-group" align="center">
        <button class="btn btn-success" type="button" id="btnsave" onclick='moveToPending()'>Move to Pending</button>
        <button class="btn btn-success" type="button" id="btnsave" onclick='generateReadingReport()'>Generate Reading Report</button>
        <button class="btn btn-success" type="button" id="btnsave" onclick='fryPlanning()'>Fry Planning</button>
        <button class="btn btn-success" type="button" id="btnsave" onclick='makaiPlanning()'>Makai Planning</button>
        <button class="btn btn-success" type="button" id="btnsave" onclick='moveToDone()'>Move to Done</button>

        <button class="btn btn-primary" style="display:none" id="generatePDF" type="button" onclick='generateInvoice("${invoiceDetails.invoice_id}");'>Generate PDF</button>
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
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText);
            }
        };
        xhttp.open("GET", "?a=generateReadingReport", false);
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
</script>
