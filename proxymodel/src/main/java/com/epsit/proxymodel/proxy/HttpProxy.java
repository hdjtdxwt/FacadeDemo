package com.epsit.proxymodel.proxy;

import java.util.HashMap;
import java.util.Map;

//代理类，包含真实的请求处理的类，用这个成员变量的类来做事情，具体用哪个类来做事情，设置下mHttp就好，好处是可以及时替换实现而不用修改特别多代码
public class HttpProxy<T> implements IHttp<T>  { //具体的接口的实现类实现的哪些方法，这里也需要implement那个接口

    private static IHttp mHttp = null;
    private static HttpProxy mInstance;
    private String mUrl;
    private Map<String,Object>mParams;

    private HttpProxy(){
        mParams = new HashMap<>();
    }
    public static HttpProxy getmInstance(){
        if(mInstance==null){
            synchronized (HttpProxy.class){
                if(mInstance ==null){
                    mInstance = new HttpProxy();
                }
            }
        }
        return mInstance;
    }
    public void obtain(IHttp iHttp){
        mHttp= iHttp;
    }
    @Override
    public void get(String url, Map<String,Object> params, Callback<T> callback) {
        if(mHttp!=null){
            mHttp.get(url, params,callback);
        }
    }

    @Override
    public void post(String url, Map<String,Object> params, Callback<T>  callback) {
        if(mHttp!=null){
            mHttp.post(url, params,callback);
        }
    }
}
