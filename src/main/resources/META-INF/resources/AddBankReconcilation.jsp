  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
`           
           
           
<c:set var="BankDetails" 
   value='${requestScope["outputObject"].get("BankDetails")}' />
<c:set var="ListOfBanks"
	value='${requestScope["outputObject"].get("ListOfBanks")}' />

<c:set var="reconcilationDate"
	value='${requestScope["outputObject"].get("reconcilationDate")}' />



</head>


<script >


function addBank()
{	
	
	
	document.getElementById("frm").submit(); 
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





<div class="container" style="padding:20px;background-color:white">

<form id="frm" action="?a=addBankReconcilation" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="row">
  
  
   <div class="col-sm-6">
  	<div class="form-group">
      <label for="email" id="lblBankId">Bank Name</label> 
      
      <input type="hidden" name="hdnReconcilationId" value="${BankDetails.bank_id}" id="hdnReconcilationId">
      <select id="drpBankId" name="drpBankId" class="form-control" onchange="getReconcilationDataForThisDate()">
      
      <c:forEach items="${ListOfBanks}" var="BankDetails">
						<option id="${BankDetails.bank_id}" value="${BankDetails.bank_id}">${BankDetails.bank_name} ${BankDetails.account_no}
							</option>
					</c:forEach>
     
			</select>
      
    </div>
  </div>
  <div class="col-sm-6">
					<div class="form-group" >
						<label>Reconcilation Date</label>
						<input type="text" id="txtreconcilationdate" name="txtreconcilationdate"
							class="form-control form-control-sm" value="${reconcilationDate}" onchange="getReconcilationDataForThisDate()"
							placeholder="Reconcilation Date" readonly />
					</div>
   </div>



  
 
  
 
  
  
  
  
 
  
  
  
  
</div>
</div>
</form>



<br>

<div class="container" style="padding:20px;background-color:white">
	<div class="row" id="someRowId">

	</div>
</div>

<script >
	
	
	<c:if test="${BankDetails.Bank_id eq null}">
		document.getElementById("divTitle").innerHTML="Add Bank Reconcilation";
		
		$( "#txtreconcilationdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
	</c:if>
	<c:if test="${BankDetails.Bank_id ne null}">
		document.getElementById("divTitle").innerHTML="Update Bank Reconcilation";		
		drpheadid.value='${BankDetails.user_id}';
		drpBankId.value="${BankDetails.bank_name}";
	</c:if>


	function getReconcilationDataForThisDate()
	{
		$.get("?a=getrecondata&bank_id="+drpBankId.value+"&recondate="+txtreconcilationdate.value, function(data, status){
    		//alert("Data: " + data + "\nStatus: " + status);
			var lstOfTransactions=JSON.parse(data);

			var textMachineName=`<div class="col-sm-3">
					<div class="form-group" >
						<label>Machine Name</label>
						<input type="text" class="form-control form-control-sm" value="machineNameGoesHere"
							placeholder="Reconcilation Date" readonly />
					</div>
   </div>`;

   var textSlotName=`<div class="col-sm-3">
					<div class="form-group" >
						<label>Slot Name</label>
						<input type="text" class="form-control form-control-sm" value="slotNameGoesHere"
							placeholder="Reconcilation Date" readonly />
					</div>
   </div>`;

   var textAmount=`<div class="col-sm-3">
					<div class="form-group" >
						<label>Amount Name</label>
						<input type="text" class="form-control form-control-sm" value="amountGoesHere"
							placeholder="Reconcilation Date" readonly />
					</div>
   </div>`;
   var buttons=`<div class="col-sm-3">
					<div class="form-group" >	
					<button type="button" class="btn btn-success btn-flat" onclick="settleThisTransaction('invoiceIdgoeshere')">Settle</button>
					<button type="button" class="btn btn-primary btn-flat" onclick="showSplitModal('invoiceIdgoeshere','totalAmountGoeshere')">Split</button>
					</div>
   </div>`;
   
   
   var finalString="";
			for(var m=0;m<lstOfTransactions.length;m++)
			{
				//console.log(lstOfTransactions[m]);
				finalString+=textMachineName.replaceAll("machineNameGoesHere",lstOfTransactions[m].swipe_machine_name);				
				finalString+=textSlotName.replaceAll("slotNameGoesHere",lstOfTransactions[m].slot_id);				

				finalString+=textAmount.replaceAll("amountGoesHere",lstOfTransactions[m].total_amount);


				buttonsString=buttons.replaceAll("invoiceIdgoeshere",lstOfTransactions[m].invoice_id);
				buttonsString=buttonsString.replaceAll("totalAmountGoeshere",lstOfTransactions[m].total_amount);

				finalString+=buttonsString;
			}
			someRowId.innerHTML=finalString;




  		});
	}


	function showSplitModal(invoiceId,amount)
	{
		
		var modalContent=`
		<div class="row">
			
			<div class="col-sm-6">
				<label> Total Amount</label>
				<input type="text" class="form-control" id="txttotalamount" value=`+amount+` readonly/>
			</div>

			<div class="col-sm-3">
				<label> Amount 1</label>
				<input type="text" class="form-control" id="txtamount1" onkeyup="calculateTextboxAmount()"  value="0" />
			</div>

			<div class="col-sm-3">
				<label> Amount 2</label>
				<input type="text" class="form-control" id="txtamount2" value="0" readonly />
			</div>

			<div class="col-sm-12">				
				<button type="button" class="btn btn-primary btn-flat" onclick="saveSplitEntries(`+invoiceId+`)">Save</button>
			</div>

		</div>
		 `;

		document.getElementById("responseText").innerHTML=modalContent;
			  document.getElementById("closebutton").style.display='block';
			  document.getElementById("loader").style.display='none';
			  $("#myModal").modal();
	}

	function calculateTextboxAmount()
	{
		txtamount2.value=Number(txttotalamount.value)- Number(txtamount1.value);
	}
	function saveSplitEntries(invoiceid)
	{

		  $.get("?a=saveSplitInvoice&invoiceid="+invoiceid+"&amount1="+txtamount1.value+"&amount2="+txtamount2.value, function(data, status){
    		alert("Data: " + data + "\nStatus: " + status);
  		  });		
	}

	function settleThisTransaction(invoiceid)
	{

		  $.get("?a=settleThisTransaction&invoiceid="+invoiceid, function(data, status){
    		//alert("Data: " + data + "\nStatus: " + status);
			window.location.reload();
  		  });		
	}

	

	txtreconcilationdate.value="26/07/2023";
	drpBankId.value="4";
	getReconcilationDataForThisDate();
	
</script>



