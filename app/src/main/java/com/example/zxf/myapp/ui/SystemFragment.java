package com.example.zxf.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.SystemMultiAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.SystemBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.ui.activity.SystemListActivity;
import com.example.zxf.myapp.widget.MyLinearlayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public class SystemFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.flotButton)
    FloatingActionButton flotButton;
    private Disposable disposable;
    private List<MultiItemEntity> systemList;
    private SystemMultiAdapter systemMultiAdapter;


    public static SystemFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        SystemFragment fragment = new SystemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        initRecycleViewAdapter();
        getSystemList();
    }

    private void initRecycleViewAdapter() {
        swipeLayout.setEnabled(false);
        flotButton.hide();
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(decoration);
        MyLinearlayoutManager linearlayoutManager = new MyLinearlayoutManager(getActivity());
        rvList.setLayoutManager(linearlayoutManager);
        systemList = new ArrayList<>();
        systemMultiAdapter = new SystemMultiAdapter(systemList);
        rvList.setAdapter(systemMultiAdapter);
        systemMultiAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(adapter.getItemViewType(position)==SystemMultiAdapter.TYPE_LEVEL_1){
                    SystemBean.ChildrenBean  sysyemBean = (SystemBean.ChildrenBean) systemList.get(position);
                    Intent intent = new Intent(getActivity(),SystemListActivity.class);
                    intent.putExtra("id",sysyemBean.getId());
                    intent.putExtra("title",sysyemBean.getName());
                    Log.i("SystemFragment", "onCreate: "+sysyemBean.getId()+"---"+sysyemBean.getName());
                    startActivity(intent);
                }
            }
        });
    }

    private void getSystemList() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getSystemList()).subscribe(new HttpObserver<List<SystemBean>>(getActivity(),swipeLayout) {
            @Override
            public void onSuccess(List<SystemBean> systemBeans) {
                if (systemBeans != null && systemBeans.size() > 0) {
                    for (SystemBean dataBean : systemBeans) {
                        List<SystemBean.ChildrenBean> childList = dataBean.getChildren();
                        for (SystemBean.ChildrenBean childrenBean:childList) {
                            dataBean.addSubItem(childrenBean);
                        }
                        systemList.add(dataBean);
                    }
                    systemMultiAdapter.notifyDataSetChanged();
                }
            }
        });
    }

}
