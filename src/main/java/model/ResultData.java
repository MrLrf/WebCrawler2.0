package model;


/**
 * 自定义数据类型，ajax返回的json格式数据
 * @author hong
 * Created by admin on 2015/12/8.
 */
public class ResultData {
    /**
     * ctroller处理结果的状态，成功（0）或失败（ 1 或其它的代码）。
     */
    private int status;
    /**
     * 消息字符串
     */
    private String msg;
    /**
     * controller处理结果所包含的数据
     */
    private Object data;
    /**
     * 兼容光哥框架的返回结果 true表示成功，false表示处理失败
     */
    private boolean result;
    /**
     * 兼容光哥框架的返回结果 错误信息
     */
    private String errormessage;
    public ResultData() {
    }

    public ResultData(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 快速设置状态及数据
     * @param status
     * @param msg
     * @param data
     */
    public void sets(int status,String msg,Object data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 快速设置状态及数据
     * @param status
     * @param msg
     */
    public void sets(int status,String msg){
        this.status = status;
        this.msg = msg;
        this.result=status==0;
        this.errormessage=msg;
    }

    /**
     * 快速设置状态及数据
     * 设置msg为  ：操作成功    OR  操作失败
     * 对应的状态为  0  1
     * @param i  数据库操作的结果  影响了多少条数据
     */
    public void setStatusByDBResult(int i){
        if(i>0){
            this.status=0;
            this.msg="操作成功!";
        }else {
            this.status=1;
            this.msg="操作失败!";
        }
        this.result=i>0;
        this.errormessage=msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
