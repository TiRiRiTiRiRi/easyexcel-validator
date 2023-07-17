package com.personnel.common.exception;

import com.personnel.common.result.ResultCodeEnum;

public class BaseException extends RuntimeException {
    // 异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     */
    public BaseException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    /**
     * 通过状态码和错误消息创建异常对象
     * @param message
     * @param code
     */
    public BaseException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
