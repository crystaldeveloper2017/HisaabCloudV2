package com.crystal.customizedpos.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.crystal.CustomExceptions.CustomerMobileAlreadyExist;
import com.crystal.Frameworkpackage.CommonFunctions;
import com.crystal.Frameworkpackage.CustomResultObject;
import com.crystal.Frameworkpackage.Role;
import com.crystal.Login.LoginDaoImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import pdfGeneration.InvoiceHistoryPDFHelper;
import pdfGeneration.NozzleRegisterPDFHelper;
import pdfGeneration.QuotePDFHelper;

public class ConfigurationServiceImpl extends CommonFunctions {
	ConfigurationDaoImpl lObjConfigDao = new ConfigurationDaoImpl();
	String filename_constant = "FileName";
	static Logger logger = Logger.getLogger(CommonFunctions.class.getName());

	public CustomResultObject getItemDetailsById(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long storeId = Long.parseLong(
				((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));

		long itemId = Integer.parseInt(request.getParameter("itemId"));
		HashMap<String, Object> outputMap = new HashMap<>();
		outputMap.put("store_id", storeId);
		outputMap.put("item_id", itemId);

		try {
			rs.setAjaxData(mapper.writeValueAsString(lObjConfigDao.getItemDetailsById(outputMap, con)));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addRemoveRole(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long userId = Long.parseLong(request.getParameter("userId"));
		String action = request.getParameter("action");
		String[] listOfRoles = request.getParameter("listOFRoles").split("~");
		String app_type = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");

		try {

			LinkedHashMap<Long, Role> roleMasterMap = cf.getRoleMasterForThisAppType(app_type);

			if (action.equals("0")) {
				for (String s : listOfRoles) {
					lObjConfigDao.removeRoleFromUser(userId, Long.valueOf(s), con);
				}
			} else {
				for (String s : listOfRoles) {
					if (!lObjConfigDao.checkIfRoleUserAlreadyExist(userId, Long.valueOf(s), con)) {
						lObjConfigDao.addUserRoleMapping(userId, Long.valueOf(s),
								roleMasterMap.get(Long.valueOf(s)).getRoleName(), con);
					}
				}
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setAjaxData("Roles Updated Succesfully");
		return rs;
	}

	public CustomResultObject getPendingAmountForCustomer(HttpServletRequest request, Connection con)
			throws SQLException {

		CustomResultObject rs = new CustomResultObject();

		try {
			rs.setAjaxData(mapper.writeValueAsString(getPendingAmountForCustomerService(request, con)));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showPendingOrdersAjax(HttpServletRequest request, Connection conWithF)
			throws ClassNotFoundException, SQLException, JsonProcessingException {

		CustomResultObject rs = new CustomResultObject();

		HashMap<String, Object> outputMap = new HashMap<>();

		String table_id = request.getParameter("table_id");
		outputMap.put("table_id", table_id);

		int storeId = Integer
				.parseInt(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));
		outputMap.put("store_id", storeId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPendingOrders(outputMap, conWithF);
		int i = 1;
		for (LinkedHashMap<String, Object> hm : lst) {
			hm.put("SrNo", i++);
		}
		outputMap.put("pendingItems", lst);
		outputMap.put("tableList", lObjConfigDao.getListOfTablesForOnGoingOrders(storeId, conWithF));

		rs.setAjaxData(mapper.writeValueAsString(outputMap));

		return rs;

	}

	public HashMap<String, Object> getPendingAmountForCustomerService(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long customerId = Integer.parseInt(request.getParameter("customerId"));
		HashMap<String, Object> outputMap = new HashMap<>();
		HashMap<String, Object> returnMap = new HashMap<>();
		
		try {
			String fromDate = "";
			String toDate = "";
			returnMap.put("pendingAmountDetails", lObjConfigDao.getPendingAmountForThisCustomer(customerId, fromDate, toDate, con));
			returnMap.put("LastMonthSalesDetails", lObjConfigDao.getLastMonthSalesForThisCustomer(customerId,  con));
			outputMap.put("reqData",returnMap);
			
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			e.printStackTrace();
			rs.setHasError(true);
		}
		return outputMap;
	}

	public HashMap<String, Object> getRoutineDetailsForThisCustomer(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long customerId = Integer.parseInt(request.getParameter("customerId"));
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			String fromDate = "";
			String toDate = "";
			outputMap.put("routineDetails",
					lObjConfigDao.getPendingAmountForThisCustomer(customerId, fromDate, toDate, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return outputMap;
	}

	public CustomResultObject savePayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long customerId = Integer.parseInt(request.getParameter("customerId"));
		String date = (request.getParameter("txtdate"));
		Double toPayAmount = Double.parseDouble(request.getParameter("payAmount"));
		String paymentMode = request.getParameter("paymentMode");
		String remarks = request.getParameter("remarks");

		String appId = request.getParameter("app_id");
		String userId = request.getParameter("user_id");
		HashMap<String, Object> hm = new HashMap<>();

		hm.put("app_id", appId);

		hm.put("user_id", userId);

		hm.put("customer_id", customerId);

		hm.put("payment_mode", paymentMode);
		hm.put("total_amount", toPayAmount);
		hm.put("payment_type", "Paid");
		hm.put("store_id", request.getParameter("store_id"));
		hm.put("payment_for", "Collection");
		hm.put("remarks", remarks);

		if (paymentMode.equals("")) {
			hm.put("payment_type", "Debit");
			hm.put("total_amount", toPayAmount * -1);
			hm.put("payment_for", "Debit Entry");
			hm.put("remarks", remarks);
		}

		try {
			hm.put("invoice_date", date);
			String retMessage = lObjConfigDao.addPaymentFromCustomer(hm, con);
			hm.put("returnMessage", retMessage);
			rs.setAjaxData(mapper.writeValueAsString(hm));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveCompositeItemDetails(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();

		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();

			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					itemListRequired.add(itemDetailsMap);
					// ID, QTY
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}
		lObjConfigDao.deleteCompositeItem(hm, con);
		for (HashMap<String, Object> item : itemListRequired) {
			item.put("parentItemId", hm.get("parentItemId"));
			lObjConfigDao.saveCompositeItem(item, con);
		}
		rs.setReturnObject(hm);
		rs.setAjaxData("Item Updated Succesfully");
		return rs;
	}

	public CustomResultObject getItemDetailsByAjax(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();

		try {

			rs.setAjaxData(mapper.writeValueAsString(getItemDetailsByAjaxService(request, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public LinkedHashMap<String, String> getItemDetailsByAjaxService(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		String itemId = request.getParameter("itemId");
		LinkedHashMap<String, String> outputMap = new LinkedHashMap<>();
		String storeId = request.getParameter("sourceStoreId");
		String destinationStoreId = request.getParameter("destinationStoreId");
		String customerId = request.getParameter("customerId");
		try {

			outputMap = lObjConfigDao.getItemdetailsByIdForStore(customerId, itemId, storeId, destinationStoreId, con);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return outputMap;

	}

	public CustomResultObject searchForCustomer(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		String searchString = request.getParameter("searchString");

		HashMap<String, Object> outputMap = new HashMap<>();
		outputMap.put("searchString", searchString);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {

			rs.setAjaxData(mapper.writeValueAsString(lObjConfigDao.getCustomerList(outputMap, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCategoryMasterNew(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "categoryId", "categoryName" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCategoryMasterWithItemsCount(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfCategories", lst);
				rs.setViewName("../Category.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showBookingsRegister(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");

		try {

			String[] colNames = { "categoryId", "categoryName" };
			outputMap.put("app_id", appId);

			boolean isAdmin = (boolean) request.getSession().getAttribute("adminFlag");

			if (!isAdmin) {
				outputMap.put("store_id", storeId);
			}

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");

			if (fromDate == null || fromDate.equals("")) {
				fromDate = getDateFromDB(con);
			}

			if (toDate == null || toDate.equals("")) {
				toDate = getDateFromDB(con);
			}
			outputMap.put("fromDate", (fromDate));
			outputMap.put("toDate", (toDate));
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getBookingRegister(outputMap, con);

			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfBookings", lst);
				rs.setViewName("../BookingsMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showCustomerGroup(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			String[] colNames = { "categoryId", "categoryName" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerGroup(appId, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerGroups");
			} else {
				outputMap.put("ListofGroups", lst);
				rs.setViewName("../CustomerGroup.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showExpenseEntry(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		String sessionStoreId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
				.get("store_id");
		try {

			String[] colNames = { "expense_name", "amount", "qty", "expense_date" };

			String fromDate = request.getParameter("fromDate");
			String toDate = request.getParameter("toDate");
			String paramStoreId = request.getParameter("store_id");

			outputMap.put("listStoreData", lObjConfigDao.getStoreMaster(outputMap, con));

			if (fromDate == null || fromDate.equals("")) {
				fromDate = getDateFromDB(con);
			}

			if (toDate == null || toDate.equals("")) {
				toDate = getDateFromDB(con);
			}

			outputMap.put("fromDate", (fromDate));
			outputMap.put("toDate", (toDate));
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));

			boolean isAdmin = (boolean) request.getSession().getAttribute("adminFlag");
			if (!isAdmin) {
				outputMap.put("store_id", sessionStoreId);
			} else {
				outputMap.put("store_id", paramStoreId);

			}

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getExpenseRegister(outputMap, con);

			outputMap.put("totalAmount", getTotalAmountExpenditure(lst));

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "Expenses");
			} else {
				outputMap.put("ListofExpense", lst);
				rs.setViewName("../ExpenseRegister.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showCustomerDeliveryRoutine(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			String[] colNames = { "categoryId", "categoryName" };

			outputMap.put("customer_id", request.getParameter("customerId"));
			outputMap.put("ListOfCustomers", lObjConfigDao.getCustomerMaster(outputMap, con));

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerDeliveryRoutine(con, outputMap);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerGroups");
			} else {
				outputMap.put("ListOfRoutines", lst);
				rs.setViewName("../Routines.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showItemMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		outputMap.put("store_id", storeId);

		try {
			String[] colNames = { "item_id", "item_name", "debit_in", "product_code", "price", "category_name" };

			outputMap.put("categoryId", request.getParameter("categoryId"));
			outputMap.put("searchInput", request.getParameter("searchInput"));

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMaster(outputMap, con);

			for (HashMap<String, Object> tempHm : lst) {
				for (String s : colNames) {
					if (s.equalsIgnoreCase("debit_in")) {
						if (tempHm.get(s).toString().equalsIgnoreCase("W"))
							tempHm.put(s, "Weight");
						else
							tempHm.put(s, "Quantity");
					}
				}
			}

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "ItemMaster");
			} else {
				outputMap.put("ListOfItems", lst);
				outputMap.put("ListOfCategories", lObjConfigDao.getCategories(outputMap, con));
				rs.setViewName("../ItemMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject deleteCategory(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long categoryId = Integer.parseInt(request.getParameter("categoryid"));

		try {
			if (!lObjConfigDao.ProductExistForThisCategory(categoryId, con)) {
				rs.setAjaxData(lObjConfigDao.deleteCategory(categoryId, con));
			} else {
				rs.setAjaxData("Cannot Delete as Items Exist with this category");
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject acceptPhonePayPayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String orderId = (request.getParameter("orderId"));

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {

			HashMap<String, String> hm = lObjConfigDao.getAccountinDateAndShiftId(userId, con);
			if (hm.isEmpty()) {
				rs.setAjaxData("You are not checked into any nozzle");
			} else {
				lObjConfigDao.updatePhonePayPayment(orderId, hm.get("niceDate"), hm.get("shift_id"), con);
				rs.setAjaxData("Succesfully Added Payment");
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteBooking(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long bookingid = Integer.parseInt(request.getParameter("bookingid"));
		String appId = (request.getParameter("app_id"));
		String userId = (request.getParameter("user_id"));
		HashMap<String, Object> outputMap = new HashMap<>();

		outputMap.put("app_id", appId);

		outputMap.put("user_id", userId);
		outputMap.put("booking_id", bookingid);
		try {

			rs.setAjaxData(lObjConfigDao.deleteBooking(outputMap, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showItems(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			outputMap.put("ListOfItems", lObjConfigDao.showItems(outputMap, con));

			rs.setViewName("../ShowItems.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject H(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = request.getParameter("appId");
		outputMap.put("app_id", appId);
		try {

			outputMap.put("ListOfCategories", lObjConfigDao.CategoryNameWithImage(Long.valueOf(appId), con));
			outputMap.put("sliderImages", lObjConfigDao.getSliderImages(Long.valueOf(appId), con));
			outputMap.put("popularProducts", lObjConfigDao.getPopularItems(appId, con));
			outputMap.put("logoDetails", lObjConfigDao.getLogoDetails(Long.valueOf(appId), con));

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, con);
			HashMap<Object, Object> reqHm = new HashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";
			for (LinkedHashMap<String, Object> temp : lst) {
				if (categoryName.equals(""))
					categoryName = temp.get("category_name").toString();
				if (categoryName.equals(temp.get("category_name"))) {
					lsttemp.add(temp);
				} else {
					LinkedHashMap<String, Object> hm1 = lsttemp.get(0);
					hm1.put("categoryName", categoryName);
					reqHm.put(hm1, lsttemp);

					lsttemp = new ArrayList<>();
					categoryName = temp.get("category_name").toString();
					lsttemp.add(temp);
				}
			}
			outputMap.put("categoriesWithItem", reqHm);
			outputMap.put("appDetails", lObjConfigDao.getAboutUsDetails(Long.valueOf(appId), con));
			outputMap.put("logoDetails", lObjConfigDao.getLogoDetails(Long.valueOf(appId), con));

			outputMap.put("jspNameGuest", "guestindex.jsp");

			rs.setViewName("../modelGuest.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCategoryForGuests(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		long appId = Long.valueOf(request.getParameter("appId"));
		String categoryId = (request.getParameter("categoryId"));
		String searchString = request.getParameter("searchString");
		outputMap.put("category_id", categoryId);
		outputMap.put("app_id", appId);
		try {

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, con);
			HashMap<Object, Object> reqHm = new HashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";
			for (LinkedHashMap<String, Object> temp : lst) {
				if (categoryName.equals(""))
					categoryName = temp.get("category_name").toString();
				if (categoryName.equals(temp.get("category_name"))) {
					lsttemp.add(temp);
				} else {
					LinkedHashMap<String, Object> hm1 = lsttemp.get(0);
					hm1.put("categoryName", categoryName);
					reqHm.put(hm1, lsttemp);

					lsttemp = new ArrayList<>();
					categoryName = temp.get("category_name").toString();
					lsttemp.add(temp);
				}
			}
			outputMap.put("categoriesWithItem", reqHm);
			if (categoryId != null) {
				outputMap.put("itemsByCategoryId", lObjConfigDao.getProductsByCategoryId(appId, categoryId, con));
			}

			if (searchString != null && !searchString.equals("")) {
				outputMap.put("itemsByCategoryId", lObjConfigDao.getProductsForSearch(appId, searchString, con));
			}

			outputMap.put("categoryDetails", lObjConfigDao.getCategoryDetails(outputMap, con));
			outputMap.put("appDetails", lObjConfigDao.getAboutUsDetails(Long.valueOf(appId), con));
			outputMap.put("logoDetails", lObjConfigDao.getLogoDetails(Long.valueOf(appId), con));

			outputMap.put("jspNameGuest", "category-full.jsp");
			rs.setViewName("../modelGuest.jsp");

			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showItemDetailForGuest(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		long appId = Long.valueOf(request.getParameter("appId"));
		long itemId = Long.valueOf(request.getParameter("itemId"));
		outputMap.put("item_id", itemId);
		outputMap.put("app_id", appId);
		try {

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, con);
			HashMap<Object, Object> reqHm = new HashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";
			for (LinkedHashMap<String, Object> temp : lst) {
				if (categoryName.equals(""))
					categoryName = temp.get("category_name").toString();
				if (categoryName.equals(temp.get("category_name"))) {
					lsttemp.add(temp);
				} else {
					LinkedHashMap<String, Object> hm1 = lsttemp.get(0);
					hm1.put("categoryName", categoryName);
					reqHm.put(hm1, lsttemp);

					lsttemp = new ArrayList<>();
					categoryName = temp.get("category_name").toString();
					lsttemp.add(temp);
				}
			}
			outputMap.put("categoriesWithItem", reqHm);

			LinkedHashMap<String, Object> itemDetails = lObjConfigDao.getItemdetailsById(outputMap, con);
			outputMap.put("itemDetails", lObjConfigDao.getItemdetailsById(outputMap, con));
			outputMap.put("relatedItems",
					lObjConfigDao.getRelatedItems(appId, itemDetails.get("category_id").toString(), con, 3, itemId));

			outputMap.put("appDetails", lObjConfigDao.getAboutUsDetails(Long.valueOf(appId), con));
			outputMap.put("logoDetails", lObjConfigDao.getLogoDetails(Long.valueOf(appId), con));

			outputMap.put("jspNameGuest", "detail.jsp");
			rs.setViewName("../modelGuest.jsp");

			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showSliderImages(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			outputMap.put("sliderImages", lObjConfigDao.getSliderImages(Long.valueOf(appId), con));
			rs.setViewName("../sliderImages.jsp");

			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showThisCategoryforH(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		long appId = Long.valueOf(request.getParameter("appId"));
		String categoryId = (request.getParameter("categoryId"));
		outputMap.put("app_id", appId);
		try {

			outputMap.put("itemsForThisCategory", lObjConfigDao.getProductsByCategoryId(appId, categoryId, con));

			rs.setViewName("../obaju/category-full.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showPrintLabelsScreen(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));
			rs.setViewName("../PrintLabels.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddBooking(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long mobile_booking_id = request.getParameter("mobile_booking_id") == null ? 0L
				: Long.parseLong(request.getParameter("mobile_booking_id"));

		try {

			if (mobile_booking_id != 0) {
				outputMap.put("mobileBookingDetails",
						lObjConfigDao.getMobileBookingDetailsById(String.valueOf(mobile_booking_id), con));
			}

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);

			String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("store_id");
			outputMap.put("store_id", storeId);

			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));

			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("tentativeSerialNo",
					lObjConfigDao.getTentativeSequenceNo(appId, "trn_booking_register", con));
			outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
			outputMap.put("EmployeeList", lObjConfigDao.getEmployeeMasterForStore(outputMap, con));
			outputMap.put("modelList", lObjConfigDao.getModelList(outputMap, con));
			outputMap.put("todaysdate", cf.getDateTimeWithoutSeconds(con));

			rs.setViewName("../AddNewBooking.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAuditTrail(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String username = request.getParameter("username");
			String schemaname = request.getParameter("schemaname");
			if (username != null && !username.equals("")) {
				outputMap.put("auditList", lObjConfigDao.getAuditListByUserAndSchema(username, schemaname, con));
			}
			outputMap.put("memoryStats", cf.getMemoryStats());
			outputMap.put("activeConnections", cf.getActiveConnections(con));
			outputMap.put("latestUserHits", lObjConfigDao.getLastestUserHits(con));

			rs.setViewName("../AuditTrail.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showPrintLabelsScreenVehicle(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);
			outputMap.put("ListOfVehicles", lObjConfigDao.getVehicleMaster(outputMap, con));
			rs.setViewName("../PrintLabelsVehicle.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showInventoryCounting(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		String stockModificationId = request.getParameter("stockModificationId");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {

			if (stockModificationId != null) {
				outputMap.put("stockModificationDetails",
						lObjConfigDao.getStockModificationDetailsInventoryCounting(stockModificationId, con));
			}

			outputMap.put("storeId", storeId);
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));
			outputMap.put("inventoryCountingList", lObjConfigDao.getInventoryCountingListForThisStore(outputMap, con));
			rs.setViewName("../InventoryCounting.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showStockStatus(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String categoryId = request.getParameter("categoryId");
		outputMap.put("categoryId", categoryId);

		String storeId = request.getParameter("storeId");
		outputMap.put("storeId", storeId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			String[] colNames = { "stock_id", "store_name", "item_name", "qty_available" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStockStatus(outputMap, con);
			// LinkedHashMap<String, Object> totalDetails =
			// gettotalDetailsStockEvaluation(lst);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "StockStatus");
			} else {
				outputMap.put("ListStock", lst);
				outputMap.put("ListOfCategories", lObjConfigDao.getCategories(outputMap, con));
				outputMap.put("listOfStore", lObjConfigDao.getStoreMaster(outputMap, con));
				// outputMap.put("totalDetails", totalDetails);

				rs.setViewName("../StockStatus.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	String delimiter = "/";

	public CustomResultObject showStockModifications(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String storeId = request.getParameter("storeId");
		outputMap.put("storeId", storeId);

		try {

			String[] colNames = { "stock_id", "store_name", "item_name", "qty_available" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStockModifications(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "StockStatus");
			} else {
				outputMap.put("ListStockModifications", lst);
				outputMap.put("listOfStore", lObjConfigDao.getStoreMaster(outputMap, con));
				rs.setViewName("../StockModifications.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject updateLowStock(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long stock_id = Long.parseLong(request.getParameter("stock_id"));
		long lowqty = Long.parseLong(request.getParameter("lowqty"));
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			lObjConfigDao.updateLowStockDetails(stock_id, lowqty, con);
			rs.setAjaxData("<script>alert('Low Stock Configured Succesfully');window.location='"
					+ request.getParameter("callerUrl") + "?a=showStockStatus';</script>");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject transferStock(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long fromStoreId = Long.parseLong(request.getParameter("drpfromstore"));
		long toStoreId = Long.parseLong(request.getParameter("drptostore"));
		String outerremarks = request.getParameter("outerremarks");
		String userId = request.getParameter("user_id");
		String appId = request.getParameter("app_id");

		String[] listOfItems = request.getParameter("itemDetails").split("\\|");
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			outputMap.put("userId", userId);
			outputMap.put("storeId", fromStoreId);
			outputMap.put("outerRemarks", outerremarks);

			String StockModificationType = "StockTransfer";
			outputMap.put("type", StockModificationType);

			outputMap.put("app_id", appId);
			long stockModificationId = lObjConfigDao.addStockModification(outputMap, con);

			for (String item : listOfItems) {
				String[] itemdetails = item.split("~");
				long itemId = Long.parseLong(itemdetails[0]);
				String sourceBefore = (itemdetails[1]);
				String sourceAfter = (itemdetails[2]);
				double qtytotransfer = Double.parseDouble(itemdetails[3]);
				String destinationBefore = (itemdetails[4]);
				String destinationAfter = (itemdetails[5]);

				outputMap.put("stockModificationId", stockModificationId);
				outputMap.put("itemId", itemId);
				outputMap.put("sourcebefore", sourceBefore);
				outputMap.put("sourceafter", sourceAfter);
				outputMap.put("qty", qtytotransfer);
				outputMap.put("destinationbefore", destinationBefore);
				outputMap.put("destinationafter", destinationAfter);
				outputMap.put("sourceStore", fromStoreId);
				outputMap.put("destinationStore", toStoreId);
				lObjConfigDao.saveStockModificationtransferStock(outputMap, con);

				String fromStockId = lObjConfigDao.checkifStockAlreadyExist(fromStoreId, itemId, con);
				if (fromStockId.equals("0")) {
					HashMap<String, Object> stockDetails = new HashMap<>();
					stockDetails.put("drpstoreId", fromStoreId);
					stockDetails.put("drpitems", itemId);
					stockDetails.put("qty", 0);
					fromStockId = String.valueOf(lObjConfigDao.addStockMaster(stockDetails, con));
				}
				String toStockId = lObjConfigDao.checkifStockAlreadyExist(toStoreId, itemId, con);
				if (toStockId.equals("0")) {
					HashMap<String, Object> stockDetails = new HashMap<>();
					stockDetails.put("drpstoreId", toStockId);
					stockDetails.put("drpitems", itemId);
					stockDetails.put("qty", 0);
					toStockId = String.valueOf(lObjConfigDao.addStockMaster(stockDetails, con));
				}

				outputMap.put("qty", qtytotransfer * -1);
				outputMap.put("stock_id", fromStockId);
				lObjConfigDao.updateStockMaster(outputMap, con);

				outputMap.put("qty", qtytotransfer);
				outputMap.put("stock_id", toStockId);

				if (toStockId.equals("0")) {
					outputMap.put("drpstoreId", fromStoreId);
					outputMap.put("drpitems", itemId);
					outputMap.put("qty", qtytotransfer);
					lObjConfigDao.addStockMaster(outputMap, con);
				} else {
					lObjConfigDao.updateStockMaster(outputMap, con);
				}
				//

				outputMap.put("drpstoreId", fromStoreId);
				outputMap.put("drpitems", itemId);
				outputMap.put("qty", qtytotransfer * -1);
				outputMap.put("type", "StockTransfer");
				outputMap.put("user_id", userId);
				outputMap.put("remarks", "");

				lObjConfigDao.addStockRegister(outputMap, con);

				outputMap.put("drpstoreId", toStoreId);
				outputMap.put("drpitems", itemId);
				outputMap.put("qty", qtytotransfer);
				outputMap.put("type", "StockTransfer");
				outputMap.put("user_id", userId);
				outputMap.put("remarks", "");

				if (lObjConfigDao.checkifStockAlreadyExist(toStoreId, itemId, con).equals("0")) {
					lObjConfigDao.addStockMaster(outputMap, con);
				}

				lObjConfigDao.addStockRegister(outputMap, con);
			}

			rs.setAjaxData("Stock Transfered Succesfully:" + stockModificationId);

			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddItem(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		try {
			String itemId = request.getParameter("itemId");
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);
			if (itemId != null) {
				outputMap.put("item_id", itemId);
				LinkedHashMap<String, Object> itemDetails = lObjConfigDao.getItemdetailsById(outputMap, con);

				outputMap.put("availableStoreIds", lObjConfigDao.getListOfStoresForThisItem(itemId, con));
				outputMap.put("itemImageFileNames", lObjConfigDao.getListofItemImages(outputMap, con));
				outputMap.put("itemImageFileNames", lObjConfigDao.getListofItemImages(outputMap, con));
				generateQRForThisString(itemDetails.get("product_code").toString(), DestinationPath, 118, 120, "QR");
				generateQRForThisString(itemDetails.get("product_code").toString(), DestinationPath, 118, 120,
						"Barcode");
				itemDetails.put("qrCodeImageName", itemDetails.get("product_code").toString() + "QR.jpg");
				itemDetails.put("barcodeCodeImageName", itemDetails.get("product_code").toString() + "Barcode.jpg");

				outputMap.put("itemDetails", itemDetails);

			}
			outputMap.put("CategoriesList", lObjConfigDao.getCategories(outputMap, con));
			outputMap.put("RawMaterialsList", lObjConfigDao.getRawMaterialMaster(outputMap,con));
			
			outputMap.put("tentativeProductCode", "7000" + lObjConfigDao.getTentativeSequenceNo(appId, "Item", con));

			outputMap.put("storeList", lst); //

			rs.setViewName("../AddItems.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddStock(HttpServletRequest request, Connection con) throws SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String stockType = request.getParameter("type");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		String stockModificationId = request.getParameter("stockModificationId");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("stockType", stockType);

		try {

			if (stockModificationId != null) {
				outputMap.put("stockModificationDetails",
						lObjConfigDao.getStockModificationDetailsAddRemove(stockModificationId, con));
			}

			outputMap.put("storeId", storeId);
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("addStockList", lObjConfigDao.getInventoryCountingListForThisStore(outputMap, con));
			rs.setViewName("../AddStock.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showCollectPayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
			String appType = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("app_type");
			outputMap.put("app_type", appType);
			rs.setViewName("../CollectPayment.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showGenerateInvoice(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			String appType = "";
			if (request.getSession().getAttribute("userdetails") != null) {
				appType = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");
			}
			String invoiceId = request.getParameter("invoice_id");
			String invoiceNo = request.getParameter("invoice_no");
			String tableId = request.getParameter("table_id");
			String bookingId = request.getParameter("booking_id");
			String MobilebookingId = request.getParameter("mobile_booking_id");
			String txtinvoicedate = request.getParameter("txtinvoicedate");
			String vehicleId = request.getParameter("vehicleId");
			

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

			String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("store_id");

			String invoiceTypeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("invoice_type");

			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			boolean adminFlag = (boolean) request.getSession().getAttribute("adminFlag");

			outputMap.putAll(lObjConfigDao.getUserConfigurations(userId, con));

			outputMap.put("store_id", storeId);
			outputMap.put("app_id", appId);
			outputMap.put("table_id", tableId);
			outputMap.put("invoice_no", invoiceNo);
			outputMap.put("txtinvoicedate", txtinvoicedate);


			
			if (vehicleId != null) {
				outputMap.put("vehicleDetails", lObjConfigDao.getVehicleDetailsById(vehicleId, con));
			}

			if (invoiceId != null) {
				outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetails(invoiceId, con));
			}

			if (tableId != null) {
				outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetailsForTable(tableId, con));
			}

			if (bookingId != null) {
				LinkedHashMap<String, Object> hm = lObjConfigDao.getInvoiceDetailsForBooking(bookingId, con);
				List<LinkedHashMap<String, Object>> lst = (List<LinkedHashMap<String, Object>>) hm.get("listOfItems");
				hm.put("customer_id", lst.get(0).get("customer_id"));
				hm.put("customer_name", lst.get(0).get("customer_name"));
				outputMap.put("invoiceDetails", hm);
			}

			if (MobilebookingId != null) {
				LinkedHashMap<String, Object> hm = lObjConfigDao.getInvoiceDetailsForMobileBooking(MobilebookingId,
						con);
				List<LinkedHashMap<String, Object>> lst = (List<LinkedHashMap<String, Object>>) hm.get("listOfItems");
				hm.put("customer_id", lst.get(0).get("customer_id"));
				hm.put("customer_name", lst.get(0).get("customer_name"));
				outputMap.put("invoiceDetails", hm);
			}

			if (invoiceId == null) {

				outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
				outputMap.put("todaysDateMinusOneMonth", lObjConfigDao.getDateFromDBMinusOneMonth(con));
				outputMap.put("tentativeSerialNo",
						lObjConfigDao.getTentativeSequenceNo(appId, "trn_invoice_register", con));
				outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
			}

			// outputMap.put("itemList",
			// lObjConfigDao.getItemMasterForGenerateInvoiceForThisStore(outputMap,con));
			if (!invoiceTypeId.equals("1")) {
				outputMap.put("itemList", lObjConfigDao.getItemMasterForGenerateInvoice(outputMap, con));
				outputMap.put("groupList", lObjConfigDao.getCustomerGroup(appId, con));
			}

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, con);
			LinkedHashMap<Object, Object> reqHm = new LinkedHashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";
			for (LinkedHashMap<String, Object> temp : lst) {
				if (categoryName.equals(""))
					categoryName = temp.get("catNameTrimmed").toString();
				if (categoryName.equals(temp.get("catNameTrimmed"))) {
					lsttemp.add(temp);
				} else {

					reqHm.put(categoryName, lsttemp);

					lsttemp = new ArrayList<>();
					categoryName = temp.get("catNameTrimmed").toString();
					lsttemp.add(temp);
				}
			}

			reqHm.put(categoryName, lsttemp);

			outputMap.put("lsitOfCategories", lObjConfigDao.getCategoriesWithAtLeastOneItem(outputMap, con));
			outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));

			outputMap.put("lstOfSwipeMaster", lObjConfigDao.getSwipeMaster(outputMap, con));
			outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, con));
			outputMap.put("categoriesWithItem", reqHm);

			if (invoiceTypeId.equals("2")) // means services and we need unique model no and unique no for this app id
			{
				outputMap.put("listUniqueModelNo", lObjConfigDao.getUniqueModelNoForThisApp(con, appId));
			}

			if (appType.equals("Battery")) {
				outputMap.put("vehicleMaster", lObjConfigDao.getListOfUniqueVehicleName(con, appId));
			}

			rs.setViewName("../GenerateInvoice" + appType + ".jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getVehicleIdForCustomer(HttpServletRequest request, Connection con) throws SQLException {

		CustomResultObject rs = new CustomResultObject();

		try {
			HashMap<String, Object> outputMap = new HashMap<>();
			outputMap.put("customer_id", request.getParameter("customerId"));
			rs.setAjaxData(mapper.writeValueAsString(lObjConfigDao.getVehicleOfCustomer(outputMap, con)));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showGeneratePurchaseInvoice(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			String invoiceId = request.getParameter("invoiceId");
			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("store_id");

			outputMap.put("store_id", storeId);
			outputMap.put("app_id", appId);

			if (invoiceId != null) {
				outputMap.put("invoiceDetails", lObjConfigDao.getPurchaseInvoiceDetails(invoiceId, con));
			}

			if (invoiceId == null) {

				outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
				outputMap.put("tentativeSerialNo",
						lObjConfigDao.getTentativeSequenceNo(appId, "trn_purchase_invoice_register", con));
				outputMap.put("vendorMaster", lObjConfigDao.getVendorMaster(outputMap, con));
			}

			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));

			HashMap<Object, Object> reqHm = new HashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";

			reqHm.put(categoryName, lsttemp);

			outputMap.put("lsitOfCategories", lObjConfigDao.getCategoriesWithAtLeastOneItem(outputMap, con));
			outputMap.put("categoriesWithItem", reqHm);

			rs.setViewName("../GeneratePI.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showGenerateQuote(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			String quote_id = request.getParameter("quote_id");

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

			String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("store_id");

			outputMap.put("store_id", storeId);
			outputMap.put("app_id", appId);

			if (quote_id != null) {
				outputMap.put("quoteDetails", lObjConfigDao.getQuoteDetails(quote_id, con));
			}

			if (quote_id == null) {
				outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
				outputMap.put("tentativeSerialNo",
						lObjConfigDao.getTentativeSequenceNo(appId, "trn_quote_register", con));
				outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
				outputMap.put("defaultTermsAndConditions", lObjConfigDao.getDefaultTermsAndConditions(con, appId));
			}
			outputMap.put("itemList", lObjConfigDao.getItemMasterForGenerateInvoice(outputMap, con));

			rs.setViewName("../QuoteGeneration.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddCategory(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long categoryId = request.getParameter("categoryId") == null ? 0L
				: Long.parseLong(request.getParameter("categoryId"));
		outputMap.put("category_id", categoryId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (categoryId != 0) {
				outputMap.put("categoryDetails", lObjConfigDao.getCategoryDetails(outputMap, connections));
			}
			rs.setViewName("../AddCategory.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showConfigureTables(HttpServletRequest request, Connection conWithF) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			rs.setViewName("../ConfigureTables.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(conWithF));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showTableOrders(HttpServletRequest request, Connection conWithF) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long tableId = request.getParameter("table_id") == null ? 0L : Long.parseLong(request.getParameter("table_id"));
		String tableNo = request.getParameter("table_no");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, conWithF));
			List<LinkedHashMap<String, Object>> orderDetails = lObjConfigDao.getOrderDetailsForTable(tableId, conWithF);
			int i = 1;
			for (LinkedHashMap<String, Object> hm : orderDetails) {
				hm.put("SrNo", i++);
			}
			outputMap.put("orderDetails", orderDetails);
			outputMap.put("table_no", tableNo);

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, conWithF);
			HashMap<Object, Object> reqHm = new HashMap<>();
			List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
			String categoryName = "";
			for (LinkedHashMap<String, Object> temp : lst) {
				if (categoryName.equals(""))
					categoryName = temp.get("category_name").toString();
				if (categoryName.equals(temp.get("category_name"))) {
					lsttemp.add(temp);
				} else {

					reqHm.put(categoryName, lsttemp);

					lsttemp = new ArrayList<>();
					categoryName = temp.get("category_name").toString();
					lsttemp.add(temp);
				}
			}

			reqHm.put(categoryName, lsttemp);

			outputMap.put("lsitOfCategories", lObjConfigDao.getCategoriesWithAtLeastOneItem(outputMap, conWithF));
			outputMap.put("categoriesWithItem", reqHm);

			rs.setViewName("../AddOrder.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(conWithF));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showTables(HttpServletRequest request, Connection conWithF) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		int storeId = Integer
				.parseInt(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getTableStatus(storeId, conWithF);
			outputMap.put("ListOfTables", lst);
			rs.setViewName("../Tables.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(conWithF));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showPendingOrders(HttpServletRequest request, Connection conWithF) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String table_id = request.getParameter("table_id");
		outputMap.put("table_id", table_id);

		int storeId = Integer
				.parseInt(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));
		outputMap.put("store_id", storeId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPendingOrders(outputMap, conWithF);
			int i = 1;
			for (LinkedHashMap<String, Object> hm : lst) {
				hm.put("SrNo", i++);
			}
			outputMap.put("pendingItems", lst);
			outputMap.put("tableList", lObjConfigDao.getListOfTables(storeId, conWithF));
			rs.setViewName("../PendingOrders.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(conWithF));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveTableConfig(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		int noOfTables = Integer.parseInt(request.getParameter("noOfTables"));

		try {
			String storeId = request.getParameter("store_id");

			rs.setAjaxData(lObjConfigDao.saveTableConfig(noOfTables, storeId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveNewApp(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String appname = request.getParameter("txtappname");
		String txtvalidtill = request.getParameter("txtvalidtill");
		String txtstorename = request.getParameter("txtstorename");
		String txtusername = request.getParameter("txtusername");
		String appType = "PetrolPump"; // need to get from front end

		try {

			long userId = lObjConfigDao.saveNewApp(appname, txtvalidtill, txtstorename, txtusername, appType, con);
			lObjConfigDao.updateConfigurationForThisUser(1, "N", "N", "N", "N", "N", "N", "N", "N", "N", "1",
					String.valueOf(userId), con);

			rs.setAjaxData("App Created Succesfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showUserRoleMapping(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String app_type = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");
		outputMap.put("app_type", app_type);

		try {

			outputMap.put("userList", lObjConfigDao.getEmployeeMaster(outputMap, con));
			outputMap.put("roleList", apptypes.get(app_type));
			rs.setViewName("../UserRoleMapping.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getRoleDetailsForthisUser(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long userId = Integer.parseInt(request.getParameter("userId"));
		HashMap<String, Object> hm = null;
		try {

			LinkedHashMap<Long, Role> roleMaster = apptypes.get("Master");

			List<LinkedHashMap<String, Object>> lstUserRoleDetails = lObjConfigDao.getUserRoleDetails(userId, con);
			List<LinkedHashMap<String, Object>> lstUserRoleDetailsNew = new ArrayList<>();
			for (LinkedHashMap<String, Object> lm : lstUserRoleDetails) {
				Role realRole = roleMaster.get(Long.valueOf(lm.get("role_id").toString()));
				lm.put("role_name", realRole.getRoleName());
				lstUserRoleDetailsNew.add(lm);

			}

			hm = new HashMap<>();
			hm.put("lstUserRoleDetails", lstUserRoleDetailsNew);
			List<String> roles = new ArrayList<>();

			for (LinkedHashMap<String, Object> mappedRole : lstUserRoleDetails) {
				roles.add(mappedRole.get("role_id").toString());
			}

			hm.put("lstElements", getElementsNewLogic(roles, CommonFunctions.elements, CommonFunctions.roles));
			rs.setAjaxData(mapper.writeValueAsString(hm));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddGroup(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long groupId = request.getParameter("groupId") == null ? 0L : Long.parseLong(request.getParameter("groupId"));

		try {
			if (groupId != 0) {
				outputMap.put("groupDetails", lObjConfigDao.getGroupDetails(groupId, con));
			}
			rs.setViewName("../AddGroup.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCompositeMapping(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long itemId = request.getParameter("itemId") == null ? 0L : Long.parseLong(request.getParameter("itemId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (itemId != 0) {
				outputMap.put("item_id", itemId);
				outputMap.put("itemCompositeMapping", lObjConfigDao.getCompositeItemDetails(itemId, con));
				outputMap.put("itemDetails", lObjConfigDao.getItemdetailsById(outputMap, con));
				outputMap.put("ListOfItems", lObjConfigDao.getItemMaster(outputMap, con));

			}
			rs.setViewName("../AddCompositeItemDetails.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddExpense(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long groupId = request.getParameter("expenseId") == null ? 0L
				: Long.parseLong(request.getParameter("expenseId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {
			String todaysDate = lObjConfigDao.getDateFromDB(con);
			outputMap.put("todaysDate", todaysDate);
			outputMap.put("distinctExpenseList", lObjConfigDao.getDistinctExpenseList(con, appId));
			String expenseDate = request.getParameter("expenseDate");

			if (expenseDate == null || expenseDate.equals("")) {
				outputMap.put("fromDate", todaysDate);
				outputMap.put("toDate", todaysDate);
			} else {
				outputMap.put("fromDate", expenseDate);
				outputMap.put("toDate", expenseDate);
				outputMap.put("todaysDate", expenseDate);

			}
			List<LinkedHashMap<String, Object>> expenseList = lObjConfigDao.getExpenseRegister(outputMap, con);

			outputMap.put("expenseList", expenseList);
			outputMap.put("totalAmount", getTotalAmountExpenditure(expenseList));

			if (groupId != 0) {
				outputMap.put("expenseDetails", lObjConfigDao.getExpenseDetails(groupId, con));

			}
			rs.setViewName("../AddExpense.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	private double getTotalAmountExpenditure(List<LinkedHashMap<String, Object>> expenseList) {
		double ReqTotal = 0;
		for (LinkedHashMap<String, Object> hm : expenseList) {
			ReqTotal += Double.valueOf(hm.get("amount").toString());
		}
		return ReqTotal;
	}

	private double getTotalLubeSales(List<LinkedHashMap<String, Object>> expenseList) {
		double ReqTotal = 0;
		for (LinkedHashMap<String, Object> hm : expenseList) {
			ReqTotal += Double.valueOf(hm.get("totalAmount").toString());
		}
		return ReqTotal;
	}

	public CustomResultObject showAddRoutine(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long RoutineId = request.getParameter("RoutineId") == null ? 0L
				: Long.parseLong(request.getParameter("RoutineId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {
			if (RoutineId != 0) {
				outputMap.put("routineDetails", lObjConfigDao.getRoutinepDetails(RoutineId, con));
			}
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));
			rs.setViewName("../AddRoutine.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showReturnScreen(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long detailId = request.getParameter("detailsId") == null ? 0L
				: Long.parseLong(request.getParameter("detailsId"));

		try {
			if (detailId != 0) {
				outputMap.put("invoiceSubDetails", lObjConfigDao.getInvoiceSubDetails(detailId, con));
			}
			rs.setViewName("../ReturnItems.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> showReturnScreenService(HttpServletRequest request, Connection con)
			throws SQLException {

		HashMap<String, Object> outputMap = new HashMap<>();
		long detailId = request.getParameter("detailsId") == null ? 0L
				: Long.parseLong(request.getParameter("detailsId"));
		outputMap.put("invoiceSubDetails", lObjConfigDao.getInvoiceSubDetails(detailId, con));
		return outputMap;
	}

	public CustomResultObject showConfigureLowStock(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long stockId = request.getParameter("stockId") == null ? 0L : Long.parseLong(request.getParameter("stockId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("stock_id", stockId);
			outputMap.put("storeList", lObjConfigDao.getStoreMaster(outputMap, con));
			outputMap.put("stockDetails", lObjConfigDao.getStockDetailsbyId(outputMap, con));
			rs.setViewName("../ConfigureLowStock.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	CommonFunctions cf = new CommonFunctions();

	public CustomResultObject addItem(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		List<String> availableStoreIds = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {
				if (item.getFieldName().equals("chk1")) {
					availableStoreIds.add(item.getString());
					continue;
				}

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else if (item.getSize() > 0) {
					toUpload.add(item);
				}
			}
		}

		try {

			String userid = (String) hm.get("user_id");
			String appId = (String) hm.get("app_id");
			String appType = (String) hm.get("app_type");
			hm.put("app_id", appId);
			hm.put("userId", userid);


			if (hm.get("drpdebitin")==null) {
				hm.put("drpdebitin", "Q");
			}

			if (hm.get("product_code").equals("")) {
				long val = lObjConfigDao.getPkForThistable("Item", Long.valueOf(hm.get("app_id").toString()), con);
				hm.put("product_code", "7000" + val);
			}

			if (lObjConfigDao.checkIfProductCodeAlreadyExist(hm, con)) {
				rs.setReturnObject(outputMap);
				rs.setAjaxData("<script>alert('Product code already exists in master');window.location='"
						+ hm.get("callerUrl") + "?a=showItemMaster';</script>");
				return rs;
			}

			if (hm.get("itemname").toString().contains("\"") || hm.get("itemname").toString().contains(delimiter)) {
				rs.setReturnObject(outputMap);
				rs.setAjaxData("<script>alert('Special Characters \" / not allowed');window.history.back();</script>");
				return rs;
			}

			hm.put("availableStoreIds", availableStoreIds);
			long itemId = 0;
			if (hm.get("hdnItemId").equals("")) {

				if (appType.equalsIgnoreCase("Restaurant"))
					itemId = lObjConfigDao.saveItemRestaurant(hm, con);
				else
					itemId = lObjConfigDao.saveItem(hm, con);
				hm.put("hdnItemId", itemId);

			} else {

				lObjConfigDao.insertIntoItemHistory(hm.get("hdnItemId").toString(), con);
				lObjConfigDao.updateItem(hm, con);
				itemId = Long.parseLong(hm.get("hdnItemId").toString());

			}

			lObjConfigDao.addStoreItemMapping(hm, con);

			// code to add image of items
			if (!toUpload.isEmpty()) {
				for (FileItem f : toUpload) {
					f.write(new File(DestinationPath + f.getName()));
					long attachmentId = cf.uploadFileToDBDual(DestinationPath + f.getName(), con, "Image", itemId);
					Files.copy(Paths.get(DestinationPath + f.getName()),
							Paths.get(DestinationPath + attachmentId + f.getName()),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}

			rs.setReturnObject(outputMap);
			rs.setAjaxData("<script>window.location='" + "?a=showItemMaster';</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addCategory(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String categoryName = hm.get("categoryName").toString();
		String appId = hm.get("app_id").toString();
		String orderNo = hm.get("order_no").toString();
		hm.put("app_id", appId);
		hm.put("category_id", categoryName);

		long categoryId = hm.get("hdnCategoryId").equals("") ? 0l : Long.parseLong(hm.get("hdnCategoryId").toString());
		try {

			if (categoryId == 0) {
				categoryId = lObjConfigDao.addCategory(con, hm);
			} else {
				lObjConfigDao.updateCategory(categoryId, con, categoryName, orderNo);
			}

			if (!toUpload.isEmpty() && toUpload.get(0).getSize() > 0) {

				toUpload.get(0).write(new File(DestinationPath + toUpload.get(0).getName()));
				long attachmentId = cf.uploadFileToDBDual(DestinationPath + toUpload.get(0).getName(), con, "Category",
						categoryId);
				Files.copy(Paths.get(DestinationPath + toUpload.get(0).getName()),
						Paths.get(DestinationPath + attachmentId + toUpload.get(0).getName()),
						StandardCopyOption.REPLACE_EXISTING);

			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showCategoryMasterNew'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addSlider(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String appId = hm.get("app_id").toString();
		hm.put("app_id", appId);

		try {

			if (!toUpload.isEmpty()) {
				for (FileItem f : toUpload) {
					f.write(new File(DestinationPath + f.getName()));
					long attachmentId = cf.uploadFileToDBDual(DestinationPath + f.getName(), con, "Slider",
							Long.valueOf(appId));
					Files.copy(Paths.get(DestinationPath + f.getName()),
							Paths.get(DestinationPath + attachmentId + f.getName()),
							StandardCopyOption.REPLACE_EXISTING);
				}
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showCategoryMasterNew'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addExpense(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long hdnExpenseId = hm.get("hdnExpenseId").equals("") ? 0l : Long.parseLong(hm.get("hdnExpenseId").toString());

		try {

			hm.put("hdnExpenseId", hdnExpenseId);

			if (hdnExpenseId == 0) {
				hdnExpenseId = lObjConfigDao.addExpense(con, hm);
			} else {
				lObjConfigDao.updateExpense(con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>alert('Updated succesfully');window.location='" + hm.get("callerUrl")
					+ "?a=showAddExpense&expenseDate=" + hm.get("txtdate") + "'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addGroup(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		HashMap<String, Object> hm = new HashMap<>();
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String group_name = hm.get("group_name").toString();
		long groupId = hm.get("hdnGroupId").equals("") ? 0l : Long.parseLong(hm.get("hdnGroupId").toString());
		String appId = (hm.get("app_id").toString());
		try {

			if (groupId == 0) {
				groupId = lObjConfigDao.addGroup(con, group_name, appId);
			} else {
				lObjConfigDao.updateGroup(groupId, con, group_name);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showCustomerGroup'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addRoutine(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		HashMap<String, Object> hm = new HashMap<>();
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long routineId = hm.get("hdnroutineid").equals("") ? 0l : Long.parseLong(hm.get("hdnroutineid").toString());
		try {

			if (routineId == 0) {
				routineId = lObjConfigDao.addRoutine(con, hm);
			} else {
				lObjConfigDao.updateRoutine(con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData(
					"<script>window.location='" + hm.get("callerUrl") + "?a=showCustomerDeliveryRoutine'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addStock(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {
			String userId = (request.getParameter("user_id"));
			String appId = (request.getParameter("app_id"));

			String actiontype = request.getParameter("action");
			String itemDetails = request.getParameter("itemDetails");
			String outerRemarks = request.getParameter("outerremarks");

			outputMap.put("app_id", appId);
			long store_id = Long.parseLong(
					((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));

			outputMap.put("user_id", userId);
			outputMap.put("drpstoreId", store_id);
			outputMap.put("outerRemarks", outerRemarks);

			String[] items = itemDetails.split("\\|");

			outputMap.put("userId", userId);
			outputMap.put("storeId", store_id);

			String StockModificationType = actiontype.equals("Remove") ? "Damage" : "StockIn";
			outputMap.put("type", StockModificationType);

			long stockModificationId = lObjConfigDao.addStockModification(outputMap, con);

			for (String item : items) {
				String[] itemVo = item.split("~");
				outputMap.put("drpitems", itemVo[0]);
				outputMap.put("qty", itemVo[1]);
				outputMap.put("remarks", itemVo[2]);

				long item_id = Long.parseLong(outputMap.get("drpitems").toString());
				outputMap.put("type", "StockIn");
				if (actiontype.equals("Remove")) {
					outputMap.put("qty", Double.parseDouble(outputMap.get("qty").toString()) * -1);
					outputMap.put("type", "Damage");
				}
				if (lObjConfigDao.checkifStockAlreadyExist(store_id, item_id, con).equals("0")) {
					lObjConfigDao.addStockMaster(outputMap, con);
				}

				lObjConfigDao.addStockRegister(outputMap, con);
				outputMap.put("stock_id", lObjConfigDao.checkifStockAlreadyExist(store_id, item_id, con));
				lObjConfigDao.updateStockMaster(outputMap, con);

				outputMap.put("stockModificationId", stockModificationId);
				outputMap.put("itemId", itemVo[0]);
				outputMap.put("currentStock", itemVo[2]);
				outputMap.put("qty", itemVo[1]);

				lObjConfigDao.addStockModificationAddRemove(outputMap, con);

			}

			rs.setReturnObject(outputMap);
			rs.setAjaxData("Stock Updated Succesfully:-" + stockModificationId);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			throw e;
		}
		return rs;
	}

	public CustomResultObject deleteItem(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long itemId = Integer.parseInt(request.getParameter("itemId"));
		try {

			lObjConfigDao.insertIntoItemHistory(String.valueOf(itemId), con);

			rs.setAjaxData(lObjConfigDao.deleteItem(itemId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject markAsServed(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String[] orderDetailIds = request.getParameter("orderDetailsId").split(",");
		try {

			rs.setAjaxData(lObjConfigDao.markAsServed(orderDetailIds, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject cancelOrderDetail(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long orderDetailId = Long.parseLong(request.getParameter("cancelOrderDetailId"));
		try {

			rs.setAjaxData(lObjConfigDao.cancelOrderDetail(orderDetailId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deletePayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long paymentId = Integer.parseInt(request.getParameter("paymentId"));

		String userId = request.getParameter("user_id");

		try {
			rs.setAjaxData(lObjConfigDao.deletePayment(paymentId, userId, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteGroup(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long groupId = Long.parseLong(request.getParameter("groupId"));
		try {

			rs.setAjaxData(lObjConfigDao.deleteGroup(groupId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject moveToPlanning(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoiceIds = (request.getParameter("invoiceIds"));
		String[] invoiceIdsArr=invoiceIds.split("~");
		try {

			for(String invoiceId:invoiceIdsArr)
			{
				lObjConfigDao.moveToPlanning(invoiceId, con);
			}
			rs.setAjaxData("Moved To Planning");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	

	public CustomResultObject movetoDone(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoiceIds = (request.getParameter("invoiceIds"));
		String[] invoiceIdsArr=invoiceIds.split("~");
		try {

			for(String invoiceId:invoiceIdsArr)
			{
				lObjConfigDao.moveToDone(invoiceId, con);
			}
			rs.setAjaxData("Moved To Done");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject moveToPending(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoiceIds = (request.getParameter("invoiceIds"));
		String[] invoiceIdsArr=invoiceIds.split("~");
		try {

			for(String invoiceId:invoiceIdsArr)
			{
				lObjConfigDao.moveToPending(invoiceId, con);
			}
			rs.setAjaxData("Moved To Pending");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	

	public CustomResultObject deleteExpense(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long expenseId = Long.parseLong(request.getParameter("expenseId"));
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {

			rs.setAjaxData(lObjConfigDao.deleteExpense(expenseId, userId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteInvoice(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoiceId = "";
		String type = "";
		if (request.getParameter("invoiceId") != null) {
			invoiceId = request.getParameter("invoiceId");
			type = request.getParameter("type");
		} else {
			invoiceId = request.getAttribute("invoiceId").toString();
			type = request.getAttribute("type").toString();
		}
		long invoiceIdLong = Long.parseLong(invoiceId);

		HashMap<String, Object> outputMap = new HashMap<>();
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String storeId = request.getParameter("store_id");
		String appId = request.getParameter("app_id");

		outputMap.put("store_id", storeId);
		outputMap.put("user_id", userId);
		outputMap.put("app_id", appId);
		outputMap.put("type", type);

		try {

			List<LinkedHashMap<String, Object>> itemDetails = (List<LinkedHashMap<String, Object>>) lObjConfigDao
					.getInvoiceDetails(invoiceId, con).get("listOfItems");

			if (!type.equals("P")) {
				lObjConfigDao.deleteInvoice(invoiceIdLong, userId, con);
				lObjConfigDao.deletePaymentAgainstInvoice(invoiceIdLong, userId, con);

				lObjConfigDao.deleteReturnsAgainstInvoice(invoiceIdLong, userId, con);

				outputMap.put("itemDetails", itemDetails);
				// lObjConfigDao.addStockAgainstCorrection(outputMap, con);

				lObjConfigDao.removeStockAgainstReturn(outputMap, con);
			} else {
				lObjConfigDao.deletePurchaseInvoice(invoiceIdLong, userId, con);
			}

			rs.setAjaxData("Invoice Deleted Succesfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getInvoiceIdByInvoiceNo(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String invoiceNo = request.getParameter("invoiceNo");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {
			String invoiceId = lObjConfigDao.getInvoiceIdByInvoiceNo(invoiceNo, appId, con).get("invoice_id");
			if (invoiceId == null) {
				invoiceId = "No Invoice Found";
			}
			rs.setAjaxData(invoiceId);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteRoutine(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long routineId = Long.parseLong(request.getParameter("RoutineId"));
		try {

			rs.setAjaxData(lObjConfigDao.deleteRoutine(routineId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveInvoice(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		rs.setAjaxData(saveInvoiceService(request, con).get("returnMessage").toString());
		return rs;
	}

	public CustomResultObject saveInvoiceServiceAjaxMobile(HttpServletRequest request, Connection con)
			throws Exception {
		CustomResultObject rs = new CustomResultObject();
		rs.setAjaxData(mapper.writeValueAsString(saveInvoiceService(request, con)));
		return rs;
	}

	public HashMap<String, Object> saveInvoiceService(HttpServletRequest request, Connection con) throws Exception {
		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		String appType = "";
		if (request.getSession().getAttribute("userdetails") != null) {
			appType = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");
		}
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~", -1);
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					itemDetailsMap.put("rate", itemDetails[2]);
					itemDetailsMap.put("custom_rate", itemDetails[3]);
					itemDetailsMap.put("item_name", itemDetails[4]);
					if (itemDetails.length >= 6) {
						itemDetailsMap.put("sgst_amount", itemDetails[5]);
						itemDetailsMap.put("sgst_percentage", itemDetails[6]);
						itemDetailsMap.put("cgst_amount", itemDetails[7]);
						itemDetailsMap.put("cgst_percentage", itemDetails[8]);

						itemDetailsMap.put("gst_amount", itemDetails[9]);
						itemDetailsMap.put("weight", itemDetails[10]);
						itemDetailsMap.put("size", itemDetails[11]);
						String purchaseDetailsId = itemDetails[12].trim().equals("") ? "0" : itemDetails[12].trim();
						String itemAmount = itemDetails[13].trim().equals("") ? "0" : itemDetails[13].trim();

						itemDetailsMap.put("purchaseDetailsId", purchaseDetailsId);
						itemDetailsMap.put("itemAmount", itemAmount);

						if (appType.equals("Battery")) {
							itemDetailsMap.put("battery_no", itemDetails[13]);
							itemDetailsMap.put("vehicle_name", itemDetails[14]);
							itemDetailsMap.put("vehicle_no", itemDetails[15]);
							itemDetailsMap.put("warranty", itemDetails[16]);
						}

					}
					itemDetailsMap.put("debit_in", lObjConfigDao.getDebitInForItem(itemDetails[0], con));
					itemListRequired.add(itemDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}

		String userId = request.getParameter("user_id");
		String storeId = request.getParameter("store_id");
		String appId = request.getParameter("appId");
		String tableId = request.getParameter("table_id");
		String bookingId = request.getParameter("booking_id");
		String hdnPreviousInvoiceId = request.getParameter("hdnPreviousInvoiceId");

		String drpshiftid = request.getParameter("drpshiftid");
		String nozzle_id = request.getParameter("nozzle_id");
		String attendant_id = request.getParameter("attendant_id");
		String slot_id = request.getParameter("slot_id");
		String vehicle_id = request.getParameter("vehicle_id");

		String swipe_id = request.getParameter("swipe_id") == null
				|| request.getParameter("swipe_id").equals("") ? null : request.getParameter("swipe_id");

		hm.put("shift_id", drpshiftid);
		hm.put("nozzle_id", nozzle_id);
		hm.put("attendant_id", attendant_id);
		hm.put("swipe_id", swipe_id);
		hm.put("slot_id", slot_id);
		hm.put("vehicle_id", vehicle_id);
		hm.put("app_type", appType);

		request.setAttribute("invoiceId", hdnPreviousInvoiceId);
		request.setAttribute("type", "S");
		if (hdnPreviousInvoiceId != null && !hdnPreviousInvoiceId.equals("") && !hdnPreviousInvoiceId.equals("0")) {
			deleteInvoice(request, con);
			String invoiceNo = lObjConfigDao.getInvoiceNoByInvoiceId(hdnPreviousInvoiceId, con).get("invoice_no");
			hm.put("invoice_no", invoiceNo);
			// added new code to let the invoice no stay as it is
		}

		if (appId == null || appId.equals("")) {
			appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		}
		hm.put("app_id", appId);
		hm.put("table_id", tableId);
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		}

		hm.put("user_id", userId);
		hm.put("store_id", storeId);
		String customer_id = request.getParameter("customer_id").equals("") ? "0" : request.getParameter("customer_id");
		hm.put("customer_id", customer_id);
		try {

			if (!appType.equals("PetrolPump") && !appType.equals("Battery") && !appType.equals("SnacksProduction")
					&& !isValidateGrossWithIndividualAmount(hm)) {
				throw new Exception("invalid Gross with Individual Amount");
			}

			if (!appType.equals("Battery") && !appType.equals("SnacksProduction") &&  !isValidateTotalWithGrossMinusDiscounts(hm)) {
				throw new Exception("Invalid Total Amount Received Vs Calculated");
			}

			HashMap<String, Object> returnMap = lObjConfigDao.saveInvoice(hm, con);

			if (appType.equals("SnacksProduction"))

			{

				hm.put("invoice_id", returnMap.get("invoice_id"));
				lObjConfigDao.saveSnacksInvoice(hm, con);



			}
			hm.put("invoice_id", returnMap.get("invoice_id"));

			String appendor = "";
			if (tableId != null && !tableId.equals("")) {
				lObjConfigDao.removeOrderFromTable(hm, con);
				lObjConfigDao.markAllAsServed(hm, con);
				appendor = "~?a=showTables";
			}

			if (bookingId != null && !bookingId.equals("") && !bookingId.equals("null")) {
				lObjConfigDao.markBookingAsServed(Long.valueOf(bookingId), con);
			}

			hm.put("returnMessage", returnMap.get("invoice_no") + "~" + returnMap.get("invoice_id") + appendor);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			throw e;
		}
		return hm;
	}

	public boolean isValidateGrossWithIndividualAmount(HashMap<String, Object> hm) {
		double GrossAmount = Double.parseDouble((hm.get("gross_amount").toString()));
		double calcuLatedGrossAmount = 0L;

		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");
		for (HashMap<String, Object> item : itemDetailsList) {
			calcuLatedGrossAmount += Double.valueOf(item.get("qty").toString())
					* Double.valueOf(item.get("rate").toString());
		}
		System.out.println(
				"Gross Amount Received " + GrossAmount + " vs " + calcuLatedGrossAmount + "calculated Gross Amount");
		return Double.valueOf(new DecimalFormat("0.").format(GrossAmount))
				.equals(Double.valueOf(new DecimalFormat("0.").format(calcuLatedGrossAmount)));
	}

	public boolean isValidateTotalWithGrossMinusDiscounts(HashMap<String, Object> hm) {
		double GrossAmount = Double.parseDouble((hm.get("gross_amount").toString()));
		double itemDiscount = Double.parseDouble((hm.get("item_discount").toString()));
		double invoiceDiscount = Double.parseDouble((hm.get("invoice_discount").toString()));
		double totalAmount = Double.parseDouble((hm.get("total_amount").toString()));
		double totalGst = 0;
		if (hm.get("total_gst") != null)
			totalGst = Double.parseDouble((hm.get("total_gst").toString()));

		double calculatedTotalAmount = GrossAmount - itemDiscount - invoiceDiscount + totalGst;

		String formattedDouble = df.format(calculatedTotalAmount);

		double formattedDoubleValue = new Double(formattedDouble);

		System.out.println(
				"calculatedTotalAmount " + formattedDoubleValue + " vs " + totalAmount + "total Amount Received");

		return Double.valueOf(new DecimalFormat("0.").format(totalAmount))
				.equals(Double.valueOf(new DecimalFormat("0.").format(formattedDoubleValue)));
	}

	private void debitStockAgainstInvoiceForCompositeItems(HashMap<String, Object> hm, Connection conWithF)
			throws Exception {
		List<HashMap<String, Object>> itemListRequired = (List<HashMap<String, Object>>) hm.get("itemDetails");

		for (HashMap<String, Object> item : itemListRequired) {
			if (item.get("debit_in").equals("S")) {
				continue;
			}
			long storeId = Long.parseLong(hm.get("store_id").toString());
			long itemId = Long.parseLong(item.get("item_id").toString());
			String stockId = lObjConfigDao.checkifStockAlreadyExist(storeId, itemId, conWithF);
			hm.put("stock_id", stockId);
			item.put("stock_id", stockId);
			String previousQty;
			if (stockId.equals("0")) {
				previousQty = "0";
			} else {
				previousQty = lObjConfigDao.getStockDetailsbyId(hm, conWithF).get("qty_available");
			}
			item.put("stock_id", stockId);
			if (lObjConfigDao.isItemComposite(itemId, conWithF) && Double.parseDouble(previousQty) <= 0) {
				// get list of all composite items and then debit them
				List<LinkedHashMap<String, Object>> childItems = lObjConfigDao.getCompositeItemChildsAndQuantity(itemId,
						conWithF);

				HashMap<String, Object> newHashmap = new HashMap<>();
				newHashmap.putAll(hm);
				List<HashMap<String, Object>> itemListRequiredChild = new ArrayList<>();
				for (LinkedHashMap<String, Object> item1 : childItems) {

					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", item1.get("child_item_id"));
					Double qty = Double.parseDouble(item1.get("qty").toString());
					Double itemQty = Double.parseDouble(item.get("qty").toString());
					itemDetailsMap.put("qty", itemQty * qty);

					itemListRequiredChild.add(itemDetailsMap);
				}
				newHashmap.put("store_id", storeId);
				newHashmap.put("itemDetails", itemListRequiredChild);
				debitStockAgainstInvoiceForCompositeItems(newHashmap, conWithF);

			} else {
				item.put("store_id", storeId);
				item.put("app_id", hm.get("app_id"));
				item.put("item_id", itemId);
				item.put("qty", item.get("qty"));
				item.put("user_id", hm.get("user_id"));
				item.put("invoice_id", hm.get("invoice_id"));
				item.put("invoice_date", hm.get("invoice_date"));
				lObjConfigDao.debitStockItem(item, conWithF); // for simple debit which item is not composite
			}
		}
	}

	public CustomResultObject saveOrder(HttpServletRequest request, Connection con) throws SQLException {
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		String BufferedImagesFolder = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		CustomResultObject rs = new CustomResultObject();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~", -1);
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					itemDetailsMap.put("remarks", itemDetails[2]);
					itemDetailsMap.put("item_name", itemDetails[3]);
					itemListRequired.add(itemDetailsMap);
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));
		}
		String userId = request.getParameter("user_id");
		String orderId = (request.getParameter("order_id"));
		String tableId = request.getParameter("table_id");
		String tableNo = request.getParameter("table_no");

		hm.put("user_id", userId);
		hm.put("table_id", tableId);
		hm.put("table_no", tableNo);

		hm.put("order_id", orderId);
		String runningFlag = "N";
		try {
			if (orderId.equals("")) {
				orderId = String.valueOf(lObjConfigDao.saveOrder(hm, con));
			} else {
				runningFlag = "Y";
			}
			hm.put("order_id", orderId);
			hm.put("runningFlag", runningFlag);

			lObjConfigDao.saveOrderDetails(hm, con);
			lObjConfigDao.updateTableWithOrderId(hm, con);

			rs.setAjaxData("Ordered Succesfull~"
					+ generateKotPDFService(hm, DestinationPath, BufferedImagesFolder, con).get("returnData"));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return rs;
	}

	public CustomResultObject saveCustomerServiceAjax(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		rs.setAjaxData(saveCustomerService(request, con).get("returnMessage").toString());
		return rs;
	}

	public HashMap<String, Object> saveCustomerService(HttpServletRequest request, Connection con) throws SQLException {
		HashMap<String, Object> hm = new HashMap<>();
		long customerId = 0;
		try {
			hm.put("app_id", request.getParameter("appId"));
			hm.put("customerName", request.getParameter("customerName"));
			hm.put("mobileNumber", request.getParameter("mobileNumber"));
			hm.put("city", request.getParameter("city"));
			hm.put("address", request.getParameter("address"));
			hm.put("customerType", request.getParameter("customerType"));
			hm.put("customerGroup", "3");
			hm.put("alternate_mobile_no", request.getParameter("alternate_mobile_no"));
			hm.put("txtgstno", request.getParameter("txtgstno"));
			
			

			if (hm.get("customerName").toString().contains(delimiter)) {
				throw new CustomerMobileAlreadyExist("Special Character Not allowed in name");
			}

			if (lObjConfigDao.mobileNoAlreadyExist(hm.get("mobileNumber").toString(), hm.get("app_id").toString(),
					con)) {
				throw new CustomerMobileAlreadyExist("Mobile Number Already exist");
			}

			customerId = lObjConfigDao.addCustomer(con, hm);
			hm.put("returnMessage", "Customer Saved Succesfully~" + customerId);
		}

		catch (CustomerMobileAlreadyExist e) {
			hm.put("returnMessage", "Customer Mobile Already Exist~" + customerId);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return hm;
	}

	public CustomResultObject printLabels(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();

			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("product_code", itemDetails[1]);
					itemDetailsMap.put("item_name", itemDetails[2]);
					itemDetailsMap.put("noOfLabels", itemDetails[3]);
					itemDetailsMap.put("isPrintPrice", itemDetails[4]);
					itemListRequired.add(itemDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}

		List<HashMap<String, Object>> newListRequired = new ArrayList<>();
		// based on no of labels adding more to list
		for (HashMap<String, Object> tempObj : itemListRequired) {
			int noOfLabels = Integer.parseInt(tempObj.get("noOfLabels").toString());
			for (int x = 0; x < noOfLabels; x++) {
				newListRequired.add(tempObj);
			}
		}
		while (true) {
			if (newListRequired.size() % 5 == 0) {
				break;
			}
			HashMap<String, Object> tempObj = new HashMap<>();
			tempObj.put("product_code", 00000);
			tempObj.put("item_name", "Adjustment");
			newListRequired.add(tempObj);
		}

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		hm.put("user_id", userId);
		hm.put("store_id", storeId);
		try {

			for (HashMap<String, Object> item : newListRequired) {
				generateQRForThisString(item.get("product_code").toString(), DestinationPath, 118, 120,
						hm.get("type").toString());
			}

			Document document = new Document(PageSize.A4, 0, 0, 0, 0);
			document.setMargins(0, 0, 0, 0);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath + "reqPDF.pdf"));
			writer.setCompressionLevel(9);
			writer.setFullCompression();
			ConfigurationServiceImpl event = new ConfigurationServiceImpl();
			writer.setPageEvent(event);
			document.open();
			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			int i = 0;
			List<String> tempList = new ArrayList<>();

			for (HashMap<String, Object> item : newListRequired) {
				PdfPCell cell;
				com.itextpdf.text.Image image = com.itextpdf.text.Image
						.getInstance(DestinationPath + item.get("product_code") + hm.get("type").toString() + ".jpg");
				cell = new PdfPCell(image);
				cell.setPadding(5);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
				tempList.add(item.get("item_name").toString());
				i++;
				if (i % 5 == 0) {
					for (String s : tempList) {
						cell = new PdfPCell(new Phrase(s, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
						cell.setHorizontalAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
						cell.setBorder(Rectangle.RECTANGLE);
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("--------------------------",
							new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
					cell.setHorizontalAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
				}
				tempList.clear();
			}

			table.completeRow();
			document.add(table);
			document.close();

			rs.setAjaxData("reqPDF.pdf");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject printLabelsVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();

			if (paramName.equals("vehicleDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("vehicle_id", itemDetails[0]);
					itemDetailsMap.put("vehicle_name", itemDetails[1]);
					itemDetailsMap.put("vehicle_number", itemDetails[2]);
					itemDetailsMap.put("noOfLabels", "1");
					itemListRequired.add(itemDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}

		List<HashMap<String, Object>> newListRequired = new ArrayList<>();
		// based on no of labels adding more to list
		for (HashMap<String, Object> tempObj : itemListRequired) {
			int noOfLabels = Integer.parseInt(tempObj.get("noOfLabels").toString());
			for (int x = 0; x < noOfLabels; x++) {
				newListRequired.add(tempObj);
			}
		}
		while (true) {
			if (newListRequired.size() % 3 == 0) {
				break;
			}
			HashMap<String, Object> tempObj = new HashMap<>();
			tempObj.put("vehicle_id", 00000);
			tempObj.put("vehicle_name", "Adjustment");
			tempObj.put("vehicle_number", "Adjustment");
			newListRequired.add(tempObj);
		}

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		hm.put("user_id", userId);
		hm.put("store_id", storeId);
		try {

			for (HashMap<String, Object> item : newListRequired) {
				generateQRForThisString(item.get("vehicle_id").toString(), DestinationPath, 150, 150,
						"QR");
			}

			Document document = new Document(PageSize.A4, 0, 0, 0, 0);
			document.setMargins(0, 0, 0, 0);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath + "reqPDF.pdf"));
			writer.setCompressionLevel(9);
			writer.setFullCompression();
			ConfigurationServiceImpl event = new ConfigurationServiceImpl();
			writer.setPageEvent(event);
			document.open();
			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			int i = 0;
			List<String> tempList = new ArrayList<>();

			for (HashMap<String, Object> item : newListRequired) {
				PdfPCell cell;
				com.itextpdf.text.Image image = com.itextpdf.text.Image
						.getInstance(DestinationPath + item.get("vehicle_id") + "QR" + ".jpg");
				cell = new PdfPCell(image);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setPadding(5);
				//cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);


				i++;
			if(i%3==0)
			{

				for(int m=i-3;m<i;m++)
				{
				HashMap<String, String> vehicleDetails=lObjConfigDao.getVehicleDetailsById((newListRequired.get(m).get("vehicle_id").toString()), con);
				if(newListRequired.get(m).get("vehicle_id").toString().equals("0"))
				{
					vehicleDetails.put("vehicle_id","NA");
					vehicleDetails.put("vehicle_name","NA");
					vehicleDetails.put("vehicle_number","NA");
					vehicleDetails.put("customer_name","NA");
					
				}
				String shoaebcustomername=vehicleDetails.get("customer_name").toString();
				String shoaebcustomervehiclename="("+vehicleDetails.get("vehicle_name")+")";
				String shoaebcustomervehiclenumber=vehicleDetails.get("vehicle_number").toString();
				

				cell = new PdfPCell(new Phrase(shoaebcustomername+"\n"+shoaebcustomervehiclename+"\n"+shoaebcustomervehiclenumber,new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));	        
				//cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
				}

				// cell = new PdfPCell(new Phrase("-------------------------------------------------------------------------------------------------------------------------------------------",new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));	        
				// //cell.setBorder(Rectangle.NO_BORDER);
				// cell.setColspan(3);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// table.addCell(cell);
			}
				
			}

			document.add(table);
			document.close();

			rs.setAjaxData("reqPDF.pdf");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteStock(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long stockid = Integer.parseInt(request.getParameter("stockid"));
		try {

			rs.setAjaxData(lObjConfigDao.deleteStock(stockid, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveInventoryCounting(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		try {
			HashMap<String, Object> outputMap = new HashMap<>();
			Long storeId = Long.valueOf(request.getParameter("store_id"));
			Long userId = Long.valueOf(request.getParameter("user_id"));

			String itemDetailsString = request.getParameter("itemDetails");
			String remarksouter = request.getParameter("remarksouter");

			String[] listOfItems = itemDetailsString.split("\\|");

			outputMap.put("userId", userId);
			outputMap.put("storeId", storeId);
			outputMap.put("type", "Inventory Counting");
			outputMap.put("outerRemarks", remarksouter);
			String appId = request.getParameter("app_id");
			outputMap.put("app_id", appId);

			long stockModificationId = lObjConfigDao.addStockModification(outputMap, con);

			List<HashMap<String, Object>> listItems = new ArrayList<>();
			for (String s : listOfItems) {
				String[] item = s.split("\\~");
				HashMap<String, Object> hm = new HashMap<>();
				hm.put("stockModificationId", stockModificationId);

				hm.put("itemId", item[0]);
				hm.put("expectedCount", item[1]);
				hm.put("currentCount", item[2]);
				hm.put("difference", item[3]);
				hm.put("differenceAmount", item[4]);
				hm.put("app_id", appId);

				listItems.add(hm);
			}

			for (HashMap<String, Object> hm : listItems) {

				lObjConfigDao.saveStockModificationInventoryCounting(hm, con);

				hm.put("type", "InventoryCounting");
				hm.put("remarks", "");
				hm.put("app_id", appId);
				hm.put("qty", hm.get("difference"));
				hm.put("drpstoreId", storeId);
				hm.put("drpitems", hm.get("itemId"));
				hm.put("user_id", userId);
				lObjConfigDao.addStockRegister(hm, con);

				hm.put("qty", hm.get("currentCount"));

				long itemId = Long.parseLong(hm.get("itemId").toString());
				if (lObjConfigDao.checkifStockAlreadyExist(storeId, itemId, con).equals("0")) {
					lObjConfigDao.addStockMaster(hm, con);
				}

				hm.put("stock_id", lObjConfigDao.checkifStockAlreadyExist(storeId, itemId, con));
				lObjConfigDao.updateStockMasterInventoryCounting(hm, con);

			}

			rs.setAjaxData("Counting added Succesfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	ObjectMapper mapper = new ObjectMapper();

	public CustomResultObject getItemsByCategoryId(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			rs.setAjaxData(mapper.writeValueAsString(lObjConfigDao.getItemsByCategoryId(outputMap, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getItemsForThisCategoryNameByAjax(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String categoryName = request.getParameter("category_name");
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("category_name", categoryName);

		try {

			rs.setAjaxData(mapper.writeValueAsString(lObjConfigDao.getItemsByCategorynName(outputMap, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showEditItem(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long itemId = Integer.parseInt(request.getParameter("itemId"));
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			outputMap.put("CategoriesList", lObjConfigDao.getCategories(outputMap, con));
			outputMap.put("ItemDetails", lObjConfigDao.getDetailsforItem(itemId, con));
			rs.setViewName("../AddItems.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showItemHistory(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String itemId = request.getParameter("itemId");
		HashMap<String, Object> outputMap = new HashMap<>();
		Long storeId = Long.parseLong(
				((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("item_id", itemId);
			outputMap.put("itemDetails", lObjConfigDao.getItemdetailsById(outputMap, con));
			outputMap.put("itemHistoryList", lObjConfigDao.getItemHistory(storeId, itemId, con));
			rs.setViewName("../ItemHistory.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showItemMasterHistory(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String itemId = request.getParameter("item_id");
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("item_id", itemId);
			outputMap.put("itemHistory", lObjConfigDao.getItemMasterHistoryForThisItem(itemId, con));
			rs.setViewName("../ItemMasterHistory.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showVehicleMaster(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("vehicleDetails", lObjConfigDao.getVehicleMaster(outputMap, con));
		try {

			rs.setViewName("../VehicleMaster.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showGenerateLR(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("vehicleDetails", lObjConfigDao.getVehicleMaster(outputMap, con));
		outputMap.put("lrno", lObjConfigDao.getMaxLrNo(con, outputMap).get("lrno"));
		outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
		try {

			rs.setViewName("../GenerateLRScreen.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject searchLR(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("lrnumbersearch", request.getParameter("lrnumbersearch"));

		outputMap.put("lrData", lObjConfigDao.searchLR(con, outputMap));

		try {

			rs.setAjaxData(mapper.writeValueAsString(outputMap));
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showConfigureLR(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("vehicleDetails", lObjConfigDao.getVehicleMaster(outputMap, con));
		try {

			rs.setViewName("../ConfigureLR.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("appId", appId);
		outputMap.put("app_id", appId);
		try {

			outputMap.put("vehicle_id", request.getParameter("vehicleId"));
			outputMap.put("vehicleDetails", lObjConfigDao.getVehicleDetails(outputMap, con));
			outputMap.put("lstCustomerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
			outputMap.put("listOfItems", lObjConfigDao.getItemMasterFuel(outputMap, con)); // this is where you


			rs.setViewName("../AddVehicle.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			outputMap.put("customerId", request.getParameter("customerName"));
			outputMap.put("vehicleName", request.getParameter("vehicleName"));
			outputMap.put("vehicleNumber", request.getParameter("vehicleNumber"));
			outputMap.put("appId", request.getParameter("app_id"));
			outputMap.put("userId", request.getParameter("user_id"));
			outputMap.put("drpfueltype", request.getParameter("drpfueltype"));


			long vehicle_id = request.getParameter("hdnVehicleId").equals("") ? 0L
					: Long.parseLong(request.getParameter("hdnVehicleId"));
			outputMap.put("vehicle_id", vehicle_id);

			if (vehicle_id == 0) {

				lObjConfigDao.addVehicle(con, outputMap);
			} else {
				lObjConfigDao.updateVehicle(outputMap, con);
			}

			rs.setReturnObject(outputMap);
			rs.setAjaxData("<script>window.location='?a=showVehicleMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long vehicleId = Integer.parseInt(request.getParameter("vehicleId"));
		try {
			outputMap.put("vehicle_id", vehicleId);
			rs.setAjaxData(lObjConfigDao.deleteVehicle(vehicleId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
			rs.setReturnObject(outputMap);
		}
		return rs;
	}

	public HashMap<String, Object> addReturnService(HttpServletRequest request, Connection con) throws Exception {

		long detailsId = Integer.parseInt(request.getParameter("hdnDetailsId"));
		long hdnItemId = Integer.parseInt(request.getParameter("hdnItemId"));
		long hdnInvoiceId = Integer.parseInt(request.getParameter("hdnInvoiceId"));
		Double hdnCustomRate = Double.parseDouble(request.getParameter("hdnCustomRate"));
		long hdnCustomerId = Integer.parseInt(request.getParameter("hdnCustomerId"));

		Double returnqty = Double.parseDouble(request.getParameter("returnqty"));
		HashMap<String, Object> outputMap = new HashMap<>();
		String userId = request.getParameter("userId") == null ? "" : request.getParameter("userId");
		long storeId = request.getParameter("storeId") == null ? 0L : Long.valueOf(request.getParameter("storeId"));
		String appId = request.getParameter("appId") == null ? "" : (request.getParameter("appId"));

		LinkedHashMap<String, String> detailsMap = lObjConfigDao.getItemdetailsById(detailsId, con);
		outputMap.put("app_id", appId);

		lObjConfigDao.insertToTrnReturnRegister(detailsId, returnqty, userId, appId, con);
		String stockId = lObjConfigDao.checkifStockAlreadyExist(storeId, hdnItemId, con);

		if (stockId.equals("0")) {
			HashMap<String, Object> stockDetails = new HashMap<>();
			stockDetails.put("drpstoreId", storeId);
			stockDetails.put("drpitems", hdnItemId);
			stockDetails.put("qty", 0);
			stockId = String.valueOf(lObjConfigDao.addStockMaster(stockDetails, con));
		}

		outputMap.put("stock_id", stockId);
		outputMap.put("qty", returnqty);

		outputMap.put("drpstoreId", storeId);
		outputMap.put("drpitems", hdnItemId);
		outputMap.put("qty", returnqty);
		outputMap.put("type", "ReturnAgainstInvoice");
		outputMap.put("user_id", userId);
		outputMap.put("remarks", " ");
		outputMap.put("invoice_id", hdnInvoiceId);
		outputMap.put("stock_date", getDateFromDB(con));

		lObjConfigDao.addStockRegister(outputMap, con);
		outputMap.put("payment_type", "Paid");
		outputMap.put("customer_id", hdnCustomerId);
		outputMap.put("payment_mode", "Return");

		double gstAmount = Double.parseDouble(detailsMap.get("gst_amount"));
		double customRate = Double.parseDouble(detailsMap.get("custom_rate"));
		double qty = Double.parseDouble(detailsMap.get("qty"));

		double gstPercentage = 100 * gstAmount / customRate * qty;

		double returnGstAmount = gstPercentage * returnqty * customRate / 100;

		outputMap.put("total_amount", (returnqty * hdnCustomRate) + returnGstAmount); //
		outputMap.put("store_id", storeId);
		outputMap.put("payment_for", "return");

		lObjConfigDao.updateStockMaster(outputMap, con);

		outputMap.put("invoice_date", getDateFromDB(con));
		lObjConfigDao.addPaymentFromCustomer(outputMap, con);

		outputMap.put("returnMessage", "Item Returned Succesfully");

		return outputMap;
		// rs.setAjaxData("<script>alert('Item Returned
		// Succesfully');window.location='?a=showItemHistory&itemId="+hdnItemId+"';</script>");

	}

	public CustomResultObject addReturn(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		rs.setAjaxData("<script>alert('" + addReturnService(request, con).get("returnMessage").toString()
				+ "');window.location='" + request.getParameter("callerUrl") + "?a=showItemHistory&itemId="
				+ request.getParameter("hdnItemId") + "';</script>");
		return rs;
	}

	public CustomResultObject ShowOrders(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			// outputMap.put("ListOfOrders", lObjConfigDao.ShowOrders(request));

			rs.setViewName("../ShowOrders.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showMobileBookings(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);

			outputMap.put("ListOfOrders", lObjConfigDao.getMobileAppOrders(appId, con));
			rs.setViewName("../MobileBookings.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteAttachment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long attachmentId = Long.parseLong(request.getParameter("attachmentId"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteAttachment(attachmentId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCustomerMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String searchInput = request.getParameter("searchInput");
		String groupId = request.getParameter("groupId");
		String customerType = request.getParameter("customerType");

		outputMap.put("searchInput", searchInput);
		outputMap.put("groupId", groupId);
		outputMap.put("customerType", customerType);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {
			String[] colNames = { "customerId", "customerName", "mobileNumber", "customerCity", "customerAddress",
					"customerType" };

			List<LinkedHashMap<String, Object>> lst = null;

			lst = lObjConfigDao.getCustomerMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerMaster");
			} else {
				outputMap.put("groupList", lObjConfigDao.getCustomerGroup(appId, con));
				outputMap.put("ListOfCustomers", lst);
				rs.setViewName("../Customer.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showVendorMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String searchInput = request.getParameter("searchInput");
		String groupId = request.getParameter("groupId");
		String customerType = request.getParameter("customerType");

		outputMap.put("searchInput", searchInput);
		outputMap.put("groupId", groupId);
		outputMap.put("customerType", customerType);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {
			String[] colNames = { "customerId", "customerName", "mobileNumber", "customerCity", "customerAddress",
					"customerType" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getVendorMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "VendorMaster");
			} else {
				outputMap.put("ListOfVendors", lst);
				rs.setViewName("../Vendor.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject deleteCustomer(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long customerId = Long.parseLong(request.getParameter("customerid"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteCustomer(customerId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteVendor(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long customerId = Long.parseLong(request.getParameter("vendorid"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteVendor(customerId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addCustomer(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		String appId = request.getParameter("app_id");

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long customerId = hm.get("hdnCustomerId").equals("") ? 0l : Long.parseLong(hm.get("hdnCustomerId").toString());
		try {
			if (hm.get("customerName").toString().contains(delimiter)) {
				throw new CustomerMobileAlreadyExist("Special Character Not allowed in name");
			}

			if (lObjConfigDao.mobileNoAlreadyExist(hm.get("mobileNumber").toString(), appId, con)) {
				throw new CustomerMobileAlreadyExist("Mobile Number Already exist");
			}

			if (customerId == 0) {
				customerId = lObjConfigDao.addCustomer(con, hm);
			} else {
				lObjConfigDao.updateCustomer(customerId, con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData(
					"<script>alert('Customer Updated Succesfully');window.location='?a=showCustomerMaster'</script>");

		} catch (CustomerMobileAlreadyExist e) {
			rs.setReturnObject(outputMap);
			rs.setAjaxData("<script>alert('" + e.getMessage() + "');window.history.back();</script>");
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject addVendor(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		HashMap<String, Object> hm = new HashMap<>();
		String appId = request.getParameter("app_id");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long customerId = hm.get("hdnVendorId").equals("") ? 0l : Long.parseLong(hm.get("hdnVendorId").toString());
		try {

			if (customerId == 0) {
				customerId = lObjConfigDao.addVendor(con, hm);
			} else {
				lObjConfigDao.updateVendor(con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>alert('Vendor Updated Succesfully');window.location='" + hm.get("callerUrl")
					+ "?a=showVendorMaster'</script>");

		} catch (CustomerMobileAlreadyExist e) {
			rs.setReturnObject(outputMap);
			rs.setAjaxData("<script>alert('" + e.getMessage() + "');window.history.back();</script>");
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showAddCustomer(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long customerId = request.getParameter("customerId") == null ? 0L
				: Long.parseLong(request.getParameter("customerId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String mobileNo = request.getParameter("mobileNo");
		outputMap.put("app_id", appId);
		try {
			LinkedHashMap<String, String> hm = new LinkedHashMap<>();
			hm.put("mobile_number", mobileNo);
			if (customerId != 0) {
				hm = lObjConfigDao.getCustomerDetails(customerId, con);
			}
			outputMap.put("customerDetails", hm);
			outputMap.put("DistinctCityNames", lObjConfigDao.getDistinctCityNames(appId, con));
			outputMap.put("groupList", lObjConfigDao.getCustomerGroup(appId, con));

			rs.setViewName("../AddCustomer.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddVendor(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long customerId = request.getParameter("vendorId") == null ? 0L
				: Long.parseLong(request.getParameter("vendorId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String mobileNo = request.getParameter("mobileNo");
		outputMap.put("app_id", appId);
		try {
			LinkedHashMap<String, String> hm = new LinkedHashMap<>();
			hm.put("mobile_number", mobileNo);
			if (customerId != 0) {
				hm = lObjConfigDao.getVendorDetails(customerId, con);
			}
			outputMap.put("vendorDetails", hm);
			outputMap.put("DistinctCityNames", lObjConfigDao.getDistinctCityNames(appId, con));

			rs.setViewName("../AddVendor.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showScanVehicleQR(HttpServletRequest request,Connection connections)
	{
		CustomResultObject rs=new CustomResultObject();			
		HashMap<String, Object> outputMap=new HashMap<>();
		
		
		String userId=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		
		try
		{	
			
			LinkedHashMap<String, String> empDetails=lObjConfigDao.getEmployeeDetails(Long.valueOf(userId), connections);			
			
			
			
			rs.setViewName("../ScanVehicle.jsp");	
			rs.setReturnObject(outputMap);		
		}
		catch (Exception e)
		{
				writeErrorToDB(e);
				rs.setHasError(true);
		}		
		return rs;
	}
	
	

	public CustomResultObject showStoreMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			String[] colNames = { "storeId", "storeName", "storeAddress", "storeEmail" };

			List<LinkedHashMap<String, Object>> lst = null;

			lst = lObjConfigDao.getStoreMaster(outputMap, con);

			if (!exportFlag.isEmpty())
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "StoreMaster");
			else {
				outputMap.put("ListOfStores", lst);
				rs.setViewName("../Store.jsp");
				rs.setReturnObject(outputMap);
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showTermsAndCondition(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			String[] colNames = { "termscondition", "order" };

			List<LinkedHashMap<String, Object>> lst = null;

			lst = lObjConfigDao.getTermsMaster(outputMap, con);

			if (!exportFlag.isEmpty())
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"TermsAndCondition");
			else {
				outputMap.put("ListOfTermsAndCondition", lst);
				rs.setViewName("../TermsAndCondition.jsp");
				rs.setReturnObject(outputMap);
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddStore(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long storeId = request.getParameter("storeId") == null ? 0L : Long.parseLong(request.getParameter("storeId"));

		try {
			if (storeId != 0) {
				outputMap.put("storeDetails", lObjConfigDao.getStoreDetails(storeId, con));
			}
			rs.setViewName("../AddStore.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddTermsAndCondition(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long termsId = request.getParameter("termsId") == null ? 0L : Long.parseLong(request.getParameter("termsId"));

		try {
			if (termsId != 0) {
				outputMap.put("termsAndConditionDetails", lObjConfigDao.gettermsAndConditionDetails(termsId, con));
			}
			rs.setViewName("../AddTermsAndCondition.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showSwitchStore(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		try {
			rs.setViewName("../SwitchStore.jsp");
			rs.setReturnObject(showSwitchStoreService(request, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> showSwitchStoreService(HttpServletRequest request, Connection con)
			throws SQLException {

		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = request.getParameter("appId");
		if (appId == null || appId.equals("")) {
			appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		}
		outputMap.put("app_id", appId);
		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}
		try {
			outputMap.put("listOfStores", lObjConfigDao.getStoreMaster(outputMap, con));
			outputMap.put("userDetails", lObjConfigDao.getuserDetailsById(Long.valueOf(userId), con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));

		}
		return outputMap;
	}

	public CustomResultObject addStore(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFactory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFactory);
		String appId = request.getParameter("app_id");
		outputMap.put("app_id", appId);
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long storeId = hm.get("hdnStoreId").equals("") ? 0l : Long.parseLong(hm.get("hdnStoreId").toString());
		try {

			if (storeId == 0) {
				storeId = lObjConfigDao.addStore(con, hm);
			} else {
				lObjConfigDao.updateStore(storeId, con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='?a=showStoreMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject addTermsAndCondition(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFactory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFactory);
		String appId = request.getParameter("app_id");
		outputMap.put("app_id", appId);
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long hdntermsId = hm.get("hdntermsId").equals("") ? 0l : Long.parseLong(hm.get("hdntermsId").toString());
		try {

			if (hdntermsId == 0) {
				hdntermsId = lObjConfigDao.addTermsAndCondition(con, hm);
			} else {
				lObjConfigDao.updateTermsAndCondition(hdntermsId, con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showTermsAndCondition'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject deleteStore(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long storeId = Long.parseLong(request.getParameter("storeId"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteStore(storeId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteTermsAndCondition(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long termsId = Long.parseLong(request.getParameter("termsId"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteTermsAndCondition(termsId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showDailyInvoiceReportParameter(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);

		try {
			outputMap.put("listStoreData", lst);
			rs.setViewName("../DailyInvoiceReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showCustomerInvocieRegisterParameter(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerMaster(outputMap, con);
			outputMap.put("customerList", lst);
			rs.setViewName("../CustomerInvoiceReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showCustomerLedgerParameter(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerMaster(outputMap, con);
			outputMap.put("customerList", lst);
			rs.setViewName("../CustomerLedgerReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showSalesReport2Parameters(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerMaster(outputMap, con);
			outputMap.put("customerList", lst);
			rs.setViewName("../SalesReport2Parameters.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showSalesReport2(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../SalesRegister2Generated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "formattedInvoiceDate", "invoice_id", "item_name", "qty", "qty_to_return",
					"BilledQty", "rate", "custom_rate", "DiscountAmount", "ItemAmount", "formattedUpdatedDate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerInvoiceHistory(customerId, fromDate,
					toDate, con, appId);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("CustomerInvoiceHistory", lst);

				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../SalesRegister2Generated.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showSalesReport3(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("drpstoreId") == null ? "" : request.getParameter("drpstoreId");
		String paymentType = request.getParameter("paymentType") == null ? "" : request.getParameter("paymentType");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		boolean deleteFlag = request.getParameter("deleteFlag") == null ? false
				: new Boolean(request.getParameter("deleteFlag").toString());

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		if (storeId.equals("")) {
			storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		hm.put("paymentType", paymentType);
		hm.put("deleteFlag", deleteFlag);
		hm.put("customerId", customerId);

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		try {
			String[] colNames = { "FormattedInvoiceDate", "updatedDate", "payment_type", "customer_name",
					"category_name", "item_name", "qty", "weight", "amt", "item_discount", "invoice_no" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSalesReport3(hm, con);
			outputMap.putAll(gettotalDetailsSalesReport3(lst));

			if (!exportFlag.isEmpty()) {
				outputMap.put("lst", lst);

				outputMap = getCommonFileGeneratorWithTotal(colNames, outputMap, exportFlag, DestinationPath, userId,
						"DailyInvoiceReport");
			} else {
				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("listStoreData", lObjConfigDao.getStoreMaster(hm, con));
				outputMap.put("txtfromdate", fromDate);
				outputMap.put("txttodate", toDate);
				outputMap.put("drpStoreId", storeId);
				outputMap.put("type", type);
				outputMap.put("app_id", appId);
				outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
				if (!customerId.equals("")) {
					outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));
				}

				rs.setViewName("../SalesRegister3Generated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showStockTransfer(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String stockModificationId = request.getParameter("stockModificationId");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);
		try {

			if (stockModificationId != null) {
				outputMap.put("stockModificationDetails",
						lObjConfigDao.getStockModificationDetailsStocktransfer(stockModificationId, con));
			}

			outputMap.put("listStoreData", lst);
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("itemList", lObjConfigDao.getItemMaster(outputMap, con));

			outputMap.put("fromDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("toDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("storeId", "-1");

			List<LinkedHashMap<String, Object>> lst1 = lObjConfigDao.getStockRegister(outputMap, con);
			outputMap.put("stockRegisterList", lst1);

			rs.setViewName("../StockTransfer.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showStockRegister(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);
		try {
			outputMap.put("listStoreData", lst);
			rs.setViewName("../StockRegisterReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateDailyInvoiceReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("drpstoreId") == null ? "" : request.getParameter("drpstoreId");
		String paymentType = request.getParameter("paymentType") == null ? "" : request.getParameter("paymentType");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");
		String attendant_id = request.getParameter("attendant_id") == null ? "" : request.getParameter("attendant_id");
		String paymentMode = request.getParameter("paymentMode") == null ? "" : request.getParameter("paymentMode");
		String battery_no = request.getParameter("battery_no") == null ? "" : request.getParameter("battery_no");
		String invoice_no = request.getParameter("invoice_no") == null ? "" : request.getParameter("invoice_no");

		boolean deleteFlag = request.getParameter("deleteFlag") == null ? false
				: new Boolean(request.getParameter("deleteFlag").toString());

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		if (storeId.equals("")) {
			storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		hm.put("paymentType", paymentType);
		hm.put("deleteFlag", deleteFlag);
		hm.put("customerId", customerId);
		hm.put("attendant_id", attendant_id);
		hm.put("paymentMode", paymentMode);
		hm.put("battery_no", battery_no);
		hm.put("invoice_no", invoice_no);
		

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		try {
			String[] colNames = { "customer_name", "total_amount", "invoice_no", "FormattedInvoiceDate", "updatedDate",
					"payment_type", "payment_mode", "name", "store_name" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getDailyInvoiceDetails(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"DailyInvoiceReport");
			} else {
				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("listStoreData", lObjConfigDao.getStoreMaster(hm, con));
				outputMap.put("txtfromdate", fromDate);
				outputMap.put("txttodate", toDate);
				outputMap.put("drpStoreId", storeId);
				outputMap.put("type", type);
				outputMap.put("app_id", appId);
				outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
				if (!customerId.equals("")) {
					outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));
				}

				rs.setViewName("../DailyInvoiceReportGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject generateStockRegister(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		long storeId = request.getParameter("txtstore") == null ? -1 : Long.valueOf(request.getParameter("txtstore"));

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate1", fromDate);
			request.getSession().setAttribute("toDate1", toDate);
			request.getSession().setAttribute("storeId1", storeId);
		} else {
			fromDate = request.getSession().getAttribute("fromDate1").toString();
			toDate = request.getSession().getAttribute("toDate1").toString();
			storeId = Long.valueOf(request.getSession().getAttribute("storeId1").toString());
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);

		hm.put("storeId", storeId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "customer_name", "total_amount", "invoice_id", "invoice_date", "payment_type",
					"payment_mode", "name" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStockRegister(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"DailyInvoiceReport");
			} else {
				outputMap.put("stockRegisterList", lst);
				rs.setViewName("../StockRegisterGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showDailyPaymentRegisterParameter(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			rs.setViewName("../DailyPaymentRegisterParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateDailyPaymentRegister(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String paymentMode = request.getParameter("paymentMode") == null ? "" : request.getParameter("paymentMode");
		String storeId = request.getParameter("storeId") == null ? "" : request.getParameter("storeId");
		String paymentFor = request.getParameter("paymentFor") == null ? "" : request.getParameter("paymentFor");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String attendant_id = request.getParameter("attendant_id") == null ? "" : request.getParameter("attendant_id");

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate", fromDate);
			request.getSession().setAttribute("toDate", toDate);
			request.getSession().setAttribute("paymentMode", toDate);
			request.getSession().setAttribute("storeId", toDate);
			request.getSession().setAttribute("paymentFor", toDate);
			request.getSession().setAttribute("type", type);

		} else {
			fromDate = request.getSession().getAttribute("fromDate").toString();
			toDate = request.getSession().getAttribute("toDate").toString();
			paymentMode = request.getSession().getAttribute("paymentMode").toString();
			attendant_id = request.getSession().getAttribute("attendant_id").toString();
			storeId = request.getSession().getAttribute("storeId").toString();
			paymentFor = request.getSession().getAttribute("paymentFor").toString();
			type = request.getSession().getAttribute("type").toString();

		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		hm.put("paymentMode", paymentMode);
		hm.put("paymentFor", paymentFor);
		hm.put("type", type);
		hm.put("attendant_id", attendant_id);

		hm.put("app_id", appId);
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "customer_name", "payment_mode", "payment_date", "amount" };
			List<LinkedHashMap<String, Object>> lst = null;
			if (type.equals("Debit")) {
				lst = lObjConfigDao.getDailyDebitRegister(hm, con);
			} else {
				lst = lObjConfigDao.getDailyPaymentRegister(hm, con);
			}

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"DailyPaymentRegister");
			} else {
				outputMap.put("dailyPaymentData", lst);
				outputMap.put("type", type);

				rs.setViewName("../DailyPaymentRegisterGenerated.jsp");
				rs.setReturnObject(outputMap);
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showPendingCustomerCollectionReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("todaysDateMinusOneMonth", lObjConfigDao.getDateFromDBMinusOneMonth(con));
			rs.setViewName("../PendingCustomerCollectionParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generatePendingCustomerCollectionReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();


		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		
		if (fromDate.equals("")) {

			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		

		try {
			String[] colNames = { "No", "customer_name", "customer_reference", "PendingAmount", "mobile_number",
					"alternate_mobile_no", "city" };
			int i = 0;
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPendingCustomerCollection(hm, con);
			
	
			for (LinkedHashMap<String, Object> tempObj : lst) {
				tempObj.put("No", ++i);
			}

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"PendingCustomerCollection");

			} else {
				outputMap.put("ListOfPendingCollection", lst);
outputMap.put("txtfromdate",fromDate);
outputMap.put("txttodate",toDate);


				rs.setViewName("../PendingCustomerCollection.jsp");
				rs.setReturnObject(outputMap);
			}


		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showCategoryWiseReportParameter(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			rs.setViewName("../CategoryWiseReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateCategoryWiseReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate", fromDate);
			request.getSession().setAttribute("toDate", toDate);
		} else {
			fromDate = request.getSession().getAttribute("fromDate").toString();
			toDate = request.getSession().getAttribute("toDate").toString();
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "CategoryName", "StoreName", "InvoiceDate", "TotalAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCategoryWiseReport(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryWiseReport");
			} else {
				outputMap.put("CategoryWiseReportData", lst);
				rs.setViewName("../CategoryWiseReportGenerated.jsp");
				rs.setReturnObject(outputMap);
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showEmployeeWiseReportParameter(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			rs.setViewName("../EmployeeWiseReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateEmployeeWiseReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate", fromDate);
			request.getSession().setAttribute("toDate", toDate);
		} else {
			fromDate = request.getSession().getAttribute("fromDate").toString();
			toDate = request.getSession().getAttribute("toDate").toString();
		}

		long storeId = Long.parseLong(
				((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "EmployeeName", "StoreName", "InvoiceDate", "TotalAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getEmployeeWiseReport(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"EmployeeWiseReport");
			} else {
				outputMap.put("EmployeeWiseReportData", lst);
				rs.setViewName("../EmployeeWiseReportGenerated.jsp");
				rs.setReturnObject(outputMap);
			}

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showConsolidatedPaymentModeCollection(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);

		try {
			outputMap.put("listStoreData", lst);
			rs.setViewName("../ConsolidatedPaymentModeParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateConsolidatedPaymentModeCollection(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		long storeId = request.getParameter("txtstore") == null ? -1 : Long.valueOf(request.getParameter("txtstore"));

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate1", fromDate);
			request.getSession().setAttribute("toDate1", toDate);
			request.getSession().setAttribute("storeId1", storeId);
		} else {
			fromDate = request.getSession().getAttribute("fromDate1").toString();
			toDate = request.getSession().getAttribute("toDate1").toString();
			storeId = Long.valueOf(request.getSession().getAttribute("storeId1").toString());
		}

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "StoreName", "TotalAmount", "PaymentMode", "InvoiceDate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getConsolidatedPaymentModeCollection(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"ConsolidatedPaymentModeCollection");
			} else {
				outputMap.put("ConsolidatedPaymentMode", lst);
				rs.setViewName("../ConsolidatedPaymentModeGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showPaymentTypeCollection(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);

		try {
			outputMap.put("listStoreData", lst);
			rs.setViewName("../PaymentTypeCollectionParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generatePaymentTypeCollection(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		long storeId = request.getParameter("txtstore") == null ? -1 : Long.valueOf(request.getParameter("txtstore"));

		if (!fromDate.equals("")) {
			request.getSession().setAttribute("fromDate1", fromDate);
			request.getSession().setAttribute("toDate1", toDate);
			request.getSession().setAttribute("storeId1", storeId);
		} else {
			fromDate = request.getSession().getAttribute("fromDate1").toString();
			toDate = request.getSession().getAttribute("toDate1").toString();
			storeId = Long.valueOf(request.getSession().getAttribute("storeId1").toString());
		}

		HashMap<String, Object> hm = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "StoreName", "InvoiceDate", "PaymentType", "Amount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPaymentTypeCollectionCollection(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"PaymentTypeCollection");
			} else {
				outputMap.put("PaymentTypeCollection", lst);
				rs.setViewName("../PaymentTypeCollectionGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject switchStore(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		try {
			rs.setAjaxData(switchStoreService(request, con).get("returnMessage").toString());
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject changeInvoiceFormat(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		try {
			rs.setAjaxData(changeInvoiceFormatService(request, con).get("returnMessage").toString());
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> switchStoreService(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long storeId = Integer.parseInt(request.getParameter("storeId"));

		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}

		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			outputMap.put("returnMessage", lObjConfigDao.updateStoreForThisUser(storeId, userId, con));
			HashMap<String, String> hm = (HashMap<String, String>) request.getSession().getAttribute("userdetails");
			hm.put("store_name", lObjConfigDao.getStoreDetails(storeId, con).get("store_name"));
			hm.put("store_id", String.valueOf(storeId));
			request.getSession().setAttribute("userdetails", hm);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return outputMap;
	}

	public HashMap<String, Object> changeInvoiceFormatService(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		long formatId = Integer.parseInt(request.getParameter("formatId"));

		String invoice_default_checked_print = request.getParameter("invoice_default_checked_print").equals("true")
				? "Y"
				: "N";
		String invoice_default_checked_generatepdf = (request.getParameter("invoice_default_checked_generatepdf")
				.equals("true") ? "Y" : "N");
		String restaurant_default_checked_generatepdf = (request.getParameter("restaurant_default_checked_generatepdf")
				.equals("true") ? "Y" : "N");
		String user_total_payments = (request.getParameter("user_total_payments").equals("true") ? "Y" : "N");
		String user_payment_collections = (request.getParameter("user_payment_collections").equals("true") ? "Y" : "N");
		String user_counter_sales = (request.getParameter("user_counter_sales").equals("true") ? "Y" : "N");
		String user_payment_sales = (request.getParameter("user_payment_sales").equals("true") ? "Y" : "N");
		String user_store_sales = (request.getParameter("user_store_sales").equals("true") ? "Y" : "N");
		String user_store_bookings = (request.getParameter("user_store_bookings").equals("true") ? "Y" : "N");
		String user_store_expenses = (request.getParameter("user_store_expenses").equals("true") ? "Y" : "N");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			outputMap.put("returnMessage",
					lObjConfigDao.updateConfigurationForThisUser(formatId, invoice_default_checked_print,
							invoice_default_checked_generatepdf, restaurant_default_checked_generatepdf,
							user_total_payments, user_payment_collections, user_counter_sales, user_payment_sales,
							user_store_sales, user_store_bookings, user_store_expenses, userId, con));
			HashMap<String, String> hm = (HashMap<String, String>) request.getSession().getAttribute("userdetails");
			hm.put("invoice_format", String.valueOf(formatId));

			hm.put("invoice_default_checked_print", invoice_default_checked_print);
			hm.put("invoice_default_checked_generatepdf", invoice_default_checked_generatepdf);
			hm.put("restaurant_default_checked_generatepdf", restaurant_default_checked_generatepdf);
			hm.put("user_total_payments", user_total_payments);
			hm.put("user_payment_collections", user_payment_collections);
			hm.put("user_counter_sales", user_counter_sales);
			hm.put("user_payment_sales", user_payment_sales);
			hm.put("user_store_sales", user_store_sales);
			hm.put("user_store_bookings", user_store_bookings);
			hm.put("user_store_expenses", user_store_expenses);

			request.getSession().setAttribute("userdetails", hm);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return outputMap;
	}

	public CustomResultObject showTimeline(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		try {

			String[] colNames = { "EmployeeId", "EmployeeName", "EmployeeRole", "MobileNumber" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getTimeline(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"EmployeeMaster");
			} else {
				outputMap.put("ListOfEmployee", lst);
				rs.setViewName("../Timeline.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showEmployeeMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
	

		try {

			String[] colNames = { "user_id", "username", "name", "mobile","email","store_name" };
			outputMap.put("app_id", appId);

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getEmployeeMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"EmployeeMaster");
			} else {
				outputMap.put("ListOfEmployee", lst);
				rs.setViewName("../Employee.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject addEmployee(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		FileItemFactory itemFactory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFactory);
		HashMap<String, Object> hm = new HashMap<>();
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long employeeId = hm.get("hdnEmployeeId").equals("") ? 0l : Long.parseLong(hm.get("hdnEmployeeId").toString());

		try {
			if (employeeId == 0) {
				hm.put("password", "default@123");
				employeeId = lObjConfigDao.addEmployee(con, hm);
				hm.put("user_id", employeeId);
				lObjConfigDao.addDefaultUserConfigurations(con, hm);

			} else {
				lObjConfigDao.updateEmployee(employeeId, con, hm);
			}
			rs.setReturnObject(outputMap);
			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showEmployeeMaster'</script>");
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject deleteEmployee(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long employeeId = Long.parseLong(request.getParameter("employeeId"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteEmployee(employeeId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddEmployee(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long employeeId = request.getParameter("employeeId") == null ? 0L
				: Long.parseLong(request.getParameter("employeeId"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (employeeId != 0) {
				outputMap.put("employeeDetails", lObjConfigDao.getEmployeeDetails(employeeId, con));
			}
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);
			outputMap.put("listStoreData", lst);
			rs.setViewName("../AddEmployee.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showReturnRegisterReport(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getStoreMaster(outputMap, con);

		try {
			outputMap.put("listReturnData", lst);
			rs.setViewName("../ReturnRegisterParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateReturnRegisterReport(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		long storeId = request.getParameter("txtstore") == null ? -1 : Long.valueOf(request.getParameter("txtstore"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);
		hm.put("app_id", appId);
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			String[] colNames = { "ReturnId", "DetailsId", "QuantityToReturn", "UpdatedBy" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getReturnRegister(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"ReturnRegister");
			} else {
				outputMap.put("ReturnRegister", lst);
				rs.setViewName("../ReturnRegisterGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showSalesRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {

			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));

		outputMap.put("txtfromdate", fromDate);

		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../CustomerInvoiceHistoryGenerated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "formattedInvoiceDate", "invoice_id", "item_name", "qty", "qty_to_return",
					"BilledQty", "rate", "custom_rate", "DiscountAmount", "ItemAmount", "formattedUpdatedDate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerInvoiceHistory(customerId, fromDate,
					toDate, con, appId);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				LinkedHashMap<String, Object> totalDetails = gettotalDetailsInvoiceHistory(lst);
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";

				outputMap.put("totalDetails", totalDetails);

				outputMap.put("CustomerInvoiceHistory", lst);
				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../CustomerInvoiceHistoryGenerated.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showNozzleRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String shiftId = request.getParameter("shiftid") == null ? "0" : request.getParameter("shiftid");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		outputMap.put("shiftid", shiftId);

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);

		try {
			String[] colNames = { "formattedInvoiceDate", "invoice_id", "item_name", "qty", "qty_to_return",
					"BilledQty", "rate", "custom_rate", "DiscountAmount", "ItemAmount", "formattedUpdatedDate" };
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getNozzleSales(outputMap, con);
			List<LinkedHashMap<String, Object>> lstGroupByItemShift = lObjConfigDao
					.getNozzleSalesGroupByItemShift(outputMap, con);

			List<LinkedHashMap<String, Object>> lstPumpTest = lObjConfigDao.getPumpTests(outputMap, con);

			List<LinkedHashMap<String, Object>> lstPayments = lObjConfigDao.getPaymentsForDatesAttendantWise(outputMap,
					con);
			TreeMap<String, Object> paymentEmpWiseMap = getEmployeeWiseTotalPaymentAmount(lstPayments);

			List<LinkedHashMap<String, Object>> lstLubeSales = lObjConfigDao.getLubeSales(outputMap, con);

			List<LinkedHashMap<String, Object>> salesEmpWiseList = new ArrayList<>(lst);
			salesEmpWiseList.addAll(lstLubeSales);
			TreeMap<String, Object> salesEmpWiseMap = getEmployeeWiseTotalSalesAmount(salesEmpWiseList);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"Nozzle Register");
			} else {

				outputMap.put("lstNozzleRegister", lst);
				outputMap.put("lstPayments", lstPayments);
				outputMap.put("totalLubeSales", getTotalLubeSales(lstLubeSales));

				outputMap.put("salesEmpWiseMap", salesEmpWiseMap);
				outputMap.put("LubeSales", lstLubeSales);
				outputMap.put("lstPumpTest", lstPumpTest);

				outputMap.put("paymentEmpWiseMap", paymentEmpWiseMap);
				outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, con));
				outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));

				rs.setViewName("../NozzleRegister.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	private TreeMap<String, Object> getEmployeeWiseTotalPaymentAmount(List<LinkedHashMap<String, Object>> lstPayments) {

		TreeMap<String, Object> hm = new TreeMap<>();
		for (LinkedHashMap<String, Object> temp : lstPayments) {
			Double totalAmount = 0d;
			totalAmount += Double.parseDouble(temp.get("csh").toString());
			totalAmount += Double.parseDouble(temp.get("pytm").toString());
			totalAmount += Double.parseDouble(temp.get("cswp").toString());
			totalAmount += Double.parseDouble(temp.get("pnding").toString());
			if (hm.get(temp.get("name").toString()) == null) {
				hm.put(temp.get("name").toString(), totalAmount);
			} else {
				Double existingValue = Double.parseDouble(hm.get(temp.get("name").toString()).toString());
				hm.put(temp.get("name").toString(), existingValue + totalAmount);
			}
		}

		for (Map.Entry<String, Object> entry : hm.entrySet()) {
			String key = entry.getKey();
			Double value = (Double) entry.getValue();

			hm.put(key, df.format(value));
		}
		return hm;
	}

	private TreeMap<String, Object> getEmployeeWiseTotalSalesAmount(List<LinkedHashMap<String, Object>> lst) {

		TreeMap<String, Object> hm = new TreeMap<>();

		for (LinkedHashMap<String, Object> temp : lst) {
			String totalAmount = temp.get("totalAmount") == null ? "0" : temp.get("totalAmount").toString();
			Double Amount = Double.parseDouble(totalAmount);
			if (hm.get(temp.get("attendantName")) == null) {
				hm.put(temp.get("attendantName").toString(), Amount);
			} else {
				Double existingAmount = Double.parseDouble(hm.get(temp.get("attendantName").toString()).toString());
				existingAmount = existingAmount + Amount;
				hm.put(temp.get("attendantName").toString(), existingAmount);
			}
		}

		for (Map.Entry<String, Object> entry : hm.entrySet()) {
			String key = entry.getKey();
			Double value = (Double) entry.getValue();

			hm.put(key, df.format(value));
		}

		return hm;

	}

	DecimalFormat df = new DecimalFormat("#.00");

	public CustomResultObject exportSalesRegister(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("txtfromdate").toString();
		String toDate = request.getParameter("txttodate").toString();
		String customerId = request.getParameter("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerInvoiceHistory(customerId, fromDate, toDate,
				con, appId);
		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDate);
		outputMap.put("totalDetails", gettotalDetailsInvoiceHistory(lst));
		outputMap.put("customerDetails", customerDetails);

		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			String appenders = "SalesSummary" + userId + customerDetails.get("customer_name").replaceAll(" ", "") + "("
					+ getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDate) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			new InvoiceHistoryPDFHelper().generatePDFForCustomerInvoiceHistory(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportNozzleRegister(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolder = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("txtfromdate").toString();
		String shiftid = request.getParameter("shiftid").toString();
		String fromDateWithoutSlashes = fromDate.replaceAll("\\/", "");
		String appenders = fromDateWithoutSlashes + "-" + shiftid + ".pdf";

		outputMap.put("app_id", appId);
		outputMap.put("shiftid", shiftid);

		DestinationPath += appenders;
		outputMap.put("txtfromdate", fromDate);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getNozzleSalesGroupByItemShift(outputMap, con);
		List<LinkedHashMap<String, Object>> lstOld = lObjConfigDao.getNozzleSalesForExport(outputMap, con);
		outputMap.put("lstNozzleSales", lst);

		List<LinkedHashMap<String, Object>> lstLubeSales = lObjConfigDao.getLubeSales(outputMap, con);
		outputMap.put("totalLubeSales", getTotalLubeSales(lstLubeSales));
		outputMap.put("lstLubeSales", lstLubeSales);

		List<LinkedHashMap<String, Object>> lstPayments = lObjConfigDao
				.getPaymentsForDatesAttendantWiseGroupByPayment(outputMap, con);
		List<LinkedHashMap<String, Object>> lstPaymentsOld = lObjConfigDao.getPaymentsForDatesAttendantWiseExport(outputMap,
				con);
		List<LinkedHashMap<String, Object>> lstCardSwipes = lObjConfigDao.getCardSwipes(outputMap, con);
		List<LinkedHashMap<String, Object>> lstPaytmSlotwise = lObjConfigDao.getPaytmSlotWise(outputMap, con);

		outputMap.put("lstCardSwipes", lstCardSwipes);
		outputMap.put("lstPayments", lstPayments);
		outputMap.put("lstPaytmSlotwise", lstPaytmSlotwise);

		List<LinkedHashMap<String, Object>> lstCreditSales = lObjConfigDao.getCreditSalesForthisDate(outputMap, con);
		outputMap.put("lstCreditSales", lstCreditSales);

		List<LinkedHashMap<String, Object>> salesEmpWiseList = new ArrayList<>(lstOld);
		salesEmpWiseList.addAll(lstLubeSales);
		TreeMap<String, Object> salesEmpWiseMap = getEmployeeWiseTotalSalesAmount(salesEmpWiseList);
		TreeMap<String, Object> paymentEmpWiseMap = getEmployeeWiseTotalPaymentAmount(lstPaymentsOld);

		String cashAgainstPumpTest = lObjConfigDao.getPumpTestEquivalentCash(fromDate, shiftid,appId, con).get("CashAmount");

		outputMap.put("salesEmpWiseMap", salesEmpWiseMap);
		outputMap.put("paymentEmpWiseMap", paymentEmpWiseMap);
		outputMap.put("cashAgainstPumpTest", cashAgainstPumpTest);

		// sales
		// get petrol Sales
		// get diesel Sales
		// get lube sales

		// payment details
		// get cash details
		// get paytm details
		// get card swipe details
		// get credit parties details
		// FSM Unadjusted

		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			new NozzleRegisterPDFHelper().generateNozzleRegister(BufferedImagesFolder, DestinationPath, outputMap, con);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportCustomerItemHistory(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("fromDate1").toString();
		String toDate = request.getParameter("toDate1").toString();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerItemHistory(appId, "", fromDate, toDate,
				con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDate);

		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			String appenders = "PartyWiseItemWiseDetails" + userId + "(" + getDateASYYYYMMDD(fromDate) + ")" + "("
					+ getDateASYYYYMMDD(toDate) + ")" + getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			new InvoiceHistoryPDFHelper().generateCustomerItemHistory(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			rs.setAjaxData(appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportSalesRegister2(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("txtfromdate").toString();
		String toDate = request.getParameter("txttodate").toString();
		String customerId = request.getParameter("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerInvoiceHistory(customerId, fromDate, toDate,
				con, appId);
		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDate);
		outputMap.put("totalDetails", gettotalDetailsInvoiceHistory(lst));
		outputMap.put("customerDetails", customerDetails);

		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			String appenders = "SalesSummary" + userId + customerDetails.get("customer_name").replaceAll(" ", "") + "("
					+ getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDate) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);

			new InvoiceHistoryPDFHelper().generateSalesRegister2(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportSalesReport2(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getSession().getAttribute("fromDate1").toString();
		String toDate = request.getSession().getAttribute("toDate1").toString();
		String customerId = request.getSession().getAttribute("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerInvoiceHistory(customerId, fromDate, toDate,
				con, appId);
		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDate);
		outputMap.put("totalDetails", gettotalDetailsInvoiceHistory(lst));
		outputMap.put("customerDetails", customerDetails);

		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			String appenders = "SalesSummary" + userId + customerDetails.get("customer_name").replaceAll(" ", "") + "("
					+ getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDate) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			new InvoiceHistoryPDFHelper().generatePDFForCustomerInvoiceHistory(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			rs.setAjaxData(appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateInvoicePDF(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String invoiceId = request.getParameter("invoiceId");

		String appenders = "Invoice" + invoiceId + ".pdf";

		try {

			generateInvoicePDFService(request, con);

			rs.setAjaxData(appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject generateInvoicePDFBattery(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String invoiceId = request.getParameter("invoiceId");

		String appenders = "Invoice" + invoiceId + ".pdf";

		try {

			generateInvoicePDFBatteryService(request, con);

			rs.setAjaxData(appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public HashMap<String, Object> generateInvoicePDFBatteryService(HttpServletRequest request, Connection con)
			throws SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;

		String invoiceId = request.getParameter("invoiceId");

		String appenders = "Invoice" + invoiceId + ".pdf";
		DestinationPath += appenders;

		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}

		outputMap.put("user_id", userId);

		try {

			String invoiceFormatName = lObjConfigDao.getInvoiceFormatName(outputMap, con).get("format_name");

			new InvoiceHistoryPDFHelper().generatePDFForInvoice3InchBattery(DestinationPath, BufferedImagesFolderPath,
					lObjConfigDao.getInvoiceDetails(invoiceId, con), con);

			outputMap.put("returnData", appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			e.printStackTrace();

		}

		return outputMap;
	}

	public CustomResultObject generateInvoicePDFServiceAjax(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String invoiceId = request.getParameter("invoiceId");

		String appenders = "Invoice" + invoiceId + ".pdf";

		try {

			rs.setAjaxData(mapper.writeValueAsString(generateInvoicePDFService(request, con)));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public HashMap<String, Object> generateInvoicePDFService(HttpServletRequest request, Connection con)
			throws SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;

		String invoiceId = request.getParameter("invoiceId");

		String appenders = "Invoice" + invoiceId + ".pdf";
		DestinationPath += appenders;

		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}

		outputMap.put("user_id", userId);

		try {

			String invoiceFormatName = lObjConfigDao.getInvoiceFormatName(outputMap, con).get("format_name");

			if (invoiceFormatName.equals("A4FullPageFormat")) {
				new InvoiceHistoryPDFHelper().generatePDFForInvoice(DestinationPath, BufferedImagesFolderPath,
						lObjConfigDao.getInvoiceDetails(invoiceId, con), con);
			} else if (invoiceFormatName.equals("3InchFormat")) {
				new InvoiceHistoryPDFHelper().generatePDFForInvoice3Inch(DestinationPath, BufferedImagesFolderPath,
						lObjConfigDao.getInvoiceDetails(invoiceId, con), con);
			} else if (invoiceFormatName.equals("2InchFormat")) {
				new InvoiceHistoryPDFHelper().generatePDFForInvoice2Inch(DestinationPath, BufferedImagesFolderPath,
						lObjConfigDao.getInvoiceDetails(invoiceId, con), con);
			} else if (invoiceFormatName.equals("3InchWithWeightSize")) {
				new InvoiceHistoryPDFHelper().generatePDFForInvoice3InchWithWeightSize(DestinationPath, BufferedImagesFolderPath,
				lObjConfigDao.getInvoiceDetails(invoiceId, con), con);
			} else if (invoiceFormatName.equals("3InchWithModelNo")) {
				new InvoiceHistoryPDFHelper().generatePDFForInvoice3InchWithModelNo(DestinationPath,
						BufferedImagesFolderPath, lObjConfigDao.getInvoiceDetails(invoiceId, con), con);
			}

			outputMap.put("returnData", appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			e.printStackTrace();

		}

		return outputMap;
	}

	public HashMap<String, Object> generateKotPDFService(HashMap<String, Object> hm, String DestinationPath,
			String BufferedImagesFolder, Connection con) throws SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();

		List<HashMap<String, Object>> itemListRequired = (List<HashMap<String, Object>>) hm.get("itemDetails");

		HashMap<String, Object> item = itemListRequired.get(itemListRequired.size() - 1);

		String invoiceId = hm.get("order_id").toString();
		String appenders = "Kot" + invoiceId + ".pdf" + cf.getDateTimeWithSeconds(con);
		DestinationPath += appenders;

		try {

			String invoiceFormatName = lObjConfigDao.getInvoiceFormatName(hm, con).get("format_name");

			new InvoiceHistoryPDFHelper().generateKotPDFService3Inch(DestinationPath, BufferedImagesFolder, hm);

			outputMap.put("returnData", appenders);
		} catch (Exception e) {
			writeErrorToDB(e);

		}

		return outputMap;
	}

	public CustomResultObject exportCustomerLedgerAsPDF(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("fromDate").toString();
		String toDate = request.getParameter("toDate").toString();
		String toDateDisplay = request.getParameter("toDate").toString();
		String customerId = request.getParameter("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerReport(customerId, fromDate, toDate,
				con);

		LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);

		Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(toDateDate);
		cal.add(Calendar.DATE, -1);
		toDateDate = cal.getTime();

		toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
		String startOfApplication = "23/01/1992";
		String pendingAmount = lObjConfigDao
				.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
				.get("PendingAmount");

		Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
		totalDetails.put("openingAmount", openingAmount);
		Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
				+ Double.parseDouble(totalDetails.get("creditSum").toString());
		totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
		outputMap.put("totalDetails", totalDetails);

		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDateDisplay);
		// outputMap.put("totalDetails",gettotalDetailsLedger(lst));
		outputMap.put("customerDetails", customerDetails);
		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

			String appenders = "CustomerLedger" + userId + customerDetails.get("customer_name").replaceAll(" ", "")
					+ "(" + getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDateDisplay) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			new InvoiceHistoryPDFHelper().generatePDFForCustomerLedger(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportCustomerLedgerWithItemVehicleAsPDF(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("fromDate").toString();
		String toDate = request.getParameter("toDate").toString();
		String toDateDisplay = request.getParameter("toDate").toString();
		String customerId = request.getParameter("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerItemReport(customerId, fromDate, toDate,
				con);

		LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedgerVehicle(lst);

		Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(toDateDate);
		cal.add(Calendar.DATE, -1);
		toDateDate = cal.getTime();

		toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
		String startOfApplication = "23/01/1992";
		String pendingAmount = lObjConfigDao
				.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
				.get("PendingAmount");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		

		Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
		totalDetails.put("openingAmount", String.valueOf(openingAmount));
		Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
				+ Double.parseDouble(totalDetails.get("creditSum").toString());
		totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
		outputMap.put("totalDetails", totalDetails);
		outputMap.put("storeDetails", lObjConfigDao.getStoreDetails(Long.valueOf(storeId), con));

		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDateDisplay);
		// outputMap.put("totalDetails",gettotalDetailsLedger(lst));
		outputMap.put("customerDetails", customerDetails);
		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

			String appenders = "CustomerLedger" + userId + customerDetails.get("customer_name").replaceAll(" ", "")
					+ "(" + getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDateDisplay) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			String BufferedImagesFolder = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
			new InvoiceHistoryPDFHelper().generatePDFForCustomerLedgerWithItemVehicle(DestinationPath,BufferedImagesFolder, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}
	

	public CustomResultObject exportCustomerLedgerWithItemAsPDF(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("fromDate").toString();
		String toDate = request.getParameter("toDate").toString();
		String toDateDisplay = request.getParameter("toDate").toString();
		String customerId = request.getParameter("customerId").toString();
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerItemReport(customerId, fromDate, toDate,
				con);

		LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);

		Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(toDateDate);
		cal.add(Calendar.DATE, -1);
		toDateDate = cal.getTime();

		toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
		String startOfApplication = "23/01/1992";
		String pendingAmount = lObjConfigDao
				.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
				.get("PendingAmount");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		

		Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
		totalDetails.put("openingAmount", String.valueOf(openingAmount));
		Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
				+ Double.parseDouble(totalDetails.get("creditSum").toString());
		totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
		outputMap.put("totalDetails", totalDetails);
		outputMap.put("storeDetails", lObjConfigDao.getStoreDetails(Long.valueOf(storeId), con));

		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDateDisplay);
		// outputMap.put("totalDetails",gettotalDetailsLedger(lst));
		outputMap.put("customerDetails", customerDetails);
		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

			String appenders = "CustomerLedger" + userId + customerDetails.get("customer_name").replaceAll(" ", "")
					+ "(" + getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDateDisplay) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			String BufferedImagesFolder = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
			new InvoiceHistoryPDFHelper().generatePDFForCustomerLedgerWithItem(DestinationPath,BufferedImagesFolder, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject exportPDFForSalesSummary(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("txtfromdate").toString();
		String toDate = request.getParameter("txttodate").toString();
		String toDateDisplay = request.getParameter("txttodate").toString();
		String customerId = request.getParameter("customerId").toString();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		outputMap.put("store_id", storeId);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSalesItemSummary(appId, storeId, customerId,
				fromDate, toDateDisplay, con);

		LinkedHashMap<String, Object> totalDetails = gettotalDetailsSummary(lst);

		Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(toDateDate);
		cal.add(Calendar.DATE, -1);
		toDateDate = cal.getTime();

		toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
		String startOfApplication = "23/01/1992";
		String pendingAmount = lObjConfigDao
				.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
				.get("PendingAmount");

		LinkedHashMap<String, String> customerDetails = lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDateDisplay);
		outputMap.put("totalDetails", totalDetails);
		outputMap.put("customerDetails", customerDetails);
		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

			String appenders = "SalesSummary" + userId + customerDetails.get("customer_name").replaceAll(" ", "") + "("
					+ getDateASYYYYMMDD(fromDate) + ")" + "(" + getDateASYYYYMMDD(toDateDisplay) + ")"
					+ getDateTimeWithSeconds(con) + ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			new InvoiceHistoryPDFHelper().generatePDFForSalesSummary(DestinationPath, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showFSMLedger(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String employeeId = request.getParameter("employeeId") == null ? "" : request.getParameter("employeeId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("employeeMaster", lObjConfigDao.getEmployeeMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (employeeId.equals("")) {
			rs.setViewName("../FSMLedger.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "transaction_date", "Type", "RefId", "creditDebit", "upd1", "debitAmount",
					"creditAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getFSMLedger(employeeId, fromDate,
					toDate,appId, con);

					
			
			String openingBalanceForLedger = lObjConfigDao.getOpeningBalanceForLedger(employeeId, "23/01/1992",getDateFromDBMinusOneDay(getDateASYYYYMMDD(fromDate),con),appId, con);
			openingBalanceForLedger=openingBalanceForLedger==null?"0":openingBalanceForLedger;
			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				LinkedHashMap<String, Object> totalDetails = gettotalDetailsFSMLedger(lst);

				double differenceSum=   Double.valueOf(totalDetails.get("paymentAmtSum").toString())- Double.valueOf(totalDetails.get("salesAmtSum").toString());
				totalDetails.put("differenceSum", differenceSum);
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				double closingAmount=Double.valueOf(openingBalanceForLedger) + differenceSum;
				totalDetails.put("closingAmount", closingAmount);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				String pendingAmount = lObjConfigDao.getPendingAmountForThisCustomer(Long.valueOf(employeeId), startOfApplication, toDate, con).get("PendingAmount");
				//Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
				//totalDetails.put("openingAmount", openingAmount);
				//Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())+ Double.parseDouble(totalDetails.get("creditSum").toString());

				//totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
				outputMap.put("totalDetails", totalDetails);
				outputMap.put("openingBalanceForLedger", openingBalanceForLedger);


				
				outputMap.put("ListLedger", lst);
				outputMap.put("employeeDetails", lObjConfigDao.getEmployeeDetails(Long.valueOf(employeeId), con));

				rs.setViewName("../FSMLedger.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}
	

	public CustomResultObject showCustomerLedger(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../CustomerLedgerGenerated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "transaction_date", "Type", "RefId", "creditDebit", "upd1", "debitAmount",
					"creditAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerReport(customerId, fromDate,
					toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				String pendingAmount = lObjConfigDao
						.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
						.get("PendingAmount");
				Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
				totalDetails.put("openingAmount", openingAmount);
				Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
						+ Double.parseDouble(totalDetails.get("creditSum").toString());

				totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
				outputMap.put("totalDetails", totalDetails);

				outputMap.put("ListLedger", lst);
				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../CustomerLedgerGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showCustomerLedgerWithItemVehicle(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../CustomerLedgerItemVehicleGenerated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "transaction_date", "Type", "RefId", "creditDebit", "upd1", "debitAmount",
					"creditAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerItemReport(customerId, fromDate,
					toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				String pendingAmount = lObjConfigDao
						.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
						.get("PendingAmount");
				Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
				totalDetails.put("openingAmount", openingAmount);
				Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
						+ Double.parseDouble(totalDetails.get("creditSum").toString());

				totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
				outputMap.put("totalDetails", totalDetails);

				outputMap.put("ListLedger", lst);
				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../CustomerLedgerItemVehicleGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}
	

	public CustomResultObject showCustomerLedgerWithItem(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../CustomerLedgerItemGenerated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "transaction_date", "Type", "RefId", "creditDebit", "upd1", "debitAmount",
					"creditAmount" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerLedgerItemReport(customerId, fromDate,
					toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				String pendingAmount = lObjConfigDao
						.getPendingAmountForThisCustomer(Long.valueOf(customerId), startOfApplication, toDate, con)
						.get("PendingAmount");
				Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
				totalDetails.put("openingAmount", openingAmount);
				Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
						+ Double.parseDouble(totalDetails.get("creditSum").toString());

				totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
				outputMap.put("totalDetails", totalDetails);

				outputMap.put("ListLedger", lst);
				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../CustomerLedgerItemGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}


	public LinkedHashMap<String, Object> gettotalDetailsFSMLedger(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		double debitSum = 0;
		double creditSum = 0;
		for (LinkedHashMap<String, Object> tempHm : lst) {
			debitSum += Double.valueOf(tempHm.get("salesAmt").toString());
			creditSum += Double.valueOf(tempHm.get("paymentAmt").toString());
		}

		reqHM.put("salesAmtSum", String.format("%.2f", debitSum));
		reqHM.put("paymentAmtSum", String.format("%.2f", creditSum));
		//reqHM.put("differenceSum", String.format("%.2f", creditSum - debitSum));
		return reqHM;
	}

	public LinkedHashMap<String, Object> gettotalDetailsLedger(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		double debitSum = 0;
		double creditSum = 0;
		for (LinkedHashMap<String, Object> tempHm : lst) {
			debitSum += Double.valueOf(tempHm.get("debitAmount").toString());
			creditSum += Double.valueOf(tempHm.get("creditAmount").toString());
		}

		reqHM.put("debitSum", String.format("%.2f", debitSum));
		reqHM.put("creditSum", String.format("%.2f", creditSum));
		reqHM.put("totalAmount", String.format("%.2f", creditSum - debitSum));
		return reqHM;
	}



	public LinkedHashMap<String, Object> gettotalDetailsLedgerVehicle(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		double debitSum = 0;
		double creditSum = 0;
		for (LinkedHashMap<String, Object> tempHm : lst) {
			System.out.println(tempHm);
			
			double itemAmt=tempHm.get("itemAmount").equals("")?0: Double.valueOf(tempHm.get("itemAmount").toString());
			debitSum += itemAmt;
			creditSum += Double.valueOf(tempHm.get("creditAmount").toString());
		}

		reqHM.put("debitSum", String.format("%.2f", debitSum));
		reqHM.put("creditSum", String.format("%.2f", creditSum));
		reqHM.put("totalAmount", String.format("%.2f", creditSum - debitSum));
		return reqHM;
	}

	public LinkedHashMap<String, Object> gettotalDetailsSummary(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();

		double Amount = 0;
		double quantitySold = 0;

		double AmountSum = 0;
		double quantitySoldSum = 0;

		for (LinkedHashMap<String, Object> tempHm : lst) {

			String keyName = tempHm.get("category_name").toString() + "Amount";
			String ExistingAmount = reqHM.get(keyName) == null ? "0" : reqHM.get(keyName).toString();
			Amount = Double.valueOf(ExistingAmount) + Double.valueOf(tempHm.get("Amount").toString());
			reqHM.put(tempHm.get("category_name").toString() + "Amount", Amount);

			String keyNameQty = tempHm.get("category_name").toString() + "qtySold";
			String ExistingQty = reqHM.get(keyNameQty) == null ? "0" : reqHM.get(keyNameQty).toString();
			quantitySold = Double.valueOf(ExistingQty) + Double.valueOf(tempHm.get("quantity").toString());
			reqHM.put(tempHm.get("category_name").toString() + "qtySold", quantitySold);

			AmountSum += Double.valueOf(tempHm.get("Amount").toString());
			quantitySoldSum += quantitySold;

		}

		reqHM.put("AmountSum", AmountSum);
		reqHM.put("quantitySoldSum", quantitySoldSum);
		return reqHM;
	}

	public LinkedHashMap<String, Object> gettotalDetailsStockEvaluation(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		double EvaluationAtPrice = 0;
		double EvaluationAtAverageCost = 0;
		for (LinkedHashMap<String, Object> tempHm : lst) {
			EvaluationAtPrice += Double.valueOf(tempHm.get("EvaluationAtPrice").toString());
			EvaluationAtAverageCost += Double.valueOf(tempHm.get("EvaluationAtPurchase").toString());
		}

		reqHM.put("TotalEvaluationAtPrice", String.format("%.2f", EvaluationAtPrice));
		reqHM.put("TotalEvaluationAtAverageCost", String.format("%.2f", EvaluationAtAverageCost));

		return reqHM;
	}

	public LinkedHashMap<String, Object> gettotalDetailsInvoiceHistory(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		double ItemAmountSum = 0;
		double discountAmountSum = 0;
		double sumReturnQty = 0;
		double sumActualItemAmount = 0;
		for (LinkedHashMap<String, Object> tempHm : lst) {
			ItemAmountSum += Double.valueOf(tempHm.get("ItemAmount").toString());
			discountAmountSum += Double.valueOf(tempHm.get("DiscountAmount").toString());
			sumReturnQty += Double.valueOf(tempHm.get("sumReturnQty").toString());
			sumActualItemAmount += Double.valueOf(tempHm.get("ActualItemAmount").toString());

		}
		reqHM.put("ItemAmountSum", String.format("%.2f", ItemAmountSum));
		reqHM.put("discountAmountSum", String.format("%.2f", discountAmountSum));
		reqHM.put("sumReturnQty", String.format("%.2f", discountAmountSum));
		reqHM.put("sumActualItemAmount", String.format("%.2f", sumActualItemAmount));

		return reqHM;
	}

	public CustomResultObject showCustomerItemHistory(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException

	{
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		try {
			String[] colNames = { "formattedInvoiceDate", "invoice_id", "item_name", "qty", "qty_to_return",
					"BilledQty", "rate", "custom_rate", "DiscountAmount", "ItemAmount", "formattedUpdatedDate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerItemHistory(appId, customerId, fromDate,
					toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			}

			else {
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("CustomerInvoiceHistory", lst);

				rs.setViewName("../CustomerItemHistoryGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showItemWiseReports(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException

	{
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		try {
			String[] colNames = { "formattedInvoiceDate", "invoice_id", "item_name", "qty", "qty_to_return",
					"BilledQty", "rate", "custom_rate", "DiscountAmount", "ItemAmount", "formattedUpdatedDate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemWiseReports(appId, customerId, fromDate,
					toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			}

			else {
				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("CustomerInvoiceHistory", lst);

				rs.setViewName("../ItemWiseReports.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showCustomerItemRegisterParameter(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerMaster(outputMap, con);
			outputMap.put("customerList", lst);
			rs.setViewName("../CustomerItemReportParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showSalesItemSummaryParameter(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCustomerMaster(outputMap, con);
			outputMap.put("customerList", lst);
			rs.setViewName("../SalesItemSummaryParameter.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showSalesSummaryReport(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String customerId = request.getParameter("customerId") == null ? "" : request.getParameter("customerId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		outputMap.put("store_id", storeId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("customerMaster", lObjConfigDao.getCustomerMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		if (customerId.equals("")) {
			rs.setViewName("../SalesSummaryGenerated.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {

			String[] colNames = { "category_name", "item_name", "product_code", "Amount", "quantity", "qty_available" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSalesItemSummary(appId, storeId, customerId,
					fromDate, toDate, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CustomerInvoiceHistory");
			} else {

				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("CustomerInvoiceHistory", lst);
				outputMap.put("customerDetails", lObjConfigDao.getCustomerDetails(Long.valueOf(customerId), con));

				rs.setViewName("../SalesSummaryGenerated.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject getDataForGenerateInvoiceScreenAjax(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		LinkedHashMap<String, String> returnMap = null;

		try {

			rs.setAjaxData(mapper.writeValueAsString(getDataForGenerateInvoiceScreen(request, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> getDataForGenerateInvoiceScreen(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException

	{
		HashMap<String, Object> outputMap = new HashMap<>();

		outputMap.put("customerId", request.getParameter("customerId"));

		if (request.getParameter("customerId") != null && !request.getParameter("customerId").equals("")) {
			outputMap.put("routineDetails",
					lObjConfigDao.getRoutineDetailsForThisCustomer(request.getParameter("customerId"), con));
			outputMap.put("customerDetails",
					lObjConfigDao.getCustomerDetails(Long.valueOf(request.getParameter("customerId")), con));
		}

		outputMap.put("categoryId", request.getParameter("categoryId"));

		outputMap.put("app_id", request.getParameter("appId"));

		List<LinkedHashMap<String, Object>> itemMasterList = lObjConfigDao.getItemMaster(outputMap, con);
		List<LinkedHashMap<String, Object>> filteredItemMasterList = new ArrayList<>();
		for (LinkedHashMap<String, Object> item : itemMasterList) {
			LinkedHashMap<String, Object> tempHm = new LinkedHashMap<>();
			tempHm.put("item_id", item.get("item_id"));
			tempHm.put("parent_category_id", item.get("parent_category_id"));
			tempHm.put("debit_in", item.get("debit_in"));
			tempHm.put("item_name", item.get("item_name"));
			tempHm.put("product_code", item.get("product_code"));
			tempHm.put("category_name", item.get("category_name"));
			tempHm.put("price", item.get("price"));
			tempHm.put("ImagePath", item.get("ImagePath"));
			filteredItemMasterList.add(tempHm);
		}
		outputMap.put("itemMaster", filteredItemMasterList);

		List<LinkedHashMap<String, Object>> custList = lObjConfigDao.getCustomerMaster(outputMap, con);
		List<LinkedHashMap<String, Object>> filteredCustList = new ArrayList<>();
		for (LinkedHashMap<String, Object> custom : custList) {
			LinkedHashMap<String, Object> tempHm = new LinkedHashMap<>();
			tempHm.put("customerId", custom.get("customerId"));
			tempHm.put("customerName", custom.get("customerName"));
			tempHm.put("mobileNumber", custom.get("mobileNumber"));
			filteredCustList.add(tempHm);
		}
		outputMap.put("customerMaster", filteredCustList);
		// outputMap.put("isGeneratePDFChecked",
		// userdetails.invoice_default_checked_generatepdf); // need to code here

		outputMap.put("categoryMaster", lObjConfigDao.getCategoryMaster(outputMap, con));
		return outputMap;
	}

	public HashMap<String, Object> getInvoiceDetailsService(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();
		String invoiceId = request.getParameter("invoice_id");
		outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetails(invoiceId, con));
		return outputMap;
	}

	public CustomResultObject getInvoiceDetailsServiceAjax(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		LinkedHashMap<String, String> returnMap = null;

		try {

			rs.setAjaxData(mapper.writeValueAsString(getInvoiceDetailsService(request, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getInvoiceDetailsByNoAjax(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		try {

			HashMap<String, Object> outputMap = new HashMap<>();
			String invoiceNo = request.getParameter("invoiceNo");
			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

			String invoiceId = lObjConfigDao.getInvoiceIdByInvoiceNo(invoiceNo, appId, con).get("invoice_id");

			outputMap.put("app_id", appId);
			outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetails(invoiceId, con));

			rs.setAjaxData(mapper.writeValueAsString(outputMap));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getInvoiceDetailsByIdBypassed(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		try {

			HashMap<String, Object> outputMap = new HashMap<>();
			String invoiceId = request.getParameter("invoice_id");

			outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetails(invoiceId, con));

			rs.setAjaxData(mapper.writeValueAsString(outputMap));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> getInvoiceDetailsBasedOnBooking(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();
		String bookingId = request.getParameter("booking_id");
		LinkedHashMap<String, Object> hm = lObjConfigDao.getInvoiceDetailsForBooking(bookingId, con);
		List<LinkedHashMap<String, Object>> lst = (List<LinkedHashMap<String, Object>>) hm.get("listOfItems");
		hm.put("customer_id", lst.get(0).get("customer_id"));
		hm.put("customer_name", lst.get(0).get("customer_name"));
		hm.put("remarks", lst.get(0).get("remarks"));
		outputMap.put("invoiceDetails", hm);
		return outputMap;
	}

	public LinkedHashMap<String, String> validateLoginForApp(HttpServletRequest request, Connection con)
			throws SQLException {
		LinkedHashMap<String, String> returnMap = null;
		String number = (request.getParameter("number"));
		String password = URLDecoder.decode(request.getParameter("password"));

		try {
			returnMap = lObjConfigDao.validateLoginForApp(number, password, con);
			if (returnMap.get("user_id") != null) {
				returnMap.put("validLogin", "true");
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}

		return returnMap;
	}

	public CustomResultObject validateLoginForAppAjax(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		LinkedHashMap<String, String> returnMap = null;

		try {
			String number = (request.getParameter("number"));
			String password = URLDecoder.decode(request.getParameter("password"));
			returnMap = lObjConfigDao.validateLoginForApp(number, password, con);
			if (returnMap.get("user_id") != null) {
				returnMap.put("validLogin", "true");
			}
			rs.setAjaxData(mapper.writeValueAsString(returnMap));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public LinkedHashMap<String, String> validateLoginForAppCustomer(HttpServletRequest request, Connection con)
			throws SQLException {
		LinkedHashMap<String, String> returnMap = null;
		String number = (request.getParameter("number"));
		String password = URLDecoder.decode(request.getParameter("password"));

		try {
			returnMap = lObjConfigDao.validateLoginForAppCustomer(number, password, con);
			if (returnMap.get("user_id") != null) {
				returnMap.put("validLogin", "true");
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}

		return returnMap;
	}

	public CustomResultObject getRecentInvoiceForThisUserAjax(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		LinkedHashMap<String, String> returnMap = null;

		try {

			rs.setAjaxData(mapper.writeValueAsString(getRecentInvoiceForThisUser(request, con)));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public LinkedHashMap<String, Object> getRecentInvoiceForThisUser(HttpServletRequest request, Connection con)
			throws SQLException {
		LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
		String userId = (request.getParameter("userId"));
		String appId = (request.getParameter("appId"));
		returnMap.put("user_id", userId);
		returnMap.put("app_id", appId);
		try {
			returnMap.put("ListOfInvoices", lObjConfigDao.getRecentInvoiceForUser(returnMap, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}

		return returnMap;
	}

	public LinkedHashMap<String, Object> getBookingsForThisUser(HttpServletRequest request, Connection con)
			throws SQLException {
		LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
		String userId = (request.getParameter("userId"));
		String appId = (request.getParameter("appId"));
		returnMap.put("user_id", userId);
		returnMap.put("app_id", appId);
		try {
			returnMap.put("ListOfBookings", lObjConfigDao.getBookingsForThisUser(returnMap, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}

		return returnMap;
	}

	public CustomResultObject addBooking(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> hm = new HashMap<>();
		String appId = request.getParameter("app_id");
		hm.put("app_id", appId);

		String store_id = request.getParameter("store_id");
		hm.put("store_id", store_id);

		try {
			Enumeration<String> params = request.getParameterNames();

			List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
			while (params.hasMoreElements()) {
				String paramName = params.nextElement();

				if (paramName.equals("itemDetails")) {
					String[] itemsList = request.getParameter(paramName).split("\\|");
					for (String item : itemsList) {
						String[] itemDetails = item.split("~");
						HashMap<String, Object> itemDetailsMap = new HashMap<>();
						itemDetailsMap.put("item_id", itemDetails[0]);
						itemDetailsMap.put("qty", itemDetails[1]);
						itemListRequired.add(itemDetailsMap);
						// ID, QTY
					}
					hm.put("itemDetails", itemListRequired);
					continue;
				}
				hm.put(paramName, request.getParameter(paramName));

			}
			/*
			 * booking commented for bike services
			 * if(lObjConfigDao.checkIfBookingAlreadyExist(hm,con)) {
			 * rs.setReturnObject(hm);
			 * rs.setAjaxData("Booking Already Exists for another Customer"); return rs; }
			 */
			long bookingId = lObjConfigDao.saveBooking(hm, con);
			for (HashMap<String, Object> item : itemListRequired) {
				item.put("bookingId", bookingId);
				lObjConfigDao.saveBookingItems(item, con);
			}

			if (hm.get("mobile_booking_id") != null && !hm.get("mobile_booking_id").equals("")) {

				hm.put("acceptFlag", "A");
				lObjConfigDao.updateMobileBookingStatus(hm, con);
			}

		} catch (Exception e) {

			e.printStackTrace();
			rs.setHasError(true);
		}
		rs.setReturnObject(hm);
		rs.setAjaxData("Booking Updated Succesfully");
		return rs;
	}

	public HashMap<String, Object> getDataForHomePageAsJson(HttpServletRequest request, Connection con)
			throws SQLException {
		HashMap<String, Object> returnMap = null;
		ConfigurationDaoImpl configDaoImpl = new ConfigurationDaoImpl();
		long appId = Long.parseLong(request.getParameter("appId"));
		try {
			returnMap = new HashMap<>();
			/* returnMap.put("catList",configDaoImpl.getCategories()); */
			returnMap.put("sliderImagesPath", configDaoImpl.getSliderImages(appId, con));
			/* returnMap.put("productsListForDisplay",webDaoImpl.getProducts()); */

			returnMap.put("itemNamesForSearch", configDaoImpl.getItemNamesForSearchAutocomplete(appId, con));

			returnMap.put("categoryNameWithImage", configDaoImpl.CategoryNameWithImage(appId, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return returnMap;
	}

	public HashMap<String, Object> getItemsByCategoryIdApp(HttpServletRequest request, Connection con)
			throws SQLException {
		HashMap<String, Object> returnMap = null;
		String category_id = (request.getParameter("category_id"));
		long appId = Long.parseLong(request.getParameter("appId"));

		try {
			returnMap = new HashMap<>();
			returnMap.put("productsList", lObjConfigDao.getProductsByCategoryId(appId, category_id, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return returnMap;
	}

	public HashMap<String, Object> getProductDetailsByIdForCart(HttpServletRequest request, Connection con)
			throws SQLException {

		HashMap<String, Object> returnMap = new HashMap<>();

		try {

			String s = request.getParameter("cartString");
			String appId = request.getParameter("appId");

			JSONArray jsonArray = new JSONArray(s);

			List<HashMap<String, Object>> lstHashMap = new ArrayList<>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				HashMap<String, Object> map = new HashMap<>();
				returnMap.put("app_id", appId);
				returnMap.put("item_id", object.getInt("itemID"));
				map = lObjConfigDao.getItemdetailsById(returnMap, con);
				/* map.put("remarks", object.getString("remarks")); */
				if (!map.isEmpty()) {
					map.put("count", String.valueOf(object.getDouble("count")));
					lstHashMap.add(map);
				}
			}

			// List<HashMap<String, Object>> lstHashMap= getHashmapFromCartString(s);

			/// List<HashMap<String,Object>>
			/// hm1=dao.getProductDetailsByIdForCart(lstHashMap);
			HashMap<String, Object> TotalDetails = new HashMap<>();
			double total = 0;
			double totalCount = 0;
			for (HashMap<String, Object> tempHashmap : lstHashMap) {
				total += Double.valueOf(tempHashmap.get("price").toString());
				totalCount += Double.valueOf(tempHashmap.get("count").toString());
			}

			TotalDetails.put("TotalAmount", total);
			TotalDetails.put("TotalCount", totalCount);

			returnMap.put("itemDetails", lstHashMap);
			returnMap.put("totalDetails", TotalDetails);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return returnMap;
	}

	public HashMap<String, Object> SearchItemsByString(HttpServletRequest request, Connection con) throws SQLException {
		HashMap<String, Object> returnMap = null;
		String lStrSearchText = request.getParameter("searchtext");
		long appId = Long.parseLong(request.getParameter("appId"));
		try {
			returnMap = new HashMap<>();
			returnMap.put("productsList", lObjConfigDao.getItemsBySearchString(appId, lStrSearchText, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return returnMap;
	}

	public HashMap<String, Object> getAboutUsDetails(HttpServletRequest request, Connection con) throws SQLException {
		HashMap<String, Object> returnMap = null;

		long appId = Long.parseLong(request.getParameter("appId"));
		try {
			returnMap = new HashMap<>();
			returnMap.put("aboutUsDetails", lObjConfigDao.getAboutUsDetails(appId, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		}
		return returnMap;
	}

	public CustomResultObject showInvoiceConfig(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		try {
			rs.setViewName("../InvoiceConfig.jsp");
			rs.setReturnObject(showInvoiceConfigService(request, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject createNewApp(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			rs.setViewName("../CreateNewApp.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			Connection con;
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public HashMap<String, Object> showInvoiceConfigService(HttpServletRequest request, Connection con)
			throws SQLException {

		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = request.getParameter("appId");
		if (appId == null || appId.equals("")) {
			appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		}
		outputMap.put("app_id", appId);
		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}
		try {
			outputMap.put("listOfInvoiceFormats", lObjConfigDao.getInvoiceFormatList(outputMap, con));
			outputMap.put("listOfInvoiceTypes", lObjConfigDao.getInvoiceTypeList(outputMap, con));
			// outputMap.put("userDetails",
			// lObjConfigDao.getuserDetailsById(Long.valueOf(userId),con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));

		}
		return outputMap;
	}

	public static void SendOtp(String Number, String Otp) throws Exception {
		String apikey = "69b2fb1f-5271-11e8-a895-0200cd936042";
		String phonenum = Number;
		String otpval = Otp;

		// Prepare Url
		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		// Send SMS API
		String mainUrl = "https://2Factor.in/API/V1/" + apikey + "/SMS/" + phonenum + "/" + otpval + "/Gloria OTP";

		try {
			// prepare connection
			myURL = new URL(mainUrl);
			myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
			// reading response
			String response;
			while ((response = reader.readLine()) != null)
				// print response
				System.out.println(response);

			// finally close connection
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public CustomResultObject saveQuote(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		rs.setAjaxData(saveQuoteService(request, con).get("returnMessage").toString());
		return rs;
	}

	public HashMap<String, Object> saveQuoteService(HttpServletRequest request, Connection con) throws Exception {
		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					itemDetailsMap.put("rate", itemDetails[2]);
					itemDetailsMap.put("custom_rate", itemDetails[3]);
					itemDetailsMap.put("item_name", itemDetails[4]);
					if (itemDetails.length == 6) {
						itemDetailsMap.put("gst_amount", itemDetails[5]);
					}
					itemListRequired.add(itemDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}

		String userId = request.getParameter("user_id");
		String stringTerms = request.getParameter("stringTerms");
		String[] stringTermsArray = stringTerms.split("\\|");

		String storeId = request.getParameter("store_id");
		String appId = request.getParameter("appId");
		String appType = "";

		String hdnPreviousInvoiceId = request.getParameter("hdnPreviousInvoiceId");
		request.setAttribute("invoiceId", hdnPreviousInvoiceId);
		if (hdnPreviousInvoiceId != null && !hdnPreviousInvoiceId.equals("") && !hdnPreviousInvoiceId.equals("0")) {
			deleteInvoice(request, con);
		}

		if (appId == null || appId.equals("")) {
			appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		}
		hm.put("app_id", appId);
		hm.put("stringTermsArray", stringTermsArray);

		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
			storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		}

		hm.put("user_id", userId);
		hm.put("store_id", storeId);
		String customer_id = request.getParameter("customer_id").equals("") ? "0" : request.getParameter("customer_id");
		hm.put("customer_id", customer_id);
		try {

			if (!isValidateGrossWithIndividualAmount(hm)) {
				throw new Exception("invalid Gross with Individual Amount");
			}

			if (!isValidateTotalWithGrossMinusDiscounts(hm)) {
				throw new Exception("Invalid Total Amount Received Vs Calculated");
			}

			HashMap<String, Object> returnMap = lObjConfigDao.saveQuote(hm, con);
			hm.put("quote_id", returnMap.get("quote_id"));
			hm.put("returnMessage", returnMap.get("quote_no") + "~" + returnMap.get("quote_id"));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			throw e;
		}
		return hm;
	}

	public CustomResultObject generateDailyQuoteReport(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("drpstoreId") == null ? "" : request.getParameter("drpstoreId");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		if (storeId.equals("")) {
			storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		try {
			String[] colNames = { "customer_name", "total_amount", "invoice_no", "FormattedInvoiceDate", "updatedDate",
					"payment_type", "payment_mode", "name", "store_name" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getDailyQuoteDetails(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"DailyInvoiceReport");
			} else {
				outputMap.put("dailyQuoteData", lst);
				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("listStoreData", lObjConfigDao.getStoreMaster(hm, con));
				outputMap.put("txtfromdate", fromDate);
				outputMap.put("txttodate", toDate);
				outputMap.put("drpStoreId", storeId);

				rs.setViewName("../DailyQuoteReportGenerated.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject generateQuotePDF(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String quoteId = request.getParameter("quoteId");

		String appenders = "Quote" + quoteId + ".pdf";

		try {

			generateQuotePDFService(request, con);

			rs.setAjaxData(appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public HashMap<String, Object> generateQuotePDFService(HttpServletRequest request, Connection con)
			throws SQLException {
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;

		String quoteId = request.getParameter("quoteId");

		String appenders = "Quote" + quoteId + ".pdf";
		DestinationPath += appenders;

		String userId = request.getParameter("userId");
		if (userId == null || userId.equals("")) {
			userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		}

		outputMap.put("user_id", userId);

		try {

			LinkedHashMap<String, Object> quoteDetails = lObjConfigDao.getQuoteDetails(quoteId, con);
			List terms = lObjConfigDao.getTermsAndConditionsForQuote(con, quoteId);

			new QuotePDFHelper().generateQuotePDF(DestinationPath, quoteDetails, terms);
			outputMap.put("returnData", appenders);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));

		}

		return outputMap;
	}

	public CustomResultObject savePurchaseInvoice(HttpServletRequest request, Connection con) throws Exception {
		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		CustomResultObject rs = new CustomResultObject();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();
			if (paramName.equals("itemDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					// ID,WAREHOUSEID,JOBSHEETNO, QTY, RATE,GSTAMOUNT,GSTPERCENT,ITEMAMOUNT
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					itemDetailsMap.put("rate", itemDetails[2]);
					itemDetailsMap.put("sgst_amount", itemDetails[3]);
					itemDetailsMap.put("sgst_percentage", itemDetails[4]);
					itemDetailsMap.put("cgst_amount", itemDetails[5]);
					itemDetailsMap.put("cgst_percentage", itemDetails[6]);
					itemDetailsMap.put("item_amount", itemDetails[7]);

					itemListRequired.add(itemDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("itemDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));
		}

		String userId = request.getParameter("user_id");
		String store_id = request.getParameter("store_id");
		String appId = request.getParameter("appId");
		String txttallyrefno = request.getParameter("txttallyrefno");
		String txtvendorinvoiceno = request.getParameter("txtvendorinvoiceno");

		hm.put("txttallyrefno", txttallyrefno);
		hm.put("txtvendorinvoiceno", txtvendorinvoiceno);
		hm.put("type", "P");

		String hdnPreviousInvoiceId = request.getParameter("hdnPreviousInvoiceId");
		if (hdnPreviousInvoiceId != null && !hdnPreviousInvoiceId.equals("") && !hdnPreviousInvoiceId.equals("0")) {
			deleteInvoice(request, con);
		}
		if (appId == null || appId.equals("")) {
			appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		}
		hm.put("app_id", appId);
		userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		store_id = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		hm.put("user_id", userId);
		hm.put("store_id", store_id);

		String customer_id = request.getParameter("customer_id").equals("") ? "0" : request.getParameter("customer_id");
		hm.put("customer_id", customer_id);
		try {
			HashMap<String, Object> returnMap = lObjConfigDao.savePurchaseInvoice(hm, con);
			hm.put("invoice_id", returnMap.get("invoice_id"));
			rs.setAjaxData(returnMap.get("invoice_no") + "~" + returnMap.get("invoice_id"));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			throw e;
		}
		return rs;
	}

	private void addStockCopy(HashMap<String, Object> hm, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {
			String userId = (hm.get("user_id").toString());
			String appId = (hm.get("app_id").toString());

			String actiontype = hm.get("action").toString();

			List<HashMap<String, Object>> items = (List<HashMap<String, Object>>) hm.get("itemDetails");

			outputMap.put("app_id", appId);
			long store_id = Long.parseLong(hm.get("store_id").toString());

			outputMap.put("user_id", userId);
			outputMap.put("drpstoreId", store_id);
			outputMap.put("outerRemarks", "Purchase");

			outputMap.put("userId", userId);
			outputMap.put("storeId", store_id);

			String StockModificationType = actiontype.equals("Remove") ? "Damage" : "StockIn";
			outputMap.put("type", StockModificationType);

			long stockModificationId = lObjConfigDao.addStockModification(outputMap, con);

			for (HashMap<String, Object> item : items) {

				long item_id = Long.parseLong(item.get("item_id").toString());
				double qty = Double.parseDouble(item.get("qty").toString());

				outputMap.put("drpitems", item_id);
				outputMap.put("qty", qty);
				outputMap.put("remarks", "");

				outputMap.put("type", "StockIn");
				if (actiontype.equals("Remove")) {
					outputMap.put("qty", Double.parseDouble(outputMap.get("qty").toString()) * -1);
					outputMap.put("type", "Damage");
				}
				if (lObjConfigDao.checkifStockAlreadyExist(store_id, item_id, con).equals("0")) {
					lObjConfigDao.addStockMaster(outputMap, con);
				}

				lObjConfigDao.addStockRegister(outputMap, con);
				outputMap.put("stock_id", lObjConfigDao.checkifStockAlreadyExist(store_id, item_id, con));
				lObjConfigDao.updateStockMaster(outputMap, con);

				outputMap.put("stockModificationId", stockModificationId);
				outputMap.put("itemId", item_id);
				outputMap.put("currentStock", 0);
				outputMap.put("qty", qty);

				lObjConfigDao.addStockModificationAddRemove(outputMap, con);

			}

			rs.setReturnObject(outputMap);
			rs.setAjaxData("Stock Updated Succesfully:-" + stockModificationId);

		} catch (Exception e) {
			writeErrorToDB(e);
			throw e;
		}
	}

	public CustomResultObject showInvoices(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {

		String storeId = request.getParameter("storeId") == null ? "" : (request.getParameter("storeId").toString());

		if (storeId.equals("")) {
			storeId = (((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id"));
		}

		long app_id = Long
				.valueOf(((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id"));

		String type = request.getParameter("type") == null ? "" : request.getParameter("type");

		String fromDate = request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate");
		if (fromDate == null || fromDate.equals("")) {
			fromDate = getDateFromDB(con);
		}

		if (toDate == null || toDate.equals("")) {
			toDate = getDateFromDB(con);
		}

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		outputMap.put("type", type);
		outputMap.put("storeId", storeId);
		outputMap.put("app_id", app_id);

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		List<LinkedHashMap<String, Object>> lst;

		lst = lObjConfigDao.getInvoicesPurchase(outputMap, con);

		try {

			outputMap.put("ListOfStores", lObjConfigDao.getStoreMaster(outputMap, con));
			outputMap.put("listReturnData", lst);
			rs.setViewName("../Invoices.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject deleteQuote(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long quoteId = Long.parseLong(request.getParameter("quoteId"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteQuote(quoteId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCategoryWiseStoreWiseSales(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("storeId") == null ? "" : request.getParameter("storeId");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("categoryMaster", lObjConfigDao.getCategoryMaster(outputMap, con));
		outputMap.put("storeMaster", lObjConfigDao.getStoreMaster(outputMap, con));
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("ListOfStores", lObjConfigDao.getStoreMaster(outputMap, con));

		if (storeId.equals("")) {
			rs.setViewName("../showCategoryWiseStoreWiseSales.jsp");
			rs.setReturnObject(outputMap);
			return rs;
		}

		try {
			String[] colNames = { "category_name", "store_name", "custom_rate" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCategoryWiseStoreWiseDetails(appId, fromDate,
					toDate, storeId, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, appId,
						"CategoryWiseStoreWiseDetails");
			} else {

				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("CategoryAndStoreLedger", lst);

				rs.setViewName("../showCategoryWiseStoreWiseSales.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject addVisitor(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFactory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFactory);
		String appId = request.getParameter("app_id");
		outputMap.put("app_id", appId);
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long visitorId = hm.get("hdnvisitorId").equals("") ? 0l : Long.parseLong(hm.get("hdnvisitorId").toString());
		try {

			if (visitorId == 0) {
				visitorId = lObjConfigDao.AddVisitor(con, hm);
			} else {
				lObjConfigDao.updateVisitor(visitorId, con, hm);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showVisitors'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showVisitors(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("storeId") == null ? "" : request.getParameter("storeId");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		try {
			String[] colNames = { "visitorId", "visitorname", "address", "EmailId" };

			List<LinkedHashMap<String, Object>> lst = null;

			lst = lObjConfigDao.showVisitors(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "VisitorEntry");
			} else {

				Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

				Calendar cal = Calendar.getInstance();
				cal.setTime(toDateDate);
				cal.add(Calendar.DATE, -1);
				toDateDate = cal.getTime();

				toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);
				String startOfApplication = "23/01/1992";
				outputMap.put("ListOfVisitors", lst);

				rs.setViewName("../Visitors.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddVisitor(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long visitorId = request.getParameter("visitorId") == null ? 0L
				: Long.parseLong(request.getParameter("visitorId"));

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {
			if (visitorId != 0) {
				outputMap.put("visitorDetails", lObjConfigDao.getvisitorDetails(visitorId, con));
			}
			outputMap.put("distinctPurposeOfVisist", lObjConfigDao.getDistinctPurposeOfVisitList(con, appId));
			rs.setViewName("../AddVisitor.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteVisitor(HttpServletRequest request, Connection con) throws SQLException {

		CustomResultObject rs = new CustomResultObject();
		long visitorId = Long.parseLong(request.getParameter("visitorId"));
		try {

			rs.setAjaxData(lObjConfigDao.deleteVisitor(visitorId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public LinkedHashMap<String, Object> gettotalDetailsSalesReport3(List<LinkedHashMap<String, Object>> lst) {
		LinkedHashMap<String, Object> reqHM = new LinkedHashMap<>();
		BigDecimal amount = new BigDecimal(0);
		for (LinkedHashMap<String, Object> tempHm : lst) {
			amount = amount.add(new BigDecimal(tempHm.get("amt").toString()));

		}

		reqHM.put("totalAmount", String.format("%.2f", amount));
		return reqHM;
	}

	public CustomResultObject showForgotPasswordScreen(HttpServletRequest request, Connection connections) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		rs.setViewName("ResetPassword.jsp");
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject sendResetPasswordEmail(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String emailid = request.getParameter("emailid");
		String returnMessage = "";
		try {

			// check if the provided email exists in our system
			if (!lObjConfigDao.getuserDetailsByEmailId(emailid, con).isEmpty()) {

				List<String> ls = new ArrayList<>();
				String newTempPassword = cf.getRandomNumberByLength(10);
				sendMessage(emailid, "crystaldevelopers2017@gmail.com", ls, ls,
						"Your Temporary Password for 'Hisaab Cloud Application' is <b>" + newTempPassword + "</b>",
						"Reset Password Requested");
				new LoginDaoImpl().changePasswordByEmailId(emailid, newTempPassword, con);
				returnMessage = "Temporary Password Sent to " + emailid + " Succesfully ";
			} else {
				returnMessage = "Please check the email and try again";
			}

			rs.setAjaxData(returnMessage);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public void sendMessage(String toAddress, String ccAddress, List<String> CCOtherEmailsList,
			List<String> attachmentPaths, String emailContent, String emailSubject)
			throws AddressException, MessagingException {

		Properties props = new Properties();
		/* System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2"); */
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		// props.put("mail.debug", "true");

		/*
		 * final String fromAddress="snr@ssegpl.com"; final String
		 * fromPassword="ssegpl@123";
		 */

		final String fromAddress = "crystaldevelopers2017@gmail.com";
		final String fromPassword = "limsndhrlhevyfut";

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromAddress, fromPassword);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromAddress));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));

		CCOtherEmailsList = new ArrayList<>(CCOtherEmailsList);
		String[] ccAddressArray = ccAddress.split(",");
		CCOtherEmailsList.addAll(Arrays.asList(ccAddressArray));

		InternetAddress[] recipientAddress = new InternetAddress[CCOtherEmailsList.size()];
		int counter = 0;
		for (String recipient : CCOtherEmailsList) {
			if (!recipient.equals("")) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
		}
		message.setRecipients(Message.RecipientType.CC, recipientAddress);

		message.setSubject(emailSubject);

		// message.setText("Please Find attached Quotes and Respective Technicals.");

		MimeBodyPart messageBodyPart = new MimeBodyPart();

		Multipart multipart = new MimeMultipart();

		/*
		 * for(String s:attachmentPaths) { addAttachment(multipart,s); }
		 */

		BodyPart htmlBodyPart = new MimeBodyPart();
		htmlBodyPart.setContent(emailContent, "text/html");
		multipart.addBodyPart(htmlBodyPart);

		message.setContent(multipart);

		Transport.send(message);
	}

	public CustomResultObject showSalesReport4(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String storeId = request.getParameter("drpstoreId") == null ? "" : request.getParameter("drpstoreId");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		HashMap<String, Object> hm = new HashMap<>();
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("storeId", storeId);

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		try {
			String[] colNames = { "FormattedInvoiceDate", "updatedDate", "payment_type", "customer_name",
					"category_name", "item_name", "qty", "weight", "amt", "item_discount", "invoice_no" };

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSalesReport4(hm, con);

			if (!exportFlag.isEmpty()) {
				outputMap.put("lst", lst);

				outputMap = getCommonFileGeneratorWithTotal(colNames, outputMap, exportFlag, DestinationPath, userId,
						"DailyInvoiceReport");
			} else {

				outputMap.put("dailyInvoiceData", lst);
				outputMap.put("listStoreData", lObjConfigDao.getStoreMaster(hm, con));
				outputMap.put("txtfromdate", fromDate);
				outputMap.put("txttodate", toDate);
				outputMap.put("drpStoreId", storeId);

				outputMap.put("app_id", appId);

				rs.setViewName("../SalesRegister4.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showFuelMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "nozzle_id", "item_name" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getFuelMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "FuelMaster");
			} else {
				outputMap.put("ListOfFuels", lst);
				rs.setViewName("../FuelMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showNozzleMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "nozzle_id", "nozzle_name", "item_name" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getNozzleMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfNozzle", lst);
				rs.setViewName("../NozzleMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject addFuel(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String fuelName = hm.get("fuelName").toString();
		String appId = hm.get("app_id").toString();

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		hm.put("app_id", appId);
		hm.put("user_id", userId);

		long fuelId = hm.get("hdnFuelId").equals("") ? 0l : Long.parseLong(hm.get("hdnFuelId").toString());
		try {

			if (fuelId == 0) {
				fuelId = lObjConfigDao.addFuel(con, hm);
			} else {
				lObjConfigDao.updateFuel(fuelId, con, fuelName);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showFuelMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject checkInNozzle(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String nozzleId = hm.get("nozzle_id").toString();
		String opening_reading = hm.get("opening_reading").toString();
		String totalizer_opening_reading = hm.get("totalizer_opening_reading").toString();
		String appId = hm.get("app_id").toString();
		String drpattendantid = hm.get("drpattendantid").toString();
		String accountingDate = hm.get("accountingDate").toString();

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		hm.put("app_id", appId);
		hm.put("user_id", userId);
		hm.put("nozzle_id", nozzleId);
		hm.put("opening_reading", opening_reading);
		hm.put("totalizer_opening_reading", totalizer_opening_reading);

		try {

			lObjConfigDao.checkInNozzleNew(con, hm);

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showCheckInScreen'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject checkOutNozzle(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String trn_nozzle_id = hm.get("trn_nozzle_id").toString();
		String closing_reading = hm.get("closing_reading").toString();
		String closing_totalizer_amount = hm.get("closing_totalizer_amount").toString();

		String appId = hm.get("app_id").toString();

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		hm.put("app_id", appId);
		hm.put("user_id", userId);
		hm.put("trn_nozzle_id", trn_nozzle_id);
		hm.put("closing_reading", closing_reading);
		hm.put("totalizer_closing_reading", closing_totalizer_amount);

		try {

			// check status before checkIn

			lObjConfigDao.checkOutNozzle(con, hm);

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showCheckInScreen'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addNozzle(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}
		String NozzleName = hm.get("NozzleName").toString();
		String drpfueltype = hm.get("drpfueltype").toString(); // this will change
		String drpDispenserId = hm.get("drpDispenserId").toString();

		String appId = hm.get("app_id").toString();
		hm.put("app_id", appId);

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		long hdnNozzleId = hm.get("hdnNozzleId").equals("") ? 0l : Long.parseLong(hm.get("hdnNozzleId").toString());
		try {

			if (hdnNozzleId == 0) {
				hdnNozzleId = lObjConfigDao.addNozzle(con, hm);
			} else {
				lObjConfigDao.updateNozzle(hdnNozzleId, NozzleName, drpfueltype, drpDispenserId, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showNozzleMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteFuel(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long fuelid = Integer.parseInt(request.getParameter("fuelid"));

		try {

			rs.setAjaxData(lObjConfigDao.deleteFuel(fuelid, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteNozzle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long categoryId = Integer.parseInt(request.getParameter("nozzleid"));

		try {
			lObjConfigDao.deleteNozzle(categoryId, con);
			rs.setAjaxData("Nozzle Deleted Succesfully");
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddFuel(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long fuelId = request.getParameter("fuelId") == null ? 0L : Long.parseLong(request.getParameter("fuelId"));
		outputMap.put("fuelId", fuelId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (fuelId != 0) {
				outputMap.put("fuelDetails", lObjConfigDao.getFuelDetails(outputMap, connections));
			}
			rs.setViewName("../AddFuel.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddNozzle(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long nozzleId = request.getParameter("nozzleId") == null ? 0L
				: Long.parseLong(request.getParameter("nozzleId"));
		outputMap.put("nozzle_id", nozzleId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (nozzleId != 0) {
				outputMap.put("NozzleDetails", lObjConfigDao.getNozzleDetails(String.valueOf(nozzleId), connections));
			}
			outputMap.put("listOfItems", lObjConfigDao.getItemMaster(outputMap, connections)); // this is where you
																								// should have written
																								// your code
			outputMap.put("listOfDispensers", lObjConfigDao.getDispenserMaster(outputMap, connections));
			rs.setViewName("../AddNozzle.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCheckInScreen(HttpServletRequest request, Connection connections)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		outputMap.put("user_id", userId);

		try {

			// get list of available nozzles

			// get list of all nozels
			List<LinkedHashMap<String, Object>> lstNozells = lObjConfigDao.getNozzleMaster(outputMap, connections);
			List<LinkedHashMap<String, Object>> lstNozellsWithMax = new ArrayList<>();

			// get max check_in_time for each nozell
			for (LinkedHashMap<String, Object> hm : lstNozells) {
				LinkedHashMap<String, String> lm = lObjConfigDao
						.getLatestNozzelByCheckout(hm.get("nozzle_id").toString(), connections);
				if (lm.isEmpty()) {
					lm.put("status", "New");
				}
				hm.putAll(lm);
				lstNozellsWithMax.add(hm);
			}

			LinkedHashMap<String, List> finalDispenserMap = new LinkedHashMap<>();

			for (LinkedHashMap<String, Object> lm : lstNozellsWithMax) {
				lm.get("dispenser_name");

				if (finalDispenserMap.get(lm.get("dispenser_name")) != null) {
					List ls = finalDispenserMap.get(lm.get("dispenser_name"));
					ls.add(lm);
					finalDispenserMap.put(lm.get("dispenser_name").toString(), ls);
				} else {
					List ls = new ArrayList();
					ls.add(lm);
					finalDispenserMap.put(lm.get("dispenser_name").toString(), ls);
				}

			}

			// System.out.println(finalDispenserMap);
			outputMap.put("finalDispenserMap", finalDispenserMap);

			rs.setViewName("../NozzleSelection.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCheckOutScreen(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long nozzle_id = request.getParameter("nozzle_id") == null ? 0L
				: Long.parseLong(request.getParameter("nozzle_id"));
		String closing_reading = request.getParameter("closing_reading") == null ? ""
				: (request.getParameter("closing_reading"));

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			outputMap.put("nozzleDetails", lObjConfigDao.getNozzleDetails(String.valueOf(nozzle_id), con));

			rs.setViewName("../NozzleCheckoutScreen.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showNozzleSelectionForm(HttpServletRequest request, Connection connections)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String nozzle_id = request.getParameter("nozzle_id");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			outputMap.put("nozzle_id", nozzle_id);

			LinkedHashMap<String, String> nzDetails = lObjConfigDao.getNozzleDetails(nozzle_id, connections);
			String itemId = nzDetails.get("item_id");
			outputMap.put("item_id", itemId);

			outputMap.put("userList", lObjConfigDao.getEmployeeMaster(outputMap, connections));
			outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, connections));
			outputMap.put("dateTimeRightNow", lObjConfigDao.getDateTime(connections));
			outputMap.put("itemPrice", lObjConfigDao.getItemdetailsById(outputMap, connections).get("price"));
			outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, connections));
			outputMap.put("todaysDate", getDateFromDB(connections));
			outputMap.put("nzDetails", nzDetails);

			rs.setViewName("../NozzleSelectionForm.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showDispenserMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "dispenser_id", "dispenser_name", "activate_flag", "updated_by", "updated_date" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getDispenserMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfDispenser", lst);
				rs.setViewName("../DispenserMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddDispenser(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long dispenserId = request.getParameter("dispenserId") == null ? 0L
				: Long.parseLong(request.getParameter("dispenserId"));
		outputMap.put("dispenser_id", dispenserId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (dispenserId != 0) {
				outputMap.put("DispenserDetails", lObjConfigDao.getDispenserDetails(outputMap, connections));
			}
			outputMap.put("lisitOfDispensers", lObjConfigDao.getDispenserMaster(outputMap, connections));
			rs.setViewName("../AddDispenser.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addDispenser(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		String dispenser_name = hm.get("dispenser_name").toString();

		String appId = hm.get("app_id").toString();
		hm.put("app_id", appId);

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		long hdnDispenserId = hm.get("hdnDispenserId").equals("") ? 0l
				: Long.parseLong(hm.get("hdnDispenserId").toString());
		try {

			if (hdnDispenserId == 0) {
				hdnDispenserId = lObjConfigDao.addDispenser(con, hm);
			} else {
				lObjConfigDao.updateDispenser(hdnDispenserId, dispenser_name, userId, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showDispenserMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteDispenser(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long dispenserId = Integer.parseInt(request.getParameter("dispenserId"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteDispenser(dispenserId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCollectPaymentSupervisor(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String order_id = request.getParameter("order_id") == null ? "" : request.getParameter("order_id");
		String collectionMode = request.getParameter("collection_mode") == null ? ""
				: request.getParameter("collection_mode");
		outputMap.put("app_id", appId);

		try {

			outputMap.put("userList", lObjConfigDao.getDistinctEmployeesCheckedInToNozzle(appId, con));

			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("yesterdaysDate", lObjConfigDao.getYesterdaysDateFromDB(con));
			outputMap.put("tommorowsDate", lObjConfigDao.getTommorowsDateFromDB(con));
			outputMap.put("collection_mode", collectionMode);

			List<LinkedHashMap<String, Object>> lstOfShifts = lObjConfigDao.getShiftMaster(outputMap, con);

			List<LinkedHashMap<String, Object>> lstOfShiftsReOrdered = new ArrayList<>();
			boolean shouldAdd = true;

			for (LinkedHashMap<String, Object> shft : lstOfShifts) {
				if (shft.get("shift_name").equals("3") && shouldAdd) {
					lstOfShiftsReOrdered.add(shft);
					shouldAdd = false;
				}
			}

			for (LinkedHashMap<String, Object> shft : lstOfShifts) {

				lstOfShiftsReOrdered.add(shft);

			}
			boolean isFirst = true;
			boolean isLast = true;
			List<LinkedHashMap<String, Object>> lstOfFinalShiftsReOrdered = new ArrayList<>();
			for (LinkedHashMap<String, Object> shft : lstOfShiftsReOrdered) {

				LinkedHashMap<String, Object> newShft = new LinkedHashMap<String, Object>();
				newShft.putAll(shft);
				if (shft.get("shift_name").equals("3") && isFirst) {

					newShft.put("theDate", outputMap.get("todaysDate"));
					newShft.put("from_time", "00:00");
					newShft.put("to_time", "06:00");
					newShft.put("isFirst", "0");
					isFirst = false;

				} else if (shft.get("shift_name").equals("3") && !isFirst) {
					newShft.put("theDate", outputMap.get("todaysDate"));
					newShft.put("from_time", "22:00");
					newShft.put("to_time", "00:00");
					newShft.put("isFirst", "1");
					isFirst = false;
					System.out.println(newShft.get("shift_name") + "" + newShft.get("theDate"));
					lstOfFinalShiftsReOrdered.add(newShft);

					newShft = new LinkedHashMap<String, Object>();
					newShft.putAll(shft);
					newShft.put("theDate", outputMap.get("tommorowsDate"));
					newShft.put("from_time", "00:00");
					newShft.put("to_time", "06:00");
					newShft.put("isFirst", "1");

					System.out.println(newShft.get("shift_name") + "" + newShft.get("theDate"));
					lstOfFinalShiftsReOrdered.add(newShft);
					continue;

				} else {
					newShft.put("theDate", outputMap.get("todaysDate"));
					newShft.put("isFirst", "1");

				}
				System.out.println(newShft.get("shift_name") + "" + newShft.get("theDate"));
				lstOfFinalShiftsReOrdered.add(newShft);

			}

			for (LinkedHashMap<String, Object> shft : lstOfFinalShiftsReOrdered) {
				System.out.println(shft.get("shift_name") + "" + shft.get("theDate"));
			}

			outputMap.put("lstOfShifts", lstOfFinalShiftsReOrdered);
			outputMap.put("lstOfShiftsActual", lstOfShifts);

			String suggestedShift = lObjConfigDao.getSuggestedShiftId(outputMap, con);
			if (suggestedShift == null || suggestedShift.equals("")) {
				suggestedShift = "4";
			}
			outputMap.put("suggestedShiftId",
					outputMap.get("todaysDate") + "~" + suggestedShift + "~" + lObjConfigDao.isTimeBetween00to06(con));

			rs.setViewName("../CollectPaymentSupervisor.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showSubmitCashtoVault(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		outputMap.put("txtfromdate", lObjConfigDao.getDateFromDB(con));
		outputMap.put("txttodate", lObjConfigDao.getDateFromDB(con));

		outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));
		outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, con));
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		outputMap.put("user_id", userId);
		Double collectionSum = Double
				.valueOf(lObjConfigDao.getSumOfCollectionAmount(outputMap, con).get("mySum") == null ? "0"
						: lObjConfigDao.getSumOfCollectionAmount(outputMap, con).get("mySum"));
		Double vaultSum = Double.valueOf(lObjConfigDao.getVaultData(outputMap, con).get("vaultSum") == null ? "0"
				: lObjConfigDao.getVaultData(outputMap, con).get("vaultSum"));

		Double collectionData = collectionSum - vaultSum;

		outputMap.put("collectionData", collectionData);

		try {

			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));

			rs.setViewName("../SubmitCashtoVault.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject submitCashtoVault(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String notes = request.getParameter("notes");
		String coins = request.getParameter("coins");
		String collectionDate = request.getParameter("collectionDate");
		String shiftId = request.getParameter("shiftId");
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		outputMap.put("notes", notes);
		outputMap.put("coins", coins);
		outputMap.put("collectionDate", collectionDate);
		outputMap.put("shiftId", shiftId);
		outputMap.put("userId", userId);
		outputMap.put("app_id", appId);

		try {
			lObjConfigDao.submitCashtoVault(con, outputMap);
			rs.setAjaxData("Cash Submitted to Vault");
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveCollectPaymentSupervisor(HttpServletRequest request, Connection con)
			throws FileUploadException, SQLException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFactory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFactory);
		String appId = request.getParameter("app_id");
		outputMap.put("app_id", appId);
		HashMap<String, Object> hm = new HashMap<>();
		hm.put("app_id", appId);
		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		try {

			lObjConfigDao.saveCollectionSupervisor(con, hm);
			String action = hm.get("paytm_order_id").equals("") ? "showCollectPaymentSupervisor"
					: "showUnclaimedPayments";

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>alert('Payment Saved Succefully'); window.location='" + hm.get("callerUrl")
					+ "?a=" + action + "'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;

	}

	public CustomResultObject showShiftMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "shift_id", "shift_name", "from_time", "to_time", "activate_flag", "updated_by",
					"updated_date" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getShiftMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfShift", lst);
				rs.setViewName("../ShiftMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showClaimScreen(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String order_id = request.getParameter("order_id") == null ? "" : (request.getParameter("order_id"));

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			// only get the users who are alloted to nozzle
			outputMap.put("userList", lObjConfigDao.getEmployeesCheckedInToNozzle(appId, connections));
			outputMap.put("paymentDetails", lObjConfigDao.getUnclaimedPaymentDetails(order_id, connections));
			rs.setViewName("../ClaimPayment.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddTestFuel(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			String todaysDate = lObjConfigDao.getDateFromDB(con);
			outputMap.put("app_id", appId);

			// code to get listof shifts

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getActiveNozzles(outputMap, con);
			outputMap.put("userList", lObjConfigDao.getEmployeesCheckedInToNozzle(appId, con));
			outputMap.put("activeNozzles", lst);
			outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));
			outputMap.put("todaysDate", todaysDate);
			outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, con));

			outputMap.put("txtfromdate", todaysDate);
			outputMap.put("txttodate", todaysDate);

			List<LinkedHashMap<String, Object>> testData = lObjConfigDao.getTestData(outputMap, con);
			outputMap.put("testData", testData);

			// code to get list of active nozzle
			// code to get list of employees at active nozzles
			rs.setViewName("../AddTestFuel.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddPumpTest(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);

			// code to get listof shifts

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getActiveNozzles(outputMap, con);
			outputMap.put("userList", lObjConfigDao.getEmployeesCheckedInToNozzle(appId, con));
			outputMap.put("activeNozzles", lst);
			outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));

			// code to get list of active nozzle
			// code to get list of employees at active nozzles
			rs.setViewName("../AddPumpTest.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddShift(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long shiftId = request.getParameter("shiftId") == null ? 0L : Long.parseLong(request.getParameter("shiftId"));
		outputMap.put("shift_id", shiftId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (shiftId != 0) {
				outputMap.put("ShiftDetails", lObjConfigDao.getShiftDetails(outputMap, connections));
			}
			outputMap.put("lisitOfShift", lObjConfigDao.getShiftMaster(outputMap, connections));
			rs.setViewName("../AddShift.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addShift(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		String shift_name = hm.get("shift_name").toString();
		String from_time_hour = hm.get("from_time_hour").toString();
		String from_time_minute = hm.get("from_time_minute").toString();
		String to_time_hour = hm.get("to_time_hour").toString();
		String to_time_minute = hm.get("to_time_minute").toString();

		hm.put("shift_name", shift_name);
		hm.put("from_time_hour", from_time_hour);
		hm.put("from_time_minute", from_time_minute);
		hm.put("to_time_hour", to_time_hour);
		hm.put("to_time_minute", to_time_minute);

		String appId = hm.get("app_id").toString();
		hm.put("app_id", appId);

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		long hdnShiftId = hm.get("hdnShiftId").equals("") ? 0l : Long.parseLong(hm.get("hdnShiftId").toString());
		try {

			if (hdnShiftId == 0) {
				hdnShiftId = lObjConfigDao.addShift(con, hm);
			} else {
				lObjConfigDao.updateShift(hdnShiftId, shift_name, from_time_hour, from_time_minute, to_time_hour,
						to_time_minute, userId, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showShiftMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteShift(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long shiftId = Integer.parseInt(request.getParameter("shiftId"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteShift(shiftId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addTestFuel(HttpServletRequest request, Connection con)
			throws SQLException, ParseException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, String> hm = new HashMap<String, String>();

		String shift_id = request.getParameter("shift_id");
		String testDate = request.getParameter("testDate");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		hm.put("shift_id", shift_id);
		hm.put("testDate", testDate);
		hm.put("test_type", "S");

		String testInputData = request.getParameter("testInputData");
		String[] listTestdata = testInputData.split("\\|");
		for (String s : listTestdata) {
			String[] testData = s.split("~");
			hm.put("attendant_id", testData[0]);
			hm.put("testNozzle", testData[1]);
			hm.put("testQuantity", testData[2]);
			if (!testData[2].equals("0")) {
				lObjConfigDao.addTestFuel(con, hm);
			}

		}

		rs.setAjaxData("Data saved Succesfully");
		return rs;

	}

	public CustomResultObject addConfigureLR(HttpServletRequest request, Connection con)
			throws SQLException, ParseException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, String> hm = new HashMap<String, String>();
		String printer = request.getParameter("printer");
		String copies = request.getParameter("copies");

		hm.put("printer", printer);
		hm.put("copies", copies);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		lObjConfigDao.addConfigureLR(con, hm);

		rs.setAjaxData("Data saved Succesfully");
		return rs;

	}

	public CustomResultObject addGenerateLR(HttpServletRequest request, Connection con)
			throws SQLException, ParseException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, String> hm = new HashMap<String, String>();
		String stockistname = request.getParameter("stockistname");
		String address = request.getParameter("address");
		String copies = request.getParameter("wadhwanto");
		String txtdate = request.getParameter("txtdate");
		String city = request.getParameter("city");
		String lrnumber = request.getParameter("lrnumber");
		String telno = request.getParameter("telno");
		String truckno = request.getParameter("truckno");
		String weight = request.getParameter("weight");
		String cement = request.getParameter("cement");
		String bags = request.getParameter("bags");
		String wadhwanto = request.getParameter("wadhwanto");

		hm.put("lrnumber", lrnumber);
		hm.put("stockistname", stockistname);
		hm.put("wadhwanto", wadhwanto);
		hm.put("address", address);
		hm.put("copies", copies);
		hm.put("txtdate", txtdate);
		hm.put("city", city);
		hm.put("lrnumber", lrnumber);
		hm.put("telno", telno);
		hm.put("truckno", truckno);
		hm.put("weight", weight);
		hm.put("cement", cement);
		hm.put("bags", bags);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		lObjConfigDao.addGenerateLR(con, hm);

		rs.setAjaxData("Data saved Succesfully");
		return rs;

	}

	public CustomResultObject addPumpTest(HttpServletRequest request, Connection con)
			throws SQLException, ParseException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, String> hm = new HashMap<String, String>();

		String shift_id = request.getParameter("shift_id");
		String testDate = request.getParameter("testDate");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		hm.put("shift_id", shift_id);
		hm.put("testDate", testDate);
		hm.put("test_type", "A");

		String testInputData = request.getParameter("testInputData");
		String[] listTestdata = testInputData.split("\\|");
		for (String s : listTestdata) {
			String[] testData = s.split("~");
			hm.put("attendant_id", testData[0]);
			hm.put("testNozzle", testData[1]);
			hm.put("testQuantity", testData[2]);
			hm.put("item_price", lObjConfigDao.getNozzleDetails(testData[1], con).get("price"));
			if (!testData[2].equals("0")) {
				lObjConfigDao.addTestFuel(con, hm);
			}

		}

		rs.setAjaxData("Data saved Succesfully");
		return rs;

	}

	public CustomResultObject showSwipeMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "swipe_machine_id", "swipe_machine_name", "bank_details",

					"swipe_machine_short_name", "activate_flag", "updated_by",
					"updated_date" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSwipeMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfSwipe", lst);

				outputMap.put("BankDetails", lst);

				rs.setViewName("../SwipeMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddSwipe(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long swipeMachineId = request.getParameter("swipeMachineId") == null ? 0L
				: Long.parseLong(request.getParameter("swipeMachineId"));
		outputMap.put("swipe_machine_id", swipeMachineId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {

			if (swipeMachineId != 0) {
				outputMap.put("SwipeDetails", lObjConfigDao.getSwipeDetails(outputMap, connections));
				outputMap.put("BankDetails", lObjConfigDao.getBankDetail(outputMap, connections));
			}
			outputMap.put("lisitOfSwipe", lObjConfigDao.getSwipeMaster(outputMap, connections));
			outputMap.put("ListOfBanks", lObjConfigDao.getBankMaster(outputMap, connections));

			rs.setViewName("../AddSwipe.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addSwipe(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		String swipe_machine_name = hm.get("swipe_machine_name").toString();
		String txtaccountid = hm.get("txtaccountid").toString();
		String swipe_machine_short_name = hm.get("swipe_machine_short_name").toString();

		hm.put("swipe_machine_name", swipe_machine_name);
		hm.put("txtaccountid", txtaccountid);
		hm.put("swipe_machine_short_name", swipe_machine_short_name);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		long hdnSwipeMachineId = hm.get("hdnSwipeMachineId").equals("") ? 0l
				: Long.parseLong(hm.get("hdnSwipeMachineId").toString());
		try {

			if (hdnSwipeMachineId == 0) {
				hdnSwipeMachineId = lObjConfigDao.addSwipe(con, hm);
			} else {
				lObjConfigDao.updateSwipe(hdnSwipeMachineId, swipe_machine_name, txtaccountid,
						swipe_machine_short_name, userId, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showSwipeMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteSwipe(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long swipeId = Long.valueOf(request.getParameter("swipeId"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteSwipe(swipeId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveCollectPaymentSupervisorNew(HttpServletRequest request, Connection con)
			throws SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();

		HashMap<String, Object> outputMap = new HashMap<>();

		String CollectionData = request.getParameter("collectionData");
		String[] IndividualCollections = CollectionData.split("\\|");

		String collectionDate = request.getParameter("txtcollectiondate");
		String shiftId = request.getParameter("shiftId");
		String mode = request.getParameter("mode");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String slot_id = request.getParameter("slot_id");

		outputMap.put("txtcollectiondate", collectionDate);

		outputMap.put("shift_date", collectionDate);

		outputMap.put("mode", mode);
		outputMap.put("drpshiftid", shiftId);
		outputMap.put("user_id", userId);
		outputMap.put("app_id", appId);
		outputMap.put("slot_id", slot_id);

		try {

			for (String m : IndividualCollections) {

				String[] individualCollection = m.split("~");
				outputMap.put("drpemployee", individualCollection[0]);

				if (individualCollection[1].equals("0")) {
					continue;
				}

				outputMap.put("amount", individualCollection[1]);
				lObjConfigDao.saveCollectionSupervisor(con, outputMap);
			}
			rs.setAjaxData("Payment Collected Successfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addExpenseNew(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		HashMap<String, Object> outputMap = new HashMap<>();

		String expense_name = request.getParameter("expense_name");
		String amount = request.getParameter("amount");
		String date = request.getParameter("date");
		String qty = request.getParameter("qty");
		String hdnExpenseId = request.getParameter("hdnExpenseId");
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");

		outputMap.put("expense_name", expense_name);
		outputMap.put("amount", amount);
		outputMap.put("txtdate", date);
		outputMap.put("qty", qty);
		outputMap.put("hdnExpenseId", hdnExpenseId);
		outputMap.put("user_id", userId);
		outputMap.put("app_id", appId);
		outputMap.put("store_id", storeId);

		try {
			if (hdnExpenseId == "" || hdnExpenseId == null) {

				lObjConfigDao.addExpense(con, outputMap);
			} else {

				lObjConfigDao.updateExpense(con, outputMap);

			}
			rs.setAjaxData("Expense Added Successfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteSupervisorTransaction(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long collectionId = Long.valueOf(request.getParameter("collection_id"));
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {
			rs.setAjaxData(lObjConfigDao.deleteSupervisorTransaction(appId, collectionId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteTestFuel(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long test_id = Long.valueOf(request.getParameter("test_id"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteTestFuel(test_id, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showReconciliationReport(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		try {

			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getReconcilationRegister(outputMap, con);

			outputMap.put("lstOfShiftData", lst);
			rs.setViewName("../ReconcilationReport.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showTestFuelRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("type", type);

		List<LinkedHashMap<String, Object>> testData = lObjConfigDao.getTestData(outputMap, con);
		outputMap.put("testData", testData);

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		rs.setViewName("../TestFuelRegister.jsp");
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showPaymentCollectionReportFuel(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		List<LinkedHashMap<String, Object>> testData = lObjConfigDao.getCollectionData(outputMap, con);

		System.out.println(testData);

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("testData", testData);

		rs.setViewName("../CollectionReport.jsp");
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showSupervisorCollection(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String attendantId = request.getParameter("attendant_id") == null ? "" : request.getParameter("attendant_id");
		String collection_mode = request.getParameter("collection_mode") == null ? ""
				: request.getParameter("collection_mode");

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("attendantId", attendantId);
		outputMap.put("collection_mode", collection_mode);

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			String[] colNames = { "collection_id", "amount", "collection_date", "updated_by", "updated_date",
					"activate_flag" };
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getSupervisorCollection(outputMap, con);
			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"Supervisor Collection");
			} else {

				outputMap.put("lstSupervisorCollection", lst);
				outputMap.put("totalAmount", getTotalSupervisorCollection(lst));

				outputMap.put("lstUsers", lObjConfigDao.getEmployeeMaster(outputMap, con));

				rs.setViewName("../SupervisorCollectionReport.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	private Double getTotalSupervisorCollection(List<LinkedHashMap<String, Object>> lst) {
		Double m = 0d;
		for (LinkedHashMap<String, Object> lm : lst) {
			m += Double.parseDouble(lm.get("amount").toString());
		}
		return m;

	}

	private Double getTotalPaytmTransactions(List<LinkedHashMap<String, Object>> lst) {
		Double m = 0d;
		for (LinkedHashMap<String, Object> lm : lst) {
			m += Double.parseDouble(lm.get("amount").toString());
		}
		return m;

	}

	public CustomResultObject showPaytmTransctions(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");
		String attendantId = request.getParameter("attendant_id") == null ? "" : request.getParameter("attendant_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		outputMap.put("attendantId", attendantId);

		outputMap.put("app_id", appId);

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("attendantId", attendantId);

		List<LinkedHashMap<String, Object>> lstUsers = lObjConfigDao.getEmployeeMaster(outputMap, con);
		outputMap.put("lstUsers", lstUsers);
		try {
			String[] colNames = { "order_id", "bhim_upi_id", "amount", "mobile_no", "date_time_from_payment",
					"updated_date",
					"updated_by_user_id" };
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPaytmTransaction(outputMap, con);
			Double totalPaytmAmount = getTotalPaytmTransactions(lst);
			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"Paytm Transction");
			} else {

				outputMap.put("lstPaytmTransction", lst);
				outputMap.put("totalPaytmAmount", totalPaytmAmount);

				rs.setViewName("../PaytmTransactionReport.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showUserDetails(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {

			System.gc();
			List<LinkedHashMap<String, Object>> lstUserRoleDetails = lObjConfigDao
					.getUserRoleDetails(Long.valueOf(userId), con);
			HashMap<String, Object> hm = new HashMap<>();
			hm.put("lstUserRoleDetails", lstUserRoleDetails);

			LinkedHashMap<Long, Role> roleMaster = apptypes.get("Master");
			List<LinkedHashMap<String, Object>> lstUserRoleDetailsNew = new ArrayList<>();
			for (LinkedHashMap<String, Object> lm : lstUserRoleDetails) {
				Role realRole = roleMaster.get(Long.valueOf(lm.get("role_id").toString()));
				lm.put("role_name", realRole.getRoleName());
				lstUserRoleDetailsNew.add(lm);

			}

			List<String> roles = new ArrayList<>();

			for (LinkedHashMap<String, Object> mappedRole : lstUserRoleDetails) {
				roles.add(mappedRole.get("role_id").toString());
			}

			outputMap.put("userDetails", lObjConfigDao.getuserDetailsById(Long.valueOf(userId), con));
			outputMap.put("lstUserRoleDetails", lstUserRoleDetailsNew);
			rs.setViewName("../UserDetails.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showQrCodeMaster(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {

			String[] colNames = { "qr_code_number", "currently_assigned_to" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getQrCodeMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfQrCode", lst);

				rs.setViewName("../QrCode.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddQrCode(HttpServletRequest request, Connection connections) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long qrId = request.getParameter("qr_id") == null ? 0L
				: Long.parseLong(request.getParameter("qr_id"));
		outputMap.put("qr_id", qrId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (qrId != 0) {
				outputMap.put("QrCodeDetails", lObjConfigDao.getQrCodeDetails(outputMap, connections));
			}

			outputMap.put("lstEmployees", lObjConfigDao.getEmployeeMaster(outputMap, connections));
			rs.setViewName("../AddQrCode.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(connections));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addQrCode(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;

		HashMap<String, Object> hm = new HashMap<>();

		List<FileItem> toUpload = new ArrayList<>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		String qr_code_number = hm.get("qr_code_number").toString();

		String appId = hm.get("app_id").toString();
		hm.put("app_id", appId);

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("user_id", userId);

		long hdnQrId = hm.get("hdnQrId").equals("") ? 0l
				: Long.parseLong(hm.get("hdnQrId").toString());
		try {

			if (hdnQrId == 0) {
				hdnQrId = lObjConfigDao.addQrCode(con, hm);

			} else {
				lObjConfigDao.updateQRCode(hm, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='" + hm.get("callerUrl") + "?a=showQrCodeMaster'</script>");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteQrCode(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long qrId = Integer.parseInt(request.getParameter("qrId"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteQrCode(qrId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showPhonePePayments(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String chkaccepted = request.getParameter("chkaccepted") == null ? "" : request.getParameter("chkaccepted");

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		outputMap.put("chkaccepted", chkaccepted);

		try {

			String[] colNames = { "order_Id", "bhim_upi_id", "amount", "datetime" };
			outputMap.put("user_id", userId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPhonePePayments(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"CategoryMaster");
			} else {
				outputMap.put("ListOfPhonePePayments", lst);

				rs.setViewName("../PhonePePayments.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject resetPassword(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String employeeId = (request.getParameter("employeeId"));

		try {

			rs.setAjaxData(lObjConfigDao.resetPassword(employeeId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showBankMaster(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + "/";
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		try {
			String[] colNames = { "BankId", "BankName" };
			outputMap.put("app_id", appId);
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getBankMaster(outputMap, con);

			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId, "BankMaster");
			} else {
				outputMap.put("ListOfBanks", lst);
				rs.setViewName("../BankMaster.jsp");
				rs.setReturnObject(outputMap);
			}
		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showAddBank(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long categoryId = request.getParameter("bankId") == null ? 0L : Long.parseLong(request.getParameter("bankId"));
		outputMap.put("bank_id", categoryId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			if (categoryId != 0) {
				outputMap.put("BankDetails", lObjConfigDao.getBankDetail(outputMap, con));
			}

			rs.setViewName("../AddBank.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addBank(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		HashMap<String, Object> hm = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		hm.put("user_id", userId);

		List<FileItem> toUpload = new ArrayList<FileItem>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long bankId = hm.get("hdnBankId").equals("") ? 0l : Long.parseLong(hm.get("hdnBankId").toString());
		try {

			if (bankId == 0) {
				bankId = lObjConfigDao.addBank(con, hm);
			} else {
				lObjConfigDao.updateBank(hm, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='?a=showBankMaster'</script>");

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteBank(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		long sbuid = Integer.parseInt(request.getParameter("bankid"));

		try {

			rs.setAjaxData(lObjConfigDao.deletebank(sbuid, con));

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showAddBankReconcilation(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		long categoryId = request.getParameter("bankId") == null ? 0L : Long.parseLong(request.getParameter("bankId"));
		outputMap.put("bank_id", categoryId);
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getBankMaster(outputMap, con);
			if (categoryId != 0) {
				outputMap.put("BankDetails", lObjConfigDao.getBankReconcilationDetail(outputMap, con));
			}
			outputMap.put("reconcilationDate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			outputMap.put("ListOfBanks", lst);
			rs.setViewName("../AddBankReconcilation.jsp");
			rs.setReturnObject(outputMap);
		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject addBankReconcilation(HttpServletRequest request, Connection con) throws Exception {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		FileItemFactory itemFacroty = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(itemFacroty);
		// String webInfPath = cf.getPathForAttachments();

		HashMap<String, Object> hm = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		hm.put("app_id", appId);
		hm.put("user_id", userId);

		List<FileItem> toUpload = new ArrayList<FileItem>();
		if (ServletFileUpload.isMultipartContent(request)) {
			List<FileItem> items = upload.parseRequest(request);
			for (FileItem item : items) {

				if (item.isFormField()) {
					hm.put(item.getFieldName(), item.getString());
				} else {
					toUpload.add(item);
				}
			}
		}

		long bankId = hm.get("hdnReconcilationId").equals("") ? 0l
				: Long.parseLong(hm.get("hdnReconcilationId").toString());
		try {

			if (bankId == 0) {
				bankId = lObjConfigDao.addBankReconcilation(con, hm);
			} else {
				lObjConfigDao.updateBankReconcilation(hm, con);
			}

			rs.setReturnObject(outputMap);

			rs.setAjaxData("<script>window.location='?a=showReconcilationRegister'</script>");

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject deleteBankReconcilation(HttpServletRequest request, Connection con) {
		CustomResultObject rs = new CustomResultObject();
		long sbuid = Integer.parseInt(request.getParameter("reconcilationid"));

		try {

			rs.setAjaxData(lObjConfigDao.deletebankReconcilation(sbuid, con));

		} catch (Exception e) {
			writeErrorToDB(e);
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showReconcilationRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String exportFlag = request.getParameter("exportFlag") == null ? "" : request.getParameter("exportFlag");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		outputMap.put("app_id", appId);

		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}
		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		try {
			String[] colNames = { "bank_account_id", "reconcilation_date", "amount",
					"updated_by" };
			List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getReconcilationRegister(outputMap, con);
			if (!exportFlag.isEmpty()) {
				outputMap = getCommonFileGenerator(colNames, lst, exportFlag, DestinationPath, userId,
						"Paytm Transction");
			} else {

				outputMap.put("lstReconcilationRegister", lst);

				rs.setViewName("../ReconcilationRegister.jsp");
				rs.setReturnObject(outputMap);

			}
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject deleteReconcilation(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long reconcilation_id = Long.valueOf(request.getParameter("reconcilation_id"));

		try {

			lObjConfigDao.deleteReconcilation(reconcilation_id, con);

			rs.setAjaxData("Delete Succesfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getAttendantsForDateAndShift(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String collectionDate = (request.getParameter("collection_date"));
		String shift_id = (request.getParameter("shift_id"));
		String type = (request.getParameter("type"));
		HashMap<String, Object> outputMap = new HashMap<>();

		try {
			List<LinkedHashMap<String, Object>> listofAttendants = lObjConfigDao
					.getAttendantsForDateAndShift(collectionDate, shift_id, con);
			outputMap.put("listofAttendants", listofAttendants);
			List<LinkedHashMap<String, String>> collectionData = lObjConfigDao
					.getCollectionDataDateAndShiftWise(collectionDate, shift_id, con);
			outputMap.put("collectionData", collectionData);

			List<LinkedHashMap<String, String>> testData = lObjConfigDao.getTestDataDateAndShiftWise(collectionDate,
					shift_id, type, con);
			outputMap.put("testData", testData);

			rs.setAjaxData(mapper.writeValueAsString(outputMap));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getAttendantsForDateAndShiftUnclubbed(HttpServletRequest request, Connection con)
			throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String collectionDate = (request.getParameter("collection_date"));
		String shift_id = (request.getParameter("shift_id"));
		String type = (request.getParameter("type"));
		HashMap<String, Object> outputMap = new HashMap<>();
		try {
			List<LinkedHashMap<String, Object>> listofAttendants = lObjConfigDao
					.getAttendantsForDateAndShiftUnclubbed(collectionDate, shift_id, con);
			outputMap.put("listofAttendants", listofAttendants);

			List<LinkedHashMap<String, String>> collectionData = lObjConfigDao
					.getCollectionDataDateAndShiftWise(collectionDate, shift_id, con);
			outputMap.put("collectionData", collectionData);

			List<LinkedHashMap<String, String>> testData = lObjConfigDao.getTestDataDateAndShiftWise(collectionDate,
					shift_id, type, con);
			outputMap.put("testData", testData);

			rs.setAjaxData(mapper.writeValueAsString(outputMap));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showCheckinRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);
		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCheckinRegister(outputMap, con);

		outputMap.put("lstCheckinRegister", lst);

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		rs.setViewName("../CheckinRegister.jsp");
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject deleteCheckin(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long nozzle_id = Long.valueOf(request.getParameter("trn_nozzle_id"));

		try {
			rs.setAjaxData(lObjConfigDao.deleteCheckin(nozzle_id, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject getrecondata(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		String recontype = (request.getParameter("recontype"));
		String bank_id = (request.getParameter("bank_id"));
		String reconcilationDate = (request.getParameter("recondate"));
		List<LinkedHashMap<String, Object>> reconData;

		try {
			if (recontype.equals("Paytm")) {
				reconData = lObjConfigDao.getRecondataForPaytm(reconcilationDate, con);
			} else {
				reconData = lObjConfigDao.getRecondata(reconcilationDate, bank_id, con);
			}
			rs.setAjaxData(mapper.writeValueAsString(reconData));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveSplitInvoice(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoiceid = (request.getParameter("invoiceid"));
		String amount1 = (request.getParameter("amount1"));
		String amount2 = (request.getParameter("amount2"));
		try {

			// get the invoice data

			LinkedHashMap<String, Object> invoiceDetails = lObjConfigDao.getInvoiceDetails(invoiceid, con);
			long invoiceIdtoDelete = Long.valueOf(invoiceDetails.get("invoice_id").toString());
			// get the rlt fuel data
			LinkedHashMap<String, Object> rltInvoiceDetails = lObjConfigDao.getRltInvoiceDetails(invoiceid, con);

			// save with amount 1
			// save trn_invoice_register
			invoiceDetails.put("amountModify", amount1);
			long invoiceId1 = lObjConfigDao.saveToTrnInvoiceRegister(invoiceDetails, con);
			rltInvoiceDetails.put("invoice_id", String.valueOf(invoiceId1));
			rltInvoiceDetails.put("slot_id", "0");
			lObjConfigDao.saveToRltInvoiceDetails(rltInvoiceDetails, con);

			invoiceDetails.put("amountModify", amount2);
			long invoiceId2 = lObjConfigDao.saveToTrnInvoiceRegister(invoiceDetails, con);
			rltInvoiceDetails.put("invoice_id", String.valueOf(invoiceId2));
			rltInvoiceDetails.put("slot_id", "1");
			lObjConfigDao.saveToRltInvoiceDetails(rltInvoiceDetails, con);

			lObjConfigDao.deleteInvoice(invoiceIdtoDelete, "0", con);

			rs.setAjaxData("Done Successfully");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject settleThisTransaction(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		String invoice_id = (request.getParameter("invoiceid"));
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String transactionType = (request.getParameter("transactionType"));

		try {
			rs.setAjaxData(mapper
					.writeValueAsString(lObjConfigDao.settleThistransaction(invoice_id, userId, transactionType, con)));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showBankDetails(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		rs.setViewName("BankDetails.jsp");
		rs.setReturnObject(outputMap);

		rs.setReturnObject(outputMap);
		return rs;
	}

	public CustomResultObject showRestaurantMenu(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		try {

			String appShortCode = request.getParameter("appShortCode");

			outputMap.put("app_short_code", appShortCode);
			outputMap.put("ListOfCategoriesAndItems", lObjConfigDao.getItemDetailsUsingAppShortCode(outputMap, con));
			rs.setViewName("RestaurantMenuForGuests.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}

	public CustomResultObject showEmployeePayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try {
			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("userList", lObjConfigDao.getEmployeeMaster(outputMap, con));
			String appType = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
					.get("app_type");
			outputMap.put("app_type", appType);
			rs.setViewName("../EmployeePayment.jsp");
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject saveEmployeePayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long employeeId = Integer.parseInt(request.getParameter("employeeId"));
		String date = (request.getParameter("txtdate"));
		Double toPayAmount = Double.parseDouble(request.getParameter("payAmount"));
		String paymentMode = request.getParameter("paymentMode");
		String paymenttype = request.getParameter("paymenttype");
		
		String remarks = request.getParameter("remarks");

		String appId = request.getParameter("app_id");
		String userId = request.getParameter("user_id");
		HashMap<String, Object> hm = new HashMap<>();

		hm.put("app_id", appId);

		hm.put("user_id", userId);

		hm.put("employee_id", employeeId);

		hm.put("payment_mode", paymentMode);
		hm.put("total_amount", toPayAmount);
		hm.put("payment_type", paymenttype);
		hm.put("remarks", remarks);

		
		try {
			hm.put("invoice_date", date);
			long retMessage = lObjConfigDao.addPaymentFromEmployee(hm, con);
			hm.put("returnMessage", "Employee Payment Saved Successfully");
			rs.setAjaxData(mapper.writeValueAsString(hm));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}


	public CustomResultObject deleteEmployeePayment(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long employee_payment_id = Integer.parseInt(request.getParameter("employee_payment_id"));

		
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {
			rs.setAjaxData(lObjConfigDao.deleteEmployeePayment(employee_payment_id,userId, con));
		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}
	
	public CustomResultObject exportFsmLedgerAsPDF(HttpServletRequest request, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String fromDate = request.getParameter("fromDate").toString();
		String toDate = request.getParameter("toDate").toString();
		String toDateDisplay = request.getParameter("toDate").toString();

		String employeeId = request.getParameter("employeeId").toString();
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		

				List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getFSMLedger(employeeId, fromDate,
				toDate,appId, con);

		//LinkedHashMap<String, Object> totalDetails = gettotalDetailsLedger(lst);
		String openingBalanceForLedger = lObjConfigDao.getOpeningBalanceForLedger(employeeId, "23/01/1992",getDateFromDBMinusOneDay(getDateASYYYYMMDD(fromDate),con),appId, con);
			openingBalanceForLedger=openingBalanceForLedger==null?"0":openingBalanceForLedger;
		Date toDateDate = new SimpleDateFormat("dd/MM/yyyy").parse(fromDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(toDateDate);
		cal.add(Calendar.DATE, -1);
		toDateDate = cal.getTime();

		toDate = new SimpleDateFormat("dd/MM/yyyy").format(toDateDate);

		
		LinkedHashMap<String, Object> totalDetails = gettotalDetailsFSMLedger(lst);

				double differenceSum= Double.valueOf(totalDetails.get("paymentAmtSum").toString()) - Double.valueOf(totalDetails.get("salesAmtSum").toString());
				totalDetails.put("differenceSum", differenceSum);
				
				outputMap.put("totalDetails", totalDetails);
				totalDetails.put("openingBalanceForLedger", openingBalanceForLedger);

				double closingAmount=Double.valueOf(openingBalanceForLedger) + differenceSum;
				totalDetails.put("closingAmount", closingAmount);



		
		String startOfApplication = "23/01/1992";
		String pendingAmount = lObjConfigDao
				.getPendingAmountForThisCustomer(Long.valueOf(employeeId), startOfApplication, toDate, con)
				.get("PendingAmount");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		

	// 	Double openingAmount = pendingAmount == null ? 0 : Double.parseDouble(pendingAmount);
	// 	totalDetails.put("openingAmount", String.valueOf(openingAmount));
	// 	Double totalAmount = openingAmount - Double.parseDouble(totalDetails.get("debitSum").toString())
	// 			+ Double.parseDouble(totalDetails.get("creditSum").toString());
	// totalDetails.put("totalAmount", String.format("%.2f", totalAmount));
	// 	outputMap.put("totalDetails", totalDetails);

	outputMap.put("storeDetails", lObjConfigDao.getStoreDetails(Long.valueOf(storeId), con));

	
		outputMap.put("ShiftDetails", lObjConfigDao.getShiftDetails(outputMap, con));

		LinkedHashMap<String, String> employeeDetails = lObjConfigDao.getEmployeeDetails(Long.valueOf(employeeId), con);

		outputMap.put("fromDate", fromDate);
		outputMap.put("toDate", toDateDisplay);

		// outputMap.put("totalDetails",gettotalDetailsLedger(lst));
		outputMap.put("employeeDetails", employeeDetails);
		try {
			String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

			String appenders = "FsmLedger" + userId + employeeDetails.get("name").replaceAll(" ", "")
					+ "(" + getDateASYYYYMMDD(fromDate) + ")" +  "(" + getDateASYYYYMMDD(toDateDisplay) + ")"
					+ getDateTimeWithSeconds(con)+ ".pdf";
			DestinationPath += appenders;
			outputMap.put("ListOfItemDetails", lst);
			outputMap.put("openingBalanceForLedger", openingBalanceForLedger);
			
			
			String BufferedImagesFolder = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
			new InvoiceHistoryPDFHelper().generatePDFForFsmLedger(DestinationPath,BufferedImagesFolder, outputMap, con);
			outputMap.put("listReturnData", lst);
			outputMap.put(filename_constant, appenders);
			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}

		return rs;
	}
	
	public CustomResultObject showCashToVaultRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

		// if parameters are blank then set to defaults
		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		if (toDate.equals("")) {
			toDate = lObjConfigDao.getDateFromDB(con);
		}

		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCashtovaultRegister(outputMap, con);

		outputMap.put("lstCashtoVaultRegister", lst);


		outputMap.put("txtfromdate", fromDate);
		outputMap.put("txttodate", toDate);

		rs.setViewName("../CashToVaultReport.jsp");
		rs.setReturnObject(outputMap);
		return rs;

	}

	
	public CustomResultObject receivedCashtovault(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		long submissionId = Long.parseLong(request.getParameter("submissionId"));
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");

		try {

			rs.setAjaxData(lObjConfigDao.receivedCashtovault(submissionId, userId, con));

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showGenerateQrForVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();
		try {

			String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
			outputMap.put("app_id", appId);
			outputMap.put("listOfVehicle", lObjConfigDao.getVehicleListForQr(outputMap, con));
			rs.setViewName("../GenerateQrForVehicle.jsp");

			rs.setReturnObject(outputMap);

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}


	public CustomResultObject generateQrForVehicle(HttpServletRequest request, Connection con) throws SQLException {
		CustomResultObject rs = new CustomResultObject();

		Enumeration<String> params = request.getParameterNames();
		HashMap<String, Object> hm = new HashMap<>();
		List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
		while (params.hasMoreElements()) {
			String paramName = params.nextElement();

			if (paramName.equals("vehicleDetails")) {
				String[] itemsList = request.getParameter(paramName).split("\\|");
				for (String item : itemsList) {
					String[] vehicleDetails = item.split("~");
					HashMap<String, Object> vehicleDetailsMap = new HashMap<>();
					vehicleDetailsMap.put("vehicle_id", vehicleDetails[0]);
					vehicleDetailsMap.put("vehicle_name", vehicleDetails[1]);
					vehicleDetailsMap.put("vehicle_number", vehicleDetails[2]);
					vehicleDetailsMap.put("customer_name", vehicleDetails[3]);

					vehicleDetailsMap.put("noOfLabels", vehicleDetails[3]);
					vehicleDetailsMap.put("isPrintPrice", vehicleDetails[5]);
					itemListRequired.add(vehicleDetailsMap);
					// ID, QTY, RATE,CustomRate
				}
				hm.put("vehicleDetails", itemListRequired);
				continue;
			}
			hm.put(paramName, request.getParameter(paramName));

		}

		List<HashMap<String, Object>> newListRequired = new ArrayList<>();
		// based on no of labels adding more to list
		for (HashMap<String, Object> tempObj : itemListRequired) {
			int noOfLabels = Integer.parseInt(tempObj.get("noOfLabels").toString());
			for (int x = 0; x < noOfLabels; x++) {
				newListRequired.add(tempObj);
			}
		}
		while (true) {
			if (newListRequired.size() % 5 == 0) {
				break;
			}
			HashMap<String, Object> tempObj = new HashMap<>();
			tempObj.put("product_code", 00000);
			tempObj.put("item_name", "Adjustment");
			newListRequired.add(tempObj);
		}

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("store_id");
		String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + File.separator;
		hm.put("user_id", userId);
		hm.put("store_id", storeId);
		try {

			for (HashMap<String, Object> item : newListRequired) {
				generateQRForThisString(item.get("product_code").toString(), DestinationPath, 118, 120,
						hm.get("type").toString());
			}

			Document document = new Document(PageSize.A4, 0, 0, 0, 0);
			document.setMargins(0, 0, 0, 0);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(DestinationPath + "reqPDF.pdf"));
			writer.setCompressionLevel(9);
			writer.setFullCompression();
			ConfigurationServiceImpl event = new ConfigurationServiceImpl();
			writer.setPageEvent(event);
			document.open();
			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			int i = 0;
			List<String> tempList = new ArrayList<>();

			for (HashMap<String, Object> item : newListRequired) {
				PdfPCell cell;
				com.itextpdf.text.Image image = com.itextpdf.text.Image
						.getInstance(DestinationPath + item.get("product_code") + hm.get("type").toString() + ".jpg");
				cell = new PdfPCell(image);
				cell.setPadding(5);
				cell.setBorder(Rectangle.NO_BORDER);
				table.addCell(cell);
				tempList.add(item.get("item_name").toString());
				i++;
				if (i % 5 == 0) {
					for (String s : tempList) {
						cell = new PdfPCell(new Phrase(s, new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
						cell.setHorizontalAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
						cell.setBorder(Rectangle.RECTANGLE);
						table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("--------------------------",
							new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL)));
					cell.setHorizontalAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
					cell.setBorder(Rectangle.NO_BORDER);
					table.addCell(cell);
				}
				tempList.clear();
			}

			table.completeRow();
			document.add(table);
			document.close();

			rs.setAjaxData("reqPDF.pdf");

		} catch (Exception e) {
			request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
			rs.setHasError(true);
		}
		return rs;
	}

	public CustomResultObject showRawMaterialsMaster(HttpServletRequest request,Connection con)
	{
		CustomResultObject rs=new CustomResultObject();
		HashMap<String, Object> outputMap=new HashMap<>();
		String exportFlag= request.getParameter("exportFlag")==null?"":request.getParameter("exportFlag");
		String DestinationPath=request.getServletContext().getRealPath("BufferedImagesFolder")+delimiter;
		String userId=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);
		try
		{
			String [] colNames= {"raw_material_id","raw_material_name"}; // change according to dao return
			List<LinkedHashMap<String, Object>> lst=lObjConfigDao.getRawMaterialMaster(outputMap,con);
			outputMap.put("ListOfRawmaterials", lst);


			
			if(!exportFlag.isEmpty())
			{
				outputMap = getCommonFileGenerator(colNames,lst,exportFlag,DestinationPath,userId,"RawMaterialMaster");
			}
		else
			{
				
				rs.setViewName("../RawMaterialMaster.jsp");
				
			}	
			
			

		}
		catch (Exception e)
		{
			writeErrorToDB(e);
			rs.setHasError(true);
		}		
		rs.setReturnObject(outputMap);

		return rs;
	}

	public CustomResultObject showAddRawMaterial(HttpServletRequest request,Connection connections)
	{
		CustomResultObject rs=new CustomResultObject();			
		HashMap<String, Object> outputMap=new HashMap<>();
		
		long rawmaterialId=request.getParameter("rawmaterialId")==null?0L:Long.parseLong(request.getParameter("rawmaterialId"));
		outputMap.put("raw_material_id", rawmaterialId);
		
		try
		{	
			if(rawmaterialId!=0) {			outputMap.put("rawmaterialDetails", lObjConfigDao.getRawMaterialDetails(outputMap ,connections));} 
			rs.setViewName("../AddRawMaterial.jsp");	
			rs.setReturnObject(outputMap);		
		}
		catch (Exception e)
		{
				writeErrorToDB(e);
				rs.setHasError(true);
		}		
		return rs;
	}

	public CustomResultObject addRawMaterial(HttpServletRequest request,Connection con) throws Exception
	{
		CustomResultObject rs=new CustomResultObject();	
		HashMap<String, Object> outputMap=new HashMap<>();	
				
		FileItemFactory itemFacroty=new DiskFileItemFactory();
		ServletFileUpload upload=new ServletFileUpload(itemFacroty);		
		//String webInfPath = cf.getPathForAttachments();
		
		HashMap<String,Object> hm=new HashMap<>();
		
		
		
		
		List<FileItem> toUpload=new ArrayList<>();
		if(ServletFileUpload.isMultipartContent(request))
		{
			List<FileItem> items=upload.parseRequest(request);
			for(FileItem item:items)
			{		
				
				if (item.isFormField()) 
				{
					hm.put(item.getFieldName(), item.getString());
			    }
				else
				{
					toUpload.add(item);
				}
			}			
		}		
		String rawmaterialName= hm.get("txtrawmaterialname").toString();
		
		
		String userId=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		hm.put("txtrawmaterialname", rawmaterialName);
		hm.put("user_id", userId);
		
		
		long rawmaterialId=hm.get("hdnRawmaterialId").equals("")?0l:Long.parseLong(hm.get("hdnRawmaterialId").toString()); 
		try
		{			
									
			
			
			if(rawmaterialId==0)
			{
				rawmaterialId=lObjConfigDao.addRawMaterial(con, hm);
			}
			else
			{
				lObjConfigDao.updateRawMaterial(rawmaterialId, con, rawmaterialName,userId);
			}
			
			
		
			rs.setReturnObject(outputMap);
			
			
			rs.setAjaxData("<script>window.location='"+hm.get("callerUrl")+"?a=showRawMaterialsMaster'</script>");
			
										
		}
		catch (Exception e)
		{
			writeErrorToDB(e);
				rs.setHasError(true);
		}		
		return rs;
	}
	
	public CustomResultObject deleteRawMaterial(HttpServletRequest request,Connection con)
	{
		CustomResultObject rs=new CustomResultObject();
		long rawmaterialId= Integer.parseInt(request.getParameter("rawmaterialId"));		
		String userId=((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		try
		{	
			
			rs.setAjaxData(lObjConfigDao.deleteRawMaterial(rawmaterialId,userId,con));
			
			
		}
		catch (Exception e)
		{
			writeErrorToDB(e);
				rs.setHasError(true);
		}		
		return rs;
	}
	
 
    
public CustomResultObject showPendingRegister(HttpServletRequest request, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {

		CustomResultObject rs = new CustomResultObject();
		HashMap<String, Object> outputMap = new HashMap<>();

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		outputMap.put("app_id", appId);

		
  
		

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPendingRegister(outputMap, con);
		outputMap.put("lstPendingRegister", lst);


		rs.setViewName("../PendingRegister.jsp");
		rs.setReturnObject(outputMap);
		return rs;

	}

	public CustomResultObject showPlanningRegister(HttpServletRequest request, Connection con)
	throws SQLException, ClassNotFoundException, ParseException {

	CustomResultObject rs = new CustomResultObject();
	HashMap<String, Object> outputMap = new HashMap<>();

	String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
	outputMap.put("app_id", appId);

	String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
	String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

	



	outputMap.put("txtfromdate", fromDate);
	outputMap.put("txttodate", toDate);

	List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getPlanningRegister(outputMap, con);
	outputMap.put("lstPlanningRegister", lst);


	rs.setViewName("../PlanningRegister.jsp");
	rs.setReturnObject(outputMap);
	return rs;

	}

	   public CustomResultObject showCompletedOrders(HttpServletRequest request, Connection con)
	     throws SQLException, ClassNotFoundException, ParseException {

	CustomResultObject rs = new CustomResultObject();
	HashMap<String, Object> outputMap = new HashMap<>();

	String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
	outputMap.put("app_id", appId);

	String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
	String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

	// if parameters are blank then set to defaults
		if (fromDate.equals("")) 
	{
	fromDate = lObjConfigDao.getDateFromDB(con);
	}
		if (toDate.equals("")) 
		{
	toDate = lObjConfigDao.getDateFromDB(con);
	}



	outputMap.put("txtfromdate", fromDate);
	outputMap.put("txttodate", toDate);

	List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getCompletedOrders(outputMap, con);
	outputMap.put("lstCompletedOrders", lst);


	rs.setViewName("../CompletedOrders.jsp");
	rs.setReturnObject(outputMap);
	return rs;

	}


	public CustomResultObject showOrderHistory(HttpServletRequest request, Connection con)
	throws SQLException, ClassNotFoundException, ParseException {

CustomResultObject rs = new CustomResultObject();
HashMap<String, Object> outputMap = new HashMap<>();

String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
outputMap.put("app_id", appId);

String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
String toDate = request.getParameter("txttodate") == null ? "" : request.getParameter("txttodate");

// if parameters are blank then set to defaults
   if (fromDate.equals("")) 
{
fromDate = lObjConfigDao.getDateFromDB(con);
}
   if (toDate.equals("")) 
   {
toDate = lObjConfigDao.getDateFromDB(con);
}



outputMap.put("txtfromdate", fromDate);
outputMap.put("txttodate", toDate);

List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getOrderHistory(outputMap, con);
outputMap.put("lstOrderHistory", lst);


rs.setViewName("../OrderHistory.jsp");
rs.setReturnObject(outputMap);
return rs;

}


public CustomResultObject generatefryPlanning(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();

	
	String appenders = "FryPlanning.pdf";
	String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		DestinationPath += appenders;

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		
	

	try {


		HashMap<String, Object> hm=new HashMap<>();
		hm.put("todaysDate",cf.getDateFromDB(con));
		hm.put("listOfItems", lObjConfigDao.getFryPlanning(con,appId));

		new InvoiceHistoryPDFHelper().generatePDFForFryPlanning(DestinationPath, BufferedImagesFolderPath,hm, con);


		rs.setAjaxData(appenders);
	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}

	return rs;
}

   public CustomResultObject generatemakaiPlanning(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();

	
	String appenders = "MakaiPlanning.pdf";
	String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		DestinationPath += appenders;

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		
	

	try {


		HashMap<String, Object> hm=new HashMap<>();
		hm.put("todaysDate",cf.getDateFromDB(con));
		hm.put("listOfItems", lObjConfigDao.getMakaiPlanning(con,appId));

		new InvoiceHistoryPDFHelper().generatePDFForMakaiPlanning(DestinationPath, BufferedImagesFolderPath,hm, con);


		rs.setAjaxData(appenders);
	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}

	return rs;
}

public CustomResultObject generateReadingReport(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();

	
	String appenders = "ReadingReport.pdf";
	String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		DestinationPath += appenders;

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		
	

	try {


		HashMap<String, Object> hm=new HashMap<>();
		List<LinkedHashMap<String, Object>> listOfItems=lObjConfigDao.getReadingReport(con,appId);

		hm.put("todaysDate",cf.getDateFromDB(con));
		hm.put("listOfItems", listOfItems);


		List<LinkedHashMap<String, Object>> itemDetailsList= lObjConfigDao.getReadingReportDetails(con, appId);

		
		List<LinkedHashMap<String, Object>> listOfCustomers =lObjConfigDao.getCustomersListForPlanning(con,appId);

		hm.put("listOfCustomers",listOfCustomers);

		HashMap<String,String> hmDetails=new HashMap<String,String>();
		for(LinkedHashMap<String,Object> temp:itemDetailsList)
		{
			hmDetails.put(temp.get("theKey").toString(), temp.get("qty").toString());
		}

		HashMap<String,String> stockDetails=new HashMap<String,String>();
		for(LinkedHashMap<String,Object> temp:listOfItems)
		{
			String currStock=temp.get("currStock")==null?"0":temp.get("currStock").toString();
			stockDetails.put(temp.get("item_id").toString(), currStock);
		}

		hm.put("hmDetails",hmDetails);
		hm.put("stockDetails",stockDetails);
		

		
		
		

		

		new InvoiceHistoryPDFHelper().generatePDFForReadingReport(DestinationPath, BufferedImagesFolderPath,hm, con);


		rs.setAjaxData(appenders);
	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}

	return rs;
}

public CustomResultObject showTodaysStock(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();
	HashMap<String, Object> outputMap = new HashMap<>();

	try {

		String appType = "";
		if (request.getSession().getAttribute("userdetails") != null) {
			appType = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_type");
		}
		String invoiceId = request.getParameter("invoice_id");
		String invoiceNo = request.getParameter("invoice_no");
		String tableId = request.getParameter("table_id");
		String bookingId = request.getParameter("booking_id");
		String MobilebookingId = request.getParameter("mobile_booking_id");
		String txtinvoicedate = request.getParameter("txtinvoicedate");
		String vehicleId = request.getParameter("vehicleId");
		

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");

		String storeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
				.get("store_id");

		String invoiceTypeId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails"))
				.get("invoice_type");

		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		boolean adminFlag = (boolean) request.getSession().getAttribute("adminFlag");

		outputMap.putAll(lObjConfigDao.getUserConfigurations(userId, con));

		outputMap.put("store_id", storeId);
		outputMap.put("app_id", appId);
		outputMap.put("table_id", tableId);
		outputMap.put("invoice_no", invoiceNo);
		outputMap.put("txtinvoicedate", txtinvoicedate);


		

		if (invoiceId != null) {
			outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetails(invoiceId, con));
		}

		if (tableId != null) {
			outputMap.put("invoiceDetails", lObjConfigDao.getInvoiceDetailsForTable(tableId, con));
		}

		
		if (invoiceId == null) {

			outputMap.put("todaysDate", lObjConfigDao.getDateFromDB(con));
			outputMap.put("todaysDateMinusOneMonth", lObjConfigDao.getDateFromDBMinusOneMonth(con));
			outputMap.put("tentativeSerialNo",
					lObjConfigDao.getTentativeSequenceNo(appId, "trn_invoice_register", con));
		}

		// outputMap.put("itemList",
		// lObjConfigDao.getItemMasterForGenerateInvoiceForThisStore(outputMap,con));
		if (!invoiceTypeId.equals("1")) {
			outputMap.put("itemList", lObjConfigDao.getItemMasterForGenerateInvoice(outputMap, con));
		}

		List<LinkedHashMap<String, Object>> lst = lObjConfigDao.getItemMasterOrderCategory(outputMap, con);
		LinkedHashMap<Object, Object> reqHm = new LinkedHashMap<>();
		List<LinkedHashMap<String, Object>> lsttemp = new ArrayList<>();
		String categoryName = "";
		for (LinkedHashMap<String, Object> temp : lst) {
			if (categoryName.equals(""))
				categoryName = temp.get("catNameTrimmed").toString();
			if (categoryName.equals(temp.get("catNameTrimmed"))) {
				lsttemp.add(temp);
			} else {

				reqHm.put(categoryName, lsttemp);

				lsttemp = new ArrayList<>();
				categoryName = temp.get("catNameTrimmed").toString();
				lsttemp.add(temp);
			}
		}

		reqHm.put(categoryName, lsttemp);

		outputMap.put("lsitOfCategories", lObjConfigDao.getCategoriesWithAtLeastOneItem(outputMap, con));
		outputMap.put("lstOfShifts", lObjConfigDao.getShiftMaster(outputMap, con));

		outputMap.put("lstOfSwipeMaster", lObjConfigDao.getSwipeMaster(outputMap, con));
		outputMap.put("suggestedShiftId", lObjConfigDao.getSuggestedShiftId(outputMap, con));
		outputMap.put("categoriesWithItem", reqHm);

		if (invoiceTypeId.equals("2")) // means services and we need unique model no and unique no for this app id
		{
			outputMap.put("listUniqueModelNo", lObjConfigDao.getUniqueModelNoForThisApp(con, appId));
		}


		rs.setViewName("../TodaysStock.jsp");

		rs.setReturnObject(outputMap);

	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}
	return rs;
}

public CustomResultObject generateOrderReport(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();

	
	String appenders = "OrderForm.pdf";
	String DestinationPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		String BufferedImagesFolderPath = request.getServletContext().getRealPath("BufferedImagesFolder") + delimiter;
		DestinationPath += appenders;

		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		
	

	try {


		HashMap<String, Object> hm=new HashMap<>();
		hm.put("todaysDate",cf.getDateFromDB(con));
		hm.put("listOfItems", lObjConfigDao.getReadingReport(con,appId));

		new InvoiceHistoryPDFHelper().generatePDFForOrderReport(DestinationPath, BufferedImagesFolderPath,hm, con);


		rs.setAjaxData(appenders);
	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}

	return rs;
}

public CustomResultObject saveTodaysStock(HttpServletRequest request, Connection con) throws SQLException {
	CustomResultObject rs = new CustomResultObject();


	List<HashMap<String, Object>> itemListRequired = new ArrayList<>();
	String stockDate=request.getParameter("stock_date");
	String[] itemsList = request.getParameter("itemDetails").split("\\|");
				for (String item : itemsList) {
					String[] itemDetails = item.split("~");
					HashMap<String, Object> itemDetailsMap = new HashMap<>();
					itemDetailsMap.put("item_id", itemDetails[0]);
					itemDetailsMap.put("qty", itemDetails[1]);
					if(itemDetails[1].equals("0"))
					{continue;}
					itemListRequired.add(itemDetailsMap);
					// ID, QTY
				}


	
	try {

		lObjConfigDao.saveTodaysStock(stockDate,itemListRequired, con);
		
		rs.setAjaxData("Saved Stock Successfully");

	} catch (Exception e) {
		request.setAttribute("error_id", writeErrorToDB(e) + "-" + getDateTimeWithSeconds(con));
		rs.setHasError(true);
	}
	return rs;
}

public CustomResultObject showTodaysStockRegister(HttpServletRequest request,Connection con) throws SQLException
	{
		CustomResultObject rs=new CustomResultObject();
		HashMap<String, Object> outputMap=new HashMap<>();
		String exportFlag= request.getParameter("exportFlag")==null?"":request.getParameter("exportFlag");
		String DestinationPath=request.getServletContext().getRealPath("BufferedImagesFolder")+delimiter;
		String appId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("app_id");
		String fromDate = request.getParameter("txtfromdate") == null ? "" : request.getParameter("txtfromdate");
		String userId = ((HashMap<String, String>) request.getSession().getAttribute("userdetails")).get("user_id");
		outputMap.put("app_id", appId);


		if (fromDate.equals("")) {
			fromDate = lObjConfigDao.getDateFromDB(con);
		}
		outputMap.put("txtfromdate", fromDate);

		
		try
		{
			String [] colNames= {"stock_id","item_name","qty"}; // change according to dao return
			List<LinkedHashMap<String, Object>> lst=lObjConfigDao.getTodaysStockRegister(fromDate,con);
			outputMap.put("lstITodaysStockRegister", lst);


			
			if(!exportFlag.isEmpty())
			{
				outputMap = getCommonFileGenerator(colNames,lst,exportFlag,DestinationPath,userId,"TodaysStockRegister","TodaysStockRegister");
			}
		else
			{
				
				rs.setViewName("../TodaysStockRegister.jsp");
				
			}	
			
			

		}
		catch (Exception e)
		{
			writeErrorToDB(e);
			rs.setHasError(true);
		}		
		rs.setReturnObject(outputMap);

		return rs;
	}

}