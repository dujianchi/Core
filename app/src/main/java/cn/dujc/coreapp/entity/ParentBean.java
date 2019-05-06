package cn.dujc.coreapp.entity;

import java.io.Serializable;

public class ParentBean<T> implements Serializable {
    private static final long serialVersionUID = -3824251436674975025L;
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
