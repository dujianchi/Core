package cn.dujc.coreapp.ui;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.initializer.back.IBackPressedOperator;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.ui.BaseWebFragment;
import cn.dujc.core.util.GodDeserializer;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.MediaUtil;
import cn.dujc.core.util.RomUtil;
import cn.dujc.core.util.StringUtil;
import cn.dujc.core.util.ToastUtil;
import cn.dujc.coreapp.R;
import cn.dujc.coreapp.entity.Bean;
import cn.dujc.coreapp.entity.Bean1;

public class MainActivity extends BaseListActivity {

    private final List<String> mList = Arrays.asList("toast"
            , "webview"
            , "listview"
            , "save image"
            , "crash"
            , "I am OPPO or VIVO"
            , "gson"
            , "ratingBar"
            , "banner", "", "", "");

    private final int requestCodeSdcard = 123;

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
                ToastUtil.showToast(mActivity, StringUtil.concat("I am OPPO = ", RomUtil.isOppo(), " I am VIVO = ", RomUtil.isVivo()));
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
            case 8:{
                starter().go(BannerActivity.class);
            }break;
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
