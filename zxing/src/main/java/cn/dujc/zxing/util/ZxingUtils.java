package cn.dujc.zxing.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;
import java.util.Vector;

import cn.dujc.core.util.LogUtil;

/**
 * Created by Du JC on 2016/6/30.
 */
public class ZxingUtils {

    private ZxingUtils() {
    }

    /**
     * 字符串生成二维码
     *
     * @param string
     * @return
     */
    public static Bitmap createQRCode(String string, int width, int height) {
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(string)) {
            LogUtil.e("the string is null or length == 0");
            return bitmap;
        }
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 1);
            BitMatrix bitMatrix = new QRCodeWriter().encode(string, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            LogUtil.e("QRCode Error: " + e.getMessage());
        }
        return bitmap;
    }

    private static Result decodeBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return null;
        MultiFormatReader multiFormatReader = new MultiFormatReader();

        //Bitmap bitmap = Tools.shrinkImage(bitmapOrigin, 32, false);

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();
            decodeFormats.add(BarcodeFormat.QR_CODE);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置继续的字符编码格式为UTF8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

        // 开始对图像资源解码
        final int width = bitmap.getWidth(), height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        /*bitmap.recycle();//不能回收，因为这个bitmap还有用
        bitmap = null;*/
        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bBitmap);
            return result;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void decodeBitmap(final Bitmap bitmap, final DecodeCallback callback) {
        new AsyncTask<Bitmap, Void, Result>() {
            @Override
            protected Result doInBackground(Bitmap... params) {
                return decodeBitmap(params[0]);
            }

            @Override
            protected void onPostExecute(Result result) {
                callback.onDecodeCallback(result);
            }
        }.execute(bitmap);
    }

    public interface DecodeCallback {
        void onDecodeCallback(Result result);
    }
}
