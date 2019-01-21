package com.example.zxf.myapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.MyPagerAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.ProJectTilteBean;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

public class ProjectFragment extends BaseFragment {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private Disposable disposable;
    private List<Fragment> fragmentList;
    private List<String> list_Title;
    private MyPagerAdapter myPagerAdapter;

    public static ProjectFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void getTitleList() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getProjectTitle()).subscribe(new HttpObserver<List<ProJectTilteBean>>(getActivity(),null) {
            @Override
            public void onSuccess(List<ProJectTilteBean> proJectTilteBeans) {
                if (proJectTilteBeans != null && proJectTilteBeans.size() > 0) {
                    list_Title.clear();
                    fragmentList.clear();
                    for (ProJectTilteBean dataBean : proJectTilteBeans) {
                        list_Title.add(dataBean.getName());
                        fragmentList.add(ProjectListFragment.newInstance(dataBean.getId()));
                    }
                    myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
                    myPagerAdapter.addData(fragmentList, list_Title);
                    viewpager.setAdapter(myPagerAdapter);
                    tabLayout.setupWithViewPager(viewpager);
                }
            }
        });
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        fragmentList = new ArrayList<>();
        list_Title = new ArrayList<>();
        getTitleList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project;
    }
}
