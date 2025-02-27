package com.resume.backend.service;

import com.resume.backend.entity.Resume;
import com.resume.backend.entity.User;
import com.resume.backend.mapper.ResumeMapper;
import com.resume.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final UserMapper userMapper;
    private final ResumeMapper resumeMapper;

    @Value("${resume.upload.dir}")
    private String UPLOAD_DIR;

    /**
     * 上传简历并更新用户的 resumeFile 字段
     * @param userId 用户ID
     * @param file 上传的文件
     * @return 保存的文件路径
     * @throws IOException 当文件保存失败时抛出异常
     */
    public String uploadResume(Long userId, MultipartFile file,String fileName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        // 确保基础上传目录存在
        File baseDir = new File(UPLOAD_DIR);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            throw new IOException("无法创建基础上传目录：" + UPLOAD_DIR);
        }

        // 创建用户专属目录
        File userDir = new File(baseDir, String.valueOf(userId));
        if (!userDir.exists() && !userDir.mkdirs()) {
            throw new IOException("无法创建用户目录：" + userDir.getAbsolutePath());
        }

        // 保留原始文件名，但处理重名问题
        String originalFilename = fileName;
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名为空");
        }
        File destFile = new File(userDir, originalFilename);
        int counter = 1;
        while (destFile.exists()) {
            int dotIndex = originalFilename.lastIndexOf('.');
            String nameWithoutExt = (dotIndex == -1) ? originalFilename : originalFilename.substring(0, dotIndex);
            String ext = (dotIndex == -1) ? "" : originalFilename.substring(dotIndex);
            String newFilename = nameWithoutExt + "(" + counter + ")" + ext;
            destFile = new File(userDir, newFilename);
            counter++;
        }

        // 保存文件
        file.transferTo(destFile);

        // 检查用户是否存在
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        // 创建 Resume 记录
        Resume resume = new Resume();
        resume.setUserId(userId);
        resume.setFilePath(destFile.getAbsolutePath());
        resumeMapper.insertResume(resume);

        return destFile.getAbsolutePath();
    }


}
