package com.zdv.hexiaobao.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.a.a.V;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.socks.library.KLog;
import com.zdv.hexiaobao.HeApplication;
import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.adapter.EvaluateItemAdapter;
import com.zdv.hexiaobao.bean.DaoSession;
import com.zdv.hexiaobao.bean.VerifyRecord;
import com.zdv.hexiaobao.bean.VerifyRecordDao;
import com.zdv.hexiaobao.bean.WDTResponseCode;
import com.zdv.hexiaobao.bean.WDTResponseContentItem;
import com.zdv.hexiaobao.bean.WandiantongOrderInfo;
import com.zdv.hexiaobao.bean.WandiantongRespInfo;
import com.zdv.hexiaobao.present.QueryPresent;
import com.zdv.hexiaobao.utils.Constant;
import com.zdv.hexiaobao.utils.Utils;
import com.zdv.hexiaobao.utils.VToast;
import com.zdv.hexiaobao.view.IOrderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;

public class FragmentMain extends BaseFragment implements IOrderView {
    private final int TYPE_MALL = 0;
    private final int TYPE_CLOUD = TYPE_MALL + 1;
    private final int TYPE_TICKET = TYPE_CLOUD + 1;
    private final int QUERY_FAIL = TYPE_TICKET + 1;
    IMainListener listener;
    @Bind(R.id.tip_text)
    TextView tip_text;

    @Bind(R.id.header_exit)
    RelativeLayout header_exit;

    /* @Bind(R.id.empty_iv)
     ImageView empty_iv;
     @Bind(R.id.empty_tv)
     TextView empty_tv;
     @Bind(R.id.empty_lay)
     RelativeLayout empty_lay;*/
    @Bind(R.id.main_tip_iv)
    ImageView main_tip_iv;
    @Bind(R.id.main_tip_tv)
    TextView main_tip_tv;
    @Bind(R.id.main_tip_btn)
    Button main_tip_btn;

    @Bind(R.id.main_print_btn)
    TextView main_print_btn;
    @Bind(R.id.main_goon_btn)
    TextView main_goon_btn;

    @Bind(R.id.main_tip_lay)
    RelativeLayout main_tip_lay;
    @Bind(R.id.main_scan_lay)
    RelativeLayout main_scan_lay;
    @Bind(R.id.header_thumb)
    RelativeLayout header_thumb;
    @Bind(R.id.main_bottom)
    LinearLayout main_bottom;

    @Bind(R.id.scan_type_cloud)
    ScrollView scan_type_cloud;
    @Bind(R.id.scan_type_mall)
    RelativeLayout scan_type_mall;
    @Bind(R.id.recycle_list)
    RecyclerView recycle_list;

    @Bind(R.id.cloud_itemid)
    TextView cloud_itemid;
    @Bind(R.id.cloud_itemname)
    TextView cloud_itemname;
    @Bind(R.id.cloud_itemcount)
    TextView cloud_itemcount;
    @Bind(R.id.cloud_itemweight)
    TextView cloud_itemweight;
    @Bind(R.id.cloud_itemvolume)
    TextView cloud_itemvolume;
    @Bind(R.id.cloud_itemtype)
    TextView cloud_itemtype;
    @Bind(R.id.cloud_itemyadd)
    TextView cloud_itemyadd;
    @Bind(R.id.cloud_itemytel)
    TextView cloud_itemytel;
    @Bind(R.id.cloud_itemyname)
    TextView cloud_itemyname;
    @Bind(R.id.cloud_itemhadd)
    TextView cloud_itemhadd;
    @Bind(R.id.cloud_itemhtel)
    TextView cloud_itemhtel;
    @Bind(R.id.cloud_itemhname)
    TextView cloud_itemhname;

    SharedPreferences sp;
    QueryPresent present;
    Utils util;
    EvaluateItemAdapter adapter;
    ArrayList<WDTResponseContentItem> data;
    String code;
    String order_sn;
    String order_money;
    Boolean isInit = false;
    WandiantongOrderInfo cloudInfo;
    private int curType;
    ArrayList<VerifyRecord> history_data;
    VerifyRecordDao verifyRecordDao;

    private PopupWindow popupWindow;
    View popupWindowView;
    private boolean isNotReceive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(FragmentMain.this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }


    private void initView() {
        data = new ArrayList<>();
        adapter = new EvaluateItemAdapter(data, context);
        popupWindowView = View.inflate(getContext(), R.layout.pop_menu, null);
        recycle_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(adapter, recycle_list);
        //     recycle_list.setEmptyView(empty_lay);
        recycle_list.setAdapter(animatorAdapter);
        int spacingInPixels = 8;
        recycle_list.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        tip_text.setText(getResources().getString(R.string.scan_tip));
        RxView.clicks(header_exit).subscribe(s -> listener.Exit());
        RxView.clicks(header_thumb).subscribe(s -> ShowPopupWindow(header_thumb));

        RxView.clicks(main_print_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> PrintAgain());
        RxView.clicks(main_goon_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> Goon());
        RxView.clicks(main_tip_btn).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> listener.gotoPay(order_sn, order_money));

    }

    private void Goon() {
        showWaitDialog("请稍等");
        main_bottom.postDelayed(() -> {
            hideWaitDialog();
        }, 2000);
        listener.startScan();
        scan_type_cloud.setVisibility(View.GONE);
        main_bottom.setVisibility(View.GONE);
        main_tip_lay.setVisibility(View.VISIBLE);
    }

    private void PrintAgain() {
        listener.closeScanThenPrint();
    }

    private void initDate() {
        util = Utils.getInstance();
        sp = getContext().getSharedPreferences(COOKIE_KEY, 0);
        present = QueryPresent.getInstance(context);
        present.initRetrofit(Constant.URL_WANDIAN, false);
        present.setView(FragmentMain.this);
        executor.execute(() -> {
            DaoSession daoSession = ((HeApplication) getActivity().getApplication()).getDaoSession();
            verifyRecordDao = daoSession.getVerifyRecordDao();
            history_data = new ArrayList<>();
            history_data.addAll(verifyRecordDao.loadAll());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            String now = sdf.format(new Date());
            long dec = util.getTodayZero();
//        KLog.v("history_data"+history_data.size()+"now="+now+"---"+util.ValidateFormat(now)+"---"+util.ValidateFormat(history_data.get(0).getDate())+"----"+dec);
            for (VerifyRecord verifyRecord_ : history_data) {
                if (util.ValidateFormat(verifyRecord_.getDate()) < dec) {
                    verifyRecordDao.delete(verifyRecord_);
                    KLog.v("DELETE one" + verifyRecord_.getDate());
                    //   return;
                }
            }
            KLog.v(sp.getString("img_path", ""));

            if (sp.getString("img_path", "").equals("")) {
                Constant.logo = BitmapFactory.decodeResource(getResources(), R.drawable.print_logo);
            } else {
                Constant.logo = util.createLogo(getContext(), (sp.getString("img_path", "")));
            }
        });
    }

    private void ShowPopupWindow(View view) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setAnimationStyle(R.style.AnimationAlphaFade);
            ColorDrawable dw = new ColorDrawable(0xffffffff);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(() -> backgroundAlpha(1.0f));
            ArrayList<String> menu_data = new ArrayList<>();
            menu_data.add("打印logo");
            menu_data.add("核销记录");
            ArrayAdapter<String> menu_adapter;
            menu_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_lay_item, menu_data);
            ListView listView = (ListView) popupWindowView.findViewById(R.id.menu_list);
            listView.setAdapter(menu_adapter);

            listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                switch (i) {
                    case 0:
                        listener.Photo();
                        break;
                    case 1:
                        listener.gotoHistory();
                        break;
                }
                popupWindow.dismiss();
            });
        }
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(view,
                Gravity.TOP | Gravity.RIGHT, 0, 100);

    }

    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = v;
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IMainListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    public void queryOrder(String code) {
        this.code = code;
        if (code.startsWith("RRS")) {
            scan_type_mall.setVisibility(View.GONE);
            curType = TYPE_CLOUD;
            present.initRetrofit(Constant.URL_RENRENSONG, false);
            HashMap<String, String> StringA1 = new HashMap<>();

            StringA1.put(Constant.ID, code);
            String sign1 = util.getSign(StringA1);
            StringA1.put(Constant.SIGN, sign1);
            present.SearchCloudOrder(StringA1.get(Constant.ID), StringA1.get(Constant.SIGN));
        } else if (code.startsWith("STTX")) {
            scan_type_cloud.setVisibility(View.GONE);
            scan_type_mall.setVisibility(View.GONE);
            curType = TYPE_TICKET;
            present.initRetrofit(Constant.URI_SHANGTONGTIANXIA, false);
            String[] codes = code.split("#");
            HashMap<String,String> StringAl=new HashMap<>();
            StringAl.put("sn_code",codes[1]);
            StringAl.put("memcode",codes[2]);
            String sign1=util.getSTTXSign(StringAl);
            StringAl.put(Constant.SIGN,sign1);

            Log.v("Hexiao","sign="+StringAl.get(Constant.SIGN)+"  memcode="+StringAl.get("memcode")+"   sn_code="+StringAl.get("sn_code"));

            present.SearchTicketOrder(StringAl.get(Constant.SIGN),StringAl.get("memcode"),StringAl.get("sn_code"));
        } else {
            scan_type_cloud.setVisibility(View.GONE);
            curType = TYPE_MALL;
            present.initRetrofit(Constant.URL_WANDIAN, false);
            present.SearchOrder(Constant.cookie.get("secret"), Constant.cookie.get("ucode"), code);
            /*scan_type_cloud.setVisibility(View.GONE);
            scan_type_mall.setVisibility(View.GONE);
            main_tip_tv.setText("不是有效的订单号");
            VToast.toast(context, "不是有效的订单号");*/
            return;
        }
    }

    @Override
    public void ResolveSearchOrderInfo(WandiantongRespInfo info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 2000);
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText("网络请求失败");
            // showDialog(QUERY_FAIL,"查询订单失败","网络请求无效!","重试","取消 ");
            VToast.toast(context, "网络错误");
            return;
        }
        //setEmptyStatus(false);
        if (!isInit) {
            main_scan_lay.setVisibility(View.VISIBLE);
            main_tip_lay.setVisibility(View.GONE);
            main_bottom.setVisibility(View.VISIBLE);
            main_goon_btn.setVisibility(View.GONE);
            main_print_btn.setVisibility(View.GONE);
            isInit = true;
        }
        if (info.getErrcode().equals(SUCCESS)) {
            scan_type_cloud.setVisibility(View.GONE);
            scan_type_mall.setVisibility(View.VISIBLE);
            data.clear();
            data.addAll(info.getContent().getLists());
            adapter.notifyDataSetChanged();
            isInit = true;
            showWaitDialog("正在核销订单");
            present.initRetrofit(Constant.URL_WANDIAN, false);
            present.ConfirmOrder(Constant.cookie.get("secret"), Constant.cookie.get("ucode"), info.getContent().getCode(), "000");
        } else {
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            //VToast.toast(context,info.getErrmsg());
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText(info.getErrmsg());
        }
    }

    /**
     * 核销人人送订单
     */
    public void ConfirmCloudOrder() {
        showWaitDialog("正在核销订单");
        present.initRetrofit(Constant.URL_RENRENSONG, false);
        HashMap<String, String> StringA1 = new HashMap<>();
        StringA1.put(Constant.ID, code);
        String sign1 = util.getSign(StringA1);
        StringA1.put(Constant.SIGN, sign1);
        present.ConfirmCloudOrder(StringA1.get(Constant.ID), StringA1.get(Constant.SIGN));
    }

    @Override
    protected void confirm(int type, DialogInterface dia) {
        super.confirm(type, dia);
        queryOrder(code);
    }

    @Override
    protected void cancel(int type, DialogInterface dia) {
        super.cancel(type, dia);
    }

    @Override
    public void ResolveConfirmOrderInfo(WandiantongRespInfo info) {
        hideWaitDialog();
        showWaitDialog("请稍等");
        main_bottom.postDelayed(() -> {
            hideWaitDialog();
            listener.startScan();
        }, 1000);
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            showDialog(QUERY_FAIL, "核销失败", "网络请求无效!", "重试", "取消 ");
            //VToast.toast(context, "网络错误");
            return;
        }
        VToast.toast(context, info.getErrmsg());
        if (info.getErrcode().equals(SUCCESS)) {
            main_tip_iv.setImageResource(R.drawable.success_tip);
            main_tip_tv.setText(info.getErrmsg());
        } else {
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText(info.getErrmsg());
        }
    }

    private void insertRecord(WandiantongOrderInfo info) {
        Boolean isExist = false;
        for (VerifyRecord verifyRecord_ : history_data) {
            if (info.getId().equals(verifyRecord_.getItem_id())) {
                isExist = true;
                break;
            }
        }
        if (isExist) {
            return;
        }

        Gson gson = new Gson();
        String g_str = gson.toJson(info);

        VerifyRecord verifyRecord = gson.fromJson(g_str, VerifyRecord.class);
        verifyRecord.setDate(currentDate("yyyy-MM-dd HH:mm:ss"));
        verifyRecord.setItem_id(info.getId());

        verifyRecordDao.insert(verifyRecord);
        history_data.add(verifyRecord);
    }

    @Override
    public void ResolveSearchCloudOrderInfo(WandiantongRespInfo info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            //setEmptyStatus(true);
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText("网络请求失败");
            //showDialog(QUERY_FAIL,"查询订单失败","网络请求无效!","重试","取消 ");
            VToast.toast(context, "网络错误");
            return;
        }
        //setEmptyStatus(false);
        if (!isInit) {
            main_scan_lay.setVisibility(View.VISIBLE);
            main_tip_lay.setVisibility(View.GONE);
            main_bottom.setVisibility(View.VISIBLE);
            main_goon_btn.setVisibility(View.GONE);
            main_print_btn.setVisibility(View.GONE);
            isInit = true;
        }

        if (info.getErrcode().equals(CLOUD_SUCCESS)) {
            scan_type_cloud.setVisibility(View.VISIBLE);
            scan_type_mall.setVisibility(View.GONE);
            main_tip_btn.setVisibility(View.GONE);

            cloudInfo = info.getContent();
            writeView(info.getContent());
            main_tip_tv.setText("查询成功");

            order_sn = info.getContent().getId();
            order_money = info.getContent().getMoney();

            isInit = true;

            if (info.getContent().getPaytype().equals("0")) {//线下支付
                if (info.getContent().getB_status().equals("0")) {//未受理
                    isNotReceive = true;
                    ConfirmCloudOrder();
                } else if (info.getContent().getB_status().equals("1")) {//已受理

                    if (info.getContent().getPay_status().equals("0")) {//未支付
                        main_tip_lay.setVisibility(View.GONE);
                        main_bottom.setVisibility(View.VISIBLE);
                        main_tip_tv.setText("订单需要支付才能继续核销");
                        if (!isNotReceive) {
                            main_print_btn.setVisibility(View.GONE);
                        }
                        isNotReceive = false;
                        main_goon_btn.setVisibility(View.VISIBLE);
                        main_tip_btn.setVisibility(View.VISIBLE);
                        listener.gotoPay(order_sn, order_money);
                    } else {//已支付

                        ConfirmCloudOrder();
                    }
                } else {
                    main_goon_btn.setVisibility(View.VISIBLE);
                    main_tip_tv.setText("此订单不能受理!");
                }
            } else if (info.getContent().getPaytype().equals("1")) {//到付
                main_tip_lay.setVisibility(View.GONE);
                main_bottom.setVisibility(View.VISIBLE);
                ConfirmCloudOrder();
            } else if (info.getContent().getPaytype().equals("2")) {//月结
                main_tip_lay.setVisibility(View.GONE);
                main_bottom.setVisibility(View.VISIBLE);
                ConfirmCloudOrder();
            } else {
                main_tip_lay.setVisibility(View.GONE);
                main_bottom.setVisibility(View.VISIBLE);
                ConfirmCloudOrder();
            }
        } else if (info.getErrcode().equals(CLOUD_EMPTY)) {
            ConfirmCloudOrder();
        } else {
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            if (info.getContent() != null) {
                scan_type_cloud.setVisibility(View.VISIBLE);
                writeView(info.getContent());
            } else {
                scan_type_cloud.setVisibility(View.GONE);
            }
            VToast.toast(context, info.getErrmsg());
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText(info.getErrmsg());
        }
    }

    private void writeView(WandiantongOrderInfo content) {
        cloud_itemid.setText(content.getId());
        cloud_itemcount.setText(content.getItem_total());
        cloud_itemname.setText(content.getItem_name());
        cloud_itemweight.setText(content.getItem_weight());
        cloud_itemvolume.setText(content.getItem_volume());
        cloud_itemhname.setText(content.getH_name());
        cloud_itemhadd.setText(content.getH_address());
        cloud_itemhtel.setText(content.getH_tel());
        cloud_itemyname.setText(content.getY_name());
        cloud_itemyadd.setText(content.getY_address());
        cloud_itemytel.setText(content.getY_tel());
    }


    /**
     * 插入记并打印
     */
    public void InsertRecordAndPrint() {
        main_goon_btn.setVisibility(View.VISIBLE);
        main_print_btn.setVisibility(View.VISIBLE);

        listener.closeScanThenPrint();
        insertRecord(cloudInfo);
    }

    @Override
    public void ResolveConfirmCloudOrderInfo(WandiantongRespInfo info) {
        hideWaitDialog();

        KLog.v(info.toString());
        if (info.getErrcode() == null) {
            showDialog(QUERY_FAIL, "核销失败", "网络请求无效!", "重试", "取消 ");
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            return;
        }
        VToast.toast(context, info.getErrmsg());
        if (info.getErrcode().equals(CLOUD_SUCCESS)) {
            main_tip_iv.setImageResource(R.drawable.success_tip);

            main_tip_btn.setVisibility(View.GONE);
            if (info.getIs_print().equals("1")) {
                queryOrder(code);
                main_tip_tv.setText("受理成功");
                main_print_btn.setVisibility(View.VISIBLE);
                InsertRecordAndPrint();
            } else {
                main_goon_btn.setVisibility(View.VISIBLE);
                main_tip_tv.setText("核销成功");
            }

        } else {
            main_goon_btn.setVisibility(View.GONE);
            main_print_btn.setVisibility(View.GONE);
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText(info.getErrmsg());
        }
    }

    @Override
    public void ResolveSearchTicketOrderInfo(WandiantongRespInfo info) {
        hideWaitDialog();
        KLog.v(info.toString());
        if (info.getErrmsg()==null){
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            main_tip_tv.setText("网络请求失败");
            //showDialog(QUERY_FAIL,"查询订单失败","网络请求无效!","重试","取消 ");
            VToast.toast(context, "网络错误");
            return;
        }
        if (!isInit) {
            main_scan_lay.setVisibility(View.VISIBLE);
            main_tip_lay.setVisibility(View.GONE);
            main_bottom.setVisibility(View.VISIBLE);
            main_goon_btn.setVisibility(View.GONE);
            main_print_btn.setVisibility(View.GONE);
            isInit = true;
        }
        if (info.getErrcode().equals(SUCCESS)){
            scan_type_cloud.setVisibility(View.GONE);
            scan_type_mall.setVisibility(View.GONE);
            main_tip_btn.setVisibility(View.GONE);
            isInit=true;
            main_tip_iv.setImageResource(R.drawable.success_tip);
            main_tip_tv.setText(info.getErrmsg());
            main_goon_btn.setVisibility(View.VISIBLE);
            main_tip_tv.setText("核销成功");
        } else {
//            VToast.toast(getContext(),info.getErrmsg());
            main_tip_iv.setImageResource(R.drawable.fail_tip);
            String tip="卡券失效";
//            main_tip_tv.setText(info.getErrmsg());
            main_tip_tv.setText(tip);
            main_tip_btn.setVisibility(View.GONE);
            main_goon_btn.setVisibility(View.GONE);
            main_print_btn.setVisibility(View.GONE);
            showWaitDialog("请稍等");
            main_bottom.postDelayed(() -> {
                hideWaitDialog();
                listener.startScan();
            }, 1000);
        }
        hideWaitDialog();
    }


    @Override
    public void ResolveConfirmOrderPay(WDTResponseCode info) {

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildPosition(view) == 0)
                outRect.top = space;
        }
    }

    public void print() {
        listener.print(code);
    }

    public interface IMainListener {
        void startScan();

        void Exit();

        void closeScanThenPrint();

        void print(String info);

        void Photo();

        void gotoHistory();

        void gotoPay(String order_id, String cash);
    }
}
