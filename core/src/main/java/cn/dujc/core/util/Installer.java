package cn.dujc.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * @author du
 * date 2018/5/30 下午7:35
 */
public class Installer {

    public static final String APK_MIME_TYPE = "application/vnd.android.package-archive";

    private Installer() { }

    /**
     * 安装apk
     */
    public static void install(Context context, File apk) {
        if (apk == null || !apk.exists()) {
            ToastUtil.showToast(context, "安装包不存在");
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//不会安装一半跳掉
            install.setDataAndType(Uri.fromFile(apk), APK_MIME_TYPE);
            try {
                context.startActivity(install);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
