package cn.dujc.core.initializer.permission;

import android.content.Context;

import cn.dujc.core.permission.IOddsPermissionOperator;
import cn.dujc.core.ui.IBaseUI;

public interface IPermissionSetup {

    public IOddsPermissionOperator getOddsPermissionOperator(Context context, IBaseUI.IPermissionKeeper permissionKeeper);

}
