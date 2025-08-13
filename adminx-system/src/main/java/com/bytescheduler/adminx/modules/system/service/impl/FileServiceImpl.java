package com.bytescheduler.adminx.modules.system.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytescheduler.adminx.common.exception.BusinessException;
import com.bytescheduler.adminx.common.utils.file.FileUtil;
import com.bytescheduler.adminx.context.UserContextHolder;
import com.bytescheduler.adminx.modules.system.entity.SysFileRecord;
import com.bytescheduler.adminx.modules.system.mapper.FileRecordMapper;
import com.bytescheduler.adminx.modules.system.service.FileService;
import com.bytescheduler.adminx.config.FileConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author byte-scheduler
 * @since 2025/7/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileRecordMapper, SysFileRecord> implements FileService {

    private final FileConfig fileConfig;
    private final FileUtil fileUtil;
    private final FileRecordMapper fileRecordMapper;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileExt = StrUtil.subAfter(originalFilename, ".", true);
        Long userId = UserContextHolder.get();
        String fileName = IdUtil.fastSimpleUUID() + "." + fileExt;

        Path userDir = Paths.get(fileConfig.getUploadDir(), userId.toString());

        if (!Files.exists(userDir)) {
            Files.createDirectories(userDir);
        }

        Path savePath = userDir.resolve(fileName);
        byte[] processedBytes = fileUtil.processFile(file);
        Files.write(savePath, processedBytes);

        SysFileRecord record = new SysFileRecord();
        record.setFileName(originalFilename);
        record.setFilePath(savePath.toString());
        record.setFileSize((long) processedBytes.length);

        fileRecordMapper.insert(record);
        return record.getId().toString();
    }

    @Override
    public String getFileToken(String fileId) {
        if (!StringUtils.isNotBlank(fileId)) {
            return "";
        }

        if (fileId.contains("AVATAR") || isQQAvatar(fileId)) {
            return fileId;
        }

        String tokenKey = "file:token_by_fileId:" + fileId;
        String existingToken = redisTemplate.opsForValue().get(tokenKey);

        if (existingToken != null) {
            String filePathKey = "file:token:" + existingToken;
            if (redisTemplate.hasKey(filePathKey)) {
                return existingToken;
            }
            redisTemplate.delete(tokenKey);
        }

        SysFileRecord fileRecord = baseMapper.selectById(fileId);
        if (fileRecord == null) {
            return "";
        }

        if (!Files.exists(Paths.get(fileRecord.getFilePath()))) {
            return "";
        }

        String newToken = "file-view:" + UUID.randomUUID();
        int expirationMinutes = fileConfig.getExpiration();

        String filePathKey = "file:token:" + newToken;
        redisTemplate.opsForValue().set(
                filePathKey,
                fileRecord.getFilePath(),
                expirationMinutes,
                TimeUnit.MINUTES
        );

        redisTemplate.opsForValue().set(
                tokenKey,
                newToken,
                expirationMinutes,
                TimeUnit.MINUTES
        );
        return newToken;
    }

    @Override
    public void viewFile(String token, HttpServletResponse response) {
        String redisKey = "file:token:" + token;
        String filePath = redisTemplate.opsForValue().get(redisKey);
        if (filePath == null || filePath.isEmpty()) {
            throw new BusinessException(404, "链接已过期或不存在");
        }
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new BusinessException(404, "图片文件不存在");
            }

            String fileName = path.getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

            String contentType = getContentType(extension);
            response.setContentType(contentType);

            response.setHeader("Cache-Control", "public, max-age=3600");

            Files.copy(path, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException e) {
            throw new BusinessException(500, "图片加载失败");
        }
    }

    private String getContentType(String extension) {
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "bmp":
                return "image/bmp";
            case "webp":
                return "image/webp";
            default:
                return "application/octet-stream";
        }
    }

    public boolean isQQAvatar(String fileId) {
        if (fileId == null) return false;
        return fileId.contains("/ek_qqapp/") ||
                fileId.toLowerCase().contains("thirdqq.qlogo.cn");
    }
}
