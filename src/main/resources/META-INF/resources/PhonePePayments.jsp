<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script >

function acceptPhonePayPayment(orderId)
{			
	
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    {
	    	
	    	alert(xhttp.responseText);
	    	  
	    	
	    	window.location.reload();			  
		}
	  };
	  xhttp.open("GET","?a=acceptPhonePayPayment&orderId="+orderId, true);    
	  xhttp.send();
	
}



</script>	



<c:set var="ListOfPhonePePayments" value='${requestScope["outputObject"].get("ListOfPhonePePayments")}' />
<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />
<c:set var="chkaccepted" value='${requestScope["outputObject"].get("chkaccepted")}' />




<br>
<div class="card">



<div class="card">
 <br>
 <div class="row">
 

                <div class="col-sm-1" align="center">
			<label for="txtfromdate">From Date</label>
		</div>
		
		<div class="col-sm-2" align="center">
			<div class="input-group input-group-sm" style="width: 200px;">
			 	<input type="text" id="txtfromdate" onchange="checkforvalidfromtodate();ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
			</div>
		</div>
		
		<div class="col-sm-1" align="center">
			<label for="txttodate">To Date</label>
		</div>
		
		<div class="col-sm-2" align="center">
			<div class="input-group input-group-sm" style="width: 200px;">
				<input type="text" id="txttodate"  onchange="checkforvalidfromtodate();ReloadFilters();"    name="txttodate" readonly class="form-control date_field"  placeholder="To Date"/>
			</div>
		</div>
		
		<div class="col-sm-1" align="center">



			<div class="form-check">
				<input type="checkbox" class="form-check-input"
					id="chkaccepted" onchange="ReloadFilters()"> <label
					class="form-check-label" for="chkdeletedinvoice">Accepted</label>
			</div>
		</div>


		
		
		
                <div class="card-tools">
                  <div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
                    <div class="icon-bar" style="font-size:22px;color:firebrick">
  <a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
  <a title="Download PDF" onclick="downloadPDF()"><i class="fa fa-file-pdf-o"></i></a>
  <a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
</div>           
                  </div>
                </div>
                
                
                
                
                

                
              </div>
              
              
              
              
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 580px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
                     
                     
                     <th><b>Bhim Upi Id</b></th>
                     <th><b>Amount</b></th>
                     <th><b>Date Time</b></th>
                     <th><b>Store Name</b></th>
                     
                     <th></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${ListOfPhonePePayments}" var="item">
					<tr >
						
						<td>${item.bhimupiidwithspace}</td>
						<td>${item.amount}</td>	
						<td>${item.date_time_from_payment}</td>						
						<td>${item.store_name}</td>
						<td>
						
						<c:if test="${item.accepted_shift_id eq null}">				
							<button class="btn btn-primary" onclick="acceptPhonePayPayment('${item.order_id}')">Accept</button>
						</c:if>
						
						</td>
						
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            
            
            
            



<script >
  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,
      "ordering": false,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 50
    });
  });
  
  
  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });



  txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';
  
  document.getElementById("divTitle").innerHTML="PhonePe Payments";
  
  
  function checkforvalidfromtodate()
  {        	
  	var fromDate=document.getElementById("txtfromdate").value;
  	var toDate=document.getElementById("txttodate").value;
  	
  	var fromDateArr=fromDate.split("/");
  	var toDateArr=toDate.split("/");
  	
  	
  	var fromDateArrDDMMYYYY=fromDate.split("/");
  	var toDateArrDDMMYYYY=toDate.split("/");
  	
  	var fromDateAsDate=new Date(fromDateArrDDMMYYYY[2],fromDateArrDDMMYYYY[1]-1,fromDateArrDDMMYYYY[0]);
  	var toDateAsDate=new Date(toDateArrDDMMYYYY[2],toDateArrDDMMYYYY[1]-1,toDateArrDDMMYYYY[0]);
  	
  	if(fromDateAsDate>toDateAsDate)
  		{
  			alert("From Date should be less than or equal to To Date");
  			window.location.reload();        			
  		}
  }
  
  function ReloadFilters()
  {	 	  
  	  		window.location="?a=showPhonePePayments&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value+"&chkaccepted="+chkaccepted.checked;
		  
  }  
  
  chkaccepted.checked=${chkaccepted};
  
</script>