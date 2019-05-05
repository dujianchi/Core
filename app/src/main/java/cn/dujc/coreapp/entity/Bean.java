package cn.dujc.coreapp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bean implements Serializable {
    private static final long serialVersionUID = 1154192701543147592L;

    private Integer aaa;
    private Float bbb;
    @SerializedName("ccc")
    private String cccc;
    private Boolean ddd;
    private Double eee;

    public Integer getAaa() {
        return aaa;
    }

    public void setAaa(Integer aaa) {
        this.aaa = aaa;
    }

    public Float getBbb() {
        return bbb;
    }

    public void setBbb(Float bbb) {
        this.bbb = bbb;
    }

    public String getCcc() {
        return cccc;
    }

    public void setCcc(String ccc) {
        this.cccc = ccc;
    }

    public Boolean getDdd() {
        return ddd;
    }

    public void setDdd(Boolean ddd) {
        this.ddd = ddd;
    }

    public Double getEee() {
        return eee;
    }

    public void setEee(Double eee) {
        this.eee = eee;
    }
}
