package cn.dujc.coreapp.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import cn.dujc.core.adapter.BaseAdapter;
import cn.dujc.core.adapter.BaseQuickAdapter;
import cn.dujc.core.adapter.BaseViewHolder;
import cn.dujc.core.ui.BaseListActivity;
import cn.dujc.core.util.LogUtil;
import cn.dujc.core.util.ToastUtil;

public class PermissionActivity extends BaseListActivity {

    @Nullable
    @Override
    public BaseQuickAdapter initAdapter() {
        BaseAdapter<String> adapter = new BaseAdapter<String>(android.R.layout.simple_list_item_1, Arrays.asList(
                Manifest.permission.CAMERA
                , Manifest.permission.RECORD_AUDIO
                , Manifest.permission.BODY_SENSORS
                , Manifest.permission.WRITE_CALENDAR
                , Manifest.permission.GET_ACCOUNTS
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE
                , Manifest.permission.SEND_SMS
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.CALL_PHONE
                //, Manifest.permission.XX
        )) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(android.R.id.text1, item);
            }
        };
        adapter.setEnableLoadMore(false);
        return adapter;
    }

    @Override
    public void initBasic(Bundle savedInstanceState) {
        super.initBasic(savedInstanceState);
        refreshEnable(false);
    }

    @Override
    public void onItemClick(int position) {
        String permission = String.valueOf(getAdapter().getItem(position));
        LogUtil.d(permission);
        permissionKeeper().requestPermissionsNormal(position, permission);
    }

    @Override
    public void loadMore() {
        loadDone(true, true);
    }

    @Override
    public void reload() {
        refreshDone();
    }

    @Override
    public void onGranted(int requestCode, List<String> permissions) {
        ToastUtil.showToast(mActivity, String.valueOf(permissions));
    }
}
