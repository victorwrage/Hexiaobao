package com.zdv.hexiaobao.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.zdv.hexiaobao.HeApplication;
import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.adapter.HistoryItemAdapter;
import com.zdv.hexiaobao.bean.DaoSession;
import com.zdv.hexiaobao.bean.VerifyRecord;
import com.zdv.hexiaobao.bean.VerifyRecordDao;
import com.zdv.hexiaobao.cus_view.RecyclerViewWithEmpty;
import com.zdv.hexiaobao.present.QueryPresent;
import com.zdv.hexiaobao.utils.Utils;
import com.zdv.hexiaobao.view.IView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;


public class FragmentHistory extends BaseFragment implements IView{
    QueryPresent present;
    Utils util;
    SharedPreferences sp;
    ArrayList<VerifyRecord> a_data;
    @Bind(R.id.history_unavailable_list)
    RecyclerViewWithEmpty history_unavailable_list;

    @Bind(R.id.empty_lay)
    RelativeLayout empty_lay;

    @Bind(R.id.header_thumb)
    RelativeLayout header_thumb;
    @Bind(R.id.header_exit)
    RelativeLayout header_exit;
    @Bind(R.id.header_title)
    TextView header_title;

    HistoryItemAdapter a_adapter;
    @Bind(R.id.empty_iv)
    ImageView empty_iv;
    @Bind(R.id.empty_tv)
    TextView empty_tv;
    @Bind(R.id.history_total_tv)
    TextView history_total_tv;

    VerifyRecordDao verifyRecordDao;
    private IHistoryListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(FragmentHistory.this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDate();
        initView();
    }

    private void initDate() {
        util = Utils.getInstance();
        present = QueryPresent.getInstance(getContext());
        present.setView(FragmentHistory.this);
        sp = getContext().getSharedPreferences(COOKIE_KEY, Context.MODE_PRIVATE);
        a_data = new ArrayList<>();
        DaoSession daoSession = ((HeApplication) getActivity().getApplication()).getDaoSession();
        verifyRecordDao = daoSession.getVerifyRecordDao();
        a_data.addAll(verifyRecordDao.loadAll());

    }

    private void initView() {

        RxView.clicks(header_exit).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s ->  Back());

        header_title.setText("核销记录");
        header_thumb.setVisibility(View.GONE);
        history_total_tv.setText("今天核销量："+a_data.size()+"条记录");
        a_adapter = new HistoryItemAdapter(a_data,getContext());
        history_unavailable_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        AlphaAnimatorAdapter animatorApdapter = new AlphaAnimatorAdapter(a_adapter, history_unavailable_list);
        history_unavailable_list.setEmptyView(empty_lay);
        history_unavailable_list.setAdapter(animatorApdapter);

        setEmptyStatus(false);
        a_adapter.notifyDataSetChanged();

    }

    public void refreshState() {
        if(a_data!=null){
            a_data.clear();
            a_data.addAll(verifyRecordDao.loadAll());
            history_total_tv.setText("今天核销量："+a_data.size()+"条记录");
            setEmptyStatus(false);
            a_adapter.notifyDataSetChanged();
        }
    }

    public void Back() {
        listener.gotoMain();
    }

    protected void setEmptyStatus(boolean isOffLine) {

        if (isOffLine) {
            empty_iv.setImageResource(R.drawable.netword_error);
            empty_tv.setText("(=^_^=)，粗错了，点我刷新试试~");
            empty_lay.setEnabled(true);
            RxView.clicks(empty_iv).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(s -> emptyClick());
        } else {
            empty_lay.setEnabled(false);
            empty_iv.setImageResource(R.drawable.smile);
            empty_tv.setText("没有记录");
        }
    }

    protected void emptyClick() {
        showWaitDialog("正在努力加载...");
        fetchFromNetWork();
    }

    private void fetchFromNetWork() {

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (IHistoryListener) context;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }


    public interface IHistoryListener {
        void gotoMain();
    }
}
