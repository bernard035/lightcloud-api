package com.rollking.lightcloud.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.hadoop.fs.FileStatus;

import java.sql.Timestamp;

import static com.rollking.lightcloud.utils.StrUtils.*;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */

@Getter
@Setter
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class UserFile {
    private int id;

    private String name;

    private String path;

    private String type;

    private long fileSize;

    private Timestamp createTime;

    private int owner;

    private String sha256;

    private boolean fav;

    private boolean rec;

    /**
     * @param fileStatus HDFS 文件状态
     */
    public UserFile(FileStatus fileStatus) {
        this.path = gloPath2userPath(fileStatus.getPath().toString());
        this.name = urlLastName(this.path);
        this.fileSize = fileStatus.getLen();
        if (fileStatus.isDirectory()) this.type = "folder";
        else if (fileStatus.isFile()) this.type = getFileType(this.path);
    }
}
