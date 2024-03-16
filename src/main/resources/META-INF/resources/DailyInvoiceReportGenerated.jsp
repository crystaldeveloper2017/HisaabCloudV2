<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="dailyInvoiceData" value='${requestScope["outputObject"].get("dailyInvoiceData")}' />
<c:set var="listStoreData" value='${requestScope["outputObject"].get("listStoreData")}' />

<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />
<c:set var="drpStoreId" value='${requestScope["outputObject"].get("drpStoreId")}' />
<c:set var="type" value='${requestScope["outputObject"].get("type")}' />
<c:set var="customerMaster" value='${requestScope["outputObject"].get("customerMaster")}' />
<c:set var="customerDetails" value='${requestScope["outputObject"].get("customerDetails")}' />






<br>
<div class="card">




		<br>

    
              
              
              
              <div class="row">
              
              
              
              
              <datalist id="customerList">

<c:forEach items="${customerMaster}" var="customer">
			    <option id="${customer.customerId}">${customer.customerName}~${customer.mobileNumber}~${customer.customerType}</option>			    
	   </c:forEach>	   	 

  	
</datalist>
              
              
               
              
              
				<div class="col-sm-2" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
  						<input type="text" id="txtfromdate" onchange="checkforvalidfromtodate();ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
                  </div>
				</div>
				
				<div class="col-sm-2" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
					<input type="text" id="txttodate"  onchange="checkforvalidfromtodate();ReloadFilters();"    name="txttodate" readonly class="form-control date_field"  placeholder="To Date"/>
  						
                    </div>
				</div>
				
				<div class="col-sm-2" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
	  					<select id="drpstoreId" name="drpstoreId"  class="form-control float-right" onchange='ReloadFilters()' style="margin-right: 15px;" >
	  						
	  						<option value='-1'>--Select--</option>  						
	  						<c:forEach items="${listStoreData}" var="store">
								<option value='${store.storeId}'> ${store.storeName}</option>
							</c:forEach>  							
	  					</select>
                  	</div>
				</div>
		<div class="col-sm-2" align="center">
			<div class="input-group input-group-sm" style="width: 200px;">
				<input type="text" class="form-control form-control-sm"
					id="txtsearchcustomer" placeholder="Search For Customer"
					name="txtsearchcustomer" autocomplete="off"
					oninput="checkforLengthAndEnableDisable();checkforMatchCustomer()">
				<input type="hidden" name="hdnSelectedCustomer"
					id="hdnSelectedCustomer" value=""> <span
					class="input-group-append">
					<button type="button" class="btn btn-danger btn-flat"
						onclick="resetCustomer()">Reset</button>
				</span>
			</div>




			



		</div>


	<div class="col-sm-1">
  	<div class="form-group">
  	<div class="input-group input-group-sm">
                  <input type="text" class="form-control form-control-sm" id="txtinvoiceno" onkeyup='ReloadFilters()'
                   placeholder="Search Invoice No" name="txtinvoiceno">                           
    </div>
    </div>
  </div>

		<div class="col-sm-1" align="center">
					
                  	
                  	
                  	<div class="form-check">
	                    <input type="checkbox" class="form-check-input" id="chkdeletedinvoice" onchange="ReloadFilters()">
	                    <label class="form-check-label" for="chkdeletedinvoice">Show Deleted Invoices</label>
                  	</div>
				</div>
				
				
				<div class="col-sm-2" align="center">
							<div class="card-tools">
		                  <div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
		                    <div class="icon-bar" style="font-size:22px;color:firebrick">
		  						<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
		 						<a title="Download PDF" onclick="downloadPDF()"><i class="fa fa-file-pdf-o"></i></a>
		  						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
							</div>           
		                  </div>
		                </div>
				</div>
				
			  </div>
              
              
              
              
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 800px;">                
                <table id="example1" class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
                     <th><b>Customer Name</b></th><th><b>Total Amount</b></th> <th><b>Invoice No</b></th>
                     <th><b>Invoice Date</b></th>
                     <th><b>Updated Date</b></th>
                     <th><b>Payment Type</b></th><th><b>Payment Mode</b></th></th><th><b>Created By</b></th><th><b>Store Name</b></th>
                     <th></th>
                     <th></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${dailyInvoiceData}" var="item">
				
				<c:if test="${item.isActive eq '1'}">				
					<tr>				
				</c:if>
				
				<c:if test="${item.isActive eq '0'}">				
					<tr style="color:red">				
				</c:if>

						<td>${item.customer_name}</td><td>${item.total_amount}</td>
						
						<td><a href="?a=showGenerateInvoice&invoice_id=${item.invoice_id}&type=${type}">${item.invoice_no}</a></td>
						
						<td>${item.FormattedInvoiceDate}</td>
						<td>${item.updatedDate}</td>
						<td>${item.payment_type}</td>
						<td>${item.payment_mode}</td>
						<td>${item.name}</td>
						<td>${item.store_name}</td>
						<c:if test="${item.isActive eq '1'}">
							<td><button class="btn btn-primary" onclick="editInvoice('${item.invoice_id}')">Edit</button></td>

							<td><button class="btn btn-danger" onclick="deleteInvoice('${item.invoice_id}')">Delete</button></td>
						</c:if>
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
</div>
            
            
            
            



<script >
  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 100,
      "order": [[ 2, "desc" ]]
    });
  });
  
  document.getElementById("divTitle").innerHTML="Sales Invoice Report";
  document.title +=" Sales Invoice Report ";
  
  
  
</script>


<script >
function ReloadFilters()
{
	  window.location="?a=generateDailyInvoiceReport&type=${type}&drpstoreId="
			  +drpstoreId.value+"&txtfromdate="+txtfromdate.value+"&txttodate="
			  +txttodate.value+"&deleteFlag="+chkdeletedinvoice.checked+"&customerId="+hdnSelectedCustomer.value+"&invoice_no="+txtinvoiceno.value;

	  //
	  
}

function checkforvalidfromtodate()
{        	
	var fromDate=document.getElementById("txtfromdate").value;
	var toDate=document.getElementById("txttodate").value;
	
	var fromDateArr=fromDate.split("/");
	var toDateArr=toDate.split("/");
	
	
	var fromDateArrDDMMYYYY=fromDate.split("/");
	var toDateArrDDMMYYYY=toDate.split("/");
	
	var fromDateAsDate=new Date(fromDateArrDDMMYYYY[2],fromDateArrDDMMYYYY[1]-1,fromDateArrDDMMYYYY[0]);
	var toDateAsDate=new Date(toDateArrDDMMYYYY[2],toDateArrDDMMYYYY[1]-1,toDateArrDDMMYYYY[0]);
	
	if(fromDateAsDate>toDateAsDate)
		{
			alert("From Date should be less than or equal to To Date");
			window.location.reload();        			
		}
}

$( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
$( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });



txtfromdate.value='${txtfromdate}';
txttodate.value='${txttodate}';
drpstoreId.value='${drpStoreId}';

if('${param.customerId}'!='')
	{
		txtsearchcustomer.value='${customerDetails.customer_name}';//
		txtsearchcustomer.disabled=true;
	}

if('${param.invoice_no}'!='')
	{
		txtinvoiceno.value='${param.invoice_no}';//
		
	}


function editInvoice(invoiceId)
{
		window.location="?a=showGenerateInvoice&editInvoice=Y&invoice_id="+invoiceId;
		//alert(invoiceId);
}
function deleteInvoice(invoiceId)
{
	
	var answer = window.confirm("Are you sure you want to delete ?");
	if (!answer) 
	{
		return;    
	}
	
	

	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	  toastr["success"](xhttp.responseText);
		    	toastr.options = {"closeButton": false,"debug": false,"newestOnTop": false,"progressBar": false,
		    	  "positionClass": "toast-top-right","preventDuplicates": false,"onclick": null,"showDuration": "1000",
		    	  "hideDuration": "500","timeOut": "500","extendedTimeOut": "500","showEasing": "swing","hideEasing": "linear",
		    	  "showMethod": "fadeIn","hideMethod": "fadeOut"}
		    	
		    	window.location.reload();
	      
		  
		}
	  };
	  xhttp.open("GET","?a=deleteInvoice&user_id=${userdetails.user_id}&app_id=${userdetails.app_id}&store_id=${userdetails.store_id}&invoiceId="+invoiceId+"&type=${param.type}", true);    
	  xhttp.send();
}


if("${param.deleteFlag}"=="true")
	{
		chkdeletedinvoice.checked=true;
	}
	
function checkforMatchCustomer()
{
	
	var searchString= document.getElementById("txtsearchcustomer").value;	
	var options1=document.getElementById("customerList").options;
	var customerId=0;
	for(var x=0;x<options1.length;x++)
		{
			if(searchString==options1[x].value)
				{
					customerId=options1[x].id;
					
					break;
				}
		}
	if(customerId!=0)
		{
			document.getElementById("hdnSelectedCustomer").value=customerId;			
			document.getElementById("txtsearchcustomer").disabled=true;
			ReloadFilters();
		}
	else
		{
			//searchForCustomer(searchString);
		}
	
	
	
}


function checkforLengthAndEnableDisable()
{
		if(txtsearchcustomer.value.length>=3)
			{
				txtsearchcustomer.setAttribute("list", "customerList");
			}
		else
			{
				txtsearchcustomer.setAttribute("list", "");
			}
}

function resetCustomer()
{	
	txtsearchcustomer.disabled=false;
	txtsearchcustomer.value="";
	hdnSelectedCustomer.value="";
	ReloadFilters();
}
	

</script>
