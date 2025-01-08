<style>
	.date_field {position: relative; z-index:1000;}
	.ui-datepicker{position: relative; z-index:1000!important;}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:set var="txtfromdate" value='${requestScope["outputObject"].get("txtfromdate")}' />
<c:set var="txttodate" value='${requestScope["outputObject"].get("txttodate")}' />

<c:set var="ListStock" value='${requestScope["outputObject"].get("ListStock")}' />

<c:set var="todaysDateMinusOneMonth" value='${requestScope["outputObject"].get("todaysDateMinusOneMonth")}' />
<c:set var="todaysDate" value='${requestScope["outputObject"].get("todaysDate")}' />



<br>

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



        <div class="card-tools">
            <div class="input-group input-group-sm" >                    
                      <input type="button"  class="btn btn-primary btn-sm" style="margin-right:11px;" onclick="window.location='?a=showAddStockDirect&type=Add'" value="Add Stock Direct" class="form-control float-right" >                                         
                      </div>
                    </div>

    <div class="card-tools">
                      <div class="input-group input-group-sm" >                    
                        <input type="button"  class="btn btn-primary btn-sm" style="margin-right:11px;" onclick="window.location='?a=showAddStockDirect&type=Damage'" value="Damaged Stock" class="form-control float-right" >                                         
                      </div>
                    </div>
    
        
        
		
		<input  type="hidden" name="customerId" id="customerId" value="">
		
		
		
		
		<div class="col-sm-2" align="center">
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
	</div>
	<br>
              
              
                
              <!-- /.card-header -->
              <div class="card-body table-responsive p-0" style="height: 800px;">                
                <table id="example1"class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
                  <thead>
                    <tr>
                      <th>Stock Id</th>
                      <th>Item Name</th>
                      <th>Stock Date</th>
                      <th>Stock Type</th>                      
                      <th>Qty</th> 
                      <th> Remarks</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
						<c:forEach items="${ListStock}" var="item">		

					<tr >


						<td>${item.stock_id}</td>
          <td> ${item.item_name} ${item.product_code}</td>
						<td>${item.stock_date}</td>
						<td>${item.stock_type}</td>
            <td>${item.qty}</td>
            <td>${item.remarks}</td>



				<td>
          <c:if test="${item.stock_type ne 'Debit'}">
            <button class="btn btn-danger" onclick="deleteStockStatusDirect('${item.stock_id}')">Delete</button>
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
      "info": true,
      "autoWidth": false,
      "responsive": true,
      "pageLength": 100,
      "order": [[ 2, 'desc' ]]

    });
  });


  txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';
 customerId.value='${param.customerId}';
  
 
  
  
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
  	  		window.location="?a=showStockRegisterDirect&txtfromdate="+txtfromdate.value+"&txttodate="+txttodate.value;
		  
  }


    function deleteStockStatusDirect(stockId)
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
        xhttp.open("GET","?a=deleteStockStatusDirect&stockId="+stockId, true);    
        xhttp.send();
    }



   document.getElementById("divTitle").innerHTML="Stock Register (Direct)";
  document.title +=" Stock Register (Direct) ";
  
  $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
  $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });

 txtfromdate.value='${txtfromdate}';
  txttodate.value='${txttodate}';


</script> 