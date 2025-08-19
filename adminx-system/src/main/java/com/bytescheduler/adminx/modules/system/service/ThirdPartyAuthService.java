package com.bytescheduler.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bytescheduler.adminx.modules.system.dto.request.BindingInfo;
import com.bytescheduler.adminx.modules.system.dto.request.BindingRequest;
import com.bytescheduler.adminx.modules.system.dto.request.CreateUser;
import com.bytescheduler.adminx.modules.system.dto.response.TokenResponse;
import com.bytescheduler.adminx.modules.system.entity.SysThirdPartyAuth;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author byte-scheduler
 * @since 2025/8/17
 */
public interface ThirdPartyAuthService extends IService<SysThirdPartyAuth> {

    void bindAccount(BindingRequest params);

    List<BindingInfo> bindList();

    TokenResponse createUser(CreateUser params, HttpServletRequest request);
}
