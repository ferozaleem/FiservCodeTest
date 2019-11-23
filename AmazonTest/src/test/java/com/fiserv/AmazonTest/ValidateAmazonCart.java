package com.fiserv.AmazonTest;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class ValidateAmazonCart {
	WebDriver driver;

	@Before
	public void startChromerBrowser() {

		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver78.exe");
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

	}

	@Test
	public void searchItem() {
		driver.get("https://www.amazon.com");
		Assert.assertEquals("Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more",
				driver.getTitle());
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Headphones");
		driver.findElement(By.xpath(".//input[@value='Go']")).sendKeys(Keys.ENTER);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		List<WebElement> bestSellers = driver.findElements(By
				.xpath("//span[text()='Best Seller']" + "/ancestor::div[@data-asin and not(.//span[.='Sponsored'])][1]"
						+ "//span[@data-component-type='s-product-image']//a"));
		List<String> bestSellersHrefs = bestSellers.stream().map(element -> element.getAttribute("href"))
				.collect(Collectors.toList());

		bestSellersHrefs.forEach(href -> {
			driver.get(href);
			wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button"))).click();
			boolean success = wait.until(ExpectedConditions.or(
					ExpectedConditions.visibilityOfElementLocated(By.className("success-message")),
					ExpectedConditions.visibilityOfElementLocated(
							By.xpath("//div[@id='attachDisplayAddBaseAlert']//h4[normalize-space(.)='Added to Cart']")),
					ExpectedConditions
							.visibilityOfElementLocated(By.xpath("//h1[normalize-space(.)='Added to Cart']"))));
			Assert.assertEquals(true, success);
		});

	}

	@After
	public void tearDown() {
		driver.close();
	}

}