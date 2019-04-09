package cn.dujc.coreapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import java.net.URLDecoder;

import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.ToastUtil;

public class WebFragment extends BaseWebFragment {

    //定义一个全局的协议头
    private static final String SCHEME = "vrybh://";

    @Override
    protected boolean _shouldOverrideUrlLoading(WebView view, final String url) {
        if (url.startsWith(SCHEME)) {//如果是以目标协议头开头，则认为是需要跳转浏览器打开链接
            go(url);
            return true;
        }
        return false;
    }

    /*@Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        //定义一个全局的协议头
        private static final String SCHEME = "vrybh://";
        //设置webview的shouldOverrideUrlLoading方法，拦截跳转
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith(SCHEME)) {//如果是以目标协议头开头，则认为是需要跳转浏览器打开链接
                    String globalUrl = url.substring(SCHEME.length());//裁切协议头后面的字符串，当作完整url使用
                    try {
                        String s = URLDecoder.decode(globalUrl, "utf8");//尝试url解码，防止url被编码
                        globalUrl = s;//解码成功即认为是完整url
                    } catch (Exception e) {//解码失败则不赋值，认为第一次截取的url已经是完整url
                        e.printStackTrace();
                    }
                    if (!globalUrl.startsWith("http")) globalUrl = "http://" + globalUrl;//如果截取的url不以http开头，主动为链接添加http://
                    Uri uri = Uri.parse(globalUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        ToastUtil.showToast(mActivity, "无法打开该链接");
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }*/

    private void go(final String url){
        String globalUrl = url.substring(SCHEME.length());//裁切协议头后面的字符串，当作完整url使用
        try {
            String s = URLDecoder.decode(globalUrl, "utf8");//尝试url解码，防止url被编码
            globalUrl = s;//解码成功即认为是完整url
        } catch (Exception e) {//解码失败则不赋值，认为第一次截取的url已经是完整url
            e.printStackTrace();
        }
        if (!globalUrl.startsWith("http")) globalUrl = "http://" + globalUrl;//如果截取的url不以http开头，主动为链接添加http://
        Uri uri = Uri.parse(globalUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.showToast(mActivity, "无法打开该链接");
        }
    }

}
