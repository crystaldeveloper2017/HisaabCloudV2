<style>
    .date_field { position: relative; z-index: 1000; }
    .ui-datepicker { position: relative; z-index: 1000!important; }
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstCompletedOrders" value='${requestScope["outputObject"].get("lstCompletedOrders")}' />

<br>

<div class="card">
    
    <br>

    <div class="card-body table-responsive p-0" style="height: 800px;">
        <table id="example1" class="table table-head-fixed table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll" onclick="toggleSelectAll(this)"></th>
                    <th><b>Invoice Id</b></th>
                    <th><b>Customer Name</b></th>
                    <th><b>City</b></th>
                    <th><b>Invoice Date</b></th>
                    <th><b>Qty</b></th>
                    <th><b>Action</b></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${lstCompletedOrders}" var="completeData">
                    <tr>
                        <td><input type="checkbox" class="rowCheckbox" name="namecheckboxes" id="${completeData.invoice_id}"></td>
                        <td>${completeData.invoice_id}</td>
                        <td>${completeData.customer_name}</td>
                        <td>${completeData.city}</td>
                        <td>${completeData.invoice_date}</td>
                        <td>${completeData.totalQty}</td>
                        <td><button class="btn btn-danger" onclick="deleteInvoice('${completeData.invoice_id}')">Delete</button></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="row text-center">
        <div class="col-md-4 col-sm-12 mb-2">
            <button class="btn btn-danger btn-block" onclick="deleteOrders()">Delete Selected</button>
        </div>
        <div class="col-md-4 col-sm-12 mb-2">
            <button class="btn btn-success btn-block" type="button" id="btnsave" onclick="moveToPlanning()">Move to Planning</button>
        </div>
        <div class="col-md-4 col-sm-12 mb-2">
            <button class="btn btn-success btn-block" type="button" id="btnsave" onclick="moveToPending()">Move to Pending</button>
        </div>
    </div>
</div>

<script>
function deleteInvoice(invoice_id) {
    var answer = window.confirm("Are you sure you want to delete?");
    if (!answer) {
        return;
    }

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            toastr["success"](xhttp.responseText);
            toastr.options = {
                "closeButton": false, "debug": false, "newestOnTop": false, "progressBar": false,
                "positionClass": "toast-top-right", "preventDuplicates": false, "onclick": null,
                "showDuration": "1000", "hideDuration": "500", "timeOut": "500", "extendedTimeOut": "500",
                "showEasing": "swing", "hideEasing": "linear", "showMethod": "fadeIn", "hideMethod": "fadeOut"
            };
            window.location.reload();
        }
    };
    xhttp.open("GET", "?a=deleteInvoice&invoice_id=" + invoice_id, true);
    xhttp.send();
}

$(function() {
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






function checkforMatchCustomer() {
    var searchString = document.getElementById("txtsearchcustomer").value;
    if (searchString.length < 3) {
        return;
    }
    var options1 = document.getElementById("customerList").options;
    var customerId = 0;
    for (var x = 0; x < options1.length; x++) {
        if (searchString == options1[x].value) {
            customerId = options1[x].id;
            break;
        }
    }
    if (customerId != 0) {
        document.getElementById("hdnSelectedCustomer").value = customerId;
        document.getElementById("txtsearchcustomer").disabled = true;
        ReloadFilters();
    }
}

function checkforLengthAndEnableDisable() {
    if (txtsearchcustomer.value.length >= 3) {
        txtsearchcustomer.setAttribute("list", "hdnSelectedCustomer");
    } else {
        txtsearchcustomer.setAttribute("list", "");
    }
}

function resetCustomer() {
    txtsearchcustomer.disabled = false;
    txtsearchcustomer.value = "";
    hdnSelectedCustomer.value = "";
    ReloadFilters();
}

document.getElementById("divTitle").innerHTML = "Completed Orders ";
document.title += " Completed Orders ";

$( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
$( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });

function toggleSelectAll(source) {
    checkboxes = document.getElementsByClassName('rowCheckbox');
    for(var i=0, n=checkboxes.length;i<n;i++) {
        checkboxes[i].checked = source.checked;
    }
}

function deleteOrders() {
    var answer = window.confirm("Are you sure you want to delete ?");
    if (!answer) {
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

</script>
