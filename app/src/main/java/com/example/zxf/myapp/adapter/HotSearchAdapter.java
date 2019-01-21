package com.example.zxf.myapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.HotKey;

import java.util.List;

public class HotSearchAdapter extends BaseQuickAdapter<HotKey,BaseViewHolder> {
    public HotSearchAdapter(@Nullable List<HotKey> data) {
        super(R.layout.item_hot_search,data);

    }

    @Override
    protected void convert(BaseViewHolder helper, HotKey item) {
        helper.setText(R.id.tv_name,item.getName());
    }
}
