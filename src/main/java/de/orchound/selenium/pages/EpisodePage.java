package de.orchound.selenium.pages;

import de.orchound.selenium.exceptions.PossibleCaptchaException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by kevin on 06.05.16.
 */
public class EpisodePage extends Page {

	private final static By byVideo = By.id("my_video_1_html5_api");
	private final static By byEpisode = By.cssSelector("#selectEpisode>option[selected]");
	private final static By byQualitySelect = By.id("selectQuality");
	private final static By byServerSelect = By.id("selectServer");

	private Select serverSelect;
	private Select qualitySelect;
	private String episodeName;

	public EpisodePage(WebDriver webDriver, String url) {
		super(webDriver, url);
	}

	@Override
	public void open() {
		super.open();
		initPage();
	}

	public String getServer() {
		return serverSelect != null ? serverSelect.getFirstSelectedOption().getText() : "";
	}

	public void setServer(String targetServer) {
		if (serverSelect != null) {
			serverSelect.selectByVisibleText(targetServer);
			initPage();
		}
	}

	public String getQuality() {
		return qualitySelect.getFirstSelectedOption().getText();
	}

	public void setQuality(String targetQuality) {
		qualitySelect.selectByVisibleText(targetQuality);
		initPage();
	}

	public Set<String> getAvailableQualityOptions() {
		return qualitySelect.getOptions().stream()
			.map(WebElement::getText).collect(Collectors.toSet());
	}

	public String getVideoLink() {
		try {
			String url = webDriver.findElement(byVideo).getAttribute("src");
			return includeFilenameInUrl(url);
		} catch (NoSuchElementException ex) {
			throw new PossibleCaptchaException();
		}
	}

	private void initPage() {
		try {
			episodeName = webDriver.findElement(byEpisode).getText();
			qualitySelect = new Select(webDriver.findElement(byQualitySelect));
		} catch (NoSuchElementException ex) {
			throw new PossibleCaptchaException();
		}

		try {
			serverSelect = new Select(webDriver.findElement(byServerSelect));
		} catch (NoSuchElementException ex) {
			serverSelect = null;
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
