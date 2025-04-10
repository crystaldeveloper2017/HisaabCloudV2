  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
           
           


<c:set var="listOfVehicle" value='${requestScope["outputObject"].get("listOfVehicle")}' />
   





</head>


<script >

function removethisitem(btn1)
{
	btn1.parentElement.parentElement.remove();	 
}

function checkforMatchVehicle()
{
	var searchString= document.getElementById("txtvehicle").value;	
	var options1=document.getElementById("listOfVehicle").options;
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
			document.getElementById("hdnselectedvehicle").value=vehicleId;			
			document.getElementById("txtvehicle").disabled=true;						
		}
	else
		{
			//searchForCustomer(searchString);
		}
				getItemDetailsAndAddToTable(vehicleId,searchString);

}


function getItemDetailsAndAddToTable(itemId,itemName)
{
	document.getElementById("closebutton").style.display='none';
	document.getElementById("loader").style.display='block';	    	
	    	
	    	var table = document.getElementById("tblitems");	    	
	    	var row = table.insertRow(-1);
	    	var cell0 = row.insertCell(0);
	    	var cell1 = row.insertCell(1);
	    	var cell2 = row.insertCell(2);
	    	var cell3 = row.insertCell(3);
	    	var cell4 = row.insertCell(4);
	    	
	    	var arritemName=itemName.split('~');
	    	
	    	
	    	cell0.innerHTML = itemId;
	    	cell1.innerHTML = arritemName[1];
	    	cell2.innerHTML = arritemName[2];
			cell3.innerHTML = arritemName[0];

	    	
	    	cell4.innerHTML = '<button type="button" class="btn btn-danger"  onclick=removethisitem(this) id="btn11" style="cursor:pointer">Delete</button>';
	    	
	    	
	    
		
	  
			
}


function generateQrForVehicle()
{
	
	var rows=tblitems.rows;
	
	var requiredDetails=[];
	 
	var arr = [];
	var itemString="";
	var confirmMessage="";
	var proceedFlag=true;
	for (var x= 1; x < rows.length; x++) 
	{   
	    // ID, Product Code, Item Name,No Of Labels,isPrintPrice
	    itemString+=
	    	rows[x].childNodes[0].innerHTML+
	    "~"+rows[x].childNodes[1].innerHTML+
	    "~"+rows[x].childNodes[2].innerHTML+
	    "~"+rows[x].childNodes[3].childNodes[1].value+
	    "~"+rows[x].childNodes[4].childNodes[0].checked+
	    "|";
	}
	
	 var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) 
	    {
	    	window.open("BufferedImagesFolder/"+xhttp.responseText);
	        
	      
	    }
	  };
	  xhttp.open("POST", "?a=generateQrForVehicle", true);
	  xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	  xhttp.send("itemDetails="+itemString+"&type="+drptype.value);
	
	
	
}



</script>


 <datalist id="listOfVehicle">
<c:forEach items="${listOfVehicle}" var="vehicle">
	<option id="${vehicle.vehicle_id}">${vehicle.customer_name}  ~ ${vehicle.vehicle_name} ~ ${vehicle.vehicle_number} ~ ${vehicle.preferred_fuel_type} </option>	

			    
	   </c:forEach></select>	   	   	
</datalist>



<br>

<div class="container" style="padding:20px;background-color:white">

<form id="frm"  method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="row">




<div class="col-sm-12">
  	<div class="form-group">      
  		<input type="text" class="form-control"  placeholder="Search for Vehicle" list="listOfVehicle" id="txtvehicle" name="txtvehicle" oninput="checkforMatchVehicle()">  
			<input type="hidden" name="hdnselectedvehicle"  value="" id="hdnselectedvehicle">  
        
    </div>
  </div>
  
 

  


<div class="col-sm-12">  
	  <div class="card-body table-responsive p-0" style="height: 370px;">                
	                <table id="tblitems"  class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
	                  <thead>
	                    <tr align="center">
	                     <th style="z-index:0">Vehicle Id</th>
	                     <th style="z-index:0">Vehicle Name</th>
						<th style="z-index:0">Vehicle Number</th>

	  			<th style="z-index:0">Customer Name</th>

<th> </th>
	  		
	                    </tr>
	                  </thead>
	                </table>
	   </div>	
  </div>
  
  
  
  <div class="col-sm-12" align="center">
  	<div class="form-group">
  	<select id="drptype" name="drptype" class="form-control" >
  		<option value="QR">QR</option>
  		<option value="Barcode">Barcode</option>
  	</select>      
  		<button class="btn btn-success" type="button" onclick='generateQrForVehicle()'>Print</button>
		<button class="btn btn-danger" type="reset" onclick='window.location="?a=showHomePage"'>Cancel</button>          
    </div>
  </div>
  
  
		
	 
</div>
</form>

<script >
	
	

		document.getElementById("divTitle").innerHTML="Generate Qr For Vehicle";
		document.title +=" Generate Qr For Vehicle ";

</script>



