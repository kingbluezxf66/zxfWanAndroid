package com.example.zxf.myapp.network;

import com.example.zxf.myapp.model.BaseResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

public class GsonResponseBodyConverter implements Converter<ResponseBody, BaseResponse> {
    private final Type type;
    private Gson gson;

    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.type = type;
        this.gson = gson;
    }

    @Override
    public BaseResponse convert(ResponseBody value) throws IOException {

        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        BaseResponse baseBean = gson.fromJson(tempStr, BaseResponse.class);
        Object info = baseBean.getData();
        if (info != null && !"".equals(info) && !"null".equals(info)) {
            try {
                return gson.fromJson(tempStr, type);
            } catch (Exception e) {
                String s = gson.toJson(info);
                baseBean.setData(s);
                return baseBean;
            }
        } else {
            baseBean.setData(null);
            return baseBean;
        }
    }
}
