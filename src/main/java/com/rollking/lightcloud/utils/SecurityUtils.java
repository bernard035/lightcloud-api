package com.rollking.lightcloud.utils;


/**
 * 安全服务工具类
 *
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12-11
 */
public class SecurityUtils {
    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密后的字符串
     */
    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 密码是否相同
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}

