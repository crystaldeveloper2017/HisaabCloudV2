<style>


	th
	{
		vertical-align:middle!important;
	}
	td
	{
		vertical-align:middle!important;
	}
	

</style>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
  <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
           
           
           



<c:set var="itemList" value='${requestScope["outputObject"].get("itemList")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="invoiceDetails" value='${requestScope["outputObject"].get("invoiceDetails")}' />
<c:set var="tentativeSerialNo" value='${requestScope["outputObject"].get("tentativeSerialNo")}' />
<c:set var="customerMaster" value='${requestScope["outputObject"].get("customerMaster")}' />
<c:set var="categoriesWithItem" value='${requestScope["outputObject"].get("categoriesWithItem")}' />
<c:set var="lsitOfCategories" value='${requestScope["outputObject"].get("lsitOfCategories")}' />
<c:set var="todaysDateMinusOneMonth" value='${requestScope["outputObject"].get("todaysDateMinusOneMonth")}' />





   

<%-- <script src="js/1ice.js"></script> --%>
<script src="Test_files/app.js"></script>
<script src="Test_files/iconv-lite.bundle.js.download" charset="utf-8"></script>



</head>


<script >


function saveInvoice()
{
	
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
	    // ID, QTY, RATE,CustomRate,Item Name,gst amount
	    
	    var arrayReq=rows[x].childNodes[0].childNodes[0].childNodes[0].value.split('~');
	    var itemId=arrayReq[0];
	    var productDetailsId=arrayReq[1];
	    
	    itemString+=
	    	itemId+ 
	    "~"+Number(rows[x].childNodes[1].childNodes[0].childNodes[0].value)+
		"~0"+
		"~0"+
		"~"+rows[x].childNodes[0].childNodes[0].childNodes[1].childNodes[1].innerHTML+ // Item Name
	    "|";
	}


	
	if(hdnSelectedCustomer.value=="")
	{
		alert("Please select customer");
		btnsave.disabled=false;
		return;
	}
	
	
	
	var reqString="customer_id="+hdnSelectedCustomer.value+
	"&gross_amount=0"+
	"&item_discount=0"+
	"&invoice_discount=0"+
	"&total_amount=0"+
	"&payment_type=Pending"+
	"&payment_mode="+
	"&paid_amount=0"+
	"&invoice_date="+txtinvoicedate.value+
	"&remarks="+
	"&hdnPreviousInvoiceId="+
	"&table_id="+
	"&booking_id="+
	"&appId=${userdetails.app_id}"+
	"&store_id=${userdetails.store_id}"+
	"&user_id=${userdetails.user_id}"+	
	"&itemDetails="+itemString;
	
	//alert(reqString);
	//return;
		
	
	

	  var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) 
	    {
	    	var invoiceId=this.responseText.split("~");
	      	//alert("Invoice Saved Succesfully"+invoiceId[0]);
	      	
	      	if(invoiceId.length==1)
	      		{
	      			alert('An Error Has Occured. Please Contact Support');
	      			resetField();
	      			return;
	      		}
	      	
	      	
	      	
	      	toastr["success"]("Invoice Saved Succesfully "+invoiceId[0]);
	    	toastr.options = {"closeButton": false,"debug": false,"newestOnTop": false,"progressBar": false,
	    	  "positionClass": "toast-top-right","preventDuplicates": false,"onclick": null,"showDuration": "1000",
	    	  "hideDuration": "500","timeOut": "500","extendedTimeOut": "500","showEasing": "swing","hideEasing": "linear",
	    	  "showMethod": "fadeIn","hideMethod": "fadeOut"
	    	}
	    	//window.location.reload();
	    	btnsave.disabled=false;
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	resetField();
	      	 if(invoiceId.length==3)
	      	    {
	      	    	window.location=invoiceId[2];	
	      	    }
	      	btnsave.disabled=false;
	      	document.getElementById("divTitle").innerHTML="Generate Invoice : "+(Number(invoiceId[0])+1);
	      	document.title +=" Generate Invoice :  " +(Number(invoiceId[0])+1);
	    	  
	      
	    }
	  };
	  xhttp.open("POST", "?a=saveInvoice", true);
	  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");	  
	  xhttp.send(encodeURI(reqString));
	
	
	
}

function resetField()
{
	
	
	
	document.getElementById("hdnSelectedCustomerType").value=
	
	
	$("#tblitems").find("tr:gt(0)").remove();

	txtsearchcustomer.value="";
	txtsearchcustomer.disabled=false;
	
	txtinvoicedate.value="${todaysDate}";
	txtinvoicedate.disabled=false;
	txtitem.disabled=false;
	
	
	
	
	btnsave.disabled=false;
	
	hdnSelectedCustomer.value='';
	setDefaultChecks();


	populateItems();
		
}
function savedInvoiceCallback(data1)
{
		console.log(data1);
}


function updateItem()
{
	
	document.getElementById("frm").action="?a=updateItem"; 
	document.getElementById("frm").submit();
	return;
	
	
}





function deleteAttachment(id)
{
		
		
		
		  document.getElementById("closebutton").style.display='none';
		   document.getElementById("loader").style.display='block';
		$("#myModal").modal();

		var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() 
		  {
		    if (xhttp.readyState == 4 && xhttp.status == 200) 
		    { 		      
		      document.getElementById("responseText").innerHTML=xhttp.responseText;
			  document.getElementById("closebutton").style.display='block';
			  document.getElementById("loader").style.display='none';
			  $("#myModal").modal();
		      
			  
			}
		  };
		  xhttp.open("GET","?a=deleteAttachment&attachmentId="+id, true);    
		  xhttp.send();
		
		
		
}








</script>


<div class="container" style="padding:20px;background-color:white;margin-top:5px;min-width:100%">


<div class="row">
	<div id="row7" class="col-sm-7">
		<form id="frm" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="row">
<datalist id="customerList">

<c:forEach items="${customerMaster}" var="customer">
			    <option id="${customer.customerId}">${customer.customerName}~${customer.customerCity}</option>			    
</c:forEach>	   	 

  	
</datalist>

<datalist id="itemList">
<c:forEach items="${itemList}" var="item">
			    <option id="${item.item_id}">${item.item_name}</option>
			    <input type="hidden" id="hdn${item.item_id}" value="${item.item_name}~${item.price}~${item.wholesale_price}~${item.franchise_rate}~${item.loyalcustomerrate3}~${item.loyalcustomerrate2}~${item.loyalcustomerrate1}~${item.distributor_rate}~${item.b2b_rate}~${item.shrikhand}~${item.qty_available}~${item.gst}">			    
	   </c:forEach>	   	   	
</datalist>






  
  
   

  <div class="col-sm-2">
  	<div class="form-group">	
  		<input type="text" id="txtinvoicedate" name="txtinvoicedate" class="form-control form-control-sm" value="${todaysDate}" placeholder="Invoice Date" readonly/>
  	</div>
  </div>
  
  <div class="col-sm-4" style="display:none">
  	
    
    <div class="input-group">
    <input type="text" class="form-control form-control-sm"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="checkforMatchItem()">
    
  </div>
  </div>


  <div class="col-sm-10">
  	<div class="form-group">
  	
  	<div class="input-group input-group-sm">
                  <input type="text" class="form-control form-control-sm" id="txtsearchcustomer" list="customerList"    placeholder="Search For Customer" name="txtsearchcustomer"  autocomplete="off"  oninput="checkforMatchCustomer()">
                  
                  <span class="input-group-append">
                    <button type="button" class="btn btn-danger btn-flat" onclick="resetCustomer()">Reset</button>
                  </span>
                  
                  <span class="input-group-append">
                    <button type="button" class="btn btn-primary btn-flat" onclick="addCustomer()">Add</button>
                  </span>
    </div>
  	
  	
        	      
      
            
      <input  type="hidden" name="hdnSelectedCustomer" id="hdnSelectedCustomer" value=""> 
   			<input  type="hidden" name="hdnSelectedCustomerType" id="hdnSelectedCustomerType" value="">
   			<input  type="hidden" name="hdnPreviousInvoiceId" id="hdnPreviousInvoiceId" value="">
   			      
    </div>
  </div>
  
  
  
  
  
  
  
  
  
  <div class="col-sm-12">  
	  <div class="card-body table-sm table-responsive p-0" style="height: 370px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center" style="font-size:10px">
	                     
	  			<th style="z-index:0">Name</th>
	  			<th style="z-index:0">Qty</th>	  			
	  			
	  			
	                    </tr>
	                  </thead>
	                </table>
	   </div>	
  </div>
  
  
  
  
  
  
	  						
	  					
  
  

  
  
  
  
  
  
  
   
  
  
	   	
   <div class="col-sm-12">
  	 <div class="form-group" align="center">	  

	 	Total Quantity : <span id="totalQty">0</span>
	   	
   </div>
   </div>
  
</div>
</form>
	</div>
   
   
   
   	
  
  
 <div class="col-sm-12">
  	 <div class="form-group" align="center">	  
	   	<button class="btn btn-success" type="button" id="btnsave" onclick='saveInvoice()'>Save (F2)</button>   
	   <button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>
	   
	   
	  	   
	   <button class="btn btn-primary" style="display:none" id="generatePDF" type="button" onclick='generateInvoice("${invoiceDetails.invoice_id}");'>Generate PDF</button>
   </div>
   </div>
  
</div>
</form>
	</div>
	
	
	<div class="col-sm-5" id="categoryPopulate">
		
	</div>
	
</div> 







<script >

function generateInvoice(invoiceId)
{
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	//alert(xhttp.responseText);
	    	window.open("BufferedImagesFolder/"+xhttp.responseText);		  
		}
	  };
	  xhttp.open("GET","?a=generateInvoicePDF&invoiceId="+invoiceId, false);    
	  xhttp.send();
}


function generateInvoicePdfPrint(invoiceId)
{
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 	
	    	//window.open("BufferedImagesFolder/"+xhttp.responseText);		  

	    	try
  			{
      		var xhttp2 = new XMLHttpRequest();
      		xhttp2.onreadystatechange = function() {
      	    if (xhttp2.readyState == 4 && xhttp2.status == 200) 
      	    {
      	      //alert(xhttp2.responseText);
      	      
      	      
      	   
      	      
      	      
      	    }
      	  };
      	xhttp2.open("POST", "http://localhost:786/invoiceId="+invoiceId, false);
      	xhttp2.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
      	xhttp2.send();
  			}
  			catch(ex)
  			{
  				resetField();
  				console.log(ex.message);
  			}
	    	
	    	
	    	
	    	
	    	
	    	
	    			  
		}
	  };
	  xhttp.open("GET","?a=generateInvoicePDF&invoiceId="+invoiceId, false);    
	  xhttp.send();
}
function searchForCustomer(searchString)
{	
	//if(searchString.length<3){return;}

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
	    		reqString+="<option id="+cusomerList[x].customer_id+">"+cusomerList[x].customer_name+"~"+cusomerList[x].mobile_number+"~"+cusomerList[x].customer_type+"</option>";
	    	}
	    	
	    	document.getElementById('customerList').innerHTML=reqString;
		}
	  };
	  xhttp.open("GET","?a=searchForCustomer&searchString="+searchString, true);    
	  xhttp.send();
	
	 
	
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
					txtitem.focus();
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


function fetchPendingAmountForThisCustomer(customerId)
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
	    			txtcustomerpendingamount.value=details.pendingAmountDetails.PendingAmount;
	    		}
	    	else
	    		{
					//alert("no pending amount for this customer");
	    			//window.location.reload();
	    		}
		}
	  };
	  xhttp.open("GET","?a=getPendingAmountForCustomer&customerId="+customerId, true);    
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
		
	
	    	
	    	//console.log(itemDetails);
	    	var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(1);	    	
	    	var cell1 = row.insertCell(0);
	    	var cell2 = row.insertCell(1);
	    	

	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	cell1.innerHTML = "<div>" +"<input type='hidden' value='"+itemId+"~"+purchaseDetailsId+"'>"+"<a onclick=window.open('?a=showItemHistory&itemId="+itemId+"') href='#'> <span style='font-size:12px'>"+ itemDetails[0] + "</span></a> </div>";
	    	//cell3.innerHTML = " <input type='text' class='form-control form-control-sm'  id='txtqty"+itemId+"' onkeyup='calculateAmount(this);checkIfEnterisPressed(event,this);' onblur='formatQty(this)' onkeypress='digitsOnlyWithDot(event);' value='1'> <input type='hidden' class='form-control form-control-sm'  readonly id='hdnavailableqty"+itemId+"' value="+itemDetails[10]+">";
	    	
	    	cell2.innerHTML = '<div class="input-group"><input type="number" style="text-align:center" class="form-control form-control-sm"  name="quantitiestextboxes" id="txtqty'+itemId+'" onkeyup="calculateAmount('+itemId+');checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="0"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty'+itemId+'" value='+itemDetails[10]+'></div>';
	    	
	    	
	    	
	    	
	    	
	    	
	    	document.getElementById("txtqty"+itemId).select();
	    	document.getElementById("txtqty"+itemId).focus();
	    	
	    	$("#txtqty"+itemId).focus(function() { $(this).select(); } );
	    	$("#txtcustomrate"+itemId).focus(function() { $(this).select(); } );
	    	$("#txtamount"+itemId).focus(function() { $(this).select(); } );
	    	
			
}



function calculateAmount(itemId)
{
	
	
	
	
	
	
	calculateTotal();
}

function checkIfEnterisPressed(evt,txtbox)
{
	
	if(evt.which!=13)
	{
		return;
	}
	
	if((txtbox.id.toString().indexOf('txtqty')!=-1)) // means that enter is pressed on qty
		{
			var itemId=txtbox.id.replace('txtqty','');
			document.getElementById('txtcustomrate'+itemId).focus();
			document.getElementById('txtcustomrate'+itemId).select();			
		}
	
	if((txtbox.id.toString().indexOf('txtcustomrate')!=-1)) // means that enter is pressed on customrate 
	{
		txtitem.focus();
	}
	
		
}

function paymentTypeChanged(selection)
{
		if(selection=="Partial")
			{
				var partialPaymentElementsList=document.getElementsByName('partialPaymentElements');
				for(var x=0;x<partialPaymentElementsList.length;x++)
				{
					partialPaymentElementsList[x].style="display:";
				}
			}
		else
		{
			var partialPaymentElementsList=document.getElementsByName('partialPaymentElements');
			for(var x=0;x<partialPaymentElementsList.length;x++)
			{
				partialPaymentElementsList[x].style="display:none";
			}
		}
		
		
		
		
		if(selection=="Pending")
		{
			var paymentModeElements=document.getElementsByName('paymentModeElements');
			for(var x=0;x<paymentModeElements.length;x++)
			{
				paymentModeElements[x].style="display:none";
			}
			
			drppaymentmode.value="NA";
		}
		else
		{
			var paymentModeElements=document.getElementsByName('paymentModeElements');
			for(var x=0;x<paymentModeElements.length;x++)
			{
				paymentModeElements[x].style="display:";
			}
			drppaymentmode.value="Cash";
		}
		
}

function calculateTotal()
{	
	var textboxes=document.getElementsByName("quantitiestextboxes");
	var qtyTota=0;

	for(var m=0;m<textboxes.length;m++)
	{
		//alert(textboxes[m].value);
		qtyTota+=Number(textboxes[m].value);
	}
	totalQty.innerHTML=qtyTota;
	

}

function removethisitem(btn1)
{
	btn1.parentElement.parentElement.remove();
	//reshuffle id's next to this
	
	reshuffleSrNos();
	
	calculateTotal(); 
}

function reshuffleSrNos()
{
	var rows1=tblitems.rows;
	for(var x=1;x<rows1.length;x++)
		{
			rows1[x].childNodes[0].childNodes[0].innerHTML=x;
		}
}

document.getElementById("divTitle").innerHTML="Generate Invoice:- "+"${tentativeSerialNo}";
document.title +=" Generate Invoice:- "+"${tentativeSerialNo} ";
$("#divBackButton").attr("href", "https://www.w3schools.com/jquery/");


$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
if('${invoiceDetails.invoice_id}'!='')
	{
		
		
		

		document.getElementById("divTitle").innerHTML="Invoice No:-"+"${invoiceDetails.invoice_no}";
		document.title +=" Invoice No:-"+"${invoiceDetails.invoice_no} ";
		hdnSelectedCustomer.value="${invoiceDetails.customer_id}"
		
			
		if('${invoiceDetails.customer_name}'!='')
			{
				txtsearchcustomer.value="${invoiceDetails.customer_name}~${invoiceDetails.mobile_number}~${invoiceDetails.customer_type}";			
			}
		
		
		document.getElementById("hdnSelectedCustomerType").value='${invoiceDetails.customer_type}';
		txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";
		totalQty.innerHTML="${invoiceDetails.totalQuantities}";
		grossAmount.innerHTML="${invoiceDetails.gross_amount}";
		txtitemdiscount.innerHTML="${invoiceDetails.item_discount}";
		txtinvoicediscount.value="${invoiceDetails.invoice_discount}";
		txtpaymenttype.value="${invoiceDetails.payment_type}";
		
		totalAmount.innerHTML="${invoiceDetails.total_amount}";
		gst.innerHTML="${invoiceDetails.total_gst}";
		txtpaidamount.value="${invoiceDetails.paid_amount}";
		paymentTypeChanged(txtpaymenttype.value);
		txtpendingamount.innerHTML=Number(totalAmount.innerHTML)-Number(txtpaidamount.value);
		txtremarks.value='${invoiceDetails.remarks}';
		drppaymentmode.value="${invoiceDetails.payment_mode}";
		
		var m=0;
		var tableNo="";
		<c:forEach items="${invoiceDetails.listOfItems}" var="item">
		
		m++;
		tableNo='${item.table_no}';
		var table = document.getElementById("tblitems");	    	
    	var row = table.insertRow(-1);	    	
    	var cell1 = row.insertCell(0);
    	var cell2 = row.insertCell(1);
    	var cell3 = row.insertCell(2);
    	var cell4 = row.insertCell(3);
    	var cell5 = row.insertCell(4);
    	var cell6 = row.insertCell(5);
    	var cell7 = row.insertCell(6);
    	var cell8 = row.insertCell(7);
    	
    	
    	
    	cell1.innerHTML = '<div>'+m+'</div><input type="hidden" value="${item.item_id}">';    	
    	cell2.innerHTML = '<a href="#">${item.item_name} ( )</a>';
    	//cell3.innerHTML = " <input type='text' class='form-control form-control-sm' id='txtqty"+${item.item_id}+"' onkeyup='calculateAmount(${item.item_id});checkIfEnterisPressed(event);' onblur='formatQty(this)' onkeypress='digitsOnlyWithDot(event);' value='${item.qty}'> <input type='hidden' readonly id='hdnavailableqty"+${item.item_id}+"'>";
    	cell3.innerHTML = '<div class="input-group"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},0)"><i class="fa fa-minus"></i></button></span><input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty${item.item_id}" onkeyup="calculateAmount(${item.item_id});checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="${item.qty}"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty${item.item_id}" value="${item.qty}"><span class="input-group-btn"><button class="btn btn-info" type="button" onclick="addremoveQuantity(${item.item_id},1)"><i class="fa fa-plus"></i></button></span></div>';
    	cell4.innerHTML = '<input type="text" readonly class="form-control form-control-sm" value="${item.rate}" id="txtrate${item.item_id}">';
    	
    	cell5.innerHTML = '<input typ="text" id="txtcustomrate${item.item_id}"  class="form-control form-control-sm" value="${item.custom_rate}" onkeyup="calculateAmount(${item.item_id})" onkeypress="digitsOnlyWithDot(event)">';
    	cell6.innerHTML = Number((Number('${item.rate}') * Number('${item.qty}')) - (Number('${item.custom_rate}') * Number('${item.qty}'))).toFixed(2);
    	var itemTotal=Number('${item.custom_rate}') * Number('${item.qty}');
    	cell7.innerHTML ='<input typ="text" class="form-control form-control-sm" value="'+itemTotal+'">';
    	cell8.innerHTML ="<input type='text' class='form-control form-control-sm' value='${item.gst_amount}' readonly id='gst${item.item_id}' onkeyup='calculateAmount(${item.item_id})'> <input type='hidden' id='hdngst${item.item_id}' value='${item.gst}'>";//
    	
    	
		
	    		//alert('${item.item_id}'+'-${item.item_name}'+'-${item.qty}'+'-${item.rate}'+'-${item.custom_rate}');			    
		</c:forEach>
 
		
		if('${param.editInvoice}'=='Y')
			{
				hdnPreviousInvoiceId.value="${invoiceDetails.invoice_id}";
				var returnButtons=document.getElementsByName('returnButtons');
				for(var m=0;m<returnButtons.length;m++)
					{
						returnButtons[m].style="display:none";
					}
				txtsearchcustomer.disabled=true;
				//txtinvoicedate.disabled=true;
				document.getElementById("divTitle").innerHTML="Edit Invoice:-"+"${invoiceDetails.invoice_no}";
				document.title +=" Edit Invoice:- "+ " ${invoiceDetails.invoice_no} ";
			}
		else
			{
				$("#frm :input").prop('disabled', true);
				$("[name=returnButtons]").prop('disabled', false);
				
				
				var deleteButtons=document.getElementsByName('deleteButtons');
				for(var m=0;m<deleteButtons.length;m++)
					{
						deleteButtons[m].style="display:none";
					}
			}
		
		if('${param.table_id}'!='')
			{
				calculateTotal();
				txtpaymenttype.value='Paid';
				drppaymentmode.value='Cash';
				txtsearchcustomer.disabled=false;
				txtinvoicedate.disabled=false;
				document.getElementById("divTitle").innerHTML="Invoice For Table No:-"+tableNo;
				document.title +=" Invoice For Table No:- "+tableNo;
				txtinvoicediscount.value=0;
			}
		
		if('${param.booking_id}'!='' || '${param.mobile_booking_id}'!='')
		{
			calculateTotal();
			txtpaymenttype.value='Paid';
			drppaymentmode.value='Cash';
			txtsearchcustomer.disabled=true;
			txtinvoicedate.disabled=false;
			document.getElementById("divTitle").innerHTML="Invoice For Booking Id - ${param.booking_id}";
			document.title +=" Invoice For Booking Id - ${param.booking_id} ";
			txtinvoicediscount.value=0;
		}
		
		document.getElementById("generatePDF").style="display:";
		generatePDF.disabled=false;
		btnRegister.disabled=false;
		
	}
	
	
function returnThisItem(detailsId)
{
		window.location="?a=showReturnScreen&detailsId="+detailsId;
}
function resetCustomer()
{
	window.location.reload();
	txtsearchcustomer.disabled=false;
	txtsearchcustomer.value="";
	hdnSelectedCustomer.value=0;	
}

function addCustomer()
{
	window.open("?a=showAddCustomer&mobileNo="+txtsearchcustomer.value);	
}


function calculateQtyFromAmount(itemId)
{	
	var amount=document.getElementById('txtamount'+itemId).value;
	var customRate=document.getElementById('txtcustomrate'+itemId).value;
	var qty=Number(amount)/Number(customRate);
	document.getElementById('txtqty'+itemId).value=qty;
	calculateTotal();
}

function formatQty(qtyTextBox)
{
	qtyTextBox.value=Number(qtyTextBox.value).toFixed(0);
		
}
function showItems(categoryname)
{
	
	
	
	var reqString='<div class="container" style="height: 80vh;overflow-y: auto; 	min-width:100%" ><div class="row">';
	const array = [];
	
	<c:forEach items="${itemList}" var="item">	
	var itemName="${item.item_name}";
	
	var categoryName="${item.category_name}"
		if (!array.includes(categoryName))
			{
		    	array.push(categoryName);
			}	
		if(categoryname==undefined || "${item.category_name}"==categoryname)
			{
				reqString+='<div class="col-sm">'
					reqString+='<img style="padding:5px" onclick="showThisItemIntoSelection(${item.item_id})" height="80px" width="80px" src="BufferedImagesFolder/${item.ImagePath}">';
					reqString+="<div>${item.item_name}</div>";
				reqString+='</div>';
			}
			
	</c:forEach>
	reqString+='</div></div>';
	
	var categoryString='<div class="container" style="min-width:100%"  ><div class="row">';
	for(var m=0;m<array.length;m++)
		{
			if(array[m]==categoryname)
				{
				categoryString+='<div class="col-sm">'
					categoryString+='<img style="border:1px solid;padding:5px" onclick="showItems(\''+array[m]+'\')" height="60px" width="60px" src="BufferedImagesFolder/dummyImageCategory.svg">';
					categoryString+="<div>"+array[m]+"</div>";
				categoryString+='</div>';
				continue;
				}
			categoryString+='<div class="col-sm">'
				categoryString+='<img style="padding:5px" onclick="showItems(\''+array[m]+'\')" height="60px" width="60px" src="BufferedImagesFolder/dummyImageCategory.svg">';
				categoryString+="<div>"+array[m]+"</div>";
			categoryString+='</div>';
		}
	
	
	
	
	
	
	categoryString+='</div></div>';
	  document.getElementById("categoryPopulate").innerHTML=categoryString+reqString;
	  
	  
}


function showThisItemIntoSelection(itemId)
{
	
		
				
				
				var rows=tblitems.rows;
				for(var x=1;x<rows.length;x++)
					{							
						if(itemId==rows[x].childNodes[0].childNodes[1].value)
							{
								alert('item already exist in selection');
								document.getElementById("txtitem").value="";
								return;
							}
					}
				
				// code to check if item already exist inselection				
				getItemDetailsAndAddToTable(itemId);
				document.getElementById("txtitem").value="";
		
}


function getPriceForThisCustomer(itemDetails)
{
	
	return itemDetails[1];	
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
	    	txtsearchcustomer.value=customerName.value+"~"+mobileNumber.value+"~"+customerType.value+"~"+customerCity.value;
	    	customerName.value="";
	    	mobileNumber.value="";
			customerCity.value="";
	    	
	    	hdnSelectedCustomer.value=xhttp.responseText.split("~")[1];
	    	hdnSelectedCustomerType.value=customerType.value;
		}
	  };
	  xhttp.open("GET","?a=saveCustomerServiceAjax&appId=${userdetails.app_id}"+"&customer_id="+hdnSelectedCustomer.value+"&mobileNumber="+mobileNumber.value+"&customerType="+customerType.value+"&city="+customerCity.value, true);    
	  xhttp.send();
}

function addremoveQuantity(itemId,addRemoveFlag) // 0 removes and 1 adds
{
	
	var	qtyElement=document.getElementById('txtqty'+itemId);
	var quantity=Number(qtyElement.value);
	
	if(quantity==1 && addRemoveFlag==0)
		{
			qtyElement.parentNode.parentNode.parentNode.remove();
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

function closeModalFast()
{
	$("#myModal").modal('hide');
}
	
	
document.addEventListener("keypress", function onPress(event) {
	
    
});

window.addEventListener('keydown', function (e) {
	if(event.which==113)
	{
		saveInvoice();
	} 
	});
	
showItems();


$('[data-widget="pushmenu"]').PushMenu("collapse");


function calculateReturnAmount()
{
	try
	{
		toReturn.value=  (Number(givenByCustomer.value)-Number(totalAmount.innerHTML)).toFixed(2);
	}
	catch(ex)
	{
		console.log(ex.message);
	}
	
}

function getGSTForAllItems()
{
	if('${param.invoice_id}'!='' && '${param.editInvoice}'=='')		
	{
		return;
	}
	var rows1=tblitems.rows;
	for(var x=1;x<rows1.length;x++)
		{
			var itemId=rows1[x].childNodes[0].childNodes[1].value;
			
			calculateAmount(itemId);
			
		}
		
}
function simulateCheck()
{
	
	
	
}


// need to bring thhis flag from db saved for later
document.getElementById('row7').className='col-sm-12';
categoryPopulate.style="display:none";

function setDefaultChecks()
{
	
	
	if("${userdetails.invoice_default_checked_generatepdf}"=='Y')
	{
		chkgeneratePDF.checked=true;		
	}

}

setDefaultChecks();


function showLedger()
{
	window.open('?a=showCustomerLedger&txtfromdate=${todaysDateMinusOneMonth}&txttodate='+txtinvoicedate.value+'&customerId='+hdnSelectedCustomer.value);
}


function printDirectAsFonts(invoiceNo,pendAmount) 
{
	
	
	
	try
	{
		
		if(invoiceNo==null)
			{
				alert("invoice No found as null");
				return;
			}
	
	var xhttp = new XMLHttpRequest();
	var invoiceResponse;
	var invoiceDetails;
	var listOfItems;
	
		  xhttp.onreadystatechange = function() 
		  {
		    if (xhttp.readyState == 4 && xhttp.status == 200) 
		    { 		      
		    	console.log(xhttp.responseText);
		       invoiceResponse=JSON.parse(xhttp.responseText);
		       invoiceDetails=(invoiceResponse.invoiceDetails);
		       listOfItems=invoiceDetails.listOfItems;
		       console.log(listOfItems);
		   		
			}
		  };
		  xhttp.open("GET","?a=getInvoiceDetailsByNoAjax&invoiceNo="+invoiceNo, false);    
		  xhttp.send();
		
		  console.log(invoiceDetails.invoice_no);
		  var topay=Number(invoiceDetails.total_amount)-Number(invoiceDetails.paid_amount);
			
    var c = new PosPrinterJob(getCurrentDriver(), getCurrentTransport());
    c.initialize();
     c.printText("Invoice Estimate: "+invoiceDetails.invoice_no, c.ALIGNMENT_CENTER, c.FONT_SIZE_MEDIUM2); // Invoice no   
    c.printText(invoiceDetails.store_name, c.ALIGNMENT_CENTER, c.FONT_SIZE_BIG).bold(true); // store name
    
    c.printText(invoiceDetails.address_line_1 + invoiceDetails.address_line_2, c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // address line 1
    c.printText(invoiceDetails.address_line_3, c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // address line 2     
    
    //c.printText(invoiceDetails.city+" - "+invoiceDetails.pincode, c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // city pincode
    
    c.printText("Phone:- "+invoiceDetails.mobile_no, c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // mobile
    c.printText("Store Timings:- "+invoiceDetails.store_timing, c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // timings
    
    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // mobile
    
    if(invoiceDetails.customer_name!=null)
    	{
    		c.printText("Name : "+invoiceDetails.customer_name, c.ALIGNMENT_LEFT, c.FONT_SIZE_NORMAL); // Name
    	}
		    c.printText("Date & Time : "+invoiceDetails.theUpdatedDate, c.ALIGNMENT_LEFT, c.FONT_SIZE_NORMAL); // Date & Time
		    c.printText("Payment Type : "+invoiceDetails.payment_type, c.ALIGNMENT_LEFT, c.FONT_SIZE_NORMAL); // Payment Type
    if(invoiceDetails.payment_type!='Pending')
    	{
    		c.printText("Payment Mode : "+invoiceDetails.payment_mode, c.ALIGNMENT_LEFT, c.FONT_SIZE_NORMAL); // Payment Type
    	}
    
    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // mobile
    c.printText("SR              ITEM NAME       Qty        Rate        AMOUNT  ", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // Payment Type
    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL); // mobile
    
    
    
    var totalWeight=0;
    var totalQty=0;
    for(var m=0;m<listOfItems.length;m++)
    	{
    	
    	totalWeight+=Number(listOfItems[m].weight);
    	totalQty+=Number(listOfItems[m].qty);
    	
    	var spaceCharacter=" ";
    	var finalLine="";
    	
    	var srnumber=m+1;
    	if(srnumber<=9)
    		{
    			srnumber="0"+srnumber;
    		}    	
    	finalLine+=srnumber;  	
    	
    	
    	var itemAllocatedSpace=29;
    	var itemName=listOfItems[m].item_name
    	for(k=0;k<itemAllocatedSpace;k++)
    		{
	    		if(itemName.length<itemAllocatedSpace)
	    		{
	    			if(k%2==0){itemName+=spaceCharacter;}
	    			if(k%2!=0){itemName=spaceCharacter+itemName;}
	    		}
    		}
    	finalLine+=" "+itemName;
    	var qtyAllocattedspace=4;
    	var qtyNum=listOfItems[m].qty;
    	var qtyWithWhiteSpaces=listOfItems[m].qty;
    	for(k=0;k<qtyAllocattedspace;k++)
    		{
	    		if(qtyWithWhiteSpaces.toString().length<qtyAllocattedspace)
	    		{
	    			if(k%2==0){qtyWithWhiteSpaces+=spaceCharacter;}
	    			if(k%2!=0){qtyWithWhiteSpaces=spaceCharacter+qtyWithWhiteSpaces;}	    					
	    		}
    		}    	
    	finalLine+=" "+qtyWithWhiteSpaces;
    	
    	var priceAllocatedSpace=11;
    	var priceWithSpaces=Number(listOfItems[m].custom_rate).toFixed(0);
    	for(k=0;k<priceAllocatedSpace;k++)
    		{
	    		if(priceWithSpaces.toString().length<priceAllocatedSpace)
	    		{
	    			if(k%2==0){priceWithSpaces+=spaceCharacter;}
	    			if(k%2!=0){priceWithSpaces=spaceCharacter+priceWithSpaces;}	    					
	    		}
    		}
    	finalLine+=" "+priceWithSpaces;
    	
    	var amountAllocatedSpace=13;
    	var Amount=Number(listOfItems[m].custom_rate)*Number(qtyNum);    	
    	Amount=Amount.toFixed(0);    	
    	var amountWithSpaces=Amount;
    	for(k=0;k<amountAllocatedSpace;k++)
    		{
	    		if(amountWithSpaces.toString().length<amountAllocatedSpace)
	    		{
	    			if(k%2==0){amountWithSpaces+=spaceCharacter;}
	    			if(k%2!=0){amountWithSpaces=spaceCharacter+amountWithSpaces;}	    					
	    		}
    		}
    	finalLine+=" "+amountWithSpaces;

    	console.log(finalLine);
    	c.printText(finalLine, c.ALIGNMENT_LEFT, c.FONT_SIZE_SMALL); // Payment Type
    
    	}
    
        
    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
    
    //c.printText("Total Weight : "+totalWeight.toFixed(3), c.ALIGNMENT_RIGHT, c.FONT_SIZE_NORMAL); // Payment Type
    c.printText("Total Qty : "+totalQty, c.ALIGNMENT_RIGHT, c.FONT_SIZE_NORMAL); // Payment Type
    
    if(invoiceDetails.invoice_discount!='' && invoiceDetails.invoice_discount!='0.00')
	{
	    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
	    c.printText("Invoice Discount :  "+invoiceDetails.invoice_discount, c.ALIGNMENT_RIGHT, c.FONT_SIZE_SMALL); // Remarks
	}
    
    if(invoiceDetails.remarks!='')
	{
	    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
	    c.printText("Remarks :  "+invoiceDetails.remarks, c.ALIGNMENT_LEFT, c.FONT_SIZE_SMALL); // Remarks
	}
    
    var topay=invoiceDetails.total_amount;
    
    if(invoiceDetails.payment_type=='Partial')
    	{    	
    	
    	// need to discuss and implement
    	c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
        c.printText("Total Amount :  "+Number(invoiceDetails.total_amount), c.ALIGNMENT_RIGHT, c.FONT_SIZE_MEDIUM1);         
        
    	c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
        c.printText("Partially Paid Amount :  "+invoiceDetails.paid_amount, c.ALIGNMENT_RIGHT, c.FONT_SIZE_MEDIUM1); // Payment Type
        topay=Number(invoiceDetails.total_amount)-Number(invoiceDetails.paid_amount);
    	}
    
    
    
    
    
    
    
    
    
    c.printText("----------------------------------------------------------------", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
    c.printText("Total Amount :  "+topay, c.ALIGNMENT_RIGHT, c.FONT_SIZE_MEDIUM1); // Payment Type
    
  
    
    
   

    c.printText("****************************************************************", c.ALIGNMENT_CENTER, c.FONT_SIZE_SMALL);
    c.printText("*Thank You, Visit Again*", c.ALIGNMENT_CENTER, c.FONT_SIZE_NORMAL);
    

    c.feed(3);
    c.execute();
    
        
    
    

	}
	catch(ex)
	{
		alert(ex.message);
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




function populateItems()
{
<c:forEach items="${itemList}" var="item">
	txtitem.value="${item.item_name}";
	checkforMatchItem();
</c:forEach>	   	   	
}

populateItems();












</script>