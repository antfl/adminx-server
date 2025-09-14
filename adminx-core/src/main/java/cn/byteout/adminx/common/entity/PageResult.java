package cn.byteout.adminx.common.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一分页返回
 *
 * @author antfl
 * @since 2025/7/5
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页
     */
    private Long current;

    /**
     * 每页条数
     */
    private Long size;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private List<T> records;

    /**
     * 快速构建方法
     */
    public static <T> PageResult<T> build(Page<T> page) {
        return new PageResult<>(
                page.getTotal(),
                page.getCurrent(),
                page.getSize(),
                page.getPages(),
                page.getRecords()
        );
    }
}
