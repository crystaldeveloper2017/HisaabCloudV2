  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 

<c:set var="todaysDate"
 value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="userList"
	value='${requestScope["outputObject"].get("userList")}' />
	<c:set var="app_type"
	 value='${requestScope["outputObject"].get("app_type")}' />

<br>

<div class="container" style="padding:20px;background-color:white">

<datalist id="userList">
<c:forEach items="${userList}" var="employee">
			    <option id="${employee.user_id}">${employee.name}</option>			    
	   </c:forEach>	 	   	   	
</datalist>

<div class="row">

<div class="col-sm-2">
  	<div class="form-group">
  	<label for="email">Date</label>	
  		<input type="text" id="txtdate" name="txtdate" class="form-control  form-control-sm" value="${todaysDate}" placeholder="Date" readonly/>
  	</div>
  </div>

<div class="col-sm-6">
  	<div class="form-group"> 
  	<label for="email">Employee Name</label> 
  	  	<div class="input-group input-group-sm">
  	    
      <input type="text" class="form-control form-control-sm" id="drpattendantid"   placeholder="Search For Employee" name="drpattendantid"  list='userList' oninput="checkforMatchEmployee()">
      <span class="input-group-append">
                    <button type="button" class="btn btn-danger btn-flat" onclick="resetEmployee()">Reset</button>
                  </span>  
                  </div>    
      <input  type="hidden" name="hdnSelectedEmployee" id="hdnSelectedEmployee" value="">
	  <input  type="hidden" name="hdnSelectedEmployeeType" id="hdnSelectedEmployeeType" value="">      
    </div>
	
  </div>
  
  <div class="col-sm-3">
  	<div class="form-group"> 
  	<label for="email">Current Balance</label>     
      <input type="text" class="form-control form-control-sm" id="txtpendingamount" readonly=true >          
    </div>
  </div>
  
  <div class="col-sm-3">
  	<div class="form-group">
  	
  	<c:if test="${param.type eq 'debit'}">
  		<label for="email">Debit Amount</label>
  	</c:if>
  	
  	<c:if test="${param.type ne 'debit'}">
  		<label for="email">Credit Amount</label>
  	</c:if>
  	     
      <input type="text" class="form-control form-control-sm" id="txtpayamount" >          
    </div>
  </div>
  
  
  
  <c:if test="${param.type eq 'debit'}">  		
	    <select class='form-control form-control-sm' id="drppaymentmode" style="display:none">
	    </select>
  </c:if>
  
  
  
  
  
  <c:if test="${param.type ne 'debit'}">  		
	  <div class="col-sm-3">
	  	<div class="form-group"> 
	  	<label for="email">Payment Mode</label>
	  	
	  	 <select class='form-control form-control-sm' id="drppaymentmode">
						  				<option value="Cash">Cash</option>		  				
						  				<option value="Paytm">Paytm</option>
						  				<option value="Amazon">Amazon</option>
						  				<option value="Google Pay">Google Pay</option>
						  				<option value="Phone Pay">Phone Pay</option>
						  				<option value="Card">Card</option>						  				
						  				<option value="Kasar">Kasar</option>
										<option value="Cheque">Cheque</option>					  									  				
				  					</select>
	  	     
	                
	    </div>
	  </div>  
  </c:if>
  
  
    <div class="col-sm-3">
  	<div class="form-group"> 
  	<label for="email">Remarks</label>     
                   <input type="txtremarks" class="form-control form-control-sm" id="txtremarks" >          
                
    </div>
  </div>
  
  <div class="col-sm-12">
  	 <div class="form-group" align="center">
  	 	
  	 		  
	   	<button class="btn btn-success" type="button" id="btnsavepayment" onclick='saveEmployeePayment()'>Save</button>   
	   <button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>
   </div>
   </div>
 
  
  </div>


<script >
	
	
</script>



<script >
function searchForCustomer(searchString)
{	
	console.log(5);
	if(searchString.length<3){return;}

	document.getElementById("closebutton").style.display='none';
	   document.getElementById("loader").style.display='block';
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	var cusomerList=JSON.parse(xhttp.responseText);
	    	var reqString="";
	    	for(var x=0;x<cusomerList.length;x++)
	    	{
	    		//console.log(cusomerList[x]);
	    		reqString+="<option id="+cusomerList[x].customer_id+">"+cusomerList[x].customer_name+"-"+cusomerList[x].mobile_number+"-"+cusomerList[x].customer_type+"</option>";
	    	}
	    	
	    	document.getElementById('userList').innerHTML=reqString;
		}
	  };
	  xhttp.open("GET","?a=searchForEmployee&searchString="+searchString, true);    
	  xhttp.send();
	
	 
	
}

function checkforMatchEmployee()
{
	var searchString= document.getElementById("drpattendantid").value;
	if(searchString.length<3){return;}
	var options1=document.getElementById("userList").options;
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
			document.getElementById("drpattendantid").disabled=true;			
			document.getElementById("hdnSelectedEmployeeType").value=document.getElementById("drpattendantid").value.split("-")[2];
			getPendingAmountForThisEmployee(employeeId);			
		}
	else
		{
			//searchForCustomer(searchString);
		}
	
	
}

function getPendingAmountForThisEmployee(employeeId)
{
	document.getElementById("closebutton").style.display='none';
	   document.getElementById("loader").style.display='block';
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	var details=JSON.parse(xhttp.responseText);	    	
	    	if(details.pendingAmountDetails.PendingAmount!=undefined)
	    		{
	    			txtpendingamount.value=details.pendingAmountDetails.PendingAmount;
	    		}
	    	else
	    		{
	    			alert("no pending amount for this employee");
	    			//window.location.reload();
	    		}
		}
	  };
	  xhttp.open("GET","?a=getPendingAmountForEmployee&employeeId="+employeeId, true);    
	  xhttp.send();
}


function saveEmployeePayment()
{
	
	if(txtpayamount.value=='')
	{
			alert('Amount is Mandatory field');			
			return;
	}
	
	if('${param.type}'!='debit' && '${todaysDate}'!=txtdate.value && '${app_type}'!='PetrolPump')
	{
		alert('Only Todays Entry is allowed');			
		return;
	}
		
	btnsavepayment.disabled=true;
	
	document.getElementById("closebutton").style.display='none';
	   document.getElementById("loader").style.display='block';
	//$('#myModal').modal({backdrop: 'static', keyboard: false});;
	
	
	
	
	
	
	
	
	
	var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() 
		  {
		    if (xhttp.readyState == 4 && xhttp.status == 200) 
		    { 		
		    	var details=JSON.parse(xhttp.responseText);
		    	//$("#myModal").modal('hide');
		    	
		    		toastr["success"]("Record Updated Successfully");
		    	toastr.options = {"closeButton": false,"debug": false,"newestOnTop": false,"progressBar": false,
		    	  "positionClass": "toast-top-right","preventDuplicates": false,"onclick": null,"showDuration": "1000",
		    	  "hideDuration": "500","timeOut": "500","extendedTimeOut": "500","showEasing": "swing","hideEasing": "linear",
		    	  "showMethod": "fadeIn","hideMethod": "fadeOut"} 
		    	  	
		    	drpattendantid.value="";
		    	drpattendantid.disabled=false;		    	
		    	txtpendingamount.value="";
		    	txtpayamount.value="";
		    	txtremarks.value="";
		    	drppaymentmode.value="Cash";
		    	btnsavepayment.disabled=false;
			}
		  };
		  
		  xhttp.open("GET","?a=saveEmployeePayment&app_id=${userdetails.app_id}&user_id=${userdetails.user_id}&employeeId="+hdnSelectedEmployee.value+"&payAmount="+txtpayamount.value+
				  "&paymentMode="+drppaymentmode.value+
				  "&txtdate="+txtdate.value+
				  
				  "&remarks="+txtremarks.value, true);    
		  xhttp.send();
		
}


function showEmployee()
{
	window.location='?a=showSalesRegister&employeeId='+hdnSelectedEmployee.value;
}

$( "#txtdate" ).datepicker({ dateFormat: 'dd/mm/yy' });

if('${param.type}'=="debit")
	{
		document.getElementById("divTitle").innerHTML="Debit Entry";
		document.title +=" Debitt Entry ";
	}
else
	{
		document.getElementById("divTitle").innerHTML="Employee Payment";
		document.title +=" Employee Payment ";
	}
	
	
function resetEmployee()
{
	drpattendantid.disabled=false;
	drpattendantid.value="";
	hdnSelectedEmployee.value=0;
	txtpendingamount.value="";
}



</script>