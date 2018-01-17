package music.model;

import org.bson.Document;

import java.util.List;

/**
 * 专辑实体
 * @author lirf
 * @date 2018/1/16 15:19
 */
public class Album implements MogoDBModel{

    private String albumId;
    private String albumName;
    private List<Song> songs;
    private String singerId;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
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
        document.append("album_id", albumId);
        document.append("album_name", albumName);
        document.append("singer_id", singerId);

        return document;
    }
}
