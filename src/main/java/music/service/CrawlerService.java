package music.service;

import music.model.Singer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 * @author lirf
 * @date 2018/1/16 15:55
 */
public class CrawlerService {

    public void parseSingers(String url) throws IOException {
        List<Singer> singers = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("#m-artist-box li");
        for (Element ele : elements) {
            Elements eleP = ele.select("p");
            Element item;
            if (eleP.isEmpty()) {
                item = ele.select("a").first();
            } else {
                item = eleP.select("a").first();
            }
            System.out.println(item.html() + ":" + item.attr("href"));
        }

    }

}
