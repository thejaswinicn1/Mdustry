package support;

import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

public class BatchFile {

	public static void main(String[] args) throws IOException {
		
				
		String filePath = "C:\\HealthAsyst\\ca_automation3\\HA.Batch.Notifications\\HA.Batch.Notifications.exe";

		String strCmdLine = null;
		Process p = Runtime.getRuntime().exec("./Library/Winium.exe");
		try {
		Log.info("*Try to execute exe on Remote machine*");
		System.out.println("*Try to execute exe on Remote machine*");
		Thread.sleep(3000);
		DesktopOptions option = new DesktopOptions();
		option.setLaunchDelay(5);
		option.setApplicationPath(filePath);
		WiniumDriver driver = new WiniumDriver(new URL("http://172.20.30.70:9999"),option);
		System.out.println("Peformed winium");
		Thread.sleep(5000);
		p.destroy();
		System.out.println("Destroy winium");
		strCmdLine = String.format("taskkill /im Winium.exe/f");
		Runtime.getRuntime().exec(strCmdLine);
		Log.info("Invoked Application successfully");
		} 
		
		catch (Exception e) {
		
		p.destroy();
		strCmdLine = String.format("taskkill /im Winium.exe /f");
		Runtime.getRuntime().exec(strCmdLine);
		System.out.println("Unable to perform Destroy winium");



		}



		}







		
	
		
		
		
		
		
		
		
		
		
		
		
		
		

}

