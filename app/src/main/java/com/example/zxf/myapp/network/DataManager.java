package com.example.zxf.myapp.network;

import com.example.zxf.myapp.model.BannerBean;
import com.example.zxf.myapp.model.BaseResponse;
import com.example.zxf.myapp.model.HomeAticleBean;
import com.example.zxf.myapp.model.HotKey;
import com.example.zxf.myapp.model.NavigationBean;
import com.example.zxf.myapp.model.ProJectTilteBean;
import com.example.zxf.myapp.model.RegistResponse;
import com.example.zxf.myapp.model.SystemBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class DataManager {
    private RequestInterface requestInterface;

    public DataManager(RequestInterface requestInterface) {
        this.requestInterface = requestInterface;
    }

    public Observable<BaseResponse<RegistResponse>> getRegistMessage(Map<String, String> map) {
        return requestInterface.goRegist(map);
    }

    public Observable<BaseResponse<RegistResponse>> goLoginMessage(Map<String, String> map) {
        return requestInterface.goLogin(map);
    }

    public Observable<BaseResponse<List<BannerBean>>> getBannerData() {
        return requestInterface.getBannerData();
    }

    public Observable<BaseResponse<HomeAticleBean>> getArticleList(int page) {
        return requestInterface.getArticleList(page);
    }

    public Observable<BaseResponse> collectArticle(String id) {
        return requestInterface.collectArticle(id);
    }

    public Observable<BaseResponse<List<HotKey>>> getHotList() {
        return requestInterface.getHotSearchList();
    }

    public Observable<BaseResponse<HomeAticleBean>> getSearchResult(int page, String keyword) {
        return requestInterface.getSearchResult(page, keyword);
    }

    public Observable<BaseResponse<List<ProJectTilteBean>>> getProjectTitle() {
        return requestInterface.getProtectList();
    }

    public Observable<BaseResponse<HomeAticleBean>> getProjectList(int page, String cid) {
        return requestInterface.getProjectList(page, cid);
    }

    public Observable<BaseResponse<List<SystemBean>>> getSystemList() {
        return requestInterface.getSystemList();
    }

    public Observable<BaseResponse<HomeAticleBean>> getSystemSecondList(int page,String id) {
        return requestInterface.getSystemSecondList(page,id);
    }
    public Observable<BaseResponse<List<NavigationBean>>> getNavigationList() {
        return requestInterface.getNavigationList();
    }

    public Observable<BaseResponse> exitLogin() {
        return requestInterface.exitLogin();
    }
}
