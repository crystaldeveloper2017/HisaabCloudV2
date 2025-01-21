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
                let buttonBox = buttonContainer.querySelector('.custom-button');

                let pendingQty = parseFloat(pendingQtyElement.textContent) - item.loaded_qty;
                let loadedQty = item.loaded_qty;

                pendingQtyElement.textContent = pendingQty >= 0 ? pendingQty : 0;
                loadedQtyElement.textContent = loadedQty;
                currentLineQtyButton.textContent = '0'; // Reset for new line session

                

                updateButtonColor(buttonBox, pendingQty);
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
    const currentLineQtyButton = button.closest('.custom-button-container').querySelector('.currentlineqty');
    const buttonContainer = button.closest('.custom-button');

    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);
    let currentLineQty = parseFloat(currentLineQtyButton.textContent);

    pendingQty -= 1;
    loadedQty += 1;
    currentLineQty += 1; // Increment from 0, independent of loadedQty

    pendingQtyElement.textContent = pendingQty.toFixed(0);
    loadedQtyElement.textContent = loadedQty.toFixed(0);
    currentLineQtyButton.textContent = currentLineQty.toFixed(0);

    if (loadedQty > 0) {
        minusButton.style.display = 'block';
        currentLineQtyButton.style.display = 'block';
    }

    updateButtonColor(buttonContainer, pendingQty);
    updateFooter();
}

function decrementQuantities(minusButton) {
    const buttonContainer = minusButton.closest('.custom-button-container');
    const button = buttonContainer.querySelector('.custom-button');
    const pendingQtyElement = button.querySelector('.pending-qty');
    const loadedQtyElement = button.querySelector('.loaded-qty');
    const currentLineQtyButton = buttonContainer.querySelector('.currentlineqty');
    
    let pendingQty = parseFloat(pendingQtyElement.textContent);
    let loadedQty = parseFloat(loadedQtyElement.textContent);
    let currentLineQty = parseFloat(currentLineQtyButton.textContent);

    if (loadedQty > 0 && currentLineQty > 0) {
        loadedQty -= 1;
        pendingQty += 1;
        currentLineQty -= 1;

        pendingQtyElement.textContent = pendingQty.toFixed(0);
        loadedQtyElement.textContent = loadedQty.toFixed(0);
        currentLineQtyButton.textContent = currentLineQty.toFixed(0);

        if (currentLineQty === 0) {
            minusButton.style.display = 'none';
            currentLineQtyButton.style.display = 'none';
        }

        updateButtonColor(button, pendingQty);
    } else {
        alert("No loaded items to unload.");
    }

    updateFooter();
}

function updateButtonColor(button, pendingQty) {
    if (pendingQty === 0) {
        button.classList.remove('white', 'pink');
        button.classList.add('green');
    } else if (pendingQty < 0) {
        button.classList.remove('white', 'green');
        button.classList.add('pink');
    } else {
        button.classList.remove('green', 'pink');
        button.classList.add('white');
    }
}


function showModalPopup() {
    // Create modal overlay
    const modalOverlay = document.createElement('div');
    modalOverlay.id = 'modalOverlay';
    modalOverlay.style.position = 'fixed';
    modalOverlay.style.top = '0';
    modalOverlay.style.left = '0';
    modalOverlay.style.width = '100%';
    modalOverlay.style.height = '100%';
    modalOverlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    modalOverlay.style.display = 'flex';
    modalOverlay.style.justifyContent = 'center';
    modalOverlay.style.alignItems = 'center';
    modalOverlay.style.zIndex = '1000';

    // Create modal container
    const modalContainer = document.createElement('div');
    modalContainer.id = 'modalContainer';
    modalContainer.style.backgroundColor = '#fff';
    modalContainer.style.borderRadius = '10px';
    modalContainer.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.2)';
    modalContainer.style.padding = '20px';
    modalContainer.style.width = '90%';
    modalContainer.style.maxWidth = '400px';
    modalContainer.style.textAlign = 'center';

    // Add modal text
    const modalText = document.createElement('p');
    modalText.textContent = 'Please choose an option below:';
    modalText.style.marginBottom = '20px';
    modalText.style.fontSize = '16px';
    modalText.style.color = '#333';

    // Create buttons
    const button1 = document.createElement('button');
    button1.textContent = 'Complete Loading';
    button1.className = 'modal-button';
    button1.onclick = () => CompleteLoading();

    const button2 = document.createElement('button');
    button2.textContent = 'Choose Another Order';
    button2.className = 'modal-button';
    button2.onclick = () => chooseAnotherOrderForLoading();

    const button3 = document.createElement('button');
    button3.textContent = 'Close';
    button3.className = 'modal-button';
    button3.onclick = () => closeModal();

    // Append elements
    modalContainer.appendChild(modalText);
    modalContainer.appendChild(button1);
    modalContainer.appendChild(button2);
    modalContainer.appendChild(button3);
    modalOverlay.appendChild(modalContainer);
    document.body.appendChild(modalOverlay);
}

function CompleteLoading()
{
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "?a=completeLoading&loading_id="+hdnloadingid.value, true);

    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(xhr.responseText);
        }
    };
    xhr.send();
}

function chooseAnotherOrderForLoading()
{
    alert('Chose another order Logic In progress');
   //window.location="show"
}

// Handle button actions
function handleModalAction(option) {
    alert(`You selected: ${option}`);
    closeModal();
}

// Close modal
function closeModal() {
    const modalOverlay = document.getElementById('modalOverlay');
    if (modalOverlay) {
        modalOverlay.remove();
    }
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
