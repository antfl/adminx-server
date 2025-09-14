package cn.byteout.adminx.modules.system.service;

import cn.byteout.adminx.modules.system.dto.response.*;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.common.entity.PageResult;
import cn.byteout.adminx.common.entity.Result;
import cn.byteout.adminx.modules.system.dto.request.UpdateUserRequest;
import cn.byteout.adminx.modules.system.dto.request.UserQueryRequest;
import cn.byteout.adminx.modules.system.entity.SysUser;

import java.util.List;

public interface UserService extends IService<SysUser> {

    Result<String> updateUser(UpdateUserRequest params);

    Result<PageResult<UserResponse>> pageQuery(UserQueryRequest params);

    Result<UserInfoResponse> getUserInfo();

    Result<UserPermissionResponse> getPermissions();

    List<RecentUserResponse> getRecentUsers();

    List<ActiveUserResponse> getActiveUsers();
}
