package com.personnel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
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
import com.personnel.common.dto.UserInformationUpdateDto;
import com.personnel.common.exception.BaseException;
import com.personnel.common.vo.PersonnelInformationVo;
import com.personnel.mapper.PersonnelInformationMapper;
import com.personnel.model.PersonnelInformation;
import com.personnel.service.PersonnelInformationService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonnelInformationServiceImpl extends ServiceImpl<PersonnelInformationMapper, PersonnelInformation>
        implements PersonnelInformationService {


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

    @SneakyThrows
    @Override
    @Transactional
    public void importData(MultipartFile file) {
        // 文件处理成io流
        InputStream in = file.getInputStream();
        // io流给ExcelReader
        ExcelReader excelReader = ExcelUtil.getReader(in);
        // 忽略第一行头(第一行是中文的情况),直接读取表的内容
        List<List<Object>> list = excelReader.read(1);
        ArrayList<PersonnelInformation> importList = new ArrayList<>();
        int i = 0;
        for (List<Object> row : list) {
            importList.add(new PersonnelInformation()
                    .setName(row.get(0).toString())
                    .setNameSpelling(row.get(1).toString())
                    .setGender(row.get(2).toString())
                    .setIdentityCardType(row.get(3).toString())
                    .setIdentityCardNumber(row.get(4).toString())
                    .setBirthday(DateUtil.parse(row.get(5).toString()))
                    .setPhone(row.get(6).toString())
                    .setEmail(row.get(7).toString())
            );
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
