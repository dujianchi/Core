package cn.dujc.coreapp.entity;

import java.io.Serializable;

public class Bean implements Serializable {
    private static final long serialVersionUID = 1154192701543147592L;

    private int aaa;
    private float bbb;
    private String ccc;
    private boolean ddd;
    private double eee;

    public int getAaa() {
        return aaa;
    }

    public void setAaa(int aaa) {
        this.aaa = aaa;
    }

    public float getBbb() {
        return bbb;
    }

    public void setBbb(float bbb) {
        this.bbb = bbb;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public boolean isDdd() {
        return ddd;
    }

    public void setDdd(boolean ddd) {
        this.ddd = ddd;
    }

    public double getEee() {
        return eee;
    }

    public void setEee(double eee) {
        this.eee = eee;
    }
}
