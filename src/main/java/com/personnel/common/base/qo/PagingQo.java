package com.personnel.common.base.qo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PagingQo extends BaseQo {

    /**
     * 页码
     */
    @NotNull(message = "页码不可为空")
    @Range(min = 1, max = 1000, message = "页码值允许的区间为1~1000")
    private int pageNo = 1;

    /**
     * 每页数量
     */
    @NotNull(message = "每页数量不可为空")
    @Range(min = 1, max = 500, message = "每页数量值允许的区间为1~500")
    private int pageSize = 20;
}
