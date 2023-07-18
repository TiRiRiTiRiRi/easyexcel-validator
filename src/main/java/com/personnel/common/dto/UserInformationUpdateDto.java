package com.personnel.common.dto;

import com.personnel.common.base.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * 保存用户信息dto
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserInformationUpdateDto extends BaseDto {

    private Long id;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 姓名全拼
     */
    @NotBlank(message = "姓名全拼不能为空")
    private String nameSpelling;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空")
    private String gender;

    /**
     * 身份证件类型：居民身份证、士官证、学生证、驾驶证、护照、港澳通行证
     */
    @NotBlank(message = "身份证件类型不能为空")
    private String identityCardType;

    /**
     * 身份证件号码
     */
    @NotBlank(message = "身份证件号码不能为空")
    private String identityCardNumber;

    /**
     * 出生日期
     */
    @NotNull(message = "生日不能为空")
    private Date birthday;

    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空")
    @Length(min = 11, max = 11, message = "手机号只能为11位")
    @Pattern(regexp = "^[1][0-9]{10}$", message = "手机号格式有误")
    private String phone;

    /**
     * 电子邮箱
     */
    @NotBlank(message = "邮件不能为空")
    @Pattern(regexp ="^[A-Za-z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$",message = "邮箱格式有误")
    private String email;

}