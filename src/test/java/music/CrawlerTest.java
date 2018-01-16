package music;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import utils.HttpClientUtil;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/16 16:18
 */
public class CrawlerTest {

    @Test
    public void parseSingerList() {
        String url = "https://music.163.com/discover/artist/cat?id=1001&initial=65";
        //https://music.163.com/discover/artist/cat?id=1001&initial=65
        String result = HttpClientUtil.getHTML(url);
        System.out.println(result);

        Document document = Jsoup.parse(result);
        Element element = document.getElementById("m-artist-box");
        Elements elements = element.select("li");
        for (Element ele : elements) {
            System.out.println(ele.getElementsByTag("p").first().getElementsByTag("a").first().html());
            System.out.println(ele.getElementsByTag("p").first().getElementsByTag("a").first().attr("href"));
        }
    }

}
