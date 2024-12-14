<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="invoiceDetails" value="${requestScope['outputObject'].get('invoiceDetails')}" />
<br>

<div class="content-container">
    <div class="container">
        <div class="row">
            <c:forEach items="${invoiceDetails.listOfItems}" var="item">
                <div class="col-6 mb-2">
                    <div class="custom-button-container">
                        <button class="custom-button" 
                                onclick="updateQuantities(this, ${item.qty})">
                            <div class="button-content">
                                ${item.item_name}
                            </div>
                            <div class="badge-container">
                                <div class="badge left-badge">
                                    <span class="badge-label">Pending Qty</span><br>
                                    <span class="badge-value pending-qty">
                                        <fmt:formatNumber type="number" pattern="###" value="${item.qty}" />
                                    </span>
                                </div>
                                <div class="badge right-badge">
                                    <span class="badge-label">Loaded Qty</span><br>
                                    <span class="badge-value loaded-qty">0</span>
                                </div>
                            </div>
                        </button>
                        <button class="minus-button" onclick="decrementQuantities(this)" style="display: none;">
                            - 
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<div class="footer">
    <span>Total: <span id="total-loaded">0</span> / <span id="total-items">0</span></span>
</div>

<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
        display: flex;
        flex-direction: column;
    }

    .content-container {
        flex: 1;
        overflow-y: auto;
         /* Space for footer */
    }

    .container {
        padding-bottom: 20px; /* Space for footer */
    }

    .custom-button-container {
        position: relative;
    }

    .custom-button {
        width: 100%;
        height: 150px;
        font-size: 16px;
        position: relative;
        background-color: white;
        color: black;
        border: 1px solid #ccc;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        overflow: hidden;
        transition: background-color 0.3s ease;
    }

    .custom-button.green {
        background-color: #28a745; /* Bootstrap's success green */
        color: white;
    }

    .badge-container {
        position: absolute;
        top: 0;
        width: 100%;
        display: flex;
        justify-content: space-between;
        padding: 0 5px;
    }

    .badge {
        background-color: white;
        color: black;
        width: 60px;
        height: 40px;
        text-align: center;
        border: 1px solid #ccc;
        border-radius: 5px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-size: 12px;
    }

    .badge-label {
        font-weight: bold;
        font-size: 10px;
    }

    .badge-value {
        font-size: 14px;
    }

    .minus-button {
        position: absolute;
        bottom: 10px;
        left: 10px;
        width: 40px;
        height: 40px;
        background-color: #dc3545; /* Bootstrap's danger red */
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 20px;
        font-weight: bold;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .minus-button:hover {
        background-color: #bd2130; /* Darker red */
    }

    .footer {
        position: fixed;
        bottom: 0;
        left: 0;
        width: 100%;
        background-color: #f8f9fa; /* Bootstrap's light gray */
        color: black;
        text-align: center;
        font-size: 18px;
        padding: 10px 0;
        border-top: 1px solid #ccc;
        z-index: 10;
    }

    @media (max-width: 768px) {
        .custom-button {
            height: 120px;
        }

        .minus-button {
            width: 35px;
            height: 35px;
            font-size: 18px;
        }

        .footer {
            font-size: 16px;
        }
    }
</style>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        initializeFooter();
    });

    function initializeFooter() {
        let totalLoaded = 0; // All items start unloaded.
        let totalItems = 0;

        document.querySelectorAll('.pending-qty').forEach(el => {
            totalItems += parseFloat(el.textContent);
        });

        document.getElementById('total-loaded').textContent = totalLoaded.toFixed(0);
        document.getElementById('total-items').textContent = totalItems.toFixed(0);
    }

    function updateFooter() {
        let totalLoaded = 0;
        let totalItems = 0;

        document.querySelectorAll('.loaded-qty').forEach(el => {
            totalLoaded += parseFloat(el.textContent);
        });

        document.querySelectorAll('.pending-qty').forEach(el => {
            totalItems += parseFloat(el.textContent);
        });

        document.getElementById('total-loaded').textContent = totalLoaded.toFixed(0);
        document.getElementById('total-items').textContent = totalItems.toFixed(0);
    }

    function updateQuantities(button, maxQty) {
        const pendingQtyElement = button.querySelector('.pending-qty');
        const loadedQtyElement = button.querySelector('.loaded-qty');
        const minusButton = button.closest('.custom-button-container').querySelector('.minus-button');

        let pendingQty = parseFloat(pendingQtyElement.textContent);
        let loadedQty = parseFloat(loadedQtyElement.textContent);

        if (pendingQty > 0) {
            pendingQty -= 1;
            loadedQty += 1;

            pendingQtyElement.textContent = pendingQty.toFixed(0);
            loadedQtyElement.textContent = loadedQty.toFixed(0);

            if (pendingQty === 0) {
                button.classList.add('green');
            }

            if (loadedQty > 0) {
                minusButton.style.display = 'block';
            }
        } else {
            // Vibrate for 300ms when no more items are pending
            if ("vibrate" in navigator) {
                navigator.vibrate(300); // Vibrates for 300ms
            }

            //alert("No more items pending to load.");
        }

        updateFooter();
    }

    function decrementQuantities(minusButton) {
        const buttonContainer = minusButton.closest('.custom-button-container');
        const button = buttonContainer.querySelector('.custom-button');
        const pendingQtyElement = button.querySelector('.pending-qty');
        const loadedQtyElement = button.querySelector('.loaded-qty');

        let pendingQty = parseFloat(pendingQtyElement.textContent);
        let loadedQty = parseFloat(loadedQtyElement.textContent);

        if (loadedQty > 0) {
            loadedQty -= 1;
            pendingQty += 1;

            pendingQtyElement.textContent = pendingQty.toFixed(0);
            loadedQtyElement.textContent = loadedQty.toFixed(0);

            if (pendingQty > 0) {
                button.classList.remove('green');
            }

            if (loadedQty === 0) {
                minusButton.style.display = 'none';
            }
        } else {
            alert("No loaded items to unload.");
        }

        updateFooter();
    }
</script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="invoiceDetails" value="${requestScope['outputObject'].get('invoiceDetails')}" />
<br>

<div class="content-container">
    <div class="container">
        <div class="row">
            <c:forEach items="${invoiceDetails.listOfItems}" var="item">
                <div class="col-6 mb-2">
                    <div class="custom-button-container">
                        <button class="custom-button" 
                                onclick="updateQuantities(this, ${item.qty})">
                            <div class="button-content">
                                ${item.item_name}
                            </div>
                            <div class="badge-container">
                                <div class="badge left-badge">
                                    <span class="badge-label">Pending Qty</span><br>
                                    <span class="badge-value pending-qty">
                                        <fmt:formatNumber type="number" pattern="###" value="${item.qty}" />
                                    </span>
                                </div>
                                <div class="badge right-badge">
                                    <span class="badge-label">Loaded Qty</span><br>
                                    <span class="badge-value loaded-qty">0</span>
                                </div>
                            </div>
                        </button>
                        <button class="minus-button" onclick="decrementQuantities(this)" style="display: none;">
                            - 
                        </button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<div class="footer">
    <span>Total: <span id="total-loaded">0</span> / <span id="total-items">0</span></span>
</div>

<style>
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
        display: flex;
        flex-direction: column;
    }

    .content-container {
        flex: 1;
        overflow-y: auto;
         /* Space for footer */
    }

    .container {
        padding-bottom: 20px; /* Space for footer */
    }

    .custom-button-container {
        position: relative;
    }

    .custom-button {
        width: 100%;
        height: 150px;
        font-size: 16px;
        position: relative;
        background-color: white;
        color: black;
        border: 1px solid #ccc;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        overflow: hidden;
        transition: background-color 0.3s ease;
    }

    .custom-button.green {
        background-color: #28a745; /* Bootstrap's success green */
        color: white;
    }

    .badge-container {
        position: absolute;
        top: 0;
        width: 100%;
        display: flex;
        justify-content: space-between;
        padding: 0 5px;
    }

    .badge {
        background-color: white;
        color: black;
        width: 60px;
        height: 40px;
        text-align: center;
        border: 1px solid #ccc;
        border-radius: 5px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        font-size: 12px;
    }

    .badge-label {
        font-weight: bold;
        font-size: 10px;
    }

    .badge-value {
        font-size: 14px;
    }

    .minus-button {
        position: absolute;
        bottom: 10px;
        left: 10px;
        width: 40px;
        height: 40px;
        background-color: #dc3545; /* Bootstrap's danger red */
        color: white;
        border: none;
        border-radius: 8px;
        font-size: 20px;
        font-weight: bold;
        display: flex;
        justify-content: center;
        align-items: center;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }

    .minus-button:hover {
        background-color: #bd2130; /* Darker red */
    }

    .footer {
        position: fixed;
        bottom: 0;
        left: 0;
        width: 100%;
        background-color: #f8f9fa; /* Bootstrap's light gray */
        color: black;
        text-align: center;
        font-size: 18px;
        padding: 10px 0;
        border-top: 1px solid #ccc;
        z-index: 10;
    }

    @media (max-width: 768px) {
        .custom-button {
            height: 120px;
        }

        .minus-button {
            width: 35px;
            height: 35px;
            font-size: 18px;
        }

        .footer {
            font-size: 16px;
        }
    }
</style>

<script>
    document.addEventListener('DOMContentLoaded', () => {
        initializeFooter();
    });

    function initializeFooter() {
        let totalLoaded = 0; // All items start unloaded.
        let totalItems = 0;

        document.querySelectorAll('.pending-qty').forEach(el => {
            totalItems += parseFloat(el.textContent);
        });

        document.getElementById('total-loaded').textContent = totalLoaded.toFixed(0);
        document.getElementById('total-items').textContent = totalItems.toFixed(0);
    }

    function updateFooter() {
        let totalLoaded = 0;
        let totalItems = 0;

        document.querySelectorAll('.loaded-qty').forEach(el => {
            totalLoaded += parseFloat(el.textContent);
        });

        document.querySelectorAll('.pending-qty').forEach(el => {
            totalItems += parseFloat(el.textContent);
        });

        document.getElementById('total-loaded').textContent = totalLoaded.toFixed(0);
        document.getElementById('total-items').textContent = totalItems.toFixed(0);
    }

    function updateQuantities(button, maxQty) {
        const pendingQtyElement = button.querySelector('.pending-qty');
        const loadedQtyElement = button.querySelector('.loaded-qty');
        const minusButton = button.closest('.custom-button-container').querySelector('.minus-button');

        let pendingQty = parseFloat(pendingQtyElement.textContent);
        let loadedQty = parseFloat(loadedQtyElement.textContent);

        if (pendingQty > 0) {
            pendingQty -= 1;
            loadedQty += 1;

            pendingQtyElement.textContent = pendingQty.toFixed(0);
            loadedQtyElement.textContent = loadedQty.toFixed(0);

            if (pendingQty === 0) {
                button.classList.add('green');
            }

            if (loadedQty > 0) {
                minusButton.style.display = 'block';
            }
        } else {
            // Vibrate for 300ms when no more items are pending
            if ("vibrate" in navigator) {
                navigator.vibrate(300); // Vibrates for 300ms
            }

            //alert("No more items pending to load.");
        }

        updateFooter();
    }

    function decrementQuantities(minusButton) {
        const buttonContainer = minusButton.closest('.custom-button-container');
        const button = buttonContainer.querySelector('.custom-button');
        const pendingQtyElement = button.querySelector('.pending-qty');
        const loadedQtyElement = button.querySelector('.loaded-qty');

        let pendingQty = parseFloat(pendingQtyElement.textContent);
        let loadedQty = parseFloat(loadedQtyElement.textContent);

        if (loadedQty > 0) {
            loadedQty -= 1;
            pendingQty += 1;

            pendingQtyElement.textContent = pendingQty.toFixed(0);
            loadedQtyElement.textContent = loadedQty.toFixed(0);

            if (pendingQty > 0) {
                button.classList.remove('green');
            }

            if (loadedQty === 0) {
                minusButton.style.display = 'none';
            }
        } else {
            alert("No loaded items to unload.");
        }

        updateFooter();
    }
</script>
