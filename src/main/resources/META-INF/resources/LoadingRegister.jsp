<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="lstITodaysStockRegister" value='${requestScope["outputObject"].get("lstITodaysStockRegister")}' />



<br>

 <div class="card">
 <br>
 <div class="row">
 
		
		<div class="col-sm-1" align="center">
			<label for="txtfromdate">From Date</label>
		</div>
		<div class="col-sm-2" align="center">
		<div class="input-group input-group-sm" style="width: 200px;">
		<input type="text" id="txtfromdate" onchange="ReloadFilters();"  name="txtfromdate" readonly class="form-control date_field" placeholder="From Date"/>
		</div>
	</div>
		
	
		
		
		<input  type="hidden" name="customerId" id="customerId" value="">
		
		
		
		
		<div class="col-sm-2" align="center">
			<div class="card-tools">
				<div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
					<div class="icon-bar" style="font-size:22px;color:firebrick">
						<a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
 						<a title="Download PDF" onclick="exportSalesRegister2()"><i class="fa fa-file-pdf-o"></i></a>
  						<a title="Download Text"  onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
					</div>           
				</div>
			</div>
		</div>
	</div>
	<br>
              
              
               
                
              
              
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 800px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                
                 
                    <tr>
                    <th><b>Loading Date</b></th>
                    <th><b>Vehicle</b></th>    
					<th><b>Order Cities</b></th> 
					<th><b>Status</b></th> 
					<th></th> 
                 
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${lstITodaysStockRegister}" var="item">
					<tr >
                  
					  
					  
					  <td>${item.loading_date}</td>
					 <td>${item.vehicle_name} -  ${item.vehicle_number}</td>
					  <td>${item.order} </td>
					  <td>${item.LoadingStatus} </td>

					  <td>
					  <c:if test="${item.LoadingStatus == 'In Progress'}">
    						    <button class="btn btn-primary" onclick="window.location='?a=showChooseOrderForLoading&loading_id=${item.loading_id}'">Load</button>
					  </c:if>
					  </td>
					 

					  
						
					</tr>
				</c:forEach>
				
				
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
             
</div>
            
            
       
            
   

<script>





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


  txtfromdate.value='${txtfromdate}';

  function exportSalesRegister2()
  {
	try
	{
	
		window.open("?a=exportSalesRegister2&txtfromdate="+txtfromdate.value);
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
  
  
  
  function ReloadFilters()
  {	 	  
  	  		window.location="?a=showTodaysStockRegister&txtfromdate="+txtfromdate.value;
		  
  }
  
  
  
  
  
  
  document.getElementById("divTitle").innerHTML="Loading Register";
  document.title +=" Loading Register";
  
  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  
  


</script> 