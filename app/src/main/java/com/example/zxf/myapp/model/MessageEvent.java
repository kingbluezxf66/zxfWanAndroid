package com.example.zxf.myapp.model;

/**
 * 项目名称：MyApplication
 * 类描述：
 * 创建人：zxf
 * 创建时间：2018/12/19 11:12
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
