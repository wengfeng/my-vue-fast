package io.renren.modules.sys.controller;

import io.renren.common.utils.R;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.form.SysLoginForm;
import io.renren.modules.sys.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class SysRegisterController {
    @Autowired
    SysRegisterService sysRegisterService;

    /**
     * 校验用户的手机号码
     */
    @RequestMapping("/sys/checkMobileIsRegister")
    public R checkUserMobile(@RequestBody SysLoginForm userEntity) {
        SysUserEntity  user=null;
        try {
            //先查询当前用户的手机是否注册过
            user=sysRegisterService.queryUserMoblie(userEntity.getMobile());
            if (user.getMobile() != null) {
                //说明当前手机已经注册过了
                return R.error(1,"该手机号已经注册，请重新输入！");
            }

        } catch (Exception e) {
            if (user == null) {
                return R.ok();
            }
        }
        return R.ok();
    }
    /**
     * 生成随机用户验证码。
     * @param user
     * @return
     */
    @PostMapping("/sys/getMobileCaptcha")
    public R getMobileCaptcha(@RequestBody SysLoginForm user) {
        try {
            //生成6位手机验证码
            Random random = new Random();
            String mobileCaptcha = "";
            for (int i = 0; i < 6; i++) {
                mobileCaptcha += random.nextInt(10);
            }
            System.out.println(mobileCaptcha);
            //调用阿里大于，给当前手机号发送验证码
            /**
             *
             */
            //把短信存入redis中，
            //c
            sysRegisterService.saveMoblieCode(user.getMobile(),mobileCaptcha);
            return R.ok("短信发送成功，请注意查收！");
        } catch (Exception e) {
            return R.error("发送失败，请重新发送！");
        }
    }

    @RequestMapping("/sys/register")
    public R register(@RequestBody SysLoginForm  userForm) {
        try {
            //先校验用户输入的验证码，
            Boolean flag=sysRegisterService.ckeckCode(userForm);
            if (!flag) {
                return R.error("验证码输入错误！");
            }
            //校验当前用户是否注册过
            SysUserEntity user=sysRegisterService.queryUserByMoblie(userForm);
            if (user != null) {
                return R.error("该手机号已经注册过！");
            }
            //注册用户
            //对用户的密码进行加密
            userForm.getPassword();
            boolean status=sysRegisterService.userResiger(userForm);
            if (status) {
                return R.ok("注册成功！");
            } else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}
