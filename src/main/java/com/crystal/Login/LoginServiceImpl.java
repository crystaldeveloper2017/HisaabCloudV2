package com.crystal.Login;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileUploadException;

import com.crystal.customizedpos.Configuration.ConfigurationDaoImpl;
import com.crystal.Frameworkpackage.CommonFunctions;
import com.crystal.Frameworkpackage.CustomResultObject;

public class LoginServiceImpl extends CommonFunctions {

	public LoginServiceImpl() {

	}

	public CustomResultObject validateLogin(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		String returnString = "";

		try {
			String Username = request.getParameter("txtusername");
			String projectName = CommonFunctions.projectName;
			String Password = request.getParameter("txtpassword");
			LoginDaoImpl lObjLoginDao = new LoginDaoImpl();
			HashMap<String, String> loginDetails = lObjLoginDao.validateLoginUSingJDBC(Username, Password, con);
			if (loginDetails != null && !loginDetails.isEmpty() ) {
				Long user_id = Long.valueOf(loginDetails.get("user_id").toString());				
				List<String> roles = lObjLoginDao.getRoles(user_id, con);				
				request.getSession().setAttribute("username", Username);
				request.getSession().setAttribute("userdetails", loginDetails);				
				boolean isAdmin=roles.contains("SuperAdmin") || roles.contains("Admin") || roles.contains("AdminServices") || roles.contains("AdminJwellery") ||roles.contains("AdminFuel")||roles.contains("AdminSnacks");
				request.getSession().setAttribute("adminFlag", isAdmin);			
				request.getSession().setAttribute("projectName", projectName);				
				copyImagesFromDBToBufferFolder(request.getServletContext(),con);
				returnString = "Succesfully Logged In";
			} else {
				returnString = "Invaid Username or password";
			}
		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		rs.setAjaxData(returnString);
		return rs;
	}

	



	public CustomResultObject Logout(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			request.getSession().invalidate();
			request.getSession().setAttribute("username", null);
			rs.setAjaxData("Logged Out Succesfully");
		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}
	ConfigurationDaoImpl lObjConfiguration = new ConfigurationDaoImpl();
	
	public CustomResultObject showHomePage(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<String, Object>();
		
		try {
			
			
			Long app_id=Long.valueOf(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id"));
			outputMap.put("app_id", app_id);
			
			outputMap.put("nozzleDetails", lObjConfiguration.getNozzles(outputMap, con).get("count(*)"));
			
			outputMap.put("dispenserDetails", lObjConfiguration.getDispensers(outputMap, con).get("count(*)"));
			
			outputMap.put("customerDetails", lObjConfiguration.getCustomers(outputMap, con).get("count(*)"));
			
			outputMap.put("invoiceDetails", lObjConfiguration.getSalesInvoices(outputMap, con).get("count(*)"));
			
			outputMap.put("vehicleDetails", lObjConfiguration.getVehicles(outputMap, con).get("count(*)"));
			
			outputMap.put("categoryDetails", lObjConfiguration.getCategoryCount(outputMap, con).get("count(*)"));
			
			outputMap.put("itemDetails", lObjConfiguration.getItems(outputMap, con).get("count(*)"));

			boolean adminFlag = (Boolean) request.getSession().getAttribute("adminFlag");

			
			String userId=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			outputMap.putAll(lObjConfiguration.getUserConfigurations(userId, con));
			
			
			String appType=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");
			
			
			
			if(appType.equals("Retail") || appType.equals("Master") || appType.equals("Jwellery") || appType.equals("RetailMobile") || appType.equals("Battery"))
					{
						outputMap.putAll(getRetailDashboardData(request, con,outputMap));					
					
					rs.setViewName("../RetailDashboard.jsp");
				
					}			
				else
					if(appType.equals("PetrolPump"))
							{
						
							rs.setViewName("../PetrolDashboard.jsp");
						
							}

							else
					if(appType.equals("PetrolPumpMini"))
							{
						
							rs.setViewName("../PetrolMiniDashboard.jsp");
						
							}

							if(appType.equals("SnacksProduction"))
							{							
								if(adminFlag)
								{
									outputMap.put("user_id", null);
								}
								outputMap.put("pendingCount", lObjConfiguration.getPendingOrdersCount(outputMap, con).get("count(*)"));
								outputMap.put("todaysPlanningCount", lObjConfiguration.getPlanningCount(outputMap, con).get("count(*)"));
								outputMap.put("completedCount", lObjConfiguration.getCompletedCount(outputMap, con).get("count(*)"));
								outputMap.put("todaysStock", lObjConfiguration.getTodaysStockCount(outputMap, con).get("todaysStock"));
								outputMap.put("rmstock", lObjConfiguration.getRMStock(outputMap, con).get("todaysStock"));


							rs.setViewName("../SnacksProductionDashboard.jsp");
						
							}
			
					else
						if(appType.equals("Transport"))
								{
							
								rs.setViewName("../TransportDashboard.jsp");
							
								}
			if(appType.equals("Restaurant"))
			{
				outputMap.putAll(getRetailDashboardData(request, con,outputMap));					
				rs.setViewName("../RestaurantDashboard.jsp");
		
			}

			if(appType.equals("RestaurantDemo"))
			{
				outputMap.putAll(getRetailDashboardData(request, con,outputMap));					
				rs.setViewName("../RestaurantDashboard.jsp");
		
			}
		}
		catch(Exception e)
		{
			writeErrorToDB(e);
			e.printStackTrace();
		}
			rs.setReturnObject(outputMap);
			return rs;
				
			
	}
	
	public HashMap<String, Object> getRetailDashboardData(HttpServletRequest request, Connection con,HashMap<String, Object> outputMap) throws SQLException, ClassNotFoundException, ParseException
	{
		
		if(Boolean.valueOf(request.getSession().getAttribute("adminFlag").toString()) == false)
		{
			Long storeId=Long.valueOf(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));
			outputMap.put("store_id", storeId);
		}
		
		
		if (request.getSession().getAttribute("username") != null) {
			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");

			if (fromDate == null || fromDate.equals("") || toDate == null || toDate.equals("")) {
				fromDate = getDateFromDB(con);
				toDate = fromDate;
			}

			outputMap.put("fromDate", fromDate);
			outputMap.put("toDate", toDate);

			
			outputMap.putAll(lObjConfiguration.getDataForHomepage(outputMap,con));

			List<LinkedHashMap<String, Object>> paymentData = lObjConfiguration.getPaymentData(outputMap, con);
			String[] paymentColnames = { "Cash", "Paytm", "Amazon", "GooglePay", "PhonePay", "Zomato",
					"Swiggy", "Card", "HoriTotal" ,"Kasar"};
			outputMap.put("paymentData", paymentData);
			outputMap.put("paymentDataTotal", calculateTotal(paymentData, paymentColnames));

			
			
			List<LinkedHashMap<String, Object>> paymentDataAgainstCollection = lObjConfiguration.getPaymentDataAgainstCollection(outputMap, con);
			String[] paymentAgainstCollectionColnames = { "Cash", "Paytm", "Amazon", "GooglePay", "PhonePay", "Zomato",
					"Swiggy", "Card", "HoriTotal","Kasar" };
			outputMap.put("PaymentDataAgainstCollection", paymentDataAgainstCollection);
			outputMap.put("PaymentDataAgainstCollectionTotal", calculateTotal(paymentDataAgainstCollection, paymentAgainstCollectionColnames));		
			
			
			
			
			List<LinkedHashMap<String, Object>> statisticalData = lObjConfiguration.getStatisticalSalesData(outputMap, con);
			String[] statisticalDataColnames = { "gross_amount", "discount", "returnedAmount", "total_amount", "paid", "pending", "profit","Count"};
			outputMap.put("statisticalData", statisticalData);
			outputMap.put("statisticalDataTotal", calculateTotal(statisticalData, statisticalDataColnames));		

			List<LinkedHashMap<String, Object>> paymentDataAgainstSales = lObjConfiguration.getPaymentDataAgainstSales(outputMap, con);
			String[] paymentDataAgainstSalesColnames = { "Cash", "Paytm", "Amazon", "GooglePay", "PhonePay", "Zomato",
					"Swiggy", "Card", "HoriTotal" };
			outputMap.put("paymentDataAgainstSales", paymentDataAgainstSales);
			outputMap.put("paymentDataAgainstSalesTotal", calculateTotal(paymentDataAgainstSales, paymentDataAgainstSalesColnames));
			
			
			List<LinkedHashMap<String, Object>> employeeData = lObjConfiguration.getEmployeeWiseDetailsForDashboard(outputMap, con);
			String[] EmployeeWiseDetailsForDashboardColnames = { "Cash", "Paytm", "Amazon", "GooglePay", "PhonePay", "Zomato",
					"Swiggy", "Card", "HoriTotal","pendingAmount" };
			outputMap.put("employeeData", employeeData);
			outputMap.put("employeeDataTotal", calculateTotal(employeeData, EmployeeWiseDetailsForDashboardColnames ));
			
			
			List<LinkedHashMap<String, Object>> bookingData = lObjConfiguration.getBookingDetailsForStore(outputMap, con);				
			outputMap.put("bookingData", bookingData);
			//outputMap.put("employeeDataTotal", calculateTotal(employeeData, EmployeeWiseDetailsForDashboardColnames ));
			
			List<LinkedHashMap<String, Object>> expenseData = lObjConfiguration.getExpenseDetailsForStore(outputMap, con);				
			outputMap.put("expenseData", expenseData);				
			
			
			
			
			
			
			

			request.getSession().setAttribute("franchiseFlag", true);
			
			
			

			outputMap.put("todaysDate", getDateFromDB(con));
			
		} 
	
	return outputMap;

	}

	private HashMap<String, Object> calculateTotal(List<LinkedHashMap<String, Object>> paymentData,
			String[] columnNames) {
		HashMap<String, Object> calculatedDetails = new HashMap<>();
		for (HashMap<String, Object> hm : paymentData) {
			for (String s : columnNames) {
				if (calculatedDetails.get(s) == null) {
					calculatedDetails.put(s, hm.get(s));
				} else {
					BigDecimal Existingvalue = new BigDecimal(calculatedDetails.get(s).toString());
					
					String m=hm.get(s)==null?"0":hm.get(s).toString();
					BigDecimal newValue = new BigDecimal(m);
					newValue=newValue.add(Existingvalue) ;
					calculatedDetails.put(s, newValue);
				}
			}
		}
		return calculatedDetails;
	}

	public CustomResultObject showChangePassword(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			rs.setViewName("changePassword.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	

	public CustomResultObject changePassword(HttpServletRequest request, Connection con) throws FileUploadException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String oldPassword = URLDecoder.decode(request.getParameter("oldPassword"), "UTF-8");
			String newPassword = URLDecoder.decode(request.getParameter("newPassword"), "UTF-8");

			String username = request.getSession().getAttribute("username").toString();
			LoginDaoImpl loginDao = new LoginDaoImpl();
			HashMap<String, String> loginDetails = loginDao.validateLoginUSingJDBC(username, oldPassword, con);
			String message = "";
			if (loginDetails != null) {
				loginDao.changePassword(username, newPassword, con);
				message = "Password Changed Succesfully ";
			} else {
				message = "Invalid Old Password";
			}

			rs.setReturnObject(outputMap);
			rs.setAjaxData(message);

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

}
