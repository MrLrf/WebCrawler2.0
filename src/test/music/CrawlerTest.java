package music;

import music.model.ProxyIP;
import music.model.Song;
import music.netease.*;
import music.service.MusicCrawlerService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Connection;
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
        MusicCrawlerService crawlerService = new MusicCrawlerService();
        crawlerService.parseSingers(url);
    }

    @Test
    public void parseAlbumsTest() throws IOException {
        String url = "https://music.163.com/artist/album?id=1876";
        MusicCrawlerService crawlerService = new MusicCrawlerService();
        //crawlerService.parseAlbums("1876");
    }

    @Test
    public void parseSongsTest() throws IOException {
        String url = "https://music.163.com/album?id=36304576";
        //String url = "https://www.baidu.com";
        ProxyIP ip = new ProxyIP("93.167.224.220", 80, "未用");
        //System.out.println(HttpClientUtil.getHTMLbyProxy(url, ip.getHostName(), ip.getPort()));
        //System.out.println(HttpClientUtil.getHTML(url));
        MusicCrawlerService.parseSongs(url, ip);
    }

    // 可以得到歌曲的详细信息
    @Test
    public void apiTest() {
        String url = "http://music.163.com/api/song/detail?id=509098886&ids=%5B509098886%5D";
        String result = HttpClientUtil.getHTML(url);
        System.out.println(result);
    }

    @Test
    public void commentTest() throws Exception {
        String songId = "509098886";
        ProxyIP ip = new ProxyIP("115.29.236.46", 3128, "未用");
        Song song = MusicCrawlerService.parseSongWithComment(songId, ip);
        System.out.println(song);
    }

    @Test
    public void parseSongInfoTest() throws IOException {
        //Song song = MusicCrawlerService.parseSongInfo("509098886");
        //System.out.println(song);

        //System.out.println(HttpClientUtil.getHTMLbyProxy("", "58.87.87.142", 80));
    }

    @Test
    public void testProxyIp() throws IOException {
        String ip = "117.90.252.18";
        int port = 9000;
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(ip, port, "http");
        HttpMethod method =new GetMethod("http://www.baidu.com");
        int statusCode = client.executeMethod(method);
        System.out.println(statusCode);
    }

    @Test
    public void neteaseApiTest() {
        try {
            String uid = "98706997";
            UrlParamPair upp = Api.getPlaylistOfUser(uid);
            String req_str = upp.getParas().toJSONString();
            Connection.Response
                    response = Jsoup.connect("http://music.163.com/weapi/user/playlist?csrf_token=")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0")
                    .header("Accept", "*/*")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .header("Host", "music.163.com")
                    .header("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3")
                    .header("DNT", "1")
                    .header("Pragma", "no-cache")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(JSSecret.getDatas(req_str))
                    .method(Connection.Method.POST)
                    .ignoreContentType(true)
                    .proxy("121.232.144.222", 9000)
                    .timeout(10000)
                    .execute();
            String list = response.body();
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
