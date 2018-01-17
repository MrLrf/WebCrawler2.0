package music.main;

import mongodb.MongodbUtil;
import music.model.ProxyIP;
import music.model.Song;
import music.service.*;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 主逻辑
 * @author lirf
 * 2016-10-26
 * */
public class NetEaseCrawler implements Runnable {

	private String[] initUrls = {"https://music.163.com/discover/artist/cat?id=1001",
			"https://music.163.com/discover/artist/cat?id=1002",
			"https://music.163.com/discover/artist/cat?id=1003"};
	private List<Document> songs = new ArrayList<>();
	private List<Song> songList = new LinkedList<>();
	private static ProxyIP proxyIP = ProxyIPQueueService.getTopUsableIp();
	
	@Override
	public void run() {
		try {
			//初始化待爬取的歌手URL队列
			initUnCrawledSingerQueue();
			
			//记录所有爬取出来的歌曲数，包含重复歌曲
			int count = 0;

			//开始根据歌单爬取
			while (!SingerQueueService.isSingersEmpty()) {
				
				//填充待爬取专辑队列
				fillUnCrawledAlbumQueue(SingerQueueService.getTopSinger());

				//专辑队列为空就返回上层循环填充专辑队列
				while (!AlbumQueueService.isAlbumsEmpty()) {

					//填充待爬取歌曲队列
					fillUnCrawledSongQueue(AlbumQueueService.getTopAlbum());

					//歌曲队列为空就返回上层循环填充歌曲队列
					while (!SongQueueService.isSongsEmpty()) {

						//取出待爬取歌曲ID
						String songId = SongQueueService.getTopSong();

						//获取到爬取结果，歌曲信息
						Song song = getSongMessage(songId);
						songs.add(song.model2Document());
						if (songList.size() < 100) {
							songList.add(song);
						} else {
							Collections.sort(songList);
							if (song.getCommentCount() > songList.get(songList.size() - 1).getCommentCount()) {
								songList.remove(songList.get(songList.size() -1 ));
								songList.add(song);
							}
						}
						count++;
					}
					MongodbUtil.insertIntoCollection(songs, "songs");
					songs.clear();
				}
			}

			System.out.println(songList);
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 循环请求获取所有歌手
	 * */
	public void initUnCrawledSingerQueue() throws IOException {
		for (String url : initUrls) {
			for (int i = 65; i <= 90; i++) {
				String newUrl = url + "&initial=" + i;
				MusicCrawlerService.parseSingers(newUrl);
			}
		}
	}

	/**
	 * 填充要爬取的专辑队列
	 */
	public void fillUnCrawledAlbumQueue(String singerId) throws IOException {
		MusicCrawlerService.parseAlbums("https://music.163.com/artist/album?id=" + singerId, proxyIP);
	}

	/**
	 * 填充要爬取的专辑队列
	 */
	public void fillUnCrawledSongQueue(String albumId) throws IOException {
		MusicCrawlerService.parseSongs(albumId, proxyIP);
	}

	/**
	 * 由于反爬的存在， 一旦被禁止爬取， 休眠几秒后再进行爬取
	 * @param songId
	 * @return
	 */
	public Song getSongMessage(String songId) {
		try {
			//Song song = MusicCrawlerService.parseSongWithComment(songId, new ProxyIP("", 1));
			Song song = MusicCrawlerService.parseSongWithComment(songId, proxyIP);

			if (song == null) {
				System.out.println("warining: be interceptted by net ease music server..");
				Thread.sleep((long) (Math.random() * 30000));
				
				//递归
				return getSongMessage(songId);
			} else {
				return song;
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("改变代理IP重新爬取");
			proxyIP = ProxyIPQueueService.getTopUsableIp();
			return getSongMessage(songId);
		}
	}
}
