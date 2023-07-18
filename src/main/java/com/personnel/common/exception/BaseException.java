package com.personnel.common.exception;

import com.personnel.common.result.Result;
import com.personnel.common.result.ResultCodeEnum;

public class BaseException extends RuntimeException {
    // 异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message 错误消息
     */
    public BaseException(String message) {
        super(message);
        this.code = ResultCodeEnum.FAIL.getCode();
    }

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message 错误消息
     * @param code    状态码
     */
    public BaseException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum 统一返回类中的枚举对象
     */
    public BaseException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    /**
     * 转换为统一返回类的结果对象
     *
     * @return 统一返回类的结果对象
     */
    public Result<Object> toResult() {
        return Result.fail(this.getMessage());
    }

    @Override
    public String toString() {
        return "BaseException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
