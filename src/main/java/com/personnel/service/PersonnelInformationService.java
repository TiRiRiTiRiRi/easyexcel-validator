package com.personnel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.personnel.common.base.qo.PagingQo;
import com.personnel.common.base.vo.PagingVo;
import com.personnel.common.dto.UserInformationDeleteDto;
import com.personnel.common.dto.UserInformationDto;
import com.personnel.common.dto.UserInformationUpdateDto;
import com.personnel.common.vo.PersonnelInformationVo;
import com.personnel.model.PersonnelInformation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface PersonnelInformationService extends IService<PersonnelInformation> {

    /**
     * 保存用户信息
     */
    void saveMessage(UserInformationDto dto);

    /**
     * 保存用户信息
     */
    PagingVo<PersonnelInformationVo> getMessage(PagingQo qo);

    /**
     * 删除信息
     */
    void deleteMessage(UserInformationDeleteDto dto);

    /**
     * 修改信息
     */
    void updateMessage(UserInformationUpdateDto dto);

    /**
     * 导出全部数据
     * @param response
     */
    void exportData(HttpServletResponse response);

    /**
     * 导出模板
     * @param response
     */
    void exportTemplate(HttpServletResponse response);

    void importData(MultipartFile file);
}
