package com.personnel.common.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 导出模板工具类
 * 根据@ExcelProperty类下载导出模板
 */
public class ExcelTemplateUtil {

    /**
     * 生成模板Excel文件并通过客户端下载
     *
     * @param response  HttpServletResponse对象
     * @param fileName  下载的文件名
     * @param sheetName 工作表名
     * @param clazz     实体类的Class对象，包含了@ExcelProperty注解
     */
    public static void downloadTemplate(HttpServletResponse response, String fileName, String sheetName, Class<?> clazz) throws IOException {
        // 创建ExcelWriter对象
        ExcelWriter excelWriter = EasyExcel.write(getOutputStream(response, fileName)).build();

        // 设置工作表信息
        WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();

        // 获取实体类中的@ExcelProperty注解信息，并生成表头数据
        List<List<String>> head = generateHeader(clazz);

        // 将表头数据写入工作表
        excelWriter.write(head, writeSheet);

        // 将数据刷新到文件并关闭ExcelWriter
        excelWriter.finish();
    }

    /**
     * 获取输出流，设置响应头信息
     *
     * @param response HttpServletResponse对象
     * @param fileName 下载的文件名
     * @return ServletOutputStream对象
     * @throws IOException IO异常
     */
    private static ServletOutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
        return response.getOutputStream();
    }

    /**
     * 生成表头数据
     *
     * @param clazz 实体类的Class对象，包含了@ExcelProperty注解
     * @return 表头数据列表
     */
    private static List<List<String>> generateHeader(Class<?> clazz) {
        List<List<String>> head = new ArrayList<>();
        List<String> headRow = new ArrayList<>();

        // 通过反射获取实体类中的@ExcelProperty注解信息，并生成表头数据
        // 这里假设实体类中的@ExcelProperty注解标记的字段名为注解值
        // 可以根据实际情况修改生成表头数据的逻辑

        // 获取所有字段
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (excelProperty != null) {
                String value = excelProperty.value()[0];
                headRow.add(value);
            }
        }

        head.add(headRow);
        return head;
    }
}
