<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="itemList" value='${requestScope["outputObject"].get("itemList")}' />

<div class="container" style="padding:20px;background-color:white;width: 100%;max-width: 100%;">

<form id="frm" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="row">
    <div class="col-sm-12">
        <!-- Select multiple-->
        <div class="form-group">
            <label>Current Item Order</label>
            <select style="height:550px" readonly multiple="" class="custom-select" id="drpcurrentroles">
                <c:forEach items="${itemList}" var="item"> 
                    <option id="${item.item_id}" value="${item.item_id}">${item.item_name}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="col-sm-12" align="center">
        <div class="form-group">
            <button class="btn btn-primary" type="button" onclick='moveitem(1)'>Move Up ></button>
            <button class="btn btn-primary" type="button" onclick='moveitem(0)'>< Move Down</button>
        </div>
    </div>


	<div class="col-sm-12" align="center">
        <div class="form-group">
            <button class="btn btn-success" type="button" onclick='saveItemOrdering()'>Save</button>
        </div>
    </div>

</div>

<datalist id="userList">
    <c:forEach items="${userList}" var="user">
        <option id="${user.user_id}">${user.username}~${user.name}</option>			    
    </c:forEach>
</datalist>
</form>

<script>
document.getElementById("divTitle").innerHTML = "Item Ordering";
document.title += " Item Ordering ";

function checkforMatchUser() {
    var searchString = document.getElementById("txtsearchuser").value;	
    var options1 = document.getElementById("userList").options;	
    var userId = 0;
    for (var x = 0; x < options1.length; x++) {
        if (searchString == options1[x].value) {
            userId = options1[x].id;
            break;
        }
    }
    if (userId != 0) {				
        document.getElementById("txtsearchuser").disabled = true;
        hdnSelectedUser.value = userId;
        getRoleDetailsForthisUser(userId);
    }		
}

function saveItemOrdering() {
    document.getElementById("closebutton").style.display = 'none';
    var xhttp = new XMLHttpRequest();

    var select = document.getElementById("drpcurrentroles");
    var reqItemString = "";

    // Loop through the options to gather itemId and order
    for (var i = 0; i < select.options.length; i++) {
        var itemId = select.options[i].value;
        var itemOrder = i + 1; // Adding 1 to i to start order from 1

        // Append itemId and order to the request string
        reqItemString += itemId + "~" + itemOrder + "|"; // Adjust format as needed
    }

    // Remove trailing comma if exists
    if (reqItemString.endsWith("|")) {
        reqItemString = reqItemString.slice(0, -1);
    }

    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            // Handle response if needed
            alert("Item ordering saved successfully.");
			window.location.reload();
        }
    };
    // Adjust the URL and parameter names as per your server-side implementation

 

	xhttp.open("POST","?a=saveItemOrdering", true);		  
				xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

		  xhttp.send("&itemString="+reqItemString);
		
}


function moveitem(moveType) {
    var select = document.getElementById("drpcurrentroles");
    var selectedIndex = select.selectedIndex;

    if (selectedIndex > -1) {
        if (moveType == 1 && selectedIndex > 0) { // Move up
            // Swap the selected item with the one above it
            var optionToMove = select.options[selectedIndex];
            var optionAbove = select.options[selectedIndex - 1];

            // Swap the options
            select.options[selectedIndex] = new Option(optionAbove.text, optionAbove.value, false, true);
            select.options[selectedIndex - 1] = new Option(optionToMove.text, optionToMove.value, false, false);

            // Keep the selection on the moved item
            select.selectedIndex = selectedIndex - 1;
        } else if (moveType == 0 && selectedIndex < select.options.length - 1) { // Move down
            // Swap the selected item with the one below it
            var optionToMove = select.options[selectedIndex];
            var optionBelow = select.options[selectedIndex + 1];

            // Swap the options
            select.options[selectedIndex] = new Option(optionBelow.text, optionBelow.value, false, false);
            select.options[selectedIndex + 1] = new Option(optionToMove.text, optionToMove.value, false, true);

            // Keep the selection on the moved item
            select.selectedIndex = selectedIndex + 1;
        }
    }
}

function resetUser() {
    window.location.reload();
}
</script>
