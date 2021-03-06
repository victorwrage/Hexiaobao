package com.zdv.hexiaobao.model;

import android.support.annotation.Nullable;

import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.bean.WandiantongLoginInfo;
import com.zdv.hexiaobao.bean.WandiantongRespInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;

/**
 * Info:接口实现类
 * Created by xiaoyl
 * 创建时间:2017/4/7 9:42
 */

public class RequestModelImpl implements IRequestMode {
    IRequestMode iRequestMode;

    @Override
    public Observable<WandiantongLoginInfo> Login(@Field("username") String username, @Field("password") String password, @Field("company_id") String company_id) {
        return iRequestMode.Login(username, password, company_id);
    }

    @Override
    public Observable<WandiantongRespInfo> SearchOrder(@Field("secret") String secret,@Field("ucode") String ucode, @Field("code") String code) {
        return iRequestMode.SearchOrder(secret, ucode, code);
    }

    @Override
    public Observable<WandiantongRespInfo> ConfirmOrder(@Field("secret") String secret, @Field("ucode") String ucode, @Field("ocode") String ocode, @Field("remark") String remark) {
        return iRequestMode.ConfirmOrder(secret, ucode, ocode, remark);
    }

    @Override
    public Observable<WDTResponseCode> ConfirmOrderPay(@Field("secret") String secret, @Field("ucode") String ucode, @Field("ocode") String ocode, @Field("paytype") String paytype, @Field("payprice") String payprice, @Field("dealtype") String dealtype, @Field("pcode") String pcode, @Field("receive") String receive, @Nullable @Field("remark") String remark) {
        return iRequestMode.ConfirmOrderPay(secret, ucode, ocode, paytype, payprice, dealtype, pcode, receive, remark);
    }

    @Override
    public Observable<WandiantongRespInfo> SearchCloudOrder(@Field("id") String id, @Field("sign") String sign) {
        return iRequestMode.SearchCloudOrder(id, sign);
    }

    @Override
    public Observable<WandiantongRespInfo> ConfirmCloudOrder(@Field("id") String id, @Field("sign") String sign) {
        return iRequestMode.ConfirmCloudOrder(id, sign);
    }

//    @Override
//    public Observable<WandiantongRespInfo> SearchTicketOrder(@Field("memcode") String memcode, @Field("sign") String sign) {
//        return iRequestMode.SearchTicketOrder(memcode,sign);
//    }

    @Override
    public Observable<WandiantongRespInfo> SearchTicketOrder(@Field("sign") String sign, @Field("memcode") String memcode, @Field("sn_code") String sn_code) {
        return iRequestMode.SearchTicketOrder(sign,memcode,sn_code);
    }

    @Override
    public Observable<WDTResponseCode> CloudPay(@Field("id") String id, @Field("sign") String sign, @Field("pay_type") String pay_type) {
        return iRequestMode.CloudPay(id, sign, pay_type);
    }
}
