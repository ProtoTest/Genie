package com.prototest.TG_WEB.ui;

import com.thoughtworks.selenium.Selenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestCase {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		WebDriver driver = new FirefoxDriver();
		String baseUrl = "http://help.change.org/";
		selenium = new WebDriverBackedSelenium(driver, baseUrl);
	}

	@Test
	public void testForm1() throws Exception {

		selenium.open("http://help.change.org/anonymous_requests/new");
		selenium.typeKeys("id=email_address", "test@yahoo.com");
		selenium.typeKeys("id=ticket_subject", "Test Subject");
		selenium.type("id=comment_value", "This is Just Test");
		selenium.typeKeys("id=ticket_fields_20736566", "test first mane");
		selenium.typeKeys("id=ticket_fields_20734118", "test last name");
		selenium.select("id=ticket_fields_21040183", "label=Petition creator (organization)");
		selenium.typeKeys("id=ticket_fields_20739407", "www.yahoo.com");
		selenium.select("id=ticket_fields_506066", "label=Windows XP");
		selenium.select("id=ticket_fields_506067", "label=Internet Explorer 9");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
