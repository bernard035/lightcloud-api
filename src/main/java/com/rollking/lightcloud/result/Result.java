package com.rollking.lightcloud.result;


import lombok.Getter;
import lombok.Setter;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-11
 * */
@Getter
@Setter
public class Result {

    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}

