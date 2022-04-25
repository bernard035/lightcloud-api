package com.rollking.lightcloud.utils;

import org.apache.commons.mail.HtmlEmail;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */
public class MailUtils {
    public static boolean sendEmail(String address, String message, String title) {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName("smtp.163.com");
            email.setCharset("UTF-8");
            email.addTo(address);// 收件地址

            email.setFrom("jiang1057910204@163.com", "LightCloud");  //  邮箱地址 用户名

            email.setAuthentication("jiang1057910204@163.com", "BMWFWPBOIMQYGICG");  // 邮箱地址 客户端授权码

            email.setSubject(title); // 邮件名
            email.setMsg(message); // 邮件内容

            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
