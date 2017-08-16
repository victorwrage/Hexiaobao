package com.zdv.hexiaobao.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.cus_view.ProgressBarItem;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseFragment extends Fragment {
    protected Context context;
    protected final String SUCCESS = "200";
    protected final String CLOUD_SUCCESS = "0";
    protected final String COOKIE_KEY = "cookie";


    public String currentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }


    protected void showWaitDialog(String tip){
        ProgressBarItem.show(getContext(),tip,false,null);
    }
    protected void hideWaitDialog() {
        ProgressBarItem.hideProgress();
    }

    protected void startLoading(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Animation rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        imageView.startAnimation(rotate);
    }

    protected void stopLoading(ImageView imageView) {
        imageView.setVisibility(View.GONE);
        imageView.clearAnimation();
    }


    AlertDialog dialog;
    protected void showDialog(int type, String title, String tip, String posbtn, String negbtn) {
        dialog = null;
        if (negbtn == null) {
            dialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .create();
        } else {
            dialog = new AlertDialog.Builder(getActivity()).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .setNegativeButton(negbtn, (dia, which) -> cancel(type, dia)).create();
        }
        dialog.setCancelable(false);
        dialog.show();

    }

    protected void cancel(int type, DialogInterface dia) {
        dialog.dismiss();
    }

    protected void confirm(int type, DialogInterface dia) {
        dialog.dismiss();
    }



}