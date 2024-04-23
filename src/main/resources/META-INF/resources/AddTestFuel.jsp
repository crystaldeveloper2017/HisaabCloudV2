<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
           
           

<c:set var="lstOfShifts" value='${requestScope["outputObject"].get("lstOfShifts")}' />
<c:set var="userList" value='${requestScope["outputObject"].get("userList")}' />

<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />
<c:set var="testData" value='${requestScope["outputObject"].get("testData")}' />




</head>


<script >


function addSwipe()
{	
	
	
	document.getElementById("frm").submit(); 
}








function addTestFuel()
{
		

	
	var nameAmounts=document.getElementsByName('nameAmounts');
	var collectionData="";
	for(var m=0;m<nameAmounts.length;m++)
		{
		collectionData+=(nameAmounts[m].id+"~"+nameAmounts[m].value)+"|";
		}
	
	var shiftId=document.getElementById('drpshiftid').value
	

	var stringToSend="testInputData="+collectionData+"&shift_id="+drpshiftid.value+"&testDate="+txttestdate.value;	
	




		//get values from textboxes and pass on in ajax
		// before sending you can alert to see if expected values are getting fetched from input controls..
		//but how will I receive the data
		
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
		  //alert("testInputData="+stringToSend+"&shift_id="+drpshiftid.value+"&testDate="+txttestdate.value);
		  
		xhttp.open("POST","?a=addTestFuel", true);		  
				xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

		  xhttp.send(stringToSend);
		
		
}








</script>



<br>

<div class="container" style="padding:20px;background-color:white">

<form id="frm" action="?a=addSwipe" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<input type="hidden" name="app_id" value="${userdetails.app_id}">
<input type="hidden" name="user_id" value="${userdetails.user_id}">
<input type="hidden" name="callerUrl" id="callerUrl" value="">

<div class="row">
  
  
  <div class="col-6">
  	<div class="form-group">
      <label for="email">Date</label>
 <input type="text" readonly class="form-control" id="txttestdate" placeholder="" name="txttestdate" value="${todaysDate }" onchange="getAttendantList()"> 
      
    </div>
  </div>


	<div class="col-6">
  	<div class="form-group">
      
    <label for="email">Shift Name</label>  
      <select class="form-control form-control-sm" name="drpshiftid" id="drpshiftid" onchange="getAttendantList()" >
      <option value="-1">----------Select----------</option>
      <c:forEach items="${lstOfShifts}" var="shift">
			    <option value="${shift.shift_id}">${shift.shift_name}~${shift.from_time}~${shift.to_time}~0</option>    
	 </c:forEach>
	   
	</select>
            
    </div>
  </div>


  
  
  
  

   
   <div id="someidgoeshere">
  	
  </div>


 
  
  
  <div class="col-sm-12" align="center">
  	<div class="form-group">
   <button class="btn btn-success" type="button" onclick='addTestFuel()'>Save</button>
		<button class="btn btn-danger" type="reset" onclick='window.location="?a=showAddTestFuel"'>Cancel</button>

 
      
    </div>
  </div>
  
   
      
    </div>
  </div>
 
   <div class="card">
   <div class="card-body table-responsive p-0" style="height: 480px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
                    <tr>
                    <th><b>Date</b></th>                  
                    <th><b>Test Quantity</b></th>
                    <th><b>Attendant Name</b>
                    <th><b>Updated By</b>
                    <th><b>Shift Name</b></th>
                    <th><b>Nozzle Name</b>
                    <th><b>Item Name</b>
                     
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${testData}" var="data">
					<tr >
					  
					  
					  <td>${data.test_date}</td>
					  <td>${data.test_quantity}</td>
					  <td>${data.attendantName}</td>
					  <td>${data.superVisorName}</td>
					  <td>${data.shift_name}</td>
					  <td>${data.nozzle_name}</td>
					  <td>${data.item_name}</td>
					  
					  <td><button class="btn btn-danger" onclick="deleteTestFuel('${data.test_id}')">Delete</button></td>
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
 		
		
		
		
</div>
</form>

<script >
	
	
	<c:if test="${SwipeDetails.SwipeMachineId eq null}">
		document.getElementById("divTitle").innerHTML="Add Test Fuel";
		document.title +=" Add Test Fuel ";
	</c:if>
	
	$( "#txttestdate" ).datepicker({ dateFormat: 'dd/mm/yy' });

function getAttendantList()
{
	var shift=drpshiftid.options[drpshiftid.selectedIndex].value;
	
	const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
    	var lsitofattendants=JSON.parse(this.responseText)["listofAttendants"];
    	

      var rowString='<div class="row">';
  	var k=`<div class="col-sm-3">
  	  	<div class="form-group">
        <label for="email">attendantNameGoesHere (Nozzle No : nozzleNoGoesHere)</label>
        
        <input type="text" value="0" name="nameAmounts" onfocus="this.select()" class="form-control" id="idGoesHere">
        
             
  	   </select>
              
      </div>
    </div>`;
    var rowStringClose='</div>';
      var reqString="";
      for(var m=0;m<lsitofattendants.length;m++)
    	  {
    	  	//console.log(lsitofattendants[m].username);
    	  	
    	  	var tempString=k.toString().replace("attendantNameGoesHere",lsitofattendants[m].username);
			tempString=tempString.replace("nozzleNoGoesHere",lsitofattendants[m].nozzle_name);			
    	  	tempString=tempString.replace("idGoesHere",lsitofattendants[m].user_id + "~" +lsitofattendants[m].nozzle_id);
    	  	reqString+=tempString;
    	  }
      document.getElementById("someidgoeshere").innerHTML=rowString+reqString+rowStringClose;
      //document.getElementById("someidgoeshere").innerHTML=reqString;
      
      
      var collectionData=JSON.parse(this.responseText)["testData"];
	  $("#example1 tr").remove();
      var table = document.getElementById("example1");	    	
  	var row = table.insertRow(-1);	    	
  	var cell1 = row.insertCell(0);
  	var cell2 = row.insertCell(1);
  	var cell3 = row.insertCell(2);
  	var cell4 = row.insertCell(3);
  	var cell5 = row.insertCell(4);
  	var cell6 = row.insertCell(5);
  	var cell7 = row.insertCell(6);
  	var cell8 = row.insertCell(7);

  	
  	cell1.innerHTML = '<b>Test Id';    	
  	cell2.innerHTML = '<b>Attendant Id';
  	cell3.innerHTML = '<b>Test Quantity';
  	cell4.innerHTML = '<b>Updated By';
  	cell5.innerHTML = '<b>Updated Date';
  	cell6.innerHTML = '<b>Shift No';
  	cell7.innerHTML = '<b>Test Date';  	
  	
  	
  	
    	for(var m=0;m<collectionData.length;m++)
    		{
    			console.log(collectionData);
    			var row = table.insertRow(-1);	    	
    		  	var cell1 = row.insertCell(0);
    		  	var cell2 = row.insertCell(1);
    		  	var cell3 = row.insertCell(2);
    		  	var cell4 = row.insertCell(3);
    		  	var cell5 = row.insertCell(4);
    		  	var cell6 = row.insertCell(5);
    		  	var cell7 = row.insertCell(6);
    		  	var cell8 = row.insertCell(7);
    		  	
    		  	cell1.innerHTML = collectionData[m].test_id;    	
    		  	cell2.innerHTML = collectionData[m].AttendantName;    	
    		  	cell3.innerHTML = collectionData[m].test_quantity;    	
    		  	cell4.innerHTML = collectionData[m].SupervisorName;    	
    		  	cell5.innerHTML = collectionData[m].updated_date;    	
    		  	cell6.innerHTML = collectionData[m].shift_name;    	
    		  	cell7.innerHTML =collectionData[m].test_date;    		  	
    			
    		}
      
      
    }
    xhttp.open("GET", "?a=getAttendantsForDateAndShiftUnclubbed&collection_date="+txttestdate.value+"&shift_id="+shift+"&type=S");
    xhttp.send();
	
	
	
}
	
</script>






