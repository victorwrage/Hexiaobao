package com.zdv.hexiaobao.model;

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
    public Observable<WandiantongRespInfo> SearchCloudOrder(@Field("id") String id, @Field("sign") String sign) {
        return iRequestMode.SearchCloudOrder(id, sign);
    }

    @Override
    public Observable<WandiantongRespInfo> ConfirmCloudOrder(@Field("id") String id, @Field("sign") String sign) {
        return iRequestMode.ConfirmCloudOrder(id, sign);
    }
}
