package de.orchound.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kevin on 06.05.16.
 */
public class CartoonPage extends Page {
	private static String baseUrl = "Cartoon/";

	private By byEpisodeListing = By.cssSelector("table.listing");
	private By byEpisodeLinks = By.cssSelector("a[href^='/Cartoon/']");

	private String title;

	public CartoonPage(WebDriver webDriver, String cartoon) {
		super(webDriver, baseUrl + cartoon);
		title = cartoon;
	}

	public List<EpisodePage> getEpisodes() {
		List<WebElement> episodeElements = getDriver().findElement(byEpisodeListing).findElements(byEpisodeLinks);

		Stream<EpisodePage> episodeStream = episodeElements.stream().map(this::initEpisodeFromWebElemenrt);
		return episodeStream.collect(Collectors.toList());
	}

	@Override
	public String getTitle() {
		return title;
	}

	private EpisodePage initEpisodeFromWebElemenrt(WebElement webElement) {
		Pattern pattern = Pattern.compile("https://kimcartoon.to/(.*)");

		Matcher subUrlMatcher = pattern.matcher(webElement.getAttribute("href"));
		if (subUrlMatcher.find())
			return new EpisodePage(getDriver(), subUrlMatcher.group(1));
		else
			throw new IllegalArgumentException("couldn't find episode url.");
	}
}
