package com.zdv.hexiaobao.present;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.bean.WandiantongLoginInfo;
import com.zdv.hexiaobao.bean.WandiantongRespInfo;
import com.zdv.hexiaobao.model.IRequestMode;
import com.zdv.hexiaobao.model.converter.CustomGsonConverter;
import com.zdv.hexiaobao.model.converter.CustomXmlConverter;
import com.zdv.hexiaobao.view.ILoginView;
import com.zdv.hexiaobao.view.IOrderView;
import com.zdv.hexiaobao.view.IPayView;
import com.zdv.hexiaobao.view.IView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Administrator on 2017/4/6.
 */

public class QueryPresent implements IRequestPresent {
    private IView iView;
    private Context context;
    private IRequestMode iRequestMode;

    private static QueryPresent instance = null;

    public void setView(Activity activity) {
        iView = (IView) activity;
    }

    public void setView(Fragment fragment) {
        iView = (IView) fragment;
    }

    private QueryPresent(Context context_) {
        context = context_;
    }

    public static synchronized QueryPresent getInstance(Context context) {
        if (instance == null) {
            return new QueryPresent(context);
        }
        return instance;
    }

    public void initRetrofit(String url, boolean isXml) {
        try {
            if (isXml) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(CustomXmlConverter.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            } else {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
                iRequestMode = retrofit.create(IRequestMode.class);
            }

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }

    public void initRetrofit2(String url, boolean isXml) {
        try {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(CustomGsonConverter.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                            .addNetworkInterceptor(
                                    new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build())
                    .build();
            iRequestMode = retrofit.create(IRequestMode.class);

        } catch (IllegalArgumentException e) {
            e.fillInStackTrace();
        }
    }


    @Override
    public void Login(String username, String password, String company) {
        iRequestMode.Login(username, password, company)
                .onErrorReturn(s -> new WandiantongLoginInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((ILoginView) iView).ResolveLoginInfo(s));
    }

    @Override
    public void SearchOrder(String secret, String ucode, String code) {
        iRequestMode.SearchOrder(secret, ucode, code)
                .onErrorReturn(s -> new WandiantongRespInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IOrderView) iView).ResolveSearchOrderInfo(s));
    }

    @Override
    public void ConfirmOrder(String secret, String ucode, String ocode, String remark) {
        iRequestMode.ConfirmOrder(secret, ucode, ocode, remark)
                .onErrorReturn(s -> new WandiantongRespInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IOrderView) iView).ResolveConfirmOrderInfo(s));
    }

    @Override
    public void SearchCloudOrder(String id, String sign) {
        iRequestMode.SearchCloudOrder(id, sign)
                .onErrorReturn(s -> new WandiantongRespInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IOrderView) iView).ResolveSearchCloudOrderInfo(s));
    }

    @Override
    public void CloudPay(String id, String sign, String pay_type) {
        iRequestMode.CloudPay(id, sign,pay_type)
                .onErrorReturn(s -> new WDTResponseCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IPayView) iView).ResolveCloudPay(s));
    }


    @Override
    public void ConfirmOrderPay(String secret, String ucode, String ocode, String paytype, String payprice, String dealtype, String pcode, String receive, String remark) {
        iRequestMode.ConfirmOrderPay(secret, ucode, ocode, paytype, payprice, dealtype, pcode, receive, remark)
                .onErrorReturn(s -> new WDTResponseCode())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IOrderView) iView).ResolveConfirmOrderPay(s));
    }

    @Override
    public void ConfirmCloudOrder(String id, String sign) {
        iRequestMode.ConfirmCloudOrder(id, sign)
                .onErrorReturn(s -> new WandiantongRespInfo())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> ((IOrderView) iView).ResolveConfirmCloudOrderInfo(s));
    }

}
