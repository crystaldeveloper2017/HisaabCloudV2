<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="invoiceDetails" value="${requestScope['outputObject'].get('invoiceDetails')}" />
<c:set var="loadingDetails" value="${requestScope['outputObject'].get('loadingDetails')}" />
<c:set var="orderDetails" value="${requestScope['outputObject'].get('orderDetails')}" />
<c:set var="loadingItemDetails" value="${requestScope['outputObject'].get('loadingItemDetails')}" />





<link rel="stylesheet" href="css/LoadingScreen.css" />


<div class="content-container">
    <div class="container">
        <div class="row">
            <c:forEach items="${invoiceDetails.listOfItems}" var="item">
                <div class="col-6 mb-2" style="padding-right:0px;padding-left:0px;">
                    <div class="custom-button-container">
                        <button class="custom-button" 
                                onclick="updateQuantities(this, ${item.qty})">
                            <div class="button-content">
                                <fmt:formatNumber type="number" pattern="###0" value="${item.qty}" /> - ${item.item_name} <input type="hidden" value="${item.item_id}"> 
                            </div>
                            <div class="badge-container">
                                <div class="badge left-badge">
                                    <span class="badge-label">Pending Qty</span>
                                    <span class="badge-value pending-qty">
                                        <fmt:formatNumber type="number" pattern="###0" value="${item.qty}" />
                                    </span>
                                </div>
                                <div class="badge right-badge">
                                    <span class="badge-label">Loaded Qty</span>
                                    <span class="badge-value loaded-qty">0</span>
                                </div>
                            </div>
                        </button>
                        <button class="minus-button" onclick="decrementQuantities(this)" style="display: none;">
                            - 
                        </button>

                        <button class="currentlineqty"  style="display: none;">
                            0
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<div class="footer">
    <div class="footer-left">
        <span>Total: <span id="total-loaded">0</span> / <span id="total-items">0</span></span>
    </div>
    <div class="footer-divider"></div>
    
    <!-- Arrow button placed between total quantities and complete line button -->
    <div class="footer-middle">
        <button id="top-arrow-button" class="top-arrow-button" onclick="showmodalpopup()">
            <i class="fas fa-arrow-up"></i> <!-- Font Awesome up arrow icon -->
        </button>
    </div>
    
    <div class="footer-right">
        <button id="complete-line" class="complete-loading-button" onclick="completeLine()">
            Complete Line ${param.line_no}
        </button>
    </div>
</div>


<input type="hidden" id="hdnlineno" value="${param.line_no}" />
<input type="hidden" id="hdnorderid" value="${param.order_id}" />
<input type="hidden" id="hdnloadingid" value="${param.loading_id}" />



<script>
let orderId = "${param.order_id}";
let loadingId = "${param.loading_id}";

document.getElementById("divTitle").innerHTML="${loadingDetails.vehicle_number} ${orderDetails.customercityname} Line No : ${param.line_no}";

</script>

<script src="js/LoadingScreen.js"></script>
