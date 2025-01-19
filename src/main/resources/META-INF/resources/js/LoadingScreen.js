let line1Completed = 0;

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

    if (pendingQty === 0) {
        button.classList.remove('pink', 'white');
        button.classList.add('green');
    } else if (pendingQty < 0) {
        button.classList.remove('green', 'white');
        button.classList.add('pink');
    } else {
        button.classList.remove('green', 'pink');
        button.classList.add('white');
    }

    if (pendingQty >= 0) {
        loadedQty += 1;
        loadedQtyElement.textContent = loadedQty.toFixed(0);
    } else {
        if ("vibrate" in navigator) {
            navigator.vibrate(300);
        }
        loadedQty += 1;
        loadedQtyElement.textContent = loadedQty.toFixed(0);
    }

    loadedQtyButton.textContent = loadedQty > 0 ? loadedQty : '0';
    if (loadedQty > 0) {
        minusButton.style.display = 'block';
        loadedQtyButton.style.display = 'block';
    } else {
        minusButton.style.display = 'none';
        loadedQtyButton.style.display = 'none';
    }

    updateFooter();
}

function decrementQuantities(minusButton) {
    const buttonContainer = minusButton.closest('.custom-button-container');
    const button = buttonContainer.querySelector('.custom-button');
    const pendingQtyElement = button.querySelector('.pending-qty');
    const loadedQtyElement = button.querySelector('.loaded-qty');
    const loadedQtyButton = button.closest('.custom-button-container').querySelector('.currentlineqty');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);

    if (loadedQty > 0) {
        loadedQty -= 1;
        pendingQty += 1;

        pendingQtyElement.textContent = pendingQty.toFixed(0);
        loadedQtyElement.textContent = loadedQty.toFixed(0);

        loadedQtyButton.textContent = loadedQty.toFixed(0);

        if (loadedQty === 0) {
            minusButton.style.display = 'none';
            loadedQtyButton.style.display = 'none';
        }

        if (pendingQty === 0) {
            button.classList.remove('pink', 'white');
            button.classList.add('green');
        } else if (pendingQty < 0) {
            button.classList.remove('green', 'white');
            button.classList.add('pink');
        } else {
            button.classList.remove('green', 'pink');
            button.classList.add('white');
        }

        updateFooter();
    } else {
        alert("No loaded items to unload.");
    }
}

function completeLoading() {
    alert("Loading Completed!");
}

function completeLine() {
    let currentLineLoadedQty = 0;
    let itemsData = [];

    document.querySelectorAll('.custom-button-container').forEach(container => {
        let button = container.querySelector('.custom-button');
        let itemId = button.querySelector('input[type="hidden"]').value;
        let pendingQty = parseFloat(button.querySelector('.pending-qty').textContent);
        let loadedQty = parseFloat(button.querySelector('.loaded-qty').textContent);
        let orderedQty = parseFloat(button.querySelector('.badge .badge-value').textContent);
        let currentLineQty = parseFloat(container.querySelector('.currentlineqty').textContent);

        currentLineLoadedQty += currentLineQty;

        itemsData.push({
            item_id: itemId,
            pending_qty: pendingQty,
            loaded_qty: loadedQty,
            ordered_qty: orderedQty,
            current_line_qty: currentLineQty,
            order_id: orderId,
            loading_id: loadingId
        });
    });

    console.log("Current Line Loaded Qty:", currentLineLoadedQty);
    console.log("Items Data:", itemsData);

    fetch('/api/complete-line', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            line_no: "${param.line_no}",
            total_loaded_qty: currentLineLoadedQty,
            items: itemsData,
        }),
    })
        .then(response => response.json())
        .then(data => {
            alert("Line Completed Successfully!");
        })
        .catch(error => {
            console.error("Error completing line:", error);
            alert("Error completing the line. Please try again.");
        });

    alert(`Line Completed! Total Loaded Qty: ${currentLineLoadedQty}`);
}
