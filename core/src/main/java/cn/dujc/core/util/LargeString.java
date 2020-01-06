package cn.dujc.core.util;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

public class LargeString {

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final File mFile;

    public LargeString(@NonNull File file) {
        mFile = file;
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }

    public LargeString(@NonNull Context context, String filename) {
        this(new File(context.getCacheDir(), filename));
    }

    /**
     * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功！)
     *
     * @param text 原字符串
     * @return 成功标记
     */
    public boolean set(String text) {
        boolean flag = true;
        if (text == null) return flag;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new StringReader(text));
            bufferedWriter = new BufferedWriter(new FileWriter(mFile));
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];         //字符缓冲区
            int len;
            while ((len = bufferedReader.read(buffer)) != -1) {
                bufferedWriter.write(buffer, 0, len);
            }
            bufferedWriter.flush();
            bufferedReader.close();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public String get() {
        return get("utf-8");
    }

    /**
     * 文本文件转换为指定编码的字符串
     *
     * @param encoding 编码类型
     * @return 转换后的字符串
     */
    public String get(String encoding) {
        String result = null;
        InputStreamReader reader = null;
        StringWriter writer = null;
        try {
            if (encoding == null || "".equals(encoding.trim())) {
                reader = new InputStreamReader(new FileInputStream(mFile));
            } else {
                reader = new InputStreamReader(new FileInputStream(mFile), encoding);
            }
            writer = new StringWriter();
            //将输入流写入输出流
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int length;
            while (-1 != (length = reader.read(buffer))) {
                writer.write(buffer, 0, length);
            }
            result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回转换结果
        return result;
    }
}
