
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
	    "|";
	}


	
	
	
	
	
	var reqString="stock_date="+txtinvoicedate.value+"&itemDetails="+itemString;
	
	
		
	
	

	  var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) 
	    {
	    	
	      	//alert("Invoice Saved Succesfully"+invoiceId[0]);
	      	
	      
	      	
	      	
	      	
	      	alert(this.responseText);
			
	    	window.location.reload();
	    	btnsave.disabled=false;
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	resetField();
	      	 if(invoiceId.length==3)
	      	    {
	      	    	window.location=invoiceId[2];	
	      	    }
	      	btnsave.disabled=false;

	      	document.getElementById("divTitle").innerHTML="Todays Stock";
	      	document.title +="Todays Stock";
	    	  
	      
	    }
	  };
	  xhttp.open("POST", "?a=saveTodaysStock", true);
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


	
	totalQty.innerHTML="0";
	
	
	
	
	btnsave.disabled=false;
	
	hdnSelectedCustomer.value='';
	setDefaultChecks();


	
		
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






  
   <div class="col-sm-4" style="display:none">
  	
    
    <div class="input-group">
    <input type="text" class="form-control form-control-sm"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="alert('hellow2')" onchange="alert('hellow1')">
    
  </div>
  </div>



  <div class="col-2">
  	<div class="form-group">	
	<label for="CustomerName"> Date</label>
  		
  	</div>
  </div>
   

  <div class="col-8">
  	<div class="form-group">	
	
  		<input type="text" id="txtinvoicedate" name="txtinvoicedate" class="form-control form-control-sm" value="${todaysDate}" placeholder="Invoice Date" onchange="getItemsAndStockForThisDate()" readonly/>
  	</div>
  </div>


<div class="col-2">
  	<div class="form-group" style="margin-bottom:0px"> 	
	<div class="custom-control custom-switch">
<input type="checkbox" class="custom-control-input" id="chk14packaging" onchange="getItemsAndStockForThisDate()">
<label class="custom-control-label" for="chk14packaging">14 Packaging</label>
</div>
  		
  	</div>
  </div>





  
  
  
  
  
  
  
  <div class="col-sm-12">  
	  <div class="card-body table-sm table-responsive p-0" style="height: 800px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center" >
	                     
	  			
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
  	 <div class="form-group" align="center">	  
	   	<button class="btn btn-success" type="button" id="btnsave" onclick='saveInvoice()'>Save (F2)</button>   
	   <button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>
	   <button class="btn btn-primary" type="reset" onclick='resetStock()'>Reset Stock</button>
	   
	   
	   
	  	   
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


function getItemDetailsAndAddToTable(itemId,itemName,qty)
{
		      
	
	

		
	
	    	
	    	//console.log(itemDetails);
	    	var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(1);	    	
	    	var cell1 = row.insertCell(0);
	    	var cell2 = row.insertCell(1);
	    	

	    	
	    	
	    	
	    	
	    	
	    	
	    	if(qty==null)
			{
				qty=0;
			}
	    	cell1.innerHTML = "<div>" +"<input type='hidden' value='"+itemId+"~"+1+"'>"+" <span style='font-size:16px'>"+ itemName + "</span> </div>";
	    	//cell3.innerHTML = " <input type='text' class='form-control form-control-sm'  id='txtqty"+itemId+"' onkeyup='calculateAmount(this);checkIfEnterisPressed(event,this);' onblur='formatQty(this)' onkeypress='digitsOnlyWithDot(event);' value='1'> <input type='hidden' class='form-control form-control-sm'  readonly id='hdnavailableqty"+itemId+"' value="+itemDetails[10]+">";
	    	
	    	cell2.innerHTML = '<div class="input-group"><input type="number" style="text-align:center" class="form-control form-control-sm"  name="quantitiestextboxes" id="txtqty'+itemId+'" onkeyup="calculateAmount('+itemId+');checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="'+qty+'"> <input type="hidden" class="form-control form-control-sm"  readonly id="hdnavailableqty'+itemId+'" value='+itemName+'></div>';
	    	
	    	
	    	
	    	
	    	
	    	
	    	$("#txtqty"+itemId).focus(function() { 

				if(document.getElementById("txtqty"+itemId).value=="0")
				{
					document.getElementById("txtqty"+itemId).value=""; 
				}



});
	    	
	    	
			
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

document.getElementById("divTitle").innerHTML=" TODAY'S STOCK ";
document.title +=" TODAY'S STOCK ";
$("#divBackButton").attr("href", "https://www.w3schools.com/jquery/");


$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
if('${invoiceDetails.invoice_id}'!='')
	{
		
		
		

		document.getElementById("divTitle").innerHTML="Invoice No:-"+"${invoiceDetails.invoice_no}";
		document.title +=" Invoice No:-"+"${invoiceDetails.invoice_no} ";
		hdnSelectedCustomer.value="${invoiceDetails.customer_id}"
		
		
			
		if('${invoiceDetails.customer_name}'!='')
			{
				txtsearchcustomer.value="${invoiceDetails.customer_name}~${invoiceDetails.customercityname}";			
			}
		
		
		document.getElementById("hdnSelectedCustomerType").value='${invoiceDetails.customer_type}';
		txtinvoicedate.value="${invoiceDetails.theInvoiceDate}";
		totalQty.innerHTML="${invoiceDetails.totalQuantities}";
		
		
		
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
    	
    	
    	
    	
    	cell1.innerHTML = '<div>'+m+'</div><input type="hidden" value="${item.item_id}">';    	
    	cell2.innerHTML = '${item.item_name}';    	
    	cell3.innerHTML = '<input type="text" style="text-align:center" class="form-control form-control-sm"  id="txtqty${item.item_id}" onkeyup="calculateAmount(${item.item_id});checkIfEnterisPressed(event,this);" onblur="formatQty(this)" onkeypress="digitsOnlyWithDot(event);" value="${item.qty}">';
    	
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
				btnsave.style="display:none";
			}
		
		
		document.getElementById("generatePDF").style="display:";
		generatePDF.disabled=false;
		
		
	}
	else
	{
		
	}
	
	
function returnThisItem(detailsId)
{
		window.location="?a=showReturnScreen&detailsId="+detailsId;
}
function resetCustomer()
{
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





function getItemsAndStockForThisDate()
{

	var packaging_type="12";
	if(chk14packaging.checked==true)
	{
		packaging_type="14";
	}


$('#tblitems tr').not(':first').remove();

  $.get("?a=getItemsAndStockForThisDate&date="+txtinvoicedate.value+"&packaging_type="+packaging_type, function(data, status){

	var listOfItems=JSON.parse(data);

	for(var k=0;k<listOfItems.length;k++)
	{		
		getItemDetailsAndAddToTable(listOfItems[k].item_id,listOfItems[k].item_name,listOfItems[k].qty);
	}
    calculateTotal();
  });

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










getItemsAndStockForThisDate();

function resetStock()
{	
	var rows=tblitems.rows;
	var requiredDetails=[];
	var arr = [];
	var itemString="";
	var confirmMessage="";
	var proceedFlag=true;
	var messageToShow="";
	for (var x= 1; x < rows.length; x++) 
	{   
	    rows[x].childNodes[1].childNodes[0].childNodes[0].value='0';
	}
	calculateTotal();
}




</script>