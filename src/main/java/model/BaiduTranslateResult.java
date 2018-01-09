package model;

import java.util.ArrayList;
import java.util.Map;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 17-6-20 下午9:35
 */
public class BaiduTranslateResult {
    private String from;
    private String to;
    private ArrayList<Map<String, String>> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<Map<String, String>> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(ArrayList<Map<String, String>> trans_result) {
        this.trans_result = trans_result;
    }
}
