package com.personnel.common.dto;

import com.personnel.common.base.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 用户信息删除dto
 */
@Getter
@Setter
@ToString
public class UserInformationDeleteDto extends BaseDto {
    /**
     * id列表
     */
    private List<Long> ids;

}
