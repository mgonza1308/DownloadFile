package ChkFil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Download {

	@FindBy(xpath = "//*[@id=\'content\']")
	public List<WebElement> MuniRegion;

	@FindBy(xpath = "//*[@id='cityhall']")
	public WebElement Titulo;

	@FindBy(xpath = "(//span[@class='grey' and contains(text(),'Internacional')])[1]")
	public WebElement Telefono;

	@FindBy(xpath = "(//span[@class='grey' and contains(text(),'Internacional')])[2]")
	public WebElement Fax;

	@FindBy(xpath = "//a[contains(@title, 'Sitio internet del municipio')]")
	public WebElement Web;

	@FindBy(xpath = "//th[contains(text(),'Partido político')]/following-sibling::td")
	public WebElement PartidoPol;
	
	@FindBy(xpath = "//th[contains(text(),'Alcalde')]/following-sibling::td")
	public WebElement AlcaldeLoc;


	WebDriver driver;
	public String Region1, direcc1 = null;
	public int paginas = 0;
	
	@BeforeTest
	public void beforeClass() {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/Driver/chromedriver.exe");
		driver = new ChromeDriver();
		PageFactory.initElements(driver, this);
		driver.get("https://www.la-municipalidad.cl/");
		driver.manage().window().maximize();
	}

	@Test
	public void downloaFile() throws InterruptedException, IOException {
		try {
			
			Charset.forName("UTF-8").newEncoder();
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement Element = driver.findElement(By.xpath("//h2[contains(text(),'Región del país')]"));
			js.executeScript("arguments[0].scrollIntoView();", Element);

			File file = new File("./src/main/resources/munis.csv");
			FileWriter fr = new FileWriter(file.getAbsoluteFile(), false);
			BufferedWriter bw = new BufferedWriter(fr);
			PrintWriter pw = new PrintWriter(bw);

			List<WebElement> region = driver.findElements(By.xpath("//a[contains(@href,'region')]"));
			String[] regionComunidad = new String[region.size()];
			String[] regionSinLink = new String[region.size()];
			for (int x = 0; x < region.size(); x++) {
				regionComunidad[x] = region.get(x).getAttribute("href");
				regionSinLink[x] = region.get(x).getText();
				System.out.println("indice: " + x + "Region: " + region.get(x).getText());
			}

			for (int i = 0; i < regionComunidad.length; i++) {
				driver.get(regionComunidad[i]);
				//driver.get(regionComunidad[13]);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				System.out.println("Region: " + regionComunidad[i]);

				List<String> muniComunidad = new ArrayList<>();
				int num = 0;
				try {
					List<String> pages = new ArrayList<>();
					
					try {
						// Down to ending page
						((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
						//WebElement that find pages quantity
						 List<WebElement> Paginacion =driver.findElements(By.xpath("//a[contains(@href,'-region-')]"));
						 int cont=2;
						 paginas = Paginacion.size();
						 for(int l =0; l<paginas; l++) {
							 String Cadena ="//a[contains(@href,'"+cont+"-region-')]";
							 pages.add(Cadena);
							 System.out.println("Cadena;: "+pages.get(l).toString());
							 cont++;
						 }
					}catch(Exception e) {
						System.out.println("errore: "+e);
					}
					 
					 
					num= 0;
					do {
						
						List<WebElement> MuniLink = driver
								.findElements(By.xpath("//a[contains(@href,'municipalidad')]"));
						for (int t = 0; t < MuniLink.size(); t++) {
							muniComunidad.add(MuniLink.get(t).getAttribute("href"));
							//System.out.println("URL: " + MuniLink.get(t).getAttribute("href"));
						}
						
						
						
						driver.findElement(By.xpath(pages.get(num))).click();
						++num;
					} while (num <= pages.size());
				} catch (Exception e) {
					System.out.println("errore: "+e);
					
				}

				Iterator<String> nombreIterator = muniComunidad.iterator();
				while (nombreIterator.hasNext()) {
					String titulo, direcc, telefono, fax, web, partido = null, alcalde = null;
					String enlace = nombreIterator.next();
					driver.get(enlace);
					WebElement dire = driver.findElement(By.xpath("//span[@class='street-address']"));
					js.executeScript("arguments[0].scrollIntoView();", Titulo);
					try {
						titulo = Titulo.getText();
					} catch (Exception e) {
						e.printStackTrace();
						titulo = "No hay informacion";
					}
					;
					try {
						partido = PartidoPol.getText();
					} catch (Exception e) {
						e.printStackTrace();
						titulo = "No hay informacion";
					}
					;
					try {
						alcalde = AlcaldeLoc.getText();
					} catch (Exception e) {
						e.printStackTrace();
						titulo = "No hay informacion";
					}
					;
					try {
						direcc = dire.getText();
						int salto = direcc.indexOf("\n");
						Region1 = direcc.substring(0, salto);
						direcc1 = direcc.substring(salto + 1);
					} catch (Exception e) {
						e.printStackTrace();
						direcc = null;
					}
					;
					try {
						telefono = Telefono.getText();
					} catch (Exception e) {
						e.printStackTrace();
						telefono = "No hay informacion";
					}
					;
					try {
						fax = Fax.getText();
					} catch (Exception e) {
						e.printStackTrace();
						fax = "No hay informacion";
					}
					;
					try {
						web = Web.getText();
					} catch (Exception e) {
						e.printStackTrace();
						web = "No hay informacion";
					}
					;
					System.out.println("Region: " + Region1);
					System.out.println("Direccion: " + direcc1);
					System.out.println("Teñlefono: " + telefono);
					System.out.println("Fax: " + fax);
					System.out.println("web: " + web);
					try {

						// create a new file
						if (!file.exists()) {
							file.createNewFile();
							bw.write("Region;Comuna;Direccion;Telefono;Fax; web\n");

						} else {
							String cadena = regionSinLink[i] + ';' + titulo + ';' + Region1 + ';' + direcc1 + ';'
									+ telefono + ';' + fax + ';' + web + ';' + alcalde + ';' + partido;
							pw.println(cadena);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			bw.close();
			pw.close();

		} catch (StaleElementReferenceException e) {
			System.out.println("Error: " + e);
		}
	}

	@AfterClass
	public void afterClass() {
		driver.close();
	}

}
