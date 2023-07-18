package com.personnel.common.easyexecl;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.personnel.mapper.PersonnelInformationMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class ExcelDataProcessor<T> {
    private final Validator validator;
    private final BaseMapper baseMapper;

    public ExcelDataProcessor(LocalValidatorFactoryBean validatorFactory, BaseMapper<T> baseMapper) {
        this.validator = validatorFactory.getValidator();
        this.baseMapper = baseMapper;
    }



    public void processUploadedExcel(InputStream inputStream, Class<T> dataType) {
        EasyExcel.read(inputStream, dataType, new DataListener(validator))
                .sheet().doRead();
    }
    public class DataListener extends AnalysisEventListener<T> {
        private final List<T> dataList = new ArrayList<>();
        private final List<String> errorList = new ArrayList<>();
        private final Validator validator;

        public DataListener(Validator validator) {
            this.validator = validator;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            Set<ConstraintViolation<T>> violations = validator.validate(data);
            if (!violations.isEmpty()) {
                for (ConstraintViolation<T> violation : violations) {
                    errorList.add(violation.getMessage());
                }
            } else {
                dataList.add(data);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            if (!errorList.isEmpty()) {
                System.out.println("数据校验不通过，错误信息如下：");
                for (String error : errorList) {
                    System.out.println(error);
                }
            } else {
                System.out.println("数据校验通过，校验通过的数据如下：");
                for (T data : dataList) {
                    System.out.println(data.toString());
                    // 将数据保存到数据库
                        baseMapper.insert(data);
                }
            }
        }
    }
}

