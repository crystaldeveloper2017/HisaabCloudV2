<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>
<style>
	.aligncenterclass
	{
		text-align:center;
	}

	hr {
    display: block;
    height: 1px;
    border: 0;
    border-top: 1px solid #ccc;
    margin: 1em 0;
    padding: 0;
    background:brown;
}


</style>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
           
           


<c:set var="SupervisorDetails" value='${requestScope["outputObject"].get("SupervisorDetails")}' />
<c:set var="userList" value='${requestScope["outputObject"].get("userList")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="txtinvoicedate" value='${requestScope["outputObject"].get("txtinvoicedate")}' />

<c:set var="lstOfShifts" value='${requestScope["outputObject"].get("lstOfShifts")}' />

<c:set var="lstOfSwipeMaster" value='${requestScope["outputObject"].get("lstOfSwipeMaster")}' />
<c:set var="customerMaster" value='${requestScope["outputObject"].get("customerMaster")}' />
<c:set var="vehicleMaster" value='${requestScope["outputObject"].get("vehicleMaster")}' />
<c:set var="itemList" value='${requestScope["outputObject"].get("itemList")}' />
<c:set var="suggestedShiftId" value='${requestScope["outputObject"].get("suggestedShiftId")}' />


<c:set var="invoiceDetails" value='${requestScope["outputObject"].get("invoiceDetails")}' />



   





</head>

<script src="js/searchinvoicebattery.js"></script>
<script >


function saveInvoice()
{	
	
	

	
	if((txtpaymenttype.value=="Pending" || txtpaymenttype.value =="Partial") && hdnSelectedCustomer.value =="") 
		{
			alert("This Payment type is not supported for Unknown customers");
			return;
		}
	btnsave.disabled=true;

	var rows=tblitems.rows;
	
	var requiredDetails=[];
	 
	var arr = [];
	var itemString="";
	var confirmMessage="";
	var proceedFlag=true;
	var messageToShow="";
	for (var x= 1; x < rows.length; x++) 
	{   
		
		
	    itemString+=
	    	rows[x].childNodes[0].childNodes[0].value.split('~')[0]+ // ID
	    "~"+Number(rows[x].childNodes[1].childNodes[0].childNodes[1].value)+ // QTY
	    "~"+Number(rows[x].childNodes[1].childNodes[1].value)+ // RATE
	    "~"+Number(rows[x].childNodes[1].childNodes[1].value)+ // Custom RATE	    	    
	    "~"+rows[x].childNodes[0].childNodes[1].innerHTML+ // Item Name
		"~0~0~0~0~0~0~0"+ // Item Name
		"~"+rows[x].childNodes[0].childNodes[0].value.split('~')[1]+ // Purchase Detail Id
		"~"+rows[x].childNodes[3].childNodes[0].value+ // Battery No
		"~"+rows[x].childNodes[4].childNodes[0].value+ // Vehicle Name
		"~"+rows[x].childNodes[5].childNodes[0].value+ // Vehicle No
		"~"+rows[x].childNodes[6].childNodes[0].value+ // Warranty		
	    "|";       
	}
	
 	
	
	
	var reqString="customer_id="+hdnSelectedCustomer.value+
	"&gross_amount="+grossAmount.innerHTML+	
	"&total_amount="+totalAmount.innerHTML+
	"&payment_type="+txtpaymenttype.value+
	"&payment_mode="+drppaymentmode.value+
	"&paid_amount=0"+
	"&item_discount=0"+
	"&invoice_discount=0"+
	"&invoice_date="+txtinvoicedate.value+
	"&remarks="+
	"&appId=${userdetails.app_id}"+
	"&store_id=${userdetails.store_id}"+
	"&user_id=${userdetails.user_id}"+
	"&total_gst=0"+
	"&total_cgst=0"+
	"&total_sgst=0"+	
	"&itemDetails="+itemString; 
	console.log(reqString);
	
	
	
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) 
	    {
	    	var invoiceId=this.responseText.split("~");
	      	//alert("Invoice Saved Succesfully"+invoiceId[0]);
	      	
	      	toastr["success"]("Invoice Saved Succesfully "+invoiceId[0]);
	    	toastr.options = {"closeButton": false,"debug": false,"newestOnTop": false,"progressBar": false,
	    	  "positionClass": "toast-top-right","preventDuplicates": false,"onclick": null,"showDuration": "1000",
	    	  "hideDuration": "500","timeOut": "500","extendedTimeOut": "500","showEasing": "swing","hideEasing": "linear",
	    	  "showMethod": "fadeIn","hideMethod": "fadeOut"
	    	}
	    	
	    	//alert(txtinvoicedate.value);
	    	window.location="?a=showGenerateInvoice&txtinvoicedate="+txtinvoicedate.value;	    		
	      	return;
	      	
	      	if(typeof chkprintinvoice !='undefined' && chkprintinvoice.checked==true)
      		{      		
	      		
      			generateInvoicePdfPrint(invoiceId[0],txtcustomerpendingamount.value);
      			btnsave.disabled=false;
    	      	document.getElementById("divTitle").innerHTML="Generate Invoice New : "+(Number(invoiceId[0])+1);
    	      	document.title +=" Generate Invoice New : " +(Number(invoiceId[0])+1);
      			return;      			
      		}
	      	
	      	
	      	if(typeof chkgeneratePDF !='undefined' && chkgeneratePDF.checked==true)
	      		{
	      			generateInvoice(invoiceId[1]);	      			
	      		}	  
	      	
	      	resetField();
	      	paymentTypeChanged(txtpaymenttype.value);
	      	
	      	 if(invoiceId.length==3)
	      	    {
	      	    	window.location=invoiceId[2];	
	      	    }
	      	btnsave.disabled=false;
	      	document.getElementById("divTitle").innerHTML="Generate Invoice New : "+(Number(invoiceId[0])+1);
	      	document.title +=" Generate Invoice New : " +(Number(invoiceId[0])+1);
	    	  
	      
	    }
	  };
	  xhttp.open("POST", "?a=saveInvoice", true);
	  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");	  
	  xhttp.send(encodeURI(reqString));
	
	 
}















</script>



<br>

<div class="container" style="padding:5px;background-color:white;max-width:100%">
<form id="frm"  method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<input type="hidden" name="app_id" value="${userdetails.app_id}">
<input type="hidden" name="user_id" value="${userdetails.user_id}">
<input type="hidden" name="callerUrl" id="callerUrl" value="">

<div class="row">

<div class="col-sm-3">
  	<div class="form-group">
  	<div class="input-group input-group-sm">
                  <input type="text" class="form-control form-control-sm" id="customerName"
                   placeholder="New Customer Name" name="customerName">                           
    </div>
    </div>
  </div>
  
  <div class="col-sm-3">
  	<div class="form-group">
  	<div class="input-group input-group-sm">
                  <input type="text" class="form-control form-control-sm" id="mobileNumber"
                   placeholder="Mobile Number" name="mobileNumber" maxlength="10" onkeypress="digitsOnly(event)">                           
    </div>
    </div>
  </div>
  
  <div class="col-sm-3">
  	<div class="form-group">
  	<div class="input-group input-group-sm">  		
  				<select id="customerType" name="customerType" class="form-control" >
  				
  			
  				
  			<option value="LoyalCustomer1">Loyal Customer 1</option>
	  				<option value="LoyalCustomer2">Loyal Customer 2</option>
  				
  				
  				<option value="LoyalCustomer3">Loyal Customer 3</option>
  				<option value="Franchise">Franchise</option>
  				<option value="WholeSeller">WholeSeller</option>
  				<option value="Distributor">Distributor</option>
  				<option value="Business2Business">Business2Business</option>
  				
  				<c:if test="${userdetails.app_id eq '1'}">
  					<option value="shrikhand">Shrikhand Buyers</option>
  				</c:if>
  				
  				<option value="New Customer" selected>New Customer</option>
  				</select>                                             
    </div>
    </div>
  </div>
  
  
  
  
  
  <div class="col-sm-3" >
  	<div class="form-group" >
  	<div class="input-group input-group-sm">
                 <span class="input-group-append" >
                    <button  type="button" class="btn btn-primary btn-flat" onclick="quickAddCustomer()">Quick Add Customer</button>
                  </span>                         
    </div>
	
	</div>
	
	</div>


	
    

			<datalist id="customerList">
				<c:forEach items="${customerMaster}" var="customer">
					<option id="${customer.customerId}">${customer.customerName}~${customer.mobileNumber}~${customer.customerType}</option>
				</c:forEach>
			</datalist>

			<datalist id="vehicleList">
				<c:forEach items="${vehicleMaster}" var="vehicle">
					<option >${vehicle}</option>
				</c:forEach>
			</datalist>
			
			<datalist id="itemList">
<c:forEach items="${itemList}" var="item">
			    <option id="${item.item_id}"> ${item.category_name}  ${item.item_name} (${item.product_code}) ~ PR ${item.PurchaseInvoiceNo} ~${item.purchase_details_id} ~ ${item.QtyAvailable}</option>
			    <input type="hidden" id="hdn${item.item_id}" value="${item.item_name}~${item.price}~${item.wholesale_price}~${item.franchise_rate}~${item.loyalcustomerrate3}~${item.loyalcustomerrate2}~${item.loyalcustomerrate1}~${item.distributor_rate}~${item.b2b_rate}~${item.shrikhand}~${item.qty_available}~${item.sgst}~${item.cgst}~${item.category_name}">			    
	   </c:forEach>	   	   	
</datalist>
			
			
			<div class="col-2">
  	<div class="form-group">
	
  	<label for="email">Invoice Date</label>
  		<input type="text" id="txtinvoicedate"  name="txtinvoicedate" class="form-control form-control-sm" value="" placeholder="Invoice Date" readonly/>
  	</div>
  </div>


  

  
   
   <div class="col-1">
  	<div class="form-group">
      
      <label for="email">Payment Type</label>
     <select class='form-control form-control-sm' onchange="paymentTypeChanged(this.value)" id="txtpaymenttype">
	                				<option value="Paid">Paid</option>	                				
	                				<option value="Pending">Pending</option>
	                			</select>  
            
    </div>
  </div>

  <div class="col-4">
	<div class="form-group">
	
	
	
		  <label for="email">Customer Name</label>
	
	<div class="input-group input-group-sm">
				<input type="text" class="form-control form-control-sm" id="txtsearchcustomer"    placeholder="Search For Customer" name="txtsearchcustomer"  autocomplete="off" list='customerList' oninput="checkforMatchCustomer()">
				
				<span class="input-group-append">
				  <button type="button" class="btn btn-danger btn-flat" onclick="resetCustomer()">Reset</button>
				</span>
				
				
  </div>
	
	
				
	
		  
	<input  type="hidden" name="hdnSelectedCustomer" id="hdnSelectedCustomer" value=""> 
			 <input  type="hidden" name="hdnSelectedCustomerType" id="hdnSelectedCustomerType" value="">
			 <input  type="hidden" name="hdnPreviousInvoiceId" id="hdnPreviousInvoiceId" value="">
				   
  </div>
</div>

 <div class="col-1" name="paymentModeElements">
  	<div class="form-group">
      
      <label for="email">Payment Mode</label>
      <select class="form-control form-control-sm" name="drppaymentmode" id="drppaymentmode" >      	    
			    <option value="Cash">Cash</option>
			    <option value="Card">Card</option>				
	   </select>
            
    </div>
  
    
  </div>
  
   <div class="col-sm-4" id="divsearchforitems">
  	<label for="email">Select Item</label>
    
    <div class="input-group">
    <input type="text" class="form-control form-control-sm"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="checkforMatchItem()">
    
    
    
    
  </div>
  </div>
 
  </div>
  
    
  
  
 

  
 
  
  
    <div class="col-sm-12">  
	  <div class="card-body table-responsive p-0" style="height: 500px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center">
	                     
	  			<th style="z-index:0">Item Name</th>
	  			<th style="z-index:0">Item Qty</th>
				<th style="z-index:0">Amount</th>
	  			<th style="z-index:0">Battery No</th>
				<th style="z-index:0">Vehicle Name</th>
				<th style="z-index:0">Vehicle No</th>
				<th style="z-index:0">Warranty (Months)</th>



	  			
	  			
	                    </tr>
	                  </thead>
	                </table>
	   </div>	
  </div>
  
  
  
 
  <div class="col-sm-6" align="center">
  	<div class="form-group">
      
            <span  >Total Qty : </span>
            <span style='font-weight:800' id="lbltotalqty"></span>
      
            
    </div>
  </div>
   
  <div class="col-sm-6" align="center">
  	<div class="form-group">
  	
  	
  	<span  >Total Amount :</span>
            <span style='font-weight:800' id="totalAmount"></span>
      
            
            
            
            <span style='display:none' id="grossAmount">0</span>
            
            
      
            
    </div>
  </div>
  
  
  
   
  
  <br>
		<div class="col-sm-12" align="center">
  	
    
    
<button class="btn btn-success" id="btnsave" type="button" onclick='saveInvoice()'>Save</button>
		<button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>    
  
  </div>
		
		
		
		
		
		
		
		
</div>
</form>

<script >
	

document.getElementById("divTitle").innerHTML="Generate Invoice Battery";
document.title +=" Generate Invoice Battery ";

if('${param.order_id}'!='')
	{
		drppaymentmode.value="Paytm";
		drppaymentmode.disabled=true;
		divsearchforitems.style="display:none";
		txtpaymenttype.disabled=true;
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
				document.getElementById("hdnSelectedCustomerType").value=document.getElementById("txtsearchcustomer").value.split("~")[2];
				
			}
		else
			{
				//searchForCustomer(searchString);
			}
		
		
		
	}

	function checkforMatchCustomerVehicle()
	{
		
		var customerId=document.getElementById("hdnSelectedCustomer").value;;	
		var options1=document.getElementById("vehicleList").options;
		var txtsearchcustomervehicle=document.getElementById("txtsearchcustomervehicle");
		var vehicleId=0;
		for(var x=0;x<options1.length;x++)
			{
				if(searchString==options1[x].value)
					{
						vehicleId=options1[x].id;
						
						break;
					}
			}
		if(vehicleId!=0)
			{
				document.getElementById("hdnSelectedCustomervehicle").value=vehicleId;			
				document.getElementById("txtsearchcustomervehicle").disabled=true;			
					
			}	

			
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	var details=JSON.parse(xhttp.responseText);	    	
	    	console.log("checkforMatchCustomerVehicle");
			console.log(details);
			var reqString="";
			for(var m=0;m<details.length;m++)
			{
				reqString+="<option value="+details[m].vehicle_id+">"+details[m].vehicle_name+"~ "+details[m].vehicle_number+" </option>"
			}
			drpvehicledetails.innerHTML=reqString;

		}
	  };
		xhttp.open("GET","?a=getVehicleIdForCustomer&customerId="+customerId, true);    
	  	xhttp.send();
		
	}
	
	
	function checkforMatchItem()
	{		
		var searchString= document.getElementById("txtitem").value;	
		var options1=document.getElementById("itemList").options;
		
		var itemId=0;
		var purchaseDetailsId=0; 
		for(var x=0;x<options1.length;x++)
			{
				if(searchString==options1[x].value)
					{
					itemId=options1[x].id;
					purchaseDetailsId=options1[x].innerHTML.split('~')[2];				
						break;
					}
			}
		if(itemId!=0)
			{
					// code to check if item already exist inselection				
					getItemDetailsAndAddToTable(itemId,purchaseDetailsId);
					document.getElementById("txtitem").value="";
			}
		else
			{	
				var count=0;
				var ReqString="";
				for(var x=0;x<options1.length;x++)
					{
					var prodCode = options1[x].value.substring(
							options1[x].value.indexOf("(") + 1, 
							options1[x].value.lastIndexOf(")")
						);
						if(prodCode==(searchString.toLowerCase()))
							{
								count++;
								ReqString=options1[x].value;
							}
					}
				
				if(count==1)
					{
						document.getElementById("txtitem").value=ReqString;
						checkforMatchItem();
					}
				
			}
		
	}


	function getItemDetailsAndAddToTable(itemId,purchaseDetailsId)
	{
			var itemDetails=document.getElementById("hdn"+itemId).value.split("~");		    	
		    	console.log(itemDetails);
		    	var table = document.getElementById("tblitems");	    	
		    	var row = table.insertRow(-1);	    	
		    	var cell1 = row.insertCell(0);
		    	var cell2 = row.insertCell(1);
		    	var cell3 = row.insertCell(2);
				var cell4 = row.insertCell(3);
				var cell5 = row.insertCell(4);
				var cell6 = row.insertCell(5);
				var cell7 = row.insertCell(6);
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	cell1.innerHTML = "<input type='hidden' value='"+itemId+"~"+purchaseDetailsId+"'><a onclick=window.open('?a=showItemHistory&itemId="+itemId+"') href='#'>"+ itemDetails[13] + itemDetails[0] + " "+ "(" + itemDetails[10]+ " ) </a>";
		    	
		    	cell1.style.width = '10%';
		    	
		    	cell2.innerHTML = '<div class="input-group"><span class="input-group-btn"><button id="btnminus'+itemId+'" class="btn btn-info" type="button" onclick="addremoveQuantity('+itemId+',0)"><i class="fa fa-minus"></i></button></span><input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty'+itemId+'" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="1"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty'+itemId+'" value='+itemDetails[10]+'><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity('+itemId+',1)"><i class="fa fa-plus"></i></button></span></div><input type="hidden" value='+itemDetails[1]+' id="txtrate'+itemId+'">';
		    	cell2.style.width = '10%';

				cell3.innerHTML = '<input type="text" onfocus="this.select()" style="width:100%" class="form-control" value="'+itemDetails[1]+'">';
		    	cell3.style.width = '10%';

				cell4.innerHTML = '<input type="text" onfocus="this.select()" style="width:100%" class="form-control">';
		    	cell4.style.width = '20%';

				cell5.innerHTML = '<input type="text" onfocus="this.select()" list="vehicleList" style="width:100%" class="form-control">';
		    	cell5.style.width = '20%';

				cell6.innerHTML = '<input type="text" onfocus="this.select()" style="width:100%" class="form-control">';
		    	cell6.style.width = '30%';

				cell7.innerHTML = '<input type="text" onfocus="this.select()" style="width:100%" class="form-control">';
		    	cell7.style.width = '30%';

				
		    	

		    	
		    	//cell4.innerHTML = '<button type="button" class="btn btn-sm btn-danger"  onclick=removethisitem(this) id="btn11" style="cursor:pointer">0</button>';
		    	
		    	
		    	
		    	document.getElementById("txtqty"+itemId).select();
		    	document.getElementById("txtqty"+itemId).focus();

				calculateTotal();
		    	
				
	}


	function calculateAmount(itemId)
	{		
		var rate=document.getElementById('txtrate'+itemId).value;
		var qty=document.getElementById('txtqty'+itemId).value;
		var amount=(Number(rate) *Number(qty) ).toFixed(2);
		document.getElementById("txtamount"+itemId).value=amount;
		calculateTotal();
	}
	
	function calculateTotal()
	{	
			var total=0;
			var totalQtyCalculated=0;
			
			var grossAmountCalculated=0;
			

			
			var rows=tblitems.rows;
			for(var x=1;x<rows.length;x++)
				{
					
					
					
					var itemQty=Number(rows[x].childNodes[1].childNodes[0].childNodes[1].value);
					var amount=Number(rows[x].childNodes[2].childNodes[0].value);					
					
					totalQtyCalculated+=itemQty;
					
					var grossItemAmount=amount;
					grossAmountCalculated+=grossItemAmount;
				}
			
			
			
			
			totalAmount.innerHTML=Number(grossAmountCalculated).toFixed(3);
			lbltotalqty.innerHTML=Number(totalQtyCalculated).toFixed(3);
			grossAmount.innerHTML=grossAmountCalculated;
			
			
			
			
			
			
	}
	
	function removethisitem(btn1)
	{
		btn1.parentElement.parentElement.remove();
		//reshuffle id's next to this
		
		reshuffleSrNos();
		
		calculateTotal(); 
	}

	function addremoveQuantity(itemId,addRemoveFlag) // 0 removes and 1 adds
	{
		
		var	qtyElement=document.getElementById('txtqty'+itemId);
		var quantity=Number(qtyElement.value);
		
		if(quantity==1 && addRemoveFlag==0)
			{
				return;
			}
		
		if(addRemoveFlag==1)
			{
				quantity++;	
			}
		
		if(addRemoveFlag==0)
		{
			quantity--;	
		}
		
		qtyElement.value=quantity;
		formatQty(document.getElementById("txtqty"+itemId));
		calculateTotal();
	}
	
	function formatQty(qtyTextBox)
	{
		qtyTextBox.value=Number(qtyTextBox.value).toFixed(3);
			
	}
	
	function calculateQtyFromAmountAndAddToTable(itemId,amount)
	{

		// hardcoded this will need to fix later
		if (itemId!=587284 && itemId!=587285)
		{return;}


		var existingRate=document.getElementById("txtrate"+itemId).value;
		var calculatedQty=Number(amount)/Number(existingRate);
		
		
		document.getElementById("txtqty"+itemId).value=calculatedQty.toFixed(3);
		
		
		calculateTotal();
		
		
	}
	
	
	
	
	
	
	
	$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
	
	
	function paymentTypeChanged(selection)
	{
						
			
			if(selection=="Pending")
			{
				var paymentModeElements=document.getElementsByName('paymentModeElements');
				var customerVehicleElements=document.getElementsByName('customerVehicleElements');
				for(var x=0;x<paymentModeElements.length;x++)
				{
					paymentModeElements[x].style="display:none";
				}
				for(var x=0;x<customerVehicleElements.length;x++)
				{
					customerVehicleElements[x].style="display:";
				}
				
				drppaymentmode.value="NA";
			}
			else
			{
				var paymentModeElements=document.getElementsByName('paymentModeElements');
				var customerVehicleElements=document.getElementsByName('customerVehicleElements');
				for(var x=0;x<paymentModeElements.length;x++)
				{
					paymentModeElements[x].style="display:";
				}
				for(var x=0;x<customerVehicleElements.length;x++)
				{
					customerVehicleElements[x].style="display:none";
				}
				drppaymentmode.value="Cash";
			}
			
	}


	




if("${param.txtinvoicedate}"!="")
{
	txtinvoicedate.value="${param.txtinvoicedate}";
}
else
{	
	txtinvoicedate.value="${todaysDate}";
}




if("${param.invoice_id}"!="")
{

	txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";		
	
	txtpaymenttype.value="${invoiceDetails.payment_type}";
	drppaymentmode.value="${invoiceDetails.payment_mode}";
	txtsearchcustomer.value="${invoiceDetails.customer_name}";
	hdnSelectedCustomer.value="${invoiceDetails.customer_id}";
	

	
	


	





	var m=0;
	<c:forEach items="${invoiceDetails.listOfItems}" var="item">
		
		m++;
		tableNo='${item.table_no}';
		var table = document.getElementById("tblitems");	    	
    	var row = table.insertRow(-1);	    	
    	var cell1 = row.insertCell(0);
    	var cell2 = row.insertCell(1);
    	var cell3 = row.insertCell(2);
    	
    	
    	
    	
    	
    	cell1.innerHTML = '<a href="#">${item.item_name} ( )</a>';
    	
    	cell2.innerHTML = '<div class="input-group"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},0)"><i class="fa fa-minus"></i></button></span><input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty${item.item_id}" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="${item.qty}"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty${item.item_id}" value="${item.qty}"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},1)"><i class="fa fa-plus"></i></button></span></div>';
    	
    	var itemTotal=Number('${item.custom_rate}') * Number('${item.qty}');
    	cell3.innerHTML ='<input typ="text" class="form-control form-control-sm" value="'+itemTotal+'">';
    	
    	
		</c:forEach>

		calculateTotal();

		disableAllComponents();
		
	
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function quickAddCustomer()
{
	document.getElementById("closebutton").style.display='none';
	   document.getElementById("loader").style.display='block';
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	
	    	
	    	if(xhttp.responseText.split("~")[1]==0)
	    		{
	    			alert(xhttp.responseText.split("~")[0]);
	    			return;
	    		}
	    		
	    	txtsearchcustomer.disabled=true;
	    	txtsearchcustomer.value=customerName.value+"~"+mobileNumber.value+"~"+customerType.value;
	    	customerName.value="";
	    	mobileNumber.value="";
	    	
	    	hdnSelectedCustomer.value=xhttp.responseText.split("~")[1];
	    	hdnSelectedCustomerType.value=customerType.value;
		}
	  };
	  xhttp.open("GET","?a=saveCustomerServiceAjax&appId=${userdetails.app_id}"+"&customerName="+customerName.value+"&mobileNumber="+mobileNumber.value+"&customerType="+customerType.value, true);    
	  xhttp.send();
}



$('[data-widget="pushmenu"]').PushMenu("collapse");
</script>



