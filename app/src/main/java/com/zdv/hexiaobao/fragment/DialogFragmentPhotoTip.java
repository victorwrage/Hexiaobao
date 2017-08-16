
package com.zdv.hexiaobao.fragment;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.utils.Constant;
import com.zdv.hexiaobao.utils.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * @Description:TODO(实名提示)
 * @author: xiaoyl
 * @date: 2013-07-20 下午6:38:07
 */
public class DialogFragmentPhotoTip extends DialogFragment {
    private static final String COOKIE_KEY = "cookie";
    private IPhotoTipListtener callBack;

    @Bind(R.id.verify_shut_lay)
    RelativeLayout verify_shut_lay;

    @Bind(R.id.photo_img_iv)
    ImageView photo_img_iv;
    @Bind(R.id.photo_thumb_lay)
    LinearLayout photo_thumb_lay;
    @Bind(R.id.photo_camera_lay)
    LinearLayout photo_camera_lay;
    @Bind(R.id.photo_default_lay)
    LinearLayout photo_default_lay;
    Utils util;

    public DialogFragmentPhotoTip() {

    }

    @Override
    public void onAttach(Context activity) {
        try {
            callBack = (IPhotoTipListtener) activity;
        } catch (ClassCastException e) {
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_tip_layout, container,
                false);
        ButterKnife.bind(DialogFragmentPhotoTip.this, view);
        util = Utils.getInstance();

        RxView.clicks(verify_shut_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> dismiss());
        RxView.clicks(photo_camera_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Camera());
        RxView.clicks(photo_thumb_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Thumb());
        RxView.clicks(photo_default_lay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Default());

        return view;
    }

    private void Default() {
        Constant.logo = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
        photo_img_iv.setImageBitmap(Constant.logo);
    }

    private void Thumb() {
        callBack.OpenPhoto(1);
    }

    private void Camera() {
        callBack.OpenPhoto(2);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
    }


    @Override
    public void onStart() {
        super.onStart();
      //  KLog.v("onHiddenChanged" + "---" + Constant.logo + "---");
        if (Constant.logo == null) {
            Constant.logo = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
        }
        photo_img_iv.setImageBitmap(Constant.logo);
    }

    public void showLogo() {
        photo_img_iv.setImageBitmap(Constant.logo);
    }

    public interface IPhotoTipListtener {
        void OpenPhoto(int type);
    }
}
