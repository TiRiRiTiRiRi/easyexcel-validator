package com.personnel.common.easyexcel;

import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * easyExcel下载文件
 */
public class ExcelDataExporter {

    public static <T> void exportDataToExcel(List<T> dataList, HttpServletResponse response, Class<T> dataType, String fileName) {
        try {
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8"); // 对文件名进行 URL 编码
            response.setContentType("application/vnd.ms-excel"); // 设置响应类型为 Excel
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\""); // 设置响应头，告诉客户端以附件形式下载文件

            OutputStream outputStream = response.getOutputStream();
            EasyExcel.write(outputStream, dataType).sheet("Sheet1").doWrite(dataList);
            outputStream.flush();
            outputStream.close();

            System.out.println("数据成功导出为 Excel 文件");
        } catch (IOException e) {
            System.out.println("导出数据到 Excel 文件失败：" + e.getMessage());
        }
    }
}
