package music.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2018/1/16 22:14
 */
public class SingerQueueService {

    private static Queue<String> unCrawlerSingers =  new ConcurrentLinkedQueue<>();

    public static boolean addSingers(String url) {
        return unCrawlerSingers.offer(url);
    }

    public String getTopSinger() {
        return unCrawlerSingers.poll();
    }

    public boolean isSingersEmpty() {
        return unCrawlerSingers.isEmpty();
    }
}
