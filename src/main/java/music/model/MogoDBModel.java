package music.model;

import org.bson.Document;

/**
 * 实体转为MongoDB Document接口
 * @author lirf
 * @date 2018/1/16 15:40
 */
public interface MogoDBModel {

    Document model2Document();

}
