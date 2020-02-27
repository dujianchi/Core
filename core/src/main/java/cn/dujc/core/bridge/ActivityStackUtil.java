package cn.dujc.core.bridge;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;

import cn.dujc.core.util.CacheMap;
import cn.dujc.core.util.ContextUtil;

/**
 * activity管理栈
 * Created by du on 2017/9/21.
 */
public class ActivityStackUtil {

    //事件接受对象
    public static final byte ACTIVITY = 0b10, FRAGMENT = 0b01, ALL = ACTIVITY | FRAGMENT;

    //private final Map<Activity, Set<Fragment>> mActivityFragments = new ArrayMap<Activity, Set<Fragment>>();
    private final Stack<Activity> mActivityStack = new Stack<Activity>();//栈，类型最好不要改变
    private final ListIterator<Activity> mActivityIterator = mActivityStack.listIterator();//栈，类型最好不要改变
    private final Set<Class<? extends Activity>> mClassSet = Collections.synchronizedSet(new HashSet<Class<? extends Activity>>());//类型，用于判断是否存在某个类的activity
    private final Application.ActivityLifecycleCallbacks mLifecycleCallbacks;
    private final CacheMap<Context, IEvent> mExtraEvents = new CacheMap<>();

    private static ActivityStackUtil sInstance = null;

    private ActivityStackUtil() {
        mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NotNull Activity activity, Bundle savedInstanceState) {
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(@NotNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NotNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NotNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NotNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(@NotNull Activity activity, @NotNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NotNull Activity activity) {
                removeActivity(activity);
                removeExtraEvent(activity);
            }
        };
    }

    public static ActivityStackUtil getInstance() {
        if (sInstance == null) {
            synchronized (ActivityStackUtil.class) {
                if (sInstance == null) {
                    sInstance = new ActivityStackUtil();
                }
            }
        }
        return sInstance;
    }

    /**
     * 向Activity中onEvent(int flag, Object value)发送事件
     *
     * @param receiver 接受者，此处为Activity和fragment
     * @param flag     标志参数
     * @param value    参数
     */
    private synchronized static void onEvent(Object receiver, int flag, Object value) {
        if (receiver instanceof IEvent) {
            ((IEvent) receiver).onMyEvent(flag, value);
        }
    }

    /**
     * 初始化方法，唯一的初始化方法
     */
    public synchronized void initApp(Application app) {
        app.registerActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 反注册方法，基本上用不到这个方法……
     */
    public synchronized void unBind(Application app) {
        app.unregisterActivityLifecycleCallbacks(mLifecycleCallbacks);
    }

    /**
     * 添加activity。这个方法无需在外界中访问
     */
    private synchronized void addActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            synchronized (mActivityIterator) {
                mActivityIterator.add(activity);
            }
            synchronized (mClassSet) {
                mClassSet.add(activity.getClass());
            }
        }
    }

    /**
     * 从管理栈中移除指定activity。并不一定会关闭activity，只是移出管理栈
     */
    private synchronized void removeActivity(Activity activity) {
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                if (mActivityIterator.next() == activity) {
                    mActivityIterator.remove();
                    break;
                }
            }
            while (mActivityIterator.hasPrevious()) {
                if (mActivityIterator.previous() == activity) {
                    mActivityIterator.remove();
                    break;
                }
            }
        }
        if (activity != null) {
            synchronized (mClassSet) {
                mClassSet.remove(activity.getClass());
            }
        }
        //removeFragments(activity);
    }

    /**
     * 关闭Activity，如果是Iterator遍历的，把remove掉Iterator，否则从Stack中remove掉
     */
    private void finish(Activity activity, Iterator iterator) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
        if (iterator != null) {
            iterator.remove();
        } else {
            removeActivity(activity);
        }
    }

    /**
     * 获取activity栈
     * 使用{@link #getActivityIterator()} 代替
     *
     * @deprecated 不建议直接操作栈
     */
    @Deprecated
    public synchronized Stack<Activity> getActivities() {
        return mActivityStack;
    }

    /**
     * 获取activity栈
     */
    public synchronized ListIterator<Activity> getActivityIterator() {
        return mActivityIterator;
    }

    /**
     * 当前注册到管理栈的activity数量
     */
    public synchronized int foregroundCount() {
        synchronized (mActivityIterator) {
            int count = 0;
            while (mActivityIterator.hasNext()) {
                count++;
            }
            while (mActivityIterator.hasPrevious()) {
                count++;
            }
            return count;
        }
    }

    /**
     * 当前管理栈顶部的activity
     */
    @Nullable
    public synchronized Activity topActivity() {
        final Activity activity;
        if (!ActivityStackUtil.getInstance().getActivities().isEmpty()
                && (activity = ActivityStackUtil.getInstance().getActivities().peek()) != null
                && !activity.isFinishing()) return activity;
        return null;
    }

    /**
     * 通过类来获取栈中的activity
     */
    @Nullable
    public synchronized <T extends Activity> T getActivity(Class<T> clazz) {
        if (clazz == null) return null;
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                if (activity != null && activity.getClass().equals(clazz)) {
                    return (T) activity;
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                if (activity != null && activity.getClass().equals(clazz)) {
                    return (T) activity;
                }
            }
        }
        return null;
    }

//    public void addFragment(Activity activity, Fragment fragment) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments == null) {
//            fragments = new ArraySet<Fragment>();
//            mActivityFragments.put(activity, fragments);
//        }
//        if (!fragments.contains(fragment)) {
//            fragments.add(fragment);
//        }
//    }

//    public void removeFragment(Activity activity, Fragment fragment) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments != null && fragments.contains(fragment)) {
//            fragments.remove(fragment);
//        }
//    }

//    public void removeFragments(Activity activity) {
//        Set<Fragment> fragments = mActivityFragments.get(activity);
//        if (fragments != null) {
//            fragments.clear();
//            mActivityFragments.remove(activity);
//        }
//    }

    /**
     * 清除管理栈
     *
     * @deprecated 如果脑袋不清楚，最好不要调用此方法
     */
    @Deprecated
    public synchronized void clearActivities() {
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                mActivityIterator.remove();
            }
            while (mActivityIterator.hasPrevious()) {
                mActivityIterator.remove();
            }
        }
    }

    /**
     * 关闭指定activity
     */
    public synchronized void finishActivity(Activity activity) {
        finish(activity, null);
    }

    /**
     * 关闭指定activity
     */
    @SafeVarargs
    public synchronized final void finishActivity(Class<? extends Activity>... classes) {
        if (classes == null || classes.length == 0) return;
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                for (Class<? extends Activity> clazz : classes) {
                    if (activity.getClass().equals(clazz)) {
                        finish(activity, mActivityIterator);
                    }
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                for (Class<? extends Activity> clazz : classes) {
                    if (activity.getClass().equals(clazz)) {
                        finish(activity, mActivityIterator);
                    }
                }
            }
        }
    }

    /**
     * 关闭activity，直到clazz为止。如果当前activity栈不存在对应从class，则不做任何操作
     */
    public synchronized void finishUntil(Class<? extends Activity> clazz) {
        if (clazz == null) return;
        if (!getInstance().contains(clazz)) return;
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                if (!activity.getClass().equals(clazz)) {
                    finish(activity, mActivityIterator);
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                if (!activity.getClass().equals(clazz)) {
                    finish(activity, mActivityIterator);
                }
            }
        }
    }

    /**
     * 关闭相同的类，但保留当前。用于关闭多开的Activity
     */
    public synchronized final void finishSameButThis(Activity lastSurvivalOfSpecies) {
        if (lastSurvivalOfSpecies == null) return;
        final Class<? extends Activity> exterminatedSpecies = lastSurvivalOfSpecies.getClass();
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                if (activity != lastSurvivalOfSpecies && activity.getClass().equals(exterminatedSpecies)) {
                    finish(activity, mActivityIterator);
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                if (activity != lastSurvivalOfSpecies && activity.getClass().equals(exterminatedSpecies)) {
                    finish(activity, mActivityIterator);
                }
            }
        }
    }

    /**
     * 关闭全部activity
     */
    public synchronized void closeAllActivity() {
        closeAllExcept((Activity) null);
    }

    /**
     * 关闭所有Activity，除了某个Activity的类，最终可能会存在多个
     *
     * @param classes Activity.class
     */
    @SafeVarargs
    public synchronized final void closeAllExcept(Class<? extends Activity>... classes) {
        if (classes == null || classes.length == 0) return;
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                boolean contain = false;
                for (Class<? extends Activity> clazz : classes) {
                    contain = contain || activity.getClass().equals(clazz);
                }
                if (!contain) {
                    mActivityIterator.remove();
                    activity.finish();
                } else if (activity.isFinishing()) {
                    mActivityIterator.remove();
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                boolean contain = false;
                for (Class<? extends Activity> clazz : classes) {
                    contain = contain || activity.getClass().equals(clazz);
                }
                if (!contain) {
                    mActivityIterator.remove();
                    activity.finish();
                } else if (activity.isFinishing()) {
                    mActivityIterator.remove();
                }
            }
        }
    }

    /**
     * 在lastSurvivalOfSpecies同类的实例中保留一个当前，其他删除；然后关闭除lastSurvivalOfSpecies以及classes数组以外的所有类
     *
     * @param lastSurvivalOfSpecies 同类的实例只保留这个
     * @param classes               保留类别，如果保留的类别中包括lastSurvivalOfSpecies，则关闭其余的
     */
    @SafeVarargs
    public synchronized final void keepOneSurvivalAndOther(Activity lastSurvivalOfSpecies, Class<? extends Activity>... classes) {
        if (lastSurvivalOfSpecies == null) return;
        finishSameButThis(lastSurvivalOfSpecies);
        Set<Class<? extends Activity>> classList = new HashSet<>();
        if (classes != null) {
            List<Class<? extends Activity>> exclude = Arrays.asList(classes);
            classList.addAll(exclude);
        }
        classList.add(lastSurvivalOfSpecies.getClass());
        Class<? extends Activity>[] newClasses = new Class[classList.size()];
        closeAllExcept(classList.toArray(newClasses));
    }

    /**
     * 是否包含某个类
     *
     * @param clazz 某个类
     * @return 是否包含某个类
     */
    public synchronized final boolean contains(Class<? extends Activity> clazz) {
        if (clazz == null) return false;
        /*for (Activity activity : mActivities) {
            if (activity != null && activity.getClass().equals(clazz)) return true;
        }
        return false;*/
        synchronized (mClassSet) {
            return mClassSet.contains(clazz);
        }
    }

    /**
     * 关闭所有Activity，除了某个Activity的实例，最终只会存在一个
     *
     * @param activity activity.this
     */
    public synchronized void closeAllExcept(Activity activity) {
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity next = mActivityIterator.next();
                if (activity != next) {
                    mActivityIterator.remove();
                    next.finish();
                } else if (next.isFinishing()) {
                    mActivityIterator.remove();
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity previous = mActivityIterator.previous();
                if (activity != previous) {
                    mActivityIterator.remove();
                    previous.finish();
                } else if (previous.isFinishing()) {
                    mActivityIterator.remove();
                }
            }
        }
    }

    /**
     * 发送事件
     *
     * @param flag     标注
     * @param value    携带参数
     * @param receiver 接受对象，0b10给Activity，0b01给Fragment
     */
    public synchronized void sendEvent(int flag, Object value, byte receiver) {
        synchronized (mActivityIterator) {
            while (mActivityIterator.hasNext()) {
                Activity activity = mActivityIterator.next();
                if ((receiver & ACTIVITY) == ACTIVITY) {
                    onEvent(activity, flag, value);
                }
                if ((receiver & FRAGMENT) == FRAGMENT && activity instanceof FragmentActivity) {
                    final List<Fragment> fragments = ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
                    sendFragmentEvent(flag, value, fragments);
                }
            }
            while (mActivityIterator.hasPrevious()) {
                Activity activity = mActivityIterator.previous();
                if ((receiver & ACTIVITY) == ACTIVITY) {
                    onEvent(activity, flag, value);
                }
                if ((receiver & FRAGMENT) == FRAGMENT && activity instanceof FragmentActivity) {
                    final List<Fragment> fragments = ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
                    sendFragmentEvent(flag, value, fragments);
                }
            }
        }
        synchronized (mExtraEvents) {
            Set<Context> contexts = mExtraEvents.keySet();
            for (final Context context : contexts) {
                IEvent event = mExtraEvents.get(context);
                if (event != null) {
                    event.onMyEvent(flag, value);
                }
            }
        }
    }

    public synchronized void addExtraEvent(Context context, IEvent event) {
        synchronized (mExtraEvents) {
            mExtraEvents.put(context, event);
        }
    }

    public synchronized void removeExtraEvent(IEvent remove) {
        synchronized (mExtraEvents) {
            Set<Context> contexts = mExtraEvents.keySet();
            for (final Context context : contexts) {
                IEvent event = mExtraEvents.get(context);
                if (event == remove) {
                    mExtraEvents.remove(context);
                }
            }
        }
    }

    private synchronized void removeExtraEvent(Activity activity) {
        if (activity == null) return;
        synchronized (mExtraEvents) {
            Set<Context> contexts = mExtraEvents.keySet();
            for (final Context context : contexts) {
                if (context == activity || ContextUtil.getActivity(context) == activity) {
                    mExtraEvents.remove(context);
                }
            }
        }
    }

    private synchronized static void sendFragmentEvent(int flag, Object value, List<Fragment> fragments) {
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                onEvent(fragment, flag, value);
                if (fragment != null) {
                    sendFragmentEvent(flag, value, fragment.getChildFragmentManager().getFragments());
                }
            }
        }
    }
}
