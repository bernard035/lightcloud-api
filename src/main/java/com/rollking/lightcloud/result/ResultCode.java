package com.rollking.lightcloud.result;

/*
 * @author Bernard
 * @mail bernard5@qq.com
 * @date 2021-12
 * */
public enum ResultCode {
    SUCCESS(200),
    FAILURE(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}

