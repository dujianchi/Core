package cn.dujc.core.initializer.toolbar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import cn.dujc.core.ui.IBaseUI;

public interface IToolbar {

    public static final int NONE = 0;
    public static final int LINEAR = 1, FRAME = 2, COORDINATOR = 3;
    public final static int AUTO = 1//自动
            , DARK = 2//深色
            , LIGHT = 3;//浅色

    View normal(ViewGroup parent);

    int statusBarColor(Context context);

    // /**
    //  * 推荐实现IToolbar的用单例模式，然后单例的方法用这个注解
    //  */
    // @Retention(RetentionPolicy.RUNTIME)
    // @Target(ElementType.METHOD)
    // public static @interface Instance {}

    @StatusBarMode
    int statusBarMode();

    @Style
    int toolbarStyle();

    // 原本考虑到要做多个通用toolbar，于是有包含和排除的规则。后来想想，一个app内通常只有一
    // 个通用的toolbar样式，其他样式通常也是在某个界面单独使用，没必要做多个样式，于是去掉了相应的规则
    // 2019.05.14 还是碰到了多个类型toolbar的情况，经过思考，还是可以做多个toolbar的，于是再把这个放出来
    List<Class<? extends IBaseUI>> include();

    List<Class<? extends IBaseUI>> exclude();

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE//无
            , LINEAR//线性排列
            , FRAME//帧（层叠）排列
            , COORDINATOR//协调布局排列
    })
    @interface Style {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NONE//不操作
            , AUTO//自动
            , DARK//深色
            , LIGHT//浅色
    })
    @interface StatusBarMode {
    }

}
