package com.example.zxf.myapp.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.HotSearchAdapter;
import com.example.zxf.myapp.adapter.SearchHistoryAdapter;
import com.example.zxf.myapp.base.BaseMVPFragment;
import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.helper.DataBaseHelper;
import com.example.zxf.myapp.model.HotKey;
import com.example.zxf.myapp.model.SearchHistory;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchFragment extends BaseMVPFragment<SearchContract.Presenter> implements SearchContract.SearchView {
    private static final String TAG = "kingbluezxf";
    @BindView(R.id.rv_hot_list)
    RecyclerView rvHotList;
    @BindView(R.id.rv_history_list)
    RecyclerView rvHistoryList;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    private List<HotKey> hotKeyList;
    private List<SearchHistory> searchHistoryList;
    private HotSearchAdapter hotSearchAdapter;
    private SearchHistoryAdapter searchHistoryAdapter;
    @Inject
    DataBaseHelper dataBaseHelper;

    /**
     * 静态内部类实现单例模式
     */
    private static class SearchFragmentHolder {
        private static final SearchFragment sInstance = new SearchFragment();
    }

    public static SearchFragment getInstance() {
        return SearchFragmentHolder.sInstance;
    }

    @NonNull
    @Override
    public BasePresenter initPresenter() {
        return new SearchPresenter(getActivity());
    }


    @Override
    public void getHotSuccess(List<HotKey> list) {
        hotKeyList = list;
        hotSearchAdapter.setNewData(hotKeyList);
        mPresenter.hotAdapterClick(hotSearchAdapter, hotKeyList);
    }

    @Override
    public void deletePositive() {
        dataBaseHelper.deleteAllSearchHistory();
        searchHistoryList.clear();
        textView2.setVisibility(View.GONE);
        ivDelete.setVisibility(View.GONE);
    }

    @Override
    public void deleteSingleSeachHistory(String key) {
        dataBaseHelper.deleteSingleSeachHistory(key);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        MyApplication.get(getActivity()).getDatabaseHelperComponent().inject(this);
        initRecycleViewAdapter();
        searchHistoryList = dataBaseHelper.querySearchHistory();
        mPresenter.searchAdapterClick(searchHistoryAdapter, searchHistoryList);
        mPresenter.searchAdapterItemClick(searchHistoryAdapter, searchHistoryList);
        mPresenter.getHotSearch();
        Log.i(TAG, "getSearchHisory: " + searchHistoryList.size());
        if (searchHistoryList != null && searchHistoryList.size() > 0) {
            searchHistoryAdapter.setNewData(searchHistoryList);
            textView2.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            textView2.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
        }


    }

    private void initRecycleViewAdapter() {
        hotKeyList = new ArrayList<>();
        searchHistoryList = new ArrayList<>();
        rvHotList.setLayoutManager(new FlexboxLayoutManager(getActivity()));
        hotSearchAdapter = new HotSearchAdapter(hotKeyList);
        rvHotList.setAdapter(hotSearchAdapter);
        rvHistoryList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchHistoryAdapter = new SearchHistoryAdapter(searchHistoryList);
        rvHistoryList.setAdapter(searchHistoryAdapter);

    }
    //删除所有的数据

    @OnClick(R.id.iv_delete)
    public void deleteAllHistory() {
        mPresenter.deleteAllHistory();
    }

}
