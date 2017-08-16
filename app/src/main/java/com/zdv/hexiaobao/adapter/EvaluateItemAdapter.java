package com.zdv.hexiaobao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdv.hexiaobao.R;
import com.zdv.hexiaobao.bean.WDTResponseContentItem;

import java.util.ArrayList;

/**
 * Info: 评价
 * Created by xiaoyl
 * 创建时间:2017/5/17 14:28
 */
public class EvaluateItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<WDTResponseContentItem> items;

    public EvaluateItemAdapter(ArrayList<WDTResponseContentItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int VIEW_TYPE) {

        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.merchant_item_lay, viewGroup,
                false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder_, int i) {
        EvaluateItemAdapter.MyViewHolder holder = (EvaluateItemAdapter.MyViewHolder) holder_;
        WDTResponseContentItem item = items.get(i);
        holder.name.setText(item.getName());
        holder.count.setText(item.getNumber());
        holder.unit.setText(item.getUnit());

        if(i%2==0){
            holder.name.setTextColor(context.getResources().getColor(R.color.hexiaobao_txt));
            holder.count.setTextColor(context.getResources().getColor(R.color.hexiaobao_txt));
            holder.unit.setTextColor(context.getResources().getColor(R.color.hexiaobao_txt));
            holder.merchant_lay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_dup));
        }else{
            holder.name.setTextColor(context.getResources().getColor(R.color.hexiaobao_btn_back));
            holder.count.setTextColor(context.getResources().getColor(R.color.hexiaobao_btn_back));
            holder.unit.setTextColor(context.getResources().getColor(R.color.hexiaobao_btn_back));
            holder.merchant_lay.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_dup2));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,count,unit;
        LinearLayout merchant_lay;
        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            unit = (TextView) view.findViewById(R.id.unit);
           // unit = (TextView) view.findViewById(R.id.unit);
            count = (TextView) view.findViewById(R.id.count);
            merchant_lay = (LinearLayout) view.findViewById(R.id.merchant_lay);
        //    name.setEllipsize(TextUtils.TruncateAt.valueOf("MARQUEE"));
          //  total = (TextView) view.findViewById(R.id.total);
        }
    }


}
