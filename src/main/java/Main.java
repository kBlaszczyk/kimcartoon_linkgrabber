import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import pages.CartoonPage;
import pages.EpisodePage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by kevin on 06.05.16.
 */
public class Main {
	public static void main(String[] args) {
		String cartoon = "";
		if (args.length > 0) {
			cartoon = args[0];
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
			writeFileHeader(fileWriter, cartoonTitle);
			for (EpisodePage episode : episodes) {
				episode.open();
				fileWriter.write(episode.getVideoLink());
			}
			fileWriter.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			webDriver.close();
		}
	}

	private static FileWriter createFileWriter(String filename) {
		FileWriter fileWriter;

		try {
			fileWriter = new FileWriter(filename, true);
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("could not open file: " + filename);
		}

		return fileWriter;
	}

	private static void writeFileHeader(FileWriter fileWriter, String cartoonTitle) throws IOException {
		fileWriter.write("\n\n" + cartoonTitle);
		fileWriter.write("==========================================");
		fileWriter.write("\n\n");
	}

	private static void printHelp() {
		System.out.println("cartoon namen als erstes argument übergeben!");
	}
}