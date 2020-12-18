package com.example.zxf.myapp.network;

import com.example.zxf.myapp.BuildConfig;
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
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestInterface {
//    public String baseUrl = "https://www.wanandroid.com";
    public String baseUrl = BuildConfig.HostUrl;

    /**
     * 注册方法
     * http://www.wanandroid.com/user/register
     * 方法：POST
     * 参数
     * username,password,repassword
     */
    @FormUrlEncoded
    @POST("/user/register")
    Observable<BaseResponse<RegistResponse>> goRegist(@FieldMap Map<String, String> map);

    /**
     * 登录方法
     * http://www.wanandroid.com/user/login
     * 方法：POST
     * 参数：
     * username，password
     */
    @FormUrlEncoded
    @POST("/user/login")
    Observable<BaseResponse<RegistResponse>> goLogin(@FieldMap Map<String, String> map);

    /**
     *     退出
     *     http://www.wanandroid.com/user/logout/json
     *     方法：GET
     */
    @GET("user/logout/json")
    Observable<BaseResponse> exitLogin();

    /**
     * 首页轮播图
     * http://www.wanandroid.com/banner/json
     * 方法：GET
     * 参数：无
     */
    @GET("banner/json")
    Observable<BaseResponse<List<BannerBean>>> getBannerData();

    /**
     * 首页文章列表
     * http://www.wanandroid.com/article/list/0/json
     * 方法：GET
     * 参数：页码，拼接在连接中，从0开始。
     * 注意：页码从0开始，拼接在链接上。
     *
     * @return
     */
    @GET("article/list/{page}/json")
    Observable<BaseResponse<HomeAticleBean>> getArticleList(@Path("page") int page);

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * 方法：POST
     * 参数： 文章id，拼接在链接中。
     */
    @POST("lg/collect/{id}/json")
    Observable<BaseResponse> collectArticle(@Path("id") String id);

    /**
     * 搜索热词
     * 即目前搜索最多的关键词。
     * http://www.wanandroid.com//hotkey/json
     * 方法：GET
     * 参数：无
     */
    @GET("/hotkey/json")
    Observable<BaseResponse<List<HotKey>>> getHotSearchList();

    /**
     * 搜索
     * http://www.wanandroid.com/article/query/0/json
     * 方法：POST
     * 参数：
     * 页码：拼接在链接上，从0开始。
     * k ： 搜索关键词
     * 注意：支持多个关键词，用空格隔开
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Observable<BaseResponse<HomeAticleBean>> getSearchResult(@Path("page") int page, @Field("k") String keywords);

    /**
     * 项目分类
     * http://www.wanandroid.com/project/tree/json
     * 方法： GET
     * 参数： 无
     */
    @GET("project/tree/json")
    Observable<BaseResponse<List<ProJectTilteBean>>> getProtectList();

    /**
     * 项目列表数据
     * 某一个分类下项目列表数据，分页展示
     * http://www.wanandroid.com/project/list/1/json?cid=294
     * 方法：GET
     * 参数：
     * cid 分类的id，上面项目分类接口
     * 页码：拼接在链接中，从1开始。
     */
    @GET("project/list/{page}/json")
    Observable<BaseResponse<HomeAticleBean>> getProjectList(@Path("page") int page, @Query("cid") String id);

    /**
     * 体系数据
     * http://www.wanandroid.com/tree/json
     * 方法：GET
     * 参数：无
     */
    @GET("tree/json")
    Observable<BaseResponse<List<SystemBean>>> getSystemList();

    /**
     * 知识体系下的文章
     * 方法：GET
     * 参数：
     * cid 分类的id，上述二级目录的id
     * 页码：拼接在链接上，从0开始。
     * 例如查看类别：Android Studio下所有的文章：http://www.wanandroid.com/article/list/0/json?cid=60
     */
    @GET("article/list/{page}/json")
    Observable<BaseResponse<HomeAticleBean>> getSystemSecondList(@Path("page") int page, @Query("cid") String id);

    /**
     *  导航数据
     *     http://www.wanandroid.com/navi/json
     *     方法：GET
     *     参数：无
     *     可直接点击查看示例：http://www.wanandroid.com/navi/json
     */
    @GET("navi/json")
    Observable<BaseResponse<List<NavigationBean>>> getNavigationList();


}
