package utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.*;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * HttpClient工具类
 *
 * @author lirf
 * @Date 2017/6/10 18:55
 */
public class HttpClientUtil {

    /**
     * connect超时时间
     */
    private static final int CONNECT_TIMEOUT = 5000;

    /**
     * socket超时时间
     */
    private static final int SOCKET_TIMEOUT = 5000;
    protected static final String GET = "GET";

    /**
     * 默认方法
     * @param url 获取的url地址
     * @return String 获取的HTML
     */
    public static String getHTML(String url) {
        String html = "";
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)   //socket超时
                .setConnectTimeout(CONNECT_TIMEOUT)   //connect超时
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            System.out.println("----------Connection timeout--------");
        }
        return html;
    }

    /**
     * cookie方法的getHTMl() 设置cookie策略,防止cookie rejected问题,拒绝写入cookie
     * @param url
     * @param hostName
     * @param port
     */
    public static String getHTML(String url, String hostName, int port) throws URISyntaxException, IOException {
        //采用用户自定义的cookie策略
        HttpHost proxy = new HttpHost(hostName, port);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CookieSpecProvider cookieSpecProvider = context -> new BrowserCompatSpec() {
            @Override
            public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
                //Oh, I am easy...
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider>create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                .register("easy", cookieSpecProvider)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy")
                .setSocketTimeout(SOCKET_TIMEOUT) //socket超时
                .setConnectTimeout(CONNECT_TIMEOUT) //connect超时
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r)
                .setRoutePlanner(routePlanner)
                .build();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        String html = "null"; //用于验证是否正常取到html
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            System.out.println("----Connection timeout----");
        }
        return html;
    }

    /**
     * proxy代理IP方法
     * @param targetUrl
     * @param hostName
     * @param port
     */
    public static String getHTMLbyProxy(String targetUrl, String hostName, int port) {
        HttpHost proxy = new HttpHost(hostName, port);
        String html = "null";
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT) //socket超时
                .setConnectTimeout(CONNECT_TIMEOUT) //connect超时
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                //.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:57.0) Gecko/20100101 Firefox/57.0")
                .setDefaultRequestConfig(requestConfig)
                .build();
        HttpGet httpGet = new HttpGet(targetUrl);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                html = EntityUtils.toString(response.getEntity(), "gb2312");
            }
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("----Connection timeout----");
        }
        return html;
    }

    public static boolean isProxyUsable(String hostName, int port) {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(hostName, port, "http");
        HttpMethod method =new GetMethod("http://music.163.com");
        int statusCode;
        try {
            statusCode = client.executeMethod(method);
            if (statusCode == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
