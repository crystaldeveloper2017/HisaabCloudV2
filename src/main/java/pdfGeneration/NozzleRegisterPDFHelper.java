
package pdfGeneration;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.crystal.Frameworkpackage.CommonFunctions;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
 
public class NozzleRegisterPDFHelper  extends PdfPageEventHelper
{
	CommonFunctions cf=new CommonFunctions();
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException 
	{
		
		//generatePDF("Ref:- SSEGPL/19/008/R","Date:- November 17th 2018","AKT Oil");
		
		
	}
	
	
	
	

	
	
	public void generateNozzleRegister(String BufferedImagesFolderPath,String DestinationPath,HashMap<String, Object> nozzleRegisterDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		List<LinkedHashMap<String, Object>> lstNozzleSales= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstNozzleSales")	;
		List<LinkedHashMap<String, Object>> lstLubeSales= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstLubeSales")	;
		
		List<LinkedHashMap<String, Object>> lstPayments= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstPayments")	;
		List<LinkedHashMap<String, Object>> lstCreditSales= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstCreditSales")	;
		TreeMap<String, Object> salesEmpWiseMap= (TreeMap<String, Object>) nozzleRegisterDetails.get("salesEmpWiseMap")	;
		TreeMap<String, Object> paymentEmpWiseMap= (TreeMap<String, Object>) nozzleRegisterDetails.get("paymentEmpWiseMap")	;
		String cashAgainstPumpTest= (String) nozzleRegisterDetails.get("cashAgainstPumpTest")	;
		Double totalLubeSales= (Double) nozzleRegisterDetails.get("totalLubeSales")	;
		
		
		
		
		
		 
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		Font font = new Font(base, 12, Font.NORMAL);
		
		  Document document = new Document (PageSize.A4, 20, 20, 0, 60);
		  document.setMargins(50, 36, 36, 36); // top, right, bottom, left

		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  double paytmSum=Double.valueOf( lstPayments.get(0).get("paytmsum").toString());
			double cardswipesum= Double.valueOf(lstPayments.get(0).get("cardswipesum").toString());
			double creditsalessum= Double.valueOf(lstPayments.get(0).get("pendingSum").toString());
		  
		  
	        PdfPCell cell;	        
			PdfPTable table;

			
			table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Nozzle Register Report : "+nozzleRegisterDetails.get("txtfromdate"),new Font(base, 14, Font.BOLD)));	        
	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	      

			document.add(table);

			document.add(new Paragraph("\n"));

			
			table = new PdfPTable(5);
			cell = new PdfPCell(new Phrase("Nozzle Sales",font));	        
	        cell.setColspan(5);
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

	        table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Shift No",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Item",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Rate",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Quantity",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Amount",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);


			double sumAmountPetrol=0;
			double sumQtyPetrol=0;

			double sumAmountDiesel=0;
			double sumQtyDiesel=0;


			for(LinkedHashMap<String, Object> sale: lstNozzleSales)
			{
				cell = new PdfPCell(new Phrase(sale.get("shift_name").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        


				cell = new PdfPCell(new Phrase(sale.get("item_name").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        

				cell = new PdfPCell(new Phrase(sale.get("rate").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);
				
				

				String formattedNumber = String.format("%.3f", sale.get("SalesSum"));


				cell = new PdfPCell(new Phrase(formattedNumber,font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	        

				cell = new PdfPCell(new Phrase(sale.get("totalAmountSum").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	  
				
				if(sale.get("item_name").toString().equals("Petrol"))
				{
					sumAmountPetrol+=Double.valueOf(sale.get("totalAmountSum").toString());
					sumQtyPetrol+=Double.valueOf(sale.get("SalesSum").toString()); 
				}

				if(sale.get("item_name").toString().equals("Diesel"))
				{
					sumAmountDiesel+=Double.valueOf(sale.get("totalAmountSum").toString());
					sumQtyDiesel+=Double.valueOf(sale.get("SalesSum").toString()); 
				}


				
			}
	        



			cell = new PdfPCell(new Phrase("S1). Total Diesel Sales",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
			cell.setColspan(3);	        
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			String sumQtyDieselString=new DecimalFormat("#.00").format(sumQtyDiesel);

			cell = new PdfPCell(new Phrase(sumQtyDieselString,font));	        
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			String sumAmountDieselString=new DecimalFormat("#.00").format(sumAmountDiesel);

			cell = new PdfPCell(new Phrase(sumAmountDieselString,font));	
			cell.setBackgroundColor(new BaseColor(Color.lightGray));        
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);


			cell = new PdfPCell(new Phrase("S2). Total Petrol Sales",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
			cell.setColspan(3);	        
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			
			String sumQtyPetrolString=new DecimalFormat("#.00").format(sumQtyPetrol);

			cell = new PdfPCell(new Phrase(String.valueOf(sumQtyPetrolString),font));	        
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			String sumAmountPetrolString=new DecimalFormat("#.00").format(sumAmountPetrol);
			cell = new PdfPCell(new Phrase(String.valueOf(sumAmountPetrolString),font));	
			cell.setBackgroundColor(new BaseColor(Color.lightGray));        
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);
			

			document.add(table);	  


			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));



			table = new PdfPTable(5);
			cell = new PdfPCell(new Phrase("Lube Sales",font));	        
	        cell.setColspan(5);
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

	        table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Shift No",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Item",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Rate",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Quantity",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Amount",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

					
			

			for(LinkedHashMap<String, Object> sale: lstLubeSales)
			{
				cell = new PdfPCell(new Phrase(sale.get("shift_name").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        


				cell = new PdfPCell(new Phrase(sale.get("item_name").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        

				cell = new PdfPCell(new Phrase(sale.get("rate").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	        


				cell = new PdfPCell(new Phrase(sale.get("qty").toString(),font));
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);

				cell = new PdfPCell(new Phrase(sale.get("total_amount").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	        

				
			}

			cell = new PdfPCell(new Phrase("S3). Total Lube Sales ",font));	        
			cell.setColspan(4);
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);	        

			

			cell = new PdfPCell(new Phrase(String.valueOf(totalLubeSales),font));	        
			cell.setColspan(2);
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        

	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);	        

	        
	        
			document.add(table);	

			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			
			
			table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Sales Summary (S)",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

			cell = new PdfPCell(new Phrase("S1.) Total Diesel Sales",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			
	        table.addCell(cell);	

			cell = new PdfPCell(new Phrase(sumAmountDieselString,font));	        
			
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			


			cell = new PdfPCell(new Phrase("S2.) Total Petrol Sales",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			
	        table.addCell(cell);	

			cell = new PdfPCell(new Phrase(sumAmountPetrolString,font));	        
			
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);	


			cell = new PdfPCell(new Phrase("S3.) Total Lube Sales",font));	        	        
			
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	

			
			cell = new PdfPCell(new Phrase(String.valueOf(totalLubeSales),font));	        			
			
	        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);


			cell = new PdfPCell(new Phrase("Total Sales (S)",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	        table.addCell(cell);	


			
			double totalSales=totalLubeSales+sumAmountDiesel+sumAmountPetrol;
			String totalSalesString=new DecimalFormat("#.00").format(totalSales);

			cell = new PdfPCell(new Phrase(totalSalesString,font));	        	        
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			
			
			document.add(table);



			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));


			List<LinkedHashMap<String, Object>> lstCardSwipes= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstCardSwipes");
			List<LinkedHashMap<String, Object>> lstPaytmSlotwise= (List<LinkedHashMap<String, Object>>) nozzleRegisterDetails.get("lstPaytmSlotwise");
			

			table = new PdfPTable(3);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Card Payment Details",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(3);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	


			cell = new PdfPCell(new Phrase("Bank Name",font));	        	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);	

			cell = new PdfPCell(new Phrase("Slot",font));	        	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);	

			cell = new PdfPCell(new Phrase("Amount",font));	        	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);	


			for(LinkedHashMap lhm: lstCardSwipes)
			{
				cell = new PdfPCell(new Phrase(lhm.get("bank_name").toString(),font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);	

				cell = new PdfPCell(new Phrase(lhm.get("slot_id").toString(),font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(lhm.get("cardAmount").toString(),font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);	
			}


			cell = new PdfPCell(new Phrase("P1).Total Card Payments",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	

			cell = new PdfPCell(new Phrase(lstPayments.get(0).get("cardswipesum").toString(),font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));

			table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Paytm Payment Details",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			
			
			cell = new PdfPCell(new Phrase("Slot Name",font));	        	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);	

			cell = new PdfPCell(new Phrase("Amount",font));	        	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);	


			for(LinkedHashMap lhm: lstPaytmSlotwise)
			{
				

				cell = new PdfPCell(new Phrase(lhm.get("slot_id").toString(),font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(lhm.get("paytmAmount").toString(),font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);	
			}
			cell = new PdfPCell(new Phrase("P2). Total Paytm Amount",font));	        	        
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
				table.addCell(cell);

				
				cell = new PdfPCell(new Phrase(lstPayments.get(0).get("paytmsum").toString(),font));
	    		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	        	table.addCell(cell);



			document.add(table);
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));


			


			


			table = new PdfPTable(2);
			cell = new PdfPCell(new Phrase("Credit Sales Details",font));	        
			cell.setBackgroundColor(new BaseColor(Color.lightGray));
	        cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

	        table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Customer Name",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Amount",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			

					
			

			for(LinkedHashMap<String, Object> sale: lstCreditSales)
			{
				cell = new PdfPCell(new Phrase(sale.get("customer_name").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        


				cell = new PdfPCell(new Phrase(sale.get("total_amount").toString(),font));	        
	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	        

				     

				
			}
			

			cell = new PdfPCell(new Phrase("P3). Total Credit Sales",font));	 
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
       	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
			cell = new PdfPCell(new Phrase(String.valueOf(creditsalessum),font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        

	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			
	        
	        
			document.add(table);



			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));


			table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Payment Summary (P) ",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

			cell = new PdfPCell(new Phrase("Cash",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
			Double cashPayments=Double.valueOf(lstPayments.get(0).get("cashsum").toString());
			cashAgainstPumpTest=cashAgainstPumpTest==null? "0":cashAgainstPumpTest;
			Double cashAgainstPumpTestDoouble=Double.valueOf(cashAgainstPumpTest);
			Double finalCash=cashPayments-cashAgainstPumpTestDoouble;
			cell = new PdfPCell(new Phrase( String.valueOf(finalCash),font));
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);


			cell = new PdfPCell(new Phrase("P1.) Card Swipe",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			cell = new PdfPCell(new Phrase(lstPayments.get(0).get("cardswipesum").toString(),font));
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);


			cell = new PdfPCell(new Phrase("P2). Total Paytm Amount",font));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			cell = new PdfPCell(new Phrase(lstPayments.get(0).get("paytmsum").toString(),font));
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);


			

			

			
			

			
			


			cell = new PdfPCell(new Phrase("P3). Total Credit Sales ",font));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
			cell = new PdfPCell(new Phrase(lstPayments.get(0).get("pendingSum").toString(),font));
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			
			
			double totalPaymentsAmount=finalCash+paytmSum+ cardswipesum+creditsalessum; 
			

			String totalPaymentsAmountString=new DecimalFormat("#.00").format(totalPaymentsAmount);

			cell = new PdfPCell(new Phrase("P). Total Payments",font));	 
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
       	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
			cell = new PdfPCell(new Phrase(String.valueOf(totalPaymentsAmountString),font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        

	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);
			
			document.add(table);


			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));


			


			table = new PdfPTable(2);
			table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Total Summary = Sales (S) - Payments (P)",font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
			cell.setColspan(2);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

			cell = new PdfPCell(new Phrase(String.valueOf("Total Sales"),font));			
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase(totalSalesString,font));	        	        
			
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);


			cell = new PdfPCell(new Phrase(String.valueOf("Total Payments"),font));			
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase(totalPaymentsAmountString,font));	        	        
			
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			
			double differenceAmount=totalSales-totalPaymentsAmount;
			String differenceString=new DecimalFormat("#.00").format(differenceAmount);


			cell = new PdfPCell(new Phrase(String.valueOf("Difference"),font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase(String.valueOf(differenceString),font));
			cell.setBackgroundColor(new BaseColor(Color.lightGray));	        	        
	    	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        table.addCell(cell);

			document.add(table);


			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));



			table = new PdfPTable(4);
			cell = new PdfPCell(new Phrase("Difference FSM",font));	  
			cell.setBackgroundColor(new BaseColor(Color.lightGray));      
	        cell.setColspan(4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        

	        table.setWidthPercentage(100f);
			cell = new PdfPCell(new Phrase("Employee Name",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Sales Amount",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Payment Amount",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	

			cell = new PdfPCell(new Phrase("Difference",font));	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	

					
			
				

			for (Map.Entry<String, Object> entry : salesEmpWiseMap.entrySet()) 
			{


				cell = new PdfPCell(new Phrase(entry.getKey().toString(),font));	        	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	table.addCell(cell);	        

				cell = new PdfPCell(new Phrase(entry.getValue().toString(),font));	        	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	        
				String paymentValue=paymentEmpWiseMap.get(entry.getKey()) == null ? "0" :paymentEmpWiseMap.get(entry.getKey()).toString();
				cell = new PdfPCell(new Phrase(paymentValue,font));	        	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	    


				Double difference= Double.parseDouble(paymentValue) - Double.parseDouble(entry.getValue().toString());
				String formattedValue=new DecimalFormat("#.00").format(difference);
				     
				cell = new PdfPCell(new Phrase(String.valueOf(formattedValue),font));	        	        	
	        	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        	table.addCell(cell);	    

				
			}
	        
	        
			document.add(table);



			
		  	document.close();


			
		  
		  
		  
		  
		
		
		
		
	}
	
	
	
			
	
	}