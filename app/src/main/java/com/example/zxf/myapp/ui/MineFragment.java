package com.example.zxf.myapp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.zxf.myapp.MyApplication;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.TasPublicAdapter;
import com.example.zxf.myapp.base.BaseFragment;
import com.example.zxf.myapp.helper.DataBaseHelper;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.User;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;
import com.example.zxf.myapp.ui.login.LoginActivity;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class MineFragment extends BaseFragment {
    @BindView(R.id.tagCloud)
    TagCloudView tagCloud;
    //    RecyclerView recyclerView;
    @BindView(R.id.switch1)
    Switch switch1;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_go)
    TextView tvGo;
    @Inject
     DataBaseHelper dataBaseHelper;

    @Override
    public int getLayoutId() {
        return R.layout.la_te;
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        MyApplication.get(getActivity()).getDatabaseHelperComponent().inject(this);
        setUserUi();
        List<String> nameList = new ArrayList<>();
        nameList.add("郭霖");
        nameList.add("鸿洋");
        nameList.add("哈哈");
        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.add("赵六");
        nameList.add("郭霖");
        nameList.add("鸿洋");
        nameList.add("哈哈");
        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.add("赵六");
        nameList.add("郭霖");
        nameList.add("鸿洋");
        nameList.add("哈哈");
        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.add("赵六");
        TasPublicAdapter textTagsAdapter = new TasPublicAdapter(nameList);
        tagCloud.setAdapter(textTagsAdapter);
        SPUtils spUtils = SPUtils.getInstance("theme");
        int theme = spUtils.getInt("theme");
        if (theme == 0) {
            switch1.setChecked(false);
        } else {
            switch1.setChecked(true);
        }
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    spUtils.put("theme", 1);
                } else {
                    spUtils.put("theme", 0);
                }
                getActivity().recreate();
            }
        });
    }

    @Nullable
    private void setUserUi() {
        User user = dataBaseHelper.getUserInfo();
        if (user != null) {
            String userName = user.getUsername();
            tvName.setText(userName);
            tvGo.setText(getString(R.string.exitLogin));
        } else {
            tvGo.setText(getString(R.string.goLoginorRegist));
            tvName.setText(getString(R.string.unLogin));
        }
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user == null) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    //退出登录
                    exitLogin();
                }
            }
        });
    }

    private void exitLogin() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.exitLogin()).subscribe(new HttpObserver(getActivity(),null) {
            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort("已退出登录");
                dataBaseHelper.deleteUserInfo();
                setUserUi();
            }
        });
    }
}
