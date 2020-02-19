package cn.dujc.core.util;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import cn.dujc.core.app.Core;

public class FileUtil {

    /**
     * 读取文件
     *
     * @param uri 图片
     * @return degree旋转的角度
     */
    @Nullable
    public static FileInputStream readUri(Context context, Uri uri) {
        if (context == null || uri == null) return null;
        try {
            ParcelFileDescriptor pdf = context.getContentResolver().openFileDescriptor(uri, "r");
            return new FileInputStream(pdf.getFileDescriptor());
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
        return null;
    }

    @NonNull
    public static byte[] toBytes(Context context, Uri file) {
        if (file != null && context != null) {
            FileInputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            try {
                inputStream = FileUtil.readUri(context, file);
                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                return outputStream.toByteArray();
            } catch (Exception e) {
                if (Core.DEBUG) e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
            }
        }
        return new byte[0];
    }

    @NonNull
    public static byte[] toBytes(File file) {
        if (file != null && file.exists()) {
            FileInputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            try {
                inputStream = new FileInputStream(file);
                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[512];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                return outputStream.toByteArray();
            } catch (Exception e) {
                if (Core.DEBUG) e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
            }
        }
        return new byte[0];
    }

    @NonNull
    public static List<String> readLines(File file) {
        List<String> result = new ArrayList<>();
        if (!file.exists()) return result;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String read;
            while ((read = reader.readLine()) != null) {
                result.add(read);
            }
            reader.close();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e1) {
                    if (Core.DEBUG) e1.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void saveText(File file, CharSequence text) {
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            bw.append(text);
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (Exception e) {
                    if (Core.DEBUG) e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (Exception e) {
                    if (Core.DEBUG) e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (Exception e) {
                    if (Core.DEBUG) e.printStackTrace();
                }
            }
        }
    }

    public static void appendText(File file, String content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
        } catch (Exception e) {
            if (Core.DEBUG) e.printStackTrace();
        }
    }

    public static void delete(File file) {
        if (file == null || !file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) for (File child : files) {
                delete(child);
            }
        }
        file.delete();
    }

    public static long size(File file) {
        if (file == null || !file.exists()) return 0;
        long length = 0;
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    length += size(child);
                }
            }
        }
        length += file.length();
        return length;
    }

    @Nullable
    public static File copyToTemporalFile(Context context, Uri file) {
        if (file != null && context != null) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir == null) cacheDir = context.getCacheDir();
            if (cacheDir == null) return null;
            if (!cacheDir.exists()) cacheDir.mkdirs();
            File tmpFile = new File(cacheDir, System.currentTimeMillis() + ".tmp");

            FileInputStream inputStream = null;
            FileOutputStream outputStream = null;
            try {
                inputStream = FileUtil.readUri(context, file);
                outputStream = new FileOutputStream(tmpFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                return tmpFile;
            } catch (Exception e) {
                if (Core.DEBUG) e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        if (Core.DEBUG) e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
