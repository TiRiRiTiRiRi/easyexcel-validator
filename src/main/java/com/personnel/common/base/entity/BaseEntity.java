package com.personnel.common.base.entity;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 所有Mysql实体的基类
 *
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    protected Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

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
