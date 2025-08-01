package com.bytescheduler.adminx.modules.system.controller;

import com.bytescheduler.adminx.common.entity.Result;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.modules.system.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author byte-scheduler
 * @since 2025/7/27
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.uploadFile(file);
            return Result.success(fileId);
        } catch (IOException e) {
            throw new BusinessException(500, "文件上传失败： " + e.getMessage());
        }
    }

    @GetMapping("/getFileToken/{fileId}")
    public Result<String> getFileToken(@PathVariable String fileId) {
       return Result.success(fileService.getFileToken(fileId));
    }

    @GetMapping("/view/{token}")
    public void viewImage(@PathVariable String token, HttpServletResponse response) {
        fileService.viewFile(token, response);
    }
}
