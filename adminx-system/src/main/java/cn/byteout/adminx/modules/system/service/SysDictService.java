package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.DictPageRequest;
import cn.byteout.adminx.modules.system.entity.SysDict;
import cn.byteout.adminx.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * @author antfl
 * @since 2025/6/18
 */
public interface SysDictService extends IService<SysDict> {

    Result<SysDict> saveUpdate(SysDict sysDict);

    Result<Void> deleteDict(Long id);

    Result<List<SysDictItem>> getDictItemsByCode(String dictCode);

    Result<PageResult<SysDict>> pageQuery(DictPageRequest params);
}
