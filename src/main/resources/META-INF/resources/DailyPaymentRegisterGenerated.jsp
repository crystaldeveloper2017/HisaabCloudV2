<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="type" value='${requestScope["outputObject"].get("type")}' />
<c:set var="message" value='${requestScope["outputObject"].get("dailyPaymentData")}' />



<br>
<div class="card">

           <div class="card-header">    
                
                
                
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
                     <th><b>Customer Name</b></th>
                     <th><b>Payment Mode</b></th>
                     <th><b>Payment For</b></th> 
                     <th><b>Payment Date</b></th>
                     <th><b>Amount</b></th>
                     <th><b>Updated By</b></th>
                     <th><b>Updated Date</b></th>
                     <th><b></b></th>
                     <th></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${message}" var="item">
					<tr >
						<td>${item.customer_name}</td>
            <td>${item.payment_mode}</td>
             <td>${item.payment_for}</td>
             <td>${item.FormattedPaymentDate}</td>
						<td>${item.amount}</td>
						<td>${item.name}</td>
						<td>${item.FormattedUpdatedDate}</td>
						
						<td><button class="btn btn-danger" onclick="deletePayment('${item.payment_id}')">Delete</button></td>
        	<td><button class="btn btn-primary" onclick="generatepdf('${item.payment_id}')">Generate Pdf</button></td>

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
  
  document.getElementById("divTitle").innerHTML="${type} Register";
  document.title +=" ${type} Register ";
  
</script>


<script >
function deletePayment(paymentId)
{
	
	var answer = window.confirm("Are you sure you want to delete ?");
	if (!answer) 
	{
		return;    
	}
	
	  

	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		      
	    	toastr["success"](xhttp.responseText);
	    	toastr.options = {"closeButton": false,"debug": false,"newestOnTop": false,"progressBar": false,
	    	  "positionClass": "toast-top-right","preventDuplicates": false,"onclick": null,"showDuration": "1000",
	    	  "hideDuration": "500","timeOut": "500","extendedTimeOut": "500","showEasing": "swing","hideEasing": "linear",
	    	  "showMethod": "fadeIn","hideMethod": "fadeOut"}
	    	
	    	window.location.reload();
		  
		}
	  };
	  xhttp.open("GET","?a=deletePayment&user_id=${userdetails.user_id}&paymentId="+paymentId, true);    
	  xhttp.send();
}


function generatepdf(paymentId)
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
	  xhttp.open("GET","?a=generatePaymentPDF&paymentId="+paymentId, false);    
	  xhttp.send();
}

</script>