package com.zdv.hexiaobao.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity(indexes = {
        @Index(value = "text, date DESC", unique = true)
})
/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/8/11 14:26
 */
public class VerifyRecord {
    @Id
    private Long ids;


    private String text;
    private String date;
    private String code;
    private String ucode;
    private String item_id;
    private String createtime;
    private String totalprice;
    String payprice;

    String paystate;
    String paytype;
    String status;
    String address;
    String coster;
    String company_id;
    String solve;
    String send_type;
    String crawl;
    String shoptype;
    String remark;
    String b_status;
    String order_type;
    String c_type;
    String money;
    String put_time;
    String item_type;
    String item_name;
    String item_total;
    String item_weight;
    String item_volume;
    String y_address;
    String y_tel;
    String y_name;
    String h_address;
    String h_tel;
    String h_name;
    String order_sn;
    String shop_id;
    

    String n_reason;
    String book_time;
    String book_shop;
@Generated(hash = 242180761)
public VerifyRecord(Long ids, String text, String date, String code,
        String ucode, String item_id, String createtime, String totalprice,
        String payprice, String paystate, String paytype, String status,
        String address, String coster, String company_id, String solve,
        String send_type, String crawl, String shoptype, String remark,
        String b_status, String order_type, String c_type, String money,
        String put_time, String item_type, String item_name, String item_total,
        String item_weight, String item_volume, String y_address, String y_tel,
        String y_name, String h_address, String h_tel, String h_name,
        String order_sn, String shop_id, String n_reason, String book_time,
        String book_shop) {
    this.ids = ids;
    this.text = text;
    this.date = date;
    this.code = code;
    this.ucode = ucode;
    this.item_id = item_id;
    this.createtime = createtime;
    this.totalprice = totalprice;
    this.payprice = payprice;
    this.paystate = paystate;
    this.paytype = paytype;
    this.status = status;
    this.address = address;
    this.coster = coster;
    this.company_id = company_id;
    this.solve = solve;
    this.send_type = send_type;
    this.crawl = crawl;
    this.shoptype = shoptype;
    this.remark = remark;
    this.b_status = b_status;
    this.order_type = order_type;
    this.c_type = c_type;
    this.money = money;
    this.put_time = put_time;
    this.item_type = item_type;
    this.item_name = item_name;
    this.item_total = item_total;
    this.item_weight = item_weight;
    this.item_volume = item_volume;
    this.y_address = y_address;
    this.y_tel = y_tel;
    this.y_name = y_name;
    this.h_address = h_address;
    this.h_tel = h_tel;
    this.h_name = h_name;
    this.order_sn = order_sn;
    this.shop_id = shop_id;
    this.n_reason = n_reason;
    this.book_time = book_time;
    this.book_shop = book_shop;
}
@Generated(hash = 1331977949)
public VerifyRecord() {
}
public Long getIds() {
    return this.ids;
}
public void setIds(Long ids) {
    this.ids = ids;
}
public String getText() {
    return this.text;
}
public void setText(String text) {
    this.text = text;
}
public String getDate() {
    return this.date;
}
public void setDate(String date) {
    this.date = date;
}
public String getCode() {
    return this.code;
}
public void setCode(String code) {
    this.code = code;
}
public String getUcode() {
    return this.ucode;
}
public void setUcode(String ucode) {
    this.ucode = ucode;
}
public String getItem_id() {
    return this.item_id;
}
public void setItem_id(String item_id) {
    this.item_id = item_id;
}
public String getCreatetime() {
    return this.createtime;
}
public void setCreatetime(String createtime) {
    this.createtime = createtime;
}
public String getTotalprice() {
    return this.totalprice;
}
public void setTotalprice(String totalprice) {
    this.totalprice = totalprice;
}
public String getPayprice() {
    return this.payprice;
}
public void setPayprice(String payprice) {
    this.payprice = payprice;
}
public String getPaystate() {
    return this.paystate;
}
public void setPaystate(String paystate) {
    this.paystate = paystate;
}
public String getPaytype() {
    return this.paytype;
}
public void setPaytype(String paytype) {
    this.paytype = paytype;
}
public String getStatus() {
    return this.status;
}
public void setStatus(String status) {
    this.status = status;
}
public String getAddress() {
    return this.address;
}
public void setAddress(String address) {
    this.address = address;
}
public String getCoster() {
    return this.coster;
}
public void setCoster(String coster) {
    this.coster = coster;
}
public String getCompany_id() {
    return this.company_id;
}
public void setCompany_id(String company_id) {
    this.company_id = company_id;
}
public String getSolve() {
    return this.solve;
}
public void setSolve(String solve) {
    this.solve = solve;
}
public String getSend_type() {
    return this.send_type;
}
public void setSend_type(String send_type) {
    this.send_type = send_type;
}
public String getCrawl() {
    return this.crawl;
}
public void setCrawl(String crawl) {
    this.crawl = crawl;
}
public String getShoptype() {
    return this.shoptype;
}
public void setShoptype(String shoptype) {
    this.shoptype = shoptype;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
public String getB_status() {
    return this.b_status;
}
public void setB_status(String b_status) {
    this.b_status = b_status;
}
public String getOrder_type() {
    return this.order_type;
}
public void setOrder_type(String order_type) {
    this.order_type = order_type;
}
public String getC_type() {
    return this.c_type;
}
public void setC_type(String c_type) {
    this.c_type = c_type;
}
public String getMoney() {
    return this.money;
}
public void setMoney(String money) {
    this.money = money;
}
public String getPut_time() {
    return this.put_time;
}
public void setPut_time(String put_time) {
    this.put_time = put_time;
}
public String getItem_type() {
    return this.item_type;
}
public void setItem_type(String item_type) {
    this.item_type = item_type;
}
public String getItem_name() {
    return this.item_name;
}
public void setItem_name(String item_name) {
    this.item_name = item_name;
}
public String getItem_total() {
    return this.item_total;
}
public void setItem_total(String item_total) {
    this.item_total = item_total;
}
public String getItem_weight() {
    return this.item_weight;
}
public void setItem_weight(String item_weight) {
    this.item_weight = item_weight;
}
public String getItem_volume() {
    return this.item_volume;
}
public void setItem_volume(String item_volume) {
    this.item_volume = item_volume;
}
public String getY_address() {
    return this.y_address;
}
public void setY_address(String y_address) {
    this.y_address = y_address;
}
public String getY_tel() {
    return this.y_tel;
}
public void setY_tel(String y_tel) {
    this.y_tel = y_tel;
}
public String getY_name() {
    return this.y_name;
}
public void setY_name(String y_name) {
    this.y_name = y_name;
}
public String getH_address() {
    return this.h_address;
}
public void setH_address(String h_address) {
    this.h_address = h_address;
}
public String getH_tel() {
    return this.h_tel;
}
public void setH_tel(String h_tel) {
    this.h_tel = h_tel;
}
public String getH_name() {
    return this.h_name;
}
public void setH_name(String h_name) {
    this.h_name = h_name;
}
public String getOrder_sn() {
    return this.order_sn;
}
public void setOrder_sn(String order_sn) {
    this.order_sn = order_sn;
}
public String getShop_id() {
    return this.shop_id;
}
public void setShop_id(String shop_id) {
    this.shop_id = shop_id;
}
public String getN_reason() {
    return this.n_reason;
}
public void setN_reason(String n_reason) {
    this.n_reason = n_reason;
}
public String getBook_time() {
    return this.book_time;
}
public void setBook_time(String book_time) {
    this.book_time = book_time;
}
public String getBook_shop() {
    return this.book_shop;
}
public void setBook_shop(String book_shop) {
    this.book_shop = book_shop;
}

}
