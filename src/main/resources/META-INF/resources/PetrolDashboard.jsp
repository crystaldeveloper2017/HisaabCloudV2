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
<c:set var="nozzleCount" value='${requestScope["outputObject"].get("nozzleDetails")}' />
<c:set var="dispenserCount" value='${requestScope["outputObject"].get("dispenserDetails")}' />
<c:set var="customerCount" value='${requestScope["outputObject"].get("customerDetails")}' />
<c:set var="invoiceCount" value='${requestScope["outputObject"].get("invoiceDetails")}' />
<c:set var="vehicleCount" value='${requestScope["outputObject"].get("vehicleDetails")}' />
<br>




<div class="row">

    <div class="col-12 col-sm-6">

     <div class="info-box mb-3 " onclick="window.location='?a=showCheckInScreen'">
                <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
				                  <div class="info-box-content">
								   <span class="info-box-text">1.Nozzle Check In / Check out</span>
								    </div>

              </div>
              </div>

    <div class="col-12 col-sm-6">

 <div class="info-box mb-3 " onclick="window.location='?a=showCollectPaymentSupervisor&collection_mode=Cash'">
                <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
				                  <div class="info-box-content">
								   <span class="info-box-text">2 .Collect Drops from Attendant</span>
								    </div>

              </div>
           </div>
          
    <div class="col-12 col-sm-6">

          <div class="info-box mb-3" onclick="window.location='?a=showGenerateInvoice&payment_type=Pending'">
            <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
                	<div class="info-box-content">
                <span class="info-box-text">3. Generate Credit Sales Invoice</span>

                       </div>
            </div>
             </div>


            <div class="col-12 col-sm-6">

          <div class="info-box mb-3" onclick="window.location='?a=showGenerateInvoice&payment_type=Paid&payment_mode=Card'">
            <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
                	<div class="info-box-content">
                <span class="info-box-text">4. Generate Card Sales Invoice</span>

                       </div>
            </div>
             </div>


              <div class="col-12 col-sm-6">

           <div class="info-box mb-3" onclick="window.location='?a=showGenerateInvoice'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
              
              <div class="info-box-content">
                <span class="info-box-text">5. Lube Sales</span>
                
              </div>
            </div>
          </div>


              <div class="col-12 col-sm-6">

          <div class="info-box mb-3" onclick="window.location='?a=showAddTestFuel'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">6. Add Test Fuel</span>
                
              </div>
            </div>

          </div>


        
         
               <div class="col-12 col-sm-6">

           <div class="info-box mb-3" onclick="window.location='?a=showCollectPayment'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>
              
              <div class="info-box-content">
                <span class="info-box-text">7. Collect Payment</span>
                
              </div>
            </div>
          </div>



                <div class="col-12 col-sm-6">
            <div class="info-box mb-3" onclick="window.location='?a=showSubmitCashtoVault'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">8. Submit Cash To Vault</span>
                
              </div>
            </div>
          </div>


               <div class="col-12 col-sm-6">
           <div class="info-box mb-3" onclick="window.location='?a=showCollectPaymentSupervisor&collection_mode=Paytm'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">9. Collect Paytm From Attendant </span>
                
              </div>
            </div>
          </div>
          
             
 
              <div class="col-12 col-sm-6">

         <div class="info-box mb-3 " onclick="window.location='?a=showNozzleRegister'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">10. Nozzle Register </span>
                
              </div>
            </div>
          </div>


            <div class="col-12 col-sm-6">

         <div class="info-box mb-3 " onclick="window.location='?a=showScanVehicleQR'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-gas-pump"></i></span>

              <div class="info-box-content">
                <span class="info-box-text">11. Scan Vehicle QR </span>
                
              </div>
            </div>
          </div>


        
<div class="col-12 col-sm-6">
      <div class="info-box mb-3 " onclick="window.location='?a=showStockRegisterDirect'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-pencil-square-o"></i></span>
				  <div class="info-box-content">
								   <span class="info-box-text">12.Stock Register (Direct)</span>
								    </div>

              </div>
              </div>  

  <div class="col-12 col-sm-6">
      <div class="info-box mb-3 " onclick="window.location='?a=showStockStatusDirect'">
              <span class="info-box-icon bg-info elevation-1"><i class="fas fa-pencil-square-o"></i></span>
				  <div class="info-box-content">
								   <span class="info-box-text">13.Stock Status (Direct)</span>
								    </div>

              </div>
              </div>  



    
          <div class="clearfix hidden-md-up"></div>

          <!-- /.col -->
          
          
          
          <!-- /.col -->
        </div>


  
 
 <br>
 

<c:if test="${adminFlag eq true}">
	
	
<div class="row">
	
	
		<div class="col-lg-3 col-6" onclick="window.location='?a=showNozzleMaster'">
            <div class="small-box bg-success">
              <div class="inner">
              
              


                <h3>${nozzleCount}</h3>

                <p>Total Nozzles</p>
              </div>
              <div class="icon">
                <i class="ion ion-bag"></i>
              </div>
              <a href="#" onclick="window.location='?a=showNozzleMaster'" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-3 col-6" onclick="window.location='?a=showDispenserMaster'">
            <!-- small box -->
            <div class="small-box bg-warning">
              <div class="inner">
                <h3>${dispenserCount}</h3>

                <p>Total Dispensers</p>
              </div>
              <div class="icon">
                <i class="ion ion-stats-bars"></i>
              </div>
              <a href="#" onclick="window.location='?a=showDispenserMaster'" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
          <!-- ./col -->
          <div class="col-lg-3 col-6" onclick="window.location='?a=showCustomerMaster'">
            <!-- small box -->
            <div class="small-box bg-danger">
              <div class="inner">
                <h3>${customerCount}</h3>

                <p>Total Customers</p>
              </div>
              <div class="icon">
                <i class="ion ion-person-add"></i>
              </div>
              <a href="#"  onclick="window.location='?a=showCustomerMaster'" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
          
          <div class="col-lg-3 col-6" onclick="window.location='?a=generateDailyInvoiceReport'">
            <!-- small box -->
            <div class="small-box bg-info">
              <div class="inner">
                <h3>${invoiceCount}</h3>
                <p>Total Sales Invoices</p>
              </div>
              <div class="icon">
                <i class="ion ion-document"></i>
                
              </div>
              <a href="#"  onclick="window.location='?a=generateDailyInvoiceReport'" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
          
          <div class="col-lg-3 col-6" onclick="window.location='?a=showVehicleMaster'">
            <!-- small box -->
            <div class="small-box bg-success">
              <div class="inner">
                <h3>${vehicleCount}</h3>
                <p>Total Vehicles</p>
              </div>
              <div class="icon">
                <i class="ion ion-document"></i>
                
              </div>
              <a href="#"  onclick="window.location='?a=showVehicleMaster'" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

</div>
	
</c:if> 
 

		
		
	
		
    
    
    <!-- below is the link to find more icons -->
<!-- https://www.tutorialspoint.com/ionic/ionic_icons.htm -->


<script type="text/javascript">
        $( function() 
        		{
            $( "#txtfromdate" ).datepicker({ dateFormat: 'dd/mm/yy' });
            $( "#txttodate" ).datepicker({ dateFormat: 'dd/mm/yy' });
            
                       
            
          } );
        
        
        function reloadData()
        {
        	window.location="?a=showHomePage&fromDate="+txtfromdate.value+"&toDate="+txttodate.value;
        }   
        
        if('${param.fromDate}'!='')
        	{
		        txtfromdate.value='${param.fromDate}';
		        txttodate.value='${param.toDate}';
        	}
        else
        	{
        	
        	txtfromdate.value='${HomePageContent.get("todaysDate")}';
        	txttodate.value='${HomePageContent.get("todaysDate")}';
	        
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
        
        
        
      
        
        	document.getElementById("divTitle").innerHTML="<p1 class='labelFonts'>Hisaab Cloud (Petrol)</p1>";

        	document.title +=" Petrol";
          
        </script>
        
        