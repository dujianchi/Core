package cn.dujc.core.util;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.util.UUID;

import cn.dujc.core.R;
import cn.dujc.core.app.Core;

public class DeviceUtil {
    private static final String SAVED_INFO = "SAVED_INFO", DEVICE_ID = "DEVICE_ID";

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.BLUETOOTH
            , Manifest.permission.ACCESS_WIFI_STATE
    })
    @NonNull
    public static String getDeviceId(Context context) {
        SharedPreferences savedInfo = context.getSharedPreferences(SAVED_INFO, Context.MODE_PRIVATE);
        String id = savedInfo.getString(DEVICE_ID, "");
        if (TextUtils.isEmpty(id)) {
            File pubDir = MediaUtil.getOutputDir(context, Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? Environment.DIRECTORY_DOCUMENTS : Environment.DIRECTORY_PICTURES
                    , "." + context.getString(R.string.app_name));
            SerializableTransfer transfer = null;
            if (pubDir != null) {
                transfer = new SerializableTransfer(new File(pubDir, ".nomedia"));
                Object read = transfer.read();
                if (read != null) id = (String) read;
            }
            if (TextUtils.isEmpty(id)) {
                id = uuid(context);
                if (transfer != null) transfer.save(id);
            }
            savedInfo.edit().putString(DEVICE_ID, id).apply();
        }
        return id;
    }

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.BLUETOOTH
            , Manifest.permission.ACCESS_WIFI_STATE
    })
    @NonNull
    private static String uuid(Context context) {
        final String fakeMac = "02:00:00:00:00:00";
        String tmDevice = null;
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tmDevice = tm.getDeviceId();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        if (!TextUtils.isEmpty(tmDevice)) return tmDevice;
        String androidId = null;
        try {
            androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        if (!TextUtils.isEmpty(androidId)) return androidId;
        String tmSerial = null;
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tmSerial = tm.getSimSerialNumber();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        if (!TextUtils.isEmpty(tmSerial)) return tmSerial;
        String macAddress = null;
        try {
            macAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        if (TextUtils.isEmpty(macAddress) || fakeMac.equals(macAddress)) {
            try {
                WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                macAddress = wifiMan.getConnectionInfo().getMacAddress();
            } catch (Exception e) {
                if (Core.DEBUG) e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(macAddress) && !fakeMac.equals(macAddress)) return macAddress;
        try {
            UUID deviceUuid = UUID.randomUUID();
            return deviceUuid.toString().replace("-", "");
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
            return "";
        }
    }
}
