package com.example.zxf.myapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.HomeArticleAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.BannerBean;
import com.example.zxf.myapp.model.HomeAticleBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.ui.activity.BrowserActivity;
import com.example.zxf.myapp.util.UiUtils;
import com.example.zxf.myapp.widget.CustomLoadMoreView;
import com.example.zxf.myapp.widget.GlideImageLoader;
import com.example.zxf.myapp.widget.MyLinearlayoutManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    Banner banner;

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.flotButton)
    FloatingActionButton flotButton;
    private List<BannerBean> bannerList;
    private List<String> images;
    private List<String> titles;
    private HomeArticleAdapter homeArticleAdapter;
    private List<HomeAticleBean.DatasBean> homeAticleList;
    private int page = 0;
    private View emptyView;
    private MyLinearlayoutManager linearlayoutManager;
    private ImageView ivCollect;
    private boolean isCollect;

    public static HomeFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        initRecycleAndAdapter();
        addBannerHeander();
        onRefresh();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycleAndAdapter() {
        flotButton.hide();
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.BLACK);
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(decoration);
        linearlayoutManager = new MyLinearlayoutManager(getActivity());
        rvList.setLayoutManager(linearlayoutManager);
        homeAticleList = new ArrayList<>();
        homeArticleAdapter = new HomeArticleAdapter(getActivity(), homeAticleList);
//        emptyView = getLayoutInflater().inflate(R.layout.emptyview, (ViewGroup) rvList.getParent(), false);
        homeArticleAdapter.setHeaderAndEmpty(true);
        homeArticleAdapter.setOnLoadMoreListener(this, rvList);
        homeArticleAdapter.setLoadMoreView(new CustomLoadMoreView());
        rvList.setAdapter(homeArticleAdapter);
        homeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), BrowserActivity.class);
                intent.putExtra("title", homeAticleList.get(position).getTitle());
                intent.putExtra("url", homeAticleList.get(position).getLink());
                startActivity(intent);
            }
        });
        homeArticleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String id = homeAticleList.get(position).getId();
                isCollect = homeAticleList.get(position).isCollect();
                ivCollect = view.findViewById(R.id.iv_collect);
                if (isCollect) {
                    collecArticle(id, false, position);
                } else {
                    collecArticle(id, true, position);
                }
            }
        });
        rvList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                RecyclerView.LayoutManager layoutManager = rvList.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    int firstVisibleItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstVisibleItemPosition > 3) {
                        flotButton.show();
                    } else {
                        flotButton.hide();
                    }
                }
            }
        });
        flotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flotButton.hide();
                UiUtils.MoveToPosition(linearlayoutManager, 0);
            }
        });

    }


    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        homeArticleAdapter.setEnableLoadMore(false);
        initBanner();
        initArticle();
    }

    private void initArticle() {
        page = 0;
        HttpObservable.getObservable(RetrofitHelper.dataManager.getArticleList(page)).subscribe(new HttpObserver<HomeAticleBean>(getActivity(),swipeLayout) {
            @Override
            public void onSuccess(HomeAticleBean homeAticleBean) {
                if (homeAticleBean.getDatas() != null && homeAticleBean.getDatas().size() > 0) {
                    swipeLayout.setRefreshing(false);
                    homeAticleList = homeAticleBean.getDatas();
                    homeArticleAdapter.setNewData(homeAticleList);
                    homeArticleAdapter.notifyDataSetChanged();
                    page++;
                    homeArticleAdapter.setEnableLoadMore(true);
                    //调用获取更多
                } else {
                    //设置空布局
                    homeArticleAdapter.setEmptyView(R.layout.emptyview);
                }
            }
        });
    }

    /**
     * 收藏文章
     *
     * @param id
     */
    private void collecArticle(String id, final boolean collect, final int position) {
        HttpObservable.getObservable(RetrofitHelper.dataManager.collectArticle(id)).subscribe(new HttpObserver(getActivity(),swipeLayout) {
            @Override
            public void onSuccess(Object o) {
                HomeAticleBean.DatasBean datasBean = homeAticleList.get(position);
                if (collect) {
                    ivCollect.setImageResource(R.mipmap.ic_collect);
                    ToastUtils.showShort("收藏成功");
                    datasBean.setCollect(true);
                } else {
                    ivCollect.setImageResource(R.mipmap.ic_un_collect);
                    ToastUtils.showShort("取消成功");
                    datasBean.setCollect(false);
                }
                homeArticleAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addBannerHeander() {
        View headView = getLayoutInflater().inflate(R.layout.head_baner, (ViewGroup) rvList.getParent(), false);
        banner = headView.findViewById(R.id.mybanner);
        homeArticleAdapter.addHeaderView(headView);
        images = new ArrayList<>();
        titles = new ArrayList<>();
        bannerList = new ArrayList<>();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent in = new Intent(getActivity(), BrowserActivity.class);
                Log.i("kingbluezxf", "onCreate: " + bannerList.get(position).getUrl());
                in.putExtra("url", bannerList.get(position).getUrl());
                in.putExtra("title", bannerList.get(position).getTitle());
                startActivity(in);
            }
        });
    }

    private void initBanner() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getBannerData())
                .subscribe(new HttpObserver<List<BannerBean>>(getActivity(),swipeLayout) {
                    @Override
                    public void onSuccess(List<BannerBean> bannerBeans) {
                        if (bannerBeans != null && bannerBeans.size() > 0) {
                            images.clear();
                            titles.clear();
                            bannerList.clear();
                            bannerList = bannerBeans;
                            for (BannerBean dataBean : bannerList) {
                                images.add(dataBean.getImagePath());
                                titles.add(dataBean.getTitle());
                            }
                            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE).
                                    setImages(images).setImageLoader(new GlideImageLoader()).
                                    setBannerTitles(titles).start();
                        }
                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {
        Log.i("kingbluezxf", "onLoadMoreRequested: " + page);
        HttpObservable.getObservable(RetrofitHelper.dataManager.getArticleList(page)).subscribe(new HttpObserver<HomeAticleBean>(getActivity(),swipeLayout) {
            @Override
            public void onSuccess(HomeAticleBean homeAticleBean) {
                if (homeAticleBean.getDatas() != null && homeAticleBean.getDatas().size() > 0) {
                    List<HomeAticleBean.DatasBean> articleList = homeAticleBean.getDatas();
                    homeArticleAdapter.addData(homeAticleList);
                    homeArticleAdapter.notifyDataSetChanged();
                    page++;
                    homeArticleAdapter.loadMoreComplete();
                    //调用获取更多
                } else {
                    homeArticleAdapter.loadMoreEnd();
                }
            }
        });
    }

}
