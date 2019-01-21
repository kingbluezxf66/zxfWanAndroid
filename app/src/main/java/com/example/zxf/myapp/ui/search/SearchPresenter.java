package com.example.zxf.myapp.ui.search;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zxf.myapp.R;
import com.example.zxf.myapp.adapter.HotSearchAdapter;
import com.example.zxf.myapp.adapter.SearchHistoryAdapter;
import com.example.zxf.myapp.helper.RetrofitHelper;
import com.example.zxf.myapp.model.HotKey;
import com.example.zxf.myapp.model.SearchHistory;
import com.example.zxf.myapp.network.HttpObservable;
import com.example.zxf.myapp.network.HttpObserver;

import java.util.List;

import javax.annotation.Nonnull;


public class SearchPresenter extends SearchContract.Presenter {
    private Context context;
    @Nonnull
    public  SearchPresenter(Context context){
        this.context = context;
    }

    @Override
    public void getHotSearch() {
        HttpObservable.getObservable(RetrofitHelper.dataManager.getHotList()).subscribe(new HttpObserver<List<HotKey>>(context, null) {
            @Override
            public void onSuccess(List<HotKey> hotKeys) {
                if (hotKeys != null && hotKeys.size() > 0) {
                    mIView.getHotSuccess(hotKeys);
                }
            }
        });
    }


    @Override
    public void deleteAllHistory() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage(R.string.clear_history_alert_dialog_message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mIView.deletePositive();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
    }

    @Override
    public void hotAdapterClick(HotSearchAdapter hotSearchAdapter,List<HotKey> hotKeyList) {
        hotSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到搜索结果界面
                ((AppCompatActivity) view.getContext())
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, SearchResultFragment.getInstance(hotKeyList.get(position).getName()))
                        .commit();
            }
        });
    }

    @Override
    public void searchAdapterItemClick(SearchHistoryAdapter searchHistoryAdapter, List<SearchHistory> list) {
        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //跳转到搜索结果界面
                ((AppCompatActivity) view.getContext())
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_container, SearchResultFragment.getInstance(list.get(position).getKeyword()))
                        .commit();
            }
        });
    }

    @Override
    public void searchAdapterClick(SearchHistoryAdapter searchHistoryAdapter, List<SearchHistory> searchHistoryList) {
        searchHistoryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //删除单个关键字
                String key = searchHistoryList.get(position).getKeyword();
                mIView.deleteSingleSeachHistory(key);
                //刷新界面
                searchHistoryAdapter.remove(position);
                searchHistoryAdapter.notifyDataSetChanged();
            }
        });
    }
}
