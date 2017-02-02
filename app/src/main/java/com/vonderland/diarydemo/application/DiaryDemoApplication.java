package com.vonderland.diarydemo.application;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class DiaryDemoApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        Realm.init(this);

    }

    /**
     * 获取 Application Context
     * 该 Context 可用于文件/资源相关操作,不可用作 UI 的上下文
     * @return
     */
    public static Context getGlobalContext() {
        return context;
    }
}
