package cn.dujc.coreapp;

import android.app.Application;

import cn.dujc.core.app.Core;
import cn.dujc.coreapp.impl.ListSetupHelper;
import cn.dujc.coreapp.impl.PermissionHelper;
import cn.dujc.coreapp.impl.ToolbarHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.init(this, ToolbarHelper.class, ListSetupHelper.class, PermissionHelper.class);
        //Core.init(this, ToolbarHelper.class, ListSetupHelper.class);
    }
}
