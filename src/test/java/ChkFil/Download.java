package ChkFil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class Download {

//	@FindBy(xpath="//a[contains(@href,'municipalidad')]")
//	public List<WebElement> MuniLink;
	
	@FindBy(xpath="//*[@id=\"content\"]")
	public List<WebElement> MuniRegion;
	
  @Test
  public void downloaFile() throws InterruptedException {
	  try {
		  System.setProperty("webdriver.chrome.driver", "./src/test/resources/Driver/chromedriver.exe"); 
		  WebDriver driver = new ChromeDriver();
		  driver.get("https://www.la-municipalidad.cl/");
		  JavascriptExecutor js = (JavascriptExecutor) driver;
		  WebElement Element = driver.findElement(By.xpath("//h2[contains(text(),'Región del país')]"));
		  js.executeScript("arguments[0].scrollIntoView();", Element); 
		  List<WebElement> region = driver.findElements(By.xpath("//a[contains(@href,'region')]"));
		  for (int i=1; i<=region.size(); i++) {
			
			  String origen = region.get(i).getAttribute("href");
			  System.out.println("Url: "+origen);
			  region.get(i).click();
			  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			  List<WebElement> MuniLink = driver.findElements(By.xpath("//a[contains(@href,'municipalidad')]"));
			  for (int t =1; t<=MuniLink.size(); t++) {

				  
				  driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				  

				 String muniComunidad = MuniLink.get(t).getAttribute("href");
				 System.out.println("Munoi de local: "+muniComunidad);
				 driver.get(muniComunidad);
//				  MuniLink.get(t).click();
				  
				  WebElement dire = driver.findElement(By.xpath("//span[@class='street-address']"));
				  System.out.println("Direccion"+dire.getText());
				  
				  driver.get(origen);
				  String pages = driver.getWindowHandle();
				  System.out.println("pagina: "+pages.toString());
				  driver.switchTo().window(pages);


			  }
			  js.executeScript("window.history.go(-1)");
		  }

		  
	  }catch (StaleElementReferenceException e) {
		  System.out.println("Error: "+e);
	  }
  }


  @AfterClass
  public void afterClass() {
	  //driver.close();
  }

}
