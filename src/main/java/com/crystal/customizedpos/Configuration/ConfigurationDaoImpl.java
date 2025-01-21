package com.crystal.customizedpos.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.crystal.Frameworkpackage.CommonFunctions;
import com.crystal.Frameworkpackage.Query;

public class ConfigurationDaoImpl extends CommonFunctions {

	public List<LinkedHashMap<String, Object>> getCategoryMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select category_id categoryId,category_name categoryName from mst_category where activate_flag=1 and app_id=?",
				con);
	}

	public HashMap<String, String> getNozzles(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from nozzle_master where activate_flag=1 and app_id=?", con);
	}
	
	


	public HashMap<String, String> getDispensers(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from dispenser_master dm where activate_flag=1 and app_id=?", con);
	}

	public HashMap<String, String> getInvoiceSnacksDetails(String invoiceId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		return getMap(parameters, "select * from snacks_invoice_status where invoice_id=?", con);
	}
	

	public HashMap<String, String> getCustomers(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from mst_customer mc where activate_flag=1 and app_id=?", con);
	}

	public HashMap<String, String> getSalesInvoices(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from trn_invoice_register tir where activate_flag=1 and app_id=?",
				con);
	}

	public HashMap<String, String> getVehicles(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from mst_vehicle mv where activate_flag=1 and app_id=?", con);
	}

	public HashMap<String, String> getCategoryCount(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from mst_category mc where activate_flag=1 and app_id=?", con);
	}

	public HashMap<String, String> getPendingOrdersCount(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		
String query="select count(*) from snacks_invoice_status sis,trn_invoice_register tir where curr_status=0 and tir.invoice_id=sis.invoice_id and tir.activate_flag=1";

if(hm.get("user_id")!=null)
		{
			query+=" and tir.updated_by=? ";
			parameters.add(hm.get("user_id"));
		}
		return getMap(parameters,
		query,
				con);
	}

	public HashMap<String, String> getPlanningCount(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getMap(parameters,
				"select count(*) from snacks_invoice_status sis,trn_invoice_register tir where curr_status=1 and tir.invoice_id=sis.invoice_id and tir.activate_flag=1",
				con);
	}

	public HashMap<String, String> getCompletedCount(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getMap(parameters,
				"select count(*) from snacks_invoice_status sis,trn_invoice_register tir where curr_status=2 and tir.invoice_id=sis.invoice_id and tir.activate_flag=1",
				con);
	}

	public HashMap<String, String> getTodaysStockCount(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getMap(parameters,
				"select CAST(sum(qty) AS UNSIGNED)  todaysStock from trn_todays_stock_snacks ttss ",

				con);
	}

	public HashMap<String, String> getRMStock(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getMap(parameters,
				"select CAST(sum(qty) AS UNSIGNED)  todaysStock from trn_todays_rm_stock_snacks ttss ",
				con);
	}

	public HashMap<String, String> getItems(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select count(*) from mst_items mi where activate_flag=1 and app_id=?", con);
	}

	public List<LinkedHashMap<String, Object>> getCategoryMasterWithItemsCount(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select\r\n"
						+ "	category_id categoryId,\r\n"
						+ "	category_name categoryName,\r\n"
						+ "	count(item_name) cnt, cat.order_no \r\n"
						+ "from\r\n"
						+ "mst_category cat left outer join mst_items mi	 on cat.category_id =mi.parent_category_id  and mi.activate_flag =1 \r\n"
						+ "and mi.app_id=? \r\n"
						+ "where\r\n"
						+ "	cat.activate_flag = 1 and 		\r\n"
						+ "	cat.app_id =? \r\n"
						+ "	group by cat.category_id ;",
				con);
	}

	public List<LinkedHashMap<String, Object>> getBookingRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));

		String query = "select\r\n"
				+ "	*,date_format(from_date,'%d/%m/%Y %H:%i') as FormattedFromDate,date_format(to_date,'%d/%m/%Y %H:%i') as FormattedToDate \r\n"
				+ "from\r\n"
				+ "	trn_booking_register tbr ,\r\n"
				+ "	mst_customer mc,tbl_user_mst tum ,mst_store store \r\n"
				+ "where\r\n"
				+ "	tbr.customer_id = mc.customer_id\r\n"
				+ "	and tbr.activate_flag = 1  and tum.user_id =tbr.preffered_employee \r\n"
				+ "	and tbr.app_id = ? and date(tbr.from_date) between ? and ? and store.store_id=tbr.store_id ";

		if (hm.get("store_id") != null) {
			parameters.add(hm.get("store_id"));
			query += " and tbr.store_id=?";
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getItemDetailsStock(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");
		String questionMarks = "";
		for (HashMap<String, Object> item : itemDetailsList) {
			parameters.add(Long.parseLong(item.get("item_id").toString()));
			questionMarks += "?,";
		}
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select item_id,qty_available from stock_status where item_id in (" + questionMarks
						+ ") and store_id=? and app_id=? for update",
				con);
	}

	public List<LinkedHashMap<String, Object>> getUserRoleDetails(long customerId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(customerId);
		return getListOfLinkedHashHashMap(parameters,
				"select  * from tbl_user_mst user,acl_user_role_rlt "
						+ " userrole where user.user_id=? "
						+ " and user.user_id=userrole.user_id and userrole.activate_flag=1",
				con);
	}

	public List<LinkedHashMap<String, Object>> getCustomerList(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add("%" + hm.get("searchString") + "%");
		parameters.add("%" + hm.get("searchString") + "%");

		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_customer where activate_flag=1 and (customer_name like ? or mobile_number like ?) and app_id=?",
				con);
	}

	public List<LinkedHashMap<String, Object>> getItemMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select mrm.*,item.*,cat.*,stock.*,"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath "
				+ "from mst_items item inner join mst_category cat on cat.category_id=item.parent_category_id left outer join "
				+ " tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' "
				+ " left outer join stock_status stock on stock.item_id=item.item_id and stock.store_id=? "
				+ " left outer join raw_material_master mrm on mrm.raw_material_id=item.raw_material_id "
				+ " where item.activate_flag=1 and item.app_id=? and cat.app_id=item.app_id ";

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		if (hm.get("searchInput") != null && !hm.get("searchInput").equals("")) {
			parameters.add("%" + hm.get("searchInput") + "%");
			parameters.add("%" + hm.get("searchInput") + "%");
			query += " and (product_code like ? or item_name like ?)";
		}

		if (hm.get("categoryId") != null && !hm.get("categoryId").equals("-1") && !hm.get("categoryId").equals("")) {
			parameters.add(hm.get("categoryId"));
			query += " and parent_category_id=? ";
		}
		query += " group by item.item_id";
		query += " order by item.order_no";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getItemMasterOrderCategory(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select item.item_id,\r\n"
				+ "item.parent_category_id,\r\n"
				+ "item.debit_in,\r\n"
				+ "item.item_name,\r\n"
				+ "item.price,\r\n"
				+ "item.wholesale_price,\r\n"
				+ "item.franchise_rate,\r\n"
				+ "item.loyalcustomerrate1,\r\n"
				+ "item.loyalcustomerrate2,\r\n"
				+ "item.loyalcustomerrate3,\r\n"
				+ "item.activate_flag,\r\n"
				+ "item.updated_by,\r\n"
				+ "item.updated_date,\r\n"
				+ "item.product_code,\r\n"
				+ "item.average_cost,\r\n"
				+ "item.distributor_rate,\r\n"
				+ "item.b2b_rate,\r\n"
				+ "item.shrikhand,\r\n"
				+ "item.app_id,\r\n"
				+ "item.sgst,item.cgst,\r\n"
				+ "item.hsn_code,\r\n"
				+ "item.catalog_no,\r\n"
				+ "item.order_no"
				+ ",cat.*,replace(cat.category_name,' ','') catNameTrimmed,"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath "
				+ "from mst_items item inner join mst_category cat on cat.category_id=item.parent_category_id left outer join "
				+ " tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' "
				+ " where item.activate_flag=1 and item.app_id=? and cat.app_id=item.app_id ";

		parameters.add(hm.get("app_id"));

		if (hm.get("searchInput") != null && !hm.get("searchInput").equals("")) {
			parameters.add("%" + hm.get("searchInput") + "%");
			parameters.add("%" + hm.get("searchInput") + "%");
			query += " and (product_code like ? or item_name like ?)";
		}

		if (hm.get("categoryId") != null && !hm.get("categoryId").equals("-1") && !hm.get("categoryId").equals("")) {
			parameters.add(hm.get("categoryId"));
			query += " and parent_category_id=? ";
		}
		query += " group by item.item_id";
		query += " order by cat.order_no,cat.category_name,item.order_no";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public LinkedHashMap<String, String> getItemDetailsById(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("item_id"));
		parameters.add(hm.get("app_id"));

		return getMap(parameters, "select item.*,cat.*,stock.*,coalesce(qty_available,0) as AvailableQty from \r\n"
				+ "				 \r\n" + "					mst_category cat,mst_items item left outer join \r\n"
				+ "					stock_status stock  on stock.item_id=item.item_id and stock.store_id=?  \r\n"
				+ "				where \r\n" + "					item.item_id =? \r\n"
				+ "					and item.parent_category_id=cat.category_id and stock.app_id=? and stock.app_id=item.app_id and stock.app_id=cat.app_id",
				con);

	}

	public List<LinkedHashMap<String, Object>> getItemsByCategoryId(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("category_id"));
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_items where activate_flag=1 and parent_category_id=? and app_id=?", con);
	}

	public List<LinkedHashMap<String, Object>> getItemsByCategorynName(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("category_name"));
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_items items, mst_category cat where items.activate_flag=1 and items.parent_category_id=cat.category_id and cat.category_name=? and items.app_id=? order by items.order_no ",
				con);
	}

	public LinkedHashMap<String, Object> getItemdetailsById(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("item_id"));
		parameters.add(hm.get("app_id"));
		LinkedHashMap<String, String> itemDetails = getMap(parameters, "select item.*,cat.*,"
				+ "case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath "
				+ " from mst_items item left outer join mst_category cat on cat.category_id=item.parent_category_id left outer join  tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'"
				+ " where item.activate_flag=1 and item.item_id=? and item.app_id=? limit 1", con);
		LinkedHashMap<String, Object> newHm = new LinkedHashMap<>();
		newHm.putAll(itemDetails);
		newHm.put("listOfItemImages", getListofItemImages(hm, con));
		return newHm;
	}

	public LinkedHashMap<String, String> getItemdetailsByIdForStore(String customerId, String itemId, String storeId,
			String destinationStoreId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		parameters.add(destinationStoreId);
		parameters.add(customerId);
		parameters.add(itemId);
		return getMap(parameters, "select \r\n" + "cat1.*,item.*,cust.customer_type,\r\n"
				+ "case when cust.customer_type='LoyalCustomer1' then item.loyalcustomerrate1\r\n"
				+ "when cust.customer_type='LoyalCustomer2' then item.loyalcustomerrate2\r\n"
				+ "when cust.customer_type='LoyalCustomer3' then item.loyalcustomerrate3\r\n"
				+ "when cust.customer_type='Franchise' then item.franchise_rate\r\n"
				+ "when cust.customer_type='WholeSeller' then item.wholesale_price\r\n"
				+ "when cust.customer_type='Distributor' then item.distributor_rate \r\n"
				+ "when cust.customer_type='shrikhand' then item.shrikhand \r\n"
				+ "when cust.customer_type='Business2Business' then item.b2b_rate \r\n" + "else item.price\r\n"
				+ "end CustomersPrice,"
				+ "case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath,\r\n"
				+ "coalesce(stock.qty_available,0)  as stockAvailable ,\r\n"
				+ "coalesce(stock2.qty_available,0)  as destinationStockAvailable \r\n"
				+ "from mst_category cat1 inner join \r\n"
				+ "mst_items item  on cat1.category_id=item.parent_category_id left outer join \r\n"
				+ "stock_status stock on stock.item_id=item.item_id and stock.store_id=? left outer join \r\n"
				+ "stock_status stock2 on stock2.item_id=item.item_id  and stock2.store_id=? left outer join tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'"
				+ " left outer join mst_customer cust on cust.customer_id=? \r\n"
				+ "where item.activate_flag=1 and item.item_id= ?  ", con);
	}

	public List<LinkedHashMap<String, Object>> getListofItemImages(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("item_id"));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select\r\n" + "	concat(attachment_id, file_name) fileName,\r\n" + "	attachment_id\r\n" + "from\r\n"
						+ "	mst_items item,\r\n" + "	tbl_attachment_mst attach\r\n" + "where\r\n"
						+ "	item_id = ? \r\n" + "	and type = 'Image'\r\n"
						+ "	and item.item_id=attach.file_id and item.app_id=?",
				con);
	}

	public long saveItem(HashMap<String, Object> itemDetails, Connection con)
			throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("item_id", "~default");
		valuesMap.put("parent_category_id", itemDetails.get("drpcategoryId"));
		valuesMap.put("debit_in", itemDetails.get("drpdebitin"));
		valuesMap.put("item_name", itemDetails.get("itemname"));
		valuesMap.put("price", itemDetails.get("itemsaleprice"));
		valuesMap.put("wholesale_price", itemDetails.get("wholesaleprice"));
		valuesMap.put("franchise_rate", itemDetails.get("franchise_price"));
		valuesMap.put("loyalcustomerrate1", itemDetails.get("loyalcustomer1price"));
		valuesMap.put("loyalcustomerrate2", itemDetails.get("loyalcustomer2price"));
		valuesMap.put("loyalcustomerrate3", itemDetails.get("loyalcustomer3price"));
		valuesMap.put("activate_flag", "1");
		valuesMap.put("updated_by", itemDetails.get("userId"));
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("product_code", itemDetails.get("product_code"));
		valuesMap.put("average_cost", itemDetails.get("txtaveragecost"));
		valuesMap.put("distributor_rate", itemDetails.get("distributor_rate"));
		valuesMap.put("b2b_rate", itemDetails.get("b2b_rate"));
		valuesMap.put("shrikhand", itemDetails.get("shrikhand"));
		valuesMap.put("app_id", itemDetails.get("app_id"));
		valuesMap.put("sgst", itemDetails.get("sgst"));
		valuesMap.put("product_details", itemDetails.get("productdetails"));
		valuesMap.put("hsn_code", itemDetails.get("hsn_code"));
		valuesMap.put("catalog_no", itemDetails.get("catalog_no"));
		valuesMap.put("order_no", itemDetails.get("order_no"));
		valuesMap.put("cgst", itemDetails.get("cgst"));		

		if(itemDetails.get("drprawmaterialid")==null)
		{
			valuesMap.put("raw_material_id", "~null");		
		}
		else
		{
			valuesMap.put("raw_material_id", itemDetails.get("drprawmaterialid"));
		}

		if(itemDetails.get("lds_per_raw_material").equals("") ||  itemDetails.get("lds_per_raw_material")==null)
		{
			valuesMap.put("lds_per_raw_material", "~null");		
		}
		else
		{
			valuesMap.put("lds_per_raw_material", itemDetails.get("lds_per_raw_material"));
		}

		
		if(itemDetails.get("packets_in_ld").equals("") | itemDetails.get("packets_in_ld")==null)
		{
			valuesMap.put("packets_in_ld", "~null");		
		}
		else
		{
			valuesMap.put("packets_in_ld", itemDetails.get("packets_in_ld"));
		}

		if(itemDetails.get("packaging_type").equals("") | itemDetails.get("packaging_type")==null)
		{
			valuesMap.put("packaging_type", "~null");		
		}
		else
		{
			valuesMap.put("packaging_type", itemDetails.get("packaging_type"));
		}


		
		


		Query q = new Query("mst_items", "insert", valuesMap);
		return insertUpdateEnhanced(q, con);

	}

	public long updateItem(HashMap<String, Object> itemDetails, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemDetails.get("drpcategoryId"));
		parameters.add(itemDetails.get("drpdebitin"));
		parameters.add(itemDetails.get("itemname"));
		parameters.add(itemDetails.get("itemsaleprice"));
		parameters.add(itemDetails.get("wholesaleprice"));
		parameters.add(itemDetails.get("franchise_price"));
		parameters.add(itemDetails.get("loyalcustomer1price"));
		parameters.add(itemDetails.get("loyalcustomer2price"));
		parameters.add(itemDetails.get("loyalcustomer3price"));
		parameters.add(itemDetails.get("userId"));
		parameters.add(itemDetails.get("product_code"));
		parameters.add(itemDetails.get("txtaveragecost"));
		parameters.add(itemDetails.get("distributor_rate"));
		parameters.add(itemDetails.get("b2b_rate"));
		parameters.add(itemDetails.get("shrikhand"));
		parameters.add(itemDetails.get("sgst"));
		parameters.add(itemDetails.get("productdetails"));
		parameters.add(itemDetails.get("hsn_code"));
		parameters.add(itemDetails.get("catalog_no"));
		parameters.add(itemDetails.get("order_no"));
		parameters.add(itemDetails.get("cgst"));
		parameters.add(itemDetails.get("drprawmaterialid"));

		int lds_per_raw_material=0;
		if(!itemDetails.get("lds_per_raw_material").equals(""))
		{
			lds_per_raw_material=Integer.parseInt(itemDetails.get("lds_per_raw_material").toString());
		}		
		parameters.add(lds_per_raw_material);


		int packets_in_ld=0;
		if(!itemDetails.get("packets_in_ld").equals(""))
		{
			packets_in_ld=Integer.parseInt(itemDetails.get("packets_in_ld").toString());
		}
		parameters.add(packets_in_ld);


		int packaging_type=0;
		if(itemDetails.get("packaging_type")!=null && !itemDetails.get("packaging_type").equals(""))
		{
			packaging_type=Integer.parseInt(itemDetails.get("packaging_type").toString());
		}
		parameters.add(packaging_type);




		


		parameters.add(Long.parseLong(itemDetails.get("hdnItemId").toString()));

		String insertQuery = "UPDATE mst_items \r\n"
				+ "SET parent_category_id=?, debit_in=?, item_name=?, price=?, wholesale_price=?, franchise_rate=?, loyalcustomerrate1=?, loyalcustomerrate2=?, loyalcustomerrate3=?,updated_by=?, updated_date=sysdate(),product_code=?,average_cost=?,distributor_rate=?,b2b_rate=?,shrikhand=?,sgst=?,product_details=?,hsn_code=?,catalog_no=?,order_no=?,cgst=?,raw_material_id=?,lds_per_raw_material=?,packets_in_ld=?,packaging_type=? \r\n"
				+ "WHERE item_id=?";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public List<LinkedHashMap<String, Object>> showItems(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"SELECT item_name itemname, category_name categoryname , price,item_id itemId FROM  mst_items item, mst_category cat"
						+ " WHERE  item.parent_category_id=cat.category_id  AND item.`activate_flag`=1 and item.app_id=? and cat.app_id=item.app_id ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getStockStatus(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		String query = " select mc.category_name,mi.item_name,Purchase.qty-COALESCE (Sales.qty,0) availableQty,Purchase.details_id,Purchase.purchasePrice from (\r\n"
				+ "select tpid.details_id,tpid.qty qty,tpid.rate purchasePrice,tpid.item_id  from	trn_purchase_invoice_register tpir inner join trn_purchase_invoice_details tpid on\r\n"
				+ "tpir.invoice_id = tpid.invoice_id where	tpir.activate_flag = 1 and tpir.app_id = ? group by tpid.details_id) as Purchase\r\n"
				+ "\r\n"
				+ "left outer join \r\n"
				+ "\r\n"
				+ "(select tid.purchase_details_id ,tid.details_id ,tid.item_id,sum(tid.qty) qty  from trn_invoice_register tir \r\n"
				+ "inner join trn_invoice_details tid on tir.invoice_id = tid.invoice_id \r\n"
				+ "where tir.activate_flag = 1 and tir.app_id = ? and tid.purchase_details_id is not null group by tid.purchase_details_id) as Sales\r\n"
				+ "\r\n"
				+ "on Purchase.details_id=Sales.purchase_details_id\r\n"
				+ "inner join mst_items mi on mi.item_id =Purchase.item_id\r\n"
				+ "inner join mst_category mc on mc.category_id =mi.parent_category_id \r\n";

		if (hm.get("categoryId") != null && !hm.get("categoryId").equals("") && !hm.get("categoryId").equals("-1")) {
			query += " where mi.parent_category_id=?";
			parameters.add(hm.get("categoryId"));
		}
		query += " having availableQty >0";

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public boolean ProductExistForThisCategory(long categoryId, Connection con)
			throws SQLException {
		boolean returnvalue = true;
		int count = 0;

		PreparedStatement stmnt = con.prepareStatement(
				"SELECT COUNT(1) AS cnt FROM mst_items WHERE parent_category_id=? AND activate_flag=1");
		stmnt.setLong(1, categoryId);

		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		if (count == 0) {
			returnvalue = false;
		}
		stmnt.close();
		rs.close();

		return returnvalue;
	}

	public String deleteCategory(long categoryId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryId);
		insertUpdateDuablDB("UPDATE mst_category  SET activate_flag=0,updated_date=SYSDATE() WHERE category_id=?",
				parameters, conWithF);
		return "Category updated Succesfully";
	}

	public String deleteBooking(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("user_Id"));
		parameters.add(hm.get("booking_id"));

		insertUpdateDuablDB(
				"update trn_booking_register set activate_flag=0,updated_by=?,updated_date=sysdate() where booking_id=?",
				parameters, conWithF);
		return "Booking Deleted Succesfully";
	}

	public String updateMobileBookingStatus(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("acceptFlag"));

		parameters.add(hm.get("mobile_booking_id"));

		insertUpdateDuablDB(
				"update trn_order_register_frommobileapp set curr_status=?,updated_date=sysdate() where order_id=?",
				parameters, conWithF);
		return "Booking Updated Succesfully";
	}

	public String updateFuel(long fuelId, Connection con, String fuelName) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(fuelName);
		parameters.add(fuelId);
		insertUpdateDuablDB("update mst_items set item_name=? where item_id=?", parameters, con);
		return "Booking Updated Succesfully";
	}

	public long removeRoleFromUser(long userId, long roleId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(roleId);
		return insertUpdateDuablDB(
				"update acl_user_role_rlt set activate_flag=0,updated_date=sysdate() where user_id=? and role_id=?",
				parameters, conWithF);
	}

	public boolean isItemComposite(long itemId, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select count(1) cnt from rlt_composite_item_mpg rcim  where item_id =?";
		parameters.add(itemId);
		int count = Integer.parseInt(getMap(parameters, query, conWithF).get("cnt"));
		return count != 0;
	}

	public String debtiStockAgainstInvoice(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		long storeId = Long.parseLong(hm.get("store_id").toString());
		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");

		for (HashMap<String, Object> item : itemDetailsList) {
			long itemId = Long.parseLong(item.get("item_id").toString());
			String stockId = checkifStockAlreadyExist(storeId, itemId, conWithF);
			if (stockId.equals("0")) {
				HashMap<String, Object> stockDetails = new HashMap<>();
				stockDetails.put("drpstoreId", storeId);
				stockDetails.put("drpitems", itemId);
				stockDetails.put("qty", 0);
				stockDetails.put("app_id", hm.get("app_id"));
				stockId = String.valueOf(addStockMaster(stockDetails, conWithF));

			}

			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(item.get("qty"));
			parameters.add(item.get("item_id"));
			parameters.add(storeId);

			hm.put("stock_id", stockId);
			String previousQty = getStockDetailsbyId(hm, conWithF).get("qty_available");
			Double newQty = Double.valueOf(previousQty) - Double.valueOf(item.get("qty").toString());
			insertUpdateDuablDB("UPDATE stock_status  SET qty_available=qty_available-? WHERE item_id=? and store_id=?",
					parameters, conWithF); // for update issue

			parameters = new ArrayList<>();
			parameters.add(hm.get("store_id")); // to be validated
			parameters.add(item.get("item_id"));
			parameters.add(Double.parseDouble(item.get("qty").toString()) * -1);
			parameters.add("Sales");
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("invoice_id"));
			parameters.add(getDateASYYYYMMDD(hm.get("invoice_date").toString()));
			parameters.add(newQty);
			parameters.add(hm.get("app_id"));

			insertUpdateDuablDB(
					"insert into trn_stock_register values (default,?,?,?,?,?,sysdate(),'Against Invoice',?,?,?,?)",
					parameters, conWithF);

		}
		return "Stock Debited Succesfully";

	}

	public String debitStockItem(HashMap<String, Object> item, Connection conWithF) throws Exception {
		long storeId = Long.parseLong(item.get("store_id").toString());
		long itemId = Long.parseLong(item.get("item_id").toString());
		String stockId = checkifStockAlreadyExist(storeId, itemId, conWithF);

		if (stockId.equals("0")) {
			HashMap<String, Object> stockDetails = new HashMap<>();
			stockDetails.put("drpstoreId", storeId);
			stockDetails.put("drpitems", itemId);
			stockDetails.put("qty", 0);
			stockDetails.put("app_id", item.get("app_id"));
			stockId = String.valueOf(addStockMaster(stockDetails, conWithF));
		}
		item.put("stock_id", stockId);

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(item.get("qty"));
		parameters.add(item.get("item_id"));
		parameters.add(storeId);

		// Double newQty = Double.valueOf(previousQty) -
		// Double.valueOf(item.get("qty").toString());

		parameters = new ArrayList<>();
		parameters.add(item.get("store_id")); // to be validated
		parameters.add(item.get("item_id"));
		parameters.add(Double.parseDouble(item.get("qty").toString()) * -1);
		parameters.add("Sales");
		parameters.add(item.get("user_id"));
		parameters.add(item.get("invoice_id"));
		parameters.add(getDateASYYYYMMDD(item.get("invoice_date").toString()));
		// parameters.add(newQty);
		parameters.add(item.get("app_id"));

		// insertUpdateDuablDB("insert into trn_stock_register values
		// (default,?,?,?,?,?,sysdate(),'Against Invoice',?,?,?,?)",parameters,
		// conWithF);

		return "Stock Debited Succesfully";

	}

	public String addStockAgainstCorrection(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		long storeId = Long.parseLong(hm.get("store_id").toString());
		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");

		for (HashMap<String, Object> item : itemDetailsList) {
			long itemId = Long.parseLong(item.get("item_id").toString());
			if (item.get("debit_in").equals("S")) {
				continue;
			}
			String stockId = checkifStockAlreadyExist(storeId, itemId, conWithF);
			if (stockId.equals("0")) {
				HashMap<String, Object> stockDetails = new HashMap<>();
				stockDetails.put("drpstoreId", storeId);
				stockDetails.put("drpitems", itemId);
				stockDetails.put("qty", 0);
				stockDetails.put("app_id", hm.get("app_id"));
				stockId = String.valueOf(addStockMaster(stockDetails, conWithF));

			}

			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add((Double.valueOf(item.get("qty").toString()) * -1));
			parameters.add(item.get("item_id"));
			parameters.add(storeId);

			hm.put("stock_id", stockId);
			String previousQty = getStockDetailsbyId(hm, conWithF).get("qty_available");
			Double newQty = Double.valueOf(previousQty) - (Double.valueOf(item.get("qty").toString()) * -1);
			insertUpdateDuablDB("UPDATE stock_status  SET qty_available=qty_available-? WHERE item_id=? and store_id=?",
					parameters, conWithF); // for update issue

			parameters = new ArrayList<>();
			parameters.add(hm.get("store_id")); // to be validated
			parameters.add(item.get("item_id"));
			parameters.add(Double.parseDouble(item.get("qty").toString()));
			parameters.add("AddAgainstDeleteInvoice");
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("invoice_id"));
			parameters.add(getDateASYYYYMMDD(getDateFromDB(conWithF)));
			parameters.add(newQty);
			parameters.add(hm.get("app_id"));

			insertUpdateDuablDB(
					"insert into trn_stock_register values (default,?,?,?,?,?,sysdate(),'AddAgainstDeleteInvoice',?,?,?,?)",
					parameters, conWithF);

		}
		return "Stock Reverted";

	}

	public String removeStockAgainstReturn(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		long storeId = Long.parseLong(hm.get("store_id").toString());
		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");

		for (HashMap<String, Object> item : itemDetailsList) {
			if (item.get("ReturnedQty").toString().equals("0.00")) {
				continue;
			}
			long itemId = Long.parseLong(item.get("item_id").toString());
			String stockId = checkifStockAlreadyExist(storeId, itemId, conWithF);
			if (stockId.equals("0")) {
				HashMap<String, Object> stockDetails = new HashMap<>();
				stockDetails.put("drpstoreId", storeId);
				stockDetails.put("drpitems", itemId);
				stockDetails.put("qty", 0);
				stockDetails.put("app_id", hm.get("app_id"));
				stockId = String.valueOf(addStockMaster(stockDetails, conWithF));

			}

			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add((Double.valueOf(item.get("ReturnedQty").toString())));
			parameters.add(item.get("item_id"));
			parameters.add(storeId);

			hm.put("stock_id", stockId);
			String previousQty = getStockDetailsbyId(hm, conWithF).get("qty_available");
			Double newQty = Double.valueOf(previousQty) - (Double.valueOf(item.get("ReturnedQty").toString()));
			insertUpdateDuablDB("UPDATE stock_status  SET qty_available=qty_available-? WHERE item_id=? and store_id=?",
					parameters, conWithF); // for update issue

			parameters = new ArrayList<>();
			parameters.add(hm.get("store_id")); // to be validated
			parameters.add(item.get("item_id"));
			parameters.add(Double.parseDouble(item.get("ReturnedQty").toString()) * -1);
			parameters.add("removeAgainstReturnDeleteInvoice");
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("invoice_id"));
			parameters.add(getDateASYYYYMMDD(getDateFromDB(conWithF)));
			parameters.add(newQty);
			parameters.add(hm.get("app_id"));

			insertUpdateDuablDB(
					"insert into trn_stock_register values (default,?,?,?,?,?,sysdate(),'removeAgainstReturnDeleteInvoice',?,?,?,?)",
					parameters, conWithF);

		}
		return "Stock Reverted";

	}

	public void addStockRegister(HashMap<String, Object> hm, Connection conWithF)
			throws SQLException, ParseException {
		String stockId = checkifStockAlreadyExist(Long.valueOf(hm.get("drpstoreId").toString()),
				Long.valueOf(hm.get("drpitems").toString()), conWithF);
		hm.put("stock_id", stockId);
		String previousQty = getStockDetailsbyId(hm, conWithF).get("qty_available");
		previousQty = previousQty == null ? "0" : previousQty;
		Double newQty = Double.valueOf(previousQty) + Double.valueOf(hm.get("qty").toString());
		ArrayList<Object> parameters = new ArrayList<>();
		parameters = new ArrayList<>();
		parameters.add(hm.get("drpstoreId")); // to be validated
		parameters.add(hm.get("drpitems"));
		parameters.add(Double.parseDouble(hm.get("qty").toString()));
		parameters.add(hm.get("type"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("invoice_id"));
		parameters.add(getDateASYYYYMMDD(getDateFromDB(conWithF)));
		parameters.add(newQty);
		parameters.add(hm.get("app_id"));
		insertUpdateDuablDB("insert into trn_stock_register values (default,?,?,?,?,?,sysdate(),?,?,?,?,?)", parameters,
				conWithF);
	}

	public HashMap<String, Object> saveInvoice(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		String invoiceNo;
		if (hm.get("invoice_no") == null) {
			invoiceNo = String.valueOf(
					getPkForThistable("trn_invoice_register", Long.valueOf(hm.get("app_id").toString()), conWithF));
		} else {
			invoiceNo = hm.get("invoice_no").toString();
		}

		parameters.add(hm.get("customer_id"));
		parameters.add(hm.get("gross_amount"));
		parameters.add(hm.get("item_discount"));
		parameters.add(hm.get("invoice_discount"));
		parameters.add(hm.get("total_amount"));
		parameters.add(hm.get("payment_type"));

		parameters.add(getDateASYYYYMMDD(hm.get("invoice_date").toString()));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("app_id"));

		parameters.add(invoiceNo);
		parameters.add(hm.get("total_gst"));

		parameters.add(hm.get("model_no"));
		parameters.add(hm.get("unique_no"));

		parameters.add(hm.get("total_sgst"));
		parameters.add(hm.get("total_cgst"));
		parameters.add(hm.get("total_igst"));

		long invoiceId = insertUpdateDuablDB(
				"insert into trn_invoice_register values (default,?,?,?,?,?,?,?,?,sysdate(),1,?,?,?,?,?,?,?,?,?,?)",
				parameters,
				conWithF);
		hm.put("invoice_id", invoiceId);

		if (hm.get("shift_id") != null && !hm.get("shift_id").equals("")) {
			insertUpdateCustomParameterized(
					"insert into rlt_invoice_fuel_details values (default,:invoice_id,:shift_id,:attendant_id,:nozzle_id,sysdate(),:swipe_id,:slot_id,:vehicle_id)",
					hm, conWithF);

		}

		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");
		for (HashMap<String, Object> item : itemDetailsList) {

			if (item.get("qty").equals("0")) {
				continue;
			}

			parameters = new ArrayList<>();
			parameters.add(invoiceId);
			parameters.add(item.get("item_id"));
			parameters.add(item.get("qty"));
			parameters.add(item.get("rate"));
			parameters.add(item.get("custom_rate"));
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("app_id"));

			if (item.get("gst_amount") == null || item.get("gst_amount").equals("")) {
				item.put("gst_amount", "0.00");
			}

			parameters.add(item.get("gst_amount"));

			String weight;
			if (item.get("weight") == null || item.get("weight").equals("")) {
				weight = "0.00";
			} else {
				weight = item.get("weight").toString();
			}

			parameters.add(weight);
			parameters.add(item.get("size"));

			String purchaseInvoiceId = null;
			if (item.get("purchaseDetailsId") != null) {
				purchaseInvoiceId = item.get("purchaseDetailsId").toString().equals("undefined") ? "0"
						: item.get("purchaseDetailsId").toString();
			}

			if (item.get("purchaseDetailsId") != null && item.get("purchaseDetailsId").toString().equals(" ")) {
				purchaseInvoiceId = null;
			}

			parameters.add(purchaseInvoiceId);

			parameters.add(item.get("sgst_percentage"));
			parameters.add(item.get("sgst_amount"));
			parameters.add(item.get("cgst_percentage"));
			parameters.add(item.get("cgst_amount"));
			parameters.add(item.get("itemAmount"));

			parameters.add(item.get("igst_percentage"));
			parameters.add(item.get("igst_amount"));

			long detailsId = insertUpdateDuablDB("insert into trn_invoice_details"
					+ "(details_id, invoice_id, item_id, qty, rate, custom_rate, updated_by,"
					+ " updated_date, app_id, gst_amount,weight,size,purchase_details_id,sgst_percentage,sgst_amount,cgst_percentage,cgst_amount,item_amount,igst_percentage,igst_amount) "
					+ " values (default,?,?,?,?,?,?,sysdate(),?,?,?,?,?,?,?,?,?,?,?,?)", parameters,
					conWithF);

			parameters.clear();

			hm.put("details_id", detailsId);
			hm.put("battery_no", item.get("battery_no"));
			hm.put("vehicle_name", item.get("vehicle_name"));
			hm.put("vehicle_no", item.get("vehicle_no"));
			hm.put("warranty", item.get("warranty"));

			if (hm.get("app_type").equals("Battery")) {
				insertUpdateCustomParameterized(
						"insert into rlt_invoice_battery_details values (default,:details_id,:battery_no,:vehicle_name,:vehicle_no,:warranty,sysdate(),:app_id)",
						hm, conWithF);

			}

			hm.put("details_id", detailsId);
			hm.put("unique_no", item.get("unique_no"));
			hm.put("warranty", item.get("warranty"));
			hm.put("invoice_id", invoiceId);
			hm.put("item_id", item.get("item_id"));
			
			

			if (hm.get("app_type").equals("Electric")) {
				insertUpdateCustomParameterized(
						"insert into rlt_invoice_electric_details values (default,:invoice_id,:details_id,:unique_no,:warranty,sysdate(),:app_id)",
						hm, conWithF);

			}

			if (hm.get("app_type").equals("Beverage") || hm.get("app_type").equals("PetrolPump")) 
			{				
				//parameters.add(hm.get("hdnselecteditem"));
				hm.put("hdnselecteditem",hm.get("item_id"));
				hm.put("txtdate",hm.get("invoice_date"));			
				hm.put("txtqty",item.get("qty"));		
				hm.put("txtremarks","Debit Against Invoice No : "+invoiceNo);			
				hm.put("hdnstocktype","Debit");
				hm.put("details_id",detailsId);
				addStockStatusDirect(conWithF, hm);
			}

			if (item.get("RSPH") != null) {

				parameters.add(detailsId);
				parameters.add(item.get("RSPH") + "~" + item.get("RCYL") + "~" + item.get("RAXIS") + "~"
						+ item.get("RADD") + "~" + item.get("RVA") + "~" + item.get("RIPD"));
				parameters.add(item.get("LSPH") + "~" + item.get("LCYL") + "~" + item.get("LAXIS") + "~"
						+ item.get("LADD") + "~" + item.get("LVA") + "~" + item.get("LIPD"));

				insertUpdateDuablDB("insert into trn_sph_details "
						+ " values (?,?,?) ", parameters,
						conWithF);
			}

		}
		hm.put("payment_for", "Invoice");
		addPaymentFromCustomer(hm, conWithF);
		hm.put("invoice_id", invoiceId);
		hm.put("invoice_no", invoiceNo);
		return hm;
	}

	public long getPkForThistable(String sequenceName, Long appId, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(sequenceName);

		long generatedPK = 0;
		LinkedHashMap<String, String> hm = getMap(parameters,
				"select current_seq_no from seq_master where app_id=? and sequence_name=? for update", conWithF);

		if (hm.get("current_seq_no") == null) {
			parameters.clear();
			parameters.add(sequenceName);
			parameters.add(appId);
			insertUpdateDuablDB("insert into seq_master values (default,?,0,?)", parameters, conWithF);
			generatedPK = 1;
		} else {
			generatedPK = Long.valueOf(hm.get("current_seq_no"));
			generatedPK = generatedPK + 1;
			parameters.clear();
			parameters.add(generatedPK);
			parameters.add(appId);
			parameters.add(sequenceName);
			insertUpdateDuablDB("update seq_master set current_seq_no=? where app_id=? and sequence_name=?", parameters,
					conWithF);
		}
		return generatedPK;
	}

	public long addPaymentFromCustomer(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		if (hm.get("payment_type").equals("Pending")) {
			return 0;
		}
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("customer_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("invoice_date").toString()));
		parameters.add(hm.get("payment_mode"));
		if (hm.get("payment_type").equals("Paid") || hm.get("payment_type").equals("Debit")) {
			parameters.add(hm.get("total_amount"));
		} else if (hm.get("payment_type").equals("Partial")) {
			parameters.add(hm.get("paid_amount"));
		}
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("invoice_id"));
		parameters.add(hm.get("payment_for"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("user_id"));
		return insertUpdateDuablDB("insert into trn_payment_register values (default,?,?,?,?,?,?,?,?,?,?,sysdate(),1)",
				parameters,
				conWithF);
		

	}

	public String updateCategory(long categoryId, Connection con, String categoryName, String orderNo)
			throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryName);
		parameters.add(orderNo);

		parameters.add(categoryId);
		insertUpdateDuablDB(
				"UPDATE mst_category  SET category_Name=?,updated_date=SYSDATE(),order_no=? WHERE category_id=?",
				parameters, con);
		return "Category updated Succesfully";

	}

	public long addCategory(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("categoryName"));
		parameters.add(hm.get("app_id"));

		String orderNo = null;
		if (hm.get("order_no").equals("")) {
			orderNo = "1";
		} else {
			orderNo = hm.get("order_no").toString();
		}
		parameters.add(orderNo);

		return insertUpdateDuablDB("insert into mst_category values (default,?,1,sysdate(),null,null,?,?)", parameters,
				con);
	}

	public long addStockMaster(HashMap<String, Object> stockDetails, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(stockDetails.get("drpstoreId"));
		parameters.add(stockDetails.get("drpitems"));
		parameters.add(0);
		parameters.add(stockDetails.get("app_id"));
		return insertUpdateDuablDB("insert into stock_status values (default,?,?,?,1,0,?)", parameters, conWithF);
	}

	

	

	public long updateStockMaster(HashMap<String, Object> stockDetails, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Double.parseDouble(stockDetails.get("qty").toString()));
		parameters.add(stockDetails.get("stock_id"));
		return insertUpdateDuablDB("update stock_status set qty_available=qty_available+(?) where stock_id=?",
				parameters, conWithF);
	}

	public long updateStockMasterInventoryCounting(HashMap<String, Object> stockDetails, Connection conWithF)
			throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Double.parseDouble(stockDetails.get("qty").toString()));
		parameters.add(stockDetails.get("stock_id"));
		return insertUpdateDuablDB("update stock_status set qty_available=? where stock_id=?",
				parameters, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getCategories(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"SELECT category_id,category_name,case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath "
						+ " FROM mst_category cat left outer join tbl_attachment_mst tam  on  tam.file_id=cat.category_id and tam.type='category' WHERE  cat.activate_Flag=1 and cat.app_id=?",
				con);

	}

	public List<LinkedHashMap<String, Object>> getCategoriesWithAtLeastOneItem(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select\r\n"
						+ "	category_id,category_name,count(1) cnt\r\n"
						+ "from\r\n"
						+ "	mst_items mi ,\r\n"
						+ "	mst_category mc\r\n"
						+ "where\r\n"
						+ "	mi.parent_category_id = mc.category_id\r\n"
						+ "	and mi.app_id = ?\r\n"
						+ "	and mi.activate_flag = 1\r\n"
						+ "	group by mi.parent_category_id having cnt>=1 order by mc.order_no;",
				con);

	}

	public LinkedHashMap<String, String> getStockDetailsbyId(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("stock_id"));
		parameters.add(hm.get("app_id"));
		return getMap(parameters, "select * from stock_status stock,mst_items item\r\n"
				+ "where stock_id=? and item.item_id=stock.item_id and stock.app_id=? and item.app_id=stock.app_id",
				con);

	}

	public LinkedHashMap<String, String> getLoadingDetails(String loadingId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();		
		parameters.add(loadingId);
		return getMap(parameters, "select * from trn_loading_register tlr,mst_vehicle mv where loading_id=? and mv.vehicle_id=tlr.vehicle_id",
				con);

	}

	

	public int getMaxAttachmentNoByItemId(long itemId, Connection con) throws SQLException {
		int count = 0;
		PreparedStatement stmnt = con.prepareStatement("SELECT count(1) FROM tbl_attachment_mst WHERE file_id=?");
		stmnt.setLong(1, itemId);
		ResultSet rs = stmnt.executeQuery();
		while (rs.next()) {
			count = rs.getInt(1);
		}
		stmnt.close();
		rs.close();
		return count;
	}

	public String deleteItem(long itemId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		insertUpdateDuablDB(
				"UPDATE mst_items  SET activate_flag=0,product_code=concat(item_id,product_code),updated_date=SYSDATE() WHERE item_Id=?",
				parameters, conWithF);
		return "Item Deleted Succesfully";
	}



	public String deleteVehicle(long vehicleId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(vehicleId);
		insertUpdateDuablDB(
				"UPDATE mst_vehicle  SET activate_flag=0,updated_date=SYSDATE() WHERE vehicle_id=?",
				parameters, conWithF);
		return "vehicle Deleted Succesfully";
	}

	

	public String deletePayment(long paymentId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(paymentId);
		insertUpdateDuablDB(
				"update trn_payment_register set activate_flag=0,updated_date=sysdate(),updated_by=? where payment_id=?",
				parameters, conWithF);
		return "Deleted Succesfully";
	}

	public String deleteEmployeePayment(long paymentId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(paymentId);
		insertUpdateDuablDB(
				"update trn_employee_payment_register set activate_flag=0,updated_date=sysdate(),updated_by=? where employee_payment_id=?",
				parameters, conWithF);
		return "Deleted Succesfully";
	}

	public String deleteStock(long stockId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(stockId);
		insertUpdateDuablDB("UPDATE stock_status  SET activate_flag=0 where stock_id=?", parameters, conWithF);
		return "Stock Deleted Succesfully";
	}

	public HashMap<String, Object> getDetailsforItem(long itemId, Connection con)
			throws SQLException {
		HashMap<String, Object> returnMap = null;

		List<HashMap<String, Object>> listofAttachments = new ArrayList<>();
		try {

			PreparedStatement stmnt = con.prepareStatement(
					"SELECT item_id,item_name,price,parent_category_id,price FROM mst_items WHERE item_id=?");
			stmnt.setLong(1, itemId);

			ResultSet rs = stmnt.executeQuery();
			while (rs.next()) {
				returnMap = new HashMap<>();
				returnMap.put("itemId", rs.getString(1));
				returnMap.put("itemName", rs.getString(2));
				returnMap.put("itemPrice", rs.getString(3));
				returnMap.put("itemParentCategoryId", rs.getString(4));
				returnMap.put("price", rs.getString(5));

				stmnt = con.prepareStatement(
						"SELECT attachment_id,concat(attachment_id,file_name) as file_name ,file_id,length(attachment_asblob) FROM tbl_attachment_mst WHERE file_id=? AND TYPE='Image' and activate_flag=1");
				stmnt.setLong(1, itemId);
				rs = stmnt.executeQuery();
				HashMap<String, Object> attachment = null;

				while (rs.next()) {
					attachment = new HashMap<>();

					attachment.put("attachmentId", rs.getString(1));
					attachment.put("path", "BufferedImagesFolder/" + rs.getString(2));
					attachment.put("file_id", rs.getString(3));
					attachment.put("file_size", rs.getLong(4) / 1024);
					listofAttachments.add(attachment);

				}

			}
			returnMap.put("listofAttachments", listofAttachments);
			stmnt.close();
			rs.close();

		} catch (Exception e) {
			writeErrorToDB(e);
		}

		return returnMap;
	}

	public String deleteAttachment(long attachmentId, Connection conWithF) throws Exception {
		try {
			String insertTableSQL = "delete from tbl_attachment_mst where  attachment_id=?";

			PreparedStatement preparedStatement = conWithF.prepareStatement(insertTableSQL);
			preparedStatement.setLong(1, attachmentId);
			preparedStatement.executeUpdate();

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			return "Attachment Deleted Successfully";
		} catch (Exception e) {
			// write to error log
			writeErrorToDB(e);
			throw e;
		}

	}

	public LinkedHashMap<String, String> getCategoryDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("category_id"));
		parameters.add(hm.get("app_id"));

		return getMap(parameters,
				"select mc.*,concat(tam.attachment_id,tam.file_name) ImagePath,tam.attachment_id as attachId from "
						+ " mst_category as mc left outer join tbl_attachment_mst tam  on tam.file_id=mc.category_id  and tam.type = 'category' where mc.category_id=? and mc.app_id=?",
				con);
	}

	public LinkedHashMap<String, String> getFuelDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("fuelId"));

		return getMap(parameters,
				"select * from mst_items where item_id=? ",
				con);
	}

	public LinkedHashMap<String, String> getNozzleDetails(String nozzleId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(nozzleId);

		return getMap(parameters,
				"select\n" +
						"*\n" +
						"from\n" +
						"nozzle_master nozmaster inner join\n" +
						"mst_items fuelmst on nozmaster.item_id =fuelmst .item_id  inner join\n" +
						"dispenser_master dm on dm.dispenser_id =nozmaster .parent_dispenser_id\n" +
						"left outer join trn_nozzle_register tnr on\n" +
						"tnr.nozzle_id = nozmaster.nozzle_id\n" +
						"left outer join shift_master sm on sm.shift_id =tnr.shift_id\n" +
						"where\n" +
						"nozmaster.nozzle_id =? \n" +
						"order by\n" +
						"tnr.accounting_date desc,sm.shift_name desc\n" +
						"limit 1;\n",
				con);
	}

	public List<LinkedHashMap<String, Object>> getCustomerMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		String query = "select customer_id customerId,customer_name customerName, mobile_number mobileNumber, "
				+ " city customerCity, address customerAddress, customer_type customerType,state_name customerstate "
				+ " from mst_customer customer left outer join customer_group group1 on group1.group_id=customer.group_id and group1.app_id=customer.app_id"
				+ " left outer join cmn_state_mst csm on csm.state_id=customer.state_id "
				+ " where customer.activate_flag = 1 and   customer.app_id=? ";

		if (hm.get("searchInput") != null && !hm.get("searchInput").equals("")) {
			query += " and (customer_name like ? or mobile_number like ?) ";
			parameters.add("%" + hm.get("searchInput") + "%");
			parameters.add("%" + hm.get("searchInput") + "%");
		}

		if (hm.get("groupId") != null && !hm.get("groupId").equals("") && !hm.get("groupId").equals("-1")) {
			query += " and group1.group_id=? ";
			parameters.add(hm.get("groupId"));
		}

		if (hm.get("customerType") != null && !hm.get("customerType").equals("")
				&& !hm.get("customerType").equals("-1")) {
			query += " and customer.customer_type=?";
			parameters.add(hm.get("customerType"));
		}

		query += " order by customer_name";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getVendorMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		String query = "select * "
				+ " from mst_vendor vendor "
				+ " where vendor.activate_flag = 1 and vendor.app_id=? ";

		if (hm.get("searchInput") != null && !hm.get("searchInput").equals("")) {
			query += " and (vendor_name like ? or mobile_number like ?) ";
			parameters.add("%" + hm.get("searchInput") + "%");
			parameters.add("%" + hm.get("searchInput") + "%");
		}

		query += " order by vendor_name";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public String deleteCustomer(long customerId, Connection conWithF) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("activate_flag", "0");
		valuesMap.put("updated_date", "~sysdate()");

		HashMap<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("customer_id", customerId);

		Query q = new Query("mst_customer", "update", valuesMap, whereMap);
		insertUpdateEnhanced(q, conWithF);
		return "Customer Deleted Succesfully";
	}

	public String deleteVendor(long customerId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Long.valueOf(customerId));
		insertUpdateDuablDB("UPDATE mst_vendor  SET activate_flag=0,updated_date=SYSDATE() WHERE vendor_id=?",
				parameters, conWithF);
		return "Vendor Deleted Succesfully";
	}

	public String updateCustomer(long customerId, Connection conWithF, HashMap<String, Object> hm) throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("customerName"));
		parameters.add(Long.parseLong(hm.get("mobileNumber").toString()));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("address"));
		parameters.add(hm.get("customerType"));
		parameters.add(hm.get("customerGroup"));
		parameters.add(hm.get("alternate_mobile_no"));
		parameters.add(hm.get("customer_reference"));
		parameters.add(hm.get("txtgstno"));
		parameters.add(hm.get("state"));

		parameters.add(Long.valueOf(customerId));
		insertUpdateDuablDB(
				"UPDATE mst_customer  SET"
						+ " customer_name=?, mobile_number = ?, city=?, address= ?, customer_type=?,updated_date=SYSDATE(),"
						+ "group_id=?,alternate_mobile_no=?,customer_reference=?,gst_no=?, state=?	 WHERE customer_id=?",
				parameters, conWithF);
		return "Customer Updated Succesfully";

	}

	public long addCustomer(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("customerName"));
		parameters.add(Long.parseLong(hm.get("mobileNumber").toString()));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("address"));
		parameters.add(hm.get("customerType"));
		parameters.add(hm.get("customerGroup"));
		parameters.add(hm.get("alternate_mobile_no"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("customer_reference"));
		parameters.add(hm.get("txtgstno"));
		parameters.add(hm.get("state"));
		String insertQuery = "insert into mst_customer values (default,?,?,?,?,?,1,sysdate(),null,null,?,?,?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long addVendor(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("vendor_id", "~default");
		valuesMap.put("vendor_name", hm.get("vendorName"));
		valuesMap.put("mobile_number", (hm.get("mobileNumber").toString()));
		valuesMap.put("city", hm.get("city"));
		valuesMap.put("address", hm.get("address"));
		valuesMap.put("activate_flag", "1");
		valuesMap.put("created_Date", "~sysdate()");
		valuesMap.put("updated_by", "~null");
		valuesMap.put("updated_Date", "~null");
		valuesMap.put("alternate_mobile_no", hm.get("alternate_mobile_no"));
		valuesMap.put("app_id", hm.get("app_id"));
		valuesMap.put("vendor_reference", hm.get("vendor_reference"));
		valuesMap.put("gst_no", hm.get("gstno"));
		valuesMap.put("business_name", hm.get("business_name"));
		Query q = new Query("mst_vendor", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);
	}

	public long addVehicle(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("customerId"));
		parameters.add(hm.get("vehicleName"));
		parameters.add(hm.get("vehicleNumber"));
		parameters.add(hm.get("appId"));
		parameters.add(hm.get("userId"));
		parameters.add(hm.get("drpfueltype"));

		String insertQuery = "INSERT INTO mst_vehicle\r\n"
				+ "VALUES(default, ?, ?, ?, ?, 1, ?, sysdate(),?);";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getVehicleMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters, "select\r\n"
				+ "	*\r\n"
				+ "from\r\n"
				+ "	mst_vehicle mv left outer join mst_customer mc on mc.customer_id=mv.customer_id \r\n"
				+ "where\r\n"
				+ "	mv.app_id = ?\r\n"
				+ "	and mv.activate_flag = 1", con);

	}

	public List<LinkedHashMap<String, Object>> getVehiclesThatAreNotUnderLoading(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters, "\r\n" + //
						" select\r\n" + //
						"\t*\r\n" + //
						"from\r\n" + //
						"\tmst_vehicle mv \r\n" + //
						"\t\tleft outer join mst_customer mc on mc.customer_id=mv.customer_id\r\n" + //
						"\t\tleft outer join trn_loading_register tlr on tlr.vehicle_id =mv.vehicle_id\r\n" + //
						"where\r\n" + //
						"\tmv.app_id = ?\r\n" + //
						"\tand mv.activate_flag = 1\r\n" + //
						"\tand (tlr.is_loading_complete =1 or tlr.is_loading_complete is null)", con);

	}

	public List<LinkedHashMap<String, Object>> getVehicleOfCustomer(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("customer_id"));

		return getListOfLinkedHashHashMap(parameters,
				"SELECT * FROM mst_vehicle mv WHERE customer_id = ? AND activate_flag = 1", con);

	}

	public long updateVendor(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("vendorName"));
		parameters.add(Long.parseLong(hm.get("mobileNumber").toString()));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("address"));
		parameters.add(hm.get("user_id"));

		parameters.add(hm.get("alternate_mobile_no"));

		parameters.add(hm.get("vendor_reference"));
		parameters.add(hm.get("gstno"));
		parameters.add(hm.get("business_name"));

		parameters.add(hm.get("hdnVendorId"));

		String insertQuery = "update mst_vendor  "
				+ " SET vendor_name=?, mobile_number=?, city=?, address=?,"
				+ " updated_by=?, updated_Date=sysdate(), alternate_mobile_no=?,"
				+ " vendor_reference=?, gst_no=?, business_name=? where vendor_id=? ";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public LinkedHashMap<String, String> getCustomerDetails(long customerId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Long.valueOf(customerId));
		return getMap(parameters,
				"select *,csm.state_name customerstatename from mst_customer mc left outer join cmn_state_mst csm on csm.state_id=mc.state_id where customer_id=?",
				con);
	}

	public LinkedHashMap<String, String> getInvoiceIdByInvoiceNo(String invoiceNo, String appId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add((invoiceNo));
		parameters.add((appId));
		return getMap(parameters, "select * from trn_invoice_register where invoice_no=? and app_id=?", con);
	}

	public LinkedHashMap<String, String> getInvoiceNoByInvoiceId(String invoiceId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add((invoiceId));
		return getMap(parameters, "select * from trn_invoice_register where invoice_id=?", con);
	}

	public LinkedHashMap<String, String> getVendorDetails(long customerId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Long.valueOf(customerId));
		return getMap(parameters, "select * from mst_vendor where vendor_id=?", con);
	}

	public LinkedHashMap<String, String> getPendingAmountForThisCustomer(long customerId, String fromDate,
			String toDate, Connection con) throws SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select \r\n" + "customer.customer_name, customer.customer_type, \r\n"
				+ " coalesce ((select sum(amount) as paid from "
				+ " trn_payment_register tpr where activate_flag=1 and  customer_id=customer.customer_id datesConditionPayment),0) -  coalesce ((select sum(total_amount) as toPay from trn_invoice_register where activate_flag=1 and customer_id=customer.customer_id datesConditionInvoice),0) "
				+ " PendingAmount "
				+ " from mst_customer customer where customer_id =?";

		if (!fromDate.equals("")) {
			query = query.replaceAll("datesConditionInvoice", " and date(invoice_date) between ? and ? ");
			parameters.add(getDateASYYYYMMDD(fromDate));
			parameters.add(getDateASYYYYMMDD(toDate));
			query = query.replaceAll("datesConditionPayment", " and date(payment_date) between ? and ? ");
			parameters.add(getDateASYYYYMMDD(fromDate));
			parameters.add(getDateASYYYYMMDD(toDate));
		} else {
			query = query.replaceAll("datesConditionInvoice", "");
			query = query.replaceAll("datesConditionPayment", "");
		}

		parameters.add(customerId);

		return getMap(parameters, query, con);
	}

	public HashMap<String, String> getDataForHomepage(HashMap<String, Object> hm, Connection conWithF)
			throws SQLException {

		String query = "select \r\n"
				+ " (select count(1) from mst_items where activate_flag=1 and  app_id=?) as ActiveItems,\r\n"
				+ " (select count(1) from mst_category where activate_flag=1 and app_id=?) as ActiveCategories,\r\n"
				+ " (select count(1) from mst_customer where activate_flag=1 and  app_id=?) as ActiveCustomers, \r\n"
				+ " (select count(1) from trn_invoice_register where activate_flag=1 and app_id=?) as InvoicesCreated \r\n"
				+ " from dual";

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		return getMap(parameters, query, conWithF);

	}

	public List<LinkedHashMap<String, Object>> getStoreMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select store_id storeId,store_name storeName, address_line_1 address_line_1,address_line_2 address_line_2, store_email storeEmail from mst_store where activate_flag=1 and app_id=?",
				con);

	}

	public List<LinkedHashMap<String, Object>> getTermsMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select terms_condition_id termsId,terms_condition_content termscondition, `order` from mst_terms_and_conditions where app_id=?  and activate_flag=1 ",
				con);

	}

	public List<LinkedHashMap<String, Object>> getInvoiceFormatList(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		return getListOfLinkedHashHashMap(parameters,
				"select * from invoice_formats order by format_name",
				con);

	}

	public List<LinkedHashMap<String, Object>> getInvoiceTypeList(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		return getListOfLinkedHashHashMap(parameters,
				"select * from invoice_types",
				con);

	}

	public LinkedHashMap<String, String> getStoreDetails(long storeId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		return getMap(parameters,
				"select * from mst_store where store_id=?", con);

	}

	public LinkedHashMap<String, String> gettermsAndConditionDetails(long termsId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(termsId);
		return getMap(parameters,
				"select * from mst_terms_and_conditions where terms_condition_id=?", con);

	}

	public long addStore(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("storeName"));
		parameters.add(hm.get("address_line_1"));
		parameters.add(hm.get("storeEmail"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("address_line_2"));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("pincode"));
		parameters.add(hm.get("gstno"));
		parameters.add(hm.get("mobile_no"));
		parameters.add(hm.get("address_line_3"));
		parameters.add(hm.get("storetiming"));

		String insertQuery = "insert into mst_store values (default,?,?,?,1,null,sysdate(),?,?,?,?,?,?,?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public long addTermsAndCondition(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("terms_condition_id", "~default");
		valuesMap.put("terms_condition_content", hm.get("termscondition"));
		valuesMap.put("order", hm.get("order"));
		valuesMap.put("app_id", hm.get("app_id"));
		Query q = new Query("mst_terms_and_conditions", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);
	}

	public String updateStore(long storeId, Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("storeName"));
		parameters.add(hm.get("address_line_1"));
		parameters.add(hm.get("address_line_2"));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("pincode"));
		parameters.add(hm.get("storeEmail"));
		parameters.add(hm.get("gstno"));
		parameters.add(hm.get("mobile_no"));
		parameters.add(hm.get("address_line_3"));
		parameters.add(hm.get("storetiming"));

		parameters.add(storeId);
		insertUpdateDuablDB(
				"UPDATE mst_store  SET store_name=?, address_line_1 = ?,address_line_2 = ?,city=?,pincode=?, store_email=?, updated_date=SYSDATE(),gst_no=?,mobile_no=?,address_line_3=?,store_timing=? WHERE store_id=?",
				parameters, conWithF);
		return "Store Updated Succesfully";

	}

	public String updateTermsAndCondition(long termsId, Connection conWithF, HashMap<String, Object> hm)
			throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("termscondition"));
		parameters.add(hm.get("order"));
		parameters.add(hm.get("app_id"));

		parameters.add(termsId);
		insertUpdateDuablDB(
				"UPDATE mst_terms_and_conditions  SET terms_condition_content=?,`order` = ?,app_id=? WHERE terms_condition_id=?",
				parameters, conWithF);
		return "Terms And Condition Updated Succesfully";

	}

	public String deleteStore(long storeId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		insertUpdateDuablDB("UPDATE mst_store  SET activate_flag=0,updated_date=SYSDATE() WHERE store_id=?", parameters,
				conWithF);
		return "Store Deleted Succesfully";
	}

	public String deleteTermsAndCondition(long termsId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(termsId);

		insertUpdateDuablDB("UPDATE mst_terms_and_conditions  SET activate_flag=0 WHERE terms_condition_id=?",
				parameters, conWithF);
		return "Terms And Condition  deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getDailyInvoiceDetails(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));
		parameters.add(hm1.get("app_id"));

		String query = "select *,date_format(inv.invoice_date,'%d/%m/%Y') as FormattedInvoiceDate,date_format(inv.updated_date,'%d/%m/%Y %H:%i:%s') as updatedDate,inv.activate_flag isActive from trn_invoice_register inv"
				+ " left outer join mst_customer cust on inv.customer_id=cust.customer_id and inv.app_id=cust.app_id  "
				+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
				+ " left outer join trn_payment_register paymnt on inv.invoice_id =paymnt.ref_id and paymnt.activate_flag=1 and paymnt.payment_for='invoice' and paymnt.app_id = inv.app_id "
				+ " inner join mst_store store1 on inv.store_id=store1.store_id left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id = inv.invoice_id left outer join trn_invoice_details tid on tid.invoice_id=inv.invoice_id left outer join rlt_invoice_electric_details ribd on ribd.details_id=tid.details_id "
				+ "where date(invoice_date) between ? and ?  and inv.app_id=?   "
				+ "and usertbl.app_id=inv.app_id and store1.app_id=inv.app_id and inv.activate_flag=1 ";

		if (hm1.get("customerId") != null && !hm1.get("customerId").equals("") && !hm1.get("customerId").equals("-1")) {
			parameters.add(hm1.get("customerId").toString());
			query += " and cust.customer_id =?";
		}

		if (hm1.get("invoice_no") != null && !hm1.get("invoice_no").equals("") && !hm1.get("invoice_no").equals("-1")) {
			parameters.add(hm1.get("invoice_no").toString());
			query += " and inv.invoice_no =?";
		}

		if (new Boolean(hm1.get("deleteFlag").toString()) == true) {
			query = query.replaceAll("and inv.activate_flag=1", "");
		}
		if (hm1.get("storeId") != null && !hm1.get("storeId").equals("") && !hm1.get("storeId").equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and inv.store_id =?";
		}

		if (hm1.get("updatedBy") != null && !hm1.get("updatedBy").equals("")) {
			parameters.add(hm1.get("updatedBy").toString());
			query += " and inv.updated_by =?";
		}

		if (hm1.get("paymentType") != null && !hm1.get("paymentType").equals("")) {

			String questionMarks = "";
			for (String s : hm1.get("paymentType").toString().split(",")) {
				questionMarks += "?" + ",";
				parameters.add(s);
			}
			questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
			query += " and inv.payment_type in (" + questionMarks + ")";
		}
		if (hm1.get("paymentMode") != null && !hm1.get("paymentMode").equals("")) {

			String questionMarks = "";
			for (String s : hm1.get("paymentMode").toString().split(",")) {
				questionMarks += "?" + ",";
				parameters.add(s);
			}
			questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
			query += " and paymnt.payment_mode in (" + questionMarks + ")";
		}

		if (hm1.get("discount") != null && !hm1.get("discount").equals("")) {

			query += " and (inv.item_discount!=0 or inv.invoice_discount!=0) ";
		}

		if (hm1.get("attendant_id") != null && !hm1.get("attendant_id").equals("")) {

			query += " and rifd.attendant_id=? ";

			parameters.add(hm1.get("attendant_id"));
		}

		if (hm1.get("battery_no") != null && !hm1.get("battery_no").equals("")) {

			query += " and ribd.unique_no like ? ";
			parameters.add("%" + hm1.get("battery_no") + "%");
		}

		query += "group by tid.invoice_id order by invoice_no desc ,invoice_date desc ,rifd.invoice_id asc ";
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getSalesReport3(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));
		parameters.add(hm1.get("app_id"));

		String query = "select *,date_format(inv.invoice_date,'%d/%m/%Y') as FormattedInvoiceDate,date_format(inv.updated_date,'%d/%m/%Y %H:%i:%s') as updatedDate,inv.activate_flag isActive,round(dtls.custom_rate*dtls.qty) amt from trn_invoice_register inv "
				+ " inner join trn_invoice_details dtls on dtls.invoice_id=inv.invoice_id "
				+ "inner join mst_items item on  item.item_id=dtls.item_id "
				+ "inner join mst_category category on item.parent_category_id=category.category_id "
				+ " left outer join mst_customer cust on inv.customer_id=cust.customer_id and inv.app_id=cust.app_id  "
				+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
				+ " left outer join trn_payment_register paymnt on inv.invoice_id =paymnt.ref_id and paymnt.activate_flag=1 and paymnt.payment_for='invoice' and paymnt.app_id = inv.app_id "
				+ " inner join mst_store store1 on inv.store_id=store1.store_id "
				+ "where date(invoice_date) between ? and ?  and inv.app_id=?   "
				+ "and usertbl.app_id=inv.app_id and store1.app_id=inv.app_id and inv.activate_flag=1 ";

		if (hm1.get("customerId") != null && !hm1.get("customerId").equals("") && !hm1.get("customerId").equals("-1")) {
			parameters.add(hm1.get("customerId").toString());
			query += " and cust.customer_id =?";
		}

		if (new Boolean(hm1.get("deleteFlag").toString()) == true) {
			query = query.replaceAll("and inv.activate_flag=1", "");
		}
		if (hm1.get("storeId") != null && !hm1.get("storeId").equals("") && !hm1.get("storeId").equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and inv.store_id =?";
		}

		if (hm1.get("updatedBy") != null && !hm1.get("updatedBy").equals("")) {
			parameters.add(hm1.get("updatedBy").toString());
			query += " and inv.updated_by =?";
		}

		if (hm1.get("paymentType") != null && !hm1.get("paymentType").equals("")) {

			String questionMarks = "";
			for (String s : hm1.get("paymentType").toString().split(",")) {
				questionMarks += "?" + ",";
				parameters.add(s);
			}
			questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
			query += " and inv.payment_type in (" + questionMarks + ")";
		}

		if (hm1.get("discount") != null && !hm1.get("discount").equals("")) {

			query += " and (inv.item_discount!=0 or inv.invoice_discount!=0) ";
		}

		query += " order by invoice_date,inv.invoice_id asc ";
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getSalesReport4(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));

		parameters.add(hm1.get("app_id"));

		String query = "select item_name,sum(tid.qty*tid.custom_rate) as amt from\r\n"
				+ "trn_invoice_register tir,\r\n"
				+ "trn_invoice_details tid,\r\n"
				+ "mst_items mi\r\n"
				+ "where invoice_date between ? and ? and tid.item_id =mi.item_id\r\n"
				+ "and tir.app_id =? and tid.invoice_id =tir.invoice_id  \r\n";

		if (hm1.get("storeId") != null && !hm1.get("storeId").equals("") && !hm1.get("storeId").equals("-1")) {
			query += " and tir.store_id=? ";
			parameters.add(hm1.get("storeId"));
		}
		query += " group by mi.item_name order by amt desc";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getShiftData(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("txttodate").toString()));
		parameters.add(hm1.get("app_id"));

		String query = "select *, name,date_format(date(check_in_time),'%d/%m/%Y') dtcheckin,\r\n"
				+ "concat(shift_name,' - ',from_time,' to ',to_time) shiftNameConcat,\r\n"
				+ "nozzle_name,fm.item_name,opening_reading,closing_reading,\r\n"
				+ "(closing_reading-opening_reading) diffReading from\r\n"
				+ "trn_nozzle_register tnr ,shift_master sm,\r\n"
				+ "nozzle_master nm ,mst_items fm ,tbl_user_mst tum\r\n"
				+ "where\r\n"
				+ "tnr.shift_id=sm.shift_id and nm.nozzle_id =tnr.nozzle_id and nm.item_id =fm.item_id and tum.user_id =tnr.user_id\r\n"
				+ "and date(check_in_time) between ? and ? and tnr.app_id=? order by tnr.check_in_time";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getStockRegister(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));
		parameters.add(hm1.get("app_id"));

		String query = "select store_name,item_name,qty,type,invoice_id,name,stockreg.updated_date,date_format(stockreg.updated_date,'%d/%m/%Y %H:%i') as FormattedUpdatedDate from \r\n"
				+ "trn_stock_register stockreg,\r\n" + "mst_items  item ,\r\n" + "mst_store store,\r\n"
				+ "tbl_user_mst user\r\n" + "					where \r\n"
				+ "					date(stockreg.updated_date) between ? and ? and  \r\n"
				+ "					item.item_id=stockreg.item_id and user.user_id=stockreg.updated_by\r\n"
				+ "					and stockreg.store_id=store.store_id and stockreg.app_id=?";
		if (!hm1.get("storeId").toString().equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and stockreg.store_id=?";
		}

		query += " order by stockreg.updated_date desc";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getInventoryCountingListForThisStore(HashMap<String, Object> hm1,
			Connection con) throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select store_name,item_name,qty,type,invoice_id,name,stockreg.updated_date,date_format(stockreg.updated_date,'%d/%m/%Y %H:%i') as FormattedUpdatedDate from \r\n"
				+ "trn_stock_register stockreg,\r\n" + "mst_items  item ,\r\n" + "mst_store store,\r\n"
				+ "tbl_user_mst user\r\n" + "					where \r\n" + "					  \r\n"
				+ "					item.item_id=stockreg.item_id and user.user_id=stockreg.updated_by\r\n"
				+ "					and stockreg.store_id=store.store_id and stockreg.type=?";
		parameters.add(hm1.get("stockType"));
		if (!hm1.get("storeId").toString().equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and stockreg.store_id=?";
		}

		query += " order by stockreg.updated_date desc";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getDailyDebitRegister(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));

		String query = "select *,date_format(payment_date,'%d/%m/%Y') as FormattedPaymentDate,payment_for,name,date_format(inv.updated_date,'%d/%m/%Y %H:%i') as FormattedUpdatedDate  \r\n"
				+ "				 from trn_payment_register inv left outer join mst_customer cust on inv.customer_id=cust.customer_id\r\n"
				+ "				  inner join tbl_user_mst user on user.user_id=inv.updated_by\r\n"
				+ "				  where date(payment_date) between ? and ?  and inv.activate_flag=1 and inv.app_id=? and payment_for in ('Debit Entry') ";

		if (hm.get("paymentMode") != null && !hm.get("paymentMode").equals("")) {
			query += " and payment_mode=?";
			parameters.add(hm.get("paymentMode"));
		}

		if (hm.get("storeId") != null && !hm.get("storeId").equals("")) {
			query += "and inv.store_id = ?";
			parameters.add(hm.get("storeId"));
		}

		if (hm.get("paymentFor") != null && !hm.get("paymentFor").equals("")) {
			query += " and inv.payment_for = ?";
			parameters.add(hm.get("paymentFor"));
		}

		query += "order by inv.updated_date desc;";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getDailyPaymentRegister(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));

		String query = "select *,date_format(payment_date,'%d/%m/%Y') as FormattedPaymentDate,payment_for,name,date_format(inv.updated_date,'%d/%m/%Y %H:%i') as FormattedUpdatedDate \r\n"
				+ "				 from trn_payment_register inv left outer join mst_customer cust on inv.customer_id=cust.customer_id\r\n"
				+ "				  inner join tbl_user_mst user on user.user_id=inv.updated_by left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id =inv.ref_id \r\n"
				+ "				  where date(payment_date) between ? and ?  and inv.activate_flag=1 and inv.app_id=? and payment_for not in ('Debit Entry') ";

		if (hm.get("paymentMode") != null && !hm.get("paymentMode").equals("")) {
			query += " and payment_mode=?";
			parameters.add(hm.get("paymentMode"));
		}

		if (hm.get("storeId") != null && !hm.get("storeId").equals("")) {
			query += "and inv.store_id = ?";
			parameters.add(hm.get("storeId"));
		}

		if (hm.get("paymentFor") != null && !hm.get("paymentFor").equals("")) {
			query += " and inv.payment_for = ?";
			parameters.add(hm.get("paymentFor"));
		}

		if (hm.get("attendant_id") != null && !hm.get("attendant_id").equals("")) {
			query += " and rifd.attendant_id = ?";
			parameters.add(hm.get("attendant_id"));
		}

		query += "order by inv.updated_date desc;";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getPendingCustomerCollection(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select sum(amount) PendingAmount,T.customer_id,T.customer_name,T.mobile_number ,T.customer_reference,T.alternate_mobile_no,T.city from \r\n"
						+
						"(\r\n" +
						"select mc.customer_id,mc.customer_name,mc.mobile_number ,mc.alternate_mobile_no,mc.customer_reference, mc.city,(tir.total_amount) amount from mst_customer mc\r\n"
						+
						"left outer join trn_invoice_register tir on tir.activate_flag=1 and mc.customer_id =tir.customer_id and tir.invoice_date  between ? and ?  and mc.app_id=?\r\n"
						+
						"union all\r\n" +
						"select mc.customer_id,mc.customer_name,mc.mobile_number ,mc.alternate_mobile_no,mc.customer_reference ,mc.city,(tpr.amount*-1)  from mst_customer mc\r\n"
						+
						"left outer join trn_payment_register tpr on mc.customer_id =tpr.customer_id and tpr.payment_date  between ? and ? and mc.app_id=? and tpr.activate_flag =1 \r\n"
						+
						")\r\n" +
						" as T group by T.customer_id having PendingAmount>0 order by T.customer_name",
				con);

	}

	public List<LinkedHashMap<String, Object>> getEmployeeWiseReport(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("storeId").toString());

		return getListOfLinkedHashHashMap(parameters,
				"select username EmployeeName, c.store_name StoreName, invoice_date InvoiceDate, \r\n"
						+ " sum(total_amount) TotalAmount,date_format(a.updated_date,'%d/%m/%Y %H:%i') as FormattedInvoiceDate from trn_invoice_register a, tbl_user_mst b, mst_store c \r\n"
						+ " where a.updated_by = b.user_id and a.store_id = c.store_id and a.invoice_date between ? and ? \r\n"
						+ " and a.store_id = ? and a.activate_flag=1 group by a.invoice_date,a.updated_by;",
				con);
	}

	public List<LinkedHashMap<String, Object>> getCategoryWiseReport(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add((hm.get("app_id").toString()));

		return getListOfLinkedHashHashMap(parameters,
				"select cat.category_name CategoryName, sto.store_name StoreName,\r\n "
						+ " sum(total_amount) TotalAmount from trn_invoice_register tir, trn_invoice_details tid,\r\n"
						+ " mst_items itm, mst_category cat, mst_store sto where tir.invoice_id=tid.invoice_id and tid.item_id = itm.item_id\r\n"
						+ " and itm.parent_category_id = cat.category_id and tir.store_id = sto.store_id and tir.invoice_date between ? and ? \r\n"
						+ " and tir.activate_flag=1 and tir.app_id=? group by tir.store_id,itm.parent_category_id;",
				con);
	}

	public long getSequenceForItem(Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		long product_code = Long.parseLong(
				getMap(parameters, "select current_seq_no from seq_master where sequence_name='Item' for update ",
						conWithF).get("current_seq_no"));
		insertUpdateDuablDB("update seq_master set  current_seq_no=current_seq_no+1 where sequence_name='Item' ",
				parameters, conWithF);
		return product_code + 1;
	}

	public boolean checkIfProductCodeAlreadyExist(HashMap<String, Object> hm, Connection conWithF)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select count(1) cnt from mst_items where product_code=? and activate_flag=1 and app_id=?";
		parameters.add(hm.get("product_code"));
		parameters.add(hm.get("app_id"));
		if (!hm.get("hdnItemId").equals("")) {
			query += " and item_id!=?";
			parameters.add(hm.get("hdnItemId"));
		}
		int count = Integer.parseInt(getMap(parameters, query, conWithF).get("cnt"));
		return count != 0;
	}

	public String checkifStockAlreadyExist(long storeId, long itemId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		parameters.add(itemId);
		String stockId = getMap(parameters,
				"select stock_id from stock_status where store_id=? and item_id=? and activate_flag=1", con)
				.get("stock_id");
		stockId = stockId == null ? "0" : stockId;
		return stockId;
	}

	public void addStoreItemMapping(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("hdnItemId"));
		insertUpdateDuablDB("delete from store_item_mpg where item_id=?", parameters, conWithF);

		List<String> availableStoreIds = (List<String>) hm.get("availableStoreIds");
		for (String s : availableStoreIds) {
			parameters = new ArrayList<>();
			parameters.add(s);
			parameters.add(hm.get("hdnItemId"));
			parameters.add(hm.get("app_id"));

			insertUpdateDuablDB("insert into store_item_mpg values (default,?,?,sysdate(),1,?)", parameters, conWithF);
		}

	}

	public List<String> getListOfStoresForThisItem(String itemId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		return getListOfString(parameters, "select store_id from store_item_mpg where item_id=?", con);

	}

	public List<String> getListOfTermsAndConditionForThisItem(String termsId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(termsId);
		return getListOfString(parameters,
				"select terms_condition_id from mst_terms_and_conditions where terms_condition_id=?", con);

	}

	public List<String> ListOfTermsAndCondition(String itemId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		return getListOfString(parameters, "select terms_condition_id from mst_terms_and_conditions where app_id=?",
				con);

	}

	public List<LinkedHashMap<String, Object>> getTableStatus(int storeId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		return getListOfLinkedHashHashMap(parameters, "select table1.table_id,table_no,tor.*,dtls.*,item.*,\r\n"
				+ "	concat('', SEC_TO_TIME(TIMESTAMPDIFF(second, start_time, sysdate() ))) activeSince,\r\n"
				+ "	sum(qty) totalQty,\r\n"
				+ "	sum(qty*price) totalAmount from  mst_tables table1 "
				+ " left outer join trn_order_register tor on tor.order_id =table1.order_id left outer join trn_order_details dtls on dtls.order_id=tor.order_id left outer join mst_items item on item.item_id=dtls.item_id "
				+ "where store_id =? group by table1.table_id", con);

	}

	public List<LinkedHashMap<String, Object>> getListOfTables(int storeId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		return getListOfLinkedHashHashMap(parameters,
				"select * from  mst_tables mt  where  store_id=? and order_id is not null order by table_no", con);

	}

	public List<LinkedHashMap<String, Object>> getListOfTablesForOnGoingOrders(int storeId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		return getListOfLinkedHashHashMap(parameters, "select\r\n"
				+ "	distinct(mt.table_id),table_no \r\n"
				+ "from\r\n"
				+ "	mst_tables mt,\r\n"
				+ "	trn_order_register tor ,\r\n"
				+ "	trn_order_details tod \r\n"
				+ "where\r\n"
				+ "	store_id =?\r\n"
				+ "	and mt.order_id is not null\r\n"
				+ "	and tor.order_id =mt.order_id and tor.order_id =tod.order_id \r\n"
				+ "	and served_time is null\r\n"
				+ "order by\r\n"
				+ "	table_no;\r\n"
				+ "	", con);

	}

	public List<LinkedHashMap<String, Object>> getFryPlanning(Connection con, String appId)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\n" + 
"raw_material_name,round(ceil(sum(finalReqBags)),0) noOfBagsreq\n" + 
"from\n" + 
"(\n" + 
"select\n" + 
"item_name,\n" + 
"raw_material_name,\n" + 
"sum(tid.qty) orderQty ,\n" + 
"COALESCE (ttss.qty,0) stockQty,\n" + 
"sum(tid.qty) - COALESCE (ttss.qty,0) reqQtyToProduce,\n" + 
"((sum(tid.qty) - COALESCE (ttss.qty,0)) / mi.lds_per_raw_material) finalReqBags,\n" + 
"mi.lds_per_raw_material\n" + 
"from\n" + 
"trn_invoice_register tir\n" + 
"left outer join snacks_invoice_status sis on\n" + 
"sis.invoice_id = tir.invoice_id\n" + 
"left outer join trn_invoice_details tid on\n" + 
"tid.invoice_id = tir.invoice_id\n" + 
"left outer join mst_items mi on\n" + 
"mi.item_id = tid.item_id\n" + 
"left outer join raw_material_master rmm on\n" + 
"mi.raw_material_id = rmm.raw_material_id\n" + 
"left outer join mst_category mc on\n" + 
"mc.category_id = mi.parent_category_id\n" + 
"left outer join trn_todays_stock_snacks ttss on ttss.item_id =mi.item_id \n" + 
"where\n" + 
"tir.app_id = ? and tir.activate_flag=1 and mc.category_name='Fry'\n" + 
"and sis.curr_status = 1 group by item_name order by item_name,raw_material_name) as M\n" + 
"where reqQtyToProduce>0\n" + 
"group by raw_material_name\n";
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getOrderFormDetails(Connection con, String appId, String invoiceId)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\n" +
				"mi.item_name ,tid.item_id,tir.customer_id,round(tid.qty,0) qty,ttss.qty currStock,mi.packets_in_ld\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"left outer join snacks_invoice_status sis on sis.invoice_id =tir.invoice_id\n" +
				"left outer join trn_invoice_details tid on tid.invoice_id =tir.invoice_id\n" +
				"left outer join mst_items mi on mi.item_id =tid.item_id\n" +
				"left outer join trn_todays_stock_snacks ttss on ttss.item_id =mi.item_id \n"
				+
				"where\n" +
				"tir.app_id = ? and tir.invoice_id=? \n" +
				" \n" +
				"group by tid.item_id";
		parameters.add(appId);
		parameters.add(invoiceId);
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getReadingReportDetails(Connection con, String appId,String[] invoiceIds)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		String questionMarks = "";
		for (String s : invoiceIds) {
			parameters.add(s);
			questionMarks += "?,";
		}
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);

		String query = "select\n" +
				"mi.item_name ,tid.item_id,tir.customer_id,round(tid.qty,0) qty,mc.customer_name,concat( mc.customer_id,'~',tid.item_id ) theKey\n"
				+
				"from\n" +
				"trn_invoice_register tir\n" +
				"left outer join snacks_invoice_status sis on sis.invoice_id =tir.invoice_id\n" +
				"left outer join trn_invoice_details tid on tid.invoice_id =tir.invoice_id\n" +
				"left outer join mst_customer mc on mc.customer_id =tir.customer_id\n" +
				"left outer join mst_items mi on mi.item_id =tid.item_id\n" +
				"where\n" +
				"tir.invoice_id in ("+questionMarks+")\n" +
				"and sis.curr_status =1 and tir.app_id = ? ";
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getReadingReport(Connection con, String appId,String[] invoiceIds,String considerStock)
			throws ClassNotFoundException, SQLException {

				ArrayList<Object> parameters = new ArrayList<>();

				if(considerStock.equals("true"))
				{
					parameters.add(1);
				}
				else
				{
					parameters.add(0);
				}

				

				String questionMarks = "";
		for (String s : invoiceIds) {
			parameters.add(s);
			questionMarks += "?,";
		}
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
		
		String query = "select\n" +
				"mi.item_name ,tid.item_id,tir.customer_id,tid.qty,round(ttss.qty*?,0) currStock,mi.packets_in_ld\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"left outer join snacks_invoice_status sis on sis.invoice_id =tir.invoice_id\n" +
				"left outer join trn_invoice_details tid on tid.invoice_id =tir.invoice_id\n" +
				"left outer join mst_items mi on mi.item_id =tid.item_id\n" +
				"left outer join trn_todays_stock_snacks ttss on ttss.item_id =mi.item_id  \n"
				+  
				"where\n" +
				"tir.activate_flag=1 and tir.invoice_id in ("+questionMarks+")\n" +
				"and sis.curr_status =1 and tir.app_id = ?  \n" +
				"group by tid.item_id order by mi.order_no";
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getMakaiPlanning(Connection con, String appId)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\n" + 
"raw_material_name,round(ceil(sum(finalReqBags)),0) noOfBagsreq\n" + 
"from\n" + 
"(\n" + 
"select\n" + 
"item_name,\n" + 
"raw_material_name,\n" + 
"sum(tid.qty) orderQty ,\n" + 
"COALESCE (ttss.qty,0) stockQty,\n" + 
"sum(tid.qty) - COALESCE (ttss.qty,0) reqQtyToProduce,\n" + 
"((sum(tid.qty) - COALESCE (ttss.qty,0)) / mi.lds_per_raw_material) finalReqBags,\n" + 
"mi.lds_per_raw_material\n" + 
"from\n" + 
"trn_invoice_register tir\n" + 
"left outer join snacks_invoice_status sis on\n" + 
"sis.invoice_id = tir.invoice_id\n" + 
"left outer join trn_invoice_details tid on\n" + 
"tid.invoice_id = tir.invoice_id\n" + 
"left outer join mst_items mi on\n" + 
"mi.item_id = tid.item_id\n" + 
"left outer join raw_material_master rmm on\n" + 
"mi.raw_material_id = rmm.raw_material_id\n" + 
"left outer join mst_category mc on\n" + 
"mc.category_id = mi.parent_category_id\n" + 
"left outer join trn_todays_stock_snacks ttss on ttss.item_id =mi.item_id \n" + 
"where\n" + 
"tir.app_id = ? and tir.activate_flag=1 and mc.category_name='Makai'\n" + 
"and sis.curr_status = 1 group by item_name order by item_name,raw_material_name) as M\n" + 
"where reqQtyToProduce>0\n" + 
"group by raw_material_name\n";
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public LinkedHashMap<String, Object> getInvoiceDetails(String invoiceId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap = getMapReturnObject(parameters, "select \r\n" + "*,\r\n"
				+ "case when cust.customer_id is null then \"\" else customer_name end  as customerName,cust.city customercityname,\r\n"
				+ "date_format(invoice_date,'%d/%m/%Y') theInvoiceDate,\r\n" + "sum(qty) totalQuantities,\r\n"
				+ "paym.amount as paid_amount,date_format(invoice.updated_date,'%d/%m/%Y %h:%i%p') theUpdatedDate"
				+ ",dtls.sgst_amount ,dtls.sgst_percentage ,dtls.cgst_amount ,dtls.sgst_percentage,ried.warranty electricwarranty \r\n" + " from\r\n"
				+ " trn_invoice_register invoice inner join mst_store store1 on store1.store_id=invoice.store_id left outer join  mst_customer cust on cust.customer_id=invoice.customer_id and invoice.activate_flag=1 \r\n"
				+ " inner join  trn_invoice_details dtls on  dtls.invoice_id=invoice.invoice_id left outer join  trn_payment_register paym on paym.ref_id=invoice.invoice_id and paym.payment_for='Invoice'\r\n"
				+ " left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id=invoice.invoice_id \r\n"
				+ " left outer join rlt_invoice_electric_details ried on ried.invoice_id=invoice.invoice_id \r\n"
				+ "where invoice.invoice_id=? order by dtls.details_id", con);

		parameters = new ArrayList<>();
		parameters.add(invoiceId);

		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select tsd.*,item.*,dtls.*,cat.*,return1.*,ribd.*,ried.*,ried.warranty as warrantyelectric,"
						+ "(select case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath from tbl_attachment_mst tam2 "
						+ "where tam2.file_id=item.item_id and tam2.type='Image' limit 1 ) ImagePath,"
						+ " sum(coalesce(qty_to_return,0)) ReturnedQty,dtls.details_id theDetailsId \r\n"
						+ "from mst_items item  inner join trn_invoice_details dtls on item.item_id=dtls.item_id "
						+ "inner join mst_category cat on cat.category_id=item.parent_category_id \r\n"
						+ "	left outer join trn_return_register return1 on return1.details_id=dtls.details_id"
						+ " left outer join trn_sph_details tsd on tsd.details_id=dtls.details_id \r\n"
						+ " left outer join rlt_invoice_battery_details ribd on ribd.details_id=dtls.details_id \r\n"
						+ " left outer join rlt_invoice_electric_details ried on ried.details_id=dtls.details_id \r\n"
						+ "where\r\n" + "dtls.invoice_id = ? group by dtls.details_id  order by dtls.details_id ", con));
		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getInvoiceElectric(String invoiceId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap = getMapReturnObject(parameters, "select \r\n" + "*,\r\n"
				+ "case when cust.customer_id is null then \"\" else customer_name end  as customerName,cust.city customercityname,csm.state_name customerstatename, \r\n"
				+ "date_format(invoice_date,'%d/%m/%Y') theInvoiceDate,\r\n" + "sum(qty) totalQuantities,\r\n"
				+ "paym.amount as paid_amount,date_format(invoice.updated_date,'%d/%m/%Y %h:%i%p') theUpdatedDate"
				+ ",dtls.sgst_amount ,dtls.sgst_percentage ,dtls.cgst_amount ,dtls.sgst_percentage,ried.warranty electricwarranty \r\n" + " from\r\n"
				+ " trn_invoice_register invoice inner join mst_store store1 on store1.store_id=invoice.store_id left outer join  mst_customer cust on cust.customer_id=invoice.customer_id and invoice.activate_flag=1 \r\n"
				+ " inner join  trn_invoice_details dtls on  dtls.invoice_id=invoice.invoice_id left outer join  trn_payment_register paym on paym.ref_id=invoice.invoice_id and paym.payment_for='Invoice'\r\n"
				+ " left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id=invoice.invoice_id \r\n"
				+ " left outer join cmn_state_mst csm on csm.state_id=cust.state_id \r\n"
				+ " left outer join rlt_invoice_electric_details ried on ried.invoice_id=invoice.invoice_id \r\n"
				+ "where invoice.invoice_id=? order by dtls.details_id", con);

		parameters = new ArrayList<>();
		parameters.add(invoiceId);

		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select tsd.*,item.*,dtls.*,cat.*,return1.*,ribd.*,ried.*,ried.warranty as warrantyelectric,"
						+ "(select case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath from tbl_attachment_mst tam2 "
						+ "where tam2.file_id=item.item_id and tam2.type='Image' limit 1 ) ImagePath,"
						+ " sum(coalesce(qty_to_return,0)) ReturnedQty,dtls.details_id theDetailsId \r\n"
						+ "from mst_items item  inner join trn_invoice_details dtls on item.item_id=dtls.item_id "
						+ "inner join mst_category cat on cat.category_id=item.parent_category_id \r\n"
						+ "	left outer join trn_return_register return1 on return1.details_id=dtls.details_id"
						+ " left outer join trn_sph_details tsd on tsd.details_id=dtls.details_id \r\n"
						+ " left outer join rlt_invoice_battery_details ribd on ribd.details_id=dtls.details_id \r\n"
						+ " left outer join rlt_invoice_electric_details ried on ried.details_id=dtls.details_id \r\n"
						+ "where\r\n" + "dtls.invoice_id = ? group by dtls.details_id  order by dtls.details_id ", con));

		List<LinkedHashMap<String, Object>> lst=getListOfLinkedHashHashMap(parameters, "select item.item_name,sum(dtls.custom_rate) setSumCustomRate,sum(dtls.qty) setQty,sum(custom_rate*qty) setSumAmount "								
		+ "from mst_items item  inner join trn_invoice_details dtls on item.item_id=dtls.item_id "
		+ "inner join mst_category cat on cat.category_id=item.parent_category_id \r\n"
		+ "	left outer join trn_return_register return1 on return1.details_id=dtls.details_id"
		+ " left outer join trn_sph_details tsd on tsd.details_id=dtls.details_id \r\n"
		+ " left outer join rlt_invoice_battery_details ribd on ribd.details_id=dtls.details_id \r\n"
		+ " left outer join rlt_invoice_electric_details ried on ried.details_id=dtls.details_id \r\n"
		+ "where item.item_name like '%Set%' and \r\n" + "dtls.invoice_id = ? group by item_name"  , con);

		itemDetailsMap.put("listOfSet",lst);

		
								HashMap<String, LinkedHashMap<String, Object>> resultMap = new HashMap<>();

								for (LinkedHashMap<String, Object> item : lst) {
									// Assuming the name of the item is in the "item_name" key
									String itemName = (String) item.get("item_name");  // Replace "item_name" with the actual key for the item name
						
									if (itemName != null) {
										resultMap.put(itemName, item);
									}
								}
		
		itemDetailsMap.put("resultMap",resultMap);


		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getPurchaseInvoiceDetails(String invoiceId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap = getMapReturnObject(parameters,
				"select *, date_format(tpir.invoice_date,'%d/%m/%Y') purchaseDate from trn_purchase_invoice_register tpir,mst_vendor vend where invoice_id =? and tpir.vendor_id=vend.vendor_id",
				con);

		parameters = new ArrayList<>();
		parameters.add(invoiceId);

		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters,
						"select * from trn_purchase_invoice_details tpid,mst_items mi where invoice_id=? and mi.item_id=tpid.item_id",
						con));
		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getQuoteDetails(String quoteId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(quoteId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap = getMapReturnObject(parameters, "select \r\n" + "*,\r\n"
				+ "case when cust.customer_id is null then \"\" else customer_name end  as customerName,\r\n"
				+ "date_format(quote_date,'%d/%m/%Y') theQuoteDate,\r\n" + "sum(qty) totalQuantities\r\n"
				+ " from\r\n"
				+ " trn_quote_register invoice inner join mst_store store1 on store1.store_id=invoice.store_id left outer join  mst_customer cust on cust.customer_id=invoice.customer_id and invoice.activate_flag=1 \r\n"
				+ " inner join  trn_quote_details dtls on  dtls.quote_id=invoice.quote_id inner join mst_items item on item.item_id=dtls.item_id \r\n"
				+ "where invoice.quote_id=? order by dtls.details_id", con);

		parameters = new ArrayList<>();
		parameters.add(quoteId);

		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select item.*,dtls.*,cat.*,"
						+ "(select case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath from tbl_attachment_mst tam2 "
						+ "where tam2.file_id=item.item_id and tam2.type='Image' limit 1 ) ImagePath "
						+ "from mst_items item  inner join trn_quote_details dtls on item.item_id=dtls.item_id "
						+ "inner join mst_category cat on cat.category_id=item.parent_category_id \r\n"
						+ "	 \r\n"
						+ "where\r\n" + "quote_id = ? group by dtls.details_id  order by dtls.details_id ", con));
		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getInvoiceDetailsForTable(String tableId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(tableId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap.put("invoice_id", "0");
		itemDetailsMap.put("theInvoiceDate", getDateFromDB(con));
		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select\r\n"
						+ "	mt.table_no,item_name,mi.item_id,price as rate,price as custom_rate,sum(qty) qty,mi.sgst sgst_percentage,mi.cgst cgst_percentage,"
						+ "price*sgst/100 as sgst_amount,price*cgst/100 as cgst_amount \r\n"
						+ "from\r\n"
						+ "	mst_tables mt,trn_order_details tod,mst_items mi \r\n"
						+ "where\r\n"
						+ "	table_id = ? and tod.order_id =mt.order_id and mi.item_id =tod.item_id group by item_id",
						con));
		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getInvoiceDetailsForBooking(String bookingId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(bookingId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap.put("invoice_id", "0");
		itemDetailsMap.put("theInvoiceDate", getDateFromDB(con));
		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select\r\n"
						+ "*,tbr.booking_id,item_name,mi.item_id,price as rate,price as custom_rate,qty \r\n"
						+ "from\r\n"
						+ "	trn_booking_register tbr,booking_item_mpg bim,mst_items mi,mst_customer cust \r\n"
						+ "where\r\n"
						+ "	tbr.booking_id=? and bim.booking_id =tbr.booking_id and mi.item_id =bim.item_id and cust.customer_id=tbr.customer_id",
						con));
		return itemDetailsMap;

	}

	public LinkedHashMap<String, Object> getInvoiceDetailsForMobileBooking(String bookingId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(bookingId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap.put("invoice_id", "0");
		itemDetailsMap.put("theInvoiceDate", getDateFromDB(con));
		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters, "select\r\n"
						+ "	*,price as rate,price as custom_rate,quantity qty,date_format(torf.created_date, '%d/%m/%Y %H:%i') as FormattedCreatedDate \r\n"
						+ "from\r\n"
						+ "	trn_order_register_frommobileapp torf ,\r\n"
						+ "	trn_suborder_register tsr,\r\n"
						+ "	customer_user_mpg cum ,\r\n"
						+ "	mst_items mi \r\n"
						+ "where\r\n"
						+ "	torf.order_id = tsr.order_id and cum.user_id =torf .user_id and torf.order_id =? and tsr.item_id =mi.item_id ;",
						con));
		return itemDetailsMap;

	}

	public List<LinkedHashMap<String, Object>> getConsolidatedPaymentModeCollection(HashMap<String, Object> hm1,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm1.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));

		String query = "select\r\n" + "	store.store_name StoreName,\r\n" + "	sum(amount) TotalAmount,\r\n"
				+ "	payment_mode PaymentMode,\r\n" + "	payment_date PaymentDate,\r\n"
				+ "	date_format(invoice.payment_date, '%d/%m/%Y %H:%i') as FormattedInvoiceDate\r\n" + "from\r\n"
				+ "	trn_payment_register invoice,\r\n" + "	mst_store store\r\n" + "where\r\n"
				+ "	invoice.store_id = store.store_id and invoice.app_id=?\r\n"
				+ "	and date(payment_date) between ? and ?  and invoice.activate_flag=1 ";

		if (!hm1.get("storeId").toString().equals("-1")) {
			query += " and store.store_id=? ";
			parameters.add(hm1.get("storeId").toString());
		}
		query += "group by PaymentDate,payment_mode order by payment_date desc";
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getPaymentTypeCollectionCollection(HashMap<String, Object> hm1,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add((hm1.get("app_id").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));
		String query = "select store.store_name StoreName,payment_type PaymentType,invoice_date InvoiceDate,sum(total_amount) Amount from "
				+
				"trn_invoice_register invoice, mst_store store where invoice.activate_flag=1 and invoice.store_id=store.store_id and invoice.app_id=? and date(invoice_date) between ? and ? \r\n";

		if (!hm1.get("storeId").toString().equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and store.store_id=? ";
		}
		query += " group by payment_type,store.store_id, invoice_date order by invoice_date desc";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public String updateGroup(long categoryId, Connection conWithF, String categoryName) throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryName);
		parameters.add(categoryId);
		insertUpdateDuablDB("UPDATE customer_group  SET group_Name=?,updated_date=SYSDATE() WHERE group_id=?",
				parameters, conWithF);
		return "Group updated Succesfully";

	}

	public String deleteGroup(long itemId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		insertUpdateDuablDB("UPDATE customer_group  SET activate_flag=0 WHERE group_id=?", parameters, conWithF);
		return "Group Deleted Succesfully";
	}

	public long updateLowStockDetails(long stock_id, long lowqty, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(lowqty);
		parameters.add(stock_id);
		return insertUpdateDuablDB("update stock_status set low_stock_limit=? where stock_id=?", parameters, conWithF);

	}

	public long addGroup(Connection conWithF, String groupName, String appId) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("group_id", "~default");
		valuesMap.put("group_name", groupName);
		valuesMap.put("updated_by", "1");
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("activate_flag", "1");
		valuesMap.put("app_id", appId);

		Query q = new Query("customer_group", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getCustomerGroup(String appId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, "select * from customer_group where activate_flag=1 and app_id=?",
				con);
	}

	public LinkedHashMap<String, String> getGroupDetails(long categoryId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryId);
		return getMap(parameters, "select * from customer_group where group_id=?", con);
	}

	public LinkedHashMap<String, String> getInvoiceSubDetails(long detailId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(detailId);
		return getMap(parameters, "select\r\n"
				+ "	register.customer_id,dtls.custom_rate,register.invoice_id,dtls.item_id,dtls.details_id,item_name,qty-coalesce(sum( qty_to_return),0) returnAbleQty\r\n"
				+ "from\r\n" + "	trn_invoice_register register,\r\n" + "	\r\n"
				+ " 	mst_items item ,trn_invoice_details dtls left outer join\r\n"
				+ "	trn_return_register ret on ret.details_id=dtls.details_id where\r\n"
				+ "	dtls.details_id =?\r\n" + "	and item.item_id = dtls.item_id\r\n"
				+ "	 and register.activate_flag=1 and register.invoice_id = dtls.invoice_id;\r\n" + "\r\n" + "\r\n"
				+ "", con);
	}

	public void insertToTrnReturnRegister(long detailsId, Double returnqty, String userId, String appId,
			Connection conWithF)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(detailsId);
		parameters.add(returnqty);
		parameters.add(userId);
		parameters.add(appId);
		insertUpdateDuablDB("insert into trn_return_register values (default,?,?,?,sysdate(),?);", parameters,
				conWithF);
	}

	public LinkedHashMap<String, String> getuserDetailsById(long userId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		return getMap(parameters, "select * from tbl_user_mst where user_id=?", con);

	}

	public String updateStoreForThisUser(long storeId, String userId, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		parameters.add(userId);
		insertUpdateDuablDB("update tbl_user_mst set store_id=? where user_id=?", parameters, conWithF);
		return "Updated Succesfully";
	}

	public String updateConfigurationForThisUser(long invoiceFormat, String invoice_default_checked_print,
			String invoice_default_checked_generatepdf, String restaurant_default_checked_generatepdf,
			String user_total_payments, String user_payment_collections, String user_counter_sales,
			String user_payment_sales, String user_store_sales, String user_store_bookings, String user_store_expenses,
			String userId, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceFormat);

		parameters.add(invoice_default_checked_print);
		parameters.add(invoice_default_checked_generatepdf);
		parameters.add(restaurant_default_checked_generatepdf);
		parameters.add(user_total_payments);
		parameters.add(user_payment_collections);
		parameters.add(user_counter_sales);
		parameters.add(user_payment_sales);
		parameters.add(user_store_sales);
		parameters.add(user_store_bookings);
		parameters.add(user_store_expenses);

		parameters.add(userId);
		insertUpdateDuablDB("update user_configurations set invoice_format=?,invoice_default_checked_print=?,"
				+ "invoice_default_checked_generatepdf=?,restaurant_default_checked_generatepdf=?,user_total_payments=?,"
				+ "user_payment_collections=?,user_counter_sales=?,user_payment_sales=?,user_store_sales=?,user_store_bookings=?,"
				+ "user_store_expenses=? where user_id=?", parameters, conWithF);
		return "Updated Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getEmployeeMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select	* from tbl_user_mst user,mst_store store"
						+ " where user.activate_flag = 1	and store.store_id = user.store_id and store.app_id=? and user.app_id=store.app_id order by user.name",
				con);
	}

	public List<LinkedHashMap<String, Object>> getTimeline(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		return getListOfLinkedHashHashMap(parameters,
				" select sysdate() as testVariable from dual",
				con);
	}

	public List<LinkedHashMap<String, Object>> getEmployeeMasterForStore(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("store_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select	* from tbl_user_mst user,mst_store store"
						+ " where user.activate_flag = 1	and store.store_id = user.store_id and store.app_id=? and user.app_id=store.app_id and store.store_id=?",
				con);
	}

	public List<LinkedHashMap<String, Object>> getModelList(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select distinct(model) modelName from trn_booking_register where app_id=?",
				con);
	}

	public String updateEmployee(long employeeId, Connection conWithF, HashMap<String, Object> hm) throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("username"));
		parameters.add(hm.get("EmployeeName"));
		parameters.add(Long.parseLong(hm.get("MobileNumber").toString()));
		parameters.add(hm.get("email").toString());
		parameters.add(Long.parseLong(hm.get("txtstore").toString()));

		parameters.add(employeeId);

		insertUpdateDuablDB(
				"UPDATE tbl_user_mst  SET username=?, name = ?,updated_date=SYSDATE(),mobile=?,email=?,store_id=? WHERE user_id=?",
				parameters, conWithF);
		return "Employee Updated Succesfully";

	}

	public long addEmployee(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("username"));
		parameters.add(getSHA256String("123"));
		parameters.add(hm.get("EmployeeName"));
		parameters.add(hm.get("MobileNumber").toString());
		parameters.add(hm.get("email").toString());
		parameters.add(Long.parseLong(hm.get("txtstore").toString()));
		parameters.add(Long.parseLong(hm.get("app_id").toString()));
		String insertQuery = "insert into tbl_user_mst values (default,?,?,sysdate(),null,1,?,?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public void saveLoadingDetails(Connection con, List<Map<String, Object>> items) throws Exception 
	{    
	
    try 
	{
        // Iterate through the list of items
        for (Map<String, Object> item : items) {
            ArrayList<Object> parameters = new ArrayList<>();
            parameters.add(item.get("loading_id")); // loading_id
            parameters.add(item.get("line_no")); // line_no
            parameters.add(item.get("order_id")); // order_id
            parameters.add(item.get("item_id")); // item_id
            parameters.add(item.get("pending_qty")); // pending_qty
            parameters.add(item.get("loaded_qty")); // loaded_qty
            parameters.add(item.get("current_line_qty")); // current_line_qty

            // Insert query for trn_loading_details
            String insertQuery = "INSERT INTO trn_loading_details " +
                                 "(loading_id, line_no, order_id, item_id, pending_qty, loaded_qty, current_line_qty) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?)";

            
            insertUpdateDuablDB(insertQuery, parameters, con);			
        }		
    } catch (Exception e) 
	{
        throw new Exception("Error saving loading details: " + e.getMessage(), e);
    }    
}




	public long addDefaultUserConfigurations(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("user_id"));

		String insertQuery = "insert into user_configurations (user_id,invoice_format) values (?,1)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long mapCustomerEmployee(long customerId, long employeeId, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(customerId);
		parameters.add(employeeId);
		String insertQuery = "insert into customer_user_mpg values (?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public LinkedHashMap<String, String> getEmployeeDetails(long customerId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(customerId);
		return getMap(parameters, "select  * from tbl_user_mst where user_id=?", con);
	}

	public boolean mobileNoAlreadyExist(String mobileNo, String appId, Connection con) throws SQLException {
		String query = "select count(1) as cnt from mst_customer where activate_flag=1 and mobile_number=? and app_id=?";
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(mobileNo);
		parameters.add(appId);
		return !getMap(parameters, query, con).get("cnt").equals("0");
	}

	public List<LinkedHashMap<String, Object>> getItemHistory(long storeId, String itemId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(itemId);

		String query = "select *,invoice_id,qty,'' as custom_rate,"
				+ "	`type` as type,"
				+ "	date_format(tsr.updated_date, '%d/%m/%Y %H:%i:%s') as formattedUpdatedDate from"
				+ "	trn_stock_register tsr,mst_store store, tbl_user_mst user1 "
				+ "where item_id = ? and tsr.store_id = store.store_id "
				+ "	and tsr.updated_by = user1.user_id ";
		if (storeId != -1) {
			query += "and store.store_id=? ";
			parameters.add(storeId);
		}
		query += " order by tsr.stock_register_id desc";

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getItemMasterHistoryForThisItem(String itemId, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(itemId);
		parameters.add(itemId);

		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_items where item_id=? Union all select * from hst_mst_items where item_id=? order by updated_date desc",
				con);

	}

	public boolean checkIfRoleUserAlreadyExist(long userId, Long roleId, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(roleId);
		return !getMap(parameters,
				"select count(1) cnt from acl_user_role_rlt where user_id=? and role_id=? and activate_flag=1",
				conWithF).get("cnt").equals("0");

	}

	public long addUserRoleMapping(long userId, Long roleId, String roleName, Connection conWithF) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(roleId);
		parameters.add(roleName);
		return insertUpdateDuablDB("insert into acl_user_role_rlt values (default,?,?,1,sysdate(),null,?)", parameters,
				conWithF);

	}

	public List<LinkedHashMap<String, Object>> getReturnRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select\r\n"
						+ "	tid.*,\r\n"
						+ "	trr.*,user.*,tir2.*\r\n"
						+ "from\r\n"
						+ "	trn_invoice_register tir2,\r\n"
						+ "	trn_invoice_details tid ,\r\n"
						+ "	trn_return_register trr,tbl_user_mst user\r\n"
						+ "where\r\n"
						+ "	invoice_date between ? and ?\r\n"
						+ "	and tid.invoice_id = tir2.invoice_id\r\n"
						+ "	and trr.details_id = tid.details_id\r\n"
						+ "	and tir2.app_id = ? and trr.updated_by =user.user_id ;\r\n"
						+ "\r\n"
						+ "",
				con);

	}

	public List<LinkedHashMap<String, Object>> getCustomerInvoiceHistory(String customerId, String fromDate,
			String toDate, Connection con, String appId) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = " select\r\n" + "	*,\r\n"
				+ "	date_format(invoice_date, '%d/%m/%Y') as formattedInvoiceDate,\r\n"
				+ "	date_format(a.updated_date, '%d/%m/%Y %H:%i:%s') as formattedUpdatedDate,\r\n"
				+ "	rate-custom_rate Discount,\r\n"
				+ "	(details.qty-sum(coalesce (return1.qty_to_return,0)))*custom_rate as ItemAmount, \r\n"
				+ "	(details.qty)*custom_rate as ActualItemAmount, \r\n"
				+ "	((details.qty-sum(coalesce (return1.qty_to_return,0)))*rate - (details.qty-sum(coalesce (return1.qty_to_return,0)))*custom_rate) DiscountAmount,\r\n"
				+ "	details.qty-sum(coalesce (return1.qty_to_return,0)) as BilledQty, sum(coalesce(qty_to_return,0)) as sumReturnQty	\r\n"
				+ "from\r\n"
				+ "mst_items item inner join trn_invoice_details details on item.item_id=details.item_id\r\n"
				+ "inner join	trn_invoice_register a on a.activate_flag=1 and details.invoice_id=a.invoice_id\r\n"
				+ "left outer join mst_customer b on	a.customer_id = b.customer_id and b.activate_flag=1 \r\n"
				+ "left outer join  trn_return_register return1 on details.details_id=return1.details_id\r\n"
				+ "where\r\n" + "	a.activate_flag = 1\r\n" + "	\r\n"
				+ "	 \r\n" + "	and a.app_id=? and date(a.invoice_date) between ? and ?  ";

		parameters.add((appId));

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		if (customerId != null && !customerId.equals("")) {
			query += " and b.customer_id = ? ";
			parameters.add(Long.valueOf(customerId));
		}

		query += "group by details.details_id order by a.invoice_date,a.invoice_id,details.details_id";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getCustomerItemHistory(String appId, String customerId, String fromDate,
			String toDate,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = " select\r\n" + "	*,\r\n"
				+ "	date_format(invoice_date, '%d/%m/%Y') as formattedInvoiceDate,\r\n"
				+ "	date_format(a.updated_date, '%d/%m/%Y %H:%i:%s') as formattedUpdatedDate,\r\n"
				+ "	rate-custom_rate Discount,\r\n"
				+ "	(details.qty-coalesce(coalesce (return1.qty_to_return,0),0))*custom_rate as ItemAmount, \r\n"
				+ "	((details.qty-coalesce(coalesce (return1.qty_to_return,0),0))*rate - (details.qty-coalesce(coalesce (return1.qty_to_return,0),0))*custom_rate) DiscountAmount,\r\n"
				+ "	details.qty-coalesce(coalesce (return1.qty_to_return,0),0) as BilledQty,category.category_name	\r\n"
				+ "from\r\n"
				+ "mst_items item inner join trn_invoice_details details on item.item_id=details.item_id "
				+ "inner join mst_category category on item.parent_category_id=category.category_id \r\n"
				+ "inner join	trn_invoice_register a on a.activate_flag=1 and details.invoice_id=a.invoice_id\r\n"
				+ "left outer join mst_customer b on	a.customer_id = b.customer_id and b.activate_flag=1 \r\n"
				+ "left outer join  trn_return_register return1 on details.details_id=return1.details_id\r\n"
				+ "where\r\n" + "	a.activate_flag = 1\r\n" + "	\r\n"
				+ "	 \r\n" + "	and date(a.invoice_date) between ? and ? and details.app_id=?  ";

		if (customerId != null && !customerId.equals("")) {
			query += " and b.customer_id = ? ";
			parameters.add(Long.valueOf(customerId));
		}

		query += " order by b.customer_name,a.invoice_date,a.invoice_id,details.details_id ";

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add(appId);

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getItemWiseReports(String appId, String customerId, String fromDate,
			String toDate,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select mi.item_name,sum(tid.qty) qty,sum(qty*custom_rate) amt,tid.item_id  from \r\n"
				+ "trn_invoice_register tir,\r\n"
				+ "trn_invoice_details tid,\r\n"
				+ "mst_items mi \r\n"
				+ "where tir.activate_flag =1 and tir.app_id =? and tir.invoice_id =tid.invoice_id and mi.item_id =tid.item_id \r\n"
				+ "and tir.invoice_date between ? and ?\r\n"
				+ "group by tid.item_id ;";

		parameters.add(appId);
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getSalesItemSummary(String appId, String storeId, String customerId,
			String fromDate, String toDate,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add(appId);
		parameters.add(storeId);

		String query = "select\r\n"
				+ "	mc.category_name,\r\n"
				+ "	item_name,\r\n"
				+ "	mi.product_code ,\r\n"
				+ "	sum(qty*custom_rate) Amount,\r\n"
				+ "	sum(tid.qty) quantity,\r\n"
				+ "	mi.item_id\r\n"
				+ "	\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir,\r\n"
				+ "	trn_invoice_details tid ,\r\n"
				+ "	mst_items mi ,\r\n"
				+ "	mst_category mc\r\n"
				+ "	\r\n"
				+ "where\r\n"
				+ "	tir.invoice_date between ? and ? \r\n"
				+ "	and tir.app_id = ?\r\n"
				+ "	and tir.invoice_id = tid.invoice_id\r\n"
				+ "	and tir.activate_flag = 1\r\n"
				+ "	and mi.item_id = tid.item_id\r\n"
				+ "	and mc.category_id = mi.parent_category_id and tir.store_id=? \r\n"
				+ "	\r\n";

		if (customerId != null && !customerId.equals("")) {
			query += " and tir.customer_id = ? ";
			parameters.add(Long.valueOf(customerId));
		}

		query += " group by item_id order by mc.category_name asc,quantity desc";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getCustomerLedgerReport(String customerId, String fromDate,
			String toDate, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\r\n"
				+ "	tir.invoice_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	invoice_id as RefId,\r\n"
				+ "	date_format(tir.invoice_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	total_amount as Amount,\r\n"
				+ "	concat('Invoice (', remarks, ' )') as type,\r\n"
				+ "	date_format(updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	'Debit' as creditDebit,\r\n"
				+ "	total_amount as debitAmount,\r\n"
				+ "	0 creditAmount\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "where\r\n"
				+ "	customer_id = ?\r\n"
				+ "	and tir.activate_flag = 1\r\n"
				+ "	and date(invoice_date) between ? and ?\r\n"
				+ "union all\r\n"
				+ "select\r\n"
				+ "	payment_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	ref_id RefId,\r\n"
				+ "	date_format(payment_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	amount*-1 as Amount,\r\n"
				+ "	concat(payment_mode, ' ( ', tpr.remarks, ' ) ', ' ( ', tpr.payment_for, ' ) ') as type,\r\n"
				+ "	date_format(tpr.updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then 'Debit'\r\n"
				+ "		else 'Credit'\r\n"
				+ "	end as creditDebit,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then amount*-1\r\n"
				+ "		else 0\r\n"
				+ "	end as debitAmount,\r\n"
				+ "	case\r\n"
				+ "		when payment_for != 'Debit Entry' then amount\r\n"
				+ "		else 0\r\n"
				+ "	end as creditAmount\r\n"
				+ "from\r\n"
				+ "	trn_payment_register tpr left outer join trn_invoice_register tir2 on tir2.invoice_id =tpr.ref_id\r\n"
				+ "where\r\n"
				+ "	tpr.customer_id = ?\r\n"
				+ "	and  tpr.activate_flag = 1\r\n"
				+ "	and date(tpr.payment_date) between ? and ?\r\n"
				+ "order by\r\n"
				+ "	orderDate,\r\n"
				+ "	upd1";

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public String getOpeningBalanceForLedger(String employeeId, String fromDate,
			String toDate, String appId, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select sum(SalesAmount) salesAmt,sum(paymentAmount) paymentAmt,dt,sum(paymentAmount)-sum(SalesAmount) openingledger,remarks,shift_name\n"
				+
				"from (\n" +
				"select\n" +
				"((totalizer_closing_reading-totalizer_opening_reading-(COALESCE (ttfr.test_quantity,0)*tnr.rate))) SalesAmount,0 paymentAmount,accounting_date dt,'Nozzle' remarks,shift_name\n"
				+
				"from\n" +
				"trn_nozzle_register tnr\n" +
				"inner join nozzle_master nm on nm.nozzle_id = tnr.nozzle_id\n" +
				"inner join tbl_user_mst tum on tum.user_id = tnr.attendant_id\n" +
				"inner join mst_items item on item.item_id = tnr.item_id\n" +
				"inner join tbl_user_mst tum2 on tum2.user_id = tnr.updated_by\n" +
				"inner JOIN shift_master shift on shift.shift_id = tnr.shift_id\n" +
				"left outer join trn_test_fuel_register ttfr on ttfr.nozzle_id =nm.nozzle_id and ttfr.test_date =accounting_date and test_type='S' and ttfr.shift_id  =tnr.shift_id and ttfr.activate_flag =1\n"
				+
				"where\n" +
				"tnr.app_id = ?\n" +
				"and accounting_date between ? and ? and tnr.activate_flag=1\n" +
				"and tum.user_id = ?\n" +
				"union all\n" +
				"select (custom_rate*qty) salesAmount,0 paymentAmount,tir.invoice_date,'Nozzle' remarks,shift_name from\n"
				+
				"trn_invoice_register tir ,\n" +
				"trn_invoice_details tid ,\n" +
				"rlt_invoice_fuel_details rifd,\n" +
				"tbl_user_mst tum,\n" +
				"shift_master sm,\n" +
				"mst_items mi\n" +
				"where\n" +
				"tir.invoice_id =tid.invoice_id\n" +
				"and rifd.invoice_id =tid.invoice_id\n" +
				"and tum.user_id =rifd.attendant_id\n" +
				"and rifd.shift_id =sm.shift_id\n" +
				"and tid.item_id =mi.item_id and (mi.item_name!='Petrol') and (mi.item_name!='Diesel')\n" +
				"and tir.app_id =? and tir.activate_flag=1\n" +
				"and tum.user_id =?\n" +
				"and tir.invoice_date between ? and ?\n" +
				"union all\n" +
				"select\n" +
				"0 SalesAmount,\n" +
				"amount  paymentAmount,\n" +
				"collection_date dt,'Nozzle' remarks,shift_name\n" +
				"from\n" +
				"trn_supervisor_collection tsc ,\n" +
				"tbl_user_mst tum,\n" +
				"shift_master sm2\n" +
				"where\n" +
				"sm2.shift_id =tsc.shift_id and sm2.activate_flag =1 and\n" +
				"tsc.attendant_id = tum.user_id\n" +
				"and tsc.collection_date between ? and ?\n" +
				"and tum.app_id=? and tsc.activate_flag=1\n" +
				"and tum.user_id =?\n" +
				"union all\n" +
				"select\n" +
				"0 salesAmount,(total_amount) paymentAmount,tir.invoice_date dt,'Nozzle',shift_name\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"inner join rlt_invoice_fuel_details rifd\n" +
				"on rifd.invoice_id =tir.invoice_id\n" +
				"inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id\n" +
				"inner join trn_payment_register tpr on tpr.ref_id=tir.invoice_id\n" +
				"left outer join shift_master sm on sm.shift_id =rifd.shift_id\n" +
				"where invoice_date between ? and ? and tpr.payment_mode !='Cash' and sm.activate_flag =1\n" +
				"and tir.app_id =? and tir.activate_flag=1 and tum.user_id =?\n" +
				"union all\n" +
				"select\n" +
				"0 salesAmount,(total_amount) paymentAmount,tir.invoice_date dt,'Nozzle' remarks,shift_name\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"inner join rlt_invoice_fuel_details rifd\n" +
				"on rifd.invoice_id =tir.invoice_id\n" +
				"inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id\n" +
				"left outer join shift_master sm on sm.shift_id =rifd.shift_id\n" +
				"where invoice_date between ? and ? and tir.payment_type='Pending' and sm.activate_flag =1\n" +
				"and tir.app_id =? and tir.activate_flag=1 and tum.user_id =?\n" +
				"union all select case when payment_type ='Debit' then amount else 0 end as salesAmount ,case when payment_type ='Credit' then amount else 0 end as paymentAmount ,payment_date,remarks,'NA' from trn_employee_payment_register tepr where payment_date between ? and ? and tepr.app_id=? and tepr.employee_id=? and tepr.activate_flag=1 )\n"
				+
				"as T ;\n";

		parameters.add((appId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add(employeeId);

		parameters.add((appId));
		parameters.add(employeeId);
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		return getMap(parameters, query, con).get("openingledger");

	}

	public List<LinkedHashMap<String, Object>> getFSMLedger(String employeeId, String fromDate,
			String toDate, String appId, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select sum(SalesAmount) salesAmt,sum(paymentAmount) paymentAmt,date_format(dt, '%d/%m/%Y') as formattedDt,sum(paymentAmount)-sum(SalesAmount) diff,remarks,shift_name\n"
				+
				"from (\n" +
				"select\n" +
				"((totalizer_closing_reading-totalizer_opening_reading-(COALESCE (ttfr.test_quantity,0)*tnr.rate))) SalesAmount,0 paymentAmount,accounting_date dt,'Nozzle' remarks,shift_name\n"
				+
				"from\n" +
				"trn_nozzle_register tnr\n" +
				"inner join nozzle_master nm on nm.nozzle_id = tnr.nozzle_id\n" +
				"inner join tbl_user_mst tum on tum.user_id = tnr.attendant_id\n" +
				"inner join mst_items item on item.item_id = tnr.item_id\n" +
				"inner join tbl_user_mst tum2 on tum2.user_id = tnr.updated_by\n" +
				"inner JOIN shift_master shift on shift.shift_id = tnr.shift_id\n" +
				"left outer join trn_test_fuel_register ttfr on ttfr.nozzle_id =nm.nozzle_id and ttfr.test_date =accounting_date and test_type='S' and  ttfr.user_id=tum.user_id and ttfr.shift_id  =tnr.shift_id and ttfr.activate_flag =1\n"
				+
				"where\n" +
				"tnr.app_id = ? and tnr.activate_flag=1 \n" +
				"and accounting_date between ? and ?\n" +
				"and tum.user_id = ?\n" +
				"union all\n" +
				"select (custom_rate*qty) salesAmount,0 paymentAmount,tir.invoice_date,'Nozzle' remarks,shift_name from\n"
				+
				"trn_invoice_register tir ,\n" +
				"trn_invoice_details tid ,\n" +
				"rlt_invoice_fuel_details rifd,\n" +
				"tbl_user_mst tum,\n" +
				"shift_master sm,\n" +
				"mst_items mi\n" +
				"where\n" +
				"tir.invoice_id =tid.invoice_id\n" +
				"and rifd.invoice_id =tid.invoice_id\n" +
				"and tum.user_id =rifd.attendant_id\n" +
				"and rifd.shift_id =sm.shift_id\n" +
				"and tid.item_id =mi.item_id and (mi.item_name!='Petrol') and (mi.item_name!='Diesel')\n" +
				"and tir.app_id =? and tir.activate_flag=1\n" +
				"and tum.user_id =?\n" +
				"and tir.invoice_date between ? and ?\n" +
				"union all\n" +
				"select\n" +
				"0 SalesAmount,\n" +
				"amount  paymentAmount,\n" +
				"collection_date dt,'Nozzle' remarks,shift_name\n" +
				"from\n" +
				"trn_supervisor_collection tsc ,\n" +
				"tbl_user_mst tum,\n" +
				"shift_master sm2\n" +
				"where\n" +
				"sm2.shift_id =tsc.shift_id and sm2.activate_flag =1 and\n" +
				"tsc.attendant_id = tum.user_id\n" +
				"and tsc.collection_date between ? and ?\n" +
				"and tum.app_id=? and tsc.activate_flag=1\n" +
				"and tum.user_id =?\n" +
				"union all\n" +
				"select\n" +
				"0 salesAmount,(total_amount) paymentAmount,tir.invoice_date dt,'Nozzle',shift_name\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"inner join rlt_invoice_fuel_details rifd\n" +
				"on rifd.invoice_id =tir.invoice_id\n" +
				"inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id\n" +
				"inner join trn_payment_register tpr on tpr.ref_id=tir.invoice_id\n" +
				"left outer join shift_master sm on sm.shift_id =rifd.shift_id\n" +
				"where invoice_date between ? and ? and tpr.payment_mode !='Cash' and sm.activate_flag =1\n" +
				"and tir.app_id =? and tir.activate_flag=1 and tum.user_id =?\n" +
				"union all\n" +
				"select\n" +
				"0 salesAmount,(total_amount) paymentAmount,tir.invoice_date dt,'Nozzle' remarks,shift_name\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"inner join rlt_invoice_fuel_details rifd\n" +
				"on rifd.invoice_id =tir.invoice_id\n" +
				"inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id\n" +
				"left outer join shift_master sm on sm.shift_id =rifd.shift_id\n" +
				"where invoice_date between ? and ? and tir.payment_type='Pending' and sm.activate_flag =1\n" +
				"and tir.app_id =? and tir.activate_flag=1 and tum.user_id =?\n" +
				"union all select case when payment_type ='Debit' then amount else 0 end as salesAmount ,case when payment_type ='Credit' then amount else 0 end as paymentAmount ,payment_date,remarks,'NA' from trn_employee_payment_register tepr where payment_date between ? and ? and tepr.app_id=? and tepr.employee_id=? and tepr.activate_flag=1 )\n"
				+
				"as T group by dt,remarks,shift_name order by dt,shift_name;\n";

		parameters.add((appId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add(employeeId);

		parameters.add((appId));
		parameters.add(employeeId);
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((appId));
		parameters.add(employeeId);

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getCustomerLedgerItemReport(String customerId, String fromDate,
			String toDate, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\r\n"
				+ "	tir.invoice_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	tir.invoice_id as RefId,\r\n"
				+ "	date_format(tir.invoice_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	item_amount as Amount,\r\n"
				+ "	concat('Invoice (', remarks, ' )') as type,\r\n"
				+ "	date_format(tir.updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	'Debit' as creditDebit,\r\n"
				+ "	 round(tid.qty*tid.custom_rate,2) as debitAmount,\r\n"
				+ "	0 creditAmount,mi.item_name ,tid.qty,tid.custom_rate,mv.vehicle_number,round(tid.item_amount,2) itemAmount \r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir inner join trn_invoice_details tid on tid.invoice_id=tir.invoice_id inner join mst_items mi on mi.item_id =tid.item_id \r\n"
				+ "left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id=tir.invoice_id left outer join mst_vehicle mv on mv.vehicle_id=rifd.vehicle_id where\r\n"
				+ "	tir.customer_id = ?\r\n"
				+ "	and tir.activate_flag = 1\r\n"
				+ "	and date(invoice_date) between ? and ?\r\n"
				+ "union all\r\n"
				+ "select\r\n"
				+ "	payment_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	ref_id RefId,\r\n"
				+ "	date_format(payment_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	amount*-1 as Amount,\r\n"
				+ "	concat(payment_mode, ' ( ', tpr.remarks, ' ) ', ' ( ', tpr.payment_for, ' ) ') as type,\r\n"
				+ "	date_format(tpr.updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then 'Debit'\r\n"
				+ "		else 'Credit'\r\n"
				+ "	end as creditDebit,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then amount*-1\r\n"
				+ "		else 0\r\n"
				+ "	end as debitAmount,\r\n"
				+ "	case\r\n"
				+ "		when payment_for != 'Debit Entry' then amount\r\n"
				+ "		else 0\r\n"
				+ "	end as creditAmount,'','','','',''\r\n"
				+ "from\r\n"
				+ "	trn_payment_register tpr left outer join trn_invoice_register tir2 on tir2.invoice_id =tpr.ref_id\r\n"
				+ "where\r\n"
				+ "	tpr.customer_id = ?\r\n"
				+ "	and  tpr.activate_flag = 1\r\n"
				+ "	and date(tpr.payment_date) between ? and ?\r\n"
				+ "order by\r\n"
				+ "	orderDate,\r\n"
				+ "	upd1";

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getCustomerLedgerItemReportItemAmount(String customerId, String fromDate,
			String toDate, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\r\n"
				+ "	tir.invoice_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	tir.invoice_id as RefId,\r\n"
				+ "	date_format(tir.invoice_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	item_amount as Amount,\r\n"
				+ "	concat('Invoice (', remarks, ' )') as type,\r\n"
				+ "	date_format(tir.updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	'Debit' as creditDebit,\r\n"
				+ "	 round(tid.item_amount,2) as debitAmount,\r\n"
				+ "	0 creditAmount,mi.item_name ,tid.qty,tid.custom_rate,mv.vehicle_number,round(tid.item_amount,2) itemAmount \r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir inner join trn_invoice_details tid on tid.invoice_id=tir.invoice_id inner join mst_items mi on mi.item_id =tid.item_id \r\n"
				+ "left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id=tir.invoice_id left outer join mst_vehicle mv on mv.vehicle_id=rifd.vehicle_id where\r\n"
				+ "	tir.customer_id = ?\r\n"
				+ "	and tir.activate_flag = 1\r\n"
				+ "	and date(invoice_date) between ? and ?\r\n"
				+ "union all\r\n"
				+ "select\r\n"
				+ "	payment_date orderDate,\r\n"
				+ "	invoice_no,\r\n"
				+ "	ref_id RefId,\r\n"
				+ "	date_format(payment_date, '%d/%m/%Y') as transaction_date,\r\n"
				+ "	amount*-1 as Amount,\r\n"
				+ "	concat(payment_mode, ' ( ', tpr.remarks, ' ) ', ' ( ', tpr.payment_for, ' ) ') as type,\r\n"
				+ "	date_format(tpr.updated_date, '%d/%m/%Y %H:%i:%s') upd1,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then 'Debit'\r\n"
				+ "		else 'Credit'\r\n"
				+ "	end as creditDebit,\r\n"
				+ "	case\r\n"
				+ "		when payment_for = 'Debit Entry' then amount*-1\r\n"
				+ "		else 0\r\n"
				+ "	end as debitAmount,\r\n"
				+ "	case\r\n"
				+ "		when payment_for != 'Debit Entry' then amount\r\n"
				+ "		else 0\r\n"
				+ "	end as creditAmount,'','','','',''\r\n"
				+ "from\r\n"
				+ "	trn_payment_register tpr left outer join trn_invoice_register tir2 on tir2.invoice_id =tpr.ref_id\r\n"
				+ "where\r\n"
				+ "	tpr.customer_id = ?\r\n"
				+ "	and  tpr.activate_flag = 1\r\n"
				+ "	and date(tpr.payment_date) between ? and ?\r\n"
				+ "order by\r\n"
				+ "	orderDate,\r\n"
				+ "	upd1";

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		parameters.add((customerId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}


	public List<LinkedHashMap<String, Object>> getStockModifications(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		String query = "select *,date_format(modification.updated_date,'%d/%m/%Y') as formattedUpdatedDate from stock_modification_master modification, mst_store store ,"
				+ " tbl_user_mst user where store.store_id=modification.store_id and user.user_id=modification.updated_user and modification.app_id=store.app_id and store.app_id=? ";
		if (hm.get("storeId") != null && !hm.get("storeId").equals("") && !hm.get("storeId").equals("-1")) {
			query += " and store.store_id=?";
			parameters.add(hm.get("storeId"));
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public long addStockModification(HashMap<String, Object> outputMap, Connection conWithF) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(outputMap.get("type"));
		parameters.add(outputMap.get("userId"));
		parameters.add(outputMap.get("storeId"));
		parameters.add(outputMap.get("outerRemarks"));
		parameters.add(outputMap.get("app_id"));

		String insertQuery = "insert into stock_modification_master values (default,?,curdate(),sysdate(),?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public long addStockModificationAddRemove(HashMap<String, Object> outputMap, Connection conWithF)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(outputMap.get("stockModificationId"));
		parameters.add(outputMap.get("itemId"));
		parameters.add(outputMap.get("currentStock"));
		parameters.add(outputMap.get("qty"));
		parameters.add(outputMap.get("remarks"));
		parameters.add(outputMap.get("app_id"));
		String insertQuery = "insert into stock_modification_addremove values (default,?,?,?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getStockModificationDetailsAddRemove(String stockModificationId,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select\r\n"
				+ "	*,date_format(transaction_date,'%d/%m/%Y') as transactionDateFormatted,master.remarks remarksouter,addrem.remarks as remarksinner \r\n"
				+ "from\r\n" + "	stock_modification_master master,\r\n"
				+ "	stock_modification_addremove addrem,\r\n" + "	mst_items item\r\n" + "where\r\n"
				+ "	master.stock_modification_id = ? and master.stock_modification_id=addrem.stock_modification_id and item.item_id=addrem.item_id";
		parameters.add(stockModificationId);
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getStockModificationDetailsInventoryCounting(String stockModificationId,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select\r\n"
				+ "	*,date_format(transaction_date,'%d/%m/%Y') as transactionDateFormatted,master.remarks remarksouter \r\n"
				+ "from\r\n" + "	stock_modification_master master,\r\n"
				+ "	stock_modification_inventorycounting ivecounting,\r\n" + "	mst_items item\r\n" + "where\r\n"
				+ "	master.stock_modification_id = ? and master.stock_modification_id=ivecounting.stock_modification_id and item.item_id=ivecounting.item_id";
		parameters.add(stockModificationId);
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public long saveStockModificationInventoryCounting(HashMap<String, Object> outputMap, Connection conWithF)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(outputMap.get("stockModificationId"));
		parameters.add(outputMap.get("itemId"));
		parameters.add(outputMap.get("expectedCount"));
		parameters.add(outputMap.get("currentCount"));
		parameters.add(outputMap.get("difference"));
		parameters.add(outputMap.get("differenceAmount"));
		parameters.add(outputMap.get("app_id"));
		String insertQuery = "insert into stock_modification_inventorycounting values (default,?,?,?,?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long saveStockModificationtransferStock(HashMap<String, Object> outputMap, Connection conWithF)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(outputMap.get("stockModificationId"));
		parameters.add(outputMap.get("itemId"));
		parameters.add(outputMap.get("sourcebefore"));
		parameters.add(outputMap.get("sourceafter"));
		parameters.add(outputMap.get("qty"));
		parameters.add(outputMap.get("destinationbefore"));
		parameters.add(outputMap.get("destinationafter"));
		parameters.add(outputMap.get("sourceStore"));
		parameters.add(outputMap.get("destinationStore"));
		parameters.add(outputMap.get("app_id"));

		String insertQuery = "insert into stock_modification_transferstock values (default,?,?,?,?,?,?,?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getStockModificationDetailsStocktransfer(String stockModificationId,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select *,date_format(transaction_date,'%d/%m/%Y') as transactionDateFormatted "
				+ "from	stock_modification_master master,"
				+ "	stock_modification_transferstock transferstock,	mst_items item where "
				+ "	master.stock_modification_id = ? and master.stock_modification_id=transferstock.stock_modification_id and item.item_id=transferstock.item_id";
		parameters.add(stockModificationId);
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public LinkedHashMap<String, String> validateLoginForApp(String number, String password, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(number);
		parameters.add(getSHA256String(password));
		return getMap(parameters,
				"select  user_id,store1.store_id,store_name,usermst.app_id from tbl_user_mst usermst,mst_store store1 where mobile=? and password=? and store1.store_id=usermst.store_id ",

				con);
	}

	public LinkedHashMap<String, String> validateLoginForAppCustomer(String number, String password, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(number);
		parameters.add(getSHA256String(password));

		return getMap(parameters,
				"select  user_id,usermst.app_id from tbl_user_mst usermst where mobile=? and password=? ",

				con);
	}

	public List<LinkedHashMap<String, Object>> getBookingsForThisUser(LinkedHashMap<String, Object> hm1,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm1.get("user_id"));
		parameters.add(hm1.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select inv.*,cust.*,date_format(inv.updated_date,'%d/%m/%Y') as FormattedInvoiceDate "
						+ "from trn_booking_register inv left outer join mst_customer cust on inv.customer_id=cust.customer_id "
						+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
						+ "where inv.preffered_employee=? and inv.activate_flag=1 and inv.app_id=? order by booking_id desc limit 100 ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getRecentInvoiceForUser(LinkedHashMap<String, Object> hm1,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm1.get("user_id"));
		parameters.add(hm1.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select inv.*,cust.*,date_format(inv.updated_date,'%d/%m/%Y') as FormattedInvoiceDate "
						+ "from trn_invoice_register inv left outer join mst_customer cust on inv.customer_id=cust.customer_id "
						+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
						+ "where inv.updated_by=? and inv.activate_flag=1 and inv.app_id=? order by invoice_id desc limit 100 ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getStatisticalSalesData(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add((hm.get("app_id").toString()));
		String query = "select\r\n"
				+ "T.store_id,	store_name,	round(sum(total_amount)) total_amount,round(sum(Paid)) paid,round(sum(Pending)) pending,	round(sum(profit)) profit,round(sum(discount)) discount,	sum(gross_amount) gross_amount ,sum(returnedAmount) returnedAmount,sum(cnt) as Count\r\n"
				+ "from\r\n" + "	(	select\r\n" + "	1 cnt,tirouter.store_id,	store_name,(total_amount),\r\n"
				+ "		case\r\n"
				+ "			when payment_type = 'Paid' then (total_amount)\r\n"
				+ "			when payment_type = 'Partial' then (amount)\r\n" + "			else 0\r\n"
				+ "		end as Paid,\r\n" + "		case\r\n"
				+ "			when payment_type = 'Pending' then (total_amount)\r\n"
				+ "			when payment_type = 'Partial' then (total_amount-amount)\r\n" + "			else 0\r\n"
				+ "		end as Pending,(\r\n" + "		select\r\n"
				+ "			round(total_amount-sum(qty*average_cost))\r\n" + "		from\r\n"
				+ "			trn_invoice_register tir, trn_invoice_details tid, mst_items mi\r\n" + "		where\r\n"
				+ "			tir.activate_flag=1 and tid.invoice_id = tirouter.invoice_id\r\n"
				+ "			and mi.item_id = tid.item_id\r\n"
				+ "			and tir.invoice_id = tid.invoice_id) as profit,\r\n" + "			(\r\n"
				+ "		select\r\n" + "			round(sum(trr.qty_to_return*tid.custom_rate)) \r\n" + "		from\r\n"
				+ "			trn_invoice_register tir, trn_invoice_details tid, mst_items mi,trn_return_register trr\r\n"
				+ "		where\r\n" + "	tir.activate_flag=1 and		tid.invoice_id = tirouter.invoice_id\r\n"
				+ "			and mi.item_id = tid.item_id and trr.details_id =tid.details_id \r\n"
				+ "			and tir.invoice_id = tid.invoice_id) as returnedAmount,\r\n"
				+ "			gross_amount -total_amount as discount,gross_amount			\r\n" + "	from\r\n"
				+ "		trn_invoice_register tirouter\r\n" + "	inner join mst_store store1 on\r\n"
				+ "		tirouter.activate_flag=1 and tirouter.store_id = store1.store_id\r\n"
				+ "	left outer join trn_payment_register tpr on\r\n"
				+ "		tpr.ref_id = tirouter.invoice_id\r\n" + "		and tpr.payment_for = 'Invoice'\r\n"
				+ "	where\r\n"
				+ "		tirouter.invoice_date between ? and ? and tirouter.app_id=? StoreFilter) as T\r\n"
				+ "group by\r\n"
				+ "	store_name;";

		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tirouter.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentDataAgainstSales(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add((hm.get("app_id").toString()));
		String query = "select store_name,round(sum(Cash)) Cash,	round(sum(Paytm)) Paytm,round(sum(Amazon)) Amazon,round(sum(GooglePay)) GooglePay,round(sum(Zomato)) Zomato,round(sum(Swiggy)) Swiggy,round(sum(Card)) Card,round(sum(PhonePay)) PhonePay, "
				+ "round(sum(Cash)+sum(Paytm)+sum(Amazon)+sum(GooglePay)+sum(Zomato)+sum(Swiggy)+sum(Card)+sum(PhonePay)) as HoriTotal,T.store_id \r\n"
				+ " from (\r\n"
				+ "\r\n" + "select\r\n"
				+ "		ms.store_id ,store_name,case when payment_mode ='Cash' then amount else 0 end Cash, \r\n"
				+ "							case when payment_mode ='Paytm' then amount else 0 end Paytm, \r\n"
				+ "							case when payment_mode ='Amazon' then amount else 0 end Amazon, \r\n"
				+ "							case when payment_mode ='Google Pay' then amount else 0 end GooglePay, \r\n"
				+ "							case when payment_mode ='Phone Pay' then amount else 0 end PhonePay, \r\n"
				+ "							case when payment_mode ='Zomato' then amount else 0 end Zomato, \r\n"
				+ "							case when payment_mode ='Card' then amount else 0 end Card, \r\n"
				+ "							case when payment_mode ='Swiggy' then amount else 0 end Swiggy \r\n"
				+ "							\r\n" + "from\r\n" + "	trn_payment_register tpr\r\n"
				+ "inner join mst_store ms on\r\n" + "	ms.store_id = tpr.store_id\r\n" + "where\r\n"
				+ "	payment_for in ('invoice') and date(payment_date) between ? and ? and tpr.app_id=? and tpr.activate_flag=1 and tpr.app_id=ms.app_id StoreFilter \r\n"
				+ ") as T group by store_name";

		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tpr.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentDataAgainstCollection(HashMap<String, Object> hm,
			Connection con) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add((hm.get("app_id").toString()));
		String query = "select store_name,round(sum(Cash)) Cash,	round(sum(Paytm)) Paytm,round(sum(Amazon)) Amazon,round(sum(GooglePay)) GooglePay,round(sum(Zomato)) Zomato,round(sum(Swiggy)) Swiggy,round(sum(Card)) Card,round(sum(PhonePay)) PhonePay,"
				+ "round(sum(Cash)+sum(Paytm)+sum(Amazon)+sum(GooglePay)+sum(Zomato)+sum(Swiggy)+sum(Card)+sum(PhonePay)) as HoriTotal,T.store_id,sum(Kasar) Kasar from (\r\n"
				+ "\r\n" + "select\r\n"
				+ "		ms.store_id ,store_name,case when payment_mode ='Cash' then amount else 0 end Cash, \r\n"
				+ "							case when payment_mode ='Paytm' then amount else 0 end Paytm, \r\n"
				+ "							case when payment_mode ='Amazon' then amount else 0 end Amazon, \r\n"
				+ "							case when payment_mode ='Google Pay' then amount else 0 end GooglePay, \r\n"
				+ "							case when payment_mode ='Phone Pay' then amount else 0 end PhonePay, \r\n"

				+ "							case when payment_mode ='Zomato' then amount else 0 end Zomato, \r\n"
				+ "							case when payment_mode ='Kasar' then amount else 0 end Kasar, \r\n"
				+ "							case when payment_mode ='Card' then amount else 0 end Card, \r\n"
				+ "							case when payment_mode ='Swiggy' then amount else 0 end Swiggy \r\n"
				+ "							\r\n" + "from\r\n" + "	trn_payment_register tpr\r\n"
				+ "inner join mst_store ms on\r\n" + "	ms.store_id = tpr.store_id\r\n" + "where\r\n"
				+ "	payment_for in ('Collection') and date(payment_date) between ? and ?  and tpr.app_id=? and tpr.app_id= ms.app_id and tpr.activate_flag =1 StoreFilter \r\n"
				+ ") as T group by store_name";
		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tpr.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentData(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add(hm.get("app_id"));
		String query = "select store_name,"
				+ "round(sum(Cash)) Cash,round(sum(Paytm)) Paytm,round(sum(Amazon)) Amazon,round(sum(GooglePay)) GooglePay,"
				+ "round(sum(Zomato)) Zomato,round(sum(Swiggy)) Swiggy,round(sum(Card)) Card,round(sum(PhonePay)) PhonePay,"
				+ "round(sum(Cash)+sum(Paytm)+sum(Amazon)+sum(GooglePay)+sum(Zomato)+sum(Swiggy)+sum(Card)+sum(PhonePay)) as HoriTotal,store_id , sum(Kasar) Kasar from (\r\n"
				+ "\r\n" + "select\r\n"
				+ "		ms.store_id ,store_name,case when payment_mode ='Cash' then amount else 0 end Cash, \r\n"
				+ "							case when payment_mode ='Paytm' then amount else 0 end Paytm, \r\n"
				+ "							case when payment_mode ='Amazon' then amount else 0 end Amazon, \r\n"
				+ "							case when payment_mode ='Google Pay' then amount else 0 end GooglePay, \r\n"
				+ "							case when payment_mode ='Phone Pay' then amount else 0 end PhonePay, \r\n"

				+ "							case when payment_mode ='Zomato' then amount else 0 end Zomato, \r\n"
				+ "							case when payment_mode ='Kasar' then amount else 0 end Kasar, \r\n"
				+ "							case when payment_mode ='Card' then amount else 0 end Card, \r\n"
				+ "							case when payment_mode ='Swiggy' then amount else 0 end Swiggy \r\n"
				+ "							\r\n" + "from\r\n" + "	trn_payment_register tpr\r\n"
				+ "inner join mst_store ms on\r\n" + "	ms.store_id = tpr.store_id\r\n" + "where\r\n"
				+ "	payment_for in ('Collection','invoice') and tpr.activate_flag=1 and date(payment_date) between ? and ? and tpr.app_id=? and tpr.app_id=ms.app_id StoreFilter \r\n"
				+ ") as T group by store_name";
		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tpr.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getEmployeeWiseDetailsForDashboard(HashMap<String, Object> hm,
			Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));
		parameters.add((hm.get("app_id").toString()));
		String query = "select T.store_id,T.updated_by,store_name,name,\r\n" + "round(sum(Cash)) Cash,\r\n"
				+ "round(sum(Paytm)) Paytm,\r\n" + "round(sum(Amazon)) Amazon,\r\n"
				+ "round(sum(GooglePay)) GooglePay,\r\n"
				+ "round(sum(Zomato)) Zomato,\r\n" + "round(sum(Swiggy)) Swiggy,\r\n" + "round(sum(Card)) Card,\r\n"
				+ "round(sum(PhonePay)) PhonePay,\r\n" + "round(sum(pendingAmount)) pendingAmount,"
				+ "round(sum(Cash)+sum(Paytm)+sum(Amazon)+sum(GooglePay)+sum(Zomato)+sum(Swiggy)+sum(Card)+sum(PhonePay)+sum(pendingAmount)) as HoriTotal, T.store_id"
				+ " \r\n" + "from (\r\n"
				+ "select \r\n" + "tir.updated_by,tir.store_id,store_name,name,\r\n"
				+ "case when payment_mode ='Cash' then amount else 0 end Cash,  \r\n"
				+ "											case when payment_mode ='Paytm' then amount else 0 end Paytm,  \r\n"
				+ "											case when payment_mode ='Amazon' then amount else 0 end Amazon,  \r\n"
				+ "											case when payment_mode ='Google Pay' then amount else 0 end GooglePay, \r\n"
				+ "											case when payment_mode ='Phone Pay' then amount else 0 end PhonePay, \r\n"

				+ "											case when payment_mode ='Zomato' then amount else 0 end Zomato, \r\n"
				+ "											case when payment_mode ='Card' then amount else 0 end Card, \r\n"
				+ "											case when payment_mode ='Swiggy' then amount else 0 end Swiggy ,\r\n"
				+ "total_amount -coalesce (amount,0) pendingAmount from trn_invoice_register tir inner join tbl_user_mst tum  on tir.activate_flag=1 and tum.user_id =tir.updated_by					\r\n"
				+ "					inner join mst_store store1 on store1.store_id =tir.store_id  \r\n"
				+ "					left outer join trn_payment_register tpr  on tpr.ref_id =tir.invoice_id  and tpr.payment_for ='Invoice' and tpr.app_id=tir.app_id \r\n"
				+ "					where date(tir.invoice_date) between ? and ? and  tir.app_id=? and tir.app_id=tum.app_id and store1.app_id=tir.app_id StoreFilter \r\n"
				+ "					) as T\r\n"
				+ "					group by store_name,name";
		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tir.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getBookingDetailsForStore(HashMap<String, Object> hm,
			Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));

		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));

		parameters.add((hm.get("app_id").toString()));
		String query = "select count(1) cnt,store_name from trn_booking_register tbr,mst_store store where store.store_id=tbr.store_id and ((from_date between ? and ?) or (to_date between ? and ?)) and tbr.app_id=? StoreFilter group by store_name ";
		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tbr.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getExpenseDetailsForStore(HashMap<String, Object> hm,
			Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("toDate").toString()));

		parameters.add((hm.get("app_id").toString()));
		String query = "select sum(amount) cnt,store_name,tbr.store_id from trn_expense_register tbr,mst_store store where store.store_id=tbr.store_id and tbr.activate_flag=1 and tbr.expense_date between ? and ? and tbr.app_id=? StoreFilter group by store_name ";
		if (hm.get("store_id") != null && !hm.get("store_id").equals("")) {
			query = query.replaceAll("StoreFilter", " and tbr.store_id=? ");
			parameters.add(hm.get("store_id"));
		} else {
			query = query.replaceAll("StoreFilter", " ");
		}
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getCustomerDeliveryRoutine(Connection con, HashMap<String, Object> hm)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		String query = "select * from "
				+ " customer_delivery_routine routine,mst_customer cust,mst_items item"
				+ " where cust.customer_id=routine.customer_id and item.item_id=routine.item_id and routine.activate_flag=1 and routine.app_id=? ";

		if (hm.get("customer_id") != null && !hm.get("customer_id").equals("")) {
			parameters.add(hm.get("customer_id"));
			query += " and routine.customer_id=?";
		}

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public String deleteRoutine(long routineId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(routineId);
		insertUpdateDuablDB("UPDATE customer_delivery_routine  SET activate_flag=0 WHERE routine_id=?", parameters,
				conWithF);
		return "Routine Deleted Succesfully";
	}
	public String completeLoading(String loadingId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(loadingId);
		insertUpdateDuablDB("update trn_loading_register set is_loading_complete=1,updated_date=sysdate() where loading_id=?", parameters,
				conWithF);
		return "Loading Completed Successfully";
	}
	

	public LinkedHashMap<String, String> getRoutinepDetails(long routineId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(routineId);
		return getMap(parameters, "select * from customer_delivery_routine routine,mst_items item,mst_customer cust "
				+ " where routine.routine_id=? and item.item_id=routine.item_id and routine.customer_id=cust.customer_id",
				con);
	}

	public long addRoutine(Connection conWithF, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("hdnSelectedCustomer"));
		parameters.add(hm.get("hdnselecteditem"));
		parameters.add(hm.get("txtcustomrate"));
		parameters.add(hm.get("txtitemqty"));
		parameters.add(hm.get("deliverypreference"));

		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		return insertUpdateDuablDB("insert into customer_delivery_routine values (default,?,?,?,?,?,1,sysdate(),?,?)",
				parameters, conWithF);
	}

	public long updateRoutine(Connection conWithF, HashMap<String, Object> hm) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("hdnSelectedCustomer"));
		parameters.add(hm.get("hdnselecteditem"));
		parameters.add(hm.get("txtcustomrate"));
		parameters.add(hm.get("txtitemqty"));
		parameters.add(hm.get("deliverypreference"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("hdnroutineid"));

		return insertUpdateDuablDB("update customer_delivery_routine set customer_id=?,item_id=?,"
				+ "custom_rate=?,qty=?,occurance=?,updated_by=?,app_id=?,updated_date=sysdate() where routine_id=?",
				parameters, conWithF);

	}

	public List<LinkedHashMap<String, Object>> getRoutineDetailsForThisCustomer(String customerId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(Long.valueOf(customerId));
		String query = "select\r\n" +
				"	deliveryroutine.*,\r\n" +
				"	cust.*,cat.*,\r\n" +
				"	item.*,\r\n" +
				"	case\r\n" +
				"		when concat(attachment_id, file_name) is null then 'dummyImage.jpg'\r\n" +
				"		else concat(attachment_id, file_name)\r\n" +
				"	end as ImagePath\r\n" +
				"from\r\n" +
				"	customer_delivery_routine deliveryroutine inner join \r\n" +
				"	mst_items item on deliveryroutine.item_id =item.item_id inner join \r\n" +
				"	mst_customer cust on cust.customer_id =deliveryroutine .customer_id"
				+ " inner join mst_category cat on cat.category_id=item.parent_category_id \r\n" +
				"left outer join tbl_attachment_mst tam on\r\n" +
				"	item.item_id = tam.file_id\r\n" +
				"	and tam.type = 'Image'\r\n" +
				"where\r\n" +
				"	deliveryroutine.customer_id =? \r\n" +
				"	and deliveryroutine.activate_flag = 1\r\n" +
				"	and item.item_id = deliveryroutine.item_id\r\n" +
				"	and cust.customer_id = deliveryroutine.customer_id";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getExpenseRegister(HashMap<String, Object> outputMap, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(outputMap.get("app_id"));
		parameters.add(getDateASYYYYMMDD(outputMap.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(outputMap.get("toDate").toString()));

		String query = "select *,date_format(expense_date,'%d/%m/%Y') as FormattedExpenseDate from trn_expense_register where app_id=? and expense_date between ? and ? and activate_flag=1 ";

		if (outputMap.get("store_id") != null) {
			parameters.add(outputMap.get("store_id"));
			query += " and store_id=?";
		}

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public String deleteExpense(long expenseId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(expenseId);
		insertUpdateDuablDB(
				"UPDATE trn_expense_register SET activate_flag=0,updated_by=?,updated_date=sysdate() WHERE expense_id=?",
				parameters, conWithF);
		return "Expense Deleted Succesfully";
	}

	public String deleteInvoice(long invoiceId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		
		parameters.add(userId);
		parameters.add(invoiceId);
		insertUpdate("UPDATE trn_invoice_register SET activate_flag=0,updated_by=? WHERE invoice_id=?", parameters,
				conWithF);
		parameters.clear();

		
		parameters.add(invoiceId);
		insertUpdate("update \r\n" + //
						"\ttrn_stock_direct_details tsdd,\r\n" + //
						"\ttrn_invoice_register tir,\r\n" + //
						"\ttrn_invoice_details tid\r\n" + //
						"\tset tsdd.activate_flag=0\t \r\n" + //
						"where\r\n" + //
						"\ttir.invoice_id = ? and tir.invoice_id =tid.invoice_id and tid.details_id =tsdd.details_id ;", parameters,
				conWithF);
		parameters.clear();

		return userId;
	}

	public String deletePaymentAgainstInvoice(long invoiceId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(invoiceId);
		insertUpdate(
				"UPDATE trn_payment_register SET activate_flag=0,updated_by=?,updated_date=sysdate() WHERE ref_id=? ",
				parameters, conWithF);
		return "Deleted Payment";

	}

	public String deleteReturnsAgainstInvoice(long invoiceId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(invoiceId);
		insertUpdate(
				"delete return1 from trn_return_register return1 ,trn_invoice_register register ,trn_invoice_details  dtls  \r\n"
						+ " where register.invoice_id =? and register.invoice_id =dtls.invoice_id  and \r\n"
						+ "return1.details_id = dtls.details_id",
				parameters, conWithF);
		return "Deleted Payment";

	}

	public LinkedHashMap<String, String> getExpenseDetails(long expenseId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(expenseId);
		return getMap(parameters,
				"select *,date_format(expense_date,'%d/%m/%Y') as FormattedExpenseDate from trn_expense_register where expense_id=?",
				con);
	}

	public long addExpense(Connection conWithF, String groupName) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(groupName);
		return insertUpdateDuablDB("insert into trn_expense_register values (default,?,?,?,sysdate(),?,1,?)",
				parameters, conWithF);
	}

	public long addExpense(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtdate").toString()));
		parameters.add(hm.get("expense_name"));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("qty"));
		parameters.add(hm.get("store_id"));

		return insertUpdateDuablDB("insert into trn_expense_register values (default,?,?,?,sysdate(),?,1,?,?,?)",
				parameters, con);
	}

	public long updateExpense(Connection con, HashMap<String, Object> hm) throws ParseException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtdate").toString()));
		parameters.add(hm.get("expense_name"));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("qty"));

		parameters.add(hm.get("hdnExpenseId"));

		return insertUpdateDuablDB(
				"update trn_expense_register set expense_date=?,expense_name=?,amount=?,updated_by=?,app_id=?,qty=? where expense_id=?",
				parameters, con);

	}

	public List<LinkedHashMap<String, Object>> getDistinctExpenseList(Connection con, String appId)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"select distinct(expense_name) expense_name from trn_expense_register where app_id=? ", con);
	}

	public List<LinkedHashMap<String, Object>> getUniqueModelNoForThisApp(Connection con, String appId)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"select distinct(model_no) as ModelNo from trn_invoice_register where app_id=?", con);
	}

	public long getTentativeSequenceNo(String appId, String tableName, Connection con) throws SQLException {
		long generatedPK = 0;
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(tableName);
		HashMap<String, String> hm = getMap(parameters,
				"select current_seq_no+1 as  current_seq_no from seq_master where app_id=? and sequence_name=?", con);
		if (hm.get("current_seq_no") == null) {
			parameters.clear();
			parameters.add(tableName);
			parameters.add(appId);
			insertUpdateDuablDB("insert into seq_master values (default,?,0,?)", parameters, con);
			generatedPK = 1;
		} else {
			generatedPK = Long.valueOf(hm.get("current_seq_no"));
		}
		return generatedPK;
	}

	public List<String> getDistinctCityNames(String appId, Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfString(parameters, "select distinct(City) from mst_customer where app_id=?", con);
	}

	public String saveTableConfig(int noOfTables, String storeId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(storeId);
		insertUpdate("delete from mst_tables where store_id=?", parameters, con);
		for (int x = 1; x <= noOfTables; x++) {
			parameters.clear();
			parameters.add(storeId);
			parameters.add(x);
			insertUpdate("insert into mst_tables values (default,?,?,null)", parameters, con);
		}

		return "Tables Added Succesfully";
	}

	public long saveNewApp(String appname, String txtvalidtill, String txtstorename, String txtusername, String appType,
			Connection con) throws SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(appname);
		parameters.add(getDateASYYYYMMDD(txtvalidtill));
		parameters.add(appType);
		long appId = insertUpdateDuablDB(
				"insert into mst_app  (app_id,app_name,valid_till,app_type) values (default,?,?,?)", parameters, con);

		parameters = new ArrayList<>();
		parameters.add(txtstorename);
		parameters.add(appId);
		long storeId = insertUpdateDuablDB(
				"insert into mst_store (store_id,store_name,app_id,activate_flag) values (default,?,?,1)", parameters,
				con);

		parameters = new ArrayList<>();
		parameters.add(txtusername);
		parameters.add(getSHA256String("123"));
		parameters.add(storeId);
		parameters.add(appId);
		long userId = insertUpdateDuablDB(
				"insert into tbl_user_mst (user_id,username,password,store_id,app_id) values (default,?,?,?,?)",
				parameters, con);

		parameters = new ArrayList<>();
		parameters.add(userId);
		insertUpdateDuablDB("insert into user_configurations (user_id) values (?)", parameters, con);

		parameters = new ArrayList<>();
		parameters.add(userId);
		insertUpdateDuablDB(
				"INSERT INTO acl_user_role_rlt (rlt_pk, user_id, role_id, activate_flag, created_date, updated_date) VALUES(default, ?, 1, 1, sysdate(), NULL);",
				parameters, con);

		return userId;
	}

	public List<LinkedHashMap<String, Object>> getOrderDetailsForTable(long tableId, Connection conWithF)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(tableId);
		return getListOfLinkedHashHashMap(parameters,
				"select table1.order_id orderId,table1.table_no  tableNo,tor.*,tod.*,mi.* from\r\n"
						+ "mst_tables table1 left outer join  trn_order_register tor  on tor.order_id=table1.order_id \r\n"
						+ "inner join trn_order_details tod   on tor.order_id =tod.order_id \r\n"
						+ "inner join mst_items mi  on mi.item_id =tod.item_id \r\n"
						+ "where table1.table_id=?",
				conWithF);
	}

	public long saveOrder(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("table_id"));
		return insertUpdateDuablDB("insert into trn_order_register values (default,?,sysdate(),null)", parameters, con);
	}

	public String saveOrderDetails(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		List<HashMap<String, Object>> itemListRequired = (List<HashMap<String, Object>>) hm.get("itemDetails");
		for (HashMap<String, Object> itemdetail : itemListRequired) {
			parameters.clear();
			parameters.add(hm.get("order_id"));
			parameters.add(itemdetail.get("item_id"));
			parameters.add(itemdetail.get("qty"));
			parameters.add("O");
			parameters.add(itemdetail.get("remarks"));
			parameters.add(hm.get("runningFlag"));

			long orderDetailsId = insertUpdateDuablDB(
					"insert into trn_order_details values (default,?,?,?,?,sysdate(),null,null,?,?)", parameters, con);

		}
		return "Save Succesfully";

	}

	public List<LinkedHashMap<String, Object>> getPendingOrders(HashMap<String, Object> hm, Connection conWithF)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("store_id"));

		String query = "select\r\n"
				+ "	*,\r\n"
				+ "	case when running_flag='Y' then 1 else 0 end priority\r\n"
				+ "from\r\n"
				+ "	mst_store ms ,\r\n"
				+ "	mst_tables mt ,\r\n"
				+ "	trn_order_register tor ,\r\n"
				+ "	trn_order_details tod,\r\n"
				+ "	mst_items item\r\n"
				+ "where\r\n"
				+ "	ms.app_id = ? \r\n"
				+ "	and ms.store_id = ? \r\n"
				+ "	and ms.store_id = mt.store_id\r\n"
				+ "	and tor.order_id = mt.order_id\r\n"
				+ "	and tod.order_id = tor.order_id\r\n"
				+ "	and item.item_id = tod.item_id\r\n"
				+ "	and tod.status = 'O'\r\n";

		if (hm.get("table_id") != null && !hm.get("table_id").toString().equals("-1")
				&& !hm.get("table_id").toString().equals("")) {
			parameters.add(hm.get("table_id"));
			query += " and mt.table_id=?";
		}

		query += " order by "
				+ " priority desc,ordered_time asc";

		return getListOfLinkedHashHashMap(parameters, query, conWithF);
	}

	public String markAsServed(String[] itemId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		String questionMarks = "";
		for (String s : itemId) {
			parameters.add(s);
			questionMarks += "?,";
		}
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);

		insertUpdateDuablDB(
				"UPDATE trn_order_details  SET status='S',served_time=sysdate() WHERE order_details_id in ("
						+ questionMarks + ")",
				parameters, conWithF);
		return "Served Succesfully";
	}

	public String markAllAsServed(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("table_id"));
		insertUpdateDuablDB(
				"update \r\n"
						+ "mst_tables mt,\r\n"
						+ "trn_order_details tod\r\n"
						+ "set status='S',served_time=sysdate()\r\n"
						+ "where mt.table_id =? and tod.order_id =mt.order_id  and status='O'",
				parameters, conWithF);
		return "Served Succesfully";
	}

	public String cancelOrderDetail(long orderDetailId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(orderDetailId);
		insertUpdateDuablDB(
				"UPDATE trn_order_details  SET status='C',cancelled_time=sysdate() WHERE order_details_id=?",
				parameters, con);
		return "Cancelled Succesfully";
	}

	public void updateTableWithOrderId(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("order_id"));
		parameters.add(hm.get("table_id"));

		insertUpdateDuablDB(
				"update mst_tables set order_id=? where table_id=?",
				parameters, con);

	}

	public void removeOrderFromTable(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("table_id"));

		insertUpdateDuablDB(
				"update mst_tables set order_id=null where table_id=?",
				parameters, con);

	}

	public List<LinkedHashMap<String, Object>> getCompositeItemDetails(long itemId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		return getListOfLinkedHashHashMap(parameters,
				"select * from rlt_composite_item_mpg rcim ,mst_items item where item.item_id=rcim.child_item_id and rcim.item_id =? ",
				con);
	}

	public long saveCompositeItem(HashMap<String, Object> itemDetails, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemDetails.get("parentItemId"));
		parameters.add(itemDetails.get("item_id"));
		parameters.add(itemDetails.get("qty"));
		String insertQuery = "insert into rlt_composite_item_mpg values (default,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public long deleteCompositeItem(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("parentItemId"));
		String insertQuery = "delete from rlt_composite_item_mpg where item_id=?";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public long saveBooking(HashMap<String, Object> itemDetails, Connection con)
			throws SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemDetails.get("customerId"));
		parameters.add(getDateASYYYYMMDDHHMM(itemDetails.get("fromDateTime").toString()));
		parameters.add(getDateASYYYYMMDDHHMM(itemDetails.get("toDateTime").toString()));
		parameters.add(itemDetails.get("prefferedEmployee"));
		parameters.add(itemDetails.get("app_id"));
		parameters.add(itemDetails.get("user_id"));
		parameters.add(itemDetails.get("remarks"));
		parameters.add(itemDetails.get("modelname"));
		parameters.add(itemDetails.get("uniqueno"));
		parameters.add(itemDetails.get("store_id"));

		String insertQuery = "insert into trn_booking_register values (default,?,?,?,?,?,?,sysdate(),1,'O',?,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public long saveBookingItems(HashMap<String, Object> itemDetails, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemDetails.get("bookingId"));
		parameters.add(itemDetails.get("item_id"));
		parameters.add(itemDetails.get("qty"));
		String insertQuery = "insert into booking_item_mpg values (default,?,?,?)";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public String markBookingAsServed(long bookingId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(bookingId);
		insertUpdateDuablDB(
				"UPDATE trn_booking_register  SET status='S',updated_date=sysdate() WHERE booking_id=?",
				parameters, conWithF);
		return "Served Succesfully";
	}

	public boolean checkIfBookingAlreadyExist(HashMap<String, Object> hm, Connection con)
			throws SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDDHHMM(hm.get("fromDateTime").toString()));
		parameters.add(getDateASYYYYMMDDHHMM(hm.get("fromDateTime").toString()));
		parameters.add(getDateASYYYYMMDDHHMM(hm.get("toDateTime").toString()));
		parameters.add(getDateASYYYYMMDDHHMM(hm.get("toDateTime").toString()));
		parameters.add(hm.get("prefferedEmployee"));
		return !getMap(parameters, "select\r\n"
				+ "	count(1) as existingBookings\r\n"
				+ "from\r\n"
				+ "	trn_booking_register tbr\r\n"
				+ "where\r\n"
				+ "	(\r\n"
				+ "		((? between from_date and to_date) and (to_date !=?))\r\n"
				+ "		or\r\n"
				+ "		((? between from_date and to_date)  and from_date !=?)\r\n"
				+ "	) and preffered_employee =? and tbr.activate_flag =1", con).get("existingBookings").equals("0");

	}

	public List<LinkedHashMap<String, Object>> getCompositeItemChildsAndQuantity(long itemId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemId);
		return getListOfLinkedHashHashMap(parameters, "select * from rlt_composite_item_mpg where item_id=?", con);
	}

	public List<LinkedHashMap<String, Object>> getAuditList(Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getListOfLinkedHashHashMap(parameters, "select * from customizedpos.frm_audit_trail  \r\n"
				+ "union all\r\n"
				+ "select * from student_attendance.frm_audit_trail  \r\n"
				+ "order by accessed_time desc \r\n"
				+ "limit 100", con);
	}

	public List<LinkedHashMap<String, Object>> getAuditListByUserAndSchema(String username, String schemaName,
			Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(username);
		String query = "select * from schemaName.frm_audit_trail where user_name=? order by accessed_time desc limit 100 ";
		query = query.replaceAll("schemaName", schemaName);
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getLastestUserHits(Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		return getListOfLinkedHashHashMap(parameters,
				"select max(accessed_time) as T1,user_name,'customizedpos' appName from customizedpos.frm_audit_trail fat where user_name !='' group by user_name having user_name is not null \r\n"
						+ //
						"union all \r\n" + //
						"select max(accessed_time) as T1,user_name,'agserp' from  agserp.frm_audit_trail fat where user_name !='' group by user_name having user_name is not null \r\n"
						+ //
						"union all \r\n" + //
						"select max(accessed_time) as T1,user_name,'ssegpl' from  ssegpl.frm_audit_trail fat where user_name !='' group by user_name having user_name is not null\r\n"
						+ //
						"union all \r\n" + //
						"select max(accessed_time) as T1,user_name,'society_maintenance' from  society_maintenance.frm_audit_trail fat where user_name !='' group by user_name having user_name is not null\r\n"
						+ //
						"union all \r\n" + //
						"select max(accessed_time) as T1,user_name,'skpsecuritygate' from  skpsecuritygate.frm_audit_trail fat where user_name !='' group by user_name having user_name is not null\r\n"
						+ //
						"order by T1 desc\r\n" + //
						"limit 30;",
				con);
	}

	public List<LinkedHashMap<String, Object>> getSliderImages(long appId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"SELECT attachment_id,concat(attachment_id,file_name) as file_name FROM tbl_attachment_mst WHERE activate_flag=1 AND TYPE='Slider' and file_id=?",
				con);
	}

	public List<LinkedHashMap<String, Object>> getItemNamesForSearchAutocomplete(long appId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"SELECT item_id,item_name,category_name FROM mst_items item, mst_category cat WHERE item.parent_category_id=cat.category_id and  item.activate_flag=1 and item.app_id=?",
				con);
	}

	public List<LinkedHashMap<String, Object>> CategoryNameWithImage(long appId, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, "select \r\n"
				+ "		category_id,category_name,\r\n"
				+ "		concat(tam.attachment_id,file_name),\r\n"
				+ "		case \r\n"
				+ "		when \r\n"
				+ "			concat(tam.attachment_id,file_name) is null 	 \r\n"
				+ "				then \r\n"
				+ "				     case when (select concat(tam.attachment_id,file_name) from mst_items as mi,tbl_attachment_mst tam where parent_category_id=mc.category_id and tam.file_id=mi.item_id and tam.type='Image' order by rand() limit 1) is null\r\n"
				+ "							then 'dummyImage.jpg' 				     	\r\n"
				+ "					else (select concat(tam.attachment_id,file_name) from mst_items as mi,tbl_attachment_mst tam where parent_category_id=mc.category_id and tam.file_id=mi.item_id and tam.type='Image' order by rand() limit 1) end 			\r\n"
				+ "				else concat(tam.attachment_id,file_name) end file_name \r\n"
				+ "	from \r\n"
				+ "		mst_category as mc \r\n"
				+ "		left outer join tbl_attachment_mst tam on mc.category_id=tam.file_id and tam.type='category'\r\n"
				+ "	where\r\n"
				+ "		mc.activate_Flag=1 and  app_id =?", con);

	}

	public List<LinkedHashMap<String, Object>> getProductsByCategoryId(long appId, String categoryId, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(categoryId);
		return getListOfLinkedHashHashMap(parameters, "SELECT  " +
				"item.item_id,item.item_name,item.`price`,cat.`category_name`, " +
				" 	(select case\r\n"
				+ "		when concat(attachment_id, file_name) is null then 'dummyImage.jpg'\r\n"
				+ "		else concat(attachment_id, file_name)\r\n"
				+ "	end as path from tbl_attachment_mst tam2  where tam2.file_id =item.item_id  and tam2.activate_flag =1 and tam2.`type` ='Image' limit 1) path "
				+
				"FROM  " +
				"mst_category cat, mst_items item   " +
				"WHERE cat.`category_id`=item.`parent_category_id` " +
				"AND item.`activate_flag`=1 AND cat.`activate_flag`=1  and item.app_id=? and cat.app_id=item.app_id  " +
				"AND cat.`category_id`=?", con);
	}

	public List<LinkedHashMap<String, Object>> getProductsForSearch(long appId, String searchString, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);

		parameters.add("%" + searchString + "%");
		parameters.add("%" + searchString + "%");
		return getListOfLinkedHashHashMap(parameters, "SELECT  " +
				"item.item_id,item.item_name,item.`price`,cat.`category_name`, " +
				" case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as path "
				+
				"FROM  " +
				"mst_category cat, mst_items item   " +
				" left outer join tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'   " +
				"WHERE cat.`category_id`=item.`parent_category_id` " +
				"AND item.`activate_flag`=1 AND cat.`activate_flag`=1  and item.app_id=? and cat.app_id=item.app_id  " +
				"AND (cat.category_name like ? or item.item_name like ?)", con);
	}

	public List<LinkedHashMap<String, Object>> getItemsOfThisCategory(long appId, long categoryId, long itemId,
			int count, Connection con) throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(categoryId);
		parameters.add(itemId);
		parameters.add(count);
		return getListOfLinkedHashHashMap(parameters, "SELECT  " +
				"item.item_id,item.item_name,item.`price`,cat.`category_name`, " +
				" case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as path "
				+
				"FROM  " +
				"mst_category cat, mst_items item   " +
				" left outer join tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'   " +
				"WHERE cat.`category_id`=item.`parent_category_id` " +
				"AND item.`activate_flag`=1 AND cat.`activate_flag`=1  and item.app_id=? and cat.app_id=item.app_id  " +
				"AND cat.`category_id`=?", con);

	}

	public List<LinkedHashMap<String, Object>> getItemsBySearchString(long appId, String SearchString, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(SearchString);
		parameters.add(SearchString);
		parameters.add(appId);

		return getListOfLinkedHashHashMap(parameters, "select\r\n"
				+ "	item.item_id,\r\n"
				+ "	item.item_name,\r\n"
				+ "	item.price,\r\n"
				+ "	cat.category_name,\r\n"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as path "
				+ "	\r\n"
				+ "from\r\n"
				+ "	mst_category cat, mst_items item left outer join  tbl_attachment_mst attachment on 	attachment.file_id = item.item_id	 and attachment.`activate_flag` = 1\r\n"
				+ "where\r\n"
				+ "	(item.item_name like ? or cat.category_name like ?) 	 \r\n"
				+ "	and item.`activate_flag` = 1\r\n"
				+ "	and item.`parent_category_id` = cat.`category_id`	\r\n"
				+ "	and cat.activate_flag = 1\r\n"
				+ "	and item.app_id = ?\r\n"
				+ "	and cat.app_id = item.app_id", con);

	}

	public LinkedHashMap<String, String> getAboutUsDetails(long appId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getMap(parameters,
				"select apm.*,concat(attach.attachment_id,attach.file_name) as fileName from mst_app apm left outer join tbl_attachment_mst attach on attach.file_id=apm.app_id  and attach.type='aboutus' where apm.app_id=? ",
				con);
	}

	public LinkedHashMap<String, String> getLogoDetails(long appId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getMap(parameters,
				"select concat(attach.attachment_id,attach.file_name) as fileName from tbl_attachment_mst attach where type='logo' and file_id=?",
				con);
	}

	public HashMap<String, Object> getCashbackForDeliveryTypes(Connection con) throws SQLException {
		HashMap<String, Object> hm = new HashMap<>();

		ResultSet rs = null;
		try {

			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT TYPE AS t1,PERCENTAGE AS p1 FROM mst_cashback ");
			while (rs.next()) {

				hm.put(rs.getString(1), rs.getInt(2));
			}
		} catch (Exception e) {
			writeErrorToDB(e);
		} finally {

			if (!rs.isClosed()) {
				rs.close();
			}
		}

		return hm;
	}

	public Long getWalletAmountForThisNumber(String number, Connection con) throws SQLException {

		PreparedStatement stmnt = null;
		ResultSet rs = null;
		Long amount = 0L;

		try {

			stmnt = con.prepareStatement("SELECT SUM(cashback_amount) FROM " +
					"trn_order_register_frommobileapp order1, " +
					"trn_cashback_register cashback " +
					"WHERE order1.number=? " +
					"AND cashback.orderId=order1.order_id");
			stmnt.setString(1, number);

			rs = stmnt.executeQuery();

			while (rs.next()) {
				amount = rs.getLong(1);
			}

		} catch (Exception e) {
			writeErrorToDB(e);
		} finally {

			if (!rs.isClosed()) {
				rs.close();
			}
			if (!stmnt.isClosed()) {
				stmnt.close();
			}

		}

		return amount;
	}

	public List<LinkedHashMap<String, Object>> getMobileAppOrders(String appId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, "select * from trn_order_register_frommobileapp where app_id=? ",
				con);
	}

	public LinkedHashMap<String, String> getInvoiceFormatName(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("user_id"));

		return getMap(parameters,
				"select * from user_configurations tum , invoice_formats format where tum.user_id=? and tum.invoice_format=format.format_id ",
				con);

	}

	public List<LinkedHashMap<String, Object>> getItemMasterForGenerateInvoice(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\r\n"
				+ "	item.*,\r\n"
				+ "	cat.*,\r\n"
				+ "	case\r\n"
				+ "		when concat(attachment_id, file_name) is null then 'dummyImage.jpg'\r\n"
				+ "		else concat(attachment_id, file_name)\r\n"
				+ "	end as ImagePath,tpir.invoice_no PurchaseInvoiceNo,tpid.details_id purchase_details_id,store_id,tpid.qty - sum(COALESCE (tid.qty ,0)) as QtyAvailable\r\n"
				+ "from\r\n"
				+ "	mst_items item inner join mst_category cat on cat.category_id = item.parent_category_id\r\n"
				+ "left outer join tbl_attachment_mst tam on tam.file_id = item.item_id and tam.type = 'Image'\r\n"
				+ "left outer join trn_purchase_invoice_details tpid  on tpid.item_id =item.item_id\r\n"
				+ "left outer join trn_purchase_invoice_register tpir  on tpid.invoice_id =tpir.invoice_id  and tpir.store_id =?\r\n"
				+ "left outer join trn_invoice_details tid on tpid.details_id =tid.purchase_details_id \r\n"
				+ "where\r\n"
				+ "	item.activate_flag = 1\r\n"
				+ "	and item.app_id = ?\r\n"
				+ "	and cat.app_id = item.app_id\r\n"
				+ "	group by item.item_id,tpid.details_id"
				+ " --  having QtyAvailable>0 \r\n"
				+ "order by\r\n"
				+ "	item.order_no;";

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getListOfItemsForSnacksProduction(String invoiceId, String appId,String packaging_type,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		parameters.add(appId);
		parameters.add(packaging_type);
		

		String query = "select\n" +
				"*\n" +
				"from\n" +
				"mst_items mi left outer join trn_invoice_details tid on tid.item_id =mi.item_id   and tid.invoice_id=?\n"
				+
				"where mi.app_id =? and mi.activate_flag =1 and mi.packaging_type=? order by order_no\n";

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getItemMasterForGenerateInvoiceType1(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select item.item_name,item.price,stock.qty_available,item.item_id,product_code,item.sgst,item.cgst,cat.*,"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath, stock.qty_available "
				+ "from mst_items item inner join mst_category cat on cat.category_id=item.parent_category_id left outer join "
				+ " tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' left outer join stock_status stock on  stock.item_id=item.item_id and stock.app_id=item.app_id and stock.store_id=? "
				+ " where item.activate_flag=1 and item.app_id=? and cat.app_id=item.app_id ";

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		query += " group by item.item_id";
		query += " order by item_name";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getItemMasterForGenerateInvoiceForThisStore(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select item.*,cat.*,"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath, stock.qty_available "
				+ "from mst_items item inner join store_item_mpg sim on sim.store_id=? and sim.item_id=item.item_id inner join mst_category cat on cat.category_id=item.parent_category_id left outer join "
				+ " tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' left outer join stock_status stock on  stock.item_id=item.item_id and stock.app_id=item.app_id and stock.store_id=? "
				+ " where item.activate_flag=1 and item.app_id=? and cat.app_id=item.app_id ";

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		query += " group by item.item_id";
		query += " order by item_name";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public boolean checkifUserAlreadyExist(long number, long appId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(number);
		int count = Integer.parseInt(
				getMap(parameters, "select count(1) as cnt from mst_customer where app_id=? and mobile_number=?", con)
						.get("cnt").toString());
		return count >= 1;
	}


	public boolean checkIfUsernameAlreadyExist(String userName, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userName);		
		int count = Integer.parseInt(
				getMap(parameters, "select count(1) as cnt from tbl_user_mst where username=?", con)
						.get("cnt").toString());
		return count >= 1;
	}

	

	public boolean hasMultipleAttemps(long number, long appId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(number);
		int count = Integer.parseInt(getMap(parameters,
				"select count(1) as cnt from trn_otp_register where app_id=? and mobile_number=?  and date(time_start)=curdate()",
				con).get("cnt").toString());
		return count >= 3;
	}

	public String validateOTP(long appId, long number, int otp, Connection con)
			throws ClassNotFoundException, SQLException {
		String message = "OTP validation Failed";

		PreparedStatement ps = con.prepareStatement(
				"SELECT count(1) FROM trn_otp_register WHERE mobile_number = ? AND otp = ? AND activate_flag = 1 and app_id=?");
		ps.setLong(1, number);
		ps.setInt(2, otp);
		ps.setLong(3, appId);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			if (rs.getString(1).equals("1")) {
				message = "OTP validation Successfull";
			}
		}

		return message;
	}

	public String resetPassword(long number, String password, long appId, Connection con) throws SQLException {

		PreparedStatement stmnt = null;
		String message = "Something went wrong";
		int rs;
		try {

			stmnt = con.prepareStatement(
					"update tbl_customer_mst set password=?,updated_date=sysdate() where mobile_number=? and app_id=?");
			stmnt.setString(1, password);
			stmnt.setLong(2, number);
			stmnt.setLong(3, appId);

			rs = stmnt.executeUpdate();
			message = "Password Updated";

		} catch (Exception e) {
			writeErrorToDB(e);
		} finally {

			if (!stmnt.isClosed()) {
				stmnt.close();
			}

		}

		return message;
	}

	public String createNewCustomer(long number, String password, long appId, Connection con) throws SQLException {

		PreparedStatement ps = null;
		int rs;
		String message = "Sign Up failed";

		try {

			if (checkifUserAlreadyExist(number, appId, con)) {
				message = "User Already Exist";
				return message;
			}

			ps = con.prepareStatement("INSERT INTO tbl_customer_mst (Mobile_number, password, "
					+ "created_date, activate_flag, updated_date,app_id) VALUES(?,?,sysdate(),1,null,?)");
			ps.setLong(1, number);
			ps.setString(2, password);
			ps.setLong(3, appId);

			rs = ps.executeUpdate();

			if (rs > 0) {
				message = "Succesfully Created Account";
			}
		} catch (Exception e) {
			writeErrorToDB(e);
		} finally {

			if (ps != null && !ps.isClosed()) {
				ps.close();
			}

		}
		return message;
	}

	public LinkedHashMap<String, String> getuserDetailsByMobileNo(String mobileNo, String appId, Connection con)
			throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(mobileNo);
		parameters.add(appId);
		return getMap(parameters, "select * from tbl_user_mst where mobile=? and app_id=?", con);

	}

	public LinkedHashMap<String, String> getuserDetailsByEmailId(String emailId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(emailId);
		return getMap(parameters, "select * from tbl_user_mst where email=?", con);

	}

	public List<HashMap<String, Object>> showOrderHistory(long number, Connection con)
			throws ClassNotFoundException, SQLException {
		HashMap<String, Object> hm = null;
		List<HashMap<String, Object>> lstHm = new ArrayList<HashMap<String, Object>>();

		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {

			stmnt = con.prepareStatement("select " +
					" order_id, " +
					" date_format( tor.created_date, '%d/%m/%y' ) as dt1, " +
					" case when curr_status = 1 then 'ordered'" +
					"	  when curr_status = 2 then 'accepted'" +
					"    when curr_status =- 1 then 'rejected'" +
					"    when curr_status = 3 then 'Delivered'" +
					"	end status1," +
					" amount," +
					" case when curr_status = 1 then '0'	when curr_status = 3 then cashback.cashback_amount" +
					"	when curr_status =- 1 then '0'" +
					"	when curr_status =2 then '0'" +
					" end status1, " +
					" tor.previouscashbackamountused" +
					" from  " +
					" trn_order_register_frommobileapp tor left outer join trn_cashback_register cashback on cashback.orderId=tor.order_id and cashback.orderType!='Redeemed'  "
					+
					" where tor.`number`=?  " +
					" order by tor.created_date desc");

			stmnt.setLong(1, number);

			rs = stmnt.executeQuery();

			while (rs.next()) {
				hm = new HashMap<>();

				hm.put("id", rs.getString(1));
				hm.put("date", rs.getString(2));
				hm.put("status", rs.getString(3));
				hm.put("amount", rs.getString(4));
				hm.put("cashbackreceived", rs.getString(5));
				hm.put("previouscashbackamountused", rs.getString(6));

				lstHm.add(hm);
			}

		} catch (Exception e) {
			writeErrorToDB(e);
		} finally {

			if (!rs.isClosed()) {
				rs.close();
			}
			if (!stmnt.isClosed()) {
				stmnt.close();
			}

		}

		return lstHm;
	}

	public List<LinkedHashMap<String, Object>> getRelatedItems(long appId, String categoryId, Connection con, int count,
			long itemId) throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(categoryId);
		parameters.add(itemId);
		parameters.add(count);
		return getListOfLinkedHashHashMap(parameters, "SELECT  " +
				"item.item_id,item.item_name,item.`price`,cat.`category_name`, " +
				" case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as path "
				+
				"FROM  " +
				"mst_category cat, mst_items item   " +
				" left outer join tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'   " +
				"WHERE cat.`category_id`=item.`parent_category_id` " +
				"AND item.`activate_flag`=1 AND cat.`activate_flag`=1  and item.app_id=? and cat.app_id=item.app_id  " +
				"AND cat.`category_id`=? and item.item_id!=? order by rand() limit ? ", con);

	}

	public List<LinkedHashMap<String, Object>> getPopularItems(String appId, Connection con)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);

		return getListOfLinkedHashHashMap(parameters, "SELECT  " +
				"item.item_id,item.item_name,item.`price`,cat.`category_name`, " +
				" case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as path "
				+
				"FROM  " +
				"mst_category cat, mst_items item   " +
				" left outer join tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image'   " +
				"WHERE cat.`category_id`=item.`parent_category_id` " +
				"AND item.`activate_flag`=1 AND cat.`activate_flag`=1  and item.app_id=? and cat.app_id=item.app_id order by rand() limit 20 ",
				con);
	}

	public LinkedHashMap<String, String> getItemdetailsById(long itemDetailsId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(itemDetailsId);
		return getMap(parameters, "select * from trn_invoice_details where details_id=?", con);

	}

	public HashMap<String, Object> saveQuote(HashMap<String, Object> hm, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		long invoiceNo = getPkForThistable("trn_quote_register", Long.valueOf(hm.get("app_id").toString()), conWithF);

		parameters.add(hm.get("customer_id"));
		parameters.add(hm.get("gross_amount"));
		parameters.add(hm.get("item_discount"));
		parameters.add(hm.get("invoice_discount"));
		parameters.add(hm.get("total_amount"));
		parameters.add(hm.get("payment_type"));

		parameters.add(getDateASYYYYMMDD(hm.get("invoice_date").toString()));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("app_id"));

		parameters.add(invoiceNo);
		parameters.add(hm.get("total_gst"));

		long quoteId = insertUpdateDuablDB(
				"insert into trn_quote_register values (default,?,?,?,?,?,?,?,?,sysdate(),1,?,?,?,?,?)", parameters,
				conWithF);
		hm.put("quote_id", quoteId);

		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");
		String[] stringTermsArray = (String[]) hm.get("stringTermsArray");

		int i = 1;
		for (Object item : stringTermsArray) {
			parameters = new ArrayList<>();
			parameters.add(quoteId);
			parameters.add(item);
			parameters.add(i++);

			insertUpdateDuablDB("insert into quote_terms_details values (default,?,?,?)", parameters,
					conWithF);
		}

		for (HashMap<String, Object> item : itemDetailsList) {
			parameters = new ArrayList<>();
			parameters.add(quoteId);
			parameters.add(item.get("item_id"));
			parameters.add(item.get("qty"));
			parameters.add(item.get("rate"));
			parameters.add(item.get("custom_rate"));
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("app_id"));
			parameters.add(item.get("gst_amount"));

			insertUpdateDuablDB("insert into trn_quote_details values (default,?,?,?,?,?,?,sysdate(),?,?)", parameters,
					conWithF);
		}

		hm.put("payment_for", "Invoice");
		hm.put("quote_id", quoteId);
		hm.put("quote_no", invoiceNo);
		return hm;
	}

	public List<LinkedHashMap<String, Object>> getDailyQuoteDetails(HashMap<String, Object> hm1, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm1.get("fromDate").toString()));
		parameters.add(getDateASYYYYMMDD(hm1.get("toDate").toString()));
		parameters.add(hm1.get("app_id"));

		String query = "select *,date_format(inv.quote_date,'%d/%m/%Y') as FormattedQuoteDate,date_format(inv.updated_date,'%d/%m/%Y %H:%i:%s') as updatedDate from trn_quote_register inv"
				+ " left outer join mst_customer cust on inv.customer_id=cust.customer_id and inv.app_id=cust.app_id  "
				+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
				+ " inner join mst_store store1 on inv.store_id=store1.store_id "
				+ "where date(quote_date) between ? and ?  and inv.app_id=?   "
				+ "and usertbl.app_id=inv.app_id and store1.app_id=inv.app_id and inv.activate_flag=1 ";

		if (hm1.get("storeId") != null && !hm1.get("storeId").equals("") && !hm1.get("storeId").equals("-1")) {
			parameters.add(hm1.get("storeId").toString());
			query += " and inv.store_id =?";
		}

		query += " order by quote_date,quote_id asc ";
		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<String> getTermsAndConditionsForQuote(Connection con, String quote_id)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(quote_id);
		return getListOfString(parameters, "select term from quote_terms_details where quote_id=? order by `order` ",
				con);
	}

	public List<String> getDefaultTermsAndConditions(Connection con, String appId)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfString(parameters,
				"select terms_condition_content from mst_terms_and_conditions where app_id=? order by `order` ", con);
	}

	public LinkedHashMap<String, Object> getMobileBookingDetailsById(String mobileBookingId, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(mobileBookingId);
		LinkedHashMap<String, Object> itemDetailsMap = new LinkedHashMap<>();
		itemDetailsMap = getMapReturnObject(parameters,
				"select *,date_format(torf.created_date,'%d/%m/%Y %H:%i') as FormattedFromDate from trn_order_register_frommobileapp torf,customer_user_mpg cum,mst_customer cust  where order_id=? and cum.user_id=torf.user_id  and cust.customer_id=cum.customer_id",
				con);

		parameters = new ArrayList<>();
		parameters.add(mobileBookingId);

		itemDetailsMap.put("listOfItems",
				getListOfLinkedHashHashMap(parameters,
						"select * from trn_suborder_register reg,mst_items item where order_id=? and item.item_id=reg.item_id",
						con));
		return itemDetailsMap;

	}

	public String getDebitInForItem(String item_id, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(item_id);
		return getMap(parameters, "select debit_in from mst_items where item_id=?", con).get("debit_in");

	}

	public HashMap<String, Object> savePurchaseInvoice(HashMap<String, Object> hm, Connection conWithF)
			throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		long invoiceNo = getPkForThistable("trn_purchase_invoice_register", Long.valueOf(hm.get("app_id").toString()),
				conWithF);

		parameters.add(hm.get("customer_id"));
		parameters.add(hm.get("gross_amount"));
		parameters.add(hm.get("total_amount"));
		parameters.add(getDateASYYYYMMDD(hm.get("invoice_date").toString()));
		parameters.add(hm.get("user_id"));

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("app_id"));
		parameters.add(invoiceNo);

		parameters.add(hm.get("total_gst"));
		parameters.add(hm.get("txttallyrefno"));
		parameters.add(hm.get("txtvendorinvoiceno"));

		long invoiceId = insertUpdateDuablDB(
				"insert into trn_purchase_invoice_register values (default,?,?,?,?,?,sysdate(),1,"
						+ "?,?,?,?,?,?,?)",
				parameters,
				conWithF);
		hm.put("invoice_id", invoiceId);

		List<HashMap<String, Object>> itemDetailsList = (List<HashMap<String, Object>>) hm.get("itemDetails");
		for (HashMap<String, Object> item : itemDetailsList) {
			parameters = new ArrayList<>();
			parameters.add(invoiceId);
			parameters.add(item.get("item_id"));
			parameters.add(item.get("qty"));
			parameters.add(item.get("rate"));

			parameters.add(item.get("sgst_amount"));
			parameters.add(item.get("sgst_percentage"));

			parameters.add(item.get("cgst_amount"));
			parameters.add(item.get("cgst_percentage"));

			parameters.add(item.get("item_amount"));
			parameters.add(item.get("user_id"));
			parameters.add(hm.get("app_id"));

			insertUpdateDuablDB("insert into trn_purchase_invoice_details values (default,?,?,?,?,?,?,"
					+ "?,?,?,?,sysdate(),?)", parameters,
					conWithF);
		}
		hm.put("invoice_id", invoiceId);
		hm.put("invoice_no", invoiceNo);
		return hm;
	}

	public List<LinkedHashMap<String, Object>> getInvoicesPurchase(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select * from trn_purchase_invoice_register tpr,mst_vendor cust, mst_store store"
				+ " where cust.vendor_id=tpr.vendor_id and store.store_id=tpr.store_id and tpr.activate_flag=1 ";

		String storeId = hm.get("storeId").toString();
		String fromDate = hm.get("txtfromdate").toString();
		String toDate = hm.get("txttodate").toString();

		if (storeId != null && !storeId.equals("") && !storeId.equals("-1")) {
			query += " and tpr.store_id= ? ";
			parameters.add(storeId);
		}

		if (fromDate != null && !fromDate.equals("")) {
			query += " and tpr.invoice_date between  ?  and ?";
			parameters.add(getDateASYYYYMMDD(fromDate));
			parameters.add(getDateASYYYYMMDD(toDate));

		}

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public String deleteQuote(long quoteId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(quoteId);
		insertUpdateDuablDB("UPDATE trn_quote_register  SET activate_flag=0,updated_date=SYSDATE() WHERE quote_id=?",
				parameters,
				conWithF);
		return "Quote Deleted Succesfully";
	}

	public String deletePurchaseInvoice(long invoiceId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(invoiceId);
		insertUpdate(
				"UPDATE trn_purchase_invoice_register SET activate_flag=0,updated_by=?,updated_date=sysdate() WHERE invoice_id=?",
				parameters, conWithF);
		parameters.clear();

		return userId;
	}

	public List<LinkedHashMap<String, Object>> getCategoryWiseStoreWiseDetails(String appId, String fromDate,
			String toDate, String storeId, Connection con) throws ParseException, ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\r\n"
				+ "ms.store_name ,mc.category_name,sum(tid.custom_rate) as amt1 \r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir,\r\n"
				+ "	trn_invoice_details tid,\r\n"
				+ "	mst_items mi ,\r\n"
				+ "	mst_category mc ,\r\n"
				+ "	mst_store ms \r\n"
				+ "where\r\n"
				+ "	tir.invoice_id = tid.invoice_id\r\n"
				+ "	and tir.activate_flag = 1\r\n"
				+ "	and tir.app_id = ?\r\n"
				+ "	 and tid.item_id =mi.item_id and mc.category_id =mi.parent_category_id and\r\n"
				+ "	 ms.store_id =tir.store_id and ms.activate_flag =1 and tir.invoice_date  between ? and ? and ms.store_id=?\r\n"
				+ "group by ms.store_name,mc.category_name";

		parameters.add((appId));
		parameters.add(getDateASYYYYMMDD(fromDate));
		parameters.add(getDateASYYYYMMDD(toDate));
		parameters.add((storeId));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public long AddVisitor(Connection conWithF, HashMap<String, Object> hm) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("visitor_id", "~default");
		valuesMap.put("visitor_name", hm.get("visitorname"));
		valuesMap.put("address", hm.get("address"));
		valuesMap.put("purpose_of_visit", hm.get("purpose_of_visit"));
		valuesMap.put("remarks", hm.get("remarks"));
		valuesMap.put("mobile_no", hm.get("MobileNo"));
		valuesMap.put("email_id", hm.get("EmailId"));
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("app_id", hm.get("app_id"));
		valuesMap.put("in_time", "~sysdate()");
		valuesMap.put("activate_flag", "1");

		Query q = new Query("visitor_entry", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);

	}

	public String updateVisitor(long visitorId, Connection conWithF, HashMap<String, Object> hm) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("visitor_name", hm.get("visitorname"));
		valuesMap.put("address", hm.get("address"));
		valuesMap.put("purpose_of_visit", hm.get("purpose_of_visit"));
		valuesMap.put("remarks", hm.get("remarks"));
		valuesMap.put("mobile_no", hm.get("MobileNo"));
		valuesMap.put("email_id", hm.get("EmailId"));
		valuesMap.put("updated_date", "~sysdate()");

		HashMap<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("visitor_id", visitorId);

		Query q = new Query("visitor_entry", "update", valuesMap, whereMap);
		insertUpdateEnhanced(q, conWithF);

		return "visitor Updated Succesfully";

	}

	public LinkedHashMap<String, String> getvisitorDetails(long VisitorId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(VisitorId);
		return getMap(parameters,
				"select * from visitor_entry where visitor_id=?", con);

	}

	public List<LinkedHashMap<String, Object>> showVisitors(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
		return getListOfLinkedHashHashMap(parameters,
				"select visitor_id visitorId,visitor_name visitorname, purpose_of_visit purpose_of_visit, mobile_no MobileNo,email_id EmailId,in_time in_time from visitor_entry where app_id=? and date(in_time) between ? and ? and activate_flag=1 order by in_time desc",
				con);

	}

	public String deleteVisitor(long visitorId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(visitorId);

		insertUpdateDuablDB("UPDATE visitor_entry  SET activate_flag=0,updated_date=SYSDATE() WHERE visitor_id=?",
				parameters, conWithF);
		return "Visitor deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getDistinctPurposeOfVisitList(Connection con, String appId)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"select distinct(purpose_of_visit) from visitor_entry where app_id=? and activate_flag=1", con);
	}

	public LinkedHashMap<String, String> getUserConfigurations(String userId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		return getMap(parameters,
				"select * from user_configurations uc where user_id =?", con);

	}

	public String deleteEmployee(long employeeId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(employeeId);
		insertUpdateDuablDB("UPDATE tbl_user_mst SET activate_flag=0,updated_date=SYSDATE() WHERE user_id=?",
				parameters, conWithF);
		return "Employee Deleted Succesfully";
	}

	public LinkedHashMap<String, String> getuserDetailsByUserName(String username, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(username);
		return getMap(parameters, "select * from tbl_user_mst where username=?", con);

	}

	public long addInvoiceFormats(long id, String formatName, Connection con) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("format_id", id);
		valuesMap.put("format_name", formatName);

		Query q = new Query("invoice_formats", "insert", valuesMap);
		return insertUpdateEnhanced(q, con);

	}

	public long addInvoiceTypes(long id, String typeName, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(id);
		parameters.add(typeName);
		return insertUpdateDuablDB("insert into invoice_types values (?,?)", parameters, con);
	}

	public long addSphDetails(long detailsId, String sphr, String sphl, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(detailsId);
		parameters.add(sphr);
		parameters.add(sphl);
		return insertUpdateDuablDB("insert into trn_sph_details values (default,?,?)", parameters, con);
	}

	public List<LinkedHashMap<String, Object>> getFuelMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_items where app_id=? and activate_flag=1",
				con);
	}

	public List<LinkedHashMap<String, Object>> getNozzleMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from nozzle_master nzlmst,mst_items fuelmst,dispenser_master dm "
						+ "where nzlmst.app_id=? and nzlmst.activate_flag=1 and fuelmst.item_id=nzlmst.item_id and dm.dispenser_id=nzlmst.parent_dispenser_id order by dispenser_name,nozzle_name",
				con);
	}

	public List<LinkedHashMap<String, Object>> getActiveNozzles(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from trn_nozzle_register tnr,nozzle_master nm,"
						+ "mst_items item,tbl_user_mst tum,shift_master shift where check_in_time is not null and check_out_time is null "
						+ "and tnr.app_id=? and nm.nozzle_id=tnr.nozzle_id and item.item_id=nm.item_id and tnr.attendant_id=tum.user_id and shift.shift_id=tnr.shift_id order by check_in_time desc",
				con);
	}

	public List<LinkedHashMap<String, Object>> getPastTwoDaysNozzles(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from trn_nozzle_register tnr,nozzle_master nm,"
						+ "mst_items item,tbl_user_mst tum,shift_master shift where "
						+ "tnr.app_id=? and nm.nozzle_id=tnr.nozzle_id and item.item_id=nm.item_id and tnr.attendant_id=tum.user_id and shift.shift_id=tnr.shift_id and check_in_time >=DATE_ADD(curdate() , INTERVAL -2 DAY) order by check_in_time desc",
				con);
	}

	public List<LinkedHashMap<String, Object>> getActiveNozzlesForMe(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("user_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from trn_nozzle_register tnr,nozzle_master nm,mst_items item where check_in_time is not null and check_out_time is null and tnr.app_id=? and nm.nozzle_id=tnr.nozzle_id and item.item_id=nm.item_id and tnr.user_id=?",
				con);
	}

	public List<LinkedHashMap<String, Object>> getAvailableNozzles(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select \r\n"
						+ "*,case when (check_in_time is not null and check_out_time is null) then 'Red' else 'Green' end color\r\n"
						+ " from nozzle_master nm \r\n"
						+ "left outer join (\r\n"
						+ "select nozzle_id,max(check_in_time) checkintime,user_id,check_in_time,check_out_time,opening_reading,closing_reading  from trn_nozzle_register tnr  where app_id =? \r\n"
						+ "group by nozzle_id ) T  on  nm.nozzle_id =T.nozzle_id \r\n"
						+ "left outer join tbl_user_mst tum on tum.user_id =nm.updated_by left outer join mst_items fuelmst on  fuelmst.item_id=nm.item_id \r\n"
						+ " \r\n"
						+ "where nm.app_id =?",
				con);
	}

	public String deleteFuel(long categoryId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryId);
		insertUpdateDuablDB("UPDATE mst_items  SET activate_flag=0,updated_date=SYSDATE() WHERE item_id=?",
				parameters, conWithF);
		return "Category updated Succesfully";
	}

	public String deleteNozzle(long categoryId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryId);
		insertUpdateDuablDB("UPDATE nozzle_master  SET activate_flag=0,updated_date=SYSDATE() WHERE nozzle_id=?",
				parameters, conWithF);
		return "Category updated Succesfully";
	}



	public long addFuel(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("fuelName"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));

		String insertQuery = "insert into mst_items values (default,?,1,?,sysdate(),?) ";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public long checkInNozzle(Connection conWithF, HashMap<String, Object> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		hm.put("itemId", getNozzleDetails(hm.get("nozzle_id").toString(), conWithF).get("item_id"));

		parameters.add(hm.get("nozzle_id"));
		parameters.add(hm.get("drpattendantid"));
		parameters.add(hm.get("opening_reading"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("drpshift"));
		parameters.add(hm.get("itemPrice"));
		parameters.add(hm.get("itemId"));
		parameters.add(getDateASYYYYMMDD(hm.get("accountingDate").toString()));

		String insertQuery = "insert into trn_nozzle_register values "
				+ " (default,?,?,sysdate(),null,?,null,1,?,sysdate(),?,?,?,?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public long checkInNozzleNew(Connection conWithF, HashMap<String, Object> hm) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		hm.put("itemId", getNozzleDetails(hm.get("nozzle_id").toString(), conWithF).get("item_id"));
		valuesMap.put("trn_nozzle_id", "~default");
		valuesMap.put("nozzle_id", hm.get("nozzle_id"));
		valuesMap.put("attendant_id", hm.get("drpattendantid"));
		valuesMap.put("check_in_time", "~sysdate()");
		valuesMap.put("check_out_time", "~null");
		valuesMap.put("opening_reading", hm.get("opening_reading"));
		valuesMap.put("closing_reading", "~null");
		valuesMap.put("totalizer_opening_reading", hm.get("totalizer_opening_reading"));
		valuesMap.put("totalizer_closing_reading", "~null");
		valuesMap.put("activate_flag", "~1");
		valuesMap.put("updated_by", hm.get("user_id"));
		valuesMap.put("updated_date", "~null");
		valuesMap.put("app_id", hm.get("app_id"));
		valuesMap.put("shift_id", hm.get("drpshift"));
		valuesMap.put("rate", hm.get("itemPrice"));
		valuesMap.put("item_id", hm.get("itemId"));
		valuesMap.put("accounting_date", getDateASYYYYMMDD(hm.get("accountingDate").toString()));

		Query q = new Query("trn_nozzle_register", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);

	}

	public long checkOutNozzle(Connection conWithF, HashMap<String, Object> hm) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("closing_reading", hm.get("closing_reading"));
		valuesMap.put("totalizer_closing_reading", hm.get("totalizer_closing_reading"));
		valuesMap.put("check_out_time", "~sysdate()");

		HashMap<String, Object> whereMap = new HashMap<String, Object>();
		whereMap.put("trn_nozzle_id", hm.get("trn_nozzle_id"));

		Query q = new Query("trn_nozzle_register", "update", valuesMap, whereMap);
		return insertUpdateEnhanced(q, conWithF);

	}

	public long addNozzle(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("NozzleName"));
		parameters.add(hm.get("drpfueltype")); // this also

		parameters.add(hm.get("user_id"));

		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("drpDispenserId"));

		String insertQuery = "insert into nozzle_master values (default,?,?,1,?,sysdate(),?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public String updateNozzle(long nozzleId, String nozzleName, String drpfuelType, String drpDispenserId,
			Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(nozzleName);
		parameters.add(drpfuelType); // this also
		parameters.add(drpDispenserId);
		parameters.add(nozzleId);
		insertUpdateDuablDB("update nozzle_master set nozzle_name=?,item_id=?,parent_dispenser_id=? where nozzle_id=?",
				parameters, con);
		return "Nozzle Updated Succesfully";
	}

	public LinkedHashMap<String, String> getLatestNozzelByCheckout(String nozzleId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(nozzleId);

		return getMap(parameters, "select *,\n" +
				"case when (check_in_time is not null and check_out_time is null) then 'Occupied' else 'Empty' end status\n"
				+
				"from trn_nozzle_register tnr2\n" +
				"left outer join tbl_user_mst tum on tnr2.attendant_id =tum.user_id\n" +
				"where trn_nozzle_id =\n" +
				"(select trn_nozzle_id from trn_nozzle_register tnr  left outer join shift_master sm on sm.shift_id=tnr.shift_id where nozzle_id =? order by accounting_date desc,shift_name desc limit 1)\n",
				con);

	}

	public LinkedHashMap<String, String> getDispenserDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("dispenser_id"));

		return getMap(parameters,
				"select * from dispenser_master where dispenser_id=? ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getDispenserMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from dispenser_master where app_id=? and activate_flag=1",
				con);

	}

	public String deleteDispenser(long dispenserId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(dispenserId);
		insertUpdateDuablDB("UPDATE dispenser_master  SET activate_flag=0,updated_date=SYSDATE() WHERE dispenser_id=?",
				parameters, conWithF);
		return "Dispenser Deleted Succesfully";
	}

	public String updateDispenser(long dispenserId, String dispenserName, String userId, Connection con)
			throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(dispenserName);
		parameters.add(userId);
		parameters.add(dispenserId);

		insertUpdateDuablDB(
				"update dispenser_master set dispenser_name=?,updated_by=?,updated_date=sysdate() where dispenser_id=?",
				parameters, con);
		return "Dispenser Updated Succesfully";
	}

	public String updateQRCode(HashMap<String, Object> hm, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("qr_code_number"));
		parameters.add(hm.get("CurrentlyAssignedTo"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("hdnQrId"));

		insertUpdateDuablDB(
				"update mst_qr_code set qr_code_number=?,currently_assigned_to=?,updated_by=? where qr_id=?",
				parameters, con);
		return "QR Updated Succesfully";
	}

	public long addDispenser(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("dispenser_name"));
		parameters.add(hm.get("app_id"));

		parameters.add(hm.get("user_id"));

		String insertQuery = "insert into dispenser_master values (default,?,?,1,?,sysdate()) ";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public List<LinkedHashMap<String, Object>> getNozzleSales(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		String query = "select totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,attendantName,check_in_time,check_out_time,opening_reading,closing_reading,testFuel,"
				+ "updated_by_supervisor,FormattedUpdatedDate"
				+ ",closing_reading-opening_reading-COALESCE(TestFuel,0) diffReading,rate,round(totalizer_closing_reading-totalizer_opening_reading - (COALESCE(TestFuel,0) * rate),2) totalAmount from ( select\r\n"
				+ "	totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,check_in_time,check_out_time,opening_reading,closing_reading,\r\n"
				+ "	date_format(tnr.updated_date, '%d/%m/%Y %H:%i:%s') as FormattedUpdatedDate,rate,\r\n"
				+ "	tum.name attendantName,\r\n"
				+ "	tum2.name updated_by_supervisor,\r\n"
				+ "	(select sum(test_quantity) from trn_test_fuel_register ttfr\r\n"
				+ "where test_date =accounting_date and user_id=tnr.attendant_id and ttfr.test_type='S' and ttfr.activate_flag=1 and shift_id =tnr.shift_id and ttfr.nozzle_id=tnr.nozzle_id)\r\n"
				+ "as testFuel \r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr,\r\n"
				+ "	nozzle_master nm,\r\n"
				+ "	tbl_user_mst tum,\r\n"
				+ "	mst_items item,\r\n"
				+ "	tbl_user_mst tum2,\r\n"
				+ "	shift_master shift\r\n"
				+ "where\r\n"
				+ "	tnr.app_id = ? \r\n"
				+ "	and nm.nozzle_id = tnr.nozzle_id\r\n"
				+ "	and tum.user_id = tnr.attendant_id\r\n"
				+ "	and tum2.user_id = tnr.updated_by\r\n"
				+ "	and shift.shift_id = tnr.shift_id\r\n"
				+ "	and accounting_date=? \r\n"
				+ "	and tnr.shift_id=? \r\n"
				+ "	and item.item_id = tnr.item_id\r\n"
				+ "order by\r\n"
				+ "	shift_name,nozzle_name ) as T";

		if (hm.get("shiftid") == null || hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tnr.shift_id=\\?", "");
		} else {
			parameters.add(hm.get("shiftid"));
		}
		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getNozzleSalesForExport(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		String query = "select totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,attendantName,check_in_time,check_out_time,opening_reading,closing_reading,testFuel,"
				+ "updated_by_supervisor,FormattedUpdatedDate"
				+ ",closing_reading-opening_reading-COALESCE(TestFuel,0) diffReading,rate,round(totalizer_closing_reading-totalizer_opening_reading - (COALESCE(TestFuel,0) * rate),2) totalAmount from ( select\r\n"
				+ "	totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,check_in_time,check_out_time,opening_reading,closing_reading,\r\n"
				+ "	date_format(tnr.updated_date, '%d/%m/%Y %H:%i:%s') as FormattedUpdatedDate,rate,\r\n"
				+ "	tum.name attendantName,\r\n"
				+ "	tum2.name updated_by_supervisor,\r\n"
				+ "	(select sum(test_quantity) from trn_test_fuel_register ttfr\r\n"
				+ "where test_date =accounting_date and user_id=tnr.attendant_id  and ttfr.activate_flag=1 and shift_id =tnr.shift_id and ttfr.nozzle_id=tnr.nozzle_id)\r\n"
				+ "as testFuel \r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr,\r\n"
				+ "	nozzle_master nm,\r\n"
				+ "	tbl_user_mst tum,\r\n"
				+ "	mst_items item,\r\n"
				+ "	tbl_user_mst tum2,\r\n"
				+ "	shift_master shift\r\n"
				+ "where\r\n"
				+ "	tnr.app_id = ? \r\n"
				+ "	and nm.nozzle_id = tnr.nozzle_id\r\n"
				+ "	and tum.user_id = tnr.attendant_id\r\n"
				+ "	and tum2.user_id = tnr.updated_by\r\n"
				+ "	and shift.shift_id = tnr.shift_id\r\n"
				+ "	and accounting_date=? \r\n"
				+ "	and tnr.shift_id=? \r\n"
				+ "	and item.item_id = tnr.item_id\r\n"
				+ "order by\r\n"
				+ "	shift_name,nozzle_name ) as T";

		if (hm.get("shiftid") == null || hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tnr.shift_id=\\?", "");
		} else {
			parameters.add(hm.get("shiftid"));
		}
		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getNozzleSalesGroupByItemShift(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		String query = " select shift_name,item_name,rate,sum(diffReading) SalesSum,rate,sum(totalAmount) totalAmountSum from ( select totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,attendantName,check_in_time,check_out_time,opening_reading,closing_reading,testFuel,"
				+ "updated_by_supervisor,FormattedUpdatedDate"
				+ ",closing_reading-opening_reading-COALESCE(TestFuel,0) diffReading,rate,round(totalizer_closing_reading-totalizer_opening_reading - (COALESCE(TestFuel,0) * rate),2) totalAmount from ( select\r\n"
				+ "	totalizer_opening_reading,totalizer_closing_reading,nozzle_name,item_name,shift_name,check_in_time,check_out_time,opening_reading,closing_reading,\r\n"
				+ "	date_format(tnr.updated_date, '%d/%m/%Y %H:%i:%s') as FormattedUpdatedDate,rate,\r\n"
				+ "	tum.name attendantName,\r\n"
				+ "	tum2.name updated_by_supervisor,\r\n"
				+ "	(select sum(test_quantity) from trn_test_fuel_register ttfr\r\n"
				+ "where test_date =accounting_date and user_id=tnr.attendant_id and ttfr.activate_flag=1 and shift_id =tnr.shift_id and ttfr.nozzle_id=tnr.nozzle_id)\r\n"
				+ "as testFuel \r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr,\r\n"
				+ "	nozzle_master nm,\r\n"
				+ "	tbl_user_mst tum,\r\n"
				+ "	mst_items item,\r\n"
				+ "	tbl_user_mst tum2,\r\n"
				+ "	shift_master shift\r\n"
				+ "where\r\n"
				+ "	tnr.app_id = ? \r\n"
				+ "	and nm.nozzle_id = tnr.nozzle_id\r\n"
				+ "	and tum.user_id = tnr.attendant_id\r\n"
				+ "	and tum2.user_id = tnr.updated_by\r\n"
				+ "	and shift.shift_id = tnr.shift_id\r\n"
				+ "	and accounting_date=? \r\n"
				+ "	and tnr.shift_id=? \r\n"
				+ "	and item.item_id = tnr.item_id\r\n"
				+ "order by\r\n"
				+ "	shift_name,nozzle_name ) as T ) as M group by item_name,shift_name order by item_name,shift_name";

		if (hm.get("shiftid") == null || hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tnr.shift_id=\\?", "");
		} else {
			parameters.add(hm.get("shiftid"));
		}
		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getLubeSales(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		String query = "select *,tum.name attendantName,tid.qty*tid.rate totalAmount,tir.invoice_no,tir.invoice_id from\n"
				+
				"trn_invoice_register tir ,\n" +
				"trn_invoice_details tid ,\n" +
				"rlt_invoice_fuel_details rifd,\n" +
				"tbl_user_mst tum,\n" +
				"shift_master sm,\n" +
				"mst_items mi\n" +
				"where\n" +
				"tir.invoice_id =tid.invoice_id\n" +
				"and rifd.invoice_id =tid.invoice_id\n" +
				"and tum.user_id =rifd.attendant_id \n" +
				"and rifd.shift_id =sm.shift_id \n" +
				"and tid.item_id =mi.item_id and (mi.item_name!='Petrol') and (mi.item_name!='Diesel') \n" +
				"and tir.app_id =? and tir.activate_flag=1\n" +
				"and tir.invoice_date =?\n";
		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("0") && !hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid"));
			query += " and rifd.shift_id=?";
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPumpTests(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		String query = "select\n" +
				"mi.item_name,ttfr.test_quantity ,mi.price,tum.username,tum.name,ttfr.test_quantity*mi.price totalAmountCash,shift_name,nm.nozzle_name\n"
				+
				"from\n" +
				"trn_test_fuel_register ttfr,trn_nozzle_register tnr ,mst_items mi,tbl_user_mst tum,shift_master sm,nozzle_master nm \n"
				+
				"where\n" +
				"ttfr.app_id=? and test_type = 'A' \n" +
				"and ttfr.nozzle_id =tnr.nozzle_id\n" +
				"and tnr.item_id = mi.item_id\n" +
				"and\ttest_date = ?  and tum.user_id=tnr.attendant_id and ttfr.activate_flag=1 and ttfr.shift_id=sm.shift_id and tnr.shift_id =ttfr.shift_id and tnr.nozzle_id=nm.nozzle_id and ttfr.user_id=tum.user_id and date(tnr.accounting_date)=ttfr.test_date  ";
		if (!hm.get("shiftid").equals("0") && !hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid"));
			query += " and ttfr.shift_id =?";
		}
		query += " order by nozzle_name";

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentsForDatesAttendantWise(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}
		parameters.add(hm.get("app_id"));

		String query = "select name,\r\n"
				+ "sum(Cash) csh,\r\n"
				+ "sum(Card) cswp,\r\n"
				+ "sum(Paytm) pytm,\r\n"
				+ "sum(Pending) pnding,\r\n"
				+ "sum(LoyaltyPoints) loyaltyPoints,\r\n"
				+ "shift_name,from_time,to_time,dt,attendant_id from \r\n"
				+ "(select \r\n"
				+ "name,\r\n"
				+ "case when paymentMode='Cash' then amt else 0 end Cash,\r\n"
				+ "case when paymentMode='Card' then amt else 0 end Card,\r\n"
				+ "case when paymentMode='Paytm' then amt else 0 end Paytm,\r\n"
				+ "case when paymentMode='Pending' then amt else 0 end Pending,\r\n"
				+ "case when paymentMode='LoyaltyPoints' then amt else 0 end LoyaltyPoints,\r\n"
				+ "shift_id,dt,attendant_id\r\n"
				+ "from (\r\n"
				+ " select\r\n"
				+ "	sum(amount) amt,\r\n"
				+ "	tum.name, tsc.collection_mode paymentMode,shift_id,collection_date dt,tsc.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_supervisor_collection tsc ,\r\n"
				+ "	tbl_user_mst tum\r\n"
				+ "where\r\n"
				+ "	tsc.attendant_id = tum.user_id\r\n"
				+ "	and tsc.collection_date =? and tsc.shift_id=? \r\n"
				+ "	and tum.app_id=? and tsc.activate_flag=1 \r\n"
				+ "group by\r\n"
				+ "	tum.name,tsc.collection_date,tsc.shift_id,tsc.collection_mode \r\n"
				+ " union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,tpr.payment_mode paymentMode,shift_id,tir.invoice_date dt,tum.user_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "inner join trn_payment_register tpr on tpr.ref_id=tir.invoice_id \r\n"
				+ "where invoice_date =? and tpr.payment_mode !='Cash'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,tpr.payment_mode,rifd.shift_id union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,'Pending',shift_id,tir.invoice_date dt,rifd.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "where invoice_date =? and tir.payment_type='Pending'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,rifd.shift_id) as T) as M,shift_master shft where shft.shift_id=M.shift_id group by name,M.shift_id";

		if (hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tsc.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentsForDatesAttendantWiseExport(HashMap<String, Object> hm,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}
		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (!hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}
		parameters.add(hm.get("app_id"));

		String query = "select name,\r\n"
				+ "sum(Cash) csh,\r\n"
				+ "sum(Card) cswp,\r\n"
				+ "sum(Paytm) pytm,\r\n"
				+ "sum(Pending) pnding,\r\n"
				+ "sum(LoyaltyPoints) loyaltyPoints,\r\n"
				+ "shift_name,from_time,to_time,dt,attendant_id from \r\n"
				+ "(select \r\n"
				+ "name,\r\n"
				+ "case when paymentMode='Cash' then amt else 0 end Cash,\r\n"
				+ "case when paymentMode='Card' then amt else 0 end Card,\r\n"
				+ "case when paymentMode='Paytm' then amt else 0 end Paytm,\r\n"
				+ "case when paymentMode='Pending' then amt else 0 end Pending,\r\n"
				+ "case when paymentMode='LoyaltyPoints' then amt else 0 end LoyaltyPoints,\r\n"
				+ "shift_id,dt,attendant_id\r\n"
				+ "from (\r\n"
				+ " select\r\n"
				+ "	sum(amount) amt,\r\n"
				+ "	tum.name, tsc.collection_mode paymentMode,shift_id,collection_date dt,tsc.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_supervisor_collection tsc ,\r\n"
				+ "	tbl_user_mst tum\r\n"
				+ "where\r\n"
				+ "	tsc.attendant_id = tum.user_id\r\n"
				+ "	and tsc.collection_date =? and tsc.shift_id=? \r\n"
				+ "	and tum.app_id=? and tsc.activate_flag=1 \r\n"
				+ "group by\r\n"
				+ "	tum.name,tsc.collection_date,tsc.shift_id,tsc.collection_mode \r\n"
				+ " union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,tpr.payment_mode paymentMode,shift_id,tir.invoice_date dt,tum.user_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "inner join trn_payment_register tpr on tpr.ref_id=tir.invoice_id \r\n"
				+ "where invoice_date =? and tpr.payment_mode !='Cash'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,tpr.payment_mode,rifd.shift_id union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,'Pending',shift_id,tir.invoice_date dt,rifd.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "where invoice_date =? and tir.payment_type='Pending'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,rifd.shift_id union all  select sum(ttfr.test_quantity*ttfr.item_price*-1) amt,tum.name,'Cash',shift_id,test_date dt,tum.user_id  from trn_test_fuel_register ttfr inner join tbl_user_mst tum on tum.user_id =ttfr.user_id "
				+ "where test_date=? and ttfr.test_type ='A' and ttfr.activate_flag =1 and ttfr.shift_id=? and ttfr.app_id =? "
				+ "group by tum.name,ttfr.shift_id) as T"
				+ ") as M,shift_master shft where shft.shift_id=M.shift_id group by name,M.shift_id";

		if (hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tsc.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
			query = query.replaceAll("and ttfr.shift_id=\\?", "");
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPaymentsForDatesAttendantWiseGroupByPayment(
			HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}

		parameters.add(hm.get("app_id"));

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("-1")) {
			parameters.add(hm.get("shiftid").toString());
		}
		parameters.add(hm.get("app_id"));

		String query = "select sum(csh) cashsum,sum(cswp) cardswipesum,sum(pytm) paytmsum,sum(pnding) pendingSum from ( select name,\r\n"
				+ "sum(Cash) csh,\r\n"
				+ "sum(Card) cswp,\r\n"
				+ "sum(Paytm) pytm,\r\n"
				+ "sum(Pending) pnding,\r\n"
				+ "sum(LoyaltyPoints) loyaltyPoints,\r\n"
				+ "shift_name,from_time,to_time,dt,attendant_id from \r\n"
				+ "(select \r\n"
				+ "name,\r\n"
				+ "case when paymentMode='Cash' then amt else 0 end Cash,\r\n"
				+ "case when paymentMode='Card' then amt else 0 end Card,\r\n"
				+ "case when paymentMode='Paytm' then amt else 0 end Paytm,\r\n"
				+ "case when paymentMode='Pending' then amt else 0 end Pending,\r\n"
				+ "case when paymentMode='LoyaltyPoints' then amt else 0 end LoyaltyPoints,\r\n"
				+ "shift_id,dt,attendant_id\r\n"
				+ "from (\r\n"
				+ " select\r\n"
				+ "	sum(amount) amt,\r\n"
				+ "	tum.name, tsc.collection_mode paymentMode,shift_id,collection_date dt,tsc.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_supervisor_collection tsc ,\r\n"
				+ "	tbl_user_mst tum\r\n"
				+ "where\r\n"
				+ "	tsc.attendant_id = tum.user_id\r\n"
				+ "	and tsc.collection_date =? and tsc.shift_id=? \r\n"
				+ "	and tum.app_id=? and tsc.activate_flag=1 \r\n"
				+ "group by\r\n"
				+ "	tum.name,tsc.collection_date,tsc.shift_id,tsc.collection_mode \r\n"
				+ " union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,tpr.payment_mode paymentMode,shift_id,tir.invoice_date dt,tum.user_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "inner join trn_payment_register tpr on tpr.ref_id=tir.invoice_id \r\n"
				+ "where invoice_date =? and tpr.payment_mode !='Cash'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,tpr.payment_mode,rifd.shift_id union all \r\n"
				+ "\r\n"
				+ "select\r\n"
				+ "	sum(total_amount) amt,tum.name ,'Pending',shift_id,tir.invoice_date dt,rifd.attendant_id\r\n"
				+ "from\r\n"
				+ "	trn_invoice_register tir\r\n"
				+ "inner join rlt_invoice_fuel_details rifd\r\n"
				+ "on rifd.invoice_id =tir.invoice_id \r\n"
				+ "inner join tbl_user_mst tum on tum.user_id =rifd.attendant_id  \r\n"
				+ "where invoice_date =? and tir.payment_type='Pending'\r\n"
				+ "and rifd.shift_id=? and tir.app_id =? and tir.activate_flag=1 group by tum.name,rifd.shift_id) as T) as M,shift_master shft where shft.shift_id=M.shift_id group by name,M.shift_id ) as M";

		if (hm.get("shiftid") == null || hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and tsc.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
			query = query.replaceAll("and rifd.shift_id=\\?", "");
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getCardSwipes(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select mb.bank_name ,slot_id,sum(amount) cardAmount from\n" +
				"trn_payment_register tpr,\n" +
				"trn_invoice_register tir ,\n" +
				"rlt_invoice_fuel_details rifd ,\n" +
				"swipe_machine_master smm ,\n" +
				"mst_bank mb\n" +
				"where\n" +
				"payment_date=? and tpr.app_id =?\n" +
				"and payment_mode ='Card' and tpr.ref_id =tir.invoice_id\n" +
				"and rifd.invoice_id =tir.invoice_id and rifd.swipe_id =smm.swipe_machine_id and mb.bank_id =smm.account_id and tir.activate_flag =1\n";

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(hm.get("app_id").toString());

		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("-1")) {
			query += " and rifd.shift_id =?";
			parameters.add(hm.get("shiftid").toString());
		}

		query += "group by mb.bank_name,slot_id ";

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPaytmSlotWise(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select\n" +
				"slot_id,sum(amount) paytmAmount\n" +
				"from\n" +
				"trn_supervisor_collection tsc where tsc.activate_flag=1 and app_id =? and collection_date = ?\n" +
				"and collection_mode = 'Paytm' ";

		parameters.add(hm.get("app_id").toString());
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));

		if (hm.get("shiftid") != null && !hm.get("shiftid").equals("-1")) {
			query += " and shift_id =?";
			parameters.add(hm.get("shiftid").toString());
		}

		query += "group by slot_id ";

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public long saveCollectionSupervisor(Connection conWithF, HashMap<String, Object> hm)
			throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("collection_id", "~default");
		valuesMap.put("attendant_id", hm.get("drpemployee"));
		valuesMap.put("amount", hm.get("amount"));
		valuesMap.put("activate_flag", "~1");
		valuesMap.put("updated_by", hm.get("user_id"));
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("shift_id", hm.get("drpshiftid"));
		valuesMap.put("collection_date", getDateASYYYYMMDD(hm.get("txtcollectiondate").toString()));
		valuesMap.put("app_id", hm.get("app_id"));
		valuesMap.put("collection_mode", hm.get("mode"));
		valuesMap.put("shift_date", getDateASYYYYMMDD(hm.get("shift_date").toString()));
		valuesMap.put("slot_id", hm.get("slot_id"));

		Query q = new Query("trn_supervisor_collection", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);

	}

	public long submitCashtoVault(Connection conWithF, HashMap<String, Object> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("userId"));
		parameters.add(hm.get("shiftId"));
		parameters.add(getDateASYYYYMMDD(hm.get("collectionDate").toString()));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("userId"));
		parameters.add(hm.get("notes"));
		parameters.add(hm.get("coins"));

		String insertQuery = "insert into trn_cash_to_vault values (default,?,?,?,?,sysdate(),1,?,?,?,0)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public LinkedHashMap<String, String> getShiftDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("shift_id"));

		return getMap(parameters,
				"select\r\n"
						+ "*,	TIME_FORMAT(from_time, '%H') fromHour,\r\n"
						+ "	minute(from_time) fromMinute,\r\n"
						+ "	TIME_FORMAT(to_time, '%H') toHour,\r\n"
						+ "	minute(to_time) toMinute\r\n"
						+ "from\r\n"
						+ "	shift_master where shift_id=? ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getShiftMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from shift_master where app_id=? and activate_flag=1",
				con);

	}

	public List<LinkedHashMap<String, Object>> getCreditSalesForthisDate(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "select\n" +
				"*\n" +
				"from\n" +
				"trn_invoice_register tir,\n" +
				"mst_customer mc ,\n" +
				"rlt_invoice_fuel_details rifd\n" +
				"where\n" +
				"payment_type = 'Pending'\n" +
				"and tir.activate_flag = 1\n" +
				"and mc.customer_id =tir.customer_id\n" +
				"and rifd.invoice_id =tir.invoice_id\n" +
				"and rifd.shift_id=? \n" +
				"and invoice_date = ?\n" +
				"and tir.app_id = ?\n";

		if (hm.get("shiftid") != null || hm.get("shiftid").equals("") || hm.get("shiftid").equals("-1")) {
			query = query.replaceAll("and rifd.shift_id=\\?", "");
		} else {
			parameters.add(hm.get("shift_id"));
		}
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(hm.get("app_id"));

		query += " order by mc.customer_name ";

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);

	}

	public String getSuggestedShiftId(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		String quer = "select shift_id from shift_master where app_id=? and activate_flag=1 and current_time() between from_time and to_time";
		// String quer="select shift_id from shift_master where app_id=? and
		// activate_flag=1 and '23:00' between from_time and to_time";
		return getMap(parameters,
				quer,
				con).get("shift_id");

	}

	public String isTimeBetween00to06(Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String quer = "select case  when curtime() between '00:00' and '06:00' then '0' else '1' end isTimeBetween from dual";
		// String quer="select case when '23:00' between '00:00' and '06:00' then '0'
		// else '1' end isTimeBetween from dual";
		return getMap(parameters,
				quer,
				con).get("isTimeBetween");

	}

	public String getSuggestedShiftIdBasedOnOrderId(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("date_time_from_payment"));
		return getMap(parameters,
				"select shift_id from shift_master where app_id=? and activate_flag=1 and time(?) between from_time and to_time",
				con).get("shift_id");

	}

	public long addShift(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("shift_name"));

		parameters.add(hm.get("from_time_hour") + ":" + hm.get("from_time_minute"));
		parameters.add(hm.get("to_time_hour") + ":" + hm.get("to_time_minute"));

		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("user_id"));
		// String insertQuery = "insert into shift_master values
		// (default,?,?,?,?,?,1,?,?,sysdate()) ";
		String insertQuery = "insert into shift_master values (default,?,?,?,1,?,?,sysdate()) ";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public String updateShift(long shiftId, String shift_name, String from_time_hour, String from_time_minute,
			String to_time_hour, String to_time_minute, String userId, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(shift_name);
		parameters.add(from_time_hour + ":" + from_time_minute);

		parameters.add(to_time_hour + ":" + to_time_minute);

		parameters.add(userId);
		parameters.add(shiftId);

		insertUpdateDuablDB(
				"update shift_master set shift_name=?,from_time=?,to_time=?,updated_by=?,updated_date=sysdate() where shift_id=?",
				parameters, con);
		return "Shift Updated Succesfully";
	}

	public HashMap<String, String> getNozzleDetailsFromRegister(HashMap<String, String> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("testNozzle"));
		return getMap(parameters,
				"select * from trn_nozzle_register where nozzle_id=? and check_in_time is not null and check_out_time is null",
				con);
	}

	public String deleteShift(long shiftId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(shiftId);
		insertUpdateDuablDB("UPDATE shift_master  SET activate_flag=0,updated_date=SYSDATE() WHERE shift_id=?",
				parameters, conWithF);
		return "Shift Deleted Succesfully";
	}

	public long addTestFuel(Connection conWithF, HashMap<String, String> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("testQuantity"));
		parameters.add(hm.get("testNozzle"));
		parameters.add(hm.get("shift_id"));
		parameters.add(hm.get("attendant_id"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("testDate")));
		parameters.add(hm.get("test_type"));
		parameters.add(hm.get("item_price"));

		String insertQuery = "INSERT INTO trn_test_fuel_register\r\n"
				+ " VALUES (default,?,?,?,?,sysdate(),?,?,?,1,?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long addItemOrdering(Connection conWithF, HashMap<String, String> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("item_order_no"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("item_id"));

		String insertQuery = "update mst_items set order_no=?,updated_by=?,updated_date=sysdate() where item_id=?";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long addConfigureLR(Connection conWithF, HashMap<String, String> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("printer"));
		parameters.add(hm.get("copies"));
		parameters.add(hm.get("app_id"));

		String insertQuery = "INSERT INTO mst_config \r\n"
				+ " VALUES (default,?,?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public long addGenerateLR(Connection conWithF, HashMap<String, String> hm) throws SQLException, ParseException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("lrnumber"));
		parameters.add(hm.get("stockistname"));
		parameters.add(hm.get("wadhwanto"));
		parameters.add(hm.get("address"));
		parameters.add(hm.get("city"));
		parameters.add(hm.get("telno"));
		parameters.add(hm.get("truckno"));
		parameters.add(hm.get("weight"));
		parameters.add(hm.get("cement"));
		parameters.add(hm.get("bags"));
		parameters.add(hm.get("app_id"));

		String insertQuery = "insert into trn_lr_register"
				+ "(lr_no,stockist_name,wadhwan_to,address,created_date,city,tel_no,truck_no,weight,cement,bags,app_id) VALUES"
				+ "(?,?,?,?,sysdate(),?,?,?,?,?,?,?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);
	}

	public LinkedHashMap<String, String> getMaxLrNo(Connection con, HashMap<String, Object> hm)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		String query = "SELECT   CASE WHEN MAX(lr_no) IS NULL  THEN 1 ELSE MAX(lr_no)+1 END lrno  FROM `trn_lr_register` WHERE app_id=?";

		return getMap(parameters, query, con);

	}

	public LinkedHashMap<String, String> searchLR(Connection con, HashMap<String, Object> hm)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("lrnumbersearch"));
		parameters.add(hm.get("app_id"));

		String query = "SELECT DATE_FORMAT(created_date,'%d/%m/%Y') AS niceDate,`stockist_name`,`wadhwan_to`,`address`,city,tel_no,truck_no,weight,cement,bags FROM trn_lr_register WHERE `lr_no`=? and app_id=?";

		return getMap(parameters, query, con);
	}

	public LinkedHashMap<String, String> getSwipeDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("swipe_machine_id"));

		return getMap(parameters,
				"select * from swipe_machine_master where swipe_machine_id=? ",
				con);
	}

	public LinkedHashMap<String, String> getVehicleDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("vehicle_id"));

		return getMap(parameters,
				"select * from mst_vehicle mv,mst_customer mc where mv.vehicle_id=? and mc.customer_id=mv.customer_id ",
				con);
	}

	public LinkedHashMap<String, String> getVehicleDetailsById(String vehicleId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(vehicleId);

		return getMap(parameters,
				"select * from mst_vehicle mv,mst_customer mc where mv.vehicle_id=? and mc.customer_id=mv.customer_id ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getSwipeMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select\r\n" + //
						"\t*\r\n" + //
						"from\r\n" + //
						"\tswipe_machine_master smm,\r\n" + //
						"\tmst_bank mb\r\n" + //
						"where\r\n" + //
						"\tmb.bank_id = smm.account_id\r\n" + //
						"\tand smm.activate_flag = 1 and smm.app_id=? ",
				con);

	}

	public long addSwipe(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		String insertQuery = "insert into swipe_machine_master  values (default,:swipe_machine_name,:txtaccountid,"
				+ ":swipe_machine_short_name,1,:app_id,:user_id,sysdate()) ";
		return insertUpdateCustomParameterized(insertQuery, hm, conWithF);
	}

	public long addIncomingPaymentPetrol(Connection conWithF, HashMap<String, String> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("order_id"));
		parameters.add(hm.get("bhim_upi_id"));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("mobile_no"));
		parameters.add(hm.get("date_time_from_payment"));
		parameters.add(hm.get("app_id"));

		String insertQuery = "INSERT INTO trn_incoming_online_payments\r\n"
				+ " VALUES (?, ?, ?, ?, 0, ?, sysdate(), NULL, '0',?)";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public LinkedHashMap<String, String> getUnclaimedPaymentDetails(String categoryId, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(categoryId);
		return getMap(parameters, "select * from trn_incoming_online_payments where order_id=?", con);
	}

	public List<LinkedHashMap<String, Object>> getEmployeesCheckedInToNozzle(String appId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, "select\r\n"
				+ "	*\r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr inner join tbl_user_mst tum on tnr.attendant_id =tum.user_id  \r\n"
				+ "where\r\n"
				+ "	check_in_time is not null\r\n"
				+ "	and check_out_time is null and tnr.app_id =?", con);
	}

	public List<LinkedHashMap<String, Object>> getDistinctEmployeesCheckedInToNozzle(String appId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters, " select\r\n"
				+ "	distinct(user_id),username,name\r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr inner join tbl_user_mst tum on tnr.attendant_id =tum.user_id  \r\n"
				+ "where\r\n"
				+ "	check_in_time is not null\r\n"
				+ "	and check_out_time is null and tnr.app_id =?", con);
	}

	public String updateSwipe(long hdnSwipeMachineId, String swipe_machine_name, String txtaccountid,
			String swipe_machine_short_name, String userId, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(swipe_machine_name);
		parameters.add(txtaccountid);
		parameters.add(swipe_machine_short_name);
		parameters.add(userId);
		parameters.add(hdnSwipeMachineId);

		insertUpdateDuablDB(
				"update swipe_machine_master set swipe_machine_name=?,account_id=?,swipe_machine_short_name=?,updated_by=?,updated_date=sysdate() where swipe_machine_id=?",
				parameters, con);
		return "Swipe Updated Succesfully";
	}

	public String updateVehicle(HashMap<String, Object> hm, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("vehicleName"));
		parameters.add(hm.get("customerId"));

		parameters.add(hm.get("vehicleNumber"));
		parameters.add(hm.get("userId"));
		parameters.add(hm.get("vehicle_id"));

		insertUpdateDuablDB(
				"update mst_vehicle set vehicle_name=?,customer_id=?, vehicle_number=?,updated_by=?,updated_date=sysdate() where vehicle_id=?",
				parameters, con);
		return "Vehicle Updated Succesfully";
	}

	public String deleteSwipe(long swipeMachineId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(swipeMachineId);
		insertUpdateDuablDB(
				"UPDATE swipe_machine_master  SET activate_flag=0,updated_date=SYSDATE() WHERE swipe_machine_id=?",
				parameters, conWithF);
		return "Swipe Deleted Succesfully";
	}

	public String deleteSupervisorTransaction(String appId, long collection_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(collection_id);
		parameters.add(appId);
		insertUpdateDuablDB(
				"UPDATE trn_supervisor_collection tsc  SET activate_flag=0,updated_date=SYSDATE() WHERE collection_id=? and app_id=?",
				parameters, conWithF);
		return "Transaction Deleted Succesfully";
	}

	public String deletePaytmTransaction(long order_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(order_id);
		insertUpdateDuablDB(
				"UPDATE trn_supervisor_collection tsc, trn_incoming_online_payments tiop SET tsc.activate_flag=0,tsc.updated_date=SYSDATE() WHERE tsc.paytm_order_id=?",
				parameters, conWithF);
		return "Transaction Deleted Succesfully";
	}

	public String deletePaytmTransactionFromCollectPayment(long collection_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(collection_id);
		insertUpdateDuablDB(
				"UPDATE trn_supervisor_collection tsc SET activate_flag=0 and updated_date=sysdate() where collection_id=?",
				parameters, conWithF);
		return "Transaction Deleted Succesfully";
	}

	public LinkedHashMap<String, String> getOrderIdForCollectionId(long collection_id, Connection conWithF)
			throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(collection_id);
		return getMap(parameters, "select paytm_order_id from trn_supervisor_collection tsc where collection_id=?",
				conWithF);
	}

	public String deleteTestFuel(long test_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(test_id);
		insertUpdateDuablDB("UPDATE trn_test_fuel_register ttfr set activate_flag=0 where test_id=?",
				parameters, conWithF);
		return "Test Fuel Deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getTestData(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("type"));

		return getListOfLinkedHashHashMap(parameters,
				"select *,tum.name attendantName,tum2.name superVisorName from trn_test_fuel_register ttfr,tbl_user_mst tum,tbl_user_mst tum2,shift_master shft,nozzle_master nm,mst_items fm "
						+ " where test_date between ? and ? and ttfr.activate_flag=1 and ttfr.app_id=? and ttfr.shift_id=shft.shift_id and "
						+ "tum.user_id=ttfr.user_id and tum2.user_id=ttfr.updated_by and nm.nozzle_id=ttfr.nozzle_id and fm.item_id=nm.item_id and ttfr.test_type=? order by ttfr.updated_date desc ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getCollectionData(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		return getListOfLinkedHashHashMap(parameters,
				"",
				con);
	}

	public List<LinkedHashMap<String, String>> getCollectionDataDateAndShiftWise(String collection_date, String shiftId,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(collection_date));
		parameters.add(shiftId);

		return getListOfLinkedHashHashMapString(parameters,
				"select \r\n"
						+ "*,\r\n"
						+ "tum.name as AttendantName,shift_name,tum2.name as SupervisorName\r\n"
						+ "from trn_supervisor_collection tsc,shift_master sm ,tbl_user_mst tum  ,tbl_user_mst tum2  \r\n"
						+ "where collection_date=? and sm.shift_id=tsc.shift_id and tsc.shift_id=? and tum.user_id =tsc.attendant_id\r\n"
						+ "and \r\n"
						+ "tum2.user_id =tsc.updated_by;",
				con);
	}

	public List<LinkedHashMap<String, String>> getTestDataDateAndShiftWise(String collection_date, String shiftId,
			String type, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(collection_date));
		parameters.add(shiftId);
		parameters.add(type);

		return getListOfLinkedHashHashMapString(parameters,
				"select \r\n"
						+ "*,\r\n"
						+ "tum.name as AttendantName,shift_name,tum2.name as SupervisorName,DATE_FORMAT(test_date,'%d/%m/%Y') AS niceTestDate\r\n"
						+ "from trn_test_fuel_register tsc,shift_master sm ,tbl_user_mst tum  ,tbl_user_mst tum2,nozzle_master nm  \r\n"
						+ "where test_date=? and sm.shift_id=tsc.shift_id and tsc.shift_id=? and tum.user_id =tsc.user_id and nm.nozzle_id=tsc.nozzle_id \r\n"
						+ "and \r\n"
						+ "tum2.user_id =tsc.updated_by and tsc.test_type=?;",
				con);
	}

	public LinkedHashMap<String, String> getPumpTestEquivalentCash(String collection_date, String shiftId, String appId,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(collection_date));
		parameters.add((appId));

		String query = "select \r\n"
				+ "\r\n"
				+ "sum( tsc.item_price * test_quantity) as CashAmount from trn_test_fuel_register tsc,shift_master sm ,tbl_user_mst tum  ,tbl_user_mst tum2,nozzle_master nm  \r\n"
				+ "where test_date=? and sm.shift_id=tsc.shift_id and tum.user_id =tsc.user_id and nm.nozzle_id=tsc.nozzle_id \r\n"
				+ "and \r\n"
				+ "tum2.user_id =tsc.updated_by and tsc.test_type='A' and tsc.app_id=? and tsc.activate_flag=1";

		if (!shiftId.equals("-1")) {
			query += " and tsc.shift_id=?";
			parameters.add(shiftId);
		}

		return getMap(parameters,
				query,
				con);
	}

	public LinkedHashMap<String, String> getSumOfCollectionAmount(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
		parameters.add(hm.get("suggestedShiftId"));
		parameters.add(hm.get("user_id"));

		return getMap(parameters,
				"select \r\n"
						+ "sum(amount) mySum,\r\n"
						+ "tum.name as AttendantName,shift_name,tum2.name as SupervisorName\r\n"
						+ "from trn_supervisor_collection tsc,shift_master sm ,tbl_user_mst tum  ,tbl_user_mst tum2  \r\n"
						+ "where collection_date  between ? and ? and tsc.shift_id=? and tsc.updated_by=? and sm.shift_id=tsc.shift_id and tum.user_id =tsc.attendant_id\r\n"
						+ "and \r\n"
						+ "tum2.user_id =tsc.updated_by;",
				con);
	}

	public LinkedHashMap<String, String> getVaultData(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
		parameters.add(hm.get("suggestedShiftId"));
		parameters.add(hm.get("user_id"));

		return getMap(parameters,
				"select \r\n"
						+ "SUM(notes+coins) as vaultSum \r\n"
						+ "from trn_cash_to_vault tctv  \r\n"
						+ "where accounting_date between ? and ? and shift_id=? and supervisor_id=? ;",
				con);
	}

	public List<LinkedHashMap<String, Object>> getItemList(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_items where app_id=? and activate_flag=1",
				con);
	}

	public long insertIntoItemHistory(String itemId, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(itemId);

		return insertUpdateDuablDB(
				"insert into hst_mst_items select * from mst_items where item_id=?", parameters,
				con);
	}

	public LinkedHashMap<String, String> getPaytmOrderDetails(String order_id, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(order_id);

		return getMap(parameters,
				"select * from trn_incoming_online_payments tiop where order_id =? ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getSupervisorCollection(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		String query = "select \r\n"
				+ "*,\r\n"
				+ "tum.name as AttendantName,shift_name,tum2.name as SupervisorName\r\n"
				+ "from trn_supervisor_collection tsc,shift_master sm ,tbl_user_mst tum  ,tbl_user_mst tum2  \r\n"
				+ "where tsc.activate_flag=1 and tsc.app_id=? and collection_date  between ? and ? and sm.shift_id=tsc.shift_id and tum.user_id =tsc.attendant_id\r\n"
				+ "and \r\n"
				+ "tum2.user_id =tsc.updated_by";

		if (hm.get("collection_mode") != null && !hm.get("collection_mode").equals("")) {
			query += " and tsc.collection_mode=?";
			parameters.add(hm.get("collection_mode"));
		}
		if (hm.get("attendantId") != null && !hm.get("attendantId").equals("")) {
			query += " and tsc.attendant_id=?";
			parameters.add(hm.get("attendantId"));
		}

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);
	}

	public List<LinkedHashMap<String, Object>> getPaytmTransaction(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		String query = "select * from trn_incoming_online_payments tsc where accepted_shift_id is not null and date(date_time_from_payment) between ? and ? ";

		if (hm.get("attendantId") != null && !hm.get("attendantId").equals("")) {
			query += " and tsc.claimed_by_user_id=?";
			parameters.add((hm.get("attendantId").toString()));

		}
		query += " order by date_time_from_payment";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public LinkedHashMap<String, String> getQrCodeDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("qr_id"));

		return getMap(parameters,
				"select * from mst_qr_code where qr_id=? ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getQrCodeMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_qr_code qr, tbl_user_mst tum where qr.app_id=? and qr.activate_flag=1 and tum.user_id=qr.currently_assigned_to",
				con);

	}

	public String deleteQrCode(long dispenserId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(dispenserId);
		insertUpdateDuablDB("UPDATE mst_qr_code  SET activate_flag=0,updated_date=SYSDATE() WHERE qr_id=?",
				parameters, conWithF);
		return "QrCode Deleted Succesfully";
	}

	public String updateQrCode(long qrId, String qrCodeNumber, String userId, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(qrCodeNumber);
		parameters.add(userId);
		parameters.add(qrId);

		insertUpdateDuablDB("update mst_qr_code set qr_code_number=?,updated_by=?,updated_date=sysdate() where qr_id=?",
				parameters, con);
		return "Dispenser Updated Succesfully";
	}

	public long addQrCode(Connection conWithF, HashMap<String, Object> hm) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("qr_code_number"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("CurrentlyAssignedTo"));

		parameters.add(hm.get("user_id"));

		String insertQuery = "insert into mst_qr_code values (default,?,?,?,?,1) ";

		return insertUpdateDuablDB(insertQuery, parameters, conWithF);

	}

	public long savePaymentToDB(HashMap<String, String> hm, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("orderId"));
		parameters.add(hm.get("bhimupiid"));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("PaymentDate"));
		parameters.add(hm.get("currently_assigned_to"));
		parameters.add(hm.get("StoreName"));

		String insertQuery = "replace into trn_incoming_online_payments values (?,?,?,?,sysdate(),?,208,null,null,?) ";

		return insertUpdateDuablDB(insertQuery, parameters, con);

	}

	public HashMap<String, String> getuserIdFromStoreName(HashMap<String, String> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("StoreName"));
		return getMap(parameters, "select currently_assigned_to from mst_qr_code where qr_code_number=?", con);

	}

	public List<LinkedHashMap<String, Object>> getPhonePePayments(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {

		String query = "select *,concat(SUBSTRING(order_id ,1,length(order_id)/2) ,' ', \r\n"
				+ "SUBSTRING(order_id ,length(order_id)/2 +1,length(order_id))) orderIdWithSpace,concat(SUBSTRING(bhim_upi_id ,1,length(order_id)/2) ,' ', \r\n"
				+ "SUBSTRING(bhim_upi_id ,length(order_id)/2 +1,length(bhim_upi_id))) bhimupiidwithspace from trn_incoming_online_payments where claimed_by_user_id=? and date(date_time_from_payment) between ? and ? ";

		if (!hm.get("chkaccepted").equals("true")) {
			query += "and accepted_shift_id is null  ";
		}

		query += "order by date_time_from_payment desc";

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("user_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		return getListOfLinkedHashHashMap(parameters,
				query,
				con);

	}

	public HashMap<String, String> getAccountinDateAndShiftId(String userId, Connection con) throws SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		return getMap(parameters,
				"select DATE_FORMAT(accounting_date,'%d/%m/%Y') AS niceDate,shift_id from trn_nozzle_register tnr where check_out_time is null and attendant_id =?",
				con);

	}

	public void updatePhonePayPayment(String orderId, String accounting_date, String shiftId, Connection con)
			throws ParseException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(accounting_date));
		parameters.add(shiftId);
		parameters.add(orderId);

		insertUpdateDuablDB("update trn_incoming_online_payments set shift_date=?,accepted_shift_id=? where order_id=?",
				parameters, con);

	}

	public String resetPassword(String employeeId, Connection conWithF) throws Exception {

		changePasswordById(employeeId, "123", conWithF);
		return "Password Reset Succesfully";
	}

	public void changePasswordById(String userId, String newPassword, Connection con) throws Exception {
		try {
			String insertTableSQL = "UPDATE tbl_user_mst set password=?,updated_date=sysdate() WHERE user_id=?";

			PreparedStatement preparedStatement = con.prepareStatement(insertTableSQL);
			preparedStatement.setString(1, getSHA256String(newPassword));
			preparedStatement.setString(2, userId);
			preparedStatement.executeUpdate();

			if (preparedStatement != null) {
				preparedStatement.close();
			}

		} catch (Exception e) {
			// write to error log
			writeErrorToDB(e);
			throw e;
		}
	}

	public List<LinkedHashMap<String, Object>> getBankMaster(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from mst_bank where activate_flag=1 and app_id=?",
				con);
	}

	public LinkedHashMap<String, String> getBankDetail(HashMap<String, Object> hm, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("bank_id"));

		return getMap(parameters,
				"select * from mst_bank where bank_id=?",
				con);
	}

	public long addBank(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("bank_name"));
		parameters.add(hm.get("account_no"));
		parameters.add(hm.get("ifsc_code"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));

		return insertUpdateDuablDB("insert into mst_bank values (default,?,?,?,1,?,sysdate(),?)", parameters,
				con);
	}

	public String updateBank(HashMap<String, Object> hm, Connection con) throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("bank_name"));
		parameters.add(hm.get("account_no"));
		parameters.add(hm.get("ifsc_code"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("hdnBankId"));

		insertUpdateDuablDB(
				"UPDATE mst_bank SET bank_name=?,account_no=?,ifsc_code=?,updated_date=SYSDATE(),updated_by=?"
						+ " WHERE bank_id=?",

				parameters, con);
		return "Bank updated Succesfully";

	}

	public String deletebank(long bankId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(bankId);
		insertUpdateDuablDB("UPDATE mst_bank SET activate_flag=0,updated_date=SYSDATE() WHERE bank_id=?",
				parameters, conWithF);
		return "Bank Deleted Succesfully";
	}

	public LinkedHashMap<String, String> getBankReconcilationDetail(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("reconcilation_id"));

		return getMap(parameters,
				"select * from trn_bank_reconcilation where reconcilation_id=?",
				con);
	}

	public long addBankReconcilation(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("drpBankId"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtreconcilationdate").toString()));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("user_id"));

		return insertUpdateDuablDB("insert into trn_bank_reconcilation values (default,?,?,?,1,?,sysdate())",
				parameters,
				con);
	}

	public String updateBankReconcilation(HashMap<String, Object> hm, Connection con) throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("drpBankId"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtreconcilationdate").toString()));
		parameters.add(hm.get("amount"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("hdnReconcilationId"));

		insertUpdateDuablDB(
				"UPDATE trn_bank_reconcilation SET bank_account_id=?,reconcilation_date=?,amount=?,updated_date=SYSDATE(),updated_by=?"
						+ " WHERE reconcilatio_id=?",

				parameters, con);
		return "Bank updated Succesfully";

	}

	public String deletebankReconcilation(long reconcilationId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(reconcilationId);
		insertUpdateDuablDB(
				"UPDATE trn_bank_reconcilation SET activate_flag=0,updated_date=SYSDATE() WHERE reconcilatio_id=?",
				parameters, conWithF);
		return "Bank Deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getReconcilationRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		String query = "select * from rlt_settelment_register rsr,\r\n" + //
				"trn_invoice_register tir,tbl_user_mst tum \r\n" + //
				"where tir.invoice_id =rsr.invoice_id \r\n" + //
				"and tir.invoice_date between ? and ? and tum.user_id=rsr.updated_by";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public String deleteReconcilation(long reconcilation_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(reconcilation_id);
		insertUpdateDuablDB(
				"UPDATE trn_bank_reconcilation tbr SET activate_flag=0 and updated_date=sysdate() where reconcilation_id=?",
				parameters, conWithF);
		return "Reconcilation Deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getAttendantsForDateAndShift(String collectiondate, String shiftId,
			Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(collectiondate));
		parameters.add(shiftId);
		return getListOfLinkedHashHashMap(parameters,
				"select distinct(username),user_id from trn_nozzle_register tnr,tbl_user_mst tum where accounting_date=? and shift_id =? and tum.user_id=tnr.attendant_id",
				con);
	}

	// public List<LinkedHashMap<String, Object>> getCheckinRegister(HashMap<String, Object> hm, Connection con, Object nozzleId)
	// 				throws ClassNotFoundException, SQLException, ParseException {
	// 			ArrayList<Object> parameters = new ArrayList<>();

				

	// 			parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
	// 		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
	// 			parameters.add(hm.get("app_id"));
		
	// 			String query="select *,tum.name attendantName,tum2.name superVisorName" +
	// " from trn_nozzle_register tnr, shift_master shft,nozzle_master nm , mst_items fm, tbl_user_mst tum"+
	// " left outer join on tbl_user_mst tum ON tum.user_id = tnr.attendant_id" + 
	// " left outer join on tbl_user_mst tum2 ON tum2 user_id=tnr.updated_by"+
	// " left outer join on shift_master shft ON tnr.shift_id = shft.shift_id"+
	// " left outer join on nozzle_master nm ON nm.nozzle_id = tnr.nozzle_id"+
	// " left outer join on mst_items fm ON fm.item_id = nm.item_id" + 
	// " where accounting_date between ? and ?" + 
	// "and tnr.activate_flag = 1 "  + 
	// "and tnr.app_id = ?";




	// 						if(hm.get("nozzleId")!=null && !hm.get("nozzleId").equals("-1") && !hm.get("nozzleId").equals(""))
	// 						{

	// 							parameters.add(hm.get("nozzleId"));

	// 							query += " and nm.nozzle_id = ?";
								
	// 						}
	// 						query += " Order by tnr.accounting_date desc, nm.nozzle_name,shift_name ";

	// 						return getListOfLinkedHashHashMap(parameters, query, con);
	// 					}


						public List<LinkedHashMap<String, Object>> getCheckinRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));
		parameters.add(hm.get("app_id"));

		String query="select *,tum.name attendantName,tum2.name superVisorName from trn_nozzle_register tnr,tbl_user_mst tum,tbl_user_mst tum2,shift_master shft,nozzle_master nm,mst_items fm "
						+ " where accounting_date between ? and ? and tnr.activate_flag=1 and tnr.app_id=? and tnr.shift_id=shft.shift_id and "
						+ "tum.user_id=tnr.attendant_id and tum2.user_id=tnr.updated_by and nm.nozzle_id=tnr.nozzle_id and fm.item_id=nm.item_id ";

						if(hm.get("nozzle_id")!=null && !hm.get("nozzle_id").equals("-1") && !hm.get("nozzle_id").equals(""))
						{

							parameters.add(hm.get("nozzle_id"));

							query += " and nm.nozzle_id = ?";
							
						}
						query += " Order by tnr.accounting_date desc, nm.nozzle_name,shift_name ";	

		return getListOfLinkedHashHashMap(parameters,
		query,
				con);
	}
						
					

	
	
					



	public String deleteCheckin(long nozzle_id, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(nozzle_id);
		insertUpdateDuablDB("Delete from  trn_nozzle_register where trn_nozzle_id=?",
				parameters, conWithF);
		return "Checkin Deleted Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getAttendantsForDateAndShiftUnclubbed(String collectiondate,
			String shiftId, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(collectiondate));
		parameters.add(shiftId);
		return getListOfLinkedHashHashMap(parameters, "select\r\n"
				+ "	username,name,\r\n"
				+ "	user_id,\r\n"

				+ "	nozzle_name,tnr.nozzle_id,item.item_name \r\n"
				+ "from\r\n"
				+ "	trn_nozzle_register tnr,\r\n"
				+ "	tbl_user_mst tum,\r\n"
				+ "	nozzle_master nm,mst_items item \r\n"
				+ "where\r\n"
				+ "	accounting_date = ?\r\n"
				+ "	and shift_id = ?\r\n"
				+ "	and tum.user_id = tnr.attendant_id\r\n"
				+ "	and nm.nozzle_id =tnr.nozzle_id and item.item_id=nm.item_id order by nozzle_name,name", con);
	}

	public List<LinkedHashMap<String, Object>> getRecondata(String reconcilationDate, String bankId, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(reconcilationDate));
		parameters.add(bankId);
		return getListOfLinkedHashHashMap(parameters, "select mb.bank_name ,\r\n" + //
				"smm.swipe_machine_name ,\r\n" + //
				"tir.total_amount,\r\n" + //
				"rifd.slot_id,\r\n" + //
				"tir.activate_flag,\r\n" + //
				"tir.invoice_id,\r\n" + //
				"mb.bank_id,rsr.invoice_id\r\n" + //
				"from \r\n" + //
				"rlt_invoice_fuel_details rifd,\r\n" + //
				"swipe_machine_master smm,\r\n" + //
				"mst_bank mb ,\r\n" + //
				"trn_invoice_register tir\r\n" + //
				"left outer join rlt_settelment_register rsr on tir.invoice_id =rsr.invoice_id \r\n" + //
				"where tir.activate_flag =1 and tir.app_id =208 and invoice_date =?  and rifd.invoice_id =tir.invoice_id \r\n"
				+ //
				"and smm.swipe_machine_id =rifd.swipe_id \r\n" + //
				"and mb.bank_id =smm.account_id and tir.payment_type !='Pending' and mb.bank_id =? and rsr .invoice_id is null\r\n"
				+ //
				"order by bank_name,slot_id,smm.swipe_machine_name,tir.updated_date ;\r\n" + //
				"", con);
	}

	public List<LinkedHashMap<String, Object>> getRecondataForPaytm(String reconcilationDate, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(getDateASYYYYMMDD(reconcilationDate));
		return getListOfLinkedHashHashMap(parameters, "select\r\n" + //
				"\tcollection_id invoice_id ,\r\n" + //
				"\tamount total_amount,\r\n" + //
				"\tslot_id,\r\n" + //
				"\t'Paytm' swipe_machine_name\r\n" + //
				"from\r\n" + //
				"\ttrn_supervisor_collection tsc left outer join rlt_settelment_register rsr on rsr.invoice_id=tsc.collection_id \r\n"
				+ //
				"\tand rsr.settelment_type='Paytm'\r\n" + //
				"where\r\n" + //
				"\tcollection_mode = 'paytm' \r\n" + //
				"\tand collection_date =? \r\n" + //
				"\tand tsc.activate_flag = 1 and rsr.settelment_id is null", con);
	}

	public LinkedHashMap<String, Object> getRltInvoiceDetails(String invoiceId, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		return getMapReturnObject(parameters, "select * from rlt_invoice_fuel_details where invoice_id=?", con);
	}

	public long saveToTrnInvoiceRegister(LinkedHashMap<String, Object> hm, Connection con)
			throws NumberFormatException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();

		String invoiceNo;
		if (hm.get("invoice_no") == null) {
			invoiceNo = String
					.valueOf(getPkForThistable("trn_invoice_register", Long.valueOf(hm.get("app_id").toString()), con));
		} else {
			invoiceNo = hm.get("invoice_no").toString();
		}

		parameters.add(hm.get("customer_id"));
		parameters.add(hm.get("amountModify")); // gross amount
		parameters.add(hm.get("item_discount"));
		parameters.add(hm.get("invoice_discount"));
		parameters.add(hm.get("amountModify")); // total_amount
		parameters.add(hm.get("payment_type"));

		parameters.add(getDateASYYYYMMDD(hm.get("theInvoiceDate").toString()));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("remarks"));
		parameters.add(hm.get("app_id"));

		parameters.add(invoiceNo);
		parameters.add(hm.get("total_gst"));

		parameters.add(hm.get("model_no"));
		parameters.add(hm.get("unique_no"));

		parameters.add(hm.get("total_sgst"));
		parameters.add(hm.get("total_cgst"));

		long invoiceId = insertUpdateDuablDB(
				"insert into trn_invoice_register values (default,?,?,?,?,?,?,?,?,sysdate(),1,?,?,?,?,?,?,?,?,?)",
				parameters,
				con);
		return invoiceId;
	}

	public long saveToRltInvoiceDetails(LinkedHashMap<String, Object> hm, Connection con)
			throws NumberFormatException, SQLException, ParseException {

		return insertUpdateCustomParameterized(
				"insert into rlt_invoice_fuel_details values (default,:invoice_id,:shift_id,:attendant_id,:nozzle_id,sysdate(),:swipe_id,:slot_id)",
				hm, con);

	}

	public Object settleThistransaction(String invoice_id, String updatedby, String trnansactiontype, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoice_id);
		parameters.add(updatedby);
		parameters.add(trnansactiontype);
		String insertQuery = "insert into rlt_settelment_register values (default,?,?,sysdate(),1,?)";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public List<String> getListOfUniqueVehicleName(Connection con, String appId)
			throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		return getListOfString(parameters,
				"select distinct(vehicle_name) from rlt_invoice_battery_details where app_id=?", con);
	}

	public List<LinkedHashMap<String, Object>> getItemDetailsUsingAppShortCode(HashMap<String, Object> outputMap,
			Connection con) throws ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(outputMap.get("app_short_code"));

		return getListOfLinkedHashHashMap(parameters, "select item.*,cat.*,app.*, \r\n" +
				" case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath \r\n"
				+
				" from mst_items item inner join mst_category cat on cat.category_id=item.parent_category_id left outer join  \r\n"
				+
				" tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' \r\n" +
				" join mst_app app on app.app_id=item.app_id and app.app_short_code=? \r\n" +
				" where item.activate_flag=1 and cat.app_id=item.app_id ", con);

	}

	public long saveItemRestaurant(HashMap<String, Object> hm, Connection con) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("item_id", "~default");
		valuesMap.put("parent_category_id", hm.get("drpcategoryId"));
		valuesMap.put("debit_in", "N");
		valuesMap.put("item_name", hm.get("itemname"));
		valuesMap.put("price", hm.get("itemsaleprice"));
		valuesMap.put("activate_flag", "1");
		valuesMap.put("updated_by", hm.get("userId"));
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("product_code", hm.get("product_code"));
		valuesMap.put("app_id", hm.get("app_id"));

		Query q = new Query("mst_items", "insert", valuesMap);
		return insertUpdateEnhanced(q, con);
	}

	public List<LinkedHashMap<String, Object>> getCashtovaultRegister(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select *,date_format(accounting_date, '%d/%m/%Y')accountingDate from trn_cash_to_vault where accounting_date between ? and ? and is_verified=0";
		parameters.add(getDateASYYYYMMDD((String) hm.get("txtfromdate")));
		parameters.add(getDateASYYYYMMDD((String) hm.get("txttodate")));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public long addPaymentFromEmployee(HashMap<String, Object> hm, Connection conWithF) throws Exception {

		HashMap<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("employee_payment_id", "~default");
		valuesMap.put("employee_id", hm.get("employee_id"));
		valuesMap.put("payment_date", getDateASYYYYMMDD(hm.get("invoice_date").toString()));
		valuesMap.put("payment_mode", hm.get("payment_mode"));
		valuesMap.put("amount", hm.get("total_amount"));
		valuesMap.put("payment_type", hm.get("payment_type"));
		valuesMap.put("remarks", hm.get("remarks"));
		valuesMap.put("app_id", hm.get("app_id"));
		valuesMap.put("updated_by", hm.get("user_id"));
		valuesMap.put("updated_date", "~sysdate()");
		valuesMap.put("activate_flag", "~1");
		Query q = new Query("trn_employee_payment_register", "insert", valuesMap);
		return insertUpdateEnhanced(q, conWithF);

	}

	public List<LinkedHashMap<String, Object>> getDeletedInvoiceRegister(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select *,invoice_no,date_format(inv.invoice_date,'%d/%m/%Y') as FormattedInvoiceDate,date_format(inv.updated_date,'%d/%m/%Y %H:%i:%s') as updatedDate,inv.activate_flag isActive from trn_invoice_register inv"
				+ " left outer join mst_customer cust on inv.customer_id=cust.customer_id and inv.app_id=cust.app_id  "
				+ "left outer join tbl_user_mst usertbl on inv.updated_by = usertbl.user_id "
				+ " left outer join trn_payment_register paymnt on inv.invoice_id =paymnt.ref_id and paymnt.activate_flag=1 and paymnt.payment_for='invoice' and paymnt.app_id = inv.app_id "
				+ " inner join mst_store store1 on inv.store_id=store1.store_id left outer join rlt_invoice_fuel_details rifd on rifd.invoice_id = inv.invoice_id left outer join trn_invoice_details tid on tid.invoice_id=inv.invoice_id left outer join rlt_invoice_battery_details ribd on ribd.details_id=tid.details_id "
				+ "where  "
				+ " usertbl.app_id=inv.app_id and store1.app_id=inv.app_id and inv.invoice_id in  ("
				+ "	select max(invoice_id)"
				+ "	from trn_invoice_register tir where invoice_date between ? and ? "
				+ "	and app_id =?"
				+ "	group by invoice_no ) and inv.activate_flag=0";
		parameters.add(getDateASYYYYMMDD((String) hm.get("txtfromdate")));
		parameters.add(getDateASYYYYMMDD((String) hm.get("txttodate")));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public LinkedHashMap<String, String> getLastMonthSalesForThisCustomer(long customerId, Connection con)
			throws SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select\n" +
				"sum(total_amount) lstSalesAmount\n" +
				"from\n" +
				"trn_invoice_register tir\n" +
				"where\n" +
				" customer_id =? and tir.activate_flag =1 and tir.invoice_date between DATE_SUB(NOW(), INTERVAL 1 MONTH) and sysdate()\n";

		parameters.add(customerId);

		return getMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getEmployeePaymentRegister(HashMap<String, Object> hm, Connection con)
			throws ParseException, ClassNotFoundException, SQLException {

		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select * from trn_employee_payment_register tepr where payment_date between ? and ? and app_id=? and activate_flag=1";
		parameters.add(getDateASYYYYMMDD((String) hm.get("txtfromdate")));
		parameters.add(getDateASYYYYMMDD((String) hm.get("txttodate")));
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public String receivedCashtovault(long submissionId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(userId);
		parameters.add(submissionId);
		insertUpdateDuablDB(
				"UPDATE trn_cash_to_vault SET is_verified=1,updated_by=?,updated_date=sysdate() WHERE submission_id=?",
				parameters, conWithF);
		return "Vault Received Succesfully";
	}

	public List<LinkedHashMap<String, Object>> getItemMasterFuel(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String query = "select item.*,cat.*,stock.*,"
				+ " case when concat(attachment_id, file_name) is null then 'dummyImage.jpg' else concat(attachment_id, file_name) end as ImagePath "
				+ "from mst_items item inner join mst_category cat on cat.category_id=item.parent_category_id left outer join "
				+ " tbl_attachment_mst tam on tam.file_id=item.item_id and tam.type='Image' "
				+ " left outer join stock_status stock on stock.item_id=item.item_id and stock.store_id=? "
				+ " where item.activate_flag=1 and item.app_id=? and cat.app_id=item.app_id and cat.category_name='fuel'";

		parameters.add(hm.get("store_id"));
		parameters.add(hm.get("app_id"));

		if (hm.get("searchInput") != null && !hm.get("searchInput").equals("")) {
			parameters.add("%" + hm.get("searchInput") + "%");
			parameters.add("%" + hm.get("searchInput") + "%");
			query += " and (product_code like ? or item_name like ?)";
		}

		if (hm.get("categoryId") != null && !hm.get("categoryId").equals("-1") && !hm.get("categoryId").equals("")) {
			parameters.add(hm.get("categoryId"));
			query += " and parent_category_id=? ";
		}
		query += " group by item.item_id";
		query += " order by item_name";
		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getVehicleListForQr(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));

		String query = "select * from mst_vehicle mv , mst_customer mc where mv.customer_id =mc.customer_id and mv.activate_flag =1 and mc.app_id=?";

		return getListOfLinkedHashHashMap(parameters, query, con);

	}

	public List<LinkedHashMap<String, Object>> getRawMaterialMaster(HashMap<String, Object> hm, Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		return getListOfLinkedHashHashMap(parameters,
				"select * from raw_material_master where activate_flag=1  and app_id=?",
				con);
	}

	public LinkedHashMap<String, String> getRawMaterialDetails(HashMap<String, Object> hm, Connection con)
			throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("raw_material_id"));

		return getMap(parameters,
				"select * from raw_material_master where raw_material_id=?",
				con);
	}

	public long addRawMaterial(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		String query = "insert into raw_material_master values (default,?,1,?,sysdate(),?,?)";
		parameters.add(hm.get("txtrawmaterialname"));
		parameters.add(hm.get("user_id"));
		parameters.add(hm.get("app_id"));
		parameters.add(hm.get("txtboraperbag"));


		return insertUpdateDuablDB(query, parameters,
				con);
	}


	public String updateRawMaterial(long rawmaterialId, Connection con, String rawmaterialName, String updatedBy, String bora_per_bag)
			throws Exception {

		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(rawmaterialName);
		parameters.add(updatedBy);
		parameters.add(bora_per_bag);

		parameters.add(rawmaterialId);

		
		insertUpdateDuablDB(
				"UPDATE raw_material_master SET raw_material_name=?,updated_date=SYSDATE(),updated_by=?,bora_per_bag=? WHERE raw_material_id=?",
				parameters, con);
		return "Raw Material updated Succesfully";

	}
	

	public String deleteRawMaterial(long rawmaterialId, String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(userId);
		parameters.add(rawmaterialId);
		insertUpdateDuablDB(
				"UPDATE raw_material_master  SET activate_flag=0,updated_date=SYSDATE(),updated_by=? WHERE raw_material_id=?",
				parameters, conWithF);
		return "Raw Material updated Succesfully";
	}

	public long saveSnacksInvoice(HashMap<String, Object> hm, Connection con) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("invoice_id"));
		parameters.add(hm.get("curr_status"));

		String insertQuery = "INSERT INTO snacks_invoice_status\r\n"
				+ "VALUES(default, ?, ?);";

		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	// public List<LinkedHashMap<String, Object>> getInwardRegister(String
	// fromDate,String toDate,Connection con, List<LinkedHashMap<String, Object>>
	// hm)
	// throws SQLException, ClassNotFoundException, ParseException {

	// ArrayList<Object> parameters = new ArrayList<>();
	// parameters.add((getDateASYYYYMMDD(fromDate)));
	// parameters.add((getDateASYYYYMMDD(toDate)));
	// parameters.add(hm.get("app_id"));

	// return getListOfLinkedHashHashMap(parameters,
	// "select * from trn_invoice_register tir left outer join snacks_invoice_status
	// sis ON tir.invoice_id = sis.invoice_id WHERE tir.app_id = ? AND
	// sis.curr_status = 0 AND tir.invoice_date BETWEEN ? AND ?",

	// con);
	// }

	public List<LinkedHashMap<String, Object>> getPendingRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));

		String query = "select *,Round(sum(qty),0)  totalQty from trn_invoice_register tir " +
				"left outer join snacks_invoice_status sis ON tir.invoice_id = sis.invoice_id " +
				"left outer join trn_invoice_details tid ON tir.invoice_id = tid.invoice_id " +
				"left outer join mst_customer cust ON cust.customer_id = tir.customer_id " +
				"left outer join mst_items mi ON mi.item_id = tid.item_id " +
				"WHERE tir.app_id = ?  AND sis.curr_status = 0 AND tir.activate_flag=1 ";
		if (hm.get("user_id") != null) {
			query += " and tir.updated_by=? ";
			parameters.add(hm.get("user_id"));
		}
		query += " group by tir.invoice_id ";

		return getListOfLinkedHashHashMap(parameters, query, con);
	}

	public List<LinkedHashMap<String, Object>> getPlanningRegister(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));

		return getListOfLinkedHashHashMap(parameters,
				"select *,round(sum(qty),0) totalQty from trn_invoice_register tir " +
						"left outer join snacks_invoice_status sis ON tir.invoice_id = sis.invoice_id " +
						"left outer join trn_invoice_details tid ON tir.invoice_id = tid.invoice_id " +
						"left outer join mst_customer cust ON cust.customer_id = tir.customer_id " +
						"left outer join mst_items mi ON mi.item_id = tid.item_id " +
						"WHERE tir.app_id = ?  AND sis.curr_status = 1  AND tir.activate_flag=1 group by tir.invoice_id ",

				con);
	}

	public List<LinkedHashMap<String, Object>> getCompletedOrders(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		

		return getListOfLinkedHashHashMap(parameters,
				"select *,sum(qty) totalQty from trn_invoice_register tir " +
						"left outer join snacks_invoice_status sis ON tir.invoice_id = sis.invoice_id " +
						"left outer join trn_invoice_details tid ON tir.invoice_id = tid.invoice_id " +
						"left outer join mst_customer cust ON cust.customer_id = tir.customer_id " +
						"left outer join mst_items mi ON mi.item_id = tid.item_id " +
						"WHERE tir.app_id = ?  AND sis.curr_status = 2 AND tir.activate_flag=1 group by tir.invoice_id ",

				con);
	}

	public List<LinkedHashMap<String, Object>> getOrderHistory(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtfromdate").toString()));
		parameters.add(getDateASYYYYMMDD(hm.get("txttodate").toString()));

		return getListOfLinkedHashHashMap(parameters,
				"select *,sum(qty) totalQty from trn_invoice_register tir " +
						"left outer join snacks_invoice_status sis ON tir.invoice_id = sis.invoice_id " +
						"left outer join trn_invoice_details tid ON tir.invoice_id = tid.invoice_id " +
						"left outer join mst_customer cust ON cust.customer_id = tir.customer_id " +
						"WHERE tir.app_id = ?   AND tir.invoice_date BETWEEN ? AND ? group by tir.invoice_id AND tir.activate_flag=1",

				con);
	}

	public long moveToPending(String invoiceId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		String insertQuery = "update snacks_invoice_status set curr_status=0 where invoice_id=?";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public long moveToPlanning(String invoiceId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		String insertQuery = "update snacks_invoice_status set curr_status=1 where invoice_id=?";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public long moveToDone(String invoiceId, Connection con) throws SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(invoiceId);
		String insertQuery = "update snacks_invoice_status set curr_status=2 where invoice_id=?";
		return insertUpdateDuablDB(insertQuery, parameters, con);
	}

	public void saveTodaysStock(int packaging_type, List<HashMap<String, Object>> itemListRequired, Connection con)
        throws SQLException, ParseException {

    ArrayList<Object> parameters = new ArrayList<>();

    // Delete existing stock
    String deleteQuery = "DELETE ttss " +
                        "FROM trn_todays_stock_snacks ttss " +
                        "INNER JOIN mst_items mi ON mi.item_id = ttss.item_id " +
                        "WHERE mi.packaging_type = ?";

    parameters.add(packaging_type);
    insertUpdateDuablDB(deleteQuery, parameters, con);

    // Insert new stock items
    for (HashMap<String, Object> hm : itemListRequired) {
        parameters = new ArrayList<>();
        parameters.add(hm.get("item_id"));
        parameters.add(hm.get("qty"));
        String insertQuery = "insert into trn_todays_stock_snacks values (default,?,?)";
        insertUpdateDuablDB(insertQuery, parameters, con);
    }
}

public void saveRMStock(List<HashMap<String, Object>> itemListRequired, Connection con)
        throws SQLException, ParseException {

    ArrayList<Object> parameters = new ArrayList<>();

    // Delete existing stock
    String deleteQuery = "DELETE ttrss " +
                        "FROM trn_todays_rm_stock_snacks ttrss " +
                        "INNER JOIN raw_material_master rmm ON rmm.raw_material_id = ttrss.raw_material_id ";

    insertUpdateDuablDB(deleteQuery, parameters, con);

    // Insert new stock items
    for (HashMap<String, Object> hm : itemListRequired) {
        parameters = new ArrayList<>();
        parameters.add(hm.get("raw_material_id"));
        parameters.add(hm.get("qty"));
        String insertQuery = "insert into trn_todays_rm_stock_snacks values (default,?,?)";
        insertUpdateDuablDB(insertQuery, parameters, con);
    }
}

	public List<LinkedHashMap<String, Object>> getTodaysStockRegister(String toDate, Connection con)
			throws SQLException, ClassNotFoundException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add((getDateASYYYYMMDD(toDate)));
		return getListOfLinkedHashHashMap(parameters,
				"select * from trn_todays_stock_snacks ttss,mst_items mi where mi.item_id=ttss.item_id  and stock_date=?",

				con);
	}

	public List<LinkedHashMap<String, Object>> getLoadingRegister(String fromDate, Connection con)
	throws SQLException, ClassNotFoundException, ParseException {
ArrayList<Object> parameters = new ArrayList<>();
parameters.add((getDateASYYYYMMDD(fromDate)));

return getListOfLinkedHashHashMap(parameters,
		"select *,case when is_loading_complete = '0' then 'In Progress' else 'Completed' end as LoadingStatus from trn_loading_register tlr,mst_vehicle mv  where loading_date =? and mv.vehicle_id=tlr.vehicle_id ",
		con);
}

	public List<LinkedHashMap<String, Object>> getCustomersListForPlanning(Connection con, String appId,String[] invoiceIds)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		String questionMarks = "";
		for (String s : invoiceIds) {
			parameters.add(s);
			questionMarks += "?,";
		}
		questionMarks = questionMarks.substring(0, questionMarks.length() - 1);
		parameters.add(appId);
		return getListOfLinkedHashHashMap(parameters,
				"select city,mc.customer_id,customer_name  from trn_invoice_register tir " +
						"inner join mst_customer mc on mc.customer_id =tir.customer_id \r\n" + //
						"inner join snacks_invoice_status sis on sis.invoice_id =tir.invoice_id \r\n" + //
						"where  sis.curr_status=1 and tir.activate_flag=1 and tir.invoice_id in ("+questionMarks+") and tir.app_id=? ",
				con);
	}

	public List<LinkedHashMap<String, Object>> getItemsAndStockForThisDate( String appId,String packaging_type,
			Connection conWithF) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);
		parameters.add(packaging_type);

		return getListOfLinkedHashHashMap(parameters,
				"select\n" +
						"item_name,mi.item_id ,ttss.qty\n" +
						"from\n" +
						"mst_items mi\n" +
						"left outer join trn_todays_stock_snacks ttss on\n" +
						"ttss.item_id = mi.item_id  \n" +
						"where \n" +
						" mi.app_id =? and mi.activate_flag=1 and packaging_type=? order by mi.order_no desc",
				conWithF);
	}

	public long addDepositCashToBank(Connection con, HashMap<String, Object> hm) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();

		parameters.add(hm.get("txtaccountid"));
		parameters.add(getDateASYYYYMMDD(hm.get("txtdate").toString()));
		parameters.add(hm.get("txtamount"));
		parameters.add(hm.get("user_id"));
		

		return insertUpdateDuablDB("insert into trn_cash_deposit_to_bank_register values (default,?,?,?,?,sysdate(),1)",
				parameters, con);
	}


	public String deleteCashDepositRegister(long bankId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(bankId);
		insertUpdateDuablDB("UPDATE trn_cash_deposit_to_bank_register SET activate_flag=0,updated_date=SYSDATE() WHERE deposit_id=?",
				parameters, conWithF);
		return "Cash Deposit Deleted Succesfully";
	}

	public long startVehicleLoading(String vehicleId,String userId, Connection conWithF) throws Exception {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(vehicleId);
		parameters.add(userId);
		return insertUpdateDuablDB("insert into trn_loading_register values (default,?,curdate(),1,?,sysdate(),0)", parameters, conWithF);
	}

	public List<LinkedHashMap<String, Object>> getRawMaterialsAndStockForThisDate( String appId,
			Connection conWithF) throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(appId);

		return getListOfLinkedHashHashMap(parameters,
				"select\n" +
						"raw_material_name,rmm.raw_material_id ,ttrss.qty\n" +
						"from\n" +
						"raw_material_master rmm\n" +
						"left outer join trn_todays_rm_stock_snacks ttrss on\n" +
						"ttrss.raw_material_id = rmm.raw_material_id  \n" +
						"where \n" +
						" rmm.app_id =? and rmm.activate_flag=1  order by rmm.raw_material_name",
				conWithF);
	}

	
			public List<LinkedHashMap<String, Object>> getReplacementRegister(Connection con)
			throws SQLException, ClassNotFoundException {
		ArrayList<Object> parameters = new ArrayList<>();
		return getListOfLinkedHashHashMap(parameters,
				"select\r\n" + //
										"\ttrr.updated_date,trr.replacement_id,mc2.customer_name,tir.invoice_date ,mc.category_name ,mi.item_name ,ried.unique_no ,trr.unique_no replacementNo\r\n" + //
										"from\r\n" + //
										"\ttrn_replacement_register trr ,\r\n" + //
										"\trlt_invoice_electric_details ried,\r\n" + //
										"\ttrn_invoice_details tid ,\r\n" + //
										"\ttrn_invoice_register tir ,\r\n" + //
										"\tmst_items mi ,\r\n" + //
										"\tmst_category mc ,\r\n" + //
										"\tmst_customer mc2 \r\n" + //
										"where\r\n" + //
										"\ttrr.rlt_invoice_id = ried .rlt_invoice_electric_pk\r\n" + //
										"\tand tid.details_id =ried .details_id\r\n" + //
										"\tand tir.invoice_id =tid.invoice_id \r\n" + //
										"\tand mi.item_id =tid.item_id \r\n" + //
										"\tand mi.parent_category_id =mc.category_id \r\n" + //
										"\tand mc2.customer_id =tir.customer_id \r\n" + //
										";\r\n" + //
										"\r\n" + //
										"  ",
				con);
		}

		public LinkedHashMap<String, String> getReplacementDetails(HashMap<String, Object> hm, Connection con) throws SQLException {
			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(hm.get("replacement_id"));
			
			
			return getMap(parameters,
					"select * from trn_replacement_register where replacement_id=?",
					con);
		}


		public long addReplacement(Connection con, HashMap<String, Object> hm) throws Exception {
			ArrayList<Object> parameters = new ArrayList<>();
			
			
			String query="insert into trn_replacement_register values (default,?,?,?,sysdate(),1)";
			parameters.add(hm.get("rltinvoiceid"));
			parameters.add(hm.get("txtinvoiceno"));
			parameters.add(hm.get("user_id"));
			
			
			return insertUpdateDuablDB(query, parameters,
					con);
		}

		
	 
		public String updateReplacement(long replacementId, Connection conWithF, HashMap<String, Object> hm) throws Exception {

			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(hm.get("txtinvoiceno"));
			
	
			parameters.add(replacementId);
	
			insertUpdateDuablDB(
					"UPDATE trn_replacement_register  SET invoice_no=?,updated_date=SYSDATE() WHERE replacement_id=?",
					parameters, conWithF);
			return "Employee Updated Succesfully";
	
		}

		public String deleteReplacement(long replacementId,String userId, Connection conWithF) throws Exception {
			ArrayList<Object> parameters = new ArrayList<>();
			
			parameters.add(userId);
			parameters.add(replacementId);
			insertUpdateDuablDB("UPDATE trn_replacement_register  SET activate_flag=0,updated_date=SYSDATE(),updated_by=? WHERE replacement_id=?",
					parameters, conWithF);
			return "Holiday updated Succesfully";
		}

		public long addStockStatusDirect(Connection conWithF, HashMap<String, Object> hm) throws Exception {
			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(hm.get("hdnselecteditem"));
			parameters.add(getDateASYYYYMMDD(hm.get("txtdate").toString()));			
			parameters.add((hm.get("hdnstocktype").toString()));

			if(hm.get("hdnstocktype").toString().equals("Damage") || hm.get("hdnstocktype").toString().equals("Debit"))
			{
				parameters.add("-"+hm.get("txtqty"));
			}
			else
			{
				parameters.add(hm.get("txtqty"));
			}
			
			parameters.add(hm.get("txtremarks"));
			parameters.add(hm.get("user_id"));
			parameters.add(hm.get("app_id"));
			parameters.add(hm.get("details_id"));


			
			
			String insertQuery = "insert into trn_stock_direct_details values (default,?,?,?,?,?,?,sysdate(),1,?,?)";
			return insertUpdateDuablDB(insertQuery, parameters, conWithF);
		}

		
	

public List<LinkedHashMap<String, Object>> getStockStatusBeverage(String fromDate,String toDate,HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		parameters.add((getDateASYYYYMMDD(fromDate)));
			parameters.add((getDateASYYYYMMDD(toDate)));

		return getListOfLinkedHashHashMap(parameters,
				"select *, date_format(stock_date,'%d/%m/%Y' ) as stock_date from trn_stock_direct_details tsdd,mst_items mi where mi.item_id=tsdd.item_id and tsdd.activate_flag=1 and tsdd.app_id=? and date(tsdd.stock_date) between ? and ?",
				con);
	}




		public String deleteStockStatusDirect(long stockId, String userId, Connection conWithF) throws Exception {
			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(userId);
			parameters.add(stockId);
			insertUpdateDuablDB(
					"UPDATE trn_stock_direct_details SET activate_flag=0,updated_by=?,updated_date=sysdate() WHERE stock_id=?",
					parameters, conWithF);
			return "Stock Deleted Succesfully";
		}

		public List<LinkedHashMap<String, Object>> getStockStatusDirect(HashMap<String, Object> hm, Connection con)
			throws ClassNotFoundException, SQLException, ParseException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(hm.get("app_id"));
		

		return getListOfLinkedHashHashMap(parameters,
				"select\r\n" + //
										"\tmi.item_name ,\r\n" + //
										"\tmc.category_name ,\r\n" + //
										"\tsum(qty) qty_available\r\n" + //
										"from\r\n" + //
										"\ttrn_stock_direct_details tsdd,\r\n" + //
										"\tmst_items mi,\r\n" + //
										"\tmst_category mc \r\n" + //
										"where\r\n" + //
										"\r\n" + //
										"\ttsdd.item_id = mi.item_id\r\n" + //
										"\tand mi.parent_category_id =mc.category_id \r\n" + //
										"\tand tsdd.activate_flag = 1\r\n" + //
										"\tand tsdd.app_id =?\r\n" + //
										"and category_name not like '%fuel%'\r\n" + //
										"\tgroup by item_name" +
										"\t",
				con);
	}


	public LinkedHashMap<String, String> getPaymentDetails(String payment_id, Connection con)
			throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(payment_id);
		return getMap(parameters, "select\r\n" + //
						"\tcustomer_name ,tpr.amount ,tpr.payment_mode ,tpr.ref_id,tpr.payment_for ,mc.mobile_number,tpr.payment_id,tpr.updated_by ,tpr.updated_date ,ms.store_id ,ms.store_name,ms.address_line_1,ms.address_line_2 ,ms.city,ms.pincode \r\n" + //
						"from\r\n" + //
						"\ttrn_payment_register tpr,\r\n" + //
						"\tmst_customer mc ,\r\n" + //
						"\ttbl_user_mst tum, \r\n" + //
						"\tmst_store ms  \r\n" + //
						"where\r\n" + //
						"\tpayment_id = ? and mc.customer_id =tpr.customer_id and tpr.updated_by = tum.user_id and ms.store_id =tpr.store_id ", con);

	}

		public List<LinkedHashMap<String, Object>> getStatesList(Connection con) throws SQLException, ClassNotFoundException 
		{
			
			
			ArrayList<Object> parameters = new ArrayList<>();
			String query="select state_id stateId,state_name stateName from cmn_state_mst";
			return getListOfLinkedHashHashMap(parameters, query, con);		
			
		}

		public LinkedHashMap<String, String> getLoadingDetails(long loadingId, Connection con) throws SQLException {
			ArrayList<Object> parameters = new ArrayList<>();
			parameters.add(Long.valueOf(loadingId));
			return getMap(parameters,
					"select * from trn_loading_register tlr,mst_vehicle mv  where loading_id=? and mv.vehicle_id =tlr.vehicle_id ",
					con);
		}

		public List<LinkedHashMap<String, Object>> getLoadingItemDetails(String loadingId, Connection con) throws ClassNotFoundException, SQLException {
		ArrayList<Object> parameters = new ArrayList<>();
		parameters.add(loadingId);
		parameters.add(loadingId);

		String query="SELECT *\n" + //
						"FROM trn_loading_details tld\n" + //
						"WHERE loading_id = ?\n" + //
						"  AND line_no = (\n" + //
						"      SELECT MAX(line_no)\n" + //
						"      FROM trn_loading_details\n" + //
						"      WHERE loading_id = ?\n" + //
						"  )";
		return getListOfLinkedHashHashMap(parameters,query ,con);
	}

	public String getInProgressLoadingCount(Connection con) throws SQLException, ClassNotFoundException 
	{
		ArrayList<Object> parameters = new ArrayList<>();		
		return getMap(parameters, "select count(*) cnt from trn_loading_register where is_loading_complete=0 ", con).get("cnt");
	}
		



}