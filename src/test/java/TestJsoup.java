import model.Paper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import utils.HttpClientUtil;
import utils.TranslateUtilByGoogle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2017/6/10 15:33
 */
public class TestJsoup {

    /*
     * 爬取会议
     */
    @Test
    public void testJsoupConference() throws Exception {
        String url = "http://dblp.uni-trier.de/db/conf/recsys/";
        String result = HttpClientUtil.getHTML(url);

        Document document = Jsoup.parse(result);

        Elements datas = document.select("div.data");

        for (Element data : datas) {
            Element title = data.select("span.title").first();
            Element href = data.select("a:containsOwn([contents])").first();

            System.out.println(TranslateUtilByGoogle.en2chs(title.html()));
            System.out.println(href.attr("href"));
            System.out.println();
        }

    }

    /*
     * 爬取文章
     */
    @Test
    public void testJsoupPaper() throws Exception {
        String url = "http://dblp.uni-trier.de/db/conf/recsys/recsys2016.html.";
        String result = HttpClientUtil.getHTML(url);
        List<Paper> paperList = new ArrayList<>();

        Document document = Jsoup.parse(result);

        Elements papers = document.select("li.entry.inproceedings");
        for (Element paper : papers) {
            String paper_url = paper.select("li.drop-down").first()
                    .select("div.head").first()
                    .select("a").first().attr("href");
            Element paperData = paper.select("div.data").first();
            String paper_name = paperData.select("span.title").first().html();
            String paper_namech = TranslateUtilByGoogle.en2chs(paper_name);
            String paper_page = paperData.select("span[itemprop=pagination]").first().html();

            Paper paper1 = new Paper("",paper_name, paper_namech, paper_page, paper_url);
            paperList.add(paper1);
        }

        Iterator<Paper> iterator = paperList.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
