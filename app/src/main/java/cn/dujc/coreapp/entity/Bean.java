package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;

public class Bean<T> implements Serializable {
    private static final long serialVersionUID = 1154192701543147592L;

    private int code;
    private String msg;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
