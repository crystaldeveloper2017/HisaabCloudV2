

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="message" value='${requestScope["outputObject"].get("ListStock")}' />





<script >
function deleteStockStatusBeverage(stockId)
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
	  xhttp.open("GET","?a=deleteStockStatusBeverage&stockId="+stockId, true);    
	  xhttp.send();
}

function configureLowStock(stockId)
{
		window.location='?a=showConfigureLowStock&stockId='+stockId;
}

</script>	







<br>
<div class="card">









           <div class="card-header">    
                
         
                
                <div class="card-tools">

                  <div class="input-group input-group-sm" >                    
                    <input type="button"  class="btn btn-primary btn-sm" style="margin-right:11px;" onclick="window.location='?a=showAddStockBeverage&type=Add'" value="Add Stock Beverage" class="form-control float-right" >                                         
                  </div>
                </div>
                
                <div class="card-tools">
                  <div class="input-group input-group-sm" >                    
                    <input type="button"  class="btn btn-primary btn-sm" style="margin-right:11px;" onclick="window.location='?a=showAddStockBeverage&type=Remove'" value="Damaged Stock" class="form-control float-right" >                                         
                  </div>
                </div>
                
                <div class="card-tools">
                  <div class="input-group input-group-sm" align="center"  style="width: 200px;display:inherit">
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
                     <th>Stock Id</th>
                     <th>Item Name</th>
                     <th>Stock Date</th>
                     <th>Stock Type</th>                      
                     <th>Qty</th> 
                    <th> Remarks</th>                      
                     

                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${message}" var="item">		
				
						<td>${item.stock_id}</td>
          <td> ${item.item_name} ${item.product_code}</td>
						<td>${item.stock_date}</td>
						<td>${item.stock_type}</td>
            <td>${item.qty}</td>
            <td>${item.remarks}</td>


				<td><button class="btn btn-danger" onclick="deleteStockStatusBeverage('${item.stock_id}')">Delete</button></td>

						
					
						
							 																				 
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
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 50
    });
  });
  
  document.getElementById("divTitle").innerHTML="Stock Status Beverage";
  document.title +=" Stock Status Beverage ";
  
  function ReloadFilters()
  {
	  window.location="?a=showStockStatus&categoryId="+drpcategoryId.value+"&storeId="+drpstoreId.value;
	  
  }
 
</script>