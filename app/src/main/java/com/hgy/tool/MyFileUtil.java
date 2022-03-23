package com.hgy.tool;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.tecsun.network.utils.LogUntil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by hgy on 2017/8/16.
 */

public class MyFileUtil {

    public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();//主路径
    public static String rootFile = rootPath + "/appTool";//数据库路径
    public static String databasePath = rootPath + "/appTool/database/";//数据库路径
    public static String register = rootPath + "/appTool/register/";//数据库路径
    public static String LOG_DIR = rootPath + "/appTool/log/";//数据库路径
    public static String csvPath = rootPath + "/appTool/download/";//导出文件路径
    public static String modePath = rootPath + "/appTool/download/mode/";//导出文件路径

    public static String image = rootPath + "/appTool/photo/image/";//拍照缓存照片
    public static String facePath = rootPath + "/appTool/photo/facePhoto/";//人脸照片
    public static String IdImage = rootPath + "/appTool/photo/IdImage/";//身份证图像
    public static String takePhoto = rootPath + "/appTool/photo/takePhoto/";//现场照片
    public static String ocrPhoto = rootPath + "/appTool/photo/ocrPhoto/";//证件照片
    public static String figPhoto = rootPath + "/appTool/photo/figPhoto/";//指纹照片
    public static String logoImage = rootPath + "/appTool/photo/logoImage/";//logo图标
    public static String whitImage = rootPath + "/appTool/photo/whiteImage/";//白名单头像
    public static String empImage = rootPath + "/appTool/photo/empImage/";//内部人员照片

    public static String APK = rootPath + "/appTool/apk/";//apk
    public static final String MODEL_PATH = rootPath + "/model";
    public static final String LICENSE_PATH = rootPath + "/license";

    /**
     * 创建人脸图片截图路径
     */
    public static boolean creatFacePath(String path) {
        // 判断是否存在sd卡
        boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        if (!sdExist) {// 如果不存在,
            return false;
        }
        // 获取sd卡路径
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            return dirFile.mkdirs();
        }
        return true;
    }


    public static void deleteFile(String path) {
        if (path == null || path.length() <= 0) {
            LogUntil.e("path:" + path + " 文件路径不存在");
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            boolean del = file.delete();
            LogUntil.i("删除：" + path + " 结果：" + del);
        }
    }

    public static void deleteFiles(File file) {
        if (!file.exists()) {
            return;
        }
        LogUntil.i("删除：" + file.getName());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getName().equals(".nomedia")) {
                        continue;
                    }
                    boolean result = f.delete();
                    LogUntil.i("deleteFile:" + result);
                } else {
                    deleteFiles(f);
                }
            }
        }
    }


    public static boolean isExists(String path) {
        File apkFile = new File(path);
        return apkFile.exists();
    }

    public static void createDir() {
        creatFacePath(facePath);
        creatFacePath(databasePath);
        creatFacePath(register);
        creatFacePath(csvPath);
        creatFacePath(image);
        creatFacePath(IdImage);
        creatFacePath(takePhoto);
        creatFacePath(ocrPhoto);
        creatFacePath(figPhoto);
        creatFacePath(logoImage);
        creatFacePath(whitImage);
        creatFacePath(empImage);
        creatFacePath(MODEL_PATH);
        creatFacePath(LICENSE_PATH);
        creatFacePath(APK);
        creatFacePath(modePath);
        creatFacePath(LOG_DIR);
    }


    /**
     * 判断是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    // 将字符串写入到文本文件中
    public static void writeTxtToFile(String stringent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        try {
            makeFilePath(filePath, fileName);
            String strFilePath = filePath + fileName;
            // 每次写入时，都换行写
            File file = new File(strFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(stringent.getBytes());
            raf.close();
        } catch (Exception e) {
        }
    }

    //生成文件
    public static void makeFilePath(String filePath, String fileName) {
        File file;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(String path) {
        File file = new File(path);
        String content = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line + "\n";
                        }
                        instream.close();//关闭输入流
                    }
                } catch (Exception e) {
                }
            }
        }
        return content;
    }


    public static String[] getPoliceInfo(File file, int length) {
        String[] content = new String[length];
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    InputStreamReader inputreader = new InputStreamReader(instream, StandardCharsets.UTF_8);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    for (int i = 0; i < length; i++) {
                        content[i] = buffreader.readLine();
                    }
                    instream.close();//关闭输入流
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                }
            }
        }
        return content;
    }

    /**
     * 复制单个文件，用于上面的复制文件夹方法
     *
     * @param fromPath 源文件路径
     * @param toPath   目标路径
     */
    public static synchronized boolean copyFile(final String fromPath, final String toPath) {
        try {
            File sourcefile = new File(fromPath);
            if (!sourcefile.exists()) {
                return false;
            }
            File targetFile = new File(toPath);
            checkExistsAndDelete(toPath);
            FileInputStream fileInputStream = new FileInputStream(sourcefile);
            BufferedInputStream inbuff = new BufferedInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(targetFile);// 新建文件输出流并对它进行缓冲
            BufferedOutputStream outbuff = new BufferedOutputStream(fileOutputStream);
            FileChannel fileChannelOutput = fileOutputStream.getChannel();
            FileChannel fileChannelInput = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (fileChannelInput.read(buffer) != -1) {
                buffer.flip();
                fileChannelOutput.write(buffer);
                buffer.clear();
            }
            outbuff.flush();
            inbuff.close();
            outbuff.close();
            fileOutputStream.close();
            fileInputStream.close();
            fileChannelOutput.close();
            fileChannelInput.close();
            return true;
        } catch (Exception e) {
            Log.e("CopyPasteUtil", "CopyPasteUtil copyFile error:" + e.getMessage());
            return false;

        }
    }

    public static boolean checkExistsAndDelete(String path) {
        File apkFile = new File(path);
        if (apkFile.exists()) {
            return apkFile.delete();
        } else {
            return false;
        }

    }

    public static boolean isPhoto(String photoName) {
        if (TextUtils.isEmpty(photoName)) {
            return false;
        }
        String extension;
        int i = photoName.lastIndexOf('.');
        if (i > 0) {
            extension = photoName.substring(i + 1);
        } else {
            extension = "";
        }
        return "jpg".equals(extension);
    }

    public static String getFileName(String headPortrait) {
        if (TextUtils.isEmpty(headPortrait)) {
            return "";
        }
        try {
            File file = new File(headPortrait);
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void saveLog(String msg) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        String time = MyTimeUtil.formatDatetime(new Date());
        printWriter.append(time).append("\n");
        printWriter.append(msg).append("\n");
        printWriter.close();
        String errormsg = writer.toString();

        File ourRoot = new File(LOG_DIR);
        if (!ourRoot.exists()) {
            boolean dirResult = ourRoot.mkdir();
            LogUntil.i("创建结果：" + dirResult);
        }
        String logName = MyTimeUtil.formatDate(new Date());
        File file = new File(ourRoot, logName + ".txt");
        int size = (int) (file.length() / 1024 / 1024);
        int maxSize = 1024 * 5;
        if (size > maxSize) {
            boolean result = file.delete();
            LogUntil.e("日志过大删除 " + result);
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(raf.length());
            raf.write(errormsg.getBytes());
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除过期日志
     */
    public static void DeleteOverdueLogFile(int overdueLog) {

        File folder = new File(LOG_DIR); //打开目录文件夹
        if (folder.isDirectory()) {
            File[] AllFiles = folder.listFiles(); //列出目录下的所有文件
            if (AllFiles == null) {
                return;
            }
            if (AllFiles.length <= 0) {
                return;
            }
            List<String> mFilesList = new ArrayList<>();  //存放/myLog 下的所有文件
            //得到文件
            for (File mFile : AllFiles) {
                String Name = mFile.getName(); //得到文件的名字
                if (Name.length() < 1)
                    continue;
                if (Name.endsWith(".txt")) {  //筛选出log
                    LogUntil.e("所有日志文件" + Name);
                    mFilesList.add(Name); //把文件名添加到链表里
                }
            }
            if (mFilesList.size() == 0) {
                return;
            }
            Collections.sort(mFilesList);   // 将文件按自然排序升序排列
            if (mFilesList.size() < overdueLog) {
                return;
            }
            //判断日志文件如果大于5，就要处理
            for (int i = 0; i < mFilesList.size() - overdueLog; i++) {
                String name = mFilesList.get(i); //得到链表最早的文件名
                LogUntil.e("删除过期log" + name);
                File mFile = new File(folder, name);  //得到最早的文件
                boolean b = mFile.delete(); //删除
                LogUntil.e("删除过去文件结果：" + b);
            }
        }
    }

}
