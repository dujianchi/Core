package cn.dujc.core.initializer.permission;

import android.content.Context;

import cn.dujc.core.permission.IOddsPermissionOperator;
import cn.dujc.core.permission.OddsPermissionFuckImpl;
import cn.dujc.core.ui.IBaseUI;

public interface IPermissionSetup {

    public IOddsPermissionOperator getOddsPermissionOperator(Context context, IBaseUI.IPermissionKeeper permissionKeeper);

    public static class DefaultImpl implements IPermissionSetup {
        @Override
        public IOddsPermissionOperator getOddsPermissionOperator(Context context, IBaseUI.IPermissionKeeper permissionKeeper) {
            return new OddsPermissionFuckImpl(context, permissionKeeper);
        }
    }
}
