




package executionEngine;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import config.Constants;
import support.ExecuteTestcase;


    
public class Controller {
	public static Properties OR;

	public static void main(String[] args) throws Exception {

		DOMConfigurator.configure("log4j.xml");
		String Path_OR = Constants.Path_OR3;
		FileInputStream fs = new FileInputStream(Path_OR);
		OR = new Properties(System.getProperties());
		OR.load(fs);

		// ****************************************************************************************************//
		// Select the test suite and test report location and input name of test suite
		// before executed
		// ****************************************************************************************************//
		ExecuteTestcase.execute_TestCase(Constants.TCRPath[0], Constants.TCRPath[1], Constants.TCRPath[2]);
        
		
		
	}

}