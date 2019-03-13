package io.renren.modules.sys.service;

import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;

public interface SysRegisterService {
    /**
     * 注册用户
     * @param userEntity
     * @return
     */
    boolean userResiger(SysLoginForm userEntity);

    /**
     * 保存用的手机验证码
     * @param mobile
     * @param mobileCaptcha
     */
    void saveMoblieCode(String mobile,String mobileCaptcha);

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    SysUserEntity queryUserMoblie(String mobile);

    /**
     * 校验用户密码
     * @param userEntity
     * @return
     */
    Boolean ckeckCode(SysLoginForm userEntity);

    SysUserEntity queryUserByMoblie(SysLoginForm userForm);
}
