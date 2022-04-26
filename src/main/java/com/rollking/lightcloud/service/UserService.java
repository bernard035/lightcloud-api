package com.rollking.lightcloud.service;

import com.rollking.lightcloud.dao.UserRepository;
import com.rollking.lightcloud.entity.User;
import com.rollking.lightcloud.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-11
 * */
@Service
public class UserService {
  @Autowired UserRepository userDAO;

  /**
   * @param userEmail 请求的用户邮箱
   * @return 用户是否存在
   */
  public boolean isExist(String userEmail) {
    User user = getByEmail(userEmail);
    return user != null;
  }

  /**
   * @param user 需要保存的用户对象
   */
  public void add(User user) {
    userDAO.save(user);
  }

  /**
   * @param userEmail 请求的用户邮箱
   * @return 用户实体
   */
  public User getByEmail(String userEmail) {
    return userDAO.findByEmail(userEmail);
  }

  /**
   * @param username 请求的用户名
   * @return 返回该用户名的全部实体
   */
  public List<User> getByUsername(String username) {
    return userDAO.findByUsername(username);
  }

  /**
   * @param user 请求的用户
   * @return 注册状态
   */
  public int register(User user) {
    String username = user.getUsername();
    String email = user.getEmail();
    username = HtmlUtils.htmlEscape(username);
    email = HtmlUtils.htmlEscape(email);
    if (username.isEmpty()) return 0;
    if (isExist(email)) return 2;
    user.setUsername(username);
    user.setEmail(email);

    // 处理密码事务
    String password = user.getPassword();
    password = HtmlUtils.htmlEscape(password);
    if (password.isEmpty()) return 0;

    String encodedPasswd = SecurityUtils.encodePassword(password);
    user.setPassword(encodedPasswd);

    userDAO.save(user);

    return 1;
  }

  /**
   * @param user 请求的用户
   * @return 登录是否成功
   */
  public boolean login(User user) {
    String userEmail = user.getEmail();
    userEmail = HtmlUtils.htmlEscape(userEmail);

    String password = user.getPassword();
    password = HtmlUtils.htmlEscape(password);

    User userInDB = userDAO.findByEmail(userEmail);
    return SecurityUtils.matchesPassword(password, userInDB.getPassword());
  }

  /**
   * @param user 请求的用户
   * @return 重置密码是否成功
   */
  public boolean resetPassword(User user) {
    String userEmail = user.getEmail();
    userEmail = HtmlUtils.htmlEscape(userEmail);

    String password = user.getPassword();
    password = HtmlUtils.htmlEscape(password);

    if (userEmail.isEmpty() || password.isEmpty()) return false;

    User userInDB = userDAO.findByEmail(userEmail);
    userInDB.setPassword(SecurityUtils.encodePassword(password));
    userDAO.save(userInDB);
    return true;
  }

  /**
   * @param userEmail 请求的用户邮箱
   * @return 该用户使用了多少容量
   */
  public long usageMemory(String userEmail) {
    userEmail = HtmlUtils.htmlEscape(userEmail);
    User userInDB = userDAO.findByEmail(userEmail);
    return userInDB.getUsed();
  }
}
