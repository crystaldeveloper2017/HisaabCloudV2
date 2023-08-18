package com.crystal.customizedpos.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.crystal.Login.LoginDaoImpl;
import com.crystal.Login.LoginServiceImpl;


import com.crystal.Frameworkpackage.CommonFunctions;

public class ExecuteSqlFile {


	 public static void main(String argv[]) throws SQLException {
		  Connection con=null;
	    try {
			  

		      DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		      String mysqlUrl = CommonFunctions.url+":"+CommonFunctions.port;
		      if(!mysqlUrl.contains("localhost") && !mysqlUrl.contains("mysqldb-container"))
		      {
		    	  System.out.println("seems you are not running for localhost");
				  System.out.println("This feature is only enabled for localhost");				  
		    	  System.exit(0);
		      }
		      con= DriverManager.getConnection(mysqlUrl, CommonFunctions.username, CommonFunctions.password);

			  // Select the database to use
			  	con.setCatalog(CommonFunctions.schemaName);
		      
		      ScriptRunner sr = new ScriptRunner(con);
			  InputStream in = ExecuteSqlFile.class.getResourceAsStream("mybackup.sql");
		      Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		      sr.runScript(reader);
			      
			      ConfigurationDaoImpl lobjConfigDaoimpl=new ConfigurationDaoImpl();
					LoginDaoImpl loginDao= new LoginDaoImpl();
					
					long userId =lobjConfigDaoimpl.saveNewApp("Test App","31/03/2028","Test Store","testadmin","Retail", con);					
					loginDao.changePassword("testadmin", "1", con);
					
					
					lobjConfigDaoimpl.addInvoiceFormats(1,"3InchFormat",  con);
					lobjConfigDaoimpl.addInvoiceFormats(2,"A4FullPageFormat",  con);
					lobjConfigDaoimpl.addInvoiceFormats(3,"2InchFormat",  con);
					lobjConfigDaoimpl.addInvoiceFormats(4,"3InchWithWeightSize",  con);
					lobjConfigDaoimpl.addInvoiceFormats(5,"3InchWithModelNo",  con);
					
					
					lobjConfigDaoimpl.addInvoiceTypes(0,"Retail",  con);
					lobjConfigDaoimpl.addInvoiceTypes(1,"Jwellery Works",  con);
					lobjConfigDaoimpl.addInvoiceTypes(2,"Services",  con);
					lobjConfigDaoimpl.addInvoiceTypes(3,"Retail Mobile",  con);
					lobjConfigDaoimpl.addInvoiceTypes(4,"Specs",  con);
					lobjConfigDaoimpl.addInvoiceTypes(5,"Petrol Pump",  con);
					
					
					
					
					lobjConfigDaoimpl.updateConfigurationForThisUser(1, "N", "N", "N", "N", "N", "N", "N", "N", "N",  "1", String.valueOf(userId), con);
					
					
					
					
					
					con.commit();
					con.close();
			      
			      
			    }
			    catch (Exception err) {
			      err.printStackTrace();
			    }
		    finally
		    {	
		    	if(con!=null)
		    		con.close();
		    }
		    
			}

}
