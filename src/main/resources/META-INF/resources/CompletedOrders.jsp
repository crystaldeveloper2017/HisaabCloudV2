<style>
    .date_field { position: relative; z-index: 1000; }
    .ui-datepicker { position: relative; z-index: 1000!important; }
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />
<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstCompletedOrders" value='${requestScope["outputObject"].get("lstCompletedOrders")}' />

<br>

<div class="card">
    <br>
    <div class="row">
        <!-- Your existing code for the date pickers and buttons -->
   <div class="col-sm-1" align="center">
			<label for="txtfromdate">From Date</label>
		</div>
		
		<div class="col-sm-2" align="center">
			<div class="input-group input-group-sm" style="width: 200px;">
			 	<input type="text" id="txtfromdate" onchange="checkforvalidfromtodate();ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
			</div>
		</div>
		
		<div class="col-sm-1" align="center">

			<label for="txttodate">To Date</label>
		</div>
		
		<div class="col-sm-2" align="center">
			<div class="input-group input-group-sm" style="width: 200px;">
				<input type="text" id="txttodate"  onchange="checkforvalidfromtodate();ReloadFilters();"    name="txttodate" readonly class="form-control date_field"  placeholder="To Date"/>
			</div>
		</div>
		
		     		
		
		
		
		<div class="col-sm-2" align="center">
			<div class="card-tools">
				<div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
					<div class="icon-bar" style="font-size:22px;color:firebrick">
						<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
 						<a title="Download PDF" onclick="exportSalesRegister2()"><i class="fa fa-file-pdf-o"></i></a>
  						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
					</div>           
				</div>
			</div>
		</div>
	</div>
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



<div class="row" align="center">
	<div class="col col-12">
		<button class="btn btn-danger" onclick="deleteOrders()">Delete Selected</button>
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

txtfromdate.value = '${txtfromdate}';
txttodate.value = '${txttodate}';

function exportSalesRegister2() {
    try {
        window.open("?a=exportSalesRegister2&txtfromdate=" + txtfromdate.value + "&txttodate=" + txttodate.value + "&customerId=" + hdnSelectedCustomer.value);
        return;

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (xhttp.readyState == 4 && xhttp.status == 200) {
                window.open("BufferedImagesFolder/" + xhttp.responseText, '_blank', 'height=500,width=500,status=no,toolbar=no,menubar=no,location=no');
            }
        };
        xhttp.open("GET", "?a=exportSalesRegister2", true);
        xhttp.send();
    } catch (ex) {
        alert(ex.message);
    }
}

function checkforvalidfromtodate() {
    var fromDate = document.getElementById("txtfromdate").value;
    var toDate = document.getElementById("txttodate").value;

    var fromDateArrDDMMYYYY = fromDate.split("/");
    var toDateArrDDMMYYYY = toDate.split("/");

    var fromDateAsDate = new Date(fromDateArrDDMMYYYY[2], fromDateArrDDMMYYYY[1] - 1, fromDateArrDDMMYYYY[0]);
    var toDateAsDate = new Date(toDateArrDDMMYYYY[2], toDateArrDDMMYYYY[1] - 1, toDateArrDDMMYYYY[0]);

    if (fromDateAsDate > toDateAsDate) {
        alert("From Date should be less than or equal to To Date");
        window.location.reload();
    }
}

function ReloadFilters() {
    window.location = "?a=showCompletedOrders&txtfromdate=" + txtfromdate.value + "&txttodate=" + txttodate.value;
}

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
</script>
