package com.example.zxf.myapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.NavigationLeftAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.NavigationBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.widget.MyLinearlayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class NavigationFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    private Disposable disposable;
    private List<NavigationBean> navigationLeftList;
    private NavigationLeftAdapter navigationLeftAdapter;
    private int targetPosition;
    private MyLinearlayoutManager linearlayoutManager;

    public static NavigationFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        NavigationFragment fragment = new NavigationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        initRecycleAndAdapter();
        getNavigationList();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initRecycleAndAdapter() {
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(decoration);
        linearlayoutManager = new MyLinearlayoutManager(getActivity());
        recyclerView.setLayoutManager(linearlayoutManager);
        navigationLeftList = new ArrayList<>();
        navigationLeftAdapter = new NavigationLeftAdapter(navigationLeftList);
        recyclerView.setAdapter(navigationLeftAdapter);
        navigationLeftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                navigationLeftAdapter.setPosition(position);
                createFragment(navigationLeftList.get(position).getArticles(),false);
            }
        });
    }

    private void getNavigationList() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getNavigationList()).subscribe(new HttpObserver<List<NavigationBean>>(getActivity(),null) {
            @Override
            public void onSuccess(List<NavigationBean> navigationBeans) {
                if (navigationBeans != null && navigationBeans.size() > 0) {
                    navigationLeftList = navigationBeans;
                    navigationLeftAdapter.setNewData(navigationLeftList);
                    navigationLeftAdapter.notifyDataSetChanged();
                    createFragment(navigationBeans.get(0).getArticles(), true);
                }
            }
        });

    }

    public void createFragment(List<NavigationBean.ArticlesBean> data, boolean first) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        NavigationRightFragment navigationRightFragment = new NavigationRightFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("rightData", (ArrayList<? extends Parcelable>) data);
        navigationRightFragment.setArguments(bundle);
        if(first){
            fragmentTransaction.add(R.id.frameLayout, navigationRightFragment);
        }else{
            fragmentTransaction.replace(R.id.frameLayout, navigationRightFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

}
