package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Bean2 implements Serializable {
    private double[] intArrays;
    private String stringArrays;
    private boolean booleanArrays;
    private List<Integer> floatList;
    private List<Float> stringList;
    private Map<String, Integer> stringStringMap;

    public double[] getIntArrays() {
        return intArrays;
    }

    public void setIntArrays(double[] intArrays) {
        this.intArrays = intArrays;
    }

    public String getStringArrays() {
        return stringArrays;
    }

    public void setStringArrays(String stringArrays) {
        this.stringArrays = stringArrays;
    }

    public boolean isBooleanArrays() {
        return booleanArrays;
    }

    public void setBooleanArrays(boolean booleanArrays) {
        this.booleanArrays = booleanArrays;
    }

    public List<Integer> getFloatList() {
        return floatList;
    }

    public void setFloatList(List<Integer> floatList) {
        this.floatList = floatList;
    }

    public List<Float> getStringList() {
        return stringList;
    }

    public void setStringList(List<Float> stringList) {
        this.stringList = stringList;
    }

    public Map<String, Integer> getStringStringMap() {
        return stringStringMap;
    }

    public void setStringStringMap(Map<String, Integer> stringStringMap) {
        this.stringStringMap = stringStringMap;
    }
}
