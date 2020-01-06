package cn.dujc.core.app;

import android.app.Application;
import android.content.Context;

import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.initializer.baselist.IBaseListSetup;
import cn.dujc.core.initializer.baselist.IBaseListSetupHandler;
import cn.dujc.core.initializer.content.IRootViewSetup;
import cn.dujc.core.initializer.content.IRootViewSetupHandler;
import cn.dujc.core.initializer.permission.IPermissionSetup;
import cn.dujc.core.initializer.permission.IPermissionSetupHandler;
import cn.dujc.core.initializer.refresh.IRefreshSetup;
import cn.dujc.core.initializer.refresh.IRefreshSetupHandler;
import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.initializer.toolbar.IToolbarHandler;

/**
 * 由于框架内有多个需要初始化的参数，所以采用这个来构造一个初始化的方法
 * ，同时可以重新实现Initializer或InitProvider来自定义初始化哪部分。
 * 当然一个个功能初始化也可以...
 */
public class Core {

    public static boolean DEBUG = false;
    private Core() {
    }

    /**
     * 初始化框架
     */
    public static void init(Application app
            , Class<? extends IToolbar>[] iToolbar
    ) {
        init(app, iToolbar, IBaseListSetup.DefaultImpl.class, null, IRefreshSetup.Impl.class);
    }

    /**
     * 初始化框架
     */
    public static void init(Application app
            , Class<? extends IToolbar>[] iToolbar
            , Class<? extends IBaseListSetup> iListSetup
    ) {
        init(app, iToolbar, iListSetup, null, IRefreshSetup.Impl.class);
    }

    /**
     * 初始化框架
     */
    public static void init(Application app
            , Class<? extends IToolbar>[] iToolbar
            , Class<? extends IBaseListSetup> iListSetup
            , Class<? extends IPermissionSetup> iPermissionSetup
    ) {
        init(app, iToolbar, iListSetup, iPermissionSetup, IRefreshSetup.Impl.class);
    }

    /**
     * 初始化框架
     */
    public static void init(Application app, Class<? extends IToolbar>[] iToolbar
            , Class<? extends IBaseListSetup> iListSetup
            , Class<? extends IPermissionSetup> iPermissionSetup
            , Class<? extends IRefreshSetup> iRefresh
    ) {
        init(app, iToolbar, iListSetup, iPermissionSetup, iRefresh, null);
    }

    /**
     * 初始化框架
     */
    public static void init(Application app, Class<? extends IToolbar>[] iToolbar
            , Class<? extends IBaseListSetup> iListSetup
            , Class<? extends IPermissionSetup> iPermissionSetup
            , Class<? extends IRefreshSetup> iRefresh
            , Class<? extends IRootViewSetup> iRootViewSetup
    ) {
        initActivityStack(app);
        if (iToolbar != null) {
            initToolbarHelper(app, iToolbar);
        }
        if (iListSetup != null) {
            initListSetup(app, iListSetup);
        }
        if (iPermissionSetup != null) {
            initPermissionSetup(app, iPermissionSetup);
        }
        if (iRefresh != null) {
            initRefresh(app, iRefresh);
        }
        if (iRootViewSetup != null) {
            initRootView(app, iRootViewSetup);
        }
    }

    private static void initActivityStack(Application app) {
        if (app != null) ActivityStackUtil.getInstance().initApp(app);
    }

    private static void initToolbarHelper(Context context, Class<? extends IToolbar>[] clazz) {
        if (context != null && clazz != null) IToolbarHandler.setToolbarClass(context, clazz);
    }

    private static void initListSetup(Context context, Class<? extends IBaseListSetup> clazz) {
        if (context != null && clazz != null) IBaseListSetupHandler.setSetupClass(context, clazz);
    }

    private static void initPermissionSetup(Context context, Class<? extends IPermissionSetup> clazz) {
        if (context != null && clazz != null) IPermissionSetupHandler.setSetupClass(context, clazz);
    }

    private static void initRefresh(Context context, Class<? extends IRefreshSetup> clazz) {
        if (context != null && clazz != null) IRefreshSetupHandler.setRefreshClass(context, clazz);
    }

    private static void initRootView(Context context, Class<? extends IRootViewSetup> clazz) {
        if (context != null && clazz != null) IRootViewSetupHandler.setSetupClass(context, clazz);
    }

}
