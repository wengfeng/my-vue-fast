package io.renren.modules.sys.dao;

import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysRegisterDao {
    SysUserEntity queryUserByMoblie(String mobile);
    //保存用户
    void saveUser(SysLoginForm user);
}
