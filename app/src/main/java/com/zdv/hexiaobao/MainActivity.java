package com.zdv.hexiaobao;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.WriterException;
import com.pos.api.Printer;
import com.socks.library.KLog;
import com.zdv.hexiaobao.activity.ClipImageActivity;
import com.zdv.hexiaobao.bean.WandiantongOrderInfo;
import com.zdv.hexiaobao.fragment.DialogFragmentPhotoTip;
import com.zdv.hexiaobao.fragment.FragmentHistory;
import com.zdv.hexiaobao.fragment.FragmentLogin;
import com.zdv.hexiaobao.fragment.FragmentMain;
import com.zdv.hexiaobao.fragment.PayFragment;
import com.zdv.hexiaobao.utils.Constant;
import com.zdv.hexiaobao.utils.D2000V1ScanInitUtils;
import com.zdv.hexiaobao.utils.Utils;
import com.zdv.hexiaobao.utils.VToast;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;


public class MainActivity extends BaseActivity implements FragmentLogin.ILoginListener, FragmentMain.IMainListener,PayFragment.IPayListener, DialogFragmentPhotoTip.IPhotoTipListtener
        , FragmentHistory.IHistoryListener {
    private final int START_ALBUM_REQUESTCODE = 1;
    private final int CAMERA_WITH_DATA = 2;
    private final int CROP_RESULT_CODE = 3;
    protected final String COOKIE_KEY = "cookie";
    FragmentLogin fragment0;
    FragmentMain fragment1;
    FragmentHistory fragment2;
    PayFragment fragment3;
    D2000V1ScanInitUtils d2000V1ScanInitUtils;
    private final static int SCAN_CLOSED = 20;
    Printer printer;
    Utils util;
    String code;
    SharedPreferences sp;
    private Executor executor;
    Boolean isInit = false;
    DialogFragmentPhotoTip dialogFragmentPhotoTip;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (fragment2 != null && !fragment2.isHidden()) {
                fragment2.Back();
                return true;
            }
            if (fragment3 != null && !fragment3.isHidden()) {
                fragment3.Back();
                return true;
            }
            Exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        executor = Executors.newSingleThreadExecutor();
        sp = getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        fragment0 = new FragmentLogin();
        ft.add(R.id.fragment_container, fragment0, PAGE_0);
        ft.show(fragment0);
        ft.commit();

        util = Utils.getInstance();

        Bmob.initialize(this, Constant.PUBLIC_BMOB_KEY);
        // BmobUpdateAgent.initAppVersion(context);
        BmobUpdateAgent.setUpdateListener((updateStatus, updateInfo) -> {
            if (updateStatus == UpdateStatus.Yes) {//版本有更新o

            } else if (updateStatus == UpdateStatus.No) {
                if (isInit) {
                    VToast.toast(context, "<<核销宝>>为最新版本");
                }
                KLog.v("版本无更新");
            } else if (updateStatus == UpdateStatus.EmptyField) {//此提示只是提醒开发者关注那些必填项，测试成功后，无需对用户提示
                KLog.v("请检查你AppVersion表的必填项，1、target_size（文件大小）是否填写；2、path或者android_url两者必填其中一项。");
            } else if (updateStatus == UpdateStatus.IGNORED) {
                KLog.v("该版本已被忽略更新");
            } else if (updateStatus == UpdateStatus.ErrorSizeFormat) {
                KLog.v("请检查target_size填写的格式，请使用file.length()方法获取apk大小。");
            } else if (updateStatus == UpdateStatus.TimeOut) {
                KLog.v("查询出错或查询超时");
            }
            isInit = true;
        });
        BmobUpdateAgent.update(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        isInit = true;
        d2000V1ScanInitUtils = D2000V1ScanInitUtils.getInstance(MainActivity.this, promptHandler);
        executor.execute(() -> d2000V1ScanInitUtils.close2());
    }

    @Override
    protected void onResume() {
        super.onResume();
        KLog.v("onResume" + isInit);
        if (isInit) {
            showWaitDialog("请稍后");
            initDevice();
            promptHandler.postDelayed(() -> {
                hideWaitDialog();
            }, 5000);
        }
    }

    private void initDevice() {

        executor.execute(() -> {
            KLog.v("initDevice");
            d2000V1ScanInitUtils = D2000V1ScanInitUtils.getInstance(MainActivity.this, promptHandler);
            if (!d2000V1ScanInitUtils.getStart()) {
                d2000V1ScanInitUtils.open();
            }
            d2000V1ScanInitUtils.d2000V1ScanOpen();
        });

    }

    private Handler promptHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    sendData((String) msg.obj);
                    break;
                case SCAN_CLOSED:
                    if (fragment1 != null) fragment1.print();
                    break;
                default:
                    break;
            }
        }
    };

    private void sendData(String obj) {
        code = obj.trim();
        if (fragment1 != null && fragment1.isVisible()) {
            fragment1.queryOrder(obj.trim());
        }
    }

    @Override
    protected void onDestroy() {
        KLog.v("onDestroy");
        if (d2000V1ScanInitUtils != null) {
            d2000V1ScanInitUtils.close();
        }
        if (printer != null) {
            printer.DLL_PrnRelease();
        }
        d2000V1ScanInitUtils = null;
        HeApplication.getInstance().exitApplication();
        super.onDestroy();
    }

    public void Exit() {
        showDialog(EXIT_CONFIRM, "提示", "是否退出?", "确认", "取消");
    }

    @Override
    public void closeScanThenPrint() {

        executor.execute(() -> d2000V1ScanInitUtils.setScanState());
        showWaitDialog("请等待打印完成");
        promptHandler.sendEmptyMessageDelayed(SCAN_CLOSED, 2000);
    }

    @Override
    public void print(String info) {
        printer = new Printer(this, bRet -> executor.execute(() -> {
            int iRet = -1;
            iRet = printer.DLL_PrnInit();
            if (iRet == 0) {
                printer.DLL_PrnSetMode((byte) 0x80);
                printLogo(info);
            } else {
                hideWaitDialog();
                VToast.toast(context, "打印错误");
            }
        }));

        promptHandler.postDelayed(() -> {
            hideWaitDialog();
          //  startScan();
        }, 5000);
    }

    @Override
    public void Photo() {
        dialogFragmentPhotoTip = new DialogFragmentPhotoTip();
        dialogFragmentPhotoTip.show(getSupportFragmentManager(), "d1");
    }

    @Override
    public void gotoHistory() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment2 == null) {
            fragment2 = new FragmentHistory();
            ft.add(R.id.fragment_container, fragment2, PAGE_2);
        } else {
            fragment2.refreshState();
        }
        ft.show(fragment2);
        ft.hide(fragment0);
        ft.hide(fragment1);
        ft.commit();
    }

    @Override
    public void gotoPay(String order_id,String cash) {
        KLog.v("order_id"+order_id +"cash:"+cash);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment3 == null) {
            fragment3 = new PayFragment();
            ft.add(R.id.fragment_container, fragment3, PAGE_3);
        } else {
            fragment3.refreshState();
        }
        fragment3.setOrderId(order_id,cash);

        ft.hide(fragment1);
        ft.show(fragment3);
        ft.commitAllowingStateLoss();

    }


    private void printLogo(String info) {
        if(Constant.logo==null){
            VToast.toast(context,"定制的LOGO不存在，将恢复默认");
            sp.edit().putString("img_path","").commit();
            Constant.logo = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
        }
        Bitmap bitmap = util.readBitMap(context, Constant.logo);
        Bitmap barcode = null;
        try {
            barcode = util.readBitMap(context, util.CreateOneDCode(code));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //Bitmap topBmp = util.createWidenQRCODE(context, barcode);
        Bitmap topBmp = util.createWidenOneCODE(context, barcode);


        printer.DLL_PrnBmp(bitmap);
        printer.DLL_PrnStr("     \n");
        printer.DLL_PrnSetFont((byte) 24, (byte) 24, (byte) 0x00);
        printer.DLL_PrnStr("            " + info + "\n");
        printer.DLL_PrnBmp(topBmp);
        printer.DLL_PrnStr("     \n");
        printer.DLL_PrnStr("     \n");
        printer.DLL_PrnStart();
    }

    private void printStr(WandiantongOrderInfo info) {

        Bitmap bitmap = util.readBitMap(context, R.drawable.print_logo);

        printer.DLL_PrnBmp(bitmap);

        printer.DLL_PrnSetFont((byte) 24, (byte) 24, (byte) 0x00);
        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStr("物件名称:" + info.getItem_name() + "\n");
        printer.DLL_PrnStr("物件ID:" + info.getId() + "\n");
        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStr("订单号:" + code + "\n");
        printer.DLL_PrnStr("合作商家:" + info.getShop_id() + "\n");
        printer.DLL_PrnStr("机器序号:" + util.getSerialNumber() + "\n");
        printer.DLL_PrnStr("寄件人:" + util.getNameEncrypt(info.getY_name()) + "\n");
        printer.DLL_PrnStr("寄件人电话:" + info.getY_tel() + "\n");
        printer.DLL_PrnStr("寄件人地址:" + info.getY_address() + "\n");
        printer.DLL_PrnStr("收件人:" + util.getNameEncrypt(info.getH_name()) + "\n");
        printer.DLL_PrnStr("收件人电话:" + info.getH_tel() + "\n");
        printer.DLL_PrnStr("收件人地址:" + info.getH_address() + "\n");

        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStr("支付日期:" + currentDate("yyyyMMdd HH:mm:ss") + "\n");
        printer.DLL_PrnStr("配送费: RMB" + info.getMoney() + "\n");
        printer.DLL_PrnStr("  \n");
        printer.DLL_PrnSetFont((byte) 16, (byte) 16, (byte) 0x00);
        printer.DLL_PrnStr("备注:" + "  \n");
        printer.DLL_PrnStr("  \n");
        printer.DLL_PrnStr("          签名：" + "  \n");
        Bitmap bitmap2 = util.readBitMap(context, R.drawable.blank);
        printer.DLL_PrnBmp(bitmap2);
        printer.DLL_PrnStr("-------------------------------\n");
        printer.DLL_PrnStart();
    }

    @Override
    protected void confirm(int type, DialogInterface dialog) {
        super.confirm(type, dialog);
        switch (type) {
            case EXIT_CONFIRM:
                finish();
                break;
        }
    }

    @Override
    public void gotoMain() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fragment1 == null) {
            initDevice();
            fragment1 = new FragmentMain();
            ft.add(R.id.fragment_container, fragment1, PAGE_1);
        }
        if (fragment2 == null) {
            fragment2 = new FragmentHistory();
            ft.add(R.id.fragment_container, fragment2, PAGE_2);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        ft.show(fragment1);
        ft.hide(fragment0);

        ft.hide(fragment2);
        ft.commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // String result = null;
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {

            case CROP_RESULT_CODE:
                // Constant.logo = util.createLogo(data.getStringExtra(ClipImageActivity.RESULT_PATH));

                String path = data.getStringExtra(ClipImageActivity.RESULT_PATH);
                Constant.logo = util.createLogo(context, path);
                sp.edit().putString("img_path", path).commit();

                if (dialogFragmentPhotoTip != null && !dialogFragmentPhotoTip.isHidden()) {
                    dialogFragmentPhotoTip.showLogo();
                }
                break;
            case START_ALBUM_REQUESTCODE:
                Constant.logo = BitmapFactory.decodeFile(util.getRealFilePath(context, data.getData()));

                if (Constant.logo.getHeight() > 360 || !util.getRealFilePath(context, data.getData()).toLowerCase().endsWith("bmp")) {
                    Constant.logo = util.createLogo(context, util.getRealFilePath(context, data.getData()));
                }
                sp.edit().putString("img_path", util.getRealFilePath(context, data.getData())).commit();
                if (dialogFragmentPhotoTip != null && !dialogFragmentPhotoTip.isHidden()) {
                    dialogFragmentPhotoTip.showLogo();
                }
                // startCropImageActivity(util.getRealFilePath(context,data.getData()));
                break;
            case CAMERA_WITH_DATA:
                // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
                KLog.v("CAMERA_WITH_DATA" + Environment.getExternalStorageDirectory()
                        + "/" + Constant.TMP_PATH);
                startCropImageActivity(Environment.getExternalStorageDirectory()
                        + "/" + Constant.TMP_PATH);
                break;
        }
    }


    @Override
    public void startScan() {
        executor.execute(() -> {
            d2000V1ScanInitUtils.open();
            d2000V1ScanInitUtils.d2000V1ScanOpen();
        });
    }

    @Override
    public void OpenPhoto(int type) {
        switch (type) {
            case 1:
                startAlbum();
                break;
            case 2:
                startCapture();
                break;
        }
    }

    // 裁剪图片的Activity
    private void startCropImageActivity(String path) {
        ClipImageActivity.startActivity(this, path, CROP_RESULT_CODE);
    }

    private void startAlbum() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        801);
                return;
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, START_ALBUM_REQUESTCODE);
    }

    private void startCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), Constant.TMP_PATH)));
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    @Override
    public void payBack(int pay_state) {
        gotoMain();
        switch(pay_state){
            case 0:

                break;
            case 1:
                fragment1.ConfirmCloudOrder();
                break;
        }
    }

}
