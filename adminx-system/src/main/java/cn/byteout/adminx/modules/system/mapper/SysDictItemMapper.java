package cn.byteout.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.byteout.adminx.modules.system.entity.SysDictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author antfl
 * @since 2025/6/18
 */
@Mapper
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    @Select("SELECT * FROM sys_dict_item WHERE dict_id = #{dictId} ORDER BY sort ASC")
    List<SysDictItem> selectByDictId(@Param("dictId") Long dictId);
}
