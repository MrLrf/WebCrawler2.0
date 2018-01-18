package music.main;

/**
 * 主运行程序
 * @author lirf
 * @date 2018/1/17 11:30
 */
public class  RunProgram {

    public static void main(String[] args) {
     ///   ProxyCrawlerService.parseProxyIp();
        new Thread(new NetEaseCrawler(), "netEaseCrawler").start();
        //new Thread(new ProxyCrawler(), "netEaseCrawler").start();
    }

}
