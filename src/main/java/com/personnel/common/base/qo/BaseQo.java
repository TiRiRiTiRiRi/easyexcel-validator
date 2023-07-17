package com.personnel.common.base.qo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * QO基类
 *
 */
@Data
@Accessors(chain = true)
public class BaseQo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 单次查询结果数量限制，分页查询不受此限制
     */
    private int resultLimit = 1000;
}
