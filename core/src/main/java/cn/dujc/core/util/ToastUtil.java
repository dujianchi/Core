package cn.dujc.core.util;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.dujc.core.bridge.ActivityStackUtil;

/**
 * 在目前的实现方式中实现自定义view的toast会有问题，以后再来改（2018年11月7日）
 * Created by du on 2017/9/21.
 */
public class ToastUtil {
    public static final int DEFAULT_GRAVITY = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    public static final int DEFAULT_DURATION = Toast.LENGTH_SHORT;
    private static WeakReference<Toast> LAST_TOAST = null;

    ToastUtil() {
    }

    public static int getDefaultYOffset(Context context) {
        if (context == null) return 48;
        return (int) (context.getApplicationContext().getResources().getDisplayMetrics().density * 24 + 0.5);
    }

    @Nullable
    public static Toast current(Context context, CharSequence text) {
        if (context == null) return null;
        if (LAST_TOAST != null) {
            final Toast lastOne = LAST_TOAST.get();
            if (lastOne != null) lastOne.cancel();
        }
        @SuppressLint("ShowToast") Toast toast = Toast.makeText(context.getApplicationContext(), text, DEFAULT_DURATION);
        LAST_TOAST = new WeakReference<>(toast);
        return toast;
    }

    public static void showToast(Context context, @StringRes int textId) {
        showToast(context, context.getString(textId));
    }

    public static void showToast(Context context, CharSequence text) {
        showToast(context, text, DEFAULT_GRAVITY, getDefaultYOffset(context), DEFAULT_DURATION);
    }

    public static void showToastObjectsDefault(Context context, Object... objects) {
        showToastObjects(context, DEFAULT_GRAVITY, getDefaultYOffset(context), objects);
    }

    public static void showToastObjects(Context context, int gravity, int yOffset, Object... objects) {
        showToast(context, StringUtil.concat(objects), gravity, yOffset, DEFAULT_DURATION);
    }

    public static void showToastFormattedDefault(Context context, String format, Object... args) {
        showToastFormatted(context, DEFAULT_GRAVITY, getDefaultYOffset(context), DEFAULT_DURATION, format, args);
    }

    public static void showToastFormatted(Context context, int gravity, int yOffset, int duration, String format, Object... args) {
        showToast(context, StringUtil.format(format, args), gravity, yOffset, duration);
    }

    public static void showToast(Context context, CharSequence text, int gravity, int yOffset, int duration) {
        if (isNotificationEnabled(context)) {
            Toast toast = current(context, text);
            if (toast != null) {
                toast.setGravity(gravity, 0, yOffset);
                toast.setDuration(duration);
                toast.show();
            }
        } else {
            Context ctx = context;
            if (context instanceof Application) {
                ctx = ActivityStackUtil.getInstance().topActivity();
            }
            if (ctx != null) {
                try {
                    ToastX.makeText(ctx, text, duration).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查通知栏权限有没有开启
     * 参考SupportCompat包中的方法： NotificationManagerCompat.from(context).areNotificationsEnabled();
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            return notificationManager != null && notificationManager.areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return (Integer) checkOpNoThrowMethod.invoke(appOps, value, uid, pkg) == 0;
            } catch (NoSuchMethodException | NoSuchFieldException | InvocationTargetException | IllegalAccessException | RuntimeException | ClassNotFoundException ignored) {
                return true;
            }
        } else {
            return true;
        }
    }

}
