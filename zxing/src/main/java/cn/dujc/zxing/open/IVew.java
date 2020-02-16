package cn.dujc.zxing.open;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

public interface IVew {

    @NonNull
    Activity getActivity();

    <T extends View> T findViewById(@IdRes int resId);

    public static class ActivityImpl implements IVew {
        @NonNull
        private final Activity mActivity;

        public ActivityImpl(@NonNull Activity activity) {
            mActivity = activity;
        }

        @NonNull
        @Override
        public Activity getActivity() {
            return mActivity;
        }

        @Override
        public <T extends View> T findViewById(int resId) {
            return (T) mActivity.findViewById(resId);
        }
    }
}
