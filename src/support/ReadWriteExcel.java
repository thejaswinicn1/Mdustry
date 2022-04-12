
package support;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.testng.annotations.Test;

import config.Constants;

/**
 * Desc: This function is used to handle Read and Write Excel file.
 * 
 */
public class ReadWriteExcel {
	private static HSSFSheet ExcelWSheet;
	private static HSSFWorkbook ExcelWBook;
	private static HSSFCell Cell;
	private static HSSFRow Row;

	//////////////////////////////////////////////////////////////////////////////////////
	// Des: This method is to set the File path and to open the Excel file

	//////////////////////////////////////////////////////////////////////////////////////
	@Test
	public static void setExcelFile(String path) throws Exception {
		try {
			FileInputStream ExcelFile = new FileInputStream(path);
			ExcelWBook = new HSSFWorkbook(ExcelFile);
		} catch (Exception e) {
			Log.error("Class ReadWrite | Method setExcelFile | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void closeExcelfile(String Path) throws Exception {
		
		FileInputStream ExcelFile = new FileInputStream(Path);
		ExcelFile.close();
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// Des: This method is to set to get the number of row on a sheet
	//
	//////////////////////////////////////////////////////////////////////////////////////
	public static int getNumberofRow(String SheetName) throws Exception {
		int number = 0;
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			number = ExcelWSheet.getLastRowNum() + 1;

		} catch (Exception e) {
			Log.error("Class ReadWriteExcel| Method getNumberofRow | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		return number;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// Des: This method is to set to get test data
	//
	//////////////////////////////////////////////////////////////////////////////////////

	
	@SuppressWarnings("deprecation")
    int rowNum=1;
public static String getCellData(int rowNum, int colNum, String sheetName)
    {
        try
        {
        	
         ExcelWSheet = ExcelWBook.getSheet(sheetName);
         
         Row = ExcelWSheet.getRow(rowNum);
            Cell = Row.getCell(colNum);
            if(Cell.getCellTypeEnum() == CellType.STRING)
                return Cell.getStringCellValue();
            else if(Cell.getCellTypeEnum() == CellType.NUMERIC || Cell.getCellTypeEnum() == CellType.FORMULA)
            {
                String cellValue  = String.valueOf(Cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(Cell))
                {
                    DateFormat df = new SimpleDateFormat("dd/MM/yy");
                    java.util.Date date = Cell.getDateCellValue();
                    cellValue = df.format(date);
                }
                
                                return cellValue;
            }else if(Cell.getCellTypeEnum() == CellType.BLANK)
                return "";
            else
                return String.valueOf(Cell.getBooleanCellValue());
        }
        catch(Exception e)
        {
           // e.printStackTrace();
            return "row "+rowNum+" or column "+colNum +" does not exist  in Excel";
        }
    }


	//////////////////////////////////////////////////////////////////////////////////////
	// Des: This method is to set to get two locators
	//
	//////////////////////////////////////////////////////////////////////////////////////
	public static double getCellData1(int RowNum, int ColNum, String SheetName) throws Exception {
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			double CellData1 = Cell.getNumericCellValue();
			return CellData1;
		} catch (Exception e) {
			Log.error("Class ReadWriteExcel| Method getCellData | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
			return 0;

		}
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// Des: This method is to read the test data from the Excel cell

	//////////////////////////////////////////////////////////////////////////////////////
	public static int getRowContains(String sTestCaseName, int colNum, String SheetName) throws Exception {
		int iRowNum = 0;
		try {
			// ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int rowCount = ReadWriteExcel.getNumberofRow(SheetName);
			for (; iRowNum < rowCount; iRowNum++) {
				if (ReadWriteExcel.getCellData(iRowNum, colNum, SheetName).equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
		} catch (Exception e) {
			Log.error("Class ReadWriteExcel| Method getRowContains | Exception desc: " + e.getMessage());
		}
		return iRowNum;
	}

	//////////////////////////////////////////////////////////////////////////////////////
	// This method is to get the count of the test steps of test case
	// This method takes three arguments (Sheet name, Test Case Id & Test case
	////////////////////////////////////////////////////////////////////////////////////// row
	////////////////////////////////////////////////////////////////////////////////////// number

	//////////////////////////////////////////////////////////////////////////////////////
	public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception {
		try {
			for (int i = iTestCaseStart; i <= ReadWriteExcel.getNumberofRow(SheetName); i++) {
				if (!sTestCaseID.equals(ReadWriteExcel.getCellData(i, Constants.Col_TestCaseID, SheetName))) {
					int number = i;
					return number;
				}
			}
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int number = ExcelWSheet.getLastRowNum() + 1;
			return number;
		} catch (Exception e) {
			Log.error("Class ReadWriteExcel| Method getRowContains | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
			return 0;
		}
	}


	public static void setCellData(String Result, int RowNum, int ColNum, String SheetName, String ExcelSave)
			throws Exception {
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			Row = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}
			
			FileOutputStream fileOut = new FileOutputStream(ExcelSave);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			ExcelWBook = new HSSFWorkbook(new FileInputStream(ExcelSave));
		} catch (Exception e) {
			ExecuteTestcase.bResult = false;
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// Des : This method is to get column count
	///////////////////////////////////////////////////////////////////////////////////////////////
	public int getColumnCount(String SheetName) {
		// check if sheet exists
		if (!isSheetExist(SheetName))
			return -1;

		ExcelWSheet = ExcelWBook.getSheet(SheetName);
		Row = ExcelWSheet.getRow(0);

		if (Row == null)
			return -1;
		else {
			return Row.getLastCellNum();
		}

	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	// Des : This method is to check whether sheet exist or not
	///////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isSheetExist(String sheetName) {
		int index = ExcelWBook.getSheetIndex(sheetName);
		if (index == -1) {
			index = ExcelWBook.getSheetIndex(sheetName.toUpperCase());
			if (index == -1)
				return false;
			else
				return true;
		} else
			return true;
	}

	//////////////////////////////////////////////////////////////////////////////////////

	// Des: This method is to set the File path and to open the Excel file

	//////////////////////////////////////////////////////////////////////////////////////
	public static void saveExcelFile(String Path) throws Exception {
		try {
			FileOutputStream fout = new FileOutputStream(Path);
			// FileInputStream ExcelFile = new FileInputStream(Path);
			// ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWBook.write(fout);
		} catch (Exception e) {
			Log.error("Class ReadWrite | Method saveExcelFile | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public int getcloumnnamecnt(String SheetName, String colName, int RowNum) {
		try {
			int colNum = 0;
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			Row = ExcelWSheet.getRow(RowNum);
			for (int i = 0; i < Row.getLastCellNum(); i++) {
				if (Row.getCell(i).getStringCellValue().trim().equals(colName))
					colNum = i;

			}
			return colNum;
		} catch (Exception e) {
			Log.error("Class ReadWriteExcel| Method getCellData | Exception desc: " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
		return 0;
	}
}
