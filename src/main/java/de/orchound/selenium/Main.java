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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Created by kevin on 06.05.16.
 */
public class Main {
	private static final String defaultDomain = "kimcartoon.to";
	private static final String defaultQuality = "720p";

	private static String cartoon;
	private static String domain;
	private static String quality;
	private static boolean testmode = false;

	public static void main(String[] args) {
		Map<String, String> options = parseOptions(
			Arrays.asList(args).subList(0, args.length - 1)
		);

		if (options.containsKey("help")) {
			printHelp();
		} else {
			cartoon = args[args.length - 1];
			domain = options.getOrDefault("domain", defaultDomain);
			quality = options.getOrDefault("quality", defaultQuality);
			testmode = options.containsKey("test");

			getEpisodeLinks();
		}
	}

	private static void getEpisodeLinks() {
		WebDriver webDriver = new FirefoxDriver();
		webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		CartoonPage cartoonPage = new CartoonPage(webDriver, domain, cartoon);
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
					trials = 0;
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
		if (!episode.getQuality().equals(quality) && qualityOptions.contains(quality))
			episode.setQuality(quality);
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
		System.out.println(String.join("\n",
			"Usage:",
			"java -jar KimCartoon-Linkgrabber.jar [OPTIONS] CARTOON",
			"",
			"CARTOON corresponds to x in the URL https://kimcartoon.to/Cartoon/x",
			"",
			"Options:",
			"-h            --help",
			"              Print this help message.",
			"-d=DOMAIN     --domain",
			"              Specifies the KimCartoon domain.",
			"              Defaults to 'kimcartoon.to'.",
			"-q=QUALITY    --quality",
			"              Specifies the desired video quality.",
			"              Defaults to 720p.",
			"-t            --test",
			"              Only grab the links for two episodes.",
			"",
			"Example:",
			"java -jar KimCartoon-Linkgrabber.jar -t SpongeBob-SquarePants-Season-01",
			""
		));
	}

	private static void waitForEnter() {
		System.out.println("waiting for enter");
		try {
			System.in.read();
		} catch (IOException ex) {
			throw new RuntimeException("IOException");
		}
	}

	private static Map.Entry<String, String> argumentToTuple(String argument) {
		Pattern pattern = Pattern.compile("-{1,2}(\\w)[\\w-]*(?:=([^\\s]+))*");
		Matcher matcher = pattern.matcher(argument);
		if (!matcher.matches())
			throw new IllegalArgumentException();

		String key = matcher.group(1);
		String value = matcher.group(2);

		return new AbstractMap.SimpleEntry<>(key, value);
	}

	private static Map<String, String> parseOptions(Collection<String> args) {
		Map<String, String> shortCutMap = new HashMap<>();
		shortCutMap.put("h", "help");
		shortCutMap.put("d", "domain");
		shortCutMap.put("q", "quality");
		shortCutMap.put("t", "test");

		try {
			return args.stream().map(Main::argumentToTuple).map(x -> {
				String key = shortCutMap.get(x.getKey());
				if (key == null)
					throw new IllegalArgumentException();
				String value = x.getValue();
				return new AbstractMap.SimpleEntry<>(key, value != null ? value : "");
			}).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
		} catch (IllegalArgumentException ex) {
			return Collections.singletonMap("help", null);
		}
	}
}
