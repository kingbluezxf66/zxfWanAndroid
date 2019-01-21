package com.example.zxf.myapp.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.NavigationBean;

import java.util.List;

public class NavigationLeftAdapter extends BaseQuickAdapter<NavigationBean, BaseViewHolder> {
    private int selectPosition;
    private int theme;

    public NavigationLeftAdapter(@Nullable List<NavigationBean> data) {
        super(R.layout.item_navigation_left, data);
        SPUtils spUtils = SPUtils.getInstance("theme");
        theme = spUtils.getInt("theme");
    }

    public void setPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        LogUtils.e("selectPosition" + selectPosition);
        notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void convert(BaseViewHolder helper, NavigationBean item) {
        helper.setText(R.id.tv_name, item.getName());
        TextView tvName = helper.getView(R.id.tv_name);
        if (selectPosition == helper.getAdapterPosition()) {
            if(theme==0){
                tvName.setBackgroundColor(Color.parseColor("#f3f3f3"));
                tvName.setTextColor(Color.parseColor("#017747"));
            }else{
                tvName.setBackgroundColor(Color.parseColor("#000000"));
                tvName.setTextColor(Color.parseColor("#c5cbcb"));
            }

        } else {
            if(theme==0){
                tvName.setBackgroundColor(Color.parseColor("#ffffff"));
                tvName.setTextColor(Color.parseColor("#262626"));
            }else{
                tvName.setBackgroundColor(Color.parseColor("#2b2b2b"));
                tvName.setTextColor(Color.parseColor("#999999"));
            }

        }
    }
}
