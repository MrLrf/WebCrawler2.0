package crawlercore;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2017/6/9 21:46
 */
public class Weibo {
    public static String getHTML(String url) throws URISyntaxException, ClientProtocolException, IOException {
        //采用用户自定义cookie策略,不显示cookie rejected的报错
        CookieSpecProvider cookieSpecProvider = new CookieSpecProvider(){
            public CookieSpec create(HttpContext context){
                return new BrowserCompatSpec(){
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {

                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider> create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                .register("cookie", cookieSpecProvider)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("cookie")
                .setSocketTimeout(5000) //设置socket超时时间
                .setConnectTimeout(5000)  //设置connect超时时间
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        String html = "html获取失败"; //用于验证是否取到正常的html
        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity(),"utf-8");
            //System.out.println(html); //打印返回的html
        } catch (IOException e) {
            e.printStackTrace();
        }
        return html;
    }

    private void writeWeibo2txt(String html, String savePath) throws IOException {
        File htmltxt = new File(savePath); //新建一个txt文件用于存放爬取的结果信息
        FileWriter fw = new FileWriter(htmltxt);
        BufferedWriter bw = new BufferedWriter(fw);
        //regex-----"id":\s"\d{19}",(\n*?)|(\s*?)"content":\s".*?",(\n*?)|(\s*?)"prettyTime":\s".*?"
        //Pattern p = Pattern.compile("\"id\":\\s\"\\d{19}\",(\\n*?)|(\\s*?)\"content\":\\s\".*?\",(\\n*?)|(\\s*?)\"prettyTime\":\\s\".*?\"");
        Pattern p = Pattern.compile("\"html\":\"(.*?)\"}\\)");
        Matcher m = p.matcher(html);
        while(m.find()) {
            System.out.println(m.group(1));
            html = html + "\n";
            bw.write(m.group());
        }
        bw.close();
    }

    public static String unicodeToCh(String s) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(s);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            s = s.replace(matcher.group(1), ch + "");
        }
        return s;
    }

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

        String s = Spider.sendGet("http://s.weibo.com/weibo/%25E8%258B%25B9%25E6%259E%259C%25E6%2589%258B%25E6%259C%25BA?topnav=1&wvr=6&b=1");
        s = unicodeToCh(s);

        Document doc = Jsoup.parse(s);
        Elements searchKey = doc.select("em");
        System.out.println(searchKey);

        //System.out.println(s);
        //String savePath = "d:/weibo/html.txt"; //输出到txt文件的存放路径
        //Weibo crawler = new Weibo();
        //crawler.writeWeibo2txt(s, savePath);
        //String html = crawler.getHTML("http://s.weibo.com/weibo/苹果手机&nodup=1&page=50");
        //String searchword = "iPad"; //搜索关键字为"iPad"的微博html页面
        //if(html != "html获取失败") {
        //    if(crawler.isExistHTML(html)) {
        //        System.out.println(html);
        //        crawler.writeWeibo2txt(html, savePath);
        //    }
        //}
        //Pattern p = Pattern.compile("<script id=\"data_searchTweet\" type=\"application/json\">.+?<\script>"); //<script id="data_searchTweet" type="application/json">.*?<\script>
        //Matcher m = p.matcher(html);
        //html = crawler.getHTML("http://s.weibo.com/weibo/"+searchword+"&nodup=1&page="+1);

        //System.out.println(html);
    }
}
