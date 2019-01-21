package com.example.zxf.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.NavigationRightAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.model.NavigationBean;
import com.example.zxf.myapp.ui.activity.BrowserActivity;
import com.google.android.flexbox.FlexboxLayoutManager;

import java.util.List;

import butterknife.BindView;

public class NavigationRightFragment extends BaseFragment {
    @BindView(R.id.rv_search_result)
    RecyclerView recyclerView;
    private List<NavigationBean.ArticlesBean> navigationRightList;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        initRecycleAndAdapter();
    }

    private void initRecycleAndAdapter() {
        List<NavigationBean.ArticlesBean> navigationRightList = getArguments().getParcelableArrayList("rightData");
        recyclerView.setLayoutManager(new FlexboxLayoutManager(getActivity()));
        NavigationRightAdapter navigationRightAdapter = new NavigationRightAdapter(navigationRightList);
        recyclerView.setAdapter(navigationRightAdapter);
        navigationRightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(),BrowserActivity.class);
                intent.putExtra("title",navigationRightList.get(position).getTitle());
                intent.putExtra("url",navigationRightList.get(position).getLink());
                startActivity(intent);
            }
        });
    }
}
