package com.epsit.proxymodel.proxy;

public interface Callback<T> {
    void onSuccess(T obj);
    void onFail(String error);
}
