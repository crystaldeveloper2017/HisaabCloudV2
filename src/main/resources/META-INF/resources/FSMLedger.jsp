
<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="ListLedger" value='${requestScope["outputObject"].get("ListLedger")}' />
<c:set var="totalDetails" value='${requestScope["outputObject"].get("totalDetails")}' />
<c:set var="employeeMaster" value='${requestScope["outputObject"].get("employeeMaster")}' />
<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />
<c:set var="employeeDetails" value='${requestScope["outputObject"].get("employeeDetails")}' />
<c:set var="totalDetails" value='${requestScope["outputObject"].get("totalDetails")}' />
<c:set var="openingBalanceForLedger" value='${requestScope["outputObject"].get("openingBalanceForLedger")}' />







<br>


<div class="card">

<br>

	<div class="row">
	
		<datalist id="employeeList">
			<c:forEach items="${employeeMaster}" var="employee">
				<option id="${employee.user_id}">${employee.name}</option>			    
			</c:forEach>	   	   	
		</datalist>
	            
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
		
		<div class="col-sm-3">
			<div class="form-group"> 
				<div class="input-group input-group-sm">
					<input type="text" class="form-control form-control-sm" id="txtsearchemployee" placeholder="Search for Employee" name="txtsearchemployee"  oninput="checkforLengthAndEnableDisable();checkforMatchEmployee();">
					<span class="input-group-append">
						<button type="button" class="btn btn-danger btn-flat" onclick="resetEmployee()">Reset</button>
					</span>  
				</div> 
				<input  type="hidden" name="hdnSelectedEmployee" id="hdnSelectedEmployee"  value="">
			</div>
		</div>
		<input  type="hidden" name="employeeId" id="employeeId" value="">
		
		<div class="col-sm-2" align="center">
			<div class="card-tools">
				<div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
					<div class="icon-bar" style="font-size:22px;color:firebrick">
						<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
						<a title="Download PDF" onclick="exportPDFForLedger()"><i class="fa fa-file-pdf-o"></i></a>
						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
					</div>           
				</div>
			</div>
		</div>
	
	</div>
	
	           
	              
	<div class="card-body table-responsive p-0">                
		<table id="testing"class="table table-head-fixed  table-bordered table-striped" role="grid" aria-describedby="example1_info">             
			<thead>
				<tr>                  
					<th colspan="3"><b>Employee Name:-${employeeDetails.name }</b></th>					
				</tr>
			</thead> 
		</table>
	</div>
	              
	
	            
	              
	              
	<!-- /.card-header -->
		<div class="card-body table-responsive p-0" >                
			<table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">             
				<thead>
					<tr>
						<th><b>Transaction Date</b></th>
						<th><b>Shift Name</b></th>
						<th><b>Sales Amount</b></th>
						<th><b>Payment Amount</b></th>
						<th><b>Difference</b></th>
						<th><b>Remarks</b></th>				
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ListLedger}" var="item">
						<tr >
							<td>${item.dt}</td>
							<td>${item.shift_name}</td>
							<td>${item.salesAmt}</td>
							<td>${item.paymentAmt}</td>
							<td>${item.diff}</td>	
							<td>${item.remarks}</td>						
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	<!-- /.card-body -->
	              
	              
		<div class="card-body table-responsive p-0">                
			<table id="testing"class="table table-head-fixed  table-bordered table-striped" role="grid" aria-describedby="example1_info">             
				<thead>
					<tr>                  
						
						<th colspan="1"><b>Opening Amount: ${openingBalanceForLedger}</b></th>                    
						<th colspan="1"><b>Sales Total: ${totalDetails.salesAmtSum}</b></th>                    
						<th colspan="1"><b>Payments Total: ${totalDetails.paymentAmtSum}</b></th>
						<th colspan="1"><b>Difference Amount: ${totalDetails.differenceSum}</b></th>
					</tr>
				<thead>
			</table>
		</div>
              
</div>
            
            
            
            



<script >
  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,      
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 50,
      //"order": [[ 4, 'desc' ]],
      "zeroRecords": " "

    });
  });
  
  document.getElementById("divTitle").innerHTML="FSM Ledger Report";
  document.title +=" Employee Ledger Report ";
  

  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });



  
  

  
  function exportPDFForLedger()
{
	
	  window.open("?a=exportFsmLedgerAsPDF&fromDate="+txtfromdate.value+"&toDate="+txttodate.value+"&employeeId="+hdnSelectedEmployee.value);
		return;

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
  
  function ReloadFilters()
  {	  
  	  window.location="?a=showFSMLedger&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value+"&employeeId="+hdnSelectedEmployee.value;  	  
  }
  
  function checkforMatchEmployee()
  {
  	var searchString= document.getElementById("txtsearchemployee").value;
  	var options1=document.getElementById("employeeList").options;
  	var employeeId=0;
  	for(var x=0;x<options1.length;x++)
  		{
  			if(searchString==options1[x].value)
  				{
  					employeeId=options1[x].id;
  					break;
  				}
  		}
  	if(employeeId!=0)
  		{
  			document.getElementById("hdnSelectedEmployee").value=employeeId;			
  			document.getElementById("txtsearchemployee").disabled=true;
			ReloadFilters();      						      						
  		}        	
  	
  }
  function checkforLengthAndEnableDisable()
  {
  		if(txtsearchemployee.value.length>=3)
  			{
			txtsearchemployee.setAttribute("list", "employeeList");
  			}
  		else
  			{
				txtsearchemployee.setAttribute("list", "");
  			}
  }
  
  function resetEmployee()
  {	
  	txtsearchemployee.disabled=false;
  	txtsearchemployee.value="";
  	hdnSelectedEmployee.value="";
  	ReloadFilters();
  }

  txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';
 employeeId.value='${param.employeeId}';
  
  if('${param.employeeId}'!='')
	{
		
		txtsearchemployee.value='${employeeDetails.name}';//
		txtsearchemployee.disabled=true;
		
		hdnSelectedEmployee.value='${employeeDetails.user_id}';
		
		
	}
  
  
</script>