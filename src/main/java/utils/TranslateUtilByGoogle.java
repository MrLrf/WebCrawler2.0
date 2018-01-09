package utils;

import org.apache.commons.httpclient.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.net.URLEncoder;
import java.text.MessageFormat;

/**
 * Google翻译工具
 *
 * @Author lirf
 * @Date 2017/6/10 18:29
 */
public class TranslateUtilByGoogle {
    protected static final String URL_TEMPLATE = "http://translate.google.com/?langpair={0}&text={1}";
    protected static final String ID_RESULTBOX = "result_box";

    protected static final String ENCODING = "UTF-8";
    protected static final String TAIWAN = "zh-TW";
    protected static final String CHINA = "zh-CN";
    protected static final String ENGLISH = "en";
    protected static final String JAPAN = "ja";

    protected static HttpClient httpclient;

    /**
     * Google翻译
     *
     * @param text        翻译文本
     * @param src_lang    翻译语系
     * @param target_lang 目标语系
     * @return
     * @throws Exception
     */
    public static String translate(final String text, final String src_lang, final String target_lang) throws Exception {
        Document doc = null;
        Element ele = null;
        try {
            // create URL string
            String url = MessageFormat.format(URL_TEMPLATE,
                    URLEncoder.encode(src_lang + "|" + target_lang, ENCODING),
                    URLEncoder.encode(text, ENCODING));

            // connect & download html
            String is = HttpClientUtil.getHTML(url);

            // parse html by Jsoup
            doc = Jsoup.parse(is);
            //doc = Jsoup.parse(is, ENCODING,"");
            ele = doc.getElementById(ID_RESULTBOX);
            String result = ele.text();
            return result;

        } finally {
            //IOUtil.closeQuietly(is);
            //is =null;
            doc = null;
            ele = null;
        }
    }

    /**
     * Google翻译: 英文-->简中
     *
     * @param text   翻译文本
     * @return
     * @throws Exception
     */
    public static String en2chs(final String text) throws Exception {
        return translate(text, ENGLISH, CHINA);
    }

    @Test
    public void mainTest() throws Exception {
        String result = translate("This is a question", ENGLISH, CHINA);
        System.out.println(result);
    }
}
