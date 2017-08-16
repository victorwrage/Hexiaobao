package com.zdv.hexiaobao.bean;

/**
 * Info: 万店通登录
 * Created by xiaoyl
 * 创建时间:2017/4/22 14:36
 */

public class WandiantongRespInfo {
    String errcode;
    String errmsg;

    public String getIs_print() {
        return is_print;
    }

    public void setIs_print(String is_print) {
        this.is_print = is_print;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    String is_print;
    String order_sn;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    String secret;
    WandiantongOrderInfo content;

    public WandiantongOrderInfo getContent() {
        return content;
    }

    public void setContent(WandiantongOrderInfo content) {
        this.content = content;
    }

    public String getErrcode() {

        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        if(content!=null) {
            return errcode + "|" +errmsg+ secret+content.toString();
        }
        return errcode+"|"+errmsg;
    }

}
