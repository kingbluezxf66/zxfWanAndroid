package com.example.zxf.myapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.HomeArticleAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.DataBaseHelper;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.HomeAticleBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.ui.activity.BrowserActivity;
import com.example.zxf.myapp.widget.CustomLoadMoreView;
import com.example.zxf.myapp.widget.MyLinearlayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.realm.RealmAsyncTask;

public class    SearchResultFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.rv_search_result)
    RecyclerView rvSearchResult;
    private int page;
    private static SearchResultFragment searchResultFragment;
    private List<HomeAticleBean.DatasBean> searchResultList;
    private HomeArticleAdapter homeArticleAdapter;

    private String keyword;
    private RealmAsyncTask transaction;
    @Inject
    DataBaseHelper dataBaseHelper;

    public static SearchResultFragment getInstance(String keyword) {
        //注意此时不能使用单例模式
        searchResultFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        searchResultFragment.setArguments(bundle);
        return searchResultFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        MyApplication.get(getActivity()).getDatabaseHelperComponent().inject(this);
        keyword = getArguments().getString("keyword");
        Log.i(TAG, "onCreateView: " + keyword);
        rvSearchResult.setLayoutManager(new MyLinearlayoutManager(getActivity()));
        rvSearchResult.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        searchResultList = new ArrayList<>();
        homeArticleAdapter = new HomeArticleAdapter(getActivity(), searchResultList);
        homeArticleAdapter.setOnLoadMoreListener(this, rvSearchResult);
        homeArticleAdapter.setLoadMoreView(new CustomLoadMoreView());
        rvSearchResult.setAdapter(homeArticleAdapter);
        homeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra("title", searchResultList.get(position).getTitle());
                intent.putExtra("url", searchResultList.get(position).getLink());
                startActivity(intent);
            }
        });
        getSearchResult(keyword);
    }

    private void getSearchResult(String keyword) {
        page = 0;
        HttpObservable.getObservable(RetrofitHelper.dataManager.getSearchResult(page, keyword)).subscribe(new HttpObserver<HomeAticleBean>(getActivity(), null) {
            @Override
            public void onSuccess(HomeAticleBean homeAticleBean) {
                //将搜索记录保存在数据库中
                dataBaseHelper.saveKeyWords(keyword);
                if (homeAticleBean != null && homeAticleBean.getDatas().size() > 0) {
                    searchResultList = homeAticleBean.getDatas();
                    homeArticleAdapter.setNewData(searchResultList);
                    page++;
                    homeArticleAdapter.setEnableLoadMore(true);
                } else {
                    //设置空布局
                    homeArticleAdapter.setEmptyView(R.layout.emptyview);
                }
            }
        });
    }


    @Override
    public void onLoadMoreRequested() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getSearchResult(page, keyword)).subscribe(new HttpObserver<HomeAticleBean>(getActivity(), null) {
            @Override
            public void onSuccess(HomeAticleBean homeAticleBean) {
                //将搜索记录保存在数据库中
                if (homeAticleBean != null && homeAticleBean.getDatas().size() > 0) {
                    homeArticleAdapter.addData(homeAticleBean.getDatas());
                    homeArticleAdapter.notifyDataSetChanged();
                    homeArticleAdapter.loadMoreComplete();
                    page++;
                } else {
                    homeArticleAdapter.loadMoreEnd();
                }
            }
        });
    }
}
