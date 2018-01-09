package model;

import java.util.UUID;

/**
 * 类的描述
 *
 * @Author lirf
 * @Date 2017/5/27 13:40
 */
public class ZhihuAnswer {
    private String answer_id;
    private String question_id;
    private String answer_content;

    public ZhihuAnswer(String question_id, String answer_content) {
        this.answer_id = UUID.randomUUID().toString();
        this.question_id = question_id;
        this.answer_content = answer_content;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }
}
