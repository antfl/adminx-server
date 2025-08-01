package com.bytescheduler.adminx.security;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;

/**
 * 基于内存缓存的 Servlet 输入流实现
 * 作用：
 * 将预缓存的字节数组（通常来自 HTTP 请求体）包装为 ServletInputStream，
 * 支持对缓存数据的重复读取操作。主要用于解决 Servlet 规范中请求体只能读
 * 取一次的限制。
 *
 * @author byte-scheduler
 * @since 2025/8/1
 */
public class CachedBodyServletInputStream extends ServletInputStream {
    private final ByteArrayInputStream buffer;

    public CachedBodyServletInputStream(byte[] contents) {
        this.buffer = new ByteArrayInputStream(contents);
    }

    @Override
    public int read() {
        return buffer.read();
    }

    /**
     * 检查流是否已结束
     *
     * @return true：所有数据已读取完毕，false：仍有数据可读
     */
    @Override
    public boolean isFinished() {
        return buffer.available() == 0;
    }

    /**
     * 检查流是否可立即读取（始终为 true）
     *
     * @return 总是返回true（数据已完全加载到内存）
     */
    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }
}