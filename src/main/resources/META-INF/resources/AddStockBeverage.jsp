  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 

<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="itemList" value='${requestScope["outputObject"].get("itemList")}' />
<c:set var="app_type" value='${requestScope["outputObject"].get("app_type")}' />
<c:set var="todaysDateMinusOneMonth" value='${requestScope["outputObject"].get("todaysDateMinusOneMonth")}' />



<br>

<div class="container" style="padding:20px;background-color:white">

 <datalist id="itemList">
<c:forEach items="${itemList}" var="item">
			    <option id="${item.item_id}">${item.item_name}~${item.product_code}</option>			    
	   </c:forEach></select>	   	   	
</datalist>


<div class="container" style="padding:20px;background-color:white"> 

<form id="frm" action="?a=addStockStatusBeverage" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<input type="hidden" name="app_id" value="${userdetails.app_id}">
<input type="hidden" name="user_id" value="${userdetails.user_id}">
<input type="hidden" name="callerUrl" id="callerUrl" value="">


<div class="col-sm-12">
  	<div class="form-group">
  	<label for="email">Stock Date</label>	
  		<input type="text" id="txtdate" name="txtdate" class="form-control  form-control-sm" value="${todaysDate}" placeholder="Date" readonly/>
  	</div>
  </div>

<div class="col-sm-12">
  	<div class="form-group"> 
  	<label for="email">Item Name</label> 
  	  	<div class="input-group input-group-sm">
  	    
      <input type="text" class="form-control form-control-sm" id="txtitem"   placeholder="Search For Item" name="txtitem"  list='itemList' oninput="checkforMatchItem()">
	  <input type="hidden" name="hdnselecteditem" id="hdnselecteditem" value="">
	  <input type="hidden" name="hdnstocktype" id="hdnstocktype" value="${param.type}">
      <span class="input-group-append">
                    <button type="button" class="btn btn-danger btn-flat" onclick="resetCustomer()">Reset</button>
                  </span>  
                  </div>    
     
  </div>


   

  
     <div class="col-sm-12">
  	<div class="form-group"> 
  	<label for="email">Qty</label>     
    <input type="txtqty" class="form-control form-control-sm" id="txtqty" name="txtqty" >          
                
    </div>
  </div>


   <div class="col-sm-12">
  	<div class="form-group"> 
  	<label for="email">Remarks</label>     
    <input type="txtremarks" class="form-control form-control-sm" id="txtremarks" name="txtremarks" >          
                
    </div>
  </div>



  	 		  
	   	<button class="btn btn-success" type="button" id="btnsave" onclick='addStockStatusBeverage()'>Save</button>   
	   <button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>




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
	    	
	    	document.getElementById('itemList').innerHTML=reqString;
		}
	  };
	  xhttp.open("GET","?a=searchForCustomer&searchString="+searchString, true);    
	  xhttp.send();
	
	 
	
}

function checkforMatchItem()
{
	var searchString= document.getElementById("txtitem").value;	
	var options1=document.getElementById("itemList").options;
	
	
	var itemId=0;
	for(var x=0;x<options1.length;x++)
		{
			if(searchString==options1[x].value)
				{
				itemId=options1[x].id;
				
					break;
				}
		}
	if(itemId!=0)
		{	
			hdnselecteditem.value=itemId;
			txtitem.readOnly=true;
		}
	
}




function getPendingAmountForThisCustomer(customerId)
{
	document.getElementById("closebutton").style.display='none';
	   document.getElementById("loader").style.display='block';
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	var responseData=JSON.parse(xhttp.responseText);	   
			var details=responseData.reqData;
 	
	    	if(details.pendingAmountDetails.PendingAmount!=undefined)
	    		{
	    			txtpendingamount.value=details.pendingAmountDetails.PendingAmount;
	    		}
	    	else
	    		{
	    			alert("no pending amount for this customer");
	    			//window.location.reload();
	    		}
		}
	  };
	  xhttp.open("GET","?a=getPendingAmountForCustomer&customerId="+customerId, true);    
	  xhttp.send();
}


function savePayment()
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
		    	  	
		    	txtsearchcustomer.value="";
		    	txtsearchcustomer.disabled=false;		    	
		    	txtpendingamount.value="";
		    	txtpayamount.value="";
		    	txtremarks.value="";
		    	drppaymentmode.value="Cash";
		    	btnsavepayment.disabled=false;
			}
		  };
		  
		  xhttp.open("GET","?a=savePayment&app_id=${userdetails.app_id}&user_id=${userdetails.user_id}&store_id=${userdetails.store_id}&customerId="+hdnSelectedCustomer.value+"&payAmount="+txtpayamount.value+
				  "&paymentMode="+drppaymentmode.value+
				  "&txtdate="+txtdate.value+
				  
				  "&remarks="+txtremarks.value, true);    
		  xhttp.send();
		
}


function addStockStatusBeverage()
{	
	
	
	
	document.getElementById("frm").submit(); 
}


function showCustomer()
{
	window.location='?a=showSalesRegister&customerId='+hdnSelectedCustomer.value;
}

$( "#txtdate" ).datepicker({ dateFormat: 'dd/mm/yy' });

if('${param.type}'=="debit")
	{
		document.getElementById("divTitle").innerHTML="Debit Entry";
		document.title +=" Debitt Entry ";
	}
else
	{
		document.getElementById("divTitle").innerHTML="Stock Status Beverage";
		document.title +=" Stock Status Beverage";
	}
	

	function showLedgerForThisCustomer()
	{
		
		window.location="?a=showCustomerLedgerWithItem&customerId="+hdnSelectedCustomer.value+"&txtfromdate=${todaysDateMinusOneMonth}&txttodate=${todaysDate}";
	}
	
function resetCustomer()
{
	txtitem.value="";
	txtitem.readOnly=false;
}

  
</script>