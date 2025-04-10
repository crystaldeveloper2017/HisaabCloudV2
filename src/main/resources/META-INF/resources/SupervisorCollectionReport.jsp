<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />

<c:set var="lstSupervisorCollection" value='${requestScope["outputObject"].get("lstSupervisorCollection")}' />
<c:set var="lstUsers" value='${requestScope["outputObject"].get("lstUsers")}' />
<c:set var="attendantId" value='${requestScope["outputObject"].get("attendantId")}' />
<c:set var="totalAmount" value='${requestScope["outputObject"].get("totalAmount")}' />




<br>

 <div class="card">
 <br>
 <div class="row">

	
		
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
		<td><input type="text" id="txttodate" onchange="checkforvalidfromtodate();ReloadFilters();" name="txttodate" readonly class="form-control date_field"  placeholder="To Date"/></td>						
        </div>
	</div>
	
	<div class="col-sm-3">
	
			<select class="form-control" name="drpattendantid" id="drpattendantid" onchange="ReloadFilters()">
      <c:forEach items="${lstUsers}" var="user">
			    <option value="${user.user_id}">${user.name}</option>    
	   </c:forEach></select>
		</div>
	
	
	<div class="col-sm-2" align="center">
		<div class="card-tools">
			<div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
				<div class="icon-bar" style="font-size:22px;color:firebrick">
				<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
 						<a title="Download PDF" onclick="exportSalesRegister()"><i class="fa fa-file-pdf-o"></i></a>
  						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a> 
				</div>          
			</div>
		</div>
	</div>
				
</div>
	<br>
              
              
               
                
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 480px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
                    <tr>
                    <th><b>Collection Id</b></th>                  
                    <th><b>Shift name</b>
                    <th><b>Attendant name</b>
                    <th><b>Supervisor name</b>
                    <th><b>Amount</b>
                    <th><b>Collection Date</b>
                    <th><b>Updated Date</b></th>
                     
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${lstSupervisorCollection}" var="data1">
					<tr >
					  
					  
					  <td>${data1.collection_id}</td>
					  <td>${data1.shift_name}</td>
					  
					  <td>${data1.AttendantName}</td>
					  
					  <td>${data1.SupervisorName}</td>
					   <td>${data1.amount}</td>
					   <td>${data1.collection_date}</td>
					  <td>${data1.updated_date}</td>
					  
					  <td><button class="btn btn-danger" onclick="deleteSupervisorTransaction('${data1.collection_id}')">Delete</button></td>
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
             
</div>
            
          <div class="row">

	
		
<div class="col-12" align="Center">
		<label for="txtfromdate">Total Amount ${ totalAmount}</label>
            </div>
   

<script >


function deleteSupervisorTransaction(collection_id)
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
	  xhttp.open("GET","?a=deleteSupervisorTransaction&collection_id="+collection_id, true);    
	  xhttp.send();
}


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


  txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';
 
  
  
  function exportSalesRegister2()
  {
	try
	{
	
		window.open("?a=exportSalesRegister2&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value+"&customerId="+hdnSelectedCustomer.value);
		return;
		
	  var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() 
		  {
		    if (xhttp.readyState == 4 && xhttp.status == 200) 
		    { 		      
		    	//window.location="BufferedImagesFolder/"+xhttp.responseText;
		    	window.open("BufferedImagesFolder/"+xhttp.responseText,'_blank','height=500,width=500,status=no, toolbar=no,menubar=no,location=no');
			}
		  };
		  xhttp.open("GET","?a=exportSalesRegister2", true);    
		  xhttp.send();
	}
	catch(ex)
	{
		alert(ex.message);
	}
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
  	 window.location="?a=showSupervisorCollection&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value+"&attendant_id="+drpattendantid.value;		  
  }
  
  function checkforMatchCustomer()
  {
  	var searchString= document.getElementById("txtsearchcustomer").value;
  	if(searchString.length<3){return;}
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
  	
  }
  
  function checkforLengthAndEnableDisable()
  {
  		if(txtsearchcustomer.value.length>=3)
  			{
  				txtsearchcustomer.setAttribute("list", "hdnSelectedCustomer");
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
  
  document.getElementById("divTitle").innerHTML="SuperVisor Collection Report";
  document.title +=" SuperVisor Collection Report ";
  
  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  
  drpattendantid.value='${attendantId}';

</script> 