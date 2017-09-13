package com.zdv.hexiaobao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseActivity extends FragmentActivity {
    protected static final String PAGE_0 = "page_0";
    protected static final String PAGE_1 = "page_1";
    protected static final String PAGE_2 = "page_2";
    protected static final String PAGE_3 = "page_3";
    protected Context context;
    ProgressDialog progressDialog;

    protected final int EXIT_CONFIRM = 20;

    boolean stop = false;//网络请求标志位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

    }

    public String currentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    protected void showWaitDialog(String tip) {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(tip);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setOnDismissListener((dia) -> onProgressDissmiss());
        progressDialog.show();

    }

    protected void showWaitDialogCacel(String tip) {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setMessage(tip);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnDismissListener((dia) -> onProgressDissmiss());
        progressDialog.show();

    }

    /**
     *
     */
    protected void onProgressDissmiss() {
        stop = true;
    }

    protected void hideWaitDialog() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
    }


    protected void showDialog(int type, String title, String tip, String posbtn, String negbtn) {
        AlertDialog dialog = null;
        if (negbtn == null) {
            dialog = new AlertDialog.Builder(this).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .create();
        } else {
            dialog = new AlertDialog.Builder(this).setTitle(title)
                    .setMessage(tip)
                    .setPositiveButton(posbtn, (dia, which) -> confirm(type, dia))
                    .setNegativeButton(negbtn, (dia, which) -> cancel(type, dia)).create();
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void confirm(int type, DialogInterface dialog) {
        dialog.dismiss();
    }

    protected void cancel(int type, DialogInterface dialog) {
        dialog.dismiss();
    }
}
