package com.vonderland.diarydemo.application;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Vonderland on 2017/2/1.
 */

public class DiaryDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
