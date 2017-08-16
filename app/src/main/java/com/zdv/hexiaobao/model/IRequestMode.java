package com.zdv.hexiaobao.model;

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
    @POST("cloudsend/index.php?g=Api&m=Book&a=searchOrder")
    Observable<WandiantongRespInfo> SearchCloudOrder(@Field("id") String id, @Field("sign") String sign);

    @FormUrlEncoded
    @POST("cloudsend/index.php?g=Api&m=Book&&a=acceptOrder")
    Observable<WandiantongRespInfo> ConfirmCloudOrder(@Field("id") String id, @Field("sign") String sign);
}
