package cn.dujc.coreapp.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.permission.OddsPermissionFuckImpl;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.MediaUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;

public class MainActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("toast"
            , "webview"
            , "listview"
            , "save image"
            , "crash"
            , "if - else", "", "", "", "", "", "");
    private final int requestCodeSdcard = 123;

    @Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        getAdapter().setEnableLoadMore(false);
        getSwipeRefreshLayout().setEnabled(false);
        OddsPermissionFuckImpl.fuckOPPOAndVIVO(mActivity, permissionKeeper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(Constants.TEST);
    }

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new BaseAdapter<String>(android.R.layout.simple_list_item_1, mList) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
            }
        };
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0: {
                ToastUtil.showToast(mActivity, "toast");
            }
            break;
            case 1: {
                starter().with(BaseWebFragment.EXTRA_URL, "https://d4jc.cc/test4.html")
                        .goFragment(WebFragment.class);
            }
            break;
            case 2: {
                starter().go(ListActivity.class);
            }
            break;
            case 3: {
                permissionKeeper().requestPermissions(requestCodeSdcard, "no permission", "need permission", Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            break;
            case 4: {
                starter().go(CrashActivity.class);
            }
            break;
            case 5: {
                boolean showHint;
                String settingsDialog, permissionOperator;
                // ---- 1 --------
                showHint = true;
                settingsDialog = null;
                permissionOperator = null;
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("1 ---------------   yes");
                } else {
                    System.out.println("1 ---------------    no");
                }
                // ---- 2 --------
                showHint = false;
                settingsDialog = null;
                permissionOperator = null;
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("2 ---------------   yes");
                } else {
                    System.out.println("2 ---------------    no");
                }
                // ---- 3 --------
                showHint = true;
                settingsDialog = "1";
                permissionOperator = null;
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("3 ---------------   yes");
                } else {
                    System.out.println("3 ---------------    no");
                }
                // ---- 4 --------
                showHint = false;
                settingsDialog = "1";
                permissionOperator = null;
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("4 ---------------   yes");
                } else {
                    System.out.println("4 ---------------    no");
                }
                // ---- 5 --------
                showHint = true;
                settingsDialog = "1";
                permissionOperator = "1";
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("5 ---------------   yes");
                } else {
                    System.out.println("5 ---------------    no");
                }
                // ---- 6 --------
                showHint = false;
                settingsDialog = "1";
                permissionOperator = "1";
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("6 ---------------   yes");
                } else {
                    System.out.println("6 ---------------    no");
                }
                // ---- 7 --------
                showHint = true;
                settingsDialog = null;
                permissionOperator = "1";
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("7 ---------------   yes");
                } else {
                    System.out.println("7 ---------------    no");
                }
                // ---- 8 --------
                showHint = false;
                settingsDialog = null;
                permissionOperator = "1";
                if (settingsDialog != null && (showHint || permissionOperator != null && permissionOperator.equals("1"))) {
                    System.out.println("8 ---------------   yes");
                } else {
                    System.out.println("8 ---------------    no");
                }
            }
            break;
            //case 00:{}break;
        }
    }

    @Override
    public void loadMore() {
    }

    @Override
    public void reload() {
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        if (requestCode == requestCodeSdcard) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test);
            String img = MediaUtil.saveImgToGallery(mActivity, bitmap, "bbb", "bbb.png");
            ToastUtil.showToast(mActivity, img);
        }
    }
}
