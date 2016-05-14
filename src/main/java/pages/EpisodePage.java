package pages;

import exceptions.PossibleCaptchaException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 06.05.16.
 */
public class EpisodePage extends Page {
	private By byVideoLink = By.linkText("HERE");
	private By byVideo = By.tagName("video");
	private By byFilenameContainer = By.id("divFileName");

	private String episodeName = null;

	//https://r1---sn-5hnedne6.googlevideo.com/videoplayback?id=d3896c3b011f886b&itag=18&source=picasa&begin=0&requiressl=yes&pl=20&mime=video/mp4&lmt=1417505760451597&ip=217.66.60.52&ipbits=8&expire=1462565063&sparams=expire,id,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,nh,pl,requiressl,source&signature=3F94A39D8D43C3E1BD715DCA8CC7E8B6E5D63F28.2708EDD5B6C5968A386DE6127AC76E63EEF63D34&key=cms1&redirect_counter=1&req_id=f167799c855a3ee&cms_redirect=yes&mm=34&mn=sn-5hnedne6&ms=ltu&mt=1462535980&mv=u&nh=IgpwcjAxLmJlcjAyKgkxMjcuMC4wLjE
	public EpisodePage(WebDriver webDriver, String url) {
		super(webDriver, url);
	}

	@Override
	public void open() {
		super.open();
		initEpisodeName();
	}

	public String getVideoLink() {
		try {
			(new WebDriverWait(getDriver(), 5)).until(ExpectedConditions.elementToBeClickable(byVideoLink)).sendKeys(Keys.RETURN);
			(new WebDriverWait(getDriver(), 5)).until(ExpectedConditions.urlContains("googlevideo.com"));
		} catch (NoSuchElementException ex) {
			throw new PossibleCaptchaException();
		}

		return includeFilenameInUrl(getUrl());
	}

	private void initEpisodeName() {
		String filenameContainerString = getDriver().findElement(byFilenameContainer).getText();
		Matcher matcher = Pattern.compile(":\\s+(.+)\\s+Please").matcher(filenameContainerString);

		if (matcher.find()) {
			episodeName = matcher.group(1);
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
