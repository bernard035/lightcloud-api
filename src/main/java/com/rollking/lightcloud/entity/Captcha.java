package com.rollking.lightcloud.entity;

import lombok.Getter;
import lombok.Setter;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-11
 * */

public class Captcha {

    @Setter
    @Getter
    private String code;

    public Captcha(String code) {
        this.code = code;
    }

}
