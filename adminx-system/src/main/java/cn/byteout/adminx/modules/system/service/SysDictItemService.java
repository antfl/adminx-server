package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.DictItemPageRequest;
import cn.byteout.adminx.modules.system.entity.SysDictItem;

/**
 * @author antfl
 * @since 2025/6/18
 */
public interface SysDictItemService extends IService<SysDictItem> {

    Result<SysDictItem> saveUpdate(SysDictItem dictItem);

    Result<Void> deleteDictData(Long id);

    Result<PageResult<SysDictItem>> pageQuery(DictItemPageRequest params);
}
