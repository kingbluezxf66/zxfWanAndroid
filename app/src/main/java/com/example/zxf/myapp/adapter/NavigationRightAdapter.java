package com.example.zxf.myapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.NavigationBean;

import java.util.List;

public class NavigationRightAdapter extends BaseQuickAdapter<NavigationBean.ArticlesBean,BaseViewHolder> {
    public NavigationRightAdapter(@Nullable List<NavigationBean.ArticlesBean> data) {
        super(R.layout.item_navigation_right,data);

    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationBean.ArticlesBean item) {
        helper.setText(R.id.tv_name,item.getTitle());
    }
}
