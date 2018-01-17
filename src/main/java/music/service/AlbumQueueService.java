package music.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 专辑队列
 * @author lirf
 * @date 2018/1/17 9:23
 */
public class AlbumQueueService {

    private static Queue<String> unCrawlerAlbums =  new ConcurrentLinkedQueue<>();

    public static boolean addAlbums(String str) {
        return unCrawlerAlbums.offer(str);
    }

    public static String getTopAlbum() {
        return unCrawlerAlbums.poll();
    }

    public static boolean isAlbumsEmpty() {
        return unCrawlerAlbums.isEmpty();
    }
}
