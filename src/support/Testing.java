package support;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import config.Constants;

public class Testing {

	public static void main(String[] args) {

		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String query = "select top 1 Appcode,Status,MRGOtherID from appointments";

		ArrayList<String> DbActualValues = new ArrayList<>();
		ArrayList<String> DbExpectedValues = new ArrayList<>();
		

		System.out.println(query);
		java.sql.Connection conn = null;

		// DataBase connection and reading data from database
		// code-------------------->

		try {
			Log.info("*Try to Verify DB With Excel Expected data*");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			while (rs.next()) {
						for (int i = 1; i <= columnsNumber; i++) {

					if (rs.getString(i) != null) {
						rs.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					DbActualValues.add(rs.getString(i));
				}
				System.out.println("Stored Actual value in DB:" + " " + DbActualValues);
			}
			
			
	
			DbExpectedValues.add("Checkin");
			DbExpectedValues.add("Openn");
			DbExpectedValues.add("5455");
			
			System.out.println("Expected value :" + " " + DbExpectedValues);
			
			Assert.assertEquals(DbActualValues,DbExpectedValues );
			
			
		
		} catch (ClassNotFoundException ex) {
			Log.info("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			Log.info("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

		}

	}

}

