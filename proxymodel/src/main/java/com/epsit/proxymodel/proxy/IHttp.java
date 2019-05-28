package com.epsit.proxymodel.proxy;

import java.util.Map;

public interface IHttp<T>{
    void get(String url, Map<String,Object> params, Callback<T>callback);
    void post(String url, Map<String,Object>params, Callback<T>callback);
}
