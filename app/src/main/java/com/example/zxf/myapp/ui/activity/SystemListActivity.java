package com.example.zxf.myapp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.HomeArticleAdapter;
import com.example.zxf.myapp.base.BaseActivity;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.HomeAticleBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.util.UiUtils;
import com.example.zxf.myapp.widget.CustomLoadMoreView;
import com.example.zxf.myapp.widget.MyLinearlayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SystemListActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SystemListActivity";
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.flotButton)
    FloatingActionButton flotButton;
    private HomeArticleAdapter homeArticleAdapter;
    private List<HomeAticleBean.DatasBean> homeAticleList;
    private int page = 0;
    private View emptyView;
    private MyLinearlayoutManager linearlayoutManager;
    private String id;
    private String title;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        initRecycleAndAdapter();
        onRefresh();
    }


    private void initDataList() {
        page = 0;
        HttpObservable.getObservable(RetrofitHelper.dataManager.getSystemSecondList(page,id)).subscribe(new HttpObserver<HomeAticleBean>(this,swipeLayout) {
            @Override
            public void onSuccess(HomeAticleBean homeAticleBean) {
                if (homeAticleBean.getDatas() != null && homeAticleBean.getDatas().size() > 0) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycleAndAdapter() {
        View headView = getLayoutInflater().inflate(R.layout.layout_toolbar, (ViewGroup) rvList.getParent(), false);
        Toolbar toolbar = headView.findViewById(R.id.toolbar);
        flotButton.hide();
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.BLACK);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(decoration);
        linearlayoutManager = new MyLinearlayoutManager(this);
        rvList.setLayoutManager(linearlayoutManager);
        homeAticleList = new ArrayList<>();
        homeArticleAdapter = new HomeArticleAdapter(this, homeAticleList);
//        emptyView = getLayoutInflater().inflate(R.layout.emptyview, (ViewGroup) rvList.getParent(), false);
        homeArticleAdapter.setHeaderAndEmpty(true);
        homeArticleAdapter.addHeaderView(headView);
        homeArticleAdapter.setOnLoadMoreListener(this, rvList);
        homeArticleAdapter.setLoadMoreView(new CustomLoadMoreView());
        rvList.setAdapter(homeArticleAdapter);
//        toolbar.inflateMenu(R.menu.mainmenu);
        toolbar.setTitle(title);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        homeArticleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SystemListActivity.this, BrowserActivity.class);
                intent.putExtra("title", homeAticleList.get(position).getTitle());
                intent.putExtra("url", homeAticleList.get(position).getLink());
                startActivity(intent);
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
        initDataList();
    }

    @Override
    public void onLoadMoreRequested() {
        Log.i("kingbluezxf", "onLoadMoreRequested: " + page);
        HttpObservable.getObservable(RetrofitHelper.dataManager.getSystemSecondList(page,id)).subscribe(new HttpObserver<HomeAticleBean>(this,swipeLayout) {
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
