package com.example.zxf.myapp.model;

import io.realm.RealmObject;

/**
 * 搜索历史
 */
public class SearchHistory extends RealmObject {
    public String getKeyword() {
        return keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    private String keyword;
}
