package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Bean1 implements Serializable {
    private int[] intArrays;
    private String[] stringArrays;
    private boolean[] booleanArrays;
    private List<Float> floatList;
    private List<String> stringList;
    private Map<String, String> stringStringMap;

    public int[] getIntArrays() {
        return intArrays;
    }

    public void setIntArrays(int[] intArrays) {
        this.intArrays = intArrays;
    }

    public String[] getStringArrays() {
        return stringArrays;
    }

    public void setStringArrays(String[] stringArrays) {
        this.stringArrays = stringArrays;
    }

    public boolean[] getBooleanArrays() {
        return booleanArrays;
    }

    public void setBooleanArrays(boolean[] booleanArrays) {
        this.booleanArrays = booleanArrays;
    }

    public List<Float> getFloatList() {
        return floatList;
    }

    public void setFloatList(List<Float> floatList) {
        this.floatList = floatList;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public Map<String, String> getStringStringMap() {
        return stringStringMap;
    }

    public void setStringStringMap(Map<String, String> stringStringMap) {
        this.stringStringMap = stringStringMap;
    }
}
