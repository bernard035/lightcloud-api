package com.rollking.lightcloud.controller;


import java.util.List;

import com.rollking.lightcloud.entity.UserFile;
import com.rollking.lightcloud.result.Result;
import com.rollking.lightcloud.result.ResultFactory;
import com.rollking.lightcloud.service.HdfsService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Bernard
 * @date: 2021/12/13
 * @description: 单独对HDFS操作的API
 */

@RestController
@RequestMapping("/api/hdfs")
public class HdfsController {

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     * @return 创建结果
     * @throws Exception 抛出HDFS异常
     */
    @RequestMapping(value = "mkdir", method = RequestMethod.POST)
    @ResponseBody
    public Result mkdir(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return ResultFactory.buildFailResult("请求参数为空");
        }
        // 创建空文件夹
        if (HdfsService.mkdir(path)) {
            return ResultFactory.buildSuccessResult("文件夹创建成功");
        } else {
            return ResultFactory.buildFailResult("文件夹创建失败");
        }
    }

    /**
     * 读取HDFS文件夹信息
     *
     * @param path 文件夹路径
     * @return 文件夹信息
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/readPathInfo")
    public Result readPathInfo(@RequestParam("path") String path) throws Exception {
        List<UserFile> list = HdfsService.readPathInfo(path);
        return ResultFactory.buildSuccessResult("读取HDFS目录信息成功", list);
    }

    /**
     * 获取HDFS文件在集群中的位置
     *
     * @param path 文件路径
     * @return HDFS文件在集群中的位置
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/getFileBlockLocations")
    public Result getFileBlockLocations(@RequestParam("path") String path) throws Exception {
        BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
        return ResultFactory.buildSuccessResult("获取HDFS文件在集群中的位置", blockLocations);
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建结果
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/createFile")
    public Result createFile(@RequestParam("path") String path, @RequestParam("file") MultipartFile file)
            throws Exception {
        if (StringUtils.isEmpty(path))  return ResultFactory.buildFailResult("请求参数为空");

        HdfsService.createFile(path, file);
        return ResultFactory.buildSuccessResult("创建文件成功");
    }

    /**
     * 读取HDFS文本内容
     *
     * @param path 文本文件路径
     * @return 文本内容字符串
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/readFile")
    public Result readFile(@RequestParam("path") String path) throws Exception {
        return ResultFactory.buildSuccessResult("读取HDFS文件内容", HdfsService.readFile(path));
    }

    /**
     * 读取HDFS文件返回Bytes
     *
     * @param path 文件路径
     * @return 对应文件的Bytes
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/openFileToBytes")
    public Result openFileToBytes(@RequestParam("path") String path) throws Exception {
        byte[] files = HdfsService.openFileToBytes(path);
        return ResultFactory.buildSuccessResult("读取HDFS文件转换成Byte类型", files);
    }

    /**
     * 读取文件列表
     *
     * @param path 需要读取的目录的路径
     * @return 读取结果，若读取成功则返回一个List
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/listFile")
    public Result listFile(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return ResultFactory.buildFailResult("请求参数为空");
        }
        List<UserFile> returnList = HdfsService.listFile(path);
        return ResultFactory.buildSuccessResult("读取文件列表成功", returnList);
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原文件目标路径
     * @param newPath 原文件目标路径
     * @return 重命名结果
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/renameFile")
    public Result renameFile(@RequestParam("oldPath") String oldPath, @RequestParam("newPath") String newPath)
            throws Exception {
        if (StringUtils.isEmpty(oldPath) || StringUtils.isEmpty(newPath)) {
            return ResultFactory.buildFailResult("请求参数为空");
        }
        if (HdfsService.renameFile(oldPath, newPath)) {
            return ResultFactory.buildSuccessResult("文件重命名成功");
        }
        return ResultFactory.buildSuccessResult("文件重命名失败");
    }

    /**
     * 删除文件
     *
     * @param path 请求删除的文件路径
     * @return 删除结果
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/deleteFile")
    public Result deleteFile(@RequestParam("path") String path) throws Exception {
        if (HdfsService.deleteFile(path))
            return ResultFactory.buildSuccessResult("delete file success");
        return ResultFactory.buildFailResult("delete file fail");
    }

    /**
     * 将本地文件系统文件上传到HDFS
     *
     * @param path 本地文件系统中的路径
     * @param uploadPath HDFS中的路径
     * @return 上传结果
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
            throws Exception {
        HdfsService.uploadFile(path, uploadPath);
        return ResultFactory.buildSuccessResult("upload file success");
    }

    /**
     * 将HDFS文件下载到本地文件系统
     *
     * @param path HDFS中的路径
     * @param downloadPath  本地文件系统中的路径
     * @return 下载结果
     * @throws Exception 抛出HDFS异常
     */
    @PostMapping("/downloadFile")
    public Result downloadFile(@RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
            throws Exception {
        HdfsService.downloadFile(path, downloadPath);
        return ResultFactory.buildSuccessResult("download file success");
    }

    /**
     * HDFS文件复制
     *
     * @param sourcePath 源路径
     * @param targetPath 目的路径
     * @return 复制结果
     */
    @PostMapping("/copyFile")
    public Result copyFile(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath)
            throws Exception {
        HdfsService.copyFile(sourcePath, targetPath);
        return ResultFactory.buildSuccessResult("copy file success");
    }

    /**
     * 查看文件是否已存在
     *
     * @param path 文件路径
     * @return 是否存在的结果
     */
    @PostMapping("/existFile")
    public Result existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = HdfsService.existFile(path);
        return ResultFactory.buildSuccessResult("file isExist: " + isExist);
    }
}