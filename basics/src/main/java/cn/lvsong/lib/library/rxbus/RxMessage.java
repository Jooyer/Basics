package cn.lvsong.lib.library.rxbus;

import androidx.annotation.Keep;

/** 传递消息的类
 * Created by Jooyer on 2017/6/3
 */
@Keep
public class RxMessage {

    private int code;
    private Object message ;
    private Object[] messages;

    public RxMessage(int code) {
        this.code = code;
    }


    public RxMessage(int code, Object message) {
        this.code = code;
        this.message = message;
    }

    public RxMessage(int code,Object... message){
        this.code = code;
        this.messages = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object[] getMessages() {
        return messages;
    }
}
