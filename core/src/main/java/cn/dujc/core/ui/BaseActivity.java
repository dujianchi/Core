package cn.dujc.core.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.dujc.core.R;
import cn.dujc.core.app.Core;
import cn.dujc.core.bridge.ActivityStackUtil;
import cn.dujc.core.initializer.back.IBackPressedOperator;
import cn.dujc.core.initializer.content.IRootViewSetupHandler;
import cn.dujc.core.initializer.permission.IPermissionSetup;
import cn.dujc.core.initializer.permission.IPermissionSetupHandler;
import cn.dujc.core.initializer.toolbar.IToolbar;
import cn.dujc.core.initializer.toolbar.IToolbarHandler;
import cn.dujc.core.permission.IOddsPermissionOperator;
import cn.dujc.core.util.ToastUtil;

/**
 * 基本的Activity。所有Activity必须继承于此类。“所有”！
 * Created by du on 2017/9/19.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseUI.WithToolbar, IBaseUI.IPermissionKeeperCallback {

    private IStarter mStarter = null;
    private IParams mParams = null;
    private IPermissionKeeper mPermissionKeeper = null;
    private TitleCompat mTitleCompat = null;
    //返回操作
    private volatile IBackPressedOperator mBackPressOperator = null;

    protected View mToolbar = null;
    protected View mRootView = null;
    protected Activity mActivity;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = this;
        if (alwaysPortrait()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);//竖屏
        }

        if (fullScreen()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        initAnimEnter();
        final int vid = getViewId();
        mRootView = getViewV();

        super.onCreate(savedInstanceState);
        //ActivityStackUtil.getInstance().addActivity(this);

        if (vid != 0 || mRootView != null) {
            mRootView = vid == 0 ? mRootView : getLayoutInflater().inflate(vid, null);
            View rootView = createRootView(mRootView);
            setContentView(rootView);
            rootViewSetup(rootView);
            mTitleCompat = initTransStatusBar();//这句一定要放在setcontent之后，initbasic之前，否则一些沉浸效果无法显现（改逻辑除外）
            initBasic(savedInstanceState);
            setupToolbar();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        initAnimExit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroyRootViewAndToolbar();
        //ActivityStackUtil.getInstance().removeActivity(this);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        //ActivityStackUtil.getInstance().addFragment(this, fragment);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressOperator == null) mBackPressOperator = backPressOperator();
        if (mBackPressOperator == null || mBackPressOperator.calculateCanBack()) {
            super.onBackPressed();
        } else {
            mBackPressOperator.showBackConfirm(mActivity);
        }
    }

    @Override
    @Nullable
    public View createRootView(View contentView) {
        final View[] viewAndToolbar = BaseToolbarHelper.createRootViewAndToolbar(toolbarStyle(), mActivity, this, contentView);
        mToolbar = viewAndToolbar[1];
        return viewAndToolbar[0];
    }

    @Nullable
    public IBackPressedOperator backPressOperator() {
        return null;
    }

    @Nullable
    public TitleCompat initTransStatusBar() {
        if (mTitleCompat == null) mTitleCompat = TitleCompat.setStatusBar(mActivity, true);
        IToolbarHandler.statusColor(this, mActivity, mTitleCompat);
        return mTitleCompat;
    }

    @Override
    public void rootViewSetup(View rootView) {
        IRootViewSetupHandler.setup(this, this, rootView);
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public TitleCompat getTitleCompat() {
        return mTitleCompat;
    }

    @Override
    @Nullable
    public View initToolbar(ViewGroup parent) {
        View toolbar = mToolbar;
        if (toolbar == null) {
            toolbar = IToolbarHandler.generateToolbar(this, parent, this);
        }
        return toolbar;
    }

    @Override
    public IStarter starter() {
        if (mStarter == null) mStarter = new IStarterImpl(this);
        else mStarter.clear();//为什么要clear呢？想了想，实际上我用的一直是同一个starter，那么，如果一直界面往不同界面都传了值，它就会一直累加……
        return mStarter;
    }

    @Override
    public IParams extras() {
        if (mParams == null) mParams = new ActivityParamsImpl(this);
        return mParams;
    }

    @Override
    public IPermissionKeeper permissionKeeper() {
        if (mPermissionKeeper == null) {
            mPermissionKeeper = new IPermissionKeeperImpl(this, this);
            permissionKeeperSetup();
        }
        return mPermissionKeeper;
    }

    @Override
    public void permissionKeeperSetup() {
        //if (mPermissionKeeper != null) {
        final IPermissionSetup setup = IPermissionSetupHandler.getSetup(mActivity);
        final IOddsPermissionOperator operator;
        if (setup != null && (operator = setup.getOddsPermissionOperator(mActivity, mPermissionKeeper)) != null) {
            mPermissionKeeper.setOddsPermissionOperator(operator);
        }
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnActivityResult(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionKeeper != null) {
            mPermissionKeeper.handOnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
    }

    @Override
    public void onDenied(int requestCode, List<String> permissions) {
        ToastUtil.showToast(mActivity, getString(R.string.core_permission_denied));
    }

    /**
     * 关联主界面 **只有在使用自定义View时使用**
     */
    @Override
    @Nullable
    public View getViewV() {
        return null;
    }

    /**
     * 是否线性排列toolbar，否的话则toolbar在布局上方
     */
    @IToolbar.Style
    @Override
    public int toolbarStyle() {
        final IToolbar iToolbar = IToolbarHandler.getToolbar(this, mActivity);
        if (iToolbar != null) return iToolbar.toolbarStyle();
        return IToolbar.LINEAR;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mToolbar != null) {
            final View textMaybe = mToolbar.findViewById(R.id.core_toolbar_title_id);
            if (textMaybe instanceof TextView) ((TextView) textMaybe).setText(title);
        }
    }

    public void setTitleMenuText(CharSequence menuText, @Nullable View.OnClickListener onClickListener) {
        setTitleMenuText(menuText, 0, onClickListener);
    }

    /**
     * 设置菜单
     *
     * @param position 所在位置，最大支持4个，为-1时全部隐藏
     *                 ，position传入0到3之间的值时，如果找到的控件类型为TextView
     *                 ，且其所在父类的第index值大于等于position，则认为是要寻找的菜单控件
     */
    public void setTitleMenuText(CharSequence menuText, @IntRange(from = -1, to = 3) int position, @Nullable View.OnClickListener onClickListener) {
        if (mToolbar != null) {
            final View textMaybe = mToolbar.findViewById(R.id.core_toolbar_menu_id);
            if (textMaybe instanceof TextView) {
                textMaybe.setVisibility(View.VISIBLE);
                ((TextView) textMaybe).setText(menuText);
                if (onClickListener != null) textMaybe.setOnClickListener(onClickListener);
            } else if (textMaybe instanceof ViewGroup) {
                if (position == -1) {
                    textMaybe.setVisibility(View.GONE);
                } else {
                    final ViewGroup viewGroup = (ViewGroup) textMaybe;
                    for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                        final View childAt = viewGroup.getChildAt(index);
                        if (position <= index && childAt instanceof TextView) {
                            textMaybe.setVisibility(View.VISIBLE);
                            childAt.setVisibility(View.VISIBLE);
                            ((TextView) childAt).setText(menuText);
                            if (onClickListener != null)
                                textMaybe.setOnClickListener(onClickListener);
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setTitleMenuIcon(@DrawableRes int menuRes, @Nullable View.OnClickListener onClickListener) {
        setTitleMenuIcon(menuRes, 0, onClickListener);
    }

    /**
     * 设置菜单
     *
     * @param position 所在位置，最大支持4个，为-1时全部隐藏
     *                 ，position传入0到3之间的值时，如果找到的控件类型为ImageView
     *                 ，且其所在父类的第index值大于等于position，则认为是要寻找的菜单控件
     */
    public void setTitleMenuIcon(@DrawableRes int menuRes, @IntRange(from = -1, to = 3) int position, @Nullable View.OnClickListener onClickListener) {
        if (mToolbar != null) {
            final View imageMaybe = mToolbar.findViewById(R.id.core_toolbar_menu_id);
            if (imageMaybe instanceof ImageView) {
                imageMaybe.setVisibility(View.VISIBLE);
                ((ImageView) imageMaybe).setImageResource(menuRes);
                if (onClickListener != null) imageMaybe.setOnClickListener(onClickListener);
            } else if (imageMaybe instanceof ViewGroup) {
                if (position == -1) {
                    imageMaybe.setVisibility(View.GONE);
                } else {
                    final ViewGroup viewGroup = (ViewGroup) imageMaybe;
                    for (int index = 0, count = viewGroup.getChildCount(); index < count; index++) {
                        final View childAt = viewGroup.getChildAt(index);
                        if (position <= index && childAt instanceof ImageView) {
                            imageMaybe.setVisibility(View.VISIBLE);
                            childAt.setVisibility(View.VISIBLE);
                            ((ImageView) childAt).setImageResource(menuRes);
                            if (onClickListener != null)
                                imageMaybe.setOnClickListener(onClickListener);
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void setupToolbar() {
        if (mToolbar instanceof Toolbar) {
            Toolbar toolbar = (Toolbar) mToolbar;
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    /*protected void destroyRootViewAndToolbar() {
        if (mToolbar != null) {
            final ViewParent parent = mToolbar.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mToolbar);
            }
            mToolbar = null;
        }
        if (mRootView != null) {
            final ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mRootView);
            }
            mRootView = null;
        }
    }*/

    /**
     * 退出动画
     */
    protected void initAnimExit() {
        overridePendingTransition(R.anim.core_push_pic_right_in, R.anim.core_push_pic_right_out);
    }

    /**
     * 进入动画
     */
    protected void initAnimEnter() {
        overridePendingTransition(R.anim.core_push_pic_left_in, R.anim.core_push_pic_left_out);
    }

    /**
     * 是否全屏
     *
     * @return true则全屏
     */
    protected boolean fullScreen() {
        return false;
    }

    /**
     * 是否强制竖屏
     *
     * @return true则强制竖屏
     */
    protected boolean alwaysPortrait() {
        return true;
    }

    protected void showFragment(@IdRes int containerViewId, Fragment fragment) {
        showFragment(containerViewId, fragment, null);
    }

    protected void showFragment(@IdRes int containerViewId, Fragment fragment, @Nullable String tag) {
        showFragmentInManager(getSupportFragmentManager(), containerViewId, fragment, tag);
    }

    private void showFragmentInManager(FragmentManager manager, @IdRes int containerViewId, Fragment fragment, @Nullable String tag) {
        if (manager == null) return;
        if (mCurrentFragment != null) {
            manager.beginTransaction().hide(mCurrentFragment).commit();
        }
        mCurrentFragment = fragment;
        if (fragment == null) return;
        if (!fragment.isAdded()) {
            manager.beginTransaction().add(containerViewId, fragment, tag).commit();
        } else {
            manager.beginTransaction().show(fragment).commit();
        }
    }

    /**
     * 清除fragment，用于Activity崩溃时，重启Activity后不自动添加fragment
     */
    protected void clearFragmentsBeforeRestoreInstanceExcept(Bundle outState, Fragment... fragments) {
        try {
            List<Fragment> list = fragments == null ? Collections.<Fragment>emptyList() : Arrays.asList(fragments);
            if (ActivityStackUtil.getInstance().topActivity() != this) return;
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            for (Fragment fragment : manager.getFragments()) {
                if (!list.contains(fragment)) transaction.remove(fragment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
    }

}
