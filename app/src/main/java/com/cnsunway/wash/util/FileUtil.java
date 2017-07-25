package com.cnsunway.wash.util;

import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    public static String readAsset(Context context,String assetFile) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(assetFile);
            return readFile(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String readRaw(Context context,int rawRes) {
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().openRawResource(rawRes);
            return readFile(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(inputStream != null){
                try {
                    inputStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String readFile(InputStream inputStream) {
        String readOutStr = null;
        try {

            DataInputStream dis = new DataInputStream(inputStream);
            try {
                long len = inputStream.available();
                if (len > Integer.MAX_VALUE)
                    throw new IOException("File  too large, was " + len + " bytes.");
                byte[] bytes = new byte[(int) len];
                dis.readFully(bytes);
                readOutStr = new String(bytes, "UTF-8");
            } finally {
                dis.close();
            }
        } catch (IOException e) {
            readOutStr = null;

            e.printStackTrace();
        }

        return readOutStr;
    }
}
