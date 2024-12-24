<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


           
           


<c:set var="lstOfVehicles" value='${requestScope["outputObject"].get("lstOfVehicles")}' />


   





</head>


<script >


function addDispenser()
{	
	
	
	document.getElementById("frm").submit(); 
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



<br>

<div class="container" style="padding:20px;background-color:white">

<form id="frm" action="?a=startVehicleLoading" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<input type="hidden" name="app_id" value="${userdetails.app_id}">
<input type="hidden" name="user_id" value="${userdetails.user_id}">
<input type="hidden" name="callerUrl" id="callerUrl" value="">

<div class="row">
  <div class="col-sm-12">
  	<div class="form-group">
      <label for="email">Loading Vehicle</label>

		<select class="form-control" name="drpvehicleid" id="drpvehicleid">
                    <c:forEach items="${lstOfVehicles}" var="item">
                        <option value="${item.vehicle_id}">${item.vehicle_name} ${item.vehicle_id}</option>             
                    </c:forEach>
                </select>

      </div>
  </div>
  
  
  
 
  
  
 		
		<button class="btn btn-success" type="button" onclick='addDispenser()'>Save</button>
		<button class="btn btn-danger" type="reset" onclick='window.location="?a=showDispenserMaster"'>Cancel</button>
		
		
</div>
</form>

<script >
	
	
	
		document.getElementById("divTitle").innerHTML="Choose Vehicle For Loading";
		document.title +=" Choose Vehicle For Loading ";
	
</script>



