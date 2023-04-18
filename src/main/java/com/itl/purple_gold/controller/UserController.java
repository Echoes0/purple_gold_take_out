package com.itl.purple_gold.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itl.purple_gold.common.R;
import com.itl.purple_gold.entity.User;
import com.itl.purple_gold.service.UserService;
import com.itl.purple_gold.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){

        //获取手机号
        String phone = user.getPhone();
        if (!StringUtils.isEmpty(phone)){
            //生成手机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("手机号{}的验证码为{}",code,phone);

            //调用阿里云短信服务api发送短信
            //SMSUtils.sendMessage("指定的签名","模板code",phone,code);

            //将生成的验证码保存到session
            session.setAttribute(phone,code);

            return R.success("手机验证码短信发送成功");
        }

        return R.success("短信发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

        log.info("移动端用户登录，{}",map.toString());

        //获取手机号
        String phone = map.get("phone").toString();

//        //获取验证码
//        String code = map.get("code").toString();

//        //获取session保存的验证码
//        Object codeInSession = session.getAttribute(phone);
//
//        //验证码比对
//        if (codeInSession!=null && codeInSession.equals(code)){
//            //成功登录
//
//        }

        //新用户注册
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);

        User user = userService.getOne(queryWrapper);

        if (user==null){
            user=new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user",user.getId());

        return R.success(user);
    }
}
