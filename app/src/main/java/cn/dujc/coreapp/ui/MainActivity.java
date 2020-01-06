package cn.dujc.coreapp.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.initializer.back.IBackPressedOperator;
import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.ui.impl.BaseListActivity;
import cn.dujc.core.ui.impl.BaseWebFragment;
import cn.dujc.core.ui.impl.FragmentShellActivity;
import cn.dujc.core.util.GodDeserializer;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.MediaUtil;
import cn.dujc.core.util.RomUtil;
import cn.dujc.core.util.StringUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.coreapp.entity.Bean;
import cn.dujc.coreapp.entity.Bean1;
import cn.dujc.coreapp.ui.window.Popup;
import cn.dujc.coreapp.util.BeanUtils;
import cn.dujc.widget.resizeable.ResizeableTextImpl;
import cn.dujc.zxing.impl.ZxingActivity;

public class MainActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("toast"
            , "webview"
            , "listview"
            , "save image"
            , "crash"
            , "I am OPPO or VIVO"
            , "gson"
            , "ratingBar"
            , "banner"
            , "go fragment"
            , "check group"
            , "------- other test --------"
            , "zxing activity"
            , "zxing fragment"
            , "show popupWindow"
            , "photoview"
            , "viewpager fragment"
            , "multi type adapter"
            , "send broadcast in thread"
            , "permission"
            , "call"
            , "list dialog"
            , "list popupwindow"
            , "layer layout"
            , "item delete"
            , "CountDownTimer"
            , "wheel picker"
            , "listview"
            , "grid test"
            , "fields"
            , "fragment s"
            //, ""
    );

    private final int requestCodeSdcard = 123;
    private ListDialog mDialog0;
    private ListWindow mDialog1;


    @Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        getAdapter().setEnableLoadMore(false);
        getSwipeRefreshLayout().setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.d(Constants.TEST);
    }

    @Nullable
    @Override
    public IBackPressedOperator backPressOperator() {
        return new IBackPressedOperator.DialogImpl();
    }

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        return new BaseAdapter<CharSequence>(android.R.layout.simple_list_item_1, mList) {
            @Override
            protected void convert(BaseViewHolder helper, CharSequence item) {
                helper.setText(android.R.id.text1, item);
            }
        };
    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            case 0: {
                ToastUtil.showToast(mActivity, "toast");
                //ToastXHandlerActivity.start(mActivity, "toast");
            }
            break;
            case 1: {
                starter().with(BaseWebFragment.EXTRA_URL, "https://d4jc.cc/test4.html")
                        .goFragment(WebFragment.class);
            }
            break;
            case 2: {
                ResizeableTextImpl.setGlobalScale(mActivity, 3.0F);
                starter().go(ListActivity.class);
            }
            break;
            case 3: {
                permissionKeeper().requestPermissions(requestCodeSdcard, "no permission", "need permission"
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
            }
            break;
            case 4: {
                LogUtil.d(Constants.TEST);
                starter().go(CrashActivity.class);
            }
            break;
            case 5: {
                int _10dp = getResources().getDimensionPixelOffset(R.dimen._10dp);
                int _10sp = getResources().getDimensionPixelOffset(R.dimen._10sp);
                ToastUtil.showToast(mActivity, StringUtil.concat("I am OPPO = ", RomUtil.isOppo(), " I am VIVO = ", RomUtil.isVivo(), " dp:sp = ", _10dp, '/', _10sp));
            }
            break;
            case 6: {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Bean.class, new GodDeserializer<Bean>())
                        .create();
                /*Bean bean = new Bean();
                bean.setAaa(111);
                bean.setBbb(111.0F);
                bean.setCcc("111");
                bean.setDdd(true);
                bean.setEee(0.0111);
                Bean1<Bean> bean1 = new Bean1<>();
                bean1.setData(bean);
                System.out.println(gson.toJson(bean1));*/
                Bean<Bean1> bean = gson.fromJson(Constants.JSON_STR, new TypeToken<Bean<Bean1>>() {
                }.getType());
                System.out.println(bean);
                if (bean != null) {
                    List<Bean1> data = bean.getData();
                    System.out.println(data);
                    if (data != null && !data.isEmpty()) {
                        Bean1 bean1 = data.get(0);
                        System.out.println(bean1);
                        if (bean1 != null) System.out.println(bean1.getId());
                    }
                }
            }
            break;
            case 7: {
                starter().go(RatingBarActivity.class);
            }
            break;
            case 8: {
                starter().go(BannerActivity.class);
            }
            break;
            case 9: {
                FragmentShellActivity.start(starter()
                                .with(FragmentShellActivity.EXTRA_TOOLBAR_STYLE, IToolbar.FRAME)
                                .with(BaseWebFragment.EXTRA_URL, "https://www.baidu.com")
                        , WebFragment.class);
            }
            break;
            case 10: {
                starter().go(CheckGroupActivity.class);
            }
            break;
            case 11: {
                /*System.out.println(StringUtil.replaceRange("12345678901", 1, 2, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", 2, 2, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", 2, 5, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", 2, -2, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", -9, 5, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", -9, -2, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", -2, -9, "*"));
                System.out.println(StringUtil.replaceRange("12345678901", 0, 0, "*"));
                System.out.println(StringUtil.replaceRange(null, 0, 3, "*"));
                System.out.println(StringUtil.replaceRange("123", 3, 4, "*"));
                System.out.println(StringUtil.replaceRange("123", 5, 4, "*"));
                System.out.println(StringUtil.repeat("33.", 3));
                System.out.println(StringUtil.repeat("33.", 4));
                System.out.println(StringUtil.repeat("33.", 5));
                System.out.println(StringUtil.repeat("33.", 6));
                System.out.println(StringUtil.repeat("33.", 7));
                System.out.println(StringUtil.concatWithSeparate("-", 7, 8, 9, "a"));*/

                System.out.println(StringUtil.subSequence("12345678901", 1, 2));
                System.out.println(StringUtil.subSequence("12345678901", 2, 2));
                System.out.println(StringUtil.subSequence("12345678901", 2, 5));
                System.out.println(StringUtil.subSequence("12345678901", 2, -2));
                System.out.println(StringUtil.subSequence("12345678901", -9, 5));
                System.out.println(StringUtil.subSequence("12345678901", -9, -2));
                System.out.println(StringUtil.subSequence("12345678901", -2, -9));
                System.out.println(StringUtil.subSequence("12345678901", 0, 0));
                System.out.println(StringUtil.subSequence(null, 0, 3));
                System.out.println(StringUtil.subSequence("12345678901", 3, 4));
                System.out.println(StringUtil.subSequence("123", 3, 4));
                System.out.println(StringUtil.subSequence("123", 5, 4));
            }
            break;
            case 12: {
                starter().go(ZxingActivity.class);
            }
            break;
            case 13: {
                starter().go(ZxingFragmentActivity.class);
            }
            break;
            case 14: {
                new Popup(mActivity).showAtLocation(mRootView);
            }
            break;
            case 15: {
                starter().go(PhotoViewActivity.class);
            }
            break;
            case 16: {
                starter().go(ViewPagerActivity.class);
            }
            break;
            case 17: {
                starter().go(MultiTypeAdapterActivity.class);
            }
            break;
            case 18: {
                starter().go(BroadcastActivity.class);
            }
            break;
            case 19: {
                starter().go(PermissionActivity.class);
            }
            break;
            case 20: {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:18059045652");
                intent.setData(data);
                startActivity(intent);
            }
            break;
            case 21: {
                if (mDialog0 == null) {
                    mDialog0 = new ListDialog(mActivity);
                }
                if (!mDialog0.isShowing()) mDialog0.show();
            }
            break;
            case 22: {
                if (mDialog1 == null) {
                    mDialog1 = new ListWindow(mActivity);
                }
                if (!mDialog1.isShowing()) mDialog1.showAtLocation(mRootView);
            }
            break;
            case 23: {
                starter().go(LayerLayoutActivity.class);
            }
            break;
            case 24: {
                starter().go(ItemDeleteListActivity.class);
            }
            break;
            case 25: {
                starter().go(CountDownTimerActivity.class);
            }
            break;
            case 26: {
                starter().go(WheelPickerActivity.class);
            }
            break;
            case 27: {
                starter().go(ListViewActivity.class);
            }
            break;
            case 28: {
                starter().go(GridViewTestActivity.class);
            }
            break;
            case 29: {
                Bean1 bean = new Bean1();
                Map<String, Object> map = new HashMap<>();
                BeanUtils.copyFiledFromMap(bean, map);
            }
            break;
            case 30: {
                starter().go(FragmentsActivity.class);
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
