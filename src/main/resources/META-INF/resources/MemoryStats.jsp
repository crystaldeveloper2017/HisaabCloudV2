<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<c:set var="auditList" value='${requestScope["outputObject"].get("auditList")}' />
<c:set var="latestUserHits" value='${requestScope["outputObject"].get("latestUserHits")}' />


<c:set var="memoryStats" value='${requestScope["outputObject"].get("memoryStats")}' />
<c:set var="activeConnections" value='${requestScope["outputObject"].get("activeConnections")}' />
<c:set var="freeMemory" value='${requestScope["outputObject"].get("freeMemory")}' />




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


	<div class="card-body table-responsive p-0" style="height: 580px;">
		<table id="example1"
			class="table table-head-fixed  table-bordered table-striped dataTable dtr-inline"
			role="grid" aria-describedby="example1_info">
			<thead>
				<tr>
					<th><b>Stats</b></th>
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${memoryStats}" var="item">
					<tr>
						<td>${item}</td>						
					</tr>
				</c:forEach>

				<tr>
					
					<td colspan="2">${freeMemory}</td>
				</tr>
			</tbody>
		</table>
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
      "pageLength": 100,
      "order": [[ 0, "desc" ]]
    });
  });
  
  document.getElementById("divTitle").innerHTML="Memory Statistics";
  document.title +=" Memory Statistics ";
  
</script>