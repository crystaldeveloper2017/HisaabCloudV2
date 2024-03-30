
package pdfGeneration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.crystal.Frameworkpackage.CommonFunctions;
import com.itextpdf.text.Chunk;
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
 
public class InvoiceHistoryPDFHelper  extends PdfPageEventHelper
{
	private static final String BufferedImagesFolderPath = null;
	CommonFunctions cf=new CommonFunctions();
	public static void main(String[] args) throws DocumentException, MalformedURLException, IOException 
	{
		
		//generatePDF("Ref:- SSEGPL/19/008/R","Date:- November 17th 2018","AKT Oil");
		
		
	}
	
	
	
	public void generateSalesRegister2(String DestinationPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		LinkedHashMap<String, String> customerDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("customerDetails");
		LinkedHashMap<String, String> totalDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("totalDetails");
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		
		
		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Sales Summary 2",new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		  document.add(table);
		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
	        
	        cell = new PdfPCell(new Phrase("Party Name :- "+customerDetails.get("customer_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	                
	        cell = new PdfPCell(new Phrase("Mobile No :- "+customerDetails.get("mobile_number").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Date :- "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
		        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		
		  
		  
		  
		  table = new PdfPTable(8);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{10,5,20,5,5,7,10,10});

		  
		  
		  cell = new PdfPCell(new Phrase("Invoice Date",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Ref",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Return",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Billed Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        	        
	        cell = new PdfPCell(new Phrase("Bill Amount",new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        
	        
	        String invoiceNo="";
	        
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	if(!invoiceNo.equals("") && !invoiceNo.equals(prod.get("invoice_no").toString()))
	        	{
	        		
	        		cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
					  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					  cell.setMinimumHeight(15);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        
				        table.addCell(cell);
	        	}
	        	invoiceNo=prod.get("invoice_no").toString();
	        	cell = new PdfPCell(new Phrase(prod.get("formattedInvoiceDate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("invoice_no").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String qtytoreturn=prod.get("qty_to_return")==null?"":prod.get("qty_to_return").toString();
			        
			        cell = new PdfPCell(new Phrase(qtytoreturn,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("BilledQty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        
					  
			        
			        cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("ItemAmount").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        
	        
	        
	       
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		  
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		      
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("ItemAmountSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        
	        
	        
	        
	        
	        
	       
	        
	        
		  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	

	public void generatePDFForCustomerInvoiceHistory(String DestinationPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		LinkedHashMap<String, String> customerDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("customerDetails");
		LinkedHashMap<String, String> totalDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("totalDetails");
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		
		
		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Sales Summary",new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		  document.add(table);
		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
	        
	        cell = new PdfPCell(new Phrase("Party Name :- "+customerDetails.get("customer_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	                
	        cell = new PdfPCell(new Phrase("Mobile No :- "+customerDetails.get("mobile_number").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Date :- "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
		        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		
		  
		  
		  
		  table = new PdfPTable(10);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{10,5,20,5,5,7,7,7,10,10});

		  
		  
		  cell = new PdfPCell(new Phrase("Invoice Date",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Ref",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Return",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Billed Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("MRP",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Discount",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Bill Amount",new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        
	        
	        String invoiceNo="";
	        
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	if(!invoiceNo.equals("") && !invoiceNo.equals(prod.get("invoice_no").toString()))
	        	{
	        		
	        		cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
					  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					  cell.setMinimumHeight(15);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
				        table.addCell(cell);
	        	}
	        	invoiceNo=prod.get("invoice_no").toString();
	        	cell = new PdfPCell(new Phrase(prod.get("formattedInvoiceDate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("invoice_no").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String qtytoreturn=prod.get("qty_to_return")==null?"":prod.get("qty_to_return").toString();
			        
			        cell = new PdfPCell(new Phrase(qtytoreturn,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("BilledQty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
					  
			        
			        cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("DiscountAmount").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("ItemAmount").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        
	        
	        
	        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		  
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("discountAmountSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("ItemAmountSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        
	        
	        
	        
	        
	        
	       
	        
	        
		  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	public void generatePDFForInvoice(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		
		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		Font font = new Font(base, 12, Font.NORMAL);
		
		  Document document = new Document (PageSize.A4, 20, 20, 0, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;
	        
	        table.setWidthPercentage(100f);
	        
	        cell = new PdfPCell(new Phrase("Invoice : "+invoiceHistoryDetails.get("invoice_no").toString(),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
	        
	        
	        cell = new PdfPCell(new Phrase("",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_2")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("city")+"-"+invoiceHistoryDetails.get("pincode"),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("****************************************************************************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
		  document.add(table);
		  
		  
		  
		  

		  
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
		  
		  
		  if(invoiceHistoryDetails.get("customer_name")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Party Name :- "+invoiceHistoryDetails.get("customer_name").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
		        
		        
		        
		  }
		 
	                
	      
	        
	        
	        cell = new PdfPCell(new Phrase("Date : "+invoiceHistoryDetails.get("theInvoiceDate").toString(),font));
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Payment Type : "+String.valueOf(invoiceHistoryDetails.get("payment_type")),font));
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        if(invoiceHistoryDetails.get("payment_mode")!=null)
	        {
		        cell = new PdfPCell(new Phrase("Payment Mode : "+String.valueOf(invoiceHistoryDetails.get("payment_mode")),font));
		        cell.setBorder(Rectangle.NO_BORDER);	        
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
	        }
	        
	        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		
		  
		  
		  
		  table = new PdfPTable(6);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{3,26,7,7,7,7});

		  
		  
		  cell = new PdfPCell(new Phrase("Sr.",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("MRP",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Total",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	      
	        
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	cell = new PdfPCell(new Phrase(String.valueOf(srno++),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("price").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        			        
			        
			        Double itemAmount=Double.parseDouble(prod.get("custom_rate").toString()) * Double.parseDouble(prod.get("qty").toString());
			        
			        cell = new PdfPCell(new Phrase(itemAmount.toString(),new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        
	    	cell = new PdfPCell(new Phrase(String.valueOf(""),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  
		        table.addCell(cell);
		        
		        
		        
		        
		        cell = new PdfPCell(new Phrase("Total Qty :  "+totalQty.toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(2);
		        table.addCell(cell);
		        
		        
		        
		        			        
		        
		        
		        
				/*
				 * cell = new PdfPCell(new
				 * Phrase("Gross Amount : "+String.valueOf(invoiceHistoryDetails.get(
				 * "gross_amount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));
				 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
				 * table.addCell(cell);
				 */
		        
		        
				  cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
				  table.addCell(cell);
				 
		        
		        
		        
		        
				/*
				 * cell = new PdfPCell(new Phrase(ItemDiscountString,new
				 * Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));
				 * cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
				 * table.addCell(cell);
				 */
			        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));
					  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
					  table.addCell(cell);
			        
		        
		        
		        
						/*
						 * cell = new PdfPCell(new
						 * Phrase("Invoice Discount : "+String.valueOf(invoiceHistoryDetails.get(
						 * "invoice_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)
						 * )); cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						 * cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
						 * table.addCell(cell);
						 */
					  
					  cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));
					  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE); cell.setColspan(3);
					  table.addCell(cell);
		        
		        
		        if(invoiceHistoryDetails.get("total_gst")!=null && !invoiceHistoryDetails.get("total_gst").equals("0.00"))
		        {
			        cell = new PdfPCell(new Phrase("Gst : "+String.valueOf(invoiceHistoryDetails.get("total_gst")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setColspan(6);
			        table.addCell(cell);
		        }
		        
		        cell = new PdfPCell(new Phrase("Total Amount : "+String.valueOf(invoiceHistoryDetails.get("total_amount")+"/-"),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(6);
		        table.addCell(cell);
		        
		        
		        if(invoiceHistoryDetails.get("payment_type").equals("Partial"))
		        {
		        cell = new PdfPCell(new Phrase(String.valueOf("Partially Paid Amount : "+String.valueOf(invoiceHistoryDetails.get("paid_amount"))),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				  cell.setColspan(6);
			        table.addCell(cell);
		        }
			        
			      
				        
				        cell = new PdfPCell(new Phrase(String.valueOf("Remarks : "+String.valueOf(invoiceHistoryDetails.get("remarks"))),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
						  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
						  cell.setColspan(6);
					        table.addCell(cell);
	        
		  document.add(table);
		  
		  
		  
		  document.add(new Paragraph("\n"));
	        
	        table = new PdfPTable(1);
			  table.setWidthPercentage(100);
		        
		        cell = new PdfPCell(new Phrase("****************************************************************************************",font));	        
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);	        
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("*Thank you, Visit Again*",font));	        
		        cell.setBorder(Rectangle.NO_BORDER);
		        	        
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        
		        
		        cell = new PdfPCell(new Phrase(" ",font));	        
		        cell.setBorder(Rectangle.NO_BORDER);
		        	        
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
			  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}

	public void generatePDFForInvoice3InchWithWeightSize(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException {
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL); 
		Font font10 = new Font(base, 10, Font.NORMAL); 

		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);

		  table.setWidthPercentage(100);
		  PdfPCell cell;
		  cell = new PdfPCell(new Phrase("Invoice Estimate: "+invoiceHistoryDetails.get("invoice_no").toString(),font14));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name").toString(), new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD)));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);

		  cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(1);	        
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);

  
		  document.add(table);
  
		  table = new PdfPTable(3);
		  table.setWidthPercentage(100);
		  table.setWidths(new int[]{1, 1, 2});
  
		  cell = new PdfPCell(new Phrase(""));
		  cell.setBorder(Rectangle.NO_BORDER);
  
		  cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1").toString()+invoiceHistoryDetails.get("address_line_2").toString(), new Font(Font.FontFamily.TIMES_ROMAN, 8)));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setColspan(3);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_3").toString(), new Font(Font.FontFamily.TIMES_ROMAN, 8)));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setColspan(3);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
  

  
		  cell = new PdfPCell(new Phrase("Phone "+invoiceHistoryDetails.get("mobile_no"), new Font(Font.FontFamily.TIMES_ROMAN, 8)));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setColspan(3);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase("Store Timings "+invoiceHistoryDetails.get("store_timing"), new Font(Font.FontFamily.TIMES_ROMAN, 8)));
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setColspan(3);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);

		  cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setPadding(0);
		  cell.setColspan(3);	        
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
		  
  
		  document.add(table);

		
		  table = new PdfPTable(3);
		  table.setWidthPercentage(100);
		  table.setWidths(new int[]{1, 4, 8});

		  cell = new PdfPCell(new Phrase(" Name : "+invoiceHistoryDetails.get("customer_name").toString(),font10));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(3);
			cell.setPadding(0);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		    table.addCell(cell);

			cell = new PdfPCell(new Phrase(" Date & Time : "+invoiceHistoryDetails.get("theUpdatedDate").toString(),font10));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase(" Paymnet Type : "+invoiceHistoryDetails.get("payment_type").toString(),font10));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
			cell.setColspan(3);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPadding(0);
			cell.setColspan(3);	        
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);
			

				
				document.add(table);

  
		  table = new PdfPTable(5);
		  table.setWidthPercentage(100);
		  table.setWidths(new int[]{2, 10, 2, 4, 3});
		  table.setSpacingBefore(10);  
		  cell = new PdfPCell(new Phrase("Sr", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase("Item Name", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase("Qty", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase("WT.", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  table.addCell(cell);
  
		  cell = new PdfPCell(new Phrase("Amount", new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD)));
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  table.addCell(cell);
		
		  int srnumber=1;
		  double totalWeight=0;
		  int totalqty=0;
		  for(HashMap<String,Object> prod:ListOfItemDetails)
		  {
			  
			  cell = new PdfPCell(new Phrase(String.valueOf(srnumber++),font ));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  table.addCell(cell);
				  
				  
				  

				  cell = new PdfPCell(new Phrase(prod.get("category_name").toString()+" "+prod.get("item_name").toString(), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  cell.setPaddingLeft(3);
				  table.addCell(cell);

				  int qty=Double.valueOf(prod.get("qty").toString()).intValue();
				totalqty+=qty;
				 cell = new PdfPCell(new Phrase(String.valueOf(qty), new Font(Font.FontFamily.TIMES_ROMAN, 10)));

				  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  cell.setPaddingLeft(3);
				  table.addCell(cell);
				  

				  totalWeight+=Double.valueOf(prod.get("weight").toString());

				  cell = new PdfPCell(new Phrase(prod.get("weight").toString(), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  cell.setPaddingLeft(3);
				  table.addCell(cell);


				  String custom_rate=invoiceHistoryDetails.get("custom_rate").toString();
				  String qtystring=invoiceHistoryDetails.get("qty").toString();
				  Double amount=Double.valueOf(custom_rate)*Double.valueOf(qtystring);
				  
				  cell = new PdfPCell(new Phrase(String.valueOf(amount), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  cell.setPaddingLeft(3);
				  table.addCell(cell);



				  
		
		
				
		  }
		  cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(5);	        
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);
		  document.add(table);

				table = new PdfPTable(2);
				table.setWidthPercentage(100);
				table.setWidths(new int[]{4, 8});

	

		

				
				cell = new PdfPCell(new Phrase("  Total WT : "+totalWeight,font10));
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setColspan(2);
				  cell.setPadding(0);
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  table.addCell(cell);
	  
				  cell = new PdfPCell(new Phrase(" QTY :  "+String.valueOf(totalqty), new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(2);
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  table.addCell(cell);

				  

				  cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(2);	        
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);
		

				  document.add(table);

				  table = new PdfPTable(1);
				table.setWidthPercentage(100);
				table.setWidths(new int[]{4});
	  
				// 	 cell = new PdfPCell(new Phrase(" Previous Due Amount :Undefined ", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				//   cell.setBorder(Rectangle.NO_BORDER);
				//   cell.setColspan(1);
				//   cell.setPadding(0);
				//   cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				//   table.addCell(cell);

				  
				  cell = new PdfPCell(new Phrase("---------------------------------------------------------------",font));	        
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(1);	        
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);

				  document.add(table);

				  table = new PdfPTable(1);
				  table.setWidthPercentage(100);
				  table.setWidths(new int[]{4});

				  cell = new PdfPCell(new Phrase("Payable Amount : 80.00 ", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD)));
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(1);	
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  table.addCell(cell);

				   
				  cell = new PdfPCell(new Phrase("**************************************",font));	        
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);	        
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);

				  document.add(table);

				  table = new PdfPTable(1);
				  table.setWidthPercentage(100);
				  table.setWidths(new int[]{4});

				  cell = new PdfPCell(new Phrase("*Thank You, Visit Again*", new Font(Font.FontFamily.TIMES_ROMAN, 10)));
				  cell.setBorder(Rectangle.NO_BORDER);
				  cell.setPadding(0);
				  cell.setColspan(1);	
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  table.addCell(cell);

				  document.add(table);
  
				 document.close();
		
		  
		  
		  
		
	}

	

	
	// public void generatePDFForInvoice3InchWithWeightSize(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	// {


		
	// 	List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		
	// 	BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
	// 	Font font14 = new Font(base, 14, Font.NORMAL);
		
	// 	Font font = new Font(base, 12, Font.NORMAL);
	// 	Font font10 = new Font(base, 10, Font.NORMAL);
	// 	Font font14bold = new Font(base, 14, Font.BOLD);

		
	// 	int fixedSize=320;
	// 	Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
	// 	Document document = new Document(pagesize,6,6,0,2);

		  
	// 	  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
	// 	  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
	// 	  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	//         writer.setPageEvent(event);
	// 	  document.open();     
		  
		  
	// 	  PdfPTable table = new PdfPTable(1);
	// 	  table.setWidthPercentage(100);
	//         PdfPCell cell;        
	        
	//         cell = new PdfPCell(new Phrase(("Invoice Estimate"),font10));	        
	//         cell.setBorder(Rectangle.NO_BORDER);
	//         cell.setPadding(0);
	//         cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	//         table.addCell(cell);
	// 	  document.add(table);
		  
		    
	        
		  
	// 	  document.close();
		  
		  
		  
		  
		
		
		
		
	
	// }
	
	public void generatePDFForInvoice3InchWithModelNo(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{


		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL);
		Font fontbold = new Font(base, 12, Font.BOLD);
		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Invoice : "+invoiceHistoryDetails.get("invoice_no").toString(),font14));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	       
	        
	        
	        cell = new PdfPCell(new Phrase("",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name").toString(),fontbold));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_2")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("city")+"-"+invoiceHistoryDetails.get("pincode"),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		  
		  
		  
		  

		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
		  
		  if(invoiceHistoryDetails.get("customer_name")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Party Name : "+invoiceHistoryDetails.get("customer_name").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);	        
		  }
		  if(invoiceHistoryDetails.get("model_no")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Model : "+invoiceHistoryDetails.get("model_no").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);	        
		  }
		  if(invoiceHistoryDetails.get("unique_no")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Unique No: "+invoiceHistoryDetails.get("unique_no").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);	        
		  }
		 
	                
	      
	        
	        
	        cell = new PdfPCell(new Phrase("Date : "+invoiceHistoryDetails.get("theInvoiceDate").toString(),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        

	        
	        
	       
	        document.add(table);
		  
		  
			document.add(new Paragraph("\n"));

		  table = new PdfPTable(5);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,10,15,7,9});

		  table.setSpacingBefore(10);
	      
		  cell = new PdfPCell(new Phrase("Sr",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Description ",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Amount",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	      
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	cell = new PdfPCell(new Phrase(String.valueOf(srno++),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("category_name").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString()	,font ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
			        Double reqQty=Double.parseDouble(prod.get("qty").toString());			        String reqQtyString=String.format("%.0f", reqQty);			        
			        cell = new PdfPCell(new Phrase(reqQtyString,font ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			   
			        			        
			        
			        Double itemAmount=Double.parseDouble(prod.get("custom_rate").toString()) * Double.parseDouble(prod.get("qty").toString());
			        
			        cell = new PdfPCell(new Phrase(itemAmount.toString(),font ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        double itemdiscount=Double.parseDouble(invoiceHistoryDetails.get("item_discount").toString());
	        double invoicediscount=Double.parseDouble(invoiceHistoryDetails.get("invoice_discount").toString());
	        
	        if(itemdiscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Item Discount : "+String.valueOf(invoiceHistoryDetails.get("item_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoicediscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Invoice Discount : "+String.valueOf(invoiceHistoryDetails.get("invoice_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoiceHistoryDetails.get("total_gst")!=null && !invoiceHistoryDetails.get("total_gst").equals("0.00"))
	        {
		        cell = new PdfPCell(new Phrase("Gst : "+String.valueOf(invoiceHistoryDetails.get("total_gst")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(6);
		        table.addCell(cell);
	        }
		        
		        
		        
	        	String totalQtyFormatted=String.format("%.0f", totalQty);
	        	
		        cell = new PdfPCell(new Phrase("Qty :  "+totalQtyFormatted,font ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(3);
		        table.addCell(cell);
		        
		        
		        
		        
		        
		        
		        			        
		        
		        
		        
		        cell = new PdfPCell(new Phrase("Total Amount : "+String.valueOf(invoiceHistoryDetails.get("total_amount")+"/-"),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(3);
		        table.addCell(cell);
		        
		        
		        

		        if(invoiceHistoryDetails.get("payment_type").equals("Partial"))
		        {
		        cell = new PdfPCell(new Phrase(String.valueOf("Partially Paid Amount : "+String.valueOf(invoiceHistoryDetails.get("paid_amount"))),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				  cell.setColspan(5);
			        table.addCell(cell);
		        }
			      
				        
				        cell = new PdfPCell(new Phrase(String.valueOf("Remarks : "+String.valueOf(invoiceHistoryDetails.get("remarks"))),font ));
						  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
						  cell.setColspan(5);
					        table.addCell(cell);
					        
					        
	        
		  document.add(table);
		  
		  document.add(new Paragraph("\n"));
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("*Thank you, Visit Again*",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	 
	        
		  document.add(table);
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	
	}
	
	
	
	
	public void generatePDFForInvoice3Inch(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL); 
		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Invoice : "+invoiceHistoryDetails.get("invoice_no").toString(),font14));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	       
	        
	        
	        cell = new PdfPCell(new Phrase("",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_2")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("city")+"-"+invoiceHistoryDetails.get("pincode"),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		  
		  
		  
		  

		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
		  
		  if(invoiceHistoryDetails.get("customer_name")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Party Name :- "+invoiceHistoryDetails.get("customer_name").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
		  }
		 
	                
	      
	        
	        
	        cell = new PdfPCell(new Phrase("Date : "+invoiceHistoryDetails.get("theInvoiceDate").toString(),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Payment Type : "+String.valueOf(invoiceHistoryDetails.get("payment_type")),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        if(invoiceHistoryDetails.get("payment_mode")!=null)
	        {	        
		        cell = new PdfPCell(new Phrase("Payment Mode : "+String.valueOf(invoiceHistoryDetails.get("payment_mode")),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);

		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
	        }
	        
	        
	       
	        document.add(table);
		  
		  
		  
		  table = new PdfPTable(5);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,24,9,7,9});

		  table.setSpacingBefore(10);
	      
		  cell = new PdfPCell(new Phrase("Sr",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Total",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	      
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	cell = new PdfPCell(new Phrase(String.valueOf(srno++),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        String customRate=String.format("%.0f", Double.valueOf(prod.get("custom_rate").toString())) ;
			        
			        
			        cell = new PdfPCell(new Phrase(customRate,font ));
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        			        
			        
			        Double itemAmount=Double.parseDouble(prod.get("custom_rate").toString()) * Double.parseDouble(prod.get("qty").toString());
			        
			        cell = new PdfPCell(new Phrase(itemAmount.toString(),font ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        double itemdiscount=Double.parseDouble(invoiceHistoryDetails.get("item_discount").toString());
	        double invoicediscount=Double.parseDouble(invoiceHistoryDetails.get("invoice_discount").toString());
	        
	        if(itemdiscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Item Discount : "+String.valueOf(invoiceHistoryDetails.get("item_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoicediscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Invoice Discount : "+String.valueOf(invoiceHistoryDetails.get("invoice_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoiceHistoryDetails.get("total_gst")!=null && !invoiceHistoryDetails.get("total_gst").equals("0.00"))
	        {
		        cell = new PdfPCell(new Phrase("Gst : "+String.valueOf(invoiceHistoryDetails.get("total_gst")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
		        
		        
		        
		        
		        cell = new PdfPCell(new Phrase("Qty :  "+totalQty.toString(),font ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(2);
		        table.addCell(cell);
		        
		        
		        
		        			        
		        
		        String totalAmount=String.format("%.0f", Double.valueOf(invoiceHistoryDetails.get("total_amount").toString())) ;
		        
		        
		        cell = new PdfPCell(new Phrase("Total Amount : "+String.valueOf(totalAmount+"/-"),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(3);
		        table.addCell(cell);
		        
		        
		        

		        if(invoiceHistoryDetails.get("payment_type").equals("Partial"))
		        {
		        cell = new PdfPCell(new Phrase(String.valueOf("Partially Paid Amount : "+String.valueOf(invoiceHistoryDetails.get("paid_amount"))),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				  cell.setColspan(5);
			        table.addCell(cell);
		        }
			      
				        
				        cell = new PdfPCell(new Phrase(String.valueOf("Remarks : "+String.valueOf(invoiceHistoryDetails.get("remarks"))),font ));
						  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
						  cell.setColspan(5);
					        table.addCell(cell);
					        
					        
	        
		  document.add(table);
		  
		  document.add(new Paragraph("\n"));
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("*Thank you, Visit Again*",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        
			/*
			 * cell = new PdfPCell(new
			 * Phrase(" Designed and Developed By crystaldevelopers2017@gmail.com",font));
			 * cell.setBorder(Rectangle.NO_BORDER);
			 * 
			 * cell.setHorizontalAlignment(Element.ALIGN_CENTER); table.addCell(cell);
			 */
	        
		  document.add(table);
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	public void generateKotPDFService3Inch(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails) throws DocumentException, MalformedURLException, IOException
	{

		
		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("itemDetails");
		
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL); 
		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Table No : "+invoiceHistoryDetails.get("table_no").toString(),font14));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	       
	        
	       
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		  
		  
		  
		  

		
		  
		  
		  
		  table = new PdfPTable(4);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,24,9,7});

		  table.setSpacingBefore(10);
	      
		  cell = new PdfPCell(new Phrase("Sr",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Remarks",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	      
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	cell = new PdfPCell(new Phrase(String.valueOf(srno++),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("remarks").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        			        
			        
			        
			        
			        
	        }
	        
	
		        
		        
		    
		        
		        
		        
	        
		
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	      
	        
	        cell = new PdfPCell(new Phrase(" Designed and Developed By crystaldevelopers2017@gmail.com",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	
	public void generatePDFForInvoice2Inch(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL); 
		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(220, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Invoice : "+invoiceHistoryDetails.get("invoice_no").toString(),font14));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	       
	        
	        
	        cell = new PdfPCell(new Phrase("",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_2")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("city")+"-"+invoiceHistoryDetails.get("pincode"),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("*********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		  
		  
		  
		  

		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
		  
		  if(invoiceHistoryDetails.get("customer_name")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Party Name :- "+invoiceHistoryDetails.get("customer_name").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
		  }
		 
	                
	      
	        
	        
	        cell = new PdfPCell(new Phrase("Date : "+invoiceHistoryDetails.get("theInvoiceDate").toString(),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Payment Type : "+String.valueOf(invoiceHistoryDetails.get("payment_type")),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        if(invoiceHistoryDetails.get("payment_mode")!=null)
	        {	        
		        cell = new PdfPCell(new Phrase("Payment Mode : "+String.valueOf(invoiceHistoryDetails.get("payment_mode")),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);

		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
	        }
	        
	        
	       
	        document.add(table);
		  
		  
		  
		  table = new PdfPTable(4);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,24,9,9});

		  table.setSpacingBefore(10);
	      
		  cell = new PdfPCell(new Phrase("Sr",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Item Total",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	      
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	cell = new PdfPCell(new Phrase(String.valueOf(srno++),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),font ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        			        
			        
			        Double itemAmount=Double.parseDouble(prod.get("custom_rate").toString()) * Double.parseDouble(prod.get("qty").toString());
			        
			        cell = new PdfPCell(new Phrase(itemAmount.toString(),font ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        }
	        
	        
	    
		        
		        
		        
		        
		        cell = new PdfPCell(new Phrase("Qty :  "+totalQty.toString(),font ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(2);
		        table.addCell(cell);
		        
		        
		        
		        			 
		        if(!invoiceHistoryDetails.get("item_discount").equals("0.00"))
		        {
			        cell = new PdfPCell(new Phrase("Item Discount : "+String.valueOf(invoiceHistoryDetails.get("item_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setColspan(5);
			        table.addCell(cell);
		        }
		        
		        if(!invoiceHistoryDetails.get("invoice_discount").equals("0.00"))
		        {
			        cell = new PdfPCell(new Phrase("Invoice Discount : "+String.valueOf(invoiceHistoryDetails.get("invoice_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setColspan(5);
			        table.addCell(cell);
		        }
		        
		        if(!invoiceHistoryDetails.get("total_gst").equals("0.00"))
		        {
			        cell = new PdfPCell(new Phrase("Gst : "+String.valueOf(invoiceHistoryDetails.get("total_gst")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setColspan(5);
			        table.addCell(cell);
		        }
		        
		        
		        
		        cell = new PdfPCell(new Phrase("Total Amount : "+String.valueOf(invoiceHistoryDetails.get("total_amount")+"/-"),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(3);
		        table.addCell(cell);
		        
		        

			        
		        
		        if(invoiceHistoryDetails.get("payment_type").equals("Partial"))
		        {
		        cell = new PdfPCell(new Phrase(String.valueOf("Partially Paid Amount : "+String.valueOf(invoiceHistoryDetails.get("paid_amount"))),font ));
				  cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				  cell.setColspan(5);
			        table.addCell(cell);
		        }
			      
				        
				        cell = new PdfPCell(new Phrase(String.valueOf("Remarks : "+String.valueOf(invoiceHistoryDetails.get("remarks"))),font ));
						  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
						  cell.setColspan(5);
					        table.addCell(cell);
	        
		  document.add(table);
		  
		  document.add(new Paragraph("\n"));
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        
	        cell = new PdfPCell(new Phrase("*********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("*Thank you, Visit Again*",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        
	        cell = new PdfPCell(new Phrase(" Designed and Developed By crystaldevelopers2017@gmail.com",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}

	

	 public void onStartPage(PdfWriter writer, Document document)
	 {
		 /*try
		 {
			 addHeader(document);
			 addWaterMark(document,writer);
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }*/
    }
	 
	 int pageNo=0;
	 public void onEndPage(PdfWriter writer, Document document)
	 {
		 try
		 {
			 PdfPTable table = new PdfPTable(1);
		        table.setWidthPercentage(100);
		        table.setTotalWidth(590);
		        
		        
		        
		        PdfPCell cell;
		        
		        cell = new PdfPCell(new Phrase("Page No:-"+(++pageNo),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));        
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        table.addCell(cell);
		        table.writeSelectedRows(0, -1, 0, 20, writer.getDirectContent());
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
    }
	 
	 public static void addWaterMark(Document documen,PdfWriter writer) throws MalformedURLException, IOException, DocumentException
	 {
		 PdfContentByte canvas = writer.getDirectContentUnder();
		 
		 
		 
		 
		 URL resource = InvoiceHistoryPDFHelper.class.getResource("watermark.png");
	        Image image =Image.getInstance(resource);
		 
		 image.setAbsolutePosition(115, 230);
		 image.scaleAbsolute(400, 400);
		 canvas.saveState();
		 PdfGState state = new PdfGState();
		 //state.setFillOpacity(0.1f);
		 canvas.setGState(state);
		 canvas.addImage(image);
		 canvas.restoreState();
		 
		 
	 }
	public static void addHeader(Document document) throws MalformedURLException, IOException, DocumentException
	{
		
		
		PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        
        
        
        
        URL resource = InvoiceHistoryPDFHelper.class.getResource("topLeft.png");
        Image img1 =Image.getInstance(resource);
        
        resource = InvoiceHistoryPDFHelper.class.getResource("topRight.jpg");
        Image img2 = Image.getInstance(resource);                
        PdfPCell cell;
        img1.scalePercent(40);
        cell = new PdfPCell(img1);        
        img2.setAlignment(Image.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        img2.scalePercent(22);        
        cell = new PdfPCell(img2);
        cell.setHorizontalAlignment(Image.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);        
        table.addCell(cell);        
		document.add(table);
		
		document.add(new Paragraph("\n"));
	}


	

	public void generatePDFForCustomerLedgerWithItem(String DestinationPath,String BufferedImagesFolder,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException, SQLException
	{

		
		LinkedHashMap<String, String> customerDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("customerDetails");
		LinkedHashMap<String, String> totalDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("totalDetails");
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		LinkedHashMap<String, Object> storeDetails= (LinkedHashMap<String, Object>) invoiceHistoryDetails.get("storeDetails");
		String fromDate = ( String) invoiceHistoryDetails.get("fromDate");
		String toDate = ( String) invoiceHistoryDetails.get("toDate");


		BaseFont base = BaseFont.createFont(BufferedImagesFolder+"/CALIBRI.TTF", BaseFont.WINANSI, false);

		Font fontnew = new Font(base, 9, Font.NORMAL); 

		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  

		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase(storeDetails.get("store_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD)));	        
	        cell.setBorder(Rectangle.BOX);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
			
			
			cell = new PdfPCell(new Phrase(storeDetails.get("address_line_1").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
	        cell.setBorder(Rectangle.BOTTOM);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		 
		  
			cell = new PdfPCell(new Phrase(storeDetails.get("address_line_2").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);	
		  String addressline3=storeDetails.get("address_line_3")==null?"":storeDetails.get("address_line_3").toString();
		  cell = new PdfPCell(new Phrase(addressline3,new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);	
		String mobileno=storeDetails.get("mobile_no")==null?"":storeDetails.get("mobile_no").toString();
		String reqmobileno="Phone No : "+mobileno;
		  cell = new PdfPCell(new Phrase(reqmobileno,new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
		  cell.setBorder(Rectangle.BOTTOM);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);	

		  
		  	

		  cell = new PdfPCell(new Phrase("Party Statement Of Accounts For The Period From : "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.BOTTOM);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        

		  cell = new PdfPCell(new Phrase("To, M/S SHREE CONSTRUCTION CO. ",new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD)));	        
		  cell.setBorder(Rectangle.BOTTOM);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		  table.addCell(cell);	

		  cell = new PdfPCell(new Phrase(customerDetails.get("address").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
		  cell.setBorder(Rectangle.NO_BORDER);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		  table.addCell(cell);	

		  
		  cell = new PdfPCell(new Phrase("Mobile No : "+customerDetails.get("mobile_number").toString()+" , "+customerDetails.get("alternate_mobile_no").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL)));	        
		  cell.setBorder(Rectangle.BOTTOM);
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		  table.addCell(cell);	

		  cell = new PdfPCell(new Phrase("Print Date & Time : "+cf.getDateTimeWithoutSeconds(con),new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL ) ));	        	        
	        cell.setBorder(Rectangle.BOTTOM);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);

	        table.addCell(cell);
	        
		 
		  
		document.add(table);


		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		  
		  
		  table = new PdfPTable(9);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{3,5,11,3,4,5,5,5,5});

		  cell = new PdfPCell(new Phrase("Sr No",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
		  
		  cell = new PdfPCell(new Phrase("Date",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Transaction Type",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Ref Id",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);

			cell = new PdfPCell(new Phrase("Particulars",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);

			

			cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  

			cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Debit",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Credit",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);



			cell = new PdfPCell(new Phrase("Opening Balance As on "+invoiceHistoryDetails.get("fromDate"),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			cell.setColspan(7);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		  table.addCell(cell);
			

			  String debitAmountString="";
			  String creditAmountString="";
			  Double s=Double.parseDouble(totalDetails.get("openingAmount"));

			  double openingbalancedouble=s;

			  if(openingbalancedouble<=0)
			  {debitAmountString=String.valueOf(openingbalancedouble*-1);}
			  else
			  {creditAmountString=String.valueOf(openingbalancedouble);}

			
			  cell = new PdfPCell(new Phrase(debitAmountString,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  table.addCell(cell);
			  
			  cell = new PdfPCell(new Phrase(creditAmountString,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  table.addCell(cell);
	        
	        
			int srno=1;

	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {

				cell = new PdfPCell(new Phrase(String.valueOf(srno++),fontnew ));

				  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell); 

					
	        	cell = new PdfPCell(new Phrase(prod.get("transaction_date").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        String type=prod.get("type")==null?"":prod.get("type").toString();
			        cell = new PdfPCell(new Phrase(type,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String invoice_no=prod.get("invoice_no")==null?"":prod.get("invoice_no").toString();
			        cell = new PdfPCell(new Phrase(invoice_no,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					

					
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			        String debitAmount=prod.get("debitAmount").toString().equals("0.00")?"":prod.get("debitAmount").toString();
			        cell = new PdfPCell(new Phrase(debitAmount,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("creditAmount").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			  
	        }
	        
			
		        
		       
				
	  
				cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
  
					
				
				  
			  cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  table.addCell(cell);

			  cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  table.addCell(cell);

				  
			  cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  table.addCell(cell);
				
			cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);

				 
				  cell = new PdfPCell(new Phrase("Debit Total",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  table.addCell(cell);
				  
				  cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("debitSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				  table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			  
			        
			       
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					



					cell = new PdfPCell(new Phrase("Credit Total",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
					
			        
			        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("creditSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);


	        
	        
			 
				        
				        cell = new PdfPCell(new Phrase("Closing Balance As On :-"+invoiceHistoryDetails.get("toDate"),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        cell.setColspan(7);
				        table.addCell(cell);
				        
				        String totalAmount=String.valueOf(totalDetails.get("totalAmount"));
				        double d=Double.parseDouble(totalAmount);
				        String creditTotal="";
				        String debitTotal="";
				        
				        if(d>0)
				        {
				        	creditTotal=String.valueOf(d);
				        }
				        else
				        {
				        	debitTotal=String.valueOf(d*-1);
				        }
				        
				        
				        cell = new PdfPCell(new Phrase(String.valueOf(debitTotal),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));			        
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        table.addCell(cell);
				        
				        cell = new PdfPCell(new Phrase(String.valueOf(creditTotal),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));			        
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        table.addCell(cell);
				        
				        
				        
				      
	        
	        
		  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}

	public void generatePDFForCustomerLedger(String DestinationPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		LinkedHashMap<String, String> customerDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("customerDetails");
		LinkedHashMap<String, String> totalDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("totalDetails");
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		
		
		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Customer Ledger",new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		  document.add(table);
		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
	        
	        cell = new PdfPCell(new Phrase("Party Name :- "+customerDetails.get("customer_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Mobile No :- "+customerDetails.get("mobile_number").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	                
	      
	        
	        cell = new PdfPCell(new Phrase("Date :- "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
		        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		  
		  
		  table = new PdfPTable(6);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,11,3,4,5,5});

		  
		  
		  cell = new PdfPCell(new Phrase("Transaction Date",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Transaction Type",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Ref Id",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Type",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Debit",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Credit",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	     
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
		        
	        	cell = new PdfPCell(new Phrase(prod.get("transaction_date").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        String type=prod.get("type")==null?"":prod.get("type").toString();
			        cell = new PdfPCell(new Phrase(type,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String invoice_no=prod.get("invoice_no")==null?"":prod.get("invoice_no").toString();
			        cell = new PdfPCell(new Phrase(invoice_no,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("creditDebit").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        String debitAmount=prod.get("debitAmount").toString().equals("0.00")?"":prod.get("debitAmount").toString();
			        cell = new PdfPCell(new Phrase(debitAmount,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("creditAmount").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			  
	        }
	        
	        
	        
	        
	        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		  
		        
		        cell = new PdfPCell(new Phrase("Opening Balance",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("openingAmount")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        table.addCell(cell);
		        
		        
		        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			  
			        
			        cell = new PdfPCell(new Phrase("Debit Total",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("debitSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
	        
	        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			  
			        
			        cell = new PdfPCell(new Phrase("Credit Total",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase("",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase(String.valueOf(totalDetails.get("creditSum")),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        
			       
	        
			        
				        
				        
				       
				  
				        
				        cell = new PdfPCell(new Phrase("Closing Balance As On :-"+invoiceHistoryDetails.get("toDate"),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        cell.setColspan(4);
				        table.addCell(cell);
				        
				        String totalAmount=String.valueOf(totalDetails.get("totalAmount"));
				        double d=Double.parseDouble(totalAmount);
				        String creditTotal="";
				        String debitTotal="";
				        
				        if(d>0)
				        {
				        	creditTotal=String.valueOf(d);
				        }
				        else
				        {
				        	debitTotal=String.valueOf(d);
				        }
				        
				        
				        cell = new PdfPCell(new Phrase(String.valueOf(debitTotal),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));			        
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        table.addCell(cell);
				        
				        cell = new PdfPCell(new Phrase(String.valueOf(creditTotal),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));			        
				        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				        table.addCell(cell);
				        
				        
				        
				      
	        
	        
		  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	
	
	
	public void generatePDFForSalesSummary(String DestinationPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		LinkedHashMap<String, String> customerDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("customerDetails");
		LinkedHashMap<String, String> totalDetails= (LinkedHashMap<String, String>) invoiceHistoryDetails.get("totalDetails");
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		
		
		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Sales Summary",new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		  document.add(table);
		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
	        
	        cell = new PdfPCell(new Phrase("Party Name :- "+customerDetails.get("customer_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Mobile No :- "+customerDetails.get("mobile_number").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD)));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	                
	      
	        
	        cell = new PdfPCell(new Phrase("Date :- "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
		        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		  
		  
		  table = new PdfPTable(5);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{4,11,3,4,5});

		  
		  
		  cell = new PdfPCell(new Phrase("Category Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Product Code",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Amount",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        cell = new PdfPCell(new Phrase("Quantity Sold",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	
	        
	       
	        String categoryName="";
	     
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	
	        	if(!categoryName.equals("") && !categoryName.equals(prod.get("category_name").toString()))
	        	{
	        		
	        		cell = new PdfPCell(new Phrase("",
							new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setMinimumHeight(15);
					table.addCell(cell);	        		
					table.addCell(cell);
					table.addCell(cell);
	        		
	        		String Amount=String.valueOf(totalDetails.get(categoryName+"Amount"));
	        		String Qty=String.valueOf(totalDetails.get(categoryName+"qtySold"));
	        		cell = new PdfPCell(new Phrase(Amount,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
					
					cell = new PdfPCell(new Phrase(Qty,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
	        		
					
				
					
					
					
					
					
	        		cell = new PdfPCell(new Phrase("",
							new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setMinimumHeight(15);
					table.addCell(cell);
					table.addCell(cell);
					table.addCell(cell);
					table.addCell(cell);
					table.addCell(cell);
					
	        	}
	        	
		        
				cell = new PdfPCell(new Phrase(prod.get("category_name").toString(),
						new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),
						new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(prod.get("product_code").toString(),
						new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(prod.get("Amount").toString(),
						new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				cell = new PdfPCell(new Phrase(prod.get("quantity").toString(),
						new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);

				
				
				categoryName=prod.get("category_name").toString();
			       
	        }
	        
	        
	    	cell = new PdfPCell(new Phrase("",
					new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setMinimumHeight(15);
			table.addCell(cell);	        		
			table.addCell(cell);
			table.addCell(cell);
    		
    		String Amount=String.valueOf(totalDetails.get(categoryName+"Amount"));
    		String Qty=String.valueOf(totalDetails.get(categoryName+"qtySold"));
    		cell = new PdfPCell(new Phrase(Amount,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(Qty,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);
    		
			
			cell = new PdfPCell(new Phrase("",
					new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setMinimumHeight(15);
			table.addCell(cell);	
	        
	   
			cell = new PdfPCell(new Phrase("",
					new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setMinimumHeight(15);
			table.addCell(cell);	        		
			table.addCell(cell);
			table.addCell(cell);
    		
    		String AmountSum=String.valueOf(totalDetails.get("AmountSum"));
    		String QtySum=String.valueOf(totalDetails.get("quantitySoldSum"));
    		cell = new PdfPCell(new Phrase(AmountSum,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);
			
			cell = new PdfPCell(new Phrase(QtySum,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);
    		
			
			
				      
	        
	        
		  document.add(table);
		  
		  
		 
	        
	        
		  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}
	
	 DecimalFormat f = new DecimalFormat("##.00");  

	
	public void generateCustomerItemHistory(String DestinationPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		
		
		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("ListOfItemDetails");
		
		
		
		  Document document = new Document (PageSize.A4, 20, 20, 20, 60);
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  /*PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("F:\\1.pdf"));*/
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Party Wise Item Wise Report",new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	        
		  document.add(table);
		  
		  
		  document.add(new Paragraph("\n"));
		  

		  
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
	     
		  cell = new PdfPCell(new Phrase("Date :- "+invoiceHistoryDetails.get("fromDate").toString()+" to "+invoiceHistoryDetails.get("toDate").toString()));	        	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
		        
	       
	        document.add(table);
		  
		  
	        document.add(new Paragraph("\n"));
		  
		
		  
		  
		  
		  table = new PdfPTable(10);
		  table.setWidthPercentage(100);
		  
	      table.setWidths(new int[]{10,5,20,5,5,7,7,7,10,10});

		  
	     
		  
		  cell = new PdfPCell(new Phrase("Invoice Date",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
		  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Ref",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	  
	        
	        cell = new PdfPCell(new Phrase("Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Return",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Billed Qty",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("MRP",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Rate",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Discount",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Bill Amount",new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD) ));	
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        
	        
	        String customerName="";
	        
	        
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        	if(!customerName.equals(prod.get("customer_name")))
	        	{
	        		
	        		cell = new PdfPCell(new Phrase(" ",new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
					  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					  cell.setMinimumHeight(15);
					  cell.setColspan(10);
				        table.addCell(cell);
	        	
	        		cell = new PdfPCell(new Phrase(prod.get("customer_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));
					  cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					  cell.setMinimumHeight(15);
					  cell.setColspan(10);
				        table.addCell(cell);
				       
	        	}
	        	customerName=prod.get("customer_name").toString();
	        	cell = new PdfPCell(new Phrase(prod.get("formattedInvoiceDate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
				  cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("invoice_no").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("item_name").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("qty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String qtytoreturn=prod.get("qty_to_return")==null?"":prod.get("qty_to_return").toString();
			        
			        cell = new PdfPCell(new Phrase(qtytoreturn,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("BilledQty").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        cell = new PdfPCell(new Phrase(prod.get("rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
					  
			        
			        cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
			        String discAmount=f.format(new BigDecimal(prod.get("DiscountAmount").toString()));
			        cell = new PdfPCell(new Phrase(discAmount,new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        String ItemAmount=f.format(new BigDecimal(prod.get("ItemAmount").toString()));
			        cell = new PdfPCell(new Phrase(ItemAmount,new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL) ));	
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);
			        
			        
	        } 
	        
		  document.add(table);	  
		  document.close();
		  
		  
		  
		  
		
		
		
		
	}


	public void generatePDFForInvoice3InchBattery(String DestinationPath,String BufferedImagesFolderPath,HashMap<String, Object> invoiceHistoryDetails,Connection con) throws DocumentException, MalformedURLException, IOException
	{

		List<LinkedHashMap<String, Object>> ListOfItemDetails= (List<LinkedHashMap<String, Object>>) invoiceHistoryDetails.get("listOfItems");
		
		BaseFont base = BaseFont.createFont(BufferedImagesFolderPath+"/CALIBRI.TTF", BaseFont.WINANSI, false);
		
		Font font14 = new Font(base, 14, Font.NORMAL);
		
		Font font = new Font(base, 12, Font.NORMAL); 

		Font fontnew = new Font(base, 9, Font.NORMAL); 
		Font fontBold = new Font(base, 9, Font.BOLD);

		
		int fixedSize=320;
		Rectangle pagesize = new Rectangle(250, fixedSize+ListOfItemDetails.size()*17);
		Document document = new Document(pagesize,2,16,0,2);

		  
		  PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath));
		  
		  InvoiceHistoryPDFHelper event = new InvoiceHistoryPDFHelper();
	        writer.setPageEvent(event);
		  document.open();     
		  
		  
		  PdfPTable table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        PdfPCell cell;        
	        cell = new PdfPCell(new Phrase("Invoice : "+invoiceHistoryDetails.get("invoice_no").toString(),font14));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	       
	        
	        
	        cell = new PdfPCell(new Phrase("",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);	
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("store_name")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_1")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);	        
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("address_line_2")+",",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase(invoiceHistoryDetails.get("city")+"-"+invoiceHistoryDetails.get("pincode"),font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
		  document.add(table);
  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);    
		  table.setWidths(new int[]{1});
		  
		  if(invoiceHistoryDetails.get("customer_name")!=null)
		  {
			  cell = new PdfPCell(new Phrase("Party Name :- "+invoiceHistoryDetails.get("customer_name").toString(),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);
		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
		  }
		 
 
	        cell = new PdfPCell(new Phrase("Date : "+invoiceHistoryDetails.get("theInvoiceDate").toString(),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Payment Type : "+String.valueOf(invoiceHistoryDetails.get("payment_type")),font));
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);
	        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	        table.addCell(cell);
	        
	        if(invoiceHistoryDetails.get("payment_mode")!=null)
	        {	        
		        cell = new PdfPCell(new Phrase("Payment Mode : "+String.valueOf(invoiceHistoryDetails.get("payment_mode")),font));
		        cell.setBorder(Rectangle.NO_BORDER);
		        cell.setPadding(0);

		        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		        table.addCell(cell);
	        }	        
	       
	        document.add(table);
		  
		  	table = new PdfPTable(5);
		  	table.setWidthPercentage(100);
		  
	      	table.setWidths(new int[]{6,30,12,12,12});

		  	table.setSpacingBefore(10);
	      
		  	cell = new PdfPCell(new Phrase("Sr",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
		  	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Item Name",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        cell.setPadding(0);
	        table.addCell(cell);
	           
	        cell = new PdfPCell(new Phrase("Item Qty",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	       
	        cell = new PdfPCell(new Phrase("Amount",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("Warranty (Months)",new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD) ));
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        table.addCell(cell);
	        
	        
	        
	        int srno=1;
	        Double totalQty=0d;
	        for(HashMap<String,Object> prod:ListOfItemDetails)
	        {
	        	
	        		cell = new PdfPCell(new Phrase(String.valueOf(srno++),fontnew ));
				  	cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				  	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);       
					
					Phrase p = new Phrase();
					p.add(new Chunk(prod.get("category_name").toString() +" "+ prod.get("item_name").toString(),fontBold));
					p.add(new Chunk("\n"+prod.get("vehicle_no").toString(),fontnew));
					p.add(new Chunk("\n"+prod.get("battery_no").toString(),fontnew));
					p.add(new Chunk("\n"+prod.get("vehicle_name").toString(),fontnew));

			        cell = new PdfPCell(p);
			        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
			        
			        totalQty+=Double.parseDouble(prod.get("qty").toString());
					String[] parts = prod.get("qty").toString().split("\\.");
			        cell = new PdfPCell(new Phrase(parts[0],fontnew ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);			        
			        
			        cell = new PdfPCell(new Phrase(prod.get("custom_rate").toString(),fontnew ));
			        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        table.addCell(cell);

					cell = new PdfPCell(new Phrase(prod.get("warranty").toString(),fontnew ));
			        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			        cell.setPaddingLeft(3);
			        table.addCell(cell);
	        }
	        
	        double itemdiscount=Double.parseDouble(invoiceHistoryDetails.get("item_discount").toString());
	        double invoicediscount=Double.parseDouble(invoiceHistoryDetails.get("invoice_discount").toString());
	        
	        if(itemdiscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Item Discount : "+String.valueOf(invoiceHistoryDetails.get("item_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoicediscount>0)
	        {
		        cell = new PdfPCell(new Phrase("Invoice Discount : "+String.valueOf(invoiceHistoryDetails.get("invoice_discount")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
	        
	        if(invoiceHistoryDetails.get("total_gst")!=null && !invoiceHistoryDetails.get("total_gst").equals("0.00"))
	        {
		        cell = new PdfPCell(new Phrase("Gst : "+String.valueOf(invoiceHistoryDetails.get("total_gst")),new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(5);
		        table.addCell(cell);
	        }
		        
		        
		        cell = new PdfPCell(new Phrase("Total Qty :  "+totalQty.toString(),fontnew ));
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(2);
		        table.addCell(cell);			        
		        
		        String totalAmount=String.format("%.0f", Double.valueOf(invoiceHistoryDetails.get("total_amount").toString())) ;
		        
		        
		        cell = new PdfPCell(new Phrase("Total Amount : "+String.valueOf(totalAmount+"/-"),new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD) ));	
		        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		        cell.setColspan(3);
		        table.addCell(cell);  

		        if(invoiceHistoryDetails.get("payment_type").equals("Partial"))
		        {
		        	cell = new PdfPCell(new Phrase(String.valueOf("Partially Paid Amount : "+String.valueOf(invoiceHistoryDetails.get("paid_amount"))),font ));
				  	cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				  	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				  	cell.setColspan(5);
			        table.addCell(cell);
		        }
			      
				        
				cell = new PdfPCell(new Phrase(String.valueOf("Remarks : "+String.valueOf(invoiceHistoryDetails.get("remarks"))),fontnew ));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);				  
				cell.setColspan(5);
				table.addCell(cell);
					             
		  document.add(table);
		  
		  document.add(new Paragraph("\n"));
		  
		  table = new PdfPTable(1);
		  table.setWidthPercentage(100);
	        
	        cell = new PdfPCell(new Phrase("***********************************",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        cell.setPadding(0);	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	        
	        cell = new PdfPCell(new Phrase("*Thank you, Visit Again*",font));	        
	        cell.setBorder(Rectangle.NO_BORDER);
	        	        
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(cell);
	         
		  document.add(table);     	        
		  
		  document.close();	
				
	}
	

	
	
	
		
	
}