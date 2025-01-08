
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>




<c:set var="ListStock" value='${requestScope["outputObject"].get("ListStock")}' />

<c:set var="todaysDateMinusOneMonth" value='${requestScope["outputObject"].get("todaysDateMinusOneMonth")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />



<br>

 <div class="card">
 <br>

	<br>
              
              
                
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 800px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>

                    <tr>
                    <th>Category Name</th>
                      <th>Item Name</th>
                      <th>Current Stock </th>
                   
                    </tr>
                  </thead>
                  <tbody>
						<c:forEach items="${ListStock}" var="item">		

					<tr >


						<td>${item.category_name}</td>
          <td> ${item.item_name} ${item.product_code}</td>
						<td>${item.qty_available}</td>


	 
		
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
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 100,
      "order": [[ 2, 'desc' ]]

    });
  });


  
  
 
  
   function ReloadFilters()
  {	 	  
  	  		window.location="?a=showStockStatusDirect";
		  
  }


   

   document.getElementById("divTitle").innerHTML="Stock Status (Direct)";
  document.title +=" Stock Status (Direct) ";
  
 


</script> 