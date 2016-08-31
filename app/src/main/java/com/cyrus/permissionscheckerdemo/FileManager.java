package com.cyrus.permissionscheckerdemo;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Cyrus on 2016/8/7.
 */
public class FileManager {

    private Context mContext;

    public FileManager(Context context) {
        mContext = context;
    }

    public void writeToExternal(String fileName, String fileContent) {
        File directory = Environment.getExternalStorageDirectory();

        if (!directory.exists()) {
            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(directory, fileName);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "创建文件失败", Toast.LENGTH_SHORT).show();
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos);

                osw.write(fileContent);
                osw.flush();

                osw.close();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "文件写入出错", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String readFromExternal(String fileName) {
        File directory = Environment.getExternalStorageDirectory();

        if (!directory.exists()) {
            Toast.makeText(mContext, "SD卡不存在", Toast.LENGTH_SHORT).show();
        } else {
            File file = new File(directory, fileName);

            if (!file.exists()) {
                Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis);

                    char buffer[] = new char[fis.available()];
                    isr.read(buffer);
                    String fileContent = new String(buffer);

                    isr.close();
                    fis.close();

                    return fileContent;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "文件读取出错", Toast.LENGTH_SHORT).show();
                }
            }

        }

        return null;
    }

}
