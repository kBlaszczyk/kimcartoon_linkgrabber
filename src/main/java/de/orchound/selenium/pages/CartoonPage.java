package de.orchound.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kevin on 06.05.16.
 */
public class CartoonPage extends Page {
	private static String baseUrl = "https://%s/Cartoon/";

	private By byEpisodeListing = By.cssSelector("table.listing");
	private By byEpisodeLinks = By.cssSelector("a[href^='/Cartoon/']");

	private String title;

	public CartoonPage(WebDriver webDriver, String domain, String cartoon) {
		super(webDriver, String.format(baseUrl, domain) + cartoon);
		title = cartoon;
	}

	public List<EpisodePage> getEpisodes() {
		List<WebElement> episodeElements = webDriver.findElement(byEpisodeListing).findElements(byEpisodeLinks);

		return episodeElements.stream()
			.map(x -> new EpisodePage(webDriver, x.getAttribute("href") + "&s=beta"))
			.collect(Collectors.toList());
	}

	@Override
	public String getTitle() {
		return title;
	}
}
