package cn.dujc.coreapp;

import androidx.multidex.MultiDexApplication;

import cn.dujc.core.app.Core;
import cn.dujc.coreapp.impl.ListSetupHelper;
import cn.dujc.coreapp.impl.PermissionHelper;
import cn.dujc.coreapp.impl.RefreshImpl;
import cn.dujc.coreapp.impl.ToolbarHelper;
import cn.dujc.coreapp.impl.ToolbarHelper2;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Core.init(this, new Class[]{ToolbarHelper.class, ToolbarHelper2.class}
                , ListSetupHelper.class
                , PermissionHelper.class
                , RefreshImpl.class
        );
        //Core.init(this, ToolbarHelper.class, ListSetupHelper.class);
    }
}
