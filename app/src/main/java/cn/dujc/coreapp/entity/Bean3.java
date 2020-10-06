package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;

public class Bean3<T> implements Serializable {
    private static final long serialVersionUID = 1154192701543147592L;

    private String code;
    private int msg;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
