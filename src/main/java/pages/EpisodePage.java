package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by kevin on 06.05.16.
 */
public class EpisodePage extends Page {
	By byVideoLink = By.linkText("HERE");

	//https://r1---sn-5hnedne6.googlevideo.com/videoplayback?id=d3896c3b011f886b&itag=18&source=picasa&begin=0&requiressl=yes&pl=20&mime=video/mp4&lmt=1417505760451597&ip=217.66.60.52&ipbits=8&expire=1462565063&sparams=expire,id,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,nh,pl,requiressl,source&signature=3F94A39D8D43C3E1BD715DCA8CC7E8B6E5D63F28.2708EDD5B6C5968A386DE6127AC76E63EEF63D34&key=cms1&redirect_counter=1&req_id=f167799c855a3ee&cms_redirect=yes&mm=34&mn=sn-5hnedne6&ms=ltu&mt=1462535980&mv=u&nh=IgpwcjAxLmJlcjAyKgkxMjcuMC4wLjE
	public EpisodePage(WebDriver webDriver, String url) {
		super(webDriver, url);
	}

	public String getVideoLink() {
		return getDriver().findElement(byVideoLink).getAttribute("href");
	}
}
