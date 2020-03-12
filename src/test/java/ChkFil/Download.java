package ChkFil;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;

public class Download {

	private WebDriver driver;
	private String downloadFilePath = "C:\\Users\\casa\\Downloads";
  @BeforeClass
  public void beforeClass() {
	  System.setProperty("webdriver.chrome.driver", "./src/test/resources/Driver/chromedriver.exe");
	  HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	  chromePrefs.put("profile.default_content_setting.popups", 0);
	  chromePrefs.put("download.default_directory", downloadFilePath);
	  
	  //Here setting options Chrome, Change open windows to download file directly
	  ChromeOptions options = new ChromeOptions();
	  options.setExperimentalOption("prefs", chromePrefs);
	  driver = new ChromeDriver(options);
	  	  
  }
  @Test
  public void downloaFile() throws InterruptedException {
	  driver.get("http://the-internet.herokuapp.com/download");
	  driver.findElement(By.cssSelector("div.example>a")).click();
	  Thread.sleep(2000);
	  
	  //Find all files in folder
	  File folder = new File(downloadFilePath);
	  File[] lisofFile = folder.listFiles();
	  assertTrue(lisofFile.length>0, "Dont file in folder");
  }


  @AfterClass
  public void afterClass() {
  }

}
