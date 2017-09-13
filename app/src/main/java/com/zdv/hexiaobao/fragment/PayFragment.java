package com.zdv.hexiaobao.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.present.QueryPresent;
import com.zdv.hexiaobao.utils.Constant;
import com.zdv.hexiaobao.utils.Utils;
import com.zdv.hexiaobao.utils.VToast;
import com.zdv.hexiaobao.view.IPayView;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayFragment extends BaseFragment implements IPayView {
    IPayListener listener;

    @Bind(R.id.cash_layout)
    LinearLayout cash_layout;
    @Bind(R.id.wxpay_layout)
    LinearLayout wxpay_layout;
    @Bind(R.id.alipay_layout)
    LinearLayout alipay_layout;

    @Bind(R.id.header_exit)
    RelativeLayout header_exit;
    @Bind(R.id.pay_count_tv)
    TextView pay_count_tv;

    String orderId,order_money;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pay, container, false);
        ButterKnife.bind(PayFragment.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }


    private void initView() {
        RxView.clicks(cash_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showDialog(0,"提示","客户是否已经付款，将返回支付成功到订单","是","否"));
        RxView.clicks(wxpay_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> WxPay());
        RxView.clicks(alipay_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> AliPay());
        RxView.clicks(header_exit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());

        pay_count_tv.setText(order_money);
    }

    private void AliPay() {
        VToast.toast(getContext(),"暂未开通");
    }

    private void WxPay() {
        VToast.toast(getContext(),"暂未开通");
    }

    public void Back() {
        listener.payBack(0);
    }

    @Override
    protected void confirm(int type, DialogInterface dia) {
        super.confirm(type, dia);
        switch (type){
            case 0:
                CashPay();
                break;

        }
    }


    private void CashPay() {
        showWaitDialog("请稍等");
        present.initRetrofit(Constant.URL_RENRENSONG, false);

        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.ID, orderId);
        StringA1.put("pay_type", "0");
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.CloudPay(StringA1.get(Constant.ID), StringA1.get(Constant.SIGN), StringA1.get("pay_type"));
    }


    private void initDate() {
        util = Utils.getInstance();
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(context);
        present.initRetrofit(Constant.URL_WANDIAN, false);
        present.setView(PayFragment.this);
    }

    public void refreshState() {

    }


    public void setOrderId(String orderId_,String order_money_) {
        orderId = orderId_;
        order_money = order_money_;
    }

    @Override
    public void ResolveCloudPay(WDTResponseCode info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrcode() == null) {

            VToast.toast(context, "网络错误");
            return;
        }
        VToast.toast(getContext(), info.getErrmsg());

        if (info.getErrcode().equals("200")) {
            listener.payBack(1);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IPayListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public interface IPayListener {

        void payBack(int pay_state);

    }
}
