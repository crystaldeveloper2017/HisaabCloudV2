<style>
    .date_field { position: relative; z-index: 1000; }
    .ui-datepicker { position: relative; z-index: 1000!important; }
    /* Center align checkboxes */
    table#example1 th,
    table#example1 td {
        text-align: center;
    }
    table#example1 th input[type="checkbox"] {
        margin: 0 auto;
        display: block;
    }
    table#example1 td input[type="checkbox"] {
        margin: 0 auto;
        display: block;
    }
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstCompletedOrders" value='${requestScope["outputObject"].get("lstCompletedOrders")}' />

<br>

<div class="card">
    <div class="card-body table-responsive p-0" style="height: 600px;">
        <table id="example1" class="table table-head-fixed table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAllCheckbox" onclick="toggleAllCheckboxes(this)"></th>
                    <th><b>City (Qty)</b></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${lstCompletedOrders}" var="completeData">
                    <tr>
                        <td>
                            <input type="checkbox" name="namecheckboxes" class="row-checkbox" value="" id="${completeData.invoice_id}">
                        </td>
                        <td>
                            <a href="?a=showGenerateInvoice&invoice_id=${completeData.invoice_id}&editInvoice=N">${completeData.city} (${completeData.totalQty})</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="col-sm-12">
    <div class="form-group" align="center">
        <button class="btn btn-success" type="button" id="btnsave" onclick='moveToPlanning()'>Move to Planning</button>
        <button class="btn btn-danger" type="button" id="btnsave" onclick='deleteOrders()'>Delete</button>
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

    function moveToPlanning() {
        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked) {
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
        xhttp.open("GET", "?a=moveToPlanning&invoiceIds=" + requiredInvoiceIds, true);
        xhttp.send();
    }


    function deleteOrders() {


        var answer = window.confirm("Are you sure you want to delete ?");
        if (!answer) 
        {
            return;    
        }

        var checkboxes = document.getElementsByName("namecheckboxes");
        var requiredInvoiceIds = "";

        for (var k = 0; k < checkboxes.length; k++) {
            if (checkboxes[k].checked) {
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
        xhttp.open("GET", "?a=deleteOrders&invoiceIds=" + requiredInvoiceIds, true);
        xhttp.send();
    }

     
  document.getElementById("divTitle").innerHTML="Completed Orders ";
  document.title +=" Completed Orders ";
</script>
