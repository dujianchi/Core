package cn.dujc.core.initializer.back;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import cn.dujc.core.R;
import cn.dujc.core.util.ToastUtil;

public interface IBackPressedOperator {

    public long limitTimeMillis();

    public boolean calculateCanBack();

    public void showBackConfirm(Activity activity);

    public static class Impl implements IBackPressedOperator {

        private long mLastBack = 0L;

        public long limitTimeMillis() {
            return 2000;
        }

        public boolean calculateCanBack() {
            long current = System.currentTimeMillis();
            boolean canBack = current - mLastBack < limitTimeMillis();
            mLastBack = current;
            return canBack;
        }

        public void showBackConfirm(Activity activity) {
            ToastUtil.showToast(activity, R.string.core_back_confirm);
        }
    }

    public static class DialogImpl implements IBackPressedOperator {

        private AlertDialog mDialog = null;

        @Override
        public long limitTimeMillis() {
            return 0;
        }

        @Override
        public boolean calculateCanBack() {
            return false;
        }

        @Override
        public void showBackConfirm(final Activity activity) {
            if (mDialog == null){
                mDialog = new AlertDialog.Builder(activity)
                        .setTitle(R.string.core_back_confirm_title)
                        .setMessage(R.string.core_back_confirm_message)
                        .setNegativeButton(R.string.core_back_confirm_negative, null)
                        .setPositiveButton(R.string.core_back_confirm_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        })
                        .create();
            }
            if (!mDialog.isShowing()) mDialog.show();
        }
    }
}
