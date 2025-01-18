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
    const loadedQtyButton = button.closest('.custom-button-container').querySelector('.loaded-qty-button');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);

    pendingQty -= 1;
    pendingQtyElement.textContent = pendingQty.toFixed(0);

    // Update button class
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

    // Update loaded quantity
    if (pendingQty >= 0) {
        loadedQty += 1;
        loadedQtyElement.textContent = loadedQty.toFixed(0);
    } else {
        // Allow adding more even if pendingQty is 0
        if ("vibrate" in navigator) {
            navigator.vibrate(300);
        }
        loadedQty += 1;
        loadedQtyElement.textContent = loadedQty.toFixed(0);
    }

    // Show the loaded qty button when it's greater than 0
    loadedQtyButton.textContent = loadedQty > 0 ? loadedQty : '0';
    if (loadedQty > 0) {
        minusButton.style.display = 'block';
        loadedQtyButton.style.display='block';
    } else {
        minusButton.style.display = 'none';
        loadedQtyButton.style.display='none';
    }

    updateFooter();
}

function decrementQuantities(minusButton) {
    const buttonContainer = minusButton.closest('.custom-button-container');
    const button = buttonContainer.querySelector('.custom-button');
    const pendingQtyElement = button.querySelector('.pending-qty');
    const loadedQtyElement = button.querySelector('.loaded-qty');
    const loadedQtyButton = button.closest('.custom-button-container').querySelector('.loaded-qty-button');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);

    // Ensure there's at least one loaded quantity to decrement
    if (loadedQty > 0) {
        // Decrement loaded quantity and increment pending quantity
        loadedQty -= 1;
        pendingQty += 1;

        // Update the quantities in the UI
        pendingQtyElement.textContent = pendingQty.toFixed(0);
        loadedQtyElement.textContent = loadedQty.toFixed(0);

        // Show the loaded qty button with the updated value
        loadedQtyButton.textContent = loadedQty.toFixed(0);

        // If no more loaded items, hide the minus button
        if (loadedQty === 0) {
            minusButton.style.display = 'none';
            loadedQtyButton.style.display = 'none';
        }
        
        // Update button colors and class based on the updated pendingQty
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

        // Update the footer
        updateFooter();
    } else {
        alert("No loaded items to unload.");
    }
}


function completeLoading() {
    // Placeholder for any additional logic when completing loading
    alert("Loading Completed!");
}

function completeLine() {
    // Create an array to store item details
    let itemsData = [];

    // Loop through each button that represents an item
    document.querySelectorAll('.custom-button').forEach(button => {
        // Get the item ID from the hidden input field within the button
        let itemId = button.querySelector('input[type="hidden"]').value;
        let pendingQty = parseFloat(button.querySelector('.pending-qty').textContent);
        let loadedQty = parseFloat(button.querySelector('.loaded-qty').textContent);
        let orderedQty = parseFloat(button.querySelector('.badge-container .badge .badge-value').textContent); // Adjust if needed

        // Store the item data in an array
        itemsData.push({
            item_id: itemId,
            pending_qty: pendingQty,
            loaded_qty: loadedQty,
            ordered_qty: orderedQty,
            order_id: orderId,      // Use the pre-defined orderId
            laoding_id: loadingId   // Use the pre-defined loadingId
        });
    });

    // Output the item details to the console for debugging (can be replaced with actual processing logic)
    console.log("Completed Line. Items:", itemsData);

    // Example of sending the data to the backend (if needed)
    // fetch('/api/complete-line', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json',
    //     },
    //     body: JSON.stringify({ items: itemsData })
    // }).then(response => response.json())
    //   .then(data => {
    //     alert("Line Completed Successfully!");
    //   })
    //   .catch(error => console.error("Error completing line:", error));

    // Placeholder alert for now
    alert("Line Completed!");
}
