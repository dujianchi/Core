package cn.dujc.coreapp.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.ToastUtil;

public class WebFragment extends BaseWebFragment {

    private static final String SCHEME = "vrybh://";
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected boolean _shouldOverrideUrlLoading(WebView view, final String url) {
        if (url.startsWith(SCHEME)) {
            go(url);
            return true;
        }
        return false;
    }

    private void go(final String url){
        String globalUrl = url.substring(SCHEME.length());
        try {
            String s = URLDecoder.decode(globalUrl, "utf8");
            globalUrl = s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!globalUrl.startsWith("http")) globalUrl = "http://" + globalUrl;
        Uri uri = Uri.parse(globalUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast(mActivity, "无法打开该链接");
        }
    }

}
