package com.example.zxf.myapp.ui.search;

import com.example.zxf.myapp.adapter.HotSearchAdapter;
import com.example.zxf.myapp.adapter.SearchHistoryAdapter;
import com.example.zxf.myapp.base.BasePresenter;
import com.example.zxf.myapp.base.BaseView;
import com.example.zxf.myapp.model.HotKey;
import com.example.zxf.myapp.model.SearchHistory;

import java.util.List;

public interface SearchContract {
    abstract class Presenter extends BasePresenter<SearchView> {
        public abstract void getHotSearch();
        public abstract void deleteAllHistory();
        public abstract void hotAdapterClick(HotSearchAdapter hotSearchAdapter,List<HotKey> list);
        public abstract void searchAdapterItemClick(SearchHistoryAdapter searchHistoryAdapter,List<SearchHistory> list);
        public abstract void searchAdapterClick(SearchHistoryAdapter searchHistoryAdapter, List<SearchHistory> list);
    }

    interface SearchView extends BaseView {

        void getHotSuccess(List<HotKey> list);
        void deletePositive();
        void deleteSingleSeachHistory(String key);
    }
}
