<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<c:set var="message" value='${requestScope["outputObject"].get("message")}' />
<c:set var="lstPendingRegister" value='${requestScope["outputObject"].get("lstPendingRegister")}' />






<br>

 <div class="card">
 

	
          
                
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 600px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
                    <tr>  
				             
				
					
<th><b>City (Qty)</b></th>
                     
                     
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${lstPendingRegister}" var="pendData">
					<tr >
					  
					  
					  
					             
							<td >
													
									
										
										                                <input type="checkbox" name="namecheckboxes" class="row-checkbox" value="" id="${pendData.invoice_id}">
																		${pendData.city} (${pendData.totalQty})

								
							</td>
					
					

					  
					  
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
             
</div>
            
            
      <div class="col-sm-12">
  	 <div class="form-group" align="center">	  
	   	<button class="btn btn-success" type="button" id="btnsave" onclick='moveToPlanning()'>Move to Planning</button>   
	   
	   
	   
	  	   
	   <button class="btn btn-primary" style="display:none" id="generatePDF" type="button" onclick='generateInvoice("${invoiceDetails.invoice_id}");'>Generate PDF</button>
   </div>
   </div>
  
</div> 
            
   

<script >


function deleteInvoice(invoice_id)
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
	  xhttp.open("GET","?a=deleteInvoice&invoiceId="+invoice_id+"&type=S&store_id=${userdetails.store_id}", true);    
	  xhttp.send();
}


  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,      
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 100,
      "order": [[ 2, 'desc' ]]

    });
  });


  
  
  
  
  function exportSalesRegister2()
  {
	try
	{
	
		window.open("?a=exportSalesRegister2&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value+"&customerId="+hdnSelectedCustomer.value);
		return;
		
	  var xhttp = new XMLHttpRequest();
		  xhttp.onreadystatechange = function() 
		  {
		    if (xhttp.readyState == 4 && xhttp.status == 200) 
		    { 		      
		    	//window.location="BufferedImagesFolder/"+xhttp.responseText;
		    	window.open("BufferedImagesFolder/"+xhttp.responseText,'_blank','height=500,width=500,status=no, toolbar=no,menubar=no,location=no');
			}
		  };
		  xhttp.open("GET","?a=exportSalesRegister2", true);    
		  xhttp.send();
	}
	catch(ex)
	{
		alert(ex.message);
	}
  }
  
  
  
 
  document.getElementById("divTitle").innerHTML="Pending Orders ";
  document.title +=" Pending Orders ";


  
        $(document).ready(function() {
            $('.table tbody tr').click(function(event) {
                // Avoid checking/unchecking when clicking directly on the checkbox
                if (!$(event.target).is('.row-checkbox')) {
                    var checkbox = $(this).find('.row-checkbox');
                    checkbox.prop('checked', !checkbox.prop('checked'));
                }
            });
        });


		function moveToPlanning()
		{
			var checkbxos=document.getElementsByName("namecheckboxes");
			var requiredInvoiceIds="";

			for(var k=0;k<checkbxos.length;k++)
			{
				if(checkbxos[k].checked==true)
				{
					
					requiredInvoiceIds+=checkbxos[k].id+"~";
				}
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
	  xhttp.open("GET","?a=moveToPlanning&invoiceIds="+requiredInvoiceIds, true);    
	  xhttp.send();



			

		}
	
  

</script> 