package music.service;

import com.alibaba.fastjson.*;
import music.model.*;
import music.netease.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.EncryptUtils;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2018/1/16 19:56
 */
public class MusicCrawlerService {

    /**
     * 网易云音乐主域名
     */
    public static final String DOMAIN = "http://music.163.com";

    /**
     * 获取评论的API路径(被加密)
     */
    public static final String NET_EASE_COMMENT_API_URL = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_";

    /**
     * 解密用的文本
     */
    public static final String TEXT = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";

    /**
     * 爬取的歌曲的索引
     */
    private static long INDEX = 1;

    /**
     * 爬取url对应页面的所有歌手以及其url并将url放入爬去队列中
     * @param url
     * @throws IOException
     */
    public static void parseSingers(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select("#m-artist-box li");
        int index = 0;
        for (Element ele : elements) {
            index++;
            Elements eleP = ele.select("p");
            Element item;
            if (eleP.isEmpty()) {
                item = ele.select("a").first();
            } else {
                item = eleP.select("a").first();
            }
            System.out.println(index + "." + item.html() + ":" + item.attr("href"));
            //SingerQueueService.addSingers(MusicCrawler.DOMAIN + "/artist/album?id=" + item.attr("href").split("=")[1]);
            SingerQueueService.addSingers(item.attr("href").split("=")[1]);
        }
    }

    /**
     * 爬取id对应的页面所有的专辑信息
     * @param url
     * @throws IOException
     */
    public static void parseAlbums(String url, ProxyIP ip) throws IOException {
        //String url = MusicCrawler.DOMAIN + "/artist/album?id=" + id;
        String result = HttpClientUtil.getHTMLbyProxy(url, ip.getHostName(), ip.getPort());
        //Document doc = Jsoup.connect(url).get();
        Document doc = Jsoup.parse(result);

        Elements elements = doc.select("#m-song-module li");
        int index = 0;
        for (Element ele : elements) {
            index++;
            Element item = ele.select("p a").first();
            AlbumQueueService.addAlbums(item.attr("href").split("=")[1]);
            System.out.println(index + "." + item.html() + ":" + item.attr("href"));
        }
        // 爬取下一页数据
        Elements eles = doc.select("a.zbtn.znxt");
        if (eles.isEmpty()) {
            return;
        }
        String nextUrl = eles.first().attr("href");
        if (!"javascript:void(0)".equals(nextUrl)) {
            parseAlbums("https://music.163.com" + nextUrl, ip);
        }
    }

    /**
     * 根据专辑id爬取歌曲列表
     * @param albumId
     * @return
     * @throws IOException
     */
    public static Album parseSongs(String albumId, ProxyIP ip) throws IOException {
        String url = MusicCrawlerService.DOMAIN + "/album?id=" + albumId;
        String result = HttpClientUtil.getHTMLbyProxy(url, ip.getHostName(), ip.getPort());
        //Document doc = Jsoup.connect(url).get();
        Document doc = Jsoup.parse(result);

        Album album = new Album();
        String singerId = doc.select("a.s-fc7").first().attr("href").split("=")[1];
        album.setAlbumId(albumId);
        album.setAlbumName(doc.select("h2.f-ff2").first().html());
        album.setSingerId(singerId);

        Elements elements = doc.select("#song-list-pre-cache ul li");
        int index = 0;
        for (Element ele : elements) {
            index++;
            Element item = ele.select("a").first();
            SongQueueService.addSongs(item.attr("href").split("=")[1]);
            System.out.println(index + "." + item.html() + ":" + item.attr("href"));
        }
        return album;
    }

    public static void parseSong(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .header("Host", "http://music.163.com")
                .header("User-Agent", "  Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0")
                .header("Accept", "  text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "zh-cn,zh;q=0.5")
                .header("Accept-Charset", "  GB2312,utf-8;q=0.7,*;q=0.7")
                .header("Connection", "keep-alive")
                .post();
        System.out.println(doc.html());

        Song song = new Song();
        song.setSongId(url.split("=")[1]);
        song.setSongName(doc.select("div.tit em.f-ff2").first().html());
        //song.setCommentNum();
    }

    /**
     * 根据歌曲id爬取歌曲信息
     */
    public static Song parseSongInfo(String songId, ProxyIP ip) throws IOException {
        String url = "http://music.163.com/api/song/detail/?id=" + songId+ "&ids=%5B" + songId + "%5D";

        JSONObject json = JSON.parseObject(HttpClientUtil.getHTMLbyProxy(url, ip.getHostName(), ip.getPort()));
        //JSONObject json = JSON.parseObject(HttpClientUtil.getHTML(url));

        String songName = JSONPath.eval(json, "$.songs[0].name").toString();
        String albumId = JSONPath.eval(json, "$.songs[0].album.id").toString();
        String albumName = JSONPath.eval(json, "$.songs[0].album.name").toString();
        String singerId = JSONPath.eval(json, "$.songs[0].artists[0].id").toString();
        String singerName = JSONPath.eval(json, "$.songs[0].artists[0].name").toString();

        Song song = new Song(songId, songName, albumId, albumName, singerId, singerName);
        return song;
    }

    /**
     * 根据歌曲id爬取歌曲信息（包含评论信息）
     * @param songId
     * @return
     * @throws Exception
     */
    public static Song parseSongWithComment(String songId, ProxyIP ip) throws Exception {
        //String secKey = new BigInteger(100, new SecureRandom()).toString(32).substring(0, 16);
        //String encText = EncryptUtils.aesEncrypt(EncryptUtils.aesEncrypt(MusicCrawlerService.TEXT, "0CoJUm6Qyw8W8jud"), secKey);
        //String encSecKey = EncryptUtils.rsaEncrypt(secKey);
        //Connection.Response response = Jsoup
        //        .connect(MusicCrawlerService.NET_EASE_COMMENT_API_URL + songId + "/?csrf_token=")
        //        .method(Connection.Method.POST).header("Referer", MusicCrawlerService.DOMAIN)
        //        .cookie("appver", "1.5.0.75771")
        //        .data(ImmutableMap.of("params", encText, "encSecKey", encSecKey)).execute();

        UrlParamPair upp = Api.getPlaylistOfUser(songId);
        String reqStr = upp.getParas().toJSONString();
        Connection.Response response = Jsoup.connect(NET_EASE_COMMENT_API_URL + songId + "?csrf_token=")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0")
                .header("Accept", "*/*").header("Cache-Control", "no-cache").header("Connection", "keep-alive")
                .header("Host", "music.163.com").header("Accept-Language", "zh-CN,en-US;q=0.7,en;q=0.3").header("DNT", "1")
                .header("Pragma", "no-cache").header("Content-Type", "application/x-www-form-urlencoded")
                .data(JSSecret.getDatas(reqStr)).method(Connection.Method.POST).ignoreContentType(true).timeout(50000).execute();

        Object res = JSON.parse(response.body());

        if (res == null) {
            return null;
        }

        //Song song = parseSongInfo(songId, ip);
        Song song = new Song();
        int commentCount = (int)JSONPath.eval(res, "$.total");
        int hotCommentCount = (int)JSONPath.eval(res, "$.hotComments.size()");
        int latestCommentCount = (int)JSONPath.eval(res, "$.comments.size()");
        song.setCommentCount(commentCount);

        List<MusicComment> ls = new ArrayList<>();

        if (commentCount != 0 && hotCommentCount != 0) {

            for (int i = 0; i < hotCommentCount; i++) {
                String nickname = JSONPath.eval(res, "$.hotComments[" + i + "].user.nickname").toString();
                String time = EncryptUtils.stampToDate((long)JSONPath.eval(res, "$.hotComments[" + i + "].time"));
                String content = JSONPath.eval(res, "$.hotComments[" + i + "].content").toString();
                String appreciation = JSONPath.eval(res, "$.hotComments[" + i + "].likedCount").toString();
                ls.add(new MusicComment("hotComment", nickname, time, content, appreciation));
            }
        } else if (commentCount != 0) {

            for (int i = 0; i < latestCommentCount; i++) {
                String nickname = JSONPath.eval(res, "$.comments[" + i + "].user.nickname").toString();
                String time = EncryptUtils.stampToDate((long)JSONPath.eval(res, "$.comments[" + i + "].time"));
                String content = JSONPath.eval(res, "$.comments[" + i + "].content").toString();
                String appreciation = JSONPath.eval(res, "$.comments[" + i + "].likedCount").toString();
                ls.add(new MusicComment("latestCommentCount", nickname, time, content, appreciation));
            }
        }

        song.setComments(ls);
        System.out.println(INDEX++);
        return song;
    }

    public static void main(String[] args) throws IOException {
        parseSong("https://music.163.com/song?id=509098886");
    }

}
