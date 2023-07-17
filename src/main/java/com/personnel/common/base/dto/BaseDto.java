package com.personnel.common.base.dto;

import cn.hutool.core.convert.Convert;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO基类
 *
 */
public class BaseDto implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 转换类型
     *
     * @param clazz<T> 目标类型
     * @return 返回转换后的目标类型对象
     */
    public <T> T convert(Class<T> clazz) {
        return Convert.convert(clazz, this);
    }

}
