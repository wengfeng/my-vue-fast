package io.renren.modules.sys.service.impl;

import io.renren.modules.sys.dao.SysRegisterDao;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;
import io.renren.modules.sys.service.SysRegisterService;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SysRegisterServiceImpl implements SysRegisterService {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    SysRegisterDao sysRegisterDao;

    @Override
    public boolean userResiger(SysLoginForm userEntity) {
        try {
            //对用户的密码进行加密
            String password = userEntity.getPassword();
            userEntity.setPassword(new Sha256Hash(userEntity.getPassword(),userEntity.getUsername()).toHex());
            
            //保存用户到数据库中
            sysRegisterDao.saveUser(userEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveMoblieCode(String mobile,String mobileCaptcha) {
        //把验证码保存到redis中
        redisTemplate.opsForValue().set(mobile,mobileCaptcha);
         //保存的时间为10minutes
        redisTemplate.expire(mobile, 24, TimeUnit.HOURS);
    }

    @Override
    public SysUserEntity queryUserMoblie(String mobile) {
        return sysRegisterDao.queryUserByMoblie( mobile);
    }

    @Override
    public Boolean ckeckCode(SysLoginForm userEntity) {
        //根据用户的手机号查询redis中验证码
        String redisCode  =(String) redisTemplate.opsForValue().get(userEntity.getMobile());
         //校验用户输入的验证码
        if(!redisCode.equals(userEntity.getMobileCaptcha())){
            return false;
        }
        return true;
    }

    @Override
    public SysUserEntity queryUserByMoblie(SysLoginForm userForm) {
        return sysRegisterDao.queryUserByMoblie(userForm.getMobile());

    }
}
