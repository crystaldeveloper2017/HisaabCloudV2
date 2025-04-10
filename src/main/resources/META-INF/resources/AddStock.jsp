  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
           
           


<c:set var="itemList" value='${requestScope["outputObject"].get("itemList")}' />
<c:set var="addStockList" value='${requestScope["outputObject"].get("addStockList")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="stockModificationDetails" value='${requestScope["outputObject"].get("stockModificationDetails")}' />






</head>


<script >



function removethisitem(btn1)
{
	btn1.parentElement.parentElement.remove();	 
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
				
				
				var total=0;
				var rows=tblitems.rows;
				for(var x=1;x<rows.length;x++)
					{							
						if(itemId==rows[x].childNodes[0].innerHTML)
							{
								alert('item already exist in selection');
								document.getElementById("txtitem").value="";
								return;
							}
					}
				
				// code to check if item already exist inselection				
			getItemDetailsAndAddToTable(itemId,searchString);
				document.getElementById("txtitem").value="";
		}
	
}




function getItemDetailsAndAddToTable(itemId,itemName)
{
	
	document.getElementById("closebutton").style.display='none';
	document.getElementById("loader").style.display='block';
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 	
		    	
	    	//alert(xhttp.responseText);
	    	var itemDetails=JSON.parse(xhttp.responseText);
	    	console.log(itemDetails);
	    	var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(-1);
	    	var cell0 = row.insertCell(0);
	    	var cell1 = row.insertCell(1);
	    	var cell2 = row.insertCell(2);
	    	var cell3 = row.insertCell(3);
	    	var cell4 = row.insertCell(4);
	    	var cell5 = row.insertCell(5);
	    	
	    	var cell6 = row.insertCell(6);
	    	
	    	var arritemName=itemName.split('~');
	    	
	    	
	    	cell0.innerHTML = itemId;
	    	cell1.innerHTML = arritemName[1];
	    	cell2.innerHTML = arritemName[0];
	    	cell3.innerHTML = " <input type='text' class='form-control input-sm' id='txtqty' readonly  onkeypress='digitsOnlyWithDot(event)' value="+itemDetails.stockAvailable+">";   	
	    	cell4.innerHTML = '<input  type="text" class="form-control"  onkeyup="digitsOnlyWithDot(event)">';
	    	cell5.innerHTML = '<input  type="text" class="form-control" >';
	    	
	    	
	    	cell6.innerHTML = '<button type="button" class="btn btn-danger"  onclick=removethisitem(this) id="btn11" style="cursor:pointer">Delete</button>';
	    	
		}
	  };
	  xhttp.open("GET","?a=getItemDetailsByAjax&itemId="+itemId+"&sourceStoreId=${userdetails.store_id}", true);    
	  xhttp.send();	
	    
		
	  
			
}


function printLabels()
{
	
	var rows=tblitems.rows;
	
	var requiredDetails=[];
	 
	
	var arr = [];
	var itemString="";
	var confirmMessage="";
	var proceedFlag=true;
	for (var x= 1; x < rows.length; x++) 
	{   
	    // ID, differenceQty
	    var id=rows[x].childNodes[0].innerHTML;
	    var qty=rows[x].childNodes[4].childNodes[0].value;
	    var currentQty=rows[x].childNodes[3].childNodes[1].value;
	    var remarks=rows[x].childNodes[5].childNodes[0].value;
	    itemString+=id+"~"+qty+"~"+currentQty+"~"+remarks+"|";
	}
	
	
	 var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) 
	    {
	    	window.location="?a=showStockStatus";
	    }
	  };
	  xhttp.open("POST", "?a=addStock", true);
	  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");	  
	  xhttp.send("itemDetails="+itemString+"&action=${param.type}"+"&outerremarks="+remarksouter.value+"&app_id=${userdetails.app_id}"+"&user_id=${userdetails.user_id}"); 
	
	
	
}



</script>



<br>



<div class="container" style="padding:20px;background-color:white">

<form id="frm"  method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="row">


<div class="col-sm-6">
  	<div class="form-group">      
  		<input type="text" class="form-control"    placeholder="Search for Items" list="itemList" id="txtitem" name="txtitem" oninput="checkforMatchItem()">          
    </div>
  </div>
  
  <div class="col-sm-6">
  	<div class="form-group">      
  		<input type="text" id="txtinvoicedate" name="txtinvoicedate" class="form-control form-control-sm" value="${todaysDate}" placeholder="Invoice Date" readonly/>          
    </div>
  </div>
  
  <datalist id="itemList">
<c:forEach items="${itemList}" var="item">
			    <option id="${item.item_id}">${item.item_name}~${item.product_code}</option>			    
	   </c:forEach></select>	   	   	
</datalist>


<div class="col-sm-12">  
	  <div class="card-body table-responsive p-0" style="height: 370px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center">
	                     <th style="z-index:0">Item Id</th>
	                     <th style="z-index:0">Product Code</th>
	  			<th style="z-index:0">Item Name</th>
	  			<th style="z-index:0">Current Stock</th>
	  			<th style="z-index:0">${param.type } Qty</th>
	  			<th style="z-index:0">Remarks</th>	  			  				  				  			  				  				  			
	  			<th></th>
	                    </tr>
	                  </thead>
	                </table>
	   </div>	
  </div>
  
  <div class="col-sm-12" align="center">
  	<div class="form-group">  	      
  		<input type="text" class="form-control" id="remarksouter" value=""  placeholder="eg. Remarks" name="remarksouter">				          
    </div>
  </div>
  
  

  
  <div class="col-sm-12" align="center">
  	<div class="form-group">  	      
  		<button class="btn btn-success" type="button" onclick='printLabels()'>Save</button>
		<button class="btn btn-danger" id="btncancel" type="reset" onclick='window.location="?a=showStockModifications"'>Cancel</button>		          
    </div>
  </div>
  
  
  
  
  
  
   <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 580px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
                     	<th><b>Store Name</b></th><th><b>Item name</b></th> <th><b>Qty</b></th><th><b>Invoice No</b></th><th><b>Updated date</b></th><th><b>Type</b></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${addStockList}" var="item">
					<tr >
						<td>${item.store_name}</td><td>${item.item_name}</td><td>${item.qty}</td><td>${item.invoice_no}</td><td>${item.FormattedUpdatedDate}</td><td>${item.type}</td>
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
  
  
		
	 
</div>

</form>

<script >
	
	

		document.getElementById("divTitle").innerHTML="${param.type} Stock";
		document.title +=" ${param.type} Stock ";
		
		$( "#txtinvoicedate" ).datepicker({ dateFormat: 'dd/mm/yy' });
		
		
		if('${stockModificationDetails.get(0)}'!='')
			{
			var m=0;
			txtinvoicedate.value='${stockModificationDetails.get(0).transactionDateFormatted}';
			
			//alert('${stockModificationDetails.get(0)}');
			
			<c:forEach items="${stockModificationDetails}" var="item">			
			m++;			
			var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(-1);	    	
	    	var cell0 = row.insertCell(0);
	    	var cell1 = row.insertCell(1);
	    	var cell2 = row.insertCell(2);
	    	var cell3 = row.insertCell(3);
	    	var cell4 = row.insertCell(4);
	    	var cell5 = row.insertCell(5);
	    	
	    	
	    	
	    	cell0.innerHTML = '${item.item_id}';
	    	cell1.innerHTML = '${item.product_code}';
	    	cell2.innerHTML = '${item.item_name}';	    	   	
	    	cell3.innerHTML = '${item.current_stock}';
	    	cell4.innerHTML = '${item.qty}';
	    	cell5.innerHTML = '${item.remarksinner}';
	    	
	    	
			
		    		//alert('${item.item_id}'+'-${item.item_name}'+'-${item.qty}'+'-${item.rate}'+'-${item.custom_rate}');			    
			</c:forEach>
				
			
			remarksouter.value='${stockModificationDetails.get(0).remarksouter}';
			
			$("#frm :input").prop('disabled', true);		
			$("[name=returnButtons]").prop('disabled', false);
			btncancel.disabled=false;
			
			}
		
		
		

</script>



