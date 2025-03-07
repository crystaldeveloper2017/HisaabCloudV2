<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script >
function deleteCategory(categoryId)
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
	  xhttp.open("GET","?a=deleteCategory&categoryid="+categoryId, true);    
	  xhttp.send();
}






function addCategory()
{			
	
	var catName=document.getElementById('txtcategorynamepopup').value;
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() 
	  {
	    if (xhttp.readyState == 4 && xhttp.status == 200) 
	    { 		    	
	    	
	    	document.getElementById("responseText").innerHTML=xhttp.responseText;
		     document.getElementById("closebutton").style.display='block';
			   document.getElementById("loader").style.display='none';			  
		}
	  };
	  xhttp.open("GET","?a=addCategory&categoryName="+catName, true);    
	  xhttp.send();
	
}



</script>	



<c:set var="message" value='${requestScope["outputObject"].get("ListOfCategories")}' />



<br>
<div class="card">









           <div class="card-header">    
                
                
                <div class="card-tools">
                  <div class="input-group input-group-sm" style="width: 200px;">                    
                    <input type="button"  class="btn btn-block btn-primary btn-sm" onclick="window.location='?a=showAddCategory'" value="Add New Category" class="form-control float-right" >                      
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
              <div class="card-body table-responsive p-0" style="height: 800px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
               <c:if test="${userdetails.app_type ne 'SnacksProduction'}">

                     <th><b>Category Id</b></th>
                      </c:if>
                     <th><b>Fenali</b></th>
                     <th><b>Priya</b></th>
              <c:if test="${userdetails.app_type ne 'SnacksProduction'}">
      			
                     <th><b>Order No</b></th>
               </c:if>
                     <th></th><th></th>
                    </tr>
                  </thead>
                  <tbody>
				<c:forEach items="${message}" var="item">
					<tr >
                  <c:if test="${userdetails.app_type ne 'SnacksProduction'}">

						<td>${item.categoryId}</td>
                    </c:if>

						<td>${item.categoryName}</td>
						<td><a href="?a=showItemMaster&categoryId=${item.categoryId}">${item.cnt}</a></td>
        <c:if test="${userdetails.app_type ne 'SnacksProduction'}">

						<td>${item.order_no }</td>
        </c:if>
						<td><a href="?a=showAddCategory&categoryId=${item.categoryId}">Edit</a></td><td><button class="btn btn-danger" onclick="deleteCategory('${item.categoryId}')">Delete</button></td>
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
      "pageLength": 100
    });
  });
  
  document.getElementById("divTitle").innerHTML="Category Master";
  document.title +=" Category Master ";

    	 $('[data-widget="pushmenu"]').PushMenu("collapse");

  
</script>