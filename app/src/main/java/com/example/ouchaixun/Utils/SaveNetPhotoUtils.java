package com.example.ouchaixun.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;


public class SaveNetPhotoUtils {
    private static final java.util.UUID UUID = java.util.UUID.fromString("7e98a0f7-c358-4372-8bcd-557106bfd0a1");
    private static Context contexts;
    private static String photoUrls;
    private static Bitmap bitmap;
    private static String mSaveMessage = "failed";

    //自定义名字
    private static String photoNames;

    /**
     * 保存图片，无须自定义名字
     *
     * @param context
     * @param photoUrl
     */
    public static void savePhoto(Context context, String photoUrl) {
        contexts = context;
        photoUrls = photoUrl;
        new Thread(saveFileRunnable).start();
    }

    /**
     * 定义图片名字保存到相册
     *
     * @param context
     * @param photoUrl
     * @param photoName 图片名字，定义格式 name.jpg/name.png/...
     */
    public static void savePhoto(Context context, String photoUrl, String photoName) {
        contexts = context;
        photoUrls = photoUrl;
        photoNames = photoName;
        new Thread(saveFileRunnable2).start();
    }

    private static Runnable saveFileRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (!TextUtils.isEmpty(photoUrls)) {
                    URL url = new URL(photoUrls);
                    InputStream inputStream = url.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                saveFile(bitmap);
                mSaveMessage = "success！";
            } catch (IOException e) {
                mSaveMessage = "failed";
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }
    };

    private static Runnable saveFileRunnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                if (!TextUtils.isEmpty(photoUrls)) {
                    URL url = new URL(photoUrls);
                    InputStream inputStream = url.openStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                saveFile(bitmap, photoNames);
                mSaveMessage = "success！";
            } catch (IOException e) {
                mSaveMessage = "failed";
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            messageHandler.sendMessage(messageHandler.obtainMessage());
        }
    };

    /**
     * 保存成功和失败通知
     */
    @SuppressLint("HandlerLeak")
    private static Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(contexts, mSaveMessage, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 保存图片
     *
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        //图片命名
        String fileName = UUID.randomUUID().toString() + ".jpg";
        File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        //广播通知相册有图片更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        contexts.sendBroadcast(intent);
    }

    /**
     * 保存图片
     *
     * @param bm
     * @param photoName 图片命名
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String photoName) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        //图片命名后保存到相册
        File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + photoName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        //广播通知相册有图片更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        contexts.sendBroadcast(intent);
    }

}
