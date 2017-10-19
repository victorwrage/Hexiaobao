package com.zdv.hexiaobao.present;


/**
 * Info:
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:46
 */

public interface IRequestPresent {
    void Login(String username, String password, String company);
    void SearchOrder( String secret, String ucode, String code);
    void ConfirmOrder(String secret,String ucode, String ocode, String remark);

    void SearchCloudOrder( String id, String sign);

    void CloudPay( String id, String sign ,String pay_type);

    void ConfirmOrderPay(String secret,String ucode, String ocode
            ,String paytype,String payprice, String dealtype,
                         String pcode,String receive,String remark);

    void SearchTicketOrder(String sign,String memcode,String sn_code);

    void ConfirmCloudOrder( String id, String sign);
}
