package de.orchound.selenium;

import de.orchound.selenium.exceptions.PossibleCaptchaException;
import de.orchound.selenium.pages.CartoonPage;
import de.orchound.selenium.pages.EpisodePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
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
		FileWriter fileWriter = createFileWriter(cartoonTitle + ".txt");
		try {
			if (testmode) {
				for (int i = 0; i < 2; i++) {
					episodes.get(i).open();
					fileWriter.write(episodes.get(i).getVideoLink() + "\n");
				}
			} else {
				for (EpisodePage episode : episodes) {
					boolean linkGrabbed = false;
					while (!linkGrabbed) {
						episode.open();

						try {
							fileWriter.write(episode.getVideoLink() + "\n");
							linkGrabbed = true;
						} catch (PossibleCaptchaException ex) {
							waitForEnter();
						}
					}
				}
			}

			fileWriter.close();
		} catch (IOException | WebDriverException ex) {
			ex.printStackTrace();
		} finally {
			webDriver.close();
		}
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
		System.out.println("cartoon namen als erstes argument übergeben!");
	}

	private static void waitForEnter() throws IOException {
		System.out.println("waiting for enter");
		System.in.read();
	}
}
