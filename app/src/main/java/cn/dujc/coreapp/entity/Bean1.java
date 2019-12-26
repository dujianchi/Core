package cn.dujc.coreapp.entity;

import java.io.Serializable;
import java.util.List;

public class Bean1 extends Bean<String> implements Serializable {
    private static final long serialVersionUID = -3824251436674975025L;

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

    private String id;
    private String otype;
    private String is_print;
    private String read_status;
    private String orders_status;
    private String complete_time;
    private String apply_read_status;
    private String test_name;
    private String test_phone;
    private String test_sex;
    private String orders_code;
    private String is_read;
    private String report_url;
    private String barcode;
    private String baonum;
    private String status;
    private String pnames_str;
    private String item_result_hint;
    private int item_result_en;
    private List<String> pdf;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getIs_print() {
        return is_print;
    }

    public void setIs_print(String is_print) {
        this.is_print = is_print;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getOrders_status() {
        return orders_status;
    }

    public void setOrders_status(String orders_status) {
        this.orders_status = orders_status;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getApply_read_status() {
        return apply_read_status;
    }

    public void setApply_read_status(String apply_read_status) {
        this.apply_read_status = apply_read_status;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getTest_phone() {
        return test_phone;
    }

    public void setTest_phone(String test_phone) {
        this.test_phone = test_phone;
    }

    public String getTest_sex() {
        return test_sex;
    }

    public void setTest_sex(String test_sex) {
        this.test_sex = test_sex;
    }

    public String getOrders_code() {
        return orders_code;
    }

    public void setOrders_code(String orders_code) {
        this.orders_code = orders_code;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getBaonum() {
        return baonum;
    }

    public void setBaonum(String baonum) {
        this.baonum = baonum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPnames_str() {
        return pnames_str;
    }

    public void setPnames_str(String pnames_str) {
        this.pnames_str = pnames_str;
    }

    public String getItem_result_hint() {
        return item_result_hint;
    }

    public void setItem_result_hint(String item_result_hint) {
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
}
