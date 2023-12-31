package com.personnel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.personnel.common.base.entity.BaseEntity;
import com.personnel.common.base.qo.PagingQo;
import com.personnel.common.base.vo.PagingVo;
import com.personnel.common.dto.UserInformationDeleteDto;
import com.personnel.common.dto.UserInformationDto;
import com.personnel.common.dto.UserInformationExcelDto;
import com.personnel.common.dto.UserInformationUpdateDto;
import com.personnel.common.easyexcel.ExcelDataExporter;
import com.personnel.common.easyexcel.ExcelDataProcessor;
import com.personnel.common.easyexcel.ExcelTemplateUtil;
import com.personnel.common.exception.BaseException;
import com.personnel.common.result.Result;
import com.personnel.common.vo.PersonnelInformationVo;
import com.personnel.mapper.PersonnelInformationMapper;
import com.personnel.model.PersonnelInformation;
import com.personnel.service.PersonnelInformationService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonnelInformationServiceImpl extends ServiceImpl<PersonnelInformationMapper, PersonnelInformation>
        implements PersonnelInformationService {

    @Autowired
    private ExcelDataProcessor excelDataProcessor;

    /**
     * 一次性保存行数
     */
    private final static int SAVE_ROW = 100;

    @Override
    @Transactional
    public void saveMessage(UserInformationDto dto) {
        PersonnelInformation convert = dto.convert(PersonnelInformation.class);
        this.save(convert.setNameSpelling(convert.getNameSpelling().toLowerCase()));
    }

    @Override
    public PagingVo<PersonnelInformationVo> getMessage(PagingQo qo) {
        System.out.println("qo = " + qo);

        Page<PersonnelInformation> rowPage = new Page<>(qo.getPageNo(), qo.getPageSize());
        LambdaQueryWrapper<PersonnelInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PersonnelInformation::getCreateTime);
        rowPage = this.baseMapper.selectPage(rowPage, queryWrapper);
        return new PagingVo<>(rowPage).convert(PersonnelInformationVo.class);
    }

    @Override
    public void deleteMessage(UserInformationDeleteDto dto) {
        if (CollUtil.isEmpty(dto.getIds())) {
            throw new BaseException("传入的删除数据为空");
        }
        this.removeBatchByIds(dto.getIds());
    }

    @Override
    public void updateMessage(UserInformationUpdateDto dto) {
        if (dto.getId() == null) {
            throw new BaseException("id参数不能为空");
        }
        this.lambdaUpdate()
                .eq(PersonnelInformation::getId, dto.getId())
                .set(PersonnelInformation::getName, dto.getName())
                .set(PersonnelInformation::getNameSpelling, dto.getNameSpelling())
                .set(PersonnelInformation::getGender, dto.getGender())
                .set(PersonnelInformation::getIdentityCardType, dto.getIdentityCardType())
                .set(PersonnelInformation::getIdentityCardNumber, dto.getIdentityCardNumber())
                .set(PersonnelInformation::getBirthday, dto.getBirthday())
                .set(PersonnelInformation::getPhone, dto.getPhone())
                .set(PersonnelInformation::getEmail, dto.getEmail())
                .update();
    }

    @SneakyThrows
    @Override
    public void exportData(HttpServletResponse response) {
        List<PersonnelInformation> list = this.lambdaQuery().orderByDesc(BaseEntity::getCreateTime).list();
        System.out.println("list = " + list);
        List<UserInformationExcelDto> convertList = list.stream().map(
                entity -> {
                    UserInformationExcelDto userInformationExcelDto = new UserInformationExcelDto();
                    userInformationExcelDto.setName(entity.getName());
                    userInformationExcelDto.setNameSpelling(entity.getNameSpelling());
                    userInformationExcelDto.setGender(entity.getGender());
                    userInformationExcelDto.setIdentityCardType(entity.getIdentityCardType());
                    userInformationExcelDto.setIdentityCardNumber(entity.getIdentityCardNumber());
                    userInformationExcelDto.setBirthday(entity.getBirthday());
                    userInformationExcelDto.setPhone(entity.getPhone());
                    userInformationExcelDto.setEmail(entity.getEmail());
                    return userInformationExcelDto;
                }
        ).toList();
        ExcelDataExporter.exportDataToExcel(convertList, response, UserInformationExcelDto.class, "总数据");
    }

    @SneakyThrows
    @Override
    public void exportTemplate(HttpServletResponse response) {
        ExcelTemplateUtil.downloadTemplate(response, "模板", "sheet1", UserInformationExcelDto.class);
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> importData(MultipartFile file) {
        System.out.println("上传的文件名：" + file.getOriginalFilename());
        // 获取excel数据并校验
        List<UserInformationExcelDto> list = excelDataProcessor.processUploadedExcel(file.getInputStream(), UserInformationExcelDto.class);
        // 获取要插入的数据
        List<PersonnelInformation> importList = new ArrayList<>();
        int i = 0;
        for (UserInformationExcelDto userInformationExcelDto : list) {
            // 添加数据
            PersonnelInformation convert = userInformationExcelDto.convert(PersonnelInformation.class);
            // 将拼音转换为小写
            convert.setNameSpelling(convert.getNameSpelling().toLowerCase());
            importList.add(convert);
            // 批量保存防止oom
            if (++i >= SAVE_ROW) {
                // 批量注册进数据库
                this.saveBatch(importList);
                i = 0;
                CollUtil.clear(importList);
            }
        }
        if (CollUtil.isNotEmpty(importList)) {
            this.saveBatch(importList);
        }
        return Result.success("上传成功");
    }

}
