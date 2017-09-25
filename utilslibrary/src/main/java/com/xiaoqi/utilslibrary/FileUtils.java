package com.xiaoqi.utilslibrary;

import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * Created by yun.wang
 * Date :2017/6/30
 * Description: ***
 * Version: 1.0.0
 */
public class FileUtils {

    /**
     * 分隔符.
     */
    public final static String FILE_EXTENSION_SEPARATOR = ".";

    public static Boolean existSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 删除指定目录中特定的文件
     *
     * @param dir
     * @param filter
     */
    public static void delete(String dir, FilenameFilter filter) {
        if (StringUtils.isNull(dir))
            return;
        File file = new File(dir);
        if (!file.exists())
            return;
        if (file.isFile())
            file.delete();
        if (!file.isDirectory())
            return;

        File[] lists = null;
        if (filter != null)
            lists = file.listFiles(filter);
        else
            lists = file.listFiles();

        if (lists == null)
            return;
        for (File f : lists) {
            if (f.isFile()) {
                f.delete();
            }
        }
    }

    /**
     * 获得不带扩展名的文件名称
     *
     * @param filePath 文件路径
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StringUtils.isNull(filePath)) {
            return filePath;
        }
        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0,
                    extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
                extenPosi) : filePath.substring(filePosi + 1));
    }


    /**
     * checkDirPath
     *
     * @param dirPath
     * @return
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    public static void writeFile(InputStream is, File file){
        try{
            FileOutputStream fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            while((is.read(b)) != -1){
                fos.write(b);
            }

            is.close();

            fos.close();
        }catch (Exception ex){

        }
    }

    public static String getMIMEType(File file) {
        String type = "";
        String name = file.getName();
        String var3 = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return type;
    }

}
