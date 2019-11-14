package de.orchound.selenium.pages;

import de.orchound.selenium.exceptions.PossibleCaptchaException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 06.05.16.
 */
public class EpisodePage extends Page {
	private By byVideo = By.id("my_video_1_html5_api");
	private By byEpisode = By.cssSelector("#selectEpisode>option[selected]");

	private String episodeName = null;

	public EpisodePage(WebDriver webDriver, String url) {
		super(webDriver, url);
	}

	@Override
	public void open() {
		super.open();
		episodeName = getDriver().findElement(byEpisode).getText();
	}

	public String getVideoLink() {
		try {
			String url = getDriver().findElement(byVideo).getAttribute("src");
			return includeFilenameInUrl(url);
		} catch (NoSuchElementException ex) {
			throw new PossibleCaptchaException();
		}
	}

	private String includeFilenameInUrl(String url) {
		String newUrl = url;

		Matcher matcher = Pattern.compile("(.+)\\?(.+)").matcher(url);
		if (episodeName != null && matcher.find()) {
			String filenameParam = "filename=%s";
			String baseUrl = matcher.group(1);
			String arguments = matcher.group(2);
			newUrl = baseUrl + "?" + String.format(filenameParam, episodeName) + "&" + arguments;
		}

		return newUrl;
	}
}
