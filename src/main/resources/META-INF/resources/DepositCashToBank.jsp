<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
           
           
           


<c:set var="SwipeDetails" value='${requestScope["outputObject"].get("SwipeDetails")}' />

<c:set var="BankDetails" value='${requestScope["outputObject"].get("BankDetails")}' />
<c:set var="ListOfBanks" value='${requestScope["outputObject"].get("ListOfBanks")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />


   





</head>


<script >


function addDepositCashToBank()
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



  function depositDateChange()
  {
		
		window.location="?a=showDepositCashToBank&depositDate="+txtdate.value;
  }




</script>



<br>

<div class="container" style="padding:20px;background-color:white">

<form id="frm" action="?a=addDepositCashToBank" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<input type="hidden" name="app_id" value="${userdetails.app_id}">
<input type="hidden" name="user_id" value="${userdetails.user_id}">
<input type="hidden" name="callerUrl" id="callerUrl" value="">

<div class="row">


  
  

  <div class="col-sm-6">
	  	<div class="form-group">
      <label for="email">Bank Account Name </label>     
      <select class="form-control" name="txtaccountid" id="txtaccountid">
      <c:forEach items="${ListOfBanks}" var="item">
       <option value="${item.bank_id}">${item.bank_name} ${item.account_no}  </option>
       </c:forEach>
	  </select>     
    </div>
  </div>
  
  

  <div class="col-sm-6">
  	<div class="form-group">
      <label for="email">Deposit Date </label>
      	<input type="text" class="form-control form-control-sm date_field" id="txtdate" name="txtdate" readonly   onchange="depositDateChange()" value="${todaysDate}"   name="txtdate">
     
    </div>
  </div>
    

  <div class="col-sm-6" >
  	<div class="form-group">
      <label for="email">Amount</label>
      <input type="text" class="form-control" id="txtamount" value="${itemDetails.amount}"  onkeyup="digitsOnly(event)" placeholder=" 0.0" name="txtamount">
    </div>
  </div>
  
  
 
 <div class="col-sm-12">
  	 <div class="form-group" align="center">

		<button class="btn btn-success" type="button" onclick='addDepositCashToBank()'>Save</button>
		<button class="btn btn-danger" type="reset" onclick='window.location="?a=showSwipeMaster"'>Cancel</button>
		
	</div>
	
	</div>

</div>
</form>

<script >
		$( "#txtdate" ).datepicker({ dateFormat: 'dd/mm/yy' });

	
	
		document.getElementById("divTitle").innerHTML=" Deposit Cash To Bank ";
		document.title +=" Deposit Cash To Bank ";
	
	
	
</script>



