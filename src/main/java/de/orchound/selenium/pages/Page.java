package de.orchound.selenium.pages;

import org.openqa.selenium.WebDriver;

/**
 * Created by kevin on 06.05.16.
 */
public class Page {

	protected WebDriver webDriver;
	protected String url;

	public Page(WebDriver webDriver, String url) {
		this.webDriver = webDriver;
		this.url = url;
	}

	public WebDriver getDriver() {
		return webDriver;
	}

	public String getUrl() {
		return url;
	}

	public void open() {
		webDriver.get(url);
	}

	public String getTitle() {
		return webDriver.getTitle();
	}
}
