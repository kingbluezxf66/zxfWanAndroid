package com.example.zxf.myapp.ui.search;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.FrameLayout;

import com.example.zxf.myapp.R;
import com.example.zxf.myapp.base.BaseActivity;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {
    private static final String TAG = "kingbluezxf";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        initTitleBar(toolbar,"",true);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container,SearchFragment.getInstance()).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchView searchView  = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getString(R.string.search_view_hint));
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setHintTextColor(getResources().getColor(R.color.white));
        /**
         * 初始是否已经是展开的状态
         * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
         */
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i(TAG, "onQueryTextSubmit: "+s);
                //跳转到搜索界面
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_container,SearchResultFragment.getInstance(s)).commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
