package music.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 歌手队列
 *
 * @Author lirf
 * @Date 2018/1/16 22:14
 */
public class SingerQueueService {

    private static Queue<String> unCrawlerSingers =  new ConcurrentLinkedQueue<>();

    public static boolean addSingers(String str) {
        return unCrawlerSingers.offer(str);
    }

    public static String getTopSinger() {
        return unCrawlerSingers.poll();
    }

    public static boolean isSingersEmpty() {
        return unCrawlerSingers.isEmpty();
    }
}
