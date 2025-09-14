package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.modules.system.entity.SysFileRecord;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author antfl
 * @since 2025/7/27
 */
public interface FileService  extends IService<SysFileRecord> {

    String uploadFile(MultipartFile file) throws IOException;

    String getFileToken(String fileId);

    void viewFile(String token, HttpServletResponse response);
}
