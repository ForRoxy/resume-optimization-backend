package com.resume.backend.controller;

import com.resume.backend.common.R;
import com.resume.backend.constants.Status;
import com.resume.backend.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/uploadResume")
    public R uploadResume(HttpServletRequest request, @RequestParam("file") MultipartFile file,@RequestParam("fileName") String fileName) {
        try {
            Long userId = (Long) request.getAttribute("userId"); // 从拦截器获取 userId
            if (userId == null) {
                return R.error(Status.CODE_401, "用户未认证");
            }
            String filePath = resumeService.uploadResume(userId, file,fileName);
            return R.success("简历上传成功，路径：" + filePath);
        } catch (IllegalArgumentException e) {
            return R.error(Status.CODE_500, e.getMessage());
        } catch (Exception e) {
            return R.error(Status.CODE_401, e.getMessage());
        }
    }


}
