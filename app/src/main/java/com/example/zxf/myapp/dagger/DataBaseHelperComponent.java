package com.example.zxf.myapp.dagger;

import com.example.zxf.myapp.ui.MineFragment;
import com.example.zxf.myapp.ui.activity.SplashActivity;
import com.example.zxf.myapp.ui.login.LoginActivity;
import com.example.zxf.myapp.ui.search.SearchFragment;
import com.example.zxf.myapp.ui.search.SearchResultFragment;

import dagger.Component;

//DataBaseHelper类注解注入器
@ApplicationScope
@Component(modules = DatabaseHelperModule.class)
public interface DataBaseHelperComponent {
    void inject(SplashActivity splashActivity);
    void inject(LoginActivity loginActivity);
    void inject(MineFragment mineFragment);
    void inject(SearchFragment searchFragment);
    void inject(SearchResultFragment searchResultFragment);
}
