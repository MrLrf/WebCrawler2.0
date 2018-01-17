package music.service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 歌曲队列
 * @author lirf
 * @date 2018/1/17 9:40
 */
public class SongQueueService {

    private static Queue<String> unCrawlerSongs =  new ConcurrentLinkedQueue<>();

    public static boolean addSongs(String str) {
        return unCrawlerSongs.offer(str);
    }

    public static String getTopSong() {
        return unCrawlerSongs.poll();
    }

    public static boolean isSongsEmpty() {
        return unCrawlerSongs.isEmpty();
    }
}
