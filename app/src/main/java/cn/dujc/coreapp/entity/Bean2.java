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

    /*private String intArrays;
    private String stringArrays;
    private String booleanArrays;
    private String floatList;
    private String stringList;
    private String stringStringMap;

    public String getIntArrays() {
        return intArrays;
    }

    public void setIntArrays(String intArrays) {
        this.intArrays = intArrays;
    }

    public String getStringArrays() {
        return stringArrays;
    }

    public void setStringArrays(String stringArrays) {
        this.stringArrays = stringArrays;
    }

    public String getBooleanArrays() {
        return booleanArrays;
    }

    public void setBooleanArrays(String booleanArrays) {
        this.booleanArrays = booleanArrays;
    }

    public String getFloatList() {
        return floatList;
    }

    public void setFloatList(String floatList) {
        this.floatList = floatList;
    }

    public String getStringList() {
        return stringList;
    }

    public void setStringList(String stringList) {
        this.stringList = stringList;
    }

    public String getStringStringMap() {
        return stringStringMap;
    }

    public void setStringStringMap(String stringStringMap) {
        this.stringStringMap = stringStringMap;
    }*/

}
