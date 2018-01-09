package crawlercore;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import utils.HttpClientUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2017/6/5 17:37
 */
public class HTML {

    private boolean isExistHTML(String html) throws InterruptedException {
        boolean isExist = false;
        Pattern pNoResult = Pattern.compile("\\\\u6ca1\\\\u6709\\\\u627e\\\\u5230\\\\u76f8"
                + "\\\\u5173\\\\u7684\\\\u5fae\\\\u535a\\\\u5462\\\\uff0c\\\\u6362\\\\u4e2a"
                + "\\\\u5173\\\\u952e\\\\u8bcd\\\\u8bd5\\\\u5427\\\\uff01"); //没有找到相关的微博呢，换个关键词试试吧！（html页面上的信息）
        Matcher mNoResult = pNoResult.matcher(html);
        if(!mNoResult.find()) {
            isExist = true;
        }
        return isExist;
    }

    /**把所有html写到本地txt文件存储 */
    public static void writeHTML2txt(String html, int num) throws IOException {
        String savePath = "d:/weibo/weibohtml/" + num + ".txt";
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(html);
        bw.close();
    }


    @Test
    public void header() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet("http://www.baidu.com");
            httpget.setHeader("Accept", "text/html, */*; q=0.01");
            httpget.setHeader("Accept-Encoding", "gzip, deflate,sdch");
            httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httpget.setHeader("Connection", "keep-alive");
            httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");

            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine()); //状态码
            if(entity != null) {
                System.out.println(entity.getContentLength());
                System.out.println(entity.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String parse(String html) {
        String s = "";
        Document doc = Jsoup.parse(html);
        Elements userNames = doc.select("dt[class].face > a");
        Elements userids = doc.select("span > a[action-data]");
        Elements dates = doc.select("a[date]");
        Elements tweetids = doc.select("dl[mid]");
        Elements tweets = doc.select("p > em");
        Elements forwardNums = doc.select("a:contains(转发)");
        Elements commentNums = doc.select("a:contains(评论)");
        for(Element userName : userNames) {
            String attr = userName.attr("title");
            s += "<userName> " + attr + " </userName>";
        }
        for(Element userid : userids) {
            String attr = userid.attr("action-data");
            attr = attr.substring(attr.indexOf("uid="));
            Pattern p = Pattern.compile("[0-9]+");
            Matcher m = p.matcher(attr);
            if(m.find()) {
                attr = m.group();
            }
            s += "<userid> " + attr + " </userid>";
        }
        for(Element date : dates) {
            String attr = date.text();
            s += "<date> " + attr + " </date>";
        }
        for(Element tweetid : tweetids) {
            String attr = tweetid.attr("mid");
            s += "<tweetid> " + attr + " </tweetid>";
        }
        for(Element tweet : tweets) {
            String attr = tweet.text();
            s += "<tweetSentence> " + attr + " </tweetSentence>";
        }
        for(Element forwardNum : forwardNums) {
            String attr = forwardNum.text();
            if(attr.equals("转发")) {
                attr = "0";
            }
            else {
                if(!attr.contains("转发(")) {
                    attr = "0";
                }
                else {
                    attr = attr.substring(attr.indexOf("转发(")+3, attr.indexOf(")"));
                }
            }
            System.out.println(attr);
            s += "<forwardNum> " + attr + " </forwardNum>";
        }
        for(Element commentNum : commentNums) {
            String attr = commentNum.text();
            if(attr.equals("评论")) {
                attr = "0";
            }
            else {
                if(!attr.contains("评论(")) {
                    attr = "0";
                }
                else {
                    attr = attr.substring(attr.indexOf("评论(")+3, attr.indexOf(""));
                }
            }
            System.out.println(attr);
            s += "<commentNum> " + attr + " </commentNum>";
        }
        //System.out.println(s);
        return s;
    }


    @Test
    public void sendGet() throws IOException {
        String url = "http://s.weibo.com/weibo/苹果手机&nodup=1&page=50";
        //String url = "http://t.163.com/tag/苹果手机";
        String html = HttpClientUtil.getHTML(url);
        writeHTML2txt(html, 1);

        System.out.println(parse(html));

        //System.out.println(htmls[0]);
        //System.out.println(htmls[1]);
    }
}
