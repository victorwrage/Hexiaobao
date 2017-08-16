package com.zdv.hexiaobao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.bean.VerifyRecord;
import com.zdv.hexiaobao.utils.Utils;

import java.util.ArrayList;

/**
 * Info: 消息
 * Created by xiaoyl
 * 创建时间:2017/8/07 10:15
 */
public class HistoryItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<VerifyRecord> items;
    Utils util;

    public HistoryItemAdapter(ArrayList<VerifyRecord> items, Context context) {
        this.items = items;
        this.context = context;
        util = Utils.getInstance();


    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.histpry_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        MyViewHolder holder = (MyViewHolder) holder_;
        VerifyRecord item = items.get(i);


        holder.order_item_count.setText("共"+item.getItem_total()+"件货物");
        holder.order_item_weight.setText(item.getItem_weight());
        holder.order_item_volume.setText(item.getItem_volume());
        holder.order_item_type.setText(item.getItem_type());
        holder.order_express_id.setText("ID:"+item.getItem_id()+"    核销时间:"+item.getDate());
        holder.order_item_name.setText(item.getItem_name());
        holder.order_item_y_name.setText(item.getY_name());
        holder.order_item_y_add.setText(item.getY_address());
        holder.order_item_y_phone.setText(item.getY_tel());
        holder.order_item_h_name.setText(item.getH_name());
        holder.order_item_h_add.setText(item.getH_address());
        holder.order_item_h_phone.setText(item.getH_tel());
        holder.order_item_h_date.setText(item.getPut_time());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView order_item_count, order_item_weight, order_item_volume,order_item_type
,order_express_id,order_item_name,order_item_y_name,order_item_y_add,order_item_y_phone,order_item_h_name,order_item_h_add,order_item_h_phone,order_item_h_date;

        public MyViewHolder(View view) {
            super(view);
            order_item_count = (TextView) view.findViewById(R.id.order_item_count);
            order_item_weight = (TextView) view.findViewById(R.id.order_item_weight);
            order_item_volume = (TextView) view.findViewById(R.id.order_item_volume);
            order_item_type = (TextView) view.findViewById(R.id.order_item_type);
            order_express_id = (TextView) view.findViewById(R.id.order_express_id);
            order_item_name = (TextView) view.findViewById(R.id.order_item_name);
            order_item_y_name = (TextView) view.findViewById(R.id.order_item_y_name);
            order_item_y_add = (TextView) view.findViewById(R.id.order_item_y_add);
            order_item_y_phone = (TextView) view.findViewById(R.id.order_item_y_phone);
            order_item_h_name = (TextView) view.findViewById(R.id.order_item_h_name);
            order_item_h_add = (TextView) view.findViewById(R.id.order_item_h_add);
            order_item_h_phone = (TextView) view.findViewById(R.id.order_item_h_phone);
            order_item_h_date = (TextView) view.findViewById(R.id.order_item_h_date);

        }
    }

}
