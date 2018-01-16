package music.model;

import org.bson.Document;

/**
 * 歌曲实体类
 * @author lirf
 * @date 2018/1/16 15:33
 */
public class Song implements MogoDBModel{

    private String songId;
    private String songName;
    private int commentNum;
    private String albumId;
    private String singerId;

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

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
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
        document.append("comment_num", commentNum);
        document.append("singer_id", singerId);
        document.append("album_id", albumId);

        return document;
    }
}
