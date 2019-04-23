package cn.dujc.core.permission;

public abstract class AbstractOddsPermissionOperator implements IOddsPermissionOperator {

    @Override
    public boolean doneHere(String... permissions) {
        return false;
    }

    @Override
    public boolean showConfirmDialog(String... permissions) {
        return false;
    }
}
