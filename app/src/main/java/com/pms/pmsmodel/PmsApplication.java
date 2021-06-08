package com.pms.pmsmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class PmsApplication extends Application {

    private static PmsApplication instance;

    public PmsApplication() {
        instance = this;
    }

    public static synchronized PmsApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    private Activity activity;
    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

}
