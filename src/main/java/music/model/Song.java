package music.model;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲实体类
 * @author lirf
 * @date 2018/1/16 15:33
 */
public class Song implements MongoDBModel, Comparable{

    private String songId;
    private String songName;
    private int commentCount;
    private String albumId;
    private String albumName;
    private String singerId;
    private String singerName;
    private List<MusicComment> comments = new ArrayList<MusicComment>();

    public Song() {

    }

    public Song(String songId, String songName, String albumId, String albumName, String singerId, String singerName) {
        this.songId = songId;
        this.songName = songName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.singerId = singerId;
        this.singerName = singerName;
    }

    public Song(String songId, String songName, int commentCount, String albumId, String albumName, String singerId, String singerName, List<MusicComment> comments) {
        this.songId = songId;
        this.songName = songName;
        this.commentCount = commentCount;
        this.albumId = albumId;
        this.albumName = albumName;
        this.singerId = singerId;
        this.singerName = singerName;
        this.comments = comments;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<MusicComment> getComments() {
        return comments;
    }

    public void setComments(List<MusicComment> comments) {
        this.comments = comments;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    @Override
    public Document model2Document() {
        Document document = new Document();
        document.append("song_id", songId);
        document.append("song_name", songName);
        document.append("comment_count", commentCount);
        document.append("singer_id", singerId);
        document.append("singer_name", singerName);
        document.append("album_id", albumId);
        document.append("album_name", albumName);

        return document;
    }

    @Override
    public String toString() {
        return "Song{" +
                "songId='" + songId + '\'' +
                ", songName='" + songName + '\'' +
                ", commentCount=" + commentCount +
                ", albumId='" + albumId + '\'' +
                ", albumName='" + albumName + '\'' +
                ", singerId='" + singerId + '\'' +
                ", singerName='" + singerName + '\'' +
                ", comments=" + comments +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Song song = (Song) o;
        if (commentCount < song.commentCount) {
            return -1;
        } else if (commentCount > song.commentCount) {
            return 1;
        } else {
            return 0;
        }
    }
}
