package com.personnel.controller;

import com.personnel.common.base.qo.PagingQo;
import com.personnel.common.base.vo.PagingVo;
import com.personnel.common.dto.UserInformationDeleteDto;
import com.personnel.common.dto.UserInformationDto;
import com.personnel.common.dto.UserInformationUpdateDto;
import com.personnel.common.result.Result;
import com.personnel.common.vo.PersonnelInformationVo;
import com.personnel.model.PersonnelInformation;
import com.personnel.service.PersonnelInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/personalInformation")
public class PersonnelInformationController {
    @Autowired
    private PersonnelInformationService personnelInformationService;

    /**
     * 保存用户信息
     */
    @PostMapping("/new")
    public Result<Object> saveMessage(@RequestBody @Validated UserInformationDto dto) {
        personnelInformationService.saveMessage(dto);
        return Result.success();
    }

    /**
     * 获取用户信息
     */
    @PostMapping("/getMessage")
    public Result<PagingVo<PersonnelInformationVo>> getMessage(@RequestBody @Validated PagingQo qo) {
        return Result.success(personnelInformationService.getMessage(qo));
    }

    /**
     * 删除信息
     */
    @PostMapping("/delete")
    public Result<Object> delete(@RequestBody UserInformationDeleteDto dto) {
        personnelInformationService.deleteMessage(dto);
        return Result.success();
    }

    /**
     * 修改信息
     */
    @PostMapping("/update")
    public Result<Object> update(@RequestBody @Validated UserInformationUpdateDto dto) {
        personnelInformationService.updateMessage(dto);
        return Result.success();
    }

    /**
     * 导出全部数据
     *
     * @param response
     */
    @GetMapping(path = "/export")
    public Result<Object> export(HttpServletResponse response) {
        personnelInformationService.exportData(response);
        return Result.success();
    }

    /**
     * 导出模板
     *
     * @param response
     */
    @GetMapping(path = "/exportTemplate")
    public Result<Object> exportTemplate(HttpServletResponse response) {
        personnelInformationService.exportTemplate(response);
        return Result.success();
    }

    /**
     * 导入数据
     */
    @PostMapping("/import")
    @ResponseBody
    public Result<Object> importData(MultipartFile file) {
        personnelInformationService.importData(file);
        return Result.success();
    }
}
