package com.epsit.proxymodel;

import android.app.Application;

import com.epsit.proxymodel.proxy.HttpProxy;
import com.epsit.proxymodel.proxy.OkhttpModel;
import com.epsit.proxymodel.proxy.VolleyModel;

public class ProxyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //HttpProxy.getmInstance().obtain(OkhttpModel.getInstance());//使用okhttp来网络请求
        HttpProxy.getmInstance().obtain(VolleyModel.getInstance(this));//使用okhttp来网络请求

        /**这种是静态代理的方式，在application里指定了要代理的对象是哪个类的实例，还有一种是动态代理
         * 动态代理是我们也不知道代理的对象是哪个类的实例，只有在代码运行的时候，通过反射机制，生成要代理的对象（也就是动态生成实现接口的类，比如当前例子里的
         * OkhttpModel和VolleyModel, 动态代理就是动态生成类似这样的类，这样的类也会implement相同的接口IHttp）然后在反射的方式设置到HttpProxy的成员变量mHttp
         *
         */


    }
}
