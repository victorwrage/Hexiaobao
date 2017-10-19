package com.zdv.hexiaobao.model;

import android.support.annotation.Nullable;

import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.bean.WandiantongLoginInfo;
import com.zdv.hexiaobao.bean.WandiantongRespInfo;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xyl on 2017/4/6.
 */

public interface IRequestMode {
    @FormUrlEncoded
    @POST("ThinkCmf/index.php?g=Api&m=Index&a=Login")
    Observable<WandiantongLoginInfo> Login(@Field("username") String username, @Field("password") String password, @Field("company_id") String company_id);

    @FormUrlEncoded
    @POST("ThinkCmf/index.php?g=Api&m=Order&a=SearchOrder")
    Observable<WandiantongRespInfo> SearchOrder(@Field("secret") String secret,@Field("ucode") String ucode, @Field("code") String code);

    @FormUrlEncoded
    @POST("ThinkCmf/index.php?g=Api&m=Order&a=CancelForVertify")
    Observable<WandiantongRespInfo> ConfirmOrder(@Field("secret") String secret, @Field("ucode") String ucode, @Field("ocode") String ocode, @Field("remark") String remark);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Book&a=searchOrder")
    Observable<WandiantongRespInfo> SearchCloudOrder(@Field("id") String id, @Field("sign") String sign);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Book&&a=acceptOrder")
    Observable<WandiantongRespInfo> ConfirmCloudOrder(@Field("id") String id, @Field("sign") String sign);

//    @FormUrlEncoded
//    @POST("index.php?g=Api&m=Curpons&a=getMemCurpons")
//    Observable<WandiantongRespInfo> SearchTicketOrder(@Field("memcode") String memcode,@Field("sign") String sign);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Curpons&a=UseCurpon")
    Observable<WandiantongRespInfo> SearchTicketOrder(@Field("sign") String sign,@Field("memcode") String memcode,@Field("sn_code") String sn_code);

    @FormUrlEncoded
    @POST("index.php?g=Api&m=Book&&a=save_paytype")
    Observable<WDTResponseCode> CloudPay(@Field("id") String id, @Field("sign") String sign, @Field("pay_type") String pay_type);

    @FormUrlEncoded
    @POST("ThinkCmf/index.php?g=Api&m=Ordercomment&a=AddOrderFeedback")
    Observable<WDTResponseCode> ConfirmOrderPay(@Field("secret") String secret, @Field("ucode") String ucode, @Field("ocode") String ocode
            , @Field("paytype") String paytype, @Field("payprice") String payprice, @Nullable @Field("dealtype") String dealtype,
                                                @Field("pcode") String pcode, @Field("receive") String receive, @Nullable @Field("remark") String remark);
}
