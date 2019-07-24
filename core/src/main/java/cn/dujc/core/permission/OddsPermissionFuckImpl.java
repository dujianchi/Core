package cn.dujc.core.permission;

import android.content.Context;

import cn.dujc.core.ui.IBaseUI;
import cn.dujc.core.util.PermissionUtil;
import cn.dujc.core.util.RomUtil;

public class OddsPermissionFuckImpl extends AbstractOddsPermissionOperator {

    private final IBaseUI.IPermissionKeeper mPermissionKeeper;
    private final Context mContext;

    public OddsPermissionFuckImpl(Context context, IBaseUI.IPermissionKeeper permissionKeeper) {
        mPermissionKeeper = permissionKeeper;
        mContext = context;
        if (permissionKeeper != null) {
            permissionKeeper.setOddsPermissionOperator(this);
        }
    }

    @Override
    public boolean useOddsPermissionOperate(Context context) {
        return RomUtil.isVivo() || RomUtil.isOppo();
    }

    @Override
    public boolean requestPermissions(int requestCode, String title, String message, String... permissions) {
        boolean hasPermission = false;
        if (permissions != null && mPermissionKeeper != null) {
            hasPermission = true;
            for (String permission : permissions) {
                if (!mPermissionKeeper.hasPermission(permission)) {
                    hasPermission = hasPermission && PermissionUtil.checkSelfPermission(mContext, permission);
                }
            }
        }
        return hasPermission;
    }

    @Override
    public boolean showConfirmDialog(String... permissions) {
        return true;
    }
}
