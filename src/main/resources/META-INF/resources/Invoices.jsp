<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="message" value='${requestScope["outputObject"].get("listReturnData")}' />
<c:set var="ListOfStores" value='${requestScope["outputObject"].get("ListOfStores")}' />

<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />
<c:set var="storeId" value='${requestScope["outputObject"].get("storeId")}' />





<br>
<div class="card">


	<br>
	
<div class="row">
              
              
              
              
              
              
              
               
              
              
				<div class="col-sm-3" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
  						<input type="text" id="txtfromdate" onchange="checkforvalidfromtodate();ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
                  </div>
				</div>
				
				<div class="col-sm-3" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
					<input type="text" id="txttodate"  onchange="checkforvalidfromtodate();ReloadFilters();"    name="txttodate" readonly class="form-control date_field"  placeholder="To Date"/>
  						
                    </div>
				</div>
				
				<div class="col-sm-3" align="center">
					<div class="input-group input-group-sm" style="width: 200px;">
	  					<select id="drpstoreId" name="drpstoreId"  class="form-control float-right" onchange='ReloadFilters()' style="margin-right: 15px;" >
	  						
	  						<option value='-1'>--Select--</option>  						
	  						<c:forEach items="${ListOfStores}" var="store">
								<option value='${store.storeId}'> ${store.storeName}</option>
							</c:forEach>  							
	  					</select>
                  	</div>
				</div>
				
				
				<div class="col-sm-3" align="center">
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
              <div class="card-body table-responsive p-0" style="height: 580px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
                     <th><b>Invoice Id</b></th>
                     <th><b>Customer Name</b></th>
                     <th><b>Date</b></th>
                     <th><b>Store Name</b></th>
                     <th><b>Total Amount</b></th>
                     <th><b>Remarks</b></th>
                     
                     <th><b></b></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${message}" var="item">
					<tr >
					
						<td><a href="#" onclick="showInvoice('${item.invoice_id}')">${item.invoice_no}</a></td>
						<td>${item.vendor_name}</td>	
						<td>${item.invoice_date}</td>
						<td>${item.store_name}</td>
						<td>${item.total_amount}</td>
						<td>${item.remarks}</td>
						
						<td></td><td><button class="btn btn-danger" onclick="deleteInvoice('${item.invoice_id}')">Delete</button></td>
						<td><button class="btn btn-primary" onclick="generatePdfInvoice('${item.invoice_id}')">Generate PDF</button></td>
						
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
      "pageLength": 50
    });
  });
  
  
  
  <c:if test="${param.type eq 'P'}">
  	document.getElementById("divTitle").innerHTML="Invoices (Purchase)";
  	document.title +=" Invoices (Purchase) ";
  </c:if>
  
  <c:if test="${param.type eq 'S'}">
  	document.getElementById("divTitle").innerHTML="Invoices (Sales)";
  	document.title +=" Invoices (Sales) ";
  </c:if>
  
  
  
  
  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  
  
  
    
  
  
  
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
  
  function ReloadFilters()
  {
	  window.location="?a=showInvoices&type=${param.type}&storeId="+drpstoreId.value+"&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value;
	  
  }
  
  txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';
  drpstoreId.value='${storeId}';
  
  
  function deleteInvoice(customerId)
  {
  	
  	var answer = window.confirm("Are you sure you want to delete ?");
  	if (!answer) 
  	{
  		return;    
  	}
  	
  	  document.getElementById("closebutton").style.display='none';
  	   document.getElementById("loader").style.display='block';
  	$('#myModal').modal({backdrop: 'static', keyboard: false});;

  	var xhttp = new XMLHttpRequest();
  	  xhttp.onreadystatechange = function() 
  	  {
  	    if (xhttp.readyState == 4 && xhttp.status == 200) 
  	    { 		      
  	      document.getElementById("responseText").innerHTML=xhttp.responseText;
  		  document.getElementById("closebutton").style.display='block';
  		  document.getElementById("loader").style.display='none';
  		  $('#myModal').modal({backdrop: 'static', keyboard: false});;
  	      
  		  
  		}
  	  };
  	  xhttp.open("GET","?a=deleteInvoice&type=${param.type}&invoiceId="+customerId, true);    
  	  xhttp.send();
  }
  
  
  
	
  function generatePdfInvoice(challanId)
  {
  	var xhttp = new XMLHttpRequest();
  	  xhttp.onreadystatechange = function() 
  	  {
  	    if (xhttp.readyState == 4 && xhttp.status == 200) 
  	    { 		      
  	    	
  	    	window.open("BufferedImagesFolder/"+xhttp.responseText);		  
  		}
  	  };
  	  xhttp.open("GET","?a=generateInvoicePDF&invoiceId="+challanId+"&type=${param.type}", false);    
  	  xhttp.send();
  }
  
function showInvoice(invoiceId)
{
	if("${param.type}"=='P')
		{
			window.location='?a=showGeneratePurchaseInvoice&invoiceId='+invoiceId;
		}
	else
		{
			window.location='?a=showGenerateSI&invoiceId='+invoiceId;
		}

}  	
  
  
</script>


