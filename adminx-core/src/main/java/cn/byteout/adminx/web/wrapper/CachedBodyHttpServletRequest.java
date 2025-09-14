package cn.byteout.adminx.web.wrapper;

import lombok.Getter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 缓存 HTTP 请求体的可重复读取包装类，将原始请求的输入流一次性
 * 读入内存字节数组。
 *
 * @author antfl
 * @since 2025/8/1
 */
@Getter
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper  {

    private final byte[] cachedBody;

    /**
     * 构造方法：读取并缓存请求体
     * @param request 原始 HTTP 请求
     * @throws IOException 当输入流读取失败时抛出
     */
    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        this.cachedBody = toByteArray(request.getInputStream());
    }

    /**
     * 将输入流转换为字节数组
     * @param input 请求输入流
     * @return 包含完整请求体的字节数组
     * @throws IOException 读取错误时抛出
     */
    private byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream));
    }


}
