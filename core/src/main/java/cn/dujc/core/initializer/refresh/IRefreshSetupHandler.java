package cn.dujc.core.initializer.refresh;

import android.content.Context;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import cn.dujc.core.app.Initializer;

/**
 * @author du
 * date 2018/5/12 下午7:07
 */
public final class IRefreshSetupHandler {

    private static final String CLASS = IRefreshSetup.class.getName();
    private static IRefreshSetup sRefresh = null;

    private IRefreshSetupHandler() {
    }

    /**
     * 设置refresh的处理类
     */
    public static void setRefreshClass(Context context, Class<? extends IRefreshSetup> refreshClass) {
        if (context == null || refreshClass == null) return;
        final String className = refreshClass.getName();
        Initializer.classesSavior(context).edit().putString(CLASS, className).apply();
        createRefreshByClass(refreshClass);
    }

    /**
     * 从保存的类中获取refresh实例。处理顺序为 1.静态公有方法  2.注解方法  3.创建实例
     */
    private static void createRefreshByClass(Class<? extends IRefreshSetup> clazz) {
        try {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                final IRefreshSetup fromStatic = createByStaticMethod(method);
                if (fromStatic != null) {
                    sRefresh = fromStatic;
                    return;
                }
            }
            sRefresh = createByNewInstance(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从静态公有方法获取IRefresh实例
     */
    private static IRefreshSetup createByStaticMethod(Method method) {
        try {
            final boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (isStatic) {
                if (!method.isAccessible()) method.setAccessible(true);
                final Object invoke = method.invoke(null);
                if (invoke instanceof IRefreshSetup) return (IRefreshSetup) invoke;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用new的方式获取IRefresh实例。这是最终也是最暴力的方式，当静态类、注解都失败的时候
     * ，会执行这个方案，这个方案会尽可能地通过传递的类来强制创建一个IRefresh实例
     */
    private static IRefreshSetup createByNewInstance(Class clazz) {
        final Constructor[] constructors = clazz.getDeclaredConstructors();
        AccessibleObject.setAccessible(constructors, true);
        for (Constructor constructor : constructors) {
            try {
                Object instance = constructor.newInstance();
                if (instance instanceof IRefreshSetup) {
                    return (IRefreshSetup) instance;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取IRefresh的实例
     */
    public static IRefreshSetup getRefresh(Context context) {
        if (sRefresh != null) return sRefresh;
        if (context == null) return null;
        try {
            final Class<?> refreshClass = Class.forName(Initializer.classesSavior(context).getString(CLASS, ""));
            createRefreshByClass((Class<? extends IRefreshSetup>) refreshClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return sRefresh;
    }

//    public static void setup(Context context, IRefreshListener refreshListener) {
//        IRefreshSetup setup = getRefresh(context);
//        if (setup != null) setup.setOnRefreshListener(refreshListener);
//    }

}
