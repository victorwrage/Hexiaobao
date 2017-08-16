package com.zdv.hexiaobao.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.bean.WandiantongLoginInfo;
import com.zdv.hexiaobao.present.QueryPresent;
import com.zdv.hexiaobao.utils.Constant;
import com.zdv.hexiaobao.utils.Utils;
import com.zdv.hexiaobao.utils.VToast;
import com.zdv.hexiaobao.view.ILoginView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;

public class FragmentLogin extends BaseFragment  implements ILoginView{
    ILoginListener listener;

    @Bind(R.id.login_company_et)
    EditText login_company_et;
    @Bind(R.id.login_user_et)
    EditText login_user_et;
    @Bind(R.id.login_password_et)
    EditText login_password_et;
    @Bind(R.id.login_login_btn)
    Button login_login_btn;
    @Bind(R.id.login_remember_btn)
    CheckBox login_remember_btn;
    @Bind(R.id.update_iv)
    ImageView update_iv;
    @Bind(R.id.update_tv)
    TextView update_tv;

    @Bind(R.id.login_forgot_tv)
    TextView login_forgot_tv;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    WandiantongLoginInfo cur_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(FragmentLogin.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initView() {
        login_user_et.setText(sp.getString("user_name", ""));
        login_password_et.setText(sp.getString("user_pw", ""));
        login_company_et.setText(sp.getString("shop_id", ""));
        login_remember_btn.setChecked(sp.getBoolean("remember",true));
        Constant.cookie.put("user_name", sp.getString("user_name", ""));
        Constant.cookie.put("user_pw", sp.getString("user_pw", ""));
        Constant.cookie.put("shop_id", sp.getString("shop_id", ""));

        RxView.clicks(login_login_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Login());
        RxView.clicks(update_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  BmobUpdateAgent.forceUpdate(getContext()));
        update_tv.setText("当前版本"+util.getAppVersionName(getContext()));
    }

    private void initDate() {
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(context);
        present.initRetrofit(Constant.URL_WANDIAN, false);
        present.setView(FragmentLogin.this);

        util = Utils.getInstance();
    }

    private void Login() {
        if (!util.isNetworkConnected(context)) {
            VToast.toast(context, "没有网络连接");
            return;
        }
        if (login_user_et.getText().toString().trim().equals("")) {
            VToast.toast(context, "请输入用户名");
        } else if (login_password_et.getText().toString().trim().equals("")) {
            VToast.toast(context, "请输入密码");
        } else if (login_company_et.getText().toString().trim().equals("")) {
            VToast.toast(context, "请输入门店号");
        } else {
            SharedPreferences.Editor editor = sp.edit();
            if (login_remember_btn.isChecked()) {
                editor.putString("user_name", login_user_et.getText().toString().trim());
                editor.putString("user_pw", login_password_et.getText().toString().trim());
                editor.putString("shop_id", login_company_et.getText().toString().trim());
                editor.putBoolean("remember", login_remember_btn.isChecked());
                editor.commit();
            } else {
                editor.clear();
                editor.commit();
            }
            showWaitDialog("正在登录");
            present.Login(login_user_et.getText().toString().trim(), login_password_et.getText().toString().trim(), login_company_et.getText().toString().trim());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ILoginListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    @Override
    public void ResolveLoginInfo(WandiantongLoginInfo info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            VToast.toast(context, "网络错误");
            return;
        }
        VToast.toast(context, info.getErrmsg());
        if(info.getErrcode().equals(SUCCESS)) {

            String secret = util.UrlEnco(info.getSecret());
            Constant.cookie.put("secret",secret);
            Constant.cookie.put("company_id",info.getContent().getCompany_id());
            Constant.cookie.put("ucode",info.getContent().getCode());
            info.setSecret(secret);
            cur_user = info;
            listener.gotoMain();
        }
    }

    public interface ILoginListener {
         void gotoMain();
    }
}
