package com.personnel.common.vo;

import com.personnel.common.base.vo.BaseVo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 人员信息返回vo
 */
@Data
@Accessors(chain = true)
public class PersonnelInformationVo extends BaseVo {

    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 姓名全拼
     */
    private String nameSpelling;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证件类型：居民身份证、士官证、学生证、驾驶证、护照、港澳通行证
     */
    private String identityCardType;

    /**
     * 身份证件号码
     */
    private String identityCardNumber;

    /**
     * 出生日期
     */
    private Date birthday;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;
}
