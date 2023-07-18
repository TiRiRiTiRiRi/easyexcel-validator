package com.personnel.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.personnel.common.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class PersonnelInformation extends BaseEntity implements Serializable {

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
