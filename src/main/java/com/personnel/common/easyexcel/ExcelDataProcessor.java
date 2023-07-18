package com.personnel.common.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.personnel.common.exception.BaseException;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 处理导入数据格式
 * @param <T>
 */
@Component
public class ExcelDataProcessor<T> {
    private final Validator validator;

    public ExcelDataProcessor(LocalValidatorFactoryBean validatorFactory) {
        this.validator = validatorFactory.getValidator();
    }

    public List<T> processUploadedExcel(InputStream inputStream, Class<T> dataType) {
        DataListener dataListener = new DataListener(validator);
        EasyExcel.read(inputStream, dataType, dataListener).sheet().doRead();
        return dataListener.getDataList();
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
                throw new BaseException("数据格式有误");
            } else {
//                System.out.println("数据校验通过，校验通过的数据如下：");
//                for (T data : dataList) {
//                    System.out.println(data.toString());
//                }
            }
        }

        public List<T> getDataList() {
            return dataList;
        }
    }
}
