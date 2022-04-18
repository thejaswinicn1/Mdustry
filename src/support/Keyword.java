package support;

import static executionEngine.Controller.OR;
import static org.monte.media.FormatKeys.EncodingKey;
import static org.monte.media.FormatKeys.FrameRateKey;
import static org.monte.media.FormatKeys.KeyFrameIntervalKey;
import static org.monte.media.FormatKeys.MIME_AVI;
import static org.monte.media.FormatKeys.MediaTypeKey;
import static org.monte.media.FormatKeys.MimeTypeKey;
import static org.monte.media.VideoFormatKeys.CompressorNameKey;
import static org.monte.media.VideoFormatKeys.DepthKey;
import static org.monte.media.VideoFormatKeys.ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE;
import static org.monte.media.VideoFormatKeys.QualityKey;
import static support.ExecuteTestcase.TestStepName;
import static support.ExecuteTestcase.sTestCaseID;
import static support.ExecuteTestcase.testsuiteName;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.aerogear.security.otp.Totp;
import org.json.simple.JSONObject;
import org.monte.media.Format;
import org.monte.media.FormatKeys.MediaType;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.mysql.jdbc.StringUtils;

import config.Constants;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Keyword {
	public static String winHandleparent;
	private ScreenRecorder screenRecorder;
	public static WebDriver DRIVER;
	public static int Transactionid;
	public static String objectText;
	public static String copay;
	public static String balance;
	public static String Prepayment;
	public static String totalAmount;
	public static int rowNum = 0;
	public static int rowValue = 0;
	public int colNum;
	public static FileInputStream fis;
	public static XSSFSheet sh;
	public static XSSFWorkbook wb;
	public static XSSFCell cel;
	public static XSSFRow row;
	public static FileOutputStream fos;
	private final static int INDEX_CAMPAIGNID = 0;
	public static BufferedImage bufferedImage;
	public static String qrcodeResult;
    public static  int maxNoOfRetries = 40;
		
	public static void openBrowser(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to define the browser /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Open Browser*");
			switch (data) {
			case "FF":
				System.setProperty("webdriver.gecko.driver", "./Library/geckodriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability("marionette", true);
				DRIVER = new FirefoxDriver();
				DRIVER.manage().window().maximize();
				DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				break;
			case "Chrome":
				System.setProperty("webdriver.chrome.driver", "./Library/chromedriver.exe");
				DRIVER = new ChromeDriver();
				DRIVER.manage().window().maximize();
				DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
				break;
			case "IE":
				System.setProperty("webdriver.ie.driver", "./Library/IEDriverServer1.exe");
				DRIVER = new InternetExplorerDriver();
				DRIVER.manage().window().maximize();
				DRIVER.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
				break;

			}
			Log.info("Opened Browser: " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to open Browser --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void openMobileBrowser(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to define the browser /
		 **************************************************************************************************/
		String[] parts = data.split(":");
		String browser = parts[0];
		String deviceName = parts[1];

		try {
			Log.info("*Try to Open Mobile Browser*");
			switch (browser) {
			case "FF":
				System.setProperty("webdriver.gecko.driver", "./Library/geckodriver.exe");
				DesiredCapabilities capabilities = DesiredCapabilities.firefox();
				capabilities.setCapability("marionette", true);
				DRIVER = new FirefoxDriver();
				DRIVER.manage().window().maximize();
				DRIVER.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
				break;
			case "Chrome":
				System.setProperty("webdriver.chrome.driver", "./Library/chromedriver2.exe");
				Map<String, Object> mobileEmulation = new HashMap<String, Object>();
				Map<String, Object> deviceMetrics = new HashMap<String, Object>();
				Map<String, Object> metrics = new HashMap<>();
				deviceMetrics.put("pixelRatio", 2);
				metrics.put("deviceMetrics", deviceMetrics);
				mobileEmulation.put("deviceName", deviceName);
				ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--start-maximized");
				chromeOptions.addArguments("test-type");
				chromeOptions.addArguments("disable-extensions");
				chromeOptions.addArguments("disable-infobars");
				chromeOptions.addArguments("--enable-show-device-frame");
				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("password_manager_enabled", false);
				chromeOptions.setExperimentalOption("prefs", prefs);
				chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				DRIVER = new ChromeDriver(cap);
				DRIVER.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
				break;
			case "IE":
				System.setProperty("webdriver.ie.driver", "./Library/IEDriverServer.exe");
				DRIVER = new InternetExplorerDriver();
				DRIVER.manage().window().maximize();
				DRIVER.manage().timeouts().implicitlyWait(150, TimeUnit.SECONDS);
				break;
			}
			Log.info("Opened Browser: " + browser);
		} catch (Exception e) {
			Log.info("-------- Unable to open Browser --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void navigateTo(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to navigate to BASE_URL /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to navigate to specify URL*");
			DRIVER.navigate().to(data);
			Log.info("Navigated to specify URL: " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to navigate to URL --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void navigateToURL(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to define the URL /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Navigate to URL *");
			switch (data) {
			case "DASH_URL":
				DRIVER.navigate().to(Constants.DASHBOARD_URL);
				Log.info("Navigated to DASHBOARD_URL");
				break;
			case "CHECKIN_URL":
				DRIVER.navigate().to(Constants.CHECKIN_URL);
				Log.info("Navigated to CHECKIN_URL");
				break;
			case "ADMIN_URL":
				DRIVER.navigate().to(Constants.ADMIN_URL);
				Log.info("Navigated to ADMIN_URL");
				break;
			case "CLOUDADMIN_URL":
				DRIVER.navigate().to(Constants.CLOUDADMIN_URL);
				Log.info("Navigated to CLOUDADMIN_URL");
				break;
			}
			Log.info("Unable to navigate to URL : " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to navigate to URL --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This Method only for payment gateway
	public static void paymentGateway(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to define the URL /
		 **************************************************************************************************/
		try {
			String iframedata = "PC_INS_VNT_FRAMEID ";

			String instCardno = "PC_TXT_INST_CARDNO";
			String instExpdate = "PC_TXT_INST_EXPDT";
			String instMakepayment = "PC_BTN_INST_MAKEPAYMENT";

			String vntCardno = "PC_TXT_VNT_CARDNO";
			String vntexpmonth = "PC_TXT_VNT_EXPMNT";
			String vntexpyear = "PC_TXT_VNT_EXPYEAR";
			String vntvMakepayment = "PC_BTN_VNTV_MAKEPAYMENT";

			String[] parts = data.split(",");
			String paymentGateway = parts[0];
			String cardN0 = parts[1];
			String expiryMonth = parts[2];
			String expiryDate = parts[3];

			Log.info("*Try to Make payment");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			switch (paymentGateway) {
			case "INSTAMED":
				DRIVER.switchTo().frame(iframedata);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(instCardno)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(instCardno))).sendKeys(cardN0);
				Log.info("Inputted Card Number : " + cardN0 + " to element " + object);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(instExpdate)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(""))).sendKeys(expiryMonth);
				Log.info("Inputted Expiry date : " + expiryDate + " to element " + object);
				DRIVER.findElement(By.xpath(OR.getProperty(instMakepayment))).click();
				Log.info("Clicked  Make Payment " + object);
				break;
			case "VANTIV":
				DRIVER.switchTo().frame(iframedata);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(vntCardno)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(vntCardno))).sendKeys(data);
				Log.info("Inputted Card Number : " + data + " to element " + object);
				Select expiryMnt = new Select(DRIVER.findElement(By.xpath(OR.getProperty(vntexpmonth))));
				expiryMnt.selectByVisibleText(expiryMonth);
				Log.info("Selected " + object + " by visible text: " + expiryMonth);
				Select expiryDt = new Select(DRIVER.findElement(By.xpath(OR.getProperty(vntexpyear))));
				expiryDt.selectByVisibleText(expiryDate);
				Log.info("Selected " + object + " by visible text: " + expiryDate);
				DRIVER.findElement(By.xpath(OR.getProperty(vntvMakepayment))).click();
				Log.info("Clicked  Make Payment " + object);
				break;
			}

			Log.info("Made payment successfully with  : " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to Make Payment --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputUser(String object, String data) {
		/**************************************************************************************************
		 * /** Desc: This function is used to define the URL /
		 **************************************************************************************************/
		try {

			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			Log.info("*Try to input to UserName *");
			switch (data) {

			case "DASH_UN":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "CHECKIN_UN":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "ADMIN_UN":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "CLOUDADMIN_UN":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			}
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputPassword(String object, String data) {
		/**************************************************************************************************
		 * /** Desc: This function is used to define the URL /
		 **************************************************************************************************/
		try {

			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			Log.info("*Try to input to UserName *");
			switch (data) {

			case "DASH_PWD":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "CHECKIN_PWD":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "ADMIN_PWD":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			case "CLOUDADMIN_PWD":
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
				DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
				System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
				Log.info("Inputted Username: " + data + " to element " + object);
				break;
			}
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void closeBrowser(String object, String data) {
		/*****************************************************************************************************
		 * /** Des: This function is used to close browser /
		 *****************************************************************************************************/
		try {
			Log.info("*Try to close the browser*");
			DRIVER.close();
			Log.info("*Closed the browser*");
		} catch (Exception e) {
			Log.info("-------- Unable to close Browser --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void quitBrowser(String object, String data) {
		/*****************************************************************************************************
		 * /** Des: This function is used to quit browser /
		 *****************************************************************************************************/
		try {
			Log.info("*Try to quit the browser*");
			DRIVER.quit();
			Log.info("*quitted the browser*");
		} catch (Exception e) {
			Log.info("-------- Unable to quit Browser --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clickElement(String object, String data) {
		/*******************************************************************************************************/
		/**
		 * Des: This function is used to click on Elements /
		 *******************************************************************************************************/
		try {
			Log.info("*Try to Click on WebElement*");
			DRIVER.findElement(By.xpath(OR.getProperty(object))).click();
			Log.info("Clicked on WebElement " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to click Element: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}
	
	public static void clickElementAfterVisible(String object, String data) throws InterruptedException {
		int counter = 0;
		while(maxNoOfRetries > 0 && counter != maxNoOfRetries) {
			Thread.sleep(1000);
			try {
				WebElement element = DRIVER.findElement(By.xpath(OR.getProperty(object)));
				JavascriptExecutor js = (JavascriptExecutor) DRIVER;
				if (js.executeScript("return document.readyState").toString().equals("complete")) {
					element.click();
					break;
				}
			}
			
			
			catch (Exception e) {
				Log.warn(String.format("clickElementAfterVisible: Element not found",object));
			}
			counter++;
		}
	}
	
	public static void clickElementInArrayBasedOnText(String object, String data)
	{
		/*****************************************************************************************************
		 * /** Des: This function is to click on webelement which is inside webelement array, based on text passed.  /
		 *****************************************************************************************************/
		try {
			Log.info("*Trying to click on element*");
			List<WebElement> lst=DRIVER.findElements(By.xpath(OR.getProperty(object)));
		    for(WebElement e : lst)
		    {
		    	System.out.println("Element text is "+e.getText());
		        if (e.getText().contains(data))
			    {
			        System.out.println("Text present");
			        e.click();
			        break;
			    }
		    }
		    
			Log.info("*element present*");
		} catch (Exception e) {
			Log.info("-------- Unable to click --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}
	
	public static void verifyTextInWebelementArray(String object, String data)
	{
		/*****************************************************************************************************
		 * /** Des: This function is to click on webelement which is inside webelement array, based on text passed.  /
		 *****************************************************************************************************/
		boolean valueresult=false;
		try {
			Log.info("*Trying to click on element*");
			List<WebElement> lst=DRIVER.findElements(By.xpath(OR.getProperty(object)));
		    for(WebElement e : lst)
		    {
		    	System.out.println("Element text is "+e.getText());
		        if (e.getText().contains(data))
			    {
			        System.out.println("Text present");
			        valueresult=true;
			        break;
			    }else {
			    	 valueresult=false;
			    }
		    }
		    if(valueresult ==true)
		    {
		    	ExecuteTestcase.bResult = true;
		    	Log.info("*Text exist*");
		    }else {
		    	ExecuteTestcase.bResult = false;
		    	Log.info("*Text not exist*");
		    }
		    
			
		} catch (Exception e) {
			Log.info("-------- Unable to click --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clickElementByLinkText(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clickElementByLinkText /
		 ****************************************************************************************************/
		try {
			Log.info("*Clicked on WebElement ByLinkText*");
			DRIVER.findElement(By.linkText(OR.getProperty(object))).click();
			Log.info("Clicked on WebElement ByLinkText " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to click Element By LinkText: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clickelementnodes(String object, String data) {
		/*******************************************************************************************************/
		/**
		 * Des: This function is used to click on Element /
		 *******************************************************************************************************/
		try {
			Log.info("*Try to Click on WebElement*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String element = (OR.getProperty(object));
			String fullXpath = String.format(element, data);
			System.out.println(fullXpath);
			DRIVER.findElement(By.xpath(fullXpath)).click();
			Log.info("Clicked on WebElement " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to click Element: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitAndPause(String object, String data) throws InterruptedException {
		/****************************************************************************************************
		 * /** Des: This function is used to pause all actions with time (MilSecond). /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to wait for 5 seconds*");
			//DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			System.out.println("Waited for 50 seconds");
			Thread.sleep(10000);
			Log.info("Waited for 50 seconds");
		} catch (Exception e) {
			Log.info("-------- Unable to wait -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitAndPauseBatch(String object, String data) throws InterruptedException {
		/****************************************************************************************************
		 * /** Des: This function is used to pause all actions with time (MilSecond). /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to wait for 5 seconds*");
			DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			Thread.sleep(400000);
			Log.info("Waited for 5 seconds");
		} catch (Exception e) {
			Log.info("-------- Unable to wait -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitAndPausesession(String object, String data) throws InterruptedException {
		/****************************************************************************************************
		 * /** Des: This function is used to pause all actions with time (MilSecond). /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to wait for 5 seconds*");
			DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			Thread.sleep(400000000);
			Log.info("Waited for 5 seconds");
		} catch (Exception e) {
			Log.info("-------- Unable to wait -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputMRN(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/

		try {
			Log.info("*Try to input value*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			JavascriptExecutor JS = ((JavascriptExecutor) DRIVER);
			JS.executeScript("document.getElementById('txtSearch').removeAttribute('readonly');");
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
			System.out.println(data);
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputValue(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/

		try {
			Log.info("*Try to input value*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
			System.out.println(data);

			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);

			System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputValue1(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/

		try {
			Log.info("*Try to input value*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);

			System.out.println(DRIVER.findElement(By.xpath(OR.getProperty(object))).getText());
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clearTextBox(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear data on text box /
		 ****************************************************************************************************/
		try {
			Log.info("*Try clear data on TextBox*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
			Log.info("Cleared data on TextBox: " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to clear TextBox: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void submit(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to submit button /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to submit the button*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).submit();
			Log.info("Submitted the button: " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to submit button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void navigateToBack(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to navigate to Back page /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to navigate to back page*");
			DRIVER.navigate().back();
			Thread.sleep(5000);
			Log.info("Navigated to back page");
		} catch (Exception e) {
			Log.info("-------- Unable to Navigate to Back --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void navigatetoForward(String object, String data) {
		/****************************************************************************************************
		 ** Des: This function is used to navigate to Forward /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Navigate to Forward page*");
			DRIVER.navigate().forward();
			Thread.sleep(5000);
			Log.info("Navigated to Forward page");
		} catch (Exception e) {
			Log.info("-------- Unable to Navigate to Forward --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void refreshPage(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to refresh page. /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Refresh page*");
			DRIVER.navigate().to(DRIVER.getCurrentUrl());
			Thread.sleep(5000);
			Log.info("Refreshed page");
		} catch (Exception e) {
			Log.info("-------- Unable to refresh page --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void moveToElement(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to movetoElement /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to hover to move To Element*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Actions actions = new Actions(DRIVER);
			WebElement Element = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			actions.moveToElement(Element).perform();
			Log.info("Hovered to move To Element: " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to move to Element: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void doubleClick(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to doubleClick /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to DoubleClick on Element*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement element = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Actions actions = new Actions(DRIVER).doubleClick(element);
			actions.build().perform();
			Log.info("DoubleClicked on Element: " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to doubleClick on Element: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitForElementVisible(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to waitForElementPresent
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Wait for element Visible*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 100);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(object))));
			Log.info("Element: " + object + " is Visible.");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is not Visible--------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitForElementAbleToClick(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to waitForElementPresent
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Wait for element is able to click*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 100);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(object))));
			Log.info("Element: " + object + " is able to click.");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is not able to click --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyElementIsExisted(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element is existed
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try toVerify Element is existed*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			Log.info("Element: " + object + " is existed");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is not existed --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyElementIsNotExisted1(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element is not exists
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not exists*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 03);
			Boolean isNotExists = DRIVER.findElements(By.xpath(OR.getProperty(object))).size() == 0;

			if (isNotExists == true) {
				ExecuteTestcase.bResult = true;
			}

			Log.info("Element: " + object + " is not existed");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is existed --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyElementIsNotExisted(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element is exists
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not exists*");

			Log.info("Element: " + object + " is not existed");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is existed --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyText(String object, String data) {
		/****************************************************************************************************
		 * /* Des: This function is used to verify actual text and expected text.
		 * 
		 * /
		 ****************************************************************************************************/
		String actualText = null;
		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String actual = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			System.out.println("Actual text is "+actual);
			actualText = actual.trim();
			if (actualText.equals(data.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("actual text on " + actualText + "and Expected text: " + data + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("actual text on " + actualText + "and Expected text: " + data + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + actualText + "and Expected text: " + data
					+ " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextElementNodes(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify actual text and expected text for
		 * element nodes * ( By passing part of xpath as data and expected text) /
		 ****************************************************************************************************/
		String actualText = null;
		String expectedData = null;
		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String xpathtext = parts[0];
			expectedData = parts[1];
			String element = (OR.getProperty(object));
			String fullXpath = String.format(element, xpathtext);
			System.out.println(fullXpath);
			actualText = DRIVER.findElement(By.xpath(fullXpath)).getText().trim();
			if (actualText.equalsIgnoreCase(expectedData.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("actual text on " + actualText + "and Expected text: " + expectedData + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("actual text on " + actualText + "and Expected text: " + expectedData + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + actualText + "and Expected text: " + expectedData
					+ " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextElementNodes1(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify actual text and expected text for
		 * element nodes * ( By passing part of xpath as data and expected text) /
		 ****************************************************************************************************/
		String actualText = null;
		String expectedData = null;
		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = data.split(":");
			String xpathtext = parts[0];
			expectedData = parts[1];
			String element = (OR.getProperty(object));
			String fullXpath = String.format(element, xpathtext);
			System.out.println(fullXpath);
			actualText = DRIVER.findElement(By.xpath(fullXpath)).getText().trim();
			if (actualText.equalsIgnoreCase(expectedData.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("actual text on " + actualText + "and Expected text: " + expectedData + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("actual text on " + actualText + "and Expected text: " + expectedData + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + actualText + "and Expected text: " + expectedData
					+ " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextContains(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify pattern matching actual text and
		 * expected text.
		 * 
		 * /
		 ****************************************************************************************************/
		String actualText = null;

		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String actual = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			actualText = actual.trim();
			if (actualText.contains(data.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + object + "" + data + "and actual text:" + actualText + "are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Expected text on " + object + "" + data + "and actual text:" + actualText + "are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Expected text on " + object + "" + data + " and actual text: "
					+ actualText + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextValue(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify actual text and expected text.
		 * 
		 * /
		 ****************************************************************************************************/
		String actualText = null;
		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String actual = DRIVER.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			actualText = actual.trim();
			System.out.println(actualText);
			if (actualText.equals(data)) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + actualText + "and actual text: " + data + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Actual text on " + actualText + "and actual text: " + data + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + actualText + "and actual text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void fetchTextObject(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to fetch the text and Stored * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to fetch text from element and store it*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String actual = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			objectText = actual.trim();
			ExecuteTestcase.bResult = true;
			Log.info("Fetched the text from the" + object + "and stored");
		} catch (Exception e) {
			Log.info("-------- Unable to Fetch text on " + object + "--------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextObject(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify actual text and expected between the
		 * 2 objects text.
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String expected = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			String expectedText = expected.trim();
			if (objectText.equals(expectedText.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("actual text on " + objectText + "and Expected text: " + expectedText + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("actual text on " + objectText + "and Expected text: " + expectedText + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + object + "and Expected text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void killBrowser(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to kill browser processes.
		 * 
		 * /
		 ****************************************************************************************************/
		String strCmdLine = null;

		try {
			Log.info("*Try to kill  browser processes*");
			switch (data) {
			case "Chrome":
				strCmdLine = String.format("taskkill /im chromedriver.exe /f");
				break;
			case "IE":
				strCmdLine = String.format("taskkill /im iexploredriver.exe /f");
				break;
			case "FF":
				strCmdLine = String.format("taskkill /im firefoxdriver.exe /f");
				break;
			}
			Runtime.getRuntime().exec(strCmdLine);
			Log.info("Killed all browser processes.");
		} catch (Exception e) {
			Log.info("-------- Unable to close all browser. --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitForAjax(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to waitForAjax.
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Wait for Ajax script be executed*");
			new WebDriverWait(DRIVER, 180).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					JavascriptExecutor js = (JavascriptExecutor) driver;
					return (Boolean) js.executeScript("return jQuery.active == 0");
				}
			});
			Log.info("Ajax script was executed");
		} catch (Exception e) {
			Log.info("-------- Error Ajax script waiting --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectByVisibleText(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to select By Visible Text
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to select by visible text*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Select selectAction = new Select(DRIVER.findElement(By.xpath(OR.getProperty(object))));
			selectAction.selectByVisibleText(data);
			Log.info("Selected " + object + " by visible text: " + data);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to select " + object + " by visible text: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectByValue(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to select By Value
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to select by value*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Select selectAction = new Select(DRIVER.findElement(By.xpath(OR.getProperty(object))));
			selectAction.selectByValue(data);
			Log.info("Selected " + object + " by value. " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to select " + object + " by value: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectByIndex(String object, int data) {
		/****************************************************************************************************
		 * /** Des: This function is used to select By Index
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to select by index*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Select selectAction = new Select(DRIVER.findElement(By.xpath(OR.getProperty(object))));
			selectAction.selectByIndex(data);
			Log.info("Selected " + object + " by index: " + data);
		} catch (Exception e) {
			Log.info("-------- Unable to select " + object + " by index: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void switchToIFrameWithName(String object, String data) {
		/****************************************************************************************************
		 * /** Desc: this function use to switch frame on page. It's used before
		 * hover_on_menu, and click_element functions /** Three functions use to click
		 * on submenu on page /** Currently, "switch_to.frame(str_frame_name)" work well
		 * on latest IE and chrome. Not work on new FF version (Verified:NOT YET) /** On
		 * FF should add more wait time. /
		 *****************************************************************************************************/
		Boolean valueResult = true;
		try {
			Log.info("*Try to switch to iFrame with name*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> iframes = DRIVER.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				System.out.println(iframe);
				if (iframe.getAttribute("name").equals(data)) {
					DRIVER.switchTo().frame(data);
					valueResult = true;
					break;
				} else {
					valueResult = false;
				}
			}
			if (valueResult) {
				Log.info("We are in iFrame with Name: " + data);
			} else {
				Log.info("Unable to switch to iFrame with iFrame Name: " + data + " --------");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("--------  Unable to switch to iFrame with name: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void switchToIFrameWithID(String object, String data) {
		/***************************************************************************************************
		 * /** Desc: this function use to switch To IFrame With ID /
		 ***************************************************************************************************/
		Boolean valueResult = true;
		try {
			Log.info("*Try to switch to iFrame with iFrameID*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> iframes = DRIVER.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				System.out.println(iframe);
				if (iframe.getAttribute("id").equals(data)) {
					DRIVER.switchTo().frame(data);
					valueResult = true;
					break;
				} else {
					valueResult = false;
				}
			}
			if (valueResult) {
				Log.info("We are in iFrame with ID: " + data);
			} else {
				Log.info("Unable to switch to iFrame with iFrame ID: " + data);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to switch to iFrame with iFrame ID: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void switchToMainPage(String object, String data) {
		/****************************************************************************************************
		 * /** Desc: this function use to switch frame to main page. /** Currently,
		 * "switch_to.frame(str_frame_name)" work well on latest IE and chrome. Not work
		 * on new FF version (Verified:NOT YET) /
		 *****************************************************************************************************/
		try {
			Log.info("*Try to switch to Main Page layout*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.switchTo().defaultContent();
			Log.info("We are in Main Page layout.");
		} catch (Exception e) {
			Log.info("-------- Unable to switch to Main Page layout --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextInTable(String object, String data) {
		/***************************************************************************************************
		 * /** Desc: this function use to verifyTextInTable /
		 ****************************************************************************************************/
		boolean valueResult = false;
		try {
			Log.info("*Try to verify Text In Table*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> listCell = DRIVER.findElements(By.xpath(OR.getProperty(object)));
			for (WebElement iCell : listCell) {
				if (iCell.getText().equals(data)) {
					Log.info("Value: " + data + " on table: " + object + " is existing.");
					valueResult = true;
					break;
				} else {
					valueResult = false;
				}
			}
			if (valueResult == true) {
				ExecuteTestcase.bResult = true;
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Value: " + data + " on table: " + object + "is not existing.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify data: " + data + " in table: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyCheckboxIsChecked(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify Checkbox Is Checked /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify Checkbox Is Checked*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement checkbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (checkbox.isSelected() == true) {
				Log.info("CheckBox: " + object + " is checked.");
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("CheckBox: " + object + " is NOT checked.");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify checkbox " + object + " is checked or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	
	public static void verifyCheckboxIsNotChecked(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify Checkbox Is NOT Checked
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify Checkbox Is Not Checked*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement checkbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (checkbox.isSelected() == false) {
				Log.info("CheckBox: " + object + " is NOT checked.");
			} else {
				Log.info("CheckBox: " + object + " is checked.");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify checkbox: " + object + " is available  or not --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyRadioIsChecked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Radio Is Checked /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify Radio Is Checked*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement radio = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (radio.isSelected() == true) {
				Log.info("Radio: " + object + " is checked.");
			} else {
				Log.info("Radio: " + object + " is NOT checked.");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify radio: " + object + " is checked or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyRadioIsNotChecked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Radio Is Not Checked /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify Radio Is Not Checked*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement radio = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (radio.isSelected() == false) {
				Log.info("Radio: " + object + " is NOT checked.");
			} else {
				Log.info("Radio: " + object + " is checked.");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify radio: " + object + " is available or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void rightClick(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Right Click on the element /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Right Click  on Element*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			Actions actions = new Actions(DRIVER);
			WebElement element = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			actions.contextClick(element).perform();
			Log.info("Right Clicked on Element: " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to Right Click  on Element: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void checkCheckBox(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to check Checkbox /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Check CheckBox*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			boolean checked = objCheckbox.isSelected();
			if (checked == false) {
				objCheckbox.click();
				Log.info("Check CheckBox: " + object);
			} else {
				Log.info("CheckBox: " + object + " was already checked .");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to check CheckBox: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void unCheckCheckBox(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to uncheck Checkbox /
		 **************************************************************************************************/
		try {
			Log.info("*Try to unCheck CheckBox*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (objCheckbox.isSelected() == true) {
				objCheckbox.click();
				Log.info("Uncheck CheckBox: " + object);
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("CheckBox: " + object + " was already unchecked.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to uncheck CheckBox: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clickTreeViewItem(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to click TreeView Item /
		 ************************************************************************************************/
		boolean valueResult = false;
		try {
			Log.info("*Try to click TreeView Item*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement treeView = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> listChild = treeView.findElements(By.tagName("span"));
			for (WebElement child : listChild)
				if (child.getText() == data) {
					child.click();
					valueResult = true;
					break;
				}
			if (valueResult = true) {
				Log.info("Item " + data + " is existed in TreeView: " + object);
			} else {
				Log.info("Item " + data + " is NOT existed in TreeView: " + object);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to click TreeView Item: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTreeViewItemExist(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to verify Treeview Item Exist /
		 ************************************************************************************************/
		boolean valueResult = false;
		try {
			Log.info("*Try to verify TreeView Item Exist*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement treeView = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> listChild = treeView.findElements(By.tagName("span"));
			for (WebElement child : listChild)
				if (child.getText() == data) {
					valueResult = true;
					break;
				}
			if (valueResult = true) {
				Log.info("Item " + data + "is existed in tree view: " + object);
			} else {
				Log.info("Item " + data + "is NOT existed in tree view: " + object);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to click TreeView Item: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTreeViewItemNotExist(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to verify Tree view Item Not Exist /
		 ************************************************************************************************/
		boolean valueResult = false;
		try {
			Log.info("*Try to verify TreeView Item Not Exist*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement treeView = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> listChild = treeView.findElements(By.tagName("span"));
			for (WebElement child : listChild)
				if (child.getText() == data) {
					valueResult = false;
					break;
				}
			if (valueResult = true) {
				Log.info("Item " + data + "is existed in TreeView: " + object);
			} else {
				Log.info("Item " + data + "is NOT existed in TreeView: " + object);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to click TreeView Item: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void closeAlertOfBrowser(String object, String data) {
		/************************************************************************************************
		 * /** Des: This function is used to close Alert Of Browser /
		 ************************************************************************************************/
		try {
			Log.info("*Try to close alert of browser*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.switchTo().alert().dismiss();
			Log.info("Closed alert of browser successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to close alert of browser --------");
			ExecuteTestcase.bResult = false;
		}
	}

	public static void acceptAlertOfBrowser(String object, String data) {
		/***********************************************************************************************
		 * /** Des: This function is used to accept Alert Of Browser /
		 ***********************************************************************************************/
		try {
			Log.info("*Try to accept alert of browser*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.switchTo().alert().accept();
			Log.info("Accepted alert of browser successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to accept alert of browser --------");
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyAlertText(String object, String data) {
		/**********************************************************************************************
		 * /** Des: This function is used to accept Alert Of Browser /
		 **********************************************************************************************/
		try {
			Log.info("*Try to verify AlertText*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String getText = DRIVER.switchTo().alert().getText();
			if (getText == data) {
				Log.info("AlertText is  the same with " + data);
			} else {
				Log.info("AlertText is  different with " + data);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to accept alert of browser --------");
			ExecuteTestcase.bResult = false;
		}
	}

	public static void sendValueToAlert(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is used to send Value To Alert /
		 *********************************************************************************************/
		try {
			Log.info("*Try to send value to Alert*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.switchTo().alert().sendKeys(data);
			Log.info("Sent value: " + data + " to alert successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to send value to Alert --------");
			ExecuteTestcase.bResult = false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Exception handling
	//
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void getscreenshot() throws Exception {
		/***************************************************************************************************
		 * /** Taking screenshots /
		 *************************************************************************************************/
		File scrFile = ((TakesScreenshot) DRIVER).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(Constants.FailScrnpath + "Error__" + testsuiteName + "__" + sTestCaseID
				+ "__" + TestStepName + "__" + GetTimeStampValue() + ".png"));
	}

	public static String GetTimeStampValue() throws IOException {
		Calendar cal = Calendar.getInstance();
		java.util.Date time = cal.getTime();
		String timestamp = time.toString();
		System.out.println(timestamp);
		String systime = timestamp.replace(":", "-");
		System.out.println(systime);
		return systime;
	}

	public static void jQueryCalendar(String object, String data) {

		/*********************************************************************************************
		 * /** Des: This function is Used to work on jquery Calendar /
		 *********************************************************************************************/
		try {
			Log.info("*Try to select Date from Calendar popup*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement dateWidget = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			List<WebElement> rows = dateWidget.findElements(By.xpath("//tr"));
			List<WebElement> columns = dateWidget.findElements(By.xpath("//td"));
			for (WebElement cell : columns) {
				// Select Specified Date
				if (cell.getText().equals(data)) {
					System.out.println("data");
					cell.findElement(By.linkText(data)).click();
					break;
				}
			}

			Log.info("Sent value: " + data + " Selected Date Sucessfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Select Date ---" + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	public static void KeyUP(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used move Key UP /
		 *********************************************************************************************/
		try {
			Log.info("*Try to move Key Up*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(Keys.UP);
			Log.info("Moved Key Up successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to move Key UP --" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}
	
	
	public static void pageDown(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used move Key UP /
		 *********************************************************************************************/
		try {
			Log.info("*Try to move Page down*");
			Actions action = new Actions(DRIVER);
			action.sendKeys(Keys.PAGE_DOWN).build().perform();
			Log.info("Moved Page down successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Page down --" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}
	
	
	public static void KeyDown(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used move Key Down /
		 *********************************************************************************************/
		try {
			Log.info("*Try to move Key Down*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(Keys.DOWN);
			Log.info("Moved Key Down successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to move Key Down ----" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void KeyDelete(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used Delete /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Delete*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(Keys.DELETE);
			Log.info("Deleted");
		} catch (Exception e) {
			Log.info("-------- Unable to Delete ----" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void KeyEnter(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Enter /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Enter*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(Keys.ENTER);
			Log.info("Performed Enter successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Performed Enter ----" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void clickAndHold(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used clickAndHold /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Click and Hold*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Actions builder = new Actions(DRIVER);
			WebElement locator = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			builder.clickAndHold(locator);
			Log.info("Clicked and Hold successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Click and Hold-----" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void dragAndDrop(String object, String object1, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Drag and drop /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Drag and Drop*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Actions builder = new Actions(DRIVER);
			WebElement Sourcelocator = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			WebElement Destinationlocator = DRIVER.findElement(By.xpath(OR.getProperty(object1)));
			builder.dragAndDrop(Sourcelocator, Destinationlocator);
			Log.info("Dragged and Dropped successfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Drag and Drop -----" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void VerifyElementEnabled(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Verify Element is Enabled /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Verify Element is Enabled*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Boolean Element = DRIVER.findElement(By.xpath(OR.getProperty(object))).isEnabled();
			if (Element == true) {
				Log.info("Element is Enabled Successfully");
			}

		} catch (Exception e) {
			Log.info("--------Element is not Enabled --" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void VerifyElementDisabled(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Verify Element is Disabled /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Verify Element is Disabled*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Boolean Element = DRIVER.findElement(By.xpath(OR.getProperty(object))).isEnabled();
			if (Element == false) {
				Log.info("Element is Disabled Successfully");
			}

		} catch (Exception e) {
			Log.info("--------Element is not Disabled --" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void VerifyElementVisible(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Verify Element is Visible /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Verify Element is Visible*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			Boolean Element = DRIVER.findElement(By.xpath(OR.getProperty(object))).isDisplayed();
			if (Element == true) {
				Log.info("Element is Visible Successfully");
				ExecuteTestcase.bResult = true;
			}

		} catch (Exception e) {
			Log.info("--------Element is not Visible-" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void VerifyElementNotVisible(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Verify Element is Visible /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Verify Element is not Visible*");
			DRIVER.manage().timeouts().implicitlyWait(05, TimeUnit.SECONDS);
			Boolean Element = DRIVER.findElement(By.xpath(OR.getProperty(object))).isDisplayed();
			if (Element == false) {
				Log.info("Element is Not Visible Successfully");
				ExecuteTestcase.bResult = true;
			}

		} catch (Exception e) {
			Log.info("--------Element is Visible-" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void deleteCookies(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Delete Cookies /
		 *********************************************************************************************/
		try {
			Log.info("*Try to Delete Cookies*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.manage().deleteAllCookies();
			{
				Log.info("Cookies Deleted Sucesfully");
			}

		} catch (Exception e) {
			Log.info("------Cookies is not Deleted-" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void DownloadFileExists(String object, String data) {
		/*********************************************************************************************
		 * /** Des: This function is Used to Verify Downloaded File Exists or not /
		 *********************************************************************************************/
		try {
			Log.info("*Try to verify Downloaded File Exists*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath(OR.getProperty(object))).click();
			File f = new File(data);

			if (f.exists()) {

				Log.info("File exists");
			}

		} catch (Exception e) {
			Log.info("------File not Found -----------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void SliderStrt(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to move the slider from starting point
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to move the slider*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement Start = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Actions builder = new Actions(DRIVER);
			Action dragAndDrop = builder.clickAndHold(Start).moveByOffset(((int) Double.parseDouble(data)), 0).release()
					.build();
			dragAndDrop.perform();
			Log.info("Slider moved from start point");
		} catch (Exception e) {
			Log.info("-------- Unable to moved from start point " + object + " by value: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void SliderEnd(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to move the slider from starting point
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to move the slider*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement Start = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Actions builder = new Actions(DRIVER);
			Action dragAndDrop = builder.clickAndHold(Start).moveByOffset(0, (int) Double.parseDouble(data)).release()
					.build();
			dragAndDrop.perform();
			Log.info("Slider moved from start point");
		} catch (Exception e) {
			Log.info("-------- Unable to move from End point " + object + " by value: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void Slider_iFrame(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to move the slider if is in iframe
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to move the slider*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> AlliFrameID = DRIVER.findElements(By.tagName("iframe"));
			System.out.println(AlliFrameID.size());
			for (int i = 0; i <= AlliFrameID.size(); i++) {
				System.out.println(AlliFrameID.get(i).getAttribute("class"));
			}
			DRIVER.switchTo().frame(DRIVER.findElement(By.className("demo-frame")));
			Point MyPoint = DRIVER.findElement(By.xpath(object)).getLocation();
			WebElement someElement = DRIVER.findElement(By.xpath(object));
			System.out.println(MyPoint.x + "--------" + MyPoint.y);
			Actions builder = new Actions(DRIVER);
			Action dragAndDrop = builder.clickAndHold(someElement).moveByOffset(((int) Double.parseDouble(data)), 0)
					.release().build();
			dragAndDrop.perform();
			Log.info("Slider moved from start point");

		} catch (Exception e) {
			Log.info("-------- Unable to move the slider " + object + " by value: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void GetWindowHandle(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to get window handle /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Get Current Window Handle and Title*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String CurrentWindow = DRIVER.getWindowHandle();
			System.out.println(CurrentWindow);
			System.out.println(DRIVER.getTitle());

			Log.info("Fetched Current Window Handle and Title" + CurrentWindow + DRIVER.getTitle());
		} catch (Exception e) {
			Log.info("-------- Unable Fetched Current Window Handle and Title " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void WndhndParent_Child(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Move control from Parent to child window /
		 ****************************************************************************************************/

		try {
			Log.info("*Try to Move control from parent to child window*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			winHandleparent = DRIVER.getWindowHandle();
			for (String winHandlechild : DRIVER.getWindowHandles()) {
				DRIVER.switchTo().window(winHandlechild);
				System.out.println(DRIVER.getTitle());
				Log.info("* Moved control from parent to child *" + DRIVER.getTitle());
			}
		} catch (Exception e) {
			Log.info("-------- Unable Move from parent to child window " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void WndhndChild_Parent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Move control from Parent to child window /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Move control from parent to child window*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			System.out.println(DRIVER.getTitle());
			DRIVER.switchTo().window(winHandleparent);
			Log.info("* Moved control from child to parent*" + DRIVER.getTitle());
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable Move from child to parent window " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void WndhndParentChild(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Move control from Parent to child window /
		 ****************************************************************************************************/

		try {
			Log.info("*Try to Move control from parent to child window*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			winHandleparent = DRIVER.getWindowHandle();
			for (String winHandlechild : DRIVER.getWindowHandles()) {
				DRIVER.switchTo().window(winHandlechild);
				System.out.println(DRIVER.getTitle());
				Log.info("* Moved control from parent to child *" + DRIVER.getTitle());
			}
		} catch (Exception e) {
			Log.info("-------- Unable Move from parent to child window " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void WndhndChildParent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Move control from Parent to child window /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Move control from parent to child window*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			System.out.println(DRIVER.getTitle());
			DRIVER.switchTo().window(winHandleparent);
			Log.info("* Moved control from child to parent*" + DRIVER.getTitle());
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable Move from child to parent window " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void isAlertPresent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify Alert is present or not /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Alert is present or not*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.alertIsPresent());
			DRIVER.switchTo().alert();

			if (true) {
				Log.info("* Alert is Present* ");
			}
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("--------There is no Alert *" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyTextPresent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify Text Present or not /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify Text is Present*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement Actual = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String ActualText = Actual.getText();

			if (ActualText == data)
				ExecuteTestcase.bResult = true;
			{
				Log.info("Text: " + object + " is Present.");

			}
		} catch (Exception e) {
			Log.info("-------- Text " + object + " is Present or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyCheckboxespresent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify Checkbox Is Present or not in a page
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify Checkbox Is Present*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement checkbox = DRIVER.findElement(By.xpath("//input[@type='checkbox']"));
			if (checkbox.isDisplayed() == true) {
				Log.info("CheckBox: " + object + " is Present.");
			} else {
				Log.info("CheckBox: " + object + " is NOT Present.");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify checkbox " + object + " is Present or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectAllFromListBox(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to select all options from Listbox /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to All option from listbox*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement Listbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Select Sel = new Select(Listbox);
			List<WebElement> elementCount = Sel.getOptions();
			int iSize = elementCount.size();
			for (int i = 0; i < iSize; i++) {
				String sValue = elementCount.get(i).getText();
				System.out.println(sValue);
				Sel.selectByVisibleText(sValue);
			}
			Log.info("All Options " + object + " is Selected");

		} catch (Exception e) {
			Log.info("-------- Unable to Select " + object + " All Options --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void waitForElementPresent(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to waitForElementPresent
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Wait for element present*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(OR.getProperty(object)))));
			Log.info("Element: " + object + " is presented.");
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is not presented --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void GetAllLinks(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Fetch all the Links
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Fetch all the elements*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> links = DRIVER.findElements(By.tagName("a"));
			System.out.println(links.size());
			for (int i = 0; i <= links.size() - 1; i = i + 1) {
				System.out.println(links.get(i).getText());
				System.out.println(links.get(i).getAttribute("href"));

			}
			Log.info("All Links " + object + " are Fetched");

		} catch (Exception e) {
			Log.info("-------- Unable: to Fetch " + object + " All Links --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void VerifyPageTitle(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Fetch all the Links
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to verify PageTitle*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String actualTitle = DRIVER.getTitle();
			String ExpectedTitle = data;

			if (actualTitle.equals(ExpectedTitle)) {
				ExecuteTestcase.bResult = true;
				Log.info("TItle: " + object + " is Present.");
			} else {
				Log.info("Title: " + object + " is NOT Present.");
				ExecuteTestcase.bResult = false;
			}

		} catch (Exception e) {
			Log.info("-------- Unable to verify Title " + object + " is Present or not --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void checkAllCheckBoxes(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to check all the checkboxes
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Check all the CheckBox*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> chkbox = DRIVER.findElements(By.xpath("//input[@type='checkbox']"));
			int iSize = chkbox.size();
			for (int i = 0; i <= iSize; i++) {

				chkbox.get(i).click();
			}
			Log.info("All CheckBox " + object + " are Checked");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("-------- Unable: to Check all " + object + " CheckBoxes --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectRadioButton(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Select Radio Button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Select Radio Button*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objRadioButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));

			if (objRadioButton.isSelected() == true) {
				Log.info("Select Radio Button: " + object);
				ExecuteTestcase.bResult = true;

			} else {
				objRadioButton.click();
				Log.info("Radio Button: " + object + " was already Selected.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Select Radio Button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void deSelectRadioButton(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Deselect Radio Button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Deselect Radio Button*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objRadioButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			if (objRadioButton.isSelected() == true) {
				objRadioButton.click();
				Log.info("Deselect Radio Button: " + object);
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("Radio Button: " + object + " was Deselected. Select Radio Button.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Deselect Radio Button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyImagePresent(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verifyImagePresent /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Image is Present*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement ImageFile = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Boolean ImagePresent = (Boolean) ((JavascriptExecutor) DRIVER).executeScript(
					"return arguments[0].complete && typeof arguments[0].naturalWidth != \"undefined\" && arguments[0].naturalWidth > 0",
					ImageFile);
			if (ImagePresent) {
				Log.info("Image " + object + "is present");
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("Image " + object + "is not present");
				ExecuteTestcase.bResult = false;
			}

		} catch (Exception e) {
			Log.info("--------Unable to verify image" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// Implemented with respect to Checkin Asyst Date Format
	public static void verifyCurrentDateTime(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verifyCurrentDateTime /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Current DateTime*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String actualDateTime = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			System.out.println(actualDateTime);
			DateFormat df = new SimpleDateFormat("hh:mm a, dd MMMMM yyyy");
			Date dateobj = new Date();
			String dateTime = (df.format(dateobj));
			String expectedDateTime = dateTime.replaceFirst("^0*", "");
			System.out.println(expectedDateTime);
			if (actualDateTime.equals(expectedDateTime)) {
				Log.info("Actual and Expected DateTime " + object + " are Equal");
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("Actual and Expected DateTime " + object + " are not Equal");
				ExecuteTestcase.bResult = false;
			}

		} catch (Exception e) {
			Log.info("--------Unable to verify DateTime" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// Implemented with respect to Checkin Asyst
	public static void signature(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Input Signature /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Sign*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Actions actionBuilder = new Actions(DRIVER);
			WebElement canvasElement = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Action drawOnCanvas = actionBuilder.contextClick(canvasElement).moveToElement(canvasElement, 8, 8)
					.clickAndHold(canvasElement).moveByOffset(120, 120).moveByOffset(60, 70).moveByOffset(-140, -140)
					.release(canvasElement).build();
			drawOnCanvas.perform();
			Log.info("---Signed Sucesfully " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to Sign" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void signaturePopup(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Input Signature /
		 **************************************************************************************************/
		try {
			Log.info("*Signature pop up*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//DIV[@id='signaturePopup']"));
			WebElement tmpElement = DRIVER.findElement(By.linkText("Done"));
			tmpElement.click();
			Log.info("---Signed Sucesfully " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to Sign" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void scrollTopOfPage(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to scroll to Top of Page /
		 **************************************************************************************************/
		try {
			Log.info("*Try to scroll to Top of the Page*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			javascript.executeScript("window.scrollTo(0, -document.body.scrollHeight)", "");
			Log.info("---Scrolled to Top of the page " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to scroll to Top of the Page" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void scrollBottomOfPage(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to scroll to Bottom of page /
		 **************************************************************************************************/
		try {
			Log.info("*Try to scroll to Bottom of the Page*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			javascript.executeScript("window.scrollTo(0, document.body.scrollHeight)", "");
			Log.info("---Scrolled to Bottom of the page " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("--------Unable to scroll to Bottom of the Page" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyHorizontalScroll(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Verify Horizontal scroll bar On Page /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Horizontal Scroll Bar Is present on page*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			Boolean horizontal = (Boolean) javascript
					.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
			if (horizontal == true) {
				Log.info("--- Horizontal Scrollbar Is Present On Page " + object + "Sucessfully");
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("---Horizontal Scrollbar not present on page. " + object + "Sucessfully");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("--------Unable to Verify Horizontal" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyVerticalScroll(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Verify Vertical Scroll Bar On Page /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Vertical Scroll Bar Is present on page*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			Boolean vertical = (Boolean) javascript.executeScript(
					"return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
			if (vertical == true) {
				Log.info("--- Vertical Scrollbar Is Present On Page " + object + "Sucessfully");
				ExecuteTestcase.bResult = true;
			} else {
				Log.info("--- Vertical Scrollbar not present on page. " + object + "Sucessfully");
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("--------Unable to Verify Vertical" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void scrollToElementandClick(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to scroll to Element and click /
		 **************************************************************************************************/
		try {
			Log.info("*Try to scroll to Element*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			JavascriptExecutor je = (JavascriptExecutor) DRIVER;
			DRIVER.findElement(By.xpath(OR.getProperty(object))).click();
			Log.info("---Scrolled to Element " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to scroll to to Element" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void scrollToElementandClick1(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to scroll to Element and click /
		 **************************************************************************************************/
		try {
			Log.info("*Try to scroll to Element*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			JavascriptExecutor je = (JavascriptExecutor) DRIVER;

			WebElement ELE = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			je.executeScript("arguments[0]. scrollIntoView(true);", ELE);

			DRIVER.findElement(By.xpath(OR.getProperty(object))).click();
			Log.info("---Scrolled to Element " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to scroll to to Element" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	
	public static void scrollToElementVisible(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to scroll to Element and click /
		 **************************************************************************************************/
		try {
			Log.info("*Try to scroll to Element*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			JavascriptExecutor je = (JavascriptExecutor) DRIVER;

			WebElement ELE = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			je.executeScript("arguments[0]. scrollIntoView(true);", ELE);

			Log.info("---Scrolled to Element " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to scroll to to Element" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	
	/*
	 * public static void verifyPdfData(String object, String data) {
	 *//**************************************************************************************************
		 * /** Des: This function is used to /
		 **************************************************************************************************//*
																											 * try {
																											 * Log.
																											 * info("*Try to VerifyPdf Document*"
																											 * );
																											 * DRIVER.
																											 * manage().
																											 * timeouts(
																											 * ).
																											 * implicitlyWait
																											 * (30,
																											 * TimeUnit.
																											 * SECONDS);
																											 * PDFTextStripper
																											 * pdfStripper
																											 * = null;
																											 * PDDocument
																											 * pdDoc =
																											 * null;
																											 * COSDocument
																											 * cosDoc =
																											 * null;
																											 * String
																											 * parsedText
																											 * = null;
																											 * 
																											 * try {
																											 * 
																											 * String
																											 * getURL =
																											 * DRIVER.
																											 * getCurrentUrl
																											 * (); URL
																											 * url = new
																											 * URL(
																											 * getURL);
																											 * BufferedInputStream
																											 * file =
																											 * new
																											 * BufferedInputStream
																											 * (url.
																											 * openStream
																											 * ());
																											 * PDFParser
																											 * parser =
																											 * new
																											 * PDFParser
																											 * (file);
																											 * parser.
																											 * parse();
																											 * cosDoc =
																											 * parser.
																											 * getDocument
																											 * ();
																											 * pdfStripper
																											 * = new
																											 * PDFTextStripper
																											 * (); //
																											 * pdfStripper
																											 * .
																											 * setStartPage
																											 * (1); //
																											 * pdfStripper
																											 * .
																											 * setEndPage
																											 * (2);
																											 * 
																											 * pdDoc =
																											 * new
																											 * PDDocument
																											 * (cosDoc);
																											 * parsedText
																											 * =
																											 * pdfStripper
																											 * .getText(
																											 * pdDoc); }
																											 * catch
																											 * (MalformedURLException
																											 * e2) {
																											 * System.
																											 * err.
																											 * println("URL string could not be parsed "
																											 * + e2.
																											 * getMessage
																											 * ()); }
																											 * catch
																											 * (IOException
																											 * e) {
																											 * System.
																											 * err.
																											 * println("Unable to open PDF Parser. "
																											 * + e.
																											 * getMessage
																											 * ()); try
																											 * { if
																											 * (cosDoc
																											 * != null)
																											 * cosDoc.
																											 * close();
																											 * if (pdDoc
																											 * != null)
																											 * pdDoc.
																											 * close();
																											 * } catch
																											 * (Exception
																											 * e1) { e.
																											 * printStackTrace
																											 * (); } }
																											 * 
																											 * System.
																											 * out.
																											 * println(
																											 * "+++++++++++++++++"
																											 * );
																											 * System.
																											 * out.
																											 * println(
																											 * parsedText
																											 * );
																											 * System.
																											 * out.
																											 * println(
																											 * "+++++++++++++++++"
																											 * );
																											 * 
																											 * if
																											 * (parsedText
																											 * .contains
																											 * (data)) {
																											 * System.
																											 * out.
																											 * println("Pdf data is matching"
																											 * ); Log.
																											 * info("--PDF "
																											 * + data +
																											 * " is matching"
																											 * );
																											 * ExecuteTestcase
																											 * .bResult
																											 * = true; }
																											 * else {
																											 * System.
																											 * out.
																											 * println("Pdf data is not matching"
																											 * ); Log.
																											 * info("--PDF "
																											 * + data +
																											 * " is not matching"
																											 * );
																											 * ExecuteTestcase
																											 * .bResult
																											 * = false;
																											 * }
																											 * 
																											 * } catch
																											 * (Exception
																											 * e) { Log.
																											 * info("--------Unable to Verify PDf "
																											 * + data +
																											 * " --------"
																											 * + e.
																											 * getMessage
																											 * ());
																											 * ExecuteTestcase
																											 * .bResult
																											 * = false;
																											 * } }
																											 */

	public void startRecording(String object, String data) throws Exception {

		/**************************************************************************************************
		 * /** Des: This function is used to Start Recording Test Execution Scripts /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Start Recording*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			File file = new File(data);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screenSize.width;
			int height = screenSize.height;

			Rectangle captureSize = new Rectangle(0, 0, width, height);

			GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
					.getDefaultConfiguration();
			// for MP4 use MIME_MP4 in place of MIME_AVI
			this.screenRecorder = new DesiredLocation(gc, captureSize,
					// File Format

					new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
					// the output format for screen capture
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
							CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey,
							Rational.valueOf(15), QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
					// the output format for mouse capture
					new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
					null, file, "TestScript");
			this.screenRecorder.start();
			Log.info("--Videos is Recording test script in " + data + "Location");
			ExecuteTestcase.bResult = true;

		}

		catch (Exception e) {
			Log.info("--------Unable to Record test Scripts in  " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public void stopRecording(String object, String data) throws Exception {

		/**************************************************************************************************
		 * /** Des: This function is used to Stop Recording Test Execution Scripts /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Stop Recording*");
			this.screenRecorder.stop();
			Log.info("--Stopped Recording in " + data + "Location");
			ExecuteTestcase.bResult = true;

		}

		catch (Exception e) {
			Log.info("--------Unable to Stop Record test Scripts in  " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public void javascriptclick(String object, String data) throws Exception {

		/**************************************************************************************************
		 * /** Des: This function is used to click element /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click the Element*");
			WebElement elem = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			System.out.println(elem);
			String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";

			((JavascriptExecutor) DRIVER).executeScript(js, elem);

			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			javascript.executeScript("arguments[0].click();", elem);

			Log.info("--Clicked the element ");
			ExecuteTestcase.bResult = true;

		}

		catch (Exception e) {
			Log.info("--------Unable to Stop Record test Scripts in  " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public void javascriptclickNodes(String object, String data) throws Exception {

		/**************************************************************************************************
		 * /** Des: This function is used to click element /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click the Element*");
			DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
			String element = (OR.getProperty(object));
			String fullXpath = String.format(element, data);
			System.out.println(fullXpath);
			WebElement elem = DRIVER.findElement(By.xpath(OR.getProperty(fullXpath)));
			String js = "arguments[0].style.height='auto'; arguments[0].style.visibility='visible';";
			((JavascriptExecutor) DRIVER).executeScript(js, elem);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			javascript.executeScript("arguments[0].click();", elem);

			Log.info("--Clicked the element ");
			ExecuteTestcase.bResult = true;

		}

		catch (Exception e) {
			Log.info("--------Unable to Stop Record test Scripts in  " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	// This method is applicable for checkinasyst

	public static void chkcheckbox(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to check Checkbox /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Check CheckBox*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objCheckbox.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-checkbox-on";

			if (Actualclass.equals(Expectedclass) == false) {
				objCheckbox.click();
				ExecuteTestcase.bResult = true;
				Log.info("Checked CheckBox: " + object);
			} else {
				Log.info("CheckBox: " + object + " was already checked .");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to check CheckBox: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst

	public static void selectRadiobtn(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to select radio button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to select Radio button*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objCheckbox.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-radio-on";

			if (Actualclass.equals(Expectedclass) == false) {
				objCheckbox.click();
				ExecuteTestcase.bResult = true;
				Log.info("selected Radio button: " + object);
			} else {
				Log.info("Radio button: " + object + " was already selected .");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to select Radio button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void unchkcheckbox(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to uncheck Checkbox /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Check CheckBox*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objRadiobtn = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objRadiobtn.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-checkbox-off";

			if (Actualclass.equals(Expectedclass) == false) {
				objRadiobtn.click();
				ExecuteTestcase.bResult = true;
				Log.info("Checked CheckBox: " + object);
			} else {
				Log.info("CheckBox: " + object + " was already checked .");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to check CheckBox: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst

	public static void deselectRadiobtn(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to deselect radio button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to select Radio button*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objRadiobtn = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objRadiobtn.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-radio-off";

			if (Actualclass.equals(Expectedclass) == false) {
				objRadiobtn.click();
				ExecuteTestcase.bResult = true;
				Log.info("selected Radio button: " + object);
			} else {
				Log.info("Radio button: " + object + " was already selected .");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to select Radio button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst

	public static void clickButtonYes(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to click Button yes /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click button*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "yes_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == false) {
				objButton.click();
				ExecuteTestcase.bResult = true;
				Log.info("Clicked button: " + object);
			} else {
				Log.info("Button: " + object + " was already Clicked.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Click Yes button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void clickButtonNo(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to click No Button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click button*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "no_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == false) {
				objButton.click();
				ExecuteTestcase.bResult = true;
				Log.info("Clicked NO button: " + object);
			} else {
				Log.info("NO Button: " + object + " was already Clicked.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Click NO button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void popUp(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to identify pop up /
		 **************************************************************************************************/
		try {
			Log.info("*Signature pop up*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath(OR.getProperty(object)));
			Log.info("---POPUP " + object + "Sucessful");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to POP UP" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void Screening(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to invoke screening APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to invoke screening*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]//img[@data-buttontype='btnapptworkflow']"))
					.click();
			Log.info("Invoked Screening: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable invoke screening : " + data + " to element " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// btnprecheckin

	public static void Screening2(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to invoke screening APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to invoke screening*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]//img[@data-buttontype='btnprecheckin']"))
					.click();
			Log.info("Invoked Screening: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable invoke screening : " + data + " to element " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void screeningPagination(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to invoke screening APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to invoke screening*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			List<WebElement> pagination = DRIVER
					.findElements(By.xpath("//div[@id='AppointmentTheme_ThemeControlID_pagingcontrols']/ul/li"));
			System.out.println(pagination.size() - 2);

			for (int i = 2; i <= pagination.size() - 1; i++)

			{
				Boolean isPresent = DRIVER
						.findElements(
								By.xpath("//div[@data-appid=" + data + "]//img[@data-buttontype='btnapptworkflow']"))
						.size() > 0;

				System.out.println(isPresent);

				if (isPresent == true) {
					WebElement Screening = DRIVER.findElement(
							By.xpath("//div[@data-appid=" + data + "]//img[@data-buttontype='btnapptworkflow']"));
					System.out.println("testing");
					Screening.click();

					break;
				} else {
					WebElement pageclick = DRIVER.findElement(By.xpath(
							".//*[@id='AppointmentTheme_ThemeControlID_repeaterPager_ctl0" + i + "_btnPageIndex']"));
					System.out.println(pageclick);
					pageclick.click();
					System.out.println(i);
				}

			}

			Log.info("Invoked Screening: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable invoke screening : " + data + " to element " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void eligibilityBenefit(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Trigger eligibility benefits based on
		 * APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Trigger Eligibility benefits*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]//input[@id='btnEligbility']")).click();
			Log.info("Triggered Eligibility benefits: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable to trigger Eligibility benefits " + data + " to element " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void checkEligibility(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Trigger checkEligibility Status based on
		 * APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Trigger checkEligibility*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]//input[@value='Check Eligibility']")).click();
			Log.info("Triggered Check Eligibility Status: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable to trigger checkEligibility Status : " + data + " to element " + object
					+ " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void acceptPayment(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Click acceptPayment based on APPointment ID
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to input value*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]//input[@value='Accept Payment']")).click();
			Log.info("Inputted value: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void reconcilePopUp(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Click reconcilePopUp based on APPointment
		 * ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to input value*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//span[@data-appid=" + data + "]/img")).click();
			Log.info("Inputted value: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void verifyDropDown(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify DropDown based on APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to select by visible text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			Select selectAction = new Select(DRIVER.findElement(By.xpath(OR.getProperty(object))));
			String actualText = selectAction.getAllSelectedOptions().get(0).getText();
			if (actualText.equals(data)) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + object + "and actual text: " + data + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Actual text on " + object + "and actual text: " + data + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + object + "and actual text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	// This method is applicable for checkinasyst
	public static void verifyupdateButtonclicked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Update button is clicked or not
		 * (specific to CheckInasyst) /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify Update button is clicked *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "yes_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("Update button was already clicked: " + object);

			}
		} catch (Exception e) {
			Log.info("-------- Unable to click update button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	// This method is applicable for checkinasyst
	public static void verifyYesButtonclicked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify yes button is clicked or not (specific
		 * to CheckInasyst) /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify Yes button is clicked *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "yes_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("Yes button was already clicked: " + object);

			}
		} catch (Exception e) {
			Log.info("-------Unable to click yes button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	// This method is applicable for checkinasyst
	public static void verifyNoButtonClicked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify NO button is clicked or not (specific
		 * to CheckInasyst) /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify NO button is clicked *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "no_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("No Button was already clicked: " + object);

			}
		} catch (Exception e) {
			Log.info("-------- Unable to click NO button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	// This method is applicable for checkinasyst
	public static void verifyCheckboxchecked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify checkbox is checked or not (specific
		 * to CheckInasyst)
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify checkBox is checked *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objCheckbox.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-checkbox-on";
			System.out.println(Actualclass);
			System.out.println(Expectedclass);

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("Checkbox was already checked: " + object);

			}

			else {
				ExecuteTestcase.bResult = false;
				Log.info("Checkbox is not checked: " + object);

			}

		} catch (Exception e) {
			Log.info("-------- Unable to Verify checkbox is checked: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyCheckboxUnchecked(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify checkbox is unchecked or not
		 * (specific to CheckInasyst)
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify checkBox is checked *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objCheckbox.getAttribute("Class");
			System.out.println(Actualclass);

			// String Expectedclass = "ui-icon ui-icon-checkbox-off ui-icon-shadow";
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-checkbox-off";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("Checkbox is unchecked: " + object);

			}

			else {
				ExecuteTestcase.bResult = false;
				Log.info("Checkbox is  checked: " + object);

			}

		} catch (Exception e) {
			Log.info("-------- unable to verify uncheck Checkbox " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-checkbox-off";
		}
	}

	// This method is applicable for checkinasyst
	public static void verifySelectRadiobtn(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Radio button is selected or not
		 * (Specific to CheckinAsyst)
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify radio button is selected *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objCheckbox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objCheckbox.getAttribute("Class");
			String Expectedclass = "ui-icon ui-icon-shadow ui-icon-radio-on";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("Checkbox was already checked: " + object);

			}
		} catch (Exception e) {
			Log.info("-------- Unable to Verify Radio button is selected: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void verifyApptStatus(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify App Status based on appointment ID
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify radio button is selected *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String xpath1 = "//div[@data-appid=" + data + "]";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement Status = DRIVER.findElement(By.xpath(fullxpath));

			Boolean ApptStatus = Status.isDisplayed();
			if (ApptStatus == true) {
				ExecuteTestcase.bResult = true;
				Log.info(" verified Appt Status: " + object);
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Verify Appt status: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyParagraphText(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Text in paragraph with multiple new
		 * line
		 * 
		 * /
		 **************************************************************************************************/
		Boolean ispresent = true;
		try {

			Log.info("*Try to Verify Text in the Paragraph*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement obj = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String ActualText = obj.getText();
			System.out.println(ActualText);
			String Expected = data;
			List<String> ParagraphText = Arrays.asList(Expected.split(","));
			for (String Text : ParagraphText) {
				System.out.println(Text);
				if (!ActualText.contains(Text))

				{
					ispresent = false;
					break;

				}
			}

			if (ispresent) {
				Log.info("It is matching " + object);
				ExecuteTestcase.bResult = true;
				Log.info("actual text on " + ActualText + "and Expected text: " + data + " are the same.");
			}

			else

			{
				Log.info("It is not matching " + object);
				ExecuteTestcase.bResult = false;
				Log.info("actual text on " + ActualText + "and Expected text: " + data + " are the not same.");
			}

		} catch (Exception e) {
			Log.info("-------- Unable to Verify Text in paragraph: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyDB(String object, String data) throws InterruptedException {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] Splitcoln = data.split(":");
		String Query = Splitcoln[0];
		String expectedData = Splitcoln[1];
		System.out.println(Query);
		String actualData = null;
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);

			if (rs.next() == false) {
				System.out.println("ResultSet is empty");
				ExecuteTestcase.bResult = false;
			}

			else {
				do {
					actualData = rs.getString(1);

				} while (rs.next());
				{
					System.out.println("Expected data " + expectedData);
					System.out.println("Actual data " + actualData);
					if ((actualData.trim()).equals(expectedData.trim())) {
						ExecuteTestcase.bResult = true;
						System.out.println("It is matching");
						Log.info("It is matching");
					} else {
						System.out.println("It is not matching");
						Log.info("It is not matching");
						ExecuteTestcase.bResult = false;
					}
				}
			}

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();

		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyDBExcel(String object, String data)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value with the excel sheet
		 * 
		 * /
		 **************************************************************************************************/
		String Excpecteddata = null;
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String datafilepath = Constants.DBdataFilepath;
		String ResultFilePath = Constants.DBResultFilepath;
		String[] parts = data.split(":");
		String query = parts[0];
		String sheetNo = parts[1];

		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> aslist = new ArrayList<>();
		List<String> excellist = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		ArrayList<String> notMatchedData = new ArrayList<>();
		List<String> allNotMatchedData = new ArrayList<String>();
		System.out.println(query);
		java.sql.Connection conn = null;
		int k;
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
				values = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber; i++) {

					if (rs.getString(i) != null) {

						rs.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					values.add(rs.getString(i));
				}
				System.out.println("Stored Values  in DB:" + " " + values);

				list.addAll(values);

			}
			System.out.println("Stored Values in DB:" + " " + list);

			// excel sheet code

			for (int i = 0; i < list.size(); i++) {
			}
			int counter = 0;
			int counterADD = 0;
			FileInputStream fis = new FileInputStream(datafilepath);
			Workbook wb = WorkbookFactory.create(fis);
			Sheet sh = wb.getSheet(sheetNo);
			int totalrow = sh.getLastRowNum();
			System.out.println("Total Number OF Row is : " + totalrow);
			for (int i = 1; i <= totalrow; i++) {

				Row row = sh.getRow(i);
				int totalcoloumn = row.getLastCellNum();
				System.out.println("Total Numer Of Column is :" + totalcoloumn);

				for (int j = 0; j < totalcoloumn; j++) {
					// Excpecteddata = row.getCell(j).getStringCellValue();
					if (row.getCell(j) != null) {
						switch (row.getCell(j).getCellType()) {
						case Cell.CELL_TYPE_STRING:
							Excpecteddata = row.getCell(j).getStringCellValue();
							System.out.println(Excpecteddata);
							break;
						case Cell.CELL_TYPE_NUMERIC: {
							Double value = row.getCell(j).getNumericCellValue();
							Long longValue = value.longValue();
							Excpecteddata = new String(longValue.toString());
						}
							break;
						case Cell.CELL_TYPE_BLANK:
							boolean value = row.getCell(j).getBooleanCellValue();
							System.out.println(value);
						}

					}
					excellist.iterator();
					excellist.add(Excpecteddata);

				}
				System.out.println("Expected data values" + excellist + "row mumber");

				// Compairing DataBase value and Excelsheet value
				// ------------------------->

				for (k = 0; k < excellist.size(); k++) {
					System.out.println("EXPECTED VALUE = " + excellist.get(k) + " DATABASE VALUE = "
							+ list.get((i - 1) * totalcoloumn + k)); // list.get((i-1)*5//
					// + k)
					if (!StringUtils.isNullOrEmpty(list.get((i - 1) * totalcoloumn + k))) {
						if (list.get((i - 1) * totalcoloumn + k).equalsIgnoreCase(excellist.get(k))) {
							System.out.println("Element is Equal");
							Cell cell = row.createCell(totalcoloumn);
							cell.setCellType(CellType.STRING);
							cell.setCellValue("PASS");
							FileOutputStream fos = new FileOutputStream(ResultFilePath);
							wb.write(fos);
							fos.close();
							System.out.println("Excel File Written.");
							ExecuteTestcase.bResult = true;
							Log.info("TestCase Passed because all elements are same");
						} else {
							System.out.println("Element not equal  ");
							ExecuteTestcase.bResult = false;
							Log.info("*The Element are not same" + excellist.get(k));
							counter++;
							counterADD = counter;
							counterADD++;
							notMatchedData.add(excellist.get(k));
						}
					}
				}
				allNotMatchedData.addAll(notMatchedData);
				if (counter > 0) {
					Cell cell = row.createCell(totalcoloumn);
					cell.setCellType(CellType.STRING);
					cell.setCellValue("FAIL" + "  " + counter + " " + "DB Data is not equal" + allNotMatchedData);
					Log.info("FAIL" + "  " + counter + " " + "DB Data is not equal" + allNotMatchedData);
					FileOutputStream fos = new FileOutputStream(ResultFilePath);
					wb.write(fos);
					fos.close();
					System.out.println("Excel File Written.");
					System.out.println("All elements are not same TestCase Failed");
					ExecuteTestcase.bResult = false;
					counter = 0;
					allNotMatchedData.clear();
					notMatchedData.clear();
				}
				if (counterADD > 0) {
					ExecuteTestcase.bResult = false;
					Log.info("TestCase failed because all elements are not equals");
				}
				excellist.clear();
			}

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

	public static void allergyMedicationDropdown(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to select Allergy_medication Drop down value
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Select value from the Dropdown*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String xpathText = parts[0];
			String value = parts[1];
			String xpath1 = "//div[@id='clinicaldata']//div[contains(text(),'" + xpathText + "')]/..";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement Onset = DRIVER.findElement(By.xpath(fullxpath));
			Select selectAction = new Select(Onset);
			selectAction.selectByVisibleText(value);
			Log.info("Selected value from the Dropdown");

		} catch (Exception e) {
			Log.info("-------- Unable to Select value from the Dropdown: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void allergyMedicationOnset(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Allergy_medication onset date
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Input Onset value*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String xpathText = parts[0];
			String input = parts[1];
			String xpath1 = "//div[@id='clinicaldata']//div[contains(text(),'" + xpathText + "')]/..";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement Onset = DRIVER.findElement(By.xpath(fullxpath));
			Onset.sendKeys(input);
			ExecuteTestcase.bResult = true;
			Log.info("Inputted Onset date");

		} catch (Exception e) {
			Log.info("-------- Unable to Input text: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void execQuery(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);
			System.out.println("Query Executed Successfully");

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = true;
			ex.printStackTrace();
		} catch (SQLException ex) {

			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyDBAnswers(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data base value with the expected data
		 * for answertemp(8) col data
		 * 
		 * /
		 **************************************************************************************************/
		Boolean ispresent = true;
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] Splitcoln = data.split(":");
		String Query = Splitcoln[0];
		System.out.println(Query);
		String Text = Splitcoln[1];
		String[] Splitcom = Text.split(",");
		String Expcol10 = Splitcom[0].trim();
		String ExpCol11 = Splitcom[1].trim();
		String Expcol2 = Splitcom[2].trim();
		String ExpCol14 = Splitcom[3].trim();
		String ExpCol15 = Splitcom[4].trim();
		String ExpCol23 = Splitcom[5].trim();
		String ExpCol273 = Splitcom[6].trim();
		String ExpCol48 = Splitcom[7].trim();

		java.sql.Connection conn = null;
		try {
			Log.info("*Try to Verify Answer Temp");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);
			while (rs.next()) {
				System.out.println(
						rs.getString(1) + "  " + rs.getString(2) + "  " + rs.getString(3) + "  " + rs.getString(4));

				String actualcol10 = rs.getString(1);
				String actualCol11 = rs.getString(2);
				String actualCol12 = rs.getString(3);
				String actualCol14 = rs.getString(4);
				String actualCol15 = rs.getString(5);
				String actualCol23 = rs.getString(6);
				String actualCol273 = rs.getString(7);
				String actualCol48 = rs.getString(8);

				if (!actualcol10.equals(Expcol10) || !(actualCol11.equals(ExpCol11)
						|| !(actualCol12.equals(Expcol2) || !(actualCol14.equals(ExpCol14)
								|| !(actualCol15.equals(ExpCol15) || !(actualCol23.equals(ExpCol23)
										|| !(actualCol273.equals(ExpCol273) || !(actualCol48.equals(ExpCol48)))))))))

				{
					ispresent = false;
					break;

				}

			}

			if (ispresent) {
				Log.info("It is matching");
				System.out.println("It is matching " + " passed ");
				ExecuteTestcase.bResult = true;
			}

			else {
				Log.info("It is not matching");
				System.out.println("It is not matching" + " Failed");
				ExecuteTestcase.bResult = false;
			}

		} catch (ClassNotFoundException ex) {
			Log.info("Could not find database driver class");
			ex.printStackTrace();
		} catch (SQLException ex) {
			Log.info("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyDBBeforeEligibilityCheck(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Fetch the latest transaction id
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;
		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				Transactionid = rs.getInt(1);
				System.out.println(Transactionid);
				ExecuteTestcase.bResult = true;
				System.out.println("Value is Stored");
				Log.info("Latest Value " + Transactionid + "is stored");
			}

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyDBAfterElgibilty(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				int actualTranasctionid = rs.getInt(1);
				String actualResponsePayLoad = rs.getString(2);
				String actualRequestPayLoad = rs.getString(3);
				String actualFileStreamIdentifier = rs.getString(4);
				System.out.println(actualTranasctionid);
				if ((actualTranasctionid > Transactionid)
						&& (actualResponsePayLoad != null && !actualResponsePayLoad.isEmpty())
						&& (actualRequestPayLoad != null && !actualRequestPayLoad.isEmpty())
						&& (actualFileStreamIdentifier != null && !actualFileStreamIdentifier.isEmpty()))

				{
					ExecuteTestcase.bResult = true;
					System.out.println("It is matching");
					Log.info("It is  matching");

				} else {
					System.out.println("It is not matching");
					Log.info("value" + actualTranasctionid + " is  matching");
					ExecuteTestcase.bResult = false;
				}
			}

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void allergyMedication_Onset(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Allergy_medication onset date
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Input Onset value*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String xpathText = parts[0];
			String input = parts[1];
			String xpath1 = "//div[@id='clinicaldata']//div[contains(text()," + xpathText + "]/..";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement Onset = DRIVER.findElement(By.xpath(fullxpath));
			Onset.sendKeys(input);
			ExecuteTestcase.bResult = true;
			Log.info("Inputted Onset date");

		} catch (Exception e) {
			Log.info("-------- Unable to Input Onset value* : " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void allergyMedication_Dropdwon(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to select Allergy_medication Drop down value
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Select value from the Allergy_medication Dropdown*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String xpathText = parts[0];
			String value = parts[1];
			String xpath1 = "//div[@id='clinicaldata']//div[contains(text()," + xpathText + "]/..";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement Onset = DRIVER.findElement(By.xpath(fullxpath));
			Select selectAction = new Select(Onset);
			selectAction.selectByVisibleText(value);
			Log.info("Selected value from the Dropdown");

		} catch (Exception e) {
			Log.info("-------- Unable to Select value from the Allergy_medication  Dropdown: " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void PatientDashboard(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to select PATIENTHYPERLINK, APPTDETAILS,
		 * NOTIFICATION with respective objects (Specific to Dashboard) /
		 **************************************************************************************************/
		try {
			Log.info("*Try to click PATIENTHYPERLINK, APPTDETAILS, NOTIFICATION *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String xpath1 = "//div[@data-appid=" + data + "]";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			WebElement patientDashboard = DRIVER.findElement(By.xpath(fullxpath));
			patientDashboard.click();
			ExecuteTestcase.bResult = true;
			Log.info(" verified and clicked on PATIENTHYPERLINK, APPTDETAILS, NOTIFICATION: " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to patientDashboard: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
		// div[@data-appid='5953']//img[@data-buttontype='btnapptworkflow']
	}

	public static void DashboardVerifyText(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verifytext based on appid
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Patient Dashboard *");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String xpath1 = "//div[@data-appid=" + data + "]";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			String actual = DRIVER.findElement(By.xpath(fullxpath)).getText();
			String actualText = actual.trim();
			if (actualText.equals(data)) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + object + "and actual text: " + data + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Actual text on " + object + "and actual text: " + data + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + object + "and actual text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyPharmacy(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Pharmacy inbound is there . if
		 * inbound validate inbound pharmacy /
		 **************************************************************************************************/
		Boolean ispresent = true;
		try {
			Log.info("*Try to Verify pharmacy with and without inbound*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = object.split(",");
			String noRecordxpath = parts[0];
			String Pharmacyxpath2 = parts[1];
			String[] Splitcol = data.split(":");
			String noRecordText = Splitcol[0];
			String pharmacyText = Splitcol[1];

			Boolean NoRecord = DRIVER.findElements(By.xpath(OR.getProperty(noRecordxpath))).size() == 1;
			System.out.println(NoRecord);

			if (NoRecord) {
				String actual = DRIVER.findElement(By.xpath(OR.getProperty(noRecordxpath))).getText();
				String NorecordText = actual.trim();
				if (NorecordText.equals(noRecordText)) {
					Log.info("-------- It is matching: " + object);
					ExecuteTestcase.bResult = true;

				}
			} else {

				WebElement text = DRIVER.findElement(By.xpath(OR.getProperty(Pharmacyxpath2)));
				String ActualText = text.getText();
				System.out.println(ActualText.trim());

				List<String> ParagraphText = Arrays.asList(pharmacyText.split(","));
				for (String Text : ParagraphText) {
					System.out.println(Text);
					if (!ActualText.contains(Text))

					{
						ispresent = false;
						break;

					}
				}

				if (ispresent) {
					Log.info("-------- It is matching: " + object);
					ExecuteTestcase.bResult = true;
				}

				else

				{
					Log.info("-------- It is not matching: " + object);
					ExecuteTestcase.bResult = false;
				}

			}

		}

		catch (Exception e) {
			Log.info("-------- Unable to verify Pharmacy : " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void PharmacyUpdate(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to click on pharmacy from list based on the
		 * data based /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify Patient Dashboard *");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String element = "//tr//a[text()='" + data + "']/../..";
			WebElement Userlist = DRIVER.findElement(By.xpath(element));
			Userlist.click();
			ExecuteTestcase.bResult = true;
			Log.info(" Clicked on pharmacy from the link : " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to Click on pharmacy: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyDBapptcount(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data base value with the application
		 * apppointment count
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Log.info("*Try to Verify Data base value with the application apppointment count *");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				System.out.println(rs.getString(1));
				String DBdata = rs.getString(1);
				String element = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
				String[] parts = element.split(":");
				String count = parts[0];
				String inprogressCount = parts[1];
				System.out.println(inprogressCount);
				System.out.println("Expected data " + inprogressCount);
				System.out.println("Actual data " + DBdata);
				if (inprogressCount.equals(DBdata)) {
					ExecuteTestcase.bResult = true;
					System.out.println("It is matching");
				} else {
					System.out.println("It is not matching");
					ExecuteTestcase.bResult = false;
				}
			}

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
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void removeCharString(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to remove the character String based on data
		 * passed /
		 **************************************************************************************************/
		int index = Integer.parseInt(data);

		try {
			Log.info("*Try to remove character from string by index value *");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String Text = DRIVER.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			System.out.println(Text);
			String removedchar = Text.substring(0, Text.length() - index);
			System.out.println(removedchar);
			ExecuteTestcase.bResult = true;
			Log.info(" Removed character bsed on the index : " + object);
		} catch (Exception e) {
			Log.info("-------- Unable to remove character from string by index value" + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyFirstChar(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify first character of a string is in
		 * upper case or not /
		 **************************************************************************************************/

		try {
			Log.info("*Try to First character of a String is in UpperCase*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String Text = DRIVER.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			boolean output = (Character.isUpperCase(Text.charAt(0)));
			if (output) {
				Log.info("First character of a String is in UpperCase*");
				ExecuteTestcase.bResult = true;
			}

			else {
				Log.info("First character of a String is in Lower Case*" + object);
				ExecuteTestcase.bResult = false;
			}

		} catch (Exception e) {
			Log.info("-------- Unable to verify First character of a string " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void insertString(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to insert String in to another String at
		 * specified index position /
		 **************************************************************************************************/
		String[] parts = data.split(",");
		String indx = parts[0];
		String dataText = parts[1];
		int index = Integer.parseInt(indx);

		try {
			Log.info("*Try to insert a String at specified index position*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String Text = DRIVER.findElement(By.xpath(OR.getProperty(object))).getAttribute("value");
			StringBuffer str1 = new StringBuffer(Text);
			str1.insert(index, dataText);
			System.out.println(str1);
			WebElement insert = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			insert.clear();
			insert.sendKeys(str1);
			Log.info("inserted a String at specified index position*" + object);
			ExecuteTestcase.bResult = true;
		}

		catch (Exception e) {
			Log.info("-------- Unable to insert a String at specified index position* " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void popupVisible(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify pop up Visible /
		 **************************************************************************************************/
		try {
			Log.info("*Try to verify pop up visible *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("style");
			System.out.println(Actualclass);
			String Expectedclass = "display: inline-block; visibility: visible;";

			if (Actualclass.equals(Expectedclass) == true) {
				ExecuteTestcase.bResult = true;
				Log.info("popup is visible: " + object);
			}

		} catch (Exception e) {
			Log.info("--------popup is not visible: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	// This is applicable only for checkinasyst
	public static void popupNotVisible(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify pop up /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click button*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("style");
			System.out.println(Actualclass);
			String Expectedclass = "display: inline-block; visibility: visible;";

			if (Actualclass.equals(Expectedclass) == false) {
				ExecuteTestcase.bResult = true;
				Log.info("popup is not visible: " + object);
			}

		} catch (Exception e) {
			Log.info("--------popup is visible: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyTextByComma(String object, String data) {
		/*****************************************************************************************************************
		 * /** Des: This function is used to get text from element and split by comma
		 * and verify the first string index value /
		 ****************************************************************************************************************/
		try {
			Log.info("*Try to split by comma and verify text*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String actual = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
			String pcpNames[] = actual.split(",");
			String updatedPcp = pcpNames[0];
			String actualText = updatedPcp.trim();
			if (actualText.equals(data.trim())) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + object + "and actual text: " + data + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Actual text on " + object + "and actual text: " + data + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Actual text on " + object + "and actual text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyAllergyMed(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Allergies /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify pharmacy with and without inbound*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = object.split(",");
			String noRecordxpath = parts[0];
			String AllergyMedxpath = parts[1];

			Boolean NoRecord = DRIVER.findElements(By.xpath(OR.getProperty(noRecordxpath))).size() == 1;
			System.out.println(NoRecord);

			if (NoRecord) {
				String actual = DRIVER.findElement(By.xpath(OR.getProperty(noRecordxpath))).getText();
				String NorecordText = actual.trim();
				if (NorecordText.equals(data)) {
					Log.info("-------- It is matching: " + object);
					ExecuteTestcase.bResult = true;
				}
			} else {

				Boolean AllergyMed = DRIVER.findElement(By.xpath(OR.getProperty(AllergyMedxpath))).isDisplayed();

				if (AllergyMed)

				{
					Log.info("-------Allergy/Medication is present:" + object);
					ExecuteTestcase.bResult = true;
				}

				else

				{
					Log.info("-------- Allergy/Medication  is not present: " + object);
					ExecuteTestcase.bResult = false;
				}

			}
		}

		catch (Exception e) {
			Log.info("-------- Unable to verify Allergy/medication : " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void verifyAppDBStgTrans(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify verifyAppDB StgTrans
		 * 
		 * /
		 **************************************************************************************************/
		Boolean ispresent = true;
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;
		String IsProcessedFlag = "Y";
		String IsTempdataProcessed = "Y";

		java.sql.Connection conn = null;
		try {
			Log.info("To verify Stg Transaction tabe*");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(rs.getString(1) + "  " + rs.getString(2));

				String Actualfirstcolumn = rs.getString(1);
				String Actualsecondcolumn = rs.getString(2);

				if (!Actualfirstcolumn.equals(IsProcessedFlag) || !(Actualsecondcolumn.equals(IsTempdataProcessed))) {
					ispresent = false;
					break;

				}

			}

			if (ispresent) {
				Log.info("It is matching");
				System.out.println("It is matching " + " passed ");
				ExecuteTestcase.bResult = true;
			}

			else {
				Log.info("It is not matching");
				System.out.println("It is not matching" + " Failed");
				ExecuteTestcase.bResult = false;
			}

		} catch (ClassNotFoundException ex) {
			Log.info("Could not find database driver class");
			ex.printStackTrace();
		} catch (SQLException ex) {
			Log.info("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void exeQuery(String object, String data) {
		/**************************************************************************************************
		 * /* Des: This function is used execute Query
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;
		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			boolean gotResults = stmt.execute(Query);
			ResultSet rs = null;
			if (!gotResults) {
				ExecuteTestcase.bResult = true;
				System.out.println("No results returned");
			}

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (Exception e) {
			System.out.println("An error occurred.");
			ExecuteTestcase.bResult = false;
			e.printStackTrace();
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyDBApp1(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data base value with the application
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Log.info("To verify DB with application");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				System.out.println(rs.getString(1));
				String DBdata = rs.getString(1);
				String elementText = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();

				String APPData = elementText.replace("$", "");
				String ActualDBdata = (DBdata.substring(0, DBdata.length() - 2));
				System.out.println("Testing" + ActualDBdata);
				if ((APPData.trim()).equals(ActualDBdata.trim())) {
					ExecuteTestcase.bResult = true;
					System.out.println("DBdata" + ActualDBdata + "APPDATA" + APPData + "");
					System.out.println("It is matching");
					Log.info("It is matching");
				} else {
					System.out.println("DBdata" + ActualDBdata + "APPDATA" + APPData + "");
					System.out.println("It is not matching");
					Log.info("It is not matching");
					ExecuteTestcase.bResult = false;
				}
			}

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

	/*
	 * public static void launchHL7Inpector(String object, String data) {
	 *//**************************************************************************************************
		 * /** Des: This function is used for launching HL7 Inspector
		 * 
		 * /
		 **************************************************************************************************//*
																											 * String[]
																											 * dataValue
																											 * = data.
																											 * split(";"
																											 * ); String
																											 * sourceMessage
																											 * =
																											 * dataValue
																											 * [0];
																											 * String
																											 * HL7MessagePath
																											 * =
																											 * dataValue
																											 * [1];
																											 * 
																											 * 
																											 * Process p
																											 * = null;
																											 * try {
																											 * ProcessBuilder
																											 * pb = new
																											 * ProcessBuilder
																											 * ("java",
																											 * "-jar",
																											 * "./HL7Inspector/hl7inspector.jar"
																											 * ); p =
																											 * pb.start(
																											 * );
																											 * 
																											 * //
																											 * Process
																											 * autoIt =
																											 * Runtime.
																											 * getRuntime
																											 * ().exec(
																											 * ".\\Library\\LaunchApp.exe"+" "
																											 * +OR.
																											 * getProperty
																											 * (object)
																											 * +" "
																											 * +sourceMessage+" "
																											 * +HL7MessagePath
																											 * );
																											 * 
																											 * Thread.
																											 * sleep(
																											 * 5000);
																											 * 
																											 * Screen
																											 * screen =
																											 * new
																											 * Screen();
																											 * 
																											 * Pattern
																											 * senderIcon
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/senderIcon.JPG"
																											 * );
																											 * Pattern
																											 * senderConfigBTN
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/senderConfigBTN.JPG"
																											 * );
																											 * Pattern
																											 * IPPortTextBox
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/portConfigTxtBox.JPG"
																											 * );
																											 * Pattern
																											 * sendOptionDialogOkBTN
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/sendOptionsOkBtn.JPG"
																											 * );
																											 * Pattern
																											 * hl7ImporterICON
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/openImportHL7BTN.JPG"
																											 * );
																											 * Pattern
																											 * choseFilePath
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/choseFilePath.JPG"
																											 * );
																											 * Pattern
																											 * openBTN =
																											 * new
																											 * Pattern(
																											 * "./sikuliImages/choseFileOpenBTN.JPG"
																											 * );
																											 * Pattern
																											 * hl7File1
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7File1.JPG"
																											 * );
																											 * Pattern
																											 * hl7File2
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7File2.JPG"
																											 * );
																											 * Pattern
																											 * hl7File3
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7File3.JPG"
																											 * );
																											 * Pattern
																											 * hl7File4
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7File4.JPG"
																											 * );
																											 * Pattern
																											 * hl7File5
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7File5.JPG"
																											 * );
																											 * Pattern
																											 * HL7Message
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/HL7Message.JPG"
																											 * );
																											 * Pattern
																											 * importOKBtn
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/importOptionOkBTN.JPG"
																											 * );
																											 * Pattern
																											 * playBTN =
																											 * new
																											 * Pattern(
																											 * "./sikuliImages/playBTN.JPG"
																											 * );
																											 * Pattern
																											 * exitBTN =
																											 * new
																											 * Pattern(
																											 * "./sikuliImages/exitHL7Inspector.JPG"
																											 * );
																											 * Pattern
																											 * exitOKBtn
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/exitYesBTN.JPG"
																											 * );
																											 * Pattern
																											 * updateNOBtn
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/updateNoBTN.JPG"
																											 * );
																											 * Pattern
																											 * maximizeBtn
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/maximizeBtn.JPG"
																											 * );
																											 * Pattern
																											 * errorHL7Inspector
																											 * = new
																											 * Pattern(
																											 * "./sikuliImages/errorHL7Inspector.JPG"
																											 * );
																											 * Pattern
																											 * errorOk =
																											 * new
																											 * Pattern(
																											 * "./sikuliImages/error_ok.JPG"
																											 * );
																											 * Pattern
																											 * title =
																											 * new
																											 * Pattern(
																											 * "./sikuliImages/appName.JPG"
																											 * );
																											 * 
																											 * 
																											 * 
																											 * if(screen
																											 * .exists(
																											 * errorHL7Inspector
																											 * )!=null)
																											 * { screen.
																											 * wait(
																											 * errorOk,
																											 * 10);
																											 * screen.
																											 * click(
																											 * errorOk);
																											 * 
																											 * screen.
																											 * wait(
																											 * updateNOBtn,
																											 * 10);
																											 * screen.
																											 * click(
																											 * updateNOBtn
																											 * );
																											 * 
																											 * if(screen
																											 * .exists(
																											 * title)!=
																											 * null) {
																											 * screen.
																											 * wait(
																											 * title,
																											 * 10);
																											 * screen.
																											 * doubleClick
																											 * (title);
																											 * } }
																											 * 
																											 * if(screen
																											 * .exists(
																											 * updateNOBtn
																											 * )!=null)
																											 * { screen.
																											 * wait(
																											 * updateNOBtn,
																											 * 10);
																											 * screen.
																											 * click(
																											 * updateNOBtn
																											 * ); }
																											 * 
																											 * if(screen
																											 * .exists(
																											 * maximizeBtn
																											 * )!=null)
																											 * { screen.
																											 * wait(
																											 * maximizeBtn,
																											 * 10);
																											 * screen.
																											 * click(
																											 * maximizeBtn
																											 * ); }
																											 * 
																											 * //screen.
																											 * 
																											 * //screen.
																											 * wait(
																											 * maximizeBtn,
																											 * 10);
																											 * //screen.
																											 * click(
																											 * maximizeBtn
																											 * );
																											 * 
																											 * 
																											 * 
																											 * screen.
																											 * wait(
																											 * senderIcon,
																											 * 10);
																											 * screen.
																											 * click(
																											 * senderIcon
																											 * );
																											 * screen.
																											 * click(
																											 * senderConfigBTN
																											 * );
																											 * 
																											 * screen.
																											 * wait(
																											 * senderIcon,
																											 * 10);
																											 * screen.
																											 * click(
																											 * IPPortTextBox
																											 * );
																											 * screen.
																											 * type("a",
																											 * Keys.CTRL
																											 * );
																											 * screen.
																											 * type(Keys
																											 * .
																											 * BACKSPACE
																											 * );
																											 * screen.
																											 * type(OR.
																											 * getProperty
																											 * (object))
																											 * ;
																											 * 
																											 * screen.
																											 * click(
																											 * sendOptionDialogOkBTN
																											 * );
																											 * 
																											 * 
																											 * screen.
																											 * wait(
																											 * hl7ImporterICON,
																											 * 10);
																											 * screen.
																											 * click(
																											 * hl7ImporterICON
																											 * );
																											 * 
																											 * screen.
																											 * wait(
																											 * choseFilePath,
																											 * 10);
																											 * screen.
																											 * click(
																											 * choseFilePath
																											 * );
																											 * screen.
																											 * type(
																											 * HL7MessagePath
																											 * );
																											 * screen.
																											 * click(
																											 * openBTN);
																											 * 
																											 * if(
																											 * sourceMessage
																											 * .
																											 * equalsIgnoreCase
																											 * (
																											 * "1_Patient"
																											 * )) {
																											 * screen.
																											 * click(
																											 * hl7File1)
																											 * ; } else
																											 * if(
																											 * sourceMessage
																											 * .
																											 * equalsIgnoreCase
																											 * (
																											 * "2_Patient"
																											 * )) {
																											 * screen.
																											 * click(
																											 * hl7File2)
																											 * ; } else
																											 * if(
																											 * sourceMessage
																											 * .
																											 * equalsIgnoreCase
																											 * (
																											 * "3_Patient"
																											 * )) {
																											 * screen.
																											 * click(
																											 * hl7File3)
																											 * ; } else
																											 * if(
																											 * sourceMessage
																											 * .
																											 * equalsIgnoreCase
																											 * (
																											 * "4_Patient"
																											 * )) {
																											 * screen.
																											 * click(
																											 * hl7File4)
																											 * ; } else
																											 * if(
																											 * sourceMessage
																											 * .
																											 * equalsIgnoreCase
																											 * (
																											 * "5_Patient"
																											 * )) {
																											 * screen.
																											 * click(
																											 * hl7File5)
																											 * ; }
																											 * 
																											 * screen.
																											 * click(
																											 * openBTN);
																											 * 
																											 * screen.
																											 * wait(
																											 * importOKBtn,
																											 * 10);
																											 * screen.
																											 * click(
																											 * importOKBtn
																											 * );
																											 * 
																											 * screen.
																											 * wait(
																											 * HL7Message,
																											 * 10);
																											 * screen.
																											 * click(
																											 * HL7Message
																											 * );
																											 * 
																											 * screen.
																											 * wait(
																											 * playBTN,
																											 * 10);
																											 * screen.
																											 * click(
																											 * playBTN);
																											 * 
																											 * Thread.
																											 * sleep(
																											 * 5000);
																											 * 
																											 * screen.
																											 * wait(
																											 * exitBTN,
																											 * 10);
																											 * screen.
																											 * click(
																											 * exitBTN);
																											 * 
																											 * screen.
																											 * wait(
																											 * exitOKBtn,
																											 * 10);
																											 * screen.
																											 * click(
																											 * exitOKBtn
																											 * );
																											 * 
																											 * //Thread.
																											 * sleep(
																											 * 35000);
																											 * 
																											 * //autoIt.
																											 * destroy()
																											 * ;
																											 * 
																											 * //Thread.
																											 * sleep(
																											 * 5000);
																											 * 
																											 * p.destroy
																											 * ();
																											 * 
																											 * ExecuteTestcase
																											 * .bResult
																											 * = true;
																											 * Log.
																											 * info("Invoked Application successfully"
																											 * );
																											 * 
																											 * } catch
																											 * (Exception
																											 * ex) {
																											 * Log.
																											 * info("-------- Unable to Invoke application --------"
																											 * + ex.
																											 * getMessage
																											 * ());
																											 * p.destroy
																											 * ();
																											 * ExecuteTestcase
																											 * .bResult
																											 * = false;
																											 * 
																											 * }
																											 * 
																											 * }
																											 */

	/*
	 * public static void beforeUpgrade(String object, String data) {
	 *//**************************************************************************************************
		 * /** Des: This function is used to just convert XMl to JSON and move files to
		 * specified folder
		 * 
		 * /
		 **************************************************************************************************/

	/*
	 * 
	 * try { String beforeUpgradePath = "./beforeUpgrade/";
	 * jsonConversion(OR.getProperty(object), beforeUpgradePath); }
	 * 
	 * catch (Exception E) { E.getStackTrace(); } }
	 */
	/*
	 * public static void afterUpgrade(String object, String data) {
	 *//**************************************************************************************************
		 * /** Des: This function is used to jst convert SMl to JSON and move files to
		 * specified folder
		 * 
		 * /
		 **************************************************************************************************//*
																											 * 
																											 * try {
																											 * String
																											 * afterUpgradePath
																											 * =
																											 * "./afterUpgrade/";
																											 * jsonConversion
																											 * (OR.
																											 * getProperty
																											 * (object),
																											 * afterUpgradePath
																											 * );
																											 * readWriteJsonValues
																											 * (
																											 * afterUpgradePath,
																											 * data);
																											 * compareJson
																											 * (object,
																											 * data); }
																											 * catch
																											 * (Exception
																											 * E) { E.
																											 * getStackTrace
																											 * (); } }
																											 */
	/*
	 * public static void jsonConversion(String object, String data) {
	 * 
	 * try { File source_Folder = new File(object); File[] listOfFiles =
	 * source_Folder.listFiles();
	 * 
	 * for (int i = 0; i < listOfFiles.length; i++) { if (listOfFiles[i].isFile()) {
	 * String fileWithExt = listOfFiles[i].getName(); String fileWithOutExt =
	 * fileWithExt.replaceFirst("[.][^.]+$", "");
	 * System.out.println(fileWithOutExt);
	 * 
	 * Log.info("JSON conversion method started");
	 * 
	 * // String fileName = Constants.HL7filesBeforeUpgrade+"\\"+data+".json"; try {
	 * 
	 * BufferedReader reader = new BufferedReader( new FileReader(object +
	 * "\\" + fileWithOutExt + ".txt")); String lines = ""; StringBuffer
	 * stringbuffer = new StringBuffer(); while ((lines = reader.readLine()) !=
	 * null) { stringbuffer.append(lines); }
	 * 
	 * String xml = stringbuffer.toString(); JSONObject jsonObj =
	 * XML.toJSONObject(xml);
	 * 
	 * // JSONObject jsonObj = new JSONObject(stringbuffer.toString()); //
	 * System.out.println(jsonObj.toString());
	 * 
	 * String fileName = data + fileWithOutExt + "-" + ".json"; FileWriter
	 * fileWriter = new FileWriter(fileName);
	 * 
	 * BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	 * 
	 * bufferedWriter.write(jsonObj.toString()); bufferedWriter.close();
	 * reader.close();
	 * 
	 * Log.info("Successfully created JSON file in mentioned path : " + object); }
	 * catch (IOException ex) { System.out.println("Error writing to file '" +
	 * "fileName" + "'");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); } finally { Path temp = null;
	 * try { temp = Files.move(Paths.get(object + "\\" + fileWithOutExt + ".txt"),
	 * Paths.get("./HL7Message/" + fileWithOutExt + "_" + date() + ".txt")); //
	 * Files.delete(Paths.get(object+"\\"+fileWithOutExt+".txt")); Log.info("Moved "
	 * + data + " file from source to detination"); } catch (IOException e) {
	 * e.printStackTrace(); } } Log.info("JSON conversion method Ended");
	 * 
	 * } else if (listOfFiles[i].isDirectory()) { System.out.println("Directory " +
	 * listOfFiles[i].getName()); } } } catch (NullPointerException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	/*
	 * public static void readWriteJsonValues(String object, String data) throws
	 * Exception { System.out.println(data);
	 * 
	 * Thread.sleep(1000);
	 * 
	 * Log.info("Read Write method Started");
	 * 
	 * BigInteger timeStampnew_ADT = null; BigInteger timeStampSecnew_ADT = null;
	 * int AppNonew_ADT = 0; BigInteger timeStampnew_MDM = null; BigInteger
	 * timeStampSecnew_MDM = null; BigInteger timeStampSecnewSecond_MDM = null; int
	 * PID_MDM = 0; BigInteger timeStampnew_SIU = null; BigInteger
	 * timeStampSecnew_SIU = null; int AppNonew_SIU = 0; int PID_SIU = 0; BigInteger
	 * timeStampAIPSeg_SIU = null; BigInteger timeStampnew_ORU = null; BigInteger
	 * timeStampSecnew_ORU = null; int PID_ORU = 0;
	 * 
	 * try { File source_Folder = new File("./afterUpgrade/"); File[] listOfFiles =
	 * source_Folder.listFiles();
	 * 
	 * for (int i = 0; i < listOfFiles.length; i++) { if (listOfFiles[i].isFile()) {
	 * String fileWithExt = listOfFiles[i].getName(); String fileWithOutExt =
	 * fileWithExt.replaceFirst("[.][^.]+$", ""); //
	 * System.out.println(fileWithOutExt); String[] fileNameOnly =
	 * fileWithOutExt.split("-"); String messageTypeOnly = fileNameOnly[0];
	 * 
	 * BufferedReader reader = new BufferedReader( new FileReader("./afterUpgrade/"
	 * + fileWithOutExt + ".json")); StringBuffer stringB = new StringBuffer();
	 * String line = ""; while ((line = reader.readLine()) != null) {
	 * stringB.append(line); } reader.close();
	 * 
	 * JSONObject jsonObj = new JSONObject(stringB.toString()); //
	 * System.out.println(jsonObj.toString());
	 * Log.info("After upgarde File is read");
	 * 
	 * if (messageTypeOnly.equals("ADT_A08")) { timeStampnew_ADT =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.7"); timeStampSecnew_ADT =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.10"); AppNonew_ADT =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("PID").getInt("PID.2");
	 * 
	 * Log.info("Successfully fetched values from ADT After Upgrade Files"); } else
	 * if (messageTypeOnly.equals("MDM_T02")) { JSONArray jsonArr =
	 * jsonObj.getJSONArray("HL7Message"); timeStampnew_MDM =
	 * jsonArr.getJSONObject(0).getJSONObject("MSH").getBigInteger("MSH.7");
	 * timeStampSecnew_MDM =
	 * jsonArr.getJSONObject(0).getJSONObject("MSH").getBigInteger("MSH.10");
	 * PID_MDM = jsonArr.getJSONObject(0).getJSONObject("PID").getInt("PID.2");
	 * timeStampSecnewSecond_MDM = jsonArr.getJSONObject(1).getJSONObject("MSH")
	 * .getBigInteger("MSH.10");
	 * 
	 * Log.info("Successfully fetched values from MDM After Upgrade Files"); } else
	 * if (messageTypeOnly.equals("SIU_S14")) { timeStampnew_SIU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.7"); timeStampSecnew_SIU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.10"); AppNonew_SIU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.1") .getInt("SCH.1.1"); PID_SIU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("PID").getInt("PID.2");
	 * timeStampAIPSeg_SIU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("AIP")
	 * .getJSONObject("AIP.6").getBigInteger("AIP.6.1");
	 * 
	 * Log.info("Successfully fetched values from SIU After Upgrade Files"); } else
	 * if (messageTypeOnly.equals("ORU_R01")) { timeStampnew_ORU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.7"); timeStampSecnew_ORU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("MSH")
	 * .getBigInteger("MSH.10"); PID_ORU =
	 * jsonObj.getJSONObject("HL7Message").getJSONObject("PID").getInt("PID.2");
	 * 
	 * Log.info("Successfully fetched values from ORU After Upgrade Files"); }
	 * 
	 * BufferedReader reader1 = new BufferedReader( new
	 * FileReader("./beforeUpgrade/" + fileWithOutExt + ".json")); BufferedWriter
	 * writer = null; StringBuffer stringB1 = new StringBuffer(); String line1 = "";
	 * while ((line1 = reader1.readLine()) != null) { stringB1.append(line1); }
	 * 
	 * reader1.close(); JSONObject jsonObj1 = new JSONObject(stringB1.toString());
	 * 
	 * if (messageTypeOnly.equals("ADT_A08")) { //
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").remove("MSH.7");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.7",
	 * timeStampnew_ADT);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").remove("MSH.10");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.10",
	 * timeStampSecnew_ADT);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.2");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.2",
	 * AppNonew_ADT);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.3");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.3",
	 * AppNonew_ADT);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("EVN").remove("EVN.2");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("EVN").put("EVN.2",
	 * timeStampnew_ADT);
	 * 
	 * writer = new BufferedWriter(new FileWriter("./beforeUpgrade/" +
	 * fileWithOutExt + ".json")); writer.write(jsonObj1.toString());
	 * writer.close();
	 * 
	 * Log.
	 * info("Successfully written values from After Upgrade File to Before upgrade File"
	 * ); } else if (messageTypeOnly.equals("SIU_S14")) {
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.2");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.2",
	 * PID_SIU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.3");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.3",
	 * PID_SIU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.1") .remove("SCH.1.1");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.1").put("SCH.1.1", AppNonew_SIU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.1") .remove("SCH.1.2");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.1").put("SCH.1.2", AppNonew_SIU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").remove("MSH.10");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.10",
	 * timeStampSecnew_SIU);
	 * 
	 * // jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").remove("MSH.7");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.7",
	 * timeStampnew_SIU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("AIP").getJSONObject(
	 * "AIP.6") .remove("AIP.6.1");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("AIP").getJSONObject(
	 * "AIP.6").put("AIP.6.1", timeStampAIPSeg_SIU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.11") .remove("SCH.11.5");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.11") .put("SCH.11.5", timeStampAIPSeg_SIU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.11") .remove("SCH.11.4");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("SCH").getJSONObject(
	 * "SCH.11") .put("SCH.11.4", timeStampAIPSeg_SIU);
	 * 
	 * writer = new BufferedWriter(new FileWriter("./beforeUpgrade/" +
	 * fileWithOutExt + ".json")); writer.write(jsonObj1.toString());
	 * writer.close();
	 * 
	 * Log.
	 * info("Successfully written values from After Upgrade File to Before upgrade File"
	 * ); } else if (messageTypeOnly.equals("ORU_R01")) {
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.7",
	 * timeStampnew_ORU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").remove("MSH.10");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("MSH").put("MSH.10",
	 * timeStampSecnew_ORU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.2");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.2",
	 * PID_ORU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").remove("PID.3");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("PID").put("PID.3",
	 * PID_ORU);
	 * 
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("OBR").getJSONObject(
	 * "OBR.7") .remove("OBR.7.1");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("OBR").getJSONObject(
	 * "OBR.7").put("OBR.7.1", timeStampnew_ORU);
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("OBR").getJSONObject(
	 * "OBR.22") .remove("OBR.22.1");
	 * jsonObj1.getJSONObject("HL7Message").getJSONObject("OBR").getJSONObject(
	 * "OBR.22") .put("OBR.22.1", timeStampnew_ORU);
	 * 
	 * JSONArray arr = jsonObj1.getJSONObject("HL7Message").getJSONArray("OBX");
	 * 
	 * for (int k = 0; k < arr.length(); k++) {
	 * jsonObj1.getJSONObject("HL7Message").getJSONArray("OBX").getJSONObject(k)
	 * .getJSONObject("OBX.14").remove("OBX.14.1");
	 * jsonObj1.getJSONObject("HL7Message").getJSONArray("OBX").getJSONObject(k)
	 * .getJSONObject("OBX.14").put("OBX.14.1", timeStampnew_ORU); }
	 * 
	 * writer = new BufferedWriter(new FileWriter("./beforeUpgrade/" +
	 * fileWithOutExt + ".json")); writer.write(jsonObj1.toString());
	 * writer.close();
	 * 
	 * // System.out.println(jsonObj1.toString());
	 * 
	 * Log.
	 * info("Successfully written values from After Upgrade File to Before upgrade File"
	 * );
	 * 
	 * } else if (messageTypeOnly.equals("MDM_T02")) {
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("PID").
	 * remove("PID.2");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("PID").put
	 * ("PID.2", PID_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("PID").
	 * remove("PID.3");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("PID").put
	 * ("PID.3", PID_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("PID").
	 * remove("PID.2");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("PID").put
	 * ("PID.2", PID_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("PID").
	 * remove("PID.3");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("PID").put
	 * ("PID.3", PID_MDM);
	 * 
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("MSH").
	 * remove("MSH.10");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("MSH").put
	 * ("MSH.10", timeStampSecnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("MSH").
	 * remove("MSH.10");
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("MSH").put
	 * ("MSH.10", timeStampSecnewSecond_MDM);
	 * 
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("MSH").put
	 * ("MSH.7", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("EVN").put
	 * ("EVN.2", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("TXA").put
	 * ("TXA.4", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(0).getJSONObject("TXA")
	 * .getJSONObject("TXA.22").put("TXA.22.15", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("MSH").put
	 * ("MSH.7", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("EVN").put
	 * ("EVN.2", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("TXA").put
	 * ("TXA.4", timeStampnew_MDM);
	 * jsonObj1.getJSONArray("HL7Message").getJSONObject(1).getJSONObject("TXA")
	 * .getJSONObject("TXA.22").put("TXA.22.15", timeStampnew_MDM);
	 * 
	 * writer = new BufferedWriter(new FileWriter("./beforeUpgrade/" +
	 * fileWithOutExt + ".json")); writer.write(jsonObj1.toString());
	 * writer.close();
	 * 
	 * Log.
	 * info("Successfully written values from After Upgrade File to Before upgrade File"
	 * ); }
	 * 
	 * } else if (listOfFiles[i].isDirectory()) { System.out.println("Directory " +
	 * listOfFiles[i].getName()); } }
	 * 
	 * } catch (NullPointerException e) { e.printStackTrace(); }
	 * 
	 * Log.info("Read Write method Ended"); Thread.sleep(1000);
	 * 
	 * }
	 * 
	 * public static void compareJson(String object, String data) throws Exception {
	 * Thread.sleep(1000); Log.info("JSON comparison method started");
	 * 
	 * String TextVal = ""; String TextValSecond = ""; String result = "";
	 * 
	 * FileReader F1 = null; FileReader F2 = null;
	 * 
	 * String fileNameWithoutExt = null;
	 * 
	 * try { File source_Folder = new File("./afterUpgrade/"); File[] listOfFiles =
	 * source_Folder.listFiles();
	 * 
	 * for (int i = 0; i < listOfFiles.length; i++) { if (listOfFiles[i].isFile()) {
	 * 
	 * try { String fileWithExt = listOfFiles[i].getName(); String fileWithOutExt =
	 * fileWithExt.replaceFirst("[.][^.]+$", "");
	 * 
	 * fileNameWithoutExt = fileWithOutExt;
	 * 
	 * F1 = new FileReader("./beforeUpgrade/" + fileWithOutExt + ".json"); F2 = new
	 * FileReader("./afterUpgrade/" + fileWithOutExt + ".json");
	 * 
	 * @SuppressWarnings("resource") BufferedReader reader1 = new
	 * BufferedReader(F1);
	 * 
	 * @SuppressWarnings("resource") BufferedReader reader2 = new
	 * BufferedReader(F2);
	 * 
	 * while (((TextVal = reader1.readLine()) != null) && ((TextValSecond =
	 * reader2.readLine()) != null)) {
	 * 
	 * if (TextVal.equals(TextValSecond)) {
	 * System.out.println("Data matches in the files"); result = "True";
	 * ExecuteTestcase.bResult = true; Log.info("Data matches with the " +
	 * fileWithOutExt + " files"); } else {
	 * System.out.println("Data mismatch in the files"); result = "False"; //
	 * ExecuteTestcase.bResult = false; Log.info("Data mismatches with the " +
	 * fileWithOutExt + " files"); } }
	 * 
	 * } catch (Exception e) { ExecuteTestcase.bResult = false;
	 * Log.info("could not read " + fileNameWithoutExt + " files"); //
	 * e.printStackTrace(); }
	 * 
	 * finally { Path tempFile1 = null; Path tempFile2 = null;
	 * 
	 * if (result.equals("True")) { try { F1.close(); F2.close(); tempFile1 =
	 * Files.move(Paths.get("./beforeUpgrade/" + fileNameWithoutExt + ".json"),
	 * Paths.get("./commonFiles/dataMatch/" + fileNameWithoutExt + "_BeforeUpgrade"
	 * + date() + ".json")); tempFile2 = Files.move(Paths.get("./afterUpgrade/" +
	 * fileNameWithoutExt + ".json"), Paths.get("./commonFiles/dataMatch/" +
	 * fileNameWithoutExt + "_AfterUpgrade" + date() + ".json")); Log.info("Moved "
	 * + fileNameWithoutExt + " file from source to detination"); } catch
	 * (IOException e) { e.printStackTrace(); } } else if (result.equals("False")) {
	 * try { F1.close(); F2.close(); tempFile1 =
	 * Files.move(Paths.get("./beforeUpgrade/" + fileNameWithoutExt + ".json"),
	 * Paths.get("./commonFiles/dataMismatch/" + fileNameWithoutExt +
	 * "_BeforeUpgrade" + date() + ".txt")); tempFile2 =
	 * Files.move(Paths.get("./afterUpgrade/" + fileNameWithoutExt + ".json"),
	 * Paths.get("./commonFiles/dataMismatch/" + fileNameWithoutExt +
	 * "_AfterUpgrade" + date() + ".txt")); Log.info("Moved " + fileNameWithoutExt +
	 * " file from source to detination"); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * if ((tempFile1 != null) || (tempFile2 != null)) {
	 * System.out.println("File renamed and moved successfully"); } else {
	 * System.out.println("Failed to move the file"); } } } } else if
	 * (listOfFiles[i].isDirectory()) { System.out.println("Directory " +
	 * listOfFiles[i].getName()); }
	 * 
	 * } } catch (NullPointerException e) { e.printStackTrace(); }
	 * 
	 * Log.info("JSON comparison method Ended"); }
	 * 
	 * public static String date() { DateFormat dateFormat = new
	 * SimpleDateFormat("yyyyMMddHHmmss"); Date date = new Date(); return
	 * dateFormat.format(date); }
	 * 
	 * public static String dateOnly() { DateFormat dateFormat = new
	 * SimpleDateFormat("yyyyMMdd"); Date date = new Date(); return
	 * dateFormat.format(date);
	 * 
	 * }
	 */

	public static void CountTextboxLimit(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/
		try {

			String[] parts = data.split(",");
			String SendkeyText = parts[0];
			String TotalCount = parts[1];
			Log.info("*Try to count the total number of character input in textbox*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object)))).clear();
			WebElement TextBox = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String SendkeysText = SendkeyText;
			TextBox.sendKeys(SendkeysText);
			int ActualTextSending = SendkeysText.length();

			System.out.println(TextBox.getAttribute("value"));
			String TextboxText = TextBox.getAttribute("value");
			// System.out.println(TextboxText);
			// System.out.println(TextboxText.length());
			if (TextboxText.length() < ActualTextSending) {
				// System.out.println("only taking 250 character");
				Log.info("Taking 250 character ");
			} else if (ActualTextSending > Integer.parseInt(TotalCount))

			{
				// System.out.println("less than 250 charater");
				Log.info("less than 250 charater");
			} else if (TextboxText.length() <= ActualTextSending) {
				// System.out.println("less than or equal to 250");
				Log.info("less than or equal to 250");
			} else {
				// System.out.println("taking more than 250 character");
				Log.info("taking more than 250 character");
				ExecuteTestcase.bResult = false;
			}
			// Log.info("Inputted value: " + data + " to element " + object);

		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void executeExe(String object, String data) throws IOException {
		/*******************************************************************************************************/
		/**
		 * Des: This function is used to invoke exe and batch in the local machine
		 *******************************************************************************************************/
		String filepath = data;
		Process p = null;

		try {
			Log.info("*Try to execute exe on Local machine*");
			Thread.sleep(3000);
			String[] command = { "cmd.exe", "/C", "Start", filepath };
			p = Runtime.getRuntime().exec(command);
			Thread.sleep(5000);
			p.destroy();
			ExecuteTestcase.bResult = true;
			Log.info("Invoked Application successfully");
		}

		catch (Exception e) {
			Log.info("-------- Unable to Invoke application " + filepath + " --------" + e.getMessage());
			p.destroy();
			ExecuteTestcase.bResult = false;
		}

	}

	public static void executeRemoteExe(String object, String data) throws IOException {
		/*******************************************************************************************************/
		/**
		 * Des: This function is used to invoke file in the Remote machine
		 *******************************************************************************************************/
		String[] parts = data.split(",");
		String filepath = parts[0];
		String remoteURL = parts[1];
		String strCmdLine = null;
		Process p = Runtime.getRuntime().exec(".\\Library\\Winium.exe");
		try {
			Log.info("*Try to execute exe on Remote machine*");
			Thread.sleep(3000);
			DesktopOptions option = new DesktopOptions();
			option.setLaunchDelay(5);
			option.setApplicationPath(filepath);
			WiniumDriver driver = new WiniumDriver(new URL(remoteURL), option);
			Thread.sleep(5000);
			p.destroy();
			strCmdLine = String.format("taskkill /im Winium.exe/f");
			Runtime.getRuntime().exec(strCmdLine);

			ExecuteTestcase.bResult = true;
			Log.info("Invoked Application successfully");
		}

		catch (Exception e) {
			Log.info("-------- Unable to Invoke application " + remoteURL + filepath + " --------" + e.getMessage());
			p.destroy();
			strCmdLine = String.format("taskkill /im Winium.exe /f");
			Runtime.getRuntime().exec(strCmdLine);
			ExecuteTestcase.bResult = false;
		}

	}

	public static void executeAutoIt(String object, String data) throws IOException {
		/*******************************************************************************************************/
		/**
		 * Des: This function is used to Execute AutoIt scripts
		 *******************************************************************************************************/
		String filepath = data;
		Process p = null;

		try {
			Log.info("*Try to execute AutoIt scripts*");
			Log.info(filepath);
			p = Runtime.getRuntime().exec("./upload.exe " + filepath);
			Thread.sleep(3000);
			p.destroy();
			ExecuteTestcase.bResult = true;
			Log.info("Executed AutoIt scripts successfully");
		}

		catch (Exception e) {
			Log.info("-------- Unable to Execute Auotit scripts  " + filepath + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void inputValueToExistingValue(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to input value*");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(object))));
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(data);
			Log.info("Inputted value: " + data + " to element " + object);
		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyDBText(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify two Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] Splitcoln = data.split(":");
		String ActualQuery = Splitcoln[0];
		String ExpectedDataQuery = Splitcoln[1];
		System.out.println(ExpectedDataQuery);
		System.out.println(ActualQuery);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				// System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			Statement stmt1 = conn.createStatement();
			ResultSet rs = stmt.executeQuery(ActualQuery);
			ResultSet rs1 = stmt1.executeQuery(ExpectedDataQuery);
			// ResultSet rs1 = stmt.executeQuery(expectedData);
			while (rs.next()) {
				// System.out.println(ActualQuery);
				String actualData = rs.getString(1);
				System.out.println("Actual data " + actualData);
				while (rs1.next()) {
					// System.out.println(ExpectedDataQuery);
					String expectedData = rs1.getString(1);
					System.out.println("Expected data " + expectedData);

					if (actualData.equals(expectedData)) {
						ExecuteTestcase.bResult = true;
						System.out.println("It is matching");
						Log.info("It is matching");
					} else {
						System.out.println("It is not matching");
						Log.info("It is not matching");
						ExecuteTestcase.bResult = false;
					}
				}
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		}

		catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void verifyElementNotExisted(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element not existed
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not existed*");

			By locator = By.xpath(OR.getProperty(object));
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			Boolean Element = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			if (Element == true) {
				Log.info("Element: " + object + " not existed");
			}
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is existed --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void selectByVisibleTextNodes(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to select By Visible Text for nodes in
		 * dropdwon
		 **************************************************************************************************/
		String element = null;
		try {
			Log.info("*Try to Select value from the Dropdown*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = data.split(":");
			String xpathText = parts[0];
			String value = parts[1];
			element = (OR.getProperty(object));
			String fullXpath = String.format(element, xpathText);
			WebElement Onset = DRIVER.findElement(By.xpath(fullXpath));
			Select selectAction = new Select(Onset);
			selectAction.selectByVisibleText(value);
			Log.info("Selected value from the Dropdown");

		} catch (Exception e) {
			Log.info("-------- Unable to Select value from the Dropdown: " + element + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void addSubtractDays1(String object, String data) {

		/*********************************************************************************************
		 * /** Des: This function is Used to select date based on current date plus
		 * number of days /
		 *********************************************************************************************/
		try {
			String element = OR.getProperty(object);
			Log.info("*Try to select Date *");
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, Integer.parseInt(data));
			cal.getTime();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String dateValue = dateFormat.format(cal.getTime());
			JavascriptExecutor JS = ((JavascriptExecutor) DRIVER);
			JS.executeScript("document.getElementById('\" + element + \"').removeAttribute('readonly',0);");
			WebElement Date = DRIVER.findElement(By.id(element));
			Date.clear();
			Date.sendKeys(dateValue);
			Log.info("Inputted : " + dateValue + "Date value Sucessfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Input Date ---" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void addSubtractDays(String object, String data) {

		/*********************************************************************************************
		 * /** Des: This function is Used to select date based on current date plus
		 * number of days /
		 *********************************************************************************************/
		try {
			String element = OR.getProperty(object);
			Log.info("*Try to select Date *");
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, Integer.parseInt(data));
			cal.getTime();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String dateValue = dateFormat.format(cal.getTime());
			JavascriptExecutor JS = ((JavascriptExecutor) DRIVER);
			JS.executeScript("document.getElementById('" + element + "').removeAttribute('readonly',0);");
			WebElement Date = DRIVER.findElement(By.id(element));
			Date.clear();
			Date.sendKeys(dateValue);
			Log.info("Inputted : " + dateValue + "Date value Sucessfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Input Date ---" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputElementNodes(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to input in to element nodes date
		 * 
		 * /
		 **************************************************************************************************/
		String element = null;
		try {
			Log.info("*Try to Input  value*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = data.split(":");
			String xpathText = parts[0];
			String input = parts[1];
			element = (OR.getProperty(object));
			String fullXpath = String.format(element, xpathText);
			WebElement Onset = DRIVER.findElement(By.xpath(fullXpath));
			Onset.sendKeys(input);
			ExecuteTestcase.bResult = true;
			Log.info("Inputted value: " + element + " to element ");

		} catch (Exception e) {
			Log.info("-------- Unable to Input text: " + element + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void VerifyElementVisibleNodes(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to verify element is visible or not for
		 * element nodes * ( By passing part of xpath as data ) /
		 ****************************************************************************************************/
		String fullXpath = null;

		try {
			Log.info("*Try to verify text between get from element and expected text*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String xpathtext = data;
			String element = (OR.getProperty(object));
			fullXpath = String.format(element, xpathtext);
			System.out.println(fullXpath);
			Boolean Element = DRIVER.findElement(By.xpath(fullXpath)).isDisplayed();
			if (Element == true) {
				Log.info("Element is Visible Successfully");
			}

			else {
				Log.info("Element is not Visible  Successfully");
				ExecuteTestcase.bResult = false;

			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Element Visible " + fullXpath + "" + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyAppDB(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data base value with the application
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				System.out.println(rs.getString(1));
				String DBdata = rs.getString(1);
				String elementText = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
				System.out.println("Expected data " + elementText);
				System.out.println("Actual data " + DBdata);
				if (elementText.equals(DBdata)) {
					ExecuteTestcase.bResult = true;
					Log.info("It is matching");
				} else {
					Log.info("It is not matching");
					ExecuteTestcase.bResult = false;
				}
			}

		} catch (ClassNotFoundException ex) {
			Log.info("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void signaturePopupSpanish(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Input Signature /
		 **************************************************************************************************/
		try {
			Log.info("*Signature pop up*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//DIV[@id='signaturePopup']"));
			// String SignDone = OR.getProperty(object);
			WebElement tmpElement = DRIVER.findElement(By.linkText("Hecho"));
			tmpElement.click();
			Log.info("---Signed Sucesfully " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to Sign" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyEligilbilityText(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Radio button is selected or not
		 * (Specific to CheckinAsyst)
		 * 
		 * /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Verify radio button is selected *");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			String[] parts = data.split(",");
			String appId = parts[0];
			String expectedText = parts[1];
			String xpath1 = "//div[@data-appid=" + appId + "]";
			String xpath2 = OR.getProperty(object);
			String fullxpath = xpath1 + xpath2;
			String actual = DRIVER.findElement(By.xpath(fullxpath)).getText();
			System.out.println(actual);
			String actualText = actual.trim();
			System.out.println(actualText);
			System.out.println(expectedText);
			if (actualText.equals(expectedText)) {
				ExecuteTestcase.bResult = true;
				Log.info("Expected text on " + expectedText + "and actual text: " + actualText + " are the same.");
			} else {
				ExecuteTestcase.bResult = false;
				Log.info("Expected text on " + expectedText + "and actual text: " + actualText + " are different.");
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Expected text on " + object + "and actual text: " + data + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void inputPastOrFutureDate(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to add or subtract Current Date in MM/DD/YYYY
		 * Format based on data /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Input Past or Future Date based on user data*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath(OR.getProperty(object)));
			int userdt = Integer.parseInt(data);
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			dateFormat.format(date);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, userdt);
			Date todate1 = cal.getTime();
			String userdate = dateFormat.format(todate1);
			System.out.println(date);
			DRIVER.findElement(By.xpath(OR.getProperty(object))).sendKeys(userdate);
			Log.info("---Inputted Input Past or Future Date based on user data " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to Input Past or Future Date based on user data " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst to select appointment time
	public static void CAAppointment(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to invoke screening APPointment ID /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to invoke screening*");

			DRIVER.findElement(By.xpath("//div[@id='wrapperApptTabs']/div[@data-appid='" + data + "']/p")).click();
			Log.info("Invoked Screening: " + data + " to element " + object);
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("-------- Unable invoke screening : " + data + " to element " + object + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void switchToIFrameWithIndex(String object, String data) {
		/****************************************************************************************************
		 * /** Desc: this function use to switch frame on page. It's used before
		 * hover_on_menu, and click_element functions /* Three functions use to click on
		 * submenu on page /* Currently, "switch_to.frame(str_frame_name)" work well on
		 * latest IE and chrome. Not work on new FF version (Verified:NOT YET) /* On FF
		 * should add more wait time. /
		 *****************************************************************************************************/

		try {
			Log.info("*Try to switch to iFrame with Index*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			// List<WebElement> iframes = DRIVER.findElements(By.xpath("//iframe")); //
			// (By.xpath("//iframe"));
			int totalframe = DRIVER.findElements(By.xpath("//iframe")).size();
			System.out.println(totalframe);
			DRIVER.switchTo().frame(7);
			System.out.println("Enterd IFrame");
		} catch (Exception e) {
			Log.info("--------  Unable to switch to iFrame with Index: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public void CopyFiles() {
		/**************************************************************************************************
		 * /** Des: This function is used to Copy file from source to destination
		 * 
		 * /
		 **************************************************************************************************/
		Log.info("Try to copy Files from Source to Destination");
		// provide the sorce file path
		String Source = "C:\\Users\\prodadmin\\Desktop\\Product_WorkSpace\\HATF\\src\\testing\\reports\\TestResult_BAT_1_ May_2018.xls";
		String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.xls'").format(new Date());
		// provide the path od destination file
		String Destination = "C:\\Users\\prodadmin\\Desktop\\CopyTestResultFIles\\" + "TestResult_BAT_1_ May_2018  "
				+ fileName;

		// Source and Destination Files of Log Files
		String Sourcelog = "C:\\Users\\prodadmin\\Desktop\\Product_WorkSpace\\HATF\\logfile.log";
		String fileNamelog = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss'.txt'").format(new Date());
		String Destinationlog = "C:\\Users\\prodadmin\\Desktop\\CopyTestResultFIles\\" + "LOGFILE   " + fileNamelog;

		File srcFolder = new File(Source);
		File destFolder = new File(Destination);
		File LogSrcFolder = new File(Sourcelog);
		File LogDestFolder = new File(Destinationlog);
		// make sure source exists
		if (!srcFolder.exists() && (!LogSrcFolder.exists())) {

			Log.info("Directory does not exist.");
			ExecuteTestcase.bResult = false;
			// just exit
			System.exit(0);

		} else {

			try {
				copyFolder(srcFolder, destFolder);
				copyFolder(LogSrcFolder, LogDestFolder);
			} catch (IOException e) {
				e.printStackTrace();
				ExecuteTestcase.bResult = false;
				// error, just exit
				System.exit(0);
			}
		}

		Log.info("copied Succesfully");
		ExecuteTestcase.bResult = true;
	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from " + src + "  to " + dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);

		}

	}

	public static void verifySSODisabled(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element not existed
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not existed*");

			By locator = By.xpath("//div[@data-appid='" + data + "']//img[@src='images/Reconcile.png']");
			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			Boolean Element = wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
			if (Element == true) {
				Log.info("Element: " + object + " not existed");
			}
		} catch (Exception e) {
			Log.info("-------- Element: " + object + " is existed --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifySSOclinicalItemExists(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element not existed
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not existed*");

			String[] parts = data.split(";");
			String state = parts[0];
			String clinicalItem = parts[1];

			By locator = By.xpath("//div[div[label[text()='" + state + "']]]/div/div[2]/div/div/label[text()='"
					+ clinicalItem + "']");

			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			Log.info("Element: " + object + " is exists");

		} catch (Exception e) {
			Log.info("-------- Element: " + object + " exists --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifySSOClinicalUpdateChart(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to Verify element not existed
		 * 
		 * /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to Verify Element not existed*");

			String[] parts = data.split(";");
			String clinicalType = parts[0];
			String clinicalItem = parts[1];

			By locator = By.xpath(
					"//div[label[contains(text(),'" + clinicalType + "')]]/div/label[text()='" + clinicalItem + "']");

			WebDriverWait wait = new WebDriverWait(DRIVER, 15);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			Log.info("Element: " + object + " is exists");

		} catch (Exception e) {
			Log.info("-------- Element: " + object + " exists --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void addDays(String object, String data) {

		/*********************************************************************************************
		 * /** Des: This function is Used to select date based on current date plus
		 * number of days /
		 *********************************************************************************************/
		try {
			Log.info("*Try to select Date *");

			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, Integer.parseInt(data)); // minus number would decrement the days
			cal.getTime();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String dateValue = dateFormat.format(cal.getTime());

			JavascriptExecutor JS = ((JavascriptExecutor) DRIVER);
			JS.executeScript("document.getElementById('datePicker').removeAttribute('readonly',0);");

			WebElement fromDateBox = DRIVER.findElement(By.id("datePicker"));
			fromDateBox.clear();
			fromDateBox.sendKeys(dateValue);

			Log.info("Sent value: " + data + " Selected Date Sucessfully.");
		} catch (Exception e) {
			Log.info("-------- Unable to Select Date ---" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	// This method is applicable for checkinasyst
	public static void UpdateButton(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to click Update Button /
		 **************************************************************************************************/
		try {
			Log.info("*Try to Click button*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			WebElement objButton = DRIVER.findElement(By.xpath(OR.getProperty(object)));
			String Actualclass = objButton.getAttribute("Class");
			String Expectedclass = "yes_btn ui-btn ui-shadow ui-btn-corner-all ui-btn-inline ui-btnActive ui-btn-up-c";

			if (Actualclass.equals(Expectedclass) == false) {
				objButton.click();
				ExecuteTestcase.bResult = true;
				Log.info("Clicked Update button: " + object);
			} else {
				Log.info("Update Button: " + object + " was already Clicked.");
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to Click Update button: " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void preScreening1(String object, String data) {
		/****************************************************************************************************
		 * /** Des: This function is used to clear and input data to text box. /
		 ****************************************************************************************************/
		try {
			Log.info("*Try to input value*");
			String USER_XPATH = OR.getProperty(object);
			System.out.println(USER_XPATH);
			String Testdata = data;
			/*
			 * String newUser = "//div[@data-appid="+Testdata+"]"; String fullXpath =
			 * String.format(USER_XPATH);
			 */
			System.out.println(USER_XPATH);
			System.out.println(Testdata);
			DRIVER.findElement(By.xpath("//div[@data-appid=" + data + "]and//div[@data-appid=" + data
					+ "]//img[@data-buttontype='btnapptworkflow']")).click();

			// DRIVER.findElement(By.xpath("//div[@data-appid="+Testdata+")).click();
			Log.info("Inputted value: " + data + " to element " + object);

		} catch (Exception e) {
			Log.info(
					"-------- Unable to input value: " + data + " to element " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void AcceptpaymentPopup(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to identify canvas element of
		 * AcceptpaymentPopup /
		 **************************************************************************************************/
		try {
			Log.info("*Signature pop up*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.findElement(By.xpath("//DIV[@id='grpThemePayment']']"));
			WebElement tmpElement = DRIVER.findElement(By.linkText("Done"));
			tmpElement.click();
			Log.info("---Signed Sucesfully " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;

		} catch (Exception e) {
			Log.info("--------Unable to Sign" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyPharmacy1(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to verify Pharmacy inbound is there . if
		 * inbound validate inbound pharmacy (can be used when two xpaths are used for
		 * diff text message in the same place) /
		 **************************************************************************************************/
		Boolean ispresent = true;
		try {
			Log.info("*Try to Verify pharmacy with and without inbound*");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			String[] parts = object.split(",");
			String noRecordxpath = parts[0];
			String Pharmacyxpath2 = parts[1];
			String[] Splitcol = data.split(":");
			String noRecordText = Splitcol[0];
			String pharmacyText = Splitcol[1];

			Boolean NoRecord = DRIVER.findElements(By.xpath(OR.getProperty(noRecordxpath))).size() == 1;
			System.out.println(NoRecord);

			if (NoRecord) {
				String actual = DRIVER.findElement(By.xpath(OR.getProperty(noRecordxpath))).getText();
				String NorecordText = actual.trim();
				if (NorecordText.equals(noRecordText)) {
					Log.info("-------- It is matching: " + object);
					ExecuteTestcase.bResult = true;

				}
			} else {

				WebElement text = DRIVER.findElement(By.xpath(OR.getProperty(Pharmacyxpath2)));
				String ActualText = text.getText();
				System.out.println(ActualText.trim());

				List<String> ParagraphText = Arrays.asList(pharmacyText.split(","));
				for (String Text : ParagraphText) {
					System.out.println(Text);
					if (!ActualText.contains(Text))

					{
						ispresent = false;
						break;

					}
				}

				if (ispresent) {
					Log.info("-------- It is matching: " + object);
					ExecuteTestcase.bResult = true;
				}

				else

				{
					Log.info("-------- It is not matching: " + object);
					ExecuteTestcase.bResult = false;
				}

			}

		}

		catch (Exception e) {
			Log.info("-------- Unable to verify Pharmacy : " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void scrollToBottomOfContainer(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Scroll To Bottom of the Container Page /
		 **************************************************************************************************/
		String id = OR.getProperty(object);
		String element = "#" + id;

		try {
			Log.info("*Try to Scroll Bottom of the Specific Container *");
			DRIVER.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
			JavascriptExecutor javascript = (JavascriptExecutor) DRIVER;
			javascript.executeScript("$('" + element + "').scrollTop($('" + element + "')[0].scrollHeight)");
			Log.info("--- Scrolled To Bottom of the Container " + object + "Sucessfully");
			ExecuteTestcase.bResult = true;
		} catch (Exception e) {
			Log.info("--------Unable to Verify Horizontal" + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void exe1Query(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Execute the Query
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;
		Log.info(Query);
		java.sql.Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
				Log.info("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(Query);
			System.out.println("Query Executed");
			Log.info("Query Executed successfully");
		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public static void exeSPparameters(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is Execute three parameters Stored procedure
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] parts = data.split(":");
		String SPName = parts[0];
		String param1 = parts[1];
		String param2 = parts[2];

		java.sql.Connection conn = null;
		java.sql.CallableStatement stmt = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			// prepare the stored procedure call
			stmt = conn.prepareCall("{call " + SPName + "(?,?)}");

			// set the parameters for Stored procedure
			stmt.setString(1, param1);
			stmt.setString(2, param2);

			// call Stored procedure
			stmt.execute();

			ResultSet rs = stmt.executeQuery();
			System.out.println("Called Stored Procedure");
			while (rs.next()) {
				System.out.println(rs.getString(1));

			}

		} catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			ex.printStackTrace();
		} catch (SQLException ex) {
			System.out.println("Passed");
			System.out.println("An error occurred.");
			ex.printStackTrace();
		} catch (Exception e) {
			Log.info("An error couured" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public static void rtePayment(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Store the RTE payment values
		 **************************************************************************************************/
		String[] elements = object.split(",");
		String elementCopay = elements[0];
		String elementBalance = elements[1];
		String elementPrepayment = elements[2];
		String elementTotalAmt = elements[3];

		try {
			Log.info("*Try to Store the RTE payment values *");
			copay = DRIVER.findElement(By.xpath(OR.getProperty(elementCopay))).getText().replaceAll("\\s+", "");
			balance = DRIVER.findElement(By.xpath(OR.getProperty(elementBalance))).getText().replaceAll("\\s+", "");
			totalAmount = DRIVER.findElement(By.xpath(OR.getProperty(elementTotalAmt))).getText().replaceAll("\\s+",
					"");
			boolean ispresent = DRIVER.findElements(By.xpath(OR.getProperty(elementPrepayment))).size() == 0;

			if (ispresent == false) {
				Prepayment = DRIVER.findElement(By.xpath(OR.getProperty(elementPrepayment))).getText()
						.replaceAll("\\s+", "");

			}

			else {
				Prepayment = "NO VALUE";

			}

		}

		catch (Exception e) {
			Log.info("/ -- Unable to Store the RTE payment values  " + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void rtePaymentValidation(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to Store the RTE payment values
		 **************************************************************************************************/

		String[] elements;
		String elementCopay;
		String elementBal;
		String elementPrepayment;
		String elementTotalAmt;
		boolean ispresent;
		String totalAmtValue;
		String copayValue;
		String balanceValue;
		String PrepaymentValue;

		try {

			Log.info("*Try to Validate RTE Payment Values*");
			switch (data) {
			case "COPAY":
				copayValue = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText().replaceAll("\\s+", "");

				if (copay.equalsIgnoreCase(copayValue)) {

					Log.info("Copay =" + copay + "and Expected Values " + "Copay Value= " + copayValue
							+ " is matching *");

				}

				else {
					Log.info("Copay =" + copay + "and Expected Values " + "Copay Value= " + copayValue
							+ " is not matching *");

					ExecuteTestcase.bResult = false;

				}

				break;

			case "BALANCE":

				balanceValue = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText().replaceAll("\\s+", "");

				if (balance.equalsIgnoreCase(balanceValue)) {

					Log.info(" Balance Amount = " + balance + " and Expected Values " + "Balance value = "
							+ balanceValue + " is matching *");

				}

				else {
					Log.info(" Balance Amount = " + balance + " and Expected Values " + "Balance value = "
							+ balanceValue + " is not matching *");
					ExecuteTestcase.bResult = false;

				}

				break;

			case "PREPAYMENT":

				PrepaymentValue = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText().replaceAll("\\s+", "");

				if (totalAmount.equalsIgnoreCase(PrepaymentValue)) {

					Log.info(" Prepayment = " + Prepayment + "  and Expected Values " + " Prepayment value = "
							+ PrepaymentValue + " is matching *");

				}

				else {
					Log.info(" Prepayment = " + Prepayment + "  and Expected Values " + " Prepayment value = "
							+ PrepaymentValue + " is not matching *");
					ExecuteTestcase.bResult = false;

				}

				break;

			case "TOTALAMT":

				totalAmtValue = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText().replaceAll("\\s+", "");

				if (totalAmount.equalsIgnoreCase(totalAmtValue)) {

					Log.info(" Total Amount= " + totalAmount + "  and Expected Values " + " Total Amount value = "
							+ totalAmtValue + " is matching *");

				}

				else {
					Log.info("*Total Amount" + totalAmount + " and " + totalAmtValue + "is not matching*");
					ExecuteTestcase.bResult = false;

				}

				break;

			case "COPAYBAL":
				elements = object.split(",");
				elementCopay = elements[0];
				elementBal = elements[1];

				copayValue = DRIVER.findElement(By.xpath(OR.getProperty(elementCopay))).getText().replaceAll("\\s+",
						"");
				balanceValue = DRIVER.findElement(By.xpath(OR.getProperty(elementBal))).getText().replaceAll("\\s+",
						"");

				if (copay.equalsIgnoreCase(copayValue) && (balance.equalsIgnoreCase(balanceValue))) {

					Log.info("Copay =" + copay + " Balance Amount = " + balance + "  and Expected Values "
							+ "Copay Value= " + copayValue + " Balance value = " + balanceValue + " is matching *");
				}

				else {
					Log.info("Copay =" + copay + " Balance Amount = " + balance + "  and Expected Values "
							+ "Copay Value= " + copayValue + " Balance value = " + balanceValue + " is not matching *");
					ExecuteTestcase.bResult = false;
				}

				break;

			case "BALPRE":
				elements = object.split(",");
				elementBal = elements[0];
				elementPrepayment = elements[1];

				balanceValue = DRIVER.findElement(By.xpath(OR.getProperty(elementBal))).getText().replaceAll("\\s+",
						"");
				ispresent = DRIVER.findElements(By.xpath(OR.getProperty(elementPrepayment))).size() == 0;

				if (ispresent == false) {
					PrepaymentValue = DRIVER.findElement(By.xpath(OR.getProperty(elementPrepayment))).getText()
							.replaceAll("\\s+", "");

				}

				else {
					PrepaymentValue = "NO VALUE";

				}

				if (balance.equalsIgnoreCase(balanceValue) && (Prepayment.equalsIgnoreCase(PrepaymentValue))) {

					Log.info(" Balance Amount = " + balance + " Prepayment = " + Prepayment + " and Expected Values "
							+ " Balance value = " + balanceValue + " Prepayment value =" + PrepaymentValue
							+ " is matching *");

				}

				else {
					Log.info(" Balance Amount = " + balance + " Prepayment = " + Prepayment + " and Expected Values "
							+ " Balance value = " + balanceValue + " Prepayment value =" + PrepaymentValue
							+ " is not matching *");
					ExecuteTestcase.bResult = false;
				}

				break;

			case "COPAYBALPRE":
				elements = object.split(",");
				elementCopay = elements[0];
				elementBal = elements[1];
				elementPrepayment = elements[2];

				copayValue = DRIVER.findElement(By.xpath(OR.getProperty(elementCopay))).getText().replaceAll("\\s+",
						"");
				balanceValue = DRIVER.findElement(By.xpath(OR.getProperty(elementBal))).getText().replaceAll("\\s+",
						"");

				ispresent = DRIVER.findElements(By.xpath(OR.getProperty(elementPrepayment))).size() == 0;

				if (ispresent == false) {
					PrepaymentValue = DRIVER.findElement(By.xpath(OR.getProperty(elementPrepayment))).getText()
							.replaceAll("\\s+", "");

				}

				else {
					PrepaymentValue = "NO VALUE";

				}

				if (copay.equalsIgnoreCase(copayValue)
						&& (balance.equalsIgnoreCase(balanceValue) && (Prepayment.equalsIgnoreCase(PrepaymentValue)))) {

					Log.info("Copay =" + copay + " Balance Amount = " + balance + " Prepayment = " + Prepayment
							+ "  and Expected Values " + "Copay Value= " + copayValue + " Balance value = "
							+ balanceValue + " Prepayment value =" + PrepaymentValue + " is matching *");

				}

				else {
					Log.info("Copay =" + copay + " Balance Amount = " + balance + " Prepayment = " + Prepayment
							+ "  and Expected Values " + "Copay Value= " + copayValue + " Balance value = "
							+ balanceValue + " Prepayment value =" + PrepaymentValue + " is not matching *");
					ExecuteTestcase.bResult = false;
				}

				break;

			case "COPAYBALPRETOTAL":
				elements = object.split(",");
				elementCopay = elements[0];
				elementBal = elements[1];
				elementPrepayment = elements[2];
				elementTotalAmt = elements[3];

				copayValue = DRIVER.findElement(By.xpath(OR.getProperty(elementCopay))).getText().replaceAll("\\s+",
						"");
				balanceValue = DRIVER.findElement(By.xpath(OR.getProperty(elementBal))).getText().replaceAll("\\s+",
						"");
				totalAmtValue = DRIVER.findElement(By.xpath(OR.getProperty(elementTotalAmt))).getText()
						.replaceAll("\\s+", "");

				ispresent = DRIVER.findElements(By.xpath(OR.getProperty(elementPrepayment))).size() == 0;

				if (ispresent == false) {
					PrepaymentValue = DRIVER.findElement(By.xpath(OR.getProperty(elementPrepayment))).getText()
							.replaceAll("\\s+", "");

				}

				else {
					PrepaymentValue = "NO VALUE";

				}

				if (copay.equalsIgnoreCase(copayValue)
						&& (balance.equalsIgnoreCase(balanceValue) && (Prepayment.equalsIgnoreCase(PrepaymentValue)
								&& (totalAmount.equalsIgnoreCase(totalAmtValue))))) {

					Log.info("Copay =" + copay + " Balance Amount = " + balance + " Prepayment = " + Prepayment
							+ " and Total Amount= " + totalAmount + "  and Expected Values " + "Copay Value= "
							+ copayValue + " Balance value = " + balanceValue + " Prepayment value =" + PrepaymentValue
							+ " Total Amount value = " + totalAmtValue + " is matching *");

				}

				else {
					Log.info("Copay =" + copay + " Balance Amount = " + balance + " Prepayment = " + Prepayment
							+ " and Total Amount= " + totalAmount + "  and Expected Values " + "Copay Value= "
							+ copayValue + " Balance value = " + balanceValue + " Prepayment value =" + PrepaymentValue
							+ " Total Amount value = " + totalAmtValue + " is not matching *");

					ExecuteTestcase.bResult = false;

				}

				break;

			}
			Log.info("RTE Payment Values are Validated:");
		} catch (Exception e) {
			Log.info("-------- Unable to Verify RTE Payments --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void pageloader1(String object, String data) throws InterruptedException {
		/****************************************************************************************************
		 * /** Des: This function is used to wait till the page is loaded (MilliSecond).
		 * /
		 ****************************************************************************************************/
		JavascriptExecutor js = (JavascriptExecutor) DRIVER;

		try {
			Log.info("*Try to wait to Page load *");
			for (int i = 1; i < 40; i++) {

				Thread.sleep(1000);

				boolean test = (boolean) js.executeScript("return jQuery.active == 0");
				System.out.println(i + "  PAGE IS LOADING ....... ");

				if (js.executeScript("return document.readyState").toString().equals("complete") && (test == true)) {
					System.out.println(" PAGE IS LOADED ");
					break;
				}

			}
			Log.info("*Page is Loaded *");
			ExecuteTestcase.bResult = true;
		}

		catch (Exception e) {

			Log.info("-------- Timeout waiting for Page Load Request to complete -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	public static void pageloader(String object, String data) throws InterruptedException {
		/****************************************************************************************************
		 * /** Des: This function is used to wait till the page is loaded (MilliSecond).
		 * /
		 ****************************************************************************************************/
		JavascriptExecutor js = (JavascriptExecutor) DRIVER;

		try {
			Log.info("*Try to wait to Page load *");
			for (int i = 1; i < 100; i++) {

				Thread.sleep(500);

				boolean test = (boolean) js.executeScript("return jQuery.active == 0");
				System.out.println(i + "  PAGE IS LOADING ....... ");

				if (js.executeScript("return document.readyState").toString().equals("complete") && (test == true)) {
					System.out.println(" PAGE IS LOADED ");
					break;
				}

			}
			Log.info("*Page is Loaded *");
			ExecuteTestcase.bResult = true;
		}

		catch (Exception e) {

			Log.info("-------- Timeout waiting for Page Load Request to complete -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;

		}
	}

	public static void verifyDBtwotables(String object, String data)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] parts = data.split(":");
		String query1 = parts[0];
		String query2 = parts[1];

		int counter = 0;
		int counterADD = 0;
		int K = 0;
		int table1rowCount = 0;
		int table2rowCount = 0;

		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> values1 = new ArrayList<>();

		List<String> list = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();

		ArrayList<String> rowNO = new ArrayList<>();
		ArrayList<String> cellNO = new ArrayList<>();
		List<String> rowList = new ArrayList<String>();
		List<String> cellList = new ArrayList<String>();

		List<String> allNotMatchedData = new ArrayList<String>();
		ArrayList<String> notMatchedData = new ArrayList<>();

		System.out.println(query1);
		java.sql.Connection conn = null;
		int k;
		// DataBase connection and reading data from database
		// code-------------------->

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query1);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("There were " + columnsNumber + " Column count records.");

			while (rs.next()) {
				table1rowCount++;
				values = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber; i++) {

					if (rs.getString(i) != null) {

						rs.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					values.add(rs.getString(i));
				}
				System.out.println("Stored Username in DB:" + " " + values);

				list.addAll(values);

			}

			System.out.println(table1rowCount);
			System.out.println("Stored Username in DB:" + " " + list);

			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(query2);
			ResultSetMetaData rsmd1 = rs1.getMetaData();
			int columnsNumber1 = rsmd1.getColumnCount();
			System.out.println("There were " + columnsNumber1 + " Column count records.");

			while (rs1.next()) {
				table2rowCount++;
				values1 = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber1; i++) {

					if (rs1.getString(i) != null) {

						rs1.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					values1.add(rs1.getString(i));
				}
				System.out.println("Stored Username1 in DB:" + " " + values1);

				list1.addAll(values1);

			}
			System.out.println("Stored Username1 in DB:" + " " + list1);

			if (table1rowCount == table2rowCount && columnsNumber == columnsNumber1) {

				for (K = 0; K <= list.size() - 1; K++) {
					int l = K + 1;

					String tableOneData = (StringUtils.isNullOrEmpty(list.get(K)) ? "" : (list.get(K)));

					System.out.println(tableOneData);
					String tableTwoData = (StringUtils.isNullOrEmpty(list1.get(K)) ? "" : (list1.get(K)));

					System.out.println(tableTwoData);

					if ((tableOneData).equalsIgnoreCase(tableTwoData)) {
						System.out.println(" Element is Equal");
						Log.info("Element is Equal");
						System.out.println("Passed");
						Log.info("Passed");
						System.out.println("*------------------------------------------------------------------");
						Log.info("TestCase Passed because all elements are same");
					} else {
						System.out.println("Element not equal");
						System.out.println("TABLE ONE VALUE = " + tableOneData);
						System.out.println("TABLE TWO VALUE    = " + tableTwoData);
						Log.info("Element not equal");
						System.out.println("Failed");
						Log.info("Failed");
						System.out.println("*The Element are not same  " + list1.get(K));
						int total = K / columnsNumber;
						int rowno = total + 1;
						System.out.println("Row number " + rowno);
						Log.info("Row number " + rowno);
						String row = String.valueOf(rowno);
						int cellcnt = ((K) - (columnsNumber * total));
						int cellno = cellcnt + 1;
						System.out.println("CellNumber " + cellno);
						Log.info("CellNumber " + cellno);
						String cell = String.valueOf(cellno);
						System.out.println("*------------------------------------------------------------------");
						String faileddata = "<-----FAIL------>" + " ******* At Count-------> " + counter + " "
								+ "****** DB Data is not equal-------->" + list1.get(K)
								+ " ******* IN ROW NO ----------->" + "  " + rowno + " " + "****** Cell NO----->  "
								+ cellno;
						Log.info("FAIL" + "  " + counter + " " + "DB Data is not equal" + list1.get(K));

						counter++;
						counterADD = counter;
						counterADD++;
						notMatchedData.add(list1.get(K));
						rowNO.add(row);
						cellNO.add(cell);

					}
				}
				allNotMatchedData.addAll(notMatchedData);
				rowList.addAll(rowNO);
				cellList.addAll(cellNO);
				if (counter > 0) {

					System.out.println("FAIL" + "  " + counter + " " + "DB Data is not equal" + allNotMatchedData);
					System.out.println(" IN ROW NO" + "  " + rowList + " " + "Cell NO" + cellList);
					System.out.println("*------------------------------------------------------------------");
					System.out.println("All elements are not same TestCase Failed");
					Log.info("Failed");
					// TODO:: Call the method to write into Excel file for the failure one
					counter = 0;
					allNotMatchedData.clear();
					notMatchedData.clear();
				}
				if (counterADD > 0) {
					System.out.println("Failed");
					Log.info("Failed");
					Log.info("TestCase failed because all elements are not equals");
					ExecuteTestcase.bResult = false;
					System.out.println("TestCase failed because all elements are not equals");
				}
				list1.clear();
			}

			else {
				System.out.println("Row and column count are not equal Between the table");
				Log.info("Row and column count are not equal Between the table");
				ExecuteTestcase.bResult = false;

			}

		}

		catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			Log.info("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			Log.info("An error occurred.");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
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

	public static void verifyDBApp(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data base value with the application
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String Query = data;

		System.out.println(Query);
		java.sql.Connection conn = null;
		try {
			Log.info("To verify DB with application");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println(Query);
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(Query);

			while (rs.next()) {
				System.out.println(Query);
				System.out.println(rs.getString(1));
				String DBdata = rs.getString(1);
				String elementText = DRIVER.findElement(By.xpath(OR.getProperty(object))).getText();
				System.out.println("Expected data " + elementText);
				System.out.println("Actual data " + DBdata);
				if (elementText.equals(DBdata)) {
					ExecuteTestcase.bResult = true;
					Log.info("It is matching");
				} else {
					Log.info("It is not matching");
					ExecuteTestcase.bResult = false;
				}
			}

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

	public static void verifyDBtwotablesDB(String object, String data)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		/**************************************************************************************************
		 * /** Des: This function is used Verify Data Base value
		 * 
		 * /
		 **************************************************************************************************/
		String databaseURL = Constants.DBURL;
		String user = Constants.userName;
		String password = Constants.pwd;
		String[] parts = data.split(":");
		String query1 = parts[0];
		String query2 = parts[1];

		int counter = 0;
		int counterADD = 0;
		int K = 0;
		int table1rowCount = 0;
		int table2rowCount = 0;

		ArrayList<String> values = new ArrayList<>();
		ArrayList<String> values1 = new ArrayList<>();

		List<String> list = new ArrayList<String>();
		List<String> list1 = new ArrayList<String>();

		ArrayList<String> rowNO = new ArrayList<>();
		ArrayList<String> cellNO = new ArrayList<>();
		List<String> rowList = new ArrayList<String>();
		List<String> cellList = new ArrayList<String>();

		List<String> allNotMatchedData = new ArrayList<String>();
		ArrayList<String> notMatchedData = new ArrayList<>();

		System.out.println(query1);
		java.sql.Connection conn = null;
		int k;
		// DataBase connection and reading data from database
		// code-------------------->

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(databaseURL, user, password);
			if (conn != null) {
				System.out.println("Connected to the database");
			}

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query1);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("There were " + columnsNumber + " Column count records.");

			while (rs.next()) {
				table1rowCount++;
				values = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber; i++) {

					if (rs.getString(i) != null) {

						rs.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					values.add(rs.getString(i));
				}
				System.out.println("Stored Username in DB:" + " " + values);

				list.addAll(values);

			}

			System.out.println(table1rowCount);
			System.out.println("Stored Username in DB:" + " " + list);

			Statement stmt1 = conn.createStatement();
			ResultSet rs1 = stmt1.executeQuery(query2);
			ResultSetMetaData rsmd1 = rs1.getMetaData();
			int columnsNumber1 = rsmd1.getColumnCount();
			System.out.println("There were " + columnsNumber1 + " Column count records.");

			while (rs1.next()) {
				table2rowCount++;
				values1 = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber1; i++) {

					if (rs1.getString(i) != null) {

						rs1.getString(i).replaceAll("\\s+", "");
					} else {
						Log.info("*The database value of the cell  *" + " " + i + "is Null");
						System.out.println("value of  cell" + " " + i + "is NULL");
					}
					values1.add(rs1.getString(i));
				}
				System.out.println("Stored Username1 in DB:" + " " + values1);

				list1.addAll(values1);

			}
			System.out.println("Stored Username1 in DB:" + " " + list1);

			if (table1rowCount == table2rowCount && columnsNumber == columnsNumber1) {

				for (K = 0; K <= list.size() - 1; K++) {
					int l = K + 1;

					String tableOneData = (StringUtils.isNullOrEmpty(list.get(K)) ? "" : (list.get(K)));

					System.out.println(tableOneData);
					String tableTwoData = (StringUtils.isNullOrEmpty(list1.get(K)) ? "" : (list1.get(K)));

					System.out.println(tableTwoData);

					if ((tableOneData).equalsIgnoreCase(tableTwoData)) {
						System.out.println(" Element is Equal");
						Log.info("Element is Equal");
						System.out.println("Passed");
						Log.info("Passed");
						System.out.println("*------------------------------------------------------------------");
						Log.info("TestCase Passed because all elements are same");
					} else {
						System.out.println("Element not equal");
						System.out.println("TABLE ONE VALUE = " + tableOneData);
						System.out.println("TABLE TWO VALUE    = " + tableTwoData);
						Log.info("Element not equal");
						System.out.println("Failed");
						Log.info("Failed");
						System.out.println("*The Element are not same  " + list1.get(K));
						int total = K / columnsNumber;
						int rowno = total + 1;
						System.out.println("Row number " + rowno);
						Log.info("Row number " + rowno);
						String row = String.valueOf(rowno);
						int cellcnt = ((K) - (columnsNumber * total));
						int cellno = cellcnt + 1;
						System.out.println("CellNumber " + cellno);
						Log.info("CellNumber " + cellno);
						String cell = String.valueOf(cellno);
						System.out.println("*------------------------------------------------------------------");
						String faileddata = "<-----FAIL------>" + " ******* At Count-------> " + counter + " "
								+ "****** DB Data is not equal-------->" + list1.get(K)
								+ " ******* IN ROW NO ----------->" + "  " + rowno + " " + "****** Cell NO----->  "
								+ cellno;
						Log.info("FAIL" + "  " + counter + " " + "DB Data is not equal" + list1.get(K));

						// writeOutPutToFileWhenComparaisonFailed(faileddata);
						counter++;
						counterADD = counter;
						counterADD++;
						notMatchedData.add(list1.get(K));
						rowNO.add(row);
						cellNO.add(cell);

					}
				}
				allNotMatchedData.addAll(notMatchedData);
				rowList.addAll(rowNO);
				cellList.addAll(cellNO);
				if (counter > 0) {

					System.out.println("FAIL" + "  " + counter + " " + "DB Data is not equal" + allNotMatchedData);
					System.out.println(" IN ROW NO" + "  " + rowList + " " + "Cell NO" + cellList);
					System.out.println("*------------------------------------------------------------------");
					System.out.println("All elements are not same TestCase Failed");
					Log.info("Failed");
					// TODO:: Call the method to write into Excel file for the failure one
					counter = 0;
					allNotMatchedData.clear();
					notMatchedData.clear();
				}
				if (counterADD > 0) {
					System.out.println("Failed");
					Log.info("Failed");
					Log.info("TestCase failed because all elements are not equals");
					ExecuteTestcase.bResult = false;
					System.out.println("TestCase failed because all elements are not equals");
				}
				list1.clear();
			}

			else {
				System.out.println("Row and column count are not equal Between the table");
				Log.info("Row and column count are not equal Between the table");
				ExecuteTestcase.bResult = false;

			}

		}

		catch (ClassNotFoundException ex) {
			System.out.println("Could not find database driver class");
			Log.info("Could not find database driver class");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
		} catch (SQLException ex) {
			Log.info("An error occurred.");
			ExecuteTestcase.bResult = false;
			ex.printStackTrace();
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

	public static void CALogin(String object, String data) {
		/***************************************************************************************************
		 * /** Des: This function is used to navigate to BASE_URL. BASE_URL is define on
		 * CONSTANTS.JAVA /
		 ***************************************************************************************************/

		try {
			Log.info("*Try to Login to CA application*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.navigate().to(Constants.DASHBOARD_URL);
			DRIVER.findElement(By.xpath(OR.getProperty("TXT_USERNAME"))).sendKeys(Constants.DASHBOARD_UN);
			DRIVER.findElement(By.xpath(OR.getProperty("TXT_PASSWORD"))).sendKeys(Constants.DASHBOARD_PWD);
			DRIVER.findElement(By.xpath(OR.getProperty("BTN_CALOGIN"))).click();
			Log.info("Logged in to BASE_URL");
		} catch (Exception e) {
			Log.info("-------- Unable to log in to BASE_URL -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void AdminLogin(String object, String data) {
		/***************************************************************************************************
		 * /** Des: This function is used to navigate to BASE_URL. BASE_URL is define on
		 * CONSTANTS.JAVA /
		 **************************************************************************************************/

		try {
			Log.info("*Try to Login to CA application*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			DRIVER.navigate().to(Constants.ADMIN_URL);
			DRIVER.findElement(By.xpath(OR.getProperty("TXT_USERNAME"))).sendKeys(Constants.ADMIN_UN);
			DRIVER.findElement(By.xpath(OR.getProperty("TXT_PASSWORD"))).sendKeys(Constants.ADMIN_PWD);
			DRIVER.findElement(By.xpath(OR.getProperty("BTN_ADMINLOGIN"))).click();
			Log.info("Logged in to BASE_URL");
		} catch (Exception e) {
			Log.info("-------- Unable to log in to BASE_URL -------- " + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void switchToIFrameWithID2(String object, String data) {
		/***************************************************************************************************
		 * /** Desc: this function use to switch To IFrame With ID /
		 ***************************************************************************************************/
		Boolean valueResult = true;
		try {
			Log.info("*Try to switch to iFrame with iFrameID*");
			DRIVER.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			List<WebElement> iframes = DRIVER.findElements(By.tagName("iframe"));
			for (WebElement iframe : iframes) {
				System.out.println(iframe);
				if (iframe.getAttribute("id").equals(data)) {
					DRIVER.switchTo().frame(data);
					valueResult = true;
					break;
				} else {
					valueResult = false;
				}
			}
			if (valueResult) {
				Log.info("We are in iFrame with ID: " + data);
			} else {
				Log.info("Unable to switch to iFrame with iFrame ID: " + data);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to switch to iFrame with iFrame ID: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void Twofactorauthenticator(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to input two factor authenticator code and
		 * submit * /
		 **************************************************************************************************/

		String passcode = "TXT_TWOWAY_AUTH";
		String submit = "BTN_TWOWAY_SUBMIT";

		/*
		 * try { Log.info("*Try to input google authenticator code *");
		 * DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
		 * 
		 * Totp totp = new Totp(Constants.OtpKeyStr);
		 * 
		 * String twoFactorCode = totp.now();
		 * 
		 * System.out.println(twoFactorCode);
		 * 
		 * WebElement Inputcode =
		 * DRIVER.findElement(By.xpath(OR.getProperty(passcode)));
		 * 
		 * Inputcode.sendKeys(twoFactorCode);
		 * 
		 * Log.info("Inputted passcode : " + twoFactorCode + " to element " +
		 * Inputcode);
		 * 
		 * DRIVER.findElement(By.xpath(OR.getProperty(submit))).click();
		 * 
		 * } catch (Exception e) {
		 * Log.info("-------- Unable to Input text google authenticator code: " +
		 * passcode + " --------" + e.getMessage()); ExecuteTestcase.bResult = false; }
		 */
	}

	public static void Twofactorauthenticator1(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used to input two factor authenticator code and
		 * submit * /
		 **************************************************************************************************/

		String passcode = "TXT_TWOWAY_AUTH";
		String submit = "BTN_TWOWAY_SUBMIT";

		try {
			Log.info("*Try to input google authenticator code *");
			DRIVER.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);

			Totp totp = new Totp(Constants.OtpKeyStr);

			String twoFactorCode = totp.now();

			System.out.println(twoFactorCode);

			WebElement Inputcode = DRIVER.findElement(By.xpath(OR.getProperty(passcode)));

			Inputcode.sendKeys(twoFactorCode);

			Log.info("Inputted passcode : " + twoFactorCode + " to element " + Inputcode);

			DRIVER.findElement(By.xpath(OR.getProperty(submit))).click();

		} catch (Exception e) {
			Log.info("-------- Unable to Input text google authenticator code: " + passcode + " --------"
					+ e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	public static void readQRcode(String object, String data) {
		/**************************************************************************************************
		 * /** Des: This function is used QR code results * /
		 **************************************************************************************************/

		String imagePath = data;
		try {
			Log.info("*Try to read QR code *");

			File file = new File(imagePath);

			BufferedImage bufferedimage = ImageIO.read(file);

			// Process the image
			LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedimage);

			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

			// To Capture details of QR code
			Result result = new MultiFormatReader().decode(binaryBitmap);

			qrcodeResult = result.getText();
			System.out.println(qrcodeResult);

		} catch (Exception e) {
			Log.info("-------- Unable to read QR  code: " + data + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyDescendingOrder(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to verify String in Descending order
		 ************************************************************************************************/
		String[] parts = object.split(",");
		String object1 = parts[0];
		String object2 = parts[1];
		String object3 = parts[2];
		String USER_XPATH = (OR.getProperty(object2));
		int i;
		boolean descending = false;
		try {
			Log.info("*Try to verify Descending Order*");
			String pagination = DRIVER.findElement(By.xpath(OR.getProperty(object1))).getText();
			int size = Integer.parseInt(pagination);
			ArrayList<String> List = new ArrayList<>();
			System.out.println(size);
			for (i = 1; i <= size; i++) {
				String fullXpath = String.format(USER_XPATH, i + 1);
				WebElement pageclick = DRIVER.findElement(By.xpath(fullXpath));
				Thread.sleep(5000);
				pageclick.click();
				List<WebElement> elementList = DRIVER.findElements(By.xpath(OR.getProperty(object3)));
				for (WebElement we : elementList) {
					List.add(we.getText().toLowerCase());
				}

			}

			ArrayList<String> obtainedList = new ArrayList<>();
			for (String s : List) {
				obtainedList.add(s.toLowerCase());
			}
			System.out.println(obtainedList);
			ArrayList<String> sortedList = new ArrayList<>();
			for (String s : obtainedList) {
				sortedList.add(s.toLowerCase());
			}
			Collections.sort(sortedList);
			Collections.reverse(sortedList);
			System.out.println(sortedList);
			Assert.assertTrue(sortedList.equals(obtainedList));
			descending = true;
			if (descending) {
				Log.info("It is Sorted in Descending  order" + object);
			} else {
				Log.info("It is not Sorted in Descending order: " + object);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify data in Descending order  " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyAscendingOrder(String object, String data) throws InterruptedException {
		/*************************************************************************************************
		 * /** Des: This function is used to verify Ascending order
		 ************************************************************************************************/
		String[] parts = object.split(",");
		String object1 = parts[0];
		String object2 = parts[1];
		String object3 = parts[2];
		String USER_XPATH = (OR.getProperty(object2));
		int i;
		boolean ascending = false;
		try {
			Log.info("*Try to verify ascending Order*");
			String pagination = DRIVER.findElement(By.xpath(OR.getProperty(object1))).getText();

			Thread.sleep(5000);
			System.out.println(pagination);
			int size = Integer.parseInt(pagination);
			ArrayList<String> List = new ArrayList<>();
			System.out.println(size);
			for (i = 1; i <= size; i++) {
				String fullXpath = String.format(USER_XPATH, i + 1);
				WebElement pageclick = DRIVER.findElement(By.xpath(fullXpath));
				System.out.println(fullXpath);
				Thread.sleep(5000);
				pageclick.click();
				List<WebElement> elementList = DRIVER.findElements(By.xpath(OR.getProperty(object3)));
				for (WebElement we : elementList) {
					List.add(we.getText().toLowerCase());
				}

			}

			ArrayList<String> obtainedList = new ArrayList<>();
			for (String s : List) {
				obtainedList.add(s.toLowerCase());
			}
			System.out.println(obtainedList);

			ArrayList<String> sortedList = new ArrayList<>();
			for (String s : obtainedList) {
				sortedList.add(s.toLowerCase());
			}
			System.out.println(sortedList);
			Collections.sort(sortedList);
			Assert.assertTrue(sortedList.equals(obtainedList));
			ascending = true;
			if (ascending = true) {
				Log.info("It is Sorted in ascending order" + object);
			} else {
				Log.info("It is not Sorted in ascending order: " + object);
				ExecuteTestcase.bResult = false;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify Sorted in ascending order  " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyAscendingOrderDate(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to verify Date in Ascending order
		 ************************************************************************************************/

		String[] parts = object.split(",");
		String object1 = parts[0];
		String object2 = parts[1];
		String object3 = parts[2];
		String USER_XPATH = (OR.getProperty(object2));
		int i;
		boolean ascending = false;
		try {
			Log.info("*Try to verify Ascending Order*");
			String pagination = DRIVER.findElement(By.xpath(OR.getProperty(object1))).getText();
			int size = Integer.parseInt(pagination);
			ArrayList<String> List = new ArrayList<>();
			System.out.println(size);
			for (i = 1; i <= size; i++) {
				String fullXpath = String.format(USER_XPATH, i + 1);
				WebElement pageclick = DRIVER.findElement(By.xpath(fullXpath));
				System.out.println(fullXpath);
				Thread.sleep(5000);
				pageclick.click();
				List<WebElement> elementList = DRIVER.findElements(By.xpath(OR.getProperty(object3)));
				for (WebElement we : elementList) {
					List.add(we.getText().toLowerCase());

				}

			}
			ArrayList<String> obtainedList = new ArrayList<>();
			for (String s : List) {
				obtainedList.add(s);
			}

			ArrayList<Date> dates = new ArrayList<>(obtainedList.size());
			for (String s : obtainedList) {
				Date dateObj = new SimpleDateFormat("MM/dd/yyyy").parse(s);
				dates.add(dateObj);
			}
			Collections.sort(dates);

			ArrayList<String> sortedList = new ArrayList<>();
			for (Date d : dates) {
				String str = new SimpleDateFormat("MM/dd/yyyy").format(d);
				sortedList.add(str.trim());
			}

			System.out.println(sortedList);
			System.out.println(obtainedList);
			Assert.assertTrue(sortedList.equals(obtainedList));
			ascending = true;
			if (ascending) {
				Log.info("Date is Sorted in ascending order" + object);
			} else {
				Log.info("Date is not sorted in ascending order: " + object);
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify date in ascending order  " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}
	}

	public static void verifyDescendingOrderDate(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to verify Descending order in date format
		 * MM/DD/YYYY Format
		 ************************************************************************************************/

		String[] parts = object.split(",");
		String object1 = parts[0];
		String object2 = parts[1];
		String object3 = parts[2];
		String userXpath = (OR.getProperty(object2));
		boolean descendingdate = false;
		try {
			Log.info("**");
			String pagination = DRIVER.findElement(By.xpath(OR.getProperty(object1))).getText();
			int size = Integer.parseInt(pagination);
			ArrayList<String> List = new ArrayList<>();
			System.out.println(size);
			for (int i = 1; i <= size; i++) {
				String fullXpath = String.format(userXpath, i + 1);
				WebElement pageclick = DRIVER.findElement(By.xpath(fullXpath));
				Thread.sleep(5000);
				pageclick.click();
				List<WebElement> elementList = DRIVER.findElements(By.xpath(OR.getProperty(object3)));
				for (WebElement we : elementList) {
					List.add(we.getText().toLowerCase());

				}

			}
			ArrayList<String> obtainedList = new ArrayList<>();
			for (String s : List) {
				obtainedList.add(s);
			}

			ArrayList<Date> dates = new ArrayList<>(obtainedList.size());
			for (String s : obtainedList) {
				Date dateObj = new SimpleDateFormat("MM/dd/yyyy").parse(s);
				dates.add(dateObj);
			}

			Collections.sort(dates);
			Collections.reverse(dates);

			ArrayList<String> sortedList = new ArrayList<>();
			for (Date d : dates) {
				String str = new SimpleDateFormat("MM/dd/yyyy").format(d);
				sortedList.add(str.trim());
			}

			System.out.println(sortedList);
			System.out.println(obtainedList);
			Assert.assertTrue(sortedList.equals(obtainedList));
			descendingdate = true;
			if (descendingdate) {
				Log.info("Date is Sorted in Descending order" + object);
			} else {
				Log.info("Date is not Descending order: " + object);
				ExecuteTestcase.bResult = true;
			}
		} catch (Exception e) {
			Log.info("-------- Unable to verify date in Descending order  " + object + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

	@SuppressWarnings("unchecked")
	public static void PostCallReprocess(String object, String data) {
		/*************************************************************************************************
		 * /** Des: This function is used to POSt process
		 * Appointment_ProcessInboundMessage API
		 ************************************************************************************************/

		String[] parts = data.split(",");
		String patID = parts[0];
		String appNO = parts[1];

		try {
			Log.info("*Try to Reprocess API*");
			RestAssured.baseURI = "https://caautomation5.healthasyst.com:20011/Appointment";
			RequestSpecification request = RestAssured.given();

			JSONObject requestParams = new JSONObject();
			requestParams.put("PatientId", patID);
			requestParams.put("AppNo", appNO);
			requestParams.put("OrganizationId", Constants.organizationID);
			requestParams.put("ApptStatus", "Status");
			requestParams.put("OrganizationName", Constants.organizationName);
			requestParams.put("IsOndemandTransaction", true);
			requestParams.put("IsRis", false);
			request.header("Content-Type", "application/json");
			request.body(requestParams.toJSONString());
			Response response = request.post("/Appointment_ProcessInboundMessage");
			int statusCode = response.getStatusCode();
			Assert.assertEquals(statusCode, 200);
			ExecuteTestcase.bResult = true;
			Log.info("*API Reprocessed Sucessfully*");
			System.out.println("*API Reprocessed Sucessfully*");
		}

		catch (Exception e) {
			Log.info("-------- Unable to Reprocess API  " + " --------" + e.getMessage());
			ExecuteTestcase.bResult = false;
		}

	}

}
