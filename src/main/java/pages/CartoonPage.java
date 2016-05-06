package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by kevin on 06.05.16.
 */
public class CartoonPage extends Page {
	private static String baseUrl = "Cartoon/";

	By byEpisodeListing = By.cssSelector("table.listing");
	By byEpisodeLinks = By.cssSelector("a[href^='/Cartoon/']");

	public CartoonPage(WebDriver webDriver, String cartoon) {
		super(webDriver, baseUrl + cartoon);
	}

	public List<EpisodePage> getEpisodes() {
		List<WebElement> episodeElements = getDriver().findElement(byEpisodeListing).findElements(byEpisodeLinks);
		//read the needed url part with regex
		Stream<EpisodePage> episodeStream = episodeElements.stream().map(x -> new EpisodePage(getDriver(), x.getAttribute("href").substring(22)));
		return episodeStream.collect(Collectors.toList());
	}

	@Override
	public String getTitle() {
		String fullTitle = super.getTitle();

		Scanner scanner = new Scanner(fullTitle);
		return scanner.nextLine();
	}
}
