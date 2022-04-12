package support;

import java.lang.reflect.Method;

import config.Constants;

public class ExecuteTestcase {
	public static boolean bResult;
	public static String sRunMode;
	public static String SheetName;
	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String sActionKeyword;
	public static String sPageObject;
	public static String TestStepName;
	public static String sData;
	public static String sData1;
	public static boolean sTestCaseResult;
	public static Method method[];
	public static Keyword actionKeywords;
	public static String testsuiteName;
	public static int Col_DataSet;
	public static int iTotaltestdata;
	public static int iTotaltest;
	public static int iTotaltest1;
	public static int TestStepResult;
	public static int counter = 0;

	public ExecuteTestcase() throws NoSuchMethodException, SecurityException {
		actionKeywords = new Keyword();
		method = actionKeywords.getClass().getMethods();
	}
	// public static WebDriver DRIVER;

	public static void execute_TestCase(String TestSuite, String TestReport, String TestsuiteName) throws Exception {

		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Desc: This function will be used to read TestSuite ~ SheetTestCases.
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// And
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Read
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// each
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// specify
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// TestSteps
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// on
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// each
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Test
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Case
		// => Read TestCases that will be executed (Runmode: Yes).
		// => Read and execute TestCase (Sheets) that named as TestSuite sheet.
		// => TestCases will be Passed only when all steps be executed and
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// passed.
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		testsuiteName = TestsuiteName;
		// System.out.println("********Testsuite name*********"+testsuiteName);
		ExecuteTestcase ext = new ExecuteTestcase();
		ReadWriteExcel.setExcelFile(TestSuite); // This is to start the Log4j
												// logging in the test case
		// This function will find number of rows in a testcase sheet.
		int iTotalTestCases = ReadWriteExcel.getNumberofRow(Constants.Sheet_TestCases);
		// System.out.println("********Starting Test execution
		// now*********"+Constants.Sheet_TestCases);
		// This loop will help to execute more than one test cases in
		// spreadsheet.
		int iTotalTestCase = iTotalTestCases - 1;
		for (int iTestcase = 1; iTestcase <= iTotalTestCase; iTestcase++) {
			// System.out.println("Total number of test cases are
			// "+iTotalTestCase);
			ReadWriteExcel RW = new ReadWriteExcel();
			iTotaltest = RW.getColumnCount(Constants.Sheet_TestSteps);
			bResult = true;
			sTestCaseID = ReadWriteExcel.getCellData(iTestcase, Constants.Col_TestCaseID, Constants.Sheet_TestCases);
			sRunMode = ReadWriteExcel.getCellData(iTestcase, Constants.Col_RunMode, Constants.Sheet_TestCases);
			System.out.println(sTestCaseID);
			if (sRunMode.equalsIgnoreCase("Yes")) {
				System.out.println(sTestCaseID + sRunMode);
				///////////////////////////////// /////////////////////////////////
				///////////////////////////////// /////////////////////////////////
				// For Multiple Test Data
				//////////////////////////////// /////////////////////////////////
				///////////////////////////////// /////////////////////////////////

				// This function will help to keep the multiple test data
				// results in respective test case sheet. iTotaltest1 will be
				// incremented inside the loop.
				SheetName = sTestCaseID;
				iTotaltest = RW.getcloumnnamecnt(SheetName, "TestDataEnd", 0);
				// System.out.println(iTotaltest);
				iTotaltest1 = iTotaltest;
				// This loop will help to iterate the dynamic multiple test data
				// columns.
				for (iTotaltestdata = 5; iTotaltestdata <= iTotaltest; iTotaltestdata++) {
					// System.out.println("Running with 1st
					// dataSet"+iTotaltestdata);
					iTotaltest1++;
					TestStepResult = iTotaltest;

					iTestStep = ReadWriteExcel.getRowContains(sTestCaseID, Constants.Col_TestCaseID, SheetName);
					// System.out.println("itestStep is "+iTestStep);
					iTestLastStep = ReadWriteExcel.getTestStepsCount(SheetName, sTestCaseID, iTestStep);

					// System.out.println("test last step is"+iTestLastStep);
					Log.startTestCase(sTestCaseID);
					bResult = true;
					//
					for (iTestStep = 1; iTestStep <= iTestLastStep - 1; iTestStep++) {

						sActionKeyword = ReadWriteExcel.getCellData(iTestStep, Constants.Col_ActionKeyword, SheetName);
						sPageObject = ReadWriteExcel.getCellData(iTestStep, Constants.Col_PageObject, SheetName);
						sPageObject = sPageObject.trim();
						TestStepName = ReadWriteExcel.getCellData(iTestStep, Constants.Col_TestStepID, SheetName);
						System.out.println(TestStepName);
						sData = ReadWriteExcel.getCellData(iTestStep, iTotaltestdata, SheetName);
						// System.out.println(sData);

						execute_Actions(TestReport);
					}
					if (sTestCaseResult == false || counter > 0) {
						ReadWriteExcel.setCellData(Constants.KEYWORD_FAIL, iTestcase, Constants.Col_Result,
								Constants.Sheet_TestCases, TestReport);
						Log.endTestCase(sTestCaseID);
						System.out.println(
								"-------------------------------------------------------------------------------------------------");
						System.out.println(
								"TestCase: ---------------------FAILED ------------------------------------------------------------");
						System.out.println(
								"---------------------------------------------------------------------------------------------------");

						ReadWriteExcel.setExcelFile(SheetName);
						ReadWriteExcel.setCellData("FAILED", iTestcase, Constants.Col_Result, Constants.Sheet_TestCases,
								TestReport);

						counter = 0;

						break;
					}

					if (sTestCaseResult == true && counter == 0) {
						ReadWriteExcel.setCellData(Constants.KEYWORD_PASS, iTestcase, Constants.Col_Result,
								Constants.Sheet_TestCases, TestReport);
						Log.endTestCase(sTestCaseID);

						System.out.println(
								"-------------------------------------------------------------------------------------------------");
						System.out.println(
								"TestCase: ---------------------PASSED ------------------------------------------------------------");
						System.out.println(
								"---------------------------------------------------------------------------------------------------");

						ReadWriteExcel.setExcelFile(SheetName);
						ReadWriteExcel.setCellData("PASSED", iTestcase, Constants.Col_Result, Constants.Sheet_TestCases,
								TestReport);

					}
				}
			}

		}

	}

	public static void execute_Actions(String TestReport) throws Exception {
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Method to control execution of all actions.
		// And will set value "PASSED" / "FAILED" for each TestSteps.
		//
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		for (int i = 0; i < method.length; i++) {
			// System.out.println(method[i].getName());
			// System.out.println(sActionKeyword);
			if (method[i].getName().equals(sActionKeyword)) {
				method[i].invoke(actionKeywords, sPageObject, sData);
				// System.out.println(method[i]);
				// System.out.println(actionKeywords);
				// This code block will execute after every test step
				if (bResult == true) {
					// If the executed test step value is true, Pass the test
					// step in Excel sheet
					ReadWriteExcel.setCellData(Constants.KEYWORD_PASS, iTestStep, iTotaltest1, SheetName, TestReport);
					sTestCaseResult = true;
					break;
				} else {
					// If the executed test step value is false, Fail the test
					// step in Excel sheet
					ReadWriteExcel.setCellData(Constants.KEYWORD_FAIL, iTestStep, iTotaltest1, SheetName, TestReport);

					// In case of false, the test execution will not reach to
					// last step of closing browser
					// So it make sense to close the browser before moving on to
					// next test case
					// Take the screenshot and catch the session not found
					// exception.Fixed.
					try {

						if (Keyword.DRIVER != null) {
							Keyword.getscreenshot();
							counter = counter + 1;
						}

					} catch (Exception e) {
						e.printStackTrace();
						ReadWriteExcel.setCellData(Constants.KEYWORD_FAIL, iTestStep, iTotaltest1, SheetName,
								TestReport);
						counter = counter + 1;

					}

					try {

						Keyword.closeBrowser("", "");

					} catch (Exception e) {
						e.printStackTrace();
					}
					sTestCaseResult = false;

					break;
				}

			}
		}

	}

}
