<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>


<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<br>


<c:set var="message" value='${requestScope["outputObject"].get("CustomerInvoiceHistory")}' />
<c:set var="customerDetails" value='${requestScope["outputObject"].get("customerDetails")}' />
<c:set var="totalDetails" value='${requestScope["outputObject"].get("totalDetails")}' />
<c:set var="lstNozzleRegister" value='${requestScope["outputObject"].get("lstNozzleRegister")}' />
<c:set var="customerMaster" value='${requestScope["outputObject"].get("customerMaster")}' />
<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="lstPayments" value='${requestScope["outputObject"].get("lstPayments")}' />
<c:set var="salesEmpWiseMap" value='${requestScope["outputObject"].get("salesEmpWiseMap")}' />
<c:set var="LubeSales" value='${requestScope["outputObject"].get("LubeSales")}' />
<c:set var="paymentEmpWiseMap" value='${requestScope["outputObject"].get("paymentEmpWiseMap")}' />
<c:set var="lstOfShifts" value='${requestScope["outputObject"].get("lstOfShifts")}' />
<c:set var="suggestedShiftId" value='${requestScope["outputObject"].get("suggestedShiftId")}' />
<c:set var="lstPumpTest" value='${requestScope["outputObject"].get("lstPumpTest")}' />



 <div class="card">
 <br>

<div class="row">

		
		
<div class="col-sm-1" align="center">
		<label for="txtfromdate">From Date</label>
	</div>

	<div class="col-sm-2" align="center">
		<div class="input-group input-group-sm" style="width: 200px;">
		<input type="text" id="txtfromdate" onchange="ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
		</div>
	</div>
	
	<div class="col-sm-1" align="center">
		<label >Shift Name</label>
	</div>
	
	<div class="col-sm-2" align="center">
		<div class="input-group input-group-sm" >
			<select class="form-control form-control-sm" name="drpshiftid" id="drpshiftid"  onchange="ReloadFilters();" >
				<option value="-1">----------Select----------</option>
				<c:forEach items="${lstOfShifts}" var="shift">
						  <option value="${shift.shift_id}">${shift.shift_name}~${shift.from_time}~${shift.to_time}</option>    
				 </c:forEach></select>
					  
			  </div>
			</div>
		
	
	
	
	
	<div class="col-sm-2" align="center">
		<div class="card-tools">
			<div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
				<div class="icon-bar" style="font-size:22px;color:firebrick">
				<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
 						<a title="Download PDF" onclick="exportNozzleRegister()"><i class="fa fa-file-pdf-o"></i></a>
  						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a> 
				</div>          
			</div>
		</div>
	</div>
				
</div>
</br>

         
              
              
               
                
              
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" >                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 <tr align="center">
				 	<th colspan="20" >
					Petrol / Diesel Sales
					</th>
				 </tr>
                    <tr>                  
                      
                     <th><b>Nozzle Name</b></th><th><b>Shift</b></th><th><b>Attendant Name</b></th>
					 
                     <th><b>Opening Reading</b></th>                     
                     <th><b>Closing Reading</b></th>
                     
                     <th><b>Tot. Opening Amount</b></th>
                     <th><b>Tot. Closing Amount</b></th>                     
                     
                     
                     <th><b>Test Fuel</b> </th><th><b>Sales Qty</b> </th>     <th><b>Rate</b> </th> 
                     <th><b>Total Amount</b> </th>
                     
                     
                     
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${lstNozzleRegister}" var="item">
				<c:if test="${item.item_name eq 'Petrol'}">
					<tr style="background-color:lightgreen">
				</c:if>

				<c:if test="${item.item_name eq 'Diesel'}">
					<tr style="background-color:lightblue">
				</c:if>
					
						<td>${item.nozzle_name} ${item.item_name}</td>
					
						<td>${item.shift_name} ${item.from_time} ${item.to_time}</td>
						<td>${item.attendantName}</td>
												
						<td>${item.opening_reading}</td>
						<td>${item.closing_reading}</td>
						
						<td>${item.totalizer_opening_reading}</td>
						<td>${item.totalizer_closing_reading}</td>
						
						
						<td>${item.testFuel}</td>
						<td>${item.diffReading}</td>
						<td>${item.rate}</td>
						<td>${item.totalAmount}</td>
						
						

						
						
						
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
              
               
</div>


	<div class="card">
 <br>

<div class="row">

	
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" >                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
				 <tr  align="center">
				 	<th colspan="20">
					Lube Sales
					</th>
				 </tr>
                    <tr>                    
                     <th><b>Attendant Name</b></th>
                     <th><b>Shift</b></th>
					 <th><b>Invoice No</b></th>
					 <th><b>Amount</b></th>
					 <th><b>Item Name</b></th>					 
					 <th><b>Qty</b></th>					 
                     
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${LubeSales}" var="paym">
					<tr >
						<td>${paym.name} </td>
						<td>${paym.shift_name} </td>
						
						<td><a href="?a=showGenerateInvoice&invoice_id=${paym.invoice_id}&type=${type}">${paym.invoice_no}</a></td>
						<td>${paym.totalAmount} </td>
						<td>${paym.item_name} </td>			
						<td>${paym.qty} </td>			
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
              
               
</div>        
       
       
   <div class="card">
 <br>

<div class="row">

	
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" >                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 <tr  align="center">
				 	<th colspan="20">
					Payments
					</th>
				 </tr>
                    <tr>                    
                     <th><b>Attendant Name</b></th>
                     <th><b>Shift</b></th>
                     
                     
                     <th><b>Cash</b></th>
                     <th><b>Card Swipe</b></th>
                     <th><b>Paytm</b></th>
                     <th><b>Credit Sales</b></th>
					 <th><b>Loyalty Points</b></th>
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${lstPayments}" var="paym">
					<tr >
						<td>${paym.name} </td>
						<td>${paym.shift_name} </td>
						
						
						<td><a href="?a=showSupervisorCollection&txtfromdate=${txtfromdate}&attendant_id=${paym.attendant_id}&collection_mode=Cash"> ${paym.csh} </a></td>
						
						<td><a href="?a=generateDailyInvoiceReport&drpstoreId=${userdetails.store_id }&paymentMode=Card&txtfromdate=${txtfromdate}&txttodate=${txtfromdate}&attendant_id=${paym.attendant_id}"> ${paym.cswp} </a></td>
						
						<td><a href="?a=showSupervisorCollection&txtfromdate=${txtfromdate}&attendant_id=${paym.attendant_id}&collection_mode=Paytm"> ${paym.pytm} </a></td>
						
						<td><a href="?a=generateDailyInvoiceReport&paymentType=Pending,Partial&drpstoreId=${userdetails.store_id }&txtfromdate=${txtfromdate}&attendant_id=${paym.attendant_id}"> ${paym.pnding} </a></td>

						<td><a href="?a=showPaytmTransctions&txtfromdate=${txtfromdate}&attendant_id=${paym.attendant_id}"> ${paym.loyaltyPoints} </a></td>
						

                     						
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
              
              
               
</div>
                    
    <br>

<div class="row">

	
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" >                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
				 <tr  align="center">
				 	<th colspan="20">
					Difference 
					</th>
				 </tr>
                  <th>Name</th>
                  <th>Sales</th>
                  <th>Payments</th>
                  <th>Difference</th>
                 <c:forEach var="entry" items="${salesEmpWiseMap}">
                 
                 <tr>
  					<td><c:out value="${entry.key}"/></td>
  					<td><c:out value="${entry.value}"/></td>
  					<td><c:out value="${paymentEmpWiseMap.get(entry.key)}"/></td>
  					
  					<td>  						
  						<fmt:formatNumber type="number"  pattern = "###.##" value="${paymentEmpWiseMap.get(entry.key) - entry.value}" />
  							  					
  					</td>
  					  					
  				 </tr>
  				   					
				 </c:forEach>  
				
				
				
                  
                </table>
              </div>
              <!-- /.card-body -->
              
              
               
</div>


<div class="row">

	
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" >                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
				 <tr  align="center">
				 	<th colspan="20">
					Pump Tests 
					</th>
				 </tr>
                  <th>Name</th>
                  <th>Shift Name</th>
				  <th>Nozzle Name</th>
				  <th>Item Name</th>
				  <th>Test Qty</th>
                  <th>Rate</th>
                  <th>Total Amount Cash</th>
                 <c:forEach var="pump" items="${lstPumpTest}">
                 
                 <tr>
  					
  					<td> ${pump.name}</td>
					<td> ${pump.shift_name}</td>
					<td> ${pump.nozzle_name}</td>
					<td> ${pump.item_name}</td>
					<td> ${pump.test_quantity}</td>
					<td> ${pump.price}</td>
					<td> ${pump.totalAmountCash}</td>
					
  					  					
  				 </tr>
  				   					
				 </c:forEach>  
				
				
				
                  
                </table>
              </div>
              <!-- /.card-body -->
              
              
               
</div>

<script>
  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,      
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 50,
      "order": [[ 2, 'desc' ]]

    });
  });
  
  document.getElementById("divTitle").innerHTML="Nozzle Register";

  document.title +=" Nozzle Register";

  

  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
 



  txtfromdate.value='${txtfromdate}';

  
  
  
  
  function exportNozzleRegister()
  {
	  window.open("?a=exportNozzleRegister&txtfromdate="+txtfromdate.value+"&shiftid="+drpshiftid.value);
		return;
  }
  
  document.getElementById("drpshiftid").value='${param.shiftid}';
	
	$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
	
	
  function ReloadFilters()
  {	  	
	window.location="?a=showNozzleRegister&txtfromdate="+txtfromdate.value+"&shiftid="+drpshiftid.value;  	  
  }
  
  if("${param.shiftid}"!="")
  {
  	drpshiftid.value="${param.shiftid}";
  }
  else
  {
	drpshiftid.value=-1;
  }


  
  
 
  
 
  
</script>