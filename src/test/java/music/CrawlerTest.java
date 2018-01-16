package music;

import music.service.MusicCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import utils.HttpClientUtil;

import java.io.IOException;

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

    @Test
    public void parseSingersTest() throws IOException {
        String url = "https://music.163.com/discover/artist/cat?id=1001&initial=65";
        MusicCrawler crawlerService = new MusicCrawler();
        crawlerService.parseSingers(url);
    }

    @Test
    public void parseAlbumsTest() throws IOException {
        String url = "https://music.163.com/artist/album?id=1876";
        MusicCrawler crawlerService = new MusicCrawler();
        crawlerService.parseAlbums(url);
    }

    @Test
    public void parseSongsTest() throws IOException {
        String url = "https://music.163.com/album?id=36304576";
        System.out.println(HttpClientUtil.getHTML(url));
        MusicCrawler musicCrawler = new MusicCrawler();
        musicCrawler.parseSongs(url);
    }

    // 可以得到歌曲的详细信息
    @Test
    public void apiTest() {
        String url = "http://music.163.com/api/song/detail/?id=509098886&ids=%5B509098886%5D";
        String result = HttpClientUtil.getHTML(url);
        System.out.println(result);
    }

    @Test
    public void commentTest() throws Exception {
        String songId = "509098886";
        MusicCrawler musicCrawler = new MusicCrawler();
        System.out.println(musicCrawler.parseSongComment(songId));
    }

}
