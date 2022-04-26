package com.rollking.lightcloud.controller;

import com.rollking.lightcloud.entity.Captcha;
import com.rollking.lightcloud.entity.User;
import com.rollking.lightcloud.result.Result;
import com.rollking.lightcloud.result.ResultFactory;
import com.rollking.lightcloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import static com.rollking.lightcloud.utils.MailUtils.sendEmail;
import static com.rollking.lightcloud.utils.StrUtils.randStr;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */

@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired UserService userService;

  /**
   * @param user 上传提交的用户实体
   * @return 是否登录成功
   */
  @PostMapping(value = "/login")
  public Result login(@RequestBody User user) {
    if (userService.login(user)) return ResultFactory.buildSuccessResult("登录成功");
    else return ResultFactory.buildFailResult("登录失败");
  }

  /**
   * @param user 查询的用户实体
   * @return 是否存在该用户
   */
  @PostMapping(value = "/exist")
  public Result exist(@RequestBody User user) {
    if (user.getEmail() != null) {
      if (userService.getByEmail(user.getEmail()) != null)
        return ResultFactory.buildSuccessResult("用户已存在");
      else return ResultFactory.buildSuccessResult("用户不存在");
    } else if (user.getUsername() != null) {
      if (userService.getByUsername(user.getUsername()) != null)
        return ResultFactory.buildSuccessResult("用户已存在");
      else return ResultFactory.buildSuccessResult("用户不存在");
    }
    return ResultFactory.buildFailResult("用户名和邮箱均为空");
  }

  /**
   * @param user 需要重置密码的用户实体
   * @return 是否修改成功
   */
  @PutMapping(value = "/resetPwd")
  public Result setPwd(@RequestBody User user) {
    if (userService.resetPassword(user)) {
      return ResultFactory.buildSuccessResult("修改成功");
    } else {
      return ResultFactory.buildFailResult("信息为空或未成功传参");
    }
  }

  /**
   * @param user 需要注册的用户实体
   * @return 是否注册成功
   */
  @PostMapping(value = "/register")
  public Result register(@RequestBody User user) {
    int status = userService.register(user);
    switch (status) {
      case 0:
        return ResultFactory.buildFailResult("参数为空");
      case 1:
        return ResultFactory.buildSuccessResult("注册成功");
      case 2:
        return ResultFactory.buildFailResult("该邮箱已注册");
    }
    return ResultFactory.buildFailResult("未知错误");
  }

  /**
   * @param user 需要发送验证码的用户实体
   * @return 发送的验证码
   */
  @PostMapping(value = "/sentCaptcha")
  public Result captcha(@RequestBody User user) {
    String userEmail = user.getEmail();
    userEmail = HtmlUtils.htmlEscape(userEmail);
    String code = randStr(8);
    String message = "\t尊敬的用户您好，感谢您使用轻云服务\n\t您的验证码是:" + code + "\n\t为了保障您帐号的安全性，请在1小时内完成验证。";
    sendEmail(userEmail, message, "轻云 - 验证码");
    return ResultFactory.buildSuccessResult("发送成功", new Captcha(code));
  }

  /**
   * @param email 用户的邮箱地址
   * @return 该用户已使用的云盘用量
   */
  @GetMapping(value = "/getUsage")
  public long usage(@RequestParam String email) {
    return userService.usageMemory(email);
  }

  /**
   * @param email 用户的邮箱地址
   * @return 该用户的昵称
   */
  @GetMapping(value = "/getUserName")
  public String getUserName(String email) {
    return userService.getByEmail(email).getUsername();
  }
}
