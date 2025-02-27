package com.resume.backend.entity;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class Resume implements Serializable {
    private Long id;
    private Long userId;          // 外键：对应用户ID
    private String filePath;      // 文件存储路径
    private Timestamp createdAt;  // 上传时间

}

