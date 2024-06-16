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

    <div class="col-12 col-sm-6">

     <div class="info-box mb-6 " onclick="window.location='?a=showItemMaster'">
                <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
				                  <div class="info-box-content">
								   <span class="info-box-text">1.Item Master</span>
								    </div>

              </div>
              </div>

    <div class="col-12 col-sm-6">

 <div class="info-box mb-6 " onclick="window.location='?a=showCategoryMasterNew'">
                <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
				                  <div class="info-box-content">
								   <span class="info-box-text">2 .Category Master</span>
								    </div>

              </div>
           </div>
          
    <div class="col-12 col-sm-6">

          <div class="info-box mb-6" onclick="window.location='?a=showRawMaterialsMaster'">
            <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
                	<div class="info-box-content">
                <span class="info-box-text">3. Raw Material Master </span>

                       </div>
            </div>
             </div>


            <div class="col-12 col-sm-6">

          <div class="info-box mb-6" onclick="window.location='?a=showCustomerMaster'">
            <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
                	<div class="info-box-content">
                <span class="info-box-text">4. Customer Master</span>

                       </div>
            </div>
             </div>


              

          
          
        




    
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
        
        
        
      
        
        	document.getElementById("divTitle").innerHTML="<p1 class='labelFonts'>Hisaab Cloud (Snacks Production)</p1>";

        	document.title +=" Snacks Production";
          
        </script>
        
        