package cn.dujc.coreapp.impl;

import android.content.Context;

import cn.dujc.core.initializer.permission.IPermissionSetup;
import cn.dujc.core.permission.IOddsPermissionOperator;
import cn.dujc.core.permission.OddsPermissionFuckImpl;
import cn.dujc.core.ui.IBaseUI;

public class PermissionHelper implements IPermissionSetup {

    @Override
    public IOddsPermissionOperator getOddsPermissionOperator(Context context, IBaseUI.IPermissionKeeper permissionKeeper) {
        return new OddsPermissionFuckImpl(context, permissionKeeper)/*{
            @Override
            public boolean doneHere(String... permissions) {
                return false;
            }
        }*/;
    }
}
