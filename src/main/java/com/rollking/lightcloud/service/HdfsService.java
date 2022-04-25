package com.rollking.lightcloud.service;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.rollking.lightcloud.entity.UserFile;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */

@Component
public class HdfsService {

    private static final int bufferSize = 1024 * 1024 * 64;

    /**
     * 获取HDFS文件系统对象
     *
     * @return 返回文件系统对象
     * @throws Exception 抛出HDFS异常
     */
    public static FileSystem getFileSystem() throws Exception {
        return FileSystem.get(new Configuration());
    }

    /**
     * 在HDFS创建文件夹
     *
     * @param path 请求的路径
     * @return 是否创建成功
     * @throws Exception 抛出HDFS异常
     */
    public static boolean mkdir(String path) throws Exception {
        if (path.isEmpty()) return false;
        if (existFile(path)) return true;
        boolean res;
        try (FileSystem fs = getFileSystem()) {
            res = fs.mkdirs(new Path(path));
        }
        return res;
    }

    /**
     * 判断HDFS文件是否存在
     *
     * @param path 请求的路径
     * @return 是否存在
     * @throws Exception 抛出HDFS异常
     */
    public static boolean existFile(String path) throws Exception {
        if (path.isEmpty()) return false;
        boolean isExists;
        try (FileSystem fs = getFileSystem()) {
            Path srcPath = new Path(path);
            isExists = fs.exists(srcPath);
        }
        return isExists;
    }

    /**
     * 读取HDFS目录信息
     *
     * @param path 请求的路径
     * @return 返回读取的目录list
     * @throws Exception 抛出HDFS异常 抛出HDFS异常
     *                   refer : https://hadoop.apache.org/docs/current/api/org/apache/hadoop/fs/FileSystem.html
     */
    public static List<UserFile> readPathInfo(String path) throws Exception {
        if (path.isEmpty()) return null;
        if (!existFile(path)) return null;
        FileStatus[] statusList;
        try (FileSystem fs = getFileSystem()) {
            Path newPath = new Path(path);
            statusList = fs.listStatus(newPath);
        }
        List<UserFile> list = new ArrayList<>();
        if (null != statusList && statusList.length > 0) {
            for (FileStatus fileStatus : statusList) {
                list.add(new UserFile(fileStatus));
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 读取HDFS文件列表
     *
     * @param path 请求的路径
     * @return 当前路径下的文件列表
     * @throws Exception 抛出HDFS异常
     */
    public static List<UserFile> listFile(String path) throws Exception {
        if (path.isEmpty()) return null;
        if (!existFile(path)) return null;
        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        // 递归找到所有文件
        RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(srcPath, true);
        List<UserFile> returnList = new ArrayList<>();
        while (filesList.hasNext()) {
            returnList.add(new UserFile(filesList.next()));
        }
        fs.close();
        return returnList;
    }

    /**
     * HDFS创建文件
     *
     * @param path 请求的路径
     * @param file 需要在HDFS上创建的路径
     * @throws Exception 抛出HDFS异常
     */
    public static void createFile(String path, MultipartFile file) throws Exception {
        if (path.isEmpty()) return;
        String fileName = file.getOriginalFilename();
        try (FileSystem fs = getFileSystem()) {
            Path faPath = new Path(path);
            if (!fs.exists(faPath)) fs.mkdirs(faPath);
            Path newPath = new Path(path + "/" + fileName);// 上传时默认当前目录，后面自动拼接文件的目录
            FSDataOutputStream outputStream = fs.create(newPath);   // 打开一个输出流
            outputStream.write(file.getBytes());
            outputStream.close();
        }
    }

    /**
     * 读取HDFS文件内容
     *
     * @param path 请求的路径
     * @return 读取的文件的转出字符串
     * @throws Exception 抛出HDFS异常
     */
    public static String readFile(String path) throws Exception {
        if (path.isEmpty() || !existFile(path)) return null;

        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt;
            StringBuilder sb = new StringBuilder();
            while ((lineTxt = reader.readLine()) != null) sb.append(lineTxt);
            return sb.toString();
        } finally {
            if (inputStream != null) inputStream.close();
            fs.close();
        }
    }


    /**
     * HDFS重命名文件
     *
     * @param oldName 原文件目标路径
     * @param newName 现文件目标路径
     * @return 重命名目标路径是否成功
     * @throws Exception 抛出HDFS异常
     */
    public static boolean renameFile(String oldName, String newName) throws Exception {
        if (oldName.isEmpty() || newName.isEmpty()) return false;
        boolean status;
        try (FileSystem fs = getFileSystem()) {
            Path oldPath = new Path(oldName);
            Path newPath = new Path(newName);
            status = fs.rename(oldPath, newPath);
        }
        return status;
    }

    /**
     * 删除HDFS文件
     *
     * @param path 请求的路径
     * @return 是否删除成功
     * @throws Exception 抛出HDFS异常
     */
    public static boolean deleteFile(String path) throws Exception {
        if (path.isEmpty() || !existFile(path)) return false;

        boolean status;
        try (FileSystem fs = getFileSystem()) {
            Path srcPath = new Path(path);
            status = fs.deleteOnExit(srcPath);
        }
        return status;
    }

    /**
     * 上传HDFS文件
     *
     * @param path       本地文件系统的路径
     * @param uploadPath 上传到HDFS的路径
     * @throws Exception 抛出HDFS异常
     */
    public static void uploadFile(String path, String uploadPath) throws Exception {
        if (path.isEmpty() || uploadPath.isEmpty()) {
            return;
        }
        try (FileSystem fs = getFileSystem()) {
            Path clientPath = new Path(path);// 上传路径
            Path serverPath = new Path(uploadPath); // 目标路径
            fs.copyFromLocalFile(false, clientPath, serverPath);
            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        }
    }

    /**
     * 下载HDFS文件
     *
     * @param path         请求的路径
     * @param downloadPath 要下载到本地文件系统的路径
     * @throws Exception 抛出HDFS异常
     */
    public static void downloadFile(String path, String downloadPath) throws Exception {
        if (path.isEmpty() || downloadPath.isEmpty()) return;

        try (FileSystem fs = getFileSystem()) {
            Path clientPath = new Path(path);  // 目标路径
            Path serverPath = new Path(downloadPath);

            // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
            fs.copyToLocalFile(false, clientPath, serverPath);
        } // 上传路径
    }

    /**
     * HDFS文件复制
     *
     * @param sourcePath 源路径
     * @param targetPath 目标路径
     * @throws Exception 抛出HDFS异常
     */
    public static void copyFile(String sourcePath, String targetPath) throws Exception {
        if (sourcePath.isEmpty() || targetPath.isEmpty()) return;

        FileSystem fs = getFileSystem();
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            inputStream = fs.open(oldPath);
            outputStream = fs.create(newPath);
            IOUtils.copyBytes(inputStream, outputStream, bufferSize, false);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            fs.close();
        }
    }

    /**
     * @param path 请求的路径
     * @return 返回HDFS上的文件的byte数组
     * @throws Exception 抛出HDFS异常
     */
    public static byte[] openFileToBytes(String path) throws Exception {
        if (path.isEmpty() || !existFile(path)) return null;
        try (FileSystem fs = getFileSystem()) {
            Path srcPath = new Path(path);// 目标路径
            FSDataInputStream inputStream = fs.open(srcPath);
            return IOUtils.readFullyToByteArray(inputStream);
        }
    }


    /**
     * @param path 请求的路径
     * @return 某个文件在HDFS的集群位置
     * @throws Exception 抛出HDFS异常
     */
    public static BlockLocation[] getFileBlockLocations(String path) throws Exception {
        if (path.isEmpty()) return null;
        if (!existFile(path)) return null;

        FileSystem fs = getFileSystem();
        // 目标路径
        Path srcPath = new Path(path);
        FileStatus fileStatus = fs.getFileStatus(srcPath);
        return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }

}
