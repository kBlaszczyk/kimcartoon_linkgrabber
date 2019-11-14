package de.orchound.selenium.pages;

import org.openqa.selenium.WebDriver;

/**
 * Created by kevin on 06.05.16.
 */
public class Page {
	private WebDriver webDriver;
	private String url = "https://kimcartoon.to/";

	public Page(WebDriver webDriver, String subUrl) {
		this.webDriver = webDriver;
		this.url = this.url + subUrl;
	}

	public WebDriver getDriver() {
		return webDriver;
	}

	public String getUrl() {
		return webDriver.getCurrentUrl();
	}

	public void open() {
		webDriver.get(url);
	}

	public String getTitle() {
		return webDriver.getTitle();
	}
}
