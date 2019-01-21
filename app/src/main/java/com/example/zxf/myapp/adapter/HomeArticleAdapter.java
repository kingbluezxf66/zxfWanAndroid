package com.example.zxf.myapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.HomeAticleBean;

import java.util.List;

public class HomeArticleAdapter extends BaseQuickAdapter<HomeAticleBean.DatasBean, BaseViewHolder> {

    private Context context;

    public HomeArticleAdapter(Context context, @Nullable List<HomeAticleBean.DatasBean> data) {
        super(R.layout.item_home_article, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeAticleBean.DatasBean item) {
        helper.setText(R.id.tv_super_name, item.getSuperChapterName())
                .setText(R.id.tv_name, item.getChapterName())
                .setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_auther, item.getAuthor())
                .setText(R.id.tv_time, item.getNiceDate())
                .addOnClickListener(R.id.iv_collect);
        boolean isCollect = item.isCollect();
        ImageView imageView = helper.getView(R.id.iv_collect);
        if (isCollect) {
            imageView.setImageResource(R.mipmap.ic_collect);
        } else {
            imageView.setImageResource(R.mipmap.ic_un_collect);
        }
    }
}
