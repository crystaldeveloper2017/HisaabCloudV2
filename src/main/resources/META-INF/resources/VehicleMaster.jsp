<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="javascript">
function deleteItem(vehicleId)
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
	  xhttp.open("GET","?a=deleteVehicle&vehicleId="+vehicleId, true);    
	  xhttp.send();
}




</script>	



<c:set var="vehicleData" value='${requestScope["outputObject"].get("vehicleDetails")}' />




<br>

<div class="card">

<br>







                    
           <div class="card-header">    
                
                
                <div class="card-tools">
                  <div class="input-group input-group-sm" style="width: 200px;">
                    <input type="button" class="btn btn-block btn-primary btn-sm" onclick="window.location='?a=showAddVehicle'" value="Add Vehicle" >                      
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
                     <th><b>Customer Name</b></th>
                     <th><b>Vehicle Name</b></th>
                     <th><b>Vehicle Number</b></th>
                     <th><b>Updated By</b></th>
                     <th><b>Updated_date</b></th>
                     
                  </thead>
                  <tbody>
				<c:forEach items="${vehicleData}" var="item">
					<tr >
					
						<td>${item.customer_name}</td>
						<td>${item.vehicle_name}</td>
						<td>${item.vehicle_number}</td>
						<td>${item.updated_by}</td>					
						<td>${item.updated_date}</td>
						<td><a href="?a=showAddVehicle&vehicleId=${item.vehicle_id}">Edit</a></td>
						<td><button class="btn btn-danger" onclick="deleteItem('${item.vehicle_id}')">Delete</button></td>
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            
            
            
            



<script type="javascript">
  $(function () {
    
    $('#example1').DataTable({
      "paging": true,      
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 50,
      "order": [[ 1, "asc" ]]
    });
  });
  
  document.getElementById("divTitle").innerHTML="Vehicle Master";
  document.title +=" Vehicle Master ";
  
  function actualSearch()
  {
	  window.location="?a=showItemMaster&searchInput="+txtsearch.value+"&categoryId="+drpcategoryId.value;  
  }
  
  function searchprod(evnt)
	{
		if(evnt.which==13)
			{
				// do some search stuff
				actualSearch();					
			}
			
	}
  
  function showThisCategory()
  {
	  window.location="?a=showItemMaster&searchInput="+txtsearch.value
	  		  +"&categoryId="+drpcategoryId.value;
	  
  }
  
  drpcategoryId.value='${param.categoryId}';
  
  
  
  window.addEventListener('keydown', function (e) {
		if(event.which==113)
		{
			window.location='?a=showAddVehicle';
		} 
		});
  
</script>