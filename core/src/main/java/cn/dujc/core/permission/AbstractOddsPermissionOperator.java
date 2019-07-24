package cn.dujc.core.permission;

import android.content.Context;

import cn.dujc.core.util.PermissionUtil;

public abstract class AbstractOddsPermissionOperator implements IOddsPermissionOperator {

    @Override
    public boolean doneHere(Context context, String... permissions) {
        boolean hasPermission = true;
        if (permissions != null) {
            for (String per : permissions) {
                hasPermission = hasPermission && PermissionUtil.checkSelfPermission(context, per);
            }
        }
        return hasPermission;
    }

    @Override
    public boolean showConfirmDialog(String... permissions) {
        return false;
    }
}
