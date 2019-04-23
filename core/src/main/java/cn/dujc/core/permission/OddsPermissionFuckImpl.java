package cn.dujc.core.permission;

import android.content.Context;

import cn.dujc.core.ui.IBaseUI;
import cn.dujc.core.util.PermissionUtil;
import cn.dujc.core.util.RomUtil;

public class OddsPermissionFuckImpl implements IOddsPermissionOperator {

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
    public void requestPermissions(int requestCode, String title, String message, String... permissions) {
        if (permissions != null && mPermissionKeeper != null) {
            for (String permission : permissions) {
                if (!mPermissionKeeper.hasPermission(permission)) {
                    PermissionUtil.checkSelfPermission(mContext, permission);
                }
            }
        }
    }

    @Override
    public boolean doneHere(String... permissions) {
        return true;
    }

    @Override
    public boolean showConfirmDialog(String... permissions) {
        return false;
    }
}
