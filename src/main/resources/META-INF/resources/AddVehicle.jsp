<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="listOfItems" value='${requestScope["outputObject"].get("listOfItems")}' />
<c:set var="lstCustomerMaster" value='${requestScope["outputObject"].get("lstCustomerMaster")}' />
<c:set var="vehicleDetails" value='${requestScope["outputObject"].get("vehicleDetails")}' />

<script>
function addVehicle() {
    if (vehicleName.value == "") {
        toastr["error"]("Please enter Vehicle Name");
        toastr.options = {"closeButton": false, "debug": false, "newestOnTop": false, "progressBar": false,
            "positionClass": "toast-top-right", "preventDuplicates": false, "onclick": null, "showDuration": "2000",
            "hideDuration": "500", "timeOut": "2000", "extendedTimeOut": "2000", "showEasing": "swing", "hideEasing": "linear",
            "showMethod": "fadeIn", "hideMethod": "fadeOut"};
        btnsave.disabled = false;
        vehicleName.focus();
        return;
    }

    if (vehicleNumber.value == "") {
        toastr["error"]("Please enter Vehicle Number");
        toastr.options = {"closeButton": false, "debug": false, "newestOnTop": false, "progressBar": false,
            "positionClass": "toast-top-right", "preventDuplicates": false, "onclick": null, "showDuration": "1000",
            "hideDuration": "500", "timeOut": "1000", "extendedTimeOut": "1000", "showEasing": "swing", "hideEasing": "linear",
            "showMethod": "fadeIn", "hideMethod": "fadeOut"};
        btnsave.disabled = false;
        vehicleNumber.focus();
        return;
    }

    document.getElementById("frm").submit();
}
</script>

<br>

<div class="container" style="padding:20px;background-color:white">

<form id="frm" action="?a=addVehicle" method="post" accept-charset="UTF-8">
    <input type="hidden" name="app_id" value="${userdetails.app_id}">
    <input type="hidden" name="user_id" value="${userdetails.user_id}">
    <input type="hidden" name="callerUrl" id="callerUrl" value="">

    <c:if test="${userdetails.app_type == 'SnacksProduction'}">
        <div class="col-sm-12">
            <div class="form-group">
                <label for="email">Vehicle Name *</label>
                <input type="text" class="form-control" id="vehicleName" value="${vehicleDetails.vehicle_name}"  placeholder="Vehicle Name" name="vehicleName">
                <input type="hidden" name="hdnVehicleId" value="${vehicleDetails.vehicle_id}" id="hdnVehicleId">
            </div>
        </div>

        <div class="col-sm-12">
            <div class="form-group">
                <label for="email">Vehicle Number *</label>
                <input type="text" class="form-control" id="vehicleNumber" value="${vehicleDetails.vehicle_number}"  placeholder="Vehicle Number" name="vehicleNumber">
            </div>
        </div>
    </c:if>

    <c:if test="${userdetails.app_type != 'SnacksProduction'}">
        <div class="form-group">
            <label for="email">Customer Name * </label>     
            <select class="form-control" name="customerName" id="customerName">
                <c:forEach items="${lstCustomerMaster}" var="customer">
                    <option value="${customer.customerId}">${customer.customerName}</option>             
                </c:forEach>
            </select>     
        </div>

        <div class="col-sm-12">
            <div class="form-group">
                <label for="email">Vehicle Name *</label>
                <input type="text" class="form-control" id="vehicleName" value="${vehicleDetails.vehicle_name}"  placeholder="Vehicle Name" name="vehicleName">
                <input type="hidden" name="hdnVehicleId" value="${vehicleDetails.vehicle_id}" id="hdnVehicleId">
            </div>
        </div>

        <div class="col-sm-12">
            <div class="form-group">
                <label for="email">Vehicle Number *</label>
                <input type="text" class="form-control" id="vehicleNumber" value="${vehicleDetails.vehicle_number}"  placeholder="Vehicle Number" name="vehicleNumber">
            </div>
        </div>

        <div class="col-sm-12">
            <div class="form-group">
                <label for="email">Preferred Fuel Type *</label>
                <select class="form-control" name="drpfueltype" id="drpfueltype">
                    <c:forEach items="${listOfItems}" var="item">
                        <option value="${item.item_name}">${item.item_name}</option>             
                    </c:forEach>
                </select>
            </div>
        </div>
    </c:if>

    <div class="col-sm-12">
        <div class="form-group" align="center">
            <button class="btn btn-success" type="button" id="btnsave" onclick='addVehicle()'>Save</button>    
            <button class="btn btn-danger" type="reset" onclick='window.location="?a=showVehicleMaster"'>Cancel</button>
        </div>
    </div>

</div>
</form>

<script>
    if ('${vehicleDetails.vehicle_id}' != '') {
        customerName.value = '${vehicleDetails.customer_id}';
    }

    <c:if test="${vehicleDetails.vehicle_id eq null}">
        document.getElementById("divTitle").innerHTML = "Add Vehicle";
        document.title += " Add Vehicle ";
    </c:if>

    <c:if test="${vehicleDetails.vehicle_id ne null}">
        document.getElementById("divTitle").innerHTML = "Update Vehicle";
        document.title += " Update Vehicle ";
    </c:if>
</script>
