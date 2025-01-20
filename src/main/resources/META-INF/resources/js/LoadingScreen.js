let line1Completed = 0;

document.addEventListener("DOMContentLoaded", function () {
    prefillLoadedQuantities();
});

function prefillLoadedQuantities() {
    let loadingItemsJson = document.getElementById("hdnloadingItems").value;
    
    if (loadingItemsJson) {
        let loadingItems = JSON.parse(loadingItemsJson);

        loadingItems.forEach(item => {
            let buttonContainer = document.querySelector(`.custom-button-container input[value="${item.item_id}"]`)?.closest('.custom-button-container');

            if (buttonContainer) {
                let pendingQtyElement = buttonContainer.querySelector('.pending-qty');
                let loadedQtyElement = buttonContainer.querySelector('.loaded-qty');
                let minusButton = buttonContainer.querySelector('.minus-button');
                let currentLineQtyButton = buttonContainer.querySelector('.currentlineqty');

                let pendingQty = parseFloat(pendingQtyElement.textContent) - item.loaded_qty;
                let loadedQty = item.loaded_qty;

                pendingQtyElement.textContent = pendingQty >= 0 ? pendingQty : 0;
                loadedQtyElement.textContent = loadedQty;
                currentLineQtyButton.textContent = loadedQty;

                if (loadedQty > 0) {
                    minusButton.style.display = 'block';
                    currentLineQtyButton.style.display = 'block';
                }
            }
        });
        updateFooter();
    }
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
    const loadedQtyButton = button.closest('.custom-button-container').querySelector('.currentlineqty');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);

    pendingQty -= 1;
    pendingQtyElement.textContent = pendingQty.toFixed(0);
    loadedQty += 1;
    loadedQtyElement.textContent = loadedQty.toFixed(0);

    loadedQtyButton.textContent = loadedQty;
    if (loadedQty > 0) {
        minusButton.style.display = 'block';
        loadedQtyButton.style.display = 'block';
    }

    updateFooter();
}

function decrementQuantities(minusButton) {
    const buttonContainer = minusButton.closest('.custom-button-container');
    const button = buttonContainer.querySelector('.custom-button');
    const pendingQtyElement = button.querySelector('.pending-qty');
    const loadedQtyElement = button.querySelector('.loaded-qty');
    const loadedQtyButton = buttonContainer.querySelector('.currentlineqty');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);

    if (loadedQty > 0) {
        loadedQty -= 1;
        pendingQty += 1;

        pendingQtyElement.textContent = pendingQty.toFixed(0);
        loadedQtyElement.textContent = loadedQty.toFixed(0);
        loadedQtyButton.textContent = loadedQty;

        if (loadedQty === 0) {
            minusButton.style.display = 'none';
            loadedQtyButton.style.display = 'none';
        }
    } else {
        alert("No loaded items to unload.");
    }

    updateFooter();
}

function completeLine() {
    let itemsData = [];
    let lineNo = document.getElementById("hdnlineno").value;
    let orderId = document.getElementById("hdnorderid").value;
    let loadingId = document.getElementById("hdnloadingid").value;

    document.querySelectorAll('.custom-button-container').forEach(container => {
        let button = container.querySelector('.custom-button');
        let itemId = button.querySelector('input[type="hidden"]').value;
        let pendingQty = parseFloat(button.querySelector('.pending-qty').textContent);
        let loadedQty = parseFloat(button.querySelector('.loaded-qty').textContent);
        let currentLineQty = parseFloat(container.querySelector('.currentlineqty').textContent);

        itemsData.push({
            loading_id: loadingId,
            line_no: lineNo,
            order_id: orderId,
            item_id: itemId,
            pending_qty: pendingQty,
            loaded_qty: loadedQty,
            current_line_qty: currentLineQty
        });
    });

    let xhr = new XMLHttpRequest();
    xhr.open("POST", "?a=completeLine", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            alert(xhr.responseText);
            if (xhr.status === 200) {
                window.location = `?a=showLoadingScreen&loading_id=${loadingId}&order_id=${orderId}&line_no=${parseInt(lineNo) + 1}`;
            } else {
                alert("Error completing the line. Please try again.");
            }
        }
    };
    
    xhr.send(JSON.stringify({ items: itemsData }));
}
