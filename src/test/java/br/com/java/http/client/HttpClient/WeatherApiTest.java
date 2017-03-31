package br.com.java.http.client.HttpClient;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

@SuppressWarnings("deprecation")
public class WeatherApiTest {
	
	private WebDriver driver;
	private String baseUrl;
	
	@Before
	public void setUp() throws Exception {
		
		driver = new FirefoxDriver();		
		baseUrl = "http://url";
	}
	
	@After
	public void tearDown() throws Exception {
		driver.close();
		driver.quit();
	}
	

	@Test
	public void test() throws ClientProtocolException, IOException {
		
		driver.get(baseUrl);
		driver.navigate().to(baseUrl + "/login");		
		
//		WebElement webElement = driver.findElement(By.tagName("pre"));
		
		WeatherApiResponse weatherApiResponse = new WeatherApiResponse();
		
		String ExpectedString = weatherApiResponse.GetResponse();
		
//		Assert.assertTrue(webElement.getText().equals(ExpectedString));
		
	}
}
