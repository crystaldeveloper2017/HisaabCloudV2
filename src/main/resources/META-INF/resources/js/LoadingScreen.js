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

    // Retrieve values from hidden fields
    let lineNo = document.getElementById("hdnlineno").value;
    let orderId = document.getElementById("hdnorderid").value;
    let loadingId = document.getElementById("hdnloadingid").value;


    document.querySelectorAll('.custom-button-container').forEach(container => {
        let button = container.querySelector('.custom-button');
        let itemId = button.querySelector('input[type="hidden"]').value;
        let pendingQty = parseFloat(button.querySelector('.pending-qty').textContent);
        let loadedQty = parseFloat(button.querySelector('.loaded-qty').textContent);
        let currentLineQty = parseFloat(container.querySelector('.currentlineqty').textContent);

        currentLineLoadedQty += currentLineQty;

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

    console.log("Items Data:", itemsData);

    // Create XMLHttpRequest
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "?a=completeLine", true);
    xhr.setRequestHeader("Content-Type", "application/json");

    // Handle response
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {  // Request is complete
            if (xhr.status === 200) {  // Successful response
                alert(xhr.responseText);  // Show server response in alert
                var lineNoint=Number(lineNo);
                lineNoint++;
                window.location="?a=showLoadingScreen&loading_id="+loadingId+"&order_id="+orderId+"&line_no="+lineNoint;
            } else {
                alert("Error completing the line. Please try again.");
            }
        }
    };

    // Send data
    xhr.send(JSON.stringify({        
        items: itemsData
    }));
}
