package cn.byteout.adminx.common.utils.file;

import cn.byteout.adminx.common.exception.BusinessException;
import cn.byteout.adminx.config.FileConfig;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author antfl
 * @since 2025/7/27
 */
@Slf4j
@Component
public class FileUtil {

    private final FileConfig fileConfig;

    private static final Set<String> SUPPORTED_IMAGE_TYPES = new HashSet<String>() {{
        add("image/jpeg");
        add("image/png");
        add("image/bmp");
        add("image/webp");
    }};

    public FileUtil(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * 处理文件上传
     *
     * @param file 上传的文件
     * @return 压缩后的文件字节数组
     * @throws IOException 文件操作异常
     */
    public byte[] processFile(MultipartFile file) throws IOException {
        int maxSizeBytes = fileConfig.getMaxFileSize() * 1024 * 1024;
        byte[] fileBytes = file.getBytes();

        // 检查文件大小
        if (file.getSize() <= maxSizeBytes) {
            return fileBytes;
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (!SUPPORTED_IMAGE_TYPES.contains(contentType)) {
            throw new BusinessException(400, "不支持的文件类型: " + contentType);
        }

        // 执行无损压缩
        byte[] compressed = compressImage(fileBytes, maxSizeBytes);
        if (compressed.length > maxSizeBytes) {
            throw new BusinessException(400, "文件过大且无法压缩到指定大小");
        }

        return compressed;
    }

    /**
     * 无损压缩图片
     *
     * @param originImage  原始图片字节
     * @param maxSizeBytes 最大字节数
     * @return 压缩后的图片字节
     */
    private byte[] compressImage(byte[] originImage, long maxSizeBytes) throws IOException {
        try (ByteArrayInputStream input = new ByteArrayInputStream(originImage);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            float quality = 0.9f;
            int attempt = 0;

            do {
                output.reset();
                Thumbnails.of(input)
                        .scale(1.0)
                        .outputQuality(quality)
                        .toOutputStream(output);

                quality -= 0.1f;
                attempt++;
            } while (output.size() > maxSizeBytes && quality > 0.1f && attempt < 5);

            return output.toByteArray();
        }
    }
}
