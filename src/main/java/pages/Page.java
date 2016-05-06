package pages;

import org.openqa.selenium.WebDriver;

/**
 * Created by kevin on 06.05.16.
 */
public class Page {
	private WebDriver webDriver;
	private String url = "http://kisscartoon.me/";

	public Page(WebDriver webDriver, String url) {
		this.webDriver = webDriver;
		this.url = this.url + url;
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
