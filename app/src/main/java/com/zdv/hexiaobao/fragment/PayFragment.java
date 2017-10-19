package com.zdv.hexiaobao.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zdv.hexiaobao.utils.AppUtils;
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

    String orderId, order_money;
    SharedPreferences sp;
    QueryPresent present;
    Utils util;

    private PayResultBroadcastReceiver receiver = new PayResultBroadcastReceiver();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    String[] payResult = (String[]) msg.obj;
                    if (payResult[1].equals("success")) {
                        CashPay();
                    } else {
                        VToast.toast(getContext(), "支付失败，请重试");
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

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
        RxView.clicks(cash_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> showDialog(0, "提示", "客户是否已经付款，将返回支付成功到订单", "是", "否"));
        RxView.clicks(wxpay_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> WxPay());
        RxView.clicks(alipay_layout).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> AliPay());
        RxView.clicks(header_exit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Back());

        pay_count_tv.setText(order_money);
    }

    private void AliPay() {
        //VToast.toast(getContext(), "暂未开通");
        jumpToPay("1", "1");
    }

    private void WxPay() {
        // VToast.toast(getContext(), "暂未开通");
        jumpToPay("1", "0");
    }

    public void Back() {
        listener.payBack(0);
    }

    @Override
    protected void confirm(int type, DialogInterface dia) {
        super.confirm(type, dia);
        switch (type) {
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


    public void setOrderId(String orderId_, String order_money_) {
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


    public class PayResultBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.qhw.shopmanagerapp.pay.action")) {
                String result = intent.getBundleExtra("bundle").getString("result");
                String type = intent.getBundleExtra("bundle").getString("type");
                String str[] = {type, result};
                Message message = mHandler.obtainMessage();
                message.what = 0x11;
                message.obj = str;
                mHandler.sendMessage(message);
            }
        }
    }

    /**
     * 注册广播接收者
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter("com.qhw.shopmanagerapp.pay.action");
        filter.addAction("com.qhw.shopmanagerapp.pay.action");
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 注销广播接收者
     */
    private void unRegisterReceiver() {
        if (receiver == null) return;

        getActivity().unregisterReceiver(receiver);
        receiver = null;
    }

    /**
     * 移动支付
     *
     * @param payType
     */
    private void jumpToPay(String type, String payType) {
        if (AppUtils.isAppInstalled(this.getActivity(), "com.qhw.swishcardapp")) {
            Intent swishIntent = this.getActivity().getPackageManager().getLaunchIntentForPackage("com.qhw.swishcardapp");
            swishIntent.setFlags(101);
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("money", order_money);
            bundle.putString("payType", payType);
            swishIntent.putExtra("bundle", bundle);
            startActivity(swishIntent);
        } else {
            VToast.toast(getContext(), "请先前往万店通应用商店下载万店通刷App");
        }
    }
}
