package model;

import crawlercore.Spider;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JavaBean
 * 用来封装需要得到的内容
 *
 * @Author lirf
 * @Date 2017/5/26 19:54
 */
public class Zhihu {
    private String title;
    private String titledescription;
    private String zhihuUrl;
    private List<String> answers;

    public Zhihu(String url) {
        title = "";
        titledescription = "";
        zhihuUrl = "";
        answers = new ArrayList<String>();

        if (getRealUrl(url)) {
            System.out.println("正在抓取链接" + zhihuUrl);

            String content = Spider.sendGet(zhihuUrl);

            Pattern pattern;
            Matcher matcher;

            pattern = Pattern.compile("<head.+?<title.+?>(.+?)</title>");
            matcher = pattern.matcher(content);
            if (matcher.find()) {
                title = matcher.group(1);
            }

            pattern = Pattern.compile("<div.+?><span class=\"RichText\">(.*?)</span>");
            matcher = pattern.matcher(content);
            if (matcher.find()) {
                titledescription = matcher.group(1);
            }

            pattern = Pattern.compile("AnswerItem-meta.+?<span class=\"RichText CopyrightRichText-richText\">(.*?)</span>");
            matcher = pattern.matcher(content);
            while (matcher.find()) {
                answers.add(matcher.group(1));
            }

        }
    }

    @Override
    public String toString() {
        return "问题为:" + title + "\n问题描述为:" + titledescription +
                "\n地址为:" + zhihuUrl + "\n回答的内容为:" + answers + "\n";
    }

    boolean getRealUrl(String url)
    {
        String regex="question/(.+?)/";

        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(url);

        while (matcher.find())
        {
            zhihuUrl="https://www.zhihu.com/question/"+matcher.group(1);

            return true;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitledescription() {
        return titledescription;
    }

    public void setTitledescription(String titledescription) {
        this.titledescription = titledescription;
    }

    public String getZhihuUrl() {
        return zhihuUrl;
    }

    public void setZhihuUrl(String zhihuUrl) {
        this.zhihuUrl = zhihuUrl;
    }

    public List<String> getAnwsers() {
        return answers;
    }

    public void setAnwsers(List<String> anwsers) {
        this.answers = anwsers;
    }
}
