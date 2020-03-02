package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;

public class Bean2 implements Serializable {
    private static final long serialVersionUID = 6603103664641364959L;

    /**
     * id : 1644533
     * otype : 5
     * is_print : 1
     * read_status : 1
     * orders_status : 4
     * complete_time : 2018-08-31
     * apply_read_status : 0
     * test_name : 啦啦啦
     * test_phone : 15222222222
     * test_sex : 男
     * orders_code : 153569989591594
     * is_read : 1
     * report_url :
     * barcode : 1406134564
     * baonum : 1
     * status : 1
     * pdf : []
     * pnames_str : 血清绒毛膜促性腺激素测定(HCG) 
     * item_result_hint : 正常
     * item_result_en : 1
     */

    private int id;
    private Integer otype;
    private boolean is_print;
    private double read_status;
    private float orders_status;
    private String complete_time;
    private Double apply_read_status;
    private String test_name;
    private Float test_phone;
    private byte test_sex;
    private String orders_code;
    private Byte is_read;
    private Long report_url;
    private long barcode;
    private int[] baonum;
    private String[] status;
    private Data pnames_str;
    private Object item_result_hint;
    private int item_result_en;
    private List<String> pdf;
    private Data2<String> data2;

    public static class Data2<T> implements Serializable {
        private static final long serialVersionUID = -4566920785569602019L;
        private T obj;

        public T getObj() {
            return obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }
    }

    public static class Data implements Serializable {
        private static final long serialVersionUID = 1170611118398679438L;
        private String test;
        private Object obj;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOtype() {
        return otype;
    }

    public void setOtype(Integer otype) {
        this.otype = otype;
    }

    public boolean isIs_print() {
        return is_print;
    }

    public void setIs_print(boolean is_print) {
        this.is_print = is_print;
    }

    public double getRead_status() {
        return read_status;
    }

    public void setRead_status(double read_status) {
        this.read_status = read_status;
    }

    public float getOrders_status() {
        return orders_status;
    }

    public void setOrders_status(float orders_status) {
        this.orders_status = orders_status;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public Double getApply_read_status() {
        return apply_read_status;
    }

    public void setApply_read_status(Double apply_read_status) {
        this.apply_read_status = apply_read_status;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public Float getTest_phone() {
        return test_phone;
    }

    public void setTest_phone(Float test_phone) {
        this.test_phone = test_phone;
    }

    public byte getTest_sex() {
        return test_sex;
    }

    public void setTest_sex(byte test_sex) {
        this.test_sex = test_sex;
    }

    public String getOrders_code() {
        return orders_code;
    }

    public void setOrders_code(String orders_code) {
        this.orders_code = orders_code;
    }

    public Byte getIs_read() {
        return is_read;
    }

    public void setIs_read(Byte is_read) {
        this.is_read = is_read;
    }

    public Long getReport_url() {
        return report_url;
    }

    public void setReport_url(Long report_url) {
        this.report_url = report_url;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public int[] getBaonum() {
        return baonum;
    }

    public void setBaonum(int[] baonum) {
        this.baonum = baonum;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public Data getPnames_str() {
        return pnames_str;
    }

    public void setPnames_str(Data pnames_str) {
        this.pnames_str = pnames_str;
    }

    public Object getItem_result_hint() {
        return item_result_hint;
    }

    public void setItem_result_hint(Object item_result_hint) {
        this.item_result_hint = item_result_hint;
    }

    public int getItem_result_en() {
        return item_result_en;
    }

    public void setItem_result_en(int item_result_en) {
        this.item_result_en = item_result_en;
    }

    public List<String> getPdf() {
        return pdf;
    }

    public void setPdf(List<String> pdf) {
        this.pdf = pdf;
    }

    public Data2<String> getData2() {
        return data2;
    }

    public void setData2(Data2<String> data2) {
        this.data2 = data2;
    }
}
