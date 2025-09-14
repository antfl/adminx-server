package cn.byteout.adminx.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.byteout.adminx.modules.system.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author antfl
 * @since 2025/6/18
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {

    @Select("SELECT * FROM sys_dict WHERE dict_code = #{dictCode}")
    SysDict selectByDictCode(@Param("dictCode") String dictCode);
}
