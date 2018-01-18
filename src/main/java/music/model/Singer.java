package music.model;

import org.bson.Document;

/**
 * 歌手实体类
 * @author lirf
 * @date 2018/1/16 15:10
 */
public class Singer implements MongoDBModel {

    private String singerId;
    private String singerName;

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

    @Override
    public Document model2Document() {
        Document document = new Document();
        document.append("singer_id", singerId);
        document.append("singer_name", singerName);

        return document;
    }
}
