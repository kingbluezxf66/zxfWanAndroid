package com.example.zxf.myapp.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.model.SearchHistory;

import java.util.List;

public class SearchHistoryAdapter extends BaseQuickAdapter<SearchHistory,BaseViewHolder> {
    public SearchHistoryAdapter(@Nullable List<SearchHistory> data) {
        super(R.layout.item_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchHistory item) {
        helper.setText(R.id.tv_content,item.getKeyword()).addOnClickListener(R.id.iv_delete);
    }
}
