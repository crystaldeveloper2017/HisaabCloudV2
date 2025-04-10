<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
function deleteVehicle(vehicleId) {
    var answer = window.confirm("Are you sure you want to delete?");
    if (!answer) {
        return;
    }
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            toastr["success"](xhttp.responseText);
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "newestOnTop": false,
                "progressBar": false,
                "positionClass": "toast-top-right",
                "preventDuplicates": false,
                "onclick": null,
                "showDuration": "1000",
                "hideDuration": "500",
                "timeOut": "500",
                "extendedTimeOut": "500",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            };
            window.location.reload();
        }
    };
    xhttp.open("GET", "?a=deleteVehicle&vehicleId=" + vehicleId, true);
    xhttp.send();
}
</script>

<c:set var="vehicleData" value='${requestScope["outputObject"].get("vehicleDetails")}' />

<br>

<div class="card">
    <div class="card-header">    
        <div class="card-tools">
            <div class="input-group input-group-sm" style="width: 200px;">
                <input type="button" class="btn btn-block btn-primary btn-sm" onclick="window.location='?a=showAddVehicle'" value="Add Vehicle">                      
            </div>
        </div>
        
        <div class="card-tools">
            <div class="input-group input-group-sm" align="center" style="width: 200px;display:inherit">
                <div class="icon-bar" style="font-size:22px;color:firebrick">
                    <a title="Download Excel" onclick="downloadExcel()"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a> 
                    <a title="Download PDF" onclick="downloadPDF()"><i class="fa fa-file-pdf-o"></i></a>
                    <a title="Download Text" onclick="downloadText()"><i class="fa fa-file-text-o"></i></a>  
                </div>           
            </div>
        </div>
    </div>
  
    <div class="card-body table-responsive p-0" style="height: 800px;">                
        <table id="example1" class="table table-head-fixed table-bordered table-striped dataTable dtr-inline" role="grid" aria-describedby="example1_info">
            
            <c:if test="${userdetails.app_type == 'SnacksProduction'}">
                <thead>
                    <tr>
                        <th><b>Vehicle Name</b></th>
                        <th><b>Vehicle Number</b></th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${vehicleData}" var="vehicle">
                        <tr>
                            <td>${vehicle.vehicle_name}</td>
                            <td>${vehicle.vehicle_number}</td>
                            <td><a href="?a=showAddVehicle&vehicleId=${vehicle.vehicle_id}">Edit</a></td>
                            <td><button class="btn btn-danger" onclick="deleteVehicle('${vehicle.vehicle_id}')">Delete</button></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </c:if>

            <c:if test="${userdetails.app_type != 'SnacksProduction'}">
                <thead>
                    <tr>
                        <th><b>Customer Name</b></th>
                        <th><b>Vehicle Name</b></th>
                        <th><b>Vehicle Number</b></th>
                        <th><b>Updated By</b></th>
                        <th><b>Updated_date</b></th>
                        <th><b>Preferred Fuel Type</b></th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${vehicleData}" var="vehicle">
                        <tr>
                            <td>${vehicle.customer_name}</td>
                            <td>${vehicle.vehicle_name}</td>
                            <td>${vehicle.vehicle_number}</td>
                            <td>${vehicle.updated_by}</td>
                            <td>${vehicle.updated_date}</td>
                            <td>${vehicle.preferred_fuel_type}</td>
                            <td><a href="?a=showAddVehicle&vehicleId=${vehicle.vehicle_id}">Edit</a></td>
                            <td><button class="btn btn-danger" onclick="deleteVehicle('${vehicle.vehicle_id}')">Delete</button></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </c:if>
        </table>
    </div>
</div>

<script>
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
      "order": [[1, "asc"]]
    });
  });
  
  document.getElementById("divTitle").innerHTML = "Vehicle Master";
  document.title += " Vehicle Master ";

  window.addEventListener('keydown', function (e) {
    if(event.which == 113) {
        window.location = '?a=showAddVehicle';
    }
  });
</script>
