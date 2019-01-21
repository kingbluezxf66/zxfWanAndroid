package com.example.zxf.myapp.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zxf.myapp.R;
import com.example.zxf.myapp.base.BaseActivity;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;


public class BrowserActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cl_content)
    LinearLayout clContent;
    private AgentWeb mAgentWeb;
    private String url;
    private String title;

    @Override
    protected int getLayout() {
        return R.layout.activity_brower;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.i("kingbluezxf", "onCreate: " + url);
        title = intent.getStringExtra("title");
         initTitleBar(toolbar,title,true);
        mAgentWeb = AgentWeb.with(this).setAgentWebParent(clContent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
        WebSettings settings = mAgentWeb.getWebCreator().getWebView().getSettings();
        settings.setSupportZoom(true);//设置可以支持缩放
        settings.setBuiltInZoomControls(true);//设置出现缩放工具
        settings.setUseWideViewPort(true);//扩大比例的缩放
        settings.setDisplayZoomControls(false);//隐藏缩放控件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.star:
                        //收藏
                        break;
                    case R.id.share:
                        //分享
                        openShareDialog();
                        break;
                    case R.id.reload:
                        refresh();
                        break;
                    case R.id.copy_link:
                        copy();
                        break;
                    case R.id.open_in_browser:
                        openBrowser();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browser, menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 分享
     */
    public void openShareDialog() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String dialogTitle = getString(R.string.share_dialog_title);
        String shareContent = "【" + title + "】：" + url
                + " （" + getString(R.string.share_source) + "）";
        intent.putExtra(Intent.EXTRA_SUBJECT, dialogTitle);
        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
        startActivity(intent);
    }

    /**
     * 刷新
     */
    private void refresh() {
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().reload(); //刷新
        }
    }

    /**
     * 复制链接
     */
    private void copy() {
        if (mAgentWeb != null) {
            ClipboardManager mClipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, mAgentWeb.getWebCreator().getWebView().getUrl()));
        }
    }

    /**
     * 测试错误页的显示
     */
    private void loadErrorWebSite() {
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl("http://www.unkownwebsiteblog.me");
        }
    }

    /**
     * 浏览器打开
     */
    private void openBrowser() {
        String targetUrl = mAgentWeb.getWebCreator().getWebView().getUrl();
        if (mAgentWeb != null) {
            if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
                Toast.makeText(this, targetUrl + " 该链接无法使用浏览器打开。", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri mUri = Uri.parse(targetUrl);
            intent.setData(mUri);
            startActivity(intent);
        }
    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {

        if (this.mAgentWeb != null) {

            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(this, "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
//            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }

    @Override
    protected void onPause() {
        if(isConnectNet()){
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        if(isConnectNet()){

            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(isConnectNet()){

            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
}
