<style>







	 .table th:first-child,
    .table td:first-child {
        width: 70%; /* 70% width for the first column */
		border: 1px solid #000000; /* Dark black border */
        text-align: left;
		vertical-align:middle;

		    padding: 0px!important;
			margin:3px;
			padding-left:10px!important;
		

		
    }

    .table th:last-child,
    .table td:last-child {
        width: 30%; /* 30% width for the second column */
		border: 1px solid #000000; /* Dark black border */
        text-align: left;
		vertical-align:middle;
		padding: 0px!important;
		margin:3px;
		
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
		"~"+rows[x].childNodes[0].childNodes[0].childNodes[2].innerHTML+ // Item Name
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
	"&hdnPreviousInvoiceId="+hdnPreviousInvoiceId.value+
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
	      	
	      	alert("Order Saved Successfully ");
			window.location="?a=showPendingRegister";
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
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
	txtsearchcustomer.readOnly=false;
	
	txtinvoicedate.value="${todaysDate}";
	txtinvoicedate.disabled=false;
	txtitem.disabled=false;


	
	totalQty.innerHTML="0";
	
	
	
	
	btnsave.disabled=false;
	
	hdnSelectedCustomer.value='';
	setDefaultChecks();


	//populateItems();
		
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


function showEditInvoice()
{
	window.location="?a=showGenerateInvoice&invoice_id=${param.invoice_id}&editInvoice=Y";
}

function generateOrderReport()
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
	  xhttp.open("GET","?a=generateOrderReport&invoice_id=${param.invoice_id}", false);    
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






  
   <div class="col-sm-4" style="display:none">
  	
    
    <div class="input-group" >
    <input type="text" class="form-control form-control-sm"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="alert('hellow2')" onchange="alert('hellow1')">
    
  </div>
  </div>



  <div class="col-2">
  	<div class="form-group" style="margin-bottom:0px">	
	<label for="CustomerName"> Date</label>
  		
  	</div>
  </div>
   

  <div class="col-10">
  	<div class="form-group" style="margin-bottom:0px">	
	
  		<input type="text" id="txtinvoicedate" name="txtinvoicedate" class="form-control form-control-sm" value="${todaysDate}" placeholder="Invoice Date" readonly/>
  	</div>
  </div>


  <div class="col-2">
  	<div class="form-group" style="margin-bottom:0px"> 	
	<label for="CustomerName"> Party</label>
  		
  	</div>
  </div>






  
  
  

  <div class="col-8">
  	<div class="form-group">

  	<div class="input-group input-group-sm">


    <div class="ui-widget" style="width:100%">
	  <input type="text" id="txtsearchcustomer" class='form-control' placeholder="Search For Customer"  name="txtsearchcustomer" onclick="resetCustomer()"> 
	</div>
                  
                  
                  
    </div>
  	
  	
        	      
      
            
      <input  type="hidden" name="hdnSelectedCustomer" id="hdnSelectedCustomer" value=""> 
   			<input  type="hidden" name="hdnSelectedCustomerType" id="hdnSelectedCustomerType" value="">
   			<input  type="hidden" name="hdnPreviousInvoiceId" id="hdnPreviousInvoiceId" value="${invoiceDetails.invoice_id}">
   			      
    </div>
  </div>
  
  
  
  
   <div class="col-2">
  	<div class="form-group" style="margin-bottom:0px"> 	
	<div class="custom-control custom-switch">
<input type="checkbox" class="custom-control-input" id="chk14packaging" onchange="changePackaging()">
<label class="custom-control-label" for="chk14packaging">14 Packaging</label>
</div>
  		
  	</div>
  </div>
  
  
  
  
  <div class="col-sm-12">  
	  <div class=" table-sm table-responsive p-0" >                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center" style="font-size:10px">
	                     
	  			
	                    </tr>
	                  </thead>
	                </table>
	   </div>	
  </div>
  
  
  
  
  
  
	  						
	  					
  
  

  
  
  
  
  
  
  
   
  
  
	   	
   <div class="col-sm-12">
  	 <div class="form-group" align="center">	  

	 	Total LD : <span id="totalQty" style="font-weight:800">0</span>
	   	
   </div>
   </div>
  
</div>
</form>
	</div>
   
   
   
   	
  


  <div class="col-sm-12">
    <div class="form-group text-center row">
        <div class="col-12 col-sm-6 col-md-4 col-lg-2 mb-2">
            <button class="btn btn-primary" type="button" id="btnsave" onclick='saveInvoice()'>Save</button>   
        </div>
		<c:if test="${param.editInvoice ne null}">
        <div class="col-12 col-sm-6 col-md-4 col-lg-2 mb-2">
            <button class="btn btn-primary" type="button" id="btnorder" onclick='showEditInvoice()'>Edit</button>   
        </div>
		
        <div class="col-12 col-sm-6 col-md-4 col-lg-2 mb-2">
            <button class="btn btn-primary"  id="generatePDF" type="button" onclick='generateInvoice("${invoiceDetails.invoice_id}");'>Order PDF</button>
        </div>
		</c:if>
    </div>
</div>



  
</div>
</form>
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
			document.getElementById("txtsearchcustomer").readOnly=true;
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


function checkforMatchItem(qty)
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
				getItemDetailsAndAddToTable(itemId,purchaseDetailsId,qty);
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


function getItemDetailsAndAddToTable(itemId,purchaseDetailsId,qty)
{
		      
	
	

		var itemDetails=document.getElementById("hdn"+itemId).value.split("~");
		
	
	    	
	    	//console.log(itemDetails);
	    	var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(-1);	    	
	    	var cell1 = row.insertCell(0);
	    	var cell2 = row.insertCell(1);
	    	

	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	cell1.innerHTML = "<div>" +"<input type='hidden' value='"+itemId+"~"+purchaseDetailsId+"'>"+" <span>"+ itemDetails[0] + "</span> </div>";
	    	//cell3.innerHTML = " <input type='text' class='form-control form-control-sm'  id='txtqty"+itemId+"' onkeyup='calculateAmount(this);checkIfEnterisPressed(event,this);' onblur='formatQty(this)' onkeypress='digitsOnlyWithDot(event);' value='1'> <input type='hidden' class='form-control form-control-sm'  readonly id='hdnavailableqty"+itemId+"' value="+itemDetails[10]+">";
	    	
	    	cell2.innerHTML = '<div class="input-group"><input type="number" style="text-align:center" class="form-control form-control-sm"  name="quantitiestextboxes" id="txtqty'+itemId+'" onkeyup="calculateAmount('+itemId+');checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="'+qty+'"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty'+itemId+'" value='+itemDetails[10]+'></div>';
	    	
	    	
	    	
	    	
	    	
	    	
	    	//document.getElementById("txtqty"+itemId).select();
	    	//document.getElementById("txtqty"+itemId).focus();
	    	
	    	$("#txtqty"+itemId).focus(function() { 
				if(document.getElementById("txtqty"+itemId).value=="0")
				{
					document.getElementById("txtqty"+itemId).value=""; }
				}
				);
	    	//$("#txtcustomrate"+itemId).focus(function() { $(this).select(); } );
	    	//$("#txtamount"+itemId).focus(function() { $(this).select(); } );
	    	
			
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

document.getElementById("divTitle").innerHTML="Generate Order";
document.title +=" Generate Order ";
$("#divBackButton").attr("href", "https://www.w3schools.com/jquery/");


$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
populateItems();
	
	
function returnThisItem(detailsId)
{
		window.location="?a=showReturnScreen&detailsId="+detailsId;
}
function resetCustomer()
{
	txtsearchcustomer.readOnly=false;
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
	    		
	    	txtsearchcustomer.readOnly=true;
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



// need to bring thhis flag from db saved for later
document.getElementById('row7').className='col-sm-12';


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

if("${invoiceDetails.invoice_id}"!="" && "${param.editInvoice}"=="Y")
{
	
	txtsearchcustomer.value="${invoiceDetails.customer_name}";
	txtsearchcustomer.readOnly=true;
	hdnSelectedCustomer.value="${invoiceDetails.customer_id}";
	txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";

	

		<c:forEach items="${itemList}" var="allItems">
						txtitem.value="${allItems.item_name}";
						checkforMatchItem(Number("${allItems.qty}").toFixed(0));
		</c:forEach>

	calculateTotal();
	
}
else if("${invoiceDetails.invoice_id}"!="" && "${param.editInvoice}"=="N")
{

	txtsearchcustomer.value="${invoiceDetails.customer_name}";
	txtsearchcustomer.readOnly=true;
	hdnSelectedCustomer.value="${invoiceDetails.customer_id}";
	txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";

	<c:forEach items="${itemList}" var="item">
	
		<c:if test="${item.qty ne null}">
			txtitem.value="${item.item_name}";
			checkforMatchItem(Number("${item.qty}").toFixed(0));			
		</c:if>
	</c:forEach>
	calculateTotal();
} else
{


	<c:forEach items="${itemList}" var="item">
		
			txtitem.value="${item.item_name}";
			checkforMatchItem("0");
		
	</c:forEach>
}


	   	   	
}



$( function() {
    var availableTags = [
      
    ];

  var options1=document.getElementById("customerList").options;
	var customerId=0;
	for(var x=0;x<options1.length;x++)
		{
			
					
          availableTags.push(options1[x].value);
					
					
				
		}

    


    $( "#txtsearchcustomer" ).autocomplete({
      source: availableTags,
      select: function(event, ui) {
                    
          setTimeout(function() {
              checkforMatchCustomer();
          }, 100);


                }
    });

   
    

  } );



  function onfoctext()
   {
              $("#txtsearchcustomer").autocomplete("search", "~");
              }











 
function checkIfReadonlyThenReset()
{
		resetCustomer();	
}

function changePackaging()
{
	if(chk14packaging.checked==true)
	{
		window.location="?a=showGenerateInvoice&packaging_type=14";
	}
	else
	{
		window.location="?a=showGenerateInvoice&packaging_type=12";
	}
}

if("${param.packaging_type}"=="14")
{
	chk14packaging.checked=true;
}




</script>