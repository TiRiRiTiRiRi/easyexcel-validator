package com.personnel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
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
import com.personnel.common.easyexecl.ExcelDataExporter;
import com.personnel.common.easyexecl.ExcelDataProcessor;
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
        this.save(convert);
    }

    @Override
    public PagingVo<PersonnelInformationVo> getMessage(PagingQo qo) {
        IPage<PersonnelInformation> page = new Page<>(qo.getPageNo(), qo.getPageSize());
        IPage<PersonnelInformation> pageData = this.lambdaQuery().page(page);
        return new PagingVo<>(pageData).convert(PersonnelInformationVo.class);
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
        List<UserInformationDto> convertList = list.stream().map(
                entity ->
                        new UserInformationDto()
                                .setName(entity.getName())
                                .setNameSpelling(entity.getNameSpelling())
                                .setGender(entity.getGender())
                                .setIdentityCardType(entity.getIdentityCardType())
                                .setIdentityCardNumber(entity.getIdentityCardNumber())
                                .setBirthday(entity.getBirthday())
                                .setPhone(entity.getPhone())
                                .setEmail(entity.getEmail())

        ).toList();
        createExcel(response, convertList, "总数据");
    }

    @Override
    public void exportTemplate(HttpServletResponse response) {
        createExcel(response, CollUtil.newArrayList(new UserInformationDto()), "模板");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> importData(MultipartFile file) {
        try {
            System.out.println("上传的文件名：" + file.getOriginalFilename());
            excelDataProcessor.processUploadedExcel(file.getInputStream(), UserInformationExcelDto.class);
            return Result.success("上传成功");
        } catch (Exception e) {
            throw new BaseException("上传失败");
        }
    }

    @SneakyThrows
    private void createExcel(HttpServletResponse response, List dataList, String fileName) { // 查询导出订单数据
        if (CollUtil.isEmpty(dataList)) {
            throw new BaseException("暂无数据！");
        }
        //在内存操作，写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("name", "姓名");
        writer.addHeaderAlias("nameSpelling", "姓名全拼");
        writer.addHeaderAlias("gender", "性别");
        writer.addHeaderAlias("identityCardType", "身份证件类型");
        writer.addHeaderAlias("identityCardNumber", "身份证件号码");
        writer.addHeaderAlias("birthday", "出生日期");
        writer.addHeaderAlias("phone", "手机号码");
        writer.addHeaderAlias("email", "电子邮箱");
        //默认配置
        writer.write(dataList, true);
        //设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        //Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        //将Writer刷新到OutPut
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }


}