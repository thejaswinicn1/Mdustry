package config;

public class Constants {
	
	/********************************************************************************************
	 * Secret key for two factor authenticator
	 ********************************************************************************************/
	//public static String OtpKeyStr = "KVZWK4RRGIZTIWLNNZQVCV2OJJLFA6CGMNBWGUQ";
		
	public static String OtpKeyStr= "IFSG22LOIBEGKYLMORUGC43ZON2C4Y3PNVBUUTJTMJCUOVLGONGUEUZQKZDQ";

	/********************************************************************************************
	 * REST API POST CALL - PROCESS INBOUND MESSAGE
	 ********************************************************************************************/
			
	public static String organizationName= "Intergy";
	public static int organizationID= 2;
		
		
	/********************************************************************************************
	 * List all in INITIAL DATA IN PROJECTS
	 ********************************************************************************************/
	public static final String Path_OR2 = ".\\src\\testing\\objectRepository\\MASTEROR.txt";
	public static final String Path_OR9 = ".\\src\\testing\\objectRepository\\MASTER_OR_CA_Team.txt";
	public static final String Path_OR1= ".\\src\\testing\\objectRepository\\MASTER_OR_CA_CloudAdmin.txt";
	public static final String Path_OR= ".\\src\\testing\\objectRepository\\MASTERORAFTERMERGEOCT122018.txt";
	public static final String Path_OR3= ".\\src\\testing\\objectRepository\\MASTER_CA.txt";
	
	

	/********************************************************************************************
	                       
	********************************************************************************************/

	public static final String FailScrnpath = ".\\src\\testing\\reports\\imagesLog\\";

	/*********************************************************************************************
	 * DATABASE CREDENTIALS
	 *********************************************************************************************/

	public static final String DBURL = "jdbc:sqlserver://172.20.30.70;databaseName =7.P1_Automation";
	public static final String userName = "AdminUser";
	public static final String pwd = "AUTODEMO@20005";
	
	
	/*
	 * public static final String DBURL =
	 * "jdbc:sqlserver:// DBSRV01;databaseName =V7.0QA_P12"; public static final
	 * String userName = "patusermirth"; public static final String pwd =
	 * "TQxvoo*wkyp*";
	 */
	
		
	public static final String DBdataFilepath = "src\\testing\\dataEngine\\DBdata.xlsx";
	public static final String DBResultFilepath = "src\\testing\\reports\\DBResults.xlsx";

	/*********************************************************************************************
	 * LOGIN CREDENTAILS
	 *********************************************************************************************/

	public static final String DASHBOARD_URL = "  ";
	public static final String DASHBOARD_UN = "  ";
	public static final String DASHBOARD_PWD = "  ";

	public static final String CHECKIN_URL = "  ";
	public static final String CHECKIN_UN = "  ";
	public static final String CHECKIN_PWD = "  ";

	public static final String ADMIN_URL = "  ";
	public static final String ADMIN_UN = "  ";
	public static final String ADMIN_PWD = "  ";

	public static final String CLOUDADMIN_URL = "  ";
	public static final String CLOUDADMIN_UN = "  ";
	public static final String CLOUDADMIN_PWD = "  ";

	/*********************************************************************************************
	 * TEST CASE PATH
	 *********************************************************************************************/

//	public static final String[] TCRPath= { "./src/testing/dataEngine/MISC1_P13.xls",
//			"./src/testing/reports/TestResult_MISC1_P13.xls", "MISC1_P13.xls"};
//
//	public static final String[] TCRPath1= { "./src/testing/dataEngine/AutoDemo.xls",
//			"./src/testing/reports/TestResult_AutoDemo.xls", "AutoDemo.xls" };
		
	public static final String[] TCRPath = { "./src/testing/dataEngine/Test1.xls",
			"./src/testing/reports/TestResult_Test1.xls", "Test1.xls" };

	/*******************************************************************************************
	 * REPORT FILE PATH
	 ********************************************************************************************/

	public static String Report1 = "D:\\Suites\\dataEngine\\ActionItem.xlsx";
	public static String Report2 = "D:\\\\Suites\\\\dataEngine\\\\ActionItem.xlsx";
	public static String Report3 = "D:\\\\Suites\\\\dataEngine\\\\ActionItem.xlsx";
	public static String Report4 = "D:\\\\Suites\\\\dataEngine\\\\ActionItem.xlsx";
	
		/*******************************************************************************************
	 * EMAIL ADDRESS
	 ********************************************************************************************/

	public static String Receipient1 = "Thyagarajan.v@healthasyst.com";
	
		
	

	/*******************************************************************************************
	 * LIST OF COLUMNS DATA-ENGINE
	 ********************************************************************************************/

	public static final int Col_TestCaseID = 0;
	public static final int Col_TestStepID = 1;
	public static final int Col_PageObject = 3;
	public static final int Col_ActionKeyword = 4;
	public static final int Col_DataSet = 5;

	public static final int Col_RunMode = 2;

	public static final int Col_Result = 3;
	public static final int Col_TestStepResult = 9;

	public static final String KEYWORD_FAIL = "FAILED";
	public static final String KEYWORD_PASS = "PASSED";

	public static final String Sheet_TestCases = "Testsuite";
	public static final String Sheet_TestSteps = "TestCase";

}
