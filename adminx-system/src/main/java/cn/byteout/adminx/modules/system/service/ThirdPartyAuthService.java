package cn.byteout.adminx.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.byteout.adminx.modules.system.dto.request.BindingInfo;
import cn.byteout.adminx.modules.system.dto.request.BindingRequest;
import cn.byteout.adminx.modules.system.dto.request.CreateUser;
import cn.byteout.adminx.modules.system.dto.response.TokenResponse;
import cn.byteout.adminx.modules.system.entity.SysThirdPartyAuth;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author antfl
 * @since 2025/8/17
 */
public interface ThirdPartyAuthService extends IService<SysThirdPartyAuth> {

    void bindAccount(BindingRequest params);

    List<BindingInfo> bindList();

    TokenResponse createUser(CreateUser params, HttpServletRequest request);
}
