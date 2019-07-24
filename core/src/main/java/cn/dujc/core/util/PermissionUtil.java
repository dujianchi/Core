package cn.dujc.core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;

/**
 * 本类方法来自 https://blog.csdn.net/u012359453/article/details/87729018 ，稍作修改
 * 对所有危险权限分组做权限判断
 * 所属权限组	权限名	权限等级	解释
 * 日历	READ_CALENDAR	危险	允许应用程序读取用户的日历数据
 * 日历	WRITE_CALENDAR	危险	允许应用程序写入用户的日历数据
 * 相机	CAMERA	危险	使用摄像头做相关工作
 * 联系人	READ_CONTACTS	危险	读取联系人
 * 联系人	WRITE_CONTACTS	危险	写入联系人
 * 联系人	GET_ACCOUNTS	危险	允许访问帐户服务中的帐户列表
 * 位置	ACCESS_FINE_LOCATION	危险	允许应用访问精确位置
 * 位置	ACCESS_COARSE_LOCATION	危险	允许应用访问大致位置
 * 麦克风	RECORD_AUDIO	危险	麦克风的使用
 * 电话	READ_PHONE_STATE	危险	允许对电话状态进行只读访问,包括设备的电话号码，当前蜂窝网络信息,任何正在进行的呼叫的状态以及设备上注册的任何PhoneAccounts列表
 * 电话	CALL_PHONE	危险	允许应用程序在不通过拨号器用户界面的情况下发起电话呼叫，以便用户确认呼叫
 * 电话	READ_CALL_LOG	危险	允许应用程序读取用户的通话记录
 * 电话	WRITE_CALL_LOG	危险	允许应用程序写入（但不读取）用户的呼叫日志数据
 * 电话	ADD_VOICEMAIL	危险	允许应用程序将语音邮件添加到系统中
 * 电话	USE_SIP	危险	允许应用程序使用SIP服务
 * 电话	PROCESS_OUTGOING_CALLS	危险	允许应用程序查看拨出呼叫期间拨打的号码，并选择将呼叫重定向到其他号码或完全中止呼叫
 * 传感器	BODY_SENSORS	危险	允许应用程序访问来自传感器的数据
 * 短信	SEND_SMS	危险	允许应用程序发送SMS消息
 * 短信	RECEIVE_SMS	危险	允许应用程序接收SMS消息
 * 短信	READ_SMS	危险	允许应用程序读取SMS消息
 * 短信	RECEIVE_WAP_PUSH	危险	允许应用程序接收WAP推送消息
 * 短信	RECEIVE_MMS	危险	允许应用程序监视传入的MMS消息（彩信）
 * 存储	READ_EXTERNAL_STORAGE	危险	允许应用程序从外部存储读取
 * 存储	WRITE_EXTERNAL_STORAGE	危险	允许应用程序写入外部存储
 * <p>
 * <p>
 * 防止权限判断错误，使用尝试性敲击权限检查，对分组的权限做哥敲击就性
 */
public class PermissionUtil {
    /**
     * 权限查询对外公布方法
     *
     * @param permission
     * @return
     */
    public static boolean checkSelfPermission(Context context, String permission) {
        // 对于非oppo和vivo手机，做正常的权限申请
        if (!RomUtil.isOppo() && !RomUtil.isVivo()) {
            return selfPermissionGranted(context, permission);
        }
        // 对于国内部分机型做非正常权限申请
        switch (permission) {
            case Manifest.permission.CAMERA:
                return checkCameraPermissions(context);

            case Manifest.permission.RECORD_AUDIO:
                return checkAudioPermission(context);

            case Manifest.permission.BODY_SENSORS:
                return checkSensorsPermission(context);

            case Manifest.permission.READ_CALENDAR:
            case Manifest.permission.WRITE_CALENDAR:
                return checkCalenderPermission(context, permission);

            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.WRITE_CONTACTS:
            case Manifest.permission.GET_ACCOUNTS:
                return checkContactsPermission(context, permission);

            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return checkLocationsPermission(context, permission);

            case Manifest.permission.READ_PHONE_STATE:
            case Manifest.permission.CALL_PHONE:
            case Manifest.permission.READ_CALL_LOG:
            case Manifest.permission.WRITE_CALL_LOG:
            case Manifest.permission.ADD_VOICEMAIL:
            case Manifest.permission.USE_SIP:
            case Manifest.permission.PROCESS_OUTGOING_CALLS:
                return checkReadPhoneStatePermission(context, permission);

            case Manifest.permission.SEND_SMS:
            case Manifest.permission.RECEIVE_SMS:
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_WAP_PUSH:
            case Manifest.permission.RECEIVE_MMS:
                return checkSMSPermission(context, permission);

            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return checkWritePermission(context, permission);

            default:
                return selfPermissionGranted(context, permission);
        }
    }


    /**
     * READ_PHONE_STATE
     *
     * @return 是否有读取手机状态的权限
     * 电话	READ_PHONE_STATE	危险	允许对电话状态进行只读访问,包括设备的电话号码，当前蜂窝网络信息,任何正在进行的呼叫的状态以及设备上注册的任何PhoneAccounts列表
     * 电话	CALL_PHONE	危险	允许应用程序在不通过拨号器用户界面的情况下发起电话呼叫，以便用户确认呼叫
     * 电话	READ_CALL_LOG	危险	允许应用程序读取用户的通话记录
     * 电话	WRITE_CALL_LOG	危险	允许应用程序写入（但不读取）用户的呼叫日志数据
     * 电话	ADD_VOICEMAIL	危险	允许应用程序将语音邮件添加到系统中
     * 电话	USE_SIP	危险	允许应用程序使用SIP服务
     * 电话	PROCESS_OUTGOING_CALLS	危险	允许应用程序查看拨出呼叫期间拨打的号码，并选择将呼叫重定向到其他号码或完全中止呼叫
     * 耗时3ms左右
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static boolean checkReadPhoneStatePermission(Context context, String permission) {
        boolean hasPermission = true;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tm.getDeviceId();
        } catch (Exception e) {
            hasPermission = false;
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }


    /**
     * 检查是否拥有读写权限
     * WRITE_EXTERNAL_STORAGE
     * 耗时 1-12ms
     *
     * @return
     */
    public static boolean checkWritePermission(Context context, String permission) {
        boolean hasPermission = true;
        File file = null;
        FileOutputStream outputStream = null;
        try {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".permission.tmp.check");
            outputStream = new FileOutputStream(file);
            outputStream.flush();
        } catch (Exception e) {
            hasPermission = false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (file != null) {
                file.delete();
            }
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }

    /**
     * 日历	READ_CALENDAR	危险	允许应用程序读取用户的日历数据
     * 日历	WRITE_CALENDAR	危险	允许应用程序写入用户的日历数据
     * 检查日历权限
     * 耗时 30ms左右
     *
     * @return
     */
    public static boolean checkCalenderPermission(Context context, String permission) {
        boolean hasPermission = true;
        Cursor cursor = null;
        try {
            String CALENDER_EVENT_URL = "content://com.android.calendar/events";
            Uri uri = Uri.parse(CALENDER_EVENT_URL);
            cursor = context.getContentResolver().query(uri, null, null, null, null);
        } catch (Exception e) {
            hasPermission = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }


    /**
     * 检测相机权限，这个因为直接拿camera还不一定行，当前测试机型：oppo 魅族 小米
     * * 相机	CAMERA	危险	使用摄像头做相关工作
     *
     * @return
     */
    public static boolean checkCameraPermissions(Context context) {
        boolean hasPermission = true;
        Camera mCamera = null;
        try {
            // 大众路线 过了基本就是有权限
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);

            // 特殊路线，oppo和vivo得根据rom去反射mHasPermission
            if ((RomUtil.isOppo() || RomUtil.isVivo()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Field fieldHas;
                if (mCamera == null) mCamera = Camera.open();
                //通过反射去拿相机是否获得了权限
                fieldHas = mCamera.getClass().getDeclaredField("mHasPermission");
                fieldHas.setAccessible(true);
                Boolean result = (Boolean) fieldHas.get(mCamera);
                if (mCamera != null) {
                    mCamera.release();
                }
                mCamera = null;
                return result;
            }
        } catch (Exception e) {
            hasPermission = false;
        } finally {
            if (mCamera != null) {
                mCamera.release();
            }
            mCamera = null;
        }
        return selfPermissionGranted(context, Manifest.permission.CAMERA) || hasPermission;
    }

    /**
     * 联系人	READ_CONTACTS	危险	读取联系人
     * 联系人	WRITE_CONTACTS	危险	写入联系人
     * 联系人	GET_ACCOUNTS	危险	允许访问帐户服务中的帐户列表
     *
     * @return
     */
    public static boolean checkContactsPermission(Context context, String permission) {
        boolean hasPermission = true;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        } catch (Exception e) {
            hasPermission = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }

    /**
     * 位置	ACCESS_FINE_LOCATION	危险	允许应用访问精确位置
     * 位置	ACCESS_COARSE_LOCATION	危险	允许应用访问大致位置
     *
     * @return
     */
    public static boolean checkLocationsPermission(Context context, String permission) {
        boolean hasPermission = true;
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // 无用操作
            lm.getAllProviders();
        } catch (Exception e) {
            hasPermission = false;
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }

    /**
     * 麦克风	RECORD_AUDIO  危险	麦克风的使用
     *
     * @return
     */
    public static boolean checkAudioPermission(Context context) {
        boolean hasPermission = true;
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            // 无用操作
            audioManager.getMode();
        } catch (Exception e) {
            hasPermission = false;
        }
        return selfPermissionGranted(context, Manifest.permission.RECORD_AUDIO) || hasPermission;
    }

    /**
     * 传感器	BODY_SENSORS	危险	允许应用程序访问来自传感器的数据
     *
     * @return
     */
    public static boolean checkSensorsPermission(Context context) {
        boolean hasPermission = true;
        try {
            SensorManager sorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            // 无用操作
            sorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } catch (Exception e) {
            hasPermission = false;
        }
        return selfPermissionGranted(context, Manifest.permission.BODY_SENSORS) || hasPermission;
    }


    /**
     * 短信	SEND_SMS	危险	允许应用程序发送SMS消息
     * 短信	RECEIVE_SMS	危险	允许应用程序接收SMS消息
     * 短信	READ_SMS	危险	允许应用程序读取SMS消息
     * 短信	RECEIVE_WAP_PUSH	危险	允许应用程序接收WAP推送消息
     * 短信	RECEIVE_MMS	危险	允许应用程序监视传入的MMS消息（彩信）
     * * @return
     */
    public static boolean checkSMSPermission(Context context, String permission) {
        boolean hasPermission = true;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://sms/failed");
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type",};
            cursor = context.getContentResolver().query(uri, projection, null,
                    null, "date desc");
        } catch (Exception e) {
            hasPermission = false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return selfPermissionGranted(context, permission) || hasPermission;
    }


    /**
     * api级别权限查询
     *
     * @param permission
     * @return
     */
    public static boolean selfPermissionGranted(Context context, String permission) {
        //return PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
        return ContextCompat.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
    }

}
