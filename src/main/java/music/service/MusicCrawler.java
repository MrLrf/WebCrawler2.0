package music.service;

import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.ImmutableMap;
import com.mongodb.util.JSON;
import music.model.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.EncryptUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2018/1/16 19:56
 */
public class MusicCrawler {

    //163主域名
    public static final String DOMAIN = "http://music.163.com";

    //获取评论的API路径(被加密)
    public static final String NET_EASE_COMMENT_API_URL = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_";

    //解密用的文本
    public static final String TEXT = "{\"username\": \"\", \"rememberLogin\": \"true\", \"password\": \"\"}";

    /**
     * 爬去url对应页面的所有歌手以及其url并将url放入爬去队列中
     * @param url
     * @throws IOException
     */
    public void parseSingers(String url) throws IOException {
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
            SingerQueueService.addSingers(this.DOMAIN + "/artist/album?id=" + item.attr("href").split("=")[1]);
        }
    }

    //TODO: 修改参数 加注释
    public void parseAlbums(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Elements elements = doc.select("#m-song-module li");
        int index = 0;
        for (Element ele : elements) {
            index++;
            Element item = ele.select("p a").first();
            System.out.println(index + "." + item.html() + ":" + item.attr("href"));
        }
        // 爬取下一页数据
        String nextUrl = doc.select("a.zbtn.znxt").first().attr("href");
        if (!"javascript:void(0)".equals(nextUrl)) {
            parseAlbums("https://music.163.com" + nextUrl);
        }
    }

    public void parseSongs(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Album album = new Album();
        String albumId = url.split("=")[1];
        String singerId = doc.select("a.s-fc7").first().attr("href").split("=")[1];
        album.setAlbumId(albumId);
        album.setAlbumName(doc.select("h2.f-ff2").first().html());
        album.setSingerId(singerId);

        Elements elements = doc.select("#song-list-pre-cache ul li");
        int index = 0;
        for (Element ele : elements) {
            index++;
            Element item = ele.select("a").first();
            System.out.println(index + "." + item.html() + ":" + item.attr("href"));
        }
    }

    public void parseSong(String url) throws IOException {
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

    public MusicCommentMessage parseSongComment(String songId) throws Exception {
        String songUrl = this.DOMAIN + "/song?id=" + songId;
        URL uri = new URL(songUrl);
        Document msdoc = Jsoup.parse(uri, 3000);

        String secKey = new BigInteger(100, new SecureRandom()).toString(32).substring(0, 16);
        String encText = EncryptUtils.aesEncrypt(EncryptUtils.aesEncrypt(this.TEXT, "0CoJUm6Qyw8W8jud"), secKey);
        String encSecKey = EncryptUtils.rsaEncrypt(secKey);
        Connection.Response response = Jsoup
                .connect(this.NET_EASE_COMMENT_API_URL + songId + "/?csrf_token=")
                .method(Connection.Method.POST).header("Referer", this.DOMAIN)
                .data(ImmutableMap.of("params", encText, "encSecKey", encSecKey)).execute();


        Object res = JSON.parse(response.body());

        if (res == null) {
            return null;
        }

        MusicCommentMessage musicCommentMessage = new MusicCommentMessage();

        int commentCount = (int)JSONPath.eval(res, "$.total");
        int hotCommentCount = (int)JSONPath.eval(res, "$.hotComments.size()");
        int latestCommentCount = (int)JSONPath.eval(res, "$.comments.size()");

        musicCommentMessage.setSongTitle(msdoc.title());
        musicCommentMessage.setSongUrl(songUrl);
        musicCommentMessage.setCommentCount(commentCount);

        List<MusicComment> ls = new ArrayList<MusicComment>();

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

        musicCommentMessage.setComments(ls);
        return musicCommentMessage;
    }

    public static void main(String[] args) throws IOException {
        MusicCrawler musicCrawler = new MusicCrawler();
        musicCrawler.parseSong("https://music.163.com/#/song?id=509098886");
    }

}
