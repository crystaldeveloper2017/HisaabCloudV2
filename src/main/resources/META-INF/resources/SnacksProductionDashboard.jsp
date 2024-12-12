<style>
	@media screen and (min-width: 601px) {
  .labelFonts {
    font-size: 20px;
    font-style: italic;color: darkblue;font-weight: 800;
  }
}

/* If the screen size is 600px wide or less, set the font-size of <div> to 30px */
@media screen and (max-width: 600px) {
  .labelFonts {
    font-size: 15px;
    font-style: italic;color: darkblue;font-weight: 800;
  }
}
</style>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="HomePageContent" value='${requestScope["outputObject"]}' />

<br>




<div class="row">


<div class="col-md-12">

<div class="card card-widget widget-user-2 shadow-sm">


<div class="card-footer p-0">
<ul class="nav flex-column">
<li class="nav-item">
<a href="#" class="nav-link" onclick="window.location='?a=showGenerateInvoice'">
New Order <span class="float-right badge bg-primary"></span>
</a>
</li>

<li class="nav-item">
<a href="#" class="nav-link" onclick="window.location='?a=showPendingRegister'">
Pending Orders <span class="float-right badge bg-danger">${HomePageContent.pendingCount}</span>
</a>
</li>

<c:if test="${adminFlag eq true}">

<li class="nav-item" onclick="window.location='?a=showPlanningRegister'">
<a href="#" class="nav-link">
Todays Planning <span class="float-right badge bg-success">${HomePageContent.todaysPlanningCount}</span>
</a>
</li>
<li class="nav-item" onclick="window.location='?a=showCompletedOrders'">
<a href="#" class="nav-link">
Completed Orders <span class="float-right badge bg-success">${HomePageContent.completedCount}</span>
</a>
</li>
<li class="nav-item">
<a href="#" class="nav-link" onclick="window.location='?a=showTodaysStock'">
Currecnt Ready Stock (${HomePageContent.todaysStock})<span class="float-right badge bg-danger"></span>
</a>
</li>

</c:if>

</ul>
</div>  
</div>

</div>


    

    <div class="col-12 col-sm-6">


         
          
    


            


              

          
          
        




    
          <div class="clearfix hidden-md-up"></div>

          <!-- /.col -->
          
          
          
          <!-- /.col -->
        </div>


  
 
 <br>
 

 

		
		
	
		
    
    
    <!-- below is the link to find more icons -->
<!-- https://www.tutorialspoint.com/ionic/ionic_icons.htm -->


<script type="text/javascript">
        $( function() 
        		{
            
            
                       
            
          } );
        
        
        function reloadData()
        {
        	window.location="?a=showHomePage&fromDate="+txtfromdate.value+"&toDate="+txttodate.value;
        }   
        
        
        
         
        	
        	$(document).ready(function () {
        		
        		
        		var validDays=Number("${userdetails.validDays}");        
                if(validDays<=7)
                	{
                		alert("Application Subscription will Expire in "+validDays+" Days");
                		alert("All Configuration and Transaction May Be Lost In case not Renewed.");
                	}
                if(validDays<=0)
                	{
                	   alert("Please Renew Subscription or Contact Administrator");
                	   document.getElementById("refLogout").click();
                	}
        		
        	  });
        
        
        
      
        
        	document.getElementById("divTitle").innerHTML="<p1 class='labelFonts'>";

        	document.title +=" Snacks Production";
          
        </script>
        
        