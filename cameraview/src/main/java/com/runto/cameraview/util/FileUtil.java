package com.runto.cameraview.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static String DST_FOLDER_NAME = "YouXiao";

    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getPath() + File.separator + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                //f.mkdir();
                f.mkdirs();
            }
        }
        Log.e("PATH-2", storagePath);
        return storagePath;
    }

    public static String saveBitmap(String dir, Bitmap b) {
        DST_FOLDER_NAME = dir;
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";
        Log.e("PATH-3", jpegName);
        //File file = new File(jpegName);
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            //Log.e("PATH-3", jpegName);
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
