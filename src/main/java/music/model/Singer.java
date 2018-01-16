package music.model;

import org.bson.Document;

import java.util.List;

/**
 * 歌手实体类
 * @author lirf
 * @date 2018/1/16 15:10
 */
public class Singer implements MogoDBModel{

    private String singerId;
    private String singerName;
    private List<Album> albums;

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    @Override
    public Document model2Document() {
        Document document = new Document();
        document.append("singer_id", singerId);
        document.append("singer_name", singerName);

        return document;
    }
}
