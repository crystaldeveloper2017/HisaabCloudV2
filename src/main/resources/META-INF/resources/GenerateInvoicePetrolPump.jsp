<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>
<style>
	.aligncenterclass
	{
		text-align:center;
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
	    "|";       
	}
	
 	var nozzDetails=drpnozzle.options[drpnozzle.selectedIndex].text.split("~");
	
	
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
	"&drpshiftid="+drpshiftid.value+
	"&slot_id="+$("#drpshiftid option:selected").text().split("~")[3]+
	"&vehicle_id="+drpvehicledetails.value+	
	"&nozzle_id="+drpnozzle.value+
	"&swipe_id="+drpmachinename.value+
	"&attendant_id="+nozzDetails[4]+
	"&paytm_order_id=${param.order_id}"+
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

			<datalist id="customerList">
				<c:forEach items="${customerMaster}" var="customer">
					<option id="${customer.customerId}">${customer.customerName}~${customer.mobileNumber}~${customer.customerType}</option>
				</c:forEach>
			</datalist>

			<datalist id="vehicleList">
				<c:forEach items="${vehicleMaster}" var="vehicle">
					<option id="${vehicle.vehicle_id}">${vehicle.vehicle_name}~${vehicle.vehicle_number}</option>
				</c:forEach>
			</datalist>
			
			<datalist id="itemList">
<c:forEach items="${itemList}" var="item">
			    <option id="${item.item_id}">${item.item_name} (${item.product_code}) ~ PR ${item.PurchaseInvoiceNo} ~${item.purchase_details_id} ~ ${item.QtyAvailable}</option>
			    <input type="hidden" id="hdn${item.item_id}" value="${item.item_name}~${item.price}~${item.wholesale_price}~${item.franchise_rate}~${item.loyalcustomerrate3}~${item.loyalcustomerrate2}~${item.loyalcustomerrate1}~${item.distributor_rate}~${item.b2b_rate}~${item.shrikhand}~${item.qty_available}~${item.sgst}~${item.cgst}">			    
	   </c:forEach>	   	   	
</datalist>
			
			
			<div class="col-6">
  	<div class="form-group">	
  	<label for="email">Invoice Date</label>
  		<input type="text" id="txtinvoicedate" onchange="getAttendantList()" name="txtinvoicedate" class="form-control form-control-sm" value="" placeholder="Invoice Date" readonly/>
  	</div>
  </div>

			<div class="col-6">
  	<div class="form-group">
      
    <label for="email">Shift Name</label>  
      <select class="form-control form-control-sm" name="drpshiftid" id="drpshiftid" onchange="getAttendantList()">
      <option value="-1">----------Select----------</option>
      <c:forEach items="${lstOfShifts}" var="shift">
		<c:if test="${shift.shift_name ne '3'}">
			    <option value="${shift.shift_id}">${shift.shift_name}~${shift.from_time}~${shift.to_time}~0</option>    
		</c:if>

		<c:if test="${shift.shift_name eq '3'}">
			<option value="${shift.shift_id}">${shift.shift_name}~22:00:00~00:00:00~0</option>
			<option value="${shift.shift_id}">${shift.shift_name}~00:00:00~06:00:00~1</option>
		</c:if>
	


	   </c:forEach>
	   
	</select>
            
    </div>
  </div>
  
   <div class="col-6">
  	<div class="form-group">
      
      <label for="email">Nozzle</label>
      <select class="form-control form-control-sm" name="drpnozzle" id="drpnozzle" onchange="checkForNozzleChangeAndGetItem()">
      <option value="-1">----------Select----------</option>
	  
	  </select>
    </div>
  </div>
  
  
   
   <div class="col-6">
  	<div class="form-group">
      
      <label for="email">Payment Type</label>
     <select class='form-control form-control-sm' onchange="paymentTypeChanged(this.value)" id="txtpaymenttype">
	                				<option value="Paid">Paid</option>	                				
	                				<option value="Pending">Pending</option>
	                			</select>  
            
    </div>
  </div>

  <div class="col-6">
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

  
    <div class="col-6" name="customerVehicleElements">
  	<div class="form-group">
      
      <label for="txtsearchcustomervehicle">Customer Vehicle</label>
      <select class="form-control form-control-sm" id="drpvehicledetails"    placeholder="Search For Vehicle" name="drpvehicledetails">
      </select>            
            
    </div>
  </div>
  
  
  <div class="col-6" name="paymentModeElements">
  	<div class="form-group">
      
      <label for="email">Payment Mode</label>
      <select class="form-control form-control-sm" name="drppaymentmode" id="drppaymentmode" onchange="checkIfMachinetoBeDisplayed()">      	    
			    <option value="Cash">Cash</option>
			    <option value="Card">Card</option>
				<option value="LoyaltyPoints">Loyalty Points</option>
	   </select>
            
    </div>
  </div>
  
    <div class="col-6" id="divmachinename" name="divmachinename" style="display:none">
  	<div class="form-group">
      <label for="email">Machine Name</label>
      
      <select class="form-control form-control-sm" name="drpmachinename" id="drpmachinename">
      <c:forEach items="${lstOfSwipeMaster}" var="swipe">
			    <option value="${swipe.swipe_machine_id}">${swipe.swipe_machine_bank} - ${swipe.swipe_machine_short_name} - ${swipe.swipe_machine_name}</option>    
	   </c:forEach></select>
            
    </div>
  </div>
  
  
 

  
  <div class="col-sm-12" id="divsearchforitems">
  	
    
    <div class="input-group">
    <input type="text" class="form-control form-control-sm"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="checkforMatchItem()">
    
    
    
    
  </div>
  </div>
  
  
    <div class="col-sm-12">  
	  <div class="card-body table-responsive p-0" style="height: 370px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center">
	                     
	  			<th style="z-index:0">Item Name</th>
	  			<th style="z-index:0">Item Qty</th>
	  			<th style="z-index:0">Amount</th>
	  			
	  			
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
	

document.getElementById("divTitle").innerHTML="Generate Invoice Fuel";
document.title +=" Generate Invoice Fuel ";

if('${param.order_id}'!='')
	{
		drppaymentmode.value="Paytm";
		drppaymentmode.disabled=true;
		divsearchforitems.style="display:none";
		txtpaymenttype.disabled=true;
	}
	
	function checkIfMachinetoBeDisplayed()
	{
		if(drppaymentmode.value=="Card")
			{
				document.getElementById("divmachinename").style="display:block";		
			}
		else
			{
			document.getElementById("divmachinename").style="display:none";
			}
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
				checkforMatchCustomerVehicle();
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
		    	//var cell4 = row.insertCell(3);
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	cell1.innerHTML = "<input type='hidden' value='"+itemId+"~"+purchaseDetailsId+"'><a onclick=window.open('?a=showItemHistory&itemId="+itemId+"') href='#'>"+ itemDetails[0] + " "+ "(" + itemDetails[10]+ " ) </a>";
		    	
		    	cell2.style.width = '10%';
		    	
		    	cell2.innerHTML = '<div class="input-group"><span class="input-group-btn"><button id="btnminus'+itemId+'" class="btn btn-info" type="button" onclick="addremoveQuantity('+itemId+',0)"><i class="fa fa-minus"></i></button></span><input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty'+itemId+'" onkeyup="calculateAmount('+itemId+');" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="1"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty'+itemId+'" value='+itemDetails[10]+'><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity('+itemId+',1)"><i class="fa fa-plus"></i></button></span></div><input type="hidden" value='+itemDetails[1]+' id="txtrate'+itemId+'">';
		    	cell2.style.width = '60%';
		    	
		    	cell3.innerHTML = '<input type="tel" onfocus="this.select()" style="width:100%" class="form-control" onkeyup="calculateQtyFromAmountAndAddToTable('+itemId+',this.value)" form-control-sm" value="'+itemDetails[1]+'" id="txtamount'+itemId+'">';
		    	cell3.style.width = '30%';
		    	

		    	
		    	//cell4.innerHTML = '<button type="button" class="btn btn-sm btn-danger"  onclick=removethisitem(this) id="btn11" style="cursor:pointer">0</button>';
		    	
		    	
		    	calculateAmount(itemId);
		    	document.getElementById("txtqty"+itemId).select();
		    	document.getElementById("txtqty"+itemId).focus();
		    	
				
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
		calculateAmount(itemId);
	}
	
	function formatQty(qtyTextBox)
	{
		qtyTextBox.value=Number(qtyTextBox.value).toFixed(3);
			
	}
	function checkForNozzleChangeAndGetItem()
	{
		
		if('${param.order_id}'!='' && drpnozzle.value!=-1)
		{				
			var itemDetails=drpnozzle.options[drpnozzle.selectedIndex].innerHTML.split("~");
			//alert(itemDetails[2]);
			// clear the table contents
			
			
			
			
			$("#tblitems tr").remove();
			
			// should add the column names back
			var table = document.getElementById("tblitems");	    	
		    	var row = table.insertRow(-1);	    	
		    	var cell1 = row.insertCell(0);
		    	var cell2 = row.insertCell(1);
		    	var cell3 = row.insertCell(2);
		    	
		    	
		    	
				cell1.outerHTML = "<th>Item Name</th>";			
		    	cell1.style.width = '10%';
		    	cell1.style= 'width:10%;text-align: center';
		    	
		    	
		    	cell2.outerHTML = '<th>Qty</th>';		    	
		    	cell2.className = 'aligncenterclass';
		    	
		    	
		    	cell3.outerHTML= '<th>Rate</th>';
		    	cell3.style.width = '30%';
		    	
		    	
		    	
		    	
			
			

			getItemDetailsAndAddToTable(itemDetails[2],0); // needs to be changed from 0 to later
			calculateQtyFromAmountAndAddToTable(itemDetails[2],'${param.amount}');
			$('#tblitems tr').each(function() {
			    $(this).find('td').each(function() {
			        $(this).find('input,select,textarea,button').attr("readonly", true);
			        $(this).find('input,select,textarea,button').attr("disabled", true);
			    });
			  });
			
		}
	}
	function calculateQtyFromAmountAndAddToTable(itemId,amount)
	{

		// hardcoded this will need to fix later
		if (itemId!=587284 && itemId!=587285)
		{
			calculateTotal();
			return;
		}


		var existingRate=document.getElementById("txtrate"+itemId).value;
		var calculatedQty=Number(amount)/Number(existingRate);
		
		
		document.getElementById("txtqty"+itemId).value=calculatedQty.toFixed(3);
		
		
		calculateTotal();
		
		
	}
	
	
	
	
	
	document.getElementById("drpshiftid").value='${suggestedShiftId}';
	
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


	function getAttendantList()
{
	var shift=drpshiftid.options[drpshiftid.selectedIndex].value;
	
		const xhttp = new XMLHttpRequest();
		xhttp.onload = function() {
		var lsitofattendants=JSON.parse(this.responseText)["listofAttendants"];
		console.log("List of attendants are");
		console.log(lsitofattendants);
		var reqString="";
		for(var m=0;m<lsitofattendants.length;m++)
		{
			//console.log(lsitofattendants.name+" "+ lsitofattendants[m].nozzle_name +" ");
			reqString+="<option value="+lsitofattendants[m].nozzle_id+">"+" "+lsitofattendants[m].name+"~Checked In to ~Nozzle No : "+lsitofattendants[m].nozzle_name+"~"+lsitofattendants[m].item_name+"~"+lsitofattendants[m].user_id+"</option>";
		}
		//<option value="${nozzle.nozzle_id}">${nozzle.name }~Checked In to ~Nozzle No : ${nozzle.nozzle_name}~${nozzle.item_name} at ~(${nozzle.check_in_time }) ~ ${nozzle.attendant_id } ~ ${nozzle.nozzle_id}</option>    
		
		drpnozzle.innerHTML=reqString;

 
    
    }
    xhttp.open("GET", "?a=getAttendantsForDateAndShiftUnclubbed&collection_date="+txtinvoicedate.value+"&shift_id="+shift);
    xhttp.send();
	
	
	
}




if("${param.txtinvoicedate}"!="")
{
	txtinvoicedate.value="${param.txtinvoicedate}";
}
else
{	
	txtinvoicedate.value="${todaysDate}";
}
if(txtinvoicedate.value!='')
{
	getAttendantList();
}



if("${param.invoice_id}"!="")
{

	txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";		
	drpshiftid.value="${invoiceDetails.shift_id}";	
	txtpaymenttype.value="${invoiceDetails.payment_type}";
	drppaymentmode.value="${invoiceDetails.payment_mode}";
	txtsearchcustomer.value="${invoiceDetails.customer_name}";
	hdnSelectedCustomer.value="${invoiceDetails.customer_id}";
	

	getAttendantList();
	checkforMatchCustomerVehicle();


	// sleeping 1 second so that the attendant list is populated
	sleep(1000).then(() => {    
	drpnozzle.value="${invoiceDetails.nozzle_id}";
	});





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
    	
    	cell2.innerHTML = '<div class="input-group"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},0)"><i class="fa fa-minus"></i></button></span><input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty${item.item_id}" onkeyup="calculateAmount(${item.item_id});checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="${item.qty}"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty${item.item_id}" value="${item.qty}"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},1)"><i class="fa fa-plus"></i></button></span></div>';
    	
    	var itemTotal=Number('${item.custom_rate}') * Number('${item.qty}');
    	cell3.innerHTML ='<input typ="text" class="form-control form-control-sm" value="'+itemTotal+'">';
    	
    	
		</c:forEach>

		calculateTotal();

		disableAllComponents();
		
	
}


function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}




</script>



