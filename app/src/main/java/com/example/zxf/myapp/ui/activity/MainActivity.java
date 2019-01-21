package com.example.zxf.myapp.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.ToastUtils;
import com.example.zxf.myapp.AppActivitManager;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.ViewpageAdapter;
import com.example.zxf.myapp.base.BaseActivity;
import com.example.zxf.myapp.helper.BottomNavigationViewHelper;
import com.example.zxf.myapp.ui.HomeFragment;
import com.example.zxf.myapp.ui.MineFragment;
import com.example.zxf.myapp.ui.NavigationFragment;
import com.example.zxf.myapp.ui.ProjectFragment;
import com.example.zxf.myapp.ui.SystemFragment;
import com.example.zxf.myapp.ui.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private MenuItem menuItem;
    private List<String> titleList = new ArrayList<>();
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        titleList.add("首页");
        titleList.add("项目");
        titleList.add("体系");
        titleList.add("导航");
        titleList.add("我的");
        initTitleBar(toolbar,titleList.get(0),false);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(titleList.get(position));
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewpageAdapter viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager());
        viewpager.setAdapter(viewpageAdapter);
        viewpager.setOffscreenPageLimit(1);
        List<Fragment> list = new ArrayList<>();
        list.add(HomeFragment.newInstance(titleList.get(0)));
        list.add(ProjectFragment.newInstance(titleList.get(1)));
        list.add(SystemFragment.newInstance(titleList.get(2)));
        list.add(NavigationFragment.newInstance(titleList.get(3)));
        list.add(new MineFragment());
        viewpageAdapter.setList(list);
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewpager.setCurrentItem(0);
                    return true;
                case R.id.navigation_project:
                    viewpager.setCurrentItem(1);
                    return true;
                case R.id.navigation_system:
                    viewpager.setCurrentItem(2);
                    return true;
                case R.id.navigation_na:
                    viewpager.setCurrentItem(3);
                    return true;
                case R.id.navigation_mine:
                    viewpager.setCurrentItem(4);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {//按键的抬起事件
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    setIsTransAnim(false);
                    AppActivitManager.getAppActivityManager().AppExit(getApplicationContext());
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    ToastUtils.showShort("再按一次，退出程序");
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
