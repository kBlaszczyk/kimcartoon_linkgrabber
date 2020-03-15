package de.orchound.selenium;

import de.orchound.selenium.exceptions.PossibleCaptchaException;
import de.orchound.selenium.exceptions.UnsupportedServerException;
import de.orchound.selenium.pages.CartoonPage;
import de.orchound.selenium.pages.EpisodePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 06.05.16.
 */
public class Main {
	private static boolean testmode = false;

	public static void main(String[] args) {
		if (args.length > 0) {
			if (args.length == 2) {
				testmode = args[1].equals("test");
				System.out.println("--- testmode ---");
			}

			String cartoon = args[0];
			getEpisodeLinks(cartoon);
		} else {
			printHelp();
		}
	}

	private static void getEpisodeLinks(String cartoon) {
		WebDriver webDriver = new FirefoxDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		CartoonPage cartoonPage = new CartoonPage(webDriver, cartoon);
		cartoonPage.open();
		List<EpisodePage> episodes = cartoonPage.getEpisodes();

		String cartoonTitle = cartoonPage.getTitle();
		Collection<String> episodeLinks = Collections.emptyList();
		try {
			if (testmode)
				episodeLinks = grabEpisodeLinks(episodes.subList(0, 2));
			else
				episodeLinks = grabEpisodeLinks(episodes);
		} catch (WebDriverException ex) {
			ex.printStackTrace();
		} finally {
			webDriver.close();
		}

		try (FileWriter fileWriter = createFileWriter(cartoonTitle + ".txt")) {
			fileWriter.write(String.join("\n", episodeLinks));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static Collection<String> grabEpisodeLinks(Collection<EpisodePage> episodes) {
		ArrayList<String> episodeLinks = new ArrayList<>();

		for (EpisodePage episode : episodes) {
			int trials = 2;

			while (trials > 0) {
				try {
					episode.open();
					prepareEpisodePage(episode);
					episodeLinks.add(episode.getVideoLink());
				} catch (PossibleCaptchaException ex) {
					if (trials != 1)
						waitForEnter();
					else
						episodeLinks.add("skipped episode: " + episode.getTitle());
				} catch (UnsupportedServerException ex) {
					trials = 0;
					episodeLinks.add("Unsupported server, skipped episode: " + episode.getTitle());
				}

				trials--;
			}
		}

		return episodeLinks;
	}

	private static void prepareEpisodePage(EpisodePage episode) {
		String server = episode.getServer();
		if (!server.isEmpty() && !server.equals("KimCartoon Beta"))
			throw new UnsupportedServerException(server);
		Set<String> qualityOptions = episode.getAvailableQualityOptions();
		String targetQuality = "720p";
		if (!episode.getQuality().equals(targetQuality) && qualityOptions.contains(targetQuality))
			episode.setQuality(targetQuality);
	}

	private static FileWriter createFileWriter(String filename) {
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(filename);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("could not open file: " + filename);
		}

		return fileWriter;
	}

	private static void printHelp() {
		System.out.println("Provide the Cartoon name as command line parameter.");
	}

	private static void waitForEnter() {
		System.out.println("waiting for enter");
		try {
			System.in.read();
		} catch (IOException ex) {
			throw new RuntimeException("IOException");
		}
	}
}
